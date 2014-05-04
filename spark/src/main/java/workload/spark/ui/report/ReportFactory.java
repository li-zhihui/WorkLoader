package workload.spark.ui.report;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class ReportFactory {
	public static void getReport() {
		if (WorkloadConf.get(Constants.WORKLOAD_STEP_REPORT).equals("false")) {
			return;
		}
		(new HtmlReport()).buildReport();
	}
}
