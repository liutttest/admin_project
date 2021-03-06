package com.evan.finance.admin.activiti.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evan.finance.admin.activiti.bean.WorkFlowView;
import com.evan.finance.admin.activiti.service.WorkFlowQuotaApplyService;
import com.evan.finance.admin.utils.WorkFlowUtils;

@Service
public class WorkFlowQuotaApplyServiceImpl implements WorkFlowQuotaApplyService{

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
		if (task!=null && task.getProcessDefinitionId().indexOf(WorkFlowUtils.process_definition_key_quota_apply)== 0) {
			WorkFlowView fcdFlowView = new WorkFlowView();
			fcdFlowView.setBusinessKey(businessKey);
			fcdFlowView.setProcessInstanceId(task.getProcessInstanceId());
			return fcdFlowView;
		}
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(WorkFlowUtils.process_definition_key_quota_apply, businessKey);
		if (processInstance == null) {
			return null;
		}
		
		WorkFlowView quickloanFlowView = new WorkFlowView();
		quickloanFlowView.setBusinessKey(businessKey);
		quickloanFlowView.setProcessInstanceId(processInstance.getProcessInstanceId());
		quickloanFlowView.setProcessInstance(processInstance);
        return quickloanFlowView;
	}

	@Override
	public List<WorkFlowView> findRunningProcessInstaces() {
        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery().processDefinitionKey(WorkFlowUtils.process_definition_key_quickloan).active().orderByProcessInstanceId().desc();
        List<ProcessInstance> list = query.list();

        List<WorkFlowView> quickloanFlowViews = new ArrayList<WorkFlowView>();
        // 关联业务实体
        for (ProcessInstance processInstance : list) {
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            WorkFlowView quickloanFlowView = new WorkFlowView();
            quickloanFlowView.setProcessInstance(processInstance);
            quickloanFlowView.setBusinessKey(businessKey);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
            quickloanFlowView.setProcessDefinition(processDefinition);
            // TODO 当前业务数据
            quickloanFlowView.setComName("卢新科技");
            // 设置当前任务信息
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().orderByTaskCreateTime().desc().listPage(0, 1);
            quickloanFlowView.setTask(tasks.get(0));
            quickloanFlowViews.add(quickloanFlowView);
        }

		return quickloanFlowViews;
	}

	@Override
	public void claim(String taskId, String userId) {
	    taskService.claim(taskId, userId);
	}

	@Override
	public List<WorkFlowView> findTodoTasks(String userId, List<String> candidateGroups) {
		TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(WorkFlowUtils.process_definition_key_quickloan).taskCandidateGroupIn(candidateGroups);
        List<Task> tasks = taskQuery.list();
        
        taskQuery = taskService.createTaskQuery().processDefinitionKey(WorkFlowUtils.process_definition_key_quickloan).taskCandidateOrAssigned(userId);
        tasks.addAll(taskQuery.list());
        
        List<WorkFlowView> quickloanFlowViews = new ArrayList<WorkFlowView>();
        // 根据流程的业务ID查询实体并关联
        for (Task task : tasks) {
        	String processInstanceId = task.getProcessInstanceId();
        	ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
        	String businessKey = processInstance.getBusinessKey();
        	if (businessKey == null) {
        		continue;
        	}
        	WorkFlowView quickloanFlowView = new WorkFlowView();
        	quickloanFlowView.setBusinessKey(businessKey);
        	// TODO 当前业务数据
        	quickloanFlowView.setTask(task);
        	quickloanFlowView.setComName("卢新科技");
        	quickloanFlowView.setProcessInstance(processInstance);
        	ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
        	quickloanFlowView.setProcessDefinition(processDefinition);
        	quickloanFlowViews.add(quickloanFlowView);
        }
		return quickloanFlowViews;
	}

	@Override
	public void complete(String taskId, Map<String, Object> variables) {
		taskService.complete(taskId, variables);
	}

}
