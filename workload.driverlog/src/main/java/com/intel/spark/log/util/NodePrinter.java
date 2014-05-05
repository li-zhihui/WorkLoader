package com.intel.spark.log.util;

import java.io.File;
import java.io.FileOutputStream;

import com.intel.spark.log.model.App;
import com.intel.spark.log.model.Job;
import com.intel.spark.log.model.Node;
import com.intel.spark.log.model.Stage;
import com.intel.spark.log.model.Task;
import com.intel.spark.log.model.TaskSet;

public class NodePrinter {

	public static void print(App app) throws Exception {
		FileOutputStream jobFs = new FileOutputStream(new File("/tmp/job.csv"));
		FileOutputStream stageFs = new FileOutputStream(new File(
				"/tmp/stage.csv"));
		FileOutputStream taskFs = new FileOutputStream(
				new File("/tmp/task.csv"));

		for (Job job : app.getChildren()) {
			String jobName = job.getName();
			jobFs
					.write((jobName + "," + buildTimeStr(job) + "\r\n")
							.getBytes());
			for (Stage stage : job.getChildren()) {
				String stageName = stage.getName();
				stageFs.write((stageName + "," + jobName + ","
						+ buildTimeStr(stage) + "\r\n").getBytes());
				for (TaskSet taskSet : stage.getChildren()) {
					for (Task task : taskSet.getChildren()) {
						taskFs.write((task.getId() + "," + stageName + ","
								+ jobName + "," + buildTimeStr(task) + "\r\n")
								.getBytes());
					}
				}
			}
		}
		jobFs.close();
		stageFs.close();
		taskFs.close();
	}

	private static String buildTimeStr(Node node) {
		return node.getStartTime() + "," + node.getEndTime() + ","
				+ (node.getStartTime() - node.getJobStartTime()) + ","
				+ (node.getEndTime() - node.getStartTime()) + ","
				+ (node.getJobEndTime() - node.getEndTime());
	}
}
