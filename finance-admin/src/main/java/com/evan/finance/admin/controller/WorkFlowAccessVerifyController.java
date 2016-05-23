package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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

import com.evan.common.user.domain.FwBankRelationFiles;
import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.monitor.MonitorTools;
import com.evan.common.user.service.FaAccessTaskService;
import com.evan.common.user.service.FaCenterDataBankService;
import com.evan.common.user.service.FaCommunicateLogService;
import com.evan.common.user.service.FaInfoNopassService;
import com.evan.common.user.service.FaOperationRecordService;
import com.evan.common.user.service.FaTempSceneService;
import com.evan.common.user.service.FilesBankService;
import com.evan.common.user.service.FwBankRelationFilesService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwComAccountService;
import com.evan.common.user.service.FwMessageService;
import com.evan.common.user.service.SysUserService;
import com.evan.common.utils.ConstantTool.ApplyState;
import com.evan.common.utils.ConstantTool.BankFileSceneKey;
import com.evan.common.utils.ConstantTool.BusinessType;
import com.evan.common.utils.ConstantTool.CommlogType;
import com.evan.common.utils.ConstantTool.OperationState;
import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.activiti.bean.WorkFlowView;
import com.evan.finance.admin.activiti.service.WorkFlowAccessVerifyService;
import com.evan.finance.admin.bank.BankTools;
import com.evan.finance.admin.notification.service.NotificationService;
import com.evan.finance.admin.utils.WorkFlowUtils;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;
import com.evan.nd.common_file.service.FwFilesService;

/**
 * 准入验证办理
 * 
 * @author stone
 */
