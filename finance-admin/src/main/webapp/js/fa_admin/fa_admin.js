$(function() {

	 /**
	 * 验证用户名是否重复
	 */
    jQuery.validator.addMethod("userNameDeplicate", function(value, element) {
    	if(action.checkUserName()==0){
			return false;
		}else if(action.checkUserName()==1){
			return true;
		}	  
    }, "该用户已被使用");
    
    
	/**
     * 验证手机号
     * 1{10}  手机
     * 0{2，3}{7，8} or 0{2，3}-{7，8}
     * 0{2，3}{7，8}-{1，4} or 0{2，3}-{7，8}{1-4}
     */
    jQuery.validator.addMethod("tel", function(value, element) {
        return this.optional(element) || /^1\d{10}$/.test(value);
    }, "请输入正确的手机号");
    
    /**
	 * 匹配密码，长度在6-20之间。
	 */
	jQuery.validator.addMethod("pwdIsFormart", function(value, element) {
		return this.optional(element) || /^.{6,20}$/.test(value);
	}, "密码格式：长度在6-20字符之间");
	
	//表单验证
	var validator = {
		    validator : function(){
		        var varlidator = $('#admin-form').validate({
		            rules : {
		                'username' : {
		                    required : true,
		                    userNameDeplicate:true
		                },
		                'realName' : {
		                    required : true
		                },
		                'tel' : {
		                    required : true,
		                    tel:true
		                },
		                'email' : {
		                    required : true,
		                    email:true
		                },
		                'dept' : {
		                    required : true
		                }
		            },
		            /* 设置错误信息 */
				    messages: {
				    	'username' : {
							required : "请输入用户名"
						},
						'realName' : {
							required : "请输入真实姓名"
						},
						'tel' : {
				        	required : "请输入手机号"
						},
						'email' : {
							required : "请输入邮箱",
							email:"请输入正确的邮箱"
						},
						'dept' : {
							required : "请选择部门"
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
	        },
         /**
	      * 验证该用户名是否可用
	      */	
		  checkUserName: function(){
		     var url=contentPath+"/faAdmin/checkExists";
		     var param={};
		     param.userName=$('#username').val();
		     param.userId = $('#userId').val();
		     param.type=0;
		     var valState='';
				$.ajax({
				        url: url,//查询方法的url
				        data: param, // params
				        type: "post",
				        dataType: "json",
				        async:false,
				        success: function (data) {
				        	if(data.data.exist){
				        		//该用户名可用
				        		valState = 1;
				        	}else{
				        		valState = 0;
				        	}
				        }
				    });	
			 return valState;
		  },
		  checkBoxRole: function(roles){
			  for (var int = 0; int < roles.length; int++) {
				var roleId = roles[int].roleId;
				$('input[name="rolename"]').each(function(){ 
					if($(this).val()==roleId){
						$(this).attr("checked",'checked')
					}
				});
			}
		  }

	};
	var build = {
		bind : function() {
			/**
			 * 为删除做事件绑定
			 */
			$("tbody").delegate(".delete-admin", "click", function() {
				if (confirm("确定删除该项么？")) {

				} else {
					return false;
				}
				var ids = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/faAdmin/remove?ids=' + ids,
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							location.href = contentPath + "/faAdmin";
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
			$("tbody").delegate(".edit-admin", "click", function() {
				validator.reset();
				var data = {};
				data.userId = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/faAdmin/getByBk',
					type : 'POST',
					data : data,
					dataType : 'json',
					success : function(data) {
						var faAdmin = data.data;
						$('#username').val(faAdmin.sysUser.userName);
						$('#tel').val(faAdmin.sysUser.tel);
						$('#userId').val(faAdmin.userId);
						$('#email').val(faAdmin.sysUser.email);
						$('#realName').val(faAdmin.sysUser.realName);
						//$('#password').val(sysUser.password);
						/*$('#dept').val(sysUser.deptId);*/
						action.checkBoxRole(faAdmin.faUserRoles);
						$('#edit-admin-modal').text('编辑用户信息');
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
			$('.add-admin').on('click',function(){
				validator.reset();
				$('#username').val('');
				$('#tel').val('');
				$('#userId').val('');
				$('#email').val('');
				$('#password').val('');
				$('#realName').val('');
				/*$('#dept').val('');*/
				$('#edit-admin-modal').text('添加用户');
				$('#modal-form').modal('show');
			});
			
			//为准入验证model隐藏绑定事件
			$('#modal-form').on('hidden.bs.modal', function () {
				//清空表单数据
				$('#username').val('');
				$('#tel').val('');
				$('#userId').val('');
				$('#email').val('');
				$('#password').val('');
				$('#realName').val('');
				$('input[name="rolename"]').each(function(){ 
						$(this).removeAttr("checked");
				});
			});
			/**
			 * 为保存按钮做事件绑定
			 */
			$('#save-admin').on('click', function() {
				if (!validator.valid()) {
					return false;
				}
				var param = {};
				param.userId = $('#userId').val();
				param.tel = $('#tel').val();
				param.email = $('#email').val();
				param.dept = $('#dept').val();
				param.userName = $('#username').val();
				param.realName = $('#realName').val();
				param.pwd = $('#password').val();
				var chk_value =''; 
				$('input[name="rolename"]:checked').each(function(){ 
				chk_value+=($(this).val()+"/"+$(this).attr('data-text')+","); 
				});
				chk_value=chk_value.substring(0, chk_value.length-1);
				param.role=chk_value;
				$.ajax({
					url : contentPath + '/faAdmin/create',
					type : 'POST',
					data : param,
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							$('#modal-form').modal('hide');
							location.href = contentPath + "/faAdmin";
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