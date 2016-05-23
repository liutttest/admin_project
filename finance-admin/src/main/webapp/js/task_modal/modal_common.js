/**
 * 新工作流弹出层公共的js(luy)
 */
var modalCommon = {
		
		/**
		 * 验证备注信息不可输入特殊字符
		 * @returns {Boolean}
		 */
		validComment : function(comment){
			if (comment.length > 0) {
				if (/[@#\$%\^&\*]+/g.test(comment)) {
	    			modalCommon.alertNotNull('备注信息只能输入汉字、字母或者数字');
					return false;
				}
			}
		},
		
		/**
		 * 是否需要挂起流程的提示(luy)
		 * @param procInsId 流程实例id
		 */
		suspendTips : function(procInsId){
			tools.openCT({
                title: '提示', 
                text: "系统异常，是否将流程设置为挂起状态，并通知管理员", 
                type: 'warning', 
                buttons: [ 
                    {
                        text: '稍后重试',    
                        fn: function () {     
                        },
                        isClose: true    
                    },
                    {
                        text: '挂起', 
                        fn: function () {  
                        	modalCommon.suspend(procInsId); // 挂起流程实例
                        },
                        isClose: true 
                    }
                    
                ]
            });
		},
		
		/**
		 * 挂起流程实例(luy)
		 * @param procInsId 流程实例id
		 */
		suspend: function(procInsId){
			var param={};
			param.procInsId=procInsId;
			tools.openLoading();
			$.ajax({
				url : contentPath+'/workflowInstance/suspend',
				data :param ,
				type : 'POST',
				dataType : 'json',
				success : function(data) {
					data = data.state;
					if(data=='1'){
						list.hideModal(); //关闭modal并刷新list数据
					}else if(data=='2'){
						tools.openCT({
	                        title: '提示',   
	                        text: '该流程已经被挂起！', 
	                        type: 'warning',  
	                        buttons: [ 
	                            {
	                                text: '确定',
	                                fn: function () {  
//	                                	location.href=contentPath+"/workflow/list";
	                                },
	                                isClose: true  
	                            }
	                        ]
	                    });
					}else{
						tools.openCT({
	                        title: '提示',   
	                        text: '该流程挂起失败！', 
	                        type: 'fail',   
	                        buttons: [   
	                            {
	                                text: '确定',  
	                                fn: function () {  
//	                                	location.href=contentPath+"/workflow/list";
	                                },
	                                isClose: true   
	                            }
	                        ]
	                    });
					}
					tools.closeLoading();
				},
				error :function(data){
					tools.openCT({
                        title: '提示', 
                        text: '服务器异常，该流程挂起失败！', 
                        type: 'fail',  
                        buttons: [ 
                            {
                                text: '确定',  
                                fn: function () {    
//                                	location.href=contentPath+"/workflow/list";
                                },
                                isClose: true 
                            }
                        ]
                    });
					tools.closeLoading();
				}
			});
		},
		
		/**
		 * 提示不能为空
		 * @param text
		 */
		alertNotNull : function(text) {
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
		
		
		//初始化事件
		initEvent : function() {
			/**
			 * 流程列表中点击“查看资料”，在当前选项卡中打开（清除cookie中存的tab连接）
			 */ 
			$("tbody").delegate('.flow-apply-detail','click',function(){
				$.cookie('the_cookie', '#accountInfo', {path: '/' }); 
				location.href = $(this).attr('data-href');
				
			});
			/**
			 * modal中点击“查看资料”，在新选项卡中打开
			 */
			$(".div-bus-detail").delegate('.flow-apply-detail-toOtherPage','click',function(){
				$.cookie('the_cookie', '#accountInfo', {path: '/' }); 
				window.open($(this).attr('data-href'));
			});
		}
};

jQuery(function($) {
	modalCommon.initEvent();
});
