#!/bin/sh

# Doc: http://yuzhouwan.com/posts/15691#%e7%b3%bb%e7%bb%9f%e6%9e%b6%e6%9e%84

command -v fincore >/dev/null 2>&1 || {
    echo >&2 "I require fincore but it's not installed. Try install..." ; exit 1;
}

pids_tmp=/tmp/cache.pids
cache_tmp=/tmp/cache.files
fincore_tmp=/tmp/cache.fincore
result_tmp=/tmp/result.fincore

echo "The top 3 cache pid:"
ps -e -o pid,rss | sort -nk2 -r | grep -vw 1 | head -3 | awk '{print $1}' > ${pids_tmp}
echo -e "`cat ${pids_tmp}`\n"

for pid in `cat ${pids_tmp}`; do
    ps -ef | grep ${pid}
done
echo -e "\n"

if [ -f ${cache_tmp} ]; then
    rm -f ${cache_tmp}
fi

while read line; do
    echo "Pid: ${line}, Files:"
    file=`lsof -p ${line} 2>/dev/null | awk '{print $9}'`
    echo "${file}" >> ${cache_tmp}
    echo -e "${file}\n" | grep -vw "NAME"
done<${pids_tmp}

if [ -f ${fincore_tmp} ]; then
    rm -f ${fincore_tmp}
fi

for i in `cat ${cache_tmp}`; do
    if [ -f ${i} ]; then
        echo ${i} >> ${fincore_tmp}
    fi
done

fincore --pages=false `cat ${fincore_tmp} | grep -v /usr/sbin/sshd | grep -v /lib64/ld-2.12.so | grep -v /bin/bash | grep -v /bin/login` > ${result_tmp}

echo "filename size	total pages	cached pages    cached size	cached percentage"

len=`cat ${result_tmp} | wc -l`
cat ${result_tmp} | tail -$(echo "${len}-1" | bc) | sort -nk4 -r

exit 0
