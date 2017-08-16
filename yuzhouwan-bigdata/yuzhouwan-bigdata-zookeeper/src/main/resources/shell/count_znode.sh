#!/usr/bin/env bash

#
# bash /home/zookeeper/zk-monitor/count_znode.sh "/home/zookeeper/data/" "/home/zookeeper/software/zookeeper" "2015" "/"

tmpPath="/home/zookeeper/zk-monitor/snapshot"
dataDir="$1"
zkHome="$2"
clientPort="$3"
znodeParentPath="$4"

if [ -z "$dataDir" -o -z "$zkHome" -o -z "$clientPort"  -o -z "$znodeParentPath" ]; then
    echo "bash /home/zookeeper/zk-monitor/count_znode.sh <dataDir> <zkHome> <clientPort> <znodeParentPath>"
    echo 'bash /home/zookeeper/zk-monitor/count_znode.sh "/home/zookeeper/data/" "/home/zookeeper/software/zookeeper" "2015" "/"'
    exit
fi

newest_snapshot=`ls -l "${dataDir}"/version-2/snapshot.* | awk 'END{print $9}'`
# Newest snapshot: /home/zookeeper/data//version-2/snapshot.0
echo "Newest snapshot: ${newest_snapshot}"

cd ${zkHome}
mkdir -p ${tmpPath}
tmp=${tmpPath}/snapshot.`date '+%Y%m%d%H%M%S'`
# Tmp: /home/zookeeper/zk-monitor/snapshot/snapshot.20170816142407
echo "Tmp: ${tmp}"
java -cp zookeeper-3.4.6.jar:lib/log4j-1.2.16.jar:lib/slf4j-log4j12-1.6.1.jar:lib/slf4j-api-1.6.1.jar org.apache.zookeeper.server.SnapshotFormatter ${newest_snapshot} > ${tmp}

arr=`echo "ls ${znodeParentPath}" | zkCli.sh -server localhost:${clientPort} | grep zookeeper`
# [leader, election, zookeeper]
echo -e "Origin:\n\t ${arr}\n"

arr=`echo ${arr:1:${#arr}-2}`
# leader, election, zookeeper
echo -e "Cut head & tail:\n\t ${arr}\n"

OLD_IFS="$IFS"
IFS=$", "
arr=(${arr})
echo "Split into array:"

result=
for a in ${arr[@]}; do
    count=`cat ${tmp} | grep "/${a}" | wc -l`
    result=`echo -e "\t\t${result}\n${count}\t/${a}"`
done

# 0	/election
# 0	/leader
# 2	/zookeeper
echo ${result} | sort -k1 -n

IFS="$OLD_IFS"
echo "Roll back Old IFS."
