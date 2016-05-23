package com.evan.finance.admin.activiti.bean;

import java.io.Serializable;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.evan.common.user.domain.FwBusinessTalks;

/**
 * 商务洽谈实体
 * 
 * @author stone
 *
 */
public class NegotiateFlowView implements Serializable ,Comparable<NegotiateFlowView>{

	private static final long serialVersionUID = -1112932544904096326L;
	private String processInstanceId;
	private String businessKey;
	private FwBusinessTalks fwBusinessTalks;
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

	public FwBusinessTalks getFwBusinessTalks() {
		return fwBusinessTalks;
	}

	public void setFwBusinessTalks(FwBusinessTalks fwBusinessTalks) {
		this.fwBusinessTalks = fwBusinessTalks;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	@Override
	public int compareTo(NegotiateFlowView o) {
		return this.getFwBusinessTalks().getCreateTime().compareTo(o.getFwBusinessTalks().getCreateTime());
	}

}
