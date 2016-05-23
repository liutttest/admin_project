package com.evan.finance.admin.controller;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.common.user.domain.JPage;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.finance.admin.activiti.bean.WorkFlow;
import com.evan.finance.admin.activiti.service.WorkFlowHistoryService;
import com.evan.jaron.core.web.controller.BaseController;

/**
 * 流程历史处理类
 * @author Gaoy
 */
@RequestMapping("/workflowHistory")
@Controller
public class WorkFlowHistoryController extends BaseController {

	Logger logger = LoggerFactory.getLogger(WorkFlowHistoryController.class);
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private WorkFlowHistoryService workFlowHistoryService;
	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	
	
	@RequestMapping(value = "/toList")
	public String toList() {
		return "activiti/history";
	}
	
	/**
     * 已办分页列表
     * @param request
     * @param response
     */
	@RequestMapping(value="/list")
	@ResponseBody
	public Object historicList(JPage page){
		try{
			return workFlowHistoryService.historicList(page);
		} catch(Exception e){
			logger.error("已办查询异常：", e);
		}
		return null;
	}
	
	/**
     * 历史详细记录
     * @param workFlow
     * @param request
     * @param response
     */
	@RequestMapping(value="/flowList")
	@ResponseBody
	public Object historyFlowList(WorkFlow workFlow){
		try{
			if(StringUtils.isNotEmpty(workFlow.getProcInsId())){
				return workFlowHistoryService.historyFlowList(workFlow.getProcInsId());
			}
		} catch(Exception e){
			logger.error("历史详细记录查询异常：", e);
		}
		return null;
	}
}
