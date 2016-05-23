<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<dict:loadDictList var="transactionType" type="TRANSACTION_TYPE" toJson="true"/>

<div class="main-content">
	<!-- #section:basics/content.breadcrumbs -->
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
		</script>

		<ul class="breadcrumb">
			<li>
				<i class="ace-icon fa fa-leaf home-icon"></i>
				贷后管理
			</li>
			<li class="active">逾期记录</li>
		</ul><!-- /.breadcrumb -->
		<!-- /section:basics/content.searchbox -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
	<!-- <div style="margin-bottom:10px;">
		<span>请选择月份：</span>
		<select id="month-select">
			
		</select>
	</div> -->
	<div style="margin-bottom:10px;margin-right: 20px;">
	<a href="${contentPath}/faOverdueInfo/toFaOverdueInfoHistory">逾期历史记录</a>
	</div>

	<div class="row">
	<div class="col-xs-12 tab-content">
	<div>
		<table id="sample-table-2" class="table table-striped table-bordered table-hover mytable">
			<thead>
				<tr>
					<th class="v-align col-sm-3 c-align">公司名称</th>
					<th class="v-align col-sm-2 c-align">借据号</th>
					<th class="v-align col-sm-2 c-align">放款日期</th>
					<th class="v-align col-sm-2 c-align">到期时间</th>
					<th class="v-align col-sm-2 c-align">逾期类型</th>
					<th class="v-align col-sm-1 c-align">
						<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
						操作
					</th>
				</tr>
			</thead>
		
			<tbody class="fw-interest-list">
			</tbody>
		</table>
		</div>
		</div>
		 </div>
	</div><!-- /.page-content -->
	
	<!-- 逾期历史记录modal -->
	<div id="history-modal-form" class="modal" tabindex="-1" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="blue bigger">逾期历史记录</h4>
				</div>
	
				<div class="modal-body">
					<div class="row">
						<div class="col-xs-12">
							<div>
								<table id="sample-table-2" class="table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th class="c-align">#</th>
											
											<th class="c-align">
												<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
												到期时间
											</th>
											<th class="c-align">逾期金额</th>
											<th class="c-align">
											          逾期类型
											</th>
										</tr>
									</thead>
								
									<tbody>
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
	
		<!-- 代偿S -->
	<div id="modal-compensatory" class="modal" tabindex="-1" data-backdrop="static">
	<form id="form-compensatory" class="form-horizontal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h5 class="blue bigger">伊文代偿</h5>
				</div>
	
				<div class="modal-body">
					<div class="row">
						<div class="col-xs-12">
							<div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >代偿本金</label>
		
		                      <div  class="col-sm-9" id="compensatory-money">
		                      </div>
		                  </div>
		                  <div class="space-4"></div>
						  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >代偿利息</label>
		
		                      <div  class="col-sm-9" id="compensatory-rate">
		                      </div>
		                  </div>
		                  <div class="space-4"></div>
		                  <div class="form-group">
		                      <label class="col-sm-3 control-label no-padding-right" >代偿总计金额</label>
		                      <div  class="col-sm-9" id="compensatory-total">
		                      </div>
		                      <input type="hidden" name="compensatory-total-input" id="compensatory-total-input">
		                      <input type="hidden" name="compensatory-loanNo" id="compensatory-loanNo">
		                  </div>
		                  <div class="space-4"></div>
			              <div class="form-group">
							  <label class="col-sm-3 control-label no-padding-right" > 备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注： </label>
							  <div class="col-sm-9">
								 <textarea class="col-xs-10 col-sm-10" name="compensatory-reason" id="compensatory-reason" placeholder="备注" rows="5" maxlength="250"></textarea>
							  </div>
						  </div>
						</div>
					</div>
				</div>
	
				<div class="modal-footer">
					<a class="btn btn-sm btn-primary submit-compensatory" href="#">
						<i class="ace-icon fa fa-check"></i>
					确认代偿
					</a>
				</div>
			</div>
		</div>
		</form>
	</div><!-- PAGE CONTENT ENDS -->
   <!-- 代偿E -->
   
   <!-- 发送逾期通知modal  -->
   <div id="warning-modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="warning-title">发送逾期通知</h4>
			</div>
			
			<div class="modal-body">
				<div class="container-fluid">
			    <form class="form-horizontal" id="notification-form" method="post">
			    <div id="warning-notify">
			    </div>
			    <input id="busId" name="busId" type="hidden" value=""/>
			    </form>
			    </div>
			</div>
			<div class="modal-footer">
			    <button class="btn btn-sm btn-primary notification-submit" type="submit" value="true"><i class="ace-icon fa fa-check"></i>发送</button>
			</div>
		</div>
	</div>
	</div>
	 <!-- /发送逾期通知modal -->
