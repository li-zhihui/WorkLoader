package workload.spark.data.cleaner;

import com.intel.spark.log.DriverlogMain;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class DriverLogCleaner extends Cleaner {

	Cleaner cleaner;

	public DriverLogCleaner(Cleaner cleaner) {
		this.cleaner = cleaner;
	}

	@Override
	public void clean() throws Exception {
		this.cleaner.clean();

		String workloadConfPath = Constants.WORKLOAD_CONF_PREFIX + "."
				+ WorkloadConf.get(Constants.WORKLOAD_NAME) + "."
				+ Constants.WORKLOAD_PATH_SUFFIX;
		System.out.println(workloadConfPath);
		String workloadPath = WorkloadConf.get(workloadConfPath);
		String logFile = workloadPath
				+ WorkloadConf.get(Constants.WORKLOAD_NAME)
				+ Constants.DRIVER_LOG_SUFFIX;
		DriverlogMain.processFile(logFile);
		String[] csvFiles = { "job", "stage", "task", "*rddmem" };
		Runtime runtime = Runtime.getRuntime();
		for (String csvFile : csvFiles) {
			String cp = "/bin/cp " + Constants.DRIVER_CSV_PAHT + "/" + csvFile
					+ ".csv "
					+ WorkloadConf.get(Constants.WORKLOAD_OUTPUT_PATH);
			String[] cpCmd = { "/bin/sh", "-c", cp };
			if (runtime.exec(cpCmd).waitFor() != 0) {
				System.out.println("copy " + csvFile + ".csv file failed:");
			}
		}
	}

}
