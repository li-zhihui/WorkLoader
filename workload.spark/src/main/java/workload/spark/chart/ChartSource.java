package workload.spark.chart;

import java.util.List;

public class ChartSource {
	List<List<Double>> dataList;
	List<String> dataNameList;
	int freq;
	String chartName = "";
	String xAxisName = "Time(s)";
	String yAxisName;
	double[] jobMarker;
	double[] stageMarker;
	//long startTime;
	
	
	public ChartSource(List<List<Double>>dataList,List<String> nameList,int freq,String chartName,String xAxisName,String yAxisName,double[] jobMarker,double[] stageMarker) {
		this.dataList = dataList;
		this.dataNameList = nameList;
		this.freq = freq;
		this.chartName = chartName;
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
		this.jobMarker = jobMarker;
		this.stageMarker = stageMarker;
	}

}
