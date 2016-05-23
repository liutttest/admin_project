package com.evan.finance.admin.activiti.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.evan.common.user.domain.JPage;




/**
 * 工作流控制接口
 * 
 * @author stone
 *
 */
public interface WorkFlowService {

	/**
	 * 部署流程, 目前只支持zip格式
	 * 
	 * @param processesKey流程key
	 * @return
	 */
	public String startDeployment(String processesKey, String fileName);

	
	/**
	 * 查询当前用户可以看到的所有待办任务
	 * 
	 * @param userId
	 * @param candidateGroups
	 * @return
	 */
	public Map<String, Object> findTodoTasks(String userId, List<String> candidateGroups,List<String> falseUser);
	
	

	/**
	 * 
	 * @param userName
	 * @param page
	 * @param suspensionState
	 * @return
	 */
	public JPage findTodoTasksSys(String userName, JPage page,String suspensionState);
	
	/**
	 * 任务转办 @liutt
	 * @param taskId
	 * @param personName
	 */
	public void turnTodoTask(String taskId,String personName);
	
	/**
	 * 根据业务id，当前节点，分配人查询任务
	 * @param definitionKey 业务id
	 * @param node 当前节点
	 * @param assgine 分配者
	 * @return
	 */
	public Task getTaskByDefineKetAndNode(String definitionKey,String node,String assgine);
}
