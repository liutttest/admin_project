<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<div class="main-content">
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try {
				ace.settings.check('breadcrumbs', 'fixed')
			} catch (e) {
			}
		</script>

		<ul class="breadcrumb">
			<li><i class="ace-icon fa fa-check-square-o home-icon"></i>
				激活流程实例</li>
		</ul>
		<!-- /.breadcrumb -->
	</div>
	<div class="col-sm-12" style="text-align: center;">
		<br />
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right" for="comName">
				流程实例ID </label>

			<div class="col-sm-10">
				<input type="text" id="procInsId" name="procInsId"
					placeholder="请输入流程实例ID" class="col-xs-10 col-sm-5">
				<a id="flowStateBtn" class="btn btn-sm btn-primary quickloan-submit"
				href="#" data-condition="true"> <i class="ace-icon fa fa-check"></i>
				确定
			</a>
			</div>
		</div>
		
	</div>
</div>
<!-- /.main-content -->


<script src="${contentPath }/js/task_modal/flowState.js"></script>
