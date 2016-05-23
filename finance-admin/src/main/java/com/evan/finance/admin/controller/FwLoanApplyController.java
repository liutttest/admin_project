package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.domain.FwLoanApply;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwCompanyService;
import com.evan.common.user.service.FwLoanApplyService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.ConstantTool.LoanApplyState;
import com.evan.common.utils.DateUtils;
import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.activiti.bean.LoanFlowView;
import com.evan.finance.admin.activiti.service.WorkFlowLoanService;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.plugins.dictionary.DictionaryHelper;
import com.evan.jaron.plugins.security.SecurityTokenHolder;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.nd.common_file.service.FwFilesService;

@RequestMapping("/fwLoanApply")
@Controller
public class FwLoanApplyController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FwLoanApplyController.class);	
	
	@Autowired
	private FwLoanApplyService fwLoanApplyService;
	
	@Autowired
	private FwCompanyService fwCompanyService;
	
	@Autowired
	private FwFilesService filesService;
	
	@Autowired
	private DictionaryHelper dictionaryHelper;
	
	@Autowired
	private WorkFlowLoanService workFlowLoanService;
	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	
	/**
	 * 点击申请放款
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = GET)
	public String goToLoanApply(Model model) {
		//查询所有未读的消息记录列表
		List<Map<String,Object>> listFwFeedback = fwLoanApplyService.getLoanApplyList();
		model.addAttribute("list", listFwFeedback);
		return "fw_loanapply/list";
	}
	
	
	/**
	 * 在待办任务 放款列表 点击 详情
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	/*@RequestMapping(method = POST,value="getcompanyDetail")
	@ResponseBody
	public String getcompanyDetail(Model model,@RequestParam Map<String, Object> map) {
		Long blId = MapUtils.getLong(map, "flId");
		FwLoanApply fwLoanApply = fwLoanApplyService.findByPk(blId);
		FwCompany fwCompany = fwCompanyService.findByPk(fwLoanApply.getComId());
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
		map2.put("fwLoanApply", fwLoanApply);
		return JsonUtils.toJson(RequestUtils.successResult(map2));
	}*/
	
	
	
	/**
	 * 在待办任务 放款列表 点击 放款记录
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	/*@RequestMapping(method = POST,value="loanApplyList")
	@ResponseBody
	public String loanApplyList(Model model,@RequestParam Map<String, Object> map) {
		Long blId = MapUtils.getLong(map, "flId");
		FwLoanApply fwLoanApply = fwLoanApplyService.findByPk(blId);
		List<FwLoanApply> list = fwLoanApplyService.getLoanApplyListByComId(fwLoanApply.getComId());
		List<FwLoanApply> list2 = dictionaryHelper.decodeBeans(list);
		return JsonUtils.toJson(RequestUtils.successResult(list2));
	}*/
	
	
	
	//=====================================================================二期

    /**
     * 保存放款信息
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/saveFcdLoansInfo", method = POST)
    @ResponseBody
    public String saveFcdLoansInfo(@RequestParam() Map<String, Object> map) {
        /*Long comId = ((SysUser) SecurityTokenHolder.getSecurityToken()
                .getUser()).getComId();*/
        Long userId = SecurityTokenHolder.getSecurityToken().getUser()
                .getUserId();
        String businessType = MapUtils.getString(map, "businessType", "");// 业务类型
        String businessId = MapUtils.getString(map, "businessId", "");// 业务Id
        double yearRate = MapUtils.getDouble(map, "yearRate", 0.00);// 年利率
        double appMoney = MapUtils.getDouble(map, "appMoney", 0.00);// 申请金额
        Integer monthsCount = MapUtils.getInteger(map, "monthsCount");//贷款时长
        FwLoanApply fwLoanApply = new FwLoanApply();
        FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(businessId);
        fwLoanApply.setComId(fwBusinessSxd.getComId());
        fwLoanApply.setBusinessType(businessType);
        fwLoanApply.setBusinessId(businessId);
        fwLoanApply.setMonthsCount(monthsCount);
        fwLoanApply.setAppMoney(new BigDecimal(appMoney));
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,monthsCount);
        date = calendar.getTime();

        SimpleDateFormat df=new SimpleDateFormat(ConstantTool.DATA_FORMAT);


        fwLoanApply.setExpitedTime(df.format(date));
        fwLoanApply.setApplySource("01");

        fwLoanApply.setYearRate(new BigDecimal(yearRate));
        fwLoanApply.setCreateTime(DateUtils
                .getCurrentDateTime(ConstantTool.DATA_FORMAT));
        fwLoanApply.setCreateUserid(userId);
        fwLoanApply.setUpdateUserid(userId);
        fwLoanApply.setUpdateTime(DateUtils
                .getCurrentDateTime(ConstantTool.DATA_FORMAT));

        FwLoanApply newFwLoanApply;
        fwLoanApply.setState(ConstantTool.LoanApplyState.APPLYING_SUBMIT_FAIL);
        newFwLoanApply = fwLoanApplyService.save(fwLoanApply);


        if (newFwLoanApply == null) {
            return JsonUtils.toJson(RequestUtils.failResult(null));

        }
        final Long loanApplyId = fwLoanApply.getLoanId();

        newFwLoanApply = fwLoanApplyService.saveAndChangeUsableMoney(fwLoanApply);
    	LoanFlowView loanView = workFlowLoanService.startProcess(loanApplyId.toString());
    	/*if (loanView!=null) {
    		newFwLoanApply.setState(LoanApplyState.APPLYING);
    		fwLoanApplyService.save(newFwLoanApply);
    	}*/
        if (newFwLoanApply == null && loanView == null) {
            return JsonUtils.toJson(RequestUtils.failResult(null));
        } else {
            return JsonUtils.toJson(RequestUtils.successResult(newFwLoanApply));
        }
    }
	

}

