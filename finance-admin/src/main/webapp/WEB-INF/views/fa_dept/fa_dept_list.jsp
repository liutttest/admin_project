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
			<li class="active">部门管理</li>
		</ul><!-- /.breadcrumb -->
	    <button class="btn btn-sm btn-primary add-dept pull-right" style="margin-top: 4px">添加</button>
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
					<th class="v-align c-align">部门名称</th>
					<th class="v-align c-align">部门岗位</th>
					<!-- <th class="v-align c-align">
						<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
						创建时间
					</th>
					<th class="hidden-480 v-align c-align">创建人</th> -->
					<th class="v-align c-align">
					操作
					</th>
				</tr>
			</thead>
		
			<tbody>
			<c:forEach items="${depts}" var ="dept">
				<tr>
					<td class="c-align">
						${dept.deptName}
					</td>
					<td>
                       <c:forEach items="${dept.roles}" var ="role">
                       [${role.roleName}]   
                       </c:forEach>
                    </td>
					<%-- <td >${dept.email}</td>
					<td>${dept.deptName}</td> --%>
					<td class="text-center v-align">
						<div class="hidden-sm hidden-xs action-buttons">
							<!-- <a class="blue" href="#">
								<i class="ace-icon fa fa-search-plus bigger-130"></i>
							</a> -->
		
							<a class="green edit-dept" data-id="${dept.deptId}" href="#" title="编辑">
								<i class="ace-icon fa fa-pencil bigger-130"></i>
							</a>
		
							<a class="red delete-dept" data-id="${dept.deptId}" href="#" title="删除">
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
										<a href="#" class="tooltip-success edit-dept" data-id="${dept.deptId}" data-rel="tooltip" title="编辑">
											<span class="green">
												<i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
											</span>
										</a>
									</li>
		
									<li>
										<a href="#" class="tooltip-error delete-dept" data-id="${dept.deptId}" data-rel="tooltip" title="删除">
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

<input type="hidden" name="deptId" id="deptId">
<jsp:include page="fa_dept_edit.jsp" />

<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath}/js/fa_dept/fa_dept.js"></script>
     <script type="text/javascript">
	jQuery(function($) {
		var oTable1 = 
		$('#sample-table-2')
		//.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
		.dataTable( {
			bAutoWidth: false,
			"aoColumns": [
			  null, null,
			  { "bSortable": false }
			],
			"aaSorting": [],
	
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
</script>

		