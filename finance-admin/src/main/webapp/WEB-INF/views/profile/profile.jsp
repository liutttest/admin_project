<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
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
				<a href="${contentPath}/main">首页</a>
			</li>
			<li class="active">个人中心</li>
		</ul><!-- /.breadcrumb -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
		<div class="row">
			<div class="col-xs-12">
			<form id="profile-form" class="form-horizontal">
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="userName"> 用户名 </label>
					<div class="col-sm-9">
					<span>${user.userName }</span>
					<input type="hidden" id="userName" value="${user.userName }"/>
						<%-- <input type="text" id="userName" name="userName" value="${user.userName }" readOnly="readonly"  class="col-xs-10 col-sm-5" /> --%>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="password"> 密码 </label>
					<div class="col-sm-9">
						<input type="password" id="password" name="password" placeholder="******" class="col-xs-10 col-sm-5" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="tel"> 电话 </label>
					<div class="col-sm-9">
						<input type="text" id="tel" name="tel" value="${user.tel }" class="col-xs-10 col-sm-5" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="email"> 邮件 </label>
					<div class="col-sm-9">
						<input type="text" id="email" name="email"  value="${user.email }" class="col-xs-10 col-sm-5" />
					</div>
				</div>
			</div>
			<div class="clearfix form-actions">
				<div class="col-md-offset-4 col-md-8">
					<button class="btn btn-info" type="button" id="save-profile">
						<i class="ace-icon fa fa-check bigger-110"></i>
						保存
					</button>
			</div>
			</form>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->

<input type="hidden" name="userId" id="userId" value="${user.userId}">
<script src="${contentPath}/js/profile/profile.js"></script>
