#!/bin/sh
# */1 * * * * bash /home/yuzhouwan/yuzhouwan-monitor/gc_monitor.sh "/home/yuzhouwan/logs/regionservergc.log" "/data/yuzhouwan/dumps" "5" "29140"
# need create $DUMP_OUTPUT_PATH firstly

GC_LOG_FILE="$1"
DUMP_OUTPUT_PATH="$2"
GC_TIME_THRESHOLD_SECOND="$3"
PROCESS_ID="$4"

if [ -z "$GC_LOG_FILE" -o -z "$DUMP_OUTPUT_PATH"  -o -z "$GC_TIME_THRESHOLD_SECOND"  -o -z "$PROCESS_ID" ]; then
    echo "Usage: gc_monitor.sh <gc log file> <dump output path> <gc time threshold second> <process id>"
    echo 'Example: gc_monitor.sh "/home/yuzhouwan/logs/regionservergc.log" "/home/yuzhouwan/dumps" "5" "29140"'
    exit
fi

longGc() {
    echo "Begin monitor..."
    if [ "$GC_LOG_FILE" = "" ]; then
        return 0
    fi
    echo "Gc file is '$GC_LOG_FILE'"
    # tail -1 /home/yuzhouwan/logs/regionservergc.log | awk '{print $13}' | cut -d "=" -f 2
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
    # jmap -dump:live,format=b,file=/data/yuzhouwan/dumps/pid_29140_date_2016_12_14-09:26:37.hprof 29140
    DUMP_EXEC=`printf "%s%s%s" "-dump:live,format=b,file=" "${DUMP_PATH}" " ${PROCESS_ID}"`
    jmap ${DUMP_EXEC}
    echo "Exec: jmap ${DUMP_EXEC}"
elif [ ${checkResult} -eq 2 ]; then
    echo "Process is healthy."
else
    echo "Cannot catch gc time!"
fi