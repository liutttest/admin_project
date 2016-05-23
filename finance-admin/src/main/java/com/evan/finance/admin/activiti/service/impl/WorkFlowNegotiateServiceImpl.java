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

import com.evan.finance.admin.activiti.bean.NegotiateFlowView;
import com.evan.finance.admin.activiti.service.WorkFlowNegotiateService;
import com.evan.finance.admin.utils.WorkFlowUtils;

@Service
public class WorkFlowNegotiateServiceImpl implements WorkFlowNegotiateService {

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
	public NegotiateFlowView startProcess(String businessKey) {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(WorkFlowUtils.process_definition_key_negotiate, businessKey);
		if (processInstance == null) {
			return null;
		}
		
		NegotiateFlowView negotiateFlowView = new NegotiateFlowView();
		negotiateFlowView.setBusinessKey(businessKey);
		negotiateFlowView.setProcessInstanceId(processInstance.getProcessInstanceId());
		negotiateFlowView.setProcessInstance(processInstance);
        return negotiateFlowView;
	}

	@Override
	public List<NegotiateFlowView> findRunningProcessInstaces() {
        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery().processDefinitionKey(WorkFlowUtils.process_definition_key_negotiate).active().orderByProcessInstanceId().desc();
        List<ProcessInstance> list = query.list();

        List<NegotiateFlowView> negotiateFlowViews = new ArrayList<NegotiateFlowView>();
        // 关联业务实体
        for (ProcessInstance processInstance : list) {
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            NegotiateFlowView negotiateFlowView = new NegotiateFlowView();
            negotiateFlowView.setProcessInstance(processInstance);
            negotiateFlowView.setBusinessKey(businessKey);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
            negotiateFlowView.setProcessDefinition(processDefinition);
            // TODO 当前业务数据
            negotiateFlowView.setComName("天文科技");
            // 设置当前任务信息
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().orderByTaskCreateTime().desc().listPage(0, 1);
            negotiateFlowView.setTask(tasks.get(0));
            negotiateFlowViews.add(negotiateFlowView);
        }

		return negotiateFlowViews;
	}

	@Override
	public void claim(String taskId, String userId) {
	    taskService.claim(taskId, userId);
	}

	@Override
	public List<NegotiateFlowView> findTodoTasks(String userId, List<String> candidateGroups) {
		TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(WorkFlowUtils.process_definition_key_negotiate).taskCandidateGroupIn(candidateGroups);
        List<Task> tasks = taskQuery.list();
        
        taskQuery = taskService.createTaskQuery().processDefinitionKey(WorkFlowUtils.process_definition_key_negotiate).taskCandidateOrAssigned(userId);
        tasks.addAll(taskQuery.list());
        
        List<NegotiateFlowView> negotiateFlowViews = new ArrayList<NegotiateFlowView>();
        // 根据流程的业务ID查询实体并关联
        for (Task task : tasks) {
        	String processInstanceId = task.getProcessInstanceId();
        	ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
        	String businessKey = processInstance.getBusinessKey();
        	if (businessKey == null) {
        		continue;
        	}
        	NegotiateFlowView negotiateFlowView = new NegotiateFlowView();
        	negotiateFlowView.setBusinessKey(businessKey);
        	// TODO 当前业务数据
        	negotiateFlowView.setTask(task);
        	negotiateFlowView.setComName("天文科技");
        	negotiateFlowView.setProcessInstance(processInstance);
        	ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
        	negotiateFlowView.setProcessDefinition(processDefinition);
        	negotiateFlowViews.add(negotiateFlowView);
        }
		return negotiateFlowViews;
	}

	@Override
	public void complete(String taskId, Map<String, Object> variables) {
		taskService.complete(taskId, variables);
	}

}
