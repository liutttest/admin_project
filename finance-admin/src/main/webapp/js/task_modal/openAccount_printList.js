var printList = {
		/**
		 * 全选or全不选
		 * @param obj
		 * @param type
		 */
		selectAllNullorReserve: function(obj,type){
		    if(obj!=null&&obj!=""){
		        if(document.getElementsByName(obj)!=undefined&&document.getElementsByName(obj).length>0){    //getElementsByName函数的作用按名字查找对象，返回一个数组。
		            var userids = document.getElementsByName(obj);
		            if(type=="全选"){
		                for(var i=0;i<userids.length;i++){
		                    if(userids[i].checked == false){
		                        userids[i].checked = true;
		                    }
		                }
		            }else if(type=="全不选"){
		                for(var i=0;i<userids.length;i++){
		                    if(userids[i].checked == true){
		                        userids[i].checked = false;
		                    }
		                }
		            }
		        }
		    }
		},
		/**
		 * 文件下载
		 * @param fileType
		 */
		downFile:function(fileType){
	  		var busId = $('#businessId').val();
	  		var param = {};
	  		param.busId = busId;
	  		param.fileType = fileType;
	  		var url = contentPath+"/fwBusinessSxd/printData";
	  		$.ajax({
				url :url,
				type : 'POST',
				data :param,
				dataType : 'json',
				async : false,
				success : function(data) {
					window.open(contentPath+'/upload'+data.data);
				},
				error : function() {
				}
	  		});
	  	},
		/**
		 * 页面加载，查询可以打印的数据
		 */
		doFindPrintFile : function (businessId){
			var busId = businessId;
			var url = contentPath+"/fwBusinessSxd/findFileTOPrint";
			var param = {};
			param.busId = busId;
			 $.ajax({
					url :url,
					type : 'POST',
					dataType : 'json',
					async:false,
					data :param,
					success : function(data) {
						if(data.status=='success'){
							var html = [];
							var list = data.data.list;
							html.push(' <button class="btn btn-sm btn-primary select-all" type="button"><i class="ace-icon fa fa-check"></i> 全选</button>');
							html.push(' <button class="btn btn-sm btn-primary select-all-not" type="button"><i class="ace-icon fa fa-check"></i> 取消全选</button>');
							for (var int = 0; int < list.length; int++) {
								html.push('<div class="form-group"><div class="checkbox" class="col-sm-12"><label>');
								html.push('<input name="print-detail" type="checkbox" class="ace" data-busId="'+list[int].business_Id+'" value="'+fileJson[list[int].FILE_TYPE].dictKey+'">');
								html.push('<span class="lbl"> '+fileJson[list[int].FILE_TYPE].dictValue0+'</span>');
								html.push('  &nbsp; <a class="down-load" data-busId="'+list[int].business_Id+'" data-busType="'+fileJson[list[int].FILE_TYPE].dictKey+'" >下载</a> &nbsp;')
								//html.push('<a class="down-load-detail" data-busId="'+list[int].business_Id+'" data-busType="'+fileJson[list[int].FILE_TYPE].dictKey+'">查看</a>')
								html.push('</label></div></div>');
							}
							
							var fieldInfo = data.data.fieldInfo;
							
							/* 联系人 */
							html.push('<div class="form-group"><div class="checkbox" class="col-sm-12"><label> 联系人：');
							html.push('<span class="lbl"> '+fieldInfo.contactName+'</span>');
							html.push('</label></div></div>');
							
							/* 联系人 */
							html.push('<div class="form-group"><div class="checkbox" class="col-sm-12"><label> 联系电话：');
							html.push('<span class="lbl"> '+fieldInfo.fieldTel+'</span>');
							html.push('</label></div></div>');
							
							/* 开户时间 */
							html.push('<div class="form-group"><div class="checkbox" class="col-sm-12"><label> 开户时间：');
							html.push('<span class="lbl"> '+fieldInfo.fieldTime+'</span>');
							html.push('</label></div></div>');
							
							/* 开户地点 */
							html.push('<div class="form-group"><div class="checkbox" class="col-sm-12"><label> 开户地点：');
							html.push('<span class="lbl"> '+fieldInfo.fieldAddr+'</span>');
							html.push('</label></div></div>');
							
							/* 备注信息 */
							html.push('<div class="form-group"><div class="checkbox" class="col-sm-10" style="width: 550px;"><label> 备注信息：');
							html.push('<span class="lbl"> '+fieldInfo.content+'</span>');
							html.push('</label></div></div>');
							
							$('#print-body').html(html.join(''));
						}
					}
				});
		},
		
		//初始化事件
		initEvent : function() {
			/**
	    	 * 点击"确认打印"按钮
	    	 */
			$('#handling').on('click',function(){
				var chk_value =''; 
				$('input[name="print-detail"]:checked').each(function(){ 
				 chk_value+=($(this).val()+","); 
				});
				if(chk_value==''){
					//进行验证
					tools.openCT({
                        title: '无下载文件',         // {String} required model title
                        text: '请选择需要下载的文件',   // {String} required model text
                        type: 'warning',
                        buttons: [              // {Array} required buttons, 可以有一个 button
		                            {
		                                text: '确定',     // {String} required button text
		                                fn: function () {                   // {Function} click function
		                                	return false;
		                                },
		                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
		                            }
 		                        ]
                    });
					
					return false;
				
				}
				printList.downFile(chk_value);
			});
			
			/**
			 * 点击“全选”
			 */
			$(".modal-body").delegate('.select-all','click',function(){
				printList.selectAllNullorReserve('print-detail','全选');
			});
			/**
			 * 点击“全不选”
			 */
			$(".modal-body").delegate('.select-all-not','click',function(){
				printList.selectAllNullorReserve('print-detail','全不选');
			});
			
			/**
			 * 点击单个文件“下载”
			 */
			$("#print-body").delegate('.down-load','click',function(){
		      var fileType = $(this).attr('data-busType');
		      printList.downFile(fileType);
			});
			
	    	
		},
		
		// 页面加载成功的数据查询
		initLoad:function(){			
			// 请求数据，填充数据
			var businessId = $('#businessId').val();
//			businessId = '201510300015';
			printList.doFindPrintFile(businessId); // 查询可以打印的文件
			
		},
};

jQuery(function($) {
	printList.initEvent();
	printList.initLoad();
});
