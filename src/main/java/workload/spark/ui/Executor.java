package workload.spark.ui;

import java.io.IOException;
import java.util.Properties;

import workload.spark.Constants;
import workload.spark.WorkloaderEnv;
import workload.spark.chart.ChartFactory;
import workload.spark.data.LoaderFactory;
import workload.spark.data.cleaner.CleanerFactory;
import workload.spark.run.RunnerFactory;
import workload.spark.ui.report.ReportFactory;

/**
 * Execute special workload.
 * 
 */
public class Executor {
	
	public void execute(String conf, String workloadPath) throws Exception {
		Properties p = buildProperties(conf);
		p.put(Constants.WORKLOAD_PATH, workloadPath);
		WorkloaderEnv.set(p);
		RunnerFactory.getRunner().run();
		LoaderFactory.getLoader().loadData();
		CleanerFactory.getCleaner().clean();
		ChartFactory.getChart().createChart();
		ReportFactory.getReport();
	}

	private Properties buildProperties(String conf) {
		return null;
	}
}
