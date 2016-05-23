$(function() {
	var build = {
		bind : function() {
			
			/**
			 * 为加入黑名单做事件绑定(liutt)
			 */
			$("tbody").delegate(".remove-to-black", "click", function() {
				var that = this;
				if (confirm("确定移入黑名单么？")) {

				} else {
					return false;
				}
				var param = {};
				param.comId = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/fwComAccount/createBlacklist',
					type : 'POST',
					data:param,
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							//location.href = contentPath + "/fwBlacklist";
							$(that).closest('tr').find('.blacklist').text('是');
							$(that).remove();
						}else{
							alert(data.msg);
							
						}
					},
					error : function() {
					}
				});

			});
			/**
			 * 为账户详情做事件绑定
			 */
			$("tbody").delegate(".company-detail", "click", function() {
				var param = {};
				param.comId = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/fwComAccount/getComaccountDetail',
					type : 'POST',
					data:param,
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							var company = data.data.fwCompany;
							var sysUser = data.data.sysUser;
				        	/*$('#com-name').text(company.comName);*/
				        	$('#user-name').text(sysUser.userName);
				        	$('#real-name').text(sysUser.realName);
				        	$('#user-tel').text(sysUser.tel);
				        	$('#user-email').text(sysUser.email);
				        	if(sysUser.isTelValida==1){
				        		$('#is-user-tel').html('<span class="label label-sm label-primary arrowed arrowed-right">已验证</span>');
				        	}else{
				        		$('#is-user-tel').html('<span class="label label-sm label-pink arrowed arrowed-right">未验证</span>');
				        	}
				        	if(sysUser.isEmailValida==1){
				        		$('#is-user-email').html('<span class="label label-sm label-primary arrowed arrowed-right">已验证</span>');
				        	}else{
				        		$('#is-user-email').html('<span class="label label-sm label-pink arrowed arrowed-right">未验证</span>');
				        	}
				        	
				        	$('#modal-form').modal('show');
						}else{
							alert(data.msg);
							
						}
					},
					error : function() {
					}
				});

			});
			
			/**
			 * 禁用企业用户做事件绑定(liutt)
			 */
			$("tbody").delegate(".disableAccount", "click", function() {
				var that = $(this);
				if (confirm("确定禁用该用户么？")) {

				} else {
					return false;
				}
				var param = {};
				param.userId = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/faAdmin/disableAccount',
					type : 'POST',
					data:param,
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							//location.href=contentPath+"/faAdmin/disableAccount";
							$(that).closest('tr').find('.disableList').html('<span class="label label-sm label-pink arrowed arrowed-right">否</span>');
							$(that).closest('tr').find('.dropdown-close').append('<li><a href="#" class="tooltip-error revertAccount" data-id="'+param.userId+'" data-rel="tooltip" title="恢复使用">'+
											'<span class="red"><i class="ace-icon fa fa-history bigger-120"></i></span></a></li>');
							$(that).closest('tr').find('.action-buttons').append('<a class="green revertAccount" data-id="'+param.userId+'" href="#" title="恢复使用">'+
								'<i class="ace-icon fa fa-history bigger-130"></i></a>');
							$(that).remove();
						}else{
							alert(data.msg);
							
						}
					},
					error : function() {
						alert('服务器异常');
					}
				});

			});
			
			
			/**
			 * 恢复使用企业用户做事件绑定(liutt)
			 */
			$("tbody").delegate(".revertAccount", "click", function() {
				var that = $(this);
				if (confirm("确定恢复该禁用用户么？")) {

				} else {
					return false;
				}
				var param = {};
				param.userId = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/faAdmin/revretAccount',
					type : 'POST',
					data:param,
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							//location.href=contentPath+"/faAdmin/disableAccount";
							$(that).closest('tr').find('.disableList').html('<span class="label label-sm label-primary arrowed arrowed-right">是</span>');
							$(that).closest('tr').find('.dropdown-close').append('<li><a href="#" class="tooltip-error disableAccount" data-id="'+param.userId+'" data-rel="tooltip" title="禁用">'+
							'<span class="red"><i class="ace-icon fa fa-ban bigger-120"></i></span></a></li>');
							$(that).closest('tr').find('.action-buttons').append('<a class="green disableAccount" data-id="'+param.userId+'" href="#" title="禁用">'+
								'<i class="ace-icon fa fa-ban bigger-130"></i></a>');
							$(that).remove();
						}else{
							alert(data.msg);
							
						}
					},
					error : function() {
						alert('服务器异常');
					}
				});

			});
			/**
			 * 为企业详情modal隐藏绑定事件
			 */
			$('#modal-form').on('hidden.bs.modal', function () {
				$('#com-name').text('');
				$('#user-name').text('');
	        	$('#user-tel').text('');
	        	$('#user-email').text('');
	        	$('#is-user-tel').html('');
	        	$('#is-user-email').html('');
	        	$('#real-name').text('');
			});
		}
	};
	build.bind();
});