$.validator.addMethod("validateEqual",function(value,element,params){
     var actual = $('#loanNew_serviceChargeActual').val();
	if(actual==$("#js-hidden-service-charge").val()){
		return true;
	}else{
		return false;
	}
},"实收服务费与应收服务费相等");

jQuery.validator.addMethod("validaIsZmSz", function (value, element) {
	return this.optional(element) || /^[A-Za-z0-9]+$/.test(value);
}, "请输入字母或者数字");

var validDataBackfill = {
	    validator : function(){
	        var varlidator = $('#frm_main').validate({
	        	
	            rules : {
	                'loanNew_contractNum' : {
	                    required : true,
	                    maxlength : 40,
	                    validaIsZmSz : true
	                },
	                'loanNew_serviceChargeActual' : {
	                	number : true,
	                	validateEqual : true,
	                },
	            },
	            /* 设置错误信息 */
			    messages: {
					'loanNew_contractNum' : {
			        	required : "请输入合同号",
			        	maxlength : "合同号最大长度为40个字符",
			        	validaIsZmSz : "请输入字母或者数字" 
					},
					'loanNew_serviceChargeActual' : {
						number : "请输入数字"
					},
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

var dataBackfill = {
		/**
		 * 页面加载，查询服务费
		 * @param busId
		 */
		getBusinessBybusId:function(busId){
	    	var url = contentPath +"/fwBusinessSxd/getBusinessByBusId";
	  		var param = {};
	  		param.busId = busId;
	  		 $.ajax({
					url :url,
					data : param,
					type : 'POST',
					async:false,
					dataType : 'json',
					success : function(data) {
						var busData = data.data;
						$('#service-charge').text(busData.serviceCharge);
						$('#js-hidden-service-charge').val(busData.serviceCharge);
					},
					error : function() {
					}
		  		});
	    },
		
		
		//初始化事件
		initEvent : function() {
			/**
        	 * 为“信息回填”层中的“是否缴纳服务费”的复选框绑定事件
        	 */
        	//$("tbody").delegate('#js-chk-isPayServiceMoney', "change", function(e){
        	$('#js-chk-isPayServiceMoney').on("click", function(e){
        		 if($("#js-chk-isPayServiceMoney").is(':checked')==true){
        			 $('#js-div-isPayServiceMoney').show();
        		 }else{
        			 $('#js-div-isPayServiceMoney').hide();
        		 }
        	});
	    	
	    	/**
	    	 * 点击"办理"按钮
	    	 */
	    	$('#handling').on('click', function (e) {
	    		
	    		if(!validDataBackfill.valid()){
					return false;
				}
	    		
	    		// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
				
				tools.openLoading();
				var url = contentPath + "/activitiSxd/task/dataBackfill";
				var param = $('#frm_main').serialize();
				/*if(submit){
					return false;
				}
				submit = true;*/
				
				$.ajax({
					url : url, 
					data : param,
					type : 'POST',
					dataType : 'json',
//					async : false,
					success : function(data) {
						/*submit = false;*/
						if (data.status === "success") {
							list.hideModal(); //关闭modal并刷新list数据
						}else{
							if(data.msg == "exception"){
								modalCommon.suspendTips($('#procInsId').val()); //是否需要挂起流程的提示
							}else{
								tools.openCT({
			                        title: '提示',         // {String} required model title
			                        text: data.msg,   // {String} required model text
			                        type: 'fail',        // {String} required 取值 success， fail， warning， default success
			                        buttons: [              // {Array} required buttons, 可以有一个 button
			                            {
			                                text: '确定',     // {String} required button text
			                                fn: function () {                   // {Function} click function
//			                                	location.href=contentPath+"/workflow/list";
			                                },
			                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
			                            }
			                        ]
			                    });
							}
						}
						tools.closeLoading(); // 关闭loading
						return;
					},
					error : function() {
						tools.openCT({
	                        title: '提示',         // {String} required model title
	                        text: '服务器异常，访问失败！',   // {String} required model text
	                        type: 'fail',        // {String} required 取值 success， fail， warning， default success
	                        buttons: [              // {Array} required buttons, 可以有一个 button
	                            {
	                                text: '确定',     // {String} required button text
	                                fn: function () {                   // {Function} click function
//	                                	location.href=contentPath+"/workflow/list";
	                                },
	                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
	                            }
	                        ]
	                    });
						tools.closeLoading();
					}
				});
            });
	    	
	    	
		},
		
		initLoad:function(){
			var businessId = $('#businessId').val();
//			businessId = '201511240011';
			dataBackfill.getBusinessBybusId(businessId); //准入验证-查询要审核的资料(luy-)
		}
};

jQuery(function($) {
	dataBackfill.initEvent();
	dataBackfill.initLoad();
});
