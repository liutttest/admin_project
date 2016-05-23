package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
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

import com.evan.common.user.domain.FaCenterDataBank;
import com.evan.common.user.domain.FaCommunicateLog;
import com.evan.common.user.domain.FaInfoNopass;
import com.evan.common.user.domain.FaOperationRecord;
import com.evan.common.user.domain.FaTempScene;
import com.evan.common.user.domain.FaTemplate;
import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.domain.FwComAccount;
import com.evan.common.user.domain.FwComPerBus;
import com.evan.common.user.domain.SysUser;
import com.evan.common.user.service.FaCenterDataBankService;
import com.evan.common.user.service.FaCommunicateLogService;
import com.evan.common.user.service.FaInfoNopassService;
import com.evan.common.user.service.FaOperationRecordService;
import com.evan.common.user.service.FaTempSceneService;
import com.evan.common.user.service.FaTemplateService;
import com.evan.common.user.service.FilesBankService;
import com.evan.common.user.service.FwBusinessSxdService;
import com.evan.common.user.service.FwComAccountService;
import com.evan.common.user.service.FwComPerBusService;
import com.evan.common.user.service.FwCompanyService;
import com.evan.common.user.service.FwLoanApplyService;
import com.evan.common.user.service.SysUserService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.ConstantTool.ApplyState;
import com.evan.common.utils.ConstantTool.BusinessType;
import com.evan.common.utils.ConstantTool.CommlogType;
import com.evan.common.utils.ConstantTool.FileType;
import com.evan.common.utils.ConstantTool.NoPass;
import com.evan.common.utils.ConstantTool.PersonType;
import com.evan.common.utils.ConstantTool.SCENE;
import com.evan.common.utils.ConstantTool.TempleteType;
import com.evan.common.utils.RequestUtils;
import com.evan.finance.admin.bank.BankTools;
import com.evan.finance.admin.pdf.PDFHandleTools;
import com.evan.jaron.core.AppContext;
import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.plugins.dictionary.DictionaryHelper;
import com.evan.jaron.plugins.security.SecurityTokenHolder;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.nd.common_file.Constant;
import com.evan.nd.common_file.domain.FwFiles;
import com.evan.nd.common_file.service.FwFilesService;
import com.itextpdf.text.DocumentException;

@RequestMapping("/fwBusinessSxd")
@Controller
public class FwBusinessSxdController extends BaseController {

	final Logger logger = LoggerFactory.getLogger(FwBusinessSxdController.class);	
	
	@Autowired
	private FwBusinessSxdService fwBusinessSxdService;
	@Autowired
	private FwFilesService fwFilesService;
	@Autowired
	private FwComPerBusService fwComPerBusService;
	@Autowired
	private FwCompanyService fwCompanyService;
	@Autowired
	private DictionaryHelper dictionaryHelper;
	@Autowired
	private FaCenterDataBankService faCenterDataBankService;
	@Autowired
	private FwComAccountService fwComAccountService;
	@Autowired
	private FaCommunicateLogService faCommunicateLogService;
	@Autowired
	private FaOperationRecordService faOperationRecordService;
	@Autowired
	private FaInfoNopassService faInfoNopassService;
	@Autowired
	private FwLoanApplyService fwLoanApplyService;
	
	@Autowired
	private FaTemplateService faTemplateService;
	
	@Autowired
	private FaTempSceneService faTempSceneService;
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	FilesBankService filesBankService;
	
	@Autowired
	BankTools bankTools;
	
	

	
	
