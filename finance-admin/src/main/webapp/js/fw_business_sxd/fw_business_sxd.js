$(function() {
	var submit  =false;
	var validator = {
	    	validator_loanApply: function () {
                var varlidator = $('#frm-loanApply').validate({
                	ignore: [],
                    rules: {
                        'appMoney': {
                            required: true,
                            number:true,
                            min:1,
                            max:parseInt($('#usable-money').text()),
                        },
                    },
                    messages: {
                    	'appMoney': {
                            required: "请输入申请放款的额度",
                            number:"请输入数字格式",
                            min:"请输入大于0的数字",
                            max:"申请放款额度不能大于剩余额度",
                        },
                    },
                    errorClass: 'text-warning',
                    errorPlacement: function (error, element) {
                        element.closest('div').append(error);
                    }
                });
                return varlidator;
            },
	        valid_loanApply: function () {
	            return this.validator_loanApply().form();
	        },
	        reset_loanApply: function () {
	            this.validator_loanApply().resetForm();
	        },
	    };
	
	/**
	 * “设置放款次数”modal验证(luy)
	 */
	var validator_setLoanNo = {
	    	validator: function () {
                var varlidator = $('#frm-set-loan-NO').validate({
//                	ignore: [],
                    rules: {
                        'txt-loanNO': {
                            required: true,
                            number:true,
                            min:1,
                        },
                        'set-loan-NO_reason': {
                            required: true,
                            maxlength:250,
                        },
                    },
                    messages: {
                    	'txt-loanNO': {
                            required: "请输入放款次数",
                            number:"请输入数字格式",
                            min:"请输入大于0的数字",
                        },
                        'set-loan-NO_reason': {
                            required: "请输入备注",
                            maxlength: "备注请不要超过250个字符",
                        },
                    },
                    errorClass: 'text-warning',
                    errorPlacement: function (error, element) {
                        element.closest('div').append(error);
                    }
                });
                return varlidator;
            },
	        valid: function () {
	            return this.validator().form();
	        },
	        reset: function () {
	            this.validator().resetForm();
	        },
	    };
	
	/**
	 * “设置还款次数”modal验证(luy)
	 */
	var validator_setRepayNo = {
	    	validator: function () {
                var varlidator = $('#frm-set-repay-NO').validate({
//                	ignore: [],
                    rules: {
                        'set-repay-NO_reason': {
                            required: true,
                            maxlength:250,
                        },
                    },
                    messages: {
                        'set-repay-NO_reason': {
                            required: "请输入备注",
                            maxlength: "备注请不要超过250个字符",
                        },
                    },
                    errorClass: 'text-warning',
                    errorPlacement: function (error, element) {
                        element.closest('div').append(error);
                    }
                });
                return varlidator;
            },
	        valid: function () {
	            return this.validator().form();
	        },
	        reset: function () {
	            this.validator().resetForm();
	        },
	    };
	
	var action = {
			/**
			 * 查询操作日志
			 * 
			 * @returns {Boolean}
			 */
			doFindCaoZuoLog : function(bsId){
				var url = contentPath + "/fwBusinessSxd/getCaoZuoLog";
				var param = {};
				if(submit){
					return false;
				}
				submit = true;
				param.busId = bsId;// 业务Id
				
				$.ajax({
					url : url, 
					data : param,
					type : 'POST',
					dataType : 'json',
					async : false,
					success : function(data) {
						if (data.status === "success") {
							alert('申请放款成功');
							$('#loan-modal-form').modal('hide');
						} else {
							$('申请放款失败！');
						}
						submit = false;
					},
					error : function() {
						alert('服务器异常，申请失败！')
						submit = false;
					}
				});
			},
			/**
			 * 申请放款
			 */
			doFangKSQ : function(){
				var url = contentPath + "/fwLoanApply/saveFcdLoansInfo";
				var param = {};
				
				if(submit){
					return false;
				}
				tools.openLoading();
				submit = true;
				param.businessId = $('#fbId').val();// 业务Id
				param.businessType = '01'; // 业务类型
				param.appMoney = $('#appMoney').val(); // 申请金额
				param.monthsCount = $('#monthsCount').val();//贷款时长
				param.yearRate = $("#interest-rate").text(); // 年利率
				
				$.ajax({
					url : url, 
					data : param,
					type : 'POST',
					dataType : 'json',
					async : false,
					success : function(data) {
						tools.closeLoading();
						if (data.status === "success") {
							alert('申请放款成功');
							$('#loan-modal-form').modal('hide');
						} else {
							$('申请放款失败！');
						}
						submit = false;
					},
					error : function() {
						alert('服务器异常，申请失败！')
						submit = false;
					}
				});
			},
         /**
	      * 查询企业控制人或者，企业控制人配偶
	      */	
		  getPerDetail: function(url,busId){
		     //var url=contentPath+"/faAdmin/checkExists";
		     var param={};
		     param.busId=busId;
				$.ajax({
				        url: url,//查询方法的url
				        type: "post",
				        data:param,
				        dataType: "json",
				        async:false,
				        success: function (data) {
				        	if (data.status=='success') {
				        		var fwComPer = data.data.fwComPerBus;
					        	var file = data.data.file;
					        	$('#com-per-name').text(fwComPer.perName);
					        	if (fwComPer.documentType=='01') {
					        		$('#com-per-card-type').text('身份证');
								}else if(fwComPer.documentType=='02'){
									$('#com-per-card-type').text('护照');
								}
					        	if (fwComPer.sex=='01') {
					        		$('#com-per-sex').text('男');
								}else if(fwComPer.sex=='02'){
									$('#com-per-sex').text('女');
								}
					        	$('#com-per-card-num').text(fwComPer.documentNum);
					        	if (file['01']!=undefined) {
					        		$('#per-card').attr('src',contentPath+"/upload/"+file['01'][0]);
								}
					        	if (file['04']!=undefined) {
					        		$('#per-credit-power').attr('src',contentPath+"/upload/"+file['04'][0]);
								}
					        	
					        	$('#modal-form-2').modal('show');
							}else{
								alert(data.msg)
							}
				        	
				        }
				    });	
		  },
		  
		  /**
		   * 设置放款次数(luy)
		   */
		  doUpdateLoanNo : function(businessId){
				var url = contentPath + "/fwBusinessSxd/doUpdateLoanNo";
				var param = {};
				
				if(submit){
					return false;
				}
				tools.openLoading();
				submit = true;
				param.businessId = businessId;// 业务Id
				param.flag = $('input[name="rad-loanNO"]:checked').val();// 标识
				param.loanNo = $('#txt-loanNO').val();// 次数
				param.reason = $('#set-loan-NO_reason').val();// 备注
				
				$.ajax({
					url : url, 
					data : param,
					type : 'POST',
					dataType : 'json',
					async : false,
					success : function(data) {
						tools.closeLoading();
						if (data.status === "success") {
							$('#modal-set-loan-NO').modal('hide');
							tools.openST({
		                        title: '系统提示',             // {String}
		                        text: '修改放款次数成功！',       // {String}
		                        type: 'success'                // {String}
		                    });
						} else {
							tools.openST({
		                        title: '系统提示',             // {String}
		                        text: '修改放款次数失败！',       // {String}
		                        type: 'fail'                // {String}
		                    });
						}
						submit = false;
					},
					error : function() {
						tools.openST({
	                        title: '系统提示',             // {String}
	                        text: '服务器异常，修改放款次数失败！',       // {String}
	                        type: 'fail'                // {String}
	                    });
						submit = false;
					}
				});
			},
			
		  /**
		   * 设置还款次数(luy)
		   */
		  doUpdateRepayNo : function(businessId){
				var url = contentPath + "/fwBusinessSxd/doUpdateRepayNo";
				var param = {};
				
				if(submit){
					return false;
				}
				tools.openLoading();
				submit = true;
				param.businessId = businessId;// 业务Id
				param.flag = $('input[name="rad-repayNO"]:checked').val();// 标识
				param.loanNo = $('#txt-repayNO').val();// 次数
				param.reason = $('#set-repay-NO_reason').val();// 备注
				
				$.ajax({
					url : url, 
					data : param,
					type : 'POST',
					dataType : 'json',
					async : false,
					success : function(data) {
						tools.closeLoading();
						if (data.status === "success") {
							$('#modal-set-repay-NO').modal('hide');
							tools.openST({
		                        title: '系统提示',             // {String}
		                        text: '设置还款次数成功！',       // {String}
		                        type: 'success'                // {String}
		                    });
						} else {
							tools.openST({
		                        title: '系统提示',             // {String}
		                        text: '设置还款次数失败！',       // {String}
		                        type: 'fail'                // {String}
		                    });
						}
						submit = false;
					},
					error : function() {
						tools.openST({
	                        title: '系统提示',             // {String}
	                        text: '服务器异常，设置还款次数失败！',       // {String}
	                        type: 'fail'                // {String}
	                    });
						submit = false;
					}
				});
			},

	};
	var build = {
		bind : function() {
			
			/**
			 *liutt
			 *为操作记录做事件绑定
			 */
			$("tbody").delegate('.activiti-detail','click',function(){
				var procInsId = $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/otherGotoJsp?type=operation_record&procInsId="+procInsId,null,function(){
					$('#public-modal-form').modal('show');
				});
			});
			
			/**
			 * 为企业详情做事件绑定
			 */
			$("tbody").delegate(".company-detail", "click", function() {
				var param = {};
				param.busId = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/fwBusinessSxd/getfwCompanyBusDetail',
					type : 'POST',
					data:param,
					dataType : 'json',
					success : function(data) {
						if (data.status == 'success') {
							var company = data.data.fwCompanyBus;
				        	var file = data.data.file;
				        	$('#com-name').text(company.comName);
				        	if (company.state==0) {
				        		$('#com-status').text('已认证');
							}else if(company.state==1){
								$('#com-status').text('认证中');
							}else if(company.state==2){
								$('#com-status').text('认证失败');
							}
				        	
				        	$('#com-record').text(company.seore);
				        	$('#com-level').text(company.lev+"级");
				        	$('#com-business').text(company.business);
				        	$('#com-tax').text(company.taxNumber);
				        	$('#last-year-profit').text(formatCurrency(company.lastYearProfit));
				        	$('#last-year-money').text(formatCurrency(company.lastYearMoney));
				        	if (file['02']!=undefined) {
				        		$('#com-credit-power').attr('src',contentPath+"/upload/"+file['02'][0]);
							}
				        	if (file['03']!=undefined) {
				        		$('#com-financial').attr('src',contentPath+"/upload/"+file['03'][0]);
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
			 * 申请放款做事件绑定
			 */
			$("tbody").delegate(".apply-loan", "click", function() {
				$('#apply-money').html($(this).attr('data-apply'));
				$('#usable-money').html($(this).attr('data-usable'));
				$('#interest-rate').html($(this).attr('data-rate'));
				$('#fbId').val($(this).attr('data-busId'));
				validator.reset_loanApply();
				$('#appMoney').val('');
				$('#monthsCount').val(1);
				$('#loan-modal-form').modal('show');
			});
			
			/**
			 * 企业控制人配偶做事件绑定
			 */
			$("tbody").delegate(".com-owner-spouse", "click", function() {
				var busId = $(this).attr('data-id');
				var url = contentPath+"/fwBusinessSxd/fwCompanyBusOwnerSpouseDetail";
				$('#com-company-per').text('企业控制人配偶详情');
				action.getPerDetail(url, busId);
			});
			
			$('.submit-apply').on('click',function(e){
				e.preventDefault();
				if (!validator.valid_loanApply()) {
                    return false;
                }
				action.doFangKSQ();
			});
			
			/**
			 * 点击查看资料，清除cookie中存的tab连接
			 */
			$("tbody").delegate('.flow-apply-detail','click',function(){
//				e.preventDefault();
//				action.delCookie('the_cookie');
//				$(this).attr('href',$(this).attr('data-href'));
//				$.cookie('path','/');
//				$.cookie('the_cookie', '#accountInfo');
				
				$.cookie('the_cookie', '#accountInfo', {path: '/' }); 
				
				location.href = $(this).attr('data-href');
				
			});
			
			/**
			 * 为企业详情modal隐藏绑定事件
			 */
			$('#modal-form').on('hidden.bs.modal', function () {
				$('#com-name').text('');
				$('#com-status').text('');
	        	$('#com-record').text('');
	        	$('#com-level').text('');
	        	$('#com-business').text('');
	        	$('#com-tax').text('');
	        	$('#last-year-profit').text('');
	        	$('#last-year-money').text('');
        		$('#com-credit-power').attr('src','');
        		$('#com-financial').attr('src','');
			});
			/**
			 * 为企业控制人，控制人配偶详情modal隐藏绑定事件
			 */
			$('#modal-form-2').on('hidden.bs.modal', function () {
				$('#com-per-name').text('');
				$('#com-per-card-type').text('');
				$('#com-per-sex').text('');
	        	$('#com-per-card-num').text('');
        		$('#per-card').attr('src','');
        		$('#per-credit-power').attr('src','');
			});
			
			/**
			*为待办任务列表流程跟踪图片绑定点击事件
			*
			*/
			 $("tbody").delegate('.process-definition', "click", function(){
				var dataUrl=$(this).attr('data-urlid');
				$('#processdefinition-modal-title').html("流程跟踪图("+$(this).attr('data-data')+")");
				$('#processdefinition-picture').attr('src',contentPath+'/workflow/instanceDiagram/'+dataUrl);
				$('#picture-modal-form').modal('show');
			});
			
			$('#picture-modal-form').on('hidden.bs.modal', function () {
				$('#processdefinition-picture').attr('src','');
			});
			
			/**
			 * 为“设置放款次数”按钮绑定事件，弹出设置放款次数的modal(luy)
			 */
			$("tbody").delegate(".a-set-loan-NO", "click", function() {
				validator_setLoanNo.reset(); //重置验证
				// 重置modal中的信息
				$('input[name="rad-loanNO"]').each(function(i,val){
					 if(val.value == "0" ){
						 val.checked = true;
					 }
				});
				$('#txt-loanNO').val('');
				$('#set-loan-NO_reason').val('');
				
				$('#businessIdForSetLoanNO').val($(this).attr('data-busId'));
				$('#modal-set-loan-NO').modal('show');
			});
			
			/**
			 * 为“放款次数”单选按钮绑定事件，是否显示填写放款次数(luy)
			 */
			$("div").delegate(".rad-loanNO", "click", function() {
				var radLoanNo = $('input[name="rad-loanNO"]:checked').val();
				if (radLoanNo == "-3") {
					$('#inp-set-loanNO').show();
				}else{
					$('#inp-set-loanNO').hide();
				}
			});
			
			/**
			 * 为“设置放款次数”modal中的“确认”按钮绑定事件(luy)
			 */
			$('.submit-set-loan-NO').on('click',function(e){
				e.preventDefault();
				if (!validator_setLoanNo.valid()) {
                    return false;
                }
				action.doUpdateLoanNo($('#businessIdForSetLoanNO').val());
			});
			
			
			/**
			 * 为“设置还款次数”按钮绑定事件，弹出设置还款次数的modal(luy)
			 */
			$("tbody").delegate(".a-set-repay-NO", "click", function() {
				validator_setRepayNo.reset(); //重置验证
				// 重置modal中的信息
				$('input[name="rad-repayNO"]').each(function(i,val){
					 if(val.value == "0" ){
						 val.checked = true;
					 }
				});
				$('#txt-repayNO').val('');
				$('#set-repay-NO_reason').val('');
				
				$('#businessIdForSetRepayNO').val($(this).attr('data-busId'));
				$('#modal-set-repay-NO').modal('show');
			});
			
			/**
			 * 为“设置还款次数”modal中的“确认”按钮绑定事件(luy)
			 */
			$('.submit-set-repay-NO').on('click',function(e){
				e.preventDefault();
				if (!validator_setRepayNo.valid()) {
                    return false;
                }
				action.doUpdateRepayNo($('#businessIdForSetRepayNO').val());
			});
			
		}
	};
	build.bind();
});