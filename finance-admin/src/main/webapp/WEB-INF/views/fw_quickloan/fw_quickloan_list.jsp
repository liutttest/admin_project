<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<!-- /section:basics/sidebar -->
<div class="main-content">
	<!-- #section:basics/content.breadcrumbs -->
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
		</script>

		<ul class="breadcrumb">
			<li>
				<i class="ace-icon fa fa-book home-icon"></i>
				业务管理
			</li>
			<li class="active">快速贷款申请</li>
		</ul>
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
		<div class="row">
			<div class="col-xs-12">
				<div>
					<table id="quickLoanList" class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th class="c-align">机构名称</th>
								<th class="c-align">联系人</th>
								<th class="c-align">电话</th>
								<th class="c-align">申请金额</th>
								<th class="c-align">申请时间</th>
								<th class="c-align">办理人</th>
								<th class="c-align">状态</th>
								<th class="c-align">操作</th>
							</tr>
						</thead>
					
						<tbody>
						<%-- <c:forEach items="${list}" var ="fwQuickLoan">
							<tr>
			
								<td>
									${fwQuickLoan.comName}
								</td>
								<td class="c-align">${fwQuickLoan.tel}</td>
								<td class="text-right">
								<fmt:formatNumber value="${fwQuickLoan.applyMoney}" pattern="#,##0.00#"/>
								</td>
								<td class="c-align">
									<c:if test="${fwQuickLoan.loanState=='01'}">
										<span class="label label-sm label-pink arrowed arrowed-right">否</span>
									</c:if>
									<c:if test="${fwQuickLoan.loanState=='02'}">
										<span class="label label-sm label-primary arrowed arrowed-right">是</span>
									</c:if>
								</td>
								<td class="c-align">
									<c:if test="${fwQuickLoan.loanState=='02'}">
										<a data-buskey="${fwQuickLoan.loanId}" class="js-do-appendViews" href="#">追加意见</a>
									</c:if>
								</td>
							</tr>
						</c:forEach> --%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->

<!-- 追加意见modal -->
<input type="hidden" id="js-busId-doAppendRemark" />
<div id="quickloan-modal-form" class="modal" tabindex="-1" data-backdrop="static">
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
											<th class="c-align">处理人</th>
											<th class="c-align">
												<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
												处理时间
											</th>
											<th class="c-align">
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
					    <form id="quickloan-form" method="post" class="form-horizontal">
							<div class="form-group">
								<label class="col-sm-2 control-label no-padding-right" for="comName"> 处理意见 </label>
								<div class="col-sm-10">
									<textarea class="col-xs-10 col-sm-10" name="quickloan_reason" id="quickloan_reason" placeholder="请输入处理意见" rows="5"></textarea>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
	
			<div class="modal-footer">
				<a id="appendRemarkForList" class="btn btn-sm btn-primary quickloan-submit" href="#" data-condition="true">
					<i class="ace-icon fa fa-check"></i>
					追加意见
				</a>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->

<!-- 点击“办理”按钮，弹出的modal -->
<div id="public-modal-form" class="modal" tabindex="-1" data-backdrop="static">
</div>


<input type="hidden" name="btId" id="btId">

<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<%-- <script src="${contentPath}/js/fw_businesstalk/fa_businesstalk.js"></script> --%>

<script src="${contentPath}/js/fw_quickloan/fw_quickloan_list.js"></script>
		