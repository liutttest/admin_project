package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
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

import com.evan.common.user.domain.FaInfoNopass;
import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.domain.FwComPerBus;
import com.evan.common.user.domain.JPage;
import com.evan.common.user.service.FaInfoNopassService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwComAccountService;
import com.evan.common.user.service.FwComPerBusService;
import com.evan.common.utils.ConstantTool.BusinessType;
import com.evan.common.utils.ConstantTool.FileType;
import com.evan.common.utils.ConstantTool.NoPass;
import com.evan.common.utils.ConstantTool.PersonType;
import com.evan.finance.admin.activiti.service.WorkFlowService;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.util.MapUtils;
import com.evan.nd.common_file.domain.FwFiles;
import com.evan.nd.common_file.service.FwFilesService;

@RequestMapping(value="/sysAdmin")
@Controller
public class WorkFlowSysTaskController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(WorkFlowSysTaskController.class);
	
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	@Autowired
	private FwComAccountService fwComAccountService;
	@Autowired
	private FwComPerBusService fwComPerBusService;
	@Autowired
	private FwFilesService fwFilesService;
	@Autowired
	private FaInfoNopassService faInfoNopassService;
	
	/**
	 * @liutt
	 * 跳转到系统任务列表页面
	 * @return
	 */
	@RequestMapping(value = "/task")
	public String toTask() {
		return "sysAdmin/list";
	}
	

	/**
	 * 系统任务列表
	 * @param page 接受分页数据
	 * @param suspensionState 接受任务状态参数
	 * @return
	 */
	@RequestMapping(value="/list", method = {GET, POST})
	@ResponseBody
	public Object list(JPage page,@RequestParam("suspensionState") String suspensionState) {
		//调用查询系统任务列表的接口
		return workFlowService.findTodoTasksSys("sys_admin",page,suspensionState);
	}
	
	/**根据业务id 查询申请资料详情
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/task/getfwApplyBusDetail", method = GET)
	public String getfwApplyBusDetail(@RequestParam Map<String,Object> map,Model model) {
		String busId = MapUtils.getString(map, "busId");
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId);
		if (fwBusinessSxd==null) {
			return "error";
		}
		//查询账户信息
		Map<String, Object> fwComAccount = fwComAccountService.getUserMapByComId(fwBusinessSxd.getComId());
		//查询企业信息
		/*FwCompany fwCompany = fwCompanyService.findByPk(fwBusinessSxd.getComId());*/
		
		//查询法人信息
		FwComPerBus fwComPerBus = fwComPerBusService.findByBusinessIdAndType(fwBusinessSxd.getBsId(), BusinessType.SXD, PersonType.COM_LEGAL);
		//查询 控制人及配偶信息
		FwComPerBus fwComPerBus2 = fwComPerBusService.findByBusinessIdAndType(fwBusinessSxd.getBsId(), BusinessType.SXD, PersonType.COM_CONTROLLER);
		
		model.addAttribute("fwComAccount", fwComAccount);
		/*model.addAttribute("fwCompany", fwCompany);*/
		model.addAttribute("fwBusinessSxd", fwBusinessSxd);
		model.addAttribute("fwComPerBus", fwComPerBus);
		model.addAttribute("controller", fwComPerBus2);
		List<FwFiles> list  = fwFilesService.findByBusIdAndBusType(busId, BusinessType.SXD);
		List<FwFiles> businessLicense =  new ArrayList<FwFiles>();//营业执照
		List<FwFiles> organiztionCode =  new ArrayList<FwFiles>();//组织机构代码证
		List<FwFiles> taxRegistartion =  new ArrayList<FwFiles>();//税务登记证
		List<FwFiles> lastYearStatements =  new ArrayList<FwFiles>();// 上一年财务报表
		List<FwFiles> taxRelatedQuery =  new ArrayList<FwFiles>();//涉税保密信息查询申请表
		List<FwFiles> companyCredit =  new ArrayList<FwFiles>();//征信查询授权书
		List<FwFiles> ledalCredit =  new ArrayList<FwFiles>();//法人征信查询授权书
		List<FwFiles> controllerCredit =  new ArrayList<FwFiles>();//控制人及配偶征信查询授权书
		List<FwFiles> ledalCard =  new ArrayList<FwFiles>();//法人证件照片
		List<FwFiles> controllerCard =  new ArrayList<FwFiles>(); //控制人身份证照片
		List<FwFiles> controllerSpoCard =  new ArrayList<FwFiles>();//控制人配偶身份证照片
		List<FwFiles> controllerEducation = new ArrayList<FwFiles>(); //学历证明
		List<FwFiles> controllerDomicilePlace = new ArrayList<FwFiles>(); //户籍证明
		List<FwFiles> purchaseSaleContract =  new ArrayList<FwFiles>();//购销合同
		List<FwFiles> customerList =  new ArrayList<FwFiles>();//上下游客户清单
		List<FwFiles> lastTaxStatements =  new ArrayList<FwFiles>();//税务报表
		List<FwFiles> lastControllerStatements =  new ArrayList<FwFiles>();//个人银行对账单
		List<FwFiles> lastCompanyStatements =  new ArrayList<FwFiles>();//银行对账单
		List<FwFiles> lastTaxCerifcate =  new ArrayList<FwFiles>();//纳税凭证
		List<FwFiles> lastPaymentVoucher =  new ArrayList<FwFiles>();//缴费凭证
		List<FwFiles> creditTechnology =  new ArrayList<FwFiles>();//资质和技术产品证书
		List<FwFiles> propertyRightCard =  new ArrayList<FwFiles>();//场地证明
		List<FwFiles> creditBusinessApp =  new ArrayList<FwFiles>();//业务查询授权书
		List<FwFiles> directorsBoard =  new ArrayList<FwFiles>();//股东会或董事会决议
		List<FwFiles> specicalIndustryCre =  new ArrayList<FwFiles>();//资质证书或经营许可证
		List<FwFiles> capitalVerification =  new ArrayList<FwFiles>();//公司章程活验资报告
		List<FwFiles> controllerAssetsLiaApply =  new ArrayList<FwFiles>();//资产负债申请表
		List<FwFiles> marriageLicense =  new ArrayList<FwFiles>();//婚姻状况证明
		List<FwFiles> controllerSpouseGuarantee =  new ArrayList<FwFiles>();//配偶的担保承诺书
		List<FwFiles> enterpriseCreditRating =  new ArrayList<FwFiles>();//企业纳税信用等级证明
		List<FwFiles> companyProfile =  new ArrayList<FwFiles>();//公司简介
		List<FwFiles> theIndividualIncomeTax =  new ArrayList<FwFiles>();//实际控制连续两年缴纳个人所得税记录
		List<FwFiles> lastYearThidYearAnnualLandTaxCertificate =  new ArrayList<FwFiles>();//上年度、本年度全年的国地税纳税证明
		List<FwFiles> confirmation =  new ArrayList<FwFiles>();//实际控制人及配偶的担保确认书
		List<FwFiles> housePropertyCard =  new ArrayList<FwFiles>();//房产证明
		List<FwFiles> enterprisePayTaxesThrough =  new ArrayList<FwFiles>();//纳税查询授权书
		List<FwFiles> theLoanCardCopy =  new ArrayList<FwFiles>();//贷款卡复印件
		List<FwFiles> householdAssets =  new ArrayList<FwFiles>();//净资产证明householdAssets
		
		for (FwFiles fwFiles : list) {
			//营业执照
			if (FileType.BUSINESS_LICENSE.equals(fwFiles.getFileType())) {
				businessLicense.add(fwFiles);
			}
			//组织机构代码证
			if (FileType.ORGANIZTION_CODE_CERTIFICATE.equals(fwFiles.getFileType())) {
				organiztionCode.add(fwFiles);
			}
			//税务登记证
			if (FileType.TAX_REGISTRATION_CERTIFICATE.equals(fwFiles.getFileType())) {
				taxRegistartion.add(fwFiles);
			}
			// 上一年财务报表
			if (FileType.LAST_YEAR_FINANCIAL_STATEMENTS.equals(fwFiles.getFileType())) {
				lastYearStatements.add(fwFiles);
			}
			//涉税保密信息查询申请表
			if (FileType.TAX_RELATED_CONFIDENTIAL_QUERY.equals(fwFiles.getFileType())) {
				taxRelatedQuery.add(fwFiles);
			}
			//企业征信查询授权书
			if (FileType.COMPANY_CREDIT_REPORT_AUTHORIZATION.equals(fwFiles.getFileType())) {
				companyCredit.add(fwFiles);
			}
			//法人征信查询授权书
			if (FileType.LEGAL_COMPANY_CREDIT_REPORT_AUTHORIZATION.equals(fwFiles.getFileType())) {
				ledalCredit.add(fwFiles);
			}
			//法人身份证
			if (FileType.LEDAL_IDENTITY_CARD.equals(fwFiles.getFileType())) {
				ledalCard.add(fwFiles);
			}
			//控制人征信查询授权书
			if (FileType.CONTROLLER_CREDIT_REPORT_AUTHORIZATION.equals(fwFiles.getFileType())) {
				controllerCredit.add(fwFiles);
			}
			//控制人身份证照片
			if (FileType.CONTROLLER_IDENTITY_CARD.equals(fwFiles.getFileType())) {
				controllerCard.add(fwFiles);
			}
			//控制人配偶身份证照片
			if (FileType.CONTROLLER_SPO_IDENTITY_CARD.equals(fwFiles.getFileType())) {
				controllerSpoCard.add(fwFiles);
			}
			//学历证明
			if (FileType.EDUCATION.equals(fwFiles.getFileType())) {
				controllerEducation.add(fwFiles);
			}
			//户籍证明
			if (FileType.DOMICILE_PLACE.equals(fwFiles.getFileType())) {
				controllerDomicilePlace.add(fwFiles);
			}
			//购销合同
			if (FileType.PURCHASE_SALE_CONTRACT.equals(fwFiles.getFileType())) {
				purchaseSaleContract.add(fwFiles);
			}
			//客服清单
			if (FileType.CUSTOMER_LIST.equals(fwFiles.getFileType())) {
				customerList.add(fwFiles);
			}
			//税务报表
			if (FileType.LAST_YEAR_TAX_STATEMENTS.equals(fwFiles.getFileType())) {
				lastTaxStatements.add(fwFiles);
			}
			//银行对账单
			if (FileType.LAST_THREE_MONTHS_STATEMENTS.equals(fwFiles.getFileType())) {
				lastCompanyStatements.add(fwFiles);
			}
			//个人银行对账单
			if (FileType.LAST_THREE_MONTHS_CONTROLLER_STATEMENTS.equals(fwFiles.getFileType())) {
				lastControllerStatements.add(fwFiles);
			}
			//纳税凭证和申请表
			if (FileType.LAST_YEAR_TAX_PAYMENT_CERTIFICATE.equals(fwFiles.getFileType())) {
				lastTaxCerifcate.add(fwFiles);
			}
			//缴费凭证
			if (FileType.LAST_UTILITIES_PAYMENT_VOUCHER.equals(fwFiles.getFileType())) {
				lastPaymentVoucher.add(fwFiles);
			}
			//资信和技术产品证书
			if (FileType.CREDIT_TECHNOLOGY_CERTIFICATION.equals(fwFiles.getFileType())) {
				creditTechnology.add(fwFiles);
			}
			//场地证明
			if (FileType.PROPERTY_RIGHT_CARD.equals(fwFiles.getFileType())) {
				propertyRightCard.add(fwFiles);
			}
			//授信业务申请书
			if (FileType.CREDIT_BUSINESS_APPLICATION.equals(fwFiles.getFileType())) {
				creditBusinessApp.add(fwFiles);
			}
			//股东会或董事会决议
			if (FileType.DIRECTORS_BOARD.equals(fwFiles.getFileType())) {
				directorsBoard.add(fwFiles);
			}
			//资质证书活经营许可证
			if (FileType.SPECIAL_INDUSTRY_CERTIFICATE.equals(fwFiles.getFileType())) {
				specicalIndustryCre.add(fwFiles);
			}
			//公司章程验资报告
			if (FileType.COMPANY_ARTICLES.equals(fwFiles.getFileType())) {
				capitalVerification.add(fwFiles);
			}
			//资产负债申请表
			if (FileType.CONTROLLER_ASSETS_LIABILITIES_APPLY.equals(fwFiles.getFileType())) {
				controllerAssetsLiaApply.add(fwFiles);
			}
			//婚姻状况证明
			if (FileType.MARRIAGE_LICENSE.equals(fwFiles.getFileType())) {
				marriageLicense.add(fwFiles);
			}
			//配偶的担保承诺书
			if (FileType.CONTROLLER_SPOUSE_GUARANTEE.equals(fwFiles.getFileType())) {
				controllerSpouseGuarantee.add(fwFiles);
			}
			//企业纳税信用等级证明
			if (FileType.ENTERPRISE_CREDIT_RATING.equals(fwFiles.getFileType())) {
				enterpriseCreditRating.add(fwFiles);
			}
			//公司简介
			if (FileType.COMPANY_PROFILE.equals(fwFiles.getFileType())) {
				companyProfile.add(fwFiles);
			}
			//实际控制连续两年缴纳个人所得税记录
			if (FileType.THE_INDIVIDUAL_INCOME_TAX.equals(fwFiles.getFileType())) {
				theIndividualIncomeTax.add(fwFiles);
			}
			//上年度、本年度全年的国地税纳税证明
			if (FileType.LAST_YEAR_THIS_YEAR_ANNUAL_LAND_TAX_CERTIFICATE.equals(fwFiles.getFileType())) {
				lastYearThidYearAnnualLandTaxCertificate.add(fwFiles);
			}
			//实际控制人及配偶的担保确认书
			if (FileType.CONFIRMATION.equals(fwFiles.getFileType())) {
				confirmation.add(fwFiles);
			}
			//房产证明
			if (FileType.HOUSE_PROPERTY_CARD.equals(fwFiles.getFileType())) {
				housePropertyCard.add(fwFiles);
			}
			//纳税查询授权书
			if (FileType.ENTERPRISE_PAY_TAXES_THROUGH_THE_DATA_QUERY_AUTHORIZATION.equals(fwFiles.getFileType())) {
				enterprisePayTaxesThrough.add(fwFiles);
			}
			//贷款卡复印件
			if (FileType.THE_LOAN_CARD_COPY.equals(fwFiles.getFileType())) {
				theLoanCardCopy.add(fwFiles);
			}
			//净资产证明householdAssets
			if (FileType.HOUSEHOLD_ASSETS.equals(fwFiles.getFileType())) {
				householdAssets.add(fwFiles);
			}
			
		}
		
		
		
		
		model.addAttribute("businessLicense", businessLicense);//营业执照
		model.addAttribute("organiztionCode", organiztionCode);//组织机构代码证
		model.addAttribute("taxRegistartion", taxRegistartion);//税务登记证
		model.addAttribute("lastYearStatements", lastYearStatements);// 上一年财务报表
		model.addAttribute("taxRelatedQuery", taxRelatedQuery);//涉税保密信息查询申请表
		model.addAttribute("companyCredit", companyCredit);//征信查询授权书
		model.addAttribute("ledalCredit", ledalCredit);//法人征信查询授权书
		model.addAttribute("ledalCard", ledalCard);//法人证件照片
		model.addAttribute("controllerCredit", controllerCredit);//控制人及配偶征信查询授权书
		model.addAttribute("controllerCard", controllerCard);//控制人身份证照片
		model.addAttribute("controllerSpoCard", controllerSpoCard);//控制人配偶身份证照片
		model.addAttribute("controllerEducation", controllerEducation);//学历证明
		model.addAttribute("controllerDomicilePlace", controllerDomicilePlace);//户籍证明
		model.addAttribute("purchaseSaleContract", purchaseSaleContract);////购销合同
		model.addAttribute("customerList", customerList);//上下游客户清单
		model.addAttribute("lastTaxStatements", lastTaxStatements);//税务报表
		model.addAttribute("lastControllerStatements", lastControllerStatements);//个人银行对账单
		model.addAttribute("lastCompanyStatements", lastCompanyStatements);//银行对账单
		model.addAttribute("lastTaxCerifcate", lastTaxCerifcate);//纳税凭证
		model.addAttribute("lastPaymentVoucher", lastPaymentVoucher);//缴费凭证
		model.addAttribute("creditTechnology", creditTechnology);//资质和技术产品证书
		model.addAttribute("propertyRightCard", propertyRightCard);//场地证明
		model.addAttribute("creditBusinessApp", creditBusinessApp);//授信业务申请书
		model.addAttribute("directorsBoard", directorsBoard);//股东会或董事会决议
		model.addAttribute("specicalIndustryCre", specicalIndustryCre);//资质证书或经营许可证
		model.addAttribute("capitalVerification", capitalVerification);//公司章程或验资报告
		model.addAttribute("controllerAssetsLiaApply", controllerAssetsLiaApply);//资产负债申请表
		model.addAttribute("marriageLicense", marriageLicense);//婚姻状况证明
		model.addAttribute("controllerSpouseGuarantee", controllerSpouseGuarantee);//配偶的担保承诺书
		model.addAttribute("enterpriseCreditRating", enterpriseCreditRating);//企业纳税信用等级证明
		model.addAttribute("companyProfile", companyProfile);//公司简介
		model.addAttribute("theIndividualIncomeTax", theIndividualIncomeTax);//实际控制连续两年缴纳个人所得税记录
		model.addAttribute("lastYearThidYearAnnualLandTaxCertificate", lastYearThidYearAnnualLandTaxCertificate);//上年度、本年度全年的国地税纳税证明
		model.addAttribute("confirmation", confirmation);//实际控制人及配偶的担保确认书
		model.addAttribute("housePropertyCard", housePropertyCard);//房产证明
		model.addAttribute("enterprisePayTaxesThrough", enterprisePayTaxesThrough);//纳税查询授权书
		model.addAttribute("theLoanCardCopy", theLoanCardCopy);//贷款卡复印件
		model.addAttribute("householdAssets", householdAssets);//净资产证明householdAssets
		
		
		// 0：不合格；1：合格
		String licenseNum_ispass = "1";// 营业执照注册号
		String taxNum_ispass = "1";//税号
		String catdCode_ispass = "1";//组织机构代码证代码 
		
		String businessLicense_ispass = "1";//营业执照
		String organiztionCode_ispass = "1";//组织机构代码证
		String taxRegistartion_ispass = "1";//税务登记证
		String lastYearStatements_ispass = "1";// 上一年财务报表
		String taxRelatedQuery_ispass = "1";//涉税保密信息查询申请表
		String companyCredit_ispass = "1";//征信查询授权书
		String ledalCredit_ispass = "1";//法人征信查询授权书
		String ledalCard_ispass = "1";//法人证件照片
		String controllerCredit_ispass = "1";//控制人征信查询授权书
		String controllerCard_ispass = "1";//控制人身份证照片
		String controllerSpoCard_ispass = "1";//控制人配偶身份证照片
		String controllerEducation_ispass = "1";//学历证明
		String controllerDomicilePlace_ispass = "1";//户籍证明
		String purchaseSaleContract_ispass = "1";////购销合同
		String customerList_ispass = "1";//上下游客户清单
		String lastTaxStatements_ispass = "1";//税务报表
		String lastControllerStatements_ispass = "1";//个人银行对账单
		String lastCompanyStatements_ispass = "1";//银行对账单
		String lastTaxCerifcate_ispass = "1";//纳税凭证
		String lastPaymentVoucher_ispass = "1";//缴费凭证
		String creditTechnology_ispass = "1";//资质和技术产品证书
		String propertyRightCard_ispass = "1";//场地证明
		String creditBusinessApp_ispass = "1";//授信业务申请书
		String directorsBoard_ispass = "1";//股东会或董事会决议
		String specicalIndustryCre_ispass = "1";//资质证书或经营许可证
		String capitalVerification_ispass = "1";//公司章程或验资报告
		String controllerAssetsLiaApply_ispass = "1";//资产负债申请表
		String marriageLicense_ispass = "1";//婚姻状况证明
		String controllerSpouseGuarantee_ispass = "1";//配偶的担保承诺书
		String enterpriseCreditRating_ispass = "1";//企业纳税信用等级证明
		String companyProfile_ispass = "1";//公司简介
		String theIndividualIncomeTax_ispass = "1";//实际控制连续两年缴纳个人所得税记录
		String lastYearThidYearAnnualLandTaxCertificate_ispass = "1";//上年度、本年度全年的国地税纳税证明
		String confirmation_ispass = "1";//实际控制人及配偶的担保确认书
		String housePropertyCard_ispass = "1";//房产证明
		String enterprisePayTaxesThrough_ispass = "1";//纳税查询授权书
		String theLoanCardCopy_ispass = "1";//贷款卡复印件
		String householdAssets_ispass = "1";//净资产证明householdAssets
		

		List<FaInfoNopass> faInfoNopasses = faInfoNopassService.findInfoNopassesByBusId(busId, BusinessType.SXD);
		for (FaInfoNopass faInfoNopass : faInfoNopasses) {
			// 营业执照注册号
			if (NoPass.LICENSE_NUM.equals(faInfoNopass.getInfoKey())) {
				licenseNum_ispass = "0";
			}
			// 税号
			if (NoPass.TAX_NUM.equals(faInfoNopass.getInfoKey())) {
				taxNum_ispass = "0";
			}
			// 组织机构代码证代码 
			if (NoPass.CARD_CODE.equals(faInfoNopass.getInfoKey())) {
				catdCode_ispass = "0";
			}
			//营业执照
			if (FileType.BUSINESS_LICENSE.equals(faInfoNopass.getInfoKey())) {
				businessLicense_ispass = "0";
			}
			//组织机构代码证
			if (FileType.ORGANIZTION_CODE_CERTIFICATE.equals(faInfoNopass.getInfoKey())) {
				organiztionCode_ispass = "0";
			}
			//税务登记证
			if (FileType.TAX_REGISTRATION_CERTIFICATE.equals(faInfoNopass.getInfoKey())) {
				taxRegistartion_ispass = "0";
			}
			// 上一年财务报表
			if (FileType.LAST_YEAR_FINANCIAL_STATEMENTS.equals(faInfoNopass.getInfoKey())) {
				lastYearStatements_ispass = "0";
			}
			//涉税保密信息查询申请表
			if (FileType.TAX_RELATED_CONFIDENTIAL_QUERY.equals(faInfoNopass.getInfoKey())) {
				taxRelatedQuery_ispass = "0";
			}
			//企业征信查询授权书
			if (FileType.COMPANY_CREDIT_REPORT_AUTHORIZATION.equals(faInfoNopass.getInfoKey())) {
				companyCredit_ispass = "0";
			}
			//法人征信查询授权书
			if (FileType.LEGAL_COMPANY_CREDIT_REPORT_AUTHORIZATION.equals(faInfoNopass.getInfoKey())) {
				ledalCredit_ispass = "0";
			}
			//法人身份证
			if (FileType.LEDAL_IDENTITY_CARD.equals(faInfoNopass.getInfoKey())) {
				ledalCard_ispass = "0";
			}
			//控制人征信查询授权书
			if (FileType.CONTROLLER_CREDIT_REPORT_AUTHORIZATION.equals(faInfoNopass.getInfoKey())) {
				controllerCredit_ispass = "0";
			}
			//控制人身份证照片
			if (FileType.CONTROLLER_IDENTITY_CARD.equals(faInfoNopass.getInfoKey())) {
				controllerCard_ispass = "0";
			}
			//控制人配偶身份证照片
			if (FileType.CONTROLLER_SPO_IDENTITY_CARD.equals(faInfoNopass.getInfoKey())) {
				controllerSpoCard_ispass = "0";
			}
			//学历证明
			if (FileType.EDUCATION.equals(faInfoNopass.getInfoKey())) {
				controllerEducation_ispass = "0";
			}
			//户籍证明
			if (FileType.DOMICILE_PLACE.equals(faInfoNopass.getInfoKey())) {
				controllerDomicilePlace_ispass = "0";
			}
			//购销合同
			if (FileType.PURCHASE_SALE_CONTRACT.equals(faInfoNopass.getInfoKey())) {
				purchaseSaleContract_ispass = "0";
			}
			//客服清单
			if (FileType.CUSTOMER_LIST.equals(faInfoNopass.getInfoKey())) {
				customerList_ispass = "0";
			}
			//税务报表
			if (FileType.LAST_YEAR_TAX_STATEMENTS.equals(faInfoNopass.getInfoKey())) {
				lastTaxStatements_ispass = "0";
			}
			//银行对账单
			if (FileType.LAST_THREE_MONTHS_STATEMENTS.equals(faInfoNopass.getInfoKey())) {
				lastCompanyStatements_ispass = "0";
			}
			//个人银行对账单
			if (FileType.LAST_THREE_MONTHS_CONTROLLER_STATEMENTS.equals(faInfoNopass.getInfoKey())) {
				lastControllerStatements_ispass = "0";
			}
			//纳税凭证和申请表
			if (FileType.LAST_YEAR_TAX_PAYMENT_CERTIFICATE.equals(faInfoNopass.getInfoKey())) {
				lastTaxCerifcate_ispass = "0";
			}
			//缴费凭证
			if (FileType.LAST_UTILITIES_PAYMENT_VOUCHER.equals(faInfoNopass.getInfoKey())) {
				lastPaymentVoucher_ispass = "0";
			}
			//资信和技术产品证书
			if (FileType.CREDIT_TECHNOLOGY_CERTIFICATION.equals(faInfoNopass.getInfoKey())) {
				creditTechnology_ispass = "0";
			}
			//场地证明
			if (FileType.PROPERTY_RIGHT_CARD.equals(faInfoNopass.getInfoKey())) {
				propertyRightCard_ispass = "0";
			}
			//授信业务申请书
			if (FileType.CREDIT_BUSINESS_APPLICATION.equals(faInfoNopass.getInfoKey())) {
				creditBusinessApp_ispass = "0";
			}
			//股东会或董事会决议
			if (FileType.DIRECTORS_BOARD.equals(faInfoNopass.getInfoKey())) {
				directorsBoard_ispass = "0";
			}
			//资质证书活经营许可证
			if (FileType.SPECIAL_INDUSTRY_CERTIFICATE.equals(faInfoNopass.getInfoKey())) {
				specicalIndustryCre_ispass = "0";
			}
			//公司章程验资报告
			if (FileType.COMPANY_ARTICLES.equals(faInfoNopass.getInfoKey())) {
				capitalVerification_ispass = "0";
			}
			//资产负债申请表
			if (FileType.CONTROLLER_ASSETS_LIABILITIES_APPLY.equals(faInfoNopass.getInfoKey())) {
				controllerAssetsLiaApply_ispass = "0";
			}
			//婚姻状况证明
			if (FileType.MARRIAGE_LICENSE.equals(faInfoNopass.getInfoKey())) {
				marriageLicense_ispass = "0";
			}
			//配偶的担保承诺书
			if (FileType.CONTROLLER_SPOUSE_GUARANTEE.equals(faInfoNopass.getInfoKey())) {
				controllerSpouseGuarantee_ispass = "0";
			}
			//企业纳税信用等级证明
			if (FileType.ENTERPRISE_CREDIT_RATING.equals(faInfoNopass.getInfoKey())) {
				enterpriseCreditRating_ispass = "0";
			}
			//公司简介
			if (FileType.COMPANY_PROFILE.equals(faInfoNopass.getInfoKey())) {
				companyProfile_ispass = "0";
			}
			//实际控制连续两年缴纳个人所得税记录
			if (FileType.THE_INDIVIDUAL_INCOME_TAX.equals(faInfoNopass.getInfoKey())) {
				theIndividualIncomeTax_ispass = "0";
			}
			//上年度、本年度全年的国地税纳税证明
			if (FileType.LAST_YEAR_THIS_YEAR_ANNUAL_LAND_TAX_CERTIFICATE.equals(faInfoNopass.getInfoKey())) {
				lastYearThidYearAnnualLandTaxCertificate_ispass = "0";
			}
			//实际控制人及配偶的担保确认书
			if (FileType.CONFIRMATION.equals(faInfoNopass.getInfoKey())) {
				confirmation_ispass = "0";
			}
			//房产证明
			if (FileType.HOUSE_PROPERTY_CARD.equals(faInfoNopass.getInfoKey())) {
				housePropertyCard_ispass = "0";
			}
			//纳税查询授权书
			if (FileType.ENTERPRISE_PAY_TAXES_THROUGH_THE_DATA_QUERY_AUTHORIZATION.equals(faInfoNopass.getInfoKey())) {
				enterprisePayTaxesThrough_ispass = "0";
			}
			//贷款卡复印件
			if (FileType.THE_LOAN_CARD_COPY.equals(faInfoNopass.getInfoKey())) {
				theLoanCardCopy_ispass = "0";
			}
			//净资产证明householdAssets
			if (FileType.HOUSEHOLD_ASSETS.equals(faInfoNopass.getInfoKey())) {
				householdAssets_ispass = "0";
			}
			
		}
		
		model.addAttribute("licenseNum_ispass", licenseNum_ispass);//营业执照注册号
		model.addAttribute("taxNum_ispass", taxNum_ispass);//税号
		model.addAttribute("catdCode_ispass", catdCode_ispass);//组织机构代码证代码 
		
		model.addAttribute("businessLicense_ispass", businessLicense_ispass);//营业执照
		model.addAttribute("organiztionCode_ispass", organiztionCode_ispass);//组织机构代码证
		model.addAttribute("taxRegistartion_ispass", taxRegistartion_ispass);//税务登记证
		model.addAttribute("lastYearStatements_ispass", lastYearStatements_ispass);// 上一年财务报表
		model.addAttribute("taxRelatedQuery_ispass", taxRelatedQuery_ispass);//涉税保密信息查询申请表
		model.addAttribute("companyCredit_ispass", companyCredit_ispass);//征信查询授权书
		model.addAttribute("ledalCredit_ispass", ledalCredit_ispass);//法人征信查询授权书
		model.addAttribute("ledalCard_ispass", ledalCard_ispass);//法人证件照片
		model.addAttribute("controllerCredit_ispass", controllerCredit_ispass);//控制人及配偶征信查询授权书
		model.addAttribute("controllerCard_ispass", controllerCard_ispass);//控制人身份证照片
		model.addAttribute("controllerSpoCard_ispass", controllerSpoCard_ispass);//控制人配偶身份证照片
		model.addAttribute("controllerEducation_ispass", controllerEducation_ispass);//学历证明
		model.addAttribute("controllerDomicilePlace_ispass", controllerDomicilePlace_ispass);//户籍证明
		model.addAttribute("purchaseSaleContract_ispass", purchaseSaleContract_ispass);////购销合同
		model.addAttribute("customerList_ispass", customerList_ispass);//上下游客户清单
		model.addAttribute("lastTaxStatements_ispass", lastTaxStatements_ispass);//税务报表
		model.addAttribute("lastControllerStatements_ispass", lastControllerStatements_ispass);//个人银行对账单
		model.addAttribute("lastCompanyStatements_ispass", lastCompanyStatements_ispass);//银行对账单
		model.addAttribute("lastTaxCerifcate_ispass", lastTaxCerifcate_ispass);//纳税凭证
		model.addAttribute("lastPaymentVoucher_ispass", lastPaymentVoucher_ispass);//缴费凭证
		model.addAttribute("creditTechnology_ispass", creditTechnology_ispass);//资质和技术产品证书
		model.addAttribute("propertyRightCard_ispass", propertyRightCard_ispass);//场地证明
		model.addAttribute("creditBusinessApp_ispass", creditBusinessApp_ispass);//授信业务申请书
		model.addAttribute("directorsBoard_ispass", directorsBoard_ispass);//股东会或董事会决议
		model.addAttribute("specicalIndustryCre_ispass", specicalIndustryCre_ispass);//资质证书或经营许可证
		model.addAttribute("capitalVerification_ispass", capitalVerification_ispass);//公司章程或验资报告
		model.addAttribute("controllerAssetsLiaApply_ispass", controllerAssetsLiaApply_ispass);//资产负债申请表
		model.addAttribute("marriageLicense_ispass", marriageLicense_ispass);//婚姻状况证明
		model.addAttribute("controllerSpouseGuarantee_ispass", controllerSpouseGuarantee_ispass);//配偶的担保承诺书
		model.addAttribute("enterpriseCreditRating_ispass", enterpriseCreditRating_ispass);//企业纳税信用等级证明
		model.addAttribute("companyProfile_ispass", companyProfile_ispass);//公司简介
		model.addAttribute("theIndividualIncomeTax_ispass", theIndividualIncomeTax_ispass);//实际控制连续两年缴纳个人所得税记录
		model.addAttribute("lastYearThidYearAnnualLandTaxCertificate_ispass", lastYearThidYearAnnualLandTaxCertificate_ispass);//上年度、本年度全年的国地税纳税证明
		model.addAttribute("confirmation_ispass", confirmation_ispass);//实际控制人及配偶的担保确认书
		model.addAttribute("housePropertyCard_ispass", housePropertyCard_ispass);//房产证明
		model.addAttribute("enterprisePayTaxesThrough_ispass", enterprisePayTaxesThrough_ispass);//纳税查询授权书
		model.addAttribute("theLoanCardCopy_ispass", theLoanCardCopy_ispass);//贷款卡复印件
		model.addAttribute("householdAssets_ispass", householdAssets_ispass);//净资产证明householdAssets

        /*if (!StringUtils.isEmpty(addr)) {
			model.addAttribute("addr", addr);//配偶的担保承诺书
		}*/
		return "task/applyDetail";
	}
}
