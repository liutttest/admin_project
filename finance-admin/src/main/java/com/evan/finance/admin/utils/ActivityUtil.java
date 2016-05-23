package com.evan.finance.admin.utils;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

public class ActivityUtil {
	
	
	public static final String PROCDEFKEY = "loanNew";//流程KEY
	public static final String BUSINESS_TABLE = "fw_business_sxd";//业务表明
	
	public static final String QUICK_LOAN_PROCDEFKEY = "quickloan";//快速贷款流程KEY
	
	public static final String LOAN_PROCDEFKEY = "loan";//放款流程KEY
	
	

	public static List<String> getActiveActivityIds(
			RuntimeService runtimeService, String processInstanceId) {
		// 会通过id -> parent_id -> parent_id -> ... 循环找出所有的执行中的子流程
		return runtimeService.getActiveActivityIds(processInstanceId);
	}

	public static List<ActivityImpl> getFlatAllActivities(
			ProcessDefinitionEntity processDefinition) {
		List<ActivityImpl> result = new ArrayList<ActivityImpl>();
		flattenActivities(result, processDefinition.getActivities());
		return result;
	}

	private static void flattenActivities(List<ActivityImpl> container,
			List<ActivityImpl> ancestors) {
		if (ancestors.size() > 0) {
			for (ActivityImpl activity : ancestors) {
				flattenActivities(container, activity.getActivities());
			}
			container.addAll(ancestors);
		}
	}

}