	/**
	 * liutt
	 * 点击税信贷管理
	 * @param map
	 * @param searchForm
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = GET)
	public String goToBusinessSxd(Model model) {
		//查询税信贷列表
		List<Map<String,Object>> fwBussiness = fwBusinessSxdService.getBussinessSxdList();
		model.addAttribute("list", fwBussiness);
//		model.addAttribute("list", fwBussiness);
		return "fw_business_sxd/list";
	}
	
	/**
	 * @liutt
	 * 现场开户 下载打印资料 功能
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/printData", method = {GET,POST})
	@ResponseBody
	public String getfwCompanyBusDetail(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		String fileType = MapUtils.getString(map, "fileType");
		String busId = MapUtils.getString(map, "busId");
		String[] filetypes = fileType.split(",");
		
		// 获取开户时间、开户地点、联系人和备注信息
		FaCenterDataBank faCenterDataBank = faCenterDataBankService.getByBusId(busId);
		List<FaCommunicateLog> logList = faCommunicateLogService.findByBusinessIdAndType(busId, CommlogType.QUOTA_RESULT);
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId);
		FwComAccount fwcomAccount = fwComAccountService.getByComId(fwBusinessSxd.getComId());
		SysUser sysUser = sysUserService.findByPk(fwcomAccount.getUserId());
		
		String contactName = sysUser.getRealName();
		String tel = sysUser.getTel();
		String fieldTime = faCenterDataBank.getFieldTime();
		String fieldAddr = faCenterDataBank.getFieldAddr();
		String content = "";
		if (logList.size() > 0)
		{
			content = logList.get(0).getContent();
		}
		
		List<String> list = fwBusinessSxdService.getListFileByBusIdAndFileType(busId, filetypes);
		PDFHandleTools pdfHandleTools = new PDFHandleTools();
		
		String fileName = fwBusinessSxdService.createFilePathPDFNew(busId, BusinessType.SXD);
		try {
			pdfHandleTools.createPdfToLocal(list, fileName, contactName, fieldTime, fieldAddr, content,tel);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//调用下载方法
		/*FileDownload fileDownload = new FileDownload();
		DownLoadResults downLoadResults =  fileDownload.downloadFileTwo(response, fileName);*/
		String name = fileName.replace(Constant.PDF_UPLOAD_PATH, "");
		return JsonUtils.toJson(RequestUtils.successResult(name));
	}
	
	/**
	 * @liutt
	 * 根据业务id 查询 所有和业务相关的可下载的文件
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findFileTOPrint", method = {GET,POST})
	@ResponseBody
	public String findFileTOPrint(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		String busId = MapUtils.getString(map, "busId");
		List<Map<String, Object>> files = fwFilesService.getFilesByBusIdandBusTypeGroupByFileType(busId, BusinessType.SXD);
		
		// 获取联系人、开户时间、开户地点和备注信息
		FaCenterDataBank faCenterDataBank = faCenterDataBankService.getByBusId(busId);
		List<FaCommunicateLog> logList = faCommunicateLogService.findByBusinessIdAndType(busId, CommlogType.QUOTA_RESULT);
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId);
		FwComAccount fwcomAccount = fwComAccountService.getByComId(fwBusinessSxd.getComId());
		SysUser sysUser = sysUserService.findByPk(fwcomAccount.getUserId());
		
		String contactName = sysUser.getRealName() != null ?  sysUser.getRealName() : "";
		String fieldTime = faCenterDataBank.getFieldTime() != null ? faCenterDataBank.getFieldTime() : "";
		String fieldAddr = faCenterDataBank.getFieldAddr() != null ? faCenterDataBank.getFieldAddr() : "";
		String content = "";
		if (logList.size() > 0)
		{
			content = logList.get(0).getContent() != null ? logList.get(0).getContent() : "";
		}
		
		Map<String, String> fieldInfoMap = new HashMap<String, String>();
		fieldInfoMap.put("contactName", contactName);
		fieldInfoMap.put("fieldTime", fieldTime);
		fieldInfoMap.put("fieldAddr", fieldAddr);
		fieldInfoMap.put("content", content);
		fieldInfoMap.put("fieldTel", sysUser.getTel());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", files);
		resultMap.put("fieldInfo", fieldInfoMap);
		
		return JsonUtils.toJson(RequestUtils.successResult(resultMap));
	}
	
	/**
	 * @liutt
	 * 打印
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/printDetail", method = {GET,POST})
	@ResponseBody
	public String printDetail(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		String fileType = MapUtils.getString(map, "fileType");
		String busId = MapUtils.getString(map, "busId");
		List<FwFiles> files = fwFilesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, fileType);
		return JsonUtils.toJson(RequestUtils.successResult(files));
	}

	
	/**
	 * 根据企业id查询开户申请 任务分派是所需要的查看的数据
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getDataCenterByBusId", method = {GET,POST})
	@ResponseBody
	public String getDataCenterByBusId(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		String busId = MapUtils.getString(map, "busId");
		FaCenterDataBank faCenterDataBank = faCenterDataBankService.getByBusId(busId);
		List<FaCommunicateLog> list = faCommunicateLogService.findByBusinessIdAndType(busId, CommlogType.QUOTA_RESULT);
		Map<String, Object> map2 = new HashMap<String, Object>();
		//开户时间
		map2.put("fieldTime", faCenterDataBank.getFieldTime());
		//开户地点
		map2.put("fieldAddr", faCenterDataBank.getFieldAddr());
		
		
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId);
		FwComAccount fwcomAccount = fwComAccountService.getByComId(fwBusinessSxd.getComId());
		SysUser sysUser = sysUserService.findByPk(fwcomAccount.getUserId());
		
		String contactName = sysUser.getRealName() != null ?  sysUser.getRealName() : "";
		//联系人姓名
		map2.put("contractName", contactName);
		//联系人电话
		map2.put("fieldTel", sysUser.getTel());
		//备注
		if (list.size()>0) {
			map2.put("remarks", list.get(0).getContent());
		}
		return JsonUtils.toJson(RequestUtils.successResult(map2));
	}
	
	
	
	/**
	 * 根据企业id查询企业控制人配偶详情
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getBusinessByBusId", method = {GET,POST})
	@ResponseBody
	public String getfwCompanyBusOwnerSpouseDetail(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		String busId = MapUtils.getString(map, "busId");
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId);
		return JsonUtils.toJson(RequestUtils.successResult(fwBusinessSxd));
	}
	
	/**
	 * 根据企业id查询企业控制人配偶详情
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getCenterBankDataByBusid", method = {GET,POST})
	@ResponseBody
	public String getCenterBankDataByBusid(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		String busId = MapUtils.getString(map, "busId");
		FaCenterDataBank faCenterDataBank = faCenterDataBankService.getByBusId(busId);
		return JsonUtils.toJson(RequestUtils.successResult(faCenterDataBank));
	}
	
	
	
	//二期
	/**根据业务id 查询申请资料详情
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getfwApplyBusDetail", method = GET)
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
		//TODO 查询补充材料
		
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
	
	/**
	 * 将用户上传的资料标记为不合格(luy)
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/doUpdateNoPass", method = POST)
	@ResponseBody
	public String doUpdateNoPass(@RequestParam Map<String,Object> map,Model model) {
		// 获取当前登录用户id
		Long userId = SecurityTokenHolder.getSecurityToken().getUser().getUserId();
		
		String busId = MapUtils.getString(map, "busId");  //业务id
		String infoName = MapUtils.getString(map, "infoName"); // 资料名称
		String infoKey = MapUtils.getString(map, "infoKey"); // 资料key
		
		FaInfoNopass faInfoNopass = faInfoNopassService.saveFaInfoNopass(infoName, infoKey, busId, BusinessType.SXD, userId);
		
		if (faInfoNopass != null) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("faInfoNopass", faInfoNopass);
			return JsonUtils.toJson(RequestUtils.successResult(retMap));
		}else {
			return JsonUtils.toJson(RequestUtils.successResult(null));
		}
		
	}
	/**
	 * 将用户上传的资料标记为不合格后再修改为合格(luy)
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/doUpdateNoPassToPass", method = POST)
	@ResponseBody
	public String doUpdateNoPassToPass(@RequestParam Map<String,Object> map,Model model) {
		// 获取当前登录用户id
//		Long userId = SecurityTokenHolder.getSecurityToken().getUser().getUserId();
		
		String busId = MapUtils.getString(map, "busId");  //业务id
		String infoKey = MapUtils.getString(map, "infoKey"); // 资料key
		
		FaInfoNopass faInfoNopass =  faInfoNopassService.findFaInfoNopassesByBusinessId(infoKey, busId, BusinessType.SXD);
		if (faInfoNopass != null) {
			FaInfoNopass retNopass = faInfoNopassService.updateFaInfoNopassIsHistory(faInfoNopass.getInfoId(), "1");
			if (retNopass != null) {
				Map<String, Object> retMap = new HashMap<String, Object>();
				retMap.put("faInfoNopass", faInfoNopass);
				return JsonUtils.toJson(RequestUtils.successResult(retMap));
			}else {
				return JsonUtils.toJson(RequestUtils.successResult(null));
			}
		}else {
			return JsonUtils.toJson(RequestUtils.successResult(null));
		}
		
	}
	
	/**查询 人工打分 需要的数据
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getCenterDataByBusId", method = POST)
	@ResponseBody
	public String getCenterDataByBusId(@RequestParam Map<String,Object> map,Model model) {
		String busId = MapUtils.getString(map, "busId");
		FaCenterDataBank faCenterDataBank = faCenterDataBankService.getByBusId(busId);
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId);
		FwComPerBus fwComPerBus = fwComPerBusService.findByBusinessIdAndType(busId, BusinessType.SXD,PersonType.COM_CONTROLLER);
		Map<String, Object> mapRes = new HashMap<String, Object>();
		mapRes.put("faCenterdata", faCenterDataBank);
		mapRes.put("fwBusinessSxd", fwBusinessSxd);
		mapRes.put("fwComPerBus", fwComPerBus);
		return JsonUtils.toJson(RequestUtils.successResult(mapRes));
	}
	
	/**
	 * 根据业务id查询操作日志（luy）
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getCaoZuoLog", method = POST)
	@ResponseBody
	public String getCaoZuoLog(@RequestParam Map<String,Object> map,Model model) {
		String busId = MapUtils.getString(map, "busId");
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<FaOperationRecord> faOperationRecords = faOperationRecordService.findByBusId(busId);
		retMap.put("faOperationRecords", faOperationRecords);
		return JsonUtils.toJson(RequestUtils.successResult(retMap));
	}
	
	/**
	 * 根据业务id查询操作日志（luy）
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getCaoZuoLogByBusIdAndType", method = POST)
	@ResponseBody
	public String getCaoZuoLogByBusIdAndType(@RequestParam Map<String,Object> map,Model model) {
		String busId = MapUtils.getString(map, "busId");
		String busType = MapUtils.getString(map, "busType");
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<FaOperationRecord> faOperationRecords = faOperationRecordService.findByBusId(busId);
		retMap.put("faOperationRecords", faOperationRecords);
		return JsonUtils.toJson(RequestUtils.successResult(retMap));
	}
	
    /**
     * 获取税信贷相关文件(luy)
     *
     * @return
     */
    @RequestMapping(value = "/applySxdFilesByType", method = {GET, POST})
    @ResponseBody
    public String applySxdFiles(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request, HttpServletResponse response) {
        String businessType = BusinessType.SXD;
        String businessId = MapUtils.getString(map, "businessId", "");// 业务id
        String fileType = MapUtils.getString(map, "fileType", "");// 文件类型
        // 查询 业务相关信息

        // 查询 业务相关文件
        List<FwFiles> files = fwFilesService.findByBusIdAndBusTypeAndFileType(businessId, businessType, fileType);
        Map<String, Object> mapRet = new HashMap<String, Object>();
        mapRet.put("files", files);

        return JsonUtils.toJson(RequestUtils.successResult(mapRet));

    }
	
    /**
     *  lixj
     * @param map
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value="/accessVerify/getAccessVerifyTemplate", method={GET, POST})
	@ResponseBody
	public String  getTemplete(@RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response)
	{
		String businessId =  MapUtils.getString(map, "busId", "");
		String type = MapUtils.getString(map, "type", "");
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(businessId);
		
		String sceneKey = null;
		
		String quotaApplyResult = "";
		String accessVerifyResult = "";
		
		if (type.equals("access-verify"))
		{
			// 准入验证的通知
			// 判断当前业务的状态
			if (fwBusinessSxd.getApplyState().equals(ApplyState.REJECT_ADMITTANCE))
			{
				sceneKey = SCENE.ZRSB;
				accessVerifyResult = "fail";
			}
			else {
				sceneKey = SCENE.ZRCG;
				accessVerifyResult = "success";
			}
		}
		else if (type.equals("access-verify-rejected"))
		{
			// 准入验证驳回
			sceneKey = SCENE.ZRBH;
		}
		else if (type.equals("quota-apply"))
		{
			FaCenterDataBank faCenterDataBank = faCenterDataBankService.getByBusId(fwBusinessSxd.getBsId());
			// 额度申请通知
			// 判断当前业务的状态
			if (faCenterDataBank.getApplyMoney()!=null && faCenterDataBank.getApplyMoney().compareTo(new BigDecimal(0))==1 && !fwBusinessSxd.getApplyState().equals(ApplyState.REJECT_LIMIT))
			{
				sceneKey = SCENE.EDSQCG;
				quotaApplyResult = "success";
			}
			else {
				sceneKey = SCENE.EDSQSB;
				quotaApplyResult = "fail";
			}
		}
		else if (type.equals("quota-apply-rejected"))
		{
			//额度申请驳回通知
			sceneKey = SCENE.EDSQBO;
		}
		
		
		// 根据场景key获取场景
		FaTempScene faTempScene = faTempSceneService.getTempBySecneKey(sceneKey);
		
		Map<String, Object> resultMap = new  HashMap<String, Object>();
		
		//  邮件模版
		List<FaTemplate> emailTemplist = faTemplateService.findBySceneId(faTempScene.getTsId(), TempleteType.EMAIL);
		resultMap.put("emailTemplate", emailTemplist);
		
		// 短信模版
		List<FaTemplate> smsTempList = faTemplateService.findBySceneId(faTempScene.getTsId(), TempleteType.SMS);
		resultMap.put("smsTemplate", smsTempList);
		
		// 站内信
		List<FaTemplate> messageTempList = faTemplateService.findBySceneId(faTempScene.getTsId(), TempleteType.MESSAGE);
		resultMap.put("messageTemplate", messageTempList);
		
		resultMap.put("quotaApplyResult", quotaApplyResult);
		resultMap.put("accessVerifyResult", accessVerifyResult);
		
		return JsonUtils.toJson(RequestUtils.successResult(resultMap));
	}
	
	/**
	 * 根据场景查询模板
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getTemplateByScene", method={GET, POST})
	@ResponseBody
	public String  getTemplateByScene(@RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response)
	{
//		String businessId =  MapUtils.getString(map, "busId", "");
		String scene = MapUtils.getString(map, "scene", ""); // 场景
//		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(businessId);
		
		// 根据场景key获取场景
		FaTempScene faTempScene = faTempSceneService.getTempBySecneKey(scene);
		
		Map<String, Object> resultMap = new  HashMap<String, Object>();
		
		//  邮件模版
		List<FaTemplate> emailTemplist = faTemplateService.findBySceneId(faTempScene.getTsId(), TempleteType.EMAIL);
		resultMap.put("emailTemplate", emailTemplist);
		
		// 短信模版
		List<FaTemplate> smsTempList = faTemplateService.findBySceneId(faTempScene.getTsId(), TempleteType.SMS);
		resultMap.put("smsTemplate", smsTempList);
		
		// 站内信
		List<FaTemplate> messageTempList = faTemplateService.findBySceneId(faTempScene.getTsId(), TempleteType.MESSAGE);
		resultMap.put("messageTemplate", messageTempList);
		
		return JsonUtils.toJson(RequestUtils.successResult(resultMap));
	}
	
	
	/**
	 * 查询业务的不合格记录
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getBusFileByBusId", method = POST)
	@ResponseBody
	public String getBusFileByBusId(@RequestParam Map<String,Object> map,Model model) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		String busId = MapUtils.getString(map, "busId");
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk(busId);
		if (fwBusinessSxd==null) {
			return JsonUtils.toJson(RequestUtils.failResult(null));
		}
		
		
		List<FwFiles> list  = fwFilesService.findByBusIdAndBusType(busId, BusinessType.SXD);
		
		List<FwFiles> specicalIndustryCre =  new ArrayList<FwFiles>();//资质证书或经营许可证
		List<FwFiles> lastPaymentVoucher =  new ArrayList<FwFiles>();//缴费凭证
		List<FwFiles> lastControllerStatements =  new ArrayList<FwFiles>();//个人银行对账单
		List<FwFiles> creditTechnology =  new ArrayList<FwFiles>();//资质和技术产品证书
		List<FwFiles> controllerSpouseGuarantee =  new ArrayList<FwFiles>();//配偶的担保承诺书
		List<FwFiles> confirmation =  new ArrayList<FwFiles>();//实际控制人及配偶的担保确认书
		List<FwFiles> housePropertyCard =  new ArrayList<FwFiles>();//房产证明
		List<FwFiles> controllerSpoCard =  new ArrayList<FwFiles>();//控制人配偶身份证照片
		
		

		for (FwFiles fwFiles : list) {
			//资质证书或经营许可证
			if (FileType.SPECIAL_INDUSTRY_CERTIFICATE.equals(fwFiles.getFileType())) {
				specicalIndustryCre.add(fwFiles);
			}
			//缴费凭证
			if (FileType.LAST_UTILITIES_PAYMENT_VOUCHER.equals(fwFiles.getFileType())) {
				lastPaymentVoucher.add(fwFiles);
			}
			//个人银行对账单
			if (FileType.LAST_THREE_MONTHS_CONTROLLER_STATEMENTS.equals(fwFiles.getFileType())) {
				lastControllerStatements.add(fwFiles);
			}
			//资信和技术产品证书
			if (FileType.CREDIT_TECHNOLOGY_CERTIFICATION.equals(fwFiles.getFileType())) {
				creditTechnology.add(fwFiles);
			}
			//配偶的担保承诺书
			if (FileType.CONTROLLER_SPOUSE_GUARANTEE.equals(fwFiles.getFileType())) {
				controllerSpouseGuarantee.add(fwFiles);
			}
			//实际控制人及配偶的担保确认书
			if (FileType.CONFIRMATION.equals(fwFiles.getFileType())) {
				confirmation.add(fwFiles);
			}
			//房产证明
			if (FileType.HOUSE_PROPERTY_CARD.equals(fwFiles.getFileType())) {
				housePropertyCard.add(fwFiles);
			}
			//控制人配偶身份证照片
			if (FileType.CONTROLLER_SPO_IDENTITY_CARD.equals(fwFiles.getFileType())) {
				controllerSpoCard.add(fwFiles);
			}
		}

		retMap.put("specicalIndustryCre", specicalIndustryCre);//资质证书或经营许可证
		retMap.put("lastPaymentVoucher", lastPaymentVoucher);//缴费凭证
		retMap.put("lastControllerStatements", lastControllerStatements);//个人银行对账单
		retMap.put("creditTechnology", creditTechnology);//资质和技术产品证书
		retMap.put("controllerSpouseGuarantee", controllerSpouseGuarantee);//配偶的担保承诺书
		retMap.put("confirmation", confirmation);//实际控制人及配偶的担保确认书
		retMap.put("housePropertyCard", housePropertyCard);//房产证明
		retMap.put("controllerSpoCard", controllerSpoCard);//控制人配偶身份证照片

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
			if (FileType.CONFIRMATION.equals(faInfoNopass.getInfoKey())) {
				housePropertyCard_ispass = "0";
			}
			//纳税查询授权书
			if (FileType.ENTERPRISE_PAY_TAXES_THROUGH_THE_DATA_QUERY_AUTHORIZATION.equals(faInfoNopass.getInfoKey())) {
				enterprisePayTaxesThrough_ispass = "0";
			}
		}
		
		retMap.put("licenseNum_ispass", licenseNum_ispass);//营业执照注册号
		retMap.put("taxNum_ispass", taxNum_ispass);//税号
		retMap.put("catdCode_ispass", catdCode_ispass);//组织机构代码证代码 
		
		retMap.put("businessLicense_ispass", businessLicense_ispass);//营业执照
		retMap.put("organiztionCode_ispass", organiztionCode_ispass);//组织机构代码证
		retMap.put("taxRegistartion_ispass", taxRegistartion_ispass);//税务登记证
		retMap.put("lastYearStatements_ispass", lastYearStatements_ispass);// 上一年财务报表
		retMap.put("taxRelatedQuery_ispass", taxRelatedQuery_ispass);//涉税保密信息查询申请表
		retMap.put("companyCredit_ispass", companyCredit_ispass);//征信查询授权书
		retMap.put("ledalCredit_ispass", ledalCredit_ispass);//法人征信查询授权书
		retMap.put("ledalCard_ispass", ledalCard_ispass);//法人证件照片
		retMap.put("controllerCredit_ispass", controllerCredit_ispass);//控制人及配偶征信查询授权书
		retMap.put("controllerCard_ispass", controllerCard_ispass);//控制人身份证照片
		retMap.put("controllerSpoCard_ispass", controllerSpoCard_ispass);//控制人配偶身份证照片
		retMap.put("controllerEducation_ispass", controllerEducation_ispass);//学历证明
		retMap.put("controllerDomicilePlace_ispass", controllerDomicilePlace_ispass);//户籍证明
		retMap.put("purchaseSaleContract_ispass", purchaseSaleContract_ispass);////购销合同
		retMap.put("customerList_ispass", customerList_ispass);//上下游客户清单
		retMap.put("lastTaxStatements_ispass", lastTaxStatements_ispass);//税务报表
		retMap.put("lastControllerStatements_ispass", lastControllerStatements_ispass);//个人银行对账单
		retMap.put("lastCompanyStatements_ispass", lastCompanyStatements_ispass);//银行对账单
		retMap.put("lastTaxCerifcate_ispass", lastTaxCerifcate_ispass);//纳税凭证
		retMap.put("lastPaymentVoucher_ispass", lastPaymentVoucher_ispass);//缴费凭证
		retMap.put("creditTechnology_ispass", creditTechnology_ispass);//资质和技术产品证书
		retMap.put("propertyRightCard_ispass", propertyRightCard_ispass);//场地证明
		retMap.put("creditBusinessApp_ispass", creditBusinessApp_ispass);//授信业务申请书
		retMap.put("directorsBoard_ispass", directorsBoard_ispass);//股东会或董事会决议
		retMap.put("specicalIndustryCre_ispass", specicalIndustryCre_ispass);//资质证书或经营许可证
		retMap.put("capitalVerification_ispass", capitalVerification_ispass);//公司章程或验资报告
		retMap.put("controllerAssetsLiaApply_ispass", controllerAssetsLiaApply_ispass);//资产负债申请表
		retMap.put("marriageLicense_ispass", marriageLicense_ispass);//婚姻状况证明
		retMap.put("controllerSpouseGuarantee_ispass", controllerSpouseGuarantee_ispass);//配偶的担保承诺书
		retMap.put("enterpriseCreditRating_ispass", enterpriseCreditRating_ispass);//企业纳税信用等级证明
		retMap.put("companyProfile_ispass", companyProfile_ispass);//公司简介
		retMap.put("theIndividualIncomeTax_ispass", theIndividualIncomeTax_ispass);//实际控制连续两年缴纳个人所得税记录
		retMap.put("lastYearThidYearAnnualLandTaxCertificate_ispass", lastYearThidYearAnnualLandTaxCertificate_ispass);//上年度、本年度全年的国地税纳税证明
		retMap.put("confirmation_ispass", confirmation_ispass);//实际控制人及配偶的担保确认书
		retMap.put("housePropertyCard_ispass", housePropertyCard_ispass);//房产证明
		retMap.put("enterprisePayTaxesThrough_ispass", enterprisePayTaxesThrough_ispass);//纳税查询授权书
        /*if (!StringUtils.isEmpty(addr)) {
			model.addAttribute("addr", addr);//配偶的担保承诺书
		}*/
		return JsonUtils.toJson(RequestUtils.successResult(retMap));
	}
	
	/**
	 * 修改业务的放款次数(luy)
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/doUpdateLoanNo", method = {GET, POST})
    @ResponseBody
    public String doUpdateLoanNo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		
		String businessId = MapUtils.getString(map, "businessId", "");// 业务id
        Integer flag = MapUtils.getInteger(map, "flag", null);// 放款的次数的标识
        Integer loanNo = MapUtils.getInteger(map, "loanNo", null);// 放款的次数
        String reason = MapUtils.getString(map, "reason", "");// 设置放款次数的备注
        Long userId = RequestUtils.getLoginedUser().getUserId(); //当前的登录用户
        
        Integer loanNum = null;
        if (flag == 0) {
        	loanNum = 0; //禁止放款（放款次数设置为0）
		}else if (flag == -1) {
			loanNum = -1; //不限制放款次数（放款次数设置为-1）
		}else if (flag == -2) {
			loanNum = Integer.parseInt(AppContext.getInstance().getString("defaultLoanApplyCount").trim()); //设置为默认值（放款次数设置为配置文件中的次数）
		}else if (flag == -3) {
			loanNum = loanNo;
		}
        
        FwBusinessSxd fwBusinessSxd = null;
        if (loanNum == null) {
        	mapRet.put("fwBusinessSxd", fwBusinessSxd);
        	return JsonUtils.toJson(RequestUtils.successResult(mapRet));
		}else {
			// 修改放款次数
			fwBusinessSxd = fwBusinessSxdService.updateBusinessSxdNum(businessId, loanNum, userId);
		}
        
        if (fwBusinessSxd != null) {
        	// 存储备注信息
        	faCommunicateLogService.saveFaCommunicateLog(businessId, ConstantTool.BusinessType.SXD, "09", reason);
        	mapRet.put("fwBusinessSxd", fwBusinessSxd);
        	return JsonUtils.toJson(RequestUtils.successResult(mapRet));
		}else {
			return JsonUtils.toJson(RequestUtils.failResult(null));
		}
        
    }
	
	
	/**
	 * 修改业务的还款次数(luy)
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/doUpdateRepayNo", method = {GET, POST})
    @ResponseBody
    public String doUpdateRepayNo(@RequestParam Map<String, Object> map, Model model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		
		String businessId = MapUtils.getString(map, "businessId", "");// 业务id
        Integer flag = MapUtils.getInteger(map, "flag", null);// 还款的次数的标识
        String reason = MapUtils.getString(map, "reason", "");// 设置还款次数的备注
//        Long userId = RequestUtils.getLoginedUser().getUserId(); //当前的登录用户
        
        Integer count = null;
        if (flag == 0) {
        	count = 0; //禁止还款（还款次数设置为0）
		}else if (flag == -1) {
			count = -1; //不限制还款次数（还款次数设置为-1）
		}else if (flag == -2) {
			count = Integer.parseInt(AppContext.getInstance().getString("defaultRepayMentCount").trim()); //设置为默认值（还款次数设置为配置文件中的次数）
		}
        
        Boolean retBoolean = null;
        if (count == null) {
        	return JsonUtils.toJson(RequestUtils.successResult(mapRet));
		}else {
			// 修改还款次数
			retBoolean = fwLoanApplyService.updateFwLoanApplyNumByBusId(businessId, BusinessType.SXD, count);;
		}
        
        if (retBoolean) {
        	// 存储备注信息
        	faCommunicateLogService.saveFaCommunicateLog(businessId, ConstantTool.BusinessType.SXD, "10", reason);
        	return JsonUtils.toJson(RequestUtils.successResult(mapRet));
		}else {
			return JsonUtils.toJson(RequestUtils.failResult(null));
		}
        
    }
	
}

