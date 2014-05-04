package workload.spark.ui;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import workload.spark.Constants;
import workload.spark.WorkloadConf;
import workload.spark.backup.Backup;
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

	public void execute(String[] args) throws Exception {
		Properties p = buildProperties(args[0]);
		p.put(Constants.WORKLOAD_NAME, args[1]);
		p.put(Constants.WORKLOAD_WORKDIR, args[2]);
		WorkloadConf.set(p);
		RunnerFactory.getRunner().run();
		LoaderFactory.getLoader().loadData();
		CleanerFactory.getCleaner().clean();
		ChartFactory.getChart().createChart();
		ReportFactory.getReport();
		Backup.backup();
		WorkloadConf.clean();
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
