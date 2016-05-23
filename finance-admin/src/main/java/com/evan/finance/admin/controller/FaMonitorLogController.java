package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.common.user.domain.FaMonitorLog;
import com.evan.common.user.monitor.MonitorUtils;
import com.evan.common.user.service.FaMonitorLogService;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.dao.support.Page;
import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;

@RequestMapping("/faMonitorLog")
@Controller
public class FaMonitorLogController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FaMonitorLogController.class);	
	
	@Autowired
	private FaMonitorLogService faMonitorLogService;
	
	@RequestMapping(method = {GET, POST})
	public String list(HttpServletRequest request, Model model) {
		String type = request.getParameter("type");
		
		if (MonitorUtils.TYPE_BANK.equals(type)) {
//			List<FaMonitorLog> faMonitorLogs2 = new ArrayList<FaMonitorLog>();
//			for (FaMonitorLog faMonitorLog : faMonitorLogs) {
//				if (!StringUtils.isEmpty(faMonitorLog.getContent())) {
//					faMonitorLog.setContent(faMonitorLog.getContent().replaceAll("<", "&lt;"));
//					faMonitorLog.setContent(faMonitorLog.getContent().replaceAll(">", "&gt;"));
//				}
//				if (!StringUtils.isEmpty(faMonitorLog.getInContent())) {
//					faMonitorLog.setInContent(faMonitorLog.getInContent().replaceAll("<", "&lt;"));
//					faMonitorLog.setInContent(faMonitorLog.getInContent().replaceAll(">", "&gt;"));
//				}
//				if (!StringUtils.isEmpty(faMonitorLog.getOutContent())) {
//					faMonitorLog.setOutContent(faMonitorLog.getOutContent().replaceAll("<", "&lt;"));
//					faMonitorLog.setOutContent(faMonitorLog.getOutContent().replaceAll(">", "&gt;"));
//				}
//				faMonitorLogs2.add(faMonitorLog);
//			}
//			model.addAttribute("bankMap", MonitorUtils.bankMap);
//			model.addAttribute("faMonitorLogs", faMonitorLogs2);
			return "faMonitorLog/bank";
		}else if(MonitorUtils.TYPE_BUSINESS_ACCESS.equals(type)){
//			List<FaMonitorLog> faMonitorLogs = faMonitorLogService.getFaMonitorLogsByType(type);
//			model.addAttribute("faMonitorLogs", faMonitorLogs);
			return "faMonitorLog/access";
		}else if(MonitorUtils.TYPE_BUSINESS_QUOTA.equals(type)){
//			List<FaMonitorLog> faMonitorLogs = faMonitorLogService.getFaMonitorLogsByType(type);
//			model.addAttribute("faMonitorLogs", faMonitorLogs);
			return "faMonitorLog/quota";
		}else if(MonitorUtils.TYPE_BUSINESS_FIELD.equals(type)){
//			List<FaMonitorLog> faMonitorLogs = faMonitorLogService.getFaMonitorLogsByType(type);
//			model.addAttribute("faMonitorLogs", faMonitorLogs);
			return "faMonitorLog/field";
		}else{
//			List<FaMonitorLog> faMonitorLogs = faMonitorLogService.getFaMonitorLogsByType(type);
//			model.addAttribute("faMonitorLogs", faMonitorLogs);
			return "faMonitorLog/other";
		}

	}
	
	@RequestMapping(method = {GET, POST}, value="/logList")
	@ResponseBody
	public String  getLogList(@RequestParam Map<String, String> map, HttpServletRequest request)
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
		
		// 日志类型
		String typeString = "";
		
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
		        else if (jsonObject.get("name").equals("type"))
		        	typeString = jsonObject.get("value").toString();
		    }  
		    catch (Exception e)  
		    {  
		        break;  
		    }  
		} 
		
		int pageNo = Integer.valueOf(iDisplayStart)/Integer.valueOf(iDisplayLength) + 1;
		searchString = searchString.trim();
		Page<FaMonitorLog> page;
		if (typeString.equals(MonitorUtils.TYPE_BANK))
		{
			page = faMonitorLogService.getFaMonitorLogsPageByType(typeString, searchString, pageNo, Integer.valueOf(iDisplayLength));
		}
		else
		{
			page = faMonitorLogService.getFaMonitorPageByType(typeString, searchString, pageNo, Integer.valueOf(iDisplayLength));
		}
		count = page.getTotalCount();	
		List<FaMonitorLog> faMonitorLogs = page.getContent();

		JSONArray jsonArray2 = new JSONArray();  
		JSONObject jsonObject2 = new JSONObject();  
		 
		// 为操作次数加1  
		int initEcho = Integer.parseInt(sEcho) + 1;  
		
		String contentString = "";
		String inputParamString = "";
		String outputString = "";

		for (FaMonitorLog faMonitorLog : faMonitorLogs)  
		{   
		    jsonObject2.put("title", faMonitorLog.getTitle());  
		    jsonObject2.put("bankInterface", MonitorUtils.bankMap.get(faMonitorLog.getTitle()));  
		    jsonObject2.put("comName", faMonitorLog.getComName()); 
		    contentString = faMonitorLog.getContent();
		    if  (!StringUtils.isEmpty(contentString))
		    {
		    	contentString = contentString.replaceAll("<", "&lt");
		    	contentString = contentString.replaceAll(">", "&gt");
		    }
		    jsonObject2.put("content", contentString);

		    inputParamString = faMonitorLog.getInContent();
		    if (!StringUtils.isEmpty(inputParamString))
		    {
		    	inputParamString = inputParamString.replaceAll("<", "&lt");
		    	inputParamString = inputParamString.replaceAll(">", "&gt");
		    }
		    
		    jsonObject2.put("inputParam", inputParamString);
		    
		    outputString = faMonitorLog.getOutContent();
		    if (!StringUtils.isEmpty(outputString))
		    {
		    	outputString = outputString.replaceAll("<", "&lt");
		    	outputString = outputString.replaceAll(">", "&gt");
		    }
		   
		    jsonObject2.put("outputParam", outputString);
		    
		    jsonObject2.put("businessId", faMonitorLog.getBusId());  
		    jsonObject2.put("opTime", faMonitorLog.getRecordingTime()); 
		    jsonObject2.put("logId", faMonitorLog.getId());
		    jsonArray2.add(jsonObject2);  
		}  
		  
		String json = "{\"sEcho\":" + initEcho + ",\"iTotalRecords\":" + count + ",\"iTotalDisplayRecords\":" + count + ",\"aaData\":" + jsonArray2.toString() + "}";  
		
		return json;
		
	}
	
	
	@RequestMapping(method = {GET, POST},value="/getDetailByBusid")
	public String getDetailByBusid(HttpServletRequest request, Model model) {
		String busId = request.getParameter("busId");
		List<FaMonitorLog> faMonitorLogs = faMonitorLogService.getFaMonitorLogsByBusIdandType(busId,MonitorUtils.TYPE_BANK);
		List<FaMonitorLog> faMonitorLogs2 = new ArrayList<FaMonitorLog>();
		for (FaMonitorLog faMonitorLog : faMonitorLogs) {
			if (!StringUtils.isEmpty(faMonitorLog.getContent())) {
				faMonitorLog.setContent(faMonitorLog.getContent().replaceAll("<", "&lt;"));
				faMonitorLog.setContent(faMonitorLog.getContent().replaceAll(">", "&gt;"));
			}
			if (!StringUtils.isEmpty(faMonitorLog.getInContent())) {
				faMonitorLog.setInContent(faMonitorLog.getInContent().replaceAll("<", "&lt;"));
				faMonitorLog.setInContent(faMonitorLog.getInContent().replaceAll(">", "&gt;"));
			}
			if (!StringUtils.isEmpty(faMonitorLog.getOutContent())) {
				faMonitorLog.setOutContent(faMonitorLog.getOutContent().replaceAll("<", "&lt;"));
				faMonitorLog.setOutContent(faMonitorLog.getOutContent().replaceAll(">", "&gt;"));
			}
			faMonitorLogs2.add(faMonitorLog);
		}
			model.addAttribute("bankMap", MonitorUtils.bankMap);
			model.addAttribute("faMonitorLogs", faMonitorLogs2);
		return "faMonitorLog/detail";
	}
	
	/**
	 * 查询列表方法
	 * @param map
	 * @param searchForm
	 * @return
	 */
	@RequestMapping(value="/list",method = {GET, POST})
	@ResponseBody
	public String listByPage(@RequestParam Map map,@ModelAttribute SearchForm searchForm) {
		String productType = MapUtils.getString(map,"selectword");
		String name = MapUtils.getString(map,"keyword");
		Map map2 = new HashMap();
		// map2.put("name"+searchForm.OP_LIKE_SUFFIX, name);
		// map2.put("prodCatalog"+searchForm.OP_EQ_SUFFIX, productType);
		searchForm.setForm(map2);
		//searchForm.setPageNo(Integer.parseInt((String) map.get("pageNum")));
		//searchForm.setPageSize(Integer.parseInt((String) map.get("pageSize")));
		searchForm.setOrderBy("id desc");
		Page<FaMonitorLog> faMonitorLogPage = faMonitorLogService.findPage(searchForm);
		return JsonUtils.toJson(RequestUtils.successResult(faMonitorLogPage));
		
	}

	
	/**
	 * 根据id查出对象信息
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getByPk", method = {GET,POST})
	@ResponseBody
	public String update(@RequestParam Map map,Model model,HttpServletResponse response) {
		Integer id = MapUtils.getInteger(map,"id");
		FaMonitorLog faMonitorLog = faMonitorLogService.findByPk(id);
		/*if (!StringUtils.isEmpty(faMonitorLog.getContent())) {
			faMonitorLog.setContent(faMonitorLog.getContent().replaceAll("<", "&lt;"));
			faMonitorLog.setContent(faMonitorLog.getContent().replaceAll(">", "&gt;"));
		}
		if (!StringUtils.isEmpty(faMonitorLog.getInContent())) {
			faMonitorLog.setInContent(faMonitorLog.getInContent().replaceAll("<", "&lt;"));
			faMonitorLog.setInContent(faMonitorLog.getInContent().replaceAll(">", "&gt;"));
		}
		if (!StringUtils.isEmpty(faMonitorLog.getOutContent())) {
			faMonitorLog.setOutContent(faMonitorLog.getOutContent().replaceAll("<", "&lt;"));
			faMonitorLog.setOutContent(faMonitorLog.getOutContent().replaceAll(">", "&gt;"));
		}*/
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("faMonitorLog", faMonitorLog);
		map2.put("bank", MonitorUtils.bankMap.get(faMonitorLog.getTitle()));
		return JsonUtils.toJson(RequestUtils.successResult(map2));
	}
	
	
	 /**
     * 创建和修改
     * @param map
     * @param model
     * @param request
     * @return
     */
	@RequestMapping(value = "/create", method = POST)
	@ResponseBody
	
	public String create(@RequestParam() Map map) {
		FaMonitorLog faMonitorLog = faMonitorLogService.saveFaMonitorLog(map);
        if(faMonitorLog==null){
        return JsonUtils.toJson(RequestUtils.failResult("error"));
        }
        return JsonUtils.toJson(RequestUtils.successResult(null));
    }
	
	
	/**
	 * 删除物品
	 * @param ids
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "/remove", method = POST)
    @ResponseBody
    public String remove(@RequestParam("ids") String ids) {
		boolean status = faMonitorLogService.removeFaMonitorLog(ids);
		if (status) {
			return JsonUtils.toJson(RequestUtils.successResult(null));
		}
		return JsonUtils.toJson(RequestUtils.failResult("error"));
    }
}

