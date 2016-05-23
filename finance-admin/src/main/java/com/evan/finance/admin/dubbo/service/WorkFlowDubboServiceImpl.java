package com.evan.finance.admin.dubbo.service;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.service.FaInfoNopassService;
import com.evan.common.user.service.FaOperationRecordService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.utils.ConstantTool.ApplyState;
import com.evan.common.utils.ConstantTool.BusinessType;
import com.evan.common.utils.ConstantTool.OperationState;
import com.evan.dubbo.finance.WorkFlowDubboService;
import com.evan.finance.admin.activiti.bean.LoanFlowView;
import com.evan.finance.admin.activiti.bean.NegotiateFlowView;
import com.evan.finance.admin.activiti.bean.QuickloanFlowView;
import com.evan.finance.admin.activiti.bean.RepaymentFlowView;
import com.evan.finance.admin.activiti.bean.WorkFlowView;
import com.evan.finance.admin.activiti.service.ActivitiSxdService;
import com.evan.finance.admin.activiti.service.WorkFlowAccessVerifyService;
import com.evan.finance.admin.activiti.service.WorkFlowInstanceService;
import com.evan.finance.admin.activiti.service.WorkFlowLoanService;
import com.evan.finance.admin.activiti.service.WorkFlowNegotiateService;
import com.evan.finance.admin.activiti.service.WorkFlowQuickloanService;
import com.evan.finance.admin.activiti.service.WorkFlowQuotaApplyService;
import com.evan.finance.admin.activiti.service.WorkFlowRepaymentService;
import com.evan.finance.admin.activiti.service.WorkFlowService;
import com.evan.finance.admin.utils.ActivityUtil;
import com.evan.finance.admin.utils.WorkFlowUtils;
import com.evan.jaron.util.MapUtils;
import com.google.common.collect.Maps;

public class WorkFlowDubboServiceImpl implements WorkFlowDubboService {
	
	Logger logger = LoggerFactory.getLogger(WorkFlowDubboServiceImpl.class);

	@Autowired
	private WorkFlowLoanService workFlowLoanService;
	
	@Autowired
	private WorkFlowNegotiateService workFlowNegotiateService;
	
	@Autowired
	private WorkFlowQuickloanService workFlowQuickloanService;
	
	@Autowired
	private WorkFlowRepaymentService workFlowRepaymentService;
	
	@Autowired
	private WorkFlowAccessVerifyService workFlowAccessVerifyService;
	
	@Autowired
	private WorkFlowQuotaApplyService workFlowQuotaApplyService;
	
	@Autowired
	private WorkFlowService workFlowService;
	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	
	@Autowired
	private FaInfoNopassService faInfoNopassService;
	
	@Autowired
	private FaOperationRecordService faOperationRecordService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private WorkFlowInstanceService workFlowInstanceService;
	
	@Autowired
	private ActivitiSxdService activitiSxdService;
	
