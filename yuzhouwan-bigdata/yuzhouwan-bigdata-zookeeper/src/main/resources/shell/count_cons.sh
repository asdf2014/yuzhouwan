#!/usr/bin/env bash

cd `dirname $0`
source ~/.bashrc

tmpPath="/tmp/zookeeper/zk-monitor/cons"
clientPort=`cat ../conf/zoo.cfg | grep clientPort | sed 's/.*=//g'`
topN="$1"

if [ -z "$clientPort" -o -z "$topN" ]; then
    echo "Usage:"
    echo -e "\t bash /home/zookeeper/software/zookeeper/tools/count_watch.sh <topN>"
    echo -e '\t bash /home/zookeeper/software/zookeeper/tools/count_watch.sh "3"\n'
    if [ -z "$topN" ]; then
        topN="10"
        echo "Default topN is 10."
    fi
fi

command -v nc >/dev/null 2>&1 || {
    echo >&2 "I require nc but it's not installed. Try install..."; exit 1;
}

build_cons() {
    mkdir -p ${tmpPath}
    tmp=${tmpPath}/cons.`date '+%H%M'`
    echo "Tmp: ${tmp}"
    echo cons | nc localhost ${clientPort} > ${tmp}
}

parse_cons() {
    # 12 10.101.4.41
    # 11 10.101.6.10
    # 10 10.101.6.18
    echo -e "Top${topN}:"
    cat ${tmp} | sed 's/:.*//g' | sed 's/.*\///g' | sort | uniq -c | sort -k1 -n | tail -${topN}
}

build_cons
parse_cons
exit 0
