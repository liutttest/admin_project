package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.evan.common.user.domain.FwHistoryBill;
import com.evan.common.user.domain.FwTransactionDetail;
import com.evan.common.user.monitor.MonitorUtils;
import com.evan.common.user.service.FwHistoryBillService;
import com.evan.common.user.service.FwTransactionDetailService;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.dao.support.Page;
import com.evan.jaron.core.dao.support.QCriteria;
import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.dao.support.Sort;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;

@RequestMapping("/fwBill")
@Controller
public class FwBillController extends BaseController{
final Logger logger = LoggerFactory.getLogger(FwBillController.class);	
	
	@Autowired
	private FwHistoryBillService fwHistoryBillService;
	
	@Autowired
	private FwTransactionDetailService fwTransactionDetailService;

	@RequestMapping(method = {GET, POST})
	public String list(@ModelAttribute SearchForm searchForm, Model model) {
		return "fw_bill/list";
	}
	
	@RequestMapping(method = {GET, POST}, value="/billList")
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
		    }  
		    catch (Exception e)  
		    {  
		        break;  
		    }  
		} 
		
		int pageNo = Integer.valueOf(iDisplayStart)/Integer.valueOf(iDisplayLength) + 1;
		searchString = searchString.trim();
		Page<FwHistoryBill> page = fwHistoryBillService.getFwHistoryBillPage(searchString, pageNo, Integer.valueOf(iDisplayLength));
		
		count = page.getTotalCount();	
		List<FwHistoryBill> fwHistoryBills = page.getContent();

		JSONArray jsonArray2 = new JSONArray();  
		JSONObject jsonObject2 = new JSONObject();  
		 
		// 为操作次数加1  
		int initEcho = Integer.parseInt(sEcho) + 1;  

		for (FwHistoryBill fwHistoryBill : fwHistoryBills)  
		{   
		    jsonObject2.put("comName", fwHistoryBill.getFwCompany().getComName());  
		    jsonObject2.put("accountPeriod", fwHistoryBill.getAccountPeriod());
		    jsonObject2.put("loanAmt", fwHistoryBill.getLoanAmt());
		    jsonObject2.put("principal", fwHistoryBill.getPrincipal());
		    jsonObject2.put("interest", fwHistoryBill.getInterest());
		    jsonObject2.put("boInterest", fwHistoryBill.getBoInterest());
		    jsonObject2.put("total", fwHistoryBill.getTotal());
		    jsonObject2.put("comId", fwHistoryBill.getComId());
		    
		    jsonArray2.add(jsonObject2);  
		}  
		  
		String json = "{\"sEcho\":" + initEcho + ",\"iTotalRecords\":" + count + ",\"iTotalDisplayRecords\":" + count + ",\"aaData\":" + jsonArray2.toString() + "}";  
		
		return json;
		
	}
	
	@RequestMapping(method = {GET, POST}, value="/getBillDetail")
	@ResponseBody
	public String getBillDetail(@RequestParam Map<String, String> map, HttpServletRequest request)
	{
		Map<String, Object> retMap = new HashMap<String, Object>();
		String comId = MapUtils.getString(map, "comId", "");
		String accountPeriod = MapUtils.getString(map, "accountPeroid", "");
		String period [] = getDate(accountPeriod);
        QCriteria qCriteria = new QCriteria();
        qCriteria.addEntry("comId", QCriteria.Op.EQ, Long.valueOf(comId));
        qCriteria.addEntry("state", QCriteria.Op.EQ, "1");
        qCriteria.addEntry("jyTime", QCriteria.Op.GTEQ, period[0]);
        qCriteria.addEntry("jyTime", QCriteria.Op.LTEQ, period[1]);
        Sort sort = new Sort();
        sort.desc("jyTime");
        List<FwTransactionDetail> list = fwTransactionDetailService.find(sort, qCriteria);
        retMap.put("list", list);
        retMap.put("dates", period);
        String year = accountPeriod.substring(0,4);
		String month = accountPeriod.substring(4,6);
		retMap.put("year", year);
		retMap.put("month", month);
        return JsonUtils.toJson(RequestUtils.successResult(retMap));
	}

	/**
	 * 账单日期
	 * @param accountPeriod
	 * @return
	 */
	private String [] getDate(String accountPeriod){
		String year = accountPeriod.substring(0,4);
		String month = accountPeriod.substring(4,6);
		String dates[] = new String[2];
		int y = Integer.parseInt(year);
		int m = Integer.parseInt(month);
		if("01".equals(month)){
			y = (y-1);
			dates[0] = y+"-"+"12-21 00:00:00";
			dates[1] = year+"-"+month+"-20 23:59:59";
		}else{
			m = m-1;
			String monString = m+"";
			if(m<10){
				monString = "0"+m;
			}
			dates[0] = year+"-"+monString+"-21 00:00:00";
			dates[1] = year+"-"+month+"-20 23:59:59";
		}
		return dates;
	}
}
