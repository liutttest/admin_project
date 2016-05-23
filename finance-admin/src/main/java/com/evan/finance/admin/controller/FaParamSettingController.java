package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.evan.common.user.domain.FaConfigure;
import com.evan.common.user.service.FaConfigureService;
import com.evan.common.utils.Constant;
import com.evan.jaron.core.web.controller.BaseController;

@RequestMapping("/faConfigure/paramSetting")
@Controller
public class FaParamSettingController extends BaseController {

	final Logger logger = LoggerFactory
			.getLogger(FaParamSettingController.class);

	@Autowired
	private FaConfigureService faConfigureService;
	
	@RequestMapping(method = { GET, POST })
	public String list(Model model) {
		List<FaConfigure> list = faConfigureService.findFaConfigureByType(Constant.FA_CONFIGURE_TYPE_PARAM_SETTING);
		for (FaConfigure faConfigure : list) {
			model.addAttribute(faConfigure.getPKey(), faConfigure);
		}
		return "fa_configure/param_setting_list";
	}
	
	@RequestMapping(value = "/edit", method = { GET, POST })
	public String edit(@RequestParam Map<String,Object> map, Model model) {
		
		// TODO 修改数据库数据
		List<FaConfigure> list = faConfigureService.findFaConfigureByType(Constant.FA_CONFIGURE_TYPE_PARAM_SETTING);
		faConfigureService.saveFaConfigure(map,list);
		return "redirect:/faConfigure/paramSetting";
	}
}
