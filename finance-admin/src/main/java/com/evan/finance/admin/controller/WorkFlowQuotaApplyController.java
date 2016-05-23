
package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evan.common.user.domain.FaCenterDataBank;
import com.evan.common.user.domain.FwBankRelationFiles;
import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.monitor.MonitorTools;
import com.evan.common.user.service.FaCenterDataBankService;
import com.evan.common.user.service.FaCommunicateLogService;
import com.evan.common.user.service.FaOperationRecordService;
import com.evan.common.user.service.FaScoreRecordService;
import com.evan.common.user.service.FilesBankService;
import com.evan.common.user.service.FwBankRelationFilesService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.utils.ConstantTool.ApplyState;
import com.evan.common.utils.ConstantTool.BankFileSceneKey;
import com.evan.common.utils.ConstantTool.BusinessType;
import com.evan.common.utils.ConstantTool.CommlogType;
import com.evan.common.utils.ConstantTool.OperationState;
import com.evan.common.utils.DateUtils;
import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.activiti.bean.WorkFlowView;
import com.evan.finance.admin.activiti.service.WorkFlowFeildAccountService;
import com.evan.finance.admin.activiti.service.WorkFlowQuotaApplyService;
import com.evan.finance.admin.bank.BankTools;
import com.evan.finance.admin.notification.service.NotificationService;
import com.evan.finance.admin.utils.WorkFlowUtils;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;

/**
 * 额度申请办理
 * 
 * @author liutt
 */
