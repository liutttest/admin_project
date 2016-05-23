<%@page import="com.evan.finance.admin.utils.WorkFlowUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<dict:loadDictList var="reasons" type="ACCCESS_FAILED_REASON"/>
<dict:loadDictList var="filetypes" type="FILE_TYPE" toJson="true"/>
<dict:loadDictList var="operationStates" type="OPERATION_STATE" toJson="true"/><!-- 操作记录状态 -->

<dict:loadDictList var="accessFiles" type="FILE_TYPE" parentKey="01" toJson="true"/><!-- 准入验证-上传的文件 -->
<dict:loadDictList var="limitFiles" type="FILE_TYPE" parentKey="02" toJson="true"/><!-- 额度申请-上传的文件 -->

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ taglib prefix="security" uri="http://www.evan.jaron.com/tags/security"%>
<security:securityUser var="user" />

<div class="main-content">
	<!-- #section:basics/content.breadcrumbs -->
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
		</script>

		<ul class="breadcrumb">
			<li>
				<i class="ace-icon fa fa-check-square-o home-icon"></i>
				待办任务
			</li>
		</ul><!-- /.breadcrumb -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
		<div class="row">
			<div class="col-xs-12">
				<div><a id="access_role_manual_verify" data-buskey="201511240011" data-taskid="737550" data-taskDefKey="role_manual_verify" data-procInsId="760094">人工准入验证办理</a></div>
				<div><a id="access_notification" data-buskey="201511240011" data-taskid="737550"  data-taskDefKey="role_service_phone_call" data-procInsId="760094">客服通知</a></div>
				<div><a id="role_manual_review" data-buskey="201511240011" data-taskid="737550" data-taskDefKey="role_manual_review" data-procInsId="760094">额度申请-人工打分-办理</a></div>
				<div><a id="role_xamination_approval" data-buskey="201511240011" data-taskid="737550" data-taskDefKey="role_xamination_approval" data-procInsId="760094">额度申请-审批 </a></div>
				<div><a id="role_approved_credit_limit" data-buskey="201511240011" data-taskid="737550" data-taskDefKey="role_approved_credit_limit" data-procInsId="760094">额度申请-人工核准授信额度</a></div>
				<div><a id="quota_manager_approval_role" data-buskey="201511240011" data-taskid="737550" data-taskDefKey="quota_manager_approval_role" data-procInsId="760094">额度申请-经理审批</a></div>
				<div><a id="role_qutoa_service_call" data-buskey="201511240011" data-taskid="737550" data-taskDefKey="role_qutoa_service_call" data-procInsId="760094">额度申请-发送申请成功通知，并确定开户时间、地点</a></div>
				<div><a id="role_task_assign" data-buskey="201511240011" data-taskid="737550" data-taskDefKey="role_task_assign" data-procInsId="760094">现场开户-现场任务分派</a></div>
				<div><a id="role_data_backfill" data-buskey="201511240011" data-taskid="737550" data-taskDefKey="role_data_backfill" data-procInsId="760094">数据回填</a></div>
				<div><a id="role_business_archive" data-buskey="201511240011" data-taskid="737550" data-taskDefKey="role_business_archive" data-procInsId="760094">业务归档</a></div>
				<div><a id="role_send_fail_msg" data-buskey="201511240011" data-taskid="737550" data-taskDefKey="role_send_fail_msg" data-procInsId="760094">失败通知</a></div>
				<br>
				<div><a id="quickLoan" data-buskey="59" data-taskid="737550" data-taskDefKey="" data-procInsId="760094">快速贷款-办理</a></div>
				<br>
				
				<div><a id="operation_record" data-procInsId="760094">操作记录</a></div>
				<div><a id="task_transferred" data-buskey="201511240011" data-taskid="737550" data-taskDefKey="role_manual_review">转办</a></div>
				<div><a id="show_flow_chart" data-data="公司名：上海伊文科技；当前状态：客服通知；" data-urlid='760056' data-taskDefKey="show_flow_chart">查看流程图</a></div>
				<div><a id="openAccount_printList" data-buskey="201511240011" data-taskid="737550" data-taskDefKey="openAccount_printList">打印清单</a></div>
			</div>
		</div>
	</div><!-- /.page-content -->
</div><!-- /.main-content -->


<div id="public-modal-form" class="modal" tabindex="-1" data-backdrop="static">
</div>

<jsp:include page="upload_show_file.jsp" />


<input type="hidden" name="turn-todo-name" id="turn-todo-name">
<input type="hidden" name="print-data-busId" id="print-data-busId">
<input type="hidden" name="turn-todo-business-key" id="turn-todo-business-key">
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="${contentPath}/plugins/formatCurrency.js"></script>

<script src="${contentPath }/js/task_modal/list.js"></script>


<%-- <script type="text/javascript" src='${contentPath}/plugins/modal/modal.js'></script> --%>
<script>
var filetypeJson = '${filetypes}';
var fileJson = JSON.parse(filetypeJson);
</script>
<script type="text/javascript">
var textAreaBoolean = false;


