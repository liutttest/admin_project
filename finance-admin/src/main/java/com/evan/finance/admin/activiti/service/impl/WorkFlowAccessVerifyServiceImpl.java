package com.evan.finance.admin.activiti.service.impl;

import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evan.finance.admin.activiti.bean.WorkFlowView;
import com.evan.finance.admin.activiti.service.WorkFlowAccessVerifyService;
import com.evan.finance.admin.utils.WorkFlowUtils;

@Service
public class WorkFlowAccessVerifyServiceImpl implements WorkFlowAccessVerifyService {

	@Autowired
	private ProcessEngine processEngine;
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
    protected TaskService taskService;

	@Autowired
    protected HistoryService historyService;

	@Autowired
    protected RepositoryService repositoryService;
	
	@Override
	public WorkFlowView startProcess(String businessKey) {
		Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
		if (task!=null && task.getProcessDefinitionId().indexOf(WorkFlowUtils.process_definition_key_access_verify)== 0) {
			WorkFlowView fcdFlowView = new WorkFlowView();
			fcdFlowView.setBusinessKey(businessKey);
			fcdFlowView.setProcessInstanceId(task.getProcessInstanceId());
			return fcdFlowView;
		}
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(WorkFlowUtils.process_definition_key_access_verify, businessKey);
		if (processInstance == null) {
			return null;
		}
		
		WorkFlowView fcdFlowView = new WorkFlowView();
		fcdFlowView.setBusinessKey(businessKey);
		fcdFlowView.setProcessInstanceId(processInstance.getProcessInstanceId());
		fcdFlowView.setProcessInstance(processInstance);
        return fcdFlowView;
	}

	@Override
	public void claim(String taskId, String userId) {
	    taskService.claim(taskId, userId);
	}

	@Override
	public void complete(String taskId, Map<String, Object> variables) {
		taskService.complete(taskId, variables);
	}

}
