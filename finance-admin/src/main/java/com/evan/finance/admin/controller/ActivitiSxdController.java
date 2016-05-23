package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.common.user.domain.FaCenterDataBank;
import com.evan.common.user.domain.FaInfoNopass;
import com.evan.common.user.domain.FaRisk;
import com.evan.common.user.domain.FaTempScene;
import com.evan.common.user.domain.FwBlacklist;
import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.domain.FwComPerBus;
import com.evan.common.user.domain.FwQuickLoan;
import com.evan.common.user.domain.SysDicIndustry;
import com.evan.common.user.domain.SysUser;
import com.evan.common.user.monitor.MonitorTools;
import com.evan.common.user.service.FaCenterDataBankService;
import com.evan.common.user.service.FaCommunicateLogService;
import com.evan.common.user.service.FaInfoNopassService;
import com.evan.common.user.service.FaOperationRecordService;
import com.evan.common.user.service.FaRiskService;
import com.evan.common.user.service.FaTempSceneService;
import com.evan.common.user.service.FilesBankService;
import com.evan.common.user.service.FwBankRelationFilesService;
import com.evan.common.user.service.FwBlacklistService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwComAccountService;
import com.evan.common.user.service.FwComPerBusService;
import com.evan.common.user.service.FwLoanApplyService;
import com.evan.common.user.service.FwMessageService;
import com.evan.common.user.service.FwQuickLoanService;
import com.evan.common.user.service.FwTransactionDetailService;
import com.evan.common.user.service.SysDicIndustryService;
import com.evan.common.user.service.SysUserService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.ConstantTool.BusinessType;
import com.evan.common.utils.ConstantTool.FileType;
import com.evan.common.utils.ConstantTool.NoPass;
import com.evan.common.utils.ConstantTool.OperationState;
import com.evan.common.utils.ConstantTool.PersonType;
import com.evan.common.utils.DateUtils;
import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.activiti.bean.WorkFlow;
import com.evan.finance.admin.activiti.service.ActivitiSxdService;
import com.evan.finance.admin.activiti.service.WorkFlowLoanService;
import com.evan.finance.admin.bank.BankTools;
import com.evan.finance.admin.notification.service.NotificationService;
import com.evan.finance.admin.utils.ActivityUtil;
import com.evan.finance.admin.utils.FalseUserCacheLoader;
import com.evan.finance.admin.utils.RoleCacheLoader;
import com.evan.finance.admin.utils.WorkFlowUtils;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;
import com.evan.nd.common_file.domain.FwFiles;
import com.evan.nd.common_file.service.FwFilesService;

/**
 * 工作流办理
 * @author liutingting
 *
 */
@RequestMapping("/activitiSxd")
@Controller
public class ActivitiSxdController extends BaseController {
	
	final Logger logger = LoggerFactory.getLogger(ActivitiSxdController.class);
	
	@Autowired
	private FwMessageService fwMessageService;

	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;

	@Autowired
	private FaCenterDataBankService faCenterDataBankService;

	@Autowired
	private BankTools bankTools;

	@Autowired
	private FwFilesService filesService;

	@Autowired
	private MonitorTools monitorTools;

	@Autowired
	private FilesBankService filesBankService;

	@Autowired
	private FaCommunicateLogService faCommunicateLogService;

	@Autowired
	private FaOperationRecordService faOperationRecordService;

	@Autowired
	private FaInfoNopassService faInfoNopassService;

	@Autowired
	private FwComAccountService fwComAccountService;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private FaTempSceneService faTempSceneService;

	@Autowired
	private FwBankRelationFilesService fwBankRelationFilesService;

	@Autowired
	private ActivitiSxdService activitiSxdService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FwLoanApplyService fwLoanApplyService;
	
	@Autowired
	private WorkFlowLoanService workFlowLoanService;
	
	@Autowired
	private FwTransactionDetailService fwTransactionDetailService;
	
	@Autowired
	private FwQuickLoanService fwQuickLoanService;
	
	@Autowired
	private FwComPerBusService fwComPerBusService;
	
	@Autowired
	private FwFilesService fwFilesService;
	
	@Autowired
	private FaRiskService faRiskService;
	
	@Autowired
	private SysDicIndustryService sysDicIndustryService;
	
	@Autowired
	private FwBlacklistService fwBlacklistService;

