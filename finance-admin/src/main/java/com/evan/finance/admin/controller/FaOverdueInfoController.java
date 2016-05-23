package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.common.user.domain.FaOverdueInfo;
import com.evan.common.user.domain.FaTempScene;
import com.evan.common.user.domain.FaTemplate;
import com.evan.common.user.service.FaOverdueInfoService;
import com.evan.common.user.service.FaTempSceneService;
import com.evan.common.user.service.FaTemplateService;
import com.evan.common.user.service.FwLoanApplyService;
import com.evan.common.utils.ConstantTool.TempleteType;
import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.bank.BankTools;
import com.evan.finance.admin.notification.service.NotificationService;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;

@RequestMapping("/faOverdueInfo")
@Controller
public class FaOverdueInfoController extends BaseController{
	final Logger logger = LoggerFactory
			.getLogger(FaOverdueInfoController.class);
	
	@Autowired
	private FaOverdueInfoService faOverdueInfoService;
	
	@Autowired
	private FaTempSceneService faTempSceneService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private FaTemplateService faTemplateService;
	
	@Autowired
	private BankTools bankTools;
	
	@Autowired
	private FwLoanApplyService fwLoanApplyService;
	
	/**
	 * 跳转到“逾期记录页面”
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toFaOverdueInfo", method = {GET,POST})
	public String toInterestList(Model model) {
		return "toFaOverdue/List";
	}

	/**
	 * liutt
	 * 获取逾期记录
	 */
	@RequestMapping(value="/getList", method = {GET,POST})
	@ResponseBody
	public String getInterestList(@RequestParam Map<String,Object> map, HttpServletRequest request, HttpServletResponse response)
	{
			// 记录操作的次数  每次加1 
			String sEcho = "0";
			
			// 起始纪录  
			String iDisplayStart = "0";
			
			// 每页的条数
			String iDisplayLength = "10";
			
			// 查询出来的总数量
			long count = 0; 
			
			// 搜索字符串
			String searchString = "";
			
			// 月份
			String month = "";
			
			// 日期
			String day = "";
			
			// 获取jquery datatable当前配置参数
			String aoData = request.getParameter("aoData");   
			JSONArray jsonArray = JSONArray.fromObject(aoData);  
			for (int i = 0; i < jsonArray.size(); i++)  
			{  
			    try  
			    {  
			        JSONObject jsonObject = (JSONObject)jsonArray.get(i);  
			        if (jsonObject.get("name").equals("sEcho"))  
			            sEcho = jsonObject.get("value").toString();  
			        else if (jsonObject.get("name").equals("iDisplayStart"))  
			            iDisplayStart = jsonObject.get("value").toString();  
			        else if (jsonObject.get("name").equals("iDisplayLength"))  
			            iDisplayLength = jsonObject.get("value").toString();
			        else if (jsonObject.get("name").equals("sSearch"))
			        	searchString = jsonObject.get("value").toString();
			        else if (jsonObject.get("name").equals("month"))
			        	month = jsonObject.get("value").toString();
			        else if (jsonObject.get("name").equals("day"))
					    day = jsonObject.get("value").toString();
			    }  
			    catch (Exception e)  
			    {  
			        break;  
			    }  
			} 
			
			int startNo = Integer.parseInt( iDisplayStart);
			
			//String expitedTime = month;
			searchString = searchString.trim();
			List<Map<String, Object>> page = faOverdueInfoService.getOverdueListByState("0", Integer.parseInt(iDisplayLength), startNo,searchString);
			count = faOverdueInfoService.getOverdueCountByState("0", searchString);
			//count = page.getTotalCount();	
			//List<FaOverdueInfo> faOverdueWarnings = page.getContent();

			JSONArray jsonArray2 = new JSONArray();  
			JSONObject jsonObject2 = new JSONObject();  
			 
			// 为操作次数加1  
			int initEcho = Integer.parseInt(sEcho) + 1;  

			for (Map<String, Object> faOverdueWarning : page)  
			{   
			    jsonObject2.put("comName", MapUtils.getString(faOverdueWarning, "comName"));  
			    jsonObject2.put("comId", MapUtils.getString(faOverdueWarning, "comId"));  
			    jsonObject2.put("loanNo", MapUtils.getString(faOverdueWarning, "loanNo"));
			    jsonObject2.put("applyPassTime", MapUtils.getString(faOverdueWarning, "applyPassTime"));
			    jsonObject2.put("expitedTime", MapUtils.getString(faOverdueWarning, "expitedTime"));
			    jsonObject2.put("busId", MapUtils.getString(faOverdueWarning, "busId"));
			    jsonObject2.put("rate", MapUtils.getString(faOverdueWarning, "rate"));
			    jsonObject2.put("money", MapUtils.getString(faOverdueWarning, "money"));
			    jsonObject2.put("createTime", MapUtils.getString(faOverdueWarning, "createTime"));
			    jsonArray2.add(jsonObject2);  
			}  
			  
			String json = "{\"sEcho\":" + initEcho + ",\"iTotalRecords\":" + count + ",\"iTotalDisplayRecords\":" + count + ",\"aaData\":" + jsonArray2.toString() + "}";  
			
			return json;
	}
	


