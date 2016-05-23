package com.evan.finance.admin.task;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.evan.common.user.domain.FaOverdueInfo;
import com.evan.common.user.domain.FaOverdueWarning;
import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.domain.FwCompany;
import com.evan.common.user.domain.FwLoanApply;
import com.evan.common.user.service.FaOverdueInfoService;
import com.evan.common.user.service.FaOverdueWarningService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwCompanyService;
import com.evan.common.user.service.FwLoanApplyService;
import com.evan.common.utils.ConstantTool;
import com.evan.finance.admin.bank.BankTools;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;
/**
 * 贷后相关
 * @author 
 *
 */
@Component
public class AfterCreditsTaskImpl implements AfterCreditsTask {
	final Logger logger = LoggerFactory.getLogger(AfterCreditsTaskImpl.class);	
	
	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	
	@Autowired
	private FwLoanApplyService fwLoanApplyService;
	
	@Autowired
	private BankTools bankTools;
	
	@Autowired
	private FaOverdueInfoService faOverdueInfoService;
	
	@Autowired 
	private FaOverdueWarningService faOverdueWarningService;
	
	@Autowired
	private FwCompanyService fwCompanyService;
	
	// 每2分钟执行一次
	@Scheduled(cron = "0 0/2 * * * ?")
    public void getBusinessTask() {
//		accessBankVerify();
    }
	