@RequestMapping("/workflow/quotaapply")
@Controller
public class WorkFlowQuotaApplyController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(WorkFlowQuotaApplyController.class);
	
	@Autowired
	private WorkFlowQuotaApplyService workFlowQuotaApplyService;
	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	
	@Autowired
	private FaScoreRecordService faScoreRecordService;
	
	@Autowired
	private FilesBankService filesBankService;
	@Autowired
	private WorkFlowFeildAccountService workFlowFeildAccountService;
	
	@Autowired
	private FaCenterDataBankService faCenterDataBankService;
	
	@Autowired
	private FaOperationRecordService faOperationRecordService;
	
	@Autowired
	private BankTools bankTools;
	

	@Autowired
	private MonitorTools monitorTools;
	
	@Autowired
	private FaCommunicateLogService faCommunicateLogService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private FwBankRelationFilesService fwBankRelationFilesService;
	/**
	 * 通过URL启动流程
	 * @param searchForm
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/startProcess/{key}", method = {GET, POST})
	@ResponseBody
	public String startProcess(@PathVariable("key") String businessKey) {
		WorkFlowView workFlowView = workFlowQuotaApplyService.startProcess(businessKey);
		if (workFlowView != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("businessKey", workFlowView.getBusinessKey());
			map.put("processInstanceId", workFlowView.getProcessInstance().getProcessInstanceId());
			map.put("processDefinitionId", workFlowView.getProcessInstance().getProcessDefinitionId());
			return JsonUtils.toJson(RequestUtils.successResult(map));
		} else {
			return JsonUtils.toJson(RequestUtils.failResult(null));
		}
	}
	
	/**
     * 签收任务
     */
    @RequestMapping(value = "task/claim/{id}")
    public String claim(@PathVariable("id") String taskId) {
    	workFlowQuotaApplyService.claim(taskId, RequestUtils.getLoginedUser().getUserName());
        return "redirect:/workflow/list";
    }
    
    /**
     * 办理业务
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "task/complete/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public String complete(@PathVariable("id") String taskId, HttpServletRequest request, RedirectAttributes attr,@RequestParam Map<String,Object> map) {
    	// 校验用户是否有权限，记录日志
    	
    	// 当前节点
    	String node = request.getParameter(WorkFlowUtils.process_definition_key_quota_apply+"_node");
    	// 验证节点
    	String nodeKey = nodeVerify(node);
    	if (nodeKey == null) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，系统找不到当前节点，请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}
    	
    	//处理结果
    	String condition = request.getParameter(WorkFlowUtils.process_definition_key_quota_apply+"_condition");
    	if (!("true".equals(condition) || "false".equals(condition)) && !nodeKey.equals(WorkFlowUtils.manualReview)) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}
    	
    	// 处理原因，不通过时会同步到业务数据
    	String reason = request.getParameter(WorkFlowUtils.process_definition_key_quota_apply+"_reason");
    	reason = reason == null ? "" : reason;
    	
    	//业务id
    	String businessKey = request.getParameter(WorkFlowUtils.process_definition_key_quota_apply+"_businessKey");
    	if ("".equals(businessKey)) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，没有放款id,请联系系统管理员。");
    		return "redirect:/workflow/list";
		}
    	Map<String, Object> variables = new HashMap<String, Object>();
    	Long userId = RequestUtils.getLoginedUser().getUserId();
    	//加操作日志
		String nodeName = MapUtils.getString(WorkFlowUtils.map,nodeKey);
    	try {
    		boolean status = true;
    		//定义操作记录状态
    		String operState = OperationState.BUSINESS_SUCCESS;
    		if (WorkFlowUtils.manualReview.equals(nodeKey)) {
    			//人工打分环节
    			if (!"3".equals(condition)) {
    				logger.debug("condition========="+condition);
    				//人工打分  mapResult 中含有打分结果xml 有分数
        			Map<String, Object> mapResult= fwBusinessSxdService.personQuotaScore(map);
        			//将打分结果存储到打分记录表当中
        			String xml = MapUtils.getString(mapResult, "xml");
        			Integer total = MapUtils.getInteger(mapResult, "total");
        			faScoreRecordService.saveFaScoreRecordMamual(businessKey, BusinessType.SXD, xml, total);
        			//进行判断 是 进行电话授信额度确认  还是直接进行银行额度申请 -----得到根据上年纳税总额度 和 申请额度进行判断
        			status = fwBusinessSxdService.choooseNext(businessKey);
        			//status ==true  通过 进行审批
        			if(status){
        				condition = "1";
        				operState = OperationState.BUSINESS_PASS;
        			}else {
        			//未通过  进行授信额度 确认
        				condition = "2";
        				operState = OperationState.BUSINESS_PASS;
        			}
        			
				}else if("3".equals(condition)){
					//额度申请驳回
					
				}
    			
			}
    		//核准授信额度
    		if (WorkFlowUtils.approvedCreditLimit.equals(nodeKey) && "true".equals(condition)) {
    			//人工核准授信额度环节
        		String quota = request.getParameter(WorkFlowUtils.process_definition_key_quota_apply+"_creditLimit");
        		// 将授信额度存到数据库
        		fwBusinessSxdService.updateIntentMoney(businessKey, new BigDecimal(quota));
    		}
    		FaCenterDataBank faCenterDataBank = faCenterDataBankService.getByBusId(businessKey);
    		if (WorkFlowUtils.qutoaServiceCall.equals(nodeKey)) {
    			//客服电话通知结果环节
    			//如果有额度则证明额度申请通过
    			FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(businessKey);
    			if (faCenterDataBank.getApplyMoney()!=null && faCenterDataBank.getApplyMoney().compareTo(new BigDecimal(0))==1 && !fwBusinessSxd.getApplyState().equals(ApplyState.REJECT_LIMIT)) {
    				//if (faCenterDataBank.getApplyMoney()!=null && faCenterDataBank.getApplyMoney().compareTo(new BigDecimal(0))==1 && !fwBusinessSxd.getApplyState().equals(ApplyState.REJECT)) {
    				logger.debug("额度申请成功，修改数据库状态，faCenterDataBank.getApplyMoney()==="+faCenterDataBank.getApplyMoney());
    				String time = DateUtils.dateAddyear(1);
    		    	//修改数据库状态并且修改客户授信金额
    		    	fwBusinessSxdService.applyPass(businessKey, "", 0, faCenterDataBank.getApplyMoney(), time);
    				
        			//启动现场开户流程
        			workFlowFeildAccountService.startProcess(businessKey);
        			//将现场开户时间 以及现场开户地址存放数据库
        			//现场开户时间
        			String fieldTime = request.getParameter(WorkFlowUtils.process_definition_key_quota_apply+"_fieldTime");
        			//现场开户地址
        			String fieldAddr = request.getParameter(WorkFlowUtils.process_definition_key_quota_apply+"_fieldAddr");
        			Map<String, Object> mapdata = new HashMap<String, Object>();
        			mapdata.put("fieldTime", fieldTime);
        			mapdata.put("fieldAddr", fieldAddr);
        			mapdata.put("busId", businessKey);
        			//将和客户沟通的时间地址保存到数据库
        			faCenterDataBankService.saveFaCenterDataBank(mapdata);
        			
				}else{
				//没有额度说明额度申请失败 修改数据库状态
					fwBusinessSxdService.applyReject(businessKey, reason, 0);
				}
    			// 额度申请通知（luy）
    			//获取前台传过来的参数
    			String isSend = MapUtils.getString(map, "isSend"); // true：办理并发送；false:办理（只发邮件）
    			
    			String isSendEmail = MapUtils.getString(map, "emailCheck"); // 是否发送邮件（on:发送；）
    			String emailTitle = MapUtils.getString(map, "emailTitle"); // 邮件模板标题
    			String emailContent = MapUtils.getString(map, "emailContent"); // 邮件内容
    			String isSendSms = MapUtils.getString(map, "smsCheck"); // 是否发送短信（on:发送；）
    			String smsContent = MapUtils.getString(map, "smsContent"); // 短信内容
    			String isSendMessage = MapUtils.getString(map, "messageCheck"); // 是否发送站内信（on:发送；）
    			String messageTitle = MapUtils.getString(map, "messageTitle"); // 站内信标题
    			String messageContent = MapUtils.getString(map, "messageContent"); // 站内信内容
    			
    			Map<String, Object> retMap = null;
    			if("true".equals(isSend)){
    				//发送客服通知
    				retMap = notificationService.doSendNotification(businessKey, isSendEmail, emailTitle, emailContent, false, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
    			}else {
    				//发送客服通知
        			retMap = notificationService.doSendNotification(businessKey, isSendEmail, emailTitle, emailContent, false, "", "", "", "", "");
				}
    			//准入客服电话通知
    			faCommunicateLogService.saveFaCommunicateLog(businessKey, BusinessType.SXD, CommlogType.QUOTA_RESULT, reason);
				
    			//监控
    			String  contentString = notificationService.monitorContentCreate(isSendEmail, isSendSms, isSendMessage, retMap);
    			if (condition.equals("true")) {
    				monitorTools.infoBusinessQuota("发送额度申请成功通知", businessKey, contentString);
				}else if(condition.equals("false")){
					monitorTools.infoBusinessQuota("发送额度申请失败通知", businessKey, contentString);
				}  			
    			
    		}//else{
			if (!StringUtils.isEmpty(reason)) {
    			//额度申请备注
    			faCommunicateLogService.saveFaCommunicateLog(businessKey, BusinessType.SXD,CommlogType.QUOTA_REMARK, reason);
			}
			//}
			
			// 额度申请驳回通知(luy)
			if(WorkFlowUtils.quotaServiceSendInformation.equals(nodeKey)){
				//获取前台传过来的参数
    			String isSend = MapUtils.getString(map, "isSend"); // true：办理并发送；false:办理（只发邮件）
    			
    			String isSendEmail = MapUtils.getString(map, "emailCheck"); // 是否发送邮件（on:发送；）
    			String emailTitle = MapUtils.getString(map, "emailTitle"); // 邮件模板标题
    			String emailContent = MapUtils.getString(map, "emailContent"); // 邮件内容
    			String isSendSms = MapUtils.getString(map, "smsCheck"); // 是否发送短信（on:发送；）
    			String smsContent = MapUtils.getString(map, "smsContent"); // 短信内容
    			String isSendMessage = MapUtils.getString(map, "messageCheck"); // 是否发送站内信（on:发送；）
    			String messageTitle = MapUtils.getString(map, "messageTitle"); // 站内信标题
    			String messageContent = MapUtils.getString(map, "messageContent"); // 站内信内容
    			
    			Map<String, Object> retMap =null;
    			if("true".equals(isSend)){
    				//发送客服通知
        			retMap = notificationService.doSendNotification(businessKey, isSendEmail, emailTitle, emailContent, true, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
    			}else {
    				//发送客服通知
        			retMap = notificationService.doSendNotification(businessKey, isSendEmail, emailTitle, emailContent, true, "", "", "", "", "");
				}
    			//修改业务状态 
    			fwBusinessSxdService.updateState(businessKey, ApplyState.LIMIT_REJECT, 0L);
    			
    			//监控
    			String  contentString = notificationService.monitorContentCreate(isSendEmail, isSendSms, isSendMessage, retMap);
    			monitorTools.infoBusinessQuota("发送额度申请驳回通知", businessKey, contentString);
			}
			
    		if (condition.equals("false")) {
    			monitorTools.infoBusinessQuota("额度申请驳回原因", businessKey, reason);
			}
            variables.put("reason", reason);
            if (WorkFlowUtils.manualReview.equals(nodeKey)) {
            	variables.put(nodeKey, condition);
    		} else {
    			variables.put(nodeKey, "true".equals(condition) ? true : false);
    		}
            //经理审批
            if (WorkFlowUtils.quotaManagerApproval.equals(nodeKey) || WorkFlowUtils.xaminationApproval.equals(nodeKey)) {
				if ("true".equals(condition)) {
					operState = OperationState.BUSINESS_PASS;
				}else {
					operState = OperationState.BUSINESS_NO_PASS;
				}
			}
            workFlowQuotaApplyService.complete(taskId, variables);
            //加操作日志
    		faOperationRecordService.saveFaOperationRecord(businessKey, userId,reason, nodeName,operState);
        } catch (Exception e) {
            logger.error("error:", e);
            attr.addFlashAttribute("errorMsg", "任务处理异常，请联系系统管理员。");
            return "redirect:/workflow/list";
        }
    	attr.addFlashAttribute("successMsg","处理成功");
        return "redirect:/workflow/list";
    }
    
    /**
     * 办理业务
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/task/personDone/{busId}/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String personDone(@PathVariable("busId") String busId,@PathVariable("taskId") String taskId, HttpServletRequest request, RedirectAttributes attr) {
    	//调用银行登录接口
		bankTools.cebankUserLogon();
		
		//上传文件
		//查询业务数据
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId);

		List<FwBankRelationFiles> fileList = fwBankRelationFilesService.findByProcessIdAndSceneKey(fwBusinessSxd.getProcessInstanceId().toString(), BankFileSceneKey.QUOTA_KEY);
		//上传文件 该处需要上传4 文件
		List<Map<String,String>> list = fwBusinessSxdService.createFileMapBank(fileList, "BANK_FILE_TYPE");
		//调用银行接口额度申请时构建map
		Map<String, Object> quotaMap = fwBusinessSxdService.bankScoringQuotaMap(fwBusinessSxd);
		quotaMap.put("list", list);
		FaCenterDataBank faCenterDataBank = faCenterDataBankService.getByBusId(busId);
		Map<String, Object> mapResult = new HashMap<String, Object>();
		try{
			
			//定义银行打分上送接口返回结果 code
			String scoreRetCode = "0";
			//调用银行打分上送接口时 失败 重新 调用
			if (faCenterDataBank!=null && "0".equals(faCenterDataBank.getIsScoreApply())) {
				Map<String, Object> scoreresult = null;
				//调用银行打分接口时构建map
				Map<String, Object> scoreMap = fwBusinessSxdService.bankScoringMap(fwBusinessSxd);
				//调用银行打分接口
				scoreresult = bankTools.uploadCustomLevelInfo(scoreMap);
				scoreresult = (Map<String, Object>) scoreresult.get("opRep");
				scoreRetCode = MapUtils.getString(scoreresult, "retCode","");//评分信息上传交易返回
			}
			
			Long userId = RequestUtils.getLoginedUser().getUserId();
			//加操作日志
    		String nodeName = MapUtils.getString(WorkFlowUtils.map,WorkFlowUtils.mranualCreditLimit);
    		
			if ("0".equals(scoreRetCode)){
				//银行打分接口成功 修改中间表状态
				//上送 打分成功  向银行中间表中记录状态 将isScoreApply字段值 修改为1
				faCenterDataBankService.updateCenterData(busId, "1");
				//调用银行额度申请接口
				Map<String, Object> quotaresult = bankTools.applyQuotaInfo(quotaMap);
				quotaresult = (Map<String, Object>) quotaresult.get("opRep");
				String retCode = MapUtils.getString(quotaresult, "retCode");//额度申请交易返回
				if ("0".equals(retCode)) {
					//处理流程进入下一节点
			    	Map<String, Object> variables = new HashMap<String, Object>();
					//向银行相关数据表任务表插入数据
					fwBusinessSxdService.insertDataQuota(busId, quotaresult);
			    	//处理该任务
			    	variables.put(WorkFlowUtils.role_mranual_credit_limit,true);
		            workFlowQuotaApplyService.complete(taskId, variables);
		          
		    		faOperationRecordService.saveFaOperationRecord(busId, userId, "", nodeName,OperationState.BUSINESS_SUCCESS);
					mapResult.put("msg", getMessage("send.success"));
		    		return JsonUtils.toJson(RequestUtils.successResult(mapResult));
				}else{
					faOperationRecordService.saveFaOperationRecord(busId, userId, "", nodeName,OperationState.BUSINESS_FAIL);
					mapResult.put("msg", getMessage("send.fail"));
					return JsonUtils.toJson(RequestUtils.failResult("error"));
				}
		    }else{
		    	 faOperationRecordService.saveFaOperationRecord(busId, userId, "", nodeName,OperationState.BUSINESS_FAIL);
		    	// mapResult.put("msg", getMessage("send.fail"));
		    	 return JsonUtils.toJson(RequestUtils.failResult(getMessage("send.fail")));
		    }
		   
		} catch (Exception e) {
			e.printStackTrace();
			//mapResult.put("msg", getMessage("send.fail"));
		  return JsonUtils.toJson(RequestUtils.failResult(getMessage("send.fail")));
		}
    }
    
    /**
     * 验证node是否有效
     * @param node
     * @return
     */
    private String nodeVerify(String node) {
    	if(node == null || node.trim().length() == 0) {
    		return null;
    	}
    	//人工复核
    	if (WorkFlowUtils.role_manual_review.equals(node)) {
    		return WorkFlowUtils.manualReview;
    	}
    	//核准授信额度
    	if (WorkFlowUtils.role_approved_credit_limit.equals(node)) {
    		return WorkFlowUtils.approvedCreditLimit;
    	}
    	//人工处理恩度申请
    	if (WorkFlowUtils.role_mranual_credit_limit.equals(node)) {
    		return WorkFlowUtils.mranualCreditLimit;
    	}
    	//客服电话通知
    	if (WorkFlowUtils.role_qutoa_service_call.equals(node)) {
    		return WorkFlowUtils.qutoaServiceCall;
    	}
    	//审批
    	if(WorkFlowUtils.role_xamination_approval.equals(node)){
    		return WorkFlowUtils.xaminationApproval;
    	}
    	//经理审批
    	if(WorkFlowUtils.quota_manager_approval_role.equals(node)){
    		return WorkFlowUtils.quotaManagerApproval;
    	}
    	// 额度申请驳回通知
    	if(WorkFlowUtils.quota_service_send_information_role.equals(node)){
    		return WorkFlowUtils.quotaServiceSendInformation;
    	}
    	return null;
    }
}

