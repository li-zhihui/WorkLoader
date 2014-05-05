package com.intel.spark.log.processor;

import com.intel.spark.log.model.App;
import com.intel.spark.log.model.Task;
import com.intel.spark.log.model.TaskSet;

public class TaskStartProcessor extends BaseProcessor {

	@Override
	public void process(App app) throws Exception {
		String taskSetId = super.subLogLine(6).split(":")[0];
		TaskSet taskSet = super.findTaskSet(app, taskSetId);
		Task task = new Task();
		taskSet.addNewChild(task);
		task.setStartTime(time);
		task.setId(super.subLogLine(9));
	}

}
