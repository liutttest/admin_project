<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.evan.finance.admin.utils.WorkFlowUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
				<i class="ace-icon fa fa-flag home-icon"></i>
				<a href="#">流程管理</a>
			</li>
			<li class="active">部署管理</li>
		</ul><!-- /.breadcrumb -->

		<a class="btn btn-sm btn-primary pull-right deployment-activiti" href="#">部署流程</a>

		<!-- /section:basics/content.searchbox -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
		<div class="row">
			<c:if test="${!empty result }">
				<div class="alert alert-success">
					<button type="button" class="close" data-dismiss="alert">
						<i class="ace-icon fa fa-times"></i>
					</button>
					<i class="ace-icon fa fa-check"></i>
					${result }
				</div>
			</c:if>
		
			<div class="col-xs-12">
			<div>
				<table id="sample-table-2" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th class="c-align v-align">ProcessDefinitionId</th>
							<th class="c-align v-align">DeploymentId</th>
							<th class="hidden-480 c-align v-align">名称</th>
							<th class="hidden-480 c-align v-align">KEY</th>
							<th class="hidden-480 c-align v-align">版本</th>
							<th class="hidden-480 c-align v-align">BPMN</th>
							<th class="hidden-480 c-align v-align">图片</th>
							<th class="hidden-480 c-align v-align">
								<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
								部署时间
							</th>
						</tr>
					</thead>

					<tbody>
					<c:forEach items="${objects }" var ="object">
						<c:set var="process" value="${object[0] }" />
						<c:set var="deployment" value="${object[1] }" />
						<tr>
							<td>${process.getId()}</td>
							<td>${process.getDeploymentId()}</td>
							<td class="hidden-480">${process.getName()}</td>
							<td class="hidden-480">${process.getKey()}</td>
							<td class="hidden-480 c-align">${process.getVersion()}</td>
							<td class="hidden-480">
								<a target="_blank" href='${contentPath}/workflow/resource/read?processDefinitionId=${process.id}&resourceType=xml'>${process.resourceName }</a>
							</td>
							<td class="hidden-480">
								<a target="_blank" href='${contentPath}/workflow/resource/read?processDefinitionId=${process.id}&resourceType=image'>${process.diagramResourceName }</a>
							</td>
							<td class="hidden-480"><fmt:formatDate value="${deployment.deploymentTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				</div>
			</div>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->


<div id="modal-form" class="modal" tabindex="-1"  data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="edit-dept-modal">流程 </h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<form id="activiti-form" class="form-horizontal" method="post" action="${contentPath}/workflow/startDeploymentAll">
                       <div class="col-xs-7">
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" >
                                	<input type="checkbox" name="check-activiti" value="<%=WorkFlowUtils.process_definition_key_access_verify%>">
                                </label>
                                <div class="col-sm-9">
                                   	 准入验证流程
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" >
                                	<input type="checkbox" name="check-activiti" value="<%=WorkFlowUtils.process_definition_key_quota_apply%>">
                                </label>
                                <div class="col-sm-9">
                                   	 额度申请流程
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" >
                                	<input type="checkbox" name="check-activiti" value="<%=WorkFlowUtils.process_definition_key_field_account%>">
                                </label>
                                <div class="col-sm-9">
                                   	 现场开户流程
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" >
                                	<input type="checkbox" name="check-activiti" value="<%=WorkFlowUtils.process_definition_key_loan%>">
                                </label>
                                <div class="col-sm-9">
                                   	 放款流程
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" >
                                	<input type="checkbox" name="check-activiti" value="<%=WorkFlowUtils.process_definition_key_quickloan%>">
                                </label>
                                <div class="col-sm-9">
                                   	 快速贷款
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" >
                                	<input type="checkbox" name="check-activiti" value="loanNew">
                                </label>
                                <div class="col-sm-9">
                                   	 业务申请
                                </div>
                            </div>
                        </div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-sm" data-dismiss="modal">
					<i class="ace-icon fa fa-times"></i>
					取消
				</button>

				<button class="btn btn-sm btn-primary" id="activiti-deploy" type=>
					<i class="ace-icon fa fa-check"></i>
					保存
				</button>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->




<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script type="text/javascript">
	jQuery(function($) {
		var oTable1 = 
		$('#sample-table-2')
		//.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
		.dataTable( {
			bAutoWidth: false,
			"aoColumns": [
			  { "bSortable": false }, { "bSortable": false },null, null, null, null, null,
			  { "bSortable": false }
			],
			"aaSorting": []

			//,
			//"sScrollY": "200px",
			//"bPaginate": false,

			//"sScrollX": "100%",
			//"sScrollXInner": "120%",
			//"bScrollCollapse": true,
			//Note: if you are applying horizontal scrolling (sScrollX) on a ".table-bordered"
			//you may want to wrap the table inside a "div.dataTables_borderWrap" element

			//"iDisplayLength": 50
	    } );
		/**
		var tableTools = new $.fn.dataTable.TableTools( oTable1, {
			"sSwfPath": "../../copy_csv_xls_pdf.swf",
	        "buttons": [
	            "copy",
	            "csv",
	            "xls",
				"pdf",
	            "print"
	        ]
	    } );
	    $( tableTools.fnContainer() ).insertBefore('#sample-table-2');
		*/
	
	

		$('[data-rel="tooltip"]').tooltip({placement: tooltip_placement});
		function tooltip_placement(context, source) {
			var $source = $(source);
			var $parent = $source.closest('table')
			var off1 = $parent.offset();
			var w1 = $parent.width();

			var off2 = $source.offset();
			//var w2 = $source.width();

			if( parseInt(off2.left) < parseInt(off1.left) + parseInt(w1 / 2) ) return 'right';
			return 'left';
		}

	})
	
	
	$(function(){
		var bind={
			bindEvent:function(){
				$('.deployment-activiti').on('click',function(){
					$('#modal-form').modal('show');
				});
				
				$('#activiti-deploy').on('click',function(){
					$('#activiti-form').submit();
					
				})
				
				//为额度申请model隐藏绑定事件
				$('#modal-form').on('hidden.bs.modal', function () {
					var el = document.getElementsByName('check-activiti');     
					  var len = el.length;     
					  for(var i=0; i<len;i++){
					     el[i].checked = false;    
					  }
				});
			}
		}
		bind.bindEvent();
	})
</script>