</div><!-- /.main-content -->


<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<link rel="stylesheet" type="text/css" href="${contentPath}/plugins/editor/css/wangEditor-1.3.11.css">
<script type="text/javascript" src='${contentPath}/plugins/editor/js/wangEditor-1.3.11.js'></script>

 <script type="text/javascript">
     var transactionType = '${transactionType}';
		var transactionTypes = JSON.parse(transactionType);
     
	jQuery(function($) {
		var resubmit = false;
		/* action.fillMonthSelect(); */
		/* 填充月份选择的下拉列表 */
		 /* var date = new Date();
		var monthSelect = $("#month-select");
		for (var i = 0; i < 10; i++)
		{
			
			monthSelect.append('<option value='+ date.getFullYear() + '-' + (date.getMonth()+1) +'>' + date.getFullYear() + '-' + (date.getMonth()+1) + '</option>');
			date.setMonth(date.getMonth() - 1, date.getDate());
		}  */
		
		//表单验证
		var validator = {
			    validator : function(){
			        var varlidator = $('#form-compensatory').validate({
			            rules : {
			                'compensatory-reason' : {//备注
			                    required : true
			                }
			            },
			            /* 设置错误信息 */
					    messages: {
							'compensatory-reason' : {
								required : "请输入备注"
							}
					    },
			            errorClass : 'text-warning red',
			            errorPlacement : function(error, element){
			                element.closest('div').append(error);
			            }
			        });
			        return varlidator;
			    },
			    valid : function(){
			        return this.validator().form();
			    },
			    reset : function(){
			        this.validator().resetForm();
			    }

		};
		
		
		var oTable1 = 
		$('#sample-table-2')
		.dataTable( {
			"bAutoWidth": false,
			"aaSorting": [],
			"ordering" : false,
			"bServerSide" : true,
			"searching": true,
			"sAjaxSource" : contentPath + "/faOverdueInfo/getList",
			"aoColumns" : [   
                           {"mData" : "comName"},   
                           {"mData" : "loanNo"},   
                           {"mData" : "applyPassTime"},   
                           {"mData" : "expitedTime"},   
                           {"mData" : "rate"},
                           {"mData" : "busId"}
                           ],
           /*  "fnServerParams" : function(aoData) { 
            	aoData.push({  
                    "name" : "month",
                    "value" : $("#month-select").val()
                });
            },  */
            "fnServerData" : function(sSource, aoData, fnCallback) {  
                               $.ajax({  
                                   "type" : 'get',  
                                   "url" : sSource,  
                                   "dataType" : "json",  
                                   "data" : {  
                                       aoData : JSON.stringify(aoData)  
                                   },  
                                   "success" : function(resp) {  
                                       fnCallback(resp);  
                                   }  
                               });  
                           },
           "aoColumnDefs":[  
						   {
								   
								"sClass":"center col-sm-1",  
							    "aTargets":[0],  
							    "mData":"comName",  
							    "mRender":function(a,b,c,d){
							    	if (c.comName)
						        	{
						        		return c.comName;
						        	}
						        	else
						        	{
						        		return "";
						        	}
							    }
						   },
						   {
							   
								"sClass":"center col-sm-1",  
							    "aTargets":[1],  
							    "mData":"loanNo",  
							    "mRender":function(a,b,c,d){
							    	if (c.loanNo)
						        	{
						        		return c.loanNo;
						        	}
						        	else
						        	{
						        		return "";
						        	}
							    }
						   },
						   {
							   
								"sClass":"center col-sm-1",  
							    "aTargets":[2],  
							    "mData":"applyPassTime",  
							    "mRender":function(a,b,c,d){
							    	if (c.applyPassTime)
						        	{
						        		return c.applyPassTime;
						        	}
						        	else
						        	{
						        		return "";
						        	}
							    }
						   },
						   {
							   
								"sClass":"center col-sm-1",  
							    "aTargets":[3],  
							    "mData":"expitedTime",  
							    "mRender":function(a,b,c,d){
							    	if (c.expitedTime)
						        	{
						        		return c.expitedTime;
						        	}
						        	else
						        	{
						        		return "";
						        	}
							    }
						   },
						   {
							   
								"sClass":"center col-sm-1",  
							    "aTargets":[4],  
							    "mData":"rate",  
							    "mRender":function(a,b,c,d){
							    	var  result = '';
							    	if (c.rate>0)
						        	{
							    		result = '<span class="label label-sm label-primary arrowed arrowed-right">利息</span>';
						        	}
							    	if(c.money>0){
							    		result =result+'<span class="label label-sm label-primary arrowed arrowed-right">本金</span>';
							    	}
					        		return result;
						    }
						   },
                           {
                        	   
                        	   "sClass":"center",  
                               "aTargets":[5],  
                               "mData":"busId",  
                               "mRender":function(a,b,c,d){//a表示对应的值，c表示当前记录行对象
                            	   
                            	   //显示“代偿”、“逾期通知”、“逾期历史记录” 
                            	   var html  =[];
                                   html.push(' <span><a data-loanNo="' + c.loanNo + '" data-busId="'+c.busId+'"class="green js－compensatory" href="#" >代偿</a> </span>');
                                   html.push(' <span><a data-busId="' + c.busId + '" class="green js－notify" href="#" >逾期通知</a></span>');
                                   html.push(' <span><a data-loanNo="' + c.loanNo + '" class="green js－overduce-history" href="#" >明细</a></span>');//查询逾期记录表中，状态为已还的
                            	   
                                   return html.join("");
                               }
                           }
                           ], 
	    } );
		
	
	
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
		
		
		var bind = {
				bindEvent : function() {
					/**
					 * 为通知按钮做事件绑定
					 */
					$("tbody").delegate(".js－notify", "click", function() {
						$('#busId').val($(this).attr('data-busId'));
						action.appendTemplateDiv("warning-notify");
						$('#warning-modal-form').modal('show');
						$('#warning-modal-form').on('hidden.bs.modal', function () {
							$('#warning-notify').html('');
						});
						action.getTemplate();
					});
					/**
					 * 为“逾期历史记录”绑定事件
					 */
					$("tbody").delegate(".js－overduce-history", "click", function() {
						var loanNo = $(this).attr('data-loanNo');
						
						/* action.appendTemplateDiv("warning-notify");
						$('#warning-modal-form').modal('show');
						$('#warning-modal-form').on('hidden.bs.modal', function () {
							$('#warning-notify').html('');
						});
						action.getTemplate(); */
						action.getHistoryList(loanNo);
						$('#history-modal-form').modal('show');
					});
					
					/**
					 * 为“代偿”绑定事件
					 */
					$("tbody").delegate(".js－compensatory", "click", function() {
						var loanNo = $(this).attr('data-loanNo');
						var busId = $(this).attr('data-busId');
						action.getCompensatory(loanNo,busId);
					});
					
					
					//为确认代偿绑定事件
					$(".submit-compensatory").on("click", function(e){
						if(!validator.valid()){
							return false;
						}
						//弹出确认代偿确认框
						if(!confirm("确认代偿么？")){
					         return false; 
					     }
						action.submitCompensatory();
					});
					// 日期月份绑定事件
					/* $(".day-tab").on("click", function(e){
						var day = $(this).attr("data-value");
						$("#day").val(day);
						
						// 重新加载数据
						oTable1.fnClearTable(0);  
						oTable1.fnDraw(); 
					}); */
					
					// 月份select绑定事件
					/* $("#month-select").on("change", function(e){ 
						$("#day").val("18");
						$('.tabbable .active').removeClass('active');
						$('.nav-tabs li:first').addClass('active');
						
						// 重新加载数据
						oTable1.fnClearTable(0);  
						oTable1.fnDraw(); 
					}); */
					
					// 发送按钮绑定事件
					$('.notification-submit').on('click', function(e){
						result = action.verifySendMsg();
						if (result == false)
						{
							return false;
						}
						
						tools.openLoading();
						action.sendNotification();
					});
					/**
					*为代偿modal绑定隐藏事件
					**/
					$('#modal-compensatory').on('hidden.bs.modal', function () {
						$('#compensatory-reason').val('');
						$('#compensatory-loanNo').val('');
						$('#compensatory-total-input').val('');
						validator.reset();
					});
				},
				
				/* 为下拉列表绑定事件--lixj */
				bindSelectChangeEvent:function()
				{

					/* 为邮件模版下拉列表绑定change事件--lixj */
					$('#emailTemplate').on("change", function(e){
					
						if ($(this).val() == '0')
						{
							// 自定义模版
							$('#emailTitle').val("");
							// $('.wangEditor-textarea p').html("");
							$('.wangEditor-textarea p').remove();
							editor.append('<p></p>');
						}
						else
						{
							for (var index = 0; index < emailTemplate.length;index++)
							{
								if (emailTemplate[index].tid == $(this).val())
								{
									$('#emailTitle').val(emailTemplate[index].title);
									$('.wangEditor-textarea p').remove();
									editor.append('<p>' + emailTemplate[index].content + '</p>');
									break;
								}
							}

						}
					});
					/* 为短信模版下拉列表绑定change事件--lixj */
					$('#smsTemplate').on("change", function(e){
						if ($(this).val() == '0')
						{
							// 自定义模版
							$('#smsContent').val("");
						}
						else
						{
							for (var index = 0; index < smsTemplate.length;index++)
							{
								if (smsTemplate[index].tid == $(this).val())
								{
									$('#smsContent').val(smsTemplate[index].content);
									break;
								}
							}

						}
					});
					
					
					/* 为站内信模版下拉列表绑定change事件--lixj */
					$('#messageTemplate').on("change", function(e){
						if ($(this).val() == '0')
						{
							// 自定义模版
							$('#messageTitle').val("");
							$('#messageContent').val("");
						}
						else
						{
							for (var index = 0; index < messageTemplate.length;index++)
							{
								if (messageTemplate[index].tid == $(this).val())
								{
									$('#messageTitle').val(messageTemplate[index].title);
									$('#messageContent').val(messageTemplate[index].content);
									break;
								}
							}

						}
					});
				}
			};
			bind.bindEvent();
			
			var action = {
				/* fillMonthSelect:function(){
					var date = new Date();
					var monthSelect = $("#month-select");
					for (var i = 0; i < 10; i++)
					{
						
						monthSelect.append('<option value='+ date.getFullYear() + '-' + (date.getMonth()+1) +'>' + date.getFullYear() + '-' + (date.getMonth()+1) + '</option>');
						date.setMonth(date.getMonth() - 1, date.getDate());
					}
				}, */
				getCompensatory :function(loanNo,busId){
					tools.openLoading();
					var url = contentPath +"/faOverdueInfo/getLoanDetail";
			  		var param = {};
			  		param.loanNo = loanNo;
			  		param.busId = busId;
			  		 $.ajax({
							url :url,
							type : 'POST',
							data :param,
							dataType : 'json',
							success : function(data){
								tools.closeLoading();
								if(data.status=='success'){
									var money = data.data.opResult.Balance;
									var rate = data.data.opResult.TotPdIntAmt;
									var total = parseFloat(money)+parseFloat(rate);
									$('#compensatory-money').text(money);
									$('#compensatory-rate').text(rate);
									$('#compensatory-total').text(total.toFixed(2));
									$('#compensatory-total-input').val(total.toFixed(2));
									$('#compensatory-loanNo').val(loanNo);
									$('#modal-compensatory').modal('show');
								}else{
									tools.openST({
										title : '提示', // {String}
										text : data.msg, // {String}
										type : 'fail' // {String}
									});
								}
								
								
							},
							error : function() {
								tools.openST({
									title : '提示', // {String}
									text : '服务器异常', // {String}
									type : 'fail' // {String}
								});
							}
				  		});
				},
				submitCompensatory :function(){
					var reason = $('#compensatory-reason').val();
					var loanNo = $('#compensatory-loanNo').val();
					var compensatoryTotal = $('#compensatory-total-input').val();
					if(resubmit==true){
						return false;
					}
					resubmit = true;
					tools.openLoading();
					var url = contentPath +"/faOverdueInfo/submitCompensatory";
			  		var param = {};
			  		param.reason = reason;
			  		param.loanNo = loanNo;
			  		param.compensatoryTotal = compensatoryTotal;
			  		 $.ajax({
							url :url,
							type : 'POST',
							data :param,
							dataType : 'json',
							success : function(data){
								tools.closeLoading();
								resubmit = false;
								if(data.status=='success'){
									tools.openST({
										title : '提示', // {String}
										text : '成功代偿！', // {String}
										type : 'success' // {String}
									});
									// 重新加载数据
									oTable1.fnClearTable(0);  
									oTable1.fnDraw(); 
								}else{
									tools.openST({
										title : '提示', // {String}
										text : data.msg, // {String}
										type : 'fail' // {String}
									});
								}
								$('#modal-compensatory').modal('hide');
								
							},
							error : function() {
								tools.openST({
									title : '提示', // {String}
									text : data.msg, // {String}
									type : 'fail' // {String}
								});
								resubmit = false;
							}
				  		});
				},
				/*发送通知需要增加界面元素的方法--lixj*/
				appendTemplateDiv:function(divId){
 				    var html = [];
					html.push('<div>');
				    html.push('<div class="col-sm-12 form-group">');
                    html.push('<div class="checkbox">');
                    html.push('<label>');
                    html.push('<input type="checkbox" id="emailCheck" name="emailCheck" checked="checked" onclick="return false;"> 邮件');              
                    html.push('</label>');          
                    html.push('</div>');      
                    html.push('</div>');  

                    html.push(' <div class="col-sm-12 form-group">'); 
                    html.push('<span class="color-green">选择模板：</span>');      
                          
                    html.push('<select name="emailTemplate" id="emailTemplate" class="common-select">');       
                    html.push('</select>');      
                          
                          
                    html.push('</div>');
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<span class="pull-left margin-t">邮件标题：</span>');      
                    html.push(' <input type="text" id="emailTitle" name="emailTitle" class="col-sm-6" placeholder="邮件标题" max-length="200">');     
                    html.push('</div>');  
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<textarea id="emailContent" name="emailContent">');      

                    html.push('</textarea>');      
                    html.push('</div>');  
                    html.push('</div>');
                    html.push('<div>');
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<div class="checkbox">');     
                    html.push('<label>');          
                    html.push('<input type="checkbox" id="smsCheck" name="smsCheck"> 短信');              
                    html.push('</label>');          
                    html.push('</div>');      
                    html.push('</div>');  

                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<span class="color-green">选择模板：</span>');      
                          
                    html.push('<select id="smsTemplate" name="smsTemplate" class="common-select">');     
                    html.push('</select>');      
                          
                          
                    html.push('</div>');
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<textarea class="col-sm-12" id="smsContent" name="smsContent" placeholder="短信内容">');      

                    html.push('</textarea>');      
                    html.push('</div>');  
                    html.push('</div>');
                    html.push('<div>');
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<div class="checkbox">');      
                    html.push('<label>');         
                    html.push('<input type="checkbox"  id="messageCheck" name="messageCheck"> 站内信');              
                    html.push('</label>');          
                    html.push('</div>');      
                    html.push('</div>'); 

                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<span class="color-green">选择模板：</span>');    
                          
                    html.push('<select id="messageTemplate" name="messageTemplate" class="common-select">');     
                    html.push('</select>');      
                          
                          
                    html.push('</div>');  
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push(' <span class="pull-left margin-t">站内信标题：</span>');     
                    html.push('<input type="text" id="messageTitle" name="messageTitle" class="col-sm-6" placeholder="站内信标题" max-length="255">');      
                    html.push('</div>');  
                    html.push('<div class="col-sm-12 form-group">');  
                    html.push('<textarea class="col-sm-12" id="messageContent" name="messageContent"  placeholder="站内信内容">');      

                    html.push('</textarea>');      
                    html.push('</div>');  
                    html.push('</div>'); 
					$('#' + divId).html(html.join(''));
					
					editor = $('#emailContent').wangEditor();
					
					// 为下拉列表绑定事件
					bind.bindSelectChangeEvent();
				},
				getHistoryList:function(loanNo){
					var url = contentPath +"/faOverdueInfo/getHistoryList";
			  		var param = {};
			  		param.loanNo = loanNo;
			  		param.state = 0;
			  		 $.ajax({
							url :url,
							type : 'POST',
							data :param,
							dataType : 'json',
							success : function(data){
								action.fillHistoryList(data);
							},
							error : function() {
							}
				  		});
				},
				fillHistoryList:function(data){
					data = data.data;
					var html = [];
					var tbody = $('#history-modal-form tbody');
					
					if(data.length>0){
						for ( var int = 0; int < data.length; int++) {
							html.push(action.createTr(data[int],int+1));
						}
						tbody.html(html.join(''));
					}else{
						tbody.html('<tr><td class="c-align" colspan="4">无逾期历史记录！</td></tr>');
					}
				},
				createTr : function(data,count) {
					var tr = [];
					tr.push('<tr>');
					tr.push('<td class="c-align"> ');
					tr.push(count);
					tr.push('</td>');
					tr.push('<td class="c-align"> ');
					tr.push(data.expitedTime);
					tr.push('</td>');
					tr.push('<td class="c-align"> ');
					tr.push(data.overdueMoney);
					tr.push('</td>');
					tr.push('<td class="c-align">');
					if(data.type=='0'){
						tr.push('本金');
					}else if(data.type=='1'){
						tr.push('利息');
					}
					tr.push('</td>');
				tr.push('</tr>');
					return tr.join('');
				},
				/* 获取模版信息，填充信息--lixj */
				getTemplate:function(){
					var url = contentPath +"/faOverdueInfo/getTemplate";
			  		var param = {};
			  		param.sceneKey = "13";
			  		 $.ajax({
							url :url,
							type : 'POST',
							data :param,
							dataType : 'json',
							success : function(data){
								action.fillTemplateData(data);
							},
							error : function() {
							}
				  		});
				},
				
				/* 填充模版数据--lixj */
				fillTemplateData:function(data)
				{
					emailTemplate = data.data.emailTemplate;
					var selectElement = $('#emailTemplate');
					$("#emailTemplate option").remove();
					selectElement.append('<option value="0" selected="selected">自定义模版</option>');	
					for (var int = 0; int < emailTemplate.length; int++) {
						{
							if (emailTemplate[int].isDefault == 1)
							{
								selectElement.append('<option value="'+emailTemplate[int].tid+'" selected="selected">'+emailTemplate[int].tempName+'</option>');	
								$('#emailTitle').val(emailTemplate[int].title);
							
								// $('.wangEditor-textarea p').html( emailTemplate[int].content );
								$('.wangEditor-textarea p').remove();
								editor.append('<p>' + emailTemplate[int].content + '</p>');
							}
							else
							{
								selectElement.append('<option value="'+emailTemplate[int].tid+'">'+emailTemplate[int].tempName+'</option>');	
							}									
						}
					}
					
					smsTemplate = data.data.smsTemplate;
					selectElement = $('#smsTemplate');
					$("#smsTemplate option").remove();
					selectElement.append('<option value="0" selected="selected">自定义模版</option>');
					for (var int = 0; int < smsTemplate.length; int++) {
						{
							if (smsTemplate[int].isDefault == 1)
							{
								selectElement.append('<option value="'+smsTemplate[int].tid+'" selected="selected">'+smsTemplate[int].tempName+'</option>');
								$('#smsContent').val(smsTemplate[int].content);
							}
							else
							{
								selectElement.append('<option value="'+smsTemplate[int].tid+'">'+smsTemplate[int].tempName+'</option>');	
							}
																	
						}
					}
					
					messageTemplate = data.data.messageTemplate;
					selectElement = $('#messageTemplate');
					$("#messageTemplate option").remove();
					selectElement.append('<option value="0" selected="selected">自定义模版</option>');
					for (var int = 0; int < messageTemplate.length; int++) {
						{
							if (messageTemplate[int].isDefault == 1)
							{
								selectElement.append('<option value="'+messageTemplate[int].tid+'" selected="selected">'+messageTemplate[int].tempName+'</option>');	
								$('#messageTitle').val(messageTemplate[int].title);
								$('#messageContent').val(messageTemplate[int].content);
							}
							else
							{
								selectElement.append('<option value="'+messageTemplate[int].tid+'">'+messageTemplate[int].tempName+'</option>');	
							}
																	
						}
					}
				},
				// 验证邮件、短信和站内信是否为空
				verifySendMsg:function(){
					
					if ($('#emailCheck').prop('checked'))
					{
						if ($('#emailTitle').val() == '')
						{
							//textAreaBoolean = true;
							action.alert("请输入邮件标题");
							return false;
						}
						if ($('#emailTitle').val().length > 200)
						{
							//textAreaBoolean = true;
							action.alert("邮件标题不能输入超过200字符，请重新输入。");
							return false;
						}
					}
					
					if ($('#smsCheck').prop('checked'))
					{
						if ($('#smsContent').val() == '')
						{
							//textAreaBoolean = true;
							action.alert("请输入短信内容");
							return false;
						}
					}
					if ($('#messageCheck').prop('checked'))
					{	
						if ($('#messageTitle').val() == '')
						{
							//textAreaBoolean = true;
							action.alert("请输入站内信标题");
							return false;
						}
						if ($('#messageTitle').val().length > 255)
						{
							//textAreaBoolean = true;
							action.alert("站内信标题不能输入超过255字符，请重新输入。");
							return false;
						}
					}
					if ($('#emailCheck').prop('checked') && $('#messageCheck').prop('checked') && $('#emailContent').val() == '' && $('#messageContent').val() == '')
					{
						if (!confirm("邮件内容和站内信内容为空，您确认发送吗？")) {
							return false;
						} 
					}
					else if ($('#emailCheck').prop('checked') && $('#emailContent').val() == '')
					{
						if (!confirm("邮件内容为空，您确认发送吗？")) {						
							return false;
						} 
					}
					else if ($('#messageCheck').prop('checked') && $('#messageContent').val() == '')
					{
						if (!confirm("站内信内容为空，您确认发送吗？")) {
							return false;
						} 
					}
					
					return true;
				},
				alert : function(text) {
					textAreaBoolean = true;
				
					tools.openCT({
                        title: '提示',         // {String} required model title
                        text: text,   // {String} required model text
                        type: 'warning',
                        buttons: [              // {Array} required buttons, 可以有一个 button
		                            {
		                                text: '确定',     // {String} required button text
		                                fn: function () {                   // {Function} click function
		                                	return false;
		                                },
		                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
		                            }
 		                        ]
                    });
				},
				sendNotification : function(){
					var url = contentPath +"/faOverdueInfo/sendNotification";
			  		var param = {};
			  		param.busId = $('#busId').val();
			  		if ($('#emailCheck').prop('checked'))
			  		{
			  			param.emailCheck = "on";
			  		}
			  		else
			  		{
			  			param.emailCheck = "";
			  		}
			  		param.emailTitle = $('#emailTitle').val();
			  		param.emailContent = $('#emailContent').val();
			  		if ($('#smsCheck').prop('checked'))
			  		{
			  			param.smsCheck = "on";
			  		}
			  		else
			  		{
			  			param.smsCheck = "";
			  		}
			  		param.smsContent = $('#smsContent').val();
			  		if ($('#messageCheck').prop('checked'))
			  		{
			  			param.messageCheck = "on";
			  		}
			  		else
			  		{
			  			param.messageCheck = "";
			  		}
			  		param.messageTitle = $('#messageTitle').val();
			  		param.messageContent = $('#messageContent').val();
			  		 $.ajax({
							url :url,
							type : 'POST',
							data :param,
							dataType : 'json',
							success : function(data1){
								
								var data = data1.data;
								if (data.emailIsSend == 'error')
								{
									// 邮件发送失败
									action.alert('邮件发送失败');
								}
								
								if (data.smsIsSend == 'error')
								{
									// 短信发送失败	
									action.alert('短信发送失败');
								}
								
								if (data.messageIsSend == 'error')
								{
									 // 站内信发送失败
									action.alert('站内信发送失败');
								}
								tools.closeLoading();
								if(data1.status=='success'){
									tools.openST({
										title : '提示', // {String}
										text : '发送成功！', // {String}
										type : 'success' // {String}
									});
								}
								$('#warning-modal-form').modal('hide'); 
							},
							error : function() {
								tools.closeLoading();
								action.alert('网络异常');
							}
				  		});
				},
			};
	})
</script>