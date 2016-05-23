package com.evan.finance.admin.activiti.bean;

import java.io.Serializable;

public class Historic implements Serializable, Comparable<WorkFlow> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String activityName;
	private String activityId;
	private String startTime;
	private String endTime;

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public int compareTo(WorkFlow o) {
		return 0;
	}

}
