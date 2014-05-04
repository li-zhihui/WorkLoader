#!/bin/bash

#FIXME read it from SPARK_HOME/conf/slave
TARGET=("sr453" "sr454" "sr462" "sr463")

for worker in ${TARGET[@]}
do
ssh $worker rm -rf ~/${worker}_stat.csv
ssh $worker dstat --mem --io --cpu --net --disk --time --output ${worker}_stat.csv 1 > stat &
done

./$1 > $2 2>&1

for worker in ${TARGET[@]}
do
ssh $worker ps aux | grep "dstat" | grep -v "grep" |awk '{print $2}' | xargs ssh $worker kill -9
done

for worker in ${TARGET[@]}
do
scp $worker:~/${worker}_stat.csv ./
done
