#!/usr/bin/env bash

cd `dirname $0`
source ~/.bashrc

dataDir=`cat ../conf/zoo.cfg | grep dataDir | sed 's/.*=//g'`
leftSnap="$1"

if [ -z "$leftSnap" ]; then
    echo "Usage:"
    echo -e "\t bash /home/zookeeper/software/zookeeper/tools/snap_clean.sh <leftSnap>"
    echo -e '\t bash /home/zookeeper/software/zookeeper/tools/snap_clean.sh "3"'
    if [ -z "$leftSnap" ]; then
        leftSnap="3"
        echo "Default left snapshot file is 3."
    fi
fi

zkCleanup.sh "${dataDir}/version-2/" ${leftSnap}

snapNum=`ls -l "${dataDir}"/version-2/snapshot.* | wc -l`
if [ $(echo "${snapNum}-${leftSnap}" | bc) -gt 0 ]; then
    echo -e "The number of snapshot files: ${snapNum} > ${leftSnap}."
    echo "Cleaning..."
    zkCleanup.sh "${dataDir}/version-2/" "${leftSnap}"
    snapNum=`ls -l "${dataDir}"/version-2/snapshot.* | wc -l`
fi
echo -e "For now, the number of snapshot files: ${snapNum}"
exit 0
