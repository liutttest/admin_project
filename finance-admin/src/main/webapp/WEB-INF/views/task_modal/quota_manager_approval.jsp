<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>

<dict:loadDictList var="operationStates" type="OPERATION_STATE" toJson="true"/><!-- 操作记录状态 -->
<script type="text/javascript">
	/* 操作记录状态 */ 
	var operationStates = '${operationStates}';
	var operationState = JSON.parse(operationStates);
</script>

<!-- 额度申请-经理审批 -->
<form class="form-horizontal" id="frm_main" method="post">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="access-title">经理审批（${comName}）</h4>
			</div>
			
			<!-- 经理审批--操作记录 -->
			<div id="jlspCaoZuoLog" class="modal-body" >
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
			

			<div class="modal-body">
				<div class="container-fluid">
						
					<div class="col-sm-12 form-group">
                       	<textarea class="col-sm-12" rows="5" placeholder="备注" id="comment" name="comment" maxlength="150"></textarea>
                    </div>
					
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm access-submit" data-dismiss="modal" type="button"><i class="ace-icon fa fa-times"></i>关闭</button>
				<button id="nopass" class="btn btn-sm" type="button" value="false"><i class="ace-icon fa fa-times"></i>不通过</button>
			    <button id="handling" class="btn btn-sm btn-primary" type="button" value="true"><i class="ace-icon fa fa-check"></i> 办理</button>
			</div>
		</div>
	</div>
	
	<input type="hidden" name="businessId" id ="businessId" value="${businessId}"/> <!-- 业务ID -->
    <input type="hidden" name="taskId" id ="taskId" value="${taskId}"/> <!-- 任务编号 -->
    <input type="hidden" name="taskDefKey" id ="taskDefKey" value="${taskDefKey}"/> <!-- 任务定义Key -->
    <input type="hidden" name="procInsId" id ="procInsId" value="${procInsId}"/> <!-- 流程实例id -->
    <input type="hidden" name="loanNew_condition" id ="loanNew_condition" /> <!-- 点击按钮，办理结果（1：通过；2：不通过；3：驳回；） -->
</form>
	
	
<script id="hisTemplate" type="text/html">
	<tr role="row" class="odd"><td>{{histIns.activityName}}</td><td>{{assignee}}</td><td>{{histIns.startTime}}</td><td>{{histIns.endTime}}</td><td>{{comment}}</td></tr>
</script> 
<script src='${contentPath}/plugins/jsTemp/template.js' type='text/javascript'></script>

<script src="${contentPath }/js/task_modal/modal_common.js"></script>
<script src="${contentPath }/js/task_modal/quota_manager_approval.js"></script>
