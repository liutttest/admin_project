$(function() {

	
	//表单验证
	var validator = {
		    validator : function(){
		        var varlidator = $('#admin-form').validate({
		            rules : {
		                'tempName' : {
		                    required : true
		                },
		                'content' : {
		                    required : true
		                }
		                
		            },
		            /* 设置错误信息 */
				    messages: {
				    	'tempName' : {
							required : "请输入模板标题"
						},
						'content' : {
							required : "请输入短信模板内容"
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
	
	var action = {
			/**
			 * //批量删除是  ，做checkbox是否选中验证使用
			 * @returns {Boolean}
			 */
			checkboxDelete: function(){
	        	 var trArray=$('#sample-table-2 tr input');
	                var num=0;
	                for (var int = 0; int < trArray.length; int++) {
						if(trArray[int].checked==true){
							return true;
						}else{
							num++;
							continue;
						}
					}
	                if(num==trArray.length){
	                	//提示信息
	                	//deletePromptMessage();
	                	return false;
	                }
	        }
	};
	var build = {
		bind : function() {
			/**
			 * 为设置默认做事件绑定
			 */
			$("tbody").delegate(".setDefault", "click", function() {
				if (confirm("确定将此模板设置为默认吗？")) {

				} else {
					return false;
				}
				var tid = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/faSmsTemplate/setDefault?tid=' + tid,
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							location.href = contentPath + "/faSmsTemplate";
						}
					},
					error : function() {
					}
				});

			});
			
			/**
			 * 为删除做事件绑定
			 */
			$("tbody").delegate(".delete", "click", function() {
				if (confirm("确定删除该项么？")) {

				} else {
					return false;
				}
				var tid = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/faSmsTemplate/remove?tid=' + tid,
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							location.href = contentPath + "/faSmsTemplate";
						}
						else if (data.status == 'error')
						{
							tools.openCT({
		                        title: '提示',         // {String} required model title
		                        text: data.msg,   // {String} required model text
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
						}
					},
					error : function() {
					}
				});

			});
			
			/**
			 * 为编辑做事件绑定
			 */
			$("tbody").delegate(".edit", "click", function() {
				validator.reset();
				var data = {};
				data.tid = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/faSmsTemplate/update',
					type : 'POST',
					data : data,
					dataType : 'json',
					success : function(data) {
						var faTemplate = data.data;
						$('#tempName').val(faTemplate.tempName);
						$('#tempSceneName').val(faTemplate.tempSceneId);
						$('#isDefault').val(faTemplate.isDefault);
						$('#content').val(faTemplate.content);
						$('#tid').val(faTemplate.tid);
						$('#edit-modal').text('修改短信模板');
						$('#modal-form').modal('show');
					},
					error : function() {
						console.error('访问失败');
					}
				});

			});
			
			/**
			 * 为添加按钮做事件绑定
			 */
			$('.add').on('click',function(){
				validator.reset();
				$('#tid').val('');
				$('#tempName').val('');
				$('#tempSceneName').val('1');
				$('#isDefault').val('1');
				$('#content').val('');
				$('#edit-modal').text('添加站内信模板');
				$('#modal-form').modal('show');
			});
			
			/**
			 * 为保存按钮做事件绑定
			 */
			$('#save').on('click', function() {
				if (!validator.valid()) {
					return false;
				}
				var param = {};
				param.tempName = $('#tempName').val();
				param.tempSceneName = $('#tempSceneName').val();
				param.isDefault = $('#isDefault').val();
				param.content = $('#content').val();
				param.tid = $('#tid').val();
				$.ajax({
					url : contentPath + '/faSmsTemplate/create',
					type : 'POST',
					data : param,
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							$('#modal-form').modal('hide');
							location.href = contentPath + "/faSmsTemplate";
						}
					},
					error : function() {
					}
				});
			});
		}
	};
	build.bind();
});