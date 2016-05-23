var quota_role_manual_review = {
		
		/**
		 * 点击“驳回”、“不通过”按钮时，提示哪些资料被标记为不合格，是否确认进行操作
		 * @param msg
		 * @returns {Boolean}
		 */
		doConFirm : function(msg){
			var nopassNames = "";
			$('#div-limit-file a.red').each(function(){
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
		 * 查询：根据文件类型，查询文件个数，为相应的隐藏域赋值(luy)
		 * @param fileType 文件的类型
		 * @param inputHiddenId 要赋值的隐藏域的Id
		 */
		getImgFileSize : function(fileType,inputHiddenId) {
			var url = contentPath + "/fwBusinessSxd/applySxdFilesByType";
			var param = {};
			param.businessId = $('#businessId').val(); // 业务ID
			param.businessType = '01'; // 业务类型
			param.fileType = fileType; // 文件类型
			$.ajax({
				url : url,
				data : param,
				type : 'POST',
				dataType : 'json',
				success : function(data) {
					if (data.status === "success") {
						var files = data.data.files;
						$('#'+inputHiddenId+'').val(files.length);
					} else {
						tools.openST({
							title : '提示', // {String}
							text : '对不起，获取信息出现了错误', // {String}
							type : 'fail' // {String}
						});
					}
				},
				error : function() {
					submit = false;
					tools.openST({
						title : '提示', // {String}
						text : '对不起，获取信息出现了错误', // {String}
						type : 'fail' // {String}
					});
				}
			});
		},
		
		
		/**
		 * 查看图片的大图
		 * @type {string}
		 */
        showBigImg : function(){
			var $overflow = '';
			var colorbox_params = {
				rel: 'colorbox',
				reposition:true,
				scalePhotos:true,
				scrolling:false,
				photo:true,
				close:'&times;',
				maxWidth:'100%',
				maxHeight:'100%',
				current: '',
				previous: '<i class="glyphicon glyphicon-arrow-left"></i>',
				next:'<i class="glyphicon glyphicon-arrow-right"></i>',
				onOpen:function(){
					$overflow = document.body.style.overflow;
					document.body.style.overflow = 'hidden';
				},
				onClosed:function(){
					document.body.style.overflow = $overflow;
				},
				onComplete:function(){
					$.colorbox.resize();
				}
			};
			$('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);
        },
	  	
	  	selectdisabled:function(busId){
	  		var url = contentPath +"/fwBusinessSxd/getCenterDataByBusId";
	  		var param = {};
	  		param.busId =busId; 
	  		 $.ajax({
					url :url,
					type : 'POST',
					data :param,
					dataType : 'json',
					success : function(data) {
						var centerData = data.data.faCenterdata;
						var fwBusinessSxd = data.data.fwBusinessSxd;
						var fwComPerBus = data.data.fwComPerBus;
						if(data.data!=null){
							//经营场所
							$('#frm-quota-role-manual_review select[name="quota_005"]').val(fwBusinessSxd.premises);
							//户籍
							$('#frm-quota-role-manual_review select[name="quota_009"]').val(fwComPerBus.domicilePlace);
							//学历
							$('#frm-quota-role-manual_review select[name="quota_010"]').val(fwComPerBus.education);
							//年龄
							$('#frm-quota-role-manual_review select[name="quota_011"]').val(centerData.age);
							//婚姻状况
							$('#frm-quota-role-manual_review select[name="quota_012"]').val(fwComPerBus.maritalState);
							//黑名单
							$('#frm-quota-role-manual_review select[name="quota_013"]').val(centerData.blackList);
							//犯罪情况
							$('#frm-quota-role-manual_review select[name="quota_014"]').val(centerData.crime);
							//家庭净资产
							$('#frm-quota-role-manual_review select[name="quota_015"]').val(fwComPerBus.householdAssets);
							$('#hide_quota_005').val(fwBusinessSxd.premises);
							$('#hide_quota_009').val(fwComPerBus.domicilePlace);
							$('#hide_quota_010').val(fwComPerBus.education);
							$('#hide_quota_011').val(centerData.age);
							$('#hide_quota_012').val(fwComPerBus.maritalState);
							$('#hide_quota_013').val(centerData.blackList);
							$('#hide_quota_014').val(centerData.crime);
							$('#hide_quota_015').val(fwComPerBus.householdAssets);
						}
						$('#frm-quota-role-manual_review select[name="quota_005"]').attr('disabled',true);
						$('#frm-quota-role-manual_review select[name="quota_009"]').attr('disabled',true);
						$('#frm-quota-role-manual_review select[name="quota_010"]').attr('disabled',true);
						$('#frm-quota-role-manual_review select[name="quota_011"]').attr('disabled',true);
						$('#frm-quota-role-manual_review select[name="quota_012"]').attr('disabled',true);
						$('#frm-quota-role-manual_review select[name="quota_013"]').attr('disabled',true);
						$('#frm-quota-role-manual_review select[name="quota_014"]').attr('disabled',true);
						$('#frm-quota-role-manual_review select[name="quota_015"]').attr('disabled',true);
					},
					error : function() {
					}
		  		});
	  	},
		/**
		 * 查询图片文件 (luy)
		 * @param callback
		 */
		getImgFileInfo : function(fileType) {
			var url = contentPath + "/fwBusinessSxd/applySxdFilesByType";
			var param = {};
			param.businessId = $('#businessId').val(); // 业务ID
			param.businessType = '01'; // 业务类型
			param.fileType = fileType; // 文件类型
			$.ajax({
				url : url,
				data : param,
				type : 'POST',
				dataType : 'json',
				success : function(data) {
					submit = false;
					if (data.status === "success") {
						$('#file-img-show').empty();
						var files = data.data.files;
						
						if (files.length <=0) {
                        	var htmlimg = "";
                            htmlimg = htmlimg + '<span>';
                            htmlimg = htmlimg + '您还没有上传过该类型的图片！';
                            htmlimg = htmlimg + '</span>';
                            $('#file-img-show').append(htmlimg);
						}
						
						for(var i = 0;i < files.length ;i++){
							var htmlimg = "";
							htmlimg = htmlimg+'<li>';
							htmlimg = htmlimg+'<button class="js-delete-imgfile" type="button" id="cboxClose" fileId="'+files[i].fileId+'"  data-toggle="tooltip" data-placement="left" title="删除图片？">x</button>';
							htmlimg = htmlimg+'<a href=".downloadImg?id='+files[i].fileId+'" data-rel="colorbox" class="cboxElement">';
							htmlimg = htmlimg+'<img width="150" height="150" alt="150x150" src=".downloadImg?id='+files[i].fileId+'">';
							htmlimg = htmlimg+'</a>';
							htmlimg = htmlimg+'</li>';
							$('#file-img-show').append(htmlimg);
						}
						
						// 调用查看大图的方法
						quota_role_manual_review.showBigImg();
					} else {
						tools.openST({
							title : '提示', // {String}
							text : '对不起，获取信息出现了错误', // {String}
							type : 'fail' // {String}
						});
						
						
					}
				},
				error : function() {
					submit = false;
					tools.openST({
						title : '提示', // {String}
						text : '对不起，获取信息出现了错误', // {String}
						type : 'fail' // {String}
					});
					
					
				}
			});
		},
		/**
		* 额度申请，查询需要审核的文件  
		*/
		buildTrForLimit : function(data) {
			
			data = data.data;
			var html = [];
			var tbody = $('.limit-file-for-nopass');
			
			html.push(quota_role_manual_review.createTrForLimit(data));
			tbody.html(html.join(''));
		},
		/**
		* 额度申请，查询需要审核的文件  
		*/
		createTrForLimit : function(data) {
			var tr = [];
			
			tr.push('<tr>');
				tr.push('<td class="c-align">1</td> ');
				tr.push('<td class="c-align">授信业务申请书</td>');
				if(data.creditBusinessApp_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'12'+'"><a class="js-do-pass red" data-infoName="授信业务申请书" data-infoKey="12" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'12'+'"><a class="js-do-nopass" data-infoName="授信业务申请书" data-infoKey="12" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">2</td> ');
				tr.push('<td class="c-align">公司简介</td>');
				if(data.companyProfile_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'36'+'"><a class="js-do-pass red" data-infoName="公司简介" data-infoKey="36" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'36'+'"><a class="js-do-nopass" data-infoName="公司简介" data-infoKey="36" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
				
			tr.push('<tr>');
				tr.push('<td class="c-align">3</td> ');
				tr.push('<td class="c-align">股东会或董事会决议</td>');
				if(data.directorsBoard_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'13'+'"><a class="js-do-pass red" data-infoName="股东会或董事会决议" data-infoKey="13" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'13'+'"><a class="js-do-nopass" data-infoName="股东会或董事会决议" data-infoKey="13" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			/*
			tr.push('<tr>');
				tr.push('<td class="c-align">3</td> ');
				tr.push('<td class="c-align">资质证书或经营许可证</td>');
				
				if(data.specicalIndustryCre.length<=0){
					tr.push('<td class="c-align js-caozuo-access'+'14'+'">无</td>');
				}else{
					if(data.specicalIndustryCre_ispass==0){
						tr.push('<td class="c-align js-caozuo-access'+'14'+'"><a class="js-do-pass red" data-infoName="资质证书或经营许可证" data-infoKey="14" href="#">已标记为不合格</a></td>');
					}else{
						tr.push('<td class="c-align js-caozuo-access'+'14'+'"><a class="js-do-nopass" data-infoName="资质证书或经营许可证" data-infoKey="14" href="#">标记为不合格</a></td>');
					}
				}
			tr.push('</tr>');
			*/
			
			tr.push('<tr>');
				tr.push('<td class="c-align">4</td> ');
				tr.push('<td class="c-align">公司章程、验资报告</td>');
				if(data.capitalVerification_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'15'+'"><a class="js-do-pass red" data-infoName="公司章程、验资报告" data-infoKey="15" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'15'+'"><a class="js-do-nopass" data-infoName="公司章程、验资报告" data-infoKey="15" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">5</td> ');
				tr.push('<td class="c-align">购销合同</td>');
				if(data.purchaseSaleContract_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'18'+'"><a class="js-do-pass red" data-infoName="购销合同" data-infoKey="18" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'18'+'"><a class="js-do-nopass" data-infoName="购销合同" data-infoKey="18" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">6</td> ');
				tr.push('<td class="c-align">个人所得税</td>');
				if(data.theIndividualIncomeTax_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'38'+'"><a class="js-do-pass red" data-infoName="个人所得税" data-infoKey="38" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'38'+'"><a class="js-do-nopass" data-infoName="个人所得税" data-infoKey="38" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">7</td> ');
				tr.push('<td class="c-align">国地税纳税证明</td>');
				if(data.lastYearThidYearAnnualLandTaxCertificate_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'37'+'"><a class="js-do-pass red" data-infoName="国地税纳税证明" data-infoKey="37" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'37'+'"><a class="js-do-nopass" data-infoName="国地税纳税证明" data-infoKey="37" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			/*
			tr.push('<tr>');
				tr.push('<td class="c-align">6</td> ');
				tr.push('<td class="c-align">客户清单</td>');
				if(data.customerList_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'19'+'"><a class="js-do-pass red" data-infoName="客户清单" data-infoKey="19" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'19'+'"><a class="js-do-nopass" data-infoName="客户清单" data-infoKey="19" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			*/
			
			/*
			tr.push('<tr>');
				tr.push('<td class="c-align">7</td> ');
				tr.push('<td class="c-align">税务报表</td>');
				if(data.lastTaxStatements_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'20'+'"><a class="js-do-pass red" data-infoName="税务报表" data-infoKey="20" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'20'+'"><a class="js-do-nopass" data-infoName="税务报表" data-infoKey="20" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			*/
			
			tr.push('<tr>');
				tr.push('<td class="c-align">8</td> ');
				tr.push('<td class="c-align">银行对账单</td>');
				if(data.lastCompanyStatements_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'21'+'"><a class="js-do-pass red" data-infoName="银行对账单" data-infoKey="21" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'21'+'"><a class="js-do-nopass" data-infoName="银行对账单" data-infoKey="21" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			/*
			tr.push('<tr>');
				tr.push('<td class="c-align">9</td> ');
				tr.push('<td class="c-align">个人银行对账单</td>');
				
				if(data.lastControllerStatements.length<=0){
					tr.push('<td class="c-align js-caozuo-access'+'22'+'">无</td>');
				}else{
					if(data.lastControllerStatements_ispass==0){
						tr.push('<td class="c-align js-caozuo-access'+'22'+'"><a class="js-do-pass red" data-infoName="个人银行对账单" data-infoKey="22" href="#">已标记为不合格</a></td>');
					}else{
						tr.push('<td class="c-align js-caozuo-access'+'22'+'"><a class="js-do-nopass" data-infoName="个人银行对账单" data-infoKey="22" href="#">标记为不合格</a></td>');
					}
				}
			tr.push('</tr>');
			*/
			
			/*
			tr.push('<tr>');
				tr.push('<td class="c-align">10</td> ');
				tr.push('<td class="c-align">纳税凭证和申请表</td>');
				if(data.lastTaxCerifcate_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'23'+'"><a class="js-do-pass red" data-infoName="纳税凭证和申请表" data-infoKey="23" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'23'+'"><a class="js-do-nopass" data-infoName="纳税凭证和申请表" data-infoKey="23" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			*/
			
			tr.push('<tr>');
				tr.push('<td class="c-align">9</td> ');
				tr.push('<td class="c-align">缴费凭证</td>');
				if(data.lastPaymentVoucher.length<=0){
					tr.push('<td class="c-align js-caozuo-access'+'24'+'">无</td>');
				}else{
					if(data.lastPaymentVoucher_ispass==0){
						tr.push('<td class="c-align js-caozuo-access'+'24'+'"><a class="js-do-pass red" data-infoName="缴费凭证" data-infoKey="24" href="#">已标记为不合格</a></td>');
					}else{
						tr.push('<td class="c-align js-caozuo-access'+'24'+'"><a class="js-do-nopass" data-infoName="缴费凭证" data-infoKey="24" href="#">标记为不合格</a></td>');
					}
				}
			tr.push('</tr>');
			
			/*
			tr.push('<tr>');
				tr.push('<td class="c-align">12</td> ');
				tr.push('<td class="c-align">资信和技术产品证书</td>');
				if(data.creditTechnology.length<=0){
					tr.push('<td class="c-align js-caozuo-access'+'14'+'">无</td>');
				}else{
					if(data.creditTechnology_ispass==0){
						tr.push('<td class="c-align js-caozuo-access'+'25'+'"><a class="js-do-pass red" data-infoName="资信和技术产品证书" data-infoKey="25" href="#">已标记为不合格</a></td>');
					}else{
						tr.push('<td class="c-align js-caozuo-access'+'25'+'"><a class="js-do-nopass" data-infoName="资信和技术产品证书" data-infoKey="25" href="#">标记为不合格</a></td>');
					}
				}
			tr.push('</tr>');
			*/
			
			tr.push('<tr>');
				tr.push('<td class="c-align">10</td> ');
				tr.push('<td class="c-align">场地证明</td>');
				if(data.propertyRightCard_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'26'+'"><a class="js-do-pass red" data-infoName="场地证明" data-infoKey="26" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'26'+'"><a class="js-do-nopass" data-infoName="场地证明" data-infoKey="26" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">11</td> ');
				tr.push('<td class="c-align">资产负债申请表</td>');
				if(data.controllerAssetsLiaApply_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'29'+'"><a class="js-do-pass red" data-infoName="资产负债申请表" data-infoKey="29" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'29'+'"><a class="js-do-nopass" data-infoName="资产负债申请表" data-infoKey="29" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">12</td> ');
				tr.push('<td class="c-align">婚姻状况证明</td>');
				if(data.marriageLicense_ispass==0){
					tr.push('<td class="c-align js-caozuo-access'+'27'+'"><a class="js-do-pass red" data-infoName="婚姻状况证明" data-infoKey="27" href="#">已标记为不合格</a></td>');
				}else{
					tr.push('<td class="c-align js-caozuo-access'+'27'+'"><a class="js-do-nopass" data-infoName="婚姻状况证明" data-infoKey="27" href="#">标记为不合格</a></td>');
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">13</td> ');
				tr.push('<td class="c-align">实际控制人及配偶的担保承诺书</td>');
				if(data.controllerSpouseGuarantee.length<=0){
					tr.push('<td class="c-align js-caozuo-access'+'28'+'">无</td>');
				}else{
					if(data.controllerSpouseGuarantee_ispass==0){
						tr.push('<td class="c-align js-caozuo-access'+'28'+'"><a class="js-do-pass red" data-infoName="实际控制人及配偶的担保承诺书" data-infoKey="28" href="#">已标记为不合格</a></td>');
					}else{
						tr.push('<td class="c-align js-caozuo-access'+'28'+'"><a class="js-do-nopass" data-infoName="实际控制人及配偶的担保承诺书" data-infoKey="28" href="#">标记为不合格</a></td>');
					}
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">14</td> ');
				tr.push('<td class="c-align">实际控制人及配偶的担保确认书</td>');
				if(data.confirmation.length<=0){
					tr.push('<td class="c-align js-caozuo-access'+'42'+'">无</td>');
				}else{
					if(data.confirmation_ispass==0){
						tr.push('<td class="c-align js-caozuo-access'+'42'+'"><a class="js-do-pass red" data-infoName="实际控制人及配偶的担保确认书" data-infoKey="42" href="#">已标记为不合格</a></td>');
					}else{
						tr.push('<td class="c-align js-caozuo-access'+'42'+'"><a class="js-do-nopass" data-infoName="实际控制人及配偶的担保确认书" data-infoKey="42" href="#">标记为不合格</a></td>');
					}
				}
			tr.push('</tr>');
			
			tr.push('<tr>');
				tr.push('<td class="c-align">15</td> ');
				tr.push('<td class="c-align">控制人及其家庭房产证明</td>');
				if(data.housePropertyCard.length<=0){
					tr.push('<td class="c-align js-caozuo-access'+'43'+'">无</td>');
				}else{
					if(data.housePropertyCard_ispass==0){
						tr.push('<td class="c-align js-caozuo-access'+'43'+'"><a class="js-do-pass red" data-infoName="控制人及其家庭房产证明" data-infoKey="43" href="#">已标记为不合格</a></td>');
					}else{
						tr.push('<td class="c-align js-caozuo-access'+'43'+'"><a class="js-do-nopass" data-infoName="控制人及其家庭房产证明" data-infoKey="43" href="#">标记为不合格</a></td>');
					}
				}
			tr.push('</tr>');
			
			
			return tr.join('');
		},
		/**
		* 额度申请，查询需要审核的文件 
		*/
		getFileForLimit : function(busId){
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
					var sysUser = quota_role_manual_review.buildTrForLimit(data);
					/*submit = false;*/
				},
				error : function() {
					alert('服务器异常，访问失败！');
					/*submit = false;*/
				}
			});
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
//                    	window.location.reload();//刷新当前页面.
//                    	alert("标记成功！");
                    	tools.openCT({
	                        title: '提示',         // {String} required model title
	                        text: '标记成功',   // {String} required model text
	                        type: 'success',        // {String} required 取值 success， fail， warning， default success
	                        buttons: [              // {Array} required buttons, 可以有一个 button
	                            {
	                                text: '确定',     // {String} required button text
	                                fn: function () {                   // {Function} click function
//	                                	location.href=contentPath+"/workflow/list";
	                                	//window.location.reload();//刷新当前页面
	                                	//$('.'+updateHtmlClass+infoKey).empty();
	                                	//$('.'+updateHtmlClass+infoKey).html('<a class="js-do-pass red" data-infoName="'+infoName+'" data-infoKey="'+infoKey+'" href="#">已标记为不合格</a>');
	                                },
	                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
	                            }
	                        ]
	                    });
                    	
                    	 $('.'+updateHtmlClass+infoKey).empty();
                         $('.'+updateHtmlClass+infoKey).html('<a class="js-do-pass red" data-infoName="'+infoName+'" data-infoKey="'+infoKey+'" href="#">已标记为不合格</a>');
                    }
                    else {
//                    	alert("标记失败！");
                    	tools.openCT({
	                        title: '提示',         // {String} required model title
	                        text: '标记失败！',   // {String} required model text
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
                    }
                },
                error: function (data) {
                    submit = false;
//                    alert("标记出现异常！");
                    tools.openCT({
                        title: '提示',         // {String} required model title
                        text: '标记出现异常',   // {String} required model text
                        type: 'warning',        // {String} required 取值 success， fail， warning， default success
                        buttons: [              // {Array} required buttons, 可以有一个 button
                            {
                                text: '确定',     // {String} required button text
                                fn: function () {                   // {Function} click function
//                                	location.href=contentPath+"/workflow/list";
                                },
                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
                            }
                        ]
                    });
                }
            });
		},
		
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
//                    	window.location.reload();//刷新当前页面.
//                    	alert("标记成功！");
                    	tools.openCT({
	                        title: '提示',         // {String} required model title
	                        text: '标记成功',   // {String} required model text
	                        type: 'success',        // {String} required 取值 success， fail， warning， default success
	                        buttons: [              // {Array} required buttons, 可以有一个 button
	                            {
	                                text: '确定',     // {String} required button text
	                                fn: function () {                   // {Function} click function
//	                                	location.href=contentPath+"/workflow/list";
	                                	//window.location.reload();//刷新当前页面
	                                	$('.'+updateHtmlClass+infoKey).empty();
	                                	$('.'+updateHtmlClass+infoKey).html('<a class="js-do-nopass" data-infoName="'+infoName+'" data-infoKey="'+infoKey+'" href="#">标记为不合格</a>');
	                                },
	                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
	                            }
	                        ]
	                    });
                    	$('.'+updateHtmlClass+infoKey).empty();
                    	$('.'+updateHtmlClass+infoKey).html('<a class="js-do-nopass" data-infoName="'+infoName+'" data-infoKey="'+infoKey+'" href="#">标记为不合格</a>');
                    }
                    else {
//                    	alert("标记失败！");
                    	tools.openCT({
	                        title: '提示',         // {String} required model title
	                        text: '标记失败！',   // {String} required model text
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
                    }
                },
                error: function (data) {
                    submit = false;
//                    alert("标记出现异常！");
                    tools.openCT({
                        title: '提示',         // {String} required model title
                        text: '标记出现异常',   // {String} required model text
                        type: 'warning',        // {String} required 取值 success， fail， warning， default success
                        buttons: [              // {Array} required buttons, 可以有一个 button
                            {
                                text: '确定',     // {String} required button text
                                fn: function () {                   // {Function} click function
//                                	location.href=contentPath+"/workflow/list";
                                },
                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
                            }
                        ]
                    });
                }
            });
		},
		
		
		//初始化事件
		initEvent : function() {
			/**
	    	 * 点击"标记为不合格"(luy-)
	    	 */
	    	$("tbody").delegate('.js-do-nopass', "click", function(e){
	            e.preventDefault();
	            var busId = $('#businessId').val();
	            var infoName = $(this).attr("data-infoName");
	            var infoKey = $(this).attr("data-infoKey");
	            quota_role_manual_review.doUpdateNoPass(busId, infoName, infoKey,"js-caozuo-access");
	        });
	    	
	    	/**
	    	 * “已标记为不合格”的点击事件，将其再变回“标记为不合格”
	    	 */
	    	$("tbody").delegate('.js-do-pass', "click", function(e){
	            e.preventDefault();
	            var busId = $('#businessId').val();
	            var infoKey = $(this).attr("data-infoKey");
	            var infoName = $(this).attr("data-infoName");
	            quota_role_manual_review.doUpdatePass(busId, infoName, infoKey,"js-caozuo-access");
	        });
	    	
	    	/**
	    	 * 点击"办理"按钮
	    	 */
	    	$('#quota-role-manual_review-handling').on('click', function (e) {
	    		// 提示有资料标记为不合格，是否确认通过
				if ($(this).val() == 'true' && $('#div-limit-file').css('display') != 'none' && $('#div-limit-file').find('a').hasClass('red'))
				{
					if (!confirm("您有标记为不合格的资料，确认通过吗？"))
					{
						return false;	
					}
				}
				
				// 验证是否上传“企业纳税信用等级证明”
				var qinsxydjzmCount = parseInt($('#qinsxydjzm_count').val());
				if (qinsxydjzmCount <= 0) {
					modalCommon.alertNotNull('请上传企业纳税信用等级证明');
					return false;
				}
				
				// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
				
				tools.openLoading();
				$("#loanNew_condition").val('1');//设置办理结果（1：通过；2：不通过；3：驳回；）
				var url = contentPath + "/activitiSxd/task/quota/review";
				var param = $('#frm-quota-role-manual_review').serialize();
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
	    	
	    	/**
	    	 * 点击"不通过"按钮
	    	 */
	    	$('#quota-role-manual_review-nopass').on('click', function (e) {
	    		if ($(this).val()!='true' && $('#comment').val()=='') {
					//textAreaBoolean = true;
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
				if (!quota_role_manual_review.doConFirm('您是否确认不通过该业务申请？')) {
					return false;
				}
				
				tools.openLoading();
				$("#loanNew_condition").val('2');//设置办理结果（1：通过；2：不通过；3：驳回；）
				var url = contentPath + "/activitiSxd/task/quota/review";
				var param = $('#frm-quota-role-manual_review').serialize();
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
	    	
	    	
	    	/**
	    	 * 点击"驳回"按钮
	    	 */
	    	$('#quota-role-manual_review-rejected').on('click', function (e) {
	    		
	    		if ($(this).val()!='true' && $('#comment').val()=='') {
					//textAreaBoolean = true;
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
				if (!quota_role_manual_review.doConFirm('您是否确认驳回该业务申请？')) {
					return false;
				}
				
				$("#loanNew_condition").val('3');//设置办理结果（1：通过；2：不通过；3：驳回；）
				
				// 判断是手动发送还是自动发送驳回通知
				var sendType = $('#sendType').val(); //（0：手动；1：自动）
				
				if('0'==sendType){
					// 如果是手动发送通知
					
					// 隐藏准入验证的div
					$('#div-limit-file').hide();
					$('#div-quota-modal-body').hide();
					$('#btn-quota').hide();
					// 显示发送准入驳回通知的div
					$('#modal-body-quota-apply-rejected-notification').show();
					$('#btn-notification').show();
					// 增加发送通知的界面元素
					comNotification.appendTemplateDiv("quota-apply-rejected-notification");
					// 请求数据，填充数据
					var businessId = $('#businessId').val();
//					var businessId = '201511240011';
					comNotification.getTemplate(businessId, "quota-apply-rejected"); // 查询额度申请-人工复核驳回通知模板（邮件、短信、站内信的模板）
					
					return false;
					
				}else{
					// 如果是自动发送通知
					
					tools.openLoading();
					var url = contentPath + "/activitiSxd/task/quota/review";
					var param = $('#frm-quota-role-manual_review').serialize();
					/*if(submit){
						return false;
					}
					submit = true;*/
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
				                        title: '提示',         // {String} required model title
				                        text: data.msg,   // {String} required model text
				                        type: 'fail',        // {String} required 取值 success， fail， warning， default success
				                        buttons: [              // {Array} required buttons, 可以有一个 button
				                            {
				                                text: '确定',     // {String} required button text
				                                fn: function () {                   // {Function} click function
//				                                	location.href=contentPath+"/workflow/list";
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
//		                                	location.href=contentPath+"/workflow/list";
		                                },
		                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
		                            }
		                        ]
		                    });
							tools.closeLoading();
						}
					});
				}
				
            });
	    	
	    	
	    	/**
	    	 * 点击“额度申请-人工复核”层中的“办理”按钮
	    	 */
	    	$('#quota-rejected-notification').on('click', function (e) {
	    		result = comNotification.verifySendMsg();
	    		if (result == false)
				{
					return false;
				}
	    		
	    		// 验证备注信息不可输入特殊字符
				validCommentResult = modalCommon.validComment($('#comment').val());
				if (validCommentResult == false){
					return false;
				}
				
	    		tools.openLoading();
				var url = contentPath + "/activitiSxd/task/quota/review";
				var param = $('#frm-quota-role-manual_review').serialize();
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
			
	    	/**
			 * 点击 '企业纳税信用等级证明34'--查看图片
			 */
			$('#qinsxydjzm-a-ck').on('click', function() {
				quota_role_manual_review.getImgFileInfo('34');
				$('#show-modal').modal('show');
				
				// 允许model滚动
				$('#show-modal').on('hidden.bs.modal', function (e) {
		            $('body').addClass('modal-open')
		        })
			});
			
			/**
			 * 点击 '查看图片'弹出层中删除按钮(luy)
			 */
			
			$("#file-img-show").delegate("button","click",function(){
				var fileId= $(this).attr('fileId');
				var that = this;
				$.ajax({
					url : contentPath+'/deleteFile?fileId=' + fileId,
					data : '',
					type : 'POST',
					dataType : 'json',
					success : function() {
                        if (!!window.ActiveXObject || "ActiveXObject" in window){
                            that.parentElement.removeNode(true);
                        } else {
                            that.parentElement.remove();
                        }
					}
				});
			});
			
			/**
			 * 关闭查看图片的modal
			 */
			$("div").delegate('.js-showImg-close', "click", function(e){
				$('#show-modal').modal('hide');
			});
			
			/**
             * 上传文件，点击modal中的'关闭'按钮，重新加载图片数量
             */
            $('#upload-modal').on('hide.bs.modal', function () {
            	quota_role_manual_review.getImgFileSize('34','qinsxydjzm_count');//为企业纳税信用等级的隐藏域赋值（即文件个数）（luy）
            });
            $('#show-modal').on('hide.bs.modal', function () {
            	quota_role_manual_review.getImgFileSize('34','qinsxydjzm_count');//为企业纳税信用等级的隐藏域赋值（即文件个数）（luy）
            });
			
		},
		
		
		initLoad:function(){
			var businessId = $('#businessId').val();
//			businessId = '201511240011';
			$('#findInfoForLimit').attr("data-href",contentPath+"/activitiSxd/getActivitiList/getfwApplyBusDetail?busId="+businessId);
			$('#businessIdForFlag').val(businessId);
			quota_role_manual_review.getFileForLimit(businessId); //额度申请-查询要审核的资料(luy-)
			quota_role_manual_review.selectdisabled(businessId);
			quota_role_manual_review.getImgFileSize('34','qinsxydjzm_count');//为企业纳税信用等级的隐藏域赋值（即文件个数）（luy）
		},
};

jQuery(function($) {
	quota_role_manual_review.initEvent();
	quota_role_manual_review.initLoad();
});
