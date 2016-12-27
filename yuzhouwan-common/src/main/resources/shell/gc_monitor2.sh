#!/bin/sh
# nohup bash /home/hbase/hbase-monitor/gc_monitor2.sh "/data01/hbase/dumps" "HRegionServer" "72" "8" >> /data01/hbase/gc_monitor2.log &
# need create $DUMP_OUTPUT_PATH firstly

DUMP_OUTPUT_PATH="$1"
PROCESS_NAME="$2"
OLD_PERCENT_THRESHOLD="$3"
OVER_THRESHOLD_COUNT="$4"

if [ -z "$DUMP_OUTPUT_PATH" -o -z "$PROCESS_NAME" -o -z "$OLD_PERCENT_THRESHOLD"  -o -z "$OVER_THRESHOLD_COUNT" ]; then
    echo "Usage: gc_monitor2.sh <dump output path> <process name> <old generation percent> <overtop threshold second>"
    echo 'Example: gc_monitor2.sh "/data01/hbase/dumps" "HRegionServer" "75" "10"'
    exit
fi

if [ -d "$DUMP_OUTPUT_PATH" ]; then
    echo "$DUMP_OUTPUT_PATH is exist."
else
    echo "$DUMP_OUTPUT_PATH is not exist!";
    mkdir -p ${DUMP_OUTPUT_PATH}
    echo "${DUMP_OUTPUT_PATH} is created."
fi

PROCESS_ID=-1

HOST_NAME=`hostname`
OLD_IFS="$IFS"
IFS="-"
eval SIMPLE_HOSTNAME=(${HOST_NAME})
IFS="$OLD_IFS"
echo "Now Machine: ${SIMPLE_HOSTNAME[0]}"

JSTAT_LOG_FILE="${DUMP_OUTPUT_PATH}"/gc_monitor2_jstat.log
jstatMonitor() {
    if [ ! -f "${JSTAT_LOG_FILE}" ]; then
        echo "Jstat log file: ${JSTAT_LOG_FILE} is not exist!"
        pkill jstat
    fi
    jstatMonitorCount=`ps -ef | grep "jstat -gcutil" | grep -v grep | grep "${PROCESS_ID}" | wc -l`
    if [ $(echo "${jstatMonitorCount}==0" | bc) -eq 1  ]; then
        PROCESS_ID=`jps | grep "${PROCESS_NAME}" | awk '{print $1}'`
        echo "[JSTAT]: now jstat process number: ${jstatMonitorCount}, then recreate jstat process..."
        jstat -gcutil ${PROCESS_ID} 500 >> "${JSTAT_LOG_FILE}" &
        echo "[EXEC]: jstat -gcutil ${PROCESS_ID} 1000 >> "${JSTAT_LOG_FILE}" &"
    else
        echo "[JSTAT]: jstat process is healthy..."
    fi
}

# global vars
GLOBAL_COOL_COUNT=0
GLOBAL_COOL_LIMIT_COUNT=1

GLOBAL_PRE_OLD_PERCENT=-1
GLOBAL_HIGH_GROWTH_COUNT=0
GLOBAL_HIGH_GROWTH_LIMIT_COUNT=5

GLOBAL_JSTACK_LIMIT_COUNT=3

GLOBAL_LAST_GC_DATE=`date '++%Y%m%d%H%M%S'`
LONG_GC_MESSAGE_TITLE="Long GC Warning"


