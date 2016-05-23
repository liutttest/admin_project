package com.evan.finance.admin.activiti.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.domain.JPage;
import com.evan.common.user.service.FwBusinessFcdService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwBusinessTalksService;
import com.evan.common.user.service.FwCompanyBusService;
import com.evan.common.user.service.FwCompanyService;
import com.evan.common.user.service.FwFeedbackService;
import com.evan.common.user.service.FwLoanApplyService;
import com.evan.common.user.service.FwQuickLoanService;
import com.evan.common.user.service.FwRepaymentService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.DateUtils;
import com.evan.finance.admin.activiti.bean.WorkFlowView;
import com.evan.finance.admin.activiti.service.WorkFlowService;
import com.evan.finance.admin.utils.ActivityUtil;
import com.evan.finance.admin.utils.WorkFlowUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;

@Service
public class WorkFlowServiceImpl implements WorkFlowService {

	@Autowired
	private ProcessEngine processEngine;
	
	@Autowired 
	private TaskService taskService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	
	@Autowired
	private FwBusinessFcdService fwBusinessFcdService;
	
	@Autowired
	private FwCompanyBusService fwCompanyBusService;
	
	@Autowired
	private FwLoanApplyService fwLoanApplyService;
	
	@Autowired 
	private FwQuickLoanService fwQuickLoanService;
	
	@Autowired
	private FwCompanyService fwCompanyService;
	
	@Autowired
	private FwFeedbackService fwFeedbackService;
	
	@Autowired
	private FwRepaymentService fwRepaymentService;
	
	@Autowired
	private FwBusinessTalksService fwBusinessTalksService;
	
	@Autowired
	private JdbcTemplate jdbcDao;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public String startDeployment(String processesKey, String fileName) {
		RepositoryService repositoryService = processEngine.getRepositoryService();  
	      
	    //获取在classpath下的流程文件  
	    InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/"+fileName+".zip");  
	    ZipInputStream zipInputStream = new ZipInputStream(in);  
	    //使用deploy方法发布流程  
	    repositoryService.createDeployment()  
	                     .addZipInputStream(zipInputStream)  
	                     .name(processesKey)  
	                     .deploy();  
		return "OK";
	}

