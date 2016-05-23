<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.evan.common.user.monitor.MonitorUtils" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 

<!-- /section:basics/sidebar -->
<div class="main-content">
	<!-- #section:basics/content.breadcrumbs -->
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
		</script>

		<ul class="breadcrumb">
			<li>
			   业务银行监控日志
				<!-- <i class="ace-icon fa fa-shield home-icon"></i>
				风险控制 -->
			</li>
			<!-- <li class="active">银行监控日志</li> -->
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
		
					<!-- <th>

					</th> -->
				</tr>
			</thead>
		
			<tbody>
			<c:if test="${fn:length(faMonitorLogs)<=0}">
			
			<tr class="odd"><td valign="top" colspan="9" class="dataTables_empty">暂时没有数据</td></tr>
			</c:if>
			<c:forEach items="${faMonitorLogs}" var ="faMonitorLog">
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
					<td class="hidden-480 ">${faMonitorLog.busId}</td>
					
					<td class="hidden-480 text-center">
						${faMonitorLog.recordingTime}
					</td>
		
					<%-- <td class="text-center v-align">
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
					</td> --%>
				</tr>
			</c:forEach>
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
   $("tbody").delegate('.content','mouseover',function(){
		$(this).prev().show();
	});
	$("tbody").delegate('.content','mouseout',function(){
		$(this).prev().hide();
	}); 
</script>

		