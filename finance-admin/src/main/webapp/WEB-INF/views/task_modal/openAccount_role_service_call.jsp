<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>

<!-- 额度申请-处理并通知现成开户的时间、地点节点，点击“办理”按钮 -->
<div id="field-modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="field-title">处理开户流程</h4>
			</div>

			<div class="modal-body">
				<div class="row" style="text-align: center;">
				    <form id="field-form" class="form-horizontal" method="post"> 
						
						<div style="display:none;" id="field-task-person-data">
						  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >联系人</label>
		
		                      <div  class="col-sm-9" id="contract-name">
		                      </div>
		                  </div>
		                  <div class="space-4"></div>
		                  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >联系电话</label>
		
		                      <div  class="col-sm-9" id="contract-tel">
		                      </div>
		                  </div>
		                  <div class="space-4"></div>
						  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >开户时间</label>
		
		                      <div  class="col-sm-9" id="field-time">
		                      </div>
		                  </div>
		                  <div class="space-4"></div>
		                  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >开户地点</label>
		
		                      <div  class="col-sm-9" id="field-addr">
		                      </div>
		                  </div>
		                  <div class="space-4"></div>
		                  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >备注信息</label>
		
		                      <div  class="col-sm-9" id="field-remarks">
		                      </div>
		                  </div>
	                   </div>
	                   <div class="control-group" style="display:none;" id="field-task-person">

						</div>
						
						<div style="display:none;" id="field-data-fill">
						  <%-- <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >U盾码</label>
		
		                      <div  class="col-sm-9">
		                          <input class="input-large col-xs-10 col-sm-5 " type="text"  name="<%=WorkFlowUtils.process_definition_key_field_account%>_dataFill" id="<%=WorkFlowUtils.process_definition_key_field_account%>_dataFill" placeholder="U盾码"/>
		                      </div>
		                  </div>
		                  <div class="space-4"></div> --%>
		                  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >合同号</label>
		
		                      <div  class="col-sm-9">
		                          <input class="input-large col-xs-10 col-sm-5 " type="text" name="loanNew_contractNum" id="loanNew_contractNum" placeholder="合同号"/>
		                      </div>
		                  </div>
		                  <div class="space-4"></div>
		                   <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >应收服务费</label>
		
		                      <div  class="col-sm-9" id="service-charge">
		                      </div>
		                      
		                      <input type="hidden" id="js-hidden-service-charge">
		                  </div>
		                  
		                  
		                  
		                  <div class="space-4"></div>
		                  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >服务费</label> 
		
		                      <div  class="col-sm-6">
		                          <input id="js-chk-isPayServiceMoney" type="checkbox" name="loanNew_serviceChargeState">已支付服务费
		                      </div>
		                  </div>
		                  
		                  <div id="js-div-isPayServiceMoney" style="display:none;">
			                  <div class="space-4"></div>
			                   <div class="form-group">
			                      <label class="col-sm-3 control-label no-padding-right" >实收服务费</label>
			                      <div  class="col-sm-9" id="service-charge">
			                      	<input class="input-large col-xs-10 col-sm-5 " type="text" name="loanNew_serviceChargeActual" id="loanNew_serviceChargeActual" placeholder="实际收取服务费用"/>
			                      </div>
			                  </div>
		                  </div>
		                  
		                  <div class="space-4"></div>
		                  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >发票</label> 
		
		                      <div  class="col-sm-6">
		                          <input type="checkbox" name="loanNew_isInvoicing" id="loanNew_isInvoicing">是否开具发票
		                      </div>
		                  </div>
		                 
						<%-- 	U盾码<input type="text" name="<%=WorkFlowUtils.process_definition_key_field_account%>_dataFill"></br>
							合同号<input type="text" name="<%=WorkFlowUtils.process_definition_key_field_account%>_contractNum"></br>
							<input type="checkbox" name="<%=WorkFlowUtils.process_definition_key_field_account%>_serviceChargeState">是否支付服务费</br>
							支付金额<input type="text" name="<%=WorkFlowUtils.process_definition_key_field_account%>_serviceCharge"></br> --%>
						</div>
						<div style="display:none;" id="business-archive">
						 <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >档案号</label> 
		
		                      <div  class="col-sm-6">
		                         <input type="text" class="input-large col-xs-10 col-sm-5 " name="loanNew_archive" id="loanNew_archive">
		                      </div>
		                  </div>
							<%-- 档案号<input type="text" name="<%=WorkFlowUtils.process_definition_key_field_account%>_archive"> --%>
						</div>
						<textarea rows="5" cols="60" placeholder="备注" id="loanNew_reason" name="loanNew_reason" maxlength="150"></textarea>
					</form>
				</div>
			</div>

			<div class="modal-footer">

				<a class="btn btn-sm btn-primary field-submit" href="#">
					<i class="ace-icon fa fa-check"></i>
					办理
				</a>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->