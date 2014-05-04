package workload.spark.chart;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class ChartFactory {
	public static SparkChart getChart() {
		if (WorkloadConf.get(Constants.WORKLOAD_STEP_CHART).equals("false")) {
			return new EmptyChart();
		}
		SparkChart sc = new JFreeSparkChart();
		sc.setCsvFolder(WorkloadConf.get(Constants.WORKLOAD_OUTPUT_PATH));
		sc.setJpgFolder(WorkloadConf.get(Constants.WORKLOAD_OUTPUT_PATH));
		return sc;
	}
}
