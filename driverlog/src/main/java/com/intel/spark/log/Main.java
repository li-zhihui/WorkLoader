package com.intel.spark.log;

import java.io.BufferedReader;
import java.io.FileReader;

import com.intel.spark.log.model.App;
import com.intel.spark.log.processor.Processor;
import com.intel.spark.log.util.MemPrinter;
import com.intel.spark.log.util.NodePrinter;
import com.intel.spark.log.util.TimeAdjuster;

public class Main {
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Please input Spark Driver's log file path.");
			System.exit(1);
		}
		String fileStr = args[0];
		processFile(fileStr);
		System.out
				.println("Complete analysis, please check output file(/tmp/job.csv, stage.csv, task.csv)");
	}

	private static void processFile(String fileStr) throws Exception {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileStr));
			String line;
			App app = new App();
			while ((line = br.readLine()) != null) {
				TimeAdjuster.recordTime(line);
				Processor processor = Matcher.build(line);
				if (processor == null) {
					continue;
				}
				processor.apply(line).process(app);
			}
			TimeAdjuster.adjustTime(app);
			NodePrinter.print(app);
            MemPrinter.print(app);
        } catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			if (br != null) {
				br.close();
			}
		}

	}
}
