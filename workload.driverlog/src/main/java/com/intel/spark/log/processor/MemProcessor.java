package com.intel.spark.log.processor;

import com.intel.spark.log.model.App;
import com.intel.spark.log.model.Job;
import com.intel.spark.log.util.Util;

import java.util.HashMap;
import java.util.Map;

public class MemProcessor extends BaseProcessor {

    @Override
    public void process(App app) {
        String rddPartitionId = subLogLine(5);
        String rddId = rddPartitionId.substring(0, rddPartitionId.lastIndexOf('_'));
        String nodeId = subLogLine(9).split(":")[0];
        float memAddSize = 0;
        try {
            memAddSize = Float.valueOf(subLogLine(11));
        } catch (Exception e) {

        }
        app.addRddMem(nodeId, rddId, memAddSize, time);
    }


}
