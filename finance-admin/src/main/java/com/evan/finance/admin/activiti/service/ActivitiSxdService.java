package com.evan.finance.admin.activiti.service;

import java.util.List;
import java.util.Map;

import com.evan.common.user.domain.FaRisk;
import com.evan.finance.admin.activiti.bean.WorkFlow;

public interface ActivitiSxdService {
	/**
	 * 启动流程
	 * 
	 * @param businessKey业务ID
	 * @return Map<String, Object>
	 */
	public WorkFlow startProcess(String businessKey);

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
	
	/**
	 * @liutt
	 * 查询待办任务(税信贷 业务相关)
	 * @param userId
	 * @param candidateGroups
	 * @param falseUser
	 * @return
	 */
	public List<Map<String, Object>> findTodoTasks(String userId, List<String> candidateGroups,List<String> falseUser,int pageSize, int currentPage,String searchString,String suspensionState);

	/**
	 * @liutt
	 * 查询待办任务数据总条数(税信贷)
	 * @param userId
	 * @param candidateGroups
	 * @param falseUser
	 * @param pageSize
	 * @param currentPage
	 * @param searchString 公司名称 
	 * @param suspensionState 下拉选 任务状态
	 * @return
	 */
	public long findTodoTasksCount(String userId, List<String> candidateGroups,List<String> falseUser,int pageSize, int currentPage,String searchString,String suspensionState);
	
	/**
	 * @liutt
	 * 人工准入验证通过之后生成pdf  并且将其存到数据库中
	 * @param busId
	 * @return
	 *//*
	private void accessCreateBankFile(String busId,String processInstanceId);*/
	
	/**
	 * @liutt
	 * 根据业务id和流程id 将额度申请时 需要向银行上传的文件存到数据库中
	 * @param busId
	 * @param processInstanceId
	 *//*
	public void quotaCreateBankFile(String busId,String processInstanceId);*/
	
	/**
	 * @liutt
	 * 人工准入验证 做业务处理 及工作流 办理
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见  1--通过，2---不通过，3---驳回
	 * @param map 前台参数 页面选择的选项 参数
	 */
	public String doAccessVerify(FaRisk faRisk,WorkFlow workFlow,String nodeKey,String condition,Map<String, Object> map,String isSendEmail,String emailTitle,String emailContent,String isSendSms,String smsContent,String isSendMessage,String messageTitle,String messageContent) throws Exception;
	
	/**
	 * @liutt
	 * 准入验证通过发送通知
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 * @param map 前台参数
	 */
	public void accessSendMsg(Map<String, Object> map,WorkFlow workFlow,Long userId,String isSendEmail,String emailTitle,String emailContent,String isSendSms,String smsContent,String isSendMessage,String messageTitle,String messageContent) throws Exception;
	
	/**
 	 * @liutt
	 * 发送通知(手动)
	 * @param map 发送内容的参数
	 * @param businessId 业务id
	 * @param title 记录发送日志的标题
	 * @param isAppendInfoNopass 是否发送不合格清单
	 * @return
	 * @throws Exception
	 */
	/*public Map<String, Object> sendMsg(Map<String, Object> map,String businessId,String title,boolean isAppendInfoNopass) throws Exception;*/
	
	/**
	 * @liutt
	 * 发送通知(自动)
	 * @param scnceKey 场景
	 * @param businessId 业务id
	 * @param isAppendInfoNopass 是否发送不合格清单
	 * @param title 标题
	 */
	public void autoSendMsg(String scnceKey,String title,String businessId,boolean isAppendInfoNopass)throws Exception;
	
	
	/**
	 * @liutt
	 * 额度申请人工复核几点办理
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 * @param map 前台参数
	 */
	public  void doQuotaReview(WorkFlow workFlow,String nodeKey,String condition,Map<String, Object> map,String isSendEmail,String emailTitle,String emailContent,String isSendSms,String smsContent,String isSendMessage,String messageTitle,String messageContent) throws Exception;
	
	/**
	 * @liutt
	 * 人工核准授信额度节点办理
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 * @param quota 授信额度
	 */
	public  void mranualCreditLimit(WorkFlow workFlow,String nodeKey,String condition,String quota) throws Exception;

	/**
	 * @liutt
	 * 额度申请--审批节点办理
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 */
	public void xaminationApproval(WorkFlow workFlow,String nodeKey,String condition) throws Exception;


