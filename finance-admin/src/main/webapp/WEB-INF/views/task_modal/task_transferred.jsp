<%@page import="com.evan.finance.admin.utils.WorkFlowUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ taglib prefix="security" uri="http://www.evan.jaron.com/tags/security"%>
<security:securityUser var="user" />

<!--任务转办model  -->
<form class="form-horizontal" id="frm_main" method="post">
	<div class="modal-dialog" >
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">任务转办</h4>
			</div>

			<div class="modal-body">
				<div class="form-group" id="turn-todo">
					<label class="col-sm-6 control-label no-padding-right" style="text-align:right;"> 将任务转给其他人 </label>
					<div class="col-sm-6" style="text-align:left;" id="turn-todo">
					</div>
				</div>
				<br />
			</div>
			
			<div class="modal-footer">
				<button class="btn btn-sm access-submit" data-dismiss="modal" type="button"><i class="ace-icon fa fa-times"></i>关闭</button>
				<button id="handling" class="btn btn-sm btn-primary access-submit" type="button" value="true"><i class="ace-icon fa fa-check"></i>办理</button>
			</div>
		</div>
	</div>
	
	<input type="hidden" name="businessId" id ="businessId" value="${businessId}"/> <!-- 业务ID -->
    <input type="hidden" name="taskId" id ="taskId" value="${taskId}"/> <!-- 任务编号 -->
    <input type="hidden" name="taskDefKey" id ="taskDefKey" value="${taskDefKey}"/> <!-- 任务定义Key -->
    <input type="hidden" name="loginUserName" id ="loginUserName" value="${user.userName}"/> <!-- 任务定义Key -->
</form>

<script src="${contentPath }/js/task_modal/task_reansferred.js"></script>
