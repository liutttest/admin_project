$(function() {
	//表单验证
	var build = {
		bind : function() {
			/**
			 * 为编辑做事件绑定
			 */
			$("tbody").delegate(".edit-dept", "click", function() {
				var data = {};
				data.fbId = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/fwFeedback/getDetail',
					type : 'POST',
					data : data,
					dataType : 'json',
					success : function(data) {
						var feed = data.data;
						$('#feedId').val(feed.fdId);
						$('#modal-form').modal('show');
					},
					error : function() {
						console.error('访问失败');
					}
				});

			});
			
			/**
			 * 为保存按钮做事件绑定
			 */
			$('#save-dept').on('click', function() {
				var param = {};
				param.feedId = $('#feedId').val();
				param.remark = $('#feed-remark').val();
				$.ajax({
					url : contentPath + '/fwFeedback/updateState',
					type : 'POST',
					data : param,
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							$('#modal-form').modal('hide');
							location.href = contentPath + "/fwFeedback";
						}else{
						
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