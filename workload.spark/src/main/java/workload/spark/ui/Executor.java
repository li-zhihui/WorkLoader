package workload.spark.ui;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import workload.spark.Constants;
import workload.spark.Util;
import workload.spark.WorkloadConf;
import workload.spark.backup.Backup;
import workload.spark.backup.Init;
import workload.spark.chart.ChartFactory;
import workload.spark.data.LoaderFactory;
import workload.spark.data.cleaner.CleanerFactory;
import workload.spark.data.parser.LogParserFactory;
import workload.spark.run.RunnerFactory;
import workload.spark.ui.report.ReportFactory;

/**
 * Execute special workload.
 * 
 */
public class Executor {

	public void execute(String[] args) throws Exception {
		Properties p = Util.buildProperties(args[0]);//configuration file path
		p.put(Constants.WORKLOAD_NAME, args[1]);
		p.put(Constants.WORKLOAD_WORKDIR, args[2]);
		WorkloadConf.set(p);
		Init.init();
		RunnerFactory.getRunner().run();
		
		LogParserFactory.getLogParser().parse();
		LoaderFactory.getLoader().loadData();
		CleanerFactory.getCleaner().clean();
		//ChartPreprocesserreturn 4-dimension lmll
		ChartFactory.getChart().createChart();
		ReportFactory.getReport();
		Backup.backup();
		WorkloadConf.clean();
	}
//
//	/**
//	 * build properties for every execution, support runtime load configuration.
//	 * 
//	 * @param conf
//	 * @return
//	 * @throws IOException
//	 */
//	private Properties buildProperties(String conf) throws IOException {
//		InputStream in = new BufferedInputStream(new FileInputStream(conf));
//		Properties props = new Properties();
//		props.load(in);
//		in.close();
//		return props;
//	}
}
