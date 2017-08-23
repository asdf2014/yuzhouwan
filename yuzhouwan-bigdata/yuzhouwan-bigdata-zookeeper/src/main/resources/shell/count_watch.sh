#!/usr/bin/env bash

# bash /home/zookeeper/zk-monitor/count_watch.sh "2181" "/" "3"

tmpPath="/home/zookeeper/zk-monitor/wchc"
clientPort="$1"
znodeParentPath="$2"
topN="$3"

if [ -z "$clientPort" -o -z "$znodeParentPath" -o -z "$topN" ]; then
    echo "bash /home/zookeeper/zk-monitor/count_watch.sh <clientPort> <znodeParentPath> <topN>"
    echo 'bash /home/zookeeper/zk-monitor/count_watch.sh "2181" "/" "3"'
    exit
fi

command -v nc >/dev/null 2>&1 || {
    echo >&2 "I require nc but it's not installed. Try install...";
    yum install nc;
    command -v nc >/dev/null 2>&1 || { echo >&2 "I require nc but it's still cannot be installed. Aborting..."; exit 1; }
}

build_wchc() {
    mkdir -p ${tmpPath}
    tmp=${tmpPath}/wchc.`date '+%Y%m%d%H%M%S'`
    echo "Tmp: ${tmp}"
    echo wchc | nc localhost ${clientPort} > ${tmp}
}

parse_wchc() {
    arr=`echo "ls ${znodeParentPath}" | zkCli.sh -server localhost:${clientPort} | grep zookeeper`
    # [leader, election, zookeeper]
    echo -e "Origin:\n\t ${arr}\n"

    arr=`echo ${arr:1:${#arr}-2}`
    # leader, election, zookeeper
    echo -e "Cut head & tail:\n\t ${arr}\n"

    OLD_IFS="$IFS"
    IFS=$", "
    arr=(${arr})

    result=
    for a in ${arr[@]}; do
        count=`cat ${tmp} | grep "/${a}" | wc -l`
        result=`echo -e "\t\t${result}\n${count}\t/${a}"`
    done

    # 0	/election
    # 0	/leader
    # 2	/zookeeper
    sorted=`echo ${result} | sort -k1 -n`
    topNInfo=`echo ${sorted} | tail -${topN}`
    echo -e "Top${topN}: \n${topNInfo}"

    IFS="$OLD_IFS"
    echo "Roll back Old IFS."
}

build_wchc
parse_wchc
