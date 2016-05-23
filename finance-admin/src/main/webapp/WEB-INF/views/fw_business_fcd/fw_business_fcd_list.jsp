<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
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
				业务管理
			</li>
			<li class="active">扶持贷管理</li>
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
					<th class="c-align">企业名称</th>
					<th class="c-align">申请金额</th>
					<th class="c-align">审批金额</th>
					<th class="c-align">可用金额</th>
					<th class="c-align">审批状态</th>
					<th class="c-align">修改人</th>
					<th class="c-align">
						<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
						修改时间
					</th>
					
		
					<th></th>
				</tr>
			</thead>
		
			<tbody>
			<c:forEach items="${list}" var ="fwBuiness">
				<tr>
					<td>
						${fwBuiness.comName}
					</td>
					<td class="text-right">
					<fmt:formatNumber value="${fwBuiness.intentMoney}" pattern="#,##0.00#"/>
					
					</td>
					<td class="text-right">
					<fmt:formatNumber value="${fwBuiness.applyMoney}" pattern="#,##0.00#"/>
					</td>
					<td class="text-right">
					<fmt:formatNumber value="${fwBuiness.usableMoney}" pattern="#,##0.00#"/>
					</td>
					<td class="c-align">
					<a href="#" class="activiti-detail" data-type="businessFcd" data-id="${fwBuiness.proId }" >
					 <c:if test="${fwBuiness.applyState==0}">申请中</c:if>
					 <c:if test="${fwBuiness.applyState==1}">审核中</c:if> 
					 <c:if test="${fwBuiness.applyState==2}">审核完成</c:if> 
					 <c:if test="${fwBuiness.applyState==3}">申请驳回</c:if> 
					 </a>
					 </td>
					<td class="c-align"> ${fwBuiness.userName} </td>
					<td class="c-align"> ${fwBuiness.updateTime} </td>
					<td class="c-align">
						<div class="hidden-sm hidden-xs action-buttons">
							<!-- <a class="blue" href="#">
								<i class="ace-icon fa fa-search-plus bigger-130"></i>
							</a> -->
		
							<a class="green company-detail" data-id="${fwBuiness.bfId}" href="#" title="企业详情">
								<i class="ace-icon fa fa-university bigger-130"></i>
							</a>
							<a class="green com-owner" data-id="${fwBuiness.bfId}" href="#" title="实际控制人">
								<i class="ace-icon fa fa-user bigger-130"></i>
							</a>
							<a class="green com-owner-spouse" data-id="${fwBuiness.bfId}" href="#" title="控制人配偶">
								<i class="ace-icon fa fa-user bigger-130"></i>
							</a>
							
						</div>
		
						<div class="hidden-md hidden-lg">
							<div class="inline position-relative">
								<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown" data-position="auto">
									<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i>
								</button>
		
								<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
		
									<li>
										<a href="#" class="tooltip-success company-detail" data-id="${fwBuiness.bfId}" data-rel="tooltip" title="企业详情">
											<span class="green">
												<i class="ace-icon fa fa-university bigger-120"></i>
											</span>
										</a>
									</li>
									
									
									<li>
										<a href="#" class="tooltip-success com-owner" data-id="${fwBuiness.bfId}" data-rel="tooltip" title="实际控制人">
											<span class="green">
												<i class="ace-icon fa fa-user bigger-120"></i>
											</span>
										</a>
									</li>
									<li>
										<a href="#" class="tooltip-success com-owner-spouse" data-id="${fwBuiness.bfId}" data-rel="tooltip" title="控制人配偶">
											<span class="green">
												<i class="ace-icon fa fa-user bigger-120"></i>
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

<input type="hidden" name="fbId" id="fbId">
<jsp:include page="/WEB-INF/layouts/public_company_detail.jsp" />
<jsp:include page="/WEB-INF/layouts/public_activiti_detail.jsp" />
<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath}/plugins/formatCurrency.js"></script>
<script src="${contentPath}/js/fw_business_fcd/fw_business_fcd.js"></script>
     <script type="text/javascript">
	jQuery(function($) {
		var oTable1 = 
		$('#sample-table-2')
		//.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
		.dataTable( {
			bAutoWidth: false,
			"aoColumns": [
			  null, null,null, null, null,null,null,
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

		