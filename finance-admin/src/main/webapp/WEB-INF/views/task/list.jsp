<%@page import="com.evan.finance.admin.utils.WorkFlowUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<dict:loadDictList var="reasons" type="ACCCESS_FAILED_REASON"/>
<dict:loadDictList var="filetypes" type="FILE_TYPE" toJson="true"/>
<dict:loadDictList var="operationStates" type="OPERATION_STATE" toJson="true"/><!-- 操作记录状态 -->

<dict:loadDictList var="accessFiles" type="FILE_TYPE" parentKey="01" toJson="true"/><!-- 准入验证-上传的文件 -->
<dict:loadDictList var="limitFiles" type="FILE_TYPE" parentKey="02" toJson="true"/><!-- 额度申请-上传的文件 -->

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ taglib prefix="security" uri="http://www.evan.jaron.com/tags/security"%>
<security:securityUser var="user" />

<div class="main-content">
	<!-- #section:basics/content.breadcrumbs -->
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
		</script>

		<ul class="breadcrumb">
			<li>
				<i class="ace-icon fa fa-check-square-o home-icon"></i>
				待办任务
			</li>
		</ul><!-- /.breadcrumb -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
	<div class="row">
	<div class="col-xs-12">
	<div>
		<table id="table2" class="table table-striped table-bordered table-hover">
			<thead>
			<tr>
				<th class="c-align">企业名称</th>
				<th class="c-align">当前状态</th>
				<th class="c-align">流程跟踪</th>
				<th class="c-align">
				<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>任务时间
				</th>
				<th class="c-align">操作</th>
			</tr>
			</thead>

			<tbody>
			<c:forEach items="${workflows }" var="workflow">
				<c:set var="processInstance" value="${workflow.processInstance }" />
				<c:set var="task" value="${workflow.task }" />
				<c:set var="processDefinition" value="${workflow.processDefinition }" />
				<tr>
					<td class="c-align">${workflow.comName }</td>
					<td class="c-align">${processDefinition.name}：${task.name }</td>
					<td class="c-align"><a  href='#' class="process-definition" data-data="申请公司:${workflow.comName }; 当前状态:${task.name }" data-urlid="${workflow.processInstanceId }">查看流程</a></td>
					<td class="c-align"><fmt:formatDate value="${task.createTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>
						<c:if test="${empty task.assignee }">
							<a class="claim" href="#"  data-url="${contentPath}/workflow/${workflow.flowType}/task/claim/${task.id}">签收</a>
						</c:if>
						<c:if test="${not empty task.assignee }">
							<!-- 非签收状态则有 转办功能-->
							<a class="flow-turn-todo" data-taskid="${task.id}" data-node="${task.taskDefinitionKey}" data-buskey="${workflow.businessKey}"  data-flowtype="${workflow.flowType}"href="#">转办</a>
						    <c:choose>
							    <c:when test='${task.taskDefinitionKey==role_mranual_processing || task.taskDefinitionKey==role_mranual_credit_limit || task.taskDefinitionKey==role_mranual_account || task.taskDefinitionKey==role_mranual_loan}'>
							    	<!--人工处理银行验证请求节点-->
							    	<c:if test="${task.taskDefinitionKey!='role_mranual_loan'}">
							    	<a class="send-apply-to-bank" data-taskid="${task.id}" data-node="${task.taskDefinitionKey}" data-buskey="${workflow.businessKey}"  data-flowtype="${workflow.flowType}"href="#">发送请求</a>
							    	</c:if>
							    	<c:if test="${task.taskDefinitionKey=='role_mranual_loan'}">
							    	<a class="send-apply-to-bank" data-taskid="${task.id}" data-node="${task.taskDefinitionKey}" data-buskey="${workflow.loanId}"  data-flowtype="${workflow.flowType}"href="#">发送请求</a>
							    	</c:if>
							    </c:when>
							    
							    <c:when test='${task.taskDefinitionKey==role_task_assign}'>
							   		<!--现场任务分派节点-->
							   		<a class="flow-bootbox-options" data-taskid="${task.id}" data-node="${task.taskDefinitionKey}" data-buskey="${workflow.businessKey}"  data-flowtype="${workflow.flowType}"href="#">分派任务</a>
							    </c:when>
							    
							    <c:when test='${task.taskDefinitionKey==role_data_backfill}'>
							    	<!--数据回填节点-->
							    	<a class="flow-bootbox-options" data-taskid="${task.id}" data-node="${task.taskDefinitionKey}" data-buskey="${workflow.businessKey}"  data-flowtype="${workflow.flowType}"href="#">信息回填</a>
							    	<a class="print-data-detail" data-taskid="${task.id}" data-node="${task.taskDefinitionKey}" data-buskey="${workflow.businessKey}"  data-flowtype="${workflow.flowType}"href="#">打印清单</a>
							    </c:when>
							    <c:otherwise>
							      <!--其他的则是办理功能-->
							      <a class="flow-bootbox-options" data-taskid="${task.id}" data-node="${task.taskDefinitionKey}" data-buskey="${workflow.businessKey}" data-busComName="${workflow.comName}"   data-flowtype="${workflow.flowType}"href="#">办理</a>
							    </c:otherwise>
						    </c:choose>
						</c:if>
						<c:if test="${workflow.flowType!= 'quickloan'}">
							<a class="green flow-apply-detail" target="_blank" data-href="${contentPath }/workflow/list/getfwApplyBusDetail?busId=${workflow.businessKey}" >
								查看资料  <!-- <i class="ace-icon fa fa-university bigger-130"></i> -->
							</a>
						</c:if>
						<c:if test="${workflow.flowType== 'quickloan'}">
							<a class="green flow-apply-detail" target="_blank" data-href="${contentPath }/workflow/list/getQuickloanDetail?loanId=${workflow.businessKey}" >
								查看资料  <!-- <i class="ace-icon fa fa-university bigger-130"></i> -->
							</a>
						</c:if>
						
						<c:if test="${workflow.flowType != 'loan' && workflow.flowType != 'quickloan'}">
							<a data-busId="${workflow.businessKey}" class="green js-find-communicate-log" href="#" >
								操作记录
							</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		</div>
		</div>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->

