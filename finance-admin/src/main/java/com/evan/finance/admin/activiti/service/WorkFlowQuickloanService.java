package com.evan.finance.admin.activiti.service;

import java.util.List;
import java.util.Map;

import com.evan.finance.admin.activiti.bean.QuickloanFlowView;

/**
 * 快速贷款申请流程控制
 * 
 * @author stone
 *
 */
public interface WorkFlowQuickloanService {

	/**
	 * 启动流程
	 * 
	 * @param businessKey业务ID
	 * @return Map<String, Object>
	 */
	public QuickloanFlowView startProcess(String businessKey);

	/**
	 * 读取运行中的流程,当前流程下所有的
	 * 
	 * @return 读取运行中的流程
	 */
	public List<QuickloanFlowView> findRunningProcessInstaces();

	/**
	 * 签收任务
	 * 
	 * @param userId
	 */
	public void claim(String taskId, String userId);

	/**
	 * 查询待办任务
	 * 
	 * @param userId
	 * @return
	 */
	public List<QuickloanFlowView> findTodoTasks(String userId,
			List<String> candidateGroups);

	/**
	 * 完成任务
	 * 
	 * @param taskId
	 * @param variables
	 */
	public void complete(String taskId, Map<String, Object> variables);

}
