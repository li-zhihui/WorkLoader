package com.intel.spark.log.util;

import com.intel.spark.log.common.MemInfo;
import com.intel.spark.log.common.rddMemInfo;
import com.intel.spark.log.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

public class MemPrinter {
    public static void print(App app) throws Exception {


        for (Map.Entry<String, MemInfo> entry : app.getMemMap().entrySet()) {
            FileOutputStream rddMemFs = new FileOutputStream(new File("/tmp/" + entry.getKey() + "rddmem.csv"));
            for (Map.Entry<String, rddMemInfo> rddEntry : entry.getValue().getRddMemInfoMap().entrySet()) {
                rddMemFs.write((rddEntry.getKey() + "," + rddEntry.getValue().getMemSize() + "," +
                        (rddEntry.getValue().getTime() - app.getLastChild().getJobStartTime()) + "\r\n").getBytes());
            }
            rddMemFs.close();
        }


    }
}
