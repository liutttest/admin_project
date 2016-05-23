package com.evan.finance.admin.activiti.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.evan.common.user.domain.ActSuspendLog;
import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.service.ActSuspendLogService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.utils.ConstantTool.ActSupportLogType;
import com.evan.common.utils.ConstantTool.ApplyState;
import com.evan.common.utils.MailSender;
import com.evan.finance.admin.activiti.service.WorkFlowInstanceService;
import com.evan.finance.admin.controller.WorkFlowInstanceController;
import com.evan.finance.admin.utils.ActivityUtil;
import com.evan.jaron.core.AppContext;
import com.google.common.collect.Maps;

@Service
public class WorkFlowInstanceServiceImpl implements WorkFlowInstanceService{
	
	Logger logger = LoggerFactory.getLogger(WorkFlowInstanceController.class);

	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	
	@Autowired
    private ActSuspendLogService actSuspendLogService;
	
	private static final String actSuspendState = AppContext.getInstance().getString("actSuspendState");
	private static final String actSuspendMail = AppContext.getInstance().getString("actSuspendMail");
	
	
	/**
     * 激活流程实例
     * @param procInsId 流程实例ID
     */
	public int activateProcessInstance(String procInsId){
		try{
			ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult();
			if(pi.isSuspended()){
				runtimeService.activateProcessInstanceById(procInsId);
				return 1;
			} else {
				return 2;
			}
		} catch(Exception e){
			logger.error("激活流程实例异常：", e);
			return -1;
		}
	}
	
	/**
     * 挂起流程实例
     * @param procInsId 流程实例ID
     */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int suspendProcessInstance(String procInsId){
		try{
			ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult();
			//判断是否已经挂起
			if(pi.isSuspended()){
				return 2;
			} else {
				logger.error("流程实例ID为:"+procInsId+"的流程被挂起.");
				runtimeService.suspendProcessInstanceById(procInsId);
				String activitiName = getActivitiName(pi.getActivityId(), pi.getProcessDefinitionId());
				FwBusinessSxd fb = fwBusinessSxdService.getFwBusinessSxdByPInsId(procInsId);
				//插入挂起记录
				this.saveActSuspendLog(procInsId, ActSupportLogType.ACTIVITI_EXCEPTION, fb==null?"":fb.getComName(), activitiName);
				return 1;
			}
		} catch(Exception e){
			e.printStackTrace();
			logger.error("挂起流程实例异常：", e);
			return -1;
		}
	}
	
	/**
     * 启动流程
     * @param busId 业务ID
     * @param userId 用户ID
     * @param vars 流程变量
     */
	public Map<String, Object> startProcess(String busId, String userId, Map<String, Object> vars) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			// 用来设置启动流程的人员ID
			identityService.setAuthenticatedUserId(userId);
			// 启动流程
			ProcessInstance procIns = runtimeService.startProcessInstanceByKey(ActivityUtil.PROCDEFKEY, busId, vars);
			map.put("state", 1);
			map.put("procInsId", procIns.getId());
		} catch(Exception e){
			map.put("state", 0);
			logger.error("启动工作流异常：", e);
		}
		return map;
	}
	
	/**
     * 完成到系统准入验证节点
     * @param procInsId 流程实例ID
     * @param busId 业务ID
     */
	@Transactional
	public int completeToAccess(String procInsId, String busId, String comName){
		Task task = taskService.createTaskQuery().processInstanceId(procInsId).singleResult();
		if(task != null){
			taskService.setAssignee(task.getId(), comName);
			Map<String, Object> vars = new HashMap<String, Object>();
			taskService.complete(task.getId(), vars);
			FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId);
			if (!"09".equals(fwBusinessSxd.getApplyState())) {
				//修改业务状态
				fwBusinessSxdService.updateState(busId, ApplyState.ADMITTANCE_APPORVALING, null);
			}
			return 1;
		}
		return 0;
	}
	
	/**
     * 完成到系统自动打分节点
     * @param procInsId 流程实例ID
     * @param busId 业务ID
     */
	@Transactional
	public int completeToMark(String procInsId, String busId, String comName){
		Task task = taskService.createTaskQuery().processInstanceId(procInsId).singleResult();
		if(task != null){
			taskService.setAssignee(task.getId(), comName);
			Map<String, Object> vars = Maps.newHashMap();
			taskService.complete(task.getId(), vars);
			//修改业务状态
			fwBusinessSxdService.updateState(busId, ApplyState.LIMIT_APPORVALING, null);
			return 1;
		}
		return 0;
	}
	
	/**
     * 保存挂起记录
     * @param procInsId 流程实例ID
     * @param type 类型1银行接口任务失败挂起
     * @param describe 描述
     */
    private void saveActSuspendLog(String procInsId, int type, String comName, String activitiName){
    	try{
    		ActSuspendLog log = new ActSuspendLog();
    		log.setProcInstId(procInsId);
        	log.setType(type);
        	log.setState(1);
        	log.setCreateDate(new Date());
        	actSuspendLogService.save(log);
        	//判断挂起后是否发生邮件 1发送
        	if("1".equals(actSuspendState)){
        		if(StringUtils.isNotEmpty(actSuspendMail)){
        			String[] email = actSuspendMail.split(",");
        			for(String mail : email){
        				MailSender.sendMail(mail, "流程挂起通知", "企业：\""+comName+"\"，在节点：\""+activitiName+"\"，因为办理异常被挂起。流程实例ID为：\""+procInsId+"\".");
        			}
        		}
        	}
    	} catch(Exception e){
    		logger.error("保存挂起记录异常：", e);
    	}
    }
    
    /**
     * 查询当前节点名称
     * @param activityId activityId
     * @param ProcessDefinitionId ProcessDefinitionId
     * @return String 当前节点名称
     */
    private String getActivitiName(String activityId, String ProcessDefinitionId){
    	if(StringUtils.isNotBlank(activityId) && StringUtils.isNotBlank(ProcessDefinitionId)){
    		try{
    			ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(ProcessDefinitionId);
        		List<ActivityImpl> activitiList = def.getActivities();
        		for(ActivityImpl activityImpl:activitiList){
        			String id = activityImpl.getId();
        			if(activityId.equals(id)){
        				return activityImpl.getProperty("name").toString();
        			}
        		}
    		} catch(Exception e){
    			logger.error("获取流程当前节点出错", e);
    			return null;
    		}
    	}
    	return null;
    }

}
