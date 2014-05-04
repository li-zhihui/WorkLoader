package com.intel.spark.log;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.intel.spark.log.processor.*;

public class Matcher {

	static Map<String, Processor> map = new HashMap<String, Processor>();
	static {
		map.put("(.*)Starting job(.*)", new JobStartProcessor());
		map.put("(.*)Submitting Stage(.*)", new StageStartProcessor());
		map.put("(.*)Starting task(.*)", new TaskStartProcessor());
		map.put("(.*)Submitting ([0-9]*) missing tasks from Stage(.*)",
				new TaskSetProcessor());
		map.put("(.*)Adding task set(.*)", new TaskSetAddProcessor());
		map.put("(.*)Starting task(.*)as TID(.*)", new TaskStartProcessor());
		map.put("(.*)TaskSetManager: Finished TID(.*)",
				new TaskFinishProcessor());
		map.put("(.*)DAGScheduler: Stage(.*)finished in(.*)",
				new StageFinishProcessor());
		map.put("(.*)Job finished(.*)", new JobFinishProcessor());
        map.put("(.*)BlockManagerMasterActor.BlockManagerInfo: Added rdd(.*)", new MemProcessor());
	}

	public static Processor build(String line) throws Exception {
		for (Entry<String, Processor> entry : map.entrySet()) {
			if (line.matches(entry.getKey())) {
				return entry.getValue().getClass().newInstance();
			}
		}
		return null;
	}
}
