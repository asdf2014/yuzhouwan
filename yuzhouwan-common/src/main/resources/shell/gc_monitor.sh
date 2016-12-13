#!/bin/sh
# */1 * * * * bash /home/hbase/hbase-monitor/gc_monitor.sh "/home/hbase/logs/regionservergc.log" "/data/hbase_monitor/dumps" "10" "29140"
# need create $DUMP_OUTPUT_PATH firstly

GC_LOG_FILE="$1"
DUMP_OUTPUT_PATH="$2"
GC_TIME_THRESHOLD_SECOND="$3"
PROCESS_ID="$4"

if [ -z "$GC_LOG_FILE" -o -z "$DUMP_OUTPUT_PATH" ]; then
    echo "Usage: gc_monitor.sh <gc log file> <dump output path>"
    echo 'Example: gc_monitor.sh "/home/hbase/logs/regionservergc.log" "/home/hbase/dump"'
    exit
fi

longGc() {
    echo "Begin monitor..."
    if [ "$GC_LOG_FILE" = "" ]; then
        return 0
    fi
    echo "Gc file is '$GC_LOG_FILE'"
    # tail -1 /home/hbase/logs/regionservergc.log | awk '{print $13}' | cut -d "=" -f 2
    userTime=`tail -1 "$GC_LOG_FILE" | awk '{print $13}' | cut -d "=" -f 2`

    if [ -z ${userTime} ]; then
        return 1
    fi

    echo "Current Gc User Time: ${userTime}"
    if [ $(echo "${GC_TIME_THRESHOLD_SECOND}<${userTime}" | bc) -eq 1 ]; then
        return 0
    else
        return 2
    fi
}

longGc
checkResult=$?
if [ ${checkResult} -eq 0 ]; then
    # 2016_12_13-17:01:11
    NOW=`date '+%Y_%m_%d-%H:%M:%S'`
    if [ -z ${DUMP_OUTPUT_PATH} ]; then
        DUMP_PATH=${DUMP_OUTPUT_PATH}
    else
        DUMP_PATH=`printf "%s%s%s%s" "${DUMP_OUTPUT_PATH}" "/" "pid_${PROCESS_ID}_date_${NOW}" ".hprof"`
    fi
    echo "Now dump: ${DUMP_PATH}"
    # nohup jmap -dump:live,format=b,file=/data/hbase/dumps/2016_12_13-19:39:09.hprof 29140 &
    DUMP_EXEC=`printf "%s%s%s" "-dump:live,format=b,file=" "${DUMP_PATH}" " ${PROCESS_ID}"`
    jmap ${DUMP_EXEC}
    echo "Exec: jmap ${DUMP_EXEC}"
elif [ ${checkResult} -eq 2 ]; then
    echo "Process is healthy."
else
    echo "Cannot catch gc time!"
fi