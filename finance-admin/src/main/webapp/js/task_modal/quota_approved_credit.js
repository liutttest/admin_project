//表单验证 额度申请
var quotaAppCreValidator = {
	    validator : function(){
	        var varlidator = $('#frm_quota_approved_credit').validate({
	            rules : {
	                'loanNew_creditLimit' : {
	                    required : true,
	                    number: true,
	                    min:1,
	                    max:9999999999
	                }
	            },
	            /* 设置错误信息 */
			    messages: {
			    	'loanNew_creditLimit' : {
						required : "请输入授信金额",
						number :"请输入合法的数字",
						min:"授信金额不能小于1",
						max:"授信额度不能大于9999999999"
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

var quotaApproverCredit = {
		
		//初始化事件
		initEvent : function() {
			
	    	/**
	    	 * 点击"办理"按钮
	    	 */
	    	$('#handling').on('click', function (e) {
	    		
	    		if(!quotaAppCreValidator.valid()){
					return false;
				}
	    		
	    		// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
	    		
				tools.openLoading();
				$("#loanNew_condition").val('true');//设置办理结果（1：通过；2：不通过；3：驳回；）
				var url = contentPath + "/activitiSxd/task/quota/mranualCreditLimit";
				var param = $('#frm_quota_approved_credit').serialize();
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
	    		
	    		if(!quotaAppCreValidator.valid()){
					return false;
				}
	    		
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
				$("#loanNew_condition").val('false');//设置办理结果（1：通过；2：不通过；3：驳回；）
				var url = contentPath + "/activitiSxd/task/quota/mranualCreditLimit";
				var param = $('#frm_quota_approved_credit').serialize();
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

		},
};

jQuery(function($) {
	quotaApproverCredit.initEvent();
	quotaApproverCredit.initLoad();
});
