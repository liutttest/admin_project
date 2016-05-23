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

import com.evan.finance.admin.activiti.bean.LoanFlowView;
import com.evan.finance.admin.activiti.bean.WorkFlowView;
import com.evan.finance.admin.activiti.service.WorkFlowLoanService;
import com.evan.finance.admin.utils.WorkFlowUtils;

@Service
public class WorkFlowLoanServiceImpl implements WorkFlowLoanService {

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
	public LoanFlowView startProcess(String businessKey) {
		TaskQuery tasks = taskService.createTaskQuery().processInstanceBusinessKey(businessKey);
		for (Task task : tasks.list()) {
			if (task!=null && task.getProcessDefinitionId().indexOf(WorkFlowUtils.process_definition_key_loan)== 0) {
				LoanFlowView loanFlowView = new LoanFlowView();
				loanFlowView.setBusinessKey(businessKey);
				loanFlowView.setProcessInstanceId(task.getProcessInstanceId());
				return loanFlowView;
			}
		}
		
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(WorkFlowUtils.process_definition_key_loan, businessKey);
		if (processInstance == null) {
			return null;
		}
		
		LoanFlowView loanFlowView = new LoanFlowView();
		loanFlowView.setBusinessKey(businessKey);
		loanFlowView.setProcessInstanceId(processInstance.getProcessInstanceId());
		loanFlowView.setProcessInstance(processInstance);
        return loanFlowView;
	}

	@Override
	public List<LoanFlowView> findRunningProcessInstaces() {
        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery().processDefinitionKey(WorkFlowUtils.process_definition_key_loan).active().orderByProcessInstanceId().desc();
        List<ProcessInstance> list = query.list();

        List<LoanFlowView> loanFlowViews = new ArrayList<LoanFlowView>();
        // 关联业务实体
        for (ProcessInstance processInstance : list) {
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            LoanFlowView loanFlowView = new LoanFlowView();
            loanFlowView.setProcessInstance(processInstance);
            loanFlowView.setBusinessKey(businessKey);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
            loanFlowView.setProcessDefinition(processDefinition);
            // TODO 当前业务数据
            loanFlowView.setComName("天文科技");
            // 设置当前任务信息
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().orderByTaskCreateTime().desc().listPage(0, 1);
            loanFlowView.setTask(tasks.get(0));
            loanFlowViews.add(loanFlowView);
        }

		return loanFlowViews;
	}

	@Override
	public void claim(String taskId, String userId) {
	    taskService.claim(taskId, userId);
	}

	@Override
	public List<LoanFlowView> findTodoTasks(String userId, List<String> candidateGroups) {
		TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(WorkFlowUtils.process_definition_key_loan).taskCandidateGroupIn(candidateGroups);
        List<Task> tasks = taskQuery.list();
        
        taskQuery = taskService.createTaskQuery().processDefinitionKey(WorkFlowUtils.process_definition_key_loan).taskCandidateOrAssigned(userId);
        tasks.addAll(taskQuery.list());
        
        List<LoanFlowView> loanFlowViews = new ArrayList<LoanFlowView>();
        // 根据流程的业务ID查询实体并关联
        for (Task task : tasks) {
        	String processInstanceId = task.getProcessInstanceId();
        	ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
        	String businessKey = processInstance.getBusinessKey();
        	if (businessKey == null) {
        		continue;
        	}
        	LoanFlowView loanFlowView = new LoanFlowView();
        	loanFlowView.setBusinessKey(businessKey);
        	// TODO 当前业务数据
        	loanFlowView.setTask(task);
        	loanFlowView.setComName("天文科技");
        	loanFlowView.setProcessInstance(processInstance);
        	ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
        	loanFlowView.setProcessDefinition(processDefinition);
        	loanFlowViews.add(loanFlowView);
        }
		return loanFlowViews;
	}

	@Override
	public void complete(String taskId, Map<String, Object> variables) {
		taskService.complete(taskId, variables);
	}

}
