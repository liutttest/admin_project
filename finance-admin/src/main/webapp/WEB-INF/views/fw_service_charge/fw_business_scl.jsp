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
				财务管理
			</li>
			<li class="active">服务费入账</li>
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
					<th class="c-align">业务编号</th>
					<th class="c-align">企业名称</th>
					<th class="c-align">授信金额</th>
					<th class="c-align">服务费金额</th>
					<th class="c-align">服务费实收金额</th>
					<th class="c-align">缴纳状态</th>
					<th class="c-align">
						<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
						授信时间
					</th>
					<th class="c-align">操作</th>
					<!-- <th></th> -->
				</tr>
			</thead>
		
			<tbody>
			<c:forEach items="${list}" var ="item">
				<c:choose>
					<c:when test="${item.serviceChargeState != '01' && item.creditPassTimeAgo>2}">
						<tr style="color:red;">
					</c:when>
					<c:otherwise>
						<tr>
					</c:otherwise>
				</c:choose>
				
				<td>${item.bsId}</td>
				<td>${item.comName}</td>
				<td class="c-align">${item.applyMoney}</td>
				<td class="c-align">${item.serviceCharge}</td>
				<td class="c-align">${item.serviceChargeActual}</td>
				<td>
					<c:choose>
						<c:when test="${item.serviceChargeState == '01'}">已缴纳</c:when>
						<c:otherwise>未缴纳</c:otherwise>
					</c:choose>
				</td>
				<td class="c-align">${item.creditPassTime}</td>
				<td class="c-align">
					<%-- <c:if test="${item.serviceChargeState != '01'}">
						<a class="flow-turn-todo" href="${contentPath}/fwServiceCharge/doConfirmBank?bsinessId=${item.bsId}" onclick="return confirm('确认已收取此业务的服务费吗?')">确认收款</a>
					</c:if> --%>
					<c:if test="${item.serviceChargeState != '01' && item.creditPassTimeAgo>2}">
						<a data-businessId="${item.bsId}" class="js-a-cuiJiao flow-turn-todo" href="#">催缴服务费</a>
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
<jsp:include page="fw_modal_cuiJiao.jsp" />

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
			  { "bSortable": false },{ "bSortable": false },{ "bSortable": false },{ "bSortable": false },{ "bSortable": false }, null,null,null
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
		};
		
		/** 点击列表中的'催缴服务费按钮'，弹出modal(luy) */
		$('.js-a-cuiJiao').on('click', function(e) {
			e.preventDefault();
			
			$('#businessId').val($(this).attr('data-businessId'));
			
			var url = contentPath + "/fwServiceCharge/findCommLog";
			var param = {};
			param.businessId = $("#businessId").val(); // 业务id
			$.ajax({
				url: url, 
				data: param,
				type: 'POST',
				dataType: 'json',
				success : function(data) {
					//submit = false;
					if (data.status === "success") {
						$('#body-commlog').empty();
						var items = data.data;
						for(var i = 0;i < items.length ;i++){
							var htmlimg = "";
							htmlimg = htmlimg+'<tr>';
							htmlimg = htmlimg+'<td>'+items[i].contactTime+'</td>';
							htmlimg = htmlimg+'<td>'+items[i].content+'</td>';
							htmlimg = htmlimg+'</tr>';
							$('#body-commlog').append(htmlimg);
						}
					} else {
						alert('对不起，查询出现错误');
					}
				},
				error : function() {
					alert('对不起，查询出现错误');
				}
			});
			
			$('#modal-form').modal('show');
			
		});
		
		/** 点击催缴服务费modal中的'确定'按钮 */
		$('#js-a-doCuiJiao').on('click', function(e) {
			e.preventDefault();
			
			if($("#content").val()==''){
				alert('请填写催缴服务费结果');
				return;
			}
			
			$('#modal-form').modal('hide');
			
			//增加催缴通知记录
			var url = contentPath + "/fwServiceCharge/doCuiJiao";
			var param = {};
			param.businessId = $("#businessId").val(); // 业务id
			param.businessType = '01'; // 业务类型
			param.content = $("#content").val(); // 沟通结果
			$.ajax({
				url: url, 
				data: param,
				type: 'POST',
				dataType: 'json',
				success : function(data) {
					//submit = false;
					if (data.status === "success") {
						window.location.href = contentPath + "/fwServiceCharge/business";
					} else {
						alert('对不起，催缴记录增加失败');
					}
				},
				error : function() {
					alert('对不起，催缴记录增加失败');
				}
			});
		});
	});
</script>

		