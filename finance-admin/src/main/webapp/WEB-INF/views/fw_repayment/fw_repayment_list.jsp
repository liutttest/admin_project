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
			<li class="active">申请还款</li>
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
					<th class="c-align">还款方式</th>
					<th class="c-align">
					<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
					还款日期
					</th>
					<th class="c-align">还款金额</th>
					<th class="c-align">
						账单月
					</th>
					<th class="c-align">
						申请人
					</th>
					<th class="c-align">状态</th>
				</tr>
			</thead>
		
			<tbody>
			<c:forEach items="${list}" var ="fwrepayment">
				<tr>
					<td>
					${fwrepayment.comName}
					</td>
					<td class="c-align">
					<dict:lookupDictValue type="REPAYMENT_TYPE" key="${fwrepayment.repayType}" />
					</td>
					<td class="c-align">
					${fwrepayment.perRepayTime}
					</td>
					<td class="text-right">
						<fmt:formatNumber value="${fwrepayment.repayMoney}" pattern="#,##0.00#"/>
					</td>
					<td class="text-right">
						${fwrepayment.billMonth}
					</td>
		
					<td class="c-align">
						${fwrepayment.userName}
					</td>
					<td class="c-align">
					<a href="#" class="activiti-detail" data-type="repaymentBack" data-id="${fwrepayment.proId }">
					<dict:lookupDictValue type="REPAY_MENT_STATE" key="${fwrepayment.state}" />
					</a>
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

<jsp:include page="/WEB-INF/layouts/public_activiti_detail.jsp" />

<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<%-- <script src="${contentPath}/js/fw_businesstalk/fa_businesstalk.js"></script> --%>
     <script type="text/javascript">
	jQuery(function($) {
		var oTable1 = 
		$('#sample-table-2')
		//.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
		.dataTable( {
			bAutoWidth: false,
			"aoColumns": [
			  null, null,null, null, null,null,null
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

		