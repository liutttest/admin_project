$(function() {

	//表单验证
	var validator = {
		    validator : function(){
		        var varlidator = $('#admin-form').validate({
		            rules : {
		                'username' : {
		                    required : true
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
					url : contentPath + '/faRole/remove?ids=' + ids,
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							location.href = contentPath + "/faRole";
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
			$("tbody").delegate(".edit-role", "click", function() {
				validator.reset();
				var data = {};
				data.roleId = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/faRole/getByBk',
					type : 'POST',
					data : data,
					dataType : 'json',
					success : function(data1) {
						var data=data1.data;
						$('#role-name').val(data.faRole.roleName);
						$('#remark').val(data.faRole.roleRemarks);
						$('#roleId').val(data.faRole.roleId);
						$('#dept').val(data.faRole.deptId);
						//勾选已有权限
			            var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
						for (var i = 0; i < data.faFuncs.length; i++) {
							for (var j = 0; j < data.faRoleFuncs.length; j++) {
								if (data.faFuncs[i].funcCode == data.faRoleFuncs[j].faFunc.funcCode) {
									var node = treeObj.getNodeByParam("id",data.faFuncs[i].funcCode);
									treeObj.checkNode(node, true, false);
								}
							}
						}
						$('#edit-role-modal').text('编辑岗位信息');
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
				$('#edit-role-modal').text('添加角色');
				$('#modal-form').modal('show');
			});
			
			$('#modal-form').on('hidden.bs.modal', function () {
				validator.reset();
				$('#role-name').val('');
				$('#remark').val('');
				$('#roleId').val('');
				$('#dept').val('');
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	        	treeObj.checkAllNodes(false);
			});
			
			/**
			 * 为保存按钮做事件绑定
			 */
			$('#save-admin').on('click', function() {
				if (!validator.valid()) {
					return false;
				}
				var treeObj=$.fn.zTree.getZTreeObj("treeDemo"),
	            nodes=treeObj.getCheckedNodes(true);
	        	var nodeIds = '';
	            for(var i = 0; i < nodes.length; i++) {
	            	nodeIds += nodes[i].value + ",";
	            }
	            nodeIds = nodeIds.substring(0,nodeIds.length-1);
	            var param = {};
	            param.nodeIds = nodeIds;
	            param.roleId = $('#roleId').val();
	            param.roleName= $('#role-name').val();
	            param.roleRemarks= $('#remark').val();
	            param.deptId= $('#dept').val();
				
				$.ajax({
					url : contentPath + '/faRole/create',
					type : 'POST',
					data : param,
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							$('#modal-form').modal('hide');
							location.href = contentPath + "/faRole";
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