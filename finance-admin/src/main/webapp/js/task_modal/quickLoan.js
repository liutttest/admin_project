jQuery.validator.addMethod("validaIsHzZmSz", function (value, element) {
	return this.optional(element) || /^[a-zA-Z\u4e00-\u9fa5][a-zA-Z0-9\u4e00-\u9fa5]*$/.test(value);
}, "请输入汉字、字母或者数字");

var validBusinessArchive = {
	    validator : function(){
	        var varlidator = $('#frm_main').validate({
	        	
	            rules : {
	                'comName' : {
	                    maxlength : 50,
	                    validaIsHzZmSz : true
	                },
	            },
	            /* 设置错误信息 */
			    messages: {
					'comName' : {
			        	maxlength :"公司名称长度不能大于50",
			        	validaIsHzZmSz : "公司名称只能输入汉字、字母或者数字，并且不能以数字开头"
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


var quickLoan = {
		
		doFindCaoZuoInfo : function(businessId){
			/* ajax查询数据 */
			var url = contentPath + "/fwQuickLoan/getHanndleRecord";
			var param = {};
			
			param.buskey = businessId;// 业务id
			
			$.ajax({
				url : url, 
				data : param,
				type : 'POST',
				dataType : 'json',
				async : false,
				success : function(data) {
					quickLoan.buildTr(data);
					
					/* 公司名称 */
					$("#comName").val(data.data.comName);
					/*submit = false;*/
				},
				error : function() {
					alert('服务器异常，访问失败！');
					/*submit = false;*/
				}
			});
		},
		
		/* 创建操作记录表--lixj */
	  	buildTr : function(data) {
			data = data.data.list;
			var html = [];
			var tbody = $('.fw-handle-record');
			
			if(data.length>0){
				for ( var int = 0; int < data.length; int++) {
					html.push(quickLoan.createTr(data[int]));
				}
				tbody.html(html.join(''));
			}else{
				tbody.html('<tr><td class="c-align" colspan="4">无处理记录！</td></tr>');
			}
		},
		createTr : function(data) {
			var tr = [];
			tr.push(' <tr> <td class="c-align">');
			tr.push(data.handleUser);
			tr.push('</td>');
			tr.push('<td class="c-align"> ');
			tr.push(data.handleTime);
			tr.push('</td>');
			tr.push('<td class="c-align">');
			tr.push(data.handleContent);
			tr.push('</td>');
			tr.push('</tr>');
			return tr.join('');
		},
		
		//初始化事件
		initEvent : function() {
			
			/**
	    	 * 为快速贷款:办理并结束
	    	 */
	    	$('#handleQuickLoan').on('click', function (e) {
	    		
	    		if(!validBusinessArchive.valid()){
					return false;
				}
	    		
	    		if ($('#comment').val()=='') {
					//弹出不能为空的备注
	    			modalCommon.alertNotNull('请输入处理意见');
					return false;
				}
	    		
	    		// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
	    		
				tools.openLoading();
				var url = contentPath + "/activitiSxd/task/doQuickLoan";
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
			

			
	    	/**
	    	 * 点击"追加意见"按钮
	    	 */
	    	$('#appendRemark').on('click', function (e) {
	    		
	    		if(!validBusinessArchive.valid()){
					return false;
				}
	    		
	    		if ($('#comment').val()=='') {
					//弹出不能为空的备注
	    			modalCommon.alertNotNull('请输入处理意见');
					return false;
				}
	    		
	    		// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
	    		
				tools.openLoading();
				var url = contentPath + "/fwQuickLoan/appendQuickLoanRemarks";
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
			
			// 请求数据，填充数据
			var businessId = $('#businessId').val();
//			businessId = '59';
			quickLoan.doFindCaoZuoInfo(businessId); 
			
		},
};

jQuery(function($) {
	quickLoan.initEvent();
	quickLoan.initLoad();
});
