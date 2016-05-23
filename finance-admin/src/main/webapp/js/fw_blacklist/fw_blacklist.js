$(function() {
	
	var action={
			buildTr : function(data) {
				data = data.data;
				var html = [];
				var tbody = $('.fw-black-detail');
				for ( var int = 0; int < data.length; int++) {
					html.push(action.createTr(data[int]));
				}
				tbody.html(html.join(''));
			},
			createTr : function(data) {
				var tr = [];
				tr.push(' <tr> <td>');
				tr.push(data.userName);
				tr.push('</td>');
				tr.push('<td class="c-align"> ');
				tr.push(data.addTime);
				tr.push('</td>');
				tr.push('<td class="c-align">');
				tr.push(data.removeTime);
				tr.push('</td>');
				if (data.isHistroy==1) {
					tr.push('<td class="c-align">');
					tr.push('<span class="label label-sm label-primary arrowed arrowed-right">是</span>');
					tr.push('</td>');
				}else if(data.isHistroy==0){
					tr.push('<td class="c-align">');
					tr.push('<span class="label label-sm label-pink arrowed arrowed-right">否</span>');
					tr.push('</td>');
				}
				tr.push('<td class="c-align">');
				tr.push(data.userName1);
				tr.push('</td>');
				tr.push('</tr>');
				return tr.join('');
			}
	};
	
	var build = {
		bind : function() {
			/**
			 * 为移除黑名单做绑定事件
			 */
			$("tbody").delegate(".edit-item", "click", function() {
				var that = this;
				if (confirm("确定移除黑名单么？")) {

				} else {
					return false;
				}
				var param = {};
				param.fbId = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/fwBlacklist/updateBlacklist',
					type : 'POST',
					data:param,
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							//location.href = contentPath + "/fwBlacklist";
							$(that).closest('tr').remove();
							//删除该行tr
						}else{
							alert(data.msg);
							
						}
					},
					error : function() {
					}
				});

			});
			/**
			 * 为详情做绑定事件
			 */
			$("tbody").delegate(".detail-item", "click", function() {
				var data = {};
				data.comId = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/fwBlacklist/gethistroyByComId',
					type : 'POST',
					data : data,
					dataType : 'json',
					success : function(data) {
						var sysUser = action.buildTr(data);
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
				$('#dept').val('');
				$('#modal-form').modal('show');
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
				param.pwd = $('#password').val();
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