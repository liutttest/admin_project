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
				<i class="ace-icon fa fa-users home-icon"></i>
				客户管理
			</li>
			<li class="active">客户管理</li>
		</ul><!-- /.breadcrumb -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
	<div class="row">
	<div class="col-xs-12">
	<div>
		<table id="sample-table-2" class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th class="v-align col-sm-3 c-align">用户名</th>
					<th class="v-align col-sm-3 c-align">电话</th>
					<!-- <th>企业</th> -->
					<th class="v-align col-sm-3 c-align">是否是黑名单</th>
					<th class="v-align col-sm-3 c-align">是否可用</th>
					<th></th>
				</tr>
			</thead>
		
			<tbody>
			<c:forEach items="${list}" var ="fwComAccount">
				<tr>
					<td>
						${fwComAccount.userName}
					</td>
					<td>${fwComAccount.tel}</td>
					<%-- <td>${fwComAccount.comName}</td> --%>
					<td class="c-align blacklist">
					<c:if test="${fwComAccount.isHistroy==1}">
					<span class="label label-sm label-primary arrowed arrowed-right">是</span>
					</c:if>
					<c:if test="${fwComAccount.isHistroy!=1}">
					<span class="label label-sm label-pink arrowed arrowed-right">否</span>
					</c:if>
					</td>
					<td class="c-align disableList">
					    <c:if test="${fwComAccount.isAvailable==1}">
						<span class="label label-sm label-primary arrowed arrowed-right">是</span>
						</c:if>
					<c:if test="${fwComAccount.isAvailable==0}">
					<span class="label label-sm label-pink arrowed arrowed-right">否</span>
					</c:if>
					</td>
					<td class="c-align v-align">
						<div class="hidden-sm hidden-xs action-buttons">
							<a class="green company-detail" data-id="${fwComAccount.comId}" href="#" title="详情">
								<i class="ace-icon fa fa-eye bigger-130"></i>
							</a>
							
							<c:if test="${fwComAccount.isHistroy!=1}">
							<a class="remove-to-black" data-id="${fwComAccount.comId}" href="#" title="移入黑名单">
								<i class="ace-icon fa fa-adjust bigger-130"></i>
							</a>
							</c:if>
							
							<c:if test="${fwComAccount.isAvailable==1}">
							<a class="green disableAccount" data-id="${fwComAccount.userId}" href="#" title="禁用">
								<i class="ace-icon fa fa-ban bigger-130"></i>
							</a>
							</c:if>
							<c:if test="${fwComAccount.isAvailable==0}">
							<a class="green revertAccount" data-id="${fwComAccount.userId}" href="#" title="恢复使用">
								<i class="ace-icon fa fa-history bigger-130"></i>
							</a>
							</c:if>
							
							
						</div>
		
						<div class="hidden-md hidden-lg">
							<div class="inline position-relative">
								<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown" data-position="auto">
									<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i>
								</button>
		
								<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
		
									<li>
										<a href="#" class="tooltip-success company-detail" data-id="${fwComAccount.comId}" data-rel="tooltip" title="详情">
											<span class="red">
												<i class="ace-icon fa fa-eye bigger-120"></i>
											</span>
										</a>
									</li>
									
									<c:if test="${fwComAccount.isHistroy!=1}">
									<li>
										<a href="#" class="tooltip-success remove-to-black" data-id="${fwComAccount.comId}" data-rel="tooltip" title="移入黑名单">
											<span class="red">
												<i class="ace-icon fa fa-adjust bigger-120"></i>
											</span>
										</a>
									</li>
									</c:if>
									<c:if test="${fwComAccount.isAvailable==1}">
									<li>
										<a href="#" class="tooltip-success disableAccount" data-id="${fwComAccount.userId}" data-rel="tooltip" title="禁用">
											<span class="red">
												<i class="ace-icon fa fa-ban bigger-120"></i>
											</span>
										</a>
									</li>
									</c:if>
									<c:if test="${fwComAccount.isAvailable==0}">
									<li>
										<a href="#" class="tooltip-success revertAccount" data-id="${fwComAccount.userId}" data-rel="tooltip" title="恢复使用">
											<span class="red">
												<i class="ace-icon fa fa-history bigger-120"></i>
											</span>
										</a>
									</li>
									</c:if>
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

<input type="hidden" name="fbId" id="fbId">
<jsp:include page="fw_comaccount_edit.jsp" />

<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath}/plugins/formatCurrency.js"></script>

<script src="${contentPath}/js/fw_comaccount/fw_comaccount.js"></script>
     <script type="text/javascript">
	jQuery(function($) {
		var oTable1 = 
		$('#sample-table-2')
		//.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
		.dataTable( {
			bAutoWidth: false,
			"aoColumns": [
			  { "bSortable": false }, { "bSortable": false }, null,null,
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

		