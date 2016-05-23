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

<!-- 准入验证-人工准入验证-点击“办理”按钮 -->
<form class="form-horizontal" id="frm_access_role_manual_verify" method="post">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="access-title">人工准入验证</h4>
			</div>
			
			<!-- luy- -->
			<input type="hidden" id="businessIdForFlag" >
			<div id="div-access-file" class="modal-body">
				<h4 class="blue bigger">需要审核的资料如下</h4>
				<div class="div-bus-detail">
					<a id="findInfoFor" class="green flow-apply-detail-toOtherPage"  >
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
							
								<tbody class="access-file-for-nopass">
									<tr>
										<%-- <c:forEach items = "${accessFiles}" var = "item" varStatus="status">
											<td>${ status.index + 1}</td>
											<td>${ item.dictValue0}</td>
											<td>${ item.dictValue0}</td>
										</c:forEach> --%>
									</tr>
								
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			

			<div id="div-access-info" class="modal-body">
				<div class="container-fluid">
			    
			    	<div id="access-reason-check"> 
			    	
			    		<h4 class="blue bigger">准入验证信息</h4>
						<div class="hr hr-18 dotted hr-double"></div>
			    	
						<!-- 						
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 支持类型 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="access_001"> 
									<option value="01" selected="selected">其他类</option>
									<option value="02">审慎类</option>
									<option value="03">退出类</option>
								</select>
							</div>
						</div>
						-->
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 销售稳定性 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="saleStable" style="width:200px;">
									<c:forEach var="item" items="${saleStables}">
										<c:choose>
											<c:when test="${faRisk.saleStable == item.dictKey}">
												<option value="${item.dictKey}" selected="selected">${item.dictValue0}</option>
											</c:when>
											<c:otherwise>
												<option value="${item.dictKey}">${item.dictValue0}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业纳税信誉 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="taxCredit" id="taxCredit"> 
									<c:forEach var="item" items="${taxCredits}">
										<c:choose>
											<c:when test="${faRisk.taxCredit == item.dictKey}">
												<option value="${item.dictKey}" selected="selected">${item.dictValue0}</option>
											</c:when>
											<c:otherwise>
												<option value="${item.dictKey}">${item.dictValue0}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业纳税年限 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="engageLife" id="engageLife"> 
									<c:forEach var="item" items="${engageLifes}">
										<c:choose>
											<c:when test="${faRisk.engageLife == item.dictKey}">
												<option value="${item.dictKey}" selected="selected">${item.dictValue0}</option>
											</c:when>
											<c:otherwise>
												<option value="${item.dictKey}">${item.dictValue0}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业欠税情况 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="oweTax" id="oweTax"> 
									<c:forEach var="item" items="${oweTaxs}">
										<c:choose>
											<c:when test="${faRisk.oweTax == item.dictKey}">
												<option value="${item.dictKey}" selected="selected">${item.dictValue0}</option>
											</c:when>
											<c:otherwise>
												<option value="${item.dictKey}">${item.dictValue0}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 是否进入税务黑名单 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="taxBlackList"> 
									<c:forEach var="item" items="${taxBlackLists}">
										<c:choose>
											<c:when test="${faRisk.taxBlackList == item.dictKey}">
												<option value="${item.dictKey}" selected="selected">${item.dictValue0}</option>
											</c:when>
											<c:otherwise>
												<option value="${item.dictKey}">${item.dictValue0}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</div>
						</div>
						<!-- 
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 个人征信记录 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="access_009"> 
									<option value="01" selected="selected">实际控制人或其配偶过去2年内逾期记录累计3次以下</option>
									<option value="02">实际控制人或其配偶过去2年内逾期记录累计3次以上</option>
								</select>
							</div>
						</div>
						-->
					<!--  
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;white-space:nowrap;"> 是否进入税务及一路贷黑名单 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="access_010"> 
								<option value="2" selected="selected">否</option>
								<option value="0">是</option>
							</select>
						</div>
					</div>
					-->
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 是否有过犯罪记录 </label>
						<div class="col-sm-8" style="text-align:left;">
							<select name="crime"> 
								<c:forEach var="item" items="${crimes}">
									<c:choose>
										<c:when test="${faRisk.crime == item.dictKey}">
											<option value="${item.dictKey}" selected="selected">${item.dictValue0}</option>
										</c:when>
										<c:otherwise>
											<option value="${item.dictKey}">${item.dictValue0}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</div>
					</div>
					
					<h4 class="blue bigger" style="margin-top: 30px;">打分资料收集</h4>
					<div class="hr hr-18 dotted hr-double"></div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业拥有员工数 </label>
						<div class="col-sm-8" style="text-align:left;">
							<input name="workers" value="${faRisk.workers}">
						</div>
					</div>
						
						
						<!-- <h4 class="blue bigger" style="margin-top: 30px;">打分资料收集</h4>
						<div class="hr hr-18 dotted hr-double"></div>
						
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 经营场所 </label>
	
							<div class="col-sm-8" style="text-align:left;">
								<select name="quota_005"> 
									<option value="4" selected="selected">自有场所经营</option>
									<option value="2">固定场所租赁满3年</option>
									<option value="0">固定场所租赁满1年</option>
								</select>
							</div>
						</div>
						<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 户籍情况 </label>

						<div class="col-sm-8" style="text-align:left;width:60%">
							<select name="quota_009" style="width:60%"> 
								<option value="3" selected="selected">本地户籍</option>
								<option value="2">非本地户籍但拥有本地居住证，且在当地近两年有连续个税缴纳记录</option>
								<option value="1">非本地户籍但有本地居住证</option>
								<option value="0">非本地户籍且无本地居住证</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="2text-align:right;"> 学历 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_010"> 
								<option value="3" selected="selected">研究生及以上学历</option>
								<option value="2">本科及以上</option>
								<option value="1">大专</option>
								<option value="0">其他</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 年龄 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_011"> 
								<option value="2" selected="selected">(35,60)</option>
								<option value="1">[18,35]</option>
								<option value="0">[0,18) 或 [60,+∞)</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 婚姻状况 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_012"> 
								<option value="2" selected="selected">已婚</option>
								<option value="0">离异或未婚</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;white-space:nowrap;"> 是否进入税务及一路贷黑名单 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_013"> 
								<option value="2" selected="selected">否</option>
								<option value="0">是</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 是否有过犯罪记录 </label>
						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_014"> 
								<option value="2" selected="selected">否</option>
								<option value="0">是</option>
							</select>
						</div>
					</div> -->
				</div>
						
						
				<!-- 是否办理并发送 -->
				<input type="hidden" id="isSend" name="isSend" value="true">
				
				<div class="col-sm-12 form-group">
					<textarea rows="5" cols="70" placeholder="备注" id="comment" name="comment" maxlength="150"></textarea>
				</div>
			</div>
		</div>
			
			
			
			
			<!-- 准入验证-人工准入验证-处理按钮 -->
			<div id="btn-access" class="modal-footer">
				<button class="btn btn-sm access-submit" data-dismiss="modal" type="button"><i class="ace-icon fa fa-times"></i>关闭</button>
				<button id="access-role-manual-verify-rejected" class="btn btn-sm access-submit" type="button" value="back"><i class="ace-icon fa fa-times"></i>驳回</button>
				<button id="access-role-manual-verify-nopass" class="btn btn-sm access-submit" type="button" value="false"><i class="ace-icon fa fa-times"></i>不通过</button>
				<!-- <button class="btn btn-sm btn-primary access-submit access-send" type="submit" value="true" style="display:none;"><i class="ace-icon fa fa-check"></i> 办理并发送</button> -->
			    <button id="access-role-manual-verify-handling" class="btn btn-sm btn-primary access-submit" type="button" value="true"><i class="ace-icon fa fa-check"></i> 办理</button>
			</div>
			
			
			
			<!-- 准入驳回通知S -->
			<div id="modal-body-access-rejected-notification" class="modal-body" style="display:none;">
				<div class="row">
					<div class="col-xs-12">
						<div id="div-access-rejected-notification"></div>
					</div>
				</div>
			</div>
            <!-- 准入驳回通知E -->
                
			<div id="btn-notification" class="modal-footer" style="display:none;">
				<button class="btn btn-sm access-submit" data-dismiss="modal" type="button"><i class="ace-icon fa fa-times"></i>关闭</button>
			    <button id="access-rejected-notification" class="btn btn-sm btn-primary access-submit" type="button" value="true"><i class="ace-icon fa fa-check"></i>办理</button>
			</div>
			
			
		</div>
	</div>
	
	
	<input type="hidden" name="businessId" id ="businessId" value="${businessId}"/> <!-- 业务ID -->
	<input type="hidden" name="businessType" id ="businessType" value="01"/> <!-- 业务类型 -->
    <input type="hidden" name="taskId" id ="taskId" value="${taskId}"/> <!-- 任务编号 -->
    <input type="hidden" name="taskDefKey" id ="taskDefKey" value="${taskDefKey}"/> <!-- 任务定义Key -->
    <input type="hidden" name="procInsId" id ="procInsId" value="${procInsId}"/> <!-- 流程实例id -->
    <input type="hidden" name="sendType" id ="sendType" value="${sendType}"/> <!-- 准入驳回通知发送方式（0：手动；1：自动） -->
    <input type="hidden" name="loanNew_condition" id ="loanNew_condition" /> <!-- 点击按钮，办理结果（1：通过；2：不通过；3：驳回；） -->
    <input type="hidden" name="riskId" id ="riskId" value="${faRisk.riskId}"/> <!-- 风控数据信息id -->
</form>

<link rel="stylesheet" type="text/css" href="${contentPath}/plugins/editor/css/wangEditor-1.3.11.css">
<script type="text/javascript" src='${contentPath}/plugins/editor/js/wangEditor-1.3.11.js'></script>

<script src="${contentPath }/js/task_modal/modal_common.js"></script> <!-- modal的公共方法 -->
<script src="${contentPath }/js/task_modal/common_notification.js"></script> <!-- 关于通知的公共方法 -->
<script src="${contentPath }/js/task_modal/access_role_manual_verify.js"></script>