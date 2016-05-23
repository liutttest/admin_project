var quotaManagerApproval = {
		
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
		
		/**
		* 经理审批-查询操作记录
		*/
		getCaoZuoLog : function(busId){
			var url = contentPath + "/fwBusinessSxd/getCaoZuoLog";
			var param = {};
			/*if(submit){
				return false;
			}
			submit = true;*/
			param.busId = busId;// 业务Id
			
			$.ajax({
				url : url, 
				data : param,
				type : 'POST',
				dataType : 'json',
				async : false,
				success : function(data) {
					quotaManagerApproval.buildTrForCaoZuoLog(data);
					/*submit = false;*/
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
//                                	location.href=contentPath+"/workflow/list";
                                },
                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
                            }
                        ]
                    });
					tools.closeLoading();
				}
			});
		},
		
		/**
		* 经理审批-填操作记录
		*/
		buildTrForCaoZuoLog : function(data) {
			data = data.data.faOperationRecords;
			var html = [];
			var tbody = $('.fw-black-detail-for-caozuolog');
			
			if(data.length>0){
				for ( var int = 0; int < data.length; int++) {
					html.push(quotaManagerApproval.createTrForCaoZuoLog(data[int],int+1));
				}
				tbody.html(html.join(''));
			}else{
				tbody.html('<tr><td class="c-align" colspan="6">无操作日志记录！</td></tr>');
			}
		},
		
		/**
		* 经理审批-填操作记录
		*/
		createTrForCaoZuoLog : function(data,count) {
			var tr = [];
			tr.push('<tr>');
				tr.push('<td class="c-align"> ');
				tr.push(count);
				tr.push('</td>');
				tr.push('<td class="c-align">');
				tr.push(data.checkNode);
				tr.push('</td>');
				tr.push('<td class="c-align">');
				tr.push(data.operName);
				tr.push('</td>');
				tr.push('<td class="c-align"> ');
				tr.push(data.operTime);
				tr.push('</td>');
				
				
	 			var state = data.operState;
	 			var stateStr = "";
	  			if(state != ""){
	  				stateStr = operationState[state].dictValue0;
	  			}
				
			  	tr.push('<td class="c-align">');
				tr.push(stateStr);
				tr.push('</td>');
				tr.push('<td class="c-align">');
				tr.push(data.checkReason);
				tr.push('</td>');
			tr.push('</tr>');
			return tr.join('');
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
				$("#loanNew_condition").val('true');//设置办理结果（1：通过；2：不通过；3：驳回；）
				var url = contentPath + "/activitiSxd/task/quota/quotaManagerApproval";
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
				$("#loanNew_condition").val('false');//设置办理结果（1：通过；2：不通过；3：驳回；）
				var url = contentPath + "/activitiSxd/task/quota/quotaManagerApproval";
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
			var businessId = $('#businessId').val();
			var procInsId = $('#procInsId').val();
			//查询操作记录
			quotaManagerApproval.showFlowListModel(procInsId);
		},
};

jQuery(function($) {
	quotaManagerApproval.initEvent();
	quotaManagerApproval.initLoad();
});
