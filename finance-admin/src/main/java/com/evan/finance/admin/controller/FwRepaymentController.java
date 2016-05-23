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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.common.user.domain.FwCompany;
import com.evan.common.user.domain.FwRepayment;
import com.evan.common.user.service.FwCompanyService;
import com.evan.common.user.service.FwRepaymentService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.bank.BankTools;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.plugins.dictionary.DictionaryHelper;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.nd.common_file.CommonFileFunc;
import com.evan.nd.common_file.domain.FwFiles;
import com.evan.nd.common_file.service.FwFilesService;

@RequestMapping("/fwRepayment")
@Controller
public class FwRepaymentController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FwRepaymentController.class);	
	
	@Autowired
	private FwRepaymentService fwRepaymentService;
	
	@Autowired
	private FwCompanyService fwCompanyService;
	
	@Autowired
	private FwFilesService filesService;
	
	@Autowired
	private BankTools bankTools;
	
	
	@Autowired
	private DictionaryHelper dictionaryHelper;
	/**
	 * 点击申请还款
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = GET)
	public String goToFaAdmin(Model model) {
		//查询所有未读的消息记录列表
		List<Map<String,Object>> listFwRepayment = fwRepaymentService.getRepayMentList();
		model.addAttribute("list", listFwRepayment);
		return "fw_repayment/list";
	}
	
	/**
	 * 
	 * @param map
	 * @param model
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/doSendRepay", method = {GET,POST})
	public String doSendRepay(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		
		String busId = MapUtils.getString(map, "businessId"); // 业务id
		String repaymentMoney = MapUtils.getString(map, "repaymentMoney"); // 还款金额
		String loanNo = MapUtils.getString(map, "loanNo"); // 借据号
		String fullFlag = MapUtils.getString(map, "fullFlag"); // 是否全额还款
		
		Map<String, Object> paramMap = fwRepaymentService.repaymentMap(busId, repaymentMoney, loanNo, fullFlag);
		
		Map<String, Object> retRsuMap = null;
		try {
			retRsuMap = bankTools.applyFinancingRepay(paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (retRsuMap != null) {
			
		}
		
		Map<String, Object> retMap = new HashMap<String, Object>();
//		if (newBus != null) {
        return JsonUtils.toJson(RequestUtils.successResult(retMap));
//        } else {
//            return JsonUtils.toJson(RequestUtils.failResult(""));
//        }
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
		Long rId=MapUtils.getLong(map,"rId");
		FwRepayment fwRepayment = fwRepaymentService.findByPk(rId);
		FwCompany fwCompany = fwCompanyService.findByPk(fwRepayment.getComId());
		if (fwCompany==null) {
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("fwcomaccount.getcompany.failed")));
		}
		//String business = RequestUtils.getCompanyBusiness(fwCompany.getBusiness());
		List<FwFiles> fwFiles =  filesService.findByFkIdAndFkType(fwCompany.getComId(),ConstantTool.FkType.FW_BUSINESS_SXD);
		//fwCompany.setBusiness(business);
		Map<String, Object> mapFile = CommonFileFunc.getMapByFileType(fwFiles);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("file", mapFile);
		map2.put("fwCompany", fwCompany);
		return JsonUtils.toJson(RequestUtils.successResult(map2));
	}
	
	
	
	/**
	 * 在待办任务 放款列表 点击 放款记录
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = POST,value="repaymentApplyList")
	@ResponseBody
	public String loanApplyList(Model model,@RequestParam Map<String, Object> map) {
		Long rId = MapUtils.getLong(map, "rId");
		FwRepayment FwRepayment = fwRepaymentService.findByPk(rId);
		List<FwRepayment> list = fwRepaymentService.getRepayMentListByComId(FwRepayment.getComId());
		List<FwRepayment> list2 = dictionaryHelper.decodeBeans(list);
		return JsonUtils.toJson(RequestUtils.successResult(list2));
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
		FwRepayment fwRepayment = fwRepaymentService.creatFwRepayment(map);
        if(fwRepayment==null){
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
		boolean status = fwRepaymentService.removeFwRepayment(ids);
    	if(status){
        return JsonUtils.toJson(RequestUtils.successResult(null));
        }
        return JsonUtils.toJson(RequestUtils.failResult("error"));
    }
}

