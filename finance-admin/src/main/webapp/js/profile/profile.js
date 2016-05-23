$(function() {
	var submit = false;
	var superHtml = [];

	/**
     * 验证手机号
     * 1{10}  手机
     * 0{2，3}{7，8} or 0{2，3}-{7，8}
     * 0{2，3}{7，8}-{1，4} or 0{2，3}-{7，8}{1-4}
     */
    jQuery.validator.addMethod("tel", function(value, element) {
        return this.optional(element) || /^1\d{10}$/.test(value);
    }, "请输入正确的手机号");
    
    
  //表单验证
	var validator = {
		    validator : function(){
		    	var validator = $('#profile-form').validate({
					// ignore: [],//设置验证隐藏表单域
					rules : {
						'email' : {
							required : true,
							email : true
						},
						'tel' : {
							required : true,
							tel : true
						}
					},
					/* 设置错误信息 */
					messages : {
						'email' : {
							required : "请输入邮件。",
							email : "邮件格式错误。"
						},
						'tel' : {
							required : "请输 如电话号"
						}
					},
					errorClass : 'text-warning red',
	                errorPlacement : function(error, element){
	                	   element.closest('div').append(error);
	                }

				});
		        return validator;
		    },
		    valid : function(){
		        return this.validator().form();
		    },
		    reset : function(){
		        this.validator().resetForm();
		    }

	};

	/**
	 * 验证的调用方法 if(!validator.valid()){ return false; } 验证重新启动 validator.reset();
	 *//*
	var validator = {
		validator : null,
		valid : function($this) {
			return this.validator.element($this);
		},
		validForm : function() {
			return this.validator.form();
		},
		reset : function() {
			this.validator.resetForm();
		},
		init : function() {
			this.validator = $('#profile-form').validate({
				// ignore: [],//设置验证隐藏表单域
				rules : {
					'email' : {
						required : true,
						email : true
					},
					'tel' : {
						required : true,
						tel : true
					}
				},
				 设置错误信息 
				messages : {
					'email' : {
						required : "请输入邮件。",
						email : "邮件格式错误。"
					},
					'tel' : {
						required : "请输 如电话号"
					}
				},
                errorPlacement : function(error, element){
                    if(($(element).val() == "") || ( element.closest('div').find('span').length > 0 ) ){
                        element.closest('div').find('span').remove();
                    }
                    element.closest('div').append('<span style="color: red">' + error.text() + '</span>');
                }

			});
		}

	};
	validator.init();*/
	var bind = {
		bind_event : function() {
			// 
			$('#save-profile').on('click', function(e) {
				e.preventDefault();
				if (!validator.valid()) {
					return false;
				}
				if (submit) {
					return;
				}
				submit = true;
				$(this).attr('disabled', true);
				var url = contentPath + "/faAdmin/updateProfile";
				var param = {};
				param.userName = $("#userName").val(); // 登陆账号
				param.password = $("#password").val(); // 登陆密码
				param.email = $("#email").val(); // 邮件
				param.userId = $("#userId").val(); // 用户id
				param.tel = $("#tel").val(); // 用户id
				$.ajax({
					url : url, 
					data : param,
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						submit = false;
						$('#save-profile').attr('disabled', false);
						if (data.status === "success") {
							alert('修改成功');
							/*window.location.href = contentPath + "/main";*/
						} else {
							alert(data.msg);
							/*if(($('#password').val() == "") || ( $('#password').closest('div').find('span').length > 0 ) ){
								$('#password').closest('div').find('span').remove();
		                    }
							$('#password').closest('div').append('<span style="color: red">' + data.msg + '</span>');*/
							
						}
					},
					error : function() {
						window.location.href = contentPath + "/error";
					}
				});
			});

		}

	};
	bind.bind_event();
});