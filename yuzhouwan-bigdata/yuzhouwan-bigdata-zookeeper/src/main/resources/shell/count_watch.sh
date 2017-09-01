#!/usr/bin/env bash

cd `dirname $0`
source ~/.bashrc

tmpPath="/tmp/zookeeper/zk-monitor/wchc"
clientPort=`cat ../conf/zoo.cfg | grep clientPort | sed 's/.*=//g'`
znodeParentPath="$1"
topN="$2"

if [ -z "${clientPort}" ]; then
    echo "Cannot get clientPort from '../conf/zoo.cfg'!"
    exit 0
fi

if [ -z "$znodeParentPath" -o -z "$topN" ]; then
    echo "Usage:"
    echo -e "\t bash /home/zookeeper/software/zookeeper/tools/count_watch.sh <znodeParentPath> <topN>"
    echo -e '\t bash /home/zookeeper/software/zookeeper/tools/count_watch.sh "/" "3"\n'
    if [ -z "$znodeParentPath" ]; then
        znodeParentPath="/"
        echo "Default znodeParentPath is '/'."
    fi
    if [ -z "$topN" ]; then
        topN="10"
        echo "Default topN is 10."
    fi
fi

command -v nc >/dev/null 2>&1 || {
    echo >&2 "I require nc but it's not installed. Try install..." ; exit 1;
}

build_wchc() {
    mkdir -p ${tmpPath}
    tmp=${tmpPath}/wchc.`date '+d%H%M'`
    echo "Tmp: ${tmp}"
    echo wchc | nc localhost ${clientPort} > ${tmp}
}

parse_wchc() {
    arr=`echo "ls ${znodeParentPath}" | zkCli.sh -server localhost:${clientPort} |& grep -e "\[*\]" | grep -v CONNECT | grep -v myid | grep -v ${clientPort}`
    # [leader, election, zookeeper]
    echo -e "Origin:\n\t ${arr}\n"

    arr=`echo ${arr:1:${#arr}-2}`
    # leader, election, zookeeper
    echo -e "Cut head & tail:\n\t ${arr}\n"

    OLD_IFS="$IFS"
    IFS=$", "
    arr=(${arr})

    if [ ${znodeParentPath} != "/" ]; then
        znodeParentPath="${znodeParentPath}/"
    fi
    for a in ${arr[@]}; do
        # -t
        count=`cat ${tmp} | grep "${znodeParentPath}${a}" | wc -l`
        result=`echo -e "\t\t${result}\n${count}\t${znodeParentPath}${a}"`
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
exit 0
