#!/usr/bin/env bash

cd `dirname $0`
source ~/.bashrc

tmpPath="/tmp/zookeeper/zk-monitor/snapshot"
# default
# get variables form config file
dataDir=`cat ../conf/zoo.cfg | grep dataDir | sed 's/.*=//g'`
zkHome=`readlink -f ../../zookeeper`"/"
clientPort=`cat ../conf/zoo.cfg | grep clientPort | sed 's/.*=//g'`
zkVersion=`ls ../ | grep -e "^zookeeper-.*jar$"`
znodeParentPath="$1"
topN="$2"

if [ -z "${dataDir}" ]; then
    echo "Cannot get dataDir from '../conf/zoo.cfg'!"
    exit 1
fi

if [ -z "${clientPort}" ]; then
    echo "Cannot get clientPort from '../conf/zoo.cfg'!"
    exit 1
fi

if [ -z "${zkHome}" ]; then
    echo "Cannot get zkHome!"
    exit 1
fi

if [ -z "${zkVersion}" ]; then
    echo "Cannot get zkVersion!"
    exit 1
fi

if [ -z "$znodeParentPath" -o -z "$topN" ]; then
    echo "Usage:"
    echo -e "\t bash /home/zookeeper/software/zookeeper/tools/count_znode.sh <znodeParentPath> <topN>"
    echo -e '\t bash /home/zookeeper/software/zookeeper/tools/count_znode.sh "/" "3"\n'
    if [ -z "$znodeParentPath" ]; then
        znodeParentPath="/"
        echo "Default znodeParentPath is '/'."
    fi
    if [ -z "$topN" ]; then
        topN="10"
        echo "Default topN is 10."
    fi
fi

build_newest_snapshot() {
    newest_snapshot=`ls -l "${dataDir}"/version-2/snapshot.* | awk 'END{print $9}'`
    # Newest snapshot: /home/zookeeper/data//version-2/snapshot.0
    echo "Newest snapshot: ${newest_snapshot}"

    cd ${zkHome}
    mkdir -p ${tmpPath}
    tmp=${tmpPath}/snapshot.`date '+%H%M'`
    # Tmp: /home/zookeeper/zk-monitor/snapshot/snapshot.20170816142407
    echo "Tmp: ${tmp}"
    java -cp ${zkHome}${zkVersion}:${zkHome}lib/* org.apache.zookeeper.server.SnapshotFormatter ${newest_snapshot} > ${tmp}
}

parse_newest_snapshot() {
    arr=`echo "ls ${znodeParentPath}" | zkCli.sh -server localhost:${clientPort} |& grep -v CONNECT | grep -v myid | grep -e "\[*\]"`
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
        count=`cat ${tmp} | grep -e "^${znodeParentPath}${a}/" | wc -l`
        result=`echo -e "\t\t${result}\n${count}\t${znodeParentPath}${a}/"`
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

build_newest_snapshot
parse_newest_snapshot
exit 0
