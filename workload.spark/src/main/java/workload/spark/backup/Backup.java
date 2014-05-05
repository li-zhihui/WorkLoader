package workload.spark.backup;

import java.io.IOException;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class Backup {

	public static void backup() throws Exception {
		Runtime run = Runtime.getRuntime();
		String backupName = WorkloadConf.get(Constants.WORKLOAD_NAME) + "_"
				+ System.currentTimeMillis() + "/";
		String backupPath = WorkloadConf.get(Constants.WORKLOAD_BACKUP_PATH)
				+ backupName;
		run.exec("mkdir " + backupPath).waitFor();
		String cmdS = "/bin/cp " + WorkloadConf.get(Constants.WORKLOAD_OUTPUT_PATH)
		+ "*.* " + backupPath;
		String[] cmd = { "/bin/sh","-c",cmdS};
		System.out.println(cmdS);
		Process p = run.exec(cmd);
		if (p.waitFor() != 0) {
			throw new Exception("backup failed.");
		}
	}
}
