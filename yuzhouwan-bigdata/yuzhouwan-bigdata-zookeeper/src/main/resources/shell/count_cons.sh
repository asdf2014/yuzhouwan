#!/usr/bin/env bash

# bash /home/zookeeper/zk-monitor/count_cons.sh "2181" "3"

tmpPath="/home/zookeeper/zk-monitor/cons"
clientPort="$1"
topN="$2"

if [ -z "$clientPort" -o -z "$topN" ]; then
    echo "bash /home/zookeeper/zk-monitor/count_watch.sh <clientPort> <topN>"
    echo 'bash /home/zookeeper/zk-monitor/count_watch.sh "2181" "3"'
    exit
fi

command -v nc >/dev/null 2>&1 || {
    echo >&2 "I require nc but it's not installed. Try install...";
    yum install nc;
    command -v nc >/dev/null 2>&1 || { echo >&2 "I require nc but it's still cannot be installed. Aborting..."; exit 1; }
}

build_cons() {
    mkdir -p ${tmpPath}
    tmp=${tmpPath}/cons.`date '+%Y%m%d%H%M%S'`
    echo "Tmp: ${tmp}"
    echo cons | nc localhost ${clientPort} > ${tmp}
}

parse_cons() {
    # 12 10.101.4.41
    # 11 10.101.6.10
    # 10 10.101.6.18
    echo -e "Top${topN}:"
    cat ${tmp} | sed 's/:.*//g' | sed 's/.*\///g' | sort | uniq -c | sort -k1 -n -r | head -${topN}
}

build_cons
parse_cons