	/**
	 * @liutt
	 * 查询逾期历史记录
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getHistoryList", method = {GET,POST})
	@ResponseBody
	public String getHistoryList(@RequestParam Map<String,Object> map, HttpServletRequest request, HttpServletResponse response)
	{
		//借据号
		String loanNo = MapUtils.getString(map, "loanNo");
		String month = MapUtils.getString(map, "month","");
		String state = MapUtils.getString(map, "state");
		List<FaOverdueInfo> list = faOverdueInfoService.getOverdueHistoryList(loanNo,month,state);
		return JsonUtils.toJson(RequestUtils.successResult(list));
	}
	
	/**
     *  lixj
     * @param map
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value="/getTemplate", method={GET, POST})
	@ResponseBody
	public String  getTemplete(@RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response)
	{		
		String sceneKey = MapUtils.getString(map, "sceneKey", "");
		
		// 根据场景key获取场景
		FaTempScene faTempScene = faTempSceneService.getTempBySecneKey(sceneKey);
		
		Map<String, Object> resultMap = new  HashMap<String, Object>();
		
		//  邮件模版
		List<FaTemplate> emailTemplist = faTemplateService.findBySceneId(faTempScene.getTsId(), TempleteType.EMAIL);
		resultMap.put("emailTemplate", emailTemplist);
		
		// 短信模版
		List<FaTemplate> smsTempList = faTemplateService.findBySceneId(faTempScene.getTsId(), TempleteType.SMS);
		resultMap.put("smsTemplate", smsTempList);
		
		// 站内信
		List<FaTemplate> messageTempList = faTemplateService.findBySceneId(faTempScene.getTsId(), TempleteType.MESSAGE);
		resultMap.put("messageTemplate", messageTempList);

		
		return JsonUtils.toJson(RequestUtils.successResult(resultMap));
	}
	
	@RequestMapping(value="/sendNotification", method={GET,POST})
	@ResponseBody
	public String sendNotification(@RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response)
	{
		String businessKey = MapUtils.getString(map, "busId");
		String isSendEmail = MapUtils.getString(map, "emailCheck"); // 是否发送邮件（on:发送；）
		String emailTitle = MapUtils.getString(map, "emailTitle"); // 邮件模板标题
		String emailContent = MapUtils.getString(map, "emailContent"); // 邮件内容
		String isSendSms = MapUtils.getString(map, "smsCheck"); // 是否发送短信（on:发送；）
		String smsContent = MapUtils.getString(map, "smsContent"); // 短信内容
		String isSendMessage = MapUtils.getString(map, "messageCheck"); // 是否发送站内信（on:发送；）
		String messageTitle = MapUtils.getString(map, "messageTitle"); // 站内信标题
		String messageContent = MapUtils.getString(map, "messageContent"); // 站内信内容
		Map<String, Object> retMap =null;
		
		// 发送通知
		try {
			retMap = notificationService.doSendNotification(businessKey, isSendEmail, emailTitle, emailContent, false, isSendSms, smsContent, isSendMessage, messageTitle, messageContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JsonUtils.toJson(RequestUtils.successResult(retMap));
	}
	
	
	
	/**
	 * @liutt
	 * 查询放款详情
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getLoanDetail", method = {GET,POST})
	@ResponseBody
	public String getLoanDetail(@RequestParam Map<String,Object> map, HttpServletRequest request, HttpServletResponse response)
	{
		//借据号
		String loanNo = MapUtils.getString(map, "loanNo");
		String busid = MapUtils.getString(map, "busid");
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("loanNo", loanNo);
		map2.put("busid", busid);
		try {
			Map<String, Object> retRsuMap = bankTools.queryLoanInfo(map2);
			retRsuMap = (Map<String, Object>) retRsuMap.get("opRep");
			String retCode = MapUtils.getString(retRsuMap, "retCode");
			if (!"0".equals(retCode)) {
				return JsonUtils.toJson(RequestUtils.failResult(getMessage("get.message.error")));
			}
			return JsonUtils.toJson(RequestUtils.successResult(retRsuMap));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("service.error")));
		}
	}
	
	/**
	 * @liutt
	 * 确认代偿
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/submitCompensatory", method = {GET,POST})
	@ResponseBody
	public String submitCompensatory(@RequestParam Map<String,Object> map)
	{
		//借据号
		String reason = MapUtils.getString(map, "reason");
		String loanNo = MapUtils.getString(map, "loanNo");
		String compensatoryTotal = MapUtils.getString(map, "compensatoryTotal");
		BigDecimal bigCompensatory = new BigDecimal(compensatoryTotal);
		
		//调用银行确认代偿接口 TODO
		//默认银行成功
		
		try {
			//修改逾期记录表
			faOverdueInfoService.updataOverdueInfos(loanNo, "0", reason);
			//修改放款表  借据号为 。。。。的放款记录状态
			fwLoanApplyService.updateLoanApplyCompensatory(loanNo);
			return JsonUtils.toJson(RequestUtils.successResult(""));
		} catch (Exception e) {
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("service.error")));
		}
	}
	
	
	
	/**
	 * 跳转到“逾期历史记录页面”(luy)
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toFaOverdueInfoHistory", method = {GET,POST})
	public String toFaOverdueInfoHistory(Model model) {
		return "toFaOverdueInfoHistory/list";
	}

	/**
	 * 获取逾期历史记录(luy)
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getHistoryFaOverdueInfoList", method = {GET,POST})
	@ResponseBody
	public String getHistoryFaOverdueInfoList(@RequestParam Map<String,Object> map, HttpServletRequest request, HttpServletResponse response)
	{
			// 记录操作的次数  每次加1 
			String sEcho = "0";
			
			// 起始纪录  
			String iDisplayStart = "0";
			
			// 每页的条数
			String iDisplayLength = "10";
			
			// 查询出来的总数量
			long count = 0; 
			
			// 搜索字符串
			String searchString = "";
			
			// 月份
			String month = "";
			
			// 日期
			String day = "";
			
			// 获取jquery datatable当前配置参数
			String aoData = request.getParameter("aoData");   
			JSONArray jsonArray = JSONArray.fromObject(aoData);  
			for (int i = 0; i < jsonArray.size(); i++)  
			{  
			    try  
			    {  
			        JSONObject jsonObject = (JSONObject)jsonArray.get(i);  
			        if (jsonObject.get("name").equals("sEcho"))  
			            sEcho = jsonObject.get("value").toString();  
			        else if (jsonObject.get("name").equals("iDisplayStart"))  
			            iDisplayStart = jsonObject.get("value").toString();  
			        else if (jsonObject.get("name").equals("iDisplayLength"))  
			            iDisplayLength = jsonObject.get("value").toString();
			        else if (jsonObject.get("name").equals("sSearch"))
			        	searchString = jsonObject.get("value").toString();
			        else if (jsonObject.get("name").equals("month"))
			        	month = jsonObject.get("value").toString();
			        else if (jsonObject.get("name").equals("day"))
					    day = jsonObject.get("value").toString();
			    }  
			    catch (Exception e)  
			    {  
			        break;  
			    }  
			} 
			
			int starNo = Integer.valueOf(iDisplayStart) ;
			
			String expitedTime = month;
			searchString = searchString.trim();
			List<Map<String, Object>> list = faOverdueInfoService.getOverdueList(searchString, expitedTime, Integer.valueOf(iDisplayLength), starNo);
			count = faOverdueInfoService.getOverdueListCount(searchString, expitedTime);	

			JSONArray jsonArray2 = new JSONArray();  
			JSONObject jsonObject2 = new JSONObject();  
			 
			// 为操作次数加1  
			int initEcho = Integer.parseInt(sEcho) ;  

			for (Map<String, Object> item : list)  
			{   
			    jsonObject2.put("comName", MapUtils.getString(item, "comName"));  
			    jsonObject2.put("comId", MapUtils.getString(item, "comId"));  
			    jsonObject2.put("loanNo", MapUtils.getString(item, "loanNo"));
			    jsonObject2.put("applyPassTime", MapUtils.getString(item, "applyPassTime"));
			    jsonObject2.put("expitedTime", MapUtils.getString(item, "expiteTime"));
			    jsonObject2.put("busId", MapUtils.getString(item, "busId"));
			    jsonObject2.put("isInterest", MapUtils.getString(item, "isInterest"));
			    jsonObject2.put("isPrincipal", MapUtils.getString(item, "isPrincipal"));
			    jsonArray2.add(jsonObject2);  
			}  
			  
			String json = "{\"sEcho\":" + initEcho + ",\"iTotalRecords\":" + count + ",\"iTotalDisplayRecords\":" + count + ",\"aaData\":" + jsonArray2.toString() + "}";  
			
			return json;
	}
	
}
