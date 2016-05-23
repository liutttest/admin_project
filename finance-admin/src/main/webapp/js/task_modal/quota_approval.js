var accessNotification = {
		
		/**
		 * 查询操作记录
		 * @param procInsId 流程实例Id
		 */
		showFlowListModel : function(procInsId){
			$.getJSON(contentPath + "/workflowHistory/flowList", { procInsId: procInsId},
			  function(data){
				$("#rows").html("");
				$.each(data, function(i, o){  
					$("#rows").append(template('hisTemplate', o));    
				});  
				
			});
		},
		
		//初始化事件
		initEvent : function() {
			
	    	/**
	    	 * 点击"办理"按钮
	    	 */
	    	$('#handling').on('click', function (e) {
	    		
	    		// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
	    		
				tools.openLoading();
				$("#loanNew_condition").val('1');//设置办理结果（1：通过；2：不通过；3：驳回；）
				var url = contentPath + "/activitiSxd/task/quota/xaminationApproval";
				var param = $('#frm_quota_approval').serialize();
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
	    	
	    	/**
	    	 * 点击"不通过"按钮
	    	 */
	    	$('#nopass').on('click', function (e) {
	    		
	    		if ($(this).val()!='true' && $('#comment').val()=='') {
					//textAreaBoolean = true;
					//弹出不能为空的备注
	    			modalCommon.alertNotNull('请输入备注信息');
					return false;
				}
	    		
	    		// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
	    		
	    		if (!confirm("是否确定不通过该业务申请？")){
					return false;	
				}
	    		
				tools.openLoading();
				$("#loanNew_condition").val('2');//设置办理结果（1：通过；2：不通过；3：驳回；）
				var url = contentPath + "/activitiSxd/task/quota/xaminationApproval";
				var param = $('#frm_quota_approval').serialize();
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
							tools.openCT({
		                        title: '提示',         // {String} required model title
		                        text: data.msg,   // {String} required model text
		                        type: 'fail',        // {String} required 取值 success， fail， warning， default success
		                        buttons: [              // {Array} required buttons, 可以有一个 button
		                            {
		                                text: '确定',     // {String} required button text
		                                fn: function () {                   // {Function} click function
//		                                	location.href=contentPath+"/workflow/list";
		                                },
		                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
		                            }
		                        ]
		                    });
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
	    	
	    	/**
	    	 * 点击"驳回"按钮
	    	 */
	    	$('#rejected').on('click', function (e) {
	    		
	    		if ($(this).val()!='true' && $('#comment').val()=='') {
					//textAreaBoolean = true;
					//弹出不能为空的备注
	    			modalCommon.alertNotNull('请输入备注信息');
					return false;
				}
	    		
	    		// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
	    		
	    		if (!confirm("是否确定驳回该业务申请？")){
					return false;	
				}
	    		
				tools.openLoading();
				$("#loanNew_condition").val('3');//设置办理结果（1：通过；2：不通过；3：驳回；）
				var url = contentPath + "/activitiSxd/task/quota/xaminationApproval";
				var param = $('#frm_quota_approval').serialize();
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
							tools.openCT({
		                        title: '提示',         // {String} required model title
		                        text: data.msg,   // {String} required model text
		                        type: 'fail',        // {String} required 取值 success， fail， warning， default success
		                        buttons: [              // {Array} required buttons, 可以有一个 button
		                            {
		                                text: '确定',     // {String} required button text
		                                fn: function () {                   // {Function} click function
//		                                	location.href=contentPath+"/workflow/list";
		                                },
		                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
		                            }
		                        ]
		                    });
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
			var procInsId = $('#procInsId').val();
			//查询操作记录
			accessNotification.showFlowListModel(procInsId);
		},
};

jQuery(function($) {
	accessNotification.initEvent();
	accessNotification.initLoad();
});
