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

import com.evan.common.user.domain.FaTempScene;
import com.evan.common.user.domain.FaTemplate;
import com.evan.common.user.domain.SysUser;
import com.evan.common.user.service.FaTempSceneService;
import com.evan.common.user.service.FaTemplateService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.DateUtils;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.plugins.security.SecurityTokenHolder;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;

@RequestMapping("/faSmsTemplate")
@Controller
public class FaSmsTemplateController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FaSmsTemplateController.class);	
	
	@Autowired
	private FaTemplateService faTemplateService;
	
	@Autowired
	private FaTempSceneService FaTempSceneService;
	
	@RequestMapping(method = GET)
	public String list(Model model) {
		List<FaTemplate> list = faTemplateService.findByTemplateType("1");
		model.addAttribute("list", list);
		List<FaTempScene> faTempScenes = FaTempSceneService.findAll();
		model.addAttribute("faTempScenes", faTempScenes);
		
		Map<String, String> faTempSceneMap = new HashMap<String, String>();
		for (FaTempScene faTempScene : faTempScenes) {
			faTempSceneMap.put(faTempScene.getSceneKey(), faTempScene.getSceneName());
		}
		model.addAttribute("faTempSceneMap", faTempSceneMap);
		
		return "fa_sms_template/list";
	}
	
	/**
	 * 根据id查出对象信息
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/update", method = {GET,POST})
	@ResponseBody
	public String update(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		Long deptId = MapUtils.getLong(map,"tid");
		FaTemplate faTemplate = faTemplateService.findByPk(deptId);
		return JsonUtils.toJson(RequestUtils.successResult(faTemplate));
	}
	
	@RequestMapping(value = "/create", method = POST)
	@ResponseBody
	public String create(@RequestParam() Map<String,Object> map) {
		String tempName = MapUtils.getString(map, "tempName");
		String tempSceneName = MapUtils.getString(map, "tempSceneName");
		String isDefault = MapUtils.getString(map, "isDefault");
		String title = MapUtils.getString(map, "title");
		String content = MapUtils.getString(map, "content");
		String tid = MapUtils.getString(map, "tid");
		
		FaTemplate faTemplate = new FaTemplate();
		faTemplate.setTempName(tempName);
		FaTempScene faTempScene = FaTempSceneService.findByPk(Long.valueOf(tempSceneName));
		faTemplate.setTempSceneName(faTempScene.getSceneName());
		faTemplate.setTempSceneId(faTempScene.getTsId());
		faTemplate.setIsDefault(isDefault);
		faTemplate.setTitle(title);
		faTemplate.setContent(content);
		SysUser sysUser = (SysUser) SecurityTokenHolder.getSecurityToken().getUser();
		faTemplate.setCreateUserid(sysUser.getUserId());
		faTemplate.setCreateTime(DateUtils.getCurrentDateTime(ConstantTool.DATA_FORMAT));
		faTemplate.setType("1");
		
		if (tid != null && tid.trim().length() > 0) {
			faTemplate.setTid(Long.valueOf(tid));
		}
		
		// 将之前默认状态设置为非默认
		if ("1".equals(isDefault)) {
			FaTemplate template = faTemplateService.findDefaultBySceneId(faTemplate.getTempSceneId(), faTemplate.getType());
			if (template != null) {
				template.setIsDefault("0");
				faTemplateService.save(template);
			}
		}
		
		FaTemplate faTemplate1 = faTemplateService.save(faTemplate);
        if(faTemplate1==null){
        	return JsonUtils.toJson(RequestUtils.failResult("error"));
        }
        return JsonUtils.toJson(RequestUtils.successResult(null));
    }
	
	/**
	 * 删除
	 * @param tid
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "/remove", method = POST)
    @ResponseBody
    public String remove(@RequestParam("tid") String tid) {
    	if (tid == null || tid.trim().length() == 0) {
    		return JsonUtils.toJson(RequestUtils.failResult("error"));
    	}
    	
    	//  错误提示信息
    	String errorMsg = "";
    	
    	// 根据tid查询当前的模版
    	FaTemplate faTemplate = faTemplateService.findByPk(Long.valueOf(tid));
    
    	// 根据类型和场景获取所有的模版
    	List<FaTemplate> list = faTemplateService.findBySceneId(faTemplate.getTempSceneId(), faTemplate.getType()); 
 
    	// 如果是最后一条，返回不能删除的提示
    	if (list != null && list.size() == 1)
    	{
    		errorMsg = getMessage("faTemplate.lastTemplate.failed");
    		errorMsg = errorMsg.replaceAll("#", faTemplate.getTempSceneName());
    		return JsonUtils.toJson(RequestUtils.failResult(errorMsg));
    	}
    	
    	// 如果不是最后一条，删除
		boolean status = faTemplateService.removeFaTemplate(tid);
		if (status) {
			return JsonUtils.toJson(RequestUtils.successResult(null));
		}
		return JsonUtils.toJson(RequestUtils.failResult("error"));
    }

    /**
	 * 设置为默认
	 * @param tid
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "/setDefault", method = POST)
    @ResponseBody
    public String setDefault(@RequestParam("tid") String tid) {
    	if (tid == null || tid.trim().length() == 0) {
    		return JsonUtils.toJson(RequestUtils.failResult("error"));
    	}
    	// 取出当前项
    	FaTemplate faTemplate = faTemplateService.findByPk(Long.valueOf(tid));
    	
    	// 修改之前为默认的项
    	FaTemplate template = faTemplateService.findDefaultBySceneId(faTemplate.getTempSceneId(), faTemplate.getType());
		if (template != null) {
			template.setIsDefault("0");
			faTemplateService.save(template);
		} 
    	
		faTemplate.setIsDefault("1");
		
		FaTemplate t = faTemplateService.save(faTemplate);
		if ("1".equals(t.getIsDefault())) {
			return JsonUtils.toJson(RequestUtils.successResult(null));
		}
		return JsonUtils.toJson(RequestUtils.failResult("error"));
    }
}

