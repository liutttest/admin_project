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
				<i class="ace-icon fa fa-shield home-icon"></i>
				风险控制
			</li>
			<li class="active">银行监控日志</li>
		</ul><!-- /.breadcrumb -->
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
					<th class="v-align c-align">标题</th>
					<th class="v-align c-align">内容</th>
					<th class="hidden-480 v-align c-align">业务id</th>
					<th class="v-align c-align">
						<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
						操作时间
					</th>
		
					<th>

					</th>
				</tr>
			</thead>
		
			<tbody>
			<%-- <c:forEach items="${faMonitorLogs}" var ="faMonitorLog">
				<tr>
					<td>
						${faMonitorLog.title}
					</td>
					<td><textarea style="width: 95%;">${faMonitorLog.content}</textarea></td>
					<td class="hidden-480 ">${faMonitorLog.busId}</td>
					
					<td class="hidden-480 text-center">
						${faMonitorLog.recordingTime}
					</td>
		
					<td class="text-center v-align">
						<div class="hidden-sm hidden-xs action-buttons">
							<!-- <a class="blue" href="#">
								<i class="ace-icon fa fa-search-plus bigger-130"></i>
							</a> -->
		
							<a class="green monitor-detail" data-id="${faMonitorLog.id}" href="#" title="详情">
								<i class="ace-icon fa fa-eye bigger-130"></i>
							</a>
						</div>
		
						<div class="hidden-md hidden-lg">
							<div class="inline position-relative">
								<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown" data-position="auto">
									<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i>
								</button>
		
								<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
									<li>
										<a href="#" class="tooltip-success monitor-detail" data-id="${faMonitorLog.id}" data-rel="tooltip" title="详情">
											<span class="green">
												<i class="ace-icon fa fa-eye icon-only bigger-120"></i>
											</span>
										</a>
									</li>
		
								</ul>
							</div>
						</div>
					</td>
				</tr>
			</c:forEach> --%>
			</tbody>
		</table>
		</div>
		</div>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->

<jsp:include page="fa_monitor_modal.jsp" />

<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath}/js/fa_monitor/fa_monitor_modal.js"></script>
     <script type="text/javascript">
	jQuery(function($) {
		var oTable1 = 
		$('#sample-table-2')
		//.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
		.dataTable( {
			"bAutoWidth": false,
			/* "order": [[ 7, "desc" ]], */
			"aaSorting": [],
			"ordering" : false,
			"bServerSide" : true,
			"searching": true,
			"sAjaxSource" : contentPath + "/faMonitorLog/logList",
			"aoColumns" : [   
                           {"mData" : "title"},     
                           {"mData" : "content"},
                           {"mData" : "businessId"},
                           {"mData" : "opTime"},
                           {"mData" : "logId"}],
            "fnServerParams" : function(aoData) {  
                aoData.push({  
                    "name" : "type",
                    "value" : "other"
                });  
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
							    "aTargets":[1],  
							    "mData":"content",  
							    "mRender":function(a,b,c,d){//a表示对应的值，c表示当前记录行对象  
							    	if (c.content)
						        	{
							    		var htmlStr = '<div style="position:relative;">'
									   	+'<div class="content-tan" style="word-wrap:break-word ;display: none;position: absolute;left:1px;top:15px;background-color: #428FB9;color: white;width: 400px;z-index: 1000;padding: 3px;">'
					        			+'<pre>'+c.content+'</pre>'
								   		+'</div>';
								   		if (c.content.length > 40)
								   		{
								   			htmlStr = htmlStr +'<span class="content">'+ c.content.substring(0, 40) +'......</span>';	
								   		}
								   		else
								   		{
								   			htmlStr = htmlStr +'<span class="content">'+ c.content + '</span>';	
										}
								   		
								   		html = htmlStr + '</div>';
						        		return htmlStr;
						        	}
						        else
						        	{
						        		return "";
						        	}
							    }  
							},
                           {
                        	   
                        	   "sClass":"center",  
                               "aTargets":[4],  
                               "mData":"logId",  
                               "mRender":function(a,b,c,d){//a表示对应的值，c表示当前记录行对象  
                                   return '<div class="hidden-sm hidden-xs action-buttons"><a class="green monitor-detail" data-id="'+ c.logId +'" href="#" title="详情"><i class="ace-icon fa fa-eye bigger-130"></i></a></div>' 
                                   	+ '<div class="hidden-md hidden-lg">'
       							   	+ '<div class="inline position-relative">'
   									+ '<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown" data-position="auto">'
                               		+ '<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i>'
   									+ '</button>'
   		
   									+'<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">'
   									+	'<li>'
   									+'<a href="#" class="tooltip-success monitor-detail" data-id="${faMonitorLog.id}" data-rel="tooltip" title="详情">'
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
	
	/**
	 * 为转办按钮绑定事件
	 */
	$("tbody").delegate('.content','mouseover',function(){
		$(this).prev().show();
	});
	$("tbody").delegate('.content','mouseout',function(){
		$(this).prev().hide();
	}); 
</script>

		