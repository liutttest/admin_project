var accessNotification = {
		
		//初始化事件
		initEvent : function() {
			
	    	/**
	    	 * 点击"办理"按钮
	    	 */
	    	$('#access-notification-handling').on('click', function (e) {
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
				var url = contentPath + "/activitiSxd/task/access/service/phone/call";
				var param = $('#frm_access_notification').serialize();
				$.ajax({
					url : url, 
					data : param,
					type : 'POST',
					dataType : 'json',
//					async : false,
					success : function(data) {
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
			comNotification.appendTemplateDiv("customer-service-notify");
			
			// 请求数据，填充数据
			var businessId = $('#businessId').val();
//			var businessId = '201511240011';
			comNotification.getTemplate(businessId, "access-verify"); // 查询准入验证成功的通知模板（邮件、短信、站内信的模板）
			
		},
};

jQuery(function($) {
	accessNotification.initEvent();
	accessNotification.initLoad();
});
