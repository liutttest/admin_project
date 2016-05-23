<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
	.text-left { text-align: left; margin-top: 4px; }
</style>

<!-- 开户成功-数据回填 -->
<form id="frm_main" class="form-horizontal" method="post"> 
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="field-title">数据回填</h4>
			</div>

			<div class="modal-body">
				<div class="row">
						
					<div id="field-data-fill" class="col-sm-12">
		            	<div class="form-group">
		                	<label class="col-sm-2 control-label no-padding-right" >合同号</label>
		                	<div  class="col-sm-9">
		                    	<input class="input-large col-xs-10 col-sm-5 " type="text" name="loanNew_contractNum" id="loanNew_contractNum" placeholder="合同号"/>
		                    </div>
		            	</div>
	                    <div class="space-4"></div>
	                    	<div class="form-group">
	                      		<label class="col-sm-2 control-label no-padding-right" >应收服务费</label>
	                      		<div  class="col-sm-4 text-left" id="service-charge">
	                      	</div>
	                      	<input type="hidden" id="js-hidden-service-charge">
	                  	</div>
		                  
		                <div class="space-4"></div>
		                <div class="form-group">
		                	<label class="col-sm-2 control-label no-padding-right" >服务费</label> 
		
		                	<div  class="col-sm-6 text-left">
		                    	<input id="js-chk-isPayServiceMoney" type="checkbox" name="loanNew_serviceChargeState">已支付服务费
		               		</div>
		              	</div>
		                  
		             	<div id="js-div-isPayServiceMoney" style="display:none;">
			      			<div class="space-4"></div>
			       			<div class="form-group">
			                	<label class="col-sm-2 control-label no-padding-right" >实收服务费</label>
			                	<div  class="col-sm-9 text-left" id="service-charge">
			                		<input class="input-large col-xs-10 col-sm-5 " type="text" name="loanNew_serviceChargeActual" id="loanNew_serviceChargeActual" placeholder="实际收取服务费用"/>
			                	</div>
			          		</div>
		            	</div>
		                  
		           		<div class="space-4"></div>
	                	<div class="form-group">
		                	<label class="col-sm-2 control-label no-padding-right" >发票</label> 
		
		                	<div  class="col-sm-6 text-left">
		            			<input type="checkbox" name="loanNew_isInvoicing" id="loanNew_isInvoicing">是否开具发票
		             		</div>
	          			</div>
	          			
	          			<div class="space-4"></div>
	          			<div class="form-group">
		                	<div class="col-sm-12">
  								<div class="col-sm-12">
		            				<textarea maxlength="150" name="comment" id="comment" placeholder="备注" rows="5" cols="74"></textarea>
		             			</div>
  							</div>
	          			</div>
		                 
					</div>
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm access-submit" data-dismiss="modal" type="button"><i class="ace-icon fa fa-times"></i>关闭</button>
			    <button id="handling" class="btn btn-sm btn-primary" type="button" value="true"><i class="ace-icon fa fa-check"></i>办理</button>
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
<script src="${contentPath }/js/task_modal/openAccount_data_backfill.js"></script>