package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evan.common.user.domain.FaCommunicateLog;
import com.evan.common.user.domain.FwQuickLoan;
import com.evan.common.user.service.FaCommunicateLogService;
import com.evan.common.user.service.FwQuickLoanService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.activiti.bean.QuickloanFlowView;
import com.evan.finance.admin.activiti.service.WorkFlowQuickloanService;
import com.evan.finance.admin.utils.WorkFlowUtils;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;

/**
 * 快速贷款申请办理
 * 
 * @author stone
 */
@RequestMapping("/workflow/quickloan")
@Controller
public class WorkFlowQuickloanController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(WorkFlowQuickloanController.class);
	
	@Autowired
	private WorkFlowQuickloanService workFlowQuickloanService;
	
	@Autowired
	private FwQuickLoanService fwQuickLoanService;
	
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
		QuickloanFlowView quickloanFlowView = workFlowQuickloanService.startProcess(businessKey);
		if (quickloanFlowView != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("businessKey", quickloanFlowView.getBusinessKey());
			map.put("processInstanceId", quickloanFlowView.getProcessInstance().getProcessInstanceId());
			map.put("processDefinitionId", quickloanFlowView.getProcessInstance().getProcessDefinitionId());
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
    	workFlowQuickloanService.claim(taskId, RequestUtils.getLoginedUser().getUserName());
        return "redirect:/workflow/list";
    }
    
    /**
     * 办理业务
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "task/complete/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public String complete(@PathVariable("id") String taskId, HttpServletRequest request,  RedirectAttributes attr) {
    	// 校验用户是否有权限，记录日志
    	
    	// 当前节点
    	String node = request.getParameter(WorkFlowUtils.process_definition_key_quickloan+"_node");
    	// 验证节点
    	String nodeKey = nodeVerify(node);
    	if (nodeKey == null) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，系统找不到当前节点，请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}
    	//处理结果
    	String condition = request.getParameter(WorkFlowUtils.process_definition_key_quickloan+"_condition");
    	// WorkFlowUtils.role_quickloan_confirm.equals(node)
    	// condition==1 满足条件，线下
    	// condition==2 满足条件,线上
    	// condition==3 不满足条件
    	/*if (!("1".equals(condition) || "2".equals(condition) || "3".equals(condition)) && WorkFlowUtils.role_quickloan_confirm.equals(node)) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}
    	if (!("true".equals(condition) || "false".equals(condition)) && !WorkFlowUtils.role_quickloan_confirm.equals(node)) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}*/
    	// 处理原因，不通过时会同步到业务数据
    	String reason = request.getParameter(WorkFlowUtils.process_definition_key_quickloan+"_reason");
    	reason = reason == null ? "" : reason;
    	
    	//快速贷款id
    	String businessKey = request.getParameter(WorkFlowUtils.process_definition_key_quickloan+"_businessKey");
    	if ("".equals(businessKey)) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，没有放款id,请联系系统管理员。");
    		return "redirect:/workflow/list";
		}
    	
    	Long userId = RequestUtils.getLoginedUser().getUserId();
    	Map<String, Object> variables = new HashMap<String, Object>();
    	try {
    		boolean status = true;
    		//进行业务逻辑处理  不符合条件
    		/*if ("3".equals(condition)) {
    			status = fwQuickLoanService.processFailCallback(Long.parseLong(businessKey), userId);
			}else if("true".equals(condition)){*/
				status = fwQuickLoanService.processSuccessCallback(Long.parseLong(businessKey), userId,reason);
			/*}*/
    		if (!status) {
    			attr.addFlashAttribute("errorMsg", "业务处理失败，请联系系统管理员。");
        		return "redirect:/workflow/list";
			}
    		
            variables.put("reason", reason);
            if (WorkFlowUtils.role_quickloan_confirm.equals(node)) {
            	variables.put(nodeKey, condition);
    		} else {
    			variables.put(nodeKey, condition == "true" ? true : false);
    		}
            
            workFlowQuickloanService.complete(taskId, variables);
        } catch (Exception e) {
            logger.error("error on complete task {}, variables={}", new Object[]{taskId, variables, e});
            attr.addFlashAttribute("errorMsg", "任务处理异常，请联系系统管理员。");
            return "redirect:/workflow/list";
        }
    	
    	// 公司名称
		String comNameString = request.getParameter("comName");
		comNameString = comNameString == null ? "" : comNameString;
		
		// 插入公司名称
		FwQuickLoan fwQuickLoan = fwQuickLoanService.saveComName(comNameString, Long.valueOf(businessKey));
		if (fwQuickLoan == null)
		{
			attr.addFlashAttribute("errorMsg", "保存公司名称出错,请联系系统管理员。");
    		return "redirect:/workflow/list";
		}
    	
    	// 插入快速贷款的备注信息,无论成功与否，该流程都结束
    	FaCommunicateLog faCommunicateLog = faCommunicateLogService.saveQuickLoanCommunicateLog(businessKey, ConstantTool.BusinessType.QUICK_LOAN, ConstantTool.CommlogType.QUICK_LOAN_REMARK, reason, userId);
    	if (faCommunicateLog == null)
    	{
    		attr.addFlashAttribute("errorMsg", "保存处理意见出错,请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}
    	
    	attr.addFlashAttribute("successMsg","处理成功");
        return "redirect:/workflow/list";
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
    	if (WorkFlowUtils.role_quickloan_confirm.equals(node)) {
    		return WorkFlowUtils.quickloanConfirm;
    	}
    	if (WorkFlowUtils.role_quickloan_web.equals(node)) {
    		return WorkFlowUtils.quickloanWeb;
    	}
    	if (WorkFlowUtils.role_quickloan_noweb.equals(node)) {
    		return WorkFlowUtils.quickloanNoweb;
    	}
    	return null;
    }
}