<div id="access-modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="access-title">处理准入验证</h4>
			</div>
			
			<!-- luy- -->
			<input type="hidden" id="businessIdForFlag" >
			<div id="div-access-file" class="modal-body" style="display: none;">
			
				<h4 class="blue bigger">需要审核的资料如下</h4>
				
				<div class="div-bus-detail">
					<%-- <a id="findInfoFor" class="green flow-apply-detail" target="_blank" href="${contentPath }/workflow/list/getfwApplyBusDetail?busId=${workflow.businessKey}" > --%>
					<a id="findInfoFor" class="green flow-apply-detail"  >
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
			
			

			<div class="modal-body">
				<div class="container-fluid">
			    <form class="form-horizontal" id="access-form" method="post">
			    	<div id="access-reason-check" style="display:none;"> 
			    	
			    		<h4 class="blue bigger">准入验证信息</h4>
						<div class="hr hr-18 dotted hr-double"></div>
			    	
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
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 销售稳定性 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="access_002"> 
									<option value="01" selected="selected">企业最近连续1年有税申报</option>
									<option value="02">企业最近连续1年无税申报</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业经营年限 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="access_003"> 
									<option value="01" selected="selected">企业持续纳税1年以上</option>
									<option value="02">企业持续纳税未满1年</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业纳税信誉 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="access_004"> 
									<option value="01" selected="selected">纳税信用等级A类企业</option>
									<option value="02">纳税信用等级B类企业</option>
									<option value="03">纳税信用等级C类企业</option>
									<option value="04">纳税信用等级D类企业</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业欠税情况 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="access_005"> 
									<option value="01" selected="selected">没有欠税</option>
									<option value="02">有欠税尚未缴清</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 是否进入企业黑名单 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="access_006"> 
									<option value="01" selected="selected">否</option>
									<option value="02">是</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 户籍情况 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="access_007"> 
									<option value="01" selected="selected">本地户籍或者有本地居住证</option>
									<option value="02">非本地户籍且无本地居住证</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 年龄 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="access_008"> 
									<option value="01" selected="selected">[18,60)</option>
									<option value="02">[0,18)</option>
									<option value="03">[60,+&infin;)</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 个人征信记录 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="access_009"> 
									<option value="01" selected="selected">实际控制人或其配偶过去2年内逾期记录累计3次以下</option>
									<option value="02">实际控制人或其配偶过去2年内逾期记录累计3次以上</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right; white-space:nowrap;"> 是否进入税务及一路贷黑名单 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="access_010"> 
									<option value="01" selected="selected">否</option>
									<option value="02">是</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 是否有过犯罪记录 </label>

							<div class="col-sm-8" style="text-align:left;">
								<select name="access_011"> 
									<option value="01" selected="selected">否</option>
									<option value="02">是</option>
								</select>
							</div>
						</div>
						<h4 class="blue bigger" style="margin-top: 30px;">打分资料收集</h4>
						<div class="hr hr-18 dotted hr-double"></div>
						
						<!-- <div class="form-group">
							<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 经营场所 </label>
	
							<div class="col-sm-8" style="text-align:left;">
								<select name="quota_005"> 
									<option value="4" selected="selected">自有场所经营</option>
									<option value="2">固定场所租赁满3年</option>
									<option value="0">固定场所租赁满1年</option>
								</select>
							</div>
						</div> -->
						<!-- <div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 户籍情况 </label>

						<div class="col-sm-8" style="text-align:left;width:60%">
							<select name="quota_009" style="width:60%"> 
								<option value="3" selected="selected">本地户籍</option>
								<option value="2">非本地户籍但拥有本地居住证，且在当地近两年有连续个税缴纳记录</option>
								<option value="1">非本地户籍但有本地居住证</option>
								<option value="0">非本地户籍且无本地居住证</option>
							</select>
						</div>
					</div> -->
					<!-- <div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="2text-align:right;"> 学历 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_010"> 
								<option value="3" selected="selected">研究生及以上学历</option>
								<option value="2">本科及以上</option>
								<option value="1">大专</option>
								<option value="0">其他</option>
							</select>
						</div>
					</div> -->
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 年龄 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_011"> 
								<option value="2" selected="selected">(35,60)</option>
								<option value="1">[18,35]</option>
								<option value="0">[0,18) 或 [60,+∞）</option>
							</select>
						</div>
					</div>
					<!-- <div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 婚姻状况 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_012"> 
								<option value="2" selected="selected">已婚</option>
								<option value="0">离异或未婚</option>
							</select>
						</div>
					</div> -->
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
					</div>
					</div>
					
					<div id="customer-service-notify" style="display:none;">
						<!-- 客服通知--lixj -->
							<!-- <div>
						      <div class="col-sm-12 form-group">
                                    <div class="checkbox">
                                        <label>
                                            <input type="checkbox" id="emailCheck" name="emailCheck" checked="checked" disabled="disabled"> 邮件
                                        </label>
                                    </div>
                                </div>

                                <div class="col-sm-12 form-group">
                                    <span class="color-green">选择模板：</span>
                                    
                                    <select name="emailTemplate" id="emailTemplate">
                                        <option value="">自定义</option>
                                    </select>
                                    
                                    
                                </div>
                                <div class="col-sm-12 form-group">
                                    <span class="pull-left margin-t">邮件标题：</span>
                                    <input type="text" id="emailTitle" name="emailTitle" class="col-sm-6">
                                </div>
                                <div class="col-sm-12 form-group">
                                    <textarea id="emailContent" name="emailContent">

                                    </textarea>
                                </div>
                            </div>
                            <div>
                                <div class="col-sm-12 form-group">
                                    <div class="checkbox">
                                        <label>
                                            <input type="checkbox" id="smsCheck" name="smsCheck"> 短信
                                        </label>
                                    </div>
                                </div>

                                <div class="col-sm-12 form-group">
                                    <span class="color-green">选择模板：</span>
                                    
                                    <select id="smsTemplate" name="smsTemplate">
                                        <option value="">自定义</option>
                                    </select>
                                    
                                    
                                </div>
                                <div class="col-sm-12 form-group">
                                    <textarea class="col-sm-12" id="smsContent" name="smsContent">

                                    </textarea>
                                </div>
                            </div>
                            <div>
                                <div class="col-sm-12 form-group">
                                    <div class="checkbox">
                                        <label>
                                            <input type="checkbox"  id="messageCheck" name="messageCheck"> 站内信
                                        </label>
                                    </div>
                                </div>

                                <div class="col-sm-12 form-group">
                                    <span class="color-green">选择模板：</span>
                                    
                                    <select id="messageTemplate" name="messageTemplate">
                                        <option value="">自定义</option>
                                    </select>
                                    
                                    
                                </div>
                                <div class="col-sm-12 form-group">
                                    <span class="pull-left margin-t">站内信标题：</span>
                                    <input type="text" id="messageTitle" name="messageTitle" class="col-sm-6">
                                </div>
                                <div class="col-sm-12 form-group">
                                    <textarea class="col-sm-12" id="messageContent" name="messageContent">

                                    </textarea>
                                </div>
                            </div> -->
					</div>
					
						<!-- 准入驳回通知S -->
						<div id="div-access-rejected-notification" style="display:none;">
							<!-- <div>
								<div class="col-sm-12 form-group">
									<div class="checkbox">
										<label> <input name="access-rejected-notification-isSendEmail" type="checkbox"> 邮件 </label>
									</div>
								</div>

								<div class="col-sm-12 form-group">
									<span class="color-green">选择模板：</span> 
									<select name="access-rejected-notification-email-tempId" id="access-rejected-notification-email-tempId">
										<option value="0">自定义</option>
									</select> 模板描述：这是自定义模板
								</div>
								<div class="col-sm-12 form-group">
									<span>邮件标题：</span> <input type="text" name="access-rejected-notification-email-title">
								</div>
								<div class="col-sm-12 form-group">
									<textarea id="textareaEditor" name="access-rejected-notification-email-content">

                                    </textarea>
								</div>
							</div>
							<div>
								<div class="col-sm-12 form-group">
									<div class="checkbox">
										<label> <input name="access-rejected-notification-isSendSms" type="checkbox"> 短信
										</label>
									</div>
								</div>

								<div class="col-sm-12 form-group">
									<span class="color-green">选择模板：</span> 
									<select >
										<option value="">自定义</option>
									</select> 模板描述：这是自定义模板
								</div>
								<div class="col-sm-12 form-group">
									<textarea class="col-sm-12">

                                    </textarea>
								</div>
							</div>
							<div>
								<div class="col-sm-12 form-group">
									<div class="checkbox">
										<label> <input type="checkbox"> 站内信
										</label>
									</div>
								</div>

								<div class="col-sm-12 form-group">
									<span class="color-green">选择模板：</span> <select>
										<option value="">自定义</option>
									</select> 模板描述：这是自定义模板
								</div>
								<div class="col-sm-12 form-group">
									<span>站内信标题：</span> <input type="text">
								</div>
								<div class="col-sm-12 form-group">
									<textarea class="col-sm-12">

                                    </textarea>
								</div>
							</div> -->
						</div>
						<!-- 准入驳回通知E -->
						
						<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_access_verify%>_condition" name="<%=WorkFlowUtils.process_definition_key_access_verify%>_condition">
						<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_access_verify%>_node" name="<%=WorkFlowUtils.process_definition_key_access_verify%>_node">
						<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_access_verify%>_businessKey" name="<%=WorkFlowUtils.process_definition_key_access_verify%>_businessKey">
						<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_access_verify%>_reasonCheck" name="<%=WorkFlowUtils.process_definition_key_access_verify%>_reasonCheck">
						
						<!-- 是否办理并发送 -->
						<input type="hidden" id="isSend" name="isSend" value="true">
						
						<div class="col-sm-12 form-group">
                             <textarea class="col-sm-12" rows="5" placeholder="备注" id="<%=WorkFlowUtils.process_definition_key_access_verify%>_reason" name="<%=WorkFlowUtils.process_definition_key_access_verify%>_reason"></textarea>
                        </div>
					
					</form>
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm access-submit access-bohui" style="display: none;" data-dismiss="modal" id="access-select-back" value="back"><i class="ace-icon fa fa-times"></i>驳回</button>
				<button class="btn btn-sm access-submit access-nopass" style="display: none;" data-dismiss="modal" id="access-select" value="false"><i class="ace-icon fa fa-times"></i>不通过</button>
				<button class="btn btn-sm btn-primary access-submit access-send" type="submit" value="true" style="display:none;"><i class="ace-icon fa fa-check"></i> 办理并发送</button>
			    <button class="btn btn-sm btn-primary access-submit" type="submit" value="true"><i class="ace-icon fa fa-check"></i> 办理</button>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->

<div id="quota-modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="quota-title">处理额度申请</h4>
			</div>
			
			<!-- luy-额度申请 -->
			<div id="div-limit-file" class="modal-body" style="display: none;">
			
				<h4 class="blue bigger">需要审核的资料如下</h4>
				
				<div class="div-bus-detail">
					<a id="findInfoForLimit" class="green flow-apply-detail"  >
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
			
			<!-- 经理审批--操作记录 -->
			<div id="jlspCaoZuoLog" class="modal-body" style="display: none;">
				<div class="row">
					<div class="col-xs-12">
						<div style="height: 260px;   overflow: auto;">
							<table id="sample-table-2" class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th class="c-align col-sm-1">#</th>
										<th class="c-align col-sm-2">流程名称</th>
										<th class="c-align col-sm-2">操作人</th>
										<th class="c-align col-sm-2">
											<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
											操作时间
										</th>
										<th class="c-align col-sm-2">
											处理结果
										</th>
										<th class="c-align col-sm-3">操作意见/备注</th>
									</tr>
								</thead>
							
								<tbody class="fw-black-detail-for-caozuolog">
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
			
			<div class="modal-body">
				<div class="container-fluid">
				  <form id="quota-form" class="form-horizontal" method="post">
				  <div id="quota-select" style="display:none;">
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 行业风险</label>
						
						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_001"> 
								<option value="6" selected="selected">支持类</option>
								<option value="3">维持类</option>
								<option value="0">审慎类或退出类</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 销售稳定性 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_002" style="width:60%"> 
								<option value="4" selected="selected">企业连续2年未出现0申报，且申报数额趋向正向增长</option>
								<option value="2">企业连续2年未出现连续三个月0申报，且总0申报次数少于4次且申报数额趋于平稳</option>
								<option value="0">企业最近连续1年无税申报</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 纳税规模 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_003"> 
								<option value="8" selected="selected">年纳税额[100,+∞）</option>
								<option value="6">年纳税额[50,100）</option>
								<option value="4">年纳税额[30,50）</option>
								<option value="2">年纳税额[0,30）</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业经营年限 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_004"> 
								<option value="8" slected="selected">企业持续经营超过8年</option>
								<option value="5">企业持续经营超过5年</option>
								<option value="2">企业持续经营超过2年</option>
								<option value="0">企业持续经营未满2年</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业纳税信誉 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_006"> 
								<option value="5" selected="selected">纳税信用等级A类企业</option>
								<option value="3">纳税信用等级B类企业</option>
								<option value="0">纳税信用等级C/D类企业</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业拥有员工数 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_007"> 
								<option value="5" selected="selected">企业拥有在职员工 >= 100人</option>
								<option value="3">企业拥有在职员工 >= 50人</option>
								<option value="2">企业拥有在职员工 >= 20人</option>
								<option value="1">企业拥有在职员工 >= 10人</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业欠税情况 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_008"> 
								<option value="3" selected="selected">没有欠税</option>
								<option value="0">有欠税尚未缴清</option>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 户籍情况 </label>

						<div class="col-sm-8" style="text-align:left;width:60%">
							<select name="quota_009" style="width:60%;background-color: gainsboro;"> 
								<option value="3" selected="selected">本地户籍</option>
								<option value="2">非本地户籍但拥有本地居住证，且在当地近两年有连续个税缴纳记录</option>
								<option value="1">非本地户籍但有本地居住证</option>
								<option value="0">非本地户籍且无本地居住证</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 学历 </label>

						<div class="col-sm-8" style="text-align:left;" >
							<select name="quota_010" style="background-color: gainsboro;"> 
								<option value="2">本科及以上</option>
								<option value="0">其他</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 年龄 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_011" style="background-color: gainsboro;"> 
								<option value="2" selected="selected">(35,60)</option>
								<option value="1">[18,35]</option>
								<option value="0">[0,18) 或 [60,+∞）</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 婚姻状况 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_012" style="background-color: gainsboro;"> 
								<option value="2" selected="selected">已婚</option>
								<option value="0">离异或未婚</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 家庭净资产情况 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_015" style="background-color: gainsboro;"> 
								<option value="5" selected="selected">500万及以上</option>
								<option value="3">300万-500万</option>
								<option value="1">净资产300万以下(房产)</option>
								<option value="0">无房产</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 是否进入税务及一路贷黑名单 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_013" style="background-color: gainsboro;"> 
								<option value="2" selected="selected">否</option>
								<option value="0">是</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 是否有过犯罪记录 </label>
						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_014" style="background-color: gainsboro;"> 
								<option value="2" selected="selected">否</option>
								<option value="0">是</option>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 经营场所 </label>

						<div class="col-sm-8" style="text-align:left;">
							<select name="quota_005" style="background-color: gainsboro;"> 
								<option value="4" selected="selected">自有场所经营</option>
								<option value="2">固定场所租赁满3年</option>
								<option value="0">固定场所租赁满1年</option>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-4 control-label no-padding-right" style="text-align:right;"> 企业纳税信用等级证明 </label>
						<div class="col-sm-8" style="text-align:left;">
							<label class="control-label"><a id="qinsxydjzm-a" href="javascript:void(0)">上传图片</a></label>
							<input type="hidden" id="upload-busId">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<label class="control-label"><a id="qinsxydjzm-a-ck" href="javascript:void(0)">查看图片</a></label>
						</div>
					</div>
					
					</div>
					
					<div id="quota-apply-notification" style="display:none;">
					<!-- 额度申请客服通知--lixj -->
					</div>
					
					<div id="quota-apply-rejected-notification" style="display:none;">
					<!-- 额度申请驳回客服通知--lixj -->
					</div>
					
					<div style="display:none;" id="quota-data-service">
						  <div class="col-sm-12 form-group" id="fieldTime" style="display:none;">
		                      <span class="pull-left margin-t">开户时间：</span>
		                      <input class="col-sm-6"  type="text"  name="<%=WorkFlowUtils.process_definition_key_quota_apply%>_fieldTime" id="<%=WorkFlowUtils.process_definition_key_quota_apply%>_fieldTime" placeholder="开户时间"/>
		                  </div>
	
		                  <div class="col-sm-12 form-group" id="fieldAddr" style="display:none;">
		                      <span class="pull-left margin-t">开户地点：</span>
		                      
		                      <input class="col-sm-6 " type="text" name="<%=WorkFlowUtils.process_definition_key_quota_apply%>_fieldAddr" placeholder="开户地点"/>
		                      
		                  </div>
                   </div>
                   <div class="col-sm-12 form-group">
						<textarea class="col-sm-12" rows="5"  placeholder="备注" id="<%=WorkFlowUtils.process_definition_key_quota_apply%>_reason" name="<%=WorkFlowUtils.process_definition_key_quota_apply%>_reason"></textarea>
					</div>
					<div class="col-sm-12 form-group" style="display:none;" id="quota_credit_limit">
						 <span class="pull-left margin-t">授信额度</span><input type="text" name="<%=WorkFlowUtils.process_definition_key_quota_apply%>_creditLimit" id="<%=WorkFlowUtils.process_definition_key_quota_apply%>_creditLimit">
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
					<!-- 是否办理并发送 -->
					<input type="hidden" id="isSend" name="isSend" value="true">
				</form>
				</div>
			</div>

			<div class="modal-footer">
				<button style="display: none" class="btn btn-sm quota-submit" data-dismiss="modal" id="quota-select-back" value="back"><i class="ace-icon fa fa-times"></i>驳回 </button>
				<button style="display: none" class="btn btn-sm quota-submit " data-dismiss="modal" id="quota-xamination_approval" value="false"><i class="ace-icon fa fa-times"></i>不通过</button>
				<button class="btn btn-sm btn-primary quota-submit quota-send" type="submit" value="true" style="display:none;"><i class="ace-icon fa fa-check"></i> 办理并发送</button>
				<button class="btn btn-sm btn-primary quota-submit" type="button" value="true"><i class="ace-icon fa fa-check"></i> 办理</button>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->

<!--现场开户办理model  -->
<div id="field-modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="field-title">处理开户流程</h4>
			</div>

			<div class="modal-body">
				<div class="row" style="text-align: center;">
				    <form id="field-form" class="form-horizontal" method="post"> 
						
						<div style="display:none;" id="field-task-person-data">
						  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >联系人</label>
		
		                      <div  class="col-sm-9" id="contract-name">
		                      </div>
		                  </div>
		                  <div class="space-4"></div>
		                  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >联系电话</label>
		
		                      <div  class="col-sm-9" id="contract-tel">
		                      </div>
		                  </div>
		                  <div class="space-4"></div>
						  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >开户时间</label>
		
		                      <div  class="col-sm-9" id="field-time">
		                      </div>
		                  </div>
		                  <div class="space-4"></div>
		                  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >开户地点</label>
		
		                      <div  class="col-sm-9" id="field-addr">
		                      </div>
		                  </div>
		                  <div class="space-4"></div>
		                  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >备注信息</label>
		
		                      <div  class="col-sm-9" id="field-remarks">
		                      </div>
		                  </div>
	                   </div>
	                   <div class="control-group" style="display:none;" id="field-task-person">

						</div>
						
						<div style="display:none;" id="field-data-fill">
						  <%-- <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >U盾码</label>
		
		                      <div  class="col-sm-9">
		                          <input class="input-large col-xs-10 col-sm-5 " type="text"  name="<%=WorkFlowUtils.process_definition_key_field_account%>_dataFill" id="<%=WorkFlowUtils.process_definition_key_field_account%>_dataFill" placeholder="U盾码"/>
		                      </div>
		                  </div>
		                  <div class="space-4"></div> --%>
		                  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >合同号</label>
		
		                      <div  class="col-sm-9">
		                          <input class="input-large col-xs-10 col-sm-5 " type="text" name="<%=WorkFlowUtils.process_definition_key_field_account%>_contractNum" id="<%=WorkFlowUtils.process_definition_key_field_account%>_contractNum" placeholder="合同号"/>
		                      </div>
		                  </div>
		                  <div class="space-4"></div>
		                   <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >应收服务费</label>
		
		                      <div  class="col-sm-9" id="service-charge">
		                      </div>
		                      
		                      <input type="hidden" id="js-hidden-service-charge">
		                  </div>
		                  
		                  
		                  
		                  <div class="space-4"></div>
		                  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >服务费</label> 
		
		                      <div  class="col-sm-6">
		                          <input id="js-chk-isPayServiceMoney" type="checkbox" name="<%=WorkFlowUtils.process_definition_key_field_account%>_serviceChargeState">已支付服务费
		                      </div>
		                  </div>
		                  
		                  <div id="js-div-isPayServiceMoney" style="display:none;">
			                  <div class="space-4"></div>
			                   <div class="form-group">
			                      <label class="col-sm-3 control-label no-padding-right" >实收服务费</label>
			                      <div  class="col-sm-9" id="service-charge">
			                      	<input class="input-large col-xs-10 col-sm-5 " type="text" name="<%=WorkFlowUtils.process_definition_key_field_account%>_serviceChargeActual" id="<%=WorkFlowUtils.process_definition_key_field_account%>_serviceChargeActual" placeholder="实际收取服务费用"/>
			                      </div>
			                  </div>
		                  </div>
		                  
		                  <div class="space-4"></div>
		                  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >发票</label> 
		
		                      <div  class="col-sm-6">
		                          <input type="checkbox" name="<%=WorkFlowUtils.process_definition_key_field_account%>_isInvoicing" id="<%=WorkFlowUtils.process_definition_key_field_account%>_isInvoicing">是否开具发票
		                      </div>
		                  </div>
		                 
						<%-- 	U盾码<input type="text" name="<%=WorkFlowUtils.process_definition_key_field_account%>_dataFill"></br>
							合同号<input type="text" name="<%=WorkFlowUtils.process_definition_key_field_account%>_contractNum"></br>
							<input type="checkbox" name="<%=WorkFlowUtils.process_definition_key_field_account%>_serviceChargeState">是否支付服务费</br>
							支付金额<input type="text" name="<%=WorkFlowUtils.process_definition_key_field_account%>_serviceCharge"></br> --%>
						</div>
						<div style="display:none;" id="business-archive">
						 <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >档案号</label> 
		
		                      <div  class="col-sm-6">
		                         <input type="text" class="input-large col-xs-10 col-sm-5 " name="<%=WorkFlowUtils.process_definition_key_field_account%>_archive" id="<%=WorkFlowUtils.process_definition_key_field_account%>_archive">
		                      </div>
		                  </div>
							<%-- 档案号<input type="text" name="<%=WorkFlowUtils.process_definition_key_field_account%>_archive"> --%>
						</div>
						<textarea rows="5" cols="60" placeholder="备注" id="<%=WorkFlowUtils.process_definition_key_field_account%>_reason" name="<%=WorkFlowUtils.process_definition_key_field_account%>_reason"></textarea>
						<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_field_account%>_condition" name="<%=WorkFlowUtils.process_definition_key_field_account%>_condition">
						<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_field_account%>_taskPerson" name="<%=WorkFlowUtils.process_definition_key_field_account%>_taskPerson">
						<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_field_account%>_node" name="<%=WorkFlowUtils.process_definition_key_field_account%>_node">
						<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_field_account%>_businessKey" name="<%=WorkFlowUtils.process_definition_key_field_account%>_businessKey">
					</form>
				</div>
			</div>

			<div class="modal-footer">

				<a class="btn btn-sm btn-primary field-submit" href="#">
					<i class="ace-icon fa fa-check"></i>
					办理
				</a>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->

<div id="quickloan-modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">处理快速贷款</h4>
			</div>

			<div class="modal-body">
			    <div style="height:340px;overflow-y:auto;">
					<div class="col-sm-12" style="text-align: center;">
						<div class="col-xs-12">
							<div>
								<table id="sample-table-2" class="table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th class="c-align col-sm-3">处理人</th>
											<th class="c-align col-sm-4">
												<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
												处理时间
											</th>
											<th class="c-align col-sm-5">
												处理意见
											</th>
										</tr>
									</thead>
									<tbody class="fw-handle-record">
									</tbody>
								</table>
							</div>
						</div>
					</div>
					
					<div class="col-sm-12" style="text-align: center;">
					    <form id="quickloan-form" method="post" class="form-horizontal">
					    	<div class="form-group">
								<label class="col-sm-2 control-label no-padding-right" for="comName"> 公司名称 </label>

								<div class="col-sm-10">
									<input type="text" id="comName" name="comName" placeholder="请输入公司名称" class="col-xs-10 col-sm-5">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label no-padding-right" for="comName"> 处理意见 </label>
								<div class="col-sm-10">
									<textarea class="col-xs-10 col-sm-10" name="quickloan_reason" id="quickloan_reason" placeholder="请输入处理意见" rows="5"></textarea>
								</div>
							</div>
							<!-- <div id="quickloan-input-hidden" style="display:none;">
							<select id="quick-loan-select" > 
							<option value="1">满足条件，线下</option>
							<option value="2">满足条件，线上</option>
							<option value="3">不满足条件</option>
							</select>
							</div> -->
							<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_quickloan%>_condition" name="<%=WorkFlowUtils.process_definition_key_quickloan%>_condition">
							<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_quickloan%>_node" name="<%=WorkFlowUtils.process_definition_key_quickloan%>_node">
							<input type="hidden" id="<%=WorkFlowUtils.process_definition_key_quickloan%>_businessKey" name="<%=WorkFlowUtils.process_definition_key_quickloan%>_businessKey">
							<input type="hidden" name="data-taskid" id="data-taskid">
						</form>
					</div>
				</div>
			</div>
	
			<div class="modal-footer">
				<a id="appendRemark" class="btn btn-sm btn-primary quickloan-submit" href="#" data-condition="true">
					<i class="ace-icon fa fa-check"></i>
					追加意见
				</a>
				<a id="handleQuickLoan" class="btn btn-sm btn-primary quickloan-submit" href="#" data-condition="true">
					<i class="ace-icon fa fa-check"></i>
					办理并结束
				</a>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->


<div id="picture-modal-form" class="modal" tabindex="-1"  data-backdrop="static">
	<div class="modal-dialog" style="width:80%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h5 class="blue bigger" id="processdefinition-modal-title"></h5>
			</div>

			<div class="modal-body">
				<div class="row" style="text-align: center; overflow:auto;">
				    <img id="processdefinition-picture" src="" style="border: none;max-width: 300%;">
				</div>
			</div>

			<div class="modal-footer">
				<a class="btn btn-sm btn-primary" href="#" data-dismiss="modal">
					<i class="ace-icon fa fa-check"></i>
					关闭
				</a>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->



<!--任务转办model  -->
<div id="turn-to-do-modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog" >
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">任务转办</h4>
			</div>

			<div class="modal-body">
				<div class="form-group">
					<label class="col-sm-6 control-label no-padding-right" style="text-align:right;"> 将任务转给其他人 </label>
					<div class="col-sm-6" style="text-align:left;" id="turn-todo">
					</div>
				</div>
				<br />
			</div>
			
			<div class="modal-footer">
				<a class="btn btn-sm btn-primary trun-todo-button" href="#">
					<i class="ace-icon fa fa-check"></i>
					确认
				</a>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->



<!-- 打印清单modal -->
<div id="print-modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
		<form class="form-horizontal" id="print-form" method="post" action="${contentPath}/fwBussinessSxdPrintData">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">打印清单</h4>
			</div>

			<div class="modal-body">
				 <div class="row" id="print-body" style="text-align: left ;padding-left: 30px;">
			   		<!-- <div class="checkbox"><label><input name="print-detail" type="checkbox" class="ace" data-busid="201508210001" value="01"><span class="lbl"> 纳税人、扣缴义务人涉税保密信息查询申请表</span>  &nbsp; <a class="down-load" data-busid="201508210001" data-bustype="01">下载</a> &nbsp;<a class="down-load-detail" data-busid="201508210001" data-bustype="01">查看</a></label></div> -->
				</div>
			</div>
			<div class="modal-footer">
			    <button class="btn btn-sm btn-primary print-submit" type="button"><i class="ace-icon fa fa-check"></i> 确认打印</button>
			</div>
			</form>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->

<!-- 上传文件 -->
<div class="modal fade" id="upload-modal" role="dialog" aria-labelledby="gridSystemModalLabel"  data-backdrop="static">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close js-uploadImg-close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="gridSystemModalLabel">图片上传</h4>
            </div>
            <div class="modal-body">
                <div class='box-content'>
                    <form action='${contentPath}/uploadFile' enctype='multipart/form-data' class='fileupload' method='POST' autocomplete="off"/>
                    <input id="businessId-uplodad" name="businessId" type='hidden'/>
                    <input id="fileType" name="fileType" type='hidden'/>
                    <input id="fkId" name="fkId" type='hidden'/>
                    <input id="businessType" name="businessType" type='hidden' value="01"/>
                    <input id="fkType" name="fkType" type='hidden' value="01"/>
                    <input id="uploadCount" name="uploadCount" type='hidden'/>
                    <div class='row-fluid fileupload-buttonbar'>
                        <div class='span7'>
                             <span class='btn btn-success fileinput-button'>
                               <i class='icon-plus icon-white'></i>
                               <span>上传图片</span>
                               <input data-autoupload="true"  id="fileImg" multiple='' name='files[]' type='file'/>
                             </span>
                        </div>
                        <!--<div class='span5 fileupload-progress fade'>-->
                        <!--<div aria-valuemax='100' aria-valuemin='0'-->
                        <!--class='progress progress-success progress-striped active' role='progressbar'>-->
                        <!--<div class='bar' style='width:0%;'></div>-->
                        <!--</div>-->
                        <!--<div class='progress-extended'></div>-->
                        <!--</div>-->
                    </div>
                    <div class='fileupload-loading'></div>
                    <br/>
                    <div class="table-scroll">
                        <table class='table table-striped' role='presentation'>
                            <tbody class='files' data-target='#modal-gallery' data-toggle='modal-gallery'></tbody>
                        </table>
                    </div>
                    <div class="row img-cc">
                        <p>
                            <span class="">图片规格：</span>
                            <span>图片不小于200像素 X 200像素，不大于1M。</span>
                        </p>
                    </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn btn-default js-uploadImg-close">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<!-- 查看图片 -->
<div class="modal fade" id="show-modal" role="dialog" aria-labelledby="gridSystemModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close js-uploadImg-close" data-dismiss="modal" aria-label="Close">
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
                <button type="button" data-dismiss="modal" class="btn btn-default js-uploadImg-close">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<!-- 弹出层-操作记录S -->
<div id="communicate-log-modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">操作记录</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12">
						<div>
							<table id="sample-table-2" class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th class="c-align">#</th>
										<th class="c-align">流程名称</th>
										<th class="c-align">操作人</th>
										<th class="c-align">
											<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
											操作时间
										</th>
										<th class="c-align">
											处理结果
										</th>
										<th class="c-align">操作意见/备注</th>
									</tr>
								</thead>
							
								<tbody class="fw-black-detail">
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

			<div class="modal-footer">
				<button class="btn btn-sm" data-dismiss="modal">
					<i class="ace-icon fa fa-times"></i>
					关闭
				</button>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->
<!-- 弹出层-操作记录E -->

<input type="hidden" name="turn-todo-name" id="turn-todo-name">
<input type="hidden" name="print-data-busId" id="print-data-busId">
<input type="hidden" name="turn-todo-business-key" id="turn-todo-business-key">
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath}/plugins/formatCurrency.js"></script>
<link rel="stylesheet" type="text/css" href="${contentPath}/plugins/editor/css/wangEditor-1.3.11.css">
<link rel="stylesheet" type="text/css" href="${contentPath}/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript" src='${contentPath}/plugins/editor/js/wangEditor-1.3.11.js'></script>
<script type="text/javascript" src='${contentPath}/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js'></script>
<script type="text/javascript" src='${contentPath}/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.zh-CN.js'></script>

<!-- / fileupload -->
<script src='${contentPath}/plugins/fileupload/vendor/jquery.ui.widget.js' type='text/javascript'></script>
<script src='${contentPath}/plugins/fileupload/jquery.iframe-transport.js' type='text/javascript'></script>
<script src='${contentPath}/plugins/fileupload/jquery.fileupload.js' type='text/javascript'></script>
<script src='${contentPath}/plugins/jsTemp/tmpl.js' type='text/javascript'></script>
<script src="${contentPath}/plugins/color-box/jquery.colorbox-min.js"></script>
<script src="${contentPath}/scripts/jquery.cookie.js"></script>

<script src="${contentPath}/scripts/main.js"></script>

<%-- <script type="text/javascript" src='${contentPath}/plugins/modal/modal.js'></script> --%>
<script>
var filetypeJson = '${filetypes}';
var fileJson = JSON.parse(filetypeJson);
</script>
<script type="text/javascript">
var textAreaBoolean = false;
jQuery(function($) {
	
	
	$('#quota-modal-form').on('shown.bs.modal', function () {
		$('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_fieldTime').datetimepicker({startDate: new Date(),autoclose: true,language:"zh-CN",pickerPosition: "bottom-right",forceParse: 0,showMeridian: 1});
	  });
	
	var oTable1 = 
	$('#table2')
	//.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
	.dataTable( {
		bAutoWidth: false,
		"aoColumns": [
		  { "bSortable": false }, null,{ "bSortable": false }, null,
		  { "bSortable": false }
		],
		"aaSorting": [],

    } );

	$(document).on('click', 'th input:checkbox' , function(){
		var that = this;
		$(this).closest('table').find('tr > td:first-child input:checkbox')
		.each(function(){
			this.checked = that.checked;
			$(this).closest('tr').toggleClass('selected');
		});
	});


	$('[data-rel="tooltip"]').tooltip({placement: tooltip_placement});
	function tooltip_placement(context, source) {
		var $source = $(source);
		var $parent = $source.closest('table')
		var off1 = $parent.offset();
		var w1 = $parent.width();

		var off2 = $source.offset();
		//var w2 = $source.width();

		if( parseInt(off2.left) < parseInt(off1.left) + parseInt(w1 / 2) ) return 'right';
		return 'left';
	}

});


$(function(){
	
	$.validator.addMethod("validateEqual",function(value,element,params){
         var actual = $('#<%=WorkFlowUtils.process_definition_key_field_account%>_serviceChargeActual').val();
		if(actual==$("#js-hidden-service-charge").val()){
			return true;
		}else{
			return false;
		}
	},"实收服务费与应收服务费相等");
	//表单验证
	var validator = {
		    validator : function(){
		        var varlidator = $('#field-form').validate({
		        	
		            rules : {
		               <%--  '<%=WorkFlowUtils.process_definition_key_field_account%>_dataFill' : {
		                    required : true,
		                }, --%>
		                '<%=WorkFlowUtils.process_definition_key_field_account%>_contractNum' : {
		                    required : true
		                },
		                '<%=WorkFlowUtils.process_definition_key_field_account%>_serviceChargeActual' : {
		                	number : true,
		                	validateEqual : true
		                },
		                '<%=WorkFlowUtils.process_definition_key_field_account%>_archive' : {
		                    required : true
		                }
		            },
		            /* 设置错误信息 */
				    messages: {
				    	<%-- '<%=WorkFlowUtils.process_definition_key_field_account%>_dataFill' : {
							required : "请输入U盾号"
						}, --%>
						'<%=WorkFlowUtils.process_definition_key_field_account%>_contractNum' : {
				        	required : "请输入合同号"
						},
						'<%=WorkFlowUtils.process_definition_key_field_account%>_serviceChargeActual' : {
							number : "请输入数字"
						},
						'<%=WorkFlowUtils.process_definition_key_field_account%>_archive' : {
							required : "请输入档案号"
						}
				    },
		            errorClass : 'text-warning red',
		            errorPlacement : function(error, element){
		                element.closest('div').append(error);
		            }
		        });
		        return varlidator;
		    },
		    valid : function(){
		        return this.validator().form();
		    },
		    reset : function(){
		        this.validator().resetForm();
		    }

	};
	
		//表单验证 额度申请
		var validatorQuota = {
			    validator : function(){
			        var varlidator = $('#quota-form').validate({
			            rules : {
			                '<%=WorkFlowUtils.process_definition_key_quota_apply%>_creditLimit' : {
			                    required : true,
			                    number: true,
			                    min:1
			                }
			            },
			            /* 设置错误信息 */
					    messages: {
					    	'<%=WorkFlowUtils.process_definition_key_quota_apply%>_creditLimit' : {
								required : "请输入授信金额",
								number :"请输入合法的数字",
								min:"授信金额不能小于1"
							}
					    },
			            errorClass : 'text-warning red',
			            errorPlacement : function(error, element){
			                element.closest('div').append(error);
			            }
			        });
			        return varlidator;
			    },
			    valid : function(){
			        return this.validator().form();
			    },
			    reset : function(){
			        this.validator().resetForm();
			    }

		};
		
		/**
		* 额度申请--客服通知（验证：开户时间、开户地点）（luy）
		*/
		var validatorQuotaService = {
			    validator : function(){
			        var varlidator = $('#quota-form').validate({
			            rules : {
			                '<%=WorkFlowUtils.process_definition_key_quota_apply%>_fieldTime' : {
			                    required : true
			                },
			                '<%=WorkFlowUtils.process_definition_key_quota_apply%>_fieldAddr' : {
			                    required : true
			                }
			            },
			            /* 设置错误信息 */
					    messages: {
					    	'<%=WorkFlowUtils.process_definition_key_quota_apply%>_fieldTime' : {
								required : "请选择开户时间"
							},
							'<%=WorkFlowUtils.process_definition_key_quota_apply%>_fieldAddr' : {
			                    required : "请输入开户地点"
			                }
					    },
			            errorClass : 'text-warning red',
			            errorPlacement : function(error, element){
			                element.closest('div').append(error);
			            }
			        });
			        return varlidator;
			    },
			    valid : function(){
			        return this.validator().form();
			    },
			    reset : function(){
			        this.validator().resetForm();
			    }

		};
	
	
	
 var resSubmit = false;
	var bind={
		bindEvent: function(){
			
			/**
			 * 点击查看资料，清除cookie中存的tab连接
			 */
			 
			 $("tbody").delegate('.flow-apply-detail','click',function(){
				 
				$.cookie('the_cookie', '#accountInfo', {path: '/' }); 
				location.href = $(this).attr('data-href');
				
			});
			
			 $(".div-bus-detail").delegate('.flow-apply-detail','click',function(){
				$.cookie('the_cookie', '#accountInfo', {path: '/' }); 
				window.open($(this).attr('data-href'));
					
			});
			
			/**
			 * 点击列表中的 '操作记录'(luy)
			 */	
			 $("tbody").delegate('.js-find-communicate-log','click',function(){
				var busId = $(this).attr('data-busId');
				action.doFindComLog(busId);
				$('#communicate-log-modal-form').modal('show');
			});
			 
			
			/**
			 * 点击 '企业纳税信用等级证明 34'--上传图片 (luy)
			 */
			$('#qinsxydjzm-a').on('click', function() {
				$('#businessId-uplodad').val($('#upload-busId').val());
				$('#fileType').val('34');
				$('#fkId').val($('#upload-busId').val());
				
				$('.table .files').empty();
				$('#upload-modal').modal('show');
				
				$('#upload-modal').on('hidden.bs.modal', function () {
					$('body').addClass('modal-open');
				});
			});
			
			/**
			 * 点击 '企业纳税信用等级证明34'--查看图片
			 */
			$('#qinsxydjzm-a-ck').on('click', function() {
				action.getImgFileInfo('34');
				$('#show-modal').modal('show');
				
				// 允许model滚动
				$('#show-modal').on('hidden.bs.modal', function (e) {
		            $('body').addClass('modal-open')
		        })
			});
			
			/**
			 * 点击 '查看图片'弹出层中删除按钮(luy)
			 */
			
			$("#file-img-show").delegate("button","click",function(){
				var fileId= $(this).attr('fileId');
				var that = this;
				$.ajax({
					url : contentPath+'/deleteFile?fileId=' + fileId,
					data : '',
					type : 'POST',
					dataType : 'json',
					success : function() {
                        if (!!window.ActiveXObject || "ActiveXObject" in window){
                            that.parentElement.removeNode(true);
                        } else {
                            that.parentElement.remove();
                        }
					}
				});
			});
			
			//为办理绑定点击事件
			$("tbody").delegate('.flow-bootbox-options','click',function(){
				var flowType = $(this).attr('data-flowtype');
				
				if(flowType=='<%=WorkFlowUtils.process_definition_key_access_verify%>'){
					//准入验证
					$('#access-form').attr('action',contentPath+'/workflow/accessverify/task/complete/'+$(this).attr('data-taskid'));
					$('#<%=WorkFlowUtils.process_definition_key_access_verify%>_node').val($(this).attr('data-node'));
					$('#<%=WorkFlowUtils.process_definition_key_access_verify%>_businessKey').val($(this).attr('data-buskey'));
					if($(this).attr('data-node')=='<%=WorkFlowUtils.role_service_phone_call%>'){
						//客服通知--lixj
						/* $('#access-title').text('准入验证客服通知结果'); */
						$('#access-select').hide();
						$('#customer-service-notify').show();
						$('#div-access-rejected-notification').hide();
						/* $('.access-send').show(); */
						
						// 隐藏驳回按钮
						$('#access-select-back').hide();
						
						// 增加发送通知的界面元素
						action.appendTemplateDiv("customer-service-notify");
						
						// 请求数据，填充数据
						action.getTemplate($(this).attr('data-buskey'), "access-verify");
					}
					//人工准入验证
					if($(this).attr('data-node')=='<%=WorkFlowUtils.role_manual_verify%>'){
						
						$('#access-title').text('人工准入验证');
						
						var busIdForAccess = $(this).attr('data-buskey');
						/* $('#findInfoFor').attr("href","${contentPath }/workflow/list/getfwApplyBusDetail?busId="+busIdForAccess); */
						$('#findInfoFor').attr("data-href","${contentPath }/workflow/list/getfwApplyBusDetail?busId="+busIdForAccess);
						$('#businessIdForFlag').val(busIdForAccess);
						action.getFileForAccess(busIdForAccess); //准入验证-查询要审核的资料(luy-)
						$('#div-access-file').show();
						
						$('#access-reason-check').show();
						$('#access-select').show();
						$('#customer-service-notify').hide();
						/* $('.access-send').hide(); */
						$('.access-bohui').show(); //显示“驳回”按钮
						$('.access-nopass').show(); //显示“不通过”按钮
					}
					//准入驳回通知
					if($(this).attr('data-node')=='<%=WorkFlowUtils.access_service_send_information_role%>'){
						$('#access-title').text('人工准入驳回通知');
						$('#div-access-rejected-notification').show();
						$('#customer-service-notify').hide();
						/* $('.access-send').show(); */
						
						// 隐藏驳回按钮
						$('#access-select-back').hide();
						
						// 增加发送通知的界面元素
						action.appendTemplateDiv("div-access-rejected-notification");
						
						// 请求数据，填充数据
						action.getTemplate($(this).attr('data-buskey'), "access-verify-rejected");
					}
					//action.getCenterDateBank($(this).attr('data-buskey'));
					$('#access-modal-form').modal('show');
				}else if(flowType=='<%=WorkFlowUtils.process_definition_key_quota_apply%>'){
					//额度申请
					$('#quota-form').attr('action',contentPath+'/workflow/quotaapply/task/complete/'+$(this).attr('data-taskid'));
					$('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_node').val($(this).attr('data-node'));
					$('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_businessKey').val($(this).attr('data-buskey'));
					//确认授信额度
					if($(this).attr('data-node')=='<%=WorkFlowUtils.role_approved_credit_limit%>'){
						$('#quota-title').text('确认授信额度');
						$('#quota_credit_limit').show();
						
						$('#quota-xamination_approval').show();//不通过按钮
					}else if($(this).attr('data-node')=='<%=WorkFlowUtils.role_xamination_approval%>'){
						//审批
						$('#quota-title').text('审批');
						$('#quota-xamination_approval').show();//不通过按钮
					}else if($(this).attr('data-node')=='<%=WorkFlowUtils.role_manual_review%>'){
						//人工打分
						$('#quota-title').text('人工打分');
						
						var busIdForAccess = $(this).attr('data-buskey');
						/* $('#findInfoForLimit').attr("href","${contentPath }/workflow/list/getfwApplyBusDetail?busId="+busIdForAccess); */
						$('#findInfoForLimit').attr("data-href","${contentPath }/workflow/list/getfwApplyBusDetail?busId="+busIdForAccess);
						$('#businessIdForFlag').val(busIdForAccess);
						action.getFileForLimit(busIdForAccess); //额度申请-查询要审核的资料(luy-)
						$('#div-limit-file').show();
						
						
						//$('#quota-xamination_approval').hide();	//不通过按钮
						//驳回按钮
						$('#quota-select-back').show();
						$('#quota-select').show();
						//让准入验证时 收集的资料不可修改
						action.selectdisabled($(this).attr('data-buskey'));
					}else if($(this).attr('data-node')=='<%=WorkFlowUtils.role_qutoa_service_call%>'){
						// 额度申请客服通知--lixj
						/* $('#quota-title').text('额度申请客服通知结果'); */
						//$('#quota-xamination_approval').hide();//不通过按钮
						$('#quota-data-service').show();
						$('#quota-apply-notification').show();
						$('#quota-apply-rejected-notification').hide();
						/* $('.quota-send').show(); */
						
						// 隐藏驳回按钮
						$('#quota-select-back').hide();
						
						// 增加发送通知的界面元素
						action.appendTemplateDiv("quota-apply-notification");
						
						// 请求数据，填充数据
						action.getTemplate($(this).attr('data-buskey'), "quota-apply");
						
					}else if($(this).attr('data-node')=='<%=WorkFlowUtils.quota_service_send_information_role%>'){
						//额度申请驳回客服通知通知
						$('#quota-title').text('额度申请驳回客服通知');
						$('#quota-data-service').hide();
						$('#quota-apply-notification').hide();
						$('#quota-apply-rejected-notification').show();
						/* $('.quota-send').show(); */
						
						// 隐藏驳回按钮
						$('#quota-select-back').hide();
						
						// 增加发送通知的界面元素
						action.appendTemplateDiv("quota-apply-rejected-notification");
						
						// 请求数据，填充数据
						action.getTemplate($(this).attr('data-buskey'), "quota-apply-rejected");
					}else if($(this).attr('data-node')=='<%=WorkFlowUtils.quota_manager_approval_role%>'){
						//经理审批
						
						action.getCaoZuoLog($(this).attr('data-buskey')); //查询操作记录
						$('#quota-title').text('审批（'+$(this).attr('data-busComName')+'）');
						$('#jlspCaoZuoLog').show();
						$('#quota-xamination_approval').show();//不通过按钮
					}
					
					$('#upload-busId').val($(this).attr('data-buskey'));
					$('#quota-modal-form').modal('show');
				}else if(flowType=='<%=WorkFlowUtils.process_definition_key_field_account%>'){
					//现场开户
					$('#field-form').attr('action',contentPath+'/workflow/fieldaccount/task/complete/'+$(this).attr('data-taskid'));
					$('#<%=WorkFlowUtils.process_definition_key_field_account%>_node').val($(this).attr('data-node'));
					$('#<%=WorkFlowUtils.process_definition_key_field_account%>_businessKey').val($(this).attr('data-buskey'));
					if($(this).attr('data-node')=='<%=WorkFlowUtils.role_task_assign%>'){
						//现场任务分配
						$('#field-title').text('现场任务分配');
						$('#field-task-person').show();
						$('#field-task-person-data').show();
						//查询所有业务
						action.getAdminByRole();
						//查询任务分配的是所需数据
						action.getDataCenterByBusId($(this).attr('data-buskey'));
					}else if($(this).attr('data-node')=='<%=WorkFlowUtils.role_data_backfill%>'){
						$('#field-title').text('数据回填');
						//数据回填
						$('#field-data-fill').show();
						action.getBusinessBybusId($(this).attr('data-buskey'));
					}else if($(this).attr('data-node')=='<%=WorkFlowUtils.role_business_archive%>'){
						$('#field-title').text('归档');
						$('#business-archive').show();
					}else{
						$('#field-title').text('客服结果通知');
					}
					$('#field-modal-form').modal('show');
				}else if(flowType=='<%=WorkFlowUtils.process_definition_key_quickloan%>'){
					
					// 快速贷款
					/* $('#quickloan-form').attr('action',contentPath+'/workflow/quickloan/task/complete/'+$(this).attr('data-taskid')); */
					$('#<%=WorkFlowUtils.process_definition_key_quickloan%>_node').val($(this).attr('data-node'));
					$('#<%=WorkFlowUtils.process_definition_key_quickloan%>_businessKey').val($(this).attr('data-buskey'));
					$('#data-taskid').val($(this).attr('data-taskid'));
					/* if ($(this).attr('data-node')=='role_quickloan_confirm') {
						$('#quickloan-input-hidden').show();
					} */
					
					/* ajax查询数据 */
					var url = contentPath + "/fwQuickLoan/getHanndleRecord";
					var param = {};
					
					param.buskey = $(this).attr('data-buskey');// 业务key
					
					$.ajax({
						url : url, 
						data : param,
						type : 'POST',
						dataType : 'json',
						async : false,
						success : function(data) {
							action.buildTr(data);
							
							/* 公司名称 */
							$("#comName").val(data.data.comName);
							/*submit = false;*/
						},
						error : function() {
							alert('服务器异常，访问失败！');
							/*submit = false;*/
						}
					});
					
					$('#quickloan-modal-form').modal('show');
				}
		
			});
			/**
			 * 准入验证为发送请求按钮绑定事件
			 */
			$("tbody").delegate('.send-apply-to-bank','click',function(e){
				e.preventDefault();
				var busKey = $(this).attr('data-buskey');
				var url ="";
				//准入验证
				if($(this).attr('data-flowtype')=='<%=WorkFlowUtils.process_definition_key_access_verify %>'){
					url = contentPath + '/workflow/accessverify/task/personDone/'+busKey+"/"+$(this).attr('data-taskid');
				//现场开户
				}else if($(this).attr('data-flowtype')=='<%=WorkFlowUtils.process_definition_key_quota_apply %>'){
					url = contentPath + '/workflow/quotaapply/task/personDone/'+busKey+"/"+$(this).attr('data-taskid');
				//额度申请
				}else if($(this).attr('data-flowtype')=='<%=WorkFlowUtils.process_definition_key_field_account%>'){
					url = contentPath + '/workflow/fieldaccount/task/personDone/'+busKey+"/"+$(this).attr('data-taskid');
				}else if($(this).attr('data-flowtype')=='<%=WorkFlowUtils.process_definition_key_loan%>'){
					//放款
					url = contentPath + '/workflow/loan/task/personDone/'+busKey+"/"+$(this).attr('data-taskid');
				}
				//弹出是否发送请求提示
				 if(!confirm("你确定要向银行发送请求么?成功后会自行处理流程")){
			         return false; 
			     }
				 if(resSubmit){
						return false;
					}
					resSubmit = true;
					tools.openLoading();
					$.ajax({
						url : url,
						type : 'GET',
						dataType : 'json',
						success : function(data) {
							tools.closeLoading();
							if (data.status == 'success') {
								tools.openCT({
			                        title: '成功',         // {String} required model title
			                        text: data.data.msg,   // {String} required model text
			                        type: 'success',        // {String} required 取值 success， fail， warning， default success
			                        buttons: [              // {Array} required buttons, 可以有一个 button
			                            {
			                                text: '确定',     // {String} required button text
			                                fn: function () {                   // {Function} click function
			                                	location.href=contentPath+"/workflow/list";
			                                },
			                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
			                            }
			                        ]
			                    });
								
								
								
							}else{
								tools.openST({
			                        title: '失败',             // {String}
			                        text: data.msg,       // {String}
			                        type: 'fail'                // {String}
			                    });
								
							}
							resSubmit=false;
						},
						error : function() {
							tools.closeLoading();
							tools.openST({
		                        title: '服务器异常',             // {String}
		                        text: '服务器异常，请求失败',       // {String}
		                        type: 'fail'                // {String}
		                    });
							
							resSubmit=false;
						}
					});
		
			});
			
			//为快速贷款:办理并结束
			$('#handleQuickLoan').on('click',function(){
				$('#quickloan-form').attr('action',contentPath+'/workflow/quickloan/task/complete/'+$("#data-taskid").val());
				$('#<%=WorkFlowUtils.process_definition_key_quickloan%>_condition').val(true);
				if ($('#<%=WorkFlowUtils.process_definition_key_quickloan%>_reason').val()=='') {
					//textAreaBoolean = true;
					//弹出不能为空的备注
					action.alertNotNull('请输入处理意见');
					return false;
				}
				$('#quickloan-form').submit();
				tools.openLoading();
			
			});
			//为快速贷款:追加意见
			$('#appendRemark').on('click',function(){
				$('#quickloan-form').attr('action',contentPath+'/fwQuickLoan/appendQuickLoanRemark');
				$('#<%=WorkFlowUtils.process_definition_key_quickloan%>_condition').val(true);
				if ($('#<%=WorkFlowUtils.process_definition_key_quickloan%>_reason').val()=='') {
					//textAreaBoolean = true;
					//弹出不能为空的备注
					action.alertNotNull('请输入处理意见');
					return false;
				}
				$('#quickloan-form').submit();
				tools.openLoading();
			
			});
			
			$('#quickloan-modal-form').on('hidden.bs.modal', function () {
				$('#<%=WorkFlowUtils.process_definition_key_quickloan%>_reason').val('');
				$('#quickloan-input-hidden').hide();
			});
			
			/**
			 * 为转办按钮绑定事件
			 */
			$("tbody").delegate('.flow-turn-todo','click',function(){
				$('#turn-todo-name').val($(this).attr('data-taskid'));
				$('#turn-todo-business-key').val($(this).attr('data-buskey'));
				
				action.getAdminByRoleAll($(this).attr('data-node'));
				$('#turn-to-do-modal-form').modal('show');
		
			});
			
			/**
			 * 资料下载提供
			 */
			$("#print-body").delegate('.down-load','click',function(){
		      var fileType = $(this).attr('data-busType');
		      action.downFile(fileType);
			});
			
			//为确认转办
			$('.trun-todo-button').on('click',function(){
				var param = {};
				param.taskId = $('#turn-todo-name').val();
				param.personName = $('#select-todo').val();
				param.busId = $('#turn-todo-business-key').val();
				tools.openLoading();
				$.ajax({
					url : contentPath + '/workflow/turnTodo/',
					type : 'POST',
					data :param,
					dataType : 'json',
					success : function(data) {
						tools.closeLoading();
						$('#turn-to-do-modal-form').modal('hide');
						if (data.status == 'success') {
							tools.openCT({
		                        title: '转办成功',         // {String} required model title
		                        text: '任务转办成功',   // {String} required model text
		                        type: 'success',        // {String} required 取值 success， fail， warning， default success
		                        buttons: [              // {Array} required buttons, 可以有一个 button
		                            {
		                                text: '确定',     // {String} required button text
		                                fn: function () {                   // {Function} click function
		                                	location.href=contentPath+"/workflow/list";
		                                },
		                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
		                            }
		                        ]
		                    });
							
						}else{
							tools.openST({
		                        title: '转办失败',             // {String}
		                        text: '业务异常转办失败',       // {String}
		                        type: 'fail'                // {String}
		                    });
							
							
						}
						resSubmit=false;
					},
					error : function() {
						tools.closeLoading();
						tools.openST({
	                        title: '转办失败',             // {String}
	                        text: '服务器异常转办失败',       // {String}
	                        type: 'fail'                // {String}
	                    });
						
						resSubmit=false;
					}
				});
			})
			
			//为准入验证 提交按钮绑定事件
			$('.access-submit').on('click',function(){
				// 人工准入验证
				if($('#<%=WorkFlowUtils.process_definition_key_access_verify%>_node').val()=='<%=WorkFlowUtils.role_manual_verify%>'){
					
					if($(this).val()=='true'){
						// 通过
						$('#<%=WorkFlowUtils.process_definition_key_access_verify%>_condition').val(1);
					}else if($(this).val()=='false'){
						// 不通过
						$('#<%=WorkFlowUtils.process_definition_key_access_verify%>_condition').val(2);
					}else if($(this).val()=='back'){
						// 驳回
						$('#<%=WorkFlowUtils.process_definition_key_access_verify%>_condition').val(3);
					}

					//$('#<%=WorkFlowUtils.process_definition_key_access_verify%>_condition').val($(this).val());
					if ($(this).val()!='true' && $('#<%=WorkFlowUtils.process_definition_key_access_verify%>_reason').val()=='') {
						//textAreaBoolean = true;
						//弹出不能为空的备注
						action.alertNotNull('请输入失败原因');
						return false;
					}
					// 提示未标记任何资料不合格
					if ($(this).val() != 'true' && $('#div-access-file').css('display') != 'none' && !$('#div-access-file').find('a').hasClass('red'))
					{
						//  驳回或者不通过时判断是否有选择不合格的资料
						if ($(this).val()=='false')
						{
							if (!confirm("您并未标记哪个资料不合格，确认不通过吗？"))
							{
								return false;	
							}
						}
						if ($(this).val()=='back')
						{
							if (!confirm("您并未标记哪个资料不合格，确认驳回吗？"))
							{
								return false;	
							}
						}
					}
					
					// 提示有资料标记为不合格，是否确认通过
					if ($(this).val() == 'true' && $('#div-access-file').css('display') != 'none' && $('#div-access-file').find('a').hasClass('red'))
					{
						if (!confirm("您有标记为不合格的资料，确认通过吗？"))
						{
							return false;	
						}
					}
					
				}
				if($('#<%=WorkFlowUtils.process_definition_key_access_verify%>_node').val()=='<%=WorkFlowUtils.role_service_phone_call%>'){
					$('#<%=WorkFlowUtils.process_definition_key_access_verify%>_condition').val(true);
					
					// 准入验证客服通知验证--lixj
					
					
					/* if ($(this).hasClass("access-send"))
					{ */
					result = action.verifySendMsg();
					if (result == false)
					{
						return false;
					}
					/* } */
					
				}
				
				// 准入驳回通知
				if($('#<%=WorkFlowUtils.process_definition_key_access_verify%>_node').val()=='<%=WorkFlowUtils.access_service_send_information_role%>'){
					$('#<%=WorkFlowUtils.process_definition_key_access_verify%>_condition').val(true);
					
						// 准入验证驳回客服通知验证--lixj
						/* if ($(this).hasClass("access-send"))
						{ */
						result = action.verifySendMsg();
						if (result == false)
						{
							return false;
						}
						/* } */
				}
				
				/* 办理或者办理并发送--lixj */
				/* if ($(this).hasClass("access-send")){
					$('#isSend').val('true');
				}
				else{
					$('#isSend').val('false');
				} */
				
				tools.openLoading();
				$('#access-form').submit();
			});
			//为准入验证model隐藏绑定事件
			$('#access-modal-form').on('hidden.bs.modal', function () {
				
				//隐藏“标记不合格”层
				$('#div-access-file').hide();
				//TODO 清空表单数据
				$('#<%=WorkFlowUtils.process_definition_key_access_verify%>_reason').val('');
				$('#access-reason-check').hide();
				$('#access-select').show();
				//隐藏办理并发送按钮
				/* $('.access-send').hide(); */
				$('.access-nopass').hide();//不通过按钮
				
				// 清除发送通知时绑定的界面元素(重要)--lixj
				$('#customer-service-notify').html("");
				$('#div-access-rejected-notification').html("");
				$('#customer-service-notify').hide();
				$('#div-access-rejected-notification').hide();
			});
			
			//为提示model隐藏绑定事件
			$('#confirm-tip-model').on('hidden.bs.modal', function () {
				$('body').addClass('modal-open');
			    //需要得到焦点
				if (textAreaBoolean ) {
					textAreaBoolean =false;
				
					if($('#customer-service-notify').css('display')!="none" || $('#div-access-rejected-notification').css('display')!="none" ||
							$('#quota-apply-notification').css('display')!="none" || $('#quota-apply-rejected-notification').css('display')!="none"){
						if($('#emailTitle').val()=='' || ($('#emailTitle').val()!='' && $('#emailTitle').val().length > 200)){
							$('#emailTitle').focus();
							return ;
						}
						if($('#smsContent').val()==''){
							$('#smsContent').focus();
							return ;
						}
						if($('#messageTitle').val()=='' || ($('#messageTitle').val()!='' && $('#messageTitle').val().length > 255)){
							$('#messageTitle').focus();
							return ;
						}
					}else {
						$(this).closest('.modal').parent().siblings('.main-container').find('.in').find('textarea').focus();
						return ;
					}
				}
			
			});
			
			//为额度申请model隐藏绑定事件
			$('#quota-modal-form').on('hidden.bs.modal', function () {
				
				// 隐藏“标记为不合格”层
				$('#div-limit-file').hide();
				// 清除div数据--lixj
				$('#quota-apply-rejected-notification').html('');
				$('#quota-apply-notification').html('');
				$('#quota-apply-rejected-notification').hide();
				$('#quota-apply-notification').hide();
				
				$('#quota-xamination_approval').hide();
				$('#quota-data-service').hide();
				$('#quota-select').hide();
				$('#quota-select').val('true');
				$('#quota_credit_limit').hide();
				$('#quota-select-back').hide();
				//清空验证内容
				validatorQuota.reset();
				validatorQuotaService.reset();
				//隐藏办理并发送按钮
				/* $('.quota-send').hide(); */
				$('#quota-modal-form select[name="quota_001"]').val('6');
				$('#quota-modal-form select[name="quota_002"]').val('4');
				$('#quota-modal-form select[name="quota_010"]').val('3');
				$('#quota-modal-form select[name="quota_011"]').val('2');
				$('#quota-modal-form select[name="quota_012"]').val('2');
				$('#quota-modal-form select[name="quota_013"]').val('2');
				$('#quota-modal-form select[name="quota_003"]').val('8');
				$('#quota-modal-form select[name="quota_004"]').val('8');
				$('#quota-modal-form select[name="quota_005"]').val('4');
				$('#quota-modal-form select[name="quota_006"]').val('5');
				$('#quota-modal-form select[name="quota_007"]').val('5');
				$('#quota-modal-form select[name="quota_008"]').val('3');
				$('#quota-modal-form select[name="quota_009"]').val('3');
				$('#quota-modal-form select[name="quota_014"]').val('2');
				$('#quota-modal-form select[name="quota_005"]').removeAttr('disabled');
				$('#quota-modal-form select[name="quota_009"]').removeAttr('disabled');
				$('#quota-modal-form select[name="quota_010"]').removeAttr('disabled');
				$('#quota-modal-form select[name="quota_011"]').removeAttr('disabled');
				$('#quota-modal-form select[name="quota_012"]').removeAttr('disabled');
				$('#quota-modal-form select[name="quota_013"]').removeAttr('disabled'); 
				$('#quota-modal-form select[name="quota_014"]').removeAttr('disabled'); 
				$('#quota-modal-form select[name="quota_015"]').removeAttr('disabled'); 
				
				$('#jlspCaoZuoLog').hide(); // 经理审批-操作记录的层
				
				$('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_reason').show();
			});
			
			//为现场开户model隐藏绑定事件
			$('#field-modal-form').on('hidden.bs.modal', function () {
				$('#<%=WorkFlowUtils.process_definition_key_field_account%>_reason').text('');
				validator.reset();
				$('#field-task-person').hide();
				$('#field-task-person-data').hide();
				$('#field-data-fill').hide();
				$('#business-archive').hide();
				$('#js-div-isPayServiceMoney').hide();
				$('#<%=WorkFlowUtils.process_definition_key_field_account%>_serviceChargeActual').val('');//清空实际收取服务费
				$('#<%=WorkFlowUtils.process_definition_key_field_account%>_archive').val('');//清空档案号
				$('#js-chk-isPayServiceMoney').removeAttr("checked");//清空是否缴纳服务费
				$('#<%=WorkFlowUtils.process_definition_key_field_account%>_isInvoicing').removeAttr("checked");//清空是否开具发票
				<%-- $('#<%=WorkFlowUtils.process_definition_key_field_account%>_dataFill').val('');//清空U盾码 --%>
				$('#<%=WorkFlowUtils.process_definition_key_field_account%>_contractNum').val('');//清空合同号
			});
			//为额度申请 提交按钮绑定时间
			$('.quota-submit').on('click',function(){
				//额度申请客服通知
				if($('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_node').val()=='<%=WorkFlowUtils.role_qutoa_service_call%>'){
					$('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_condition').val(true);
					
					// 额度申请客服通知验证--lixj
					

					/* if ($(this).hasClass("access-send"))
					{ */
					
					
					result = action.verifySendMsg();
					if (result == false)
					{
						return false;
					}

					if(!validatorQuotaService.valid()){
						return false;
					}
					/* } */
					
				}else if($('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_node').val()=='<%=WorkFlowUtils.role_xamination_approval%>'){
					// 审批节点
					$('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_condition').val($(this).val());
					if($(this).val()=='false' && $('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_reason').val()==''){
						 //弹出不能为空的备注
						//textAreaBoolean = true;
						action.alertNotNull('请输入失败原因');
						return false;
					}
				//人工打分
				}else if($('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_node').val()=='<%=WorkFlowUtils.role_manual_review%>'){
					if($(this).val()=='true'){
						$('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_condition').val('1');
					}else if($(this).val()=='false'){
						$('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_condition').val('2');
					}else if($(this).val()=='back'){
						$('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_condition').val('3');
					}
					
					if ($(this).val()=='back' && $('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_reason').val()=='') {
						//textAreaBoolean = true;
						action.alertNotNull('请输入驳回原因');
						return false;
					}
					
					// 提示未标记不合格的资料--lixj
					if ($(this).val() != 'true' && $('#div-limit-file').css('display') != 'none' && !$('#div-limit-file').find('a').hasClass('red'))
					{
						//  驳回或者不通过时判断是否有选择不合格的资料
						if ($(this).val()=='false')
						{
							if (!confirm("您并未标记哪个资料不合格，确认不通过吗？"))
							{
								return false;	
							}
						}
						if ($(this).val()=='back')
						{
							if (!confirm("您并未标记哪个资料不合格，确认驳回吗？"))
							{
								return false;	
							}
						}
					}
					
					// 提示有资料标记为不合格，是否确认通过
					if ($(this).val() == 'true' && $('#div-limit-file').css('display') != 'none' && $('#div-limit-file').find('a').hasClass('red'))
					{
						if (!confirm("您有标记为不合格的资料，确认通过吗？"))
						{
							return false;	
						}
					}
				}else if($('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_node').val()=='<%=WorkFlowUtils.role_approved_credit_limit%>'){
					//人工核准授信金额节点
					$('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_condition').val($(this).val());
					if($(this).val()=='false' && $('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_reason').val()==''){
						//弹出不能为空的备注
						//textAreaBoolean =true;
						action.alertNotNull('请输入失败原因');
						return false;
					}
					if($(this).val()=='true'){
						if(!validatorQuota.valid()){
							return false;
						}
					}
				}
				
				//经理审批环节
				if($('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_node').val()=='<%=WorkFlowUtils.quota_manager_approval_role%>'){
					$('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_condition').val($(this).val());
					if($(this).val()=='false' && $('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_reason').val()==''){
						//弹出不能为空的备注
						//textAreaBoolean = true;
						action.alertNotNull('请输入审批意见');
						return false;
					}
				}
				
				// 额度申请驳回通知
				if($('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_node').val()=='<%=WorkFlowUtils.quota_service_send_information_role%>'){
					$('#<%=WorkFlowUtils.process_definition_key_quota_apply%>_condition').val(true);
					
					// 额度申请驳回客服通知验证--lixj
					
					/* if ($(this).hasClass("access-send"))
					{ */
					result = action.verifySendMsg();
					if (result == false)
					{
						return false;
					}
					/* } */
				}
				
				/* 办理或者办理并发送--lixj */
				/* if ($(this).hasClass("quota-send")){
					$('#isSend').val('true');
				}
				else{
					$('#isSend').val('false');
				} */
				
				$('#quota-form').submit();
				tools.openLoading();
			
			});
	
			//为现场开户 提交按钮绑定时间
			$('.field-submit').on('click',function(){
				
				
				//现场任务分配
				if($('#<%=WorkFlowUtils.process_definition_key_field_account%>_node').val()=='<%=WorkFlowUtils.role_task_assign%>'){
					var chk_value =''; 
					$('input[name="task-person"]:checked').each(function(){ 
					 chk_value+=($(this).val()+","); 
					});
					$('#<%=WorkFlowUtils.process_definition_key_field_account%>_taskPerson').val(chk_value);
				}else if($('#<%=WorkFlowUtils.process_definition_key_field_account%>_node').val()=='<%=WorkFlowUtils.role_data_backfill%>'){
					if(!validator.valid()){
						 return false;	
					}
				}
				
				//现场开户没有驳回
				$('#<%=WorkFlowUtils.process_definition_key_field_account%>_condition').val(true);
				tools.openLoading();
				$('#field-form').submit();
			});
			
			
			$("tbody").delegate('.claim','click',function(){
				if (confirm("确定签收么？")) {
					location.href=$(this).attr('data-url');
				} else {
					return false;
				}
			});
			
			$(".modal-body").delegate('.select-all','click',function(){
				action.selectAllNullorReserve('print-detail','全选');
			});
			$(".modal-body").delegate('.select-all-not','click',function(){
				action.selectAllNullorReserve('print-detail','全不选');
			});
			
			
			/**
			*为待办任务列表流程跟踪图片绑定点击事件
			*
			*/
			 $("tbody").delegate('.process-definition', "click", function(){
				var dataUrl="?proInsId="+$(this).attr('data-urlid')+"&d="+new Date().getTime();
				$('#processdefinition-modal-title').html("流程跟踪图("+$(this).attr('data-data')+")");
				$('#processdefinition-picture').attr('src',contentPath+'/workflow/instanceDiagram/'+dataUrl);
				$('#picture-modal-form').modal('show');
			});
			
			$('#picture-modal-form').on('hidden.bs.modal', function () {
				$('#processdefinition-picture').attr('src','');
			});
			$("tbody").delegate('.print-data-detail', "click", function(){
			//$('.print-data-detail').on('click',function(){
				var busId = $(this).attr('data-buskey');
				$('#print-data-busId').val(busId);
				var url = contentPath+"/fwBusinessSxd/findFileTOPrint";
				var param = {};
				param.busId = busId;
				 $.ajax({
						url :url,
						type : 'POST',
						dataType : 'json',
						async:false,
						data :param,
						success : function(data) {
							if(data.status=='success'){
								var html = [];
								var list = data.data.list;
								html.push(' <button class="btn btn-sm btn-primary select-all" type="button"><i class="ace-icon fa fa-check"></i> 全选</button>');
								html.push(' <button class="btn btn-sm btn-primary select-all-not" type="button"><i class="ace-icon fa fa-check"></i> 取消全选</button>');
								for (var int = 0; int < list.length; int++) {
									html.push('<div class="form-group"><div class="checkbox" class="col-sm-12"><label>');
									html.push('<input name="print-detail" type="checkbox" class="ace" data-busId="'+list[int].business_Id+'" value="'+fileJson[list[int].FILE_TYPE].dictKey+'">');
									html.push('<span class="lbl"> '+fileJson[list[int].FILE_TYPE].dictValue0+'</span>');
									html.push('  &nbsp; <a class="down-load" data-busId="'+list[int].business_Id+'" data-busType="'+fileJson[list[int].FILE_TYPE].dictKey+'" >下载</a> &nbsp;')
									//html.push('<a class="down-load-detail" data-busId="'+list[int].business_Id+'" data-busType="'+fileJson[list[int].FILE_TYPE].dictKey+'">查看</a>')
									html.push('</label></div></div>');
								}
								
								var fieldInfo = data.data.fieldInfo;
								
								/* 联系人 */
								html.push('<div class="form-group"><div class="checkbox" class="col-sm-12"><label> 联系人:');
								html.push('<span class="lbl"> '+fieldInfo.contactName+'</span>');
								html.push('</label></div></div>');
								
								/* 联系人 */
								html.push('<div class="form-group"><div class="checkbox" class="col-sm-12"><label> 联系电话:');
								html.push('<span class="lbl"> '+fieldInfo.fieldTel+'</span>');
								html.push('</label></div></div>');
								
								/* 开户时间 */
								html.push('<div class="form-group"><div class="checkbox" class="col-sm-12"><label> 开户时间:');
								html.push('<span class="lbl"> '+fieldInfo.fieldTime+'</span>');
								html.push('</label></div></div>');
								
								/* 开户地点 */
								html.push('<div class="form-group"><div class="checkbox" class="col-sm-12"><label> 开户地点:');
								html.push('<span class="lbl"> '+fieldInfo.fieldAddr+'</span>');
								html.push('</label></div></div>');
								
								/* 备注信息 */
								html.push('<div class="form-group"><div class="checkbox" class="col-sm-12"><label> 备注信息:');
								html.push('<span class="lbl"> '+fieldInfo.content+'</span>');
								html.push('</label></div></div>');
								
								$('#print-body').html(html.join(''));
								$('#print-modal-form').modal('show');
							}
						},
						error : function() {
						}
			  		});
			});
			
			$('.print-submit').on('click',function(){
				var chk_value =''; 
				$('input[name="print-detail"]:checked').each(function(){ 
				 chk_value+=($(this).val()+","); 
				});
				if(chk_value==''){
					//进行验证
					tools.openCT({
                        title: '无下载文件',         // {String} required model title
                        text: '请选择需要下载的文件',   // {String} required model text
                        type: 'warning',
                        buttons: [              // {Array} required buttons, 可以有一个 button
		                            {
		                                text: '确定',     // {String} required button text
		                                fn: function () {                   // {Function} click function
		                                	return false;
		                                },
		                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
		                            }
 		                        ]
                    });
					
					return false;
				
				}
				action.downFile(chk_value);
			});
			
			
			/**
        	 * 点击"标记为不合格"(luy-)
        	 */
        	$("tbody").delegate('.js-do-nopass', "click", function(e){
                e.preventDefault();
                var busId = $('#businessIdForFlag').val();
                var infoName = $(this).attr("data-infoName");
                var infoKey = $(this).attr("data-infoKey");
                action.doUpdateNoPass(busId, infoName, infoKey,"js-caozuo-access");
            });
        	
        	/**
        	 * “已标记为不合格”的点击事件，将其再变回“标记为不合格”
        	 */
        	$("tbody").delegate('.js-do-pass', "click", function(e){
                e.preventDefault();
                var busId = $('#businessIdForFlag').val();
                var infoKey = $(this).attr("data-infoKey");
                var infoName = $(this).attr("data-infoName");
                action.doUpdatePass(busId, infoName, infoKey,"js-caozuo-access");
            });
        	
        	/**
        	 * 为“信息回填”层中的“是否缴纳服务费”的复选框绑定事件
        	 */
        	//$("tbody").delegate('#js-chk-isPayServiceMoney', "change", function(e){
        	$('#js-chk-isPayServiceMoney').on("click", function(e){
        		 if($("#js-chk-isPayServiceMoney").is(':checked')==true){
        			 $('#js-div-isPayServiceMoney').show();
        		 }else{
        			 $('#js-div-isPayServiceMoney').hide();
        		 }
        	});
			
		},
		
		/* 为下拉列表绑定事件--lixj */
		bindSelectChangeEvent:function()
		{

			/* 为邮件模版下拉列表绑定change事件--lixj */
			$('#emailTemplate').on("change", function(e){
			
				if ($(this).val() == '0')
				{
					// 自定义模版
					$('#emailTitle').val("");
					// $('.wangEditor-textarea p').html("");
					$('.wangEditor-textarea p').remove();
					editor.append('<p></p>');
				}
				else
				{
					for (var index = 0; index < emailTemplate.length;index++)
					{
						if (emailTemplate[index].tid == $(this).val())
						{
							$('#emailTitle').val(emailTemplate[index].title);
							$('.wangEditor-textarea p').remove();
							editor.append('<p>' + emailTemplate[index].content + '</p>');
							break;
						}
					}

				}
			});
			/* 为短信模版下拉列表绑定change事件--lixj */
			$('#smsTemplate').on("change", function(e){
				if ($(this).val() == '0')
				{
					// 自定义模版
					$('#smsContent').val("");
				}
				else
				{
					for (var index = 0; index < smsTemplate.length;index++)
					{
						if (smsTemplate[index].tid == $(this).val())
						{
							$('#smsContent').val(smsTemplate[index].content);
							break;
						}
					}

				}
			});
			/* 为站内信模版下拉列表绑定change事件--lixj */
			$('#messageTemplate').on("change", function(e){
				if ($(this).val() == '0')
				{
					// 自定义模版
					$('#messageTitle').val("");
					$('#messageContent').val("");
				}
				else
				{
					for (var index = 0; index < messageTemplate.length;index++)
					{
						if (messageTemplate[index].tid == $(this).val())
						{
							$('#messageTitle').val(messageTemplate[index].title);
							$('#messageContent').val(messageTemplate[index].content);
							break;
						}
					}

				}
			});
		}
	};
	bind.bindEvent();
	
	var action = {
			
				buildTrForDoFindComLog : function(data) {
					data = data.data.faOperationRecords;
					var html = [];
					var tbody = $('.fw-black-detail');
					
					if(data.length>0){
						for ( var int = 0; int < data.length; int++) {
							html.push(action.createTrForDoFindComLog(data[int],int+1));
						}
						tbody.html(html.join(''));
					}else{
						tbody.html('<tr><td class="c-align" colspan="4">无操作日志记录！</td></tr>');
					}
				},
				alertNotNull : function(text) {
					textAreaBoolean = true;
				
					tools.openCT({
                        title: '提示',         // {String} required model title
                        text: text,   // {String} required model text
                        type: 'warning',
                        buttons: [              // {Array} required buttons, 可以有一个 button
		                            {
		                                text: '确定',     // {String} required button text
		                                fn: function () {                   // {Function} click function
		                                	return false;
		                                },
		                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
		                            }
 		                        ]
                    });
					
					
				},
				createTrForDoFindComLog : function(data,count) {
					var tr = [];
					tr.push('<tr>');
						tr.push('<td class="c-align"> ');
						tr.push(count);
						tr.push('</td>');
						tr.push('<td class="c-align">');
						tr.push(data.checkNode);
						tr.push('</td>');
						tr.push('<td class="c-align">');
						tr.push(data.operName);
						tr.push('</td>');
						tr.push('<td class="c-align"> ');
						tr.push(data.operTime);
						tr.push('</td>');
						
						var operationStates = '${operationStates}';
						var operationState = JSON.parse(operationStates);

						var state = data.operState;
			  			var stateStr = "";
			  			if(state != ""){
			  				stateStr = operationState[state].dictValue0;
			  			}
						
					  	tr.push('<td class="c-align">');
						tr.push(stateStr);
						tr.push('</td>');
						tr.push('<td class="c-align">');
						tr.push(data.checkReason);
						tr.push('</td>');
					tr.push('</tr>');
					return tr.join('');
				},
			
				doFindComLog : function(busId){
					var url = contentPath + "/fwBusinessSxd/getCaoZuoLog";
					var param = {};
					/*if(submit){
						return false;
					}
					submit = true;*/
					param.busId = busId;// 业务Id
					
					$.ajax({
						url : url, 
						data : param,
						type : 'POST',
						dataType : 'json',
						async : false,
						success : function(data) {
							var sysUser = action.buildTrForDoFindComLog(data);
							//$('#activiti-modal-form').modal('show');
							/*submit = false;*/
						},
						error : function() {
							alert('服务器异常，访问失败！');
							/*submit = false;*/
						}
					});
				},
			
			    getBusinessBybusId:function(busId){
			    	var url = contentPath +"/fwBusinessSxd/getBusinessByBusId";
			  		var param = {};
			  		param.busId = busId;
			  		 $.ajax({
							url :url,
							data : param,
							type : 'POST',
							async:false,
							dataType : 'json',
							success : function(data) {
								var busData = data.data;
								$('#service-charge').text(busData.serviceCharge);
								$('#js-hidden-service-charge').val(busData.serviceCharge);
							},
							error : function() {
							}
				  		});
			    },
				selectAllNullorReserve: function(obj,type){
				    if(obj!=null&&obj!=""){
				        if(document.getElementsByName(obj)!=undefined&&document.getElementsByName(obj).length>0){    //getElementsByName函数的作用按名字查找对象，返回一个数组。
				            var userids = document.getElementsByName(obj);
				            if(type=="全选"){
				                for(var i=0;i<userids.length;i++){
				                    if(userids[i].checked == false){
				                        userids[i].checked = true;
				                    }
				                }
				            }else if(type=="全不选"){
				                for(var i=0;i<userids.length;i++){
				                    if(userids[i].checked == true){
				                        userids[i].checked = false;
				                    }
				                }
				            }
				        }
				    }
				},
				getDataCenterByBusId:function(busId){
			  		var url = contentPath +"/fwBusinessSxd/getDataCenterByBusId";
			  		var param = {};
			  		param.busId = busId;
			  		 $.ajax({
							url :url,
							data : param,
							type : 'POST',
							async:false,
							dataType : 'json',
							success : function(data) {
								var fieldData = data.data;
								$('#field-time').text(fieldData.fieldTime);
								$('#field-addr').text(fieldData.fieldAddr);
								$('#contract-name').text(fieldData.contractName);
								$('#contract-tel').text(fieldData.fieldTel);
								$('#field-remarks').text(fieldData.remarks);
							},
							error : function() {
							}
				  		});
			  	},
			  	getAdminByRole:function(){
			  		var url = contentPath +"/faAdmin/getAdminByRole";
			  		 $.ajax({
							url :url,
							type : 'POST',
							async:false,
							dataType : 'json',
							success : function(data) {
								var sysUser = data.data;
								var html = [];
								html.push('<label class="control-label bolder blue">任务分配 </label>');
								for (var int = 0; int < sysUser.length; int++) {
									html.push('<div class="checkbox">');
									html.push('<label>');
									html.push('<input type="checkbox"  class="ace" name="task-person" value="'+sysUser[int].userId+'"><span class="lbl">'+sysUser[int].realName+'</span>');
									html.push('</label>');
									html.push('</div>');
								
								}
								
								$('#field-task-person').html(html.join(''));
							},
							error : function() {
							}
				  		});
			  	},
			  	downFile:function(fileType){
			  		var busId = $('#print-data-busId').val();
			  		var param = {};
			  		param.busId = busId;
			  		param.fileType = fileType;
			  		var url = contentPath+"/fwBusinessSxd/printData";
			  		$.ajax({
						url :url,
						type : 'POST',
						data :param,
						dataType : 'json',
						success : function(data) {
							window.open(contentPath+'/upload'+data.data);
						},
						error : function() {
						}
			  		});
			  	},
			  	//转办查询所有人
			  	getAdminByRoleAll:function(node){
			  		var url = contentPath +"/faAdmin/getAdminByRoleALL";
			  		var param = {};
			  		param.node =node; 
			  		 $.ajax({
							url :url,
							type : 'POST',
							data :param,
							dataType : 'json',
							async : false,
							success : function(data) {
								var sysUser = data.data;
								var html = [];
								html.push('<select id="select-todo">');
								for (var int = 0; int < sysUser.length; int++) {
									if ('${user.userName}' != sysUser[int].userName) {
										html.push('<option value="'+sysUser[int].userId+'">'+sysUser[int].realName+'</option>');										
									}
								}
								html.push('</select>');
								$('#turn-todo').html(html.join(''));
							},
							error : function() {
							}
				  		});
			  	},
			  	
			  	
			  	/**
				 * 查询图片文件 (luy)
				 * @param callback
				 */
				getImgFileInfo : function(fileType) {
					var url = contentPath + "/fwBusinessSxd/applySxdFilesByType";
					var param = {};
					param.businessId = $('#upload-busId').val(); // 业务ID
					param.businessType = '01'; // 业务类型
					param.fileType = fileType; // 文件类型
					$.ajax({
						url : url,
						data : param,
						type : 'POST',
						dataType : 'json',
						success : function(data) {
							submit = false;
							if (data.status === "success") {
								$('#file-img-show').empty();
								var files = data.data.files;
								
								if (files.length <=0) {
		                        	var htmlimg = "";
		                            htmlimg = htmlimg + '<span>';
		                            htmlimg = htmlimg + '您还没有上传过该类型的图片！';
		                            htmlimg = htmlimg + '</span>';
		                            $('#file-img-show').append(htmlimg);
								}
								
								for(var i = 0;i < files.length ;i++){
									var htmlimg = "";
									htmlimg = htmlimg+'<li>';
									htmlimg = htmlimg+'<button class="js-delete-imgfile" type="button" id="cboxClose" fileId="'+files[i].fileId+'"  data-toggle="tooltip" data-placement="left" title="删除图片？">x</button>';
									htmlimg = htmlimg+'<a href=".downloadImg?id='+files[i].fileId+'" data-rel="colorbox" class="cboxElement">';
									htmlimg = htmlimg+'<img width="150" height="150" alt="150x150" src=".downloadImg?id='+files[i].fileId+'">';
									htmlimg = htmlimg+'</a>';
									htmlimg = htmlimg+'</li>';
									$('#file-img-show').append(htmlimg);
								}
								
								// 调用查看大图的方法
								action.showBigImg();
							} else {
								tools.openST({
									title : '提示', // {String}
									text : '对不起，获取信息出现了错误', // {String}
									type : 'fail' // {String}
								});
								
								
							}
						},
						error : function() {
							submit = false;
							tools.openST({
								title : '提示', // {String}
								text : '对不起，获取信息出现了错误', // {String}
								type : 'fail' // {String}
							});
							
							
						}
					});
				},
				
				/**
				 * 查看图片的大图
				 * @type {string}
				 */
		        showBigImg : function(){
					var $overflow = '';
					var colorbox_params = {
						rel: 'colorbox',
						reposition:true,
						scalePhotos:true,
						scrolling:false,
						close:'&times;',
						maxWidth:'100%',
						maxHeight:'100%',
						photo:true,
						current: '',
						previous: '<i class="glyphicon glyphicon-arrow-left"></i>',
						next:'<i class="glyphicon glyphicon-arrow-right"></i>',
						onOpen:function(){
							$overflow = document.body.style.overflow;
							document.body.style.overflow = 'hidden';
						},
						onClosed:function(){
							document.body.style.overflow = $overflow;
						},
						onComplete:function(){
							$.colorbox.resize();
						}
					};
					$('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);
		        },
			  	
			  	selectdisabled:function(busId){
			  		var url = contentPath +"/fwBusinessSxd/getCenterDataByBusId";
			  		var param = {};
			  		param.busId =busId; 
			  		 $.ajax({
							url :url,
							type : 'POST',
							data :param,
							dataType : 'json',
							success : function(data) {
								var centerData = data.data.faCenterdata;
								var fwBusinessSxd = data.data.fwBusinessSxd;
								var fwComPerBus = data.data.fwComPerBus;
								if(data.data!=null){
									//经营场所
									$('#quota-modal-form select[name="quota_005"]').val(fwBusinessSxd.premises);
									//户籍
									$('#quota-modal-form select[name="quota_009"]').val(fwComPerBus.domicilePlace);
									//学历
									$('#quota-modal-form select[name="quota_010"]').val(fwComPerBus.education);
									//年龄
									$('#quota-modal-form select[name="quota_011"]').val(centerData.age);
									//婚姻状况
									$('#quota-modal-form select[name="quota_012"]').val(fwComPerBus.maritalState);
									//黑名单
									$('#quota-modal-form select[name="quota_013"]').val(centerData.blackList);
									//犯罪情况
									$('#quota-modal-form select[name="quota_014"]').val(centerData.crime);
									//家庭净资产
									$('#quota-modal-form select[name="quota_015"]').val(fwComPerBus.householdAssets);
									$('#hide_quota_005').val(fwBusinessSxd.premises);
									$('#hide_quota_009').val(fwComPerBus.domicilePlace);
									$('#hide_quota_010').val(fwComPerBus.education);
									$('#hide_quota_011').val(centerData.age);
									$('#hide_quota_012').val(fwComPerBus.maritalState);
									$('#hide_quota_013').val(centerData.blackList);
									$('#hide_quota_014').val(centerData.crime);
									$('#hide_quota_015').val(fwComPerBus.householdAssets);
								}
								$('#quota-modal-form select[name="quota_005"]').attr('disabled',true);
								$('#quota-modal-form select[name="quota_009"]').attr('disabled',true);
								$('#quota-modal-form select[name="quota_010"]').attr('disabled',true);
								$('#quota-modal-form select[name="quota_011"]').attr('disabled',true);
								$('#quota-modal-form select[name="quota_012"]').attr('disabled',true);
								$('#quota-modal-form select[name="quota_013"]').attr('disabled',true);
								$('#quota-modal-form select[name="quota_014"]').attr('disabled',true);
								$('#quota-modal-form select[name="quota_015"]').attr('disabled',true);
							},
							error : function() {
							}
				  		});
			  	},
			  	
			  	/* 创建操作记录表--lixj */
			  	buildTr : function(data) {
					data = data.data.list;
					var html = [];
					var tbody = $('.fw-handle-record');
					
					if(data.length>0){
						for ( var int = 0; int < data.length; int++) {
							html.push(action.createTr(data[int]));
						}
						tbody.html(html.join(''));
					}else{
						tbody.html('<tr><td class="c-align" colspan="4">无处理记录！</td></tr>');
					}
				},
				createTr : function(data) {
					var tr = [];
					tr.push(' <tr> <td class="c-align">');
					tr.push(data.handleUser);
					tr.push('</td>');
					tr.push('<td class="c-align"> ');
					tr.push(data.handleTime);
					tr.push('</td>');
					tr.push('<td class="c-align">');
					tr.push(data.handleContent);
					tr.push('</td>');
					tr.push('</tr>');
					return tr.join('');
				},
				
				/*发送通知需要增加界面元素的方法--lixj*/
				appendTemplateDiv:function(divId){
 				    var html = [];
					html.push('<div>');
				    html.push('<div class="col-sm-12 form-group">');
                    html.push('<div class="checkbox">');
                    html.push('<label>');
                    html.push('<input type="checkbox" id="emailCheck" name="emailCheck" checked="checked" onclick="return false;"> 邮件');              
                    html.push('</label>');          
                    html.push('</div>');      
                    html.push('</div>');  

                    html.push(' <div class="col-sm-12 form-group">'); 
                    html.push('<span class="color-green">选择模板：</span>');      
                          
                    html.push('<select name="emailTemplate" id="emailTemplate" class="common-select">');       
                    html.push('</select>');      
                          
                          
                    html.push('</div>');
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<span class="pull-left margin-t">邮件标题：</span>');      
                    html.push(' <input type="text" id="emailTitle" name="emailTitle" class="col-sm-6" placeholder="邮件标题" max-length="200">');     
                    html.push('</div>');  
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<textarea id="emailContent" name="emailContent">');      

                    html.push('</textarea>');      
                    html.push('</div>');  
                    html.push('</div>');
                    html.push('<div>');
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<div class="checkbox">');     
                    html.push('<label>');          
                    html.push('<input type="checkbox" id="smsCheck" name="smsCheck"> 短信');              
                    html.push('</label>');          
                    html.push('</div>');      
                    html.push('</div>');  

                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<span class="color-green">选择模板：</span>');      
                          
                    html.push('<select id="smsTemplate" name="smsTemplate" class="common-select">');     
                    html.push('</select>');      
                          
                          
                    html.push('</div>');  
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<textarea class="col-sm-12" id="smsContent" name="smsContent" placeholder="短信内容">');      

                    html.push('</textarea>');      
                    html.push('</div>');  
                    html.push('</div>');
                    html.push('<div>');
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<div class="checkbox">');      
                    html.push('<label>');         
                    html.push('<input type="checkbox"  id="messageCheck" name="messageCheck"> 站内信');              
                    html.push('</label>');          
                    html.push('</div>');      
                    html.push('</div>'); 

                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<span class="color-green">选择模板：</span>');    
                          
                    html.push('<select id="messageTemplate" name="messageTemplate" class="common-select">');     
                    html.push('</select>');      
                          
                          
                    html.push('</div>');  
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push(' <span class="pull-left margin-t">站内信标题：</span>');     
                    html.push('<input type="text" id="messageTitle" name="messageTitle" class="col-sm-6" placeholder="站内信标题" max-length="255">');      
                    html.push('</div>');  
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<textarea class="col-sm-12" id="messageContent" name="messageContent"  placeholder="站内信内容">');      

                    html.push('</textarea>');      
                    html.push('</div>');  
                    html.push('</div>'); 
					$('#' + divId).html(html.join(''));
					
					editor = $('#emailContent').wangEditor();
					
					// 为下拉列表绑定事件
					bind.bindSelectChangeEvent();
				},
				
				/* 获取模版信息，填充信息--lixj */
				getTemplate:function(busId, type){
					var url = contentPath +"/fwBusinessSxd/accessVerify/getAccessVerifyTemplate";
			  		var param = {};
			  		param.busId =busId; 
			  		param.type = type;
			  		 $.ajax({
							url :url,
							type : 'POST',
							data :param,
							dataType : 'json',
							success : function(data) {
								// 额度申请结果
								
								quotaApplyResult = data.data.quotaApplyResult;
								accessVerifyResult = data.data.accessVerifyResult;
								
								// 额度申请成功或者失败显示隐藏开户时间和开户地点
								if (quotaApplyResult == 'fail')
								{
									$('#fieldTime').hide();
									$('#fieldAddr').hide();
									$('#quota-title').text('额度申请失败客服通知');
								}
								else if (quotaApplyResult == 'success')
								{
									$('#fieldTime').show();
									$('#fieldAddr').show();
									$('#quota-title').text('额度申请成功客服通知');
								}
								
								if (accessVerifyResult == 'fail')
								{
									$('#access-title').text('准入验证失败客服通知');
								}
								else if (accessVerifyResult == 'success')
								{
									$('#access-title').text('准入验证成功客服通知');
								}
								
								
								action.fillTemplateData(data);
							},
							error : function() {
							}
				  		});
				},
				
				/* 填充模版数据--lixj */
				fillTemplateData:function(data)
				{
					emailTemplate = data.data.emailTemplate;
					var selectElement = $('#emailTemplate');
					$("#emailTemplate option").remove();
					selectElement.append('<option value="0" selected="selected">自定义模版</option>');	
					for (var int = 0; int < emailTemplate.length; int++) {
						{
							if (emailTemplate[int].isDefault == 1)
							{
								selectElement.append('<option value="'+emailTemplate[int].tid+'" selected="selected">'+emailTemplate[int].tempName+'</option>');	
								$('#emailTitle').val(emailTemplate[int].title);
							
								// $('.wangEditor-textarea p').html( emailTemplate[int].content );
								$('.wangEditor-textarea p').remove();
								editor.append('<p>' + emailTemplate[int].content + '</p>');
							}
							else
							{
								selectElement.append('<option value="'+emailTemplate[int].tid+'">'+emailTemplate[int].tempName+'</option>');	
							}									
						}
					}
					
					smsTemplate = data.data.smsTemplate;
					selectElement = $('#smsTemplate');
					$("#smsTemplate option").remove();
					selectElement.append('<option value="0" selected="selected">自定义模版</option>');
					for (var int = 0; int < smsTemplate.length; int++) {
						{
							if (smsTemplate[int].isDefault == 1)
							{
								selectElement.append('<option value="'+smsTemplate[int].tid+'" selected="selected">'+smsTemplate[int].tempName+'</option>');
								$('#smsContent').val(smsTemplate[int].content);
							}
							else
							{
								selectElement.append('<option value="'+smsTemplate[int].tid+'">'+smsTemplate[int].tempName+'</option>');	
							}
																	
						}
					}
					
					messageTemplate = data.data.messageTemplate;
					selectElement = $('#messageTemplate');
					$("#messageTemplate option").remove();
					selectElement.append('<option value="0" selected="selected">自定义模版</option>');
					for (var int = 0; int < messageTemplate.length; int++) {
						{
							if (messageTemplate[int].isDefault == 1)
							{
								selectElement.append('<option value="'+messageTemplate[int].tid+'" selected="selected">'+messageTemplate[int].tempName+'</option>');	
								$('#messageTitle').val(messageTemplate[int].title);
								$('#messageContent').val(messageTemplate[int].content);
							}
							else
							{
								selectElement.append('<option value="'+messageTemplate[int].tid+'">'+messageTemplate[int].tempName+'</option>');	
							}
																	
						}
					}
				},
				// 验证邮件、短信和站内信是否为空
				verifySendMsg:function(){
					
					if ($('#emailCheck').prop('checked'))
					{
						if ($('#emailTitle').val() == '')
						{
							//textAreaBoolean = true;
							action.alertNotNull("请输入邮件标题");
							return false;
						}
						if ($('#emailTitle').val().length > 200)
						{
							//textAreaBoolean = true;
							action.alertNotNull("邮件标题不能输入超过200字符，请重新输入。");
							return false;
						}
					}
					
					if ($('#smsCheck').prop('checked'))
					{
						if ($('#smsContent').val() == '')
						{
							//textAreaBoolean = true;
							action.alertNotNull("请输入短信内容");
							return false;
						}
					}
					if ($('#messageCheck').prop('checked'))
					{	
						if ($('#messageTitle').val() == '')
						{
							//textAreaBoolean = true;
							action.alertNotNull("请输入站内信标题");
							return false;
						}
						if ($('#messageTitle').val().length > 255)
						{
							//textAreaBoolean = true;
							action.alertNotNull("站内信标题不能输入超过255字符，请重新输入。");
							return false;
						}
					}
					if ($('#emailCheck').prop('checked') && $('#messageCheck').prop('checked') && $('#emailContent').val() == '' && $('#messageContent').val() == '')
					{
						if (!confirm("邮件内容和站内信内容为空，您确认发送吗？")) {
							return false;
						} 
					}
					else if ($('#emailCheck').prop('checked') && $('#emailContent').val() == '')
					{
						if (!confirm("邮件内容为空，您确认发送吗？")) {						
							return false;
						} 
					}
					else if ($('#messageCheck').prop('checked') && $('#messageContent').val() == '')
					{
						if (!confirm("站内信内容为空，您确认发送吗？")) {
							return false;
						} 
					}
					
					return true;
				},
				
				/**
				* 经理审批-填操作记录
				*/
				buildTrForCaoZuoLog : function(data) {
					data = data.data.faOperationRecords;
					var html = [];
					var tbody = $('.fw-black-detail-for-caozuolog');
					
					if(data.length>0){
						for ( var int = 0; int < data.length; int++) {
							html.push(action.createTrForCaoZuoLog(data[int],int+1));
						}
						tbody.html(html.join(''));
					}else{
						tbody.html('<tr><td class="c-align" colspan="4">无操作日志记录！</td></tr>');
					}
				},
				/**
				* 经理审批-填操作记录
				*/
				createTrForCaoZuoLog : function(data,count) {
					var tr = [];
					tr.push('<tr>');
						tr.push('<td class="c-align"> ');
						tr.push(count);
						tr.push('</td>');
						tr.push('<td class="c-align">');
						tr.push(data.checkNode);
						tr.push('</td>');
						tr.push('<td class="c-align">');
						tr.push(data.operName);
						tr.push('</td>');
						tr.push('<td class="c-align"> ');
						tr.push(data.operTime);
						tr.push('</td>');
						
						var operationStates = '${operationStates}';
						var operationState = JSON.parse(operationStates);
						
			 			var state = data.operState;
			 			var stateStr = "";
			  			if(state != ""){
			  				stateStr = operationState[state].dictValue0;
			  			}
						
					  	tr.push('<td class="c-align">');
						tr.push(stateStr);
						tr.push('</td>');
						tr.push('<td class="c-align">');
						tr.push(data.checkReason);
						tr.push('</td>');
					tr.push('</tr>');
					return tr.join('');
				},
				/**
				* 经理审批-查询操作记录
				*/
				getCaoZuoLog : function(busId){
					var url = contentPath + "/fwBusinessSxd/getCaoZuoLog";
					var param = {};
					/*if(submit){
						return false;
					}
					submit = true;*/
					param.busId = busId;// 业务Id
					
					$.ajax({
						url : url, 
						data : param,
						type : 'POST',
						dataType : 'json',
						async : false,
						success : function(data) {
							var sysUser = action.buildTrForCaoZuoLog(data);
							$('#activiti-modal-form').modal('show');
							/*submit = false;*/
						},
						error : function() {
							alert('服务器异常，访问失败！');
							/*submit = false;*/
						}
					});
				},
				
				/**
				* 准入验证，查询需要审核的文件  
				*/
				buildTrForAccess : function(data) {
					
					data = data.data;
					var html = [];
					var tbody = $('.access-file-for-nopass');
					
					html.push(action.createTrForAccess(data));
					tbody.html(html.join(''));
				},
				/**
				* 准入验证，查询需要审核的文件  
				*/
				createTrForAccess : function(data) {
					var tr = [];
					tr.push('<tr>');
						tr.push('<td class="c-align">1</td> ');
						tr.push('<td class="c-align">营业执照注册号</td>');
						if(data.licenseNum_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'001'+'"><a class="js-do-pass red" data-infoName="营业执照注册号" data-infoKey="001" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'001'+'"><a class="js-do-nopass" data-infoName="营业执照注册号" data-infoKey="001" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
						
					tr.push('<tr>');
						tr.push('<td class="c-align">2</td> ');
						tr.push('<td class="c-align">组织机构代码</td>');
						if(data.catdCode_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'003'+'"><a class="js-do-pass red" data-infoName="组织机构代码" data-infoKey="003" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'003'+'"><a class="js-do-nopass" data-infoName="组织机构代码" data-infoKey="003" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">3</td> ');
						tr.push('<td class="c-align">税号</td>');
						if(data.taxNum_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'002'+'"><a class="js-do-pass red" data-infoName="税号" data-infoKey="002" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'002'+'"><a class="js-do-nopass" data-infoName="税号" data-infoKey="002" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">4</td> ');
						tr.push('<td class="c-align">企业营业执照</td>');
						if(data.businessLicense_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'02'+'"><a class="js-do-pass red" data-infoName="企业营业执照" data-infoKey="02" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'02'+'"><a class="js-do-nopass" data-infoName="企业营业执照" data-infoKey="02" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">5</td> ');
						tr.push('<td class="c-align">组织机构代码证</td>');
						if(data.organiztionCode_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'04'+'"><a class="js-do-pass red" data-infoName="组织机构代码证" data-infoKey="04" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'04'+'"><a class="js-do-nopass" data-infoName="组织机构代码证" data-infoKey="04" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">6</td> ');
						tr.push('<td class="c-align">税务登记证</td>');
						if(data.taxRegistartion_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'03'+'"><a class="js-do-pass red" data-infoName="税务登记证" data-infoKey="03" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'03'+'"><a class="js-do-nopass" data-infoName="税务登记证" data-infoKey="03" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">7</td> ');
						tr.push('<td class="c-align">近三年财务报表</td>');
						if(data.lastYearStatements_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'06'+'"><a class="js-do-pass red" data-infoName="近三年财务报表" data-infoKey="06" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'06'+'"><a class="js-do-nopass" data-infoName="近三年财务报表" data-infoKey="06" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">8</td> ');
						tr.push('<td class="c-align">涉税保密信息查询申请表</td>');
						if(data.taxRelatedQuery_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'01'+'"><a class="js-do-pass red" data-infoName="涉税保密信息查询申请表" data-infoKey="01" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'01'+'"><a class="js-do-nopass" data-infoName="涉税保密信息查询申请表" data-infoKey="01" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">9</td> ');
						tr.push('<td class="c-align">征信查询授权书</td>');
						if(data.companyCredit_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'05'+'"><a class="js-do-pass red" data-infoName="征信查询授权书" data-infoKey="05" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'05'+'"><a class="js-do-nopass" data-infoName="征信查询授权书" data-infoKey="05" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					/*
					tr.push('<tr>');
						tr.push('<td class="c-align">10</td> ');
						tr.push('<td class="c-align">法人征信查询授权书</td>');
						if(data.ledalCredit_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'10'+'"><a class="js-do-pass red" data-infoName="法人征信查询授权书" data-infoKey="10" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'10'+'"><a class="js-do-nopass" data-infoName="法人征信查询授权书" data-infoKey="10" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					*/
					
					tr.push('<tr>');
						tr.push('<td class="c-align">10</td> ');
						tr.push('<td class="c-align">法人证件照片</td>');
						if(data.ledalCard_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'07'+'"><a class="js-do-pass red" data-infoName="法人证件照片" data-infoKey="07" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'07'+'"><a class="js-do-nopass" data-infoName="法人证件照片" data-infoKey="07" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">11</td> ');
						tr.push('<td class="c-align">控制人及配偶征信报告查询授权书</td>');
						if(data.controllerCredit_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'11'+'"><a class="js-do-pass red" data-infoName="控制人及配偶征信报告查询授权书" data-infoKey="11" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'11'+'"><a class="js-do-nopass" data-infoName="控制人及配偶征信报告查询授权书" data-infoKey="11" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">12</td> ');
						tr.push('<td class="c-align">控制人证件照片</td>');
						if(data.controllerCard_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'08'+'"><a class="js-do-pass red" data-infoName="控制人证件照片" data-infoKey="08" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'08'+'"><a class="js-do-nopass" data-infoName="控制人证件照片" data-infoKey="08" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">13</td> ');
						tr.push('<td class="c-align">控制人配偶身份证照片</td>');
						if(data.controllerSpoCard.length<=0){
							tr.push('<td class="c-align js-caozuo-access'+'14'+'">无</td>');
						}else{
							if(data.controllerSpoCard_ispass==0){
								tr.push('<td class="c-align js-caozuo-access'+'09'+'"><a class="js-do-pass red" data-infoName="控制人配偶身份证照片" data-infoKey="09" href="#">已标记为不合格</a></td>');
							}else{
								tr.push('<td class="c-align js-caozuo-access'+'09'+'"><a class="js-do-nopass" data-infoName="控制人配偶身份证照片" data-infoKey="09" href="#">标记为不合格</a></td>');
							}
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">14</td> ');
						tr.push('<td class="c-align">控制人学历证明</td>');
						if(data.controllerEducation_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'30'+'"><a class="js-do-pass red" data-infoName="控制人学历证明" data-infoKey="30" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'30'+'"><a class="js-do-nopass" data-infoName="控制人学历证明" data-infoKey="30" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">15</td> ');
						tr.push('<td class="c-align">控制人户籍证明</td>');
						if(data.controllerDomicilePlace_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'31'+'"><a class="js-do-pass red" data-infoName="控制人户籍证明" data-infoKey="31" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'31'+'"><a class="js-do-nopass" data-infoName="控制人户籍证明" data-infoKey="31" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					
					return tr.join('');
				},
				/**
				* 准入验证，查询需要审核的文件 
				*/
				getFileForAccess : function(busId){
					var url = contentPath + "/fwBusinessSxd/getBusFileByBusId";
					var param = {};
					/*if(submit){
						return false;
					}
					submit = true;*/
					param.busId = busId;// 业务Id
					
					$.ajax({
						url : url, 
						data : param,
						type : 'POST',
						dataType : 'json',
						async : false,
						success : function(data) {
							var sysUser = action.buildTrForAccess(data);
							/*submit = false;*/
						},
						error : function() {
							alert('服务器异常，访问失败！');
							/*submit = false;*/
						}
					});
				},
				
				/**
				* 额度申请，查询需要审核的文件  
				*/
				buildTrForLimit : function(data) {
					
					data = data.data;
					var html = [];
					var tbody = $('.limit-file-for-nopass');
					
					html.push(action.createTrForLimit(data));
					tbody.html(html.join(''));
				},
				/**
				* 额度申请，查询需要审核的文件  
				*/
				createTrForLimit : function(data) {
					var tr = [];
					
					tr.push('<tr>');
						tr.push('<td class="c-align">1</td> ');
						tr.push('<td class="c-align">授信业务申请书</td>');
						if(data.creditBusinessApp_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'12'+'"><a class="js-do-pass red" data-infoName="授信业务申请书" data-infoKey="12" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'12'+'"><a class="js-do-nopass" data-infoName="授信业务申请书" data-infoKey="12" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">2</td> ');
						tr.push('<td class="c-align">公司简介</td>');
						if(data.companyProfile_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'36'+'"><a class="js-do-pass red" data-infoName="公司简介" data-infoKey="36" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'36'+'"><a class="js-do-nopass" data-infoName="公司简介" data-infoKey="36" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
						
					tr.push('<tr>');
						tr.push('<td class="c-align">3</td> ');
						tr.push('<td class="c-align">股东会或董事会决议</td>');
						if(data.directorsBoard_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'13'+'"><a class="js-do-pass red" data-infoName="股东会或董事会决议" data-infoKey="13" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'13'+'"><a class="js-do-nopass" data-infoName="股东会或董事会决议" data-infoKey="13" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					/*
					tr.push('<tr>');
						tr.push('<td class="c-align">3</td> ');
						tr.push('<td class="c-align">资质证书或经营许可证</td>');
						
						if(data.specicalIndustryCre.length<=0){
							tr.push('<td class="c-align js-caozuo-access'+'14'+'">无</td>');
						}else{
							if(data.specicalIndustryCre_ispass==0){
								tr.push('<td class="c-align js-caozuo-access'+'14'+'"><a class="js-do-pass red" data-infoName="资质证书或经营许可证" data-infoKey="14" href="#">已标记为不合格</a></td>');
							}else{
								tr.push('<td class="c-align js-caozuo-access'+'14'+'"><a class="js-do-nopass" data-infoName="资质证书或经营许可证" data-infoKey="14" href="#">标记为不合格</a></td>');
							}
						}
					tr.push('</tr>');
					*/
					
					tr.push('<tr>');
						tr.push('<td class="c-align">4</td> ');
						tr.push('<td class="c-align">公司章程、验资报告</td>');
						if(data.capitalVerification_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'15'+'"><a class="js-do-pass red" data-infoName="公司章程、验资报告" data-infoKey="15" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'15'+'"><a class="js-do-nopass" data-infoName="公司章程、验资报告" data-infoKey="15" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">5</td> ');
						tr.push('<td class="c-align">购销合同</td>');
						if(data.purchaseSaleContract_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'18'+'"><a class="js-do-pass red" data-infoName="购销合同" data-infoKey="18" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'18'+'"><a class="js-do-nopass" data-infoName="购销合同" data-infoKey="18" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">6</td> ');
						tr.push('<td class="c-align">个人所得税</td>');
						if(data.theIndividualIncomeTax_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'38'+'"><a class="js-do-pass red" data-infoName="个人所得税" data-infoKey="38" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'38'+'"><a class="js-do-nopass" data-infoName="个人所得税" data-infoKey="38" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">7</td> ');
						tr.push('<td class="c-align">国地税纳税证明</td>');
						if(data.lastYearThidYearAnnualLandTaxCertificate_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'37'+'"><a class="js-do-pass red" data-infoName="国地税纳税证明" data-infoKey="37" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'37'+'"><a class="js-do-nopass" data-infoName="国地税纳税证明" data-infoKey="37" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					/*
					tr.push('<tr>');
						tr.push('<td class="c-align">6</td> ');
						tr.push('<td class="c-align">客户清单</td>');
						if(data.customerList_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'19'+'"><a class="js-do-pass red" data-infoName="客户清单" data-infoKey="19" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'19'+'"><a class="js-do-nopass" data-infoName="客户清单" data-infoKey="19" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					*/
					
					/*
					tr.push('<tr>');
						tr.push('<td class="c-align">7</td> ');
						tr.push('<td class="c-align">税务报表</td>');
						if(data.lastTaxStatements_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'20'+'"><a class="js-do-pass red" data-infoName="税务报表" data-infoKey="20" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'20'+'"><a class="js-do-nopass" data-infoName="税务报表" data-infoKey="20" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					*/
					
					tr.push('<tr>');
						tr.push('<td class="c-align">8</td> ');
						tr.push('<td class="c-align">银行对账单</td>');
						if(data.lastCompanyStatements_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'21'+'"><a class="js-do-pass red" data-infoName="银行对账单" data-infoKey="21" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'21'+'"><a class="js-do-nopass" data-infoName="银行对账单" data-infoKey="21" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					/*
					tr.push('<tr>');
						tr.push('<td class="c-align">9</td> ');
						tr.push('<td class="c-align">个人银行对账单</td>');
						
						if(data.lastControllerStatements.length<=0){
							tr.push('<td class="c-align js-caozuo-access'+'22'+'">无</td>');
						}else{
							if(data.lastControllerStatements_ispass==0){
								tr.push('<td class="c-align js-caozuo-access'+'22'+'"><a class="js-do-pass red" data-infoName="个人银行对账单" data-infoKey="22" href="#">已标记为不合格</a></td>');
							}else{
								tr.push('<td class="c-align js-caozuo-access'+'22'+'"><a class="js-do-nopass" data-infoName="个人银行对账单" data-infoKey="22" href="#">标记为不合格</a></td>');
							}
						}
					tr.push('</tr>');
					*/
					
					/*
					tr.push('<tr>');
						tr.push('<td class="c-align">10</td> ');
						tr.push('<td class="c-align">纳税凭证和申请表</td>');
						if(data.lastTaxCerifcate_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'23'+'"><a class="js-do-pass red" data-infoName="纳税凭证和申请表" data-infoKey="23" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'23'+'"><a class="js-do-nopass" data-infoName="纳税凭证和申请表" data-infoKey="23" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					*/
					
					tr.push('<tr>');
						tr.push('<td class="c-align">9</td> ');
						tr.push('<td class="c-align">缴费凭证</td>');
						if(data.lastPaymentVoucher.length<=0){
							tr.push('<td class="c-align js-caozuo-access'+'24'+'">无</td>');
						}else{
							if(data.lastPaymentVoucher_ispass==0){
								tr.push('<td class="c-align js-caozuo-access'+'24'+'"><a class="js-do-pass red" data-infoName="缴费凭证" data-infoKey="24" href="#">已标记为不合格</a></td>');
							}else{
								tr.push('<td class="c-align js-caozuo-access'+'24'+'"><a class="js-do-nopass" data-infoName="缴费凭证" data-infoKey="24" href="#">标记为不合格</a></td>');
							}
						}
					tr.push('</tr>');
					
					/*
					tr.push('<tr>');
						tr.push('<td class="c-align">12</td> ');
						tr.push('<td class="c-align">资信和技术产品证书</td>');
						if(data.creditTechnology.length<=0){
							tr.push('<td class="c-align js-caozuo-access'+'14'+'">无</td>');
						}else{
							if(data.creditTechnology_ispass==0){
								tr.push('<td class="c-align js-caozuo-access'+'25'+'"><a class="js-do-pass red" data-infoName="资信和技术产品证书" data-infoKey="25" href="#">已标记为不合格</a></td>');
							}else{
								tr.push('<td class="c-align js-caozuo-access'+'25'+'"><a class="js-do-nopass" data-infoName="资信和技术产品证书" data-infoKey="25" href="#">标记为不合格</a></td>');
							}
						}
					tr.push('</tr>');
					*/
					
					tr.push('<tr>');
						tr.push('<td class="c-align">10</td> ');
						tr.push('<td class="c-align">场地证明</td>');
						if(data.propertyRightCard_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'26'+'"><a class="js-do-pass red" data-infoName="场地证明" data-infoKey="26" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'26'+'"><a class="js-do-nopass" data-infoName="场地证明" data-infoKey="26" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">11</td> ');
						tr.push('<td class="c-align">资产负债申请表</td>');
						if(data.controllerAssetsLiaApply_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'29'+'"><a class="js-do-pass red" data-infoName="资产负债申请表" data-infoKey="29" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'29'+'"><a class="js-do-nopass" data-infoName="资产负债申请表" data-infoKey="29" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">12</td> ');
						tr.push('<td class="c-align">婚姻状况证明</td>');
						if(data.marriageLicense_ispass==0){
							tr.push('<td class="c-align js-caozuo-access'+'27'+'"><a class="js-do-pass red" data-infoName="婚姻状况证明" data-infoKey="27" href="#">已标记为不合格</a></td>');
						}else{
							tr.push('<td class="c-align js-caozuo-access'+'27'+'"><a class="js-do-nopass" data-infoName="婚姻状况证明" data-infoKey="27" href="#">标记为不合格</a></td>');
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">13</td> ');
						tr.push('<td class="c-align">配偶的担保承诺书</td>');
						if(data.controllerSpouseGuarantee.length<=0){
							tr.push('<td class="c-align js-caozuo-access'+'28'+'">无</td>');
						}else{
							if(data.controllerSpouseGuarantee_ispass==0){
								tr.push('<td class="c-align js-caozuo-access'+'28'+'"><a class="js-do-pass red" data-infoName="配偶的担保承诺书" data-infoKey="28" href="#">已标记为不合格</a></td>');
							}else{
								tr.push('<td class="c-align js-caozuo-access'+'28'+'"><a class="js-do-nopass" data-infoName="配偶的担保承诺书" data-infoKey="28" href="#">标记为不合格</a></td>');
							}
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">14</td> ');
						tr.push('<td class="c-align">配偶的担保确认书</td>');
						if(data.confirmation.length<=0){
							tr.push('<td class="c-align js-caozuo-access'+'42'+'">无</td>');
						}else{
							if(data.confirmation_ispass==0){
								tr.push('<td class="c-align js-caozuo-access'+'42'+'"><a class="js-do-pass red" data-infoName="配偶的担保确认书" data-infoKey="42" href="#">已标记为不合格</a></td>');
							}else{
								tr.push('<td class="c-align js-caozuo-access'+'42'+'"><a class="js-do-nopass" data-infoName="配偶的担保确认书" data-infoKey="42" href="#">标记为不合格</a></td>');
							}
						}
					tr.push('</tr>');
					
					tr.push('<tr>');
						tr.push('<td class="c-align">15</td> ');
						tr.push('<td class="c-align">控制人及其家庭房产证明</td>');
						if(data.housePropertyCard.length<=0){
							tr.push('<td class="c-align js-caozuo-access'+'43'+'">无</td>');
						}else{
							if(data.housePropertyCard_ispass==0){
								tr.push('<td class="c-align js-caozuo-access'+'43'+'"><a class="js-do-pass red" data-infoName="控制人及其家庭房产证明" data-infoKey="43" href="#">已标记为不合格</a></td>');
							}else{
								tr.push('<td class="c-align js-caozuo-access'+'43'+'"><a class="js-do-nopass" data-infoName="控制人及其家庭房产证明" data-infoKey="43" href="#">标记为不合格</a></td>');
							}
						}
					tr.push('</tr>');
					
					
					return tr.join('');
				},
				/**
				* 额度申请，查询需要审核的文件 
				*/
				getFileForLimit : function(busId){
					var url = contentPath + "/fwBusinessSxd/getBusFileByBusId";
					var param = {};
					/*if(submit){
						return false;
					}
					submit = true;*/
					param.busId = busId;// 业务Id
					
					$.ajax({
						url : url, 
						data : param,
						type : 'POST',
						dataType : 'json',
						async : false,
						success : function(data) {
							var sysUser = action.buildTrForLimit(data);
							/*submit = false;*/
						},
						error : function() {
							alert('服务器异常，访问失败！');
							/*submit = false;*/
						}
					});
				},
				
				doUpdatePass : function(busId, infoName, infoKey,updateHtmlClass){
					var url = contentPath + "/fwBusinessSxd/doUpdateNoPassToPass";
		            var param = {};
		            param.busId = busId; //业务id
		            param.infoKey = infoKey; // 资料key

		            $.ajax({
		                url: url,
		                data: param,
		                type: 'POST',
		                dataType: 'json',
		                async : false,
		                success: function (data) {
		                    submit = false;
		                    if (data.status === "success") {
//		                    	window.location.reload();//刷新当前页面.
//		                    	alert("标记成功！");
		                    	tools.openCT({
			                        title: '提示',         // {String} required model title
			                        text: '标记成功',   // {String} required model text
			                        type: 'success',        // {String} required 取值 success， fail， warning， default success
			                        buttons: [              // {Array} required buttons, 可以有一个 button
			                            {
			                                text: '确定',     // {String} required button text
			                                fn: function () {                   // {Function} click function
//			                                	location.href=contentPath+"/workflow/list";
			                                	//window.location.reload();//刷新当前页面
			                                	$('.'+updateHtmlClass+infoKey).empty();
			                                	$('.'+updateHtmlClass+infoKey).html('<a class="js-do-nopass" data-infoName="'+infoName+'" data-infoKey="'+infoKey+'" href="#">标记为不合格</a>');
			                                },
			                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
			                            }
			                        ]
			                    });
		                    	$('.'+updateHtmlClass+infoKey).empty();
                            	$('.'+updateHtmlClass+infoKey).html('<a class="js-do-nopass" data-infoName="'+infoName+'" data-infoKey="'+infoKey+'" href="#">标记为不合格</a>');
		                    }
		                    else {
//		                    	alert("标记失败！");
		                    	tools.openCT({
			                        title: '提示',         // {String} required model title
			                        text: '标记失败！',   // {String} required model text
			                        type: 'fail',        // {String} required 取值 success， fail， warning， default success
			                        buttons: [              // {Array} required buttons, 可以有一个 button
			                            {
			                                text: '确定',     // {String} required button text
			                                fn: function () {                   // {Function} click function
//			                                	location.href=contentPath+"/workflow/list";
			                                },
			                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
			                            }
			                        ]
			                    });
		                    }
		                },
		                error: function (data) {
		                    submit = false;
//		                    alert("标记出现异常！");
		                    tools.openCT({
		                        title: '提示',         // {String} required model title
		                        text: '标记出现异常',   // {String} required model text
		                        type: 'warning',        // {String} required 取值 success， fail， warning， default success
		                        buttons: [              // {Array} required buttons, 可以有一个 button
		                            {
		                                text: '确定',     // {String} required button text
		                                fn: function () {                   // {Function} click function
//		                                	location.href=contentPath+"/workflow/list";
		                                },
		                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
		                            }
		                        ]
		                    });
		                }
		            });
				},
		    	/**
		    	 * 将资料“标记为不合格”
		    	 * @param busId 业务id
		    	 * @param infoName 资料名称
		    	 * @param infoKey 资料key
		    	 */
				doUpdateNoPass : function(busId, infoName, infoKey,updateHtmlClass){
					var url = contentPath + "/fwBusinessSxd/doUpdateNoPass";
		            var param = {};
		            param.busId = busId; //业务id
		            param.infoName = infoName; // 资料名称
		            param.infoKey = infoKey; // 资料key

		            $.ajax({
		                url: url,
		                data: param,
		                type: 'POST',
		                dataType: 'json',
		                async : false,
		                success: function (data) {
		                    submit = false;
		                    if (data.status === "success") {
//		                    	window.location.reload();//刷新当前页面.
//		                    	alert("标记成功！");
		                    	tools.openCT({
			                        title: '提示',         // {String} required model title
			                        text: '标记成功',   // {String} required model text
			                        type: 'success',        // {String} required 取值 success， fail， warning， default success
			                        buttons: [              // {Array} required buttons, 可以有一个 button
			                            {
			                                text: '确定',     // {String} required button text
			                                fn: function () {                   // {Function} click function
//			                                	location.href=contentPath+"/workflow/list";
			                                	//window.location.reload();//刷新当前页面
			                                	//$('.'+updateHtmlClass+infoKey).empty();
			                                	//$('.'+updateHtmlClass+infoKey).html('<a class="js-do-pass red" data-infoName="'+infoName+'" data-infoKey="'+infoKey+'" href="#">已标记为不合格</a>');
			                                },
			                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
			                            }
			                        ]
			                    });
		                    	
		                    	 $('.'+updateHtmlClass+infoKey).empty();
	                             $('.'+updateHtmlClass+infoKey).html('<a class="js-do-pass red" data-infoName="'+infoName+'" data-infoKey="'+infoKey+'" href="#">已标记为不合格</a>');
		                    }
		                    else {
//		                    	alert("标记失败！");
		                    	tools.openCT({
			                        title: '提示',         // {String} required model title
			                        text: '标记失败！',   // {String} required model text
			                        type: 'fail',        // {String} required 取值 success， fail， warning， default success
			                        buttons: [              // {Array} required buttons, 可以有一个 button
			                            {
			                                text: '确定',     // {String} required button text
			                                fn: function () {                   // {Function} click function
//			                                	location.href=contentPath+"/workflow/list";
			                                },
			                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
			                            }
			                        ]
			                    });
		                    }
		                },
		                error: function (data) {
		                    submit = false;
//		                    alert("标记出现异常！");
		                    tools.openCT({
		                        title: '提示',         // {String} required model title
		                        text: '标记出现异常',   // {String} required model text
		                        type: 'warning',        // {String} required 取值 success， fail， warning， default success
		                        buttons: [              // {Array} required buttons, 可以有一个 button
		                            {
		                                text: '确定',     // {String} required button text
		                                fn: function () {                   // {Function} click function
//		                                	location.href=contentPath+"/workflow/list";
		                                },
		                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
		                            }
		                        ]
		                    });
		                }
		            });
				},
		}; 
	
	
	
	
});
</script>