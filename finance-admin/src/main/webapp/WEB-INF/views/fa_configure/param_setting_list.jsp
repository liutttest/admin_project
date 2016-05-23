<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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
				<i class="ace-icon fa fa-info-circle home-icon"></i>
				系统设置
			</li>
			<li class="active">利率/参数管理</li>
		</ul><!-- /.breadcrumb -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
		<div class="row">
			<div class="col-xs-12">
				<form class="form-horizontal" role="form" action="${contentPath}/faConfigure/paramSetting/edit" method="post">
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 服务费利率 </label>

						<div class="col-sm-9">
							<input type="text" id="service_rate" name="service_rate" value="${service_rate.getPValue() }" class="col-sm-2" /> &nbsp; <label class="control-label"> % </label>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 银行年化利率 </label>

						<div class="col-sm-9">
							<input type="text" id="bank_rate" name="bank_rate" value="${bank_rate.getPValue() }" class="col-sm-2" /> &nbsp; <label class="control-label"> % </label>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 产品最低授信额度 </label>

						<div class="col-sm-9">
							<input type="text" id="min_quota" name="min_quota" value="${min_quota.getPValue() }" class="col-sm-2" /> &nbsp; <label class="control-label"> 万元 </label>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 产品最高授信额度 </label>

						<div class="col-sm-9">
							<input type="text" id="max_quota" name="max_quota" value="${max_quota.getPValue() }" class="col-sm-2" /> &nbsp; <label class="control-label"> 万元 </label>
						</div>
					</div>

					<div class="clearfix form-actions">
						<div class="col-md-offset-3 col-md-9">
							<button class="btn" type="reset">
								<i class="ace-icon fa fa-undo bigger-110"></i>
								重置
							</button>

							&nbsp; &nbsp; &nbsp;
							<button class="btn btn-info" type="submit">
								<i class="ace-icon fa fa-check bigger-110"></i>
								保存
							</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->

<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script type="text/javascript">
</script>

