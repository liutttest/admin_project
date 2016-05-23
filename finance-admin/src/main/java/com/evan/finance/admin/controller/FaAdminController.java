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

import com.evan.common.user.domain.FaAdmin;
import com.evan.common.user.domain.FaRole;
import com.evan.common.user.domain.SysUser;
import com.evan.common.user.service.FaAdminService;
import com.evan.common.user.service.FaDeptService;
import com.evan.common.user.service.FaRoleService;
import com.evan.common.user.service.SysUserService;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.dao.support.QCriteria;
import com.evan.jaron.core.dao.support.QCriteria.Op;
import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.plugins.security.SecurityTokenHolder;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;

@RequestMapping("/faAdmin")
@Controller
public class FaAdminController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FaAdminController.class);	
	
	@Autowired
	private FaAdminService faAdminService;
	
	@Autowired
	private FaDeptService faDeptService;
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private FaRoleService faRoleService;
	
	
	/**
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = GET)
	public String goToFaAdmin(Model model) {
		
		List<FaAdmin> list = faAdminService.findAll();
		List<FaRole> roles = faRoleService.findAll();
		
		model.addAttribute("list", list);
		model.addAttribute("roles", roles);
		return "fa_admin/list";
	}
	
	
	/**
	 * 查询是否可用
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/checkExists", method = {GET,POST})
	@ResponseBody
    public String checkExists(@RequestParam Map<?, ?> map) {
		String userName=MapUtils.getString(map, "userName");
		String userId=MapUtils.getString(map, "userId");
		Integer type=MapUtils.getInteger(map, "type");
		
		QCriteria qcriteria=new QCriteria();
		qcriteria.addEntry("userName", Op.EQ, userName);
		qcriteria.addEntry("type", Op.EQ, type);
		SysUser expressUser = sysUserService.findOne(qcriteria);
		Map<String, Boolean> retMap = new HashMap<String, Boolean>();
		//如果userId为空的话 则证明是添加
		if(StringUtils.isEmpty(userId)){
			//根据用户名查询  判断查询结果 是否为空 如果 为空 则证明 此用户不存在
			if(expressUser==null){
				retMap.put("exist", true);
				return JsonUtils.toJson(RequestUtils.successResult(retMap));
			}else{
				retMap.put("exist", false);
				return JsonUtils.toJson(RequestUtils.successResult(retMap));
			}
		//如果userId不为空的话  则证明是修改
		}else {
			//如果是修改的话  判断 查询结果是否为空
			//如果不为空  则需要 再次判断 查询结果 id与 要修改的用户id是否相等 如果相等则可以修改  如果 不相等说明该用户名已被注册
			if(expressUser!=null){
				if(expressUser.getUserId()==Integer.parseInt(userId)){
					retMap.put("exist", true);
					return JsonUtils.toJson(RequestUtils.successResult(retMap));
				}else{
					retMap.put("exist", false);
					return JsonUtils.toJson(RequestUtils.successResult(retMap));
					
				}
			}else{
				retMap.put("exist", true);
				return JsonUtils.toJson(RequestUtils.successResult(retMap));
			}
		}
	}	
	
	/**二期
	 * 查询现场任务分派可以分配的所有人
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAdminByRole",method = {GET, POST})
	@ResponseBody
	public String listByPage(@RequestParam Map<String,Object> map,@ModelAttribute SearchForm searchForm, Model model,HttpServletRequest request,HttpServletResponse response) {
		List<Map<String, Object>> list = faAdminService.getAdminByRole(null);
	
		return JsonUtils.toJson(RequestUtils.successResult(list));
	}
	
	
	/**
	 * 二期
	 * 查询现场任务分派可以分配的所有人
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAdminByRoleALL",method = {GET, POST})
	@ResponseBody
	public String getAdminByRoleALL(@RequestParam Map<String,Object> map,@ModelAttribute SearchForm searchForm, Model model,HttpServletRequest request,HttpServletResponse response) {
		String node = MapUtils.getString(map, "node");
		// 获取当前登录用户
		Long userId = SecurityTokenHolder.getSecurityToken().getUser().getUserId();
		List<Map<String, Object>> list = faAdminService.getAdminByRoleAll(node,userId);
	
		return JsonUtils.toJson(RequestUtils.successResult(list));
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
		Long userId=MapUtils.getLong(map,"userId");
		FaAdmin faAdmin = faAdminService.findByPk(userId);  
		return JsonUtils.toJson(RequestUtils.successResult(faAdmin));
	}
	
	/**
	 * 跳转到个人中心页面，并且进行信息显示(liutt)
	 * 根据id查出对象信息
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/profile", method = {GET,POST})
	public String profile(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		Long userId=MapUtils.getLong(map,"userId");
		Map<String, Object> mapResult = faAdminService.getByPk(userId); 
		model.addAttribute("user",mapResult);
		return "profile";
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
		FaAdmin faAdmin = faAdminService.creatFaAdmin(map);
        if(faAdmin==null){
        return JsonUtils.toJson(RequestUtils.failResult("error"));
        }
        return JsonUtils.toJson(RequestUtils.successResult(null));
    }
	
	

	 /**
    * 个人中心页面修改个人信息
    * @param map
    * @param model
    * @param request
    * @return
    */
	@RequestMapping(value = "/updateProfile", method = POST)
	@ResponseBody
	public String updateProfile(@RequestParam() Map<String,Object> map) {
		SysUser faAdmin = faAdminService.updateFaAdmin(map);
       if(faAdmin==null){
       return JsonUtils.toJson(RequestUtils.failResult("error"));
       }
       return JsonUtils.toJson(RequestUtils.successResult(null));
   }
	
  /**
    * 禁止用户使用登陆操作
    * @param map
    * @return
    */
	@RequestMapping(value = "/disableAccount", method = POST)
	@ResponseBody
	public String disableAccount(@RequestParam() Map<String,Object> map) {
		Long userId = MapUtils.getLong(map, "userId");
		SysUser faAdmin = faAdminService.updateSysUserIsavailable(userId,0);
       if(faAdmin==null){
       return JsonUtils.toJson(RequestUtils.failResult("error"));
       }
       return JsonUtils.toJson(RequestUtils.successResult(null));
   }
	
	  /**
	    * 恢复用户使用登陆操作
	    * @param map
	    * @return
	    */
		@RequestMapping(value = "/revretAccount", method = POST)
		@ResponseBody
		public String revretAccount(@RequestParam() Map<String,Object> map) {
			Long userId = MapUtils.getLong(map, "userId");
			SysUser faAdmin = faAdminService.updateSysUserIsavailable(userId,1);
	       if(faAdmin==null){
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
		boolean status = faAdminService.removeFaAdmin(ids);
    	if(status){
        return JsonUtils.toJson(RequestUtils.successResult(null));
        }
        return JsonUtils.toJson(RequestUtils.failResult("error"));
    }
}

