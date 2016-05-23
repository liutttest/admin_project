<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="main-content">
	<!-- #section:basics/content.breadcrumbs -->
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
		</script>

		<ul class="breadcrumb">
			<li>
				<i class="ace-icon fa fa-check-square-o home-icon"></i>
				已办列表
			</li>
		</ul><!-- /.breadcrumb -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
	<div class="row">
	<div class="col-xs-12 tabbable">
      
	<div class="col-xs-12 tab-content">
	<div>
		<table id="historyTable" class="table table-striped table-bordered table-hover">
			<thead>
			<tr>
				<th class="c-align">企业名称</th>
				<th class="c-align">操作人</th>
				<th class="c-align">办理环节</th>
				<th class="c-align">流程名称</th>
				<th class="c-align">完成时间</th>
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

<!--现场开户办理model  -->
<div id="public-modal-form" class="modal" tabindex="-1" data-backdrop="static">
</div>

<div id="modal-detailed" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="access-title">详细</h4>
			</div>
			<input type="hidden" id="businessIdForFlag" >

			<div class="modal-body">
				<div class="container-fluid">
				<form method="post">
			    	<table id="historyTable" class="table table-striped table-bordered table-hover">
						<thead>
						<tr>
							<th class="c-align" width="15%">执行环节</th>
							<th class="c-align" width="15%">执行人</th>
							<th class="c-align" width="18%">开始时间</th>
							<th class="c-align" width="18%">结束时间</th>
							<th class="c-align">提交意见</th>
							
						</tr>
						</thead>
			
						<tbody id="rows"> 
            			</tbody> 
					</table>
				</form>
				</div>
			</div>

			<div class="modal-footer">
				
			    <button class="btn btn-sm" data-dismiss="modal">
					<i class="ace-icon fa fa-times"></i>
					关闭
				</button>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->
<script id="hisTemplate" type="text/html">
	<tr role="row" class="odd"><td>{{histIns.activityName}}</td><td>{{assignee}}</td><td>{{histIns.startTime}}</td><td>{{histIns.endTime}}</td><td>{{comment}}</td></tr>
</script> 
<script src='${contentPath}/plugins/jsTemp/template.js' type='text/javascript'></script>
<script src="${contentPath}/plugins/color-box/jquery.colorbox-min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath }/js/task_modal/historyList.js"></script>
