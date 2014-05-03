package workload.spark.ui;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import workload.spark.Constants;
import workload.spark.WorkloaderContext;
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
		WorkloaderContext.set(p);
		RunnerFactory.getRunner().run();
		LoaderFactory.getLoader().loadData();
		CleanerFactory.getCleaner().clean();
		ChartFactory.getChart().createChart();
		ReportFactory.getReport();
		WorkloaderContext.clean();
	}

	/**
	 * build properties for every execution, support runtime load configuration.
	 * 
	 * @param conf
	 * @return
	 * @throws IOException
	 */
	private Properties buildProperties(String conf) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(conf));
		Properties props = new Properties();
		props.load(in);
		in.close();
		return props;
	}
}
