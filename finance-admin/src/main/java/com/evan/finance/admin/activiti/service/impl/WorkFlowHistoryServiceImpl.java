package com.evan.finance.admin.activiti.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.domain.FwCompany;
import com.evan.common.user.domain.FwLoanApply;
import com.evan.common.user.domain.FwQuickLoan;
import com.evan.common.user.domain.JPage;
import com.evan.common.user.service.FaOperationRecordService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwCompanyService;
import com.evan.common.user.service.FwLoanApplyService;
import com.evan.common.user.service.FwQuickLoanService;
import com.evan.common.utils.DateUtils;
import com.evan.finance.admin.activiti.bean.Historic;
import com.evan.finance.admin.activiti.bean.WorkFlow;
import com.evan.finance.admin.activiti.service.WorkFlowHistoryService;
import com.evan.finance.admin.utils.ActivityUtil;
import com.evan.jaron.plugins.security.SecurityTokenHolder;

@Service
public class WorkFlowHistoryServiceImpl implements WorkFlowHistoryService{

	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	RepositoryService repositoryService;
	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	
	@Autowired
	private FaOperationRecordService operationRecordService;
	
	@Autowired
	private FaOperationRecordService faOperationRecordService;
	
	@Autowired
	private FwQuickLoanService fwQuickLoanService;
	
	@Autowired
	private FwLoanApplyService fwLoanApplyService;
	
