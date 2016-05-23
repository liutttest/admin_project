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
				<i class="ace-icon fa fa-comments home-icon"></i>
				消息管理
			</li>
			<li class="active">站内信</li>
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
					<th class="c-align col-sm-3">标题</th>
					<th class="c-align col-sm-1">接收人</th>
					<th class="c-align col-sm-5">消息内容</th>
					<th class="c-align col-sm-2">
						<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
						创建时间
					</th>
					
		
					<th class="c-align col-sm-1">状态</th>
					<!-- <th></th> -->
				</tr>
			</thead>
		
			<tbody>
			<c:forEach items="${list}" var ="fwMessage">
				<tr>
					<td>
						${fwMessage.title}
					</td>
					<td class="c-align">${fwMessage.userName}</td>
					<td>${fwMessage.content}</td>
					<td class="c-align">${fwMessage.createTime}</td>
					<td class="c-align">
					<c:if test="${fwMessage.state==1}">
					<span class="label label-sm label-primary arrowed arrowed-right">已读</span>
					</c:if>
					<c:if test="${fwMessage.state==0}">
					<span class="label label-sm label-pink arrowed arrowed-right">未读</span>
					</c:if>
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

<input type="hidden" name="btId" id="btId">
<%-- <jsp:include page="fa_admin_edit.jsp" /> --%>

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
			  { "bSortable": false },{ "bSortable": false },{ "bSortable": false }, null,null
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

		