	/**
	 * @liutt
	 * 额度申请--审批节点办理
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 * @param map 前台参数
	 */
	public void quotaManagerApproval(WorkFlow workFlow,String nodeKey,String condition) throws Exception;

	/**
	 * @liutt
	 * 额度申请通过发送通知
	 * @param workFlow 接收前台参数的对象
	 * @param fieldTime 开户时间
	 * @param fieldAddr 开户地点
	 */
	public void quotaSendMsg(String fieldTime,String fieldAddr,WorkFlow workFlow,String isSendEmail,String emailTitle,String emailContent,String isSendSms,String smsContent,String isSendMessage,String messageTitle,String messageContent) throws Exception;

	/**
	 * @liutt
	 * 现场开户--任务分配
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 * @param taskPerson 任务分配人员
	 */
	public void taskAssign(WorkFlow workFlow,String taskPerson) throws Exception;
	
	/**
	 * @liutt
	 * 现场开户--信息回填
	 * @param workFlow
	 * @param contractNum 合同号
	 * @param serviceChargeState 是否收取服务费
	 * @param isInvoicing 是否开具发票
	 * @param serviceChargeActual 实际收取服务费
	 * @param userName 办理人
	 * @throws Exception
	 */
	public void dataBackfill(WorkFlow workFlow,String contractNum,String serviceChargeState,String isInvoicing,String serviceChargeActual,String userName) throws Exception;

	/**
	 * @liutt
	 * 现场开户--归档
	 * @param workFlow 接收前台参数的对象
	 * @param archive 档案号
	 */
	public void businessArchive(WorkFlow workFlow, String archive) throws Exception;
	
	/**
	 * @liutt
	 * 客服发送业务失败通知
	 * @param workFlow
	 * @param map
	 * @throws Exception
	 */
	public void sendFailMsg(WorkFlow workFlow,Map<String, Object> map,String isSendEmail,String emailTitle,String emailContent,String isSendSms,String smsContent,String isSendMessage,String messageTitle,String messageContent) throws Exception;

	/**
	 * 任务转办 @liutt
	 * @param taskId
	 * @param personName
	 */
	public void turnTodoTask(String taskId,String personName);
	
	/**
	 * 查询待办任务数据总条数(放款)
	 * @param userId
	 * @param candidateGroups
	 * @param falseUser
	 * @param pageSize
	 * @param currentPage
	 * @param searchString
	 * @return
	 */
	public long findTodoTasksCountLoan(String userId, List<String> candidateGroups,List<String> falseUser,String searchString,String suspensionState) ;

	/**
	 * 查询待办任务(放款 业务相关)
	 * @param userId
	 * @param candidateGroups
	 * @param falseUser
	 * @return
	 */
	public List<Map<String, Object>> findTodoTasksLoan(String userId, List<String> candidateGroups,List<String> falseUser,int pageSize, int currentPage,String searchString,String suspensionState);
	
	/**
	 * 查询待办任务数据总条数(快速贷款)
	 * @param userId
	 * @param candidateGroups
	 * @param falseUser
	 * @param pageSize
	 * @param currentPage
	 * @param searchString
	 * @return
	 */
	public long findTodoTasksCountQuickloan(String userId, List<String> candidateGroups,List<String> falseUser,String searchString,String suspensionState) ;

	/**
	 * 查询待办任务(快速贷款 业务相关)
	 * @param userId
	 * @param candidateGroups
	 * @param falseUser
	 * @return
	 */
	public List<Map<String, Object>> findTodoTasksQuickloan(String userId, List<String> candidateGroups,List<String> falseUser,int pageSize, int currentPage,String searchString,String suspensionState);
	
	/**
	 * 快速贷款办理（luy）
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 * @param map 前台参数
	 */
	public void doQuickLoan(WorkFlow workFlow, Long userId,String comName) throws Exception;

	/**
	 * 办理任务
	 * @param taskId
	 */
	public void complete(String taskId);
	
	/**
	 * @liutt
	 * 测试使用模拟用户登陆后返回数据
	 * @param userId
	 * @return
	 */
	public Map<String, Object> simulationSession(long userId);
	
	/**
	 * @liutt
	 * 人工向银行发送放款申请，成功进行业务处理，工作流办理
	 * @param loanId 放款id
	 * @param taskId 任务id
	 * @throws Exception 
	 */
	public boolean personDoLoanApply(Long loanId,String taskId) throws Exception;
}
