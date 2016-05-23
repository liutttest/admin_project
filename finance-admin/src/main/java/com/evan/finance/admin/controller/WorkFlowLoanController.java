package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evan.common.user.domain.FwLoanApply;
import com.evan.common.user.monitor.MonitorTools;
import com.evan.common.user.service.FaOperationRecordService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwLoanApplyService;
import com.evan.common.user.service.FwTransactionDetailService;
import com.evan.common.utils.ConstantTool.OperationState;
import com.evan.common.utils.ConstantTool.TransactionSource;
import com.evan.common.utils.ConstantTool.TransactionType;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.DateUtils;
import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.activiti.bean.LoanFlowView;
import com.evan.finance.admin.activiti.service.WorkFlowLoanService;
import com.evan.finance.admin.bank.BankTools;
import com.evan.finance.admin.utils.WorkFlowUtils;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;

/**
 * 放款办理
 * 
 * @author stone
 */
@RequestMapping("/workflow/loan")
@Controller
public class WorkFlowLoanController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(WorkFlowLoanController.class);
	
	@Autowired
	private WorkFlowLoanService workFlowLoanService;
	
	@Autowired
	private FwLoanApplyService fwLoanApplyService;
	
	@Autowired
	private FaOperationRecordService faOperationRecordService;
	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	@Autowired
	private BankTools bankTools;
	
	@Autowired
	private MonitorTools monitorTools;
	
	@Autowired
	private FwTransactionDetailService fwTransactionDetailService;
	
	/**
	 * 通过URL启动流程
	 * @param searchForm
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/startProcess/{key}", method = {GET, POST})
	@ResponseBody
	public String startProcess(@PathVariable("key") String businessKey) {
		LoanFlowView loanFlowView = workFlowLoanService.startProcess(businessKey);
		if (loanFlowView != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("businessKey", loanFlowView.getBusinessKey());
			map.put("processInstanceId", loanFlowView.getProcessInstance().getProcessInstanceId());
			map.put("processDefinitionId", loanFlowView.getProcessInstance().getProcessDefinitionId());
			return JsonUtils.toJson(RequestUtils.successResult(map));
		} else {
			return JsonUtils.toJson(RequestUtils.failResult(null));
		}
	}
	
	/**
     * 签收任务
     */
    @RequestMapping(value = "task/claim/{id}")
    public String claim(@PathVariable("id") String taskId) {
    	workFlowLoanService.claim(taskId, RequestUtils.getLoginedUser().getUserName());
        return "redirect:/workflow/list";
    }
    
    /**
     * 办理业务
     *
     * @param id
     * @return
     */
    //没有调用该方法
    @RequestMapping(value = "task/complete/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public String complete(@PathVariable("id") String taskId, HttpServletRequest request, RedirectAttributes attr) {
    	// 校验用户是否有权限，记录日志
    	
    	// 当前节点
    	String node = request.getParameter(WorkFlowUtils.process_definition_key_loan+"_node");
    	// 验证节点
    	String nodeKey = nodeVerify(node);
    	if (nodeKey == null) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，系统找不到当前节点，请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}
    	//处理结果
    	String condition = request.getParameter(WorkFlowUtils.process_definition_key_loan+"_condition");
    	if (!("true".equals(condition) || "false".equals(condition))) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，请联系系统管理员。");
    		return "redirect:/workflow/list";
    	}
    	// 处理原因，不通过时会同步到业务数据
    	String reason = request.getParameter(WorkFlowUtils.process_definition_key_loan+"_reason");
    	reason = reason == null ? "" : reason;
    	
    	//业务id
    	String businessKey = request.getParameter(WorkFlowUtils.process_definition_key_loan+"_businessKey");
    	if ("".equals(businessKey)) {
    		attr.addFlashAttribute("errorMsg", "任务办理失败，没有放款id,请联系系统管理员。");
    		return "redirect:/workflow/list";
		}
    	//修改人id
    	Long userId = RequestUtils.getLoginedUser().getUserId();
    	Map<String, Object> variables = new HashMap<String, Object>();
    	
    	try {
    		// 处理意见及结果将同步到业务数据
    		boolean status=true;
    		// 最后意见
    		if ("true".equals(condition)) {
    			//  将审核结果告诉用户
    			//status = fwLoanApplyService.processSuccessCallback(Long.parseLong(businessKey), userId,reason);
    		} else {
    			// 将审核状态同步给用户
    			//status = fwLoanApplyService.processFailCallback(Long.parseLong(businessKey), userId,reason);
    		}
    		monitorTools.infoOther("放款", status+"");
    		if (status==false) {
    			attr.addFlashAttribute("errorMsg", "业务办理失败，请联系系统管理员。");
        		return "redirect:/workflow/list";
			}
            variables.put("reason", reason);
            variables.put(nodeKey, "true".equals(condition) ? true : false);
            workFlowLoanService.complete(taskId, variables);
        } catch (Exception e) {
            logger.error("error on complete task {}, variables={}", new Object[]{taskId, variables, e});
            attr.addFlashAttribute("errorMsg", "任务处理异常，请联系系统管理员。");
            return "redirect:/workflow/list";
        }
    	attr.addFlashAttribute("successMsg","处理成功");
        return "redirect:/workflow/list";
    }
    
    
    /**
     * 办理业务 人工办理业务
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/task/personDone/{busId}/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String personDone(@PathVariable("busId") String loanId,@PathVariable("taskId") String taskId, HttpServletRequest request, RedirectAttributes attr) {
    	//向中间数据表中插入数据 
		//String  loanId = execution.getProcessBusinessKey();
		//调用银行登录接口
    	 Map<String, Object> mapResult = new HashMap<String, Object>();
		try {
			 Map<String, Object> variables = new HashMap<String, Object>();
			bankTools.cebankUserLogon();
			//查询业务数据
			FwLoanApply fwLoanApply = fwLoanApplyService.findByPk(Long.parseLong(loanId));
			// 调用银行接口 时构建map
			Map<String,Object> map = fwLoanApplyService.bankLoanApplyMap(fwLoanApply);
			//调用银行接口
			 Map<String, Object> result = bankTools.applyFinancingLoan(map);
			 result = (Map<String, Object>) result.get("opRep");
			 //得到放款成功与否结果的code  0：成功  如果不是0则证明放款失败
			 String retCode = MapUtils.getString(result, "retCode");//本次交易返回
			 String message = MapUtils.getString(result, "errMsg");
			if (retCode.equals("0")) {
				//放款成功
				//查询银行接口 放款到期日
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("LoanNo",fwLoanApply.getLoanNo());
				map2.put("busid",fwLoanApply.getBusinessId());
				Map<String, Object> retRsuMap = null;
				try {
					retRsuMap = bankTools.queryLoanInfo(map2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				retRsuMap = (Map<String, Object>) retRsuMap.get("opRep");
				String retCodeLoan = MapUtils.getString(retRsuMap, "retCode");
				retRsuMap = (Map<String, Object>) retRsuMap.get("opResult");
				//放款到期日
				String endDate = "";
				if ("0".equals(retCode)) {
					endDate = MapUtils.getString(retRsuMap, "T24DateDue");
				}
				Date dateEnd = new SimpleDateFormat(ConstantTool.DATA_FORMAT_DAY).parse(endDate);
				String endDateStr = new SimpleDateFormat(ConstantTool.DATA_FORMAT).format(dateEnd);
				//放款成功做业务处理
			    fwLoanApplyService.processSuccessCallback(Long.parseLong(loanId), 0L, "",endDateStr,retRsuMap);
			   
			    //根据放款任务id  查询放款记录 并且向交易记录表中插入放款成功记录
			    String date = DateUtils.getCurrentDateTime(ConstantTool.DATA_FORMAT);
			    //加操作日志
			    fwTransactionDetailService.saveFwTransactionDetail(fwLoanApply.getLoanNo(), fwLoanApply.getAppMoney(), "01", message, fwLoanApply.getCreateTime(), date, "1",TransactionType.FANGKUAN_STR,TransactionSource.YILUDAI,fwLoanApply.getComId());
			    mapResult.put("msg", getMessage("loan.success"));
			    monitorTools.infoOther("放款成功", "true");
			}else{
				//放款失败 进行业务处理
			   fwLoanApplyService.processFailCallback(Long.parseLong(loanId), 0L, "");
			   //加放款失败的操作日志
			   fwTransactionDetailService.saveFwTransactionDetail(fwLoanApply.getLoanNo(), fwLoanApply.getAppMoney(), "01", message, fwLoanApply.getCreateTime(), fwLoanApply.getApplyPassTime(), "0",TransactionType.FANGKUAN_STR,TransactionSource.YILUDAI,fwLoanApply.getComId());
			   //放款失败
			   monitorTools.infoOther("放款失败", "true");
			   mapResult.put("msg", getMessage("loan.fail"));
			}
			 variables.put(WorkFlowUtils.role_mranual_loan,true);
		     workFlowLoanService.complete(taskId, variables);
		     return JsonUtils.toJson(RequestUtils.successResult(mapResult));
		} 
		catch (Exception e) {
			//放款失败
			//mapResult.put("msg", getMessage("send.fail"));
			return JsonUtils.toJson(RequestUtils.failResult(getMessage("send.fail")));
		}
    }
    
    /**
     * 验证node是否有效
     * @param node
     * @return
     */
    private String nodeVerify(String node) {
    	if(node == null || node.trim().length() == 0) {
    		return null;
    	}
    	if (WorkFlowUtils.role_loan_bank_handle.equals(node)) {
    		return WorkFlowUtils.loanBankHandle;
    	}
    	if (WorkFlowUtils.role_mranual_loan.equals(node)) {
    		return WorkFlowUtils.mranualLoan;
    	}
    	return null;
    }
}