	/**
	 * 跳转到待办任务页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getActivitiList", method = GET)
	public String activitiSxd() {
		return "activiti/task";
	}

	/**
	 * 查询待办任务列表(税信贷)
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getActivitiList/sxd", method = { GET, POST })
	@ResponseBody
	public String getActivitiSxdList(HttpServletRequest request, @RequestParam Map<String, Object> map) {

		// 默认查询的是税信贷业务相关的工作流
		String userKey = RequestUtils.getLoginedUser().getUserName();// 当前登陆用户
		List<String> candidateGroups = RoleCacheLoader.getInstance().getRoles(userKey);// 用户权限组
		List<String> userNameList = FalseUserCacheLoader.getInstance().getRoles(userKey);// 分配任务所用到的 假用户

		// 记录操作的次数 每次加1
	String sEcho = "0";

		// 起始纪录
		String iDisplayStart = "0";

		// 每页的条数
		String iDisplayLength = "10";

		// 搜索字符串
		String searchString = "";

		//下拉选
        String suspensionState = "";
		// 获取jquery datatable当前配置参数
		String aoData = request.getParameter("aoData");
		JSONArray jsonArray = JSONArray.fromObject(aoData);
		for (int i = 0; i < jsonArray.size(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				if (jsonObject.get("name").equals("sEcho"))
					sEcho = jsonObject.get("value").toString();
				else if (jsonObject.get("name").equals("iDisplayStart"))
					iDisplayStart = jsonObject.get("value").toString();
				else if (jsonObject.get("name").equals("iDisplayLength"))
					iDisplayLength = jsonObject.get("value").toString();
				else if (jsonObject.get("name").equals("sSearch"))
					searchString = jsonObject.get("value").toString();
				else if (jsonObject.get("name").equals("suspensionState"))
					suspensionState = jsonObject.get("value").toString();
			} catch (Exception e) {
				break;
			}
		}
		searchString = searchString.trim();
		
		int startNo = Integer.parseInt(iDisplayStart);
		List<Map<String, Object>> list = activitiSxdService.findTodoTasks(userKey, candidateGroups, userNameList,Integer.parseInt(iDisplayLength), startNo, searchString,suspensionState);
		
		// 查询出来的总数量
		long count = activitiSxdService.findTodoTasksCount(userKey, candidateGroups,userNameList, Integer.parseInt(iDisplayLength), startNo,searchString,suspensionState);
		
		JSONArray jsonArray2 = new JSONArray();
		JSONObject jsonObject2 = new JSONObject();

		// 为操作次数加1
		int initEcho = Integer.parseInt(sEcho) + 1;
		SimpleDateFormat sFormat = new SimpleDateFormat(ConstantTool.DATA_FORMAT);
		for (Map<String, Object> activiti : list) {
			jsonObject2.put("comName", MapUtils.getString(activiti, "COM_NAME"));
			jsonObject2.put("comId", MapUtils.getString(activiti, "COM_ID"));
			Date date = null;
			try {
				date = sFormat.parse(MapUtils.getString(activiti, "CREATE_TIME_"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String createTime = sFormat.format(date);
			jsonObject2.put("createTime",createTime);// 任务创建时间
			jsonObject2.put("businessId", MapUtils.getString(activiti, "BS_ID"));
			jsonObject2.put("name", MapUtils.getString(activiti, "NAME_"));// 节点名称
			jsonObject2.put("procInsId",MapUtils.getString(activiti, "PROC_INST_ID_"));// 流程实例id
			jsonObject2.put("taskId", MapUtils.getString(activiti, "ID_"));
			jsonObject2.put("taskDefKey",MapUtils.getString(activiti, "TASK_DEF_KEY_"));
			jsonObject2.put("assignee",MapUtils.getString(activiti, "ASSIGNEE_"));
			jsonObject2.put("suspensionState",MapUtils.getString(activiti, "SUSPENSION_STATE_"));
			jsonArray2.add(jsonObject2);
		}

		String json = "{\"sEcho\":" + initEcho + ",\"iTotalRecords\":" + count + ",\"iTotalDisplayRecords\":" + count + ",\"aaData\":" + jsonArray2.toString() + "}";

		return json;
	}

	/**
	 * 签收任务
	 */
	@RequestMapping(value = "task/claim", method = POST)
	@ResponseBody
	public String claim(@ModelAttribute WorkFlow workFlow) {
		try {
			activitiSxdService.claim(workFlow.getTaskId(), RequestUtils.getLoginedUser().getUserName());
		} catch (Exception e) {
			// 服务器异常
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("service.error")));
		}
		// 签收成功
		return JsonUtils.toJson(RequestUtils.successResult(""));
	}
	
	
	/**
	 * 快速贷款签收任务
	 */
	@RequestMapping(value = "task/claim/quickloan", method = POST)
	@ResponseBody
	public String claimQuickloan(@ModelAttribute WorkFlow workFlow,@RequestParam("loanId") Long loanId) {
		try {
			activitiSxdService.claim(workFlow.getTaskId(), RequestUtils.getLoginedUser().getUserName());
			//修改快速贷款状态为01处理中
			fwQuickLoanService.updateQuickLoanState(loanId, ConstantTool.QuickApplyState.APPLYING);
		} catch (Exception e) {
			// 服务器异常
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("service.error")));
		}
		// 签收成功
		return JsonUtils.toJson(RequestUtils.successResult(""));
	}

	/**
	 * 点击办理进行页面跳转model弹出来
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	//TODO else if 优化
	@RequestMapping(value = "/banliGotoJsp", method = RequestMethod.GET)
	public String banliGotoJsp(HttpServletRequest request, @RequestParam Map<String, Object> map, @ModelAttribute WorkFlow workFlow, Model model) {
		// 点击办理根据节点进行页面跳转的判断
		model.addAttribute("businessId", workFlow.getBusinessId());
		model.addAttribute("taskId", workFlow.getTaskId());
		model.addAttribute("taskDefKey", workFlow.getTaskDefKey());
		model.addAttribute("procInsId", workFlow.getProcInsId());
		if (WorkFlowUtils.role_manual_verify.equals(workFlow.getTaskDefKey())) {
			// 人工准入验证
			
			// 查询准入驳回通知场景是否是手动发送通知
			FaTempScene faTempScene = faTempSceneService.getTempBySecneKey(ConstantTool.SCENE.ZRBH);
			model.addAttribute("sendType", faTempScene.getSendType()); 			
			// 查询风控数据
			FaRisk faRisk = faRiskService.findByBusinessIdAndType(workFlow.getBusinessId(), ConstantTool.BusinessType.SXD);
			model.addAttribute("faRisk", faRisk);
			
			return "role_manual_verify";
		} else if (WorkFlowUtils.role_service_phone_call.equals(workFlow.getTaskDefKey())) {
			// 准入验证客服通知

			return "role_service_phone_call";
		} else if (WorkFlowUtils.role_manual_review.equals(workFlow.getTaskDefKey())) {
			// 人工复核
			
			// 查询额度申请-人工复核驳回通知场景是否是手动发送通知
			FaTempScene faTempScene = faTempSceneService.getTempBySecneKey(ConstantTool.SCENE.EDSQBO);
			model.addAttribute("sendType", faTempScene.getSendType());
			// 查询风控数据
			FaRisk faRisk = faRiskService.findByBusinessIdAndType(workFlow.getBusinessId(), ConstantTool.BusinessType.SXD);
			model.addAttribute("faRisk", faRisk);
			// 查询业务相关信息
			FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(workFlow.getBusinessId());
			model.addAttribute("fwBusinessSxd", fwBusinessSxd);
			// 查询行业相关信息
			if (fwBusinessSxd.getIndustryId() != null) {
				SysDicIndustry sysDicIndustry = sysDicIndustryService.findByPk(fwBusinessSxd.getIndustryId());
				model.addAttribute("sysDicIndustry", sysDicIndustry);
			}
			// 计算企业的经营年限
			Long operationYearInteger = 0L;
			Date setUpTime = DateUtils.parseDate( fwBusinessSxd.getSetUpTime());
			operationYearInteger = DateUtils.getDiffYears(setUpTime, new Date());
			model.addAttribute("operationYearInteger", operationYearInteger);
			// 查询企业实际控制人信息
			FwComPerBus controllerPer = fwComPerBusService.findByBusinessIdAndType(workFlow.getBusinessId(), ConstantTool.BusinessType.SXD, ConstantTool.PersonType.COM_CONTROLLER);
			model.addAttribute("controllerPer", controllerPer);
			// 查询是否进入一路贷黑名单(0:不是；1：是；)
			Integer isFwBlacklist = 0; //(0:不是；1：是；)
			FwBlacklist fwBlacklist = fwBlacklistService.findByComId(fwBusinessSxd.getComId());
			if (fwBlacklist != null) {
				isFwBlacklist = 1;
			}
			model.addAttribute("isFwBlacklist", isFwBlacklist);
			// 查询风控项的分数
			FaCenterDataBank faCenterDataBank = faCenterDataBankService.getByBusinessId(workFlow.getBusinessId());
			model.addAttribute("faCenterDataBank", faCenterDataBank);
			
			return "role_manual_review";
		} else if (WorkFlowUtils.role_xamination_approval.equals(workFlow.getTaskDefKey())) {
			// 审批
			return "role_xamination_approval";
		} else if (WorkFlowUtils.role_approved_credit_limit.equals(workFlow.getTaskDefKey())) {
			// 人工核准授信额度
			
			// 查询业务相关信息，以便页面显示
			FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(workFlow.getBusinessId());
			model.addAttribute("fwBusinessSxd", fwBusinessSxd); 	
			
			return "role_approved_credit_limit";
		} else if (WorkFlowUtils.quota_manager_approval_role.equals(workFlow.getTaskDefKey())) {
			// 经理审批
			
			FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(workFlow.getBusinessId());
			model.addAttribute("comName", fwBusinessSxd.getComName()); 	
			
			return "quota_manager_approval_role";
		} else if (WorkFlowUtils.role_qutoa_service_call.equals(workFlow.getTaskDefKey())) {
			// 客服发送额度申请结果并通知现场开户
			return "role_qutoa_service_call";
		} else if (WorkFlowUtils.role_task_assign.equals(workFlow.getTaskDefKey())) {
			// 现场分配任务
			
			return "role_task_assign";
		} else if (WorkFlowUtils.role_data_backfill.equals(workFlow.getTaskDefKey())) {
			// 数据回填
			return "role_data_backfill";
		} else if (WorkFlowUtils.role_business_archive.equals(workFlow.getTaskDefKey())) {
			// 业务归档
			return "role_business_archive";
		}else if(WorkFlowUtils.role_send_fail_msg.equals(workFlow.getTaskDefKey())){
			//获取场景模板的key
			String scene = (String)taskService.getVariable(workFlow.getTaskId(), "scene");
			//
			model.addAttribute("scene", scene);
			return "role_send_fail_msg";
		}
		// 跳转到错误页面
		return "result/error";
	}
	
	
	/**
	 * 待办任务-快速贷款-点击“办理”-跳转modal
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/banliGotoJspForQuickLoan", method = RequestMethod.GET)
	public String banliGotoJspForQuickLoan(HttpServletRequest request,@RequestParam Map<String, Object> map,@ModelAttribute WorkFlow workFlow, Model model) {
		// 点击办理根据节点进行页面跳转的判断
		model.addAttribute("businessId", workFlow.getBusinessId());
		model.addAttribute("taskId", workFlow.getTaskId());
		model.addAttribute("taskDefKey", workFlow.getTaskDefKey());
		model.addAttribute("procInsId", workFlow.getProcInsId());
		
		
		return "quickLoan";
	}
	
	
	
	/**
	 * @liutt
	 * 其他 如 转办  打印清单  查看流程  进行页面跳转
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/otherGotoJsp", method = RequestMethod.GET)
	public String otherGoTOJsp(HttpServletRequest request,
			@RequestParam Map<String, Object> map,
			@ModelAttribute WorkFlow workFlow, Model model) {
		String type = MapUtils.getString(map, "type");
		
		model.addAttribute("businessId", workFlow.getBusinessId());
		model.addAttribute("taskId", workFlow.getTaskId());
		model.addAttribute("taskDefKey", workFlow.getTaskDefKey());
		
		if ("show_flow_chart".equals(type)) {
			//查看流程
			String data = MapUtils.getString(map, "data");
			try {
				data = java.net.URLDecoder.decode(data, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String procInsId = workFlow.getProcInsId();
			
			model.addAttribute("data", data);
			model.addAttribute("procInsId", procInsId);
			return "show_flow_chart";
		}else if("task_transferred".equals(type)){
			//转办
			
			return "task_transferred";
		}else if("openAccount_printList".equals(type)){
			//打印清单
			return "openAccount_printList";
		}else if("operation_record".equals(type)){
			//操作记录
			String procInsId = workFlow.getProcInsId();
			model.addAttribute("procInsId", procInsId);
			return "operation_record";
		}
		// 跳转到错误页面
		return "result/error";
	}

	/**
	 * 人工准入验证办理节点进行办理
	 * @liutt
	 * @param taskId
	 * @param request
	 * @param attr
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "task/access/manual/verify", method = {RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String complete(@ModelAttribute WorkFlow workFlow,@ModelAttribute FaRisk faRisk, HttpServletRequest request, @RequestParam Map<String, Object> map) {
		
		// 获取发消息的参数所需的参数
		String isSendEmail = MapUtils.getString(map, "emailCheck"); // 是否发送邮件（on:发送；）
		String emailTitle = MapUtils.getString(map, "emailTitle"); // 邮件模板标题
		String emailContent = MapUtils.getString(map, "emailContent"); // 邮件内容
		String isSendSms = MapUtils.getString(map, "smsCheck"); // 是否发送短信（on:发送；）
		String smsContent = MapUtils.getString(map, "smsContent"); // 短信内容
		String isSendMessage = MapUtils.getString(map, "messageCheck"); // 是否发送站内信（on:发送；）
		String messageTitle = MapUtils.getString(map, "messageTitle"); // 站内信标题
		String messageContent = MapUtils.getString(map, "messageContent"); // 站内信内容

		// 当前节点
		String node = workFlow.getTaskDefKey();
		// 验证节点
		String nodeKey = nodeVerify(node);
		if (nodeKey == null) {
			return JsonUtils.toJson(RequestUtils.failResult("当前节点不存在请联系系统管理员！"));
		}

		// 业务id
		String businessKey = workFlow.getBusinessId();
		if (StringUtils.isEmpty(businessKey)) {
			return JsonUtils.toJson(RequestUtils.failResult("任务办理失败，没有业务id,请联系系统管理员。"));
		}

		// 处理结果
		String condition = request.getParameter(ActivityUtil.PROCDEFKEY + "_condition");
		Map<String,Object> mapResult = new HashMap<String, Object>();
		try {
			//人工准入验证 进行业务处理 及工作流处理
			String status = activitiSxdService.doAccessVerify(faRisk,workFlow, nodeKey, condition, map, isSendEmail, emailTitle, emailContent, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
			mapResult.put("result", status);
		} catch (Exception e) {
			logger.error("error:", e);
			return JsonUtils.toJson(RequestUtils.failResult("exception"));
		}
		return JsonUtils.toJson(RequestUtils.successResult(mapResult));
	}
	
	
	/**
	 * 准入验证成功通知节点
	 * 
	 * @param taskId
	 * @param request
	 * @param attr
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "task/access/service/phone/call", method = {RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String accessSendMsg(@ModelAttribute WorkFlow workFlow,HttpServletRequest request, @RequestParam Map<String, Object> map) {

		Long userId = RequestUtils.getLoginedUser().getUserId(); // 获取登录用户
		if (userId == null) {
			return JsonUtils.toJson(RequestUtils.failResult("请重新登录！"));
		}
		
		// 获取发消息的参数所需的参数
		String isSendEmail = MapUtils.getString(map, "emailCheck"); // 是否发送邮件（on:发送；）
		String emailTitle = MapUtils.getString(map, "emailTitle"); // 邮件模板标题
		String emailContent = MapUtils.getString(map, "emailContent"); // 邮件内容
		String isSendSms = MapUtils.getString(map, "smsCheck"); // 是否发送短信（on:发送；）
		String smsContent = MapUtils.getString(map, "smsContent"); // 短信内容
		String isSendMessage = MapUtils.getString(map, "messageCheck"); // 是否发送站内信（on:发送；）
		String messageTitle = MapUtils.getString(map, "messageTitle"); // 站内信标题
		String messageContent = MapUtils.getString(map, "messageContent"); // 站内信内容
		
		// 当前节点
		String node = workFlow.getTaskDefKey();
		// 验证节点
		String nodeKey = nodeVerify(node);
		if (nodeKey == null) {
			return JsonUtils.toJson(RequestUtils.failResult("当前节点不存在请联系系统管理员！"));
		}

		// 业务id
		String businessKey = workFlow.getBusinessId();
		if (StringUtils.isEmpty(businessKey)) {
			return JsonUtils.toJson(RequestUtils.failResult("任务办理失败，没有业务id,请联系系统管理员。"));
		}

		try {
//			activitiSxdService.accessSendMsg(map, workFlow, userId);
			activitiSxdService.accessSendMsg(map, workFlow, userId, isSendEmail, emailTitle, emailContent, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
		} catch (Exception e) {
			logger.error("error:", e);
			return JsonUtils.toJson(RequestUtils.failResult("exception"));
		}
		return JsonUtils.toJson(RequestUtils.successResult(""));
	}
	
	/**
	 * 人工打分节点办理
	 * @liutt
	 * @param taskId
	 * @param request
	 * @param attr
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "task/quota/review", method = {
			RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String taskQuotaReview(@ModelAttribute WorkFlow workFlow, HttpServletRequest request, @RequestParam Map<String, Object> map) {
		
		// 获取发消息的参数所需的参数
		String isSendEmail = MapUtils.getString(map, "emailCheck"); // 是否发送邮件（on:发送；）
		String emailTitle = MapUtils.getString(map, "emailTitle"); // 邮件模板标题
		String emailContent = MapUtils.getString(map, "emailContent"); // 邮件内容
		String isSendSms = MapUtils.getString(map, "smsCheck"); // 是否发送短信（on:发送；）
		String smsContent = MapUtils.getString(map, "smsContent"); // 短信内容
		String isSendMessage = MapUtils.getString(map, "messageCheck"); // 是否发送站内信（on:发送；）
		String messageTitle = MapUtils.getString(map, "messageTitle"); // 站内信标题
		String messageContent = MapUtils.getString(map, "messageContent"); // 站内信内容

		// 当前节点
		String node = workFlow.getTaskDefKey();
		// 验证节点
		String nodeKey = nodeVerify(node);
		if (nodeKey == null) {
			return JsonUtils.toJson(RequestUtils.failResult("当前节点不存在请联系系统管理员！"));
		}

		// 业务id
		String businessKey = workFlow.getBusinessId();
		if ("".equals(businessKey)) {
			return JsonUtils.toJson(RequestUtils.failResult("任务办理失败，没有业务id,请联系系统管理员。"));
		}

		// 处理结果
		String condition = request.getParameter(ActivityUtil.PROCDEFKEY + "_condition");

		try {
//			activitiSxdService.doQuotaReview(workFlow, nodeKey, condition, map);
			activitiSxdService.doQuotaReview(workFlow, nodeKey, condition, map, isSendEmail, emailTitle, emailContent, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
		} catch (Exception e) {
			logger.error("error:", e);
			return JsonUtils.toJson(RequestUtils.failResult("exception"));
		}
		return JsonUtils.toJson(RequestUtils.successResult(""));
	}
	
	
	/**
	 * 人工核准授信额度节点办理
	 * @liutt
	 * @param taskId
	 * @param request
	 * @param attr
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "task/quota/mranualCreditLimit", method = {
			RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String mranualCreditLimit(@ModelAttribute WorkFlow workFlow,
			HttpServletRequest request, @RequestParam Map<String, Object> map) {

		// 当前节点
		String node = workFlow.getTaskDefKey();
		// 验证节点
		String nodeKey = nodeVerify(node);
		if (nodeKey == null) {
			return JsonUtils.toJson(RequestUtils.failResult("当前节点不存在请联系系统管理员！"));
		}

		// 处理原因
		String reason = workFlow.getComment();
		reason = reason == null ? "" : reason;

		// 业务id
		String businessKey = workFlow.getBusinessId();
		if ("".equals(businessKey)) {
			return JsonUtils.toJson(RequestUtils
					.failResult("任务办理失败，没有业务id,请联系系统管理员。"));
		}

		// 处理结果
		String condition = request.getParameter(ActivityUtil.PROCDEFKEY + "_condition");
		if (!("true".equals(condition) || "false".equals(condition))) {
			return JsonUtils
					.toJson(RequestUtils.failResult("任务办理失败，请联系系统管理员。"));
		}
		String quota = MapUtils.getString(map, "loanNew_creditLimit","");
		//人工核准授信额度
		if ("true".equals(condition) && StringUtils.isEmpty(quota)) {
			return JsonUtils.toJson(RequestUtils.failResult("授信额度不能为空"));
		}

		try {
			activitiSxdService.mranualCreditLimit(workFlow, nodeKey, condition, quota);
		} catch (Exception e) {
			logger.error("error:", e);
			return JsonUtils.toJson(RequestUtils.failResult("exception"));
		}
		return JsonUtils.toJson(RequestUtils.successResult(""));
	}
	

	/**
	 * 审批节点
	 * @liutt
	 * @param taskId
	 * @param request
	 * @param attr
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "task/quota/xaminationApproval", method = {
			RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String xaminationApproval(@ModelAttribute WorkFlow workFlow,
			HttpServletRequest request, @RequestParam Map<String, Object> map) {

		// 当前节点
		String node = workFlow.getTaskDefKey();
		// 验证节点
		String nodeKey = nodeVerify(node);
		if (nodeKey == null) {
			return JsonUtils.toJson(RequestUtils.failResult("当前节点不存在请联系系统管理员！"));
		}

		// 业务id
		String businessKey = workFlow.getBusinessId();
		if ("".equals(businessKey)) {
			return JsonUtils.toJson(RequestUtils
					.failResult("任务办理失败，没有业务id,请联系系统管理员。"));
		}

		// 处理结果
		String condition = request.getParameter(ActivityUtil.PROCDEFKEY + "_condition");

		try {
			activitiSxdService.xaminationApproval(workFlow, nodeKey, condition);
		} catch (Exception e) {
			logger.error("error:", e);
			return JsonUtils.toJson(RequestUtils.failResult("exception"));
		}
		return JsonUtils.toJson(RequestUtils.successResult(""));
	}
	
	/**
	 * 经理审批节点
	 * @liutt
	 * @param taskId
	 * @param request
	 * @param attr
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "task/quota/quotaManagerApproval", method = {
			RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String quotaManagerApproval(@ModelAttribute WorkFlow workFlow,
			HttpServletRequest request, @RequestParam Map<String, Object> map) {

		// 当前节点
		String node = workFlow.getTaskDefKey();
		// 验证节点
		String nodeKey = nodeVerify(node);
		if (nodeKey == null) {
			return JsonUtils.toJson(RequestUtils.failResult("当前节点不存在请联系系统管理员！"));
		}

		// 业务id
		String businessKey = workFlow.getBusinessId();
		if ("".equals(businessKey)) {
			return JsonUtils.toJson(RequestUtils
					.failResult("任务办理失败，没有业务id,请联系系统管理员。"));
		}

		// 处理结果
		String condition = request.getParameter(ActivityUtil.PROCDEFKEY + "_condition");
		if (!("true".equals(condition) || "false".equals(condition))) {
			return JsonUtils
					.toJson(RequestUtils.failResult("任务办理失败，请联系系统管理员。"));
		}

		try {
			activitiSxdService.quotaManagerApproval(workFlow, nodeKey, condition);
		} catch (Exception e) {
			logger.error("error:", e);
			return JsonUtils.toJson(RequestUtils.failResult("exception"));
		}
		return JsonUtils.toJson(RequestUtils.successResult(""));
	}
	
	
	/**
	 * 额度申请发送客服通知，并且通知现场开户办理
	 * @liutt
	 * @param taskId
	 * @param request
	 * @param attr
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "task/quota/qutoaServiceCall", method = {RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String qutoaServiceCall(@ModelAttribute WorkFlow workFlow, HttpServletRequest request, @RequestParam Map<String, Object> map) {
		// 获取发消息的参数所需的参数
		String isSendEmail = MapUtils.getString(map, "emailCheck"); // 是否发送邮件（on:发送；）
		String emailTitle = MapUtils.getString(map, "emailTitle"); // 邮件模板标题
		String emailContent = MapUtils.getString(map, "emailContent"); // 邮件内容
		String isSendSms = MapUtils.getString(map, "smsCheck"); // 是否发送短信（on:发送；）
		String smsContent = MapUtils.getString(map, "smsContent"); // 短信内容
		String isSendMessage = MapUtils.getString(map, "messageCheck"); // 是否发送站内信（on:发送；）
		String messageTitle = MapUtils.getString(map, "messageTitle"); // 站内信标题
		String messageContent = MapUtils.getString(map, "messageContent"); // 站内信内容
		
		// 当前节点
		String node = workFlow.getTaskDefKey();
		// 验证节点
		String nodeKey = nodeVerify(node);
		if (nodeKey == null) {
			return JsonUtils.toJson(RequestUtils.failResult("当前节点不存在请联系系统管理员！"));
		}

		// 业务id
		String businessKey = workFlow.getBusinessId();
		if ("".equals(businessKey)) {
			return JsonUtils.toJson(RequestUtils.failResult("任务办理失败，没有业务id,请联系系统管理员。"));
		}

		//现场开户时间
		String fieldTime = MapUtils.getString(map,"loanNew_fieldTime","");
		if (StringUtils.isEmpty(fieldTime)) {
			return JsonUtils.toJson(RequestUtils.failResult("现场开户时间不能为空"));
		}
		//现场开户地址
		String fieldAddr = MapUtils.getString(map,"loanNew_fieldAddr","");
		
		if (StringUtils.isEmpty(fieldAddr)) {
			return JsonUtils.toJson(RequestUtils.failResult("现场开户地点不能为空"));
		}
		try {
			//额度申请成功发送通知 及工作流处理
			activitiSxdService.quotaSendMsg(fieldTime,fieldAddr, workFlow, isSendEmail, emailTitle, emailContent, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
		} catch (Exception e) {
			logger.error("error:", e);
			return JsonUtils.toJson(RequestUtils.failResult("exception"));
		}
		return JsonUtils.toJson(RequestUtils.successResult(""));
	}
	

	/**
	 * 现场分配任务办理
	 * @liutt
	 * @param taskId
	 * @param request
	 * @param attr
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "task/taskAssign", method = {RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String taskAssign(@ModelAttribute WorkFlow workFlow, HttpServletRequest request, @RequestParam Map<String, Object> map) {
		// 当前节点
		String node = workFlow.getTaskDefKey();
		// 验证节点
		String nodeKey = nodeVerify(node);
		if (nodeKey == null) {
			return JsonUtils.toJson(RequestUtils.failResult("当前节点不存在请联系系统管理员！"));
		}

		// 业务id
		String businessKey = workFlow.getBusinessId();
		if ("".equals(businessKey)) {
			return JsonUtils.toJson(RequestUtils.failResult("任务办理失败，没有业务id,请联系系统管理员。"));
		}
		String taskPerson = MapUtils.getString(map,"loanNew_taskPersons");
		try {
			activitiSxdService.taskAssign(workFlow,taskPerson);
		} catch (Exception e) {
			logger.error("error:", e);
			return JsonUtils.toJson(RequestUtils.failResult("exception"));
		}
		return JsonUtils.toJson(RequestUtils.successResult(""));
	}
	
	
	/**
	 * 开户信息回填
	 * @liutt
	 * @param taskId
	 * @param request
	 * @param attr
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "task/dataBackfill", method = {
			RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String dataBackfill(@ModelAttribute WorkFlow workFlow,
			HttpServletRequest request, @RequestParam Map<String, Object> map) {
		
		SysUser sysUser = (SysUser)RequestUtils.getLoginedUser();
		if (sysUser==null) {
			return JsonUtils.toJson("请重新登陆");
		}

		// 当前节点
		String node = workFlow.getTaskDefKey();
		// 验证节点
		String nodeKey = nodeVerify(node);
		if (nodeKey == null) {
			return JsonUtils.toJson(RequestUtils.failResult("当前节点不存在请联系系统管理员！"));
		}

		// 业务id
		String businessKey = workFlow.getBusinessId();
		if ("".equals(businessKey)) {
			return JsonUtils.toJson(RequestUtils
					.failResult("任务办理失败，没有业务id,请联系系统管理员。"));
		}
		
		//合同号
		String contractNum = MapUtils.getString(map, "loanNew_contractNum","");
		if (StringUtils.isEmpty(contractNum)) {
			return JsonUtils.toJson(RequestUtils.failResult("合同号不能为空！"));
		}
		//是否支付服务费
		String serviceChargeState = MapUtils.getString(map, "loanNew_serviceChargeState","");
		//是否开具发票
		String isInvoicing = MapUtils.getString(map,"loanNew_isInvoicing","");
		//实际收取服务费用
		String serviceChargeActual = MapUtils.getString(map, "loanNew_serviceChargeActual","");
		try {
			activitiSxdService.dataBackfill(workFlow,contractNum,serviceChargeState,isInvoicing,serviceChargeActual,sysUser.getUserName());
		} catch (Exception e) {
			logger.error("error:", e);
			return JsonUtils.toJson(RequestUtils.failResult("exception"));
		}
		return JsonUtils.toJson(RequestUtils.successResult(""));
	}
	
	/**
	 * 归档
	 * @liutt
	 * @param taskId
	 * @param request
	 * @param attr
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "task/businessArchive", method = {
			RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String businessArchive(@ModelAttribute WorkFlow workFlow,
			HttpServletRequest request, @RequestParam Map<String, Object> map) {
		// 当前节点
		String node = workFlow.getTaskDefKey();
		// 验证节点
		String nodeKey = nodeVerify(node);
		if (nodeKey == null) {
			return JsonUtils.toJson(RequestUtils.failResult("当前节点不存在请联系系统管理员！"));
		}

		//档案号
		String archive = MapUtils.getString(map,"loanNew_archive","");
		if (StringUtils.isEmpty(archive)) {
			return JsonUtils.toJson(RequestUtils.failResult("档案号不能为空"));
		}
		
		// 业务id
		String businessKey = workFlow.getBusinessId();
		if ("".equals(businessKey)) {
			return JsonUtils.toJson(RequestUtils
					.failResult("任务办理失败，没有业务id,请联系系统管理员。"));
		}

		try {
			activitiSxdService.businessArchive(workFlow,archive);
		} catch (Exception e) {
			logger.error("error:", e);
			return JsonUtils.toJson(RequestUtils.failResult("exception"));
		}
		return JsonUtils.toJson(RequestUtils.successResult(""));
	}
	
	
	
	/**
	 * 客服通知客户业务失败
	 * @liutt
	 * @param taskId
	 * @param request
	 * @param attr
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "task/sendFailMsg", method = {RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String sendFailMsg(@ModelAttribute WorkFlow workFlow, HttpServletRequest request, @RequestParam Map<String, Object> map) {
		// 获取发消息的参数所需的参数
		String isSendEmail = MapUtils.getString(map, "emailCheck"); // 是否发送邮件（on:发送；）
		String emailTitle = MapUtils.getString(map, "emailTitle"); // 邮件模板标题
		String emailContent = MapUtils.getString(map, "emailContent"); // 邮件内容
		String isSendSms = MapUtils.getString(map, "smsCheck"); // 是否发送短信（on:发送；）
		String smsContent = MapUtils.getString(map, "smsContent"); // 短信内容
		String isSendMessage = MapUtils.getString(map, "messageCheck"); // 是否发送站内信（on:发送；）
		String messageTitle = MapUtils.getString(map, "messageTitle"); // 站内信标题
		String messageContent = MapUtils.getString(map, "messageContent"); // 站内信内容

		// 当前节点
		String node = workFlow.getTaskDefKey();
		// 验证节点
		String nodeKey = nodeVerify(node);
		if (nodeKey == null) {
			return JsonUtils.toJson(RequestUtils.failResult("当前节点不存在请联系系统管理员！"));
		}

		// 业务id
		String businessKey = workFlow.getBusinessId();
		if ("".equals(businessKey)) {
			return JsonUtils.toJson(RequestUtils.failResult("任务办理失败，没有业务id,请联系系统管理员。"));
		}

		try {
			activitiSxdService.sendFailMsg(workFlow, map, isSendEmail, emailTitle, emailContent, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
		} catch (Exception e) {
			logger.error("error:", e);
			return JsonUtils.toJson(RequestUtils.failResult("exception"));
		}
		return JsonUtils.toJson(RequestUtils.successResult(""));
	}
	
	/**
	 * 转办
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/turnTodo", method = {GET, POST})
	@ResponseBody
	public String turnTodo(@RequestParam Map<String,Object> map,@ModelAttribute WorkFlow workFlow) {
		//Long userId = SecurityTokenHolder.getSecurityToken().getUser().getUserId();//获得当前登录用户
		SysUser sysUserLogin = (SysUser)RequestUtils.getLoginedUser();
		if (sysUserLogin==null) {
			return JsonUtils.toJson("请重新登陆");
		}
		
		String taskId = workFlow.getTaskId();
		long personId = MapUtils.getLong(map, "personId");
		String busId = workFlow.getBusinessId();
		SysUser sysUser = sysUserService.findByPk(personId);
		activitiSxdService.turnTodoTask(taskId, sysUser.getUserName());

		Task task = taskService.createTaskQuery().taskId(workFlow.getTaskId()).singleResult();
		faOperationRecordService.saveFaOperationRecordNew(busId, sysUserLogin.getUserId(), "转办给"+sysUser.getUserName(), task.getName(), OperationState.BUSINESS_SUCCESS,task.getProcessInstanceId(),taskId);
		return JsonUtils.toJson(RequestUtils.successResult(null));
	}
	
	
	/**
	 * 验证node是否有效
	 * 
	 * @param node
	 * @return
	 */
	private String nodeVerify(String node) {
		if (node == null || node.trim().length() == 0) {
			return null;
		}
		// 注入人工验证
		if (WorkFlowUtils.role_manual_verify.equals(node)) {
			return WorkFlowUtils.manualVerify;
		}
		// 准入验证客服通知
		if (WorkFlowUtils.role_service_phone_call.equals(node)) {
			return WorkFlowUtils.servicePhoneCall;
		}
		// 人工复核
		if (WorkFlowUtils.role_manual_review.equals(node)) {
			return WorkFlowUtils.manualReview;
		}
		// 核准授信额度
		if (WorkFlowUtils.role_approved_credit_limit.equals(node)) {
			return WorkFlowUtils.approvedCreditLimit;
		}
		// 客服电话通知额度申请结果及现场开户通知
		if (WorkFlowUtils.role_qutoa_service_call.equals(node)) {
			return WorkFlowUtils.qutoaServiceCall;
		}
		// 审批
		if (WorkFlowUtils.role_xamination_approval.equals(node)) {
			return WorkFlowUtils.xaminationApproval;
		}
		// 经理审批
		if (WorkFlowUtils.quota_manager_approval_role.equals(node)) {
			return WorkFlowUtils.quotaManagerApproval;
		}
		// 现场任务分配
		if (WorkFlowUtils.role_task_assign.equals(node)) {
			return WorkFlowUtils.taskAssign;
		}
		// 数据回填
		if (WorkFlowUtils.role_data_backfill.equals(node)) {
			return WorkFlowUtils.dataBackfill;
		}
		// 业务归档
		if (WorkFlowUtils.role_business_archive.equals(node)) {
			return WorkFlowUtils.businessArchive;
		}
		// 客服通知业务处理失败
		if (WorkFlowUtils.role_send_fail_msg.equals(node)) {
			return WorkFlowUtils.sendFailMsg;
		}

		return null;
	}

	/**
	 * 跳转到待办任务-放款页面
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getActivitiList/toGetLoanList", method = {RequestMethod.GET,POST})
	public String toGetLoanList(HttpServletRequest request,
			@RequestParam Map<String, Object> map) {

		return "activiti/taskLoan";
	}

	/**
	 * 查询待办任务-放款列表
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/doGetLoanList", method = { GET, POST })
	@ResponseBody
	public String doGetLoanList(HttpServletRequest request,
			@RequestParam Map<String, Object> map) {

		// 默认查询的是税信贷业务相关的工作流
		String userKey = RequestUtils.getLoginedUser().getUserName();// 当前登陆用户
		List<String> candidateGroups = RoleCacheLoader.getInstance().getRoles(
				userKey);// 用户权限组
		List<String> userNameList = FalseUserCacheLoader.getInstance()
				.getRoles(userKey);// 分配任务所用到的 假用户

		// 记录操作的次数 每次加1
		String sEcho = "0";

		// 起始纪录
		String iDisplayStart = "0";

		// 每页的条数
		String iDisplayLength = "10";

		// 搜索字符串
		String searchString = "";

		// 查询出来的总数量
		long count = 0;
		
		//下拉菜单--任务状态
		String suspensionState = "";

		// 获取jquery datatable当前配置参数
		String aoData = request.getParameter("aoData");
		JSONArray jsonArray = JSONArray.fromObject(aoData);
		for (int i = 0; i < jsonArray.size(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				if (jsonObject.get("name").equals("sEcho"))
					sEcho = jsonObject.get("value").toString();
				else if (jsonObject.get("name").equals("iDisplayStart"))
					iDisplayStart = jsonObject.get("value").toString();
				else if (jsonObject.get("name").equals("iDisplayLength"))
					iDisplayLength = jsonObject.get("value").toString();
				else if (jsonObject.get("name").equals("sSearch"))
					searchString = jsonObject.get("value").toString();
				else if (jsonObject.get("name").equals("suspensionState"))
					suspensionState = jsonObject.get("value").toString();
			} catch (Exception e) {
				break;
			}
		}
		searchString = searchString.trim();
		int startNo = Integer.parseInt(iDisplayStart);
		List<Map<String, Object>> list = activitiSxdService.findTodoTasksLoan(
				userKey, candidateGroups, userNameList,
				Integer.parseInt(iDisplayLength), startNo, searchString,suspensionState);
		count = activitiSxdService.findTodoTasksCountLoan(userKey, candidateGroups,
				userNameList,searchString,suspensionState);
		JSONArray jsonArray2 = new JSONArray();
		JSONObject jsonObject2 = new JSONObject();

		// 为操作次数加1
		int initEcho = Integer.parseInt(sEcho) + 1;
		SimpleDateFormat sFormat = new SimpleDateFormat(ConstantTool.DATA_FORMAT);
		for (Map<String, Object> activiti : list) {
			jsonObject2.put("comName", MapUtils.getString(activiti, "COM_NAME"));
			jsonObject2.put("comId", MapUtils.getString(activiti, "COM_ID"));
			Date date = null;
			try {
				date = sFormat.parse(MapUtils.getString(activiti, "CREATE_TIME_"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String createTime = sFormat.format(date);
			jsonObject2.put("createTime",createTime);// 任务创建时间
			/*jsonObject2.put("createTime",
					MapUtils.getString(activiti, "CREATE_TIME_"));// 任务创建时间
*/			jsonObject2.put("businessId", MapUtils.getString(activiti, "BUSINESS_ID"));
			jsonObject2.put("name", MapUtils.getString(activiti, "NAME_"));// 节点名称
			jsonObject2.put("loanId", MapUtils.getString(activiti, "LOAN_ID"));// 节点名称
			jsonObject2.put("procInsId",MapUtils.getString(activiti, "PROC_INST_ID_"));// 流程实例id
			jsonObject2.put("taskId", MapUtils.getString(activiti, "ID_"));
			jsonObject2.put("taskDefKey",MapUtils.getString(activiti, "TASK_DEF_KEY_"));
			jsonObject2.put("assignee",MapUtils.getString(activiti, "ASSIGNEE_"));
			jsonObject2.put("suspensionState",MapUtils.getString(activiti, "SUSPENSION_STATE_"));
			
			jsonArray2.add(jsonObject2);
		}

		String json = "{\"sEcho\":" + initEcho + ",\"iTotalRecords\":" + count
				+ ",\"iTotalDisplayRecords\":" + count + ",\"aaData\":"
				+ jsonArray2.toString() + "}";

		return json;
	}
	
	/**
	 * 跳转到待办任务-快速贷款页面
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getActivitiList/toGetQuickLoanList", method = {RequestMethod.GET,POST})
	public String toGetQuickLoanList(HttpServletRequest request,
			@RequestParam Map<String, Object> map) {

		return "activiti/taskQuickLoan";
	}

	/**
	 * 查询待办任务-快速贷款列表
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/doGetQuickLoanList", method = { GET, POST })
	@ResponseBody
	public String doGetQuickLoanList(HttpServletRequest request,
			@RequestParam Map<String, Object> map) {

		// 查询快速贷款列表
		String userKey = RequestUtils.getLoginedUser().getUserName();// 当前登陆用户
		List<String> candidateGroups = RoleCacheLoader.getInstance().getRoles(userKey);// 用户权限组
		List<String> userNameList = FalseUserCacheLoader.getInstance().getRoles(userKey);// 分配任务所用到的 假用户

		// 记录操作的次数 每次加1
		String sEcho = "0";

		// 起始纪录
		String iDisplayStart = "0";

		// 每页的条数
		String iDisplayLength = "10";

		// 搜索字符串
		String searchString = "";

		// 查询出来的总数量
		long count = 0;
		
		//下拉菜单--任务状态
		String suspensionState = "";
		

		// 获取jquery datatable当前配置参数
		String aoData = request.getParameter("aoData");
		JSONArray jsonArray = JSONArray.fromObject(aoData);
		for (int i = 0; i < jsonArray.size(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				if (jsonObject.get("name").equals("sEcho"))
					sEcho = jsonObject.get("value").toString();
				else if (jsonObject.get("name").equals("iDisplayStart"))
					iDisplayStart = jsonObject.get("value").toString();
				else if (jsonObject.get("name").equals("iDisplayLength"))
					iDisplayLength = jsonObject.get("value").toString();
				else if (jsonObject.get("name").equals("sSearch"))
					searchString = jsonObject.get("value").toString();
				else if (jsonObject.get("name").equals("suspensionState"))
					suspensionState = jsonObject.get("value").toString();
			} catch (Exception e) {
				break;
			}
		}
		searchString = searchString.trim();
		int startNo = Integer.parseInt(iDisplayStart);
		List<Map<String, Object>> list = activitiSxdService.findTodoTasksQuickloan(userKey, candidateGroups, userNameList,Integer.parseInt(iDisplayLength), startNo, searchString,suspensionState);
		count = activitiSxdService.findTodoTasksCountQuickloan(userKey, candidateGroups,userNameList, searchString,suspensionState);
		JSONArray jsonArray2 = new JSONArray();
		JSONObject jsonObject2 = new JSONObject();

		// 为操作次数加1
		int initEcho = Integer.parseInt(sEcho) + 1;
		SimpleDateFormat sFormat = new SimpleDateFormat(ConstantTool.DATA_FORMAT);
		for (Map<String, Object> activiti : list) {
			jsonObject2.put("comName", MapUtils.getString(activiti, "COM_NAME"));
			jsonObject2.put("tel", MapUtils.getString(activiti, "TEL"));
			jsonObject2.put("contactName", MapUtils.getString(activiti, "CONTACT_NAME"));
			jsonObject2.put("comId", MapUtils.getString(activiti, "COM_ID"));
			jsonObject2.put("createTime",MapUtils.getString(activiti, "CREATE_TIME"));// 任务创建时间
			jsonObject2.put("loanId", MapUtils.getString(activiti, "LOAN_ID"));
			jsonObject2.put("name", MapUtils.getString(activiti, "NAME_"));// 节点名称
			jsonObject2.put("procInsId",MapUtils.getString(activiti, "PROC_INST_ID_"));// 流程实例id
			jsonObject2.put("taskId", MapUtils.getString(activiti, "ID_"));
			jsonObject2.put("taskDefKey",MapUtils.getString(activiti, "TASK_DEF_KEY_"));
			jsonObject2.put("assignee",MapUtils.getString(activiti, "ASSIGNEE_"));
			jsonObject2.put("suspensionState",MapUtils.getString(activiti, "SUSPENSION_STATE_"));
			jsonArray2.add(jsonObject2);
			
		}

		String json = "{\"sEcho\":" + initEcho + ",\"iTotalRecords\":" + count
				+ ",\"iTotalDisplayRecords\":" + count + ",\"aaData\":"
				+ jsonArray2.toString() + "}";

		return json;
	}
	
	/**
	 * 快速贷款（luy）
	 * @param workFlow
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "task/doQuickLoan", method = {RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String doQuickLoan(@ModelAttribute WorkFlow workFlow, HttpServletRequest request, @RequestParam Map<String, Object> map) {

		// 业务id
		String businessKey = workFlow.getBusinessId();
		if (StringUtils.isEmpty(businessKey)) {
			return JsonUtils.toJson(RequestUtils.failResult("任务办理失败，没有业务id,请联系系统管理员。"));
		}

		// 获取登录用户id
		Long userId = RequestUtils.getLoginedUser().getUserId();
		String  comName = MapUtils.getString(map, "comName");
		try {
			activitiSxdService.doQuickLoan(workFlow, userId,comName);
		} catch (Exception e) {
			logger.error("error:", e);
			return JsonUtils.toJson(RequestUtils.failResult("exception"));
		}
		return JsonUtils.toJson(RequestUtils.successResult(""));
	}
	
	
	 /**
	  * @liutt
      * 放款 人工发送请求
      * @param id
      * @return
      */
    @RequestMapping(value = "/loan/task/personDone", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String personDone(@ModelAttribute WorkFlow workFlow, @RequestParam Map<String, Object> map1) {
    	//向中间数据表中插入数据 
		long loanId = MapUtils.getLong(map1, "loanId");
		try {
			boolean status = activitiSxdService.personDoLoanApply(loanId, workFlow.getTaskId());
			if (!status) {
				return JsonUtils.toJson(RequestUtils.failResult(getMessage("bank.loan.error")));
			}
			return JsonUtils.toJson(RequestUtils.successResult(""));
		} 
		catch (Exception e) {
			//放款失败
			logger.error("error:放款异常",e);
			//修改放款流程为state 01
			logger.error("loanapply------------------error",loanId);
			//fwLoanApplyService.updateLoanState(Long.parseLong(loanId),LoanApplyState.APPLYING);
			return JsonUtils.toJson(RequestUtils.failResult("exception"));
		}
	    
    }
    
    
    /**
	 * 根据快速申请贷款id查询快速贷款申请详情(liutt)
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getActivitiList/getQuickloanDetail",method = {GET, POST})
	public String getQucikLoanDetail(@RequestParam Map<String,Object> map,Model model){
		Long loanId = MapUtils.getLong(map, "loanId");
		FwQuickLoan fwQuickLoan  = fwQuickLoanService.findByPk(loanId);
		model.addAttribute("quickLoan", fwQuickLoan);
		return "quickloan/detail";
	}
    
    /**根据业务id 查询申请资料详情
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getActivitiList/getfwApplyBusDetail", method = GET)
	public String getfwApplyBusDetail(@RequestParam Map<String,Object> map,Model model) {
		String busId = MapUtils.getString(map, "busId");
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId);
		if (fwBusinessSxd==null) {
			return "error";
		}
		//查询账户信息
		Map<String, Object> fwComAccount = fwComAccountService.getUserMapByComId(fwBusinessSxd.getComId());
		//查询企业信息
		/*FwCompany fwCompany = fwCompanyService.findByPk(fwBusinessSxd.getComId());*/
		
		//查询法人信息
		FwComPerBus fwComPerBus = fwComPerBusService.findByBusinessIdAndType(fwBusinessSxd.getBsId(), BusinessType.SXD, PersonType.COM_LEGAL);
		//查询 控制人及配偶信息
		FwComPerBus fwComPerBus2 = fwComPerBusService.findByBusinessIdAndType(fwBusinessSxd.getBsId(), BusinessType.SXD, PersonType.COM_CONTROLLER);
		//TODO 查询补充材料
		
		model.addAttribute("fwComAccount", fwComAccount);
		/*model.addAttribute("fwCompany", fwCompany);*/
		model.addAttribute("fwBusinessSxd", fwBusinessSxd);
		model.addAttribute("fwComPerBus", fwComPerBus);
		model.addAttribute("controller", fwComPerBus2);
		List<FwFiles> list  = fwFilesService.findByBusIdAndBusType(busId, BusinessType.SXD);
		List<FwFiles> businessLicense =  new ArrayList<FwFiles>();//营业执照
		List<FwFiles> organiztionCode =  new ArrayList<FwFiles>();//组织机构代码证
		List<FwFiles> taxRegistartion =  new ArrayList<FwFiles>();//税务登记证
		List<FwFiles> lastYearStatements =  new ArrayList<FwFiles>();// 上一年财务报表
		List<FwFiles> taxRelatedQuery =  new ArrayList<FwFiles>();//涉税保密信息查询申请表
		List<FwFiles> companyCredit =  new ArrayList<FwFiles>();//征信查询授权书
		List<FwFiles> ledalCredit =  new ArrayList<FwFiles>();//法人征信查询授权书
		List<FwFiles> controllerCredit =  new ArrayList<FwFiles>();//控制人及配偶征信查询授权书
		List<FwFiles> ledalCard =  new ArrayList<FwFiles>();//法人证件照片
		List<FwFiles> controllerCard =  new ArrayList<FwFiles>(); //控制人身份证照片
		List<FwFiles> controllerSpoCard =  new ArrayList<FwFiles>();//控制人配偶身份证照片
		List<FwFiles> controllerEducation = new ArrayList<FwFiles>(); //学历证明
		List<FwFiles> controllerDomicilePlace = new ArrayList<FwFiles>(); //户籍证明
		List<FwFiles> purchaseSaleContract =  new ArrayList<FwFiles>();//购销合同
		List<FwFiles> customerList =  new ArrayList<FwFiles>();//上下游客户清单
		List<FwFiles> lastTaxStatements =  new ArrayList<FwFiles>();//税务报表
		List<FwFiles> lastControllerStatements =  new ArrayList<FwFiles>();//个人银行对账单
		List<FwFiles> lastCompanyStatements =  new ArrayList<FwFiles>();//银行对账单
		List<FwFiles> lastTaxCerifcate =  new ArrayList<FwFiles>();//纳税凭证
		List<FwFiles> lastPaymentVoucher =  new ArrayList<FwFiles>();//缴费凭证
		List<FwFiles> creditTechnology =  new ArrayList<FwFiles>();//资质和技术产品证书
		List<FwFiles> propertyRightCard =  new ArrayList<FwFiles>();//场地证明
		List<FwFiles> creditBusinessApp =  new ArrayList<FwFiles>();//业务查询授权书
		List<FwFiles> directorsBoard =  new ArrayList<FwFiles>();//股东会或董事会决议
		List<FwFiles> specicalIndustryCre =  new ArrayList<FwFiles>();//资质证书或经营许可证
		List<FwFiles> capitalVerification =  new ArrayList<FwFiles>();//公司章程活验资报告
		List<FwFiles> controllerAssetsLiaApply =  new ArrayList<FwFiles>();//资产负债申请表
		List<FwFiles> marriageLicense =  new ArrayList<FwFiles>();//婚姻状况证明
		List<FwFiles> controllerSpouseGuarantee =  new ArrayList<FwFiles>();//配偶的担保承诺书
		List<FwFiles> enterpriseCreditRating =  new ArrayList<FwFiles>();//企业纳税信用等级证明
		List<FwFiles> companyProfile =  new ArrayList<FwFiles>();//公司简介
		List<FwFiles> theIndividualIncomeTax =  new ArrayList<FwFiles>();//实际控制连续两年缴纳个人所得税记录
		List<FwFiles> lastYearThidYearAnnualLandTaxCertificate =  new ArrayList<FwFiles>();//上年度、本年度全年的国地税纳税证明
		List<FwFiles> confirmation =  new ArrayList<FwFiles>();//实际控制人及配偶的担保确认书
		List<FwFiles> housePropertyCard =  new ArrayList<FwFiles>();//房产证明
		List<FwFiles> enterprisePayTaxesThrough =  new ArrayList<FwFiles>();//纳税查询授权书
		List<FwFiles> theLoanCardCopy =  new ArrayList<FwFiles>();//贷款卡复印件
		List<FwFiles> householdAssets =  new ArrayList<FwFiles>();//净资产证明householdAssets
		
		for (FwFiles fwFiles : list) {
			//营业执照
			if (FileType.BUSINESS_LICENSE.equals(fwFiles.getFileType())) {
				businessLicense.add(fwFiles);
			}
			//组织机构代码证
			if (FileType.ORGANIZTION_CODE_CERTIFICATE.equals(fwFiles.getFileType())) {
				organiztionCode.add(fwFiles);
			}
			//税务登记证
			if (FileType.TAX_REGISTRATION_CERTIFICATE.equals(fwFiles.getFileType())) {
				taxRegistartion.add(fwFiles);
			}
			// 上一年财务报表
			if (FileType.LAST_YEAR_FINANCIAL_STATEMENTS.equals(fwFiles.getFileType())) {
				lastYearStatements.add(fwFiles);
			}
			//涉税保密信息查询申请表
			if (FileType.TAX_RELATED_CONFIDENTIAL_QUERY.equals(fwFiles.getFileType())) {
				taxRelatedQuery.add(fwFiles);
			}
			//企业征信查询授权书
			if (FileType.COMPANY_CREDIT_REPORT_AUTHORIZATION.equals(fwFiles.getFileType())) {
				companyCredit.add(fwFiles);
			}
			//法人征信查询授权书
			if (FileType.LEGAL_COMPANY_CREDIT_REPORT_AUTHORIZATION.equals(fwFiles.getFileType())) {
				ledalCredit.add(fwFiles);
			}
			//法人身份证
			if (FileType.LEDAL_IDENTITY_CARD.equals(fwFiles.getFileType())) {
				ledalCard.add(fwFiles);
			}
			//控制人征信查询授权书
			if (FileType.CONTROLLER_CREDIT_REPORT_AUTHORIZATION.equals(fwFiles.getFileType())) {
				controllerCredit.add(fwFiles);
			}
			//控制人身份证照片
			if (FileType.CONTROLLER_IDENTITY_CARD.equals(fwFiles.getFileType())) {
				controllerCard.add(fwFiles);
			}
			//控制人配偶身份证照片
			if (FileType.CONTROLLER_SPO_IDENTITY_CARD.equals(fwFiles.getFileType())) {
				controllerSpoCard.add(fwFiles);
			}
			//学历证明
			if (FileType.EDUCATION.equals(fwFiles.getFileType())) {
				controllerEducation.add(fwFiles);
			}
			//户籍证明
			if (FileType.DOMICILE_PLACE.equals(fwFiles.getFileType())) {
				controllerDomicilePlace.add(fwFiles);
			}
			//购销合同
			if (FileType.PURCHASE_SALE_CONTRACT.equals(fwFiles.getFileType())) {
				purchaseSaleContract.add(fwFiles);
			}
			//客服清单
			if (FileType.CUSTOMER_LIST.equals(fwFiles.getFileType())) {
				customerList.add(fwFiles);
			}
			//税务报表
			if (FileType.LAST_YEAR_TAX_STATEMENTS.equals(fwFiles.getFileType())) {
				lastTaxStatements.add(fwFiles);
			}
			//银行对账单
			if (FileType.LAST_THREE_MONTHS_STATEMENTS.equals(fwFiles.getFileType())) {
				lastCompanyStatements.add(fwFiles);
			}
			//个人银行对账单
			if (FileType.LAST_THREE_MONTHS_CONTROLLER_STATEMENTS.equals(fwFiles.getFileType())) {
				lastControllerStatements.add(fwFiles);
			}
			//纳税凭证和申请表
			if (FileType.LAST_YEAR_TAX_PAYMENT_CERTIFICATE.equals(fwFiles.getFileType())) {
				lastTaxCerifcate.add(fwFiles);
			}
			//缴费凭证
			if (FileType.LAST_UTILITIES_PAYMENT_VOUCHER.equals(fwFiles.getFileType())) {
				lastPaymentVoucher.add(fwFiles);
			}
			//资信和技术产品证书
			if (FileType.CREDIT_TECHNOLOGY_CERTIFICATION.equals(fwFiles.getFileType())) {
				creditTechnology.add(fwFiles);
			}
			//场地证明
			if (FileType.PROPERTY_RIGHT_CARD.equals(fwFiles.getFileType())) {
				propertyRightCard.add(fwFiles);
			}
			//授信业务申请书
			if (FileType.CREDIT_BUSINESS_APPLICATION.equals(fwFiles.getFileType())) {
				creditBusinessApp.add(fwFiles);
			}
			//股东会或董事会决议
			if (FileType.DIRECTORS_BOARD.equals(fwFiles.getFileType())) {
				directorsBoard.add(fwFiles);
			}
			//资质证书活经营许可证
			if (FileType.SPECIAL_INDUSTRY_CERTIFICATE.equals(fwFiles.getFileType())) {
				specicalIndustryCre.add(fwFiles);
			}
			//公司章程验资报告
			if (FileType.COMPANY_ARTICLES.equals(fwFiles.getFileType())) {
				capitalVerification.add(fwFiles);
			}
			//资产负债申请表
			if (FileType.CONTROLLER_ASSETS_LIABILITIES_APPLY.equals(fwFiles.getFileType())) {
				controllerAssetsLiaApply.add(fwFiles);
			}
			//婚姻状况证明
			if (FileType.MARRIAGE_LICENSE.equals(fwFiles.getFileType())) {
				marriageLicense.add(fwFiles);
			}
			//配偶的担保承诺书
			if (FileType.CONTROLLER_SPOUSE_GUARANTEE.equals(fwFiles.getFileType())) {
				controllerSpouseGuarantee.add(fwFiles);
			}
			//企业纳税信用等级证明
			if (FileType.ENTERPRISE_CREDIT_RATING.equals(fwFiles.getFileType())) {
				enterpriseCreditRating.add(fwFiles);
			}
			//公司简介
			if (FileType.COMPANY_PROFILE.equals(fwFiles.getFileType())) {
				companyProfile.add(fwFiles);
			}
			//实际控制连续两年缴纳个人所得税记录
			if (FileType.THE_INDIVIDUAL_INCOME_TAX.equals(fwFiles.getFileType())) {
				theIndividualIncomeTax.add(fwFiles);
			}
			//上年度、本年度全年的国地税纳税证明
			if (FileType.LAST_YEAR_THIS_YEAR_ANNUAL_LAND_TAX_CERTIFICATE.equals(fwFiles.getFileType())) {
				lastYearThidYearAnnualLandTaxCertificate.add(fwFiles);
			}
			//实际控制人及配偶的担保确认书
			if (FileType.CONFIRMATION.equals(fwFiles.getFileType())) {
				confirmation.add(fwFiles);
			}
			//房产证明
			if (FileType.HOUSE_PROPERTY_CARD.equals(fwFiles.getFileType())) {
				housePropertyCard.add(fwFiles);
			}
			//纳税查询授权书
			if (FileType.ENTERPRISE_PAY_TAXES_THROUGH_THE_DATA_QUERY_AUTHORIZATION.equals(fwFiles.getFileType())) {
				enterprisePayTaxesThrough.add(fwFiles);
			}
			//贷款卡复印件
			if (FileType.THE_LOAN_CARD_COPY.equals(fwFiles.getFileType())) {
				theLoanCardCopy.add(fwFiles);
			}
			//净资产证明householdAssets
			if (FileType.HOUSEHOLD_ASSETS.equals(fwFiles.getFileType())) {
				householdAssets.add(fwFiles);
			}
			
		}
		
		
		
		
		model.addAttribute("businessLicense", businessLicense);//营业执照
		model.addAttribute("organiztionCode", organiztionCode);//组织机构代码证
		model.addAttribute("taxRegistartion", taxRegistartion);//税务登记证
		model.addAttribute("lastYearStatements", lastYearStatements);// 上一年财务报表
		model.addAttribute("taxRelatedQuery", taxRelatedQuery);//涉税保密信息查询申请表
		model.addAttribute("companyCredit", companyCredit);//征信查询授权书
		model.addAttribute("ledalCredit", ledalCredit);//法人征信查询授权书
		model.addAttribute("ledalCard", ledalCard);//法人证件照片
		model.addAttribute("controllerCredit", controllerCredit);//控制人及配偶征信查询授权书
		model.addAttribute("controllerCard", controllerCard);//控制人身份证照片
		model.addAttribute("controllerSpoCard", controllerSpoCard);//控制人配偶身份证照片
		model.addAttribute("controllerEducation", controllerEducation);//学历证明
		model.addAttribute("controllerDomicilePlace", controllerDomicilePlace);//户籍证明
		model.addAttribute("purchaseSaleContract", purchaseSaleContract);////购销合同
		model.addAttribute("customerList", customerList);//上下游客户清单
		model.addAttribute("lastTaxStatements", lastTaxStatements);//税务报表
		model.addAttribute("lastControllerStatements", lastControllerStatements);//个人银行对账单
		model.addAttribute("lastCompanyStatements", lastCompanyStatements);//银行对账单
		model.addAttribute("lastTaxCerifcate", lastTaxCerifcate);//纳税凭证
		model.addAttribute("lastPaymentVoucher", lastPaymentVoucher);//缴费凭证
		model.addAttribute("creditTechnology", creditTechnology);//资质和技术产品证书
		model.addAttribute("propertyRightCard", propertyRightCard);//场地证明
		model.addAttribute("creditBusinessApp", creditBusinessApp);//授信业务申请书
		model.addAttribute("directorsBoard", directorsBoard);//股东会或董事会决议
		model.addAttribute("specicalIndustryCre", specicalIndustryCre);//资质证书或经营许可证
		model.addAttribute("capitalVerification", capitalVerification);//公司章程或验资报告
		model.addAttribute("controllerAssetsLiaApply", controllerAssetsLiaApply);//资产负债申请表
		model.addAttribute("marriageLicense", marriageLicense);//婚姻状况证明
		model.addAttribute("controllerSpouseGuarantee", controllerSpouseGuarantee);//配偶的担保承诺书
		model.addAttribute("enterpriseCreditRating", enterpriseCreditRating);//企业纳税信用等级证明
		model.addAttribute("companyProfile", companyProfile);//公司简介
		model.addAttribute("theIndividualIncomeTax", theIndividualIncomeTax);//实际控制连续两年缴纳个人所得税记录
		model.addAttribute("lastYearThidYearAnnualLandTaxCertificate", lastYearThidYearAnnualLandTaxCertificate);//上年度、本年度全年的国地税纳税证明
		model.addAttribute("confirmation", confirmation);//实际控制人及配偶的担保确认书
		model.addAttribute("housePropertyCard", housePropertyCard);//房产证明
		model.addAttribute("enterprisePayTaxesThrough", enterprisePayTaxesThrough);//纳税查询授权书
		model.addAttribute("theLoanCardCopy", theLoanCardCopy);//贷款卡复印件
		model.addAttribute("householdAssets", householdAssets);//净资产证明householdAssets
		
		
		// 0：不合格；1：合格
		String licenseNum_ispass = "1";// 营业执照注册号
		String taxNum_ispass = "1";//税号
		String catdCode_ispass = "1";//组织机构代码证代码 
		
		String businessLicense_ispass = "1";//营业执照
		String organiztionCode_ispass = "1";//组织机构代码证
		String taxRegistartion_ispass = "1";//税务登记证
		String lastYearStatements_ispass = "1";// 上一年财务报表
		String taxRelatedQuery_ispass = "1";//涉税保密信息查询申请表
		String companyCredit_ispass = "1";//征信查询授权书
		String ledalCredit_ispass = "1";//法人征信查询授权书
		String ledalCard_ispass = "1";//法人证件照片
		String controllerCredit_ispass = "1";//控制人征信查询授权书
		String controllerCard_ispass = "1";//控制人身份证照片
		String controllerSpoCard_ispass = "1";//控制人配偶身份证照片
		String controllerEducation_ispass = "1";//学历证明
		String controllerDomicilePlace_ispass = "1";//户籍证明
		String purchaseSaleContract_ispass = "1";////购销合同
		String customerList_ispass = "1";//上下游客户清单
		String lastTaxStatements_ispass = "1";//税务报表
		String lastControllerStatements_ispass = "1";//个人银行对账单
		String lastCompanyStatements_ispass = "1";//银行对账单
		String lastTaxCerifcate_ispass = "1";//纳税凭证
		String lastPaymentVoucher_ispass = "1";//缴费凭证
		String creditTechnology_ispass = "1";//资质和技术产品证书
		String propertyRightCard_ispass = "1";//场地证明
		String creditBusinessApp_ispass = "1";//授信业务申请书
		String directorsBoard_ispass = "1";//股东会或董事会决议
		String specicalIndustryCre_ispass = "1";//资质证书或经营许可证
		String capitalVerification_ispass = "1";//公司章程或验资报告
		String controllerAssetsLiaApply_ispass = "1";//资产负债申请表
		String marriageLicense_ispass = "1";//婚姻状况证明
		String controllerSpouseGuarantee_ispass = "1";//配偶的担保承诺书
		String enterpriseCreditRating_ispass = "1";//企业纳税信用等级证明
		String companyProfile_ispass = "1";//公司简介
		String theIndividualIncomeTax_ispass = "1";//实际控制连续两年缴纳个人所得税记录
		String lastYearThidYearAnnualLandTaxCertificate_ispass = "1";//上年度、本年度全年的国地税纳税证明
		String confirmation_ispass = "1";//实际控制人及配偶的担保确认书
		String housePropertyCard_ispass = "1";//房产证明
		String enterprisePayTaxesThrough_ispass = "1";//纳税查询授权书
		String theLoanCardCopy_ispass = "1";//贷款卡复印件
		String householdAssets_ispass = "1";//净资产证明householdAssets
		

		List<FaInfoNopass> faInfoNopasses = faInfoNopassService.findInfoNopassesByBusId(busId, BusinessType.SXD);
		for (FaInfoNopass faInfoNopass : faInfoNopasses) {
			// 营业执照注册号
			if (NoPass.LICENSE_NUM.equals(faInfoNopass.getInfoKey())) {
				licenseNum_ispass = "0";
			}
			// 税号
			if (NoPass.TAX_NUM.equals(faInfoNopass.getInfoKey())) {
				taxNum_ispass = "0";
			}
			// 组织机构代码证代码 
			if (NoPass.CARD_CODE.equals(faInfoNopass.getInfoKey())) {
				catdCode_ispass = "0";
			}
			//营业执照
			if (FileType.BUSINESS_LICENSE.equals(faInfoNopass.getInfoKey())) {
				businessLicense_ispass = "0";
			}
			//组织机构代码证
			if (FileType.ORGANIZTION_CODE_CERTIFICATE.equals(faInfoNopass.getInfoKey())) {
				organiztionCode_ispass = "0";
			}
			//税务登记证
			if (FileType.TAX_REGISTRATION_CERTIFICATE.equals(faInfoNopass.getInfoKey())) {
				taxRegistartion_ispass = "0";
			}
			// 上一年财务报表
			if (FileType.LAST_YEAR_FINANCIAL_STATEMENTS.equals(faInfoNopass.getInfoKey())) {
				lastYearStatements_ispass = "0";
			}
			//涉税保密信息查询申请表
			if (FileType.TAX_RELATED_CONFIDENTIAL_QUERY.equals(faInfoNopass.getInfoKey())) {
				taxRelatedQuery_ispass = "0";
			}
			//企业征信查询授权书
			if (FileType.COMPANY_CREDIT_REPORT_AUTHORIZATION.equals(faInfoNopass.getInfoKey())) {
				companyCredit_ispass = "0";
			}
			//法人征信查询授权书
			if (FileType.LEGAL_COMPANY_CREDIT_REPORT_AUTHORIZATION.equals(faInfoNopass.getInfoKey())) {
				ledalCredit_ispass = "0";
			}
			//法人身份证
			if (FileType.LEDAL_IDENTITY_CARD.equals(faInfoNopass.getInfoKey())) {
				ledalCard_ispass = "0";
			}
			//控制人征信查询授权书
			if (FileType.CONTROLLER_CREDIT_REPORT_AUTHORIZATION.equals(faInfoNopass.getInfoKey())) {
				controllerCredit_ispass = "0";
			}
			//控制人身份证照片
			if (FileType.CONTROLLER_IDENTITY_CARD.equals(faInfoNopass.getInfoKey())) {
				controllerCard_ispass = "0";
			}
			//控制人配偶身份证照片
			if (FileType.CONTROLLER_SPO_IDENTITY_CARD.equals(faInfoNopass.getInfoKey())) {
				controllerSpoCard_ispass = "0";
			}
			//学历证明
			if (FileType.EDUCATION.equals(faInfoNopass.getInfoKey())) {
				controllerEducation_ispass = "0";
			}
			//户籍证明
			if (FileType.DOMICILE_PLACE.equals(faInfoNopass.getInfoKey())) {
				controllerDomicilePlace_ispass = "0";
			}
			//购销合同
			if (FileType.PURCHASE_SALE_CONTRACT.equals(faInfoNopass.getInfoKey())) {
				purchaseSaleContract_ispass = "0";
			}
			//客服清单
			if (FileType.CUSTOMER_LIST.equals(faInfoNopass.getInfoKey())) {
				customerList_ispass = "0";
			}
			//税务报表
			if (FileType.LAST_YEAR_TAX_STATEMENTS.equals(faInfoNopass.getInfoKey())) {
				lastTaxStatements_ispass = "0";
			}
			//银行对账单
			if (FileType.LAST_THREE_MONTHS_STATEMENTS.equals(faInfoNopass.getInfoKey())) {
				lastCompanyStatements_ispass = "0";
			}
			//个人银行对账单
			if (FileType.LAST_THREE_MONTHS_CONTROLLER_STATEMENTS.equals(faInfoNopass.getInfoKey())) {
				lastControllerStatements_ispass = "0";
			}
			//纳税凭证和申请表
			if (FileType.LAST_YEAR_TAX_PAYMENT_CERTIFICATE.equals(faInfoNopass.getInfoKey())) {
				lastTaxCerifcate_ispass = "0";
			}
			//缴费凭证
			if (FileType.LAST_UTILITIES_PAYMENT_VOUCHER.equals(faInfoNopass.getInfoKey())) {
				lastPaymentVoucher_ispass = "0";
			}
			//资信和技术产品证书
			if (FileType.CREDIT_TECHNOLOGY_CERTIFICATION.equals(faInfoNopass.getInfoKey())) {
				creditTechnology_ispass = "0";
			}
			//场地证明
			if (FileType.PROPERTY_RIGHT_CARD.equals(faInfoNopass.getInfoKey())) {
				propertyRightCard_ispass = "0";
			}
			//授信业务申请书
			if (FileType.CREDIT_BUSINESS_APPLICATION.equals(faInfoNopass.getInfoKey())) {
				creditBusinessApp_ispass = "0";
			}
			//股东会或董事会决议
			if (FileType.DIRECTORS_BOARD.equals(faInfoNopass.getInfoKey())) {
				directorsBoard_ispass = "0";
			}
			//资质证书活经营许可证
			if (FileType.SPECIAL_INDUSTRY_CERTIFICATE.equals(faInfoNopass.getInfoKey())) {
				specicalIndustryCre_ispass = "0";
			}
			//公司章程验资报告
			if (FileType.COMPANY_ARTICLES.equals(faInfoNopass.getInfoKey())) {
				capitalVerification_ispass = "0";
			}
			//资产负债申请表
			if (FileType.CONTROLLER_ASSETS_LIABILITIES_APPLY.equals(faInfoNopass.getInfoKey())) {
				controllerAssetsLiaApply_ispass = "0";
			}
			//婚姻状况证明
			if (FileType.MARRIAGE_LICENSE.equals(faInfoNopass.getInfoKey())) {
				marriageLicense_ispass = "0";
			}
			//配偶的担保承诺书
			if (FileType.CONTROLLER_SPOUSE_GUARANTEE.equals(faInfoNopass.getInfoKey())) {
				controllerSpouseGuarantee_ispass = "0";
			}
			//企业纳税信用等级证明
			if (FileType.ENTERPRISE_CREDIT_RATING.equals(faInfoNopass.getInfoKey())) {
				enterpriseCreditRating_ispass = "0";
			}
			//公司简介
			if (FileType.COMPANY_PROFILE.equals(faInfoNopass.getInfoKey())) {
				companyProfile_ispass = "0";
			}
			//实际控制连续两年缴纳个人所得税记录
			if (FileType.THE_INDIVIDUAL_INCOME_TAX.equals(faInfoNopass.getInfoKey())) {
				theIndividualIncomeTax_ispass = "0";
			}
			//上年度、本年度全年的国地税纳税证明
			if (FileType.LAST_YEAR_THIS_YEAR_ANNUAL_LAND_TAX_CERTIFICATE.equals(faInfoNopass.getInfoKey())) {
				lastYearThidYearAnnualLandTaxCertificate_ispass = "0";
			}
			//实际控制人及配偶的担保确认书
			if (FileType.CONFIRMATION.equals(faInfoNopass.getInfoKey())) {
				confirmation_ispass = "0";
			}
			//房产证明
			if (FileType.HOUSE_PROPERTY_CARD.equals(faInfoNopass.getInfoKey())) {
				housePropertyCard_ispass = "0";
			}
			//纳税查询授权书
			if (FileType.ENTERPRISE_PAY_TAXES_THROUGH_THE_DATA_QUERY_AUTHORIZATION.equals(faInfoNopass.getInfoKey())) {
				enterprisePayTaxesThrough_ispass = "0";
			}
			//贷款卡复印件
			if (FileType.THE_LOAN_CARD_COPY.equals(faInfoNopass.getInfoKey())) {
				theLoanCardCopy_ispass = "0";
			}
			//净资产证明householdAssets
			if (FileType.HOUSEHOLD_ASSETS.equals(faInfoNopass.getInfoKey())) {
				householdAssets_ispass = "0";
			}
			
		}
		
		model.addAttribute("licenseNum_ispass", licenseNum_ispass);//营业执照注册号
		model.addAttribute("taxNum_ispass", taxNum_ispass);//税号
		model.addAttribute("catdCode_ispass", catdCode_ispass);//组织机构代码证代码 
		
		model.addAttribute("businessLicense_ispass", businessLicense_ispass);//营业执照
		model.addAttribute("organiztionCode_ispass", organiztionCode_ispass);//组织机构代码证
		model.addAttribute("taxRegistartion_ispass", taxRegistartion_ispass);//税务登记证
		model.addAttribute("lastYearStatements_ispass", lastYearStatements_ispass);// 上一年财务报表
		model.addAttribute("taxRelatedQuery_ispass", taxRelatedQuery_ispass);//涉税保密信息查询申请表
		model.addAttribute("companyCredit_ispass", companyCredit_ispass);//征信查询授权书
		model.addAttribute("ledalCredit_ispass", ledalCredit_ispass);//法人征信查询授权书
		model.addAttribute("ledalCard_ispass", ledalCard_ispass);//法人证件照片
		model.addAttribute("controllerCredit_ispass", controllerCredit_ispass);//控制人及配偶征信查询授权书
		model.addAttribute("controllerCard_ispass", controllerCard_ispass);//控制人身份证照片
		model.addAttribute("controllerSpoCard_ispass", controllerSpoCard_ispass);//控制人配偶身份证照片
		model.addAttribute("controllerEducation_ispass", controllerEducation_ispass);//学历证明
		model.addAttribute("controllerDomicilePlace_ispass", controllerDomicilePlace_ispass);//户籍证明
		model.addAttribute("purchaseSaleContract_ispass", purchaseSaleContract_ispass);////购销合同
		model.addAttribute("customerList_ispass", customerList_ispass);//上下游客户清单
		model.addAttribute("lastTaxStatements_ispass", lastTaxStatements_ispass);//税务报表
		model.addAttribute("lastControllerStatements_ispass", lastControllerStatements_ispass);//个人银行对账单
		model.addAttribute("lastCompanyStatements_ispass", lastCompanyStatements_ispass);//银行对账单
		model.addAttribute("lastTaxCerifcate_ispass", lastTaxCerifcate_ispass);//纳税凭证
		model.addAttribute("lastPaymentVoucher_ispass", lastPaymentVoucher_ispass);//缴费凭证
		model.addAttribute("creditTechnology_ispass", creditTechnology_ispass);//资质和技术产品证书
		model.addAttribute("propertyRightCard_ispass", propertyRightCard_ispass);//场地证明
		model.addAttribute("creditBusinessApp_ispass", creditBusinessApp_ispass);//授信业务申请书
		model.addAttribute("directorsBoard_ispass", directorsBoard_ispass);//股东会或董事会决议
		model.addAttribute("specicalIndustryCre_ispass", specicalIndustryCre_ispass);//资质证书或经营许可证
		model.addAttribute("capitalVerification_ispass", capitalVerification_ispass);//公司章程或验资报告
		model.addAttribute("controllerAssetsLiaApply_ispass", controllerAssetsLiaApply_ispass);//资产负债申请表
		model.addAttribute("marriageLicense_ispass", marriageLicense_ispass);//婚姻状况证明
		model.addAttribute("controllerSpouseGuarantee_ispass", controllerSpouseGuarantee_ispass);//配偶的担保承诺书
		model.addAttribute("enterpriseCreditRating_ispass", enterpriseCreditRating_ispass);//企业纳税信用等级证明
		model.addAttribute("companyProfile_ispass", companyProfile_ispass);//公司简介
		model.addAttribute("theIndividualIncomeTax_ispass", theIndividualIncomeTax_ispass);//实际控制连续两年缴纳个人所得税记录
		model.addAttribute("lastYearThidYearAnnualLandTaxCertificate_ispass", lastYearThidYearAnnualLandTaxCertificate_ispass);//上年度、本年度全年的国地税纳税证明
		model.addAttribute("confirmation_ispass", confirmation_ispass);//实际控制人及配偶的担保确认书
		model.addAttribute("housePropertyCard_ispass", housePropertyCard_ispass);//房产证明
		model.addAttribute("enterprisePayTaxesThrough_ispass", enterprisePayTaxesThrough_ispass);//纳税查询授权书
		model.addAttribute("theLoanCardCopy_ispass", theLoanCardCopy_ispass);//贷款卡复印件
		model.addAttribute("householdAssets_ispass", householdAssets_ispass);//净资产证明householdAssets

        /*if (!StringUtils.isEmpty(addr)) {
			model.addAttribute("addr", addr);//配偶的担保承诺书
		}*/
		return "task/applyDetail";
	}
	
	/**
	 * 保存风控数据(luy)
	 * @param workFlow
	 * @param faRisk
	 * @return
	 */
	@RequestMapping(value = "doSaveRisk", method = {RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String doSaveRisk(@ModelAttribute WorkFlow workFlow, @ModelAttribute FaRisk faRisk) {
		FaRisk retFaRisk = faRiskService.saveFaRisk(faRisk);
		if (retFaRisk != null) {
			return JsonUtils.toJson(RequestUtils.successResult(""));
		} else {
			return JsonUtils.toJson(RequestUtils.failResult("风控数据保存失败"));
		}
	}
}
