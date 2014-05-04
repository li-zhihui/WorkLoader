package com.intel.spark.log.processor;

import com.intel.spark.log.model.App;
import com.intel.spark.log.model.TaskSet;

public class TaskSetProcessor extends BaseProcessor {

	@Override
	public void process(App app) throws Exception {
		String stageId = super.subLogLine(10);
		TaskSet taskSet = new TaskSet();
		super.findStage(app, stageId).addNewChild(taskSet);
		app.getContext().put(app.CURRENT_TASK_SET, taskSet);
	}

}
