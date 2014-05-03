package workload.spark.ui;

public class Cli {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.print("Please input conf file path and workload path");
			System.exit(1);
		}
		Executor exe = new Executor();
		try {
			exe.execute(args[0],args[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
