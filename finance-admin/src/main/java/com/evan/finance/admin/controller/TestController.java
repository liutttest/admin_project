package com.evan.finance.admin.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.common.user.domain.FwBusinessSxd;
import com.evan.common.user.domain.FwComPerBus;
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
import com.evan.common.user.service.SysUserService;
import com.evan.common.utils.ConstantTool;
import com.evan.common.utils.RequestUtils;
import com.evan.common.utils.ConstantTool.BusinessType;
import com.evan.common.utils.ConstantTool.FileType;
import com.evan.common.utils.ConstantTool.MaritalState;
import com.evan.finance.admin.activiti.bean.WorkFlow;
import com.evan.finance.admin.activiti.service.ActivitiSxdService;
import com.evan.finance.admin.bank.BankTools;
import com.evan.finance.admin.pdf.PDFHandleTools;
import com.evan.jaron.core.dao.support.SearchForm;
import com.evan.jaron.core.web.controller.BaseController;
import com.evan.jaron.plugins.dictionary.DictionaryHelper;
import com.evan.jaron.util.JsonUtils;
import com.evan.jaron.util.MapUtils;
import com.evan.jaron.util.StringUtils;
import com.evan.nd.common_file.Constant;
import com.evan.nd.common_file.domain.FwFiles;
import com.evan.nd.common_file.service.FwFilesService;
import com.itextpdf.text.DocumentException;
@Controller
public class TestController extends BaseController{
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
	private FaTemplateService faTemplateService;
	
	@Autowired
	private FaTempSceneService faTempSceneService;
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	FilesBankService filesBankService;
	
	@Autowired
	BankTools bankTools;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ActivitiSxdService activitiSxdService;
	
	@Autowired
	private RuntimeService  runtimeService;
	/**
	 * @liutt
	 * 现场开户 下载打印资料 功能
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/printDatatest", method = {GET,POST})
	@ResponseBody
	public String getfwCompanyBusDetailnew(@RequestParam Map<String,Object> map,Model model,HttpServletResponse response) {
		
		String busId = "201511200004";
		FwBusinessSxd fwBusinessSxd = fwBusinessSxdService.findByPk("201511200004"); // 业务信息
		FwComPerBus shiJiKongZhiRenBus = fwComPerBusService.findByBusinessIdAndType(busId, ConstantTool.BusinessType.SXD, ConstantTool.PersonType.COM_CONTROLLER); // 查询实际控制人相关信息
		
		//根据业务id  上传准入验证所需的文件
		//企业征信 授权书
		List<FwFiles> listComCredit = fwFilesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.COMPANY_CREDIT_REPORT_AUTHORIZATION);
		//法人证件照片
		List<FwFiles> listPerCredit = fwFilesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.LEDAL_IDENTITY_CARD);
		//控制人征信报告查询授权书
		List<FwFiles> listPerCreditCon = fwFilesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.CONTROLLER_CREDIT_REPORT_AUTHORIZATION);
		String fileNames = "";
		List<Map<String, String>> listFile = new ArrayList<Map<String,String>>();
		
		
		/**
		 * 构造“申请人身份证正面	pdf	
		 * 实际控制人配偶身份证正面；
		 * 实际控制人配偶身份证背面；
		 * 企业纳税数据查询授权书；
		 * 纳税人、扣缴义务人涉税保密信息查询申请表； 
		 * 营业执照 ；
		 * 税务登记证  ；
		 * 组织机构代码证”
		 */
		List<String> listother =  new ArrayList<String>();
		
