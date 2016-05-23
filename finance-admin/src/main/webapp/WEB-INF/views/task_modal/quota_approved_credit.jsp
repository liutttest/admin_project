<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
	.margin-tp {  margin-top: 6px; }
</style>
<!-- 额度申请-人工核准授信额度 -->
<form class="form-horizontal" id="frm_quota_approved_credit" method="post">
	<div class="modal-dialog">
		<div class="modal-content">
		
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="access-title">核准授信额度</h4>
			</div>

			<div class="modal-body">
			
				<div class="container-fluid">
					
					<div class="col-sm-12 form-group">
						<label class="col-sm-3 control-label">上一年纳税总额</label>
	                	<div class="col-sm-9 margin-tp">
	                    	${fwBusinessSxd.lastyTaxmoney}
	                    </div>
					</div>
					<div class="col-sm-12 form-group">
						<label class="col-sm-3 control-label">贷款金额</label>
	                	<div class="col-sm-9 margin-tp">
	                    	${fwBusinessSxd.intentMoney}
	                    </div>
					</div>
					
					<div class="col-sm-12 form-group">
							<label class="col-sm-3 control-label">授信额度</label>
		                	<div class="col-sm-9">
		                    	<input type="text" name="loanNew_creditLimit" id="loanNew_creditLimit">
		                    </div>
					</div>
				
					<div class="col-sm-12 form-group">
                       <div class="col-sm-12">
							<textarea rows="5" placeholder="备注" id="comment" name="comment" maxlength="150" cols="70"></textarea>
						</div>
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
	
	
	
<script src="${contentPath }/js/task_modal/modal_common.js"></script> <!-- modal的公共方法 -->
<script src="${contentPath }/js/task_modal/common_notification.js"></script> <!-- 关于通知的公共方法 -->
<script src="${contentPath }/js/task_modal/quota_approved_credit.js"></script>
