<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<dict:loadDictList var="filetypes" type="FILE_TYPE" toJson="true"/>
<!-- 现场开户-打印清单资料 -->
<form class="form-horizontal" id="print-form" method="post" action="${contentPath}/fwBussinessSxdPrintData">
	<div class="modal-dialog">
		<div class="modal-content">
		
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">打印清单</h4>
			</div>

			<div class="modal-body">
				 <div class="row" id="print-body" style="text-align: left ;padding-left: 30px;">
			   		<!-- <div class="checkbox"><label><input name="print-detail" type="checkbox" class="ace" data-busid="201508210001" value="01"><span class="lbl"> 纳税人、扣缴义务人涉税保密信息查询申请表</span>  &nbsp; <a class="down-load" data-busid="201508210001" data-bustype="01">下载</a> &nbsp;<a class="down-load-detail" data-busid="201508210001" data-bustype="01">查看</a></label></div> -->
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-sm access-submit" data-dismiss="modal" type="button"><i class="ace-icon fa fa-times"></i>关闭</button>
				<button id="handling" class="btn btn-sm btn-primary" type="button" value="true"><i class="ace-icon fa fa-check"></i> 确认打印</button>
			</div>
			
		</div>
	</div>
	
	<input type="hidden" name="businessId" id ="businessId" value="${businessId}"/> <!-- 业务ID -->
    <input type="hidden" name="taskId" id ="taskId" value="${taskId}"/> <!-- 任务编号 -->
    <input type="hidden" name="taskDefKey" id ="taskDefKey" value="${taskDefKey}"/> <!-- 任务定义Key -->
    
</form>

<script>
var filetypeJson = '${filetypes}';
var fileJson = JSON.parse(filetypeJson);
</script>
<script src="${contentPath }/js/task_modal/openAccount_printList.js"></script>