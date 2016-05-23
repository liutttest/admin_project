package com.evan.finance.admin.activiti.service;

import java.util.Map;

import com.evan.finance.admin.activiti.bean.WorkFlowView;

/**
 * 准入验证流程控制
 * 
 * @author liutt
 *
 */
public interface WorkFlowFeildAccountService {

	/**
	 * 启动流程
	 * 
	 * @param businessKey业务ID
	 * @return Map<String, Object>
	 */
	public WorkFlowView startProcess(String businessKey);

	/**
	 * 签收任务
	 * 
	 * @param userId
	 */
	public void claim(String taskId, String userId);

	/**
	 * 完成任务
	 * 
	 * @param taskId
	 * @param variables
	 */
	public void complete(String taskId, Map<String, Object> variables);

}