		// 判断实际控制人是否有配偶,如果有配偶，则上传配偶的身份证正面和反面
		if (MaritalState.MARRIED.equals(shiJiKongZhiRenBus.getMaritalState())) {
			// 实际控制人配偶的身份证正面和反面
			List<FwFiles> controllerSpoList = fwFilesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.CONTROLLER_SPO_IDENTITY_CARD);
			if (controllerSpoList.size()>0) {
				for (FwFiles fwFiles : controllerSpoList) {
					listother.add(Constant.FILE_UPLOAD_PATH+File.separator +fwFiles.getUrl());
				}
			}
		}
		// 纳税人、扣缴义务人涉税保密信息查询申请表
		List<FwFiles> TaxRelatedList = fwFilesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.TAX_RELATED_CONFIDENTIAL_QUERY);
		if (TaxRelatedList.size()>0) {
			for (FwFiles fwFiles : TaxRelatedList) {
				listother.add(Constant.FILE_UPLOAD_PATH+File.separator +fwFiles.getUrl());
			}
		}
		// 企业纳税数据查询授权书
		List<FwFiles> EnterpreisePayList = fwFilesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.ENTERPRISE_PAY_TAXES_THROUGH_THE_DATA_QUERY_AUTHORIZATION);
		if (EnterpreisePayList.size()>0) {
			for (FwFiles fwFiles : EnterpreisePayList) {
				listother.add(Constant.FILE_UPLOAD_PATH+File.separator +fwFiles.getUrl());
			}
		}
		// 判断贷款卡号是否填写，如果没有填写上传“营业执照、税务登记证、组织机构代码、”
		if (StringUtils.isEmpty(fwBusinessSxd.getLoanCardCode())) {
			// 营业执照
			List<FwFiles> BusinessLicenseList = fwFilesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.BUSINESS_LICENSE);
			if (BusinessLicenseList.size()>0) {
				for (FwFiles fwFiles : BusinessLicenseList) {
					listother.add(Constant.FILE_UPLOAD_PATH+File.separator +fwFiles.getUrl());
				}
			}
			// 税务登记证
			List<FwFiles> TaxRegisteationCertificateList = fwFilesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.TAX_REGISTRATION_CERTIFICATE);
			if (TaxRegisteationCertificateList.size()>0) {
				for (FwFiles fwFiles : TaxRegisteationCertificateList) {
					listother.add(Constant.FILE_UPLOAD_PATH+File.separator +fwFiles.getUrl());
				}
			}
			// 组织机构代码证
			List<FwFiles> organiztionCodeCerTificateList = fwFilesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.ORGANIZTION_CODE_CERTIFICATE);
			if (organiztionCodeCerTificateList.size()>0) {
				for (FwFiles fwFiles : organiztionCodeCerTificateList) {
					listother.add(Constant.FILE_UPLOAD_PATH+File.separator +fwFiles.getUrl());
				}
			}
		}
		
		
		// 申请人身份证正面 pdf 实际控制人配偶身份证正面；实际控制人配偶身份证背面；企业纳税数据查询授权书；纳税人、扣缴义务人涉税保密信息查询申请表； 营业执照 ； 税务登记证  ； 组织机构代码证
		if (listother.size()>0) {
			String pdfName = pdfName(listother, busId);
			Map<String, String> map1 = new HashMap<String, String>();
			String fileName = this.uploadFileBank(pdfName, busId);
			map1.put(fileName,"申请人身份证正面");
			listFile.add(map1);
			/*FwFiles fwFiles = listPerCredit.get(0);
			Map<String, String> map = new HashMap<String, String>();
			String fileName = this.uploadFileBank(fwFiles, busId);
			map.put(fileName, "申请人身份证正面");
			listFile.add(map);*/
			
		}
		
		/**
		 * 构造“申请人身份证反面 pdf”即实际控制人
		 * 
		 * 申请人身份证正面；
		 * 申请人身份证背面
		 */
		List<FwFiles> ControllerList = fwFilesService.findByBusIdAndBusTypeAndFileType(busId, BusinessType.SXD, FileType.CONTROLLER_IDENTITY_CARD);
		if (ControllerList.size()>0) {
			for (FwFiles fwFiles : ControllerList) {
				listother.add(Constant.FILE_UPLOAD_PATH+File.separator +fwFiles.getUrl());
			}
		}
		
		//申请人身份证 背面
		if (ControllerList.size()>0) {
			List<String> listf =  new ArrayList<String>();
			for (FwFiles fwFiles : listPerCredit) {
				listf.add(Constant.FILE_UPLOAD_PATH+File.separator +fwFiles.getUrl());
			}
			String pdfName = pdfName(listf, busId);
			Map<String, String> map2 = new HashMap<String, String>();
			String fileName = this.uploadFileBank(pdfName, busId);
			map2.put(fileName,"申请人身份证背面");
			listFile.add(map2);
			/*FwFiles fwFiles = listPerCredit.get(1);
			Map<String, String> map = new HashMap<String, String>();
			String fileName = this.uploadFileBank(fwFiles, busId);
			map.put(fileName, "申请人身份证背面");
			listFile.add(map);*/	
		}
		return "";
	}
	
	
	
	  public String pdfName(List<String> list,String busId){
		  PDFHandleTools pdfHandleTools = new PDFHandleTools();
		  logger.debug("list.toString()"+list.toString());
			
			String fileName = fwBusinessSxdService.createFilePathPDF(busId, BusinessType.SXD);
			 logger.debug("pdfName===="+fileName);
			try {
				pdfHandleTools.createPdfToLocal(list, fileName);
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
			return fileName;
	  }
	  
		public String uploadFileBank(String uploadFile,String busId){
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("file", uploadFile);
		//	map1.put("file", Constant.FILE_UPLOAD_PATH + File.separator + files.getUrl());
			map1.put("busid", busId);
			try {
				//加日志
				//filePathMonitor(busId,MapUtils.getString(map1, "file"));
				Map<String, Object> mapRes = bankTools.uploadFile(map1);
				mapRes = (Map<String, Object>) mapRes.get("opRep");
				if ("0".equals(MapUtils.getString(mapRes, "retCode"))) {
					String fileName = MapUtils.getString(mapRes, "fileName");
					return fileName;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return null;
			
		}
		
		// 测试
		@RequestMapping(value = "/complete/task", method = { GET, POST })
		@ResponseBody
		public String completeTask(@RequestParam Map<String, Object> map) {
			Map<String, Object> variables = new HashMap<String, Object>();
			String taskId = MapUtils.getString(map, "taskId");
			String nodes = MapUtils.getString(map, "nodes");
			// String condtion = MapUtils.getString(map,"condtion");

			if (nodes.equals("manualVerify")) {
				// 人工准入验证
				variables.put(nodes, "1");
			} else if (nodes.equals("person")) {
				// 额度申请
				// ${applyMoney<=(lastYearTax*2)}
				variables.put("applyMoney", "40");
				variables.put("lastYearTax", "15");
			} else if (nodes.equals("xaminationApproval")) {
				// 审批
				variables.put(nodes, "1");
			} else if (nodes.equals("role_task_assign")) {
				// 现场任务分配
				variables.put("userId", "admin");
			} else {
				variables.put(nodes, true);
			}
			try {
				taskService.complete(taskId, variables);
			} catch (Exception e) {
				/*
				 * logger.error("error on complete task {}, variables={}", new
				 * Object[]{taskId, variables, e}); return
				 * "redirect:/workflow/list";
				 */
			}

			return "";
		}

		// 测试
		/**
		 * 通过URL启动流程
		 * 
		 * @param searchForm
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/startProcess/{key}", method = { GET, POST })
		@ResponseBody
		public String startProcess(@PathVariable("key") String businessKey) {
			WorkFlow workFlow = activitiSxdService.startProcess(businessKey);
			return JsonUtils.toJson(RequestUtils.successResult(workFlow));
		}
		
		
		/**
		 * 通过URL结束流程实例
		 * @param searchForm
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/killProcess/{key}", method = { GET, POST })
		@ResponseBody
		public String killProcess(@PathVariable("key") String processInstId) {
			runtimeService.deleteProcessInstance(processInstId, "");
			return JsonUtils.toJson(RequestUtils.successResult(""));
		}
		//
		
		/**
		 * 跳转--新版工作流--所有的modal测试页面（luy）
		 * @return
		 */
		@RequestMapping(value="/toTestModal", method = {GET, POST})
		public String toTestModal() {
			return "taskModal/testModal";
		}
}
