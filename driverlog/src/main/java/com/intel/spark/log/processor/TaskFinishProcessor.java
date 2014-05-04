package com.intel.spark.log.processor;

import com.intel.spark.log.model.App;
import com.intel.spark.log.model.Task;

public class TaskFinishProcessor extends BaseProcessor {

	@Override
	public void process(App app) throws Exception {
		String taskId = super.subLogLine(6);
		Task task = super.findTask(app, taskId);
		task.setEndTime(time);
		task.setDuration(super.subLogLine(8));
	}

}
