jQuery.validator.addMethod("validaIsZmSz", function (value, element) {
	return this.optional(element) || /^[A-Za-z0-9]+$/.test(value);
}, "请输入字母或者数字");

var validBusinessArchive = {
	    validator : function(){
	        var varlidator = $('#frm_main').validate({
	        	
	            rules : {
	                'loanNew_archive' : {
	                    required : true,
	                    maxlength : 20,
	                    validaIsZmSz : true
	                },
	            },
	            /* 设置错误信息 */
			    messages: {
					'loanNew_archive' : {
			        	required : "请输入档案号",
			        	maxlength :"档案号长度不能大于20",
			        	validaIsZmSz : "请输入字母或者数字"
					},
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

var businessArchive = {
		
		//初始化事件
		initEvent : function() {
	    	
	    	/**
	    	 * 点击"办理"按钮
	    	 */
	    	$('#handling').on('click', function (e) {
	    		
	    		if(!validBusinessArchive.valid()){
					return false;
				}
				
	    		// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
				
				tools.openLoading();
				var url = contentPath + "/activitiSxd/task/businessArchive";
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
		
		initLoad:function(){
		}
};

jQuery(function($) {
	businessArchive.initEvent();
	businessArchive.initLoad();
});
