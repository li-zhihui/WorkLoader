package workload.spark;

public class Constants {
	public static final String DSTAT_FILE = "_dstat.csv";
	public static final String JOB_NAME = "job";
	public static final String STAGE_NAME = "stage";
	public static final String TASK_NAME = "task";
	public static final String CSV_SUFFIX = ".csv";
	public static final String GRAPH_SUFFIX = ".jpg";
	public static final String DATA_SPLIT = ",";

	/************* WorkloadEnv *************/
	public static final String SPARK_HOME = "spark.home";
	public static final String WORKLOAD_NAME = "workload.name";
	public static final String WORKLOAD_WORKDIR = "workload.workdir";
	public static final String WORKLOAD_STEP_RUNNER = "workload.step.runner";
	public static final String WORKLOAD_STEP_PARSER = "workload.step.parser";
	public static final String WORKLOAD_STEP_LOADER = "workload.step.loader";
	public static final String WORKLOAD_STEP_CLEANER = "workload.step.cleaner";
	public static final String WORKLOAD_STEP_CHART = "workload.step.chart";
	public static final String WORKLOAD_STEP_REPORT = "workload.step.report";
	public static final String WORKLOAD_OUTPUT_PATH = "workload.output.path";
	public static final String WORKLOAD_BACKUP_PATH = "workload.backup.path";
	public static final String WORKLOAD_CONF_PREFIX = "workload";
	public static final String WORKLOAD_PATH_SUFFIX = "path";
	public static final String WORKLOAD_RUN_SUFFIX = "run";
	public static final String WORKLOAD_EXECUTE_BACKUP = "workload.execute.backup";

	/**************** Runtime *****************/
	public static final String DRIVER_LOG_SUFFIX = "log.log";
	public static final String DRIVER_CSV_PAHT = "/tmp";
	public static final String WORKLOAD_LOG = "workload.log";
	
	/**************** other conf********************/
	public static final String DSTAT_SAMPLE_FREQ = "dstat.sample.freq";
}
