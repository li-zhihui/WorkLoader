package workload.spark.des;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandDes {
	String name;
	int startSkip = 0;
	List<ChartDes> chartDeses = new ArrayList<ChartDes>();
	List<GroupDes> groupDeses = new ArrayList<GroupDes>();

	public void setCommandName(String name){
		this.name = name;
	}
	public void setStartSkip(int startSkip){
		this.startSkip = startSkip;
	}
	public void setChartDeses(List<ChartDes> chartDeses){
		this.chartDeses = chartDeses;
	}
	public void setGroupDeses(List<GroupDes> groupDeses){
		this.groupDeses = groupDeses;
	}
	public String getCommandName(){
		return name;
	}
	public int getStartSkip(){
		return startSkip;
	}
	public List<ChartDes> getChartDes(){
		return chartDeses;
	}
	public List<GroupDes> getGroupDes(){
		return groupDeses;
	}
	
}
