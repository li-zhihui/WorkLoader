package workload.spark.data;

public class LoaderFactory {
	public static Loader getLoader() {
		return new LinuxLoader();
	}
}
