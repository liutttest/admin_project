<%@page import="com.evan.finance.admin.utils.WorkFlowUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<dict:loadDictList var="saleStables" type="SALE_STABLE" /><!-- 销售稳定性 -->
<dict:loadDictList var="taxCredits" type="TAX_CREDIT" /><!-- 企业经营年限 -->
<dict:loadDictList var="engageLifes" type="ENGAGE_LIFE" /><!-- 企业纳税信誉 -->
<dict:loadDictList var="oweTaxs" type="OWE_TAX" /><!-- 企业欠税情况 -->
<dict:loadDictList var="taxBlackLists" type="TAX_BLACK_LIST" /><!-- 是否进入税务黑名单 -->
<dict:loadDictList var="crimes" type="CRIME" /><!-- 是否有过犯罪记录 -->

<!-- 额度申请-人工复核-点击“办理”按钮 -->
<form id="frm-quota-role-manual_review" class="form-horizontal" method="post">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="quota-title">人工复核</h4>
			</div>
			
			<!-- luy-额度申请 -->
			<div id="div-limit-file" class="modal-body">
			
				<h4 class="blue bigger">需要审核的资料如下</h4>
				
				<div class="div-bus-detail">
					<a id="findInfoForLimit" class="green flow-apply-detail-toOtherPage"  >
					<%-- <a id="findInfoForLimit" class="green flow-apply-detail" target="_blank" href="${contentPath }/workflow/list/getfwApplyBusDetail?busId=${workflow.businessKey}" > --%>
						点击查看业务详细资料 
					</a>
				</div>
				
				<div class="hr hr-18 dotted hr-double"></div>
			
				<div class="row">
					<div class="col-xs-12">
						<div>
							<table id="sample-table-2" class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th class="c-align col-sm-1">#</th>
										<th class="c-align col-sm-2">资料名称</th>
										<th class="c-align col-sm-2">操作</th>
									</tr>
								</thead>
							
								<tbody class="limit-file-for-nopass">
									<tr>
										<%-- <c:forEach items = "${accessFiles}" var = "item" varStatus="status">
											<td>${ status.index + 1}</td>
											<td>${ item.dictValue0}</td>
											<td>${ item.dictValue0}</td>
										</c:forEach> --%>
									</tr>
								
									<%-- <tr>
										<td>
											<a href="#">${fwBlacklist.comName}</a>
										</td>
										<td>${fwBlacklist.addTime}</td>
										<td>${fwBlacklist.removeTime}</td>
										<td>
										<c:if test="${fwBlacklist.isHistroy==1}">YES</c:if>
										<c:if test="${fwBlacklist.isHistroy!=1}">NO</c:if>
										</td>
										<td>${fwBlacklist.userName}</td>
									</tr> --%>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			
			
			<div id="div-quota-modal-body" class="modal-body">
				<div class="container-fluid">
				  
				  <div id="quota-select">
				  
				  	<h4 class="blue bigger">行业</h4>
					<div class="hr hr-18 dotted hr-double"></div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 行业风险</label>
						
						<div class="col-sm-8 form-control-static" style="text-align:left;">
							<c:choose>
								<c:when test="${empty fwBusinessSxd.industryId}">
									无
								</c:when>
								<c:otherwise>
									${sysDicIndustry.industryName}
									(
										<c:if test="${sysDicIndustry.industryType==1}">
											支持类
										</c:if>
										<c:if test="${sysDicIndustry.industryType==2}">
											维持类
										</c:if>
										<c:if test="${sysDicIndustry.industryType==3}">
											审慎类
										</c:if>
										<c:if test="${sysDicIndustry.industryType==4}">
											退出类
										</c:if>
									)
									[${empty faCenterDataBank.tradeRisk ? "0":faCenterDataBank.tradeRisk}分]
								</c:otherwise>
							</c:choose>
						
							
							<!-- <select name="quota_001"> 
								<option value="6" selected="selected">支持类</option>
								<option value="3">维持类</option>
								<option value="0">审慎类或退出类</option>
							</select> -->
						</div>
					</div>
					
					<h4 class="blue bigger">收入情况</h4>
					<div class="hr hr-18 dotted hr-double"></div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 销售稳定性 </label>

						<div class="col-sm-8 form-control-static" style="text-align:left;">
							<dict:lookupDictValue type="SALE_STABLE" key="${faRisk.saleStable}" />
							[${empty faCenterDataBank.saleStable ? "0":faCenterDataBank.saleStable}分]
							<!-- <select name="quota_002" style="width:60%"> 
								<option value="4" selected="selected">企业连续2年未出现0申报，且申报数额趋向正向增长</option>
								<option value="2">企业连续2年未出现连续三个月0申报，且总0申报次数少于4次且申报数额趋于平稳</option>
								<option value="0">企业最近连续1年无税申报</option>
							</select> -->
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 纳税规模 </label>

						<div class="col-sm-8 form-control-static" style="text-align:left;">
							${fwBusinessSxd.lastyTaxmoney}
							[${empty faCenterDataBank.taxScale ? "0":faCenterDataBank.taxScale}分]
							<!-- <select name="quota_003"> 
								<option value="8" selected="selected">年纳税额[100,+∞）</option>
								<option value="6">年纳税额[50,100）</option>
								<option value="4">年纳税额[30,50）</option>
								<option value="2">年纳税额[0,30）</option>
							</select> -->
						</div>
					</div>
					
					<h4 class="blue bigger">企业运营情况</h4>
					<div class="hr hr-18 dotted hr-double"></div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业经营年限 </label>

						<div class="col-sm-8 form-control-static" style="text-align:left;">
							${operationYearInteger}
							[${empty faCenterDataBank.engageLife ? "0":faCenterDataBank.engageLife}分]
							<!-- <select name="quota_004"> 
								<option value="8" slected="selected">企业持续经营超过8年</option>
								<option value="5">企业持续经营超过5年</option>
								<option value="2">企业持续经营超过2年</option>
								<option value="0">企业持续经营未满2年</option>
							</select> -->
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业纳税信誉 </label>

						<div class="col-sm-8 form-control-static" style="text-align:left;">
							<dict:lookupDictValue type="TAX_CREDIT" key="${faRisk.taxCredit}" />
							[${empty faCenterDataBank.tax_credit ? "0":faCenterDataBank.tax_credit}分]
							<!-- <select name="quota_006"> 
								<option value="5" selected="selected">纳税信用等级A类企业</option>
								<option value="3">纳税信用等级B类企业</option>
								<option value="0">纳税信用等级C/D类企业</option>
							</select> -->
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业拥有员工数 </label>

						<div class="col-sm-8 form-control-static" style="text-align:left;">
							${faRisk.workers}
							[${empty faCenterDataBank.workers ? "0":faCenterDataBank.workers}分]
							<!-- <select name="quota_007"> 
								<option value="5" selected="selected">企业拥有在职员工 >= 100人</option>
								<option value="3">企业拥有在职员工 >= 50人</option>
								<option value="2">企业拥有在职员工 >= 20人</option>
								<option value="1">企业拥有在职员工 >= 10人</option>
							</select> -->
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业欠税情况 </label>

						<div class="col-sm-8 form-control-static" style="text-align:left;">
							<dict:lookupDictValue type="OWE_TAX" key="${faRisk.oweTax}" />
							[${empty faCenterDataBank.owe_tax ? "0":faCenterDataBank.owe_tax}分]
							<!-- <select name="quota_008"> 
								<option value="3" selected="selected">没有欠税</option>
								<option value="0">有欠税尚未缴清</option>
							</select> -->
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 经营场所 </label>

						<div class="col-sm-8 form-control-static" style="text-align:left;">
							<dict:lookupDictValue type="PREMISES" key="${fwBusinessSxd.premises}" />
							[${empty faCenterDataBank.engagePalce ? "0":faCenterDataBank.engagePalce}分]
							<!-- <select name="quota_005" style="background-color: gainsboro;"> 
								<option value="4" selected="selected">自有场所经营</option>
								<option value="2">固定场所租赁满3年</option>
								<option value="0">固定场所租赁满1年</option>
							</select> -->
						</div>
					</div>
					
					<h4 class="blue bigger">实际控制人基本情况</h4>
					<div class="hr hr-18 dotted hr-double"></div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 户籍情况 </label>

						<div class="col-sm-8 form-control-static" style="text-align:left;">
							<dict:lookupDictValue type="DOMICILE_PLACE" key="${controllerPer.domicilePlace}" />
							[${empty faCenterDataBank.census ? "0":faCenterDataBank.census}分]
							<!-- <select name="quota_009" style="width:60%;background-color: gainsboro;"> 
								<option value="3" selected="selected">本地户籍</option>
								<option value="2">非本地户籍但拥有本地居住证，且在当地近两年有连续个税缴纳记录</option>
								<option value="1">非本地户籍但有本地居住证</option>
								<option value="0">非本地户籍且无本地居住证</option>
							</select> -->
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 学历 </label>

						<div class="col-sm-8 form-control-static" style="text-align:left;">
							<dict:lookupDictValue type="EDUCATION" key="${controllerPer.education}" />
							[${empty faCenterDataBank.education ? "0":faCenterDataBank.education}分]
							<!-- <select name="quota_010" style="background-color: gainsboro;"> 
								<option value="2">本科及以上</option>
								<option value="0">其他</option>
							</select> -->
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 年龄 </label>

						<div class="col-sm-8 form-control-static" style="text-align:left;">
							${empty controllerPer.age ? "无" : controllerPer.age}
							[${empty faCenterDataBank.age ? "0":faCenterDataBank.age}分]
							<!-- <select name="quota_011" style="background-color: gainsboro;"> 
								<option value="2" selected="selected">(35,60)</option>
								<option value="1">[18,35]</option>
								<option value="0">[0,18) 或 [60,+∞）</option>
							</select> -->
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 婚姻状况 </label>

						<div class="col-sm-8 form-control-static" style="text-align:left;">
							<dict:lookupDictValue type="MARITAL_STATE" key="${controllerPer.maritalState}" />
							[${empty faCenterDataBank.marriage ? "0":faCenterDataBank.marriage}分]
							<!-- <select name="quota_012" style="background-color: gainsboro;"> 
								<option value="2" selected="selected">已婚</option>
								<option value="0">离异或未婚</option>
							</select> -->
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 家庭净资产情况 </label>

						<div class="col-sm-8 form-control-static" style="text-align:left;">
							<dict:lookupDictValue type="HOUSEHOLD_ASSETS" key="${controllerPer.householdAssets}" />
							[${empty faCenterDataBank.family_netassets ? "0":faCenterDataBank.family_netassets}分]
							<!-- <select name="quota_015" style="background-color: gainsboro;"> 
								<option value="5" selected="selected">500万及以上</option>
								<option value="3">300万-500万</option>
								<option value="1">净资产300万以下(房产)</option>
								<option value="0">无房产</option>
							</select> -->
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 是否进入一路贷及税务黑名单 </label>

						<div class="col-sm-8 form-control-static" style="text-align:left;">
							${faCenterDataBank.blackList==0 ? "是" : "否"}
							[${empty faCenterDataBank.blackList ? "0":faCenterDataBank.blackList}分]
							<!-- <select name="quota_013" style="background-color: gainsboro;"> 
								<option value="2" selected="selected">否</option>
								<option value="0">是</option>
							</select> -->
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 是否有过犯罪记录 </label>
						<div class="col-sm-8 form-control-static" style="text-align:left;">
							<dict:lookupDictValue type="CRIME" key="${faRisk.crime}" />
							[${empty faCenterDataBank.crime ? "0":faCenterDataBank.crime}分]
							<!-- <select name="quota_014" style="background-color: gainsboro;"> 
								<option value="2" selected="selected">否</option>
								<option value="0">是</option>
							</select> -->
						</div>
					</div>
					
					
					<div class="hr hr-18 dotted hr-double"></div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业纳税信用等级证明 </label>
						<div class="col-sm-8" style="text-align:left;">
							<label class="control-label"><a id="qinsxydjzm-a" href="javascript:void(0)">上传图片</a></label>
							<input type="hidden" id="upload-busId">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<label class="control-label"><a id="qinsxydjzm-a-ck" href="javascript:void(0)">查看图片</a></label>
							<input type="hidden" id="qinsxydjzm_count" value="" />
						</div>
					</div>
					
					</div>
					
                    <div class="col-sm-12 form-group">
						<textarea class="col-sm-12" rows="5"  placeholder="备注" id="comment" name="comment"  maxlength="150"></textarea>
					</div>
					
                    
				</div>
			</div>

			<!-- 额度申请-人工复核-处理按钮 -->
			<div id="btn-quota" class="modal-footer">
				<button class="btn btn-sm access-submit" data-dismiss="modal" type="button"><i class="ace-icon fa fa-times"></i>关闭</button>
				<button id="quota-role-manual_review-rejected" class="btn btn-sm quota-submit" type="button" value="back"><i class="ace-icon fa fa-times"></i>驳回 </button>
				<!-- <button id="quota-role-manual_review-nopass" class="btn btn-sm quota-submit " type="button" value="false"><i class="ace-icon fa fa-times"></i>不通过</button> -->
				<button id="quota-role-manual_review-handling" class="btn btn-sm btn-primary" type="button" value="true"><i class="ace-icon fa fa-check"></i> 办理</button>
			</div>
			
			<!-- 额度申请驳回通知S -->
			<div id="modal-body-quota-apply-rejected-notification" class="modal-body" style="display:none;">
				<div class="row">
					<div class="col-xs-12">
						<div id="quota-apply-rejected-notification"></div>
					</div>
				</div>
			</div>
            <!-- 额度申请通知E -->
                
			<div id="btn-notification" class="modal-footer" style="display:none;">
				<button class="btn btn-sm access-submit" data-dismiss="modal" type="button"><i class="ace-icon fa fa-times"></i>关闭</button>
			    <button id="quota-rejected-notification" class="btn btn-sm btn-primary access-submit" type="button" value="true"><i class="ace-icon fa fa-check"></i>办理</button>
			</div>
			
			
			
			
		</div>
	</div>
	
	<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_quota_apply%>_condition" name="<%=WorkFlowUtils.process_definition_key_quota_apply%>_condition">
	<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_quota_apply%>_node" name="<%=WorkFlowUtils.process_definition_key_quota_apply%>_node">
	<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_quota_apply%>_businessKey" name="<%=WorkFlowUtils.process_definition_key_quota_apply%>_businessKey">
	<input type="hidden" id="hide_quota_005" name="hide_quota_005">
	<input type="hidden" id="hide_quota_009" name="hide_quota_009">
	<input type="hidden" id="hide_quota_010" name="hide_quota_010">
	<input type="hidden" id="hide_quota_011" name="hide_quota_011">
	<input type="hidden" id="hide_quota_012" name="hide_quota_012">
	<input type="hidden" id="hide_quota_013" name="hide_quota_013">
	<input type="hidden" id="hide_quota_014" name="hide_quota_014">
	<input type="hidden" id="hide_quota_015" name="hide_quota_015">
	
	
	<input type="hidden" name="businessId" id ="businessId" value="${businessId}"/> <!-- 业务ID -->
    <input type="hidden" name="taskId" id ="taskId" value="${taskId}"/> <!-- 任务编号 -->
    <input type="hidden" name="taskDefKey" id ="taskDefKey" value="${taskDefKey}"/> <!-- 任务定义Key -->
    <input type="hidden" name="procInsId" id ="procInsId" value="${procInsId}"/> <!-- 流程实例id -->
    <input type="hidden" name="sendType" id ="sendType" value="${sendType}"/> <!-- 准入驳回通知发送方式（0：手动；1：自动） -->
    <input type="hidden" name="loanNew_condition" id ="loanNew_condition" /> <!-- 点击按钮，办理结果（1：通过；2：不通过；3：驳回；） -->
	
</form>


<!-- 查看图片 -->
<div class="modal fade" id="show-modal" role="dialog" aria-labelledby="gridSystemModalLabel">
    <div class="modal-dialog" role="document" style="margin-top: 0;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close js-showImg-close" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" >图片显示</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <div>
                            <ul id="file-img-show" class="ace-thumbnails clearfix">
                                <!-- #section:pages/gallery -->
                                <!-- <li>
                                    <button type="button" id="cboxClose"  data-toggle="tooltip" data-placement="left" title="删除图片？">×</button>
                                    <a href="../img/gallery/image-1.jpg" data-rel="colorbox" class="cboxElement">
                                        <img width="150" height="150" alt="150x150" src="../img/gallery/thumb-1.jpg">
                                    </a>
                                </li>

                                <li>
                                    <button type="button" id="cboxClose" data-toggle="tooltip" data-placement="left" title="删除图片？">×</button>
                                    <a href="../img/gallery/image-2.jpg" data-rel="colorbox" class="cboxElement">
                                        <img width="150" height="150" alt="150x150" src="../img/gallery/thumb-2.jpg">
                                    </a>
                                </li> -->

                            </ul>
                        </div><!-- PAGE CONTENT ENDS -->
                    </div><!-- /.col -->
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default js-showImg-close">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>



<link rel="stylesheet" type="text/css" href="${contentPath}/plugins/editor/css/wangEditor-1.3.11.css">
<script type="text/javascript" src='${contentPath}/plugins/editor/js/wangEditor-1.3.11.js'></script>


<script src="${contentPath }/js/task_modal/modal_common.js"></script> <!-- modal的公共方法 -->
<script src="${contentPath }/js/task_modal/common_notification.js"></script> <!-- 关于通知的公共方法 -->
<script src="${contentPath }/js/task_modal/quota_role_manual_review.js"></script>

