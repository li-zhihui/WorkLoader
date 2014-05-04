package com.intel.spark.log.model;

import com.intel.spark.log.common.MemInfo;

import java.util.HashMap;
import java.util.Map;

public class App extends Node<Job, Object> {
	public String CURRENT_TASK_SET = "currentTaskSet";
	public String CURRENT_JOB = "currentJob";// FIXME assume job is NOT
												// concurrent.
    private Map<String,MemInfo> memMap = new HashMap<String,MemInfo>();

    public Map<String, MemInfo> getMemMap() {
        return memMap;
    }

	private Map<String, Node> context = new HashMap<String, Node>();

	public Map<String, Node> getContext() {
		return context;
	}

	public void setContext(Map<String, Node> context) {
		this.context = context;
	}

    public void addRddMem(String nodeId, String rddId, float memSize, long time) {
        if (memMap.containsKey(nodeId)) {
            memMap.get(nodeId).addRddMemInfo(rddId, time, memSize);
        } else {
            memMap.put(nodeId, new MemInfo(rddId, time, memSize));
        }
    }

}
