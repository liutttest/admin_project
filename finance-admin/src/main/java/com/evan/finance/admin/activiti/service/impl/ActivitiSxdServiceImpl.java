package com.evan.finance.admin.activiti.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evan.common.user.domain.FaAdmin;
import com.evan.common.user.domain.FaCenterDataBank;
import com.evan.common.user.domain.FaFunc;
import com.evan.common.user.domain.FaRisk;
import com.evan.common.user.domain.FaTempScene;
import com.evan.common.user.domain.FaTemplate;
import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.domain.FwComPerBus;
import com.evan.common.user.domain.FwLoanApply;
import com.evan.common.user.domain.SysTimerTask;
import com.evan.common.user.monitor.MonitorTools;
import com.evan.common.user.service.FaAdminService;
import com.evan.common.user.service.FaCenterDataBankService;
import com.evan.common.user.service.FaCommunicateLogService;
import com.evan.common.user.service.FaFalseUsersService;
import com.evan.common.user.service.FaScoreRecordService;
import com.evan.common.user.service.FaTempSceneService;
import com.evan.common.user.service.FaTemplateService;
import com.evan.common.user.service.FwBankRelationFilesService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwComPerBusService;
import com.evan.common.user.service.FwCompanyService;
import com.evan.common.user.service.FwLoanApplyService;
import com.evan.common.user.service.FwQuickLoanService;
import com.evan.common.user.service.FwTransactionDetailService;
import com.evan.common.user.service.SysTimerTaskService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.ConstantTool.ApplyState;
import com.evan.common.utils.ConstantTool.BankFileSceneKey;
import com.evan.common.utils.ConstantTool.BankFileType;
import com.evan.common.utils.ConstantTool.BusinessType;
import com.evan.common.utils.ConstantTool.CommlogType;
import com.evan.common.utils.ConstantTool.FailMsgType;
import com.evan.common.utils.ConstantTool.FileType;
import com.evan.common.utils.ConstantTool.MaritalState;
import com.evan.common.utils.ConstantTool.SCENE;
import com.evan.common.utils.ConstantTool.SysTaskState;
import com.evan.common.utils.ConstantTool.TransactionSource;
import com.evan.common.utils.ConstantTool.TransactionType;
import com.evan.common.utils.DateUtils;
import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.activiti.bean.WorkFlow;
import com.evan.finance.admin.activiti.service.ActivitiSxdService;
import com.evan.finance.admin.activiti.service.WorkFlowLoanService;
import com.evan.finance.admin.bank.BankTools;
import com.evan.finance.admin.notification.service.NotificationService;
import com.evan.finance.admin.pdf.PDFHandleTools;
import com.evan.finance.admin.utils.ActivityUtil;
import com.evan.finance.admin.utils.WorkFlowUtils;
import com.evan.jaron.plugins.dictionary.DictionaryHelper;
import com.evan.jaron.plugins.security.domain.IPermission;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;
import com.evan.nd.common_file.Constant;
import com.evan.nd.common_file.domain.FwFiles;
import com.evan.nd.common_file.service.FwFilesService;
import com.itextpdf.text.DocumentException;

@Service
public class ActivitiSxdServiceImpl implements ActivitiSxdService {
	
	final Logger logger = LoggerFactory.getLogger(ActivitiSxdServiceImpl.class);
	
	@Autowired
	private ProcessEngine processEngine;
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
    protected TaskService taskService;

	@Autowired
    protected HistoryService historyService;

	@Autowired
    protected RepositoryService repositoryService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private FwFilesService filesService;
	@Autowired
	private DictionaryHelper dictionaryHelper;
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	@Autowired
	private MonitorTools monitorTools;
	@Autowired
	private FwComPerBusService fwComPerBusService;
	
	@Autowired
	private FwBankRelationFilesService fwBankRelationFilesService;
	
	@Autowired
	private FaCenterDataBankService faCenterDataBankService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private FaCommunicateLogService faCommunicateLogService;
	
	@Autowired
	private FaTempSceneService faTempSceneService;
	
	@Autowired
	private FaTemplateService faTemplateService;
	
	@Autowired
	private SysTimerTaskService sysTimerTaskService;
	
	@Autowired
	private FaScoreRecordService faScoreRecordService;
	
	@Autowired
	private FaFalseUsersService faFalseUsersService;
	
	@Autowired
	private FwCompanyService fwCompanyService;
	
	@Autowired
	private FwQuickLoanService fwQuickLoanService;
	
	@Autowired
	private FaAdminService faAdminService;
	
	@Autowired
	private BankTools bankTools;
	
	@Autowired
	private FwLoanApplyService fwLoanApplyService;
	
	@Autowired
	private FwTransactionDetailService fwTransactionDetailService;
	
	@Autowired
	private WorkFlowLoanService workFlowLoanService;
	
	
	/**
	 * 启动流程实例
	 */
	@Override
	public WorkFlow startProcess(String businessKey) {
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ActivityUtil.PROCDEFKEY, businessKey);
		if (processInstance == null) {
			return null;
		}
		
