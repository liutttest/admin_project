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
				<i class="ace-icon fa fa-check-square-o home-icon"></i>
				待办任务
			</li>
			<li class="active">查看资料</li>
		</ul><!-- /.breadcrumb -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
		<div class="row">
			<div class="col-xs-12">
			
				<div class="page-header">
					<h1>快速贷款</h1>
				</div>
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;">联系人 </label>
						<div class="col-sm-3">
							<label class="control-label" style="word-break: break-all;text-align:left;"> ${quickLoan.contactName} </label>
						</div>
						<label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 联系电话 </label>
						<div class="col-sm-5">
							<label class="control-label">${quickLoan.tel} </label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 企业名 </label>
						<div class="col-sm-3">
							<label class="control-label" style="word-break: break-all;text-align:left;"> ${quickLoan.comName} </label>
						</div>
						<label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 贷款金额 </label>
						<div class="col-sm-5">
							<label class="control-label">${quickLoan.applyMoney} </label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 申请时间 </label>
						<div class="col-sm-3">
							<label class="control-label">  ${quickLoan.createTime} </label>
						</div>
					</div>
				</div>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->

<!-- page specific plugin scripts -->

<!-- <script src="../assets/js/jquery.colorbox-min.js"></script> -->


<!-- <script type="text/javascript">
jQuery(function($) {
	var $overflow = '';
	var colorbox_params = {
		rel: 'colorbox',
		reposition:true,
		scalePhotos:true,
		scrolling:false,
		previous:'<i class="ace-icon fa fa-arrow-left"></i>',
		next:'<i class="ace-icon fa fa-arrow-right"></i>',
		close:'&times;',
		current:'{current} of {total}',
		maxWidth:'100%',
		maxHeight:'100%',
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
	$("#cboxLoadingGraphic").html("<i class='ace-icon fa fa-spinner orange'></i>");//let's add a custom loading icon
})
</script> -->
