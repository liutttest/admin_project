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

import com.evan.common.user.domain.FwMessage;
import com.evan.common.user.service.FwMessageService;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.dao.support.Page;
import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;

@RequestMapping("/fwMessage")
@Controller
public class FwMessageController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FwMessageController.class);	
	
	@Autowired
	private FwMessageService fwMessageService;
	
	
	/**
	 * 点击投诉建议
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = GET)
	public String goToFaAdmin(Model model) {
		//查询所有未读的消息记录列表
		List<Map<String,Object>> listFwFeedback = fwMessageService.getMessageListByState(0);
		model.addAttribute("list", listFwFeedback);
		return "fw_message/list";
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
		/*String productType=request.getParameter("selectword");
		String name=request.getParameter("keyword");
		Map map2=new HashMap();
		//map2.put("name"+searchForm.OP_LIKE_SUFFIX, name);
		//map2.put("prodCatalog"+searchForm.OP_EQ_SUFFIX, productType);
		searchForm.setForm(map2);*/
		searchForm.setPageNo(Integer.parseInt((String) map.get("pageNum")));
		searchForm.setPageSize(Integer.parseInt((String) map.get("pageSize")));
		searchForm.setOrderBy("fmId desc");
		Page<FwMessage> fwMessagePage = fwMessageService.findPage(searchForm);
	
		return JsonUtils.toJson(RequestUtils.successResult(fwMessagePage));
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
		boolean status = fwMessageService.removeFwMessage(ids);
    	if(status){
        return JsonUtils.toJson(RequestUtils.successResult(null));
        }
        return JsonUtils.toJson(RequestUtils.failResult("error"));
    }
}

