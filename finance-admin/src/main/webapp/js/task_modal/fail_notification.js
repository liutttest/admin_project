var failNotification = {
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
			failNotification.bindSelectChangeEvent();
		},
		
		/* 为下拉列表绑定事件--lixj */
		bindSelectChangeEvent:function(){

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
		},
		
		/* 获取模版信息，填充信息--lixj */
		getTemplate:function(busId, scene){
			var url = contentPath +"/fwBusinessSxd/getTemplateByScene";
	  		var param = {};
//	  		param.busId =busId; 
	  		param.scene = scene;
	  		 $.ajax({
					url :url,
					type : 'POST',
					data :param,
					dataType : 'json',
					success : function(data) {
						failNotification.fillTemplateData(data);
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
		//初始化事件
		initEvent : function() {
			
	    	/**
	    	 * 点击"办理"按钮
	    	 */
	    	$('#handling').on('click', function (e) {
	    		result = comNotification.verifySendMsg();
				if (result == false){
					return false;
				}
				
				// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
				
				tools.openLoading();
				var url = contentPath + "/activitiSxd/task/sendFailMsg";
				var param = $('#frm_main').serialize();
				$.ajax({
					url : url, 
					data : param,
					type : 'POST',
					dataType : 'json',
//					async : false,
					success : function(data) {
						/*submit = false;*/
						if (data.status === "success") {
							list.hideModal(); //关闭modal并刷新list数据
						}else{
							if(data.msg == "exception"){
								modalCommon.suspendTips($('#procInsId').val()); //是否需要挂起流程的提示
							}else{
								tools.openCT({
			                        title: '提示',         // {String} required model title
			                        text: data.msg,   // {String} required model text
			                        type: 'fail',        // {String} required 取值 success， fail， warning， default success
			                        buttons: [              // {Array} required buttons, 可以有一个 button
			                            {
			                                text: '确定',     // {String} required button text
			                                fn: function () {                   // {Function} click function
//			                                	location.href=contentPath+"/workflow/list";
			                                },
			                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
			                            }
			                        ]
			                    });
							}
						}
						tools.closeLoading(); // 关闭loading
						return;
					},
					error : function() {
						tools.openCT({
	                        title: '提示',         // {String} required model title
	                        text: '服务器异常，访问失败！',   // {String} required model text
	                        type: 'fail',        // {String} required 取值 success， fail， warning， default success
	                        buttons: [              // {Array} required buttons, 可以有一个 button
	                            {
	                                text: '确定',     // {String} required button text
	                                fn: function () {                   // {Function} click function
//	                                	location.href=contentPath+"/workflow/list";
	                                },
	                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
	                            }
	                        ]
	                    });
						tools.closeLoading();
					}
				});
            });
		},
		
		// 页面加载成功的数据查询
		initLoad:function(){
			// 增加发送通知的界面元素
			failNotification.appendTemplateDiv("fail-notification");
			
			// 请求数据，填充数据
			var scene = $('#scene').val();
//			var businessId = '201511240011';
			failNotification.getTemplate(businessId, scene); // 查询通知模板（邮件、短信、站内信的模板）
			
		},
};

jQuery(function($) {
	failNotification.initEvent();
	failNotification.initLoad();
});
