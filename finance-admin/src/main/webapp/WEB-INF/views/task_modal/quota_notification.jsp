<%@page import="com.evan.finance.admin.utils.WorkFlowUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<link rel="stylesheet" type="text/css" href="${contentPath}/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
<style>
</style>

<!-- 额度申请-发送申请成功通知，并确定开户时间、地点 -->
<form class="form-horizontal" id="frm_main" method="post">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="access-title">客服通知</h4>
			</div>
			<input type="hidden" id="businessIdForFlag" >

			<div class="modal-body">
				<div class="container-fluid">
						<div id="quota-apply-notification">
							<!-- 额度申请客服通知--lixj -->
						</div>
						<div id="quota-data-service">
							<div class="col-sm-12 form-group" id="fieldTime">
								<span class="pull-left margin-t">开户时间：</span> 
								<input class="col-sm-6" type="text" name="loanNew_fieldTime" id="loanNew_fieldTime" placeholder="开户时间" readonly="true" />
							</div>
	
							<div class="col-sm-12 form-group" id="fieldAddr">
								<span class="pull-left margin-t">开户地点：</span> 
								<input class="col-sm-6 " type="text" name="loanNew_fieldAddr" id="loanNew_fieldAddr" placeholder="开户地点" />
							</div>
						</div>
	
						<!-- 是否办理并发送 -->
						<input type="hidden" id="isSend" name="isSend" value="true">
						
						<div class="col-sm-12 form-group">
	                       	<textarea class="col-sm-12" rows="5" placeholder="备注" id="comment" name="comment" maxlength="150"></textarea>
	                    </div>
					
					
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm access-submit" data-dismiss="modal" type="button"><i class="ace-icon fa fa-times"></i>关闭</button>
			    <button id="handling" class="btn btn-sm btn-primary" type="button" value="true"><i class="ace-icon fa fa-check"></i> 办理</button>
			</div>
		</div>
	</div>
	
	
	<input type="hidden" name="businessId" id ="businessId" value="${businessId}"/> <!-- 业务ID -->
    <input type="hidden" name="taskId" id ="taskId" value="${taskId}"/> <!-- 任务编号 -->
    <input type="hidden" name="taskDefKey" id ="taskDefKey" value="${taskDefKey}"/> <!-- 任务定义Key -->
    <input type="hidden" name="procInsId" id ="procInsId" value="${procInsId}"/> <!-- 流程实例id -->
	
</form>


<link rel="stylesheet" type="text/css" href="${contentPath}/plugins/editor/css/wangEditor-1.3.11.css">
<script type="text/javascript" src='${contentPath}/plugins/editor/js/wangEditor-1.3.11.js'></script>
<script type="text/javascript" src='${contentPath}/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js'></script>
<script type="text/javascript" src='${contentPath}/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.zh-CN.js'></script>
<script src="${contentPath }/js/task_modal/modal_common.js"></script> <!-- modal的公共方法 -->
<script src="${contentPath }/js/task_modal/common_notification.js"></script> <!-- 关于通知的公共方法 -->

<script src="${contentPath }/js/task_modal/quota_notification.js"></script>


