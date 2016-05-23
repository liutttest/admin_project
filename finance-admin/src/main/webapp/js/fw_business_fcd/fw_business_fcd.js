$(function() {
	
	var action = {
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
		  }

	};
	var build = {
		bind : function() {
			
			/**
			 * 为删除做事件绑定
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
							$(that).closest('tr').find('.blacklist').text('YES');
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
			 * 为企业详情做事件绑定
			 */
			$("tbody").delegate(".company-detail", "click", function() {
				var param = {};
				param.busId = $(this).attr("data-id");
				$.ajax({
					url : contentPath + '/fwBusinessFcd/getfwCompanyBusDetail',
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
			 * 企业控制人详情做事件绑定
			 */
			$("tbody").delegate(".com-owner", "click", function() {
				var comId = $(this).attr('data-id');
				var url = contentPath+"/fwBusinessFcd/fwCompanyBusOwnerDetail";
				$('#com-company-per').text('企业控制人详情');
				action.getPerDetail(url, comId);
			});
			
			/**
			 * 企业控制人配偶做事件绑定
			 */
			$("tbody").delegate(".com-owner-spouse", "click", function() {
				var comId = $(this).attr('data-id');
				var url = contentPath+"/fwBusinessFcd/fwCompanyBusOwnerSpouseDetail";
				$('#com-company-per').text('企业控制人配偶详情');
				action.getPerDetail(url, comId);
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
		}
	};
	build.bind();
});