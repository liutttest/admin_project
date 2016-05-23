package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.common.user.domain.FwBlacklist;
import com.evan.common.user.service.FwBlacklistService;
import com.evan.common.utils.ConstantTool.BlackListIsHistroy;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;

@RequestMapping("/fwBlacklist")
@Controller
public class FwBlacklistController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FwBlacklistController.class);	
	
	@Autowired
	private FwBlacklistService fwBlacklistService;
	
	
	/**
	 * 点击黑名单管理
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = GET)
	public String goToBlacklist(Model model) {
		
		/*QCriteria criteria = new QCriteria();
		criteria.addEntry("isHistory", Op.EQ, 1);
		Sort sort = new Sort();
		sort.desc("addTime");*/
		List<Map<String, Object>> fwBlacklists = fwBlacklistService.getBlickList(BlackListIsHistroy.NO_HISTROY);
		
		
		model.addAttribute("list", fwBlacklists);
		return "fw_blacklist/list";
	}
	
	
	
	/**
	 * test
	 * @return
	 */
	@RequestMapping(method = GET,value="/goanalysis")
	public String goanalysis(Model model) {
		
		return "analiysis";
	}
	
	/**
	 * test
	 * @return
	 */
	@RequestMapping(method = POST,value="/analiysis")
	@ResponseBody
	public String analiysis(Model model) {
		
		/*QCriteria criteria = new QCriteria();
		criteria.addEntry("isHistory", Op.EQ, 1);
		Sort sort = new Sort();
		sort.desc("addTime");*/
		/*List<Map<String, Object>> fwBlacklists = fwBlacklistService.getBlickList();*/
		
		Map<String, Object> map =  new HashMap<String, Object>();
		List<Long> list  = new ArrayList<Long>();
		list.add(20L);
		list.add(140L);
		list.add(150L);
		list.add(160L);
		list.add(300L);
		

		List<Long> list1  = new ArrayList<Long>();
		list1.add(120L);
		list1.add(160L);
		list1.add(150L);
		list1.add(160L);
		list1.add(30L);
		
		
		
		List<String> list2  = new ArrayList<String>();
		list2.add("2015-01-01");
		list2.add("2015-01-02");
		list2.add("2015-01-03");
		list2.add("2015-01-04");
		list2.add("2015-01-05");
		list2.add("2015-01-06");
		map.put("list",list2);
		map.put("list1",list);
		map.put("list2",list1);
		return JsonUtils.toJson(map);
	}
	
	
	
	/**
	 * 点击黑名单操作记录
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="histroy",method = GET)
	public String histroy(Model model) {
		
		/*QCriteria criteria = new QCriteria();
		criteria.addEntry("isHistory", Op.EQ, 1);
		Sort sort = new Sort();
		sort.desc("addTime");*/
		List<Map<String, Object>> fwBlacklists = fwBlacklistService.getBlickList();
		
		model.addAttribute("list", fwBlacklists);
		return "fw_blacklist_histroy/list";
	}
	
	
	
	
	
	/**
	 * 将账户从黑名单中移除，级修改ishistory字段
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateBlacklist",method = {GET, POST})
	@ResponseBody
	public String updateBlacklist(@RequestParam Map<String,Object> map,@ModelAttribute SearchForm searchForm, Model model,HttpServletRequest request,HttpServletResponse response) {
		Long fbId  = MapUtils.getLong(map, "fbId");
		//移除黑名单，即修改黑名单是否为历史记录字段为0
		FwBlacklist fwBlacklist = fwBlacklistService.updateIsHistory(BlackListIsHistroy.IS_HISTROY, fbId);
	
		if (fwBlacklist==null) {
			JsonUtils.toJson(RequestUtils.failResult("移除黑名单失败"));
		}
		return JsonUtils.toJson(RequestUtils.successResult(null));
	}

	
	/**
	 * 根据id查出对象信息
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/gethistroyByComId", method = {GET,POST})
	@ResponseBody
	public String update(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		Long comId=MapUtils.getLong(map,"comId");
		List<Map<String, Object>> fwBlacklist = fwBlacklistService.getBlickListBycomId(comId);
		return JsonUtils.toJson(RequestUtils.successResult(fwBlacklist));
	}
	
	
	 /**
	  * unused
     * 创建和修改
     * @param map
     * @param model
     * @param request
     * @return
     */
	@RequestMapping(value = "/create", method = POST)
	@ResponseBody
	
	public String create(@RequestParam() Map<String,Object> map,Model model ,HttpServletRequest request,HttpServletResponse response) {
		FwBlacklist fwBlacklist = fwBlacklistService.creatFwBlacklist(map);
        if(fwBlacklist==null){
        return JsonUtils.toJson(RequestUtils.failResult("error"));
        }
        return JsonUtils.toJson(RequestUtils.successResult(null));
    }
	
	
	/**
	 * unused
	 * 删除物品
	 * @param ids
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "/remove", method = POST)
    @ResponseBody
    public String remove(@RequestParam("ids") String ids, Model model,HttpServletResponse response) {
		boolean status = fwBlacklistService.removeFwBlacklist(ids);
    	if(status){
        return JsonUtils.toJson(RequestUtils.successResult(null));
        }
        return JsonUtils.toJson(RequestUtils.failResult("error"));
    }
}

