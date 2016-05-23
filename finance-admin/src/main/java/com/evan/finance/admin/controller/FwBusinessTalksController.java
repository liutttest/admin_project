package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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

import com.evan.common.user.domain.FwBusinessTalks;
import com.evan.common.user.service.FwBusinessTalksService;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.dao.support.Page;
import com.evan.jaron.core.dao.support.QCriteria;
import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.dao.support.Sort;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.plugins.dictionary.DictionaryHelper;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;

@RequestMapping("/fwBusinessTalks")
@Controller
public class FwBusinessTalksController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FwBusinessTalksController.class);	
	
	@Autowired
	private FwBusinessTalksService fwBusinessTalksService;
	
	@Autowired
	private DictionaryHelper dictionaryHelper;
	
	
	
	/**
	 * 点击快速申请贷款
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = GET)
	public String goToFaAdmin(Model model) {
		
		QCriteria criteria = new QCriteria();
		
		Sort sort = new Sort();
		sort.desc("createTime");
		List<FwBusinessTalks> listFwBusinessTalks = fwBusinessTalksService.find(sort, criteria);
		//List<Map<String,String>> list = dictionaryHelper.decodeBeansToMaps(listFwBusinessTalks);
		model.addAttribute("list", listFwBusinessTalks);
		return "fw_businesstalk/list";
	}
	
	/**
	 * unused
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/list",method = {GET, POST})
	@ResponseBody
	public String listByPage(@RequestParam Map<String,Object> map,@ModelAttribute SearchForm searchForm, Model model,HttpServletRequest request,HttpServletResponse response) {
		//String productType=request.getParameter("selectword");
		//String name=request.getParameter("keyword");
		//Map map2=new HashMap();
		//map2.put("name"+searchForm.OP_LIKE_SUFFIX, name);
		//map2.put("prodCatalog"+searchForm.OP_EQ_SUFFIX, productType);
		//searchForm.setForm(map2);
		searchForm.setPageNo(Integer.parseInt((String) map.get("pageNum")));
		searchForm.setPageSize(Integer.parseInt((String) map.get("pageSize")));
		searchForm.setOrderBy("btId desc");
		Page<FwBusinessTalks> fwBusinessTalksPage = fwBusinessTalksService.findPage(searchForm);
	
		return JsonUtils.toJson(RequestUtils.successResult(fwBusinessTalksPage));
	}

	
	/**
	 * 
	 * 根据id查出对象信息
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getByBk", method = {GET,POST})
	@ResponseBody
	public String update(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		Long btId=MapUtils.getLong(map,"btId");
		FwBusinessTalks fwBusinessTalks = fwBusinessTalksService.findByPk(btId);
		return JsonUtils.toJson(RequestUtils.successResult(fwBusinessTalks));
	}
	
	
   /**
	 *unused 
     * 创建和修改
     * @param map
     * @param model
     * @param request
     * @return
     */
	@RequestMapping(value = "/create", method = POST)
	@ResponseBody
	
	public String create(@RequestParam() Map<String,Object> map,Model model ,HttpServletRequest request,HttpServletResponse response) {
		FwBusinessTalks fwBusinessTalks = fwBusinessTalksService.creatFwBusinessTalks(map);
        if(fwBusinessTalks==null){
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
		boolean status = fwBusinessTalksService.removeFwBusinessTalks(ids);
    	if(status){
        return JsonUtils.toJson(RequestUtils.successResult(null));
        }
        return JsonUtils.toJson(RequestUtils.failResult("error"));
    }
}