	@Autowired
	private FwCompanyService fwCompanyService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	/**
     * 历史记录分页列表
     * @param page 分页对象
     */
	public JPage historicList(JPage page){
		String userName = SecurityTokenHolder.getSecurityToken().getUser().getUserName();
		
		HistoricTaskInstanceQuery histTaskQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(userName).finished().orderByHistoricTaskInstanceEndTime().desc();
		Long count = histTaskQuery.count();
		page.setiTotalDisplayRecords(count);
		page.setiTotalRecords(count);
		
		List<HistoricTaskInstance> histList = histTaskQuery.listPage(page.getiDisplayStart(), page.getiDisplayLength());
		List<Map<String, Object>> aList = new ArrayList<Map<String, Object>>();
		
		for (HistoricTaskInstance history : histList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("assignee", history.getAssignee());
			map.put("name", history.getName());
			map.put("procInsId", history.getProcessInstanceId());
			map.put("procDefId", history.getProcessDefinitionId());
			//查询流程名称
			ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(history.getProcessDefinitionId()).singleResult();
			map.put("pdName", pd.getName());
			map.put("endTime",DateUtils.formatDate(history.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
			
			//查询企业名称
			if(history.getProcessDefinitionId().startsWith(ActivityUtil.PROCDEFKEY)){
				FwBusinessSxd bus = fwBusinessSxdService.getFwBusinessSxdByPInsId(history.getProcessInstanceId());
				map.put("comName", bus == null ? "" : bus.getComName());
			} else if(history.getProcessDefinitionId().startsWith(ActivityUtil.QUICK_LOAN_PROCDEFKEY)){
				FwQuickLoan quick = fwQuickLoanService.getFwQuickLoanByPInsId(history.getProcessInstanceId());
				map.put("comName", quick == null ? "" : quick.getComName());
			} else if(history.getProcessDefinitionId().startsWith(ActivityUtil.LOAN_PROCDEFKEY)){
				FwLoanApply fa = fwLoanApplyService.getLoanApplyByProcessInstanceId(history.getProcessInstanceId());
				if(fa != null){
					FwCompany com = fwCompanyService.getCompanyByComId(fa.getComId());
					map.put("comName", com == null ? "" : com.getComName());
				} else {
					map.put("comName", "");
				}
			} else {
				map.put("comName", "");
			}
			aList.add(map);
		}
		page.setAaData(aList);
		
		return page;
	}
	
	/**
     * 历史详细记录
     * @param procInsId 流程实例ID
     */
	public List<WorkFlow> historyFlowList (String procInsId){
		List<WorkFlow> workList = new ArrayList<WorkFlow>();
		List<Map<String, Object>> list = faOperationRecordService.historyFlowList(procInsId);
		for(Map<String, Object> map : list){
			WorkFlow w = new WorkFlow();
			Historic h = new Historic();
			//判断是开始节点
			if("startEvent".equals(map.get("ACT_TYPE_") == null ? "" : map.get("ACT_TYPE_").toString())){
				w.setAssignee(this.getAssignee(procInsId, map.get("PROC_DEF_ID_") == null ? "" : map.get("PROC_DEF_ID_").toString()));
			}
			//判断是结束节点
			else if("endEvent".equals(map.get("ACT_TYPE_") == null ? "" : map.get("ACT_TYPE_").toString())){
				w.setAssignee("系统");
			}
			//判断是自动节点
			else if("serviceTask".equals(map.get("ACT_TYPE_") == null ? "" : map.get("ACT_TYPE_").toString())) {
				w.setAssignee("系统");
			} 
			else if("sys_admin".equals(map.get("ASSIGNEE_") == null ? "" : map.get("ASSIGNEE_"))
					&& "userTask".equals(map.get("ACT_TYPE_") == null ? "" : map.get("ACT_TYPE_").toString())){
				w.setAssignee(map.get("END_TIME_") == null ? "" : "系统");
			}
			else {
				w.setAssignee(map.get("ASSIGNEE_") == null ? "" : map.get("ASSIGNEE_").toString().startsWith(DateUtils.getYear()) ? "" : map.get("ASSIGNEE_").toString());
			}
			h.setActivityName(map.get("ACT_NAME_") == null ? "" : map.get("ACT_NAME_").toString());
			h.setStartTime(map.get("START_TIME_") == null ? "" : map.get("START_TIME_").toString());
			h.setEndTime(map.get("END_TIME_") == null ? "" : map.get("END_TIME_").toString());
			w.setHistIns(h);
			
			// 获取意见评论内容
			if (null == map.get("TASK_ID_") || ("null").equals(map.get("COMMENT_"))){
				if(null != map.get("TASK_ID_") && !("null").equals(map.get("TASK_ID_"))){
					List<Comment> commentList = taskService.getTaskComments(map.get("TASK_ID_").toString());
					if (commentList.size()>0){
						w.setComment(commentList.get(0).getFullMessage());
					}
				}
			} else {
				w.setComment(map.get("COMMENT_").toString());
			}
			
			workList.add(w);
		}
		return workList;
	}
	
	/**
     * 查询启动流程的企业名称
     * @param procInsId 流程实例ID
     */
	private String getAssignee(String procInsId, String processDefinitionId){
		if(StringUtils.isNotBlank(procInsId) && StringUtils.isNotBlank(processDefinitionId)){
			if(processDefinitionId.startsWith(ActivityUtil.PROCDEFKEY)){
				List<HistoricProcessInstance> il = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInsId).orderByProcessInstanceStartTime().asc().list();
				if (il.size() > 0){
					if (StringUtils.isNotBlank(il.get(0).getProcessDefinitionId())){
						FwBusinessSxd bus = fwBusinessSxdService.getFwBusinessSxdByPInsId(procInsId);
						return bus == null ? "" : bus.getComName();
					}
				}
				return "";
			} else if(processDefinitionId.startsWith(ActivityUtil.QUICK_LOAN_PROCDEFKEY)){
				FwQuickLoan quick = fwQuickLoanService.getFwQuickLoanByPInsId(procInsId);
				return quick == null ? "" : quick.getComName();
			} else if(processDefinitionId.startsWith(ActivityUtil.LOAN_PROCDEFKEY)){
				FwLoanApply fa = fwLoanApplyService.getLoanApplyByProcessInstanceId(procInsId);
				if(fa != null){
					FwCompany com = fwCompanyService.getCompanyByComId(fa.getComId());
					return com == null ? "" : com.getComName();
				} else {
					return "";
				}
			}
		}
		return "";
	}
}
