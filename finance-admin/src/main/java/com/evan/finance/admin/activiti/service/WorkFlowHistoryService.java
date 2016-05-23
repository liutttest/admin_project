package com.evan.finance.admin.activiti.service;

import java.util.List;

import com.evan.common.user.domain.JPage;
import com.evan.finance.admin.activiti.bean.WorkFlow;

public interface WorkFlowHistoryService {

	/**
     * 历史记录分页列表
     * @param page 分页对象
     */
	public JPage historicList(JPage page);
	
	/**
     * 历史详细记录
     * @param procInsId 流程实例ID
     */
	public List<WorkFlow> historyFlowList (String procInsId);
}
