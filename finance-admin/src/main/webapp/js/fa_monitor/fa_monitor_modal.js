$(function() {
	var build = {
		bind : function() {
			/**
			 * 为编辑做事件绑定
			 */
			$("tbody").delegate(".monitor-detail", "click", function() {
				var data = {};
				data.id = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/faMonitorLog/getByPk',
					type : 'POST',
					data : data,
					dataType : 'json',
					success : function(data) {
						var monitor = data.data.faMonitorLog;
						$('#monitor-title').text(monitor.title);
						$('#business-id').text(monitor.busId);
						$('#com-name').text(monitor.comName);
						$('#monitor-content').text(monitor.content);
						//$('#monitor-incontent').text(monitor.inContent);
						//$('#monitor-outcontent').text(monitor.outContent);
						//$('#bank-service').text(data.data.bank);
						$('#modal-form').modal('show');
					},
					error : function() {
						console.error('访问失败');
					}
				});

			});
			//为额度申请model隐藏绑定事件
			$('#modal-form').on('hidden.bs.modal', function () {
				$('#monitor-title').text('');
				$('#business-id').text('');
				$('#monitor-content').text('');
			});
		}
	};
	build.bind();
});