package com.evan.finance.admin.activiti.bean;

import java.io.Serializable;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.evan.common.user.domain.FwBusinessSxd;

/**
 * 睡信贷流程实体
 * 
 * @author stone
 *
 */
public class WorkFlowView implements Serializable,Comparable<WorkFlowView> {

	private static final long serialVersionUID = -1112932544904096326L;
	private String processInstanceId;
	private String businessKey;
	private FwBusinessSxd fwBusinessSxd;
	private String comName;
	private String flowType;
	private String loanId; 

	// 运行中的流程实例
	private ProcessInstance processInstance;
	// 流程定义
	private ProcessDefinition processDefinition;
	// 流程任务
	private Task task;
	
	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

	// 历史的流程实例
	private HistoricProcessInstance historicProcessInstance;

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public HistoricProcessInstance getHistoricProcessInstance() {
		return historicProcessInstance;
	}

	public void setHistoricProcessInstance(
			HistoricProcessInstance historicProcessInstance) {
		this.historicProcessInstance = historicProcessInstance;
	}

	public FwBusinessSxd getFwBusinessSxd() {
		return fwBusinessSxd;
	}

	public void setFwBusinessSxd(FwBusinessSxd fwBusinessSxd) {
		this.fwBusinessSxd = fwBusinessSxd;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	@Override
	public int compareTo(WorkFlowView o) {
		// TODO Auto-generated method stub
		return o.getTask().getCreateTime().compareTo(this.getTask().getCreateTime());
	}

}
