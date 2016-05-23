$(function() {
	
	var action={
			buildTr : function(data) {
				data = data.data.faOperationRecords;
				var html = [];
				var tbody = $('.fw-black-detail');
				
				if(data.length>0){
					for ( var int = 0; int < data.length; int++) {
						html.push(action.createTr(data[int],int+1));
					}
					tbody.html(html.join(''));
				}else{
					tbody.html('<tr><td class="c-align" colspan="6">无操作日志记录！</td></tr>');
				}
			},
			createTr : function(data,count) {
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
			}
	};
	
	var build = {
		bind : function() {
			/**
			 * 为详情做绑定事件
			 */
			$("div").delegate(".activiti-detail", "click", function() {
//				$('#activiti-modal-form').modal('show');
				var url = contentPath + "/fwBusinessSxd/getCaoZuoLog";
				var param = {};
				/*if(submit){
					return false;
				}
				submit = true;*/
				param.busId = $(this).attr('data-id');// 业务Id
				
				$.ajax({
					url : url, 
					data : param,
					type : 'POST',
					dataType : 'json',
					async : false,
					success : function(data) {
						var sysUser = action.buildTr(data);
						$('#activiti-modal-form').modal('show');
						/*submit = false;*/
					},
					error : function() {
						alert('服务器异常，访问失败！');
						/*submit = false;*/
					}
				});
				/*var data = {};
				data.proId = $(this).attr("data-id");
				$.ajax({
					var url = contentPath + "/fwBusinessSxd/getCaoZuoLog";
					type : 'POST',
					data : data,
					dataType : 'json',
					success : function(data) {
						var sysUser = action.buildTr(data);
						$('#activiti-modal-form').modal('show');
					},
					error : function() {
						console.error('访问失败');
					}
				});*/

			});
			
		}
	};
	build.bind();
});