package com.intel.spark.log.processor;

import com.intel.spark.log.model.App;
import com.intel.spark.log.model.Job;

public class JobStartProcessor extends BaseProcessor {

	@Override
	public void process(App app) {
		Job job = new Job();
		initNode(job);
		job.setStartTime(time);
		app.addNewChild(job);
		job.setName(super.subLogLine(6,7,8));
		app.getContext().put(app.CURRENT_JOB, job);
	}

}
