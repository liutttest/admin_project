/**
* 准入验证-验证企业拥有的员工数（luy）
*/
var validator = {
	    validator : function(){
	        var varlidator = $('#frm_access_role_manual_verify').validate({
	            rules : {
	                'workers' : {
	                    required : true,
	                    digits : true,
	                    min : 1,
	                }
	            },
	            /* 设置错误信息 */
			    messages: {
			    	'workers' : {
						required : "请输入企业拥有员工数",
						digits : '企业拥有员工数必须为整数',
						min : '企业拥有员工数必须大于0',
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

var access_role_manual_verify = {
		
		/**
		 * "企业纳税信誉"下拉选值改变时的事件
		 */
		doChangeTaxCredit : function(){
			var taxCredit = $('#taxCredit').val();
			if (taxCredit == '01') {
				$('#engageLife').val('01');
				$('#engageLife').attr('disabled',true);
				$('#engageLife').attr("style", "background-color: gainsboro;");
				$("#engageLife").find("option[val='01']").attr("selected",true);
				$('#oweTax').val('01');
				$('#oweTax').attr('disabled',true);
				$('#oweTax').attr("style", "background-color: gainsboro;");
				$("#oweTax").find("option[val='01']").attr("selected",true);
			}else {
				$('#engageLife').removeAttr('disabled');
				$('#engageLife').removeAttr("style");
				$('#oweTax').removeAttr('disabled');
				$('#oweTax').removeAttr("style");
			}
		},
		
		/**
		 * 保存风控数据
		 */
		doSaveRisk : function(){
			tools.openLoading();
			
			var ret = false;
			
			$('#engageLife').removeAttr('disabled');
			$('#oweTax').removeAttr('disabled');
			
			var url = contentPath + "/activitiSxd/doSaveRisk";
			var param = $('#frm_access_role_manual_verify').serialize();
			
			$.ajax({
				url : url, 
				data : param,
				type : 'POST',
				async: false, 
				dataType : 'json',
				success : function(data) {
					tools.closeLoading();
					if (data.status === "success") {
						ret = true;
					}else{
						ret = false;
					}
				},
				error : function() {
					tools.closeLoading();
					ret = false;
				}
			});
			
			return ret;
		},
		
		/**
		 * 点击“驳回”、“不通过”按钮时，提示哪些资料被标记为不合格，是否确认进行操作
		 * @param msg
		 * @returns {Boolean}
		 */
		doConFirm : function(msg){
			var nopassNames = "";
			$('#div-access-file a.red').each(function(){
				nopassNames = nopassNames + $(this).attr('data-infoname') + '，';
			});
			
			if (nopassNames != "") {
				if (!confirm('以下资料被标记为不合格：' + nopassNames + msg)) {
					return false;	
				}else {
					return true;
				}
			}else {
				if (!confirm('当前没有资料被标记为不合格，' + msg)) {
					return false;	
				}else {
					return true;
				}
			}
		},
		
		/**
		* 准入验证，查询需要审核的文件 
		*/
		getFileForAccess : function(busId){
			var url = contentPath + "/fwBusinessSxd/getBusFileByBusId";
			var param = {};
			/*if(submit){
				return false;
			}
			submit = true;*/
			param.busId = busId;// 业务Id
			
			$.ajax({
				url : url, 
				data : param,
				type : 'POST',
				dataType : 'json',
				async : false,
				success : function(data) {
					var sysUser = access_role_manual_verify.buildTrForAccess(data);
					/*submit = false;*/
				},
				error : function() {
					alert('服务器异常，访问失败！');
					/*submit = false;*/
				}
			});
		},
		/**
		* 准入验证，查询需要审核的文件  
		*/
		createTrForAccess : function(data) {
			var tr = [];
			tr.push('<tr>');
				tr.push('<td class="c-align">1</td> ');
				tr.push('<td class="c-align">营业执照注册号</td>');
				if(data.licenseNum_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'001'+'"><a class="js-do-pass red" data-infoName="营业执照注册号" data-infoKey="001" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'001'+'"><a class="js-do-nopass" data-infoName="营业执照注册号" data-infoKey="001" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
				
			tr.push('<tr>');
				tr.push('<td class="c-align">2</td> ');
				tr.push('<td class="c-align">组织机构代码</td>');
				if(data.catdCode_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'003'+'"><a class="js-do-pass red" data-infoName="组织机构代码" data-infoKey="003" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'003'+'"><a class="js-do-nopass" data-infoName="组织机构代码" data-infoKey="003" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">3</td> ');
				tr.push('<td class="c-align">税号</td>');
				if(data.taxNum_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'002'+'"><a class="js-do-pass red" data-infoName="税号" data-infoKey="002" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'002'+'"><a class="js-do-nopass" data-infoName="税号" data-infoKey="002" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">4</td> ');
				tr.push('<td class="c-align">企业营业执照</td>');
				if(data.businessLicense_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'02'+'"><a class="js-do-pass red" data-infoName="企业营业执照" data-infoKey="02" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'02'+'"><a class="js-do-nopass" data-infoName="企业营业执照" data-infoKey="02" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">5</td> ');
				tr.push('<td class="c-align">组织机构代码证</td>');
				if(data.organiztionCode_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'04'+'"><a class="js-do-pass red" data-infoName="组织机构代码证" data-infoKey="04" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'04'+'"><a class="js-do-nopass" data-infoName="组织机构代码证" data-infoKey="04" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">6</td> ');
				tr.push('<td class="c-align">税务登记证</td>');
				if(data.taxRegistartion_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'03'+'"><a class="js-do-pass red" data-infoName="税务登记证" data-infoKey="03" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'03'+'"><a class="js-do-nopass" data-infoName="税务登记证" data-infoKey="03" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">7</td> ');
				tr.push('<td class="c-align">近三年财务报表</td>');
				if(data.lastYearStatements_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'06'+'"><a class="js-do-pass red" data-infoName="近三年财务报表" data-infoKey="06" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'06'+'"><a class="js-do-nopass" data-infoName="近三年财务报表" data-infoKey="06" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">8</td> ');
				tr.push('<td class="c-align">涉税保密信息查询申请表</td>');
				if(data.taxRelatedQuery_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'01'+'"><a class="js-do-pass red" data-infoName="涉税保密信息查询申请表" data-infoKey="01" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'01'+'"><a class="js-do-nopass" data-infoName="涉税保密信息查询申请表" data-infoKey="01" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">9</td> ');
				tr.push('<td class="c-align">征信查询授权书</td>');
				if(data.companyCredit_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'05'+'"><a class="js-do-pass red" data-infoName="征信查询授权书" data-infoKey="05" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'05'+'"><a class="js-do-nopass" data-infoName="征信查询授权书" data-infoKey="05" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			/*
			tr.push('<tr>');
				tr.push('<td class="c-align">10</td> ');
				tr.push('<td class="c-align">法人征信查询授权书</td>');
				if(data.ledalCredit_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'10'+'"><a class="js-do-pass red" data-infoName="法人征信查询授权书" data-infoKey="10" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'10'+'"><a class="js-do-nopass" data-infoName="法人征信查询授权书" data-infoKey="10" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			*/
			
			tr.push('<tr>');
				tr.push('<td class="c-align">10</td> ');
				tr.push('<td class="c-align">法人证件照片</td>');
				if(data.ledalCard_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'07'+'"><a class="js-do-pass red" data-infoName="法人证件照片" data-infoKey="07" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'07'+'"><a class="js-do-nopass" data-infoName="法人证件照片" data-infoKey="07" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">11</td> ');
				tr.push('<td class="c-align">控制人及配偶征信报告查询授权书</td>');
				if(data.controllerCredit_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'11'+'"><a class="js-do-pass red" data-infoName="控制人及配偶征信报告查询授权书" data-infoKey="11" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'11'+'"><a class="js-do-nopass" data-infoName="控制人及配偶征信报告查询授权书" data-infoKey="11" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">12</td> ');
				tr.push('<td class="c-align">控制人证件照片</td>');
				if(data.controllerCard_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'08'+'"><a class="js-do-pass red" data-infoName="控制人证件照片" data-infoKey="08" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'08'+'"><a class="js-do-nopass" data-infoName="控制人证件照片" data-infoKey="08" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">13</td> ');
				tr.push('<td class="c-align">控制人配偶身份证照片</td>');
				if(data.controllerSpoCard.length<=0){
					tr.push('<td class="c-align js-caozuo-access'+'14'+'">无</td>');
				}else{
					if(data.controllerSpoCard_ispass==0){
						tr.push('<td class="c-align js-caozuo-access'+'09'+'"><a class="js-do-pass red" data-infoName="控制人配偶身份证照片" data-infoKey="09" href="#">已标记为不合格</a></td>');
					}else{
						tr.push('<td class="c-align js-caozuo-access'+'09'+'"><a class="js-do-nopass" data-infoName="控制人配偶身份证照片" data-infoKey="09" href="#">标记为不合格</a></td>');
					}
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">14</td> ');
				tr.push('<td class="c-align">控制人学历证明</td>');
				if(data.controllerEducation_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'30'+'"><a class="js-do-pass red" data-infoName="控制人学历证明" data-infoKey="30" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'30'+'"><a class="js-do-nopass" data-infoName="控制人学历证明" data-infoKey="30" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">15</td> ');
				tr.push('<td class="c-align">控制人户籍证明</td>');
				if(data.controllerDomicilePlace_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'31'+'"><a class="js-do-pass red" data-infoName="控制人户籍证明" data-infoKey="31" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'31'+'"><a class="js-do-nopass" data-infoName="控制人户籍证明" data-infoKey="31" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			
			return tr.join('');
		},
		/**
		* 准入验证，查询需要审核的文件  
		*/
		buildTrForAccess : function(data) {
			data = data.data;
			var html = [];
			var tbody = $('.access-file-for-nopass');
			html.push(access_role_manual_verify.createTrForAccess(data));
			tbody.html(html.join(''));
		},
		/**
    	 * 将资料“标记为不合格”
    	 * @param busId 业务id
    	 * @param infoName 资料名称
    	 * @param infoKey 资料key
    	 */
		doUpdateNoPass : function(busId, infoName, infoKey,updateHtmlClass){
			var url = contentPath + "/fwBusinessSxd/doUpdateNoPass";
            var param = {};
            param.busId = busId; //业务id
            param.infoName = infoName; // 资料名称
            param.infoKey = infoKey; // 资料key

            $.ajax({
                url: url,
                data: param,
                type: 'POST',
                dataType: 'json',
                async : false,
                success: function (data) {
                    submit = false;
                    if (data.status === "success") {
                    	tools.openCT({
	                        title: '提示',   
	                        text: '标记成功', 
	                        type: 'success',  
	                        buttons: [ 
	                            {
	                                text: '确定',  
	                                fn: function () {   
//	                                	location.href=contentPath+"/workflow/list";
	                                },
	                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
	                            }
	                        ]
	                    });
                    	
                    	 $('.'+updateHtmlClass+infoKey).empty();
                         $('.'+updateHtmlClass+infoKey).html('<a class="js-do-pass red" data-infoName="'+infoName+'" data-infoKey="'+infoKey+'" href="#">已标记为不合格</a>');
                    }
                    else {
                    	tools.openCT({
	                        title: '提示',  
	                        text: '标记失败！', 
	                        type: 'fail', 
	                        buttons: [  
	                            {
	                                text: '确定', 
	                                fn: function () {   
//	                                	location.href=contentPath+"/workflow/list";
	                                },
	                                isClose: true 
	                            }
	                        ]
	                    });
                    }
                },
                error: function (data) {
                    submit = false;
                    tools.openCT({
                        title: '提示', 
                        text: '标记出现异常',
                        type: 'warning',
                        buttons: [ 
                            {
                                text: '确定',
                                fn: function () { 
//                                	location.href=contentPath+"/workflow/list";
                                },
                                isClose: true 
                            }
                        ]
                    });
                }
            });
		},
		
		
		/**
    	 * 将不合格资料标记为合格
    	 * @param busId 业务id
    	 * @param infoName 资料名称
    	 * @param infoKey 资料key
    	 */
		doUpdatePass : function(busId, infoName, infoKey,updateHtmlClass){
			var url = contentPath + "/fwBusinessSxd/doUpdateNoPassToPass";
            var param = {};
            param.busId = busId; //业务id
            param.infoKey = infoKey; // 资料key

            $.ajax({
                url: url,
                data: param,
                type: 'POST',
                dataType: 'json',
                async : false,
                success: function (data) {
                    submit = false;
                    if (data.status === "success") {
                    	tools.openCT({
	                        title: '提示', 
	                        text: '标记成功', 
	                        type: 'success',  
	                        buttons: [ 
	                            {
	                                text: '确定', 
	                                fn: function () {
//	                                	location.href=contentPath+"/workflow/list";
	                                	$('.'+updateHtmlClass+infoKey).empty();
	                                	$('.'+updateHtmlClass+infoKey).html('<a class="js-do-nopass" data-infoName="'+infoName+'" data-infoKey="'+infoKey+'" href="#">标记为不合格</a>');
	                                },
	                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
	                            }
	                        ]
	                    });
                    }
                    else {
                    	tools.openCT({
	                        title: '提示',   
	                        text: '标记失败！', 
	                        type: 'fail',  
	                        buttons: [ 
	                            {
	                                text: '确定', 
	                                fn: function () { 
//	                                	location.href=contentPath+"/workflow/list";
	                                },
	                                isClose: true  
	                            }
	                        ]
	                    });
                    }
                },
                error: function (data) {
                    submit = false;
                    tools.openCT({
                        title: '提示',    
                        text: '标记出现异常', 
                        type: 'warning',  
                        buttons: [    
                            {
                                text: '确定', 
                                fn: function () {  
//                                	location.href=contentPath+"/workflow/list";
                                },
                                isClose: true  
                            }
                        ]
                    });
                }
            });
		},
		
		
		//初始化事件
		initEvent : function() {
			/**
	    	 * 点击"标记为不合格"(luy)
	    	 */
	    	$("tbody").delegate('.js-do-nopass', "click", function(e){
	            e.preventDefault();
	            var busId = $('#businessIdForFlag').val();
	            var infoName = $(this).attr("data-infoName");
	            var infoKey = $(this).attr("data-infoKey");
	            access_role_manual_verify.doUpdateNoPass(busId, infoName, infoKey,"js-caozuo-access");
	        });
	    	
	    	/**
	    	 * “已标记为不合格”的点击事件，将其再变回“标记为不合格”
	    	 */
	    	$("tbody").delegate('.js-do-pass', "click", function(e){
	            e.preventDefault();
	            var busId = $('#businessIdForFlag').val();
	            var infoKey = $(this).attr("data-infoKey");
	            var infoName = $(this).attr("data-infoName");
	            access_role_manual_verify.doUpdatePass(busId, infoName, infoKey,"js-caozuo-access");
	        });
	    	
	    	/**
	    	 * "企业纳税信誉"下拉选值改变时的事件
	    	 */
	    	$('#taxCredit').on('change', function (e) {
	    		access_role_manual_verify.doChangeTaxCredit();
	    	});
	    	
	    	/**
	    	 * 点击"办理"按钮
	    	 */
	    	$('#access-role-manual-verify-handling').on('click', function (e) {
	    		// 提示有资料标记为不合格，是否确认通过
				if ($(this).val() == 'true' && $('#div-access-file').css('display') != 'none' && $('#div-access-file').find('a').hasClass('red')){
					if (!confirm("您有标记为不合格的资料，确认通过吗？")){
						return false;	
					}
				}
				
				// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
				// 验证企业拥有员工数是否填写
				if(!validator.valid()){
					return false;
				}
				
				// 保存风控数据信息
				if(!access_role_manual_verify.doSaveRisk()){
					tools.openCT({
                        title: '提示', 
                        text: '服务器异常，访问失败！', 
                        type: 'fail', 
                        buttons: [ 
                            {
                                text: '确定',
                                fn: function () { 
                                },
                                isClose: true
                            }
                        ]
                    });
					return false;
				}

				tools.openLoading();
				$("#loanNew_condition").val('1');//设置办理结果（1：通过；2：不通过；3：驳回；）
				var url = contentPath + "/activitiSxd/task/access/manual/verify";
				var param = $('#frm_access_role_manual_verify').serialize();
				
				$.ajax({
					url : url, 
					data : param,
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						if (data.status === "success") {
							
							if (data.data.result == "2") {
								tools.openCT({
			                        title: '提示',   
			                        text: '该业务不符合准入条件，未能通过申请', 
			                        type: 'fail', 
			                        buttons: [ 
			                            {
			                                text: '确定',
			                                fn: function () { 
//			                                	location.href=contentPath+"/workflow/list";
			                                },
			                                isClose: true 
			                            }
			                        ]
			                    });
							}
							
							list.hideModal(); //关闭modal并刷新list数据
						}else{
							if(data.msg == "exception"){
								modalCommon.suspendTips($('#procInsId').val()); //是否需要挂起流程的提示
							}else{
								tools.openCT({
			                        title: '提示',   
			                        text: data.msg, 
			                        type: 'fail', 
			                        buttons: [ 
			                            {
			                                text: '确定',
			                                fn: function () { 
//			                                	location.href=contentPath+"/workflow/list";
			                                },
			                                isClose: true 
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
	                        title: '提示', 
	                        text: '服务器异常，访问失败！', 
	                        type: 'fail', 
	                        buttons: [ 
	                            {
	                                text: '确定',
	                                fn: function () {
//	                                	location.href=contentPath+"/workflow/list";
	                                },
	                                isClose: true
	                            }
	                        ]
	                    });
						tools.closeLoading();
					}
				});
            });
	    	
	    	/**
	    	 * 点击"不通过"按钮
	    	 */
	    	$('#access-role-manual-verify-nopass').on('click', function (e) {
	    		
	    		if ($(this).val()!='true' && $('#comment').val()=='') {
					//弹出不能为空的备注
	    			modalCommon.alertNotNull('请输入备注信息');
					return false;
				}
	    		// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
	    		
				// 提示"标记不合格资料"，并确认是否进行不通过操作
				if (!access_role_manual_verify.doConFirm('您是否确认不通过该业务申请？')) {
					return false;
				}
				
				// 验证企业拥有员工数是否填写
				if(!validator.valid()){
					return false;
				}
				
				// 保存风控数据信息
				if(!access_role_manual_verify.doSaveRisk()){
					tools.openCT({
                        title: '提示', 
                        text: '服务器异常，访问失败！', 
                        type: 'fail', 
                        buttons: [ 
                            {
                                text: '确定',
                                fn: function () { 
                                },
                                isClose: true
                            }
                        ]
                    });
					return false;
				}
				
				tools.openLoading();
				$("#loanNew_condition").val('2');//设置办理结果（1：通过；2：不通过；3：驳回；）
				var url = contentPath + "/activitiSxd/task/access/manual/verify";
				var param = $('#frm_access_role_manual_verify').serialize();
				$.ajax({
					url : url, 
					data : param,
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						if (data.status === "success") {
							list.hideModal(); //关闭modal并刷新list数据
						}else{
							if(data.msg == "exception"){
								modalCommon.suspendTips($('#procInsId').val()); //是否需要挂起流程的提示
							}else{
								tools.openCT({
			                        title: '提示', 
			                        text: data.msg, 
			                        type: 'fail',  
			                        buttons: [ 
			                            {
			                                text: '确定',  
			                                fn: function () { 
//			                                	location.href=contentPath+"/workflow/list";
			                                },
			                                isClose: true  
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
	                        title: '提示',   
	                        text: '服务器异常，访问失败！', 
	                        type: 'fail', 
	                        buttons: [
	                            {
	                                text: '确定',
	                                fn: function () { 
//	                                	location.href=contentPath+"/workflow/list";
	                                },
	                                isClose: true 
	                            }
	                        ]
	                    });
						tools.closeLoading();
					}
				});
            });
	    	
	    	
	    	/**
	    	 * 点击"驳回"按钮
	    	 */
	    	$('#access-role-manual-verify-rejected').on('click', function (e) {
	    		if ($(this).val()!='true' && $('#comment').val()=='') {
					//弹出不能为空的备注
	    			modalCommon.alertNotNull('请输入备注信息');
					return false;
				}
	    		// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
				
				// 提示"标记不合格资料"，并确认是否进行驳回操作
				if (!access_role_manual_verify.doConFirm('您是否确认驳回该业务申请？')) {
					return false;
				}
				
				// 验证企业拥有员工数是否填写
				if(!validator.valid()){
					return false;
				}
				
				// 保存风控数据信息
				if(!access_role_manual_verify.doSaveRisk()){
					tools.openCT({
                        title: '提示', 
                        text: '服务器异常，访问失败！', 
                        type: 'fail', 
                        buttons: [ 
                            {
                                text: '确定',
                                fn: function () { 
                                },
                                isClose: true
                            }
                        ]
                    });
					return false;
				}
				
				$("#loanNew_condition").val('3');//设置办理结果（1：通过；2：不通过；3：驳回；）
				
				// 判断是手动发送还是自动发送驳回通知
				var sendType = $('#sendType').val(); //（0：手动；1：自动）
				
				if('0'==sendType){
					// 如果是手动发送通知
					
					// 隐藏准入验证的div
					$('#div-access-file').hide();
					$('#div-access-info').hide();
					$('#btn-access').hide();
					// 显示发送准入驳回通知的div
					$('#modal-body-access-rejected-notification').show();
					$('#btn-notification').show();
					// 增加发送通知的界面元素
					comNotification.appendTemplateDiv("div-access-rejected-notification");
					// 请求数据，填充数据
					var businessId = $('#businessId').val();
//					var businessId = '201511240011';
					comNotification.getTemplate(businessId, "access-verify-rejected"); // 查询准入驳回通知模板（邮件、短信、站内信的模板）
					
					return false;
					
				}else{
					// 如果是自动发送通知
					
					tools.openLoading();
					var url = contentPath + "/activitiSxd/task/access/manual/verify";
					var param = $('#frm_access_role_manual_verify').serialize();
					$.ajax({
						url : url, 
						data : param,
						type : 'POST',
						dataType : 'json',
//						async : false,
						success : function(data) {
							/*submit = false;*/
							if (data.status === "success") {
								list.hideModal(); //关闭modal并刷新list数据
							}else{
								if(data.msg == "exception"){
									modalCommon.suspendTips($('#procInsId').val()); //是否需要挂起流程的提示
								}else{
									tools.openCT({
				                        title: '提示', 
				                        text: data.msg, 
				                        type: 'fail', 
				                        buttons: [  
				                            {
				                                text: '确定',  
				                                fn: function () {  
//				                                	location.href=contentPath+"/workflow/list";
				                                },
				                                isClose: true 
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
		                        title: '提示',  
		                        text: '服务器异常，访问失败！', 
		                        type: 'fail', 
		                        buttons: [ 
		                            {
		                                text: '确定', 
		                                fn: function () {   
//		                                	location.href=contentPath+"/workflow/list";
		                                },
		                                isClose: true   
		                            }
		                        ]
		                    });
							tools.closeLoading();
						}
					});
				}
				
				
            });
	    	
	    	
	    	/**
	    	 * 点击“准入驳回通知”层中的“办理”按钮
	    	 */
	    	$('#access-rejected-notification').on('click', function (e) {
	    		result = comNotification.verifySendMsg();
	    		if (result == false)
				{
					return false;
				}
	    		tools.openLoading();
				var url = contentPath + "/activitiSxd/task/access/manual/verify";
				var param = $('#frm_access_role_manual_verify').serialize();
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
			                        title: '提示', 
			                        text: data.msg, 
			                        type: 'fail',
			                        buttons: [ 
			                            {
			                                text: '确定', 
			                                fn: function () { 
//			                                	location.href=contentPath+"/workflow/list";
			                                },
			                                isClose: true 
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
	                        title: '提示',  
	                        text: '服务器异常，访问失败！', 
	                        type: 'fail',  
	                        buttons: [   
	                            {
	                                text: '确定', 
	                                fn: function () {   
//	                                	location.href=contentPath+"/workflow/list";
	                                },
	                                isClose: true   
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
//			var businessId = '201511240011';
			$('#findInfoFor').attr("data-href",contentPath+"/activitiSxd/getActivitiList/getfwApplyBusDetail?busId="+businessId);
			$('#businessIdForFlag').val(businessId);
			access_role_manual_verify.getFileForAccess(businessId); //准入验证-查询要审核的资料(luy-)
			access_role_manual_verify.doChangeTaxCredit();
		}
};

jQuery(function($) {
	access_role_manual_verify.initEvent();
	access_role_manual_verify.initLoad();
});
