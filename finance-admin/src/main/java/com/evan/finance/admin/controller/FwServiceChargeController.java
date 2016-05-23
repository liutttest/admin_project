package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.common.user.domain.FaCommunicateLog;
import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.service.FaCommunicateLogService;
import com.evan.common.user.service.FaInvoiceMailService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.RequestUtils;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;

@RequestMapping("/fwServiceCharge")
@Controller
public class FwServiceChargeController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FwServiceChargeController.class);	
	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	@Autowired
	private FaCommunicateLogService faCommunicateLogService;
	@Autowired
	private FaInvoiceMailService faInvoiceMailService;
	
	/**
	 * 跳转到'财务管理-服务费入账'页面(luy)
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/finan",method = {GET, POST})
	public String toServiceChargeList(Model model) {
		//查询所有未读的消息记录列表
		List<FwBusinessSxd> fwBusinessSxds = null;
		try {
			fwBusinessSxds = fwBusinessSxdService.findForServiceCharge();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("list", fwBusinessSxds);
		return "fw_service_charge/list";
	}
	
	
	
	/**
	 * '服务费入账'页面，工作人员确认收款，修改业务服务费缴纳状态(luy)
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/doConfirmBank",method = {GET, POST})
	public String doConfirmBank(@RequestParam Map<String,Object> map, Model model) {
		//得到当前登陆用户id
    	Long userId = RequestUtils.getLoginedUser().getUserId();
		String bsinessId = MapUtils.getString(map, "bsinessId");
		String businessType = MapUtils.getString(map, "businessType");
		if (!StringUtils.isEmpty(bsinessId)) {
			FwBusinessSxd businessSxd = fwBusinessSxdService.findByPk(bsinessId);
			FwBusinessSxd newBusiSxd = null;
			if (businessSxd!=null) {
				businessSxd.setServiceChargeState("01");
				businessSxd.setUpdateTime(new SimpleDateFormat(ConstantTool.DATA_FORMAT).format(new Date()));
				businessSxd.setUpdateUserid(userId);
				newBusiSxd = fwBusinessSxdService.save(businessSxd);
			}
			
			if (newBusiSxd != null) {
				if (!StringUtils.isEmpty(newBusiSxd.getIsInvoicing()) && "02".equals(newBusiSxd.getIsInvoicing())) {
					faInvoiceMailService.saveFaInvoiceMail(bsinessId, businessType);
				}
			}
		}
		return "redirect:/fwServiceCharge/finan";
	}
	
	/**
	 * 增加电话催缴服务费记录(luy)
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/doCuiJiao",method = {GET, POST})
	@ResponseBody
	public String doCuiJiao(@RequestParam Map<String,Object> map, Model model) {
		//得到当前登陆用户id
//    	Long userId = RequestUtils.getLoginedUser().getUserId();
		String businessId = MapUtils.getString(map, "businessId");
		String businessType = MapUtils.getString(map, "businessType");
		String type = "03";
		String content = MapUtils.getString(map, "content");
		
		FaCommunicateLog retLog = faCommunicateLogService.saveFaCommunicateLog(businessId, businessType, type, content);
		
		if (retLog != null) {
            return JsonUtils.toJson(RequestUtils.successResult(retLog));
        } else {
            return JsonUtils.toJson(RequestUtils.failResult(""));
        }
	}
	
	/**
	 * 查询 业务的催缴服务通知  记录
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/findCommLog",method = {GET, POST})
	@ResponseBody
	public String findCommLog(@RequestParam Map<String,Object> map, Model model) {
		//得到当前登陆用户id
//    	Long userId = RequestUtils.getLoginedUser().getUserId();
		String businessId = MapUtils.getString(map, "businessId");
		String type = "03";
		List<FaCommunicateLog> communicateLogs = faCommunicateLogService.findByBusinessIdAndType(businessId, type);
        return JsonUtils.toJson(RequestUtils.successResult(communicateLogs));
	}
	
	
	
	/**
	 * 跳转到'业务管理-服务费管理'页面(luy)
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/business",method = {GET, POST})
	public String toBusinessServiceChargeList(Model model) {
		//查询所有未读的消息记录列表
		List<FwBusinessSxd> fwBusinessSxds = null;
		try {
			fwBusinessSxds = fwBusinessSxdService.findForServiceCharge();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		model.addAttribute("list", fwBusinessSxds);
		return "fw_business_scl/list";
	}
}

