package workload.spark.ui.report;

public class ReportFactory {
	public static void getReport() {
		(new HtmlReport()).buildReport();
	}
}
