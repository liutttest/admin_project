package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.web.controller.BaseController;

@RequestMapping("/activiti")
@Controller
public class ActivitiDemoController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(ActivitiDemoController.class);
	
	@Autowired
	private ProcessEngine processEngine;
	
	@RequestMapping(value="/demo", method = {GET, POST})
	public String main(@ModelAttribute SearchForm searchForm, Model model) {
		return "main/demo";
	}
	
	// 发布流程 部署
	// 发布流程后，流程文件会保存到数据库中
	@RequestMapping(value="/deployFlow", method = {GET, POST})
	public String deployFlow(@ModelAttribute SearchForm searchForm, Model model) {
		
		RepositoryService repositoryService = processEngine.getRepositoryService();  
	      
	    //获取在classpath下的流程文件  
	    InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/fuchidai.zip");  
	    ZipInputStream zipInputStream = new ZipInputStream(in);  
	    //使用deploy方法发布流程  
	    repositoryService.createDeployment()  
	                     .addZipInputStream(zipInputStream)  
	                     .name("fuchidai")  
	                     .deploy();  
	    model.addAttribute("deployResult", "OK");
		return "main/demo";
	}
	
	// 获取流程定义信息
	@RequestMapping(value="/queryProcdef", method = {GET, POST})
	public String queryProcdef(@ModelAttribute SearchForm searchForm, Model model) {
		RepositoryService repositoryService = processEngine.getRepositoryService();  
	    //创建查询对象  
	    ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();  
	    //添加查询条件  
	    query.processDefinitionKey("fuchidai");//通过key获取  
	        // .processDefinitionName("My process")//通过name获取  
	        // .orderByProcessDefinitionId()//根据ID排序  
	    //执行查询获取流程定义明细  
	    List<ProcessDefinition> pds = query.list();  
	    String definitionInfo = "";
	    for (ProcessDefinition pd : pds) {  
	    	definitionInfo += "ID:"+pd.getId()+",NAME:"+pd.getName()+",KEY:"+pd.getKey()+",VERSION:"+pd.getVersion()+",RESOURCE_NAME:"+pd.getResourceName()+",DGRM_RESOURCE_NAME:"+pd.getDiagramResourceName()+"\n";  
	    }  
	    model.addAttribute("definitionInfo", definitionInfo);
		return "main/demo";
	}
	
	// 发布流程 实例
	@RequestMapping(value="/startFlow", method = {GET, POST})
	public String startFlow(@ModelAttribute SearchForm searchForm, Model model) {
		RuntimeService runtimeService = processEngine.getRuntimeService();  
        /** 
         * 启动请假单流程  并获取流程实例 
         * 因为该请假单流程可以会启动多个所以每启动一个请假单流程都会在数据库中插入一条新版本的流程数据 
         * 通过key启动的流程就是当前key下最新版本的流程 
         */  
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("fuchidai");
        model.addAttribute("startFlowInfo", "id:"+processInstance.getId()+",activitiId:"+processInstance.getActivityId());
        return "main/demo";
	}
	
	// 查看任务 通过用户ID
	@RequestMapping(value="/queryTask", method = {GET, POST})
	public String queryTask(@ModelAttribute SearchForm searchForm, Model model) {
		//获取任务服务对象  
	    TaskService taskService = processEngine.getTaskService();  
	    //根据接受人获取该用户的任务  
	    List<Task> tasks = taskService.createTaskQuery()  
	                                .taskAssignee("张三")  
	                                .list();  
	    String taskInfo = "";
	    for (Task task : tasks) {  
	    	taskInfo += "ID:"+task.getId()+",姓名:"+task.getName()+",接收人:"+task.getAssignee()+",开始时间:"+task.getCreateTime()+"\n";  
	    } 
	    model.addAttribute("taskInfo", taskInfo);
		return "main/demo";
	}

	// 提出请假申请，启动流程
	@RequestMapping(value="/startTask", method = {GET, POST})
	public String startTask(@ModelAttribute SearchForm searchForm, Model model) {
		TaskService taskService = processEngine.getTaskService();  
	    //taskId 就是查询任务中的 ID  
	    String taskId = "12502";  
	    //完成请假申请任务  
	    taskService.complete(taskId );
	    model.addAttribute("startTaskInfo", "OK");
		return "main/demo";
	}
}

