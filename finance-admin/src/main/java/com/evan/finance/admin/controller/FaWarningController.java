package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.math.BigDecimal;
import java.util.Calendar;
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

import com.evan.common.user.domain.FaCenterDataBank;
import com.evan.common.user.domain.FaOverdueWarning;
import com.evan.common.user.domain.FaTempScene;
import com.evan.common.user.domain.FaTemplate;
import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.domain.FwCompany;
import com.evan.common.user.domain.FwHistoryBill;
import com.evan.common.user.domain.FwLoanApply;
import com.evan.common.user.service.FaOverdueWarningService;
import com.evan.common.user.service.FaTempSceneService;
import com.evan.common.user.service.FaTemplateService;
import com.evan.common.user.service.FwCompanyService;
import com.evan.common.user.service.FwLoanApplyService;
import com.evan.common.utils.DateUtils;
import com.evan.common.utils.RequestUtils;
import com.evan.common.utils.ConstantTool.ApplyState;
import com.evan.common.utils.ConstantTool.SCENE;
import com.evan.common.utils.ConstantTool.TempleteType;
import com.evan.finance.admin.notification.service.NotificationService;
import com.evan.jaron.core.dao.support.Page;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;

@RequestMapping("/faWarning")
@Controller
public class FaWarningController extends BaseController {

	@Autowired
	FaOverdueWarningService faOverdueWarningService;
	
	@Autowired
	private FaTempSceneService faTempSceneService;
	
	@Autowired
	private FaTemplateService faTemplateService;
	
	@Autowired
	private FwLoanApplyService fwLoanApplyService;
	
	@Autowired
	private FwCompanyService fwCompanyService;
	
	@Autowired
	private NotificationService notificationService;
	
	final Logger logger = LoggerFactory.getLogger(FaWarningController.class);
	
	
	/**
	 * 跳转到“利息预警页面”
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toInterestList", method = {GET,POST})
	public String toInterestList(Model model) {
		return "fw_business_sxd/toInterestList";
	}
	
	/**
	 * 跳转到“本金预警页面”
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toPrincipalList", method = {GET,POST})
	public String goToBusinessSxd(Model model) {
		return "fw_business_sxd/toPrincipalList";
	}
	
	/**
	 * lixj
	 * 获取还息预警列表
	 */
	@RequestMapping(value="/getInterestList", method = {GET,POST})
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
			
			int pageNo = Integer.valueOf(iDisplayStart)/Integer.valueOf(iDisplayLength) + 1;
			
			String createTime = month + "-" + day;
			searchString = searchString.trim();
			Page<FaOverdueWarning> page = faOverdueWarningService.getOverdueListByCreateTime(searchString, createTime, Integer.valueOf(iDisplayLength), pageNo);
			
			count = page.getTotalCount();	
			List<FaOverdueWarning> faOverdueWarnings = page.getContent();

			JSONArray jsonArray2 = new JSONArray();  
			JSONObject jsonObject2 = new JSONObject();  
			 
			// 为操作次数加1  
			int initEcho = Integer.parseInt(sEcho) + 1;  

			for (FaOverdueWarning faOverdueWarning : faOverdueWarnings)  
			{   
			    jsonObject2.put("comName", faOverdueWarning.getComName());  
			    jsonObject2.put("loanNo", faOverdueWarning.getLoanNo());
			    jsonObject2.put("applyPassTime", faOverdueWarning.getApplyPassTime());
			    jsonObject2.put("expitedTime", faOverdueWarning.getExpitedTime());
			    jsonObject2.put("overdueInterest", faOverdueWarning.getOverdueInterest());
			    jsonObject2.put("currentInterest", faOverdueWarning.getCurrentInterest());
			    jsonObject2.put("busId", faOverdueWarning.getBusId());
			    jsonArray2.add(jsonObject2);  
			}  
			  
			String json = "{\"sEcho\":" + initEcho + ",\"iTotalRecords\":" + count + ",\"iTotalDisplayRecords\":" + count + ",\"aaData\":" + jsonArray2.toString() + "}";  
			
			return json;
	}
	
	/**
	 * lixj
	 * 获取本金预警列表
	 */
	@RequestMapping(value="/getPrincipleList", method = {GET,POST})
	@ResponseBody
	public String getPrincipleList(@RequestParam Map<String,Object> map, HttpServletRequest request, HttpServletResponse response)
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
			
			int pageNo = Integer.valueOf(iDisplayStart)/Integer.valueOf(iDisplayLength) + 1;
			
			String currentTime = DateUtils.getCurrentDateTime("yyyy-MM-dd 00:00:00");
			String endTime =  DateUtils.dateAddWithoutTime(Integer.valueOf(day));
			searchString = searchString.trim();
			Page<FwLoanApply> page = fwLoanApplyService.getPrincipleListByTime(searchString, currentTime, endTime, pageNo,  Integer.valueOf(iDisplayLength));
			
			count = page.getTotalCount();	
			List<FwLoanApply> fwLoanApplies = page.getContent();

			JSONArray jsonArray2 = new JSONArray();  
			JSONObject jsonObject2 = new JSONObject();  
			 
			// 为操作次数加1  
			int initEcho = Integer.parseInt(sEcho) + 1;  

			for (FwLoanApply fwLoanApply : fwLoanApplies)  
			{   
				FwCompany fwCompany = fwCompanyService.findByPk(fwLoanApply.getComId());
				
			    jsonObject2.put("comName", fwCompany != null ? fwCompany.getComName() : "");  
			    jsonObject2.put("loanNo", fwLoanApply.getLoanNo());
			    jsonObject2.put("applyPassTime", fwLoanApply.getApplyPassTime());
			    jsonObject2.put("expitedTime", fwLoanApply.getExpitedTime());
			    jsonObject2.put("repaymentMoney", fwLoanApply.getRepaymentMoney());
			    jsonObject2.put("busId", fwLoanApply.getBusinessId());
			    jsonArray2.add(jsonObject2);  
			}  
			  
			String json = "{\"sEcho\":" + initEcho + ",\"iTotalRecords\":" + count + ",\"iTotalDisplayRecords\":" + count + ",\"aaData\":" + jsonArray2.toString() + "}";  
			
			return json;
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
}

