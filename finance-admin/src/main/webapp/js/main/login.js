$(function() {
	var submit = false;
	var superHtml = [];
	/**
	 * 匹配企业帐号，以字母开头，长度在6-20之间，只能包含字符、数字和下划线。
	 */
//	jQuery.validator.addMethod("userNameIsFormart", function(value, element) {
//		return this.optional(element) || /^[a-zA-Z]\w{5,19}$/.test(value);
//	}, "企业名称格式：以字母开头，长度在6-20字符之间，只能包含字符、数字和下划线。");
	/**
	 * 验证企业名称是否存在；
	 */
//	jQuery.validator.addMethod("userNameIsExist", function(value, element) {
//		return action.checkUserName();
//	}, "该企业名称不存在。");

	/**
	 * 匹配密码，长度在6-20之间。
	 */
//	jQuery.validator.addMethod("pwdIsFormart", function(value, element) {
//		return this.optional(element) || /^.{6,20}$/.test(value);
//	}, "企业密码格式：长度在6-20字符之间");

	/**
	 * 验证的调用方法 if(!validator.valid()){ return false; } 验证重新启动 validator.reset();
	 */
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
			this.validator = $('#form_login').validate({
				// ignore: [],//设置验证隐藏表单域
				rules : {
					'userName' : {
						required : true,
//						userNameIsFormart : true,
//						userNameIsExist : true
					},
					'password' : {
						required : true,
//						pwdIsFormart : true
					},
				},
				/* 设置错误信息 */
				messages : {
					'userName' : {
						required : "请输入用户名",
					},
					'password' : {
						required : "请输入密码",
					}
				},
	            errorClass : 'text-warning red',
	            errorPlacement : function(error, element){
	                element.closest('label').append(error);
	            }

			});
		}

	};
	validator.init();
	var action = {
		/**
		 * 验证该用户名是否已被使用
		 */
		checkUserName : function() {
			var url = contentPath + "/sysUser/testSysUserExist";
			var param = {};
			param.userName = $('#userName').val();
			param.type = $('#type').val();
			var valState = true;
			$.ajax({
				url : url,// 查询方法的url
				data : param, // params
				type : "post",
				dataType : "json",
				async : false,
				success : function(data) {
					if (data.data.exist) {
						valState = true;
					} else {
						valState = false;
					}
				}
			});
			return valState;
		}
	};

	var bind = {
		bind_event : function() {
			// 
			$('#btn_do_login').on('click', function(e) {
				e.preventDefault();
				if (!validator.validForm()) {
					return false;
				}
				if (submit) {
					return;
				}
				submit = true;
				$(this).attr('disabled', true);
				// 
				var url = contentPath + "/doLogin";
				var param = {};
				param.username = $("#userName").val(); // 登陆账号
				param.password = $("#password").val(); // 登陆密码
				param.type = $("#type").val(); // 用户类型
				$.ajax({
					url : url, 
					data : param,
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						submit = false;
						$('#btn_do_login').attr('disabled', false);
						if (data.status === "success") {
							window.location.href = contentPath + "/main";
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
			
			/**
			 * 企业名称获得焦点，将提示消息清空
			 */
			/*$('#userName').on('focus', function() {
				$('#userName').closest('div').find('span').remove();
				if($('#password').val()!=''){
					$('#password').closest('div').find('span').remove();
				}
			});
			
			*//**
			 * 企业密码获得焦点，将提示消息清空
			 *//*
			$('#password').on('focus', function() {
				$('#password').closest('div').find('span').remove();
			});*/
			
			

			/**
			 * 企业账号失去焦点事件
			 */
//			$('#userName').on('blur', function() {
//				validator.valid(this);
//			});

			/**
			 * 企业密码失去焦点事件
			 */
//			$('#password').on('blur', function() {
//				validator.valid(this);
//			});

		}

	};
	document.onkeydown = function(e){
        if(!e) e = window.event;//火狐中是 window.event
        if((e.keyCode || e.which) == 13){
            document.getElementById("btn_do_login").click();
        }
    }
	bind.bind_event();
});