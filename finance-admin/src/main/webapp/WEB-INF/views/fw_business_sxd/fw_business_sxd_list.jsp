<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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
			<li class="active">贷款管理</li>
		</ul><!-- /.breadcrumb -->
		<!-- #section:basics/content.searchbox -->
		<!-- /.nav-search -->

		<!-- /section:basics/content.searchbox -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
	<div class="row">
	<div class="col-xs-12">
	<div>
		<table id="sample-table-2" class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th class="c-align col-sm-1">企业名称</th>
					<th class="c-align col-sm-1">申请金额</th>
					<th class="c-align">审批金额</th>
					<th class="c-align col-sm-1 ">可用金额</th>
					<th class="c-align col-sm-1ssss">授信期限</th>
					<th class="c-align">审批状态</th>
					<!-- <th class="c-align col-sm-1">贷款进度</th> -->
					<th class="c-align">修改人</th>
					<th class="c-align">
						<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
						修改时间
					</th>
					<th class=" col-sm-2 ">
						操作
					</th>
				</tr>
			</thead>
		
			<tbody>
			<c:forEach items="${list}" var ="fwBuiness">
				<tr>
					<td class="c-align">
						${fwBuiness.comName}
					</td>
					<td class="c-align">
					<c:choose>
						<c:when test="${empty fwBuiness.intentMoney || fwBuiness.intentMoney == '0.00'}">
							-
						</c:when>
						<c:otherwise>
					 		￥<fmt:formatNumber value="${fwBuiness.intentMoney}" pattern="#,##0.00#"/>
						</c:otherwise>
					</c:choose>
					</td>
					<td class="c-align">
					<c:choose>
						<c:when test="${empty fwBuiness.applyMoney || fwBuiness.applyMoney == '0.00'}">
							-
						</c:when>
						<c:otherwise>
					 		￥<fmt:formatNumber value="${fwBuiness.applyMoney}" pattern="#,##0.00#"/>
						</c:otherwise>
					</c:choose>
					</td>
					<td class="c-align">
					<c:choose>
						<c:when test="${empty fwBuiness.usableMoney || fwBuiness.usableMoney == '0.00'}">
							-
						</c:when>
						<c:otherwise>
					 		￥<fmt:formatNumber value="${fwBuiness.usableMoney}" pattern="#,##0.00#"/>
						</c:otherwise>
					</c:choose>
					</td>
					<td class="c-align">
					<c:choose>
						<c:when test="${empty fwBuiness.creditTime}">
							-
						</c:when>
						<c:otherwise>
					 		${fwBuiness.creditTime}
						</c:otherwise>
					</c:choose>
					</td>
					<td class="c-align">
					<a href="#" class="activiti-detail" data-type="businessSxd" data-id="${fwBuiness.bsId }" data-procInsId="${fwBuiness.proId }">
					 <dict:lookupDictValue key="${fwBuiness.applyState}" type="APPLY_STATE"></dict:lookupDictValue>
					 <%-- <c:if test="${fwBuiness.applyState eq '01'}">准入申请</c:if>
					 <c:if test="${fwBuiness.applyState eq '02'}">准入审核</c:if>
					 <c:if test="${fwBuiness.applyState eq '0209'}">准入申请提交失败</c:if> 
					 <c:if test="${fwBuiness.applyState eq '03'}">额度申请</c:if> 
					 <c:if test="${fwBuiness.applyState eq '04'}">额度审核</c:if>
					 <c:if test="${fwBuiness.applyState eq '0409'}">额度申请提交失败</c:if>
					 <c:if test="${fwBuiness.applyState eq '05'}">授信中</c:if>
					 <c:if test="${fwBuiness.applyState eq '06'}">超过授信期</c:if>
					 <c:if test="${fwBuiness.applyState eq '09'}">终止授信</c:if>
					 <c:if test="${fwBuiness.applyState eq '0301'}">准入驳回</c:if>
					 <c:if test="${fwBuiness.applyState eq '0101'}">额度申请驳回</c:if> --%>
					</a>
					 </td>
					<%--  <td class="c-align"><a  href='#' class="process-definition" data-data="申请公司:${fwBuiness.comName}" date-applyState="${fwBuiness.applyState }"  data-urlid="${fwBuiness.proId }">查看进度</a></td> --%>
					<td class="c-align"> ${fwBuiness.userName} </td>
					<td class="c-align"> ${fwBuiness.updateTime} </td>
					<td class="c-align">
						<div class="hidden-sm hidden-xs action-buttons">
							<a class="green flow-apply-detail" target="_blank" data-href="${contentPath }/fwBusinessSxd/getfwApplyBusDetail?busId=${fwBuiness.bsId}" >
								查看资料  <!-- <i class="ace-icon fa fa-university bigger-130"></i> -->
							</a>
							<c:if test="${fwBuiness.applyState eq '05' && fwBuiness.serviceChargeState eq '01' && fwBuiness.num != 0}">
								<a class="green apply-loan" href="#" data-busId="${fwBuiness.bsId}" data-usable="${fwBuiness.usableMoney}" data-rate="${fwBuiness.interestRate}" data-apply="${fwBuiness.applyMoney}">
									申请放款 <!-- <i class="ace-icon fa fa-university bigger-130"></i> -->
								</a>
							</c:if>
							<a class="green a-set-loan-NO" href="#" data-busId="${fwBuiness.bsId}">
								设置放款次数
							</a>
							<a class="green a-set-repay-NO" href="#" data-busId="${fwBuiness.bsId}">
								设置还款次数
							</a>
							<%-- <a class="green apply-loan" href="#" >
								<a href="${contentPath}/faMonitorLog/getDetailByBusid?busId=${fwBuiness.bsId}">银行监控日志 </a> <!-- <i class="ace-icon fa fa-university bigger-130"></i> -->
							</a> --%>
							<%-- <a  href='#' class="process-definition" data-data="申请公司:${workflow.comName }; 当前状态:${task.name }" data-urlid="${fwBuiness.processInstanceId }">查看流程</a> --%>
						</div>
						<div class="hidden-md hidden-lg">
							<div class="inline position-relative">
								<button class="btn btn-minier btn-yellow dropdown-toggle" data-toggle="dropdown" data-position="auto" >
									<i class="ace-icon fa fa-caret-down icon-only bigger-120"></i>
								</button>
		
								<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
									<li>
										<a class="tooltip-success flow-apply-detail" target="_blank" data-href="${contentPath }/fwBusinessSxd/getfwApplyBusDetail?busId=${fwBuiness.bsId}">
											<span class="green">
												查看资料
											</span>
										</a>
									</li>
		
									<c:if test="${fwBuiness.applyState eq '05' && fwBuiness.serviceChargeState eq '01' && fwBuiness.num != '0'}">
									<li>
										<a href="#" class="tooltip-error apply-loan" href="#" data-busId="${fwBuiness.bsId}" data-usable="${fwBuiness.usableMoney}" data-rate="${fwBuiness.interestRate}" data-apply="${fwBuiness.applyMoney}">
											<span class="red">
												申请放款
											</span>
										</a>
									</li>
									</c:if>
									<li>
										<a class="green a-set-loan-NO" href="#" data-busId="${fwBuiness.bsId}">
											设置放款次数
										</a>
									</li>
									<li>
										<a class="green a-set-repay-NO" href="#" data-busId="${fwBuiness.bsId}">
											设置还款次数
										</a>
									</li>
									<%-- <li>
										<a href="#" class="tooltip-error " href="${contentPath}/faMonitorLog/getDetailByBusid?busId=${fwBuiness.bsId}" >
											<span class="red">
												银行监控日志
											</span>
										</a>
									</li> --%>
								</ul>
							</div>
						</div>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		</div>
		</div>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->

