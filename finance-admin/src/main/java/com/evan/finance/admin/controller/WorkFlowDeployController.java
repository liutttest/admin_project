package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.activiti.service.WorkFlowService;
import com.evan.finance.admin.utils.ProcessDefinitionGeneratorEx;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;

@RequestMapping("/workflow")
@Controller
public class WorkFlowDeployController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(WorkFlowDeployController.class);

	@Autowired
	private ProcessEngine processEngine;
	
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private WorkFlowService workFlowService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private HistoryService historyService;
	
	/**
	 * 查询已经部署的流程
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/deploy", method = {GET, POST})
	public String deploy(Model model) {
        List<Object[]> objects = new ArrayList<Object[]>();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.list();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            objects.add(new Object[]{processDefinition, deployment});
        }
        model.addAttribute("objects", objects);
		return "activiti/deploy";
	}
	
	/**
     * 读取资源，通过部署ID
     *
     * @param processDefinitionId 流程定义
     * @param resourceType        资源类型(xml|image)
     * @throws Exception
     */
	@RequestMapping(value = "/resource/read")
	public void loadByDeployment(@RequestParam("processDefinitionId") String processDefinitionId, @RequestParam("resourceType") String resourceType,
	                             HttpServletResponse response) throws Exception {
	    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
	    String resourceName = "";
	    if (resourceType.equals("image")) {
	        resourceName = processDefinition.getDiagramResourceName();
	    } else if (resourceType.equals("xml")) {
	        resourceName = processDefinition.getResourceName();
	    }
	    InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
	    byte[] b = new byte[1024];
	    int len = -1;
	    while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
	        response.getOutputStream().write(b, 0, len);
	    }
	}
    
    /**
     * 部署流程
     * @return
     */
    @RequestMapping(value = "/startDeployment")
    @ResponseBody
    public String startDeployment(HttpServletRequest request) {
    	String processesKey = request.getParameter("processesKey");
    	String fileName = request.getParameter("fileName");
    	String result = workFlowService.startDeployment(processesKey, fileName);
    	return JsonUtils.toJson(RequestUtils.successResult(result));
    }
    
    /**
     * 重新发布所有流程
     * @return
     */
    @RequestMapping(value = "/startDeploymentAll")
    public String startDeploymentAll(RedirectAttributes attr,HttpServletRequest request) {
    	String[] activitis= request.getParameterValues("check-activiti");
    	for (int i = 0; i < activitis.length; i++) {
			String activi = activitis[i];
			 workFlowService.startDeployment(activi, activi);
		}
    	return "redirect:/workflow/deploy";
    }
    
    
    /**
     * 流程跟踪
     * @return
     */
    @RequestMapping(value = "/process/tracking/{taskId}")
    public String processTracking(@PathVariable("taskId") String taskId, Model model) {
    	ProcessDefinition pd = this.getProcessDefinitionByTaskId(taskId);
    	// 1. 获取流程部署ID
    	model.addAttribute("deploymentId", pd.getDeploymentId());
    	// 2. 获取流程图片的名称
    	model.addAttribute("imageName", pd.getDiagramResourceName());
    	// 3.获取当前活动的坐标
    	Map<String,Object> currentActivityCoordinates =this.getCurrentActivityCoordinates(taskId);
    	model.addAttribute("acs", currentActivityCoordinates);
    	return "process/tracking";
    }

    
    public ProcessDefinition getProcessDefinitionByTaskId(String taskId) {
    	// 1. 得到task
    	Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    	// 2. 通过task对象的pdid获取流程定义对象
    	ProcessDefinition pd = repositoryService.getProcessDefinition(task.getProcessDefinitionId());
    	return pd;
    }
    
    public Map<String, Object> getCurrentActivityCoordinates(String taskId) {
    	Map<String, Object> coordinates = new HashMap<String, Object>();
    	// 1. 获取到当前活动的ID
    	Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    	ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
    	String currentActivitiId = pi.getActivityId();
    	// 2. 获取到流程定义
    	ProcessDefinitionEntity pd = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
    	// 3. 使用流程定义通过currentActivitiId得到活动对象
    	ActivityImpl activity = pd.findActivity(currentActivitiId);
    	// 4. 获取活动的坐标
    	coordinates.put("x", activity.getX());
    	coordinates.put("y", activity.getY());
    	coordinates.put("width", activity.getWidth());
    	coordinates.put("height", activity.getHeight());
    	return coordinates;
    }
    
    @RequestMapping(value = "/testHisNode/{taskId}")
    @ResponseBody
    public String testHisNode(@PathVariable("taskId") String taskId) {
    	Map<String,Object> currentActivityCoordinates =this.getCurrentActivityCoordinates(taskId);
    	return JsonUtils.toJson(RequestUtils.successResult(currentActivityCoordinates));
    }
    
    /**
     * 历史活动查看
     * @param proInsId
     * @return
     */
    @RequestMapping(value="/queryhis/{proInsId}", method = {GET, POST})
    @ResponseBody
	public String queryhis(@PathVariable("proInsId") String proInsId) {
    	if (proInsId == null || proInsId.trim().length() == 0) {
    		return "";
    	}
    	List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery().processInstanceId(proInsId).orderByHistoricActivityInstanceEndTime().asc().list();
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	for (HistoricActivityInstance hai : hais) {
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("activitiId", hai.getActivityId());
    		map.put("name", hai.getActivityName());
    		map.put("type", hai.getActivityType());
    		map.put("pid", hai.getProcessInstanceId());
    		map.put("assignee", hai.getAssignee());
//    		map.put("startTime", hai.getStartTime());
//    		map.put("endTime", hai.getEndTime());
    		map.put("duration:", hai.getDurationInMillis());
    		list.add(map);
    	}
    	return JsonUtils.toJson(RequestUtils.successResult(list));
	}
    
    /**
     * 通过指定的流程实例id生成流程监控图
     * @param proInsId 流程实例ID
     * @param response
     */
    @RequestMapping(value="/instanceDiagram", method = {GET, POST})
	public void instanceDiagram(String proInsId, HttpServletResponse response){
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
//			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(proInsId).singleResult();
//			if(processInstance==null){
//				throw new RuntimeException("获取流程图异常!"); 
//			}else{
//				BpmnModel bpmnModel= repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
//				List<HistoricActivityInstance> activityInstances=historyService .createHistoricActivityInstanceQuery()
//							.processInstanceId(proInsId)
//							.orderByHistoricActivityInstanceStartTime().asc().list();
//				List<String> activitiIds=new ArrayList<String>();
//				List<String> flowIds=new ArrayList<String>();
//				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
//							.getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
//				flowIds=this.getHighLightedFlows(processDefinition, activityInstances);//获取流程走过的线
//				
//				for(HistoricActivityInstance hai:activityInstances){
//					 activitiIds.add(hai.getActivityId());//获取流程走过的节点
//				}
//				ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
//				 
//				Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());
////				Context.setProcessEngineConfiguration(processEngineFactoryBean.getProcessEngineConfiguration());
////				Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration());
//				
//				InputStream imageStream = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator().generateDiagram(bpmnModel, "png", activitiIds,flowIds);
//				
				ProcessDefinitionGeneratorEx peg = new ProcessDefinitionGeneratorEx(repositoryService, runtimeService);
				InputStream imageStream = peg.generateDiagramWithHighLight(proInsId);
				response.setContentType("image/png");
				OutputStream os = response.getOutputStream();
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				imageStream.close();
//			}
		} catch (Exception e) {
			logger.error("获取流程图异常!", e);
			throw new RuntimeException("获取流程图异常!");
		}
	}

	private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {
		List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
			ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息
			List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
			ActivityImpl sameActivityImpl1 = processDefinitionEntity.findActivity(historicActivityInstances.get(i + 1).getActivityId());// 将后面第一个节点放在时间相同节点的集合里
			sameStartTimeNodes.add(sameActivityImpl1);
			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
				HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
				HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
				if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {// 如果第一个节点和第二个节点开始时间相同保存
					ActivityImpl sameActivityImpl2 = processDefinitionEntity.findActivity(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {// 有不相同跳出循环
					break;
				}
			}
			List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
			for (PvmTransition pvmTransition : pvmTransitions) {// 对所有的线进行遍历
				ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					highFlows.add(pvmTransition.getId());
				}
			}
		}
		return highFlows;
	}

}
