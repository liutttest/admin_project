package com.evan.finance.admin.dubbo.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.domain.FwLoanApply;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwLoanApplyService;
import com.evan.common.user.service.FwLoanInfoService;
import com.evan.common.user.service.FwRepaymentService;
import com.evan.common.user.service.FwTransactionDetailService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.DateUtils;
import com.evan.dubbo.finance.FwBusinessDubboService;
import com.evan.finance.admin.bank.BankTools;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;

public class FwBusinessDubboServiceImpl implements FwBusinessDubboService{
	final Logger logger = LoggerFactory.getLogger(FwBusinessDubboServiceImpl.class);	
	
	@Autowired
	private FwRepaymentService fwRepaymentService;
	
	@Autowired
	private FwTransactionDetailService fwTransactionDetailService;
	
    @Autowired
    private FwBusinessSxdService fwBusinessSxdService;
    
    @Autowired
    private FwLoanApplyService fwLoanApplyService; 
    
    @Autowired
    private FwLoanInfoService fwLoanInfoService;
	
	@Autowired
	private BankTools bankTools;

	/**
	 * 还款申请
	 */
	public Map<String,Object> applyFinancingRepay(String busId,String repaymentMoney,String loanNo,String fullFlag) {
		Map<String, Object> paramMap = fwRepaymentService.repaymentMap(busId, repaymentMoney, loanNo, fullFlag);
		    //Long comId = fwBusinessSxdService.findByPk(busId).getComId();
			
		    bankTools.cebankUserLogon();
			Map<String, Object> retRsuMap = null;
			try {
				retRsuMap = bankTools.applyFinancingRepay(paramMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    //if (retRsuMap != null) {
			//retRsuMap = (Map<String, Object>) retRsuMap.get("opRep");
			/*String resultCode = MapUtils.getString(retRsuMap, "errMsg");
			//Map<String, Object> variables = new HashMap<String, Object>();
			String retCode = MapUtils.getString(retRsuMap, "retCode");
			String date = DateUtils.getCurrentDateTime(ConstantTool.DATA_FORMAT);
			String message = MapUtils.getString(retRsuMap, "errMsg");
			if ("0".equals(retCode)) {
				//成功  向交易记录中插入交易记录
				fwTransactionDetailService.saveFwTransactionDetail(loanNo, new BigDecimal(repaymentMoney), "02", message, date, date, "1",TransactionType.HUANKUAN_STR,TransactionSource.YILUDAI,comId);
				logger.debug("saveFwTransactionDetail-----success");
				return true;
			}else{
				
				fwTransactionDetailService.saveFwTransactionDetail(loanNo, new BigDecimal(repaymentMoney), "02", message, date, date, "0",TransactionType.HUANKUAN_STR,TransactionSource.YILUDAI,comId);
				logger.debug("saveFwTransactionDetail-----error");
				return false;
			}*/
			return retRsuMap;
     }
 
	/**
	 * 贷款信息查询
	 * @param LoanNo  借据号
	 * @param busId   业务id
	 * @return
	 */
	public Map<String, Object> queryLoanInfo(String loanNo,String busId) {
		//查询业务数据
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId);
		//查询放款数据
		FwLoanApply fwLoanApply = fwLoanApplyService.getLoanApplyByLoanNo(loanNo);
		
	    bankTools.cebankUserLogon();
	    Map<String, Object> map = new HashMap<String, Object>();
		map.put("LoanNo", loanNo);
		map.put("busid", busId);
		Map<String, Object> retRsuMap = null;
		try {
			retRsuMap = bankTools.queryLoanInfo(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		retRsuMap = (Map<String, Object>) retRsuMap.get("opRep");
		String retCode = MapUtils.getString(retRsuMap, "retCode");
		if (!"0".equals(retCode)) {
			return null;
		}
		
		//开始时间
		String date = DateUtils.getCurrentDateTime(ConstantTool.DATA_FORMAT_DAY);
		SimpleDateFormat simpleDateFormat =  new SimpleDateFormat(ConstantTool.DATA_FORMAT_DAY);
		String startDate = fwLoanApply.getApplyPassTime();
		try {
			Date sDate = simpleDateFormat.parse(startDate);
			startDate = simpleDateFormat.format(sDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*Map<String, Object> map2 =  this.thisQueryHistoryCurrentList(fwBusinessSxd.getLicenseNum(), startDate, date);
		BigDecimal lixiMoney = this.getLiXi(loanNo, map2);
		if (lixiMoney==null) {
			return null;
		}
		//将已还的利息 存到map当中
		retRsuMap.put("nowAmt", lixiMoney);*/
		fwLoanInfoService.save(loanNo, fwBusinessSxd.getComId(), retRsuMap);
		logger.info("queryLoanInfo--result"+retRsuMap.toString());
		return retRsuMap;
     }
	
	
	public BigDecimal getLiXi(String loanNo,Map<String, Object> map){
		String retCode = MapUtils.getString(map, "retCode");
		if (!"0".equals(retCode)) {
			return null;
		}
		Map<String, Object> opResultSet = (Map<String, Object>)map.get("opResultSet");
		Object object =  (Object)opResultSet.get("opResult");
		List<Map<String, Object>> listRes = new ArrayList<Map<String,Object>>();
		if (object instanceof Map) {
			Map<String, Object> map3 = (Map<String, Object>)object;
			listRes.add(map3);
		}else if(object instanceof List){
			listRes = (List<Map<String, Object>>)object;
		}
		BigDecimal totalMoney = new BigDecimal(0);
		for (Map<String, Object> map3 : listRes) {
			// TODO 进行循环判断  交易记录表中插入交易记录
			String pati = MapUtils.getString(map3, "PATI","");
			String loanNo2 = MapUtils.getString(map3, "T24F", "");//借据号
			//不是扣息则进行 下一条的判断
			if (!StringUtils.isEmpty(pati) || !ConstantTool.TransactionType.KOUXI.equals(pati) || !loanNo2.equals(loanNo)) {
				continue;
			}
		    // 如果是 扣息
			double money = MapUtils.getDouble(map3, "FSJE", 0.00);//交易金额
			//将已扣的利息叠加
			totalMoney= totalMoney.add(new BigDecimal(money));
		}
		return totalMoney;
	}
	
	/**
	 * 查询用户历史明细
	 * @param acno 账号--银行账号
	 * @param qsrq 开始时间  如 20150301
	 * @param zzeq 结束时间  如 20150330
	 * @return
	 */
	public  Map<String, Object> queryHistoryCurrentList(String acno,String qsrq,String zzeq) {
		
		bankTools.cebankUserLogon();
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ACNO", acno);
		map2.put("MIFS", "0");
		map2.put("QSRQ", qsrq);
		map2.put("ZZRQ", zzeq);
		map2.put("MAFS", "9999999999.99");
		Map<String, Object> retRsuMap = null;
		try {
			retRsuMap = bankTools.queryHistoryCurrentList(map2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		retRsuMap = (Map<String, Object>) retRsuMap.get("opRep");
		return retRsuMap;
		
     }
	
	
public  Map<String, Object> thisQueryHistoryCurrentList(String acno,String qsrq,String zzeq) {
		
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ACNO", acno);
		map2.put("MIFS", "0");
		map2.put("QSRQ", qsrq);
		map2.put("ZZRQ", zzeq);
		map2.put("MAFS", "9999999999.99");
		Map<String, Object> retRsuMap = null;
		try {
			retRsuMap = bankTools.queryHistoryCurrentList(map2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		retRsuMap = (Map<String, Object>) retRsuMap.get("opRep");
		return retRsuMap;
		
     }
	
	
	
	/**
	 * 账户余额查询
	 * @param acno 账号--银行账号
	 * @return
	 */
	public  Map<String, Object> batchQueryBalanceCurrent(String acno) {
		bankTools.cebankUserLogon();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sumu", "1");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("acno", acno);
		list.add(maps);
		map.put("list", list);
		Map<String, Object> retRsuMap = null;
		try {
			retRsuMap = bankTools.batchQueryBalanceCurrent(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		retRsuMap = (Map<String, Object>) retRsuMap.get("opRep");
		return retRsuMap;
		
     }
}
