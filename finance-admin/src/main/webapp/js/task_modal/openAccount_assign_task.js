var assignTask = {
		/**
		 * 页面加载，查询开户时间、地点等信息
		 * @param busId
		 */
		getDataCenterByBusId:function(busId){
	  		var url = contentPath +"/fwBusinessSxd/getDataCenterByBusId";
	  		var param = {};
	  		param.busId = busId;
	  		 $.ajax({
					url :url,
					data : param,
					type : 'POST',
					async:false,
					dataType : 'json',
					success : function(data) {
						var fieldData = data.data;
						$('#field-time').text(fieldData.fieldTime);
						$('#field-addr').text(fieldData.fieldAddr);
						$('#contract-name').text(fieldData.contractName);
						$('#contract-tel').text(fieldData.fieldTel);
						$('#field-remarks').text(fieldData.remarks);
					},
					error : function() {
					}
		  		});
	  	},
	  	
	  	/**
	  	 * 页面加载，查询可分配的人
	  	 */
	  	getAdminByRole:function(){
	  		var url = contentPath +"/faAdmin/getAdminByRole";
	  		 $.ajax({
					url :url,
					type : 'POST',
					async:false,
					dataType : 'json',
					success : function(data) {
						var sysUser = data.data;
						var html = [];
						html.push('<div class="form-group"><h4 class="blue bigger" style="float:left;margin-left:34px;">任务分配</h4></div>');
						for (var int = 0; int < sysUser.length; int++) {
							html.push('<div class="checkbox" style="float:left;margin-left:70px;clear:both;">');
							html.push('<label>');
							html.push('<input type="checkbox"  class="ace" name="task-person" value="'+sysUser[int].userId+'">');
							html.push('<span class="lbl"> &nbsp;&nbsp;&nbsp;'+sysUser[int].realName+'</span>');
							html.push('</label>');
							html.push('</div>');
						}
						
						$('#field-task-person').html(html.join(''));
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
	    		
	    		// 任务分派，提示至少分配给两个人
	    		if ($('input[name="task-person"]:checked').length < 2) {
	    			modalCommon.alertNotNull('任务分派，至少需要分派给两个人！');
					return false;
				}
	    		
	    		var chk_value =''; 
				$('input[name="task-person"]:checked').each(function(){ 
					chk_value+=($(this).val()+","); 
				});
				$('#loanNew_taskPersons').val(chk_value);
				
				// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
				
				tools.openLoading();
				var url = contentPath + "/activitiSxd/task/taskAssign";
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
			var businessId = $('#businessId').val();
//			businessId = '201511180012';
			assignTask.getDataCenterByBusId(businessId); //页面加载，查询开户时间、地点等信息(luy)
			assignTask.getAdminByRole(); //页面加载，查询可分配的人
		}
};

jQuery(function($) {
	assignTask.initEvent();
	assignTask.initLoad();
});
