package workload.spark.chart;

public interface JobChart {
	void draw() throws Exception;

	Long getStartTime();
	double[][] getMarker();
    void setCsvFolder(String csvFolder);
    void setJpgFolder(String jpgFolder);
}
