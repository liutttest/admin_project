<%@page import="com.evan.finance.admin.utils.WorkFlowUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<dict:loadDictList var="reasons" type="ACCCESS_FAILED_REASON"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<div class="main-content">
	<!-- #section:basics/content.breadcrumbs -->
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
		</script>

		<ul class="breadcrumb">
			<li>
				<i class="menu-icon fa fa-desktop home-icon"></i>
				系统任务
			</li>
			<!-- <li class="active">待办任务</li> -->
		</ul><!-- /.breadcrumb -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
	<form>
		<div style="margin-bottom:10px;margin-right: 20px;float: right;">
			<span>任务状态：</span>
			<select id="suspension_state">
				<option value="0" selected="selected">全部</option>
				<option value="1" >正常</option>
				<option value="2" >挂起</option>
			</select>
		</div>
	</form>
	<div class="row">
	<div class="col-xs-12">
	<div>
		<table id="sys-task" class="table table-striped table-bordered table-hover">
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
						<a class="green flow-apply-detail" target="_blank" data-href="${contentPath }/sysAdmin/task/getfwApplyBusDetail?busId=${workflow.businessKey}" >
							查看资料  <!-- <i class="ace-icon fa fa-university bigger-130"></i> -->
						</a>
					</td>
				</tr>
			</c:forEach> --%>
			</tbody>
		</table>
		</div>
		</div>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->

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

<%-- <jsp:include page="/WEB-INF/views/task/apply_detail.jsp" /> --%>
<%-- <jsp:include page="list_modal.jsp" /> --%>
<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath}/plugins/formatCurrency.js"></script>
<script src="${contentPath}/scripts/jquery.cookie.js"></script>
<script src="${contentPath }/js/task/sys_admin_list.js"></script>
<!-- <script type="text/javascript">


</script> -->