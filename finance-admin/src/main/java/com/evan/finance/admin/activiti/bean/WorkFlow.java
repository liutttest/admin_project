package com.evan.finance.admin.activiti.bean;

import java.io.Serializable;

public class WorkFlow implements Serializable, Comparable<WorkFlow> {

	private static final long serialVersionUID = 1L;
	private String taskId; // 任务编号
	private String taskName; // 任务名称
	private String taskDefKey; // 任务定义Key（任务环节标识）
	private String procInsId; // 流程实例ID
	private String procDefId; // 流程定义ID
	private String procDefKey; // 流程定义Key（流程定义标识）
	private String businessTable; // 业务绑定Table
	private String businessId; // 业务绑定ID
	private String title; // 任务标题
	private String status; // 任务状态
	private String comment; // 任务意见
	private String flag; // 意见状态
	private String nodeName;//节点名称
	
	private String assignee; // 任务执行人编号
	private String assigneeName; // 任务执行人名称
	private Historic histIns;	//历史活动任务

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public String getProcDefKey() {
		return procDefKey;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}

	public String getBusinessTable() {
		return businessTable;
	}

	public void setBusinessTable(String businessTable) {
		this.businessTable = businessTable;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}
	

	public Historic getHistIns() {
		return histIns;
	}

	public void setHistIns(Historic histIns) {
		this.histIns = histIns;
	}

	@Override
	public int compareTo(WorkFlow o) {
		return 0;
	}

}
