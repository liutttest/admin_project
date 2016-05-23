package com.evan.finance.admin.activi.nodes;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evan.common.user.monitor.MonitorTools;
import com.evan.common.user.service.FwBusinessSxdService;

@Service
public class WorkFlowQuotaSysService implements JavaDelegate{
	final Logger logger = LoggerFactory.getLogger(WorkFlowQuotaSysService.class);

	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	
	@Autowired
	private MonitorTools monitorTools;
	
	@Override
	@Transactional
	public void execute(DelegateExecution execution) throws Exception {
		//系统自动打分环节 
		String busId = execution.getProcessBusinessKey();
		//进行系统自动打分
		String scoreResult =fwBusinessSxdService.sysScoring(execution.getProcessBusinessKey());
		//将打分结果存到数据库 加日志
		monitorTools.infoBusinessQuota("额度申请系统打分成功", busId, "打分结果"+scoreResult);
	}

}
