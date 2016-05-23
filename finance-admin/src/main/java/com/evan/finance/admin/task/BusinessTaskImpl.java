package com.evan.finance.admin.task;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.evan.common.user.domain.FaAccessTask;
import com.evan.common.user.domain.FaFieldTask;
import com.evan.common.user.domain.FaQuotaTask;
import com.evan.common.user.domain.FwLoanApply;
import com.evan.common.user.domain.FwTransactionDetail;
import com.evan.common.user.monitor.MonitorTools;
import com.evan.common.user.service.FaAccessTaskService;
import com.evan.common.user.service.FaFieldTaskService;
import com.evan.common.user.service.FaLoanTaskService;
import com.evan.common.user.service.FaOperationRecordService;
import com.evan.common.user.service.FaQuotaTaskService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwComAccountService;
import com.evan.common.user.service.FwLoanApplyService;
import com.evan.common.user.service.FwTransactionDetailService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.ConstantTool.OperationState;
import com.evan.finance.admin.bank.BankTools;
import com.evan.finance.admin.notification.service.NotificationService;
import com.evan.finance.admin.utils.WorkFlowUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;
@Component
public class BusinessTaskImpl implements BusinessTask {
	final Logger logger = LoggerFactory.getLogger(BusinessTaskImpl.class);	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FaAccessTaskService faAccessTaskService;
	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	
	@Autowired
	private FaQuotaTaskService faQuotaTaskService;
	
	@Autowired
	private FaFieldTaskService faFieldTaskService;
	
	@Autowired
	private FaLoanTaskService faLoanTaskService;
	
	@Autowired
	private FwLoanApplyService fwLoanApplyService;
	
	@Autowired
	private BankTools bankTools;
	
	@Autowired
	private MonitorTools monitorTools;
	
	@Autowired
	private FwComAccountService fwComAccountService;
	
	@Autowired
	private FwTransactionDetailService fwTransactionDetailService;
	
	@Autowired
	private FaOperationRecordService faOperationRecordService;
	
	@Autowired
	private NotificationService notificationService;
	
	// 每2分钟执行一次
    public void getBusinessTask() {
    	/*logger.error("getBusinessTask=============start");
		accessBankVerify();
		quotaBankScoring();
		fieldAccountApply();*/
		//loanBankApply();

    }
	