	@Override
	public Map<String, Object> findTodoTasks(String userId, List<String> candidateGroups,List<String> falseUser) {
		TaskQuery taskQuery = null;
		List<Task> tasks = new ArrayList<Task>();
		if (candidateGroups!=null) {
			taskQuery = taskService.createTaskQuery().taskCandidateGroupIn(candidateGroups);
			tasks = taskQuery.list();
		}
        
        taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(userId);
        tasks.addAll(taskQuery.list());
        //循环查询任务分派 假用户的任务
        for (String userName : falseUser) {
        	taskQuery = taskService.createTaskQuery().taskAssignee(userName);
        	tasks.addAll(taskQuery.list());
		}
        
        
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        List<WorkFlowView> workFlowViews = new ArrayList<WorkFlowView>();
        // 根据流程的业务ID查询实体并关联
        for (Task task : tasks) {
        	
        	String processInstanceId = task.getProcessInstanceId();
        	ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
        	String businessKey = "";
        	if (processInstance==null || StringUtils.isEmpty(processInstance.getBusinessKey())) {
        		continue;
        	}
        	businessKey = processInstance.getBusinessKey();
        	
        	WorkFlowView workFlowView = new WorkFlowView();
        	//SxdFlowView sxdFlowView = new SxdFlowView();
        	workFlowView.setTask(task);
        	workFlowView.setProcessInstance(processInstance);
        	workFlowView.setProcessInstanceId(processInstanceId);
        	ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
        	workFlowView.setProcessDefinition(processDefinition);
        	
        	// 当前业务数据
        	//准入验证
    		if (task.getProcessDefinitionId().indexOf(WorkFlowUtils.process_definition_key_access_verify) == 0) {
    			workFlowView.setFlowType(WorkFlowUtils.process_definition_key_access_verify);
    		//额度申请
    		}else if(task.getProcessDefinitionId().indexOf(WorkFlowUtils.process_definition_key_quota_apply) == 0) {
    			workFlowView.setFlowType(WorkFlowUtils.process_definition_key_quota_apply);
    		//现场开户
    		}else if(task.getProcessDefinitionId().indexOf(WorkFlowUtils.process_definition_key_field_account) == 0){
    			workFlowView.setFlowType(WorkFlowUtils.process_definition_key_field_account);
    		//放款
    		}else if(task.getProcessDefinitionId().indexOf(WorkFlowUtils.process_definition_key_loan) == 0 && task.getProcessDefinitionId().indexOf(ActivityUtil.PROCDEFKEY) != 0){
    			//如果是放款 则查询出 放款对象 根据放款对象查询出 业务id
    			continue;
    			/*workFlowView.setFlowType(WorkFlowUtils.process_definition_key_loan);
    			FwLoanApply fwLoanApply = fwLoanApplyService.findByPk(Long.parseLong(businessKey));
    			if (fwLoanApply!=null) {
    				//TODO 上海银行联调
    			businessKey = fwLoanApply.getBusinessId();
    			workFlowView.setLoanId(fwLoanApply.getLoanId().toString());
    			}*/
    			
    		}else if(task.getProcessDefinitionId().indexOf(ActivityUtil.PROCDEFKEY) == 0){
    			continue;
    		}
    		
    		if (task.getProcessDefinitionId().indexOf(WorkFlowUtils.process_definition_key_quickloan) == 0) {
    			continue;
    			//如果是快速贷款 则查询出 放款对象 根据放款对象查询出 业务id
    			/*workFlowView.setFlowType(WorkFlowUtils.process_definition_key_quickloan);
    			FwQuickLoan fwQuickLoan = fwQuickLoanService.findByPk(Long.parseLong(businessKey));
    			if(fwQuickLoan!=null){
    				workFlowView.setComName(fwQuickLoan.getComName());//企业名称
    			}*/
			}else{
				FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(businessKey);
	    		//根据业务id和业务类型查询 和业务相关的企业信息
	    		//FwCompany fwCompany = fwCompanyService.findByPk(fwBusinessSxd.getComId());
	    		if (fwBusinessSxd!=null) {
	    			workFlowView.setComName(fwBusinessSxd.getComName());//企业名称
	    		}
	    		workFlowView.setFwBusinessSxd(fwBusinessSxd);
			}
    		workFlowView.setBusinessKey(businessKey);
    		workFlowViews.add(workFlowView);//业务数据
    		
        }
        Collections.sort(workFlowViews);
        map.put("workflows", workFlowViews);

		return map;
	}
	
