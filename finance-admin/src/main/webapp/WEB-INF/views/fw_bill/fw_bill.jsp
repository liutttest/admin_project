<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<dict:loadDictList var="transactionType" type="TRANSACTION_TYPE" toJson="true"/>
<div class="main-content">
	<!-- #section:basics/content.breadcrumbs -->
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
		</script>

		<ul class="breadcrumb">
			<li>
				<i class="ace-icon fa fa-shield home-icon"></i>
				账单
			</li>
			
		</ul><!-- /.breadcrumb -->
		<!-- /section:basics/content.searchbox -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
	<div class="row">
	<div class="col-xs-12">
	<div>
		<table id="sample-table-2" class="table table-striped table-bordered table-hover mytable">
			<thead>
				<tr>
					<th class="v-align c-align">公司名称</th>
					<th class="v-align c-align">账期</th>
					<th class="v-align c-align">贷款金额(元)</th>
					<th class="v-align c-align">已还本金(元)</th>
					<th class="v-align c-align">已还利息(元)</th>
					<th class="v-align c-align">逾期利息(元)</th>
					<th class="v-align c-align">已还合计(元)</th>
					
					<th class="v-align c-align">
						<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
						操作
					</th>
				</tr>
			</thead>
		
			<tbody>
			</tbody>
		</table>
		</div>
		</div>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->


<div id="bill-detail-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">交易明细</h4>
			</div>

			<div class="modal-body">
			    <div style="height:340px;overflow-y:auto;">
					<div class="col-sm-12" style="text-align: center;">
						<div class="col-xs-12">
							<div>
								<table id="sample-table-2" class="table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th class="c-align">交易时间</th>
											<th class="c-align">
												借据号
											</th>
											<th class="c-align">
												交易类型
											</th>
											<th class="c-align">
												金额(元)
											</th>
											<th class="c-align">
												备注
											</th>
										</tr>
									</thead>
									<tbody class="fw-bill-detail">
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
	
			<div class="modal-footer">
				<a class="btn btn-sm btn-primary" href="#" data-dismiss="modal">
					<i class="ace-icon fa fa-check"></i>
					关闭
				</a>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->

