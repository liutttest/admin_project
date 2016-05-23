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

import com.evan.common.user.domain.FwFeedback;
import com.evan.common.user.service.FwFeedbackService;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.dao.support.Page;
import com.evan.jaron.core.dao.support.QCriteria;
import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.dao.support.Sort;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.plugins.dictionary.DictionaryHelper;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;

@RequestMapping("/fwFeedback")
@Controller
public class FwFeedbackController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FwFeedbackController.class);	
	
	@Autowired
	private FwFeedbackService fwFeedbackService;
	
	@Autowired
	private DictionaryHelper dictionaryHelper;
	
	
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
		
		QCriteria criteria = new QCriteria();
		
		Sort sort = new Sort();
		sort.desc("createTime");
		List<FwFeedback> listFwFeedback = fwFeedbackService.find(sort, criteria);
		
		//List<Map<String,String>> list = dictionaryHelper.decodeBeansToMaps(listFwFeedback);
		model.addAttribute("list", listFwFeedback);
		return "fw_feedback/list";
	}
	
	
	
	/**
	 * 点击处理
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = POST,value="/updateState")
	@ResponseBody
	public String fwFeedBackDone(@RequestParam Map<String,Object> map,Model model) {
		Long feedId = MapUtils.getLong(map, "feedId");
		String remark = MapUtils.getString(map, "remark");
		FwFeedback feedback = fwFeedbackService.findByPk(feedId);
		feedback.setFeedState("02");;
		FwFeedback feedback2 =fwFeedbackService.save(feedback);
		if (feedback2!=null) {
			return JsonUtils.toJson(RequestUtils.successResult(null));
		}else{
			return JsonUtils.toJson(RequestUtils.failResult("处理失败"));
		}
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
		searchForm.setOrderBy("fdId desc");
		Page<FwFeedback> fwFeedbackPage = fwFeedbackService.findPage(searchForm);
	
		return JsonUtils.toJson(RequestUtils.successResult(fwFeedbackPage));
	}
	
	
	
	
	/**
	 * 根据意见反馈id查询意见反馈详情(liutt)
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getDetail",method = {GET, POST})
	@ResponseBody
	public String getDetail(@RequestParam Map<String,Object> map){
		Long fbId = MapUtils.getLong(map, "fbId");
		FwFeedback fwFeedback  = fwFeedbackService.findByPk(fbId);
		fwFeedback.setIssueType(dictionaryHelper.lookupDictValue0("FEED_TYPE",fwFeedback.getIssueType()));
		return JsonUtils.toJson(RequestUtils.successResult(fwFeedback));
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
		boolean status = fwFeedbackService.removeFwFeedback(ids);
    	if(status){
        return JsonUtils.toJson(RequestUtils.successResult(null));
        }
        return JsonUtils.toJson(RequestUtils.failResult("error"));
    }
}

