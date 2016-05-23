<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
	.margin-tp { margin-top: 6px; text-align: left; }
</style>
<!-- 现场开户-任务分配 -->
<form class="form-horizontal" id="frm_main" method="post">
	<div class="modal-dialog">
		<div class="modal-content">
		
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">现场分派任务</h4>
			</div>

			<div class="modal-body">
				<div class="row" style="text-align: center;">
						
						<div id="field-task-person-data" class="col-sm-12">
						  <div class="form-group">
		                      <label class="col-sm-2 control-label no-padding-right">联系人</label>
		
		                      <div class="col-sm-9 margin-tp" id="contract-name">liuliu</div>
		                  </div>
		                  <div class="form-group">
		                      <label class="col-sm-2 control-label no-padding-right">联系电话</label>
		
		                      <div class="col-sm-9 margin-tp" id="contract-tel">13844013559</div>
		                  </div>
						  <div class="form-group">
		                      <label class="col-sm-2 control-label no-padding-right">开户时间</label>
		
		                      <div class="col-sm-9 margin-tp" id="field-time"></div>
		                  </div>
		                  <div class="form-group">
		                      <label class="col-sm-2 control-label no-padding-right">开户地点</label>
		
		                      <div class="col-sm-9 margin-tp" id="field-addr"></div>
		                  </div>
		                  <div class="form-group">
		                      <label class="col-sm-2 control-label no-padding-right">备注信息</label>
		
		                      <div class="col-sm-9 huanHangLeft margin-tp" id="field-remarks">
		                      
		                      </div>
		                  </div>
		                  <div class="form-group">	
		                  <!-- 任务分配，可以分配的人 -->
		                   	<div class="control-group" id="field-task-person">
		                   		
							</div>
							
							<div style="height: "></div>
						</div>
		                  <div class="form-group">	
		                      <div class="col-sm-12 huanHangLeft margin-tp" id="field-remarks">
  								<div class="col-sm-12">
		                      		<textarea rows="5" cols="74" placeholder="备注" id="comment" name="comment" maxlength="150"></textarea>
		                     	 </div>
  								</div>
		                      
		                	</div>
	                   	</div>
	                   	
	                   	
						
						
						
						
						
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm access-submit" data-dismiss="modal" type="button"><i class="ace-icon fa fa-times"></i>关闭</button>
			    <button id="handling" class="btn btn-sm btn-primary" type="button" value="true"><i class="ace-icon fa fa-check"></i> 办理</button>
			</div>
		</div>
	</div>
	
	
	<input type="hidden" name="businessId" id ="businessId" value="${businessId}"/> <!-- 业务ID -->
    <input type="hidden" name="taskId" id ="taskId" value="${taskId}"/> <!-- 任务编号 -->
    <input type="hidden" name="taskDefKey" id ="taskDefKey" value="${taskDefKey}"/> <!-- 任务定义Key -->
    <input type="hidden" name="procInsId" id ="procInsId" value="${procInsId}"/> <!-- 流程实例id -->
    <input type="hidden" name="loanNew_taskPersons" id ="loanNew_taskPersons" /> <!-- 任务分配的数组，存人的ids-->
	
</form>



<script src="${contentPath }/js/task_modal/modal_common.js"></script> <!-- modal的公共方法 -->
<script src="${contentPath }/js/task_modal/common_notification.js"></script> <!-- 关于通知的公共方法 -->
<script src="${contentPath }/js/task_modal/openAccount_assign_task.js"></script>