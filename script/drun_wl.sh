#!/bin/bash

TARGET=("sr453" "sr454" "sr463" "sr462")

for worker in ${TARGET[@]}
do
ssh $worker rm -rf ~/${worker}_dstat.csv
ssh $worker emon -i "/home/frank/confidential/edp-v1.9d/Architecture\ Specific/SNB-EP/snb-ep-events.txt" > ${worker}_emonstat.dat &
echo "start emon collection on node $worker"
ssh $worker dstat --mem --io --cpu --net -N eth0,eth1,total --disk --time --output ${worker}_dstat.csv 2 > /dev/null &
echo "start dstat collection on node $worker"
./"$worker"_jstatRun.sh &
echo "start jstat collection on node $worker"
done

echo "start to sync files on workers every 1 s"
./startSyncFile.sh &

./$1 > $2 2>&1

for worker in ${TARGET[@]}
do
ssh $worker ps aux | grep -i "jstat" | grep -v "grep" | awk '{print $2}' | xargs ssh $worker kill -9
#ssh $worker ps aux | grep "dstat" | grep -v "grep" |awk '{print $2}' | xargs ssh $worker kill -9
ssh $worker /home/frank/confidential/edp-v1.9d/kill-emon-script.sh
done

sleep 40
for worker in ${TARGET[@]}
do
ssh $worker ps aux | grep "dstat" | grep -v "grep" |awk '{print $2}' | xargs ssh $worker kill -9
echo "kill dstat on node $worker"
done

#echo "stop syncing files on works"
./stopSyncFile.sh

./copyDstatCsv.sh
./formatJstat.sh
