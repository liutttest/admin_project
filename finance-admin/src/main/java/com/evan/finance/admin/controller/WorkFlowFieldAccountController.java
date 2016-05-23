package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.monitor.MonitorTools;
import com.evan.common.user.service.FaCommunicateLogService;
import com.evan.common.user.service.FaFalseUsersService;
import com.evan.common.user.service.FaOperationRecordService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwCompanyService;
import com.evan.common.utils.ConstantTool.CommlogType;
import com.evan.common.utils.ConstantTool.OperationState;
import com.evan.common.utils.RequestUtils;
import com.evan.common.utils.ConstantTool.BusinessType;
import com.evan.finance.admin.activiti.bean.WorkFlowView;
import com.evan.finance.admin.activiti.service.WorkFlowFeildAccountService;
import com.evan.finance.admin.bank.BankTools;
import com.evan.finance.admin.utils.WorkFlowUtils;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;

/**
 * 现场开户办理
 * 
 * @author liutt
 */
@RequestMapping("/workflow/fieldaccount")
@Controller
public class WorkFlowFieldAccountController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(WorkFlowFieldAccountController.class);
	
	@Autowired
	private WorkFlowFeildAccountService workFlowFeildAccountService;	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	
	@Autowired
	private FaOperationRecordService faOperationRecordService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FaFalseUsersService faFalseUsersService;
	
	@Autowired
	private FwCompanyService fwCompanyService;
	
	@Autowired
	private BankTools bankTools;
	@Autowired
	private MonitorTools monitorTools;
	@Autowired
	private FaCommunicateLogService faCommunicateLogService;
	
	/**
	 * 通过URL启动流程
	 * @param searchForm
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/startProcess/{key}", method = {GET, POST})
	@ResponseBody
	public String startProcess(@PathVariable("key") String businessKey) {
		WorkFlowView fcdFlowView = workFlowFeildAccountService.startProcess(businessKey);
		if (fcdFlowView != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("businessKey", fcdFlowView.getBusinessKey());
			map.put("processInstanceId", fcdFlowView.getProcessInstance().getProcessInstanceId());
			map.put("processDefinitionId", fcdFlowView.getProcessInstance().getProcessDefinitionId());
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
    	workFlowFeildAccountService.claim(taskId, RequestUtils.getLoginedUser().getUserName());
        return "redirect:/workflow/list";
    }
    
    /**
     * 办理业务
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "task/complete/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public String complete(@PathVariable("id") String taskId, HttpServletRequest request, RedirectAttributes attr) {
    	// 校验用户是否有权限，记录日志
    	
    	// 当前节点
    	String node = request.getParameter(WorkFlowUtils.process_definition_key_field_account+"_node");
    	// 验证节点
    	String nodeKey = nodeVerify(node);
    	if (nodeKey == null) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，系统找不到当前节点，请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}
    	//处理结果
    	String condition = request.getParameter(WorkFlowUtils.process_definition_key_field_account+"_condition");
    	if (!("true".equals(condition) || "false".equals(condition))) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}
    	
    	// 处理原因，不通过时会同步到业务数据
    	String reason = request.getParameter(WorkFlowUtils.process_definition_key_field_account+"_reason");
    	reason = reason == null ? "" : reason;
    	
    	//业务id
    	String businessKey = request.getParameter(WorkFlowUtils.process_definition_key_field_account+"_businessKey");
    	if ("".equals(businessKey)) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，没有业务id,请联系系统管理员。");
    		return "redirect:/workflow/list";
		}
    	
    	Map<String, Object> variables = new HashMap<String, Object>();
    	
    	//得到当前登陆用户id
    	//Long userId = RequestUtils.getLoginedUser().getUserId();
    	//boolean status=true;
    	//加操作日志
		String nodeName = MapUtils.getString(WorkFlowUtils.map,nodeKey);
    	try {
    		//定义操作记录状态
    		String operState = OperationState.BUSINESS_SUCCESS;
    		//业务处理
    		if (WorkFlowUtils.taskAssign.equals(nodeKey)) {
    			//任务分配
        		String taskPerson = request.getParameter(WorkFlowUtils.process_definition_key_field_account+"_taskPerson");
        		System.out.println("taskPerson=========="+taskPerson);
        		
        		String[] person = taskPerson.split(",");
        		//任务分配
        		String taskPer = faFalseUsersService.assignTask(person);
        		variables.put("userId", taskPer);
    		}else if(WorkFlowUtils.dataBackfill.equals(nodeKey)){
    			//数据回填 U顿号
    			/*String dataFill = request.getParameter(WorkFlowUtils.process_definition_key_field_account+"_dataFill");*/
    			//合同号
    			String contractNum = request.getParameter(WorkFlowUtils.process_definition_key_field_account+"_contractNum");
    			//是否支付服务费
    			String serviceChargeState = request.getParameter(WorkFlowUtils.process_definition_key_field_account+"_serviceChargeState");
    			//是否开具发票
    			String isInvoicing = request.getParameter(WorkFlowUtils.process_definition_key_field_account+"_isInvoicing");
    			String serviceChargeActual = request.getParameter(WorkFlowUtils.process_definition_key_field_account+"_serviceChargeActual");
    			List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).list();
    			Task task = null; 
    			for (Task task1 : tasks) {
					if (WorkFlowUtils.role_data_backfill.equals(task1.getTaskDefinitionKey())) {
						task=task1;
					}
				}
    			//将数据回填到业务表中 将U盾回填到数据表中
    			fwCompanyService.updateCompanyUCode(businessKey, "",contractNum,serviceChargeState,isInvoicing,serviceChargeActual);
    			//信息回填之后 将分配任务关系解除
    			faFalseUsersService.removeUsersbyuserName(task.getAssignee());
    		
    		}else if(WorkFlowUtils.businessArchive.endsWith(nodeKey)){
    			String archive = request.getParameter(WorkFlowUtils.process_definition_key_field_account+"_archive");
    			fwBusinessSxdService.updateFwbusinessSxd(businessKey,archive);
    		}
    		if (WorkFlowUtils.accountServiceCall.equals(nodeKey)) {
    			//现场开户客服通知结果
    			faCommunicateLogService.saveFaCommunicateLog(businessKey, BusinessType.SXD, CommlogType.FIELD_RESULT, reason);
    		}else{
				if (reason!=null) {
	    			//现场开户备注
	    			faCommunicateLogService.saveFaCommunicateLog(businessKey, BusinessType.SXD, CommlogType.ACCESS_REMARK, reason);
				}
			}
    		
    		Long userId = RequestUtils.getLoginedUser().getUserId();
		    
            variables.put("reason", reason);
            
            variables.put(nodeKey, "true".equals(condition) ? true : false);
            workFlowFeildAccountService.complete(taskId, variables);
            //加操作日志
    		faOperationRecordService.saveFaOperationRecord(businessKey, userId, reason, nodeName,operState);
        } catch (Exception e) {
//        	e.printStackTrace();
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
		//查询业务数据
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId);
		//调用银行接口 企业征信查询接口是构建map
		Map<String, Object> accountMap = fwBusinessSxdService.bankFieldAccountMap(fwBusinessSxd);
		Long userId = RequestUtils.getLoginedUser().getUserId();
		//加操作日志
 		String nodeName = MapUtils.getString(WorkFlowUtils.map,WorkFlowUtils.mranualAccount);
 		Map<String, Object> mapResult = new HashMap<String, Object>();
		try{
			//调用银行开户申请接口
			Map<String, Object> result = bankTools.applyOpenAccount(accountMap);
			result = (Map<String, Object>) result.get("opRep");
			String retCode = MapUtils.getString(result, "retCode");//本次交易返回
			Map<String, Object> variables = new HashMap<String, Object>();
			if (retCode.equals("0")) {
				//请求成功
				//向银行相关数据表任务表插入数据
				fwBusinessSxdService.insertDataFieldAccount(busId, result);
				variables.put(WorkFlowUtils.bankCheck,true);
				 workFlowFeildAccountService.complete(taskId, variables);
				
	    		faOperationRecordService.saveFaOperationRecord(busId, userId, "", nodeName,OperationState.BUSINESS_SUCCESS);
	    		mapResult.put("msg", getMessage("send.success"));
	    		return JsonUtils.toJson(RequestUtils.successResult(mapResult));
			}
			faOperationRecordService.saveFaOperationRecord(busId, userId, "", nodeName,OperationState.BUSINESS_FAIL);
			//mapResult.put("msg", getMessage("send.fail"));
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("send.fail")));
		} catch (Exception e) {
			e.printStackTrace();
			faOperationRecordService.saveFaOperationRecord(busId, userId, "", nodeName,OperationState.BUSINESS_FAIL);
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
    	if (WorkFlowUtils.role_task_assign.equals(node)) {
    		return WorkFlowUtils.taskAssign;
    	}
    	if (WorkFlowUtils.role_data_backfill.equals(node)) {
    		return WorkFlowUtils.dataBackfill;
    	}
    	if (WorkFlowUtils.role_mranual_account.equals(node)) {
    		return WorkFlowUtils.mranualAccount;
    	}
    	if(WorkFlowUtils.role_account_service_call.equals(node)){
    		return WorkFlowUtils.accountServiceCall;
    	}
    	if (WorkFlowUtils.role_business_archive.equals(node)) {
			return WorkFlowUtils.businessArchive;
		}
    	return null;
    }
}

