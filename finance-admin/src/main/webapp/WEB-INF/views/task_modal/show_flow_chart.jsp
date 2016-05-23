<%@page import="com.evan.finance.admin.utils.WorkFlowUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<!-- 显示流程图 -->
	<div class="modal-dialog" style="width:80%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h5 class="blue bigger" id="processdefinition-modal-title">流程跟踪图(${data})</h5>
			</div>

			<div class="modal-body">
				<div class="row" style="text-align: center; overflow:auto;">
					<!-- <a href="" data-rel="colorbox" class="cboxElement"> -->
				    	<img id="processdefinition-picture" src="" style="border: none;max-width: 300%;">
				    <!-- </a> -->
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

	<input type="hidden" name="data" id ="data" value="${data}"/> <!-- 标题：公司名称、当前状态 -->
    <input type="hidden" name="procInsId" id ="procInsId" value="${procInsId}"/> <!-- 流程实例id -->
    
<script src="${contentPath }/js/task_modal/show_flow_chart.js"></script>
