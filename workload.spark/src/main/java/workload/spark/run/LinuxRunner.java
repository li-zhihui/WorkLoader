package workload.spark.run;

import java.io.File;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class LinuxRunner extends Runner {

	@Override
	public void run() throws Exception {
		String workloadConfPath = Constants.WORKLOAD_CONF_PREFIX + "."
				+ WorkloadConf.get(Constants.WORKLOAD_NAME) + "."
				+ Constants.WORKLOAD_PATH_SUFFIX;
		System.out.println(workloadConfPath);
		String workloadPath = WorkloadConf.get(workloadConfPath);
		String drunCp = "/bin/cp "
				+ WorkloadConf.get(Constants.WORKLOAD_WORKDIR)
				+ "/script/drun_wl.sh " + workloadPath;
		Runtime runtime = Runtime.getRuntime();
		String[] cpCmd = { "/bin/sh", "-c", drunCp };
		System.out.println(drunCp);
		if (runtime.exec(cpCmd).waitFor() != 0) {
			throw new Exception("cp drun_wl.sh failed.");
		}

		String workloadConfRun = Constants.WORKLOAD_CONF_PREFIX + "."
				+ WorkloadConf.get(Constants.WORKLOAD_NAME) + "."
				+ Constants.WORKLOAD_RUN_SUFFIX;
		System.out.println(workloadConfRun);
		String logFile = WorkloadConf.get(Constants.WORKLOAD_NAME)+Constants.DRIVER_LOG_SUFFIX;
		String runsh = workloadPath + "drun_wl.sh "
				+ WorkloadConf.get(workloadConfRun)+" "+logFile;
		String[] runCmd = { "/bin/sh", "-c", runsh };
		System.out.println(runsh);
		if (runtime.exec(runCmd, null, new File(workloadPath)).waitFor() != 0) {
			throw new Exception("drun failed.");
		}
	}
}
