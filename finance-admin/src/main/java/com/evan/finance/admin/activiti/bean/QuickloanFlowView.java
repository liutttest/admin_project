package com.evan.finance.admin.activiti.bean;

import java.io.Serializable;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.evan.common.user.domain.FwQuickLoan;

/**
 * 快速申请贷款实体
 * 
 * @author stone
 *
 */
public class QuickloanFlowView implements Serializable ,Comparable<QuickloanFlowView>{

	private static final long serialVersionUID = -1112932544904096326L;
	private String processInstanceId;
	private String businessKey;
	private FwQuickLoan fwQuickLoan;
	private String comName;

	// 运行中的流程实例
	private ProcessInstance processInstance;
	// 流程定义
	private ProcessDefinition processDefinition;
	// 流程任务
	private Task task;
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

	public FwQuickLoan getFwQuickLoan() {
		return fwQuickLoan;
	}

	public void setFwQuickLoan(FwQuickLoan fwQuickLoan) {
		this.fwQuickLoan = fwQuickLoan;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	@Override
	public int compareTo(QuickloanFlowView o) {
		return this.getFwQuickLoan().getCreateTime().compareTo(o.getFwQuickLoan().getCreateTime());
	}

}