	/**
	 * 申请放款
	 * 
	 * @param businessKey业务ID
	 * @return businessKey=业务ID processInstanceId=流程ID status=0/1 0表示放款申请成功  1标识放款申请异常  errorMsg 申请异常时错误信息
	 */
	@Override
	public Map<String, String> startProcessLoan(String businessKey) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			LoanFlowView loanFlowView = workFlowLoanService.startProcess(businessKey);
			map.put("businessKey", loanFlowView.getBusinessKey());
			map.put("processInstanceId", loanFlowView.getProcessInstanceId());
			map.put("status", "0");
		} catch (Exception e) {
			map.put("status", "1");
			map.put("errorMsg", e.getMessage());
		}
		return map;
	}

	/**
	 * 启动商务洽谈流程
	 * 
	 * @param businessKey业务ID
	 * @return businessKey=业务ID processInstanceId=流程ID
	 */
	@Override
	public Map<String, String> startProcessNegotiate(String businessKey) {
		if (businessKey == null || businessKey.trim().length() == 0) {
			return null;
		}
		NegotiateFlowView negotiateFlowView = workFlowNegotiateService.startProcess(businessKey);
		Map<String, String> map = new HashMap<String, String>();
		map.put("businessKey", negotiateFlowView.getBusinessKey());
		map.put("processInstanceId", negotiateFlowView.getProcessInstanceId());
		map.put("status", "0");
		return map;
	}

	/**
	 * 启动快速贷款流程
	 * 
	 * @param businessKey业务ID
	 * @return businessKey=业务ID processInstanceId=流程ID
	 */
	@Override
	public Map<String, String> startProcessQuickloan(String businessKey) {
		if (businessKey == null || businessKey.trim().length() == 0) {
			return null;
		}
		QuickloanFlowView quickloanFlowView = workFlowQuickloanService.startProcess(businessKey);
		Map<String, String> map = new HashMap<String, String>();
		map.put("businessKey", quickloanFlowView.getBusinessKey());
		map.put("processInstanceId", quickloanFlowView.getProcessInstanceId());
		map.put("status", "0");
		return map;
	}

	/**
	 * 启动还款流程
	 * 
	 * @param businessKey业务ID
	 * @return businessKey=业务ID processInstanceId=流程ID
	 */
	@Override
	public Map<String, String> startProcessRepayment(String businessKey) {
		if (businessKey == null || businessKey.trim().length() == 0) {
			return null;
		}
		RepaymentFlowView repaymentFlowView = workFlowRepaymentService.startProcess(businessKey);
		Map<String, String> map = new HashMap<String, String>();
		map.put("businessKey", repaymentFlowView.getBusinessKey());
		map.put("processInstanceId", repaymentFlowView.getProcessInstanceId());
		map.put("status", "0");
		return map;
	}

	@Override
	public Map<String, String> startProcessAccess(String businessKey) {
		if (businessKey == null || businessKey.trim().length() == 0) {
			return null;
		}
		//提交申请 01 
		String  nodeName = MapUtils.getString(WorkFlowUtils.map, "alreadySubmit");//操作记录节点名称
		faOperationRecordService.saveFaOperationRecord(businessKey, 0, "",nodeName,OperationState.BUSINESS_SUCCESS);
		WorkFlowView workFlowView = workFlowAccessVerifyService.startProcess(businessKey);
		Map<String, String> map = new HashMap<String, String>();
		map.put("businessKey", workFlowView.getBusinessKey());
		map.put("processInstanceId", workFlowView.getProcessInstanceId());
		map.put("status", "0");
		return map;
	}

	/**
	 * 补充材料后，启动额度申请流程
	 * 
	 * @param businessKey
	 * @return businessKey=业务ID processInstanceId=流程ID
	 */
	@Override
	public Map<String, String> startProcessQuota(String businessKey) {
		if (businessKey == null || businessKey.trim().length() == 0) {
			return null;
		}
		//补充资料 03
		String  nodeName = MapUtils.getString(WorkFlowUtils.map, "relpenishInformation");//操作记录节点名称
		faOperationRecordService.saveFaOperationRecord(businessKey, 0, "",nodeName,OperationState.BUSINESS_SUCCESS);

		WorkFlowView workFlowView = workFlowQuotaApplyService.startProcess(businessKey);
		Map<String, String> map = new HashMap<String, String>();
		map.put("businessKey", workFlowView.getBusinessKey());
		map.put("processInstanceId", workFlowView.getProcessInstanceId());
		map.put("status", "0");
		return map;
	}

	/**
	 * 准入驳回后，用户提交资料，继续办理流程，进入人工准入验证环节
	 * @param businessKey
	 * @return
	 */
	public boolean reStartProcessAccess(String businessKey){
		Task task = workFlowService.getTaskByDefineKetAndNode(businessKey,WorkFlowUtils.access_wait_cus_information,"sys_admin");
		if (task!=null) {
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put(WorkFlowUtils.access_wait_cus_information, true);
			try {
				String  nodeName = MapUtils.getString(WorkFlowUtils.map, "accessWaitCusInformation");//操作记录节点名称
				faOperationRecordService.saveFaOperationRecord(businessKey, 0, "成功",nodeName,OperationState.BUSINESS_SUCCESS);
				workFlowAccessVerifyService.complete(task.getId(), variables);
				FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(businessKey);
				fwBusinessSxd.setApplyState(ApplyState.ADMITTANCE_APPORVALING);
				fwBusinessSxd = fwBusinessSxdService.save(fwBusinessSxd);
				faInfoNopassService.updateAllIsHistoryByBusId("1", businessKey, BusinessType.SXD);
				if (fwBusinessSxd==null) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
			
		}
		return  true;
	}
	
	/**
  	 * 额度申请驳回后，用户提交资料，继续办理流程，进入人工打分/复核环节
	 * @param businessKey
	 * @return
	 */
	public boolean reStartProcessQuota(String businessKey){
		Task task = workFlowService.getTaskByDefineKetAndNode(businessKey,WorkFlowUtils.quota_wait_cus_information,"sys_admin");
		if (task!=null) {
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put(WorkFlowUtils.quota_wait_cus_information, true);
			try {
				//额度申请重新录入资料0301
				String  nodeName = MapUtils.getString(WorkFlowUtils.map, "quotaWaitCusInformation");//操作记录节点名称
				faOperationRecordService.saveFaOperationRecord(businessKey, 0, "成功",nodeName,OperationState.BUSINESS_SUCCESS);
		
				workFlowQuotaApplyService.complete(task.getId(), variables);
				FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(businessKey);
				fwBusinessSxd.setApplyState(ApplyState.LIMIT_APPORVALING);
				fwBusinessSxd = fwBusinessSxdService.save(fwBusinessSxd);
				faInfoNopassService.updateAllIsHistoryByBusId("1", businessKey, BusinessType.SXD);
				if (fwBusinessSxd==null) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return  true;
	}
	
	/**
  	 * 启动新工作流 
	 * @param busId 业务ID
	 * @param userId 用户ID
	 * @return
	 */
	public Map<String, Object> startProcess(String busId, String userId) {
		Map<String, Object> map = null;
		if(StringUtils.isNotEmpty(busId) && StringUtils.isNotEmpty(userId)){
			Map<String, Object> vars = Maps.newHashMap();
			return workFlowInstanceService.startProcess(busId, userId, vars);
		}
		return map;
	}
	
	/**
  	 * 用户填写资料完成到系统准入验证节点
  	 * @param procInsId 流程实例ID
	 * @param busId 业务ID
	 * @param comName 企业名称
	 * @return 1成功 	其他返回值为失败
	 */
	public int completeToAccess(String procInsId, String busId, String comName){
		try{
			if(StringUtils.isNotEmpty(procInsId)){
				return workFlowInstanceService.completeToAccess(procInsId, busId, comName);
			}
		} catch(Exception e){
			logger.error("到系统准入验证节点异常：", e);
			return -1;
		}
		return 0;
	}
	
	/**
  	 * 用户补充资料到系统自动打分
	 * @param procInsId 流程实例ID
	 * @param busId 业务ID
	 * @param comName 企业名称
	 * @return 1成功 	其他返回值为失败
	 */
	public int completeToMark(String procInsId, String busId, String comName){
		try{
			if(StringUtils.isNotEmpty(procInsId)){
				return workFlowInstanceService.completeToMark(procInsId, busId, comName);
			}
		} catch(Exception e){
			logger.error("到系统自动打分节点异常：", e);
			return -1;
		}
		return 0;
	}
	
	/**
  	 * 银行返回结果，更具银行返回结果操作工作流
	 * @param taskId 任务id
	 * @param variables 操作工作流参数
	 */
	public void completeToBusinessFail(String taskId, Map<String,Object> variables){
		
		activitiSxdService.complete(taskId, variables);
	}
	
	/**
  	 * 根据流程实例ID判断是否是新工作流
	 * @param processInstanceId 流程实例ID
	 * @return 1新流程 2旧流程 0异常
	 */
	public int workFlowState(String processInstanceId){
		if(StringUtils.isNotBlank(processInstanceId)){
			try{
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
				if(processInstance.getProcessDefinitionId().startsWith(ActivityUtil.PROCDEFKEY)){
					return 1;
				} else {
					return 2;
				}
			} catch(Exception e){
				logger.error("获取流程状态异常：", e);
				return -1;
			}
		}
		return 0;
	}
}
