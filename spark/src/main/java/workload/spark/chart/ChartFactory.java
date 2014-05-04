package workload.spark.chart;

public class ChartFactory {
	public static SparkChart getChart() {
		return new JFreeSparkChart();
	}
}
