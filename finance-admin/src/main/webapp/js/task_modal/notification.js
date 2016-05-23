var notification = {
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
			notification.bindSelectChangeEvent();
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
		getTemplate:function(busId, type){
			var url = contentPath +"/fwBusinessSxd/accessVerify/getAccessVerifyTemplate";
	  		var param = {};
	  		param.busId =busId; 
	  		param.type = type;
	  		 $.ajax({
					url :url,
					type : 'POST',
					data :param,
					dataType : 'json',
					success : function(data) {
						// 额度申请结果
						
						quotaApplyResult = data.data.quotaApplyResult;
						accessVerifyResult = data.data.accessVerifyResult;
						
						// 额度申请成功或者失败显示隐藏开户时间和开户地点
						if (quotaApplyResult == 'fail')
						{
							$('#fieldTime').hide();
							$('#fieldAddr').hide();
							$('#quota-title').text('额度申请失败客服通知');
						}
						else if (quotaApplyResult == 'success')
						{
							$('#fieldTime').show();
							$('#fieldAddr').show();
							$('#quota-title').text('额度申请成功客服通知');
						}
						
						if (accessVerifyResult == 'fail')
						{
							$('#access-title').text('准入验证失败客服通知');
						}
						else if (accessVerifyResult == 'success')
						{
							$('#access-title').text('准入验证成功客服通知');
						}
						
						
						notification.fillTemplateData(data);
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
					modalCommon.alertNotNull("请输入邮件标题");
					return false;
				}
				if ($('#emailTitle').val().length > 200)
				{
					//textAreaBoolean = true;
					modalCommon.alertNotNull("邮件标题不能输入超过200字符，请重新输入。");
					return false;
				}
			}
			
			if ($('#smsCheck').prop('checked'))
			{
				if ($('#smsContent').val() == '')
				{
					//textAreaBoolean = true;
					modalCommon.alertNotNull("请输入短信内容");
					return false;
				}
			}
			if ($('#messageCheck').prop('checked'))
			{	
				if ($('#messageTitle').val() == '')
				{
					//textAreaBoolean = true;
					modalCommon.alertNotNull("请输入站内信标题");
					return false;
				}
				if ($('#messageTitle').val().length > 255)
				{
					//textAreaBoolean = true;
					modalCommon.alertNotNull("站内信标题不能输入超过255字符，请重新输入。");
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
		
		
		//初始化事件
		initEvent : function() {
			
	    	/**
	    	 * 点击"办理"按钮
	    	 */
	    	$('#notification-handling').on('click', function (e) {
	    		result = notification.verifySendMsg();
				if (result == false)
				{
					return false;
				}
				tools.openLoading();
				var url = contentPath + "/fwBusinessSxd/getBusFileByBusId";
				var param = $('#frm_notification').serialize();
				/*if(submit){
					return false;
				}
				submit = true;*/
				$.ajax({
					url : url, 
					data : param,
					type : 'POST',
					dataType : 'json',
					async : false,
					success : function(data) {
						/*submit = false;*/
						tools.closeLoading();
					},
					error : function() {
						alert('服务器异常，访问失败！');
						tools.closeLoading();
					}
				});
            });
	    	
	    	
	    	
			
		}
};

jQuery(function($) {
	notification.initEvent();
});