		WorkFlow workFlow = new WorkFlow();
		workFlow.setBusinessId(businessKey);
		workFlow.setProcInsId(processInstance.getProcessInstanceId());
        return workFlow;
	}

	/**
	 * 签收任务
	 * @param userId
	 */
	@Override
	public void claim(String taskId, String userId) {
	    taskService.claim(taskId, userId);
	}

	/**
	 * 完成任务
	 * 
	 * @param taskId 任务id
	 * @param variables
	 */
	@Override
	public void complete(String taskId, Map<String, Object> variables) {
		taskService.complete(taskId, variables);
	}
	
	/**
	 * 完成任务
	 * 
	 * @param taskId 任务id
	 */
	@Override
	public void complete(String taskId) {
		taskService.complete(taskId);
	}
	

	/**
	 * 查询待办任务(税信贷 业务相关)
	 * @param userId
	 * @param candidateGroups
	 * @param falseUser
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findTodoTasks(String userId, List<String> candidateGroups,List<String> falseUser,int pageSize, int currentPage,String searchString,String suspensionState) {
		StringBuffer sql = new StringBuffer();
		 sql.append("select RES.* ,SXD.* FROM ACT_RU_TASK RES LEFT JOIN ACT_RU_IDENTITYLINK I  ON I.TASK_ID_ = RES.ID_ LEFT JOIN FW_BUSINESS_SXD SXD ON RES.PROC_INST_ID_ = SXD.PROCESS_INSTANCE_ID where ");
		 sql.append("(RES.PROC_DEF_ID_ LIKE '"+ActivityUtil.PROCDEFKEY+":%' AND RES.ASSIGNEE_ = '");
	     sql.append(userId).append("'");
	     //搜索条件
	     if (!StringUtils.isEmpty(searchString)) {
			sql.append(" AND (SXD.COM_NAME like '%").append(searchString).append("%' ");
			sql.append(" or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '%").append(searchString).append("%') ");
		 }
	     if (!"0".equals(suspensionState)) {
			sql.append(" AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(")");
		 }
	     sql.append(" OR ( RES.PROC_DEF_ID_ LIKE '"+ActivityUtil.PROCDEFKEY+":%' AND RES.ASSIGNEE_ IS NULL AND ");
	     sql.append("( I.USER_ID_ = '");
	     sql.append(userId);
	     sql.append("' )))");
	     //角色权限
		if (candidateGroups!=null) {
			sql.append(" OR (RES.ASSIGNEE_ IS NULL AND I.TYPE_ = 'candidate' AND RES.PROC_DEF_ID_ LIKE '"+ActivityUtil.PROCDEFKEY+":%' AND (");
			if (!StringUtils.isEmpty(searchString)) {
				sql.append(" (SXD.COM_NAME like '%").append(searchString).append("%' ");
				sql.append(" or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '").append(searchString).append("') AND");
			}
			if (!"0".equals(suspensionState)) {
				sql.append(" (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(") AND ");
			 }
			sql.append(" I.GROUP_ID_ IN (");
			String groups = "";
			for (String group : candidateGroups) {
				
				groups+="'"+group+"',";
			}
			groups = groups.substring(0, groups.length()-1);
     		sql.append(groups);
			sql.append(")))");
			
		}
        
        //循环查询任务分派 假用户的任务
		if (falseUser!=null && falseUser.size()>0) {
			String userNames = "";
	        for (String userName : falseUser) {
	        	userNames += "'"+userName+"',";
			}
	        userNames = userNames.substring(0, userNames.length()-1);
	        sql.append(" or RES.PROC_DEF_ID_ LIKE '"+ActivityUtil.PROCDEFKEY+":%' ");
	        if (!StringUtils.isEmpty(searchString)) {
				sql.append(" AND (SXD.COM_NAME like '%").append(searchString).append("%' ");
				sql.append(" or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '").append(searchString).append("') ");
			}
	        
	        if (!"0".equals(suspensionState)) {
				sql.append(" AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(") ");
			 }
	        
	        sql.append(" AND RES.ASSIGNEE_ in(");
	        sql.append(userNames);
	        sql.append(")");
		}
		
		sql.append(" order by RES.CREATE_TIME_ desc limit ");
		sql.append(currentPage);
		sql.append(",");
		sql.append(pageSize);
		
		//查询待办任务
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql.toString());
        
		return list;
	}
	
	/**
	 * 查询待办任务数据总条数(税信贷)
	 * @param userId
	 * @param candidateGroups
	 * @param falseUser
	 * @param pageSize
	 * @param currentPage
	 * @param searchString
	 * @return
	 */
	@Override
	public long findTodoTasksCount(String userId, List<String> candidateGroups,List<String> falseUser,int pageSize, int currentPage,String searchString ,String suspensionState) {
		StringBuffer sql = new StringBuffer();
		 sql.append("select RES.* ,SXD.* FROM ACT_RU_TASK RES LEFT JOIN ACT_RU_IDENTITYLINK I  ON I.TASK_ID_ = RES.ID_ LEFT JOIN FW_BUSINESS_SXD SXD ON RES.PROC_INST_ID_ = SXD.PROCESS_INSTANCE_ID where ");
		 sql.append("(RES.PROC_DEF_ID_ LIKE '"+ActivityUtil.PROCDEFKEY+":%' AND RES.ASSIGNEE_ = '");
	     sql.append(userId).append("'");
	     //搜索条件
	     if (!StringUtils.isEmpty(searchString)) {
				sql.append(" AND (SXD.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '%").append(searchString).append("%') ");
			}
	     if (!"0".equals(suspensionState)) {
			sql.append("AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(")");
		 }
	     sql.append(" OR ( RES.PROC_DEF_ID_ LIKE '"+ActivityUtil.PROCDEFKEY+":%' AND RES.ASSIGNEE_ IS NULL AND ");
	     sql.append("( I.USER_ID_ = '");
	     sql.append(userId);
	     sql.append("' )))");
	     
	     //角色权限
		if (candidateGroups!=null) {
			sql.append(" OR (RES.ASSIGNEE_ IS NULL AND I.TYPE_ = 'candidate' AND RES.PROC_DEF_ID_ LIKE '"+ActivityUtil.PROCDEFKEY+":%' AND (");
			if (!StringUtils.isEmpty(searchString)) {
				sql.append(" (SXD.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '").append(searchString).append("') AND");
			}
			if (!"0".equals(suspensionState)) {
				sql.append(" (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(") AND ");
			 }
			sql.append(" I.GROUP_ID_ IN (");
			String groups = "";
			for (String group : candidateGroups) {
				
				groups+="'"+group+"',";
			}
			groups = groups.substring(0, groups.length()-1);
    		sql.append(groups);
			sql.append(")))");
			
		}
       
       //循环查询任务分派 假用户的任务
		if (falseUser!=null && falseUser.size()>0) {
			String userNames = "";
	        for (String userName : falseUser) {
	        	userNames += "'"+userName+"',";
			}
	        userNames = userNames.substring(0, userNames.length()-1);
	        sql.append(" or RES.PROC_DEF_ID_ LIKE '"+ActivityUtil.PROCDEFKEY+":%' ");
	        if (!StringUtils.isEmpty(searchString)) {
				sql.append(" AND (SXD.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '").append(searchString).append("') ");
			}
	        
	        if (!"0".equals(suspensionState)) {
				sql.append(" AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(") ");
			 }
	        
	        sql.append(" AND RES.ASSIGNEE_ in(");
	        sql.append(userNames);
	        sql.append(")");
		}
		
		//查询待办任务
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql.toString());
        
		return list.size();
	}
	
	
	/**
	 * 查询待办任务(快速贷款 业务相关)
	 * @param userId
	 * @param candidateGroups
	 * @param falseUser
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findTodoTasksQuickloan(String userId, List<String> candidateGroups,List<String> falseUser,int pageSize, int currentPage,String searchString,String suspensionState) {
		StringBuffer sql = new StringBuffer();
		 sql.append("select RES.* ,LOAN.* FROM ACT_RU_TASK RES LEFT JOIN ACT_RU_IDENTITYLINK I  ON I.TASK_ID_ = RES.ID_ LEFT JOIN ");
		 sql.append(" FW_QUICK_LOAN LOAN ON RES.PROC_INST_ID_ = LOAN.PROCESS_INSTANCE_ID where ");
		 sql.append("(RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_quickloan+":%' AND RES.ASSIGNEE_ = '");
	     sql.append(userId).append("'");
	     //搜索条件
	     if (!StringUtils.isEmpty(searchString)) {
				sql.append(" AND (LOAN.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '%").append(searchString).append("%') ");
			}
	     if (!"0".equals(suspensionState)) {
				sql.append("AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(")");
		 }
	     sql.append(" OR ( RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_quickloan+":%' AND RES.ASSIGNEE_ IS NULL AND ");
	     sql.append("( I.USER_ID_ = '");
	     sql.append(userId);
	     sql.append("' )))");
	     
	     //角色权限
		if (candidateGroups!=null) {
			sql.append(" OR (RES.ASSIGNEE_ IS NULL AND I.TYPE_ = 'candidate' AND RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_quickloan+":%' AND (");
			if (!StringUtils.isEmpty(searchString)) {
				sql.append(" (LOAN.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '").append(searchString).append("') AND");
			}
			if (!"0".equals(suspensionState)) {
				sql.append(" (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(") AND ");
			 }
			sql.append(" I.GROUP_ID_ IN (");
			String groups = "";
			for (String group : candidateGroups) {
				
				groups+="'"+group+"',";
			}
			groups = groups.substring(0, groups.length()-1);
   		sql.append(groups);
			sql.append(")))");
			
		}
      
      //循环查询任务分派 假用户的任务
		if (falseUser!=null && falseUser.size()>0) {
			String userNames = "";
	        for (String userName : falseUser) {
	        	userNames += "'"+userName+"',";
			}
	        userNames = userNames.substring(0, userNames.length()-1);
	        sql.append(" or RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_quickloan+":%' ");
	        if (!StringUtils.isEmpty(searchString)) {
				sql.append(" AND (LOAN.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '").append(searchString).append("') ");
			}
	        if (!"0".equals(suspensionState)) {
				sql.append(" AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(") ");
			 }
	        sql.append(" AND RES.ASSIGNEE_ in(");
	        sql.append(userNames);
	        sql.append(")");
		}
		
		sql.append(" order by RES.CREATE_TIME_ desc limit ");
		sql.append(currentPage);
		sql.append(",");
		sql.append(pageSize);
		
		//查询待办任务
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql.toString());
        
		return list;
	}
	
	/**
	 * 查询待办任务数据总条数(快速贷款)
	 * @param userId
	 * @param candidateGroups
	 * @param falseUser
	 * @param pageSize
	 * @param currentPage
	 * @param searchString
	 * @return
	 */
	@Override
	public long findTodoTasksCountQuickloan(String userId, List<String> candidateGroups,List<String> falseUser,String searchString,String suspensionState) {
		StringBuffer sql = new StringBuffer();
		 sql.append("select RES.* ,LOAN.* FROM ACT_RU_TASK RES LEFT JOIN ACT_RU_IDENTITYLINK I  ON I.TASK_ID_ = RES.ID_ LEFT JOIN ");
		 sql.append(" FW_QUICK_LOAN LOAN ON RES.PROC_INST_ID_ = LOAN.PROCESS_INSTANCE_ID where ");
		 sql.append("(RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_quickloan+":%' AND RES.ASSIGNEE_ = '");
	     sql.append(userId).append("'");
	     //搜索条件
	     if (!StringUtils.isEmpty(searchString)) {
				sql.append(" AND (LOAN.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '%").append(searchString).append("%') ");
			}
	     if (!"0".equals(suspensionState)) {
				sql.append("AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(")");
			 }
	     sql.append(" OR ( RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_quickloan+":%' AND RES.ASSIGNEE_ IS NULL AND ");
	     sql.append("( I.USER_ID_ = '");
	     sql.append(userId);
	     sql.append("' )))");
	     
	     //角色权限
		if (candidateGroups!=null) {
			sql.append(" OR (RES.ASSIGNEE_ IS NULL AND I.TYPE_ = 'candidate' AND RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_quickloan+":%' AND (");
			if (!StringUtils.isEmpty(searchString)) {
				sql.append(" (LOAN.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '").append(searchString).append("') AND");
			}
			if (!"0".equals(suspensionState)) {
				sql.append(" (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(") AND ");
			 }
			sql.append(" I.GROUP_ID_ IN (");
			String groups = "";
			for (String group : candidateGroups) {
				
				groups+="'"+group+"',";
			}
			groups = groups.substring(0, groups.length()-1);
    		sql.append(groups);
			sql.append(")))");
			
		}
       
       //循环查询任务分派 假用户的任务
		if (falseUser!=null && falseUser.size()>0) {
			String userNames = "";
	        for (String userName : falseUser) {
	        	userNames += "'"+userName+"',";
			}
	        userNames = userNames.substring(0, userNames.length()-1);
	        sql.append(" or RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_quickloan+":%' ");
	        if (!StringUtils.isEmpty(searchString)) {
				sql.append(" AND (LOAN.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '").append(searchString).append("') ");
			}
	        if (!"0".equals(suspensionState)) {
				sql.append(" AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(") ");
			 }
	        sql.append(" AND RES.ASSIGNEE_ in(");
	        sql.append(userNames);
	        sql.append(")");
		}
		
		//查询待办任务
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql.toString());
        
		return list.size();
	}
	
	
	/**
	 * 查询待办任务(放款 业务相关)
	 * @param userId
	 * @param candidateGroups
	 * @param falseUser
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findTodoTasksLoan(String userId, List<String> candidateGroups,List<String> falseUser,int pageSize, int currentPage,String searchString,String suspensionState) {
		StringBuffer sql = new StringBuffer();
		 sql.append("select RES.* ,LOAN.*,SXD.COM_NAME FROM ACT_RU_TASK RES LEFT JOIN ACT_RU_IDENTITYLINK I  ON I.TASK_ID_ = RES.ID_ LEFT JOIN FW_LOAN_APPLY LOAN ON RES.PROC_INST_ID_ = LOAN.PROCESS_INSTANCE_ID ");
		 sql.append(" LEFT JOIN FW_BUSINESS_SXD SXD ON LOAN.BUSINESS_ID = SXD.BS_ID where ");
		 sql.append("(RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_loan+":%' AND RES.ASSIGNEE_ = '");
	     sql.append(userId).append("'");
	     //搜索条件
	     if (!StringUtils.isEmpty(searchString)) {
				sql.append(" AND (SXD.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '%").append(searchString).append("%') ");
			}
	     if (!"0".equals(suspensionState)) {
				sql.append("AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(")");
			 }
	     sql.append(" OR ( RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_loan+":%' AND RES.ASSIGNEE_ IS NULL AND ");
	     sql.append("( I.USER_ID_ = '");
	     sql.append(userId);
	     sql.append("' )))");
	     
	     //角色权限
		 if (candidateGroups!=null) {
			sql.append(" OR (RES.ASSIGNEE_ IS NULL AND I.TYPE_ = 'candidate' AND RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_loan+":%' AND (");
			if (!StringUtils.isEmpty(searchString)) {
				sql.append(" (SXD.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '").append(searchString).append("') AND");
			}
			if (!"0".equals(suspensionState)) {
				sql.append(" (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(") AND ");
			 }
			sql.append(" I.GROUP_ID_ IN (");
			String groups = "";
			for (String group : candidateGroups) {
				
				groups+="'"+group+"',";
			}
			groups = groups.substring(0, groups.length()-1);
     		sql.append(groups);
			sql.append(")))");
			
		}
        
        //循环查询任务分派 假用户的任务
		if (falseUser!=null && falseUser.size()>0) {
			String userNames = "";
	        for (String userName : falseUser) {
	        	userNames += "'"+userName+"',";
			}
	        userNames = userNames.substring(0, userNames.length()-1);
	        sql.append(" or RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_loan+":%' ");
	        if (!StringUtils.isEmpty(searchString)) {
				sql.append(" AND (SXD.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '").append(searchString).append("') ");
			}
	        if (!"0".equals(suspensionState)) {
				sql.append(" AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(") ");
			 }
	        sql.append(" AND RES.ASSIGNEE_ in(");
	        sql.append(userNames);
	        sql.append(")");
		}
		
		sql.append(" order by RES.CREATE_TIME_ desc limit ");
		sql.append(currentPage);
		sql.append(",");
		sql.append(pageSize);
		
		//查询待办任务
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql.toString());
        
		return list;
	}
	
	/**
	 * 查询待办任务数据总条数(放款)
	 * @param userId
	 * @param candidateGroups
	 * @param falseUser
	 * @param pageSize
	 * @param currentPage
	 * @param searchString
	 * @return
	 */
	@Override
	public long findTodoTasksCountLoan(String userId, List<String> candidateGroups,List<String> falseUser,String searchString,String suspensionState) {
		StringBuffer sql = new StringBuffer();
		 sql.append("select RES.* ,LOAN.*,SXD.COM_NAME FROM ACT_RU_TASK RES LEFT JOIN ACT_RU_IDENTITYLINK I  ON I.TASK_ID_ = RES.ID_ LEFT JOIN FW_LOAN_APPLY LOAN ON RES.PROC_INST_ID_ = LOAN.PROCESS_INSTANCE_ID ");
		 sql.append(" LEFT JOIN FW_BUSINESS_SXD SXD ON LOAN.BUSINESS_ID = SXD.BS_ID where ");
		 sql.append("(RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_loan+":%' AND RES.ASSIGNEE_ = '");
	     sql.append(userId).append("'");
	     //搜索条件
	     if (!StringUtils.isEmpty(searchString)) {
				sql.append(" AND (SXD.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '%").append(searchString).append("%') ");
			}
	     if (!"0".equals(suspensionState)) {
				sql.append("AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(")");
		 }
	     sql.append(" OR ( RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_loan+":%' AND RES.ASSIGNEE_ IS NULL AND ");
	     sql.append("( I.USER_ID_ = '");
	     sql.append(userId);
	     sql.append("' )))");
	     
	     //角色权限
		 if (candidateGroups!=null) {
			sql.append(" OR (RES.ASSIGNEE_ IS NULL AND I.TYPE_ = 'candidate' AND RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_loan+":%' AND (");
			if (!StringUtils.isEmpty(searchString)) {
				sql.append(" (SXD.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '").append(searchString).append("') AND");
			}
			if (!"0".equals(suspensionState)) {
				sql.append(" (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(") AND ");
			 }
			sql.append(" I.GROUP_ID_ IN (");
			String groups = "";
			for (String group : candidateGroups) {
				
				groups+="'"+group+"',";
			}
			groups = groups.substring(0, groups.length()-1);
    		sql.append(groups);
			sql.append(")))");
			
		}
       
       //循环查询任务分派 假用户的任务
		if (falseUser!=null && falseUser.size()>0) {
			String userNames = "";
	        for (String userName : falseUser) {
	        	userNames += "'"+userName+"',";
			}
	        userNames = userNames.substring(0, userNames.length()-1);
	        sql.append(" or RES.PROC_DEF_ID_ LIKE '"+WorkFlowUtils.process_definition_key_loan+":%' ");
	        if (!StringUtils.isEmpty(searchString)) {
				sql.append(" AND (SXD.COM_NAME like '%").append(searchString).append("%' ");
				sql.append("or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '").append(searchString).append("') ");
			}
	        if (!"0".equals(suspensionState)) {
				sql.append(" AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(") ");
			 }
	        sql.append(" AND RES.ASSIGNEE_ in(");
	        sql.append(userNames);
	        sql.append(")");
		}
		
		//查询待办任务
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql.toString());
        
		return list.size();
	}
	
	
	/**
	 * @liutt
	 * 人工准入验证 做业务处理 及工作流 办理
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见  1--通过，2---不通过，3---驳回
	 * @param map 前台参数 页面选择的选项 参数
	 */
	@Override
	@Transactional
	public String doAccessVerify(FaRisk faRisk,WorkFlow workFlow,String nodeKey,String condition,Map<String, Object> map,String isSendEmail,String emailTitle,String emailContent,String isSendSms,String smsContent,String isSendMessage,String messageTitle,String messageContent) throws Exception {
		logger.info("doAccessVerify----start"+System.currentTimeMillis());
		boolean status = true;
		Map<String, Object> variables = new HashMap<String, Object>();
		//定义工作流 操作日志的备注--自动添加上 同意、不通过、驳回
		String reason = "";
		String conditionStatus = "";
		//准入不通过
		if ("2".equals(condition)) {
			// 准入失败
			conditionStatus = "2";
			taskService.setVariable(workFlow.getTaskId(), "taskDefKey", FailMsgType.ACCESS);
			reason= "【不通过】";
		//准入通过
		} else if ("1".equals(condition)) {
			// 调用系统方法进行验证  对页面上的选择进行准入验证
			status = fwBusinessSxdService.newActivitipersonScoring(faRisk);//系统进行判断是否通过
			if (!status) {
				// 准入失败
				conditionStatus = "2";
				taskService.setVariable(workFlow.getTaskId(), "taskDefKey", FailMsgType.ACCESS);
				reason= "【不通过】";
			}else{
			    //验证通过  生成银行数据中间表， 并且生成客户号
				FaCenterDataBank faCenterDataBank = faCenterDataBankService.newActivitisavefaQuotaData(workFlow.getBusinessId(), BusinessType.SXD);
				// 准入通过
				conditionStatus = "1";
				//查询流程实例id
				String processInstanceId = taskService.createTaskQuery().taskId(workFlow.getTaskId()).singleResult().getProcessInstanceId();
				//将需要上传到银行的合并文件生成pdf,以及需要上传的文件存入到数据库中
				this.accessCreateBankFile(workFlow.getBusinessId(),processInstanceId);
				// 向任务表里 插入任务数据 定时向银行发送 征信申请
				sysTimerTaskService.createSysTimerTask(processInstanceId,SysTaskState.TASKTYPE_ACCESS , workFlow.getBusinessId(), faCenterDataBank.getId());
				reason= "【同意】";
			}
		}
	   else if ("3".equals(condition)) {
			// 准入驳回为
		   conditionStatus = "3";
			// 修改数据库状态为 01--准入申请
			fwBusinessSxdService.updateState(workFlow.getBusinessId(), ApplyState.ADMITTANCE_APPLYING, RequestUtils.getLoginedUser().getUserId());
			//进行发送通知的判断
			String sendType = MapUtils.getString(map, "sendType");
			//手动发送通知
			if ("0".equals(sendType)) {
				logger.info("doAccessVerify----sendMsg");
//				this.sendMsg(map, workFlow.getBusinessId(), "准入驳回通知",true);
				this.sendMsg(workFlow.getBusinessId(), "准入驳回通知",true, isSendEmail, emailTitle, emailContent, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
			}else{
				logger.info("doAccessVerify----autoSendMsg");
			//自动发送通知
				this.autoSendMsg(SCENE.ZRBH, "准入驳回通知", workFlow.getBusinessId(),true);
			}
			reason= "【驳回】";
		}

		
		taskService.addComment(workFlow.getTaskId(), workFlow.getProcInsId(),  workFlow.getComment()+reason);
		variables.put(nodeKey, conditionStatus);
		this.complete(workFlow.getTaskId(), variables);
		/*if (!StringUtils.isEmpty(workFlow.getComment())) {
			//准入验证备注
			faCommunicateLogService.saveFaCommunicateLog(workFlow.getBusinessId(), BusinessType.SXD,CommlogType.ACCESS_REMARK, workFlow.getComment());
		}*/
		//对人工准入验证进行日志监控
		monitorTools.infoBusinessAccess("人工验证", workFlow.getBusinessId(),JsonUtils.toJson(map));
		logger.info("doAccessVerify----end"+System.currentTimeMillis());
		return conditionStatus;
	}

	/**
	 * @liutt
	 * 准入验证通过发送通知
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 * @param map 前台参数
	 */
	@Override
	@Transactional
	public void accessSendMsg(Map<String, Object> map,WorkFlow workFlow,Long userId,String isSendEmail,String emailTitle,String emailContent,String isSendSms,String smsContent,String isSendMessage,String messageTitle,String messageContent) throws Exception{
		
		//业务id
		String businessId = workFlow.getBusinessId();
		//发送客服通知
		this.sendMsg(businessId, "准入验证成功通知",false, isSendEmail, emailTitle, emailContent, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
		//将数据库状态修改为 额度申请
		fwBusinessSxdService.updateState(businessId, ApplyState.LIMIT_APPLYING, userId);
		
		//添加操作记录的处理意见
		taskService.addComment(workFlow.getTaskId(), workFlow.getProcInsId(),  workFlow.getComment()+"【同意】");
		//办理流程
		this.complete(workFlow.getTaskId());
		/*if (!StringUtils.isEmpty(workFlow.getComment())) {
			//准入验证备注
			faCommunicateLogService.saveFaCommunicateLog(workFlow.getBusinessId(), BusinessType.SXD,CommlogType.ACCESS_RESULT, workFlow.getComment());
		}*/
	}

	/**
	 * 发送通知(手动)
	 * @param businessId 业务Id
	 * @param title 记录发送日志的标题
	 * @param isAppendInfoNopass 是否发送不合格清单
	 * @param isSendEmail 是否发送邮件（on:发送；）
	 * @param emailTitle 邮件模板标题
	 * @param emailContent 邮件内容
	 * @param isSendSms 是否发送短信（on:发送；）
	 * @param smsContent 短信内容
	 * @param isSendMessage 是否发送站内信（on:发送；）
	 * @param messageTitle 站内信标题
	 * @param messageContent 站内信内容
	 * @return
	 * @throws Exception
	 */
	@Transactional
	private Map<String, Object> sendMsg(String businessId,String title,boolean isAppendInfoNopass,String isSendEmail,String emailTitle,String emailContent,String isSendSms,String smsContent,String isSendMessage,String messageTitle,String messageContent) throws Exception{
		Map<String, Object> retMap = null;
		//发送客服通知
		retMap = notificationService.doSendNotification(businessId, isSendEmail, emailTitle, emailContent, isAppendInfoNopass , isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
		//监控 发送消息 将其存入数据库
		String  contentString = notificationService.monitorContentCreate(isSendEmail, isSendSms, isSendMessage, retMap);
		monitorTools.infoBusinessAccess(title, businessId, contentString);
		return retMap;
	}

	
	/**
	 * @liutt
	 * 发送通知(自动)
	 * @param scnceKey 场景
	 * @param businessId 业务id
	 * @param isAppendInfoNopass 是否发送不合格清单
	 * @param title 标题
	 */
	@Override
	@Transactional
	public void autoSendMsg(String scnceKey, String title,String businessId,boolean isAppendInfoNopass)throws Exception {
		FaTempScene faTempScene = faTempSceneService.getTempBySecneKey(scnceKey);
		String isSendEmail = "";
		String emailTitle = "";
		String emailContent = "";
		
		// 判断是否发送邮件
		if ("1".equals(faTempScene.getIsSendEmail()))
		{
			// 查询邮件模版
			FaTemplate emailTemplate = faTemplateService.findDefaultBySceneId(faTempScene.getTsId(), ConstantTool.TempleteType.EMAIL);
			if (emailTemplate != null)
			{
				isSendEmail = "on";
				emailTitle = emailTemplate.getTitle();
				emailContent = emailTemplate.getContent();
			}
		}
		
		String isSendSms = "";
		String smsContent = "";
		
		// 判断是否发送短信
		if ("1".equals(faTempScene.getIsSendSms()))
		{
			// 查询短信模版
			FaTemplate smsTemplate = faTemplateService.findDefaultBySceneId(faTempScene.getTsId(), ConstantTool.TempleteType.SMS);
			if (smsTemplate != null)
			{
				isSendSms = "on";
				smsContent = smsTemplate.getContent();
			}
		}
		
		String isSendMessage = "";
		String messageTitle = "";
		String messageContent = "";
		
		// 判断是否发送短信
		if ("1".equals(faTempScene.getIsSendMessage()))
		{
			// 查询短信模版
			FaTemplate messageTemplate = faTemplateService.findDefaultBySceneId(faTempScene.getTsId(), ConstantTool.TempleteType.MESSAGE);
			if (messageTemplate != null)
			{
				isSendMessage = "on";
				messageTitle = messageTemplate.getTitle();
				messageContent = messageTemplate.getContent();
			}
		}
		
		// 发送
		Map<String, Object> map = notificationService.doSendNotification(businessId, isSendEmail, emailTitle, emailContent, isAppendInfoNopass, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
		String  contentString = notificationService.monitorContentCreate(isSendEmail, isSendSms, isSendMessage, map);
		monitorTools.infoBusinessAccess(title, businessId, contentString);
		
	}
	
	/**
	 * @liutt
	 *  额度申请人工复核打分环节办理
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 * @param map 前台参数
	 */
	public  void doQuotaReview(WorkFlow workFlow,String nodeKey,String condition,Map<String, Object> map,String isSendEmail,String emailTitle,String emailContent,String isSendSms,String smsContent,String isSendMessage,String messageTitle,String messageContent) throws Exception{
		boolean status = true;
		Map<String, Object> variables = new HashMap<String, Object>();
		String reason = "";
		//定义处理结果
		String conditionStatus = "";
		//人工复核通过
		if (!"3".equals(condition)) {
			/*//人工打分  mapResult 中含有打分结果xml 有分数
			Map<String, Object> mapResult= fwBusinessSxdService.personQuotaScore(map);
			//将打分结果存储到打分记录表当中
			String xml = MapUtils.getString(mapResult, "xml");
			Integer total = MapUtils.getInteger(mapResult, "total");
			faScoreRecordService.saveFaScoreRecordMamual(workFlow.getBusinessId(), BusinessType.SXD, xml, total);*/
			//进行判断 是 进行电话授信额度确认  还是直接进行银行额度申请 -----得到根据上年纳税总额度 和 申请额度进行判断 
			//如果申请额度大于上年纳税总额的2倍 怎进行人工授信额度，小于等于则进行审批
			status = fwBusinessSxdService.choooseNext(workFlow.getBusinessId());
			
			if(status){
				//通过  --进行审批
				conditionStatus = "1";
				reason= "【同意】";
			}else {
			    //未通过  进行授信额度 确认
				conditionStatus = "2";
				reason= "【同意】";
			}
			
		}else if("3".equals(condition)){
			//额度申请驳回
			// 修改数据库状态为 03--补充资料
			fwBusinessSxdService.updateState(workFlow.getBusinessId(), ApplyState.LIMIT_APPLYING, RequestUtils.getLoginedUser().getUserId());
			//进行发送通知的判断
			String sendType = MapUtils.getString(map, "sendType");
			//手动发送通知
			if ("0".equals(sendType)) {
				this.sendMsg(workFlow.getBusinessId(), "额度申请驳回通知",true, isSendEmail, emailTitle, emailContent, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
			}else{
			    //自动发送通知
				this.autoSendMsg(SCENE.EDSQBO, "额度申请驳回通知", workFlow.getBusinessId(),true);
			}
			conditionStatus="3";
			reason= "【驳回】";
		}
		
		variables.put(nodeKey, conditionStatus);
		
		taskService.addComment(workFlow.getTaskId(), workFlow.getProcInsId(),  workFlow.getComment()+reason);
		
		this.complete(workFlow.getTaskId(), variables);
		/*if (!StringUtils.isEmpty(workFlow.getComment())) {
			//额度申请备注
			faCommunicateLogService.saveFaCommunicateLog(workFlow.getBusinessId(), BusinessType.SXD,CommlogType.QUOTA_REMARK, workFlow.getComment());
		}*/
	}
	
	/**
	 * @liutt
	 * 人工核准授信额度节点办理
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 * @param quota 授信额度
	 */
	@Override
	@Transactional
	public void mranualCreditLimit(WorkFlow workFlow,String nodeKey,String condition,String quota) throws Exception {
		String reason = "";
		//人工核准授信额度环节
		if ("true".equals(condition)) {
			//核准通过的话
			//String quota = MapUtils.getString(map, "loanNew_creditLimit");
			// 将授信额度存到数据库
			fwBusinessSxdService.updateIntentMoney(workFlow.getBusinessId(), new BigDecimal(quota));	
			reason= "【同意】";
		}else{
			taskService.setVariable(workFlow.getTaskId(), "taskDefKey", FailMsgType.QUOTA);
			reason= "【不通过】";
		}
		
		//加操作日志
		taskService.addComment(workFlow.getTaskId(), workFlow.getProcInsId(),  workFlow.getComment()+reason);

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(nodeKey, "true".equals(condition) ? true : false);
		//处理工作流
		this.complete(workFlow.getTaskId(), variables);
		
		/*if (!StringUtils.isEmpty(workFlow.getComment())) {
			//额度申请备注
			faCommunicateLogService.saveFaCommunicateLog(workFlow.getBusinessId(), BusinessType.SXD,CommlogType.QUOTA_REMARK, workFlow.getComment());
		}*/
		
	}
	
	/**
	 * @liutt
	 * 额度申请--审批节点办理
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 * @param map 前台参数
	 */
	@Transactional
	public void xaminationApproval(WorkFlow workFlow,String nodeKey,String condition) throws Exception {
		String reason = "";
		//通过
		if ("1".equals(condition)) {
			//查询流程实例id
			String processInstanceId = taskService.createTaskQuery().taskId(workFlow.getTaskId()).singleResult().getProcessInstanceId();
			//将需要上传至银行的文件，
			this.quotaCreateBankFile(workFlow.getBusinessId(), processInstanceId);
			SysTimerTask sysTimerTask = sysTimerTaskService.getSysTimerTaskByBusId(processInstanceId);
			//修改任务表
			sysTimerTaskService.updateStateAndType(1, ConstantTool.SysTaskState.TASKTYPE_MARK, sysTimerTask.getId());
			reason= "【同意】";
		}else if("2".equals(condition)){
		//不通过
		taskService.setVariable(workFlow.getTaskId(), "taskDefKey", FailMsgType.QUOTA);
		reason= "【不通过】";
		}else if ("3".equals(condition)) {
			//驳回
			reason= "【驳回】";
		}
		
		Map<String, Object> variables = new HashMap<String, Object>();
		//加操作日志
		taskService.addComment(workFlow.getTaskId(), workFlow.getProcInsId(),  workFlow.getComment()+reason);
		variables.put(nodeKey, condition);
		this.complete(workFlow.getTaskId(), variables);
		/*if (!StringUtils.isEmpty(workFlow.getComment())) {
			//额度申请备注
			faCommunicateLogService.saveFaCommunicateLog(workFlow.getBusinessId(), BusinessType.SXD,CommlogType.QUOTA_REMARK, workFlow.getComment());
		}*/
		
	}
	
	/**
	 * @liutt
	 * 额度申请--经理审批节点
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 */
	@Override
	@Transactional
	public void quotaManagerApproval(WorkFlow workFlow,String nodeKey,String condition) throws Exception {
		String reason = "";
		//经理审批环节
		if ("true".equals(condition)) {
			reason= "【同意】";
		}else{
			taskService.setVariable(workFlow.getTaskId(), "taskDefKey", FailMsgType.QUOTA);
			reason= "【不通过】";
		}
		
		//加操作日志
		taskService.addComment(workFlow.getTaskId(), workFlow.getProcInsId(),  workFlow.getComment()+reason);
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(nodeKey, "true".equals(condition) ? true : false);
		//处理工作流
		this.complete(workFlow.getTaskId(), variables);
		
		/*if (!StringUtils.isEmpty(workFlow.getComment())) {
			//额度申请备注
			faCommunicateLogService.saveFaCommunicateLog(workFlow.getBusinessId(), BusinessType.SXD,CommlogType.QUOTA_REMARK, workFlow.getComment());
		}*/
		
	}
	
	
	/**
	 * @liutt
	 * 额度申请通过发送通知
	 * @param workFlow 接收前台参数的对象
	 * @param fieldTime 开户时间
	 * @param fieldAddr 开户地点
	 */
	public void quotaSendMsg(String fieldTime,String fieldAddr,WorkFlow workFlow,String isSendEmail,String emailTitle,String emailContent,String isSendSms,String smsContent,String isSendMessage,String messageTitle,String messageContent) throws Exception{
		String businessId = workFlow.getBusinessId();
		//获取银行数据中间表
		FaCenterDataBank faCenterDataBank = faCenterDataBankService.getByBusId(businessId);
		
		String time = DateUtils.dateAddyear(1);
    	//修改数据库状态并且修改客户授信金额
    	fwBusinessSxdService.applyPass2(businessId, "", 0, faCenterDataBank.getApplyMoney(), time);
		
		//将现场开户时间 以及现场开户地址存放数据库
		Map<String, Object> mapdata = new HashMap<String, Object>();
		mapdata.put("fieldTime", fieldTime);
		mapdata.put("fieldAddr", fieldAddr);
		mapdata.put("busId", businessId);
		//将和客户沟通的时间地址保存到数据库
		faCenterDataBankService.saveFaCenterDataBank(mapdata);
		
		//发送客服通知
		this.sendMsg(businessId, "额度申请成功，通知现场开户",false, isSendEmail, emailTitle, emailContent, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
		
		taskService.addComment(workFlow.getTaskId(), workFlow.getProcInsId(),  workFlow.getComment()+"【同意】");
		
		this.complete(workFlow.getTaskId());
		if (!StringUtils.isEmpty(workFlow.getComment())) {
			//额度申请备注
			faCommunicateLogService.saveFaCommunicateLog(workFlow.getBusinessId(), BusinessType.SXD,CommlogType.QUOTA_RESULT, workFlow.getComment());
		}
	}
	
	/**
	 * @liutt
	 * 现场开户--任务分配
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 * @param map 前台参数
	 */
	@Override
	@Transactional
	public void taskAssign(WorkFlow workFlow,String taskPerson) throws Exception {
		
		//获得分配的人
		String[] person = StringUtils.split(taskPerson, ",");
		//任务分配
		String taskPer = faFalseUsersService.assignTask(person);
		String userNames = "";
		//TODO　循环查询优化
		for (int i = 0; i < person.length; i++) {
			String userId = person[i];
			FaAdmin faAdmin = faAdminService.findByPk(Long.parseLong(userId));
			userNames += faAdmin.getSysUser().getUserName()+"，";
		}
		userNames = StringUtils.substring(userNames,0, userNames.length()-1);
		//添加操作记录的备注
		taskService.addComment(workFlow.getTaskId(), workFlow.getProcInsId(),  workFlow.getComment()+"  任务分配给："+userNames+"【同意】");
		Map<String, Object> variables = new HashMap<String, Object>();
		//将分配的假用户赋值到 工作流中
		variables.put("userId", taskPer);
		//处理工作流
		this.complete(workFlow.getTaskId(), variables);
		
		/*if (!StringUtils.isEmpty(workFlow.getComment())) {
			faCommunicateLogService.saveFaCommunicateLog(workFlow.getBusinessId(), BusinessType.SXD,CommlogType.FIELD_REMARK, workFlow.getComment());
		}*/
		
	}

	/**
	 * @liutt
	 * 现场开户--信息回填
	 * @param workFlow
	 * @param contractNum 合同号
	 * @param serviceChargeState 是否收取服务费
	 * @param isInvoicing 是否开具发票
	 * @param serviceChargeActual 实际收取服务费
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void dataBackfill(WorkFlow workFlow,String contractNum,String serviceChargeState,String isInvoicing,String serviceChargeActual,String userName) throws Exception {
		//查询当前处理的任务
		Task task = taskService.createTaskQuery().taskId(workFlow.getTaskId()).singleResult();
		//将数据回填到业务表中 将U盾回填到数据表中
		fwCompanyService.updateCompanyUCode(workFlow.getBusinessId(), "",contractNum,serviceChargeState,isInvoicing,serviceChargeActual);
		
		//信息回填之后 将分配任务关系解除
		faFalseUsersService.removeUsersbyuserName(task.getAssignee());
		
		//添加操作记录的处理意见
		taskService.addComment(workFlow.getTaskId(), workFlow.getProcInsId(),  workFlow.getComment()+"【同意】");
		//修改办理人
		taskService.setAssignee(workFlow.getTaskId(), userName);
		//处理工作流
		this.complete(workFlow.getTaskId());
		//查询任务数据
		SysTimerTask sysTimerTask = sysTimerTaskService.getSysTimerTaskByBusId(task.getProcessInstanceId());
		//修改任务表 类型为开户信息上传 状态为未处理
		sysTimerTaskService.updateStateAndType(1, ConstantTool.SysTaskState.TASKTYPE_OPENACCOUNT, sysTimerTask.getId());
		
		/*if (!StringUtils.isEmpty(workFlow.getComment())) {
			//现场开户备注
			faCommunicateLogService.saveFaCommunicateLog(workFlow.getBusinessId(), BusinessType.SXD,CommlogType.FIELD_REMARK, workFlow.getComment());
		}*/
	}
	/**
	 * @liutt
	 * 现场开户--归档
	 * @param workFlow 接收前台参数的对象
	 * @param archive 档案号
	 */
	@Override
	@Transactional
	public void businessArchive(WorkFlow workFlow,String archive) throws Exception {
		
		fwBusinessSxdService.updateFwbusinessSxd(workFlow.getBusinessId(),archive);
		//添加操作记录的处理意见
		taskService.addComment(workFlow.getTaskId(), workFlow.getProcInsId(),  workFlow.getComment()+"【同意】");
		
		//处理工作流
		this.complete(workFlow.getTaskId());
		
		/*if (!StringUtils.isEmpty(workFlow.getComment())) {
			//现场开户备注
			faCommunicateLogService.saveFaCommunicateLog(workFlow.getBusinessId(), BusinessType.SXD,CommlogType.FIELD_REMARK, workFlow.getComment());
		}*/
	}
	/**
	 * @liutt
	 * 客服发送业务失败通知
	 * @param workFlow
	 * @param nodeKey
	 * @param condition
	 * @param map
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void sendFailMsg(WorkFlow workFlow, Map<String, Object> map,String isSendEmail,String emailTitle,String emailContent,String isSendSms,String smsContent,String isSendMessage,String messageTitle,String messageContent) throws Exception {
		String businessId = workFlow.getBusinessId();
    	
		//修改数据库状态为驳回
    	fwBusinessSxdService.updateState(businessId, ApplyState.REJECT, RequestUtils.getLoginedUser().getUserId());
		
		//发送客服通知
		this.sendMsg(businessId, "业务办理失败",false, isSendEmail, emailTitle, emailContent, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
		//添加操作记录的处理意见
		taskService.addComment(workFlow.getTaskId(), workFlow.getProcInsId(),  workFlow.getComment()+"【同意】");
		
		//办理流程
		this.complete(workFlow.getTaskId());
	/*	if (!StringUtils.isEmpty(workFlow.getComment())) {
			//发送失败通知的备注
			faCommunicateLogService.saveFaCommunicateLog(workFlow.getBusinessId(), BusinessType.SXD,CommlogType.BUSINESS_FAIL, workFlow.getComment());
		}*/
	}


	
	/**
	 * 转办
	 */
	@Override
	public void turnTodoTask(String taskId, String personName) {
		taskService.setAssignee(taskId, personName);
		
	}
	
	
	/**
	 * 快速贷款办理（luy）
	 * @param workFlow 接收前台参数的对象
	 * @param nodeKey 处理节点
	 * @param condition 处理意见
	 * @param map 前台参数
	 */
	@Override
	@Transactional
	public void doQuickLoan(WorkFlow workFlow, Long userId,String comName) throws Exception {
		// 更新快速贷款申请的状态,修改公司名称
		fwQuickLoanService.processSuccessCallback(Long.parseLong(workFlow.getBusinessId()), userId,workFlow.getComment(),comName);
		
		// 增加操作记录
		if (!StringUtils.isEmpty(workFlow.getComment())) {
			taskService.addComment(workFlow.getTaskId(), workFlow.getProcInsId(),  workFlow.getComment());
		}
		//添加操作日志
		faCommunicateLogService.saveQuickLoanCommunicateLog(workFlow.getBusinessId(), ConstantTool.BusinessType.QUICK_LOAN, ConstantTool.CommlogType.QUICK_LOAN_REMARK,  workFlow.getComment(), userId);
		
		//办理任务
		this.complete(workFlow.getTaskId());
	}
	
	

	/**
	 * @liutt
	 * 人工准入验证通过之后生成pdf  并且将其存到数据库中
	 * @param busId
	 * @return
	 */
	@Transactional
	private void accessCreateBankFile(String busId,String processInstanceId){
		//存储 构造"申请人身份证正面" pdf
		this.saveComController(busId, processInstanceId);
		
		// 构造“申请人身份证反面 pdf”即实际控制人 包括：申请人身份证正面；申请人身份证背面
		this.saveControllerIndentityCard(busId, processInstanceId);
		
		//控制人征信报告查询授权书
		this.saveControllerCreditReport(busId, processInstanceId);
		
		//企业征信 授权书
		this.saveCompanyCreditReport(busId, processInstanceId);
		
	}
	
	/**
	 * 构建申请人身份证pdf，并且将其存到数据库中
	 *  构造“申请人身份证正面	pdf	 包括以下几项：
	 * 实际控制人配偶身份证正面；
	 * 实际控制人配偶身份证背面；
	 * 企业纳税数据查询授权书；
	 * 纳税人、扣缴义务人涉税保密信息查询申请表； 
	 * 营业执照 ；
	 * 税务登记证  ；
	 * 组织机构代码证”
	 */
	private void saveComController(String busId,String processInstanceId){
		// 业务信息
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId); 
		List<String> listother =  new ArrayList<String>();
		// 查询实际控制人相关信息
		FwComPerBus shiJiKongZhiRenBus = fwComPerBusService.findByBusinessIdAndType(busId, ConstantTool.BusinessType.SXD, ConstantTool.PersonType.COM_CONTROLLER); // 查询实际控制人相关信息
		// 判断实际控制人是否有配偶,如果有配偶，则上传配偶的身份证正面和反面
		if (MaritalState.MARRIED.equals(shiJiKongZhiRenBus.getMaritalState())) {
			// 实际控制人配偶的身份证正面和反面
			List<FwFiles> controllerSpoList = filesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.CONTROLLER_SPO_IDENTITY_CARD);
			for (FwFiles fwFiles : controllerSpoList) {
				listother.add(Constant.FILE_UPLOAD_PATH+fwFiles.getUrl());
			}
		}
		// 纳税人、扣缴义务人涉税保密信息查询申请表
		List<FwFiles> TaxRelatedList = filesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.TAX_RELATED_CONFIDENTIAL_QUERY);
		for (FwFiles fwFiles : TaxRelatedList) {
			listother.add(Constant.FILE_UPLOAD_PATH +fwFiles.getUrl());
		}
		
		// 企业纳税数据查询授权书
		List<FwFiles> EnterpreisePayList = filesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.ENTERPRISE_PAY_TAXES_THROUGH_THE_DATA_QUERY_AUTHORIZATION);
		for (FwFiles fwFiles : EnterpreisePayList) {
			listother.add(Constant.FILE_UPLOAD_PATH +fwFiles.getUrl());
		}
		
		// 判断贷款卡号是否填写，如果没有填写上传“营业执照、税务登记证、组织机构代码、”
		if (StringUtils.isEmpty(fwBusinessSxd.getLoanCardCode())) {
			// 营业执照
			List<FwFiles> BusinessLicenseList = filesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.BUSINESS_LICENSE);
			if (BusinessLicenseList.size()>0) {
				for (FwFiles fwFiles : BusinessLicenseList) {
					listother.add(Constant.FILE_UPLOAD_PATH +fwFiles.getUrl());
				}
			}
			// 税务登记证
			List<FwFiles> TaxRegisteationCertificateList = filesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.TAX_REGISTRATION_CERTIFICATE);
			if (TaxRegisteationCertificateList.size()>0) {
				for (FwFiles fwFiles : TaxRegisteationCertificateList) {
					listother.add(Constant.FILE_UPLOAD_PATH+fwFiles.getUrl());
				}
			}
			// 组织机构代码证
			List<FwFiles> organiztionCodeCerTificateList = filesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.ORGANIZTION_CODE_CERTIFICATE);
			if (organiztionCodeCerTificateList.size()>0) {
				for (FwFiles fwFiles : organiztionCodeCerTificateList) {
					listother.add(Constant.FILE_UPLOAD_PATH+fwFiles.getUrl());
				}
			}
		}
		
		// 申请人身份证正面 pdf 实际控制人配偶身份证正面；实际控制人配偶身份证背面；企业纳税数据查询授权书；纳税人、扣缴义务人涉税保密信息查询申请表； 营业执照 ； 税务登记证  ； 组织机构代码证
		if (listother.size()>0) {
			String pdfName = pdfName(listother, busId);
			//将生成的pdf以及银行返回的结果存到数据库中
			String name = pdfName.replace(Constant.FILE_UPLOAD_PATH, "");
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, name, "", BankFileType.IDENTITY_CARD, BankFileSceneKey.ACCESS_KEY, 0);
			
		}
		
	}
	
	

	/**
	 * 构造“申请人身份证反面 pdf”即实际控制人
	 * 
	 * 申请人身份证正面；
	 * 申请人身份证背面
	 */
	private void saveControllerIndentityCard(String busId,String processInstanceId){
		//申请人身份证 
		List<FwFiles> ControllerList = filesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.CONTROLLER_IDENTITY_CARD);
		if (ControllerList.size()>0) {
			List<String> listf =  new ArrayList<String>();
			for (FwFiles fwFiles : ControllerList) {
				listf.add(Constant.FILE_UPLOAD_PATH+fwFiles.getUrl());
			}
			String pdfName = pdfName(listf, busId);
			//将生成的pdf以及银行返回的结果存到数据库中
			String name = pdfName.replace(Constant.FILE_UPLOAD_PATH, "");
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, name, "", BankFileType.IDENTITY_CARD_BACK, BankFileSceneKey.ACCESS_KEY, 0);
		}
	}
	
	
	/**
	 * 控制人征信报告查询授权书
	 * 
	 */
	private void saveControllerCreditReport(String busId,String processInstanceId){
		List<FwFiles> listPerCreditCon = filesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.CONTROLLER_CREDIT_REPORT_AUTHORIZATION);
		if (listPerCreditCon.size()>0) {
			for (FwFiles fwFiles : listPerCreditCon) {
				fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), "", BankFileType.PERSONAL_CREDIT_AUTHORIZATION, BankFileSceneKey.ACCESS_KEY, 0);
			}
		}
	}
	
	
	/**
	 * 企业征信 授权书
	 */
	private void saveCompanyCreditReport(String busId,String processInstanceId){
		List<FwFiles> listComCredit = filesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.COMPANY_CREDIT_REPORT_AUTHORIZATION);
		if (listComCredit.size()>0) {
			for (FwFiles fwFiles : listComCredit) {
				fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), "", BankFileType.COMPANY_CREDIT_AUTHORIZATION, BankFileSceneKey.ACCESS_KEY, 0);
			}
		}
	}
	
	
	/**
	 * 根据业务id、业务类型生成pdf名字
	 * @param list
	 * @param busId
	 * @return
	 */
	private String pdfName(List<String> list,String busId){
	    PDFHandleTools pdfHandleTools = new PDFHandleTools();
		
		String name = RequestUtils.makeFileName();
		String fileName = Constant.FILE_UPLOAD_PATH+File.separator+ConstantTool.BusinessType.SXD+File.separator+busId+File.separator+name;
		File file=new File(fileName);    
		if(!file.getParentFile().exists()){    
			file.getParentFile().mkdirs();    
		}
		try {
			pdfHandleTools.createPdfToLocal(list, fileName);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName;
	  }
	
	/**
	 * @liutt
	 * 根据业务id和流程id 将额度申请时 需要向银行上传的文件存到数据库中
	 * @param busId
	 * @param processInstanceId
	 */
	//TODO 查询优化 采用 in()
	@Transactional
	private void quotaCreateBankFile(String busId,String processInstanceId) {
		List<FwFiles> list = filesService.findByBusIdAndBusType(busId, BusinessType.SXD);
		for (int i = 0; i < list.size(); i++) {
			FwFiles fwFiles = list.get(i);
			String fileType = fwFiles.getFileType();
			String isfile= "";
		if (FileType.CREDIT_BUSINESS_APPLICATION.equals(fileType)) {
			//授信业务申请书
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.DIRECTORS_BOARD.equals(fileType)){
			//股东会或董事会会决议
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.BUSINESS_LICENSE.equals(fileType)){
			//企业营业执照（正、副本）
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.ORGANIZTION_CODE_CERTIFICATE.equals(fileType)){
			//经年检的企业法人组织机构代码证（正、副本）
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.TAX_REGISTRATION_CERTIFICATE.equals(fileType)){
			//税务登记证（国、地税）
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.COMPANY_ARTICLES.equals(fileType)){
			//公司章程
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.CAPITAL_VERIFICATION_REPORT.equals(fileType)){
			//验资报告
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.LEDAL_IDENTITY_CARD.equals(fileType)){
			//企业法定代表人身份证明
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.PURCHASE_SALE_CONTRACT.equals(fileType)){
			//主要供、销客户的购销合同（或购销协议）复印件（3份左右）
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.LAST_YEAR_TAX_STATEMENTS.equals(fileType)){
			//企业近三年财务报表及即期财务报表；即期报表主要科目明细（必须包括其他应收、其他应付）
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.LAST_THREE_MONTHS_STATEMENTS.equals(fileType)){
			//近3个月主要结算行对账单（基本户及主要结算账户）/近三个月企业主要结算行对账单复印件（银行对账单）
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.LAST_YEAR_TAX_PAYMENT_CERTIFICATE.equals(fileType)){
			//上年度、本年度全年的国地税纳税证明（包括纳税申报表及电子缴税凭证）/上一年度（12个月）和近三期主要税种纳税凭证复印件、增值税纳税申报表（报税系统拉报表）
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.LAST_UTILITIES_PAYMENT_VOUCHER.equals(fileType)){
			//近3个月水电费单/最近一期公用事业费缴费凭证复印件（如为制造业必须提供） - 水电煤
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.PROPERTY_RIGHT_CARD.equals(fileType)){
			//经营场地的产证（如为自有物业）或租赁合同（如为租赁物业）
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.CONTROLLER_ASSETS_LIABILITIES_APPLY.equals(fileType)){
			//个人资产负债申报表
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.ENTERPRISE_CREDIT_RATING.equals(fileType)){
			//企业纳税信用等级证明
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.MARRIAGE_LICENSE.equals(fileType)){
			//实际控制人结婚证或其他婚姻状况证明/个人保证人结婚证或其他婚姻状况证明复印件
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.FAMILY_PROPERTY.equals(fileType)){
			//企业或实际控制人及其家庭房产证
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.THE_INDIVIDUAL_INCOME_TAX.equals(fileType)){
			//实际控制连续两年缴纳个人所得税记录
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.ACCOUNT_OR_RESIDENCEPERMIT.equals(fileType)){
			//实际控制人及配偶户口本；非本地户籍实际控制人提供居住证
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.COMPANY_PROFILE.equals(fileType)){
			//公司简介（包括但不限于公司历史沿革、主要产品、合作情况、内部管理、发展情况等）
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}else if(FileType.LOAN_CARD_CODE.equals(fileType)){
			//贷款卡复印件
			fwBankRelationFilesService.saveBankRelationFiles(processInstanceId, fwFiles.getUrl(), isfile, fwFiles.getFileType(), BankFileSceneKey.QUOTA_KEY, 0);
		}
		
		}
	}

	/**
	 * 测试单元测试使用
	 * @liutt
	 * 测试使用模拟用户登陆后返回数据
	 * @param userId
	 * @return
	 */
	//TODO 
	public Map<String, Object> simulationSession(long userId){
		//模拟登陆用户获得登录用户信息
		FaAdmin sysUser = faAdminService.findByPk(userId);
		
		String sql = "SELECT DISTINCT f.* FROM fa_func f, fa_role_func rf, fa_role r, fa_admin u,"
				+ "fa_user_role ur WHERE f.func_id = rf.func_id AND r.role_id = rf.role_id AND "
				+ "r.role_id = ur.`ROLE_ID` AND ur.`USER_ID`=u.`USER_ID` AND u.user_id = ? ORDER BY f.func_id";
		
		String[] params = new String[] {sysUser.getUserId().toString()};
		//List<IPermission> funcs = new ArrayList<IPermission>();
		List<FaFunc> candidateGroups = jdbcTemplate.query(sql, params, new RowMapper<FaFunc>() {
			@Override
			public FaFunc mapRow(ResultSet rs, int rowNum) throws SQLException {
				FaFunc sysFunc = new FaFunc();
				sysFunc.setFuncId(rs.getLong("func_id"));
				//sysFunc.setFuncRemarks(rs.getString("func_name"));
				sysFunc.setFuncUrl(rs.getString("func_url"));
				sysFunc.setFuncRemarks(rs.getString("func_remarks"));
				sysFunc.setGroupCode(rs.getString("group_code"));
				sysFunc.setFuncOrder(rs.getInt("func_order"));
				sysFunc.setParentId(rs.getString("parent_id"));
				sysFunc.setFuncCode(rs.getString("func_code"));
				sysFunc.setCssClass(rs.getString("css_class"));
				FaFunc parent = new FaFunc();
				parent.setFuncCode(rs.getString("parent_id"));
				
				return sysFunc;
			}
		});
		
		// 模拟每次登陆成功后缓存角色列表，供工作流使用
		List<String> roles = new ArrayList<String>();
		for (Iterator iterator = candidateGroups.iterator(); iterator.hasNext();) {
			IPermission iPermission = (IPermission) iterator.next();
			if ("00".equals(iPermission.getGroupCode()) && iPermission.getCode().indexOf("func")==0) {
				roles.add(iPermission.getUrl());
			}
		}
		
		//模拟登陆用户查询假用户使用 创建假用户
		List<String> falseUser = faFalseUsersService.getFalseUsersByUserId(userId);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roles", roles);
		map.put("falseUser", falseUser);
		map.put("sysUser",sysUser);
		
		return map;
	}

	@Transactional
	@Override
	public boolean personDoLoanApply(Long loanId,String taskId) throws Exception {
				//调用银行登录接口
			logger.info("LoanBankApplyService========+start");
			boolean status = bankTools.checkBankLogin();
			if(!status){
				return false;
			}
			//查询业务数据
			FwLoanApply fwLoanApply = fwLoanApplyService.findByPk(loanId);
			// 调用银行接口 时构建map
			Map<String,Object> map = fwLoanApplyService.bankLoanApplyMap(fwLoanApply);
			//调用银行接口
			 Map<String, Object> result = bankTools.applyFinancingLoan(map);
			 if(result==null){
					return false;
			}
			 result = (Map<String, Object>) result.get("opRep");
			 //得到放款成功与否结果的code  0：成功  如果不是0则证明放款失败
			 String retCode = MapUtils.getString(result, "retCode");//本次交易返回
			 String message = MapUtils.getString(result, "errMsg");
			if (retCode.equals("0")) {
				//放款成功
				//根据借据号 访问银行接口查询 贷款信息
				Map<String, Object> retRsuMap = fwBusinessSxdService.getBankLoan(fwLoanApply.getLoanNo());
				String retCodeLoan = MapUtils.getString(retRsuMap, "retCode");
				retRsuMap = (Map<String, Object>) retRsuMap.get("opResult");
				//放款到期日
				String endDate = "";
				if ("0".equals(retCodeLoan)) {
					endDate = MapUtils.getString(retRsuMap, "T24DateDue","");
					Date dateEnd = new SimpleDateFormat(ConstantTool.DATA_FORMAT_DAY).parse(endDate);
					endDate = new SimpleDateFormat(ConstantTool.DATA_FORMAT).format(dateEnd);
				}
				//放款成功做业务处理
			    fwLoanApplyService.processSuccessCallback(loanId, 0L, "",endDate,result);
			    //根据放款任务id  查询放款记录 并且向交易记录表中插入放款成功记录
			    String date = DateUtils.getCurrentDateTime(ConstantTool.DATA_FORMAT);
			    //加操作日志
			    fwTransactionDetailService.saveFwTransactionDetail(fwLoanApply.getLoanNo(), fwLoanApply.getAppMoney(), "01", message, fwLoanApply.getCreateTime(), date, "1",TransactionType.FANGKUAN_STR,TransactionSource.YILUDAI,fwLoanApply.getComId());
			    monitorTools.infoOther("放款成功", "true");
			}else{
				//放款失败 进行业务处理
			   fwLoanApplyService.processFailCallback(loanId, 0L, "");
			   //加放款失败的交易记录
			   fwTransactionDetailService.saveFwTransactionDetail(fwLoanApply.getLoanNo(), fwLoanApply.getAppMoney(), "01", message, fwLoanApply.getCreateTime(), fwLoanApply.getApplyPassTime(), "0",TransactionType.FANGKUAN_STR,TransactionSource.YILUDAI,fwLoanApply.getComId());
			   //放款失败
			   monitorTools.infoOther("放款失败", "true");
			}
			Map<String, Object> variables = new HashMap<String, Object>();
		    workFlowLoanService.complete(taskId,variables);
			return true;

	}
	
}
