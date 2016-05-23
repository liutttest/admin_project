package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.evan.common.user.domain.FaDept;
import com.evan.common.user.domain.FaRole;
import com.evan.common.user.service.FaDeptService;
import com.evan.common.user.service.FaRoleService;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;

@RequestMapping("/faDept")
@Controller
public class FaDeptController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FaDeptController.class);	
	
	@Autowired
	private FaDeptService faDeptService;
	
	@Autowired
	private FaRoleService faRoleService;
	
	@RequestMapping(method = {GET, POST})
	public String list(@ModelAttribute SearchForm searchForm, Model model) {
		List<FaDept> list =  new ArrayList<FaDept>();
		List<FaDept> depts = faDeptService.getAll();
		for (FaDept faDept : depts) {
			List<FaRole> roles = faRoleService.getFaRolesByDeptId(faDept.getDeptId());
			faDept.setRoles(roles);
			list.add(faDept);
		}
		model.addAttribute("depts", list);		
		return "faDept/list";
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
		Long deptId = MapUtils.getLong(map,"deptId");
		FaDept faDept = faDeptService.findByPk(deptId);
		return JsonUtils.toJson(RequestUtils.successResult(faDept));
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
	
	public String create(@RequestParam() Map<String,Object> map) {
		FaDept faDept = faDeptService.creatFaDept(map);
        if(faDept==null){
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
		boolean status = faDeptService.removeFaDept(ids);
		if (status) {
			return JsonUtils.toJson(RequestUtils.successResult(null));
		}
		return JsonUtils.toJson(RequestUtils.failResult("error"));
    }
	
}

