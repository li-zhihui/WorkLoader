package workload.spark.chart;


/**
 * Read CSV files, then output JPG files.
 * 
 */
public abstract class SparkChart {
	String csvFolder;
	String jpgFolder;

	public abstract void createChart() throws Exception;

	public void setCsvFolder(String csvFolder) {
		this.csvFolder = csvFolder;
	}

	public void setJpgFolder(String jpgFolder) {
		this.jpgFolder = jpgFolder;
	}
}
