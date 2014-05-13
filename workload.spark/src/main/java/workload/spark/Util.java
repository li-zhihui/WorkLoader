package workload.spark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Util {
	private static List<String> slaves;

	public static void runCmd(String cmd) throws Exception {
		String[] exe = { "/bin/sh", "-c", cmd };
		if (Runtime.getRuntime().exec(exe).waitFor() != 0) {
			throw new Exception("Failed to execute:" + cmd);
		}
	}

	public static String getWorkloadPath() {
		String workloadConfPath = Constants.WORKLOAD_CONF_PREFIX + "."
				+ WorkloadConf.get(Constants.WORKLOAD_NAME) + "."
				+ Constants.WORKLOAD_PATH_SUFFIX;
		return WorkloadConf.get(workloadConfPath);
	}

	public static List<String> getSlavesHost() throws Exception {
		if (slaves != null) {
			return slaves;
		}

		String slavesFilePath = WorkloadConf.get(Constants.SPARK_HOME)
				+ "conf/slaves";
		BufferedReader br = new BufferedReader(new FileReader(new File(
				slavesFilePath)));
		slaves = new ArrayList<String>();
		String line;
		while ((line = br.readLine()) != null) {
			if (!line.startsWith("#")) {
				slaves.add(line);
			}
		}
		return slaves;
	}

	public static String getLogFileName() {
		return WorkloadConf.get(Constants.WORKLOAD_NAME)
				+ Constants.DRIVER_LOG_SUFFIX;
	}

	public static String getWorkPath() {
		return WorkloadConf.get(Constants.WORKLOAD_OUTPUT_PATH);
	}

	public static void print(String log) {
		if ("true".equals(WorkloadConf.get(Constants.WORKLOAD_LOG))) {
			System.out.println(log);
		}
	}
}
