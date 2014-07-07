package workload.spark.chart.preprocessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import workload.spark.Util;
import workload.spark.des.CommandDes;
import workload.spark.des.Regex;

public class XChartPreprocessor{

	/**
	 * Store data for each command as 4-D list
	 */
	String csvFolder;
	long startTime;
	List<Map<String,List<List<String>>>> dataList = new ArrayList<Map<String,List<List<String>>>>();
	
	public void setCSVFolder(String csvFolder){
		this.csvFolder = csvFolder;
	}
	
	public void setStartTime(long startTime){
		this.startTime = startTime;
	}
	
	private void readFile(CommandDes des,String currentSlave) throws Exception{
		System.out.println(csvFolder + currentSlave+"_" + des.getCommandName()+".dat");
		FileReader fr = new FileReader(csvFolder + currentSlave+"_" + des.getCommandName()+".dat");
		BufferedReader br = new BufferedReader(fr);
		String line;
		int i = 0; 
		while(i < des.getStartSkip()){
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
		line = br.readLine();
		while(true){
			if(line.equals("")){
				Map<String,List<List<String>>> map = new HashMap<String,List<List<String>>>();
				for(int m = 0; m < des.getGroupDes().size(); m++){
					List<List<String>> groupTable = new ArrayList<List<String>>();
					String split = des.getGroupDes().get(m).getSplit();
					Regex regex = des.getGroupDes().get(m).getRegex();
				  	String regexValue = des.getGroupDes().get(m).getRegexValue();
				  	line = br.readLine();
				  	if(checkRegex(line,regex,regexValue)){
				  			line = br.readLine();
				  			while(line != null&& !line.equals("") ){
				  				List<String> record = Util.getList(line, split);
				  				line = br.readLine();
				  				groupTable.add(record);
				  			}
				  	}
				  	else{
				  		br.close();
				  		throw new Exception("Wrong regex: " + regex);
				  	}
				  	map.put(des.getGroupDes().get(m).getGroupName(), groupTable);
				}
				dataList.add(map);
			}
			else if(des.getGroupDes().get(0).getGroupName().equals("null")){
				Map<String,List<List<String>>> map = new HashMap<String,List<List<String>>>();
				List<List<String>> table = new ArrayList<List<String>>();
				String split = des.getGroupDes().get(0).getSplit();
				List<String> record = Util.getList(line, split);
				table.add(record);
				map.put("null",table);
				line = br.readLine();
				dataList.add(map);
			}
			else{
				br.close();
				throw new Exception("Cannot process the file format");
			}
			if(line == null){
				br.close();
				break;
			}
				
		}
		br.close();	
	}
	
	private boolean checkRegex(String line, Regex regex, String regexValue) {
		if(regex.equals(Regex.indexOf) && line.indexOf(regexValue)!= -1)
			return true;
		else if(regex.equals(Regex.startWith)&&line.startsWith(regexValue))
			return true;
		return false;
	}
	
	public  List<Map<String, List<List<String>>>> getDataList(CommandDes cd,String slave) throws Exception{
				readFile(cd, slave);
				System.out.println("dataList Size: " + dataList.size());
//				for(int i = 0; i < dataList.size(); i++){
//					Map<String,List<List<String>>> map = dataList.get(i);
//					for(int j = 0 ; j < cd.getGroupDes().size(); j++){
//						System.out.println("Group Name: " + cd.getGroupDes().get(j).getGroupName());
//						List<List<String>> table = map.get(cd.getGroupDes().get(j).getGroupName());
//						for(int m = 0; m < table.size(); m++){
//							List<String> record = table.get(m);
//							for(int n = 0 ; n < table.get(0).size(); n++)
//								System.out.print(record.get(n) + " ");
//							System.out.println();
//						}
//					}
//				}
			return dataList;
	}
}

