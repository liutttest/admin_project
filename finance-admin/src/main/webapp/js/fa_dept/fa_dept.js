$(function() {

	
	//表单验证
	var validator = {
		    validator : function(){
		        var varlidator = $('#admin-form').validate({
		            rules : {
		                'deptName' : {
		                    required : true
		                }
		            },
		            /* 设置错误信息 */
				    messages: {
				    	'deptName' : {
							required : "请输入部门名称"
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
			 * 为删除做事件绑定
			 */
			$("tbody").delegate(".delete-dept", "click", function() {
				if (confirm("确定删除该项么？")) {

				} else {
					return false;
				}
				var ids = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/faDept/remove?ids=' + ids,
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							location.href = contentPath + "/faDept";
							//删除该行tr
						}
					},
					error : function() {
					}
				});

			});
			/**
			 * 为编辑做事件绑定
			 */
			$("tbody").delegate(".edit-dept", "click", function() {
				validator.reset();
				var data = {};
				data.deptId = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/faDept/update',
					type : 'POST',
					data : data,
					dataType : 'json',
					success : function(data) {
						var faDept = data.data;
						$('#deptName').val(faDept.deptName);
						$('#deptId').val(faDept.deptId);
						$('#edit-dept-modal').text('修改部门');
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
			$('.add-dept').on('click',function(){
				validator.reset();
				$('#deptName').val('');
				$('#deptId').val('');
				$('#edit-dept-modal').text('添加部门');
				$('#modal-form').modal('show');
			});
			
			/**
			 * 为保存按钮做事件绑定
			 */
			$('#save-dept').on('click', function() {
				if (!validator.valid()) {
					return false;
				}
				var param = {};
				param.deptId = $('#deptId').val();
				param.deptName = $('#deptName').val();
				$.ajax({
					url : contentPath + '/faDept/create',
					type : 'POST',
					data : param,
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							$('#modal-form').modal('hide');
							location.href = contentPath + "/faDept";
						}
					},
					error : function() {
					}
				});
			});

			//批量删除
			/*$('#remove-admin').on('click', function() {
				e.preventDefault();
                if(!that.checkboxDelete()){
                	return false;	
                }
				var param = {};
				param.userName = $('#userName1').val();
				param.pwd = $('#password1').val();
				$.ajax({
					url : contentPath + '/sysUser/create',
					type : 'POST',
					data : param,
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							location.href = contentPath + "/sysUser";
						}
					},
					error : function() {
					}
				});
			});*/
		}
	};
	build.bind();
});