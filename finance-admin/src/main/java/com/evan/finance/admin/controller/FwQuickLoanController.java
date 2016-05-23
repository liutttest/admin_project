package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evan.common.user.domain.FaCommunicateLog;
import com.evan.common.user.domain.FwQuickLoan;
import com.evan.common.user.domain.JPage;
import com.evan.common.user.domain.SysUser;
import com.evan.common.user.service.FaCommunicateLogService;
import com.evan.common.user.service.FwQuickLoanService;
import com.evan.common.user.service.SysUserService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.activiti.bean.WorkFlow;
import com.evan.finance.admin.utils.WorkFlowUtils;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.plugins.dictionary.DictionaryHelper;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;

@RequestMapping("/fwQuickLoan")
@Controller
public class FwQuickLoanController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FwQuickLoanController.class);	
	
	@Autowired
	private FwQuickLoanService fwQuickLoanService;
	
	@Autowired
	private FaCommunicateLogService faCommunicateLogService;
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private DictionaryHelper dictionaryHelper;
	
	/**
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = GET)
	public String goToFaFwQuickLoan() {
		return "fw_quickloan/list";
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
	public JPage listByPage(JPage page) {
		//调用查询系统任务列表的接口
		return fwQuickLoanService.findAllQuickLoan(page);
	}
	
	
	/**
	 * 根据快速申请贷款id查询快速贷款申请详情(liutt)
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getDetail",method = {GET, POST})
	@ResponseBody
	public String getDetail(@RequestParam Map<String,Object> map){
		Long loanId = MapUtils.getLong(map, "loanId");
		FwQuickLoan fwQuickLoan  = fwQuickLoanService.findByPk(loanId);
		return JsonUtils.toJson(RequestUtils.successResult(fwQuickLoan));
	}
	
	
	/**
	 * 获取快速贷款的操作记录
	 * lixj
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getHanndleRecord", method={GET, POST})
	@ResponseBody
	public String getQuickLoanHandleRecord(@RequestParam Map<String, Object> map, HttpServletRequest request)
	{
		// 业务Id
		String businessId = MapUtils.getString(map, "buskey", "");
		List<Map<String, String>> resultList =  new ArrayList<Map<String,String>>();
		if (businessId.isEmpty())
		{
			return JsonUtils.toJson(RequestUtils.successResult(resultList));
		}
		else
		{
			// 操作记录
			List<FaCommunicateLog> faCommunicateLogs = faCommunicateLogService.findByBusinessIdAndType(businessId, ConstantTool.CommlogType.QUICK_LOAN_REMARK);
			for (FaCommunicateLog faCommunicateLog : faCommunicateLogs) {
				Map<String, String> map2 = new HashMap<String, String>();
				
				// 获取处理人信息
				SysUser sysUser = sysUserService.findByPk(faCommunicateLog.getContactId());
				
				map2.put("handleUser", sysUser.getUserName());
				map2.put("handleTime", faCommunicateLog.getContactTime());
				map2.put("handleContent", faCommunicateLog.getContent());
				resultList.add(map2);
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", resultList);
		
		// 查询公司名称
		FwQuickLoan fwQuickLoan = fwQuickLoanService.findByPk(Long.valueOf(businessId));
		if (fwQuickLoan != null)
		{
			resultMap.put("comName", fwQuickLoan.getComName());
		}
		
		return JsonUtils.toJson(RequestUtils.successResult(resultMap));
	}
	
	/**
	 * 快速贷款追加处理意见
	 * lixj
	 */
	@RequestMapping(value="/appendQuickLoanRemark", method={GET, POST})
	public String appendQuickLoanRemark(HttpServletRequest request, RedirectAttributes attr)
	{
		// 业务id
		String businessId = request.getParameter(WorkFlowUtils.process_definition_key_quickloan+"_businessKey");
		if ("".equals(businessId) || businessId == null) {
    		attr.addFlashAttribute("errorMsg", "追加意见失败，没有快速贷款id,请联系系统管理员。");
    		return "redirect:/workflow/list";
		}
		
		// 公司名称
		String comNameString = request.getParameter("comName");
		comNameString = comNameString == null ? "" : comNameString;
		
		// 插入公司名称
		FwQuickLoan fwQuickLoan = fwQuickLoanService.saveComName(comNameString, Long.valueOf(businessId));
		if (fwQuickLoan == null)
		{
			attr.addFlashAttribute("errorMsg", "保存公司名称出错,请联系系统管理员。");
    		return "redirect:/workflow/list";
		}
		
		// 处理意见
		String reason = request.getParameter(WorkFlowUtils.process_definition_key_quickloan+"_reason");
		reason = reason == null ? "" : reason;
		
		// 登陆用户id
		Long userId = RequestUtils.getLoginedUser().getUserId();
		
		// 插入快速贷款的备注信息
		FaCommunicateLog faCommunicateLog = faCommunicateLogService.saveQuickLoanCommunicateLog(businessId, ConstantTool.BusinessType.QUICK_LOAN, ConstantTool.CommlogType.QUICK_LOAN_REMARK, reason, userId);
		if (faCommunicateLog == null)
		{
			attr.addFlashAttribute("errorMsg", "追加意见失败，请联系系统管理员。");
    		return "redirect:/workflow/list";
		}
		
		attr.addFlashAttribute("successMsg","追加意见成功");
		
		return "redirect:/workflow/list";
	}
	
	/**
	 * 快速贷款追加意见（luy）
	 * @param request
	 * @param attr
	 * @return
	 */
	@RequestMapping(value="/appendQuickLoanRemarks", method={GET, POST})
	@ResponseBody
	public String appendQuickLoanRemarks(@RequestParam Map<String,Object> map,@ModelAttribute WorkFlow workFlow){
		
		String businessId = MapUtils.getString(map, "businessId",""); // 业务id
		String comName = MapUtils.getString(map, "comName",""); // 公司名称
		String reason = MapUtils.getString(map, "comment",""); // 处理意见
		
		if (StringUtils.isEmpty(businessId)) {
			return JsonUtils.toJson(RequestUtils.failResult("追加意见失败，没有快速贷款id,请联系系统管理员。"));
		}
		
		// 插入公司名称
		FwQuickLoan fwQuickLoan = fwQuickLoanService.saveComName(comName, Long.valueOf(businessId));
		if (fwQuickLoan == null){
			return JsonUtils.toJson(RequestUtils.failResult("保存公司名称出错,请联系系统管理员。"));
		}
		
		// 登陆用户id
		Long userId = RequestUtils.getLoginedUser().getUserId();
		
		// 插入快速贷款的备注信息
		FaCommunicateLog faCommunicateLog = faCommunicateLogService.saveQuickLoanCommunicateLog(businessId, ConstantTool.BusinessType.QUICK_LOAN, ConstantTool.CommlogType.QUICK_LOAN_REMARK, reason, userId);
		if (faCommunicateLog == null){
			return JsonUtils.toJson(RequestUtils.failResult("追加意见失败，请联系系统管理员。"));
		}
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("faCommunicateLog", faCommunicateLog);
		return JsonUtils.toJson(RequestUtils.successResult(retMap));
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
		boolean status = fwQuickLoanService.removeFwQuickLoan(ids);
    	if(status){
        return JsonUtils.toJson(RequestUtils.successResult(null));
        }
        return JsonUtils.toJson(RequestUtils.failResult("error"));
    }
    
    /**
	 * 快速贷款追加处理意见
	 * lixj
	 */
	@RequestMapping(value="/doAppendQLRemark", method={GET, POST})
	@ResponseBody
	public String doAppendQLRemark(@RequestParam Map<String, Object> map, HttpServletRequest request)	{
		// 业务id
		String businessId = MapUtils.getString(map, "businessId"); //业务id
		String reason = MapUtils.getString(map, "reason"); //处理意见
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		if ("".equals(businessId) || businessId == null) {
			return JsonUtils.toJson(RequestUtils.failResult("追加意见失败，没有快速贷款id,请联系系统管理员。"));
		}
		
		// 处理意见
		reason = reason == null ? "" : reason;
		
		// 登陆用户id
		Long userId = RequestUtils.getLoginedUser().getUserId();
		
		// 插入快速贷款的备注信息
		FaCommunicateLog faCommunicateLog = faCommunicateLogService.saveQuickLoanCommunicateLog(businessId, ConstantTool.BusinessType.QUICK_LOAN, ConstantTool.CommlogType.QUICK_LOAN_REMARK, reason, userId);
		if (faCommunicateLog == null){
			return JsonUtils.toJson(RequestUtils.failResult("追加意见失败，请联系系统管理员。"));
		}
		
		retMap.put("faCommunicateLog", faCommunicateLog);
		return JsonUtils.toJson(RequestUtils.successResult(retMap));
		
	}
}

