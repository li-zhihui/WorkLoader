package com.intel.spark.log.processor;

import com.intel.spark.log.model.App;
import com.intel.spark.log.model.Stage;

public class StageFinishProcessor extends BaseProcessor {

	@Override
	public void process(App app) throws Exception {
		String stageId = super.subLogLine(5);
		Stage stage = super.findStage(app, stageId);
		stage.setEndTime(time);
		stage.setDuration(super.subLogLine(11));
	}

}
