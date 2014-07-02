#!/bin/bash

TARGET=("sr479" "sr480" "sr481" "sr482")

for worker in ${TARGET[@]}
do
iostat  2 > ${worker}_iostat.dat &
sar -P -ALL 2 > ${worker}_sar.dat &
done
./run.sh > nweightlog.log 2>&1
sleep 40
for worker in ${TARGET[@]}
do
ssh $worker ps aux | grep -i "iostat" | grep -v "grep" | awk '{print $2}' | xargs ssh $worker kill -9
ssh $worker ps aux | grep -i "sar" | grep -v "grep" | awk '{print $2}' | xargs ssh $worker kill -9
done
