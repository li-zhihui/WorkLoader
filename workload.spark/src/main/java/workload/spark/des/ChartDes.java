package workload.spark.des;

import java.util.List;

public class ChartDes {

	String chartName;
	ChartGroupByType groupByType;
	ChartType chartType;
	String groupByName = null;
	String groupByValue = null;
	List<String> colName;

	public void setChartName(String chartName){
		this.chartName = chartName;
	}
	public void setGroupByName(String groupByName){
		this.groupByName = groupByName;
	}
	public void setGroupByValue(String groupByValue){
		this.groupByValue = groupByValue;
	}
	public void setColName(List<String> colName){
		this.colName = colName;
	}
	public void setChartGroupByType(ChartGroupByType groupByType){
		this.groupByType = groupByType;
	}
	public void setChartType(ChartType chartType){
		this.chartType = chartType;
	}
}
