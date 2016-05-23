/**
* 额度申请--客服通知（验证：开户时间、开户地点）（luy）
*/
var validatorQuotaService = {
	    validator : function(){
	        var varlidator = $('#frm_main').validate({
	            rules : {
	                'loanNew_fieldTime' : {
	                    required : true
	                },
	                'loanNew_fieldAddr' : {
	                    required : true,
	                    maxlength : 50
	                }
	            },
	            /* 设置错误信息 */
			    messages: {
			    	'loanNew_fieldTime' : {
						required : "请选择开户时间"
					},
					'loanNew_fieldAddr' : {
	                    required : "请输入开户地点",
	                    maxlength : "开户地点最多可输入50个字符"
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



var quotaNotification = {
		
		//初始化事件
		initEvent : function() {
			/**
			 * “开户时间”文本框内容改变的事件，去掉验证提示信息
			 */
			$('#loanNew_fieldTime').on('change', function (e) {
				var time = $('#loanNew_fieldTime').val();
				if (time.length > 0) {
					if ($('#fieldTime').find('label').hasClass('red')) {
						$('#fieldTime').find('label').remove();
					}
				}
            });
			
	    	/**
	    	 * 点击"办理"按钮
	    	 */
	    	$('#handling').on('click', function (e) {
	    		result = comNotification.verifySendMsg();
				if (result == false)
				{
					return false;
				}
				
				if(!validatorQuotaService.valid()){
					return false;
				}
				
				// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
				
				tools.openLoading();
				var url = contentPath + "/activitiSxd/task/quota/qutoaServiceCall";
				var param = $('#frm_main').serialize();
				/*if(submit){
					return false;
				}
				submit = true;*/
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
			/**
			 * 开户时间-时间控件
			 */
			$('#loanNew_fieldTime').datetimepicker({
		        language:  'zh-CN',
		        formate: 'YYYY-MM-DD HH:mm',
				autoclose: true,
		        startDate: new Date()		    
			});
			
			// 增加发送通知的界面元素
			comNotification.appendTemplateDiv("quota-apply-notification");
			
			// 请求数据，填充数据
			var businessId = $('#businessId').val();
//			var businessId = '201511240011';
			comNotification.getTemplate(businessId, "quota-apply"); // 查询准入验证成功的通知模板（邮件、短信、站内信的模板）
			
		},
};

jQuery(function($) {
	quotaNotification.initEvent();
	quotaNotification.initLoad();
});
