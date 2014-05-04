package com.intel.spark.log.processor;

import com.intel.spark.log.model.App;

public interface Processor {

	void process(App app) throws Exception;

	Processor apply(String logLine) throws Exception;
}
