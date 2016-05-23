<%@page import="com.evan.finance.admin.utils.WorkFlowUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<!-- 准入验证-人工准入验证-点击“办理”按钮 -->
<div id="modal-notification" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="access-title">通知</h4>
			</div>
			<!-- luy- -->
			<input type="hidden" id="businessIdForFlag" >

			<div class="modal-body">
				<div class="container-fluid">
			    <form class="form-horizontal" id="frm_notification" method="post">
			    	
					<div id="customer-service-notify">
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
                             <textarea class="col-sm-12" rows="5" placeholder="备注" id="access_role_manual_verify_reason" name="access_role_manual_verify_reason"></textarea>
                        </div>
					
					</form>
				</div>
			</div>

			<div class="modal-footer">
				<!-- <button id="access-role-manual-verify-rejected" class="btn btn-sm access-submit access-bohui" data-dismiss="modal" id="access-select-back" value="back"><i class="ace-icon fa fa-times"></i>驳回</button> -->
				<!-- <button id="access-role-manual-verify-nopass" class="btn btn-sm access-submit access-nopass" data-dismiss="modal" id="access-select" value="false"><i class="ace-icon fa fa-times"></i>不通过</button> -->
				<!-- <button class="btn btn-sm btn-primary access-submit access-send" type="submit" value="true" style="display:none;"><i class="ace-icon fa fa-check"></i> 办理并发送</button> -->
			    <button id="notification-handling" class="btn btn-sm btn-primary" type="submit" value="true"><i class="ace-icon fa fa-check"></i> 办理</button>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->



