package com.evan.finance.admin.activiti.service;

import java.util.Map;

public interface WorkFlowInstanceService {

	public int activateProcessInstance(String procInsId);
	
	public int suspendProcessInstance(String procInsId);
	
	public Map<String, Object> startProcess(String busId, String userId, Map<String, Object> vars);
	
	public int completeToAccess(String procInsId, String busId, String comName);
	
	public int completeToMark(String procInsId, String busId, String comName);
}