	@Override
	public JPage findTodoTasksSys(String userName,JPage page,String suspensionState) {
		String searchString = page.getsSearch().trim();
		
		long count = this.getSysTaskCount(userName,searchString, suspensionState);
		page.setiTotalDisplayRecords(count);
		page.setiTotalRecords(count);
		List<Map<String,Object>> list = this.getSysList(userName, page, suspensionState, searchString);
		//定义系统任务数组
		List<Map<String,Object>> listObject = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> activiti : list) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("comName", MapUtils.getString(activiti, "COM_NAME",""));
			map.put("comId", MapUtils.getString(activiti, "COM_ID"));
			map.put("createTime",DateUtils.getDateStrFormat(ConstantTool.DATA_FORMAT,MapUtils.getString(activiti, "CREATE_TIME_")));// 任务创建时间
			map.put("businessId", MapUtils.getString(activiti, "BS_ID"));
			map.put("name", MapUtils.getString(activiti, "NAME_"));// 节点名称
			map.put("procInsId",MapUtils.getString(activiti, "PROC_INST_ID_"));// 流程实例id
			map.put("taskId", MapUtils.getString(activiti, "ID_"));
			map.put("taskDefKey",MapUtils.getString(activiti, "TASK_DEF_KEY_"));
			map.put("assignee",MapUtils.getString(activiti, "ASSIGNEE_"));
			map.put("suspensionState",MapUtils.getString(activiti, "SUSPENSION_STATE_"));
			listObject.add(map);
		}
		page.setAaData(listObject);
       
		return page;
	}
	
	/**
	 * 根据查询条件查询数据总条数
	 * @param userName
	 * @param page
	 * @param searchString
	 * @param suspensionState
	 * @return
	 */
	public long getSysTaskCount(String userName,String searchString,String suspensionState){
		StringBuffer sql = new StringBuffer();
		 sql.append("select count(*) as count FROM ACT_RU_TASK RES LEFT JOIN ACT_RU_IDENTITYLINK I  ON I.TASK_ID_ = RES.ID_ LEFT JOIN FW_BUSINESS_SXD SXD ON RES.PROC_INST_ID_ = SXD.PROCESS_INSTANCE_ID where ");
		 sql.append("(RES.PROC_DEF_ID_ LIKE '"+ActivityUtil.PROCDEFKEY+":%' AND RES.ASSIGNEE_ = '");
	     sql.append(userName).append("'");
	     //搜索条件
	     if (StringUtils.isNotEmpty(searchString)) {
			sql.append(" AND (SXD.COM_NAME like '%").append(searchString).append("%' ");
			sql.append(" or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '%").append(searchString).append("%') ");
		 }
	     if (!"0".equals(suspensionState)) {
			sql.append(" AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(")");
		 }
	     sql.append(" OR ( RES.PROC_DEF_ID_ LIKE '"+ActivityUtil.PROCDEFKEY+":%' AND RES.ASSIGNEE_ IS NULL AND ");
	     sql.append("( I.USER_ID_ = '");
	     sql.append(userName);
	     sql.append("' )))");
	     //查询总条数
	     Map<String,Object> map = jdbcTemplate.queryForMap(sql.toString());
	     return MapUtils.getLongValue(map, "count");
	}
	
	/**
	 * 根据查询条件查询系统任务分页数据
	 * @param userName
	 * @param page
	 * @param suspensionState
	 * @param searchString
	 * @return
	 */
	public List<Map<String,Object>> getSysList(String userName,JPage page,String suspensionState,String searchString){
		StringBuffer sql = new StringBuffer();
		 sql.append("select RES.* ,SXD.* FROM ACT_RU_TASK RES LEFT JOIN ACT_RU_IDENTITYLINK I  ON I.TASK_ID_ = RES.ID_ LEFT JOIN FW_BUSINESS_SXD SXD ON RES.PROC_INST_ID_ = SXD.PROCESS_INSTANCE_ID where ");
		 sql.append("(RES.PROC_DEF_ID_ LIKE '"+ActivityUtil.PROCDEFKEY+":%' AND RES.ASSIGNEE_ = '");
	     sql.append(userName).append("'");
	     //搜索条件
	     if (StringUtils.isNotEmpty(searchString)) {
			sql.append(" AND (SXD.COM_NAME like '%").append(searchString).append("%' ");
			sql.append(" or  RES.NAME_ like '%").append(searchString).append("%' or RES.CREATE_TIME_ like '%").append(searchString).append("%') ");
		 }
	     if (!"0".equals(suspensionState)) {
			sql.append(" AND (RES.SUSPENSION_STATE_ = ").append(suspensionState).append(")");
		 }
	     sql.append(" OR ( RES.PROC_DEF_ID_ LIKE '"+ActivityUtil.PROCDEFKEY+":%' AND RES.ASSIGNEE_ IS NULL AND ");
	     sql.append("( I.USER_ID_ = '");
	     sql.append(userName);
	     sql.append("' )))");
		 sql.append(" order by RES.CREATE_TIME_ desc limit ");
		 sql.append(page.getiDisplayStart());
		 sql.append(",");
		 sql.append(page.getiDisplayLength());
		
		//查询待办任务
		return jdbcTemplate.queryForList(sql.toString());
	}

	@Override
	public void turnTodoTask(String taskId, String personName) {
		taskService.setAssignee(taskId, personName);
		
	}
	
	/**
	 * 根据业务id，当前节点，分配人查询任务
	 * @param definitionKey 业务id
	 * @param node 当前节点
	 * @param assgine 分配者
	 * @return
	 */
	public Task getTaskByDefineKetAndNode(String definitionKey,String node,String assgine){
		List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(definitionKey).list();
		Task task = null;
		for (Task task1 : tasks) {
			if (assgine.equals(task1.getAssignee()) && task1.getTaskDefinitionKey().indexOf(node)== 0 ) {
				task=task1;
			}
		}
		return task;
	}

}
