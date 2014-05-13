package workload.spark.data;

import workload.spark.Constants;
import workload.spark.Util;
import workload.spark.WorkloadConf;

public class LoaderFactory {
	public static Loader getLoader() {

		if (WorkloadConf.get(Constants.WORKLOAD_STEP_LOADER).equals("false")) {
			return new EmptyLoader();
		}
		Util.print("call linux loader");
		return new LinuxLoader();
	}
}
