package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.evan.common.user.domain.FwSms;
import com.evan.common.user.service.FwSmsService;
import com.evan.jaron.core.dao.support.QCriteria;
import com.evan.jaron.core.dao.support.Sort;
import com.evan.jaron.core.web.controller.BaseController;

@RequestMapping("/fwSms")
@Controller
public class FwSmsController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FwSmsController.class);	
	
	@Autowired
	private FwSmsService fwSmsService;
	
	
	/**
	 * 点击短信管理
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = GET)
	public String goToFaAdmin(Model model) {
		//查询所有未读的消息记录列表
		Sort sort = new Sort();
		sort.desc("smsId");
		List<FwSms> fwSms = fwSmsService.find(sort, new QCriteria());
		model.addAttribute("list", fwSms);
		return "fw_sms/list";
	}
	
}

