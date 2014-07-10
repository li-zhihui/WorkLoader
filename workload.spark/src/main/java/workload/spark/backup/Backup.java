package workload.spark.backup;

import workload.spark.Constants;
import workload.spark.WorkloadConf;

public class Backup {

	public static void backup() throws Exception {
               if (WorkloadConf.get(Constants.WORKLOAD_EXECUTE_BACKUP).equals("false")) {
                       return;
               }

		Runtime run = Runtime.getRuntime();
		String backupName = WorkloadConf.get(Constants.WORKLOAD_NAME) + "_"
				+ System.currentTimeMillis() + "/";
		String backupPath = WorkloadConf.get(Constants.WORKLOAD_BACKUP_PATH)
				+ backupName;
		String webPath = "../workload.web/src/main/webapp/LogPic";
		run.exec("mkdir " + backupPath).waitFor();
		run.exec("mkdir" + webPath).waitFor();
		String cmdS = "/bin/mv "
				+ WorkloadConf.get(Constants.WORKLOAD_OUTPUT_PATH) + "*.* "
				+ backupPath ;
		String[] cmd = { "/bin/sh", "-c", cmdS };
		System.out.println(cmdS);
		Process p = run.exec(cmd);
		if (p.waitFor() != 0) {
			throw new Exception("backup failed.");
		}
		
		cmdS = "bin/mv "+ WorkloadConf.get(Constants.WORKLOAD_OUTPUT_PATH)+"*.* "+webPath ;
		System.out.println(cmdS);
		String[] cmdW = { "/bin/sh", "-c", cmdS };
		p = run.exec(cmdW);
		if (p.waitFor() != 0) {
			throw new Exception("web copy failed.");
		}
	}
}
