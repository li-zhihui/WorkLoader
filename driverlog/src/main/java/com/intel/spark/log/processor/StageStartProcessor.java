package com.intel.spark.log.processor;

import com.intel.spark.log.model.App;
import com.intel.spark.log.model.Job;
import com.intel.spark.log.model.Stage;

public class StageStartProcessor extends BaseProcessor {

	@Override
	public void process(App app) throws Exception {
		Job job = app.getLastChild();
		Stage stage = new Stage();
		job.addNewChild(stage);
		initNode(stage);
		stage.setStartTime(time);
		stage.setId(super.subLogLine(6));
		stage.setName(super.subLogLine(7).substring(1));
	}

}
