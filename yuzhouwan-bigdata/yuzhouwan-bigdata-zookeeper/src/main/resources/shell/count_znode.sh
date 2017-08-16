#!/usr/bin/env bash

# /home/zookeeper/datadir/version-2/snapshot.f034a7b20
newest_snapshot=`ls -l ~/datadir/version-2/snapshot.* | awk 'END{print $9}'`
echo "Newest snapshot: ${newest_snapshot}"
cd ~/software/zookeeper
tmp=snapshot.`date '+%Y%m%d%H%M%S'`
echo "Tmp ${tmp}"
java -cp zookeeper-3.4.6.jar:lib/log4j-1.2.16.jar:lib/slf4j-log4j12-1.6.1.jar:lib/slf4j-api-1.6.1.jar org.apache.zookeeper.server.SnapshotFormatter ${newest_snapshot} > ${tmp}

arr=`echo "ls /" | zkCli.sh -server localhost:2015 | grep zookeeper`
# [dynamic, hbase-ha, read-write-lock-B, read-write-lock-A, spark1.4.0pre, ocep, druid_test, storm, rmstore, bigquery, hbase, hindex, leader, config, hbase-monitor, phoenix, flink, read-write-lock, spark1.4.0, druid_common, consumers, druid, spark1.5.2.1, yarn-leader-election, controller_epoch, yuzhouwan, dubbo, election, isr_change_notification, hadoop-ha, zookeeper, admin, controller, tranquility, brokers]
echo -e "Origin:\n\t ${arr}\n"

arr=`echo ${arr:1:${#arr}-2}`
# dynamic, hbase-ha, read-write-lock-B, read-write-lock-A, spark1.4.0pre, ocep, druid_test, storm, rmstore, bigquery, hbase, hindex, leader, config, hbase-monitor, phoenix, flink, read-write-lock, spark1.4.0, druid_common, consumers, druid, spark1.5.2.1, yarn-leader-election, controller_epoch, yuzhouwan, dubbo, election, isr_change_notification, hadoop-ha, zookeeper, admin, controller, tranquility, brokers
echo -e "Cut head & tail:\n\t ${arr}\n"

OLD_IFS="$IFS"
IFS=$", "
arr=($arr)
echo "Split into array:"

result=
for a in ${arr[@]}; do
    count=`cat ${tmp} | grep "/${a}" | wc -l`
    result=`echo -e "\t\t${result}\n${count}\t/${a}"`
done

echo ${result} | sort -k1 -n

IFS="$OLD_IFS"
echo "Roll back Old IFS."
