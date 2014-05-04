package com.intel.spark.log.util;

import com.intel.spark.log.model.App;
import com.intel.spark.log.model.Job;
import com.intel.spark.log.model.Stage;
import com.intel.spark.log.model.Task;
import com.intel.spark.log.model.TaskSet;

public class TimeAdjuster {

	public static Long jobStartTime;
	public static Long jobEndTime;

	public static void recordTime(String logLine) throws Exception {
		if (logLine.matches("[0-9][0-9]/[0-1][0-9]/[0-3][0-9](.*)")) {
			long time = Util.transformTime(logLine.substring(0, 17));
			if (jobStartTime == null) {
				jobStartTime = time;
			}
			jobEndTime = time;
		}
	}

	public static void adjustTime(App app) {

		for (Job job : app.getChildren()) {
			job.setJobStartTime(jobStartTime);
			job.setJobEndTime(jobEndTime);
			for (Stage stage : job.getChildren()) {
				stage.setJobStartTime(jobStartTime);
				stage.setJobEndTime(jobEndTime);
				for (TaskSet taskSet : stage.getChildren()) {
					for (Task task : taskSet.getChildren()) {
						task.setJobStartTime(jobStartTime);
						task.setJobEndTime(jobEndTime);
					}
				}
			}
		}
	}
}
