<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
	<div style="margin-bottom:10px;margin-right: 20px;float: right;">
		<span>任务状态：</span>
		<select id="suspension_state">
			<option value="0" selected="selected">全部</option>
			<option value="1" >正常</option>
			<option value="2" >挂起</option>
		</select>
	</div>
	<div class="row">
	<div class="col-xs-12 tabbable">
        <ul class="nav nav-tabs">
          <li role="presentation"><a class="day-tab" href="${contentPath}/activitiSxd/getActivitiList">税信贷</a></li>
          <li role="presentation" class="active"><a class="day-tab" href="${contentPath}/activitiSxd/getActivitiList/toGetLoanList">放款</a></li>
          <li role="presentation"><a class="day-tab" href="${contentPath}/activitiSxd/getActivitiList/toGetQuickLoanList">快速贷款</a></li>
        </ul>
	<div class="col-xs-12 tab-content">
	<div>
		<table id="table2" class="table table-striped table-bordered table-hover">
			<thead>
			<tr>
				<th class="c-align col-sm-1">企业名称</th>
				<th class="c-align col-sm-1">当前状态</th>
				<th class="c-align col-sm-1">流程跟踪</th>
				<th class="c-align col-sm-1">
				<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>任务时间
				</th>
				<th class="c-align col-sm-1ssss">操作</th>
			</tr>
			</thead>

			<tbody>
			<%-- <c:forEach items="${workflows }" var="workflow">
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
			</c:forEach> --%>
			</tbody>
		</table>
		</div>
		</div>
		</div>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->

<!--现场开户办理model  -->
<div id="public-modal-form" class="modal" tabindex="-1" data-backdrop="static">
</div>


<script src='${contentPath}/plugins/jsTemp/tmpl.js' type='text/javascript'></script>
<script src="${contentPath}/plugins/color-box/jquery.colorbox-min.js"></script>
<script src="${contentPath}/scripts/jquery.cookie.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath }/js/task_modal/listLoan.js"></script>
<script src="${contentPath }/js/task_modal/modal_common.js"></script> <!-- modal的公共方法 -->
<script src="${contentPath }/js/task_modal/common_notification.js"></script> <!-- 关于通知的公共方法 -->