	// “利息逾期记录”，每月的22日，同步银行数据
	@Scheduled(cron = "0 0 0 22 * ?")
	public void queryBankForInterestOverdueInfo() {
		logger.error("=============queryBankForInterestOverdueInfo");
		
		// 查询放款表中，未还完的记录
		List<FwLoanApply> fwLoanApplies = fwLoanApplyService.findFwLoanAppliesForNotFinished();
		
		if (fwLoanApplies!=null && fwLoanApplies.size()>0) {
			bankTools.cebankUserLogon();
			
			for (FwLoanApply fwLoanApply : fwLoanApplies) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("LoanNo", fwLoanApply.getLoanNo());
				paramMap.put("busid", fwLoanApply.getBusinessId());
				Map<String, Object> resMap = null;
				try {
					resMap = bankTools.queryLoanInfo(paramMap);
					resMap = (Map<String, Object>) resMap.get("opRep");
					String retCode = MapUtils.getString(resMap, "retCode");
					if (!"0".equals(retCode)) {
						continue;
					}
					
					Object object =  (Object)resMap.get("opResult");
					
					List<Map<String, Object>> listRes = new ArrayList<Map<String,Object>>();
					if (object instanceof Map) {
						Map<String, Object> map3 = (Map<String, Object>)object;
						listRes.add(map3);
					}else if(object instanceof List){
						listRes = (List<Map<String, Object>>)object;
					}
					//List<Map<String, Object>> listRes = (List<Map<String, Object>>)opResultSet.get("opResult");
					for (Map<String, Object> map3 : listRes) {
						String TotPdIntAmtStr = MapUtils.getString(map3, "TotPdIntAmt","");//贷款逾期利息
						if (!StringUtils.isEmpty(TotPdIntAmtStr)) {
							BigDecimal TotPdIntAmtBD = new BigDecimal(TotPdIntAmtStr);
							// 判断逾期利息是否大于0
							if (TotPdIntAmtBD.compareTo(new BigDecimal(0)) == 1) {
								FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(fwLoanApply.getBusinessId());//业务信息
								// 将之前的逾期记录修改为已还
								faOverdueInfoService.updateFaOverdueInfoByLoanNo(fwLoanApply.getLoanNo(), "1","1");
								// 增加新的逾期记录
								FaOverdueInfo faOverdueInfo = faOverdueInfoService.saveFaOverdueInfo(fwBusinessSxd.getComId(), fwBusinessSxd.getComName(), fwBusinessSxd.getBsId(), fwLoanApply.getBusinessType(), TotPdIntAmtBD, fwLoanApply.getApplyPassTime(), fwLoanApply.getExpitedTime(), "1", new SimpleDateFormat(ConstantTool.DATA_FORMAT).format(new Date()), fwLoanApply.getLoanNo(), "");
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	// “本金逾期记录”，每天查询放款表，到期未还的
	@Scheduled(cron = "0 0 0 * * ?")
	public void queryBankForPrincipalOverdueInfo() {
		logger.error("=============queryBankForPrincipalOverdueInfo");
		
		// 查询放款表中，未还完的记录
		List<FwLoanApply> fwLoanApplies = fwLoanApplyService.findFwLoanAppliesForNotFinishedAndExpired();
		
		if (fwLoanApplies!=null && fwLoanApplies.size()>0) {
			
			for (FwLoanApply fwLoanApply : fwLoanApplies) {
				FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(fwLoanApply.getBusinessId());
				// 将之前的逾期记录修改为已还
				faOverdueInfoService.updateFaOverdueInfoByLoanNo(fwLoanApply.getLoanNo(), "1","2");
				// 增加新的逾期记录
				FaOverdueInfo faOverdueInfo = faOverdueInfoService.saveFaOverdueInfo(fwBusinessSxd.getComId(), fwBusinessSxd.getComName(), fwBusinessSxd.getBsId(), fwLoanApply.getBusinessType(), fwLoanApply.getRepaymentMoney(), fwLoanApply.getApplyPassTime(), fwLoanApply.getExpitedTime(), "1", new SimpleDateFormat(ConstantTool.DATA_FORMAT).format(new Date()), fwLoanApply.getLoanNo(), "");
			}
		}
		
	}
	
	// “还息预警”，每月的18，20，21日，同步银行数据
	@Scheduled(cron = "0 0 0 18,20,21 * ?")
	public void queryBankForInterestWarning() {
		logger.error("=============queryBankForInterestWarning");
		
		// 查询放款表中，未还完的记录
		List<FwLoanApply> fwLoanApplies = fwLoanApplyService.findFwLoanAppliesForNotFinished();
		
		if (fwLoanApplies!=null && fwLoanApplies.size()>0) {
			bankTools.cebankUserLogon();
			
			for (FwLoanApply fwLoanApply : fwLoanApplies) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("LoanNo", fwLoanApply.getLoanNo());
				paramMap.put("busid", fwLoanApply.getBusinessId());
				Map<String, Object> resMap = null;
				try {
					resMap = bankTools.queryLoanInfo(paramMap);
					resMap = (Map<String, Object>) resMap.get("opRep");
					String retCode = MapUtils.getString(resMap, "retCode");
					if (!"0".equals(retCode)) {
						continue;
					}
					
					Object object =  (Object)resMap.get("opResult");
					
					List<Map<String, Object>> listRes = new ArrayList<Map<String,Object>>();
					if (object instanceof Map) {
						Map<String, Object> map3 = (Map<String, Object>)object;
						listRes.add(map3);
					}else if(object instanceof List){
						listRes = (List<Map<String, Object>>)object;
					}
					//List<Map<String, Object>> listRes = (List<Map<String, Object>>)opResultSet.get("opResult");
					for (Map<String, Object> map3 : listRes) {
						
						FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(fwLoanApply.getBusinessId());//业务信息
						
						// 获取该账户的可用余额
						//获取账户余额
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("sumu", "1");
						List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
						Map<String, Object> maps = new HashMap<String, Object>();
						
						String loanCardCodeString = "";
						FwCompany company = fwCompanyService.findByPk(fwBusinessSxd.getComId());
						loanCardCodeString = company.getLendersAccount();
						
						maps.put("acno", loanCardCodeString);
						list.add(maps);
						map.put("list", list);
						Map<String, Object> retRsuMap = bankTools.batchQueryBalanceCurrent(map);
						Map<String, Object> accountBalance = (Map<String, Object>) retRsuMap.get("opRep");
						Map<String, Object> accountMap1 = (Map<String, Object>) accountBalance.get("opResultSet");
						Map<String, Object> accountMap2 = (Map<String, Object>) accountMap1.get("opResult");
						
						String kyer = MapUtils.getString(accountMap2, "KYER", "");//账户可用余额
						
						String TotPdIntAmtStr = MapUtils.getString(map3, "TotPdIntAmt", "");//贷款逾期利息
						String ToNwIntAmtStr = MapUtils.getString(map3, "ToNwIntAmt", "");//贷款当前利息
						String BalanceStr = MapUtils.getString(map3, "Balance", "");//贷款余额
						String RateStr = MapUtils.getString(map3, "Rate", "");//利率
						
						if (!StringUtils.isEmpty(TotPdIntAmtStr) && !StringUtils.isEmpty(ToNwIntAmtStr) && !StringUtils.isEmpty(kyer)) {
							BigDecimal TotPdIntAmtBD = new BigDecimal(TotPdIntAmtStr);
							BigDecimal ToNwIntAmtStrBD = new BigDecimal(ToNwIntAmtStr);
							BigDecimal BalanceStrBD = new BigDecimal(BalanceStr);
							BigDecimal kyerBD = new BigDecimal(kyer);
							BigDecimal RateStrBD = new BigDecimal(RateStr);
							
							BigDecimal lixiBD = TotPdIntAmtBD.add(ToNwIntAmtStrBD);
							
							// 当前利息和逾期利息的和  是否大于  账户余额
							if (lixiBD.compareTo(kyerBD) == 1) {
								// 增加还息预警记录
								FaOverdueWarning faOverdueWarning = faOverdueWarningService.saveFaOverdueWarning(fwBusinessSxd.getComId(), fwBusinessSxd.getComName(), fwBusinessSxd.getBsId(), fwLoanApply.getBusinessType(), fwLoanApply.getAppMoney(), fwLoanApply.getApplyPassTime(), fwLoanApply.getExpitedTime(), new SimpleDateFormat(ConstantTool.DATA_FORMAT).format(new Date()), fwLoanApply.getLoanNo(), fwBusinessSxd.getLoanCardCode(), BalanceStrBD, RateStrBD, TotPdIntAmtBD, ToNwIntAmtStrBD);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}

}
