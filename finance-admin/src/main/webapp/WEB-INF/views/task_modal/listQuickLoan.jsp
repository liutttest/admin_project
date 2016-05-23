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
          <li role="presentation"><a class="day-tab" href="${contentPath}/activitiSxd/getActivitiList/toGetLoanList">放款</a></li>
          <li role="presentation" class="active"><a class="day-tab" href="${contentPath}/activitiSxd/getActivitiList/toGetQuickLoanList">快速贷款</a></li>
        </ul>
	<div class="col-xs-12 tab-content">
	<div>
		<table id="table2" class="table table-striped table-bordered table-hover">
			<thead>
			<tr>
				<th class="c-align">企业名称</th>
				<!-- <th class="c-align">当前状态</th> -->
				<th class="c-align">联系电话</th>
				<th class="c-align">贷款联系人</th>
				<th class="c-align">
				<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>申请时间
				</th>
				<th class="c-align">操作</th>
			</tr>
			</thead>

			<tbody>
			
			</tbody>
		</table>
		</div>
		</div>
		</div>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->

<!--办理model  -->
<div id="public-modal-form" class="modal" tabindex="-1" data-backdrop="static">
</div>


<script src='${contentPath}/plugins/jsTemp/tmpl.js' type='text/javascript'></script>
<script src="${contentPath}/scripts/jquery.cookie.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath }/js/task_modal/listQuickLoan.js"></script>
<script src="${contentPath }/js/task_modal/modal_common.js"></script> <!-- modal的公共方法 -->
<script src="${contentPath }/js/task_modal/common_notification.js"></script> <!-- 关于通知的公共方法 -->
