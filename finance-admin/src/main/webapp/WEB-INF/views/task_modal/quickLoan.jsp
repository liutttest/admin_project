<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 快速贷款-“办理”按钮 -->
<form id="frm_main" method="post" class="form-horizontal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">处理快速贷款</h4>
			</div>

			<div class="modal-body">
			    <div style="height:340px;overflow-y:auto;">
					<div class="col-sm-12" style="text-align: center;">
						<div class="col-xs-12">
							<div>
								<table id="sample-table-2" class="table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th class="c-align col-sm-3">处理人</th>
											<th class="c-align col-sm-4">
												<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
												处理时间
											</th>
											<th class="c-align col-sm-5">
												处理意见
											</th>
										</tr>
									</thead>
									<tbody class="fw-handle-record">
									</tbody>
								</table>
							</div>
						</div>
					</div>
					
					<div class="col-sm-12" style="text-align: center;">
					    
					    	<div class="form-group">
								<label class="col-sm-2 control-label no-padding-right" for="comName"> 公司名称 </label>

								<div class="col-sm-10">
									<input type="text" id="comName" name="comName" placeholder="请输入公司名称" class="col-xs-10 col-sm-5">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label no-padding-right" for="comName"> 处理意见 </label>
								<div class="col-sm-10">
									<textarea class="col-xs-10 col-sm-10" name="comment" id="comment" placeholder="请输入处理意见" rows="5"  maxlength="150"></textarea>
								</div>
							</div>
							<!-- <div id="quickloan-input-hidden" style="display:none;">
							<select id="quick-loan-select" > 
							<option value="1">满足条件，线下</option>
							<option value="2">满足条件，线上</option>
							<option value="3">不满足条件</option>
							</select>
							</div> -->
						
					</div>
				</div>
			</div>
	
			<div class="modal-footer">
				<a id="appendRemark" class="btn btn-sm btn-primary quickloan-submit" href="#" data-condition="true">
					<i class="ace-icon fa fa-check"></i>
					追加意见
				</a>
				<a id="handleQuickLoan" class="btn btn-sm btn-primary quickloan-submit" href="#" data-condition="true">
					<i class="ace-icon fa fa-check"></i>
					办理并结束
				</a>
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
<script src="${contentPath }/js/task_modal/quickLoan.js"></script>