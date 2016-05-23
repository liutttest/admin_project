<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 弹出层-操作记录S -->
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">操作记录</h4>
			</div>

			<div class="modal-body">
				<div class="container-fluid">
			    	<table id="historyTable" class="table table-striped table-bordered table-hover">
						<thead>
						<tr>
							<th class="c-align" width="15%">执行环节</th>
							<th class="c-align" width="15%">执行人</th>
							<th class="c-align" width="18%">开始时间</th>
							<th class="c-align" width="18%">结束时间</th>
							<th class="c-align">提交意见</th>
							
						</tr>
						</thead>
			
						<tbody id="rows"> 
            			</tbody> 
					</table>
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm" data-dismiss="modal">
					<i class="ace-icon fa fa-times"></i>
					关闭
				</button>
			</div>
		</div>
	</div>
	
	<input type="hidden" name="procInsId" id ="procInsId" value="${procInsId}"/> <!-- 流程实例id -->
	
<!-- 弹出层-操作记录E -->

<script id="hisTemplate" type="text/html">
	<tr role="row" class="odd"><td>{{histIns.activityName}}</td><td>{{assignee}}</td><td>{{histIns.startTime}}</td><td>{{histIns.endTime}}</td><td>{{comment}}</td></tr>
</script> 

<script src='${contentPath}/plugins/jsTemp/template.js' type='text/javascript'></script>
<script src="${contentPath }/js/task_modal/modal_common.js"></script> <!-- modal的公共方法 -->
<script src="${contentPath }/js/task_modal/common_notification.js"></script> <!-- 关于通知的公共方法 -->
<script src="${contentPath }/js/task_modal/operation_record.js"></script>