longGc() {
    PROCESS_ID=`jps | grep "${PROCESS_NAME}" | awk '{print $1}'`
    echo "Process: ${PROCESS_NAME}, and PID: ${PROCESS_ID}"
    if [ "$DUMP_OUTPUT_PATH" = "" -o "$OLD_PERCENT_THRESHOLD" = ""  -o "$PROCESS_ID" = "" ]; then
        exit
    fi
    echo "Begin Checking..."
    jstatMonitor
    thresholdCount=0
    while [ $(echo "${thresholdCount}<${OVER_THRESHOLD_COUNT}" | bc) -eq 1 ]; do
        # jstat -gcutil 16125
        # S0     S1     E      O      P     YGC     YGCT    FGC    FGCT     GCT
        # 0.00  96.03  92.77  49.71  34.07  11855  803.209   255   15.829  819.039
        oldPercent=`tail -1 "${JSTAT_LOG_FILE}" | awk '{print $4}'`
        NOW=`date '+%Y-%m-%d %H:%M:%S'`
        if [ -z ${oldPercent} ]; then
            return 1
        fi
        echo "${NOW} Current Old Generation used percent: ${oldPercent} %"

        # speed up
        if [ $(echo "${GLOBAL_PRE_OLD_PERCENT}!=-1" | bc) -eq 1 ];then
            incrPercent=$(echo "${oldPercent}-${GLOBAL_PRE_OLD_PERCENT}" | bc)
            echo "Growth Rate of Old Generation used percent: ${incrPercent}%"
            if [ $(echo "${incrPercent}>=2" | bc) -eq 1 -a $(echo "${incrPercent}<3" | bc) -eq 1 ]; then
                thresholdCount=$(echo "${thresholdCount}+2" | bc)
            elif [ $(echo "${incrPercent}>=3" | bc) -eq 1 -a $(echo "${incrPercent}<5" | bc) -eq 1 ]; then
                thresholdCount=$(echo "${thresholdCount}+5" | bc)
                GLOBAL_HIGH_GROWTH_COUNT=$(echo "${GLOBAL_HIGH_GROWTH_COUNT}+1" | bc)
                echo "High Growth of Old Generation! [${GLOBAL_HIGH_GROWTH_COUNT}/${GLOBAL_HIGH_GROWTH_LIMIT_COUNT}]"
            elif [ $(echo "${incrPercent}>=5" | bc) -eq 1 ]; then
                thresholdCount=$(echo "${thresholdCount}+10" | bc)
                GLOBAL_HIGH_GROWTH_COUNT=$(echo "${GLOBAL_HIGH_GROWTH_COUNT}+2" | bc)
                echo "High Growth of Old Generation! [${GLOBAL_HIGH_GROWTH_COUNT}/${GLOBAL_HIGH_GROWTH_LIMIT_COUNT}]"
            fi
        fi
        GLOBAL_PRE_OLD_PERCENT=${oldPercent}

        if [ $(echo "${oldPercent}>${OLD_PERCENT_THRESHOLD}" | bc) -eq 1 ]; then
            thresholdCount=$(echo "${thresholdCount}+1" | bc)
        elif [ $(echo "${oldPercent}>(${OLD_PERCENT_THRESHOLD}*2/3)" | bc) -eq 1 -a $(echo "${GLOBAL_HIGH_GROWTH_COUNT}>=${GLOBAL_HIGH_GROWTH_LIMIT_COUNT}" | bc) -eq 1 ]; then
            # 72 * 2 / 3 = 48%
            # 5 <= 1 + 1 + 1 + 1 + 1 / 1 + 1 + 1 + 2 / 1 + 1 + 2 + 2 / 1 + 2 + 2 / 2 + 2 + 2
            echo "High Growth of Old Generation: ${GLOBAL_HIGH_GROWTH_COUNT}>=${GLOBAL_HIGH_GROWTH_LIMIT_COUNT}!"
            return 0
        else
            return 2
        fi
        echo "Overload threshold: (${thresholdCount}/${OVER_THRESHOLD_COUNT})"
        sleep 1
    done
    if [ $(echo "${thresholdCount}==${OVER_THRESHOLD_COUNT}" | bc) -eq 1 ]; then
        return 0
    else
        return 2
    fi
}

