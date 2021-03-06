package com.evan.finance.admin.activiti.bean;

import java.io.Serializable;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.evan.common.user.domain.FwRepayment;

/**
 * 还款流程实体
 * 
 * @author stone
 *
 */
public class RepaymentFlowView implements Serializable,Comparable<RepaymentFlowView> {

	private static final long serialVersionUID = -1801450825057750554L;
	private String processInstanceId;
	private String businessKey;

	// TODO 没有引入放款业务实体
	private FwRepayment fwRepayment;

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

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}


	public FwRepayment getFwRepayment() {
		return fwRepayment;
	}

	public void setFwRepayment(FwRepayment fwRepayment) {
		this.fwRepayment = fwRepayment;
	}

	@Override
	public int compareTo(RepaymentFlowView o) {
		// TODO Auto-generated method stub
		return this.getFwRepayment().getCreateTime().compareTo(o.getFwRepayment().getCreateTime());
	}
	

}
