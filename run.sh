#!/bin/bash
conf=`pwd`/conf.properties
classpath=$conf
for file in web/target/web-1.0-SNAPSHOT/WEB-INF/lib/*.jar
do
classpath="$classpath":"`pwd`"/"$file"
done
java -cp $classpath workload.spark.ui.Cli $conf $1 `pwd`
