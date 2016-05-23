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

import com.evan.common.user.domain.FaDept;
import com.evan.common.user.domain.FaFunc;
import com.evan.common.user.domain.FaRole;
import com.evan.common.user.domain.FaRoleFunc;
import com.evan.common.user.service.FaDeptService;
import com.evan.common.user.service.FaFuncService;
import com.evan.common.user.service.FaRoleFuncService;
import com.evan.common.user.service.FaRoleService;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.dao.support.QCriteria;
import com.evan.jaron.core.dao.support.QCriteria.Op;
import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;

@RequestMapping("/faRole")
@Controller
public class FaRoleController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FaRoleController.class);	
	
	@Autowired
	private FaRoleService faRoleService;
	
	@Autowired
	private FaFuncService faFuncService;
	
	@Autowired
	private FaRoleFuncService faRoleFuncService;
	
	@Autowired
	private FaDeptService faDeptService;
	
	
	/**
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = GET)
	public String goToFaAdmin(Model model) {
		List<FaRole> listRole = faRoleService.findAll();
		List<FaDept> listDept =  faDeptService.getAll();
		
		model.addAttribute("list", listRole);
		model.addAttribute("depts", listDept);
		return "fa_role/list";
	}
	
	/**
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAllFunc",method = {GET, POST})
	@ResponseBody
	public String getAllFunc(@ModelAttribute SearchForm searchForm, Model model,HttpServletRequest request,HttpServletResponse response) {
		List<FaFunc> funcs = faFuncService.findAll();
		return JsonUtils.toJson(RequestUtils.successResult(funcs));
	}

	
	/**
	 * 根据id查出对象信息
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getByBk", method = {GET,POST})
	@ResponseBody
	public String update(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		Long roleId=MapUtils.getLong(map,"roleId");
		FaRole faRole = faRoleService.findByPk(roleId);
		
		QCriteria criteria = new QCriteria();
        criteria.addEntry("roleId", Op.EQ, roleId);
        
        List<FaRoleFunc> faRoleFuncs =faRoleFuncService.find(criteria);   //拿到被修改的角色的权限
        List<FaFunc> faFuncs = faFuncService.findAll();
        
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("faRoleFuncs", faRoleFuncs);
        map2.put("faFuncs", faFuncs);
        map2.put("faRole", faRole);
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
	
	public String create(@RequestParam() Map<String,Object> map,Model model ,HttpServletRequest request,HttpServletResponse response) {
		
		FaRole faRole = faRoleService.creatFaRole(map);
        if(faRole==null){
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
    public String remove(@RequestParam("ids") String ids, Model model,HttpServletResponse response) {
		boolean status = faRoleService.removeFaRole(ids);
    	if(status){
        return JsonUtils.toJson(RequestUtils.successResult(null));
        }
        return JsonUtils.toJson(RequestUtils.failResult("error"));
    }
}

