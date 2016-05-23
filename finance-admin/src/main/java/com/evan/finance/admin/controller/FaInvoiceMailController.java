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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.common.user.domain.FaInvoiceMail;
import com.evan.common.user.service.FaInvoiceMailService;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.dao.support.Page;
import com.evan.jaron.core.dao.support.QCriteria;
import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.dao.support.Sort;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;

@RequestMapping("/faInvoiceMail")
@Controller
public class FaInvoiceMailController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FaInvoiceMailController.class);	
	
	@Autowired
	private FaInvoiceMailService faInvoiceMailService;
	
	@RequestMapping(method = {GET, POST})
	public String list(@ModelAttribute SearchForm searchForm, Model model) {
		QCriteria qCriteria = new QCriteria();
		Sort sort = new Sort();
		sort.desc("createTime");
		List<FaInvoiceMail> list = faInvoiceMailService.find(sort, qCriteria);
		model.addAttribute("list", list);
		return "faInvoiceMail/list";
	}
	
	/**
	 * 查询列表方法
	 * @param map
	 * @param searchForm
	 * @return
	 */
	@RequestMapping(value="/list",method = {GET, POST})
	@ResponseBody
	public String listByPage(@RequestParam Map map,@ModelAttribute SearchForm searchForm) {
		String productType = MapUtils.getString(map,"selectword");
		String name = MapUtils.getString(map,"keyword");
		Map map2 = new HashMap();
		// map2.put("name"+searchForm.OP_LIKE_SUFFIX, name);
		// map2.put("prodCatalog"+searchForm.OP_EQ_SUFFIX, productType);
		searchForm.setForm(map2);
		//searchForm.setPageNo(Integer.parseInt((String) map.get("pageNum")));
		//searchForm.setPageSize(Integer.parseInt((String) map.get("pageSize")));
		searchForm.setOrderBy("id desc");
		Page<FaInvoiceMail> faInvoiceMailPage = faInvoiceMailService.findPage(searchForm);
		return JsonUtils.toJson(RequestUtils.successResult(faInvoiceMailPage));
		
	}

	
	/**
	 * 根据id查出对象信息
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/update", method = {GET,POST})
	@ResponseBody
	public String update(@RequestParam Map map,Model model,HttpServletResponse response) {
		Long id = MapUtils.getLong(map,"id");
		FaInvoiceMail faInvoiceMail = faInvoiceMailService.findByPk(id);
		return JsonUtils.toJson(RequestUtils.successResult(faInvoiceMail));
	}
	
	
	 /**
     * 开票邮寄
     * @param map
     * @param model
     * @param request
     * @return
     */
	@RequestMapping(value = "/create", method = POST)
	@ResponseBody
	
	public String create(@RequestParam() Map map) {
		FaInvoiceMail faInvoiceMail = faInvoiceMailService.updateFaInvoiceMail(map);
        if(faInvoiceMail==null){
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
		boolean status = faInvoiceMailService.removeFaInvoiceMail(ids);
		if (status) {
			return JsonUtils.toJson(RequestUtils.successResult(null));
		}
		return JsonUtils.toJson(RequestUtils.failResult("error"));
    }
	
}

