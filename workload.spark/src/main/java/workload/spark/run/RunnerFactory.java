package workload.spark.run;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class RunnerFactory {
	public static Runner getRunner() {
		if (WorkloadConf.get(Constants.WORKLOAD_STEP_RUNNER).equals("false")) {
			return new EmptyRunner();
		}
		return new LinuxRunner();
	}
}