dealAlert() {
    longGc
    checkResult=$?
    if [ ${checkResult} -eq 0 ]; then
        echo "Process ID [${PROCESS_ID}] could happening long GC!!!"

        if [ ${GLOBAL_COOL_COUNT} -ne 0 -a $(( `date '+%Y%m%d%H%M%S'` - $GLOBAL_LAST_GC_DATE )) -gt $(echo "${OVER_THRESHOLD_COUNT}*${GLOBAL_COOL_LIMIT_COUNT}*2" | bc) ]; then
            GLOBAL_LAST_GC_DATE=`date '++%Y%m%d%H%M%S'`
            GLOBAL_COOL_COUNT=0
        fi

        if [ ${GLOBAL_COOL_COUNT} -eq 0 ]; then
            jstackProcess &
            jmapProcess &
            sendMessage
        fi
        GLOBAL_COOL_COUNT=$(echo "${GLOBAL_COOL_COUNT}+1" | bc)
        if [ $(echo "${GLOBAL_COOL_COUNT}==${GLOBAL_COOL_LIMIT_COUNT}" | bc) -eq 1 ]; then
            GLOBAL_COOL_COUNT=0
            GLOBAL_HIGH_GROWTH_COUNT=0
            skillCooling
        fi
    elif [ ${checkResult} -eq 2 ]; then
        echo "Process is healthy."
    else
        echo "Cannot catch Old Generation used size!"
    fi
}

jmapProcess() {
    buildOutputPath basicPath
    DUMP_PATH=`printf "%s%s" "${basicPath}" ".hprof"`
    echo "Now dump into: ${DUMP_PATH}"
    # jmap -dump:live,format=b,file=/data/hbase/dumps/pid_29140_date_2016_12_14-09:26:37.hprof 29140
    # cannot use live, because the option will make a gc
    DUMP_EXEC=`printf "%s%s%s" "-dump:format=b,file=" "${DUMP_PATH}" " ${PROCESS_ID}"`
    echo "[EXEC]: jmap ${DUMP_EXEC} &, Date: ${NOW}"
    jmap ${DUMP_EXEC} &
}

jstackProcess() {
    jstackCount=0
    while [ $(echo "${jstackCount}<${GLOBAL_JSTACK_LIMIT_COUNT}" | bc) -eq 1 ]; do
        buildOutputPath basicPath
        JSTACK_PATH=`printf "%s%s" "${basicPath}" ".jstack"`
        echo "[EXEC]: jstack -l "${PROCESS_ID}" >> "${JSTACK_PATH}", Date: ${NOW}"
        jstack -l "${PROCESS_ID}" >> "${JSTACK_PATH}"
        jstackCount=$(echo "${jstackCount}+1" | bc)
        sleep 2
    done
}

buildOutputPath() {
    # 2016_12_13-17:01:11
    NOW=`date '+%Y%m%d%H%M%S'`
    if [ -z ${DUMP_OUTPUT_PATH} ]; then
        BASIC_PATH=`printf "%s%s%s" "./" "${SIMPLE_HOSTNAME[0]}" "_${PROCESS_ID}_${NOW}"`
    else
        BASIC_PATH=`printf "%s%s%s%s" "${DUMP_OUTPUT_PATH}" "/" "${SIMPLE_HOSTNAME[0]}" "_${PROCESS_ID}_${NOW}"`
    fi
    eval $1="${BASIC_PATH}"
}

sendMessage() {
    echo "Sending Alert Message..."
    message="[Machine HostName]: `hostname` \r\n [Process ID]: ${PROCESS_ID} \r\n [Old Generation Percent Threshold]: ${OLD_PERCENT_THRESHOLD}% \r\n [Old Generation Percent Now]: ${oldPercent}% \r\n [The length of time beyond the threshold]: ${OVER_THRESHOLD_COUNT}s \r\n [Dump file path]: ${DUMP_PATH}"
    echo -e "Title: ${LONG_GC_MESSAGE_TITLE} \n Message: ${message}"
    # maybe should setup SMTP service
    echo "${message}" | mail -s "${LONG_GC_MESSAGE_TITLE}" 1571805553@qq.com
}

skillCooling() {
    echo "Skill cooling..."
    sleepCountDown=61
    while [ $(echo "${sleepCountDown}>1" | bc) -eq 1 ]; do
        sleepCountDown=$(echo "${sleepCountDown}-1" | bc)
        echo "Skill cooling... ${sleepCountDown} second"
        sleep 1
    done
    echo "Continue."
}

echo "Begin monitor..."
while true; do
    dealAlert
    sleep 1
done
echo "All done."