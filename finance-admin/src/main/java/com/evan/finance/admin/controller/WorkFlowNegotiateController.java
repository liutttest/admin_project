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

import com.evan.common.user.service.FwBusinessTalksService;
import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.activiti.bean.NegotiateFlowView;
import com.evan.finance.admin.activiti.service.WorkFlowNegotiateService;
import com.evan.finance.admin.utils.WorkFlowUtils;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;

/**
 * 商务洽谈
 * 
 * @author stone
 */
@RequestMapping("/workflow/negotiate")
@Controller
public class WorkFlowNegotiateController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(WorkFlowNegotiateController.class);
	
	@Autowired
	private WorkFlowNegotiateService workFlowNegotiateService;
	
	@Autowired
	private FwBusinessTalksService fwBusinessTalksService;
	
	/**
	 * 通过URL启动流程
	 * @param searchForm
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/startProcess/{key}", method = {GET, POST})
	@ResponseBody
	public String startProcess(@PathVariable("key") String businessKey) {
		NegotiateFlowView negotiateFlowView = workFlowNegotiateService.startProcess(businessKey);
		if (negotiateFlowView != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("businessKey", negotiateFlowView.getBusinessKey());
			map.put("processInstanceId", negotiateFlowView.getProcessInstance().getProcessInstanceId());
			map.put("processDefinitionId", negotiateFlowView.getProcessInstance().getProcessDefinitionId());
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
    	workFlowNegotiateService.claim(taskId, RequestUtils.getLoginedUser().getUserName());
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
    	String node = request.getParameter(WorkFlowUtils.process_definition_key_negotiate+"_node");
    	// 验证节点
    	String nodeKey = nodeVerify(node);
    	if (nodeKey == null) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，系统找不到当前节点，请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}
    	//处理结果
    	String condition = request.getParameter(WorkFlowUtils.process_definition_key_negotiate+"_condition");
    	if (!("true".equals(condition) || "false".equals(condition))) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}
    	// 处理原因，不通过时会同步到业务数据
    	String reason = request.getParameter(WorkFlowUtils.process_definition_key_negotiate+"_reason");
    	reason = reason == null ? "" : reason;
    	
    	//业务id
    	String businessKey = request.getParameter(WorkFlowUtils.process_definition_key_negotiate+"_businessKey");
    	if ("".equals(businessKey)) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，没有放款id,请联系系统管理员。");
    		return "redirect:/workflow/list";
		}
    	
    	Long userId = RequestUtils.getLoginedUser().getUserId();
    	Map<String, Object> variables = new HashMap<String, Object>();
    	try {
    		// TODO 处理意见及结果将同步到业务数据
    		boolean status = fwBusinessTalksService.processCallback(Long.parseLong(businessKey), userId);
    		if (status==false) {
    			attr.addFlashAttribute("errorMsg", "业务处理失败，请联系系统管理员。");
        		return "redirect:/workflow/list";
			}
    		
    		variables.put("reason", reason);
            variables.put(nodeKey, "true".equals(condition) ? true : false);
            workFlowNegotiateService.complete(taskId, variables);
        } catch (Exception e) {
            logger.error("error on complete task {}, variables={}", new Object[]{taskId, variables, e});
            attr.addFlashAttribute("errorMsg", "任务处理异常，请联系系统管理员。");
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
    	if (WorkFlowUtils.role_negotiate_processing.equals(node)) {
    		return WorkFlowUtils.role_negotiate_processing;
    	}
    	return null;
    }
}

