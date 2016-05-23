<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.evan.common.user.monitor.MonitorUtils" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
		<table id="sample-table-2" class="table table-striped table-bordered table-hover mytable">
			<thead>
				<tr>
					<th class="v-align c-align">标题</th>
					<th class="v-align c-align">银行接口</th>
					<th class="v-align c-align">公司名称</th>
					<th class="v-align c-align">内容</th>
					<th class="v-align c-align">输入参数</th>
					<th class="v-align c-align">输出参数</th>
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
					<td>
					<c:set var="bankKey" value="${faMonitorLog.title}"></c:set>
					<%=
					   MonitorUtils.bankMap.get((String)pageContext.getAttribute("bankKey"))
					%>
					</td>
					<td>
						${faMonitorLog.comName}
					</td>
					<td>
					<div style="position:relative;"> 
					   <div class="content-tan" style="word-wrap:break-word ;display: none;position: absolute;left:1px;top:15px;background-color: #428FB9;color: white;width: 400px;z-index: 1000;padding: 3px;">
					     <pre>${faMonitorLog.content}</pre>
					   </div>
						 <c:choose>
                       		<c:when test="${!empty faMonitorLog.content}">
                       		<span class="content">${fn:substring(faMonitorLog.content, 0, 20)}......</span>
                       		</c:when>
                       	</c:choose>
                     </div>
					</td>
					<td>
					<div style="position:relative;"> 
					   <div style="word-wrap:break-word ;display: none;position: absolute;left:1px;top:15px;background-color: #428FB9;color: white;width: 250px;z-index: 1000;padding: 3px;">
					     <pre>${ faMonitorLog.inContent}</pre>
					   </div>
						 <c:choose>
                       		<c:when test="${!empty faMonitorLog.inContent}">
                       		<span class="content">${fn:substring(faMonitorLog.inContent, 0, 20)}......</span></c:when>
                       	</c:choose>
                     </div>
					</td>
					<td>
					<div style="position:relative;"> 
					   <div style="word-wrap:break-word ;display: none;position: absolute;left:1px;top:15px;background-color: #428FB9;color: white;width: 250px;z-index: 1000;padding: 3px;">
					    <pre> ${ faMonitorLog.outContent}</pre>
					   </div>
						 <c:choose>
                       		<c:when test="${!empty faMonitorLog.outContent}">
                       		<span class="content">${fn:substring(faMonitorLog.outContent, 0, 20)}......</span>
                       		</c:when>
                       		<c:otherwise>
                       		<span class="content">${faMonitorLog.outContent}</span>
                       		</c:otherwise>
                       	</c:choose>
                       	
                     </div>
					</td>
					<td class="hidden-480 "><a href="${contentPath}/faMonitorLog/getDetailByBusid?busId=${faMonitorLog.busId}">${faMonitorLog.busId}</a></td>
					
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

<jsp:include page="fa_monitor_detail.jsp" />

<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath}/js/fa_monitor/fa_monitor.js"></script>
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
                           {"mData" : "bankInterface"},   
                           {"mData" : "comName"},   
                           {"mData" : "content"},   
                           {"mData" : "inputParam"},   
                           {"mData" : "outputParam"},
                           {"mData" : "businessId"},
                           {"mData" : "opTime"},
                           {"mData" : "logId"}],
            "fnServerParams" : function(aoData) {  
                aoData.push({  
                    "name" : "type",
                    "value" : "bank"
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
							    "aTargets":[2],  
							    "mData":"comName",  
							    "mRender":function(a,b,c,d){//a表示对应的值，c表示当前记录行对象  
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
							    "aTargets":[3],  
							    "mData":"content",  
							    "mRender":function(a,b,c,d){//a表示对应的值，c表示当前记录行对象  
							    	if (c.content)
						        	{
							    		var htmlStr = '<div style="position:relative;">'
									   	+'<div class="content-tan" style="word-wrap:break-word ;display: none;position: absolute;left:1px;top:15px;background-color: #428FB9;color: white;width: 400px;z-index: 1000;padding: 3px;">'
					        			+'<pre>'+c.content+'</pre>'
								   		+'</div>';
								   		if (c.content.length > 20)
								   		{
								   			htmlStr = htmlStr +'<span class="content">'+ c.content.substring(0, 20) +'......</span>';	
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
							    "aTargets":[4],  
							    "mData":"inputParam",  
							    "mRender":function(a,b,c,d){//a表示对应的值，c表示当前记录行对象  
							        if (c.inputParam)
							        	{
							        		var inputParamStr = '<div style="position:relative;">'
											   	+'<div class="content-tan" style="word-wrap:break-word ;display: none;position: absolute;left:1px;top:15px;background-color: #428FB9;color: white;width: 400px;z-index: 1000;padding: 3px;">'
							        			+'<pre>'+c.inputParam+'</pre>'
										   		+'</div>';
										   	if (c.inputParam.length > 20)
										   	{
										   		inputParamStr = inputParamStr + '<span class="content">'+ c.inputParam.substring(0, 20) +'......</span>';
										   	}
										   	else
										   	{
										   		inputParamStr = inputParamStr + '<span class="content">'+ c.inputParam + '</span>'
										   	}
										   	
										   	inputParamStr = inputParamStr + '</div>';
										   	
							        		return inputParamStr;
							        	}
							        else
							        	{
							        		return "";
							        	}
							    }  
							},
							{   
							    "aTargets":[5],  
							    "mData":"outputParam",  
							    "mRender":function(a,b,c,d){//a表示对应的值，c表示当前记录行对象  
							        if (c.outputParam)
							        	{
							        	var outputParamStr = '<div style="position:relative;">'
										   	+'<div class="content-tan" style="word-wrap:break-word ;display: none;position: absolute;left:1px;top:15px;background-color: #428FB9;color: white;width: 400px;z-index: 1000;padding: 3px;">'
						        			+'<pre>'+c.outputParam+'</pre>'
									   		+'</div>';
									   	if (c.outputParam.length > 20)
									   	{
									   		outputParamStr = outputParamStr + '<span class="content">'+ c.outputParam.substring(0, 20) +'......</span>';
									   	}
									   	else
									   	{
									   		outputParamStr = outputParamStr + '<span class="content">'+ c.outputParam + '</span>'
									   	}
									   	
									   	outputParamStr = outputParamStr + '</div>';
									   	
						        		return outputParamStr;
							        	}
							        else
							        	{
							        		return "";
							        	}
							    }  
							},
                           {  
                        	   
                               "aTargets":[6],  
                               "mData":"businessId",  
                               "mRender":function(a,b,c,d){//a表示对应的值，c表示当前记录行对象 
                            	   if (c.businessId)
                            		{
                            		   return '<a href="${contentPath}/faMonitorLog/getDetailByBusid?busId='+ c.businessId +'">'+c.businessId+'</a>';
                            		}
                            	   else
                            		{
                            			return "";
                            		}
                                     
                               }  
                           },
                           {
                        	   
                        	   "sClass":"center",  
                               "aTargets":[8],  
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
                 /* "fnRowCallback" : function(nRow, aData, iDisplayIndex) {//相当于对字段格式化  
                   
                     $('td:eq(8)', nRow).html('<a class="green monitor-detail" data-id="${faMonitorLog.id}" href="#" title="详情"><i class="ace-icon fa fa-eye bigger-130"></i></a>');    
             },   */
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

		