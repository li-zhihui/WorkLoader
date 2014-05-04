package com.intel.spark.log.processor;

import com.intel.spark.log.model.App;
import com.intel.spark.log.model.Node;

public class TaskSetAddProcessor extends BaseProcessor {

	@Override
	public void process(App app) throws Exception {
		Node taskSet = app.getContext().get(app.CURRENT_TASK_SET);
		taskSet.setId(super.subLogLine(7));
	}

}
