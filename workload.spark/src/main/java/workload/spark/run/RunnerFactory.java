package workload.spark.run;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class RunnerFactory {
	public static Runner getRunner() {
		if (WorkloadConf.get(Constants.WORKLOAD_STEP_RUNNER).equals("emptyRunner")) {
			return new EmptyRunner();
		}
		else if (WorkloadConf.get(Constants.WORKLOAD_STEP_RUNNER).equals("linuxRunner"))
			return new LinuxRunner();
		else 
			return new XRunner();
	}
}