<!-- 弹出层 -->
<div id="public-modal-form" class="modal" tabindex="-1" data-backdrop="static">
</div>


<div id="picture-modal-form" class="modal" tabindex="-1"  data-backdrop="static">
	<div class="modal-dialog" style="width:80%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h5 class="blue bigger" id="processdefinition-modal-title"></h5>
			</div>

			<div class="modal-body">
				<div class="row" style="text-align: center;">
				    <img id="processdefinition-picture" src="" style="border: none;max-width: 100%;" >
				</div>
			</div>

			<div class="modal-footer">
				<a class="btn btn-sm btn-primary" href="#" data-dismiss="modal">
					<i class="ace-icon fa fa-check"></i>
					关闭
				</a>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->


<div id="loan-modal-form" class="modal" tabindex="-1"  data-backdrop="static">
<form id="frm-loanApply" class="form-horizontal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h5 class="blue bigger">申请放款</h5>
			</div>

			<div class="modal-body">
				<div class="row">
				<div class="col-xs-12">
				<div class="form-group">
                      <label class="col-sm-3 control-label no-padding-right" >总额度</label>

                      <div  class="col-sm-9" id="apply-money">
                         	
                      </div>
                  </div>
                  <div class="space-4"></div>
                  <div class="form-group">
                      <label class="col-sm-3 control-label no-padding-right" >剩余额度</label>

                      <div  class="col-sm-9" id="usable-money">
                         		
                      </div>
                  </div>
                  <div class="space-4"></div>
                  <div class="form-group">
                      <label class="col-sm-3 control-label no-padding-right" >利率</label>

                      <div  class="col-sm-9" id="interest-rate">
                         		
                      </div>
                  </div>
                  <div class="space-4"></div>
				   <div class="form-group">
                      <label class="col-sm-3 control-label no-padding-right" >申请放款额度</label>

                      <div  class="col-sm-9">
                          <input class="input-large col-xs-10 col-sm-5 " type="text" id="appMoney" name="appMoney" placeholder="申请额度"/>
                      </div>
                  </div>
				<div class="space-4"></div>
				<div class="form-group">
                         <label class="col-sm-3 control-label no-padding-left" >贷款时间</label>
                          <div  class="col-sm-9">
                               <select id="monthsCount" name="monthsCount">
                                   <option value="1">1个月</option>
                                   <option value="2">2个月</option>
                                   <option value="3">3个月</option>
                                   <option value="4">4个月</option>
                                   <option value="5">5个月</option>
                                   <option value="6">6个月</option>
                               </select>
                          </div>
                     </div>
				</div>
				</div>
			</div>

			<div class="modal-footer">
				<a class="btn btn-sm btn-primary submit-apply" href="#">
					<i class="ace-icon fa fa-check"></i>
				确认申请
				</a>
			</div>
		</div>
	</div>
	</form>