$(function(){
	
	var bind={
		bindEvent: function(){
			/* -----------------测试----------------------------------- */
			
			// 准入验证-人工准入验证
			$("div").delegate('#access_role_manual_verify','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				var procInsId = $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJsp?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey+"&procInsId="+procInsId,null,function(){
					$('#public-modal-form').modal('show');
				});
			});
			
			// 准入验证-发送准入验证成功通知
			$("div").delegate('#access_notification','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				var procInsId = $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJsp?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey+"&procInsId="+procInsId,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			// 额度申请-人工打分办理页面
			$("div").delegate('#role_manual_review','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				var procInsId = $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJsp?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey+"&procInsId="+procInsId,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			// 额度申请-审批
			$("div").delegate('#role_xamination_approval','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				var procInsId = $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJsp?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey+"&procInsId="+procInsId,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			// 额度申请-人工核准授信额度
			$("div").delegate('#role_approved_credit_limit','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				var procInsId = $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJsp?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey+"&procInsId="+procInsId,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			// 额度申请-经理审批
			$("div").delegate('#quota_manager_approval_role','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				var procInsId = $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJsp?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey+"&procInsId="+procInsId,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			// 额度申请-发送申请成功通知，并确定开户时间、地点
			$("div").delegate('#role_qutoa_service_call','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				var procInsId = $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJsp?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey+"&procInsId="+procInsId,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			// 现场开户-现场任务分派
			$("div").delegate('#role_task_assign','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				var procInsId = $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJsp?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey+"&procInsId="+procInsId,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			// 数据回填
			$("div").delegate('#role_data_backfill','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				var procInsId = $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJsp?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey+"&procInsId="+procInsId,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			// 业务归档
			$("div").delegate('#role_business_archive','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				var procInsId = $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJsp?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey+"&procInsId="+procInsId,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			// 失败通知
			$("div").delegate('#role_send_fail_msg','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				var procInsId = $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJsp?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey+"&procInsId="+procInsId,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			
			//操作记录
			$("div").delegate('#operation_record','click',function(){
				var procInsId= $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/otherGotoJsp?type=operation_record&procInsId="+procInsId,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});

			//转办
			$("div").delegate('#task_transferred','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				$("#public-modal-form").load(contentPath+"/activitiSxd/otherGotoJsp?type=task_transferred&businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			//查看流程图
			$("div").delegate('#show_flow_chart','click',function(){
				var data= $(this).attr('data-data');
				var urlid = $(this).attr('data-urlid');
				var taskDefKey = $(this).attr('data-taskDefKey');
				$("#public-modal-form").load(contentPath+"/activitiSxd/otherGotoJsp?type=show_flow_chart&data="+data+"&procInsId="+urlid+"&taskDefKey="+taskDefKey,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			//打印清单
			$("div").delegate('#openAccount_printList','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				$("#public-modal-form").load(contentPath+"/activitiSxd/otherGotoJsp?type=openAccount_printList&businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			//快速贷款
			$("div").delegate('#quickLoan','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJspForQuickLoan?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey,null,function(){
					$('#public-modal-form').modal('show');
				});
				
			});
			
			
			
			
			
			
			
			
			
			/* ----人工准入验证  办理按钮---- */
			/* $('#access_role_manual_verify').on('click', function() {
				$('#access-modal-form').modal('show');
				
				var busIdForAccess = $(this).attr('data-buskey');
				/* $('#findInfoFor').attr("href","${contentPath }/workflow/list/getfwApplyBusDetail?busId="+busIdForAccess); 
				$('#findInfoFor').attr("data-href","${contentPath }/workflow/list/getfwApplyBusDetail?busId="+busIdForAccess);
				$('#businessIdForFlag').val(busIdForAccess);
				access_role_manual_verify.getFileForAccess(busIdForAccess); //准入验证-查询要审核的资料(luy-)
				
				// 允许model滚动
				$('#show-modal').on('hidden.bs.modal', function (e) {
		            $('body').addClass('modal-open')
		        })
			}); */
			
			/* ----人工准入验证  客服通知  办理按钮---- */
			/* $('#notification').on('click', function() {
				$('#modal-notification').modal('show');
				
				// 增加发送通知的界面元素
				notification.appendTemplateDiv("customer-service-notify");
				
				// 请求数据，填充数据
				var busId = $(this).attr('data-buskey');
				notification.getTemplate(busId, "access-verify");
				
				// 允许model滚动
				$('#show-modal').on('hidden.bs.modal', function (e) {
		            $('body').addClass('modal-open')
		        })
			}); */
			
			/* ----额度申请 人工打分  办理按钮---- */
			/* $('#quota_role_manual_review').on('click', function() {
				$('#modal-quota-manual-review').modal('show');
				
				//人工打分
				$('#quota-title').text('人工打分');
				
				var busIdForAccess = $(this).attr('data-buskey');
				/* $('#findInfoForLimit').attr("href","${contentPath }/workflow/list/getfwApplyBusDetail?busId="+busIdForAccess); 
				$('#findInfoForLimit').attr("data-href","${contentPath }/workflow/list/getfwApplyBusDetail?busId="+busIdForAccess);
				$('#businessIdForFlag').val(busIdForAccess);
				quota_role_manual_review.getFileForLimit(busIdForAccess); //额度申请-查询要审核的资料(luy-)
				$('#div-limit-file').show();
				
				// 允许model滚动
				$('#show-modal').on('hidden.bs.modal', function (e) {
		            $('body').addClass('modal-open')
		        })
			}); */
			
			
			
			
			
			
			
			
			
			
			/* -----------------测试----------------------------------- */
			
			
			
			
	
		}
	};
	bind.bindEvent();
	
});
</script>

