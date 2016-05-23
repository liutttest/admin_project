package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
import com.evan.common.user.domain.FwCompany;
import com.evan.common.user.service.FwBlacklistService;
import com.evan.common.user.service.FwComAccountService;
import com.evan.common.user.service.FwComPerService;
import com.evan.common.user.service.FwCompanyService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.plugins.area.AreaHelper;
import com.evan.jaron.plugins.dictionary.DictionaryHelper;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.nd.common_file.CommonFileFunc;
import com.evan.nd.common_file.domain.FwFiles;
import com.evan.nd.common_file.service.FwFilesService;

@RequestMapping("/fwComAccount")
@Controller
public class FwComAccountController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FwComAccountController.class);	
	
	@Autowired
	private FwComAccountService fwComAccountService;
	
	
	@Autowired
	private FwBlacklistService fwBlacklistService;
	
	@Autowired 
	private FwCompanyService fwCompanyService;
	
	@Autowired
	private FwFilesService fwFilesService;
	
	@Autowired
	private FwComPerService fwComPerService;
	
	@Autowired
	private AreaHelper areaHelper;
	
	@Autowired
	private DictionaryHelper dictionaryHelper;
	
	/**
	 * 首页点击用户管理
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = GET)
	public String goToComAcount(Model model) {
		//查询用户列表
		List<Map<String,Object>> fwComAccounts = fwComAccountService.getComAcountList();
		//List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		/*for (Map<String, Object> map : fwComAccounts) {
			String provice = areaHelper.lookupAreaName(MapUtils.getString(map, "provice"));
			String city = areaHelper.lookupAreaName(MapUtils.getString(map, "city"));
			String area = areaHelper.lookupAreaName(MapUtils.getString(map, "area"));
			map.put("address",provice+" "+city+" "+area);
			Map<String, Object> map2 = map;
			list.add(map2);
		}*/
		model.addAttribute("list", fwComAccounts);
		return "fw_comaccount/list";
	}
	
	/**
	 * 将用户移入黑名单
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/createBlacklist",method = {GET, POST})
	@ResponseBody
	public String createBlacklist(@RequestParam Map<String,Object> map,@ModelAttribute SearchForm searchForm, Model model,HttpServletRequest request,HttpServletResponse response) {
		FwBlacklist fwBlacklist = fwBlacklistService.creatFwBlacklist(map);
		if (fwBlacklist==null) {
			JsonUtils.toJson(RequestUtils.failResult(getMessage("fwcomaccount.remove.failed")));
		}
		return JsonUtils.toJson(RequestUtils.successResult(null));
	}

	
	/**
	 * 根据企业id查询详情
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getComaccountDetail", method = {GET,POST})
	@ResponseBody
	public String getCompanyDetail(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		Long comId = MapUtils.getLong(map, "comId");
		FwCompany fwCompany = fwCompanyService.findByPk(comId);
		Map<String, Object> map2 = fwComAccountService.getUserMapByComId(comId);
		if (map2==null) {
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("fwcomaccount.getcompany.failed")));
		}
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("sysUser", map2);
		map3.put("fwCompany", fwCompany);
		return JsonUtils.toJson(RequestUtils.successResult(map3));
	}
	
	/*
	*//**
	 * 根据企业id查询企业控制人详情
	 * @param map
	 * @param model
	 * @return
	 *//*
	@RequestMapping(value = "/getCompanyOwnerDetail", method = {GET,POST})
	@ResponseBody
	public String getCompanyOwnerDetail(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		Long comId = MapUtils.getLong(map, "comId");
		//根据企业id和企业相关人的类型查询 查询企业控制人
		FwComPer fwComPer = fwComPerService.findByComIdAndPerType(comId, PersonType.COM_LEGAL);
		if (fwComPer==null) {
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("fwcomaccount.getcompanyowner.failed")));
		}
		//查询企业控制人的所有文件
		List<FwFiles> fwFiles =  fwFilesService.findByFkIdAndFkType(fwComPer.getPerId(), FkType.FW_BUSINESS_SXD);
		Map<String, Object> mapFile = CommonFileFunc.getMapByFileType(fwFiles);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("file", mapFile);
		map2.put("fwComPer", fwComPer);
		return JsonUtils.toJson(RequestUtils.successResult(map2));
	}
	
	
	
	*//**
	 * 根据企业id查询企业控制人配偶详情
	 * @param map
	 * @param model
	 * @return
	 *//*
	@RequestMapping(value = "/getCompanyOwnerSpouseDetail", method = {GET,POST})
	@ResponseBody
	public String getCompanyOwnerSpouseDetail(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		Long comId = MapUtils.getLong(map, "comId");
		//根据企业id和企业相关人的类型查询 查询企业控制人配偶
		FwComPer fwComPer = fwComPerService.findByComIdAndPerType(comId, PersonType.COM_LEGAL);
		if (fwComPer==null) {
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("fwcomaccount.getcompany.ownerspouse.failed")));
		}
		//查询企业控制人的所有文件
		List<FwFiles> fwFiles =  fwFilesService.findByFkIdAndFkType(fwComPer.getPerId(), FkType.FW_BUSINESS_SXD);
		Map<String, Object> mapFile = CommonFileFunc.getMapByFileType(fwFiles);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("file", mapFile);
		map2.put("fwComPer", fwComPer);
		return JsonUtils.toJson(RequestUtils.successResult(map2));
	}
	
	
	 *//**
     * 创建和修改
     * @param map
     * @param model
     * @param request
     * @return
     *//*
	@RequestMapping(value = "/create", method = POST)
	@ResponseBody
	
	public String create(@RequestParam() Map<String,Object> map,Model model ,HttpServletRequest request,HttpServletResponse response) {
		FwComAccount fwComAccount = fwComAccountService.saveComAccount(map);
        if(fwComAccount==null){
        return JsonUtils.toJson(RequestUtils.failResult("error"));
        }
        return JsonUtils.toJson(RequestUtils.successResult(null));
    }*/
	
	
	 
}

