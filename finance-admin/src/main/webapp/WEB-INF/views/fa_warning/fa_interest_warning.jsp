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
			<li class="active">还息预警</li>
		</ul><!-- /.breadcrumb -->
		<!-- /section:basics/content.searchbox -->
	</div>

	<!-- /section:basics/content.breadcrumbs -->
	<div class="page-content">
	<div style="margin-bottom:10px;">
		<span>请选择月份：</span>
		<select id="month-select">
			
		</select>
	</div>

	<div class="row">
	<div class="col-xs-12 tabbable">
        <ul class="nav nav-tabs">
          <li role="presentation" class="active"><a class="day-tab" data-toggle="tab" data-value="18" href="#">18日</a></li>
          <li role="presentation"><a class="day-tab" data-toggle="tab" data-value="20" href="#">20日</a></li>
          <li role="presentation"><a class="day-tab" data-toggle="tab" data-value="21" href="#">21日</a></li>
        </ul>
	<div class="col-xs-12 tab-content">
	<div>
		<table id="sample-table-2" class="table table-striped table-bordered table-hover mytable">
			<thead>
				<tr>
					<th class="v-align col-sm-3 c-align">公司名称</th>
					<th class="v-align col-sm-2 c-align">借据号</th>
					<th class="v-align col-sm-2 c-align">放款日期</th>
					<th class="v-align col-sm-2 c-align">到期时间</th>
					<th class="v-align col-sm-1 c-align">逾期利息(元)</th>
					<th class="v-align col-sm-1 c-align">当前利息(元)</th>
					
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
		 </div>
	</div><!-- /.page-content -->
	
	<div id="warning-modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="warning-title">还息预警</h4>
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
	
</div><!-- /.main-content -->

<!-- 当前选择的日期 -->
<input id="day" type="hidden" value="18"/>

<!-- page specific plugin scripts -->
<script src="${contentPath }/assets/js/jquery.dataTables.min.js"></script>
<script src="${contentPath }/assets/js/jquery.dataTables.bootstrap.js"></script>
<link rel="stylesheet" type="text/css" href="${contentPath}/plugins/editor/css/wangEditor-1.3.11.css">
<script type="text/javascript" src='${contentPath}/plugins/editor/js/wangEditor-1.3.11.js'></script>

 <script type="text/javascript">
     var transactionType = '${transactionType}';
		var transactionTypes = JSON.parse(transactionType);
     
	jQuery(function($) {
		
		/* action.fillMonthSelect(); */
		/* 填充月份选择的下拉列表 */
		var date = new Date();
		var monthSelect = $("#month-select");
		for (var i = 0; i < 10; i++)
		{
			
			var month = date.getMonth()+1;
			if(month<=9){
				month="0"+month;
			}
			monthSelect.append('<option value='+ date.getFullYear() + '-' + month +'>' + date.getFullYear() + '-' + month + '</option>');
			date.setMonth(date.getMonth() - 1, date.getDate());
		}
		
		var oTable1 = 
		$('#sample-table-2')
		.dataTable( {
			"bAutoWidth": false,
			"aaSorting": [],
			"ordering" : false,
			"bServerSide" : true,
			"searching": true,
			"sAjaxSource" : contentPath + "/faWarning/getInterestList",
			"aoColumns" : [   
                           {"mData" : "comName"},   
                           {"mData" : "loanNo"},   
                           {"mData" : "applyPassTime"},   
                           {"mData" : "expitedTime"},   
                           {"mData" : "overdueInterest"},   
                           {"mData" : "currentInterest"},
                           {"mData" : "busId"}
                           ],
            "fnServerParams" : function(aoData) { 
            	aoData.push({  
                    "name" : "month",
                    "value" : $("#month-select").val()
                });
            	aoData.push({
            		"name":"day",
                    "value":$("#day").val()
            	});
            }, 
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
								   
								"sClass":"center",  
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
							   
								"sClass":"center",  
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
							   
								"sClass":"center",  
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
							   
								"sClass":"center",  
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
							   
								"sClass":"center",  
							    "aTargets":[4],  
							    "mData":"loanAmt",  
							    "mRender":function(a,b,c,d){
							    	
							    	return c.overdueInterest.toFixed(2);
							    }
						   },
						   {
							   
								"sClass":"center",  
							    "aTargets":[5],  
							    "mData":"principal",  
							    "mRender":function(a,b,c,d){
							    	return c.currentInterest.toFixed(2);
							    }
						   },
						   
                           {
                        	   
                        	   "sClass":"center",  
                               "aTargets":[6],  
                               "mData":"busId",  
                               "mRender":function(a,b,c,d){//a表示对应的值，c表示当前记录行对象
                            	   var currentDate = new Date();
									
                               	   //  判断选择的是不是当前的日期，如果是当前日期，则显示通知按钮
                               	   var current = currentDate.getFullYear() + "-" + (currentDate.getMonth() + 1) + "-" + currentDate.getDate();
                               	   var selectDate = $('#month-select').val() + "-" + $('#day').val();
                               	   /* return '<a data-busId="' + c.busId + '" class="green js－notify" href="#" >通知</a>'; */
                               	   if (current == selectDate)
                               	   {
                               			return '<a data-busId="' + c.busId + '" class="green js－notify" href="#" >通知</a>';
                               	   }
                               	   else
                               	   {
                               			return "";   
                               	   }
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
					
					// 日期月份绑定事件
					$(".day-tab").on("click", function(e){
						var day = $(this).attr("data-value");
						$("#day").val(day);
						
						// 重新加载数据
						oTable1.fnClearTable(0);  
						oTable1.fnDraw(); 
					});
					
					// 月份select绑定事件
					$("#month-select").on("change", function(e){ 
						$("#day").val("18");
						$('.tabbable .active').removeClass('active');
						$('.nav-tabs li:first').addClass('active');
						
						// 重新加载数据
						oTable1.fnClearTable(0);  
						oTable1.fnDraw(); 
					});
					
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
				fillMonthSelect:function(){
					var date = new Date();
					var monthSelect = $("#month-select");
					for (var i = 0; i < 10; i++)
					{
						var month = date.getMonth()+1;
						if(month<=9){
							month="0"+month;
						}
						monthSelect.append('<option value='+ date.getFullYear() + '-' + month +'>' + date.getFullYear() + '-' + month + '</option>');
						date.setMonth(date.getMonth() - 1, date.getDate());
					}
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
				
				/* 获取模版信息，填充信息--lixj */
				getTemplate:function(){
					var url = contentPath +"/faWarning/getTemplate";
			  		var param = {};
			  		param.sceneKey = "11";
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
					var url = contentPath +"/faWarning/sendNotification";
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
							success : function(data){
								
								var data = data.data;
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