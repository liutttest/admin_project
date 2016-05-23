<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<dict:loadDictList var="operationStates" type="OPERATION_STATE" toJson="true"/><!-- 操作记录状态 -->
<script type="text/javascript">
	var operationStates = '${operationStates}';
	var operationState = JSON.parse(operationStates);
</script>
<div id="activiti-modal-form" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">详情</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12">
						<div>
							<table id="sample-table-2" class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th class="c-align">#</th>
										<th class="c-align">流程名称</th>
										<th class="c-align">操作人</th>
										<th class="c-align">
											<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
											操作时间
										</th>
										<th class="c-align">
											处理结果
										</th>
										<th class="c-align">操作意见/备注</th>
									</tr>
								</thead>
							
								<tbody class="fw-black-detail">
									<%-- <tr>
										<td>
											<a href="#">${fwBlacklist.comName}</a>
										</td>
										<td>${fwBlacklist.addTime}</td>
										<td>${fwBlacklist.removeTime}</td>
										<td>
										<c:if test="${fwBlacklist.isHistroy==1}">YES</c:if>
										<c:if test="${fwBlacklist.isHistroy!=1}">NO</c:if>
										</td>
										<td>${fwBlacklist.userName}</td>
									</tr> --%>
								</tbody>
							</table>
							</div>
							</div>
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
</div><!-- PAGE CONTENT ENDS -->

<script src="${contentPath}/js/layouts/activiti.js"></script>