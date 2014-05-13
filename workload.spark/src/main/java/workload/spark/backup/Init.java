package workload.spark.backup;

import java.io.File;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class Init {

	public static void init() {
		File workPath = new File(WorkloadConf
				.get(Constants.WORKLOAD_OUTPUT_PATH));
		if (!workPath.exists()) {
			workPath.mkdir();
		}

	}
}
