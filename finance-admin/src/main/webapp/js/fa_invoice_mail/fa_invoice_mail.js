$(function() {

	
	//表单验证
	var validator = {
		    validator : function(){
		        var varlidator = $('#invoice-form').validate({
		            rules : {
		                'invoiceNum' : {
		                    required : true
		                },
		                'money' : {
		                    required : true,
		                    number :true
		                },
		                'courierCompany' : {
		                    required : true
		                },
		                'courierNum' : {
		                    required : true
		                }
		            },
		            /* 设置错误信息 */
				    messages: {
				    	'invoiceNum' : {
							required : "请输入发票代码"
						},
		                'money' : {
		                    required : "请输入开票金额",
		                    number :"请输入数字"
		                },
		                'courierCompany' : {
		                    required : "请输入快递公司名称"
		                },
		                'courierNum' : {
		                    required : "请输入快递单号"
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
			 * 为编辑做事件绑定
			 */
			$("tbody").delegate(".edit-invoice", "click", function() {
				validator.reset();
				$('#id').val($(this).attr("data-id"))
				$('#modal-form').modal('show');
			});
			
			//为准入验证model隐藏绑定事件
			$('modal-form').on('hidden.bs.modal', function () {
				$('#invoiceNum').val('');
				$('#money').val('');
				$('#courierCompany').val('');
				$('#courierNum').val('');
				$('#remarks').val('');
				$('#id').val('');
			});
			/**
			 * 为编辑做事件绑定
			 */
			$("tbody").delegate(".invoice-detail", "click", function() {
				var data = {};
				data.id = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/faInvoiceMail/update',
					type : 'POST',
					data : data,
					dataType : 'json',
					success : function(data) {
						var invoice = data.data;
						$('#detail-bus-id').text(invoice.busId);
						$('#detail-com-name').text(invoice.comName);
						$('#detail-mail-address').text(invoice.mailAddress);
						$('#detail-invoice-num').text(invoice.invoiceNum);
						$('#detail-money').text(invoice.money);
						$('#detail-courier-company').text(invoice.courierCompany);
						$('#courier-num').text(invoice.courierNum);
						$('#detail-remark').text(invoice.remarks);
						$('#modal-form-detail').modal('show');
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
				if (!validator.valid()) {
					return false;
				}
				var param = {};
				param.invoiceNum = $('#invoiceNum').val();
				param.money = $('#money').val();
				param.courierCompany = $('#courierCompany').val();
				param.courierNum = $('#courierNum').val();
				param.remarks = $('#remarks').val();
				param.id = $('#id').val();
				$.ajax({
					url : contentPath + '/faInvoiceMail/create',
					type : 'POST',
					data : param,
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							$('#modal-form').modal('hide');
							location.href = contentPath + "/faInvoiceMail";
							
						}else{
							tools.openST({
		                        title: '操作失败',             // {String}
		                        text: '业务异常操作失败',       // {String}
		                        type: 'fail'                // {String}
		                    });
							
						}
					},
					error : function() {
						tools.openST({
	                        title: '服务器异常',             // {String}
	                        text: '服务器异常，操作失败',       // {String}
	                        type: 'fail'                // {String}
	                    });
						resSubmit=false;
					}
				});
			});

		}
	};
	build.bind();
});