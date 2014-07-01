package workload.spark.chart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import workload.spark.Constants;
import workload.spark.Util;
import workload.spark.WorkloadConf;
import workload.spark.WorkloadContext;

public class XChart extends JSTChart {	
	@Override
	public void createChart() throws Exception {
		getChartBound();
		//readFile 
		String[] command = WorkloadConf.get(Constants.WORKLOAD_RUNNER_COMMAND).split(Constants.DATA_SPLIT);
		for(int i = 0; i < command.length; i++){
			int start = (Integer) WorkloadContext.get(command[i]+".des.start");
			List<String> slaves = Util.getSlavesHost();
			for (String slave : slaves) {
				int index = 0;
				String split = (String) WorkloadContext.get(command[i]+".des.split");
				List<List<Double>> totalData = readFile(slave + "_"+ command[i] +".dat",start,split);
				String commandData = (String) WorkloadContext.get(command[i] + ".des.data");
				String[] data = commandData.split("]");
				// new chartSource and call ChartUtil
				for(int j = 0; j < data.length; j++){
					String[] line = data[j].substring(1).split(":");
					if(line.length !=2){
						System.out.println("Des file: Wrong format");
						System.exit(1);
					}
					List<String> nameList = new ArrayList<String>();
					String chartName = line[0].split(",")[0].toUpperCase();
					String chartType = line[0].split(",")[1];
					String[] itemName = line[1].split(",");
					List<List<Double>> dataList = new ArrayList<List<Double>>();
					for(int k = 0 ; k < itemName.length; k++){
						nameList.add(itemName[k]);
					}
					for(int m = 0 ; m < totalData.size(); m++){
						List<Double> row = new ArrayList<Double>();
						for(int n=0 ; n < itemName.length;n++)
							row.add(totalData.get(m).get(n+index));
						dataList.add(row);
					}
					index += itemName.length;
					String yAxisName = "";
					if(chartType.equals("line")){
						yAxisName = "BYTE";
						ChartSource cs = new ChartSource(dataList,nameList,freq,chartName,"TIME(s)", yAxisName,jobMarker,stageMarker);
						outputGraph(slave+"_"+command[i] +"_"+ cs.chartName,ChartUtil.lineChart(cs),width,height);
					}
					else if(chartType.equals("stack")){
						yAxisName = "PERCENTAGE";
						ChartSource cs = new ChartSource(dataList,nameList,freq,chartName,"TIME(s)", yAxisName,jobMarker,stageMarker);
						outputGraph(slave+"_" + command[i] + "_"+cs.chartName,ChartUtil.stackChart(cs),width,height);
					}
					else {
						System.out.println("WRONG CHART TYPE: " + chartType);
						System.exit(1);
					}
				}
			}
			
		}	
	}
	
	private List<List<Double>> readFile(String file, int start,String split) throws Exception{
		FileReader fr = new FileReader(csvFolder + file);
		BufferedReader br = new BufferedReader(fr);
		List<List<Double>> data = new ArrayList<List<Double>>();
		String line;
		int i = 0; 
		while(i < start){
			br.readLine();
			i++;
		}
		
//		long time = (Long) WorkloadContext.get(Constants.WORKLOAD_RUNTIME) - startTime;
//		//discard records preceding the start of job
//		if(time < 0){
//			long count = time / Integer.parseInt((WorkloadConf.get(Constants.WORKLOAD_RUNNER_FREQUENCY)));
//			i = 0;
//			while( i < count){
//				br.readLine();
//				i++;
//			}	
//		}
		while(true){
			line = br.readLine();
			//System.out.println(line);
			if(line == null)
				break;
			StringTokenizer st = new StringTokenizer(line, split);
			List<Double> record = new ArrayList<Double>();
			while(st.hasMoreTokens()){
				String token = st.nextToken().trim();
				record.add(Double.parseDouble(token));
			}
			data.add(record);
		}
		br.close();
		return data;
	}	
}
