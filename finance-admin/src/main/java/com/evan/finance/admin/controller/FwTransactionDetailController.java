package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.evan.common.user.service.FwTransactionDetailService;
import com.evan.jaron.core.web.controller.BaseController;

@RequestMapping("/fwTransactionDetail")
@Controller
public class FwTransactionDetailController extends BaseController {

    final Logger logger = LoggerFactory.getLogger(FwTransactionDetailController.class);

    @Autowired
    private FwTransactionDetailService fwTransactionDetailService;

    /**
     * 交易明细
     *
     * @return
     */
    @RequestMapping( method = {GET, POST})
    public String fwTransactionDetail(Model model) {
    	//查询所有未读的消息记录列表
		List<Map<String,Object>> fwTransactionDetail = fwTransactionDetailService.findAllTransactionList();
		model.addAttribute("list", fwTransactionDetail);
        return "fw_transaction_detail/list";
    }

}