</div><!-- PAGE CONTENT ENDS -->

<!-- 设置放款次数S -->
<div id="modal-set-loan-NO" class="modal" tabindex="-1" data-backdrop="static">
<input type="hidden" id="businessIdForSetLoanNO">
<form id="frm-set-loan-NO" class="form-horizontal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h5 class="blue bigger">设置放款次数</h5>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12">
						<div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >放款次数：</label>
		                      <div class="col-sm-9">
			                       	<input type = "radio" name = "rad-loanNO" id = "rad-loanNO" value="0" class="rad-loanNO" checked="checked" /> 禁止放款
			                       	<input type = "radio" name = "rad-loanNO" id = "rad-loanNO" value="-1" class="rad-loanNO" /> 不限制
			                       	<input type = "radio" name = "rad-loanNO" id = "rad-loanNO" value="-2" class="rad-loanNO" /> 恢复默认
			                       	<input type = "radio" name = "rad-loanNO" id = "rad-loanNO" value="-3" class="rad-loanNO" /> 其他
		                      </div>
		                </div>
		                
		                <div id="inp-set-loanNO" style="display: none;">
			                <div class="space-4"></div>
			                <div class="form-group">
			                    <label class="col-sm-3 control-label no-padding-right" ></label>
			                    <div  class="col-sm-9">
			                       <input type = "text" name = "txt-loanNO" id = "txt-loanNO"/>(次)		
			                    </div>
			                </div>
		                </div>
		                
		                <div class="space-4"></div>
		                <div class="form-group">
							<label class="col-sm-3 control-label no-padding-right" for="comName"> 备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注： </label>
							<div class="col-sm-9">
								<textarea class="col-xs-10 col-sm-10" name="set-loan-NO_reason" id="set-loan-NO_reason" placeholder="备注" rows="5" maxlength="250"></textarea>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="modal-footer">
				<a class="btn btn-sm btn-primary submit-set-loan-NO" href="#">
					<i class="ace-icon fa fa-check"></i>
				确认
				</a>
			</div>
		</div>
	</div>
	</form>
</div><!-- PAGE CONTENT ENDS -->
<!-- 设置放款次数E -->

