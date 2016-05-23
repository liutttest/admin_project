package com.evan.finance.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.finance.admin.activiti.bean.WorkFlow;
import com.evan.finance.admin.activiti.service.WorkFlowInstanceService;
import com.evan.jaron.core.web.controller.BaseController;

/**
 * 流程实例处理类
 * @author Gaoy
 */
@RequestMapping("/workflowInstance")
@Controller
public class WorkFlowInstanceController extends BaseController {
	
	Logger logger = LoggerFactory.getLogger(WorkFlowInstanceController.class);
	
	@Autowired
	private WorkFlowInstanceService workFlowInstanceService;

	
	@RequestMapping(value = "/toFlowState")
	public String toFlowState(HttpServletRequest request,
			@RequestParam Map<String, Object> map) {
		return "activiti/flowState";
	}
		
	/**
     * 激活流程实例
     * @param workFlow
     * @param request
     * @param response
     */
	@RequestMapping(value="/active")
	@ResponseBody
	public Object setFlowState(WorkFlow workFlow, HttpServletRequest request, HttpServletResponse response){
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(StringUtils.isNotEmpty(workFlow.getProcInsId())){
			map.put("state", workFlowInstanceService.activateProcessInstance(workFlow.getProcInsId()));
		}
		return map;
	}
	
	/**
     * 挂起流程实例
     * @param workFlow
     * @param request
     * @param response
     */
	@RequestMapping(value="/suspend")
	@ResponseBody
	public Object suspend(WorkFlow workFlow, HttpServletRequest request, HttpServletResponse response){
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(StringUtils.isNotEmpty(workFlow.getProcInsId())){
			map.put("state", workFlowInstanceService.suspendProcessInstance(workFlow.getProcInsId()));
		}
		return map;
	}
	
}
