package workload.spark.run;

public class RunnerFactory {
	public static Runner getRunner() {
		return new LinuxRunner();
	}
}