<!-- 设置还款次数S -->
<div id="modal-set-repay-NO" class="modal" tabindex="-1" data-backdrop="static">
<input type="hidden" id="businessIdForSetRepayNO">
<form id="frm-set-repay-NO" class="form-horizontal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h5 class="blue bigger">设置还款次数</h5>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12">
						<div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >还款次数：</label>
		                      <div class="col-sm-9">
			                       	<input type = "radio" name = "rad-repayNO" id = "rad-repayNO" value="0" checked="checked" /> 禁止还款
			                       	<input type = "radio" name = "rad-repayNO" id = "rad-repayNO" value="-1" /> 不限制
			                       	<input type = "radio" name = "rad-repayNO" id = "rad-repayNO" value="-2" /> 恢复默认
		                      </div>
		                </div>
		                
		                <div class="space-4"></div>
		                <div class="form-group">
							<label class="col-sm-3 control-label no-padding-right" for="comName"> 备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注： </label>
							<div class="col-sm-9">
								<textarea class="col-xs-10 col-sm-10" name="set-repay-NO_reason" id="set-repay-NO_reason" placeholder="备注" rows="5" maxlength="250"></textarea>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="modal-footer">
				<a class="btn btn-sm btn-primary submit-set-repay-NO" href="#">
					<i class="ace-icon fa fa-check"></i>
				确认
				</a>
			</div>
		</div>
	</div>
	</form>
</div><!-- PAGE CONTENT ENDS -->
<!-- 设置还款次数E -->

<input type="hidden" name="fbId" id="fbId">
<%-- <jsp:include page="/WEB-INF/layouts/public_company_detail.jsp" /> --%>
<%-- <jsp:include page="/WEB-INF/layouts/public_activiti_detail.jsp" /> --%> 
<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath }/plugins/formatCurrency.js"></script>
<script src="${contentPath}/js/fw_business_sxd/fw_business_sxd.js"></script>
<script src="${contentPath}/scripts/jquery.cookie.js"></script>

     <script type="text/javascript">
	jQuery(function($) {
		var oTable1 = 
		$('#sample-table-2')
		//.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
		.dataTable( {
			bAutoWidth: false,
			"aoColumns": [
			   { "bSortable": false }, null,null,null,null,{ "bSortable": false }, { "bSortable": false },null,
			  { "bSortable": false }
			],
			"aaSorting": [],
	
			//,
			//"sScrollY": "200px",
			//"bPaginate": false,
	
			//"sScrollX": "100%",
			//"sScrollXInner": "120%",
			//"bScrollCollapse": true,
			//Note: if you are applying horizontal scrolling (sScrollX) on a ".table-bordered"
			//you may want to wrap the table inside a "div.dataTables_borderWrap" element
	
			//"iDisplayLength": 50
	    } );
		/**
		var tableTools = new $.fn.dataTable.TableTools( oTable1, {
			"sSwfPath": "../../copy_csv_xls_pdf.swf",
	        "buttons": [
	            "copy",
	            "csv",
	            "xls",
				"pdf",
	            "print"
	        ]
	    } );
	    $( tableTools.fnContainer() ).insertBefore('#sample-table-2');
		*/
	
	
		$(document).on('click', 'th input:checkbox' , function(){
			var that = this;
			$(this).closest('table').find('tr > td:first-child input:checkbox')
			.each(function(){
				this.checked = that.checked;
				$(this).closest('tr').toggleClass('selected');
			});
		});
	
	
		$('[data-rel="tooltip"]').tooltip({placement: tooltip_placement});
		function tooltip_placement(context, source) {
			var $source = $(source);
			var $parent = $source.closest('table')
			var off1 = $parent.offset();
			var w1 = $parent.width();
	
			var off2 = $source.offset();
			//var w2 = $source.width();
	
			if( parseInt(off2.left) < parseInt(off1.left) + parseInt(w1 / 2) ) return 'right';
			return 'left';
		}
	
	});
	
	
	$(function(){
	var bind={
		bindEvent: function(){
			/**
			*为待办任务列表流程跟踪图片绑定点击事件
			*
			*/
			 $("tbody").delegate('.process-definition', "click", function(){
				var dataUrl=$(this).attr('data-urlid');
				$('#processdefinition-modal-title').html("流程跟踪图("+$(this).attr('data-data')+")");
				$('#processdefinition-picture').attr('src',contentPath+'/workflow/instanceDiagram/'+dataUrl);
				$('#processdefinition-picture').attr('onerror',"nofind("+$('#processdefinition-picture').attr('date-applyState')+");");
				$('#picture-modal-form').modal('show');
			});
			
			$('#picture-modal-form').on('hidden.bs.modal', function () {
				$('#processdefinition-picture').attr('src','');
			});
			
			
			
		}
	};
	bind.bindEvent();
	
});
	function nofind(state){
		if(state=='05'){
			$('#processdefinition-picture').attr('src',contentPath+"/img/tishi_shouxin.jpg");
		}else{
			$('#processdefinition-picture').attr('src',contentPath+"/img/tishi.jpg");
		}
		
		};
		
	
	
</script>

		