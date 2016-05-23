<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!-- /section:basics/sidebar -->
<div class="main-content">
	<!-- #section:basics/content.breadcrumbs -->
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
		</script>

		<ul class="breadcrumb">
			<li>
				<i class="ace-icon fa fa-home home-icon"></i>
				<a href="#">Home</a>
			</li>
			<li class="active">Dashboard</li>
		</ul><!-- /.breadcrumb -->

		<!-- #section:basics/content.searchbox -->
		<div class="nav-search" id="nav-search">
			<form class="form-search">
				<span class="input-icon">
					<input type="text" placeholder="Search ..." class="nav-search-input" id="nav-search-input" autocomplete="off" />
					<i class="ace-icon fa fa-search nav-search-icon"></i>
				</span>
			</form>
		</div><!-- /.nav-search -->

		<!-- /section:basics/content.searchbox -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
		<h3>请假流程测试</h3>
		<a href="/activiti/deployFlow">发布流程</a> &nbsp;&nbsp; ${deployResult }
		<br />
		<a href="/activiti/queryProcdef">流程定义信息</a>
		<br />
		DefinitionInfo: ${definitionInfo }	
		<br />
		<a href="/activiti/startFlow">启动请假流程</a>
		<br />
		StartFlowInfo: ${startFlowInfo }
		<br />
		<a href="/activiti/queryTask">查看任务</a>
		<br />
		TaskInfo: ${taskInfo }
		<br />
		<a href="/activiti/startTask">查看任务</a> == ${startTaskInfo }
		<br />
	</div><!-- /.page-content -->
</div><!-- /.main-content -->
