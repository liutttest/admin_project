<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 业务归档 -->
<form id="frm_main" class="form-horizontal" method="post"> 
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="field-title">归档</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
		                	<label class="col-sm-2 control-label no-padding-right" >档案号</label>
		                	<div  class="col-sm-9">
		                    	<input type="text" class="input-large col-xs-10 col-sm-5 " name="loanNew_archive" id="loanNew_archive"  maxlength="30">
		                    </div>
		            	</div>
		            	<div class="form-group">
		                	<div class="col-sm-1">&nbsp;</div>
							<div class="col-sm-10">
	            				<textarea maxlength="150" name="comment" id="comment" placeholder="备注" rows="5" cols="62"></textarea>
  							</div>
							<div class="col-sm-1">&nbsp;</div>
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

</form>

<script src="${contentPath }/js/task_modal/modal_common.js"></script> <!-- modal的公共方法 -->
<script src="${contentPath }/js/task_modal/common_notification.js"></script> <!-- 关于通知的公共方法 -->
<script src="${contentPath }/js/task_modal/openAccount_business_archive.js"></script>