@RequestMapping("/workflow/accessverify")
@Controller
public class WorkFlowAccessVerifyController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(WorkFlowAccessVerifyController.class);
	
	@Autowired
	private WorkFlowAccessVerifyService workFlowAccessVerifyService;
	
	@Autowired
	private FwMessageService fwMessageService;
	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	
	@Autowired
	private FaCenterDataBankService faCenterDataBankService;
	
	@Autowired
	private FaAccessTaskService faAccessTaskService;
	
	@Autowired
	private BankTools bankTools;
	
	@Autowired
	private FwFilesService filesService;
	
	@Autowired
	private MonitorTools monitorTools;
	
	@Autowired
	private FilesBankService filesBankService;
	
	@Autowired
	private FaCommunicateLogService faCommunicateLogService;
	
	@Autowired
	private FaOperationRecordService faOperationRecordService;
	
	@Autowired
	private FaInfoNopassService faInfoNopassService;
	
	@Autowired
	private FwComAccountService fwComAccountService;
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private FaTempSceneService faTempSceneService;
	
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
		WorkFlowView accessVerifyFlowView = workFlowAccessVerifyService.startProcess(businessKey);
		if (accessVerifyFlowView != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("businessKey", accessVerifyFlowView.getBusinessKey());
			map.put("processInstanceId", accessVerifyFlowView.getProcessInstance().getProcessInstanceId());
			map.put("processDefinitionId", accessVerifyFlowView.getProcessInstance().getProcessDefinitionId());
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
    	workFlowAccessVerifyService.claim(taskId, RequestUtils.getLoginedUser().getUserName());
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
    	String node = request.getParameter(WorkFlowUtils.process_definition_key_access_verify+"_node");
    	// 验证节点
    	String nodeKey = nodeVerify(node);
    	if (nodeKey == null) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，系统找不到当前节点，请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}
    	
    	//得到当前登陆用户id
    	Long userId = RequestUtils.getLoginedUser().getUserId();
    	
    	// 处理原因，不通过时会同步到业务数据
    	String reason = request.getParameter(WorkFlowUtils.process_definition_key_access_verify+"_reason");
    	reason = reason == null ? "" : reason;
    	
    	//业务id
    	String businessKey = request.getParameter(WorkFlowUtils.process_definition_key_access_verify+"_businessKey");
    	if ("".equals(businessKey)) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，没有业务id,请联系系统管理员。");
    		return "redirect:/workflow/list";
		}
    	
    	//处理结果
    	String condition = request.getParameter(WorkFlowUtils.process_definition_key_access_verify+"_condition");
    	if (!("true".equals(condition) || "false".equals(condition)) && !nodeKey.equals(WorkFlowUtils.manualVerify)) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}
    	
    	Map<String, Object> variables = new HashMap<String, Object>();
    	
    	boolean status = true;
    	 //加操作日志
		String nodeName = MapUtils.getString(WorkFlowUtils.map,nodeKey);
    	try {
    		//定义操作记录状态
    		String operState = OperationState.BUSINESS_SUCCESS;
    		
    		//人工验证
    		if (nodeKey.equals(WorkFlowUtils.manualVerify) && !"3".equals(condition)) {
    			//如果验证失败不通过的话
    			//调用系统方法进行验证
    			status = fwBusinessSxdService.personScoring(map);
    			if ("2".equals(condition) || !status) {
    				//准入失败
					condition = "2";
					operState = OperationState.BUSINESS_NO_PASS;
				}else if("1".equals(condition)){
					//将收集到的人工打分所需要的资料录入数据库 并且生成客户号 验证通过的话
					faCenterDataBankService.saveQuotaData(map,businessKey,BusinessType.SXD);
    				//准入通过
    				condition = "1";
    				operState = OperationState.BUSINESS_PASS;
				}
    		
    			monitorTools.infoBusinessAccess("人工验证", businessKey, JsonUtils.toJson(map));
    			
    			//驳回
    		}else if(nodeKey.equals(WorkFlowUtils.manualVerify) && "3".equals(condition)){
    			//准入驳回为
    			condition = "3";
    			nodeName =  MapUtils.getString(WorkFlowUtils.map,"manualVerifyReject");//准入验证驳回节点
    		}
    		//准入驳回通知（luy）
    		if (nodeKey.equals(WorkFlowUtils.accessServiceSendInformation)) {
    			
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
    			fwBusinessSxdService.updateState(businessKey, ApplyState.ADMITTANCE_REJECT, userId);
    			//监控
    			String  contentString = notificationService.monitorContentCreate(isSendEmail, isSendSms, isSendMessage, retMap);
    			monitorTools.infoBusinessAccess("发送准入驳回通知", businessKey, contentString);
    			nodeName = MapUtils.getString(WorkFlowUtils.map,"accessServiceSendInformation");//准入验证驳回客服发送通知
			}
    		
    		//客服通知结果
    		if (nodeKey.equals(WorkFlowUtils.servicePhoneCall)) {
    			
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
    			FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(businessKey);
    			//如果是准入验证通过  将数据库状态修改为 额度申请
    			if (fwBusinessSxd.getApplyState().equals(ApplyState.ADMITTANCE_PASS)) {
					fwBusinessSxdService.updateState(businessKey, ApplyState.LIMIT_APPLYING, userId);
				}else{
					fwBusinessSxdService.updateState(businessKey, ApplyState.REJECT, userId);
				}
    			//准入客服电话通知
    			faCommunicateLogService.saveFaCommunicateLog(businessKey, BusinessType.SXD, CommlogType.ACCESS_RESULT, reason);
				
    			//监控
    			String  contentString = notificationService.monitorContentCreate(isSendEmail, isSendSms, isSendMessage, retMap);
    			if (condition.equals("true")) {
    				monitorTools.infoBusinessAccess("发送准入验证成功通知", businessKey, contentString);
				}else if(condition.equals("false")){
					monitorTools.infoBusinessAccess("发送准入验证失败通知", businessKey, contentString);
				}
			}else{
				if (!StringUtils.isEmpty(reason)) {
	    			//准入备注
	    			faCommunicateLogService.saveFaCommunicateLog(businessKey, BusinessType.SXD, CommlogType.ACCESS_REMARK, reason);
				}
			}
    		
    	
    		variables.put("reason", reason);
            //variables.put(nodeKey,condition);
            if (WorkFlowUtils.manualVerify.equals(nodeKey)) {
            	variables.put(nodeKey, condition);
    		} else {
    			variables.put(nodeKey, "true".equals(condition) ? true : false);
    		}
            workFlowAccessVerifyService.complete(taskId, variables);
            faOperationRecordService.saveFaOperationRecord(businessKey, userId, reason, nodeName,operState);
        } catch (Exception e) {
            logger.error("error:", e);
            attr.addFlashAttribute("errorMsg", "任务处理异常，请联系系统管理员。");
            return "redirect:/workflow/list";
        }
    	attr.addFlashAttribute("successMsg","处理成功");
        return "redirect:/workflow/list";
    }
    
    
    
    
    /**
     * 办理业务 人工办理业务
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/task/personDone/{busId}/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String personDone(@PathVariable("busId") String busId,@PathVariable("taskId") String taskId, HttpServletRequest request, RedirectAttributes attr) {
    	//调用银行登录接口
		bankTools.cebankUserLogon();
		
		
		//查询业务数据
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId);
		//上传文件 该处需要上传两个文件
		List<FwBankRelationFiles> fileList = fwBankRelationFilesService.findByProcessIdAndSceneKey(fwBusinessSxd.getProcessInstanceId(), BankFileSceneKey.ACCESS_KEY);
		//上传文件 该处需要上传4 文件
		List<Map<String,String>> list = fwBusinessSxdService.createFileMapBank(fileList, "FILE_TYPE");
		Long userId = RequestUtils.getLoginedUser().getUserId();
		//加操作日志
 		String nodeName = MapUtils.getString(WorkFlowUtils.map,WorkFlowUtils.mranualProcessing);
 		Map<String, Object> mapResult = new HashMap<String, Object>();
		try {
			//调用银行接口 企业征信查询接口是构建map
			Map<String, Object> creditMap = fwBusinessSxdService.bankCreditInfoMap(fwBusinessSxd);
			creditMap.put("list", list);
			//调用银行授信接口
			Map<String, Object> result = bankTools.queryCreditInfo(creditMap);
			result = (Map<String, Object>) result.get("opRep");
		    String retCode = MapUtils.getString(result, "retCode");//本次交易返回
		    logger.debug("retCodezhengxin======="+result);
		    if (retCode.equals("0")) {
		    	//向数据库中插入数据
		    	fwBusinessSxdService.insertDataAccess(busId, result);
		    	//处理流程进入下一节点
		    	Map<String, Object> variables = new HashMap<String, Object>();
		    	//处理该任务
		    	variables.put(WorkFlowUtils.role_mranual_processing,true);
	            workFlowAccessVerifyService.complete(taskId, variables);
	            faOperationRecordService.saveFaOperationRecord(busId, userId, "", nodeName,OperationState.BUSINESS_SUCCESS);
				mapResult.put("msg",getMessage("send.success"));
	            return JsonUtils.toJson(RequestUtils.successResult(mapResult));
		    }
		    faOperationRecordService.saveFaOperationRecord(busId, userId, "", nodeName,OperationState.BUSINESS_FAIL);
		    //mapResult.put("msg", getMessage("send.fail"));
		    return JsonUtils.toJson(RequestUtils.failResult(getMessage("send.fail")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			faOperationRecordService.saveFaOperationRecord(busId, userId, "", nodeName,OperationState.BUSINESS_FAIL);
			 //mapResult.put("msg", getMessage("send.fail"));
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("send.fail")));
		}
    }
    
    /**
     * 办理业务 人工办理业务 处理等待
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/task/personDone/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String personDon(@PathVariable("taskId") String taskId, HttpServletRequest request, RedirectAttributes attr) {
    	Map<String, Object> variables = new HashMap<String, Object>();
		try {
		    	variables.put(WorkFlowUtils.quota_wait_cus_information,true);
	            workFlowAccessVerifyService.complete(taskId, variables);
				return JsonUtils.toJson(RequestUtils.successResult(null));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  return JsonUtils.toJson(RequestUtils.failResult("error"));
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
    	//注入人工验证
    	if (WorkFlowUtils.role_manual_verify.equals(node)) {
    		return WorkFlowUtils.manualVerify;
    	}
    	if (WorkFlowUtils.role_mranual_processing.equals(node)){
    		return WorkFlowUtils.mranualProcessing;
    	}
    	if(WorkFlowUtils.role_service_phone_call.equals(node)){
    		return WorkFlowUtils.servicePhoneCall;
    	}
    	if(WorkFlowUtils.manual_automatic_role.equals(node)){
    		return WorkFlowUtils.manualAutomatic;
    	}
    	if (WorkFlowUtils.access_service_send_information_role.equals(node)) {
    		return WorkFlowUtils.accessServiceSendInformation;
		}
  
    	return null;
    }
    
    
}