<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath}/js/fa_monitor/fa_monitor.js"></script>
     <script type="text/javascript">
     var transactionType = '${transactionType}';
		var transactionTypes = JSON.parse(transactionType);
     
	jQuery(function($) {
		var oTable1 = 
		$('#sample-table-2')
		.dataTable( {
			"bAutoWidth": false,
			"aaSorting": [],
			"ordering" : false,
			"bServerSide" : true,
			"searching": true,
			"sAjaxSource" : contentPath + "/fwBill/billList",
			"aoColumns" : [   
                           {"mData" : "comName"},   
                           {"mData" : "accountPeriod"},   
                           {"mData" : "loanAmt"},   
                           {"mData" : "principal"},   
                           {"mData" : "interest"},   
                           {"mData" : "boInterest"},
                           {"mData" : "total"},
                           {"mData" : "comId"}],
            "fnServerParams" : function(aoData) {  
                 
            }, 
            "fnServerData" : function(sSource, aoData, fnCallback) {  
                               $.ajax({  
                                   "type" : 'get',  
                                   "url" : sSource,  
                                   "dataType" : "json",  
                                   "data" : {  
                                       aoData : JSON.stringify(aoData)  
                                   },  
                                   "success" : function(resp) {  
                                       fnCallback(resp);  
                                   }  
                               });  
                           },
           "aoColumnDefs":[  
						   {
								   
								"sClass":"center",  
							    "aTargets":[0],  
							    "mData":"comName",  
							    "mRender":function(a,b,c,d){
							    	if (c.comName)
						        	{
						        		return c.comName;
						        	}
						        	else
						        	{
						        		return "";
						        	}
							    }
						   },
						   {
							   
								"sClass":"center",  
							    "aTargets":[2],  
							    "mData":"loanAmt",  
							    "mRender":function(a,b,c,d){
							    	
							    	return c.loanAmt.toFixed(2);
							    }
						   },
						   {
							   
								"sClass":"center",  
							    "aTargets":[3],  
							    "mData":"principal",  
							    "mRender":function(a,b,c,d){
							    	return c.principal.toFixed(2);
							    }
						   },
						   {
							   
								"sClass":"center",  
							    "aTargets":[4],  
							    "mData":"interest",  
							    "mRender":function(a,b,c,d){
							    	return c.interest.toFixed(2);
							    }
						   },
						   {
							   
								"sClass":"center",  
							    "aTargets":[5],  
							    "mData":"boInterest",  
							    "mRender":function(a,b,c,d){
							    	return c.boInterest.toFixed(2);
							    }
						   },
						   {
							   
								"sClass":"center",  
							    "aTargets":[6],  
							    "mData":"total",  
							    "mRender":function(a,b,c,d){
							    	return c.total.toFixed(2);
							    }
						   },
                           {
                        	   
                        	   "sClass":"center",  
                               "aTargets":[7],  
                               "mData":"comId",  
                               "mRender":function(a,b,c,d){//a表示对应的值，c表示当前记录行对象  
                                   return '<div class="hidden-sm hidden-xs action-buttons"><a class="green bill-detail" data-id="'+ c.comId +'" data-period="' + c.accountPeriod + '" href="#" title="详情"><i class="ace-icon fa fa-eye bigger-130"></i></a></div>' 
                                   	+ '<div class="hidden-md hidden-lg">'
       							   	+ '<div class="inline position-relative">'
   									+ '<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown" data-position="auto">'
                               		+ '<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i>'
   									+ '</button>'
   		
   									+'<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">'
   									+	'<li>'
   									+'<a href="#" class="tooltip-success monitor-detail" data-id="${c.comId}" data-rel="tooltip" title="详情">'
   									+'	<span class="green">'
   									+'		<i class="ace-icon fa fa-eye icon-only bigger-120"></i>'
   									+'</span>'
   									+'</a>'
   									+'</li>'
   		
   									+'</ul>'
   									+'</div>'
   									+'</div>';  
                               }
                           }
                           ], 
	    } );
		
	
	
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
		
		
		var bind = {
				bindEvent : function() {
					/**
					 * 为编辑做事件绑定
					 */
					$("tbody").delegate(".bill-detail", "click", function() {
						var data = {};
						data.comId = $(this).attr("data-id");
						data.accountPeroid = $(this).attr("data-period");
						$.ajax({
							url : contentPath + '/fwBill/getBillDetail',
							type : 'POST',
							data : data,
							dataType : 'json',
							success : function(data) {
								// 查看详情
								action.buildTr(data);
								$('#bill-detail-form').modal('show');
							},
							error : function() {
								console.error('访问失败');
							}
						});
					});
				}
			};
			bind.bindEvent();
			
			action={
				  	buildTr : function(data) {
						data = data.data.list;
						var html = [];
						var tbody = $('.fw-bill-detail');
						
						if(data.length>0){
							for ( var int = 0; int < data.length; int++) {
								html.push(action.createTr(data[int]));
							}
							tbody.html(html.join(''));
						}else{
							tbody.html('<tr><td class="c-align" colspan="5">无交易明细。</td></tr>');
						}
					},
					createTr : function(data) {
						var tr = [];
						tr.push(' <tr> <td class="c-align">');
						tr.push(data.jyTime);
						tr.push('</td>');
						tr.push('<td class="c-align"> ');
						tr.push(data.loanNo);
						tr.push('</td>');
						tr.push('<td class="c-align"> ');
						if (transactionTypes[data.transactionType] != undefined)
						{
							tr.push( transactionTypes[data.transactionType].dictValue0);
						}
						tr.push('</td>');
						tr.push('<td class="c-align">');
						tr.push(data.money.toFixed(2));
						tr.push('</td>');
						tr.push('<td class="c-align">');
						tr.push(data.note);
						tr.push('</td>');
						tr.push('</tr>');
						return tr.join('');
					},		
			};
	})
</script>

		