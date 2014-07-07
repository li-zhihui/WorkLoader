package workload.spark.chart;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class ChartFactory {
	public static SparkChart getChart() {
		if (WorkloadConf.get(Constants.WORKLOAD_STEP_CHART)
				.equals("emptyChart")) {
			return new EmptyChart();
		}
		JobChart jc = new JSTChart();
		XChart xc = new XChart();
		xc.setCsvFolder(WorkloadConf.get(Constants.WORKLOAD_OUTPUT_PATH));
		xc.setJpgFolder(WorkloadConf.get(Constants.WORKLOAD_OUTPUT_PATH));
		xc.setJobChart(jc);
		try {
			xc.createChart();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xc;
	}
}
