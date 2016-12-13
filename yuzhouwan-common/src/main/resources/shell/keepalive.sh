#!/bin/sh
# */1 * * * * bash /home/yuzhouwan/keepalive.sh "yuzhouwan-1.0.0-jar-with-dependencies.jar" "nohup java -jar /home/yuzhouwan/yuzhouwan-1.0.0-jar-with-dependencies.jar >> /home/yuzhouwan/logs/nohup.log 2>&1 &"

check="$1"
alive="$2"
threshold=3

if [ -z "$check" -o -z "$alive" ]; then
    echo "Usage: keepalive.sh '<process>' '<execCommand>'"
    echo "Example: keepalive.sh 'httpd' '/etc/init.d/httpd start'"
    exit 1
fi

keepAlive() {
    echo "Begin keep alive..."
    if [ "$check" == "" -o "$alive" == "" ]; then
        return 0
    fi
    echo "Check command is '$check' and '$alive'"
    processNum=`ps -ef | grep "$check" | grep -v "grep" | wc -l`

    echo "Current Process num: $processNum"
    if [ ${processNum} -lt ${threshold} ]; then
        return 0
    else
        return 1
    fi
}

keepAlive
checkResult=$?
if [ ${checkResult} -eq 0 ]; then
    killall -9 ${check}
    echo "Now execute: $alive"
    exec ${alive} &
else
    echo "Process is running..."
fi