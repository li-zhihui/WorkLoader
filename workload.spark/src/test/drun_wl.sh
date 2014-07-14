#!/bin/bash

TARGET=("sr160" "sr161" "sr197" "sr205")

for worker in ${TARGET[@]}
do
ssh $worker iostat  2 > ${worker}_iostat.dat &
done
./run.sh > nweightlog.log 2>&1
sleep 40
for worker in ${TARGET[@]}
do
ssh $worker ps aux | grep -i "iostat" | grep -v "grep" | awk '{print $2}' | xargs ssh $worker kill -9
done