	// 每5秒执行一次
//	@Scheduled(cron = "0/59 * *  * * ?")
	//@Scheduled(cron = "0 0/3 * * * ?")
	//@Scheduled(cron = "0 0/5 * * * ?")
	public void accessBankVerify() {
		logger.error("================================accessBankVerify");
		List<FaAccessTask> faAccessTasks = faAccessTaskService.getAccessTasksByStartTime();
		if (faAccessTasks.size()>0) {
			bankTools.cebankUserLogon();
			
			for (FaAccessTask faAccessTask : faAccessTasks) {
				//构建 调用银行查询征信结果查询接口的map
				Map<String, Object> map = fwBusinessSxdService.bankCreditResultMap(faAccessTask.getBusId());
				try {
					Map<String, Object> variables = new HashMap<String, Object>();
					// 调用银行查询征信结果查询接口
					Map<String, Object> reaultMap = bankTools.queryCreditResult(map);
					reaultMap = (Map<String, Object>) reaultMap.get("opRep");
					String resultCode = MapUtils.getString(reaultMap, "investResult");
					String retCode = MapUtils.getString(reaultMap, "retCode");
					//如果查询失败则继续下一条
					if (!"0".equals(retCode)) {
						continue;
					}
					List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(faAccessTask.getBusId()).list();
					Task task = null;
					for (Task task1 : tasks) {
						if ("sys_admin".equals(task1.getAssignee()) && task1.getProcessDefinitionId().indexOf(WorkFlowUtils.process_definition_key_access_verify)== 0 && "usertask1".equals(task1.getTaskDefinitionKey()) ) {
							task=task1;
						}
					}
					//定义操作记录状态
		    		String operState = OperationState.BUSINESS_SUCCESS;
					if ("成功".equals(resultCode)) {
						//成功准入
					    variables.put(WorkFlowUtils.bankVerify, true);
					    operState = OperationState.BUSINESS_PASS;
					    //加操作日志
					}else if("失败".equals(resultCode)){
						variables.put(WorkFlowUtils.bankVerify, false);
						operState = OperationState.BUSINESS_NO_PASS;
					}else {
						continue;
					}
					if (task==null) {
						continue;
					}
					taskService.complete(task.getId(), variables);
					//任务执行成功之后  删除该任务
					faAccessTaskService.remove(faAccessTask.getId());
					
				   String  nodeName = MapUtils.getString(WorkFlowUtils.map, "bankVerifyResult");//操作记录节点名称
	    		   faOperationRecordService.saveFaOperationRecord(faAccessTask.getBusId(), 0, "",nodeName,operState);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
	
	
	// 每天6到18点每隔1小时执行一次
	//@Scheduled(cron = "0 0 6-23/1 * * ?")
	//@Scheduled(cron = "0 0/5 * * * ?")
	//@Scheduled(cron = "0 0/5 * * * ?")
//	@Scheduled(cron = "0/55 * *  * * ?")
	public void quotaBankScoring() {
		logger.error("================================quotaBankScoring");
		List<FaQuotaTask> faQuotaTasks = faQuotaTaskService.getQuotaTasksByStartTime();
		if (faQuotaTasks.size()>0) {
			bankTools.cebankUserLogon();
			for (FaQuotaTask faQuotaTask : faQuotaTasks) {
				//构建银行打分结果需要的map
				Map<String, Object> scoreMap = fwBusinessSxdService.bankScoringResultMap(faQuotaTask.getBusId());
				//构建 调用银行额度申请接口的map
				 Map<String, Object> quotaMap = fwBusinessSxdService.bankScoringQuotaResultMap(faQuotaTask.getBusId());
				try {
					
					//调用银行额度申请结果接口
					Map<String, Object> variables = new HashMap<String, Object>();
					//调用银行打分结果查询接口
					Map<String, Object> scoreResultMap = bankTools.queryCustomerLevelResult(scoreMap);
					scoreResultMap = (Map<String, Object>) scoreResultMap.get("opRep");
					//调用银行额度申请结果查询
					Map<String, Object> quotaResultMap = bankTools.queryQuotaResult(quotaMap);
					quotaResultMap = (Map<String, Object>) quotaResultMap.get("opRep");
					
					String resultCode = MapUtils.getString(quotaResultMap, "QuotaResult");
					
					String scoreRetCode = MapUtils.getString(scoreResultMap, "retCode");
					String quotaRetCode = MapUtils.getString(quotaResultMap, "retCode");
					//如果查询失败则继续下一条
					if (!"0".equals(scoreRetCode) || !"0".equals(quotaRetCode)) {
						continue;
					}
					List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(faQuotaTask.getBusId()).list();
					Task task = null;
					for (Task task1 : tasks) {
						if ("sys_admin".equals(task1.getAssignee()) && task1.getProcessDefinitionId().indexOf(WorkFlowUtils.process_definition_key_quota_apply)== 0 && "usertask4".equals(task1.getTaskDefinitionKey())) {
							task=task1;
						}
					}
					//定义操作记录状态
		    		String operState = OperationState.BUSINESS_SUCCESS;
					if ("1000".equals(resultCode)) {
						//将打分结果传到数据库中
					    //授信额度
					    String loanCreditAmt = MapUtils.getString(quotaResultMap, "loanCreditAmt");
						variables.put("loanCreditAmt",loanCreditAmt);
					    //将打分结果修改到数据库
						fwBusinessSxdService.BankScoring(faQuotaTask.getBusId(),scoreResultMap);
					    variables.put(WorkFlowUtils.applyResult, true);
					    operState = OperationState.BUSINESS_PASS;
					}else if("2000".equals(resultCode)){
						//驳回
						variables.put(WorkFlowUtils.applyResult, false);
						 operState = OperationState.BUSINESS_NO_PASS;
					}else if("0020".equals(resultCode)){
						//补件
						variables.put(WorkFlowUtils.applyResult, false);
					}else {
						continue;
					}
					if (task==null) {
						continue;
					}
					taskService.complete(task.getId(), variables);
					//任务执行成功之后  删除该任务
					faQuotaTaskService.remove(faQuotaTask.getId());
					
					String  nodeName = MapUtils.getString(WorkFlowUtils.map, "mranualCreditLimitResult");//操作记录节点名称
	    		    faOperationRecordService.saveFaOperationRecord(faQuotaTask.getBusId(), 0, "",nodeName,operState);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	   }
	
	   /**
	    * 访问银行开户申请结果查询接口
	    */
	    // 每5秒执行一次
		//@Scheduled(cron = "0 0 6-23/1 * * ?")
		//@Scheduled(cron = "0 0/1 * * * ?")
		//@Scheduled(cron = "0/30 * *  * * ?")
	    //@Scheduled(cron = "0 0/5 * * * ?")
//	    @Scheduled(cron = "0/40 * *  * * ?")
		public void fieldAccountApply() {
			logger.error("================================fieldAccountApply");
			List<FaFieldTask> faFieldTasks = faFieldTaskService.getFieldTaskByStartTime();
			if (faFieldTasks.size()>0) {
				bankTools.cebankUserLogon();
				for (FaFieldTask faFieldTask : faFieldTasks) {
					//构建开户申请结果查询的map
 					Map<String, Object> map = fwBusinessSxdService.bankFieldAccountResultMap(faFieldTask.getBusId());
					try {
						Map<String, Object> variables = new HashMap<String, Object>();
						//调用银行开户申请结果查询接口
						Map<String, Object> resultMap = bankTools.queryOpenAccountResult(map);
						
						resultMap = (Map<String, Object>) resultMap.get("opRep");
						String retCode = MapUtils.getString(resultMap, "retCode");
						//如果查询失败则继续下一条
						if (!"0".equals(retCode)) {
							continue;
						}
						String resultCode = MapUtils.getString(resultMap, "applyResult");
						List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(faFieldTask.getBusId()).list();
						Task task = null;
						for (Task task1 : tasks) {
							if ("sys_admin".equals(task1.getAssignee()) && task1.getProcessDefinitionId().indexOf(WorkFlowUtils.process_definition_key_field_account)== 0 && "usertask2".equals(task1.getTaskDefinitionKey())) {
								task=task1;
							}
						}
						//定义操作记录状态
			    		String operState = OperationState.BUSINESS_SUCCESS;
						if ("Y".equals(resultCode)) {
							//成功准入
						    variables.put(WorkFlowUtils.accountResult, true);
						    variables.put("opResultSet", MapUtils.getObject(resultMap, "opResultSet"));
						    operState = OperationState.BUSINESS_SUCCESS;
						}else if("N".equals(resultCode)){
							variables.put(WorkFlowUtils.accountResult, false);
							operState = OperationState.BUSINESS_FAIL;
						}else {
							continue;
						}
						if (task==null) {
							continue;
						}
						
						//variables.put(WorkFlowUtils.accountResult,true);
						taskService.complete(task.getId(), variables);
						//任务执行成功之后  删除该任务
						faFieldTaskService.remove(faFieldTask.getId());
						String  nodeName = MapUtils.getString(WorkFlowUtils.map, "bankResult");//操作记录节点名称
		    		    faOperationRecordService.saveFaOperationRecord(faFieldTask.getBusId(), 0, "",nodeName,operState);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		   }
		
		/**
		 * 定时访问银行接口  将数据同步到平台数据库上  2.4.4.	查询活期当日明细
		 */
		// 每5秒执行一次
//		@Scheduled(cron = "0 0 22 * * ?")
		//@Scheduled(cron = "0 0/15 * * * ?")
//		@Scheduled(cron = "0/50 * *  * * ?")
		//@Scheduled(cron = "0 0 6-18/1 * * ?")
		//@Scheduled(cron = "0 0/10 * * * ?")
		public void queryBankDailyCurrentList() {
			logger.error("=============queryBankDailyCurrentList");
			//查询所有用户
			List<Map<String, Object>> list = fwComAccountService.getComAcountList();
			
			bankTools.cebankUserLogon();
			
			//循环企业用户 向银行接口发送请求 查询该企业账号的  当日活期明细
			for (Map<String, Object> map : list) {
				long comId = MapUtils.getLong(map, "comId", null);
				
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("ACNO", MapUtils.getString(map, "lenersAccount"));
				map2.put("MIFS", 0);
				map2.put("MAFS", 99999999999999999.99);
				Map<String, Object> resMap = null;
				try {
					resMap = bankTools.queryDailyCurrentList(map2);
					resMap = (Map<String, Object>) resMap.get("opRep");
					String retCode = MapUtils.getString(resMap, "retCode");
					if (!"0".equals(retCode)) {
						continue;
					}
					Map<String, Object> opResultSet = (Map<String, Object>)resMap.get("opResultSet");
					Object object =  (Object)opResultSet.get("opResult");
					List<Map<String, Object>> listRes = new ArrayList<Map<String,Object>>();
					if (object instanceof Map) {
						Map<String, Object> map3 = (Map<String, Object>)object;
						listRes.add(map3);
					}else if(object instanceof List){
						listRes = (List<Map<String, Object>>)object;
					}
					//List<Map<String, Object>> listRes = (List<Map<String, Object>>)opResultSet.get("opResult");
					for (Map<String, Object> map3 : listRes) {
						// TODO 进行循环判断  交易记录表中插入交易记录
						String pati = MapUtils.getString(map3, "PATI","");
						if (!StringUtils.isEmpty(pati)) {
							// 如果是 扣息
							if (ConstantTool.TransactionType.KOUXI.equals(pati)) {
								String loanNo = MapUtils.getString(map3, "T24F", "");//借据号
								double money = MapUtils.getDouble(map3, "FSJE", 0.00);//交易金额
								String type = "02";//交易类型 01:收入；02：支出
								String note = MapUtils.getString(map3, "BEZH", "");//备注
								String FSSJ = MapUtils.getString(map3, "FSSJ", "");//发生时间
								String time = new SimpleDateFormat(ConstantTool.DATA_FORMAT_DATE).format(new Date()) + " " + FSSJ+":00";
								String state = "1";//状态
								fwTransactionDetailService.saveFwTransactionDetail(loanNo, new BigDecimal(money), type, note, time, time, state, ConstantTool.TransactionType.KOUXI_STR,ConstantTool.TransactionSource.BANK,comId);
								continue;
							}
							
							// 如果是 自主还款/到期扣本金
							if (ConstantTool.TransactionType.HUANKUAN.equals(pati)) {
								String loanNo = MapUtils.getString(map3, "T24F", "");//借据号
								double money = MapUtils.getDouble(map3, "FSJE", 0.00);//交易金额
								String type = "02";//交易类型 01:收入；02：支出
								String note = MapUtils.getString(map3, "BEZH", "");//备注
								String FSSJ = MapUtils.getString(map3, "FSSJ", "");//发生时间
								String time = new SimpleDateFormat(ConstantTool.DATA_FORMAT_DATE).format(new Date()) + " " + FSSJ+":00";
								String state = "1";//状态
								
								List<FwTransactionDetail> details = fwTransactionDetailService.findFwTransactionDetailsForIsExist(loanNo, new BigDecimal(money), type, note, time, state);
								if (details==null || details.size() == 0) {
									FwTransactionDetail ftd = fwTransactionDetailService.saveFwTransactionDetail(loanNo, new BigDecimal(money), type, note, time, time, state, ConstantTool.TransactionType.HUANKUAN_STR,ConstantTool.TransactionSource.BANK,comId);
									if (ftd !=null) {
										// 向 还款记录表增加记录，并提升相应的额度
										FwLoanApply fwLoanApply = fwLoanApplyService.getRepaymentByLoanNo(loanNo);
										if (fwLoanApply!=null) {
											fwLoanApplyService.updateRepayment(fwLoanApply, money+"", comId, 0L, false,ConstantTool.TransactionSource.BANK,"1","","");
										}
									}
								}
								continue;
							}
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
 			
		}
		
		
		
		
		/**
		 * 定时向银行发送放款确认
		 */
		// 每5秒执行一次
		// @Scheduled(cron = "0/59 * *  * * ?")
		// @Scheduled(cron = "0 0 22 * * ?")
		// @Scheduled(cron = "0 0 6-23/1 * * ?")
		// @Scheduled(cron = "0 0/5 * * * ?")
		// @Scheduled(cron = "0/59 * *  * * ?")
/*
		public void loanBankApply() {
			//返回结果没有放款额度
			logger.error("=============loanBankApply");
			List<FaLoanTask> faLoanTasks = faLoanTaskService.getLoanTasksByStartTime();

			if (faLoanTasks.size()>0) {
				bankTools.cebankUserLogon();
				for (FaLoanTask faLoanTask : faLoanTasks) {
					//构建 调用银行放款申请结果查询的接口的map
					Map<String, Object> map = fwLoanApplyService.bankLoanApplyResultMap(faLoanTask.getLoanId());
					try {
						//调用银行放款确认结果查询的接口
						Map<String, Object> resultMap = bankTools.applyFinancingLoan(map);
						resultMap = (Map<String, Object>) resultMap.get("opRep");
						String resultCode = MapUtils.getString(resultMap, "errMsg");
						//Map<String, Object> variables = new HashMap<String, Object>();
						String retCode = MapUtils.getString(resultMap, "retCode");
						//如果查询失败则继续下一条
						if (!"0".equals(retCode)) {
							continue;
						}
						List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(faLoanTask.getLoanId().toString()).list();
						Task task = null;
						for (Task task1 : tasks) {
							if ("sys_admin".equals(task1.getAssignee()) && task1.getProcessDefinitionId().indexOf(WorkFlowUtils.process_definition_key_loan)== 0) {
								task=task1;
							}
						}
						FwLoanApply fwLoanApply = fwLoanApplyService.findByPk(faLoanTask.getLoanId());
						String message = MapUtils.getString(resultMap, "errMsg");
						if ("成功".equals(resultCode)) {
							//查询银行接口 放款到期日
							Map<String, Object> map2 = new HashMap<String, Object>();
							map2.put("LoanNo",fwLoanApply.getLoanNo());
							map2.put("busid",fwLoanApply.getBusinessId());
							Map<String, Object> retRsuMap = null;
							try {
								retRsuMap = bankTools.queryLoanInfo(map);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							retRsuMap = (Map<String, Object>) retRsuMap.get("opRep");
							String retCodeLoan = MapUtils.getString(retRsuMap, "retCode");
							retRsuMap = (Map<String, Object>) retRsuMap.get("opResult");
							
							//放款到期日
							String endDate = "";
							if ("0".equals(retCode)) {
								endDate = MapUtils.getString(retRsuMap, "T24DateDue");
							}
							Date dateEnd = new SimpleDateFormat(ConstantTool.DATA_FORMAT_DAY).parse(endDate);
							String endDateStr = new SimpleDateFormat(ConstantTool.DATA_FORMAT).format(dateEnd);
							
							
						    fwLoanApplyService.processSuccessCallback(faLoanTask.getLoanId(), 0L, "",endDateStr);
						    //发送放款成功通知
						    notificationService.sendApplySuccess(faLoanTask.getLoanId());
						    //根据放款任务id  查询放款记录 并且向交易记录表中插入放款成功记录
						    
						    String date = DateUtils.getCurrentDateTime(ConstantTool.DATA_FORMAT);
						    //加操作日志
						    fwTransactionDetailService.saveFwTransactionDetail(fwLoanApply.getLoanNo(), fwLoanApply.getAppMoney(), "01", message, fwLoanApply.getCreateTime(), date, "1",TransactionType.FANGKUAN_STR,TransactionSource.YILUDAI,fwLoanApply.getComId());
						    //
						    monitorTools.infoOther("发送放款通知成功", "true");
						}else if("失败".equals(resultCode)){
							//variables.put(WorkFlowUtils.loanBankHandle, false);
						   fwLoanApplyService.processFailCallback(faLoanTask.getLoanId(), 0L, "");
						   //发送放款失败通知
						   notificationService.sendApplyFailed(faLoanTask.getLoanId());
						   fwTransactionDetailService.saveFwTransactionDetail(fwLoanApply.getLoanNo(), fwLoanApply.getAppMoney(), "01", message, fwLoanApply.getCreateTime(), fwLoanApply.getApplyPassTime(), "0",TransactionType.FANGKUAN_STR,TransactionSource.YILUDAI,fwLoanApply.getComId());
						   monitorTools.infoOther("发送放款通知失败", "false");
						}else {
							continue;
						}
						if (task==null) {
							continue;
						}
						//taskService.complete(task.getId(), variables);
						//任务执行成功之后  删除该任务
						faLoanTaskService.remove(faLoanTask.getId());
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		   }*/
		

		
		

		/**
		 * 定时访问银行 融资申请信息 接口  查询所有放款申请
		 */
		// 每5秒执行一次
		/*@Scheduled(cron = "0 0 23 * * ?")
		//@Scheduled(cron = "0/59 * *  * * ?")
		//@Scheduled(cron = "0 0 6-23/3 * * ?")
		//@Scheduled(cron = "0 0/10 * * * ?")
		public void findBankLoan() {
			logger.error("=============findBankLoan");
			bankTools.cebankUserLogon();
			Map<String, Object> map1 = new HashMap<String, Object>();
			String endTime = DateUtils.getCurrentDateTime(ConstantTool.DATA_FORMAT_DAY);
			String startTime = DateUtils.dateAddDay(-10);
			map1.put("SDATE", startTime);
			map1.put("EDATE", endTime);
			try {
				Map<String, Object> resultMap = bankTools.queryLoanAppInfo(map1);
				resultMap = (Map<String, Object>) resultMap.get("opRep");
				String retCode = MapUtils.getString(resultMap, "retCode");
				//如果查询失败则继续下一条
				if ("0".equals(retCode)) {
					//查询成功 进行循环处理操作
					Map<String, Object> opResultSet = (Map<String, Object>)resultMap.get("opResultSet");
					//List<Map<String, Object>> opResultList = (List<Map<String, Object>>)opResultSet.get("opResult");
					Object object =  (Object)opResultSet.get("opResult");
					List<Map<String, Object>> listRes = new ArrayList<Map<String,Object>>();
					if (object instanceof Map) {
						Map<String, Object> map3 = (Map<String, Object>)object;
						listRes.add(map3);
					}else if(object instanceof List){
						listRes = (List<Map<String, Object>>)object;
					}
					
					for (Map<String, Object> map : listRes) {
						String payNo = MapUtils.getString(map, "FlowNo");
						//流水号
						logger.error("FlowNo============="+payNo);
						FwLoanApply fwLoanApply = fwLoanApplyService.getFwLoanApplyByPayNo(payNo);
						if (fwLoanApply==null) {
							logger.error("fwLoanApply=============null");
							//向放款申请表中插入 放款记录
							FwLoanApply fwLoanApply2 = fwLoanApplyService.bankSaveLoan(map);
							if (fwLoanApply2==null) {
								continue;
							}
							logger.error("BusinessTasklImpl.faLoanTaskService.saveFaLoanTask============="+fwLoanApply2.getLoanId());
							//向放款任务表中 插入放款任务记录
							faLoanTaskService.saveFaLoanTask(fwLoanApply2.getLoanId(), DateUtils.getCurrentDateTime(ConstantTool.DATA_FORMAT));
						}
						
					}
				}
				//System.out.println("map=============="+JsonUtils.toJson(resultMap));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/

}
