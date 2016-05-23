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
				<i class="ace-icon fa fa-book home-icon"></i>
				业务管理
			</li>
			<li class="active">投诉建议</li>
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
					<th class="c-align">姓名</th>
					<th class="c-align">电话</th>
					<th class="c-align">问题类型</th>
					<th class="c-align">投诉建议</th>
					<th class="c-align">
						<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
						创建时间
					</th>
					<th class="c-align">状态</th>
				</tr>
			</thead>
		
			<tbody>
			<c:forEach items="${list}" var ="fwFeedback">
				<tr>
					<td>
					${fwFeedback.contactName}
					</td>
					<td class="c-align">${fwFeedback.tel}</td>
					<td class="c-align"><dict:lookupDictValue type="FEED_TYPE" key="${fwFeedback.issueType}" /></td>
					<td>${fwFeedback.content}</td>
					
					<td class="c-align">
						${fwFeedback.createTime}
					</td>
					<td class="c-align">
						<c:if test="${fwFeedback.feedState eq '02'}">
							<dict:lookupDictValue type="FEED_STATE" key="${fwFeedback.feedState}" />
						</c:if>
						<c:if test="${fwFeedback.feedState eq '01'}">
							<a href="#" class="edit-dept" data-type="feedBack" data-id="${fwFeedback.fdId}">处理</a>
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

<input type="hidden" name="feedId" id="feedId">
<jsp:include page="fw_feedback_edit.jsp" />

<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath}/js/fw_feedback/fw_feedback.js"></script>
     <script type="text/javascript">
	jQuery(function($) {
		var oTable1 = 
		$('#sample-table-2')
		//.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
		.dataTable( {
			bAutoWidth: false,
			"aoColumns": [
			  { "bSortable": false }, { "bSortable": false },null, null, null,null
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

		