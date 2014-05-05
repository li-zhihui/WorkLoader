package com.intel.spark.log.processor;

import com.intel.spark.log.model.App;
import com.intel.spark.log.model.Node;

public class JobFinishProcessor extends BaseProcessor {

	@Override
	public void process(App app) throws Exception {
		Node job = app.getContext().get(app.CURRENT_JOB);
		job.setEndTime(time);
		job.setDuration(super.subLogLine(10));
	}

}
