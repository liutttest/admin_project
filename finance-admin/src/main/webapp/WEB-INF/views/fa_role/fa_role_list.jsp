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
				<i class="ace-icon fa fa-cogs home-icon"></i>
				员工管理
			</li>
			<li class="active">岗位管理</li>
		</ul><!-- /.breadcrumb -->
        <button class="btn btn-sm btn-primary add-admin pull-right">添加</button>
		<!-- /section:basics/content.searchbox -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
	<div class="row">
	<div class="col-xs-12">
	<div>
		<table id="sample-table-2" class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th class="min-width v-align c-align">角色</th>
					<th class="max-width v-align c-align">权限</th>
					<th class="max-width v-align c-align">部门</th>
					<th class="center">操作</th>
				</tr>
			</thead>
		
			<tbody>
			<c:forEach items="${list}" var ="faRole">
				<tr>
					<td>
						${faRole.roleName}
					</td>
					<td class="max-width">
					<c:forEach items="${faRole.faRoleFunc}" var="roleFunc">
					${roleFunc.faFunc.funcRemarks},
					</c:forEach>
					</td>
					 <td>${faRole.faDept.deptName}</td>
					<td class="min-width v-align c-align">
						<div class="hidden-sm hidden-xs action-buttons">
		
							<a class="green edit-role" data-id="${faRole.roleId}" href="#" title="编辑">
								<i class="ace-icon fa fa-pencil bigger-130"></i>
							</a>
		
							<a class="red delete-admin" data-id="${faRole.roleId}" href="#" title="删除">
								<i class="ace-icon fa fa-trash-o bigger-130"></i>
							</a>
						</div>
						<div class="hidden-md hidden-lg">
							<div class="inline position-relative">
								<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown" data-position="auto" >
									<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i>
								</button>
		
								<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
									<li>
										<a href="#" class="tooltip-success edit-role" data-id="${faRole.roleId}" data-rel="tooltip" title="编辑">
											<span class="green">
												<i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
											</span>
										</a>
									</li>
		
									<li>
										<a href="#" class="tooltip-error delete-admin" data-id="${faRole.roleId}" data-rel="tooltip" title="删除">
											<span class="red">
												<i class="ace-icon fa fa-trash-o bigger-120"></i>
											</span>
										</a>
									</li>
								</ul>
							</div>
						</div>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		</div>
		</div>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->

<input type="hidden" name="roleId" id="roleId">
<jsp:include page="fa_role_edit.jsp" />
<link rel="stylesheet" href="${contentPath}/plugins/ztree/zTreeStyle.css" />
<script src="${contentPath }/plugins/ztree/jquery.ztree.core-3.5.min.js"></script>
<script src="${contentPath }/plugins/ztree/jquery.ztree.excheck-3.5.min.js"></script>
<script src="js/fa_role/ztree.js"></script>
<!-- page specific plugin scripts -->

<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath}/js/fa_role/fa_role.js"></script>


<script type="text/javascript">
jQuery(function($) {
	var oTable1 =
	$('#sample-table-2')
	//.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
	.dataTable( {
		bAutoWidth: false,
		"aoColumns": [
		  null, null,null,
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


	$(document).on('click', 'th input:checkbox' , function(){
		var that = this;
		$(this).closest('table').find('tr > td:first-child input:checkbox')
		.each(function(){
			this.checked = that.checked;
			$(this).closest('tr').toggleClass('selected');
		});
	});



})
</script>

