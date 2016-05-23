var taskReansferred = {
		
		//转办查询所有人
	  	getAdminByRoleAll:function(node){
	  		var url = contentPath +"/faAdmin/getAdminByRoleALL";
	  		var param = {};
	  		param.node =node; 
	  		$.ajax({
				url :url,
				type : 'POST',
				data :param,
				dataType : 'json',
				async : false,
				success : function(data) {
					var sysUser = data.data;
					var html = [];
					
					if (sysUser.length > 0) {
						html.push('<label class="col-sm-5 control-label no-padding-right" style="text-align:right;"> 将任务转给其他人 </label>');
						html.push('<div  class="col-sm-6"><select class="col-sm-8" id="personId" name="personId">');
						for (var int = 0; int < sysUser.length; int++) {
							var loginUserName = $('#loginUserName').val();
							if (loginUserName != sysUser[int].userName) {
								html.push('<option value="'+sysUser[int].userId+'">'+sysUser[int].realName+'</option>');										
							}
						}
						html.push('</select></div>');
					}else{
						html.push('<label class="col-sm-6 control-label no-padding-right" style="text-align:right;"> 该任务不可以转给其他人办理 </label>');
					}
					
					$('#turn-todo').html(html.join(''));
				},
				error : function() {
				}
	  		});
	  	},
		
		//初始化事件
		initEvent : function() {
			
	    	/**
	    	 * 点击"办理"按钮
	    	 */
	    	$('#handling').on('click', function (e) {
				tools.openLoading();
				var url = contentPath + "/activitiSxd/turnTodo";
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
//			var businessId = $('#businessId').val();
			var taskDefKey = $('#taskDefKey').val();
			taskReansferred.getAdminByRoleAll(taskDefKey); // 查询可以转办的人
			
		},
};

jQuery(function($) {
	taskReansferred.initEvent();
	taskReansferred.initLoad();
});
