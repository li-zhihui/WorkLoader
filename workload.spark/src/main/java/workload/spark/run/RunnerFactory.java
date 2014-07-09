package workload.spark.run;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class RunnerFactory {
	public static Runner getRunner() {
		if (WorkloadConf.get(Constants.WORKLOAD_STEP_RUNNER).equals("linuxRunner"))
			return new LinuxRunner();
		else if(WorkloadConf.get(Constants.WORKLOAD_STEP_RUNNER).equals("XRunner"))
			return new XRunner();
		else 
			return new EmptyRunner();
	}
}
