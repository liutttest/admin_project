package com.evan.finance.admin.activi.nodes;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evan.common.user.domain.FwBlacklist;
import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.domain.FwComPerBus;
import com.evan.common.user.domain.SysDicIndustry;
import com.evan.common.user.monitor.MonitorTools;
import com.evan.common.user.service.FwBlacklistService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwComPerBusService;
import com.evan.common.user.service.SysDicIndustryService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.ConstantTool.BusinessType;
import com.evan.common.utils.ConstantTool.FailMsgType;
import com.evan.common.utils.ConstantTool.PersonType;
import com.evan.common.utils.DateUtils;
import com.evan.finance.admin.utils.WorkFlowUtils;

@Service
public class WorkFlowAccessSysService implements JavaDelegate {
	
	final Logger logger = LoggerFactory.getLogger(WorkFlowAccessSysService.class);

	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	
	@Autowired
	private MonitorTools monitorTools;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		String busId = execution.getProcessBusinessKey();
		
		boolean verifyStatus = fwBusinessSxdService.sysVerify(busId);
		
		   //系统准入验证通过
		if (verifyStatus) {
			monitorTools.infoBusinessQuota("系统准入验证", busId, "系统准入验证通过");
			execution.setVariable(WorkFlowUtils.sysVerify, true);
		}else{
		   //系统准入验证不通过
			monitorTools.infoBusinessQuota("系统准入验证", busId, "系统准入验证不通过");
			execution.setVariable(WorkFlowUtils.sysVerify, false);
			//如果系统准入验证失败，在客服通知业务失败环节可以知道，是在哪个环节失败的
			execution.setVariable("taskDefKey",FailMsgType.ACCESS);
		}
		
	}
	
}
