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
				<i class="ace-icon fa fa-usd home-icon"></i>
				服务费管理
			</li>
			<li class="active">服务费开票邮寄</li>
		</ul><!-- /.breadcrumb -->
	    <!-- <button class="btn btn-sm btn-primary add-dept pull-right" style="margin-top: 4px">添加</button> -->
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
					<th class="v-align c-align">业务编号</th>
					<th class="v-align c-align">企业名称</th>
					<th class="v-align c-align">
						<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
						创建时间
					</th>
					<th class="hidden-480 v-align c-align">
					<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
					邮寄时间</th>
					<th class="v-align c-align">邮寄地址</th>
					<th class="v-align c-align">邮编</th>
					<th class="v-align c-align">是否已邮寄</th>
					<th class="v-align c-align">
					操作
					</th>
				</tr>
			</thead>
		
			<tbody>
			<c:forEach items="${list}" var ="invoicing">
				<tr>
					<td class="c-align">
						${invoicing.busId}
					</td>
					<td>
                       ${invoicing.comName}
                    </td>
					<td >${invoicing.createTime}</td>
					<td>${invoicing.mailTime}</td>
					<td>${invoicing.mailAddress}</td>
					<td>${invoicing.postCode}</td>
					<td>
					<c:if test="${invoicing.isAlreadyMail=='01'}">
					未邮寄
					</c:if>
					<c:if test="${invoicing.isAlreadyMail=='02'}">
					已邮寄
					</c:if>
					</td>
					<td class="text-center v-align">
					<c:if test="${invoicing.isAlreadyMail=='01'}">
						<div class="hidden-sm hidden-xs action-buttons">
		
							<a class="green edit-invoice" data-id="${invoicing.id}" href="#" title="开票邮寄">
								<i class="ace-icon fa fa-envelope bigger-130"></i>
							</a>
						</div>
					</c:if>
					<div class="hidden-sm hidden-xs action-buttons">
		
							<a class="green invoice-detail" data-id="${invoicing.id}" href="#" title="查看详情">
								<i class="ace-icon fa  fa-eye bigger-130"></i>
							</a>
						</div>
					
						<div class="hidden-md hidden-lg">
						
							<div class="inline position-relative">
								<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown" data-position="auto" >
									<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i>
								</button>
								<c:if test="${invoicing.isAlreadyMail=='01'}">
								<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
									<li>
										<a href="#" class="tooltip-success edit-invoice" data-id="${invoicing.id}" data-rel="tooltip" title="开票邮寄">
											<span class="green">
												<i class="ace-icon fa fa-envelope bigger-120"></i>
											</span>
										</a>
									</li>
		
								</ul>
								</c:if>
								<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
									<li>
										<a href="#" class="tooltip-success invoice-detail" data-id="${invoicing.id}" data-rel="tooltip" title="查看详情">
											<span class="green">
												<i class="ace-icon fa  fa-eyesss bigger-120"></i>
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

<input type="hidden" name="id" id="id">
<jsp:include page="fa_invoice_mail_edit.jsp" />

<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath}/js/fa_invoice_mail/fa_invoice_mail.js"></script>
     <script type="text/javascript">
	jQuery(function($) {
		var oTable1 = 
		$('#sample-table-2')
		//.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
		.dataTable( {
			bAutoWidth: false,
			"aoColumns": [
			  null,{ "bSortable": false },null,null,{ "bSortable": false },{ "bSortable": false },null,
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

		