package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.common.user.domain.FwComPerBus;
import com.evan.common.user.domain.FwCompanyBus;
import com.evan.common.user.service.FwBusinessFcdService;
import com.evan.common.user.service.FwComPerBusService;
import com.evan.common.user.service.FwCompanyBusService;
import com.evan.common.utils.ConstantTool.BusinessType;
import com.evan.common.utils.ConstantTool.FkType;
import com.evan.common.utils.ConstantTool.PersonType;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.plugins.dictionary.DictionaryHelper;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.nd.common_file.CommonFileFunc;
import com.evan.nd.common_file.domain.FwFiles;
import com.evan.nd.common_file.service.FwFilesService;

@RequestMapping("/fwBusinessFcd")
@Controller
public class FwBusinessFcdController extends BaseController {

	final Logger logger = LoggerFactory
			.getLogger(FwBusinessFcdController.class);

	@Autowired
	private FwBusinessFcdService fwBusinessFcdService;
	
	
	@Autowired
	private FwFilesService fwFilesService;
	
	@Autowired
	private FwComPerBusService fwComPerBusService;
	
	@Autowired
	private FwCompanyBusService fwCompanyBusService;
	
	@Autowired
	private DictionaryHelper dictionaryHelper;
	
	
	/**
	 * liutt
	 * 点击扶持贷管理
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = GET)
	public String goToBusinessFcd(Model model) {
		//查询所有未读的消息记录列表
		List<Map<String,Object>> fwBussiness = fwBusinessFcdService.getBussinessFcdList();
		model.addAttribute("list", fwBussiness);
		return "fw_business_fcd/list";
	}
	

	/**
	 * 根据企业id查询和业务相关的企业详情
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getfwCompanyBusDetail", method = {GET,POST})
	@ResponseBody
	public String getfwCompanyBusDetail(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		String busId = MapUtils.getString(map, "busId");
		//根据业务id和业务类型查询 和业务相关的企业信息
		FwCompanyBus fwCompanyBus = fwCompanyBusService.findByBusinessIdAndBusinessType(busId, BusinessType.FCD);
		if (fwCompanyBus==null) {
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("fwcomaccount.getcompany.failed")));
		}
		String business = dictionaryHelper.lookupDictValue0("INDUSTRY_TYPE",fwCompanyBus.getBusiness());
		//根据外键id和外键类型查询记录
		List<FwFiles> fwFiles =  fwFilesService.findByBusIdAndfkType(busId,FkType.FW_BUSINESS_SXD);
		//List<FwFiles> fwFiles =  fwFilesService.findByBusIdAndBusTypeAndFkId(busId, FkType.FW_COMPANY_BUS,fwCompanyBus.getCbId());
		fwCompanyBus.setBusiness(business);
		Map<String, Object> mapFile = CommonFileFunc.getMapByFileType(fwFiles);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("file", mapFile);
		map2.put("fwCompanyBus", fwCompanyBus);
		return JsonUtils.toJson(RequestUtils.successResult(map2));
	}
	
	
	/**
	 * 根据企业id查询企业和业务相关的控制人详情
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/fwCompanyBusOwnerDetail", method = {GET,POST})
	@ResponseBody
	public String fwCompanyBusOwnerDetail(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		String busId = MapUtils.getString(map, "busId");
		//根据业务id，业务类型，人的类型，查询企业相关人的信息
		FwComPerBus fwComPer = fwComPerBusService.findByBusinessIdAndType(busId, BusinessType.FCD,PersonType.COM_LEGAL);
		if (fwComPer==null) {
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("fwcomaccount.getcompanyowner.failed")));
		}
		//查询企业控制人的所有文件
		//根据业务id  业务类型 外键id  查询文件
		//TODO
		List<FwFiles> fwFiles =  fwFilesService.findByBusIdAndBusTypeAndFkId(busId, BusinessType.FCD,fwComPer.getId());
		Map<String, Object> mapFile = CommonFileFunc.getMapByFileType(fwFiles);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("file", mapFile);
		map2.put("fwComPerBus", fwComPer);
		return JsonUtils.toJson(RequestUtils.successResult(map2));
	}
	
	
	
	/**
	 * 根据企业id查询企业控制人配偶详情
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/fwCompanyBusOwnerSpouseDetail", method = {GET,POST})
	@ResponseBody
	public String getfwCompanyBusOwnerSpouseDetail(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		String busId = MapUtils.getString(map, "busId");
		//根据业务id，业务类型，人的类型，查询企业相关人的信息
		FwComPerBus fwComPer = fwComPerBusService.findByBusinessIdAndType(busId, BusinessType.FCD,PersonType.COM_LEGAL);
		if (fwComPer==null) {
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("fwcomaccount.getcompanyowner.failed")));
		}
		//查询企业控制人的所有文件
		//根据业务id  业务类型 外键id  查询文件
		//TODO
		List<FwFiles> fwFiles =  fwFilesService.findByBusIdAndBusTypeAndFkId(busId, BusinessType.FCD,fwComPer.getId());
		Map<String, Object> mapFile = CommonFileFunc.getMapByFileType(fwFiles);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("file", mapFile);
		map2.put("fwComPerBus", fwComPer);
		return JsonUtils.toJson(RequestUtils.successResult(map2));
	}
	
}
