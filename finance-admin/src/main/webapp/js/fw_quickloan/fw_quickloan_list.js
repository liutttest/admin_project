var list = {
	oTable : null,
	getlist : function() {
		list.oTable = $('#quickLoanList').dataTable({
			"bAutoWidth" : false,
			"aaSorting" : [],
			"ordering" : false,
			"bServerSide" : true,
			"searching" : true,
			"sAjaxSource" : contentPath + "/fwQuickLoan/list",
			"aoColumns" : [ 
			     {"mData" : "comName"}, //机构名称
			     {"mData" : "contactName"}, //联系人
			     {"mData" : "tel"}, //电话
			     {"mData" : "applyMoney"}, //申请金额
			     {"mData" : "createTime"}, //申请时间
			     {"mData" : function(source){
			    	 if (source.assignee != '' && source.assignee != null) {
						 return source.assignee;
			    	 } else {
			    		 return source.taskHandler;
			    	 }
			     }}, //签收人
                 {"mData" : function(source){
                	 if (source.loanState == '0109') {
                		 return '等待业务数据';
                	 }
                	 if (source.loanState == '01') {
                		 return '处理中';
                	 }
                	 if (source.loanState == '02') {
                		 return '已处理';
                	 }
                	 if (source.loanState == '03') {
                		 return '未处理';
                	 }
                 }},   
				 {"mData" : function(c) {
					 var html  =[];
					 if(c.loanState == '01' && c.assignee == userName){
						 html.push('<a class="flow-bootbox-options" data-taskid="'+c.taskId+'" data-taskDefKey="'+c.taskDefKey+'" data-buskey="'+c.loanId+'" data-busComName="'+c.comName +'"data-procInsId="'+c.procInsId+'" href="#">办理</a> &nbsp;');
		    		 }
					 html.push('<a data-buskey="'+c.loanId+'" class="js-do-appendViews" href="#">追加意见</a>  &nbsp;');
					return html.join('');
				 }
			}, ],
			 /*"fnServerParams" : function(aoData) { 
		    	 aoData.push({  
		            "name" : "suspensionState",
		            "value" : $('#suspension_state').val()
		        });
			 },*/
			"fnServerData" : function(sSource, aoData, fnCallback) {
				$.ajax({
					"type" : 'get',
					"url" : sSource,
					"dataType" : "json",
					"data" :  aoData,  
					"success" : function(resp) {
						fnCallback(resp);
					}
				});
			},
		});
	},
	
	/**
	 * 查询“追加意见”记录
	 */
	doGetHanndleRecord : function (data_buskey){
		var url = contentPath + "/fwQuickLoan/getHanndleRecord";
		var param = {};
		
		param.buskey = data_buskey;// 业务key
		
		$.ajax({
			url : url, 
			data : param,
			type : 'POST',
			dataType : 'json',
			async : false,
			success : function(data) {
				list.buildTr(data);
				/* 公司名称 */
				$("#comName").val(data.data.comName);
				/*submit = false;*/
			},
			error : function() {
				alert('服务器异常，访问失败！');
				/*submit = false;*/
			}
		});
	},
	
	/**
	 * 追加意见
	 */
	doAppendRemark : function (data_buskey,reason){
		var url = contentPath + "/fwQuickLoan/doAppendQLRemark";
		var param = {};
		
		param.businessId = data_buskey;// 业务key
		param.reason = reason; //意见内容
		
		$.ajax({
			url : url, 
			data : param,
			type : 'POST',
			dataType : 'json',
			async : false,
			success : function(data) {
				
				$("#quickloan_reason").val("");
				$('#quickloan-modal-form').modal('hide');
				alert("追加意见成功！");
				
				/*submit = false;*/
			},
			error : function() {
				alert('服务器异常，追加意见成功失败！');
				/*submit = false;*/
			}
		});
	},
	
	/* 创建操作记录表 */
  	buildTr : function(data) {
		data = data.data.list;
		var html = [];
		var tbody = $('.fw-handle-record');
		
		if(data.length>0){
			for ( var int = 0; int < data.length; int++) {
				html.push(list.createTr(data[int]));
			}
			tbody.html(html.join(''));
		}else{
			tbody.html('<tr><td class="c-align" colspan="4">无处理记录！</td></tr>');
		}
	},
	createTr : function(data) {
		var tr = [];
		tr.push(' <tr> <td class="c-align">');
		tr.push(data.handleUser);
		tr.push('</td>');
		tr.push('<td class="c-align"> ');
		tr.push(data.handleTime);
		tr.push('</td>');
		tr.push('<td class="c-align">');
		tr.push(data.handleContent);
		tr.push('</td>');
		tr.push('</tr>');
		return tr.join('');
	},
	
	/**
	 *liutt
	 *待办任务列表点击办理按钮绑定事件
	 **/
	banliaction : function(taskId,businessId,taskDefKey,procInsId){
		$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJspForQuickLoan?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey+"&procInsId="+procInsId,null,function(){
			list.showModel();
		});
	},
	showModel:function(){
		$('#public-modal-form').modal('show');
	},
	hideModal:function(){
		$('#public-modal-form').modal('hide');
		tools.openST({
            title: '系统提示',             // {String}
            text: '操作成功',       // {String}
            type: 'success'                // {String}
        });
		// 重新加载数据
		list.oTable.fnClearTable(0);  
		list.oTable.fnDraw();
	},
	
	inEvent:function(){
		/**
    	 * 点击列表中的"追加意见"按钮(luy)
    	 */
    	$("tbody").delegate('.js-do-appendViews','click',function(){
    		var buskey = $(this).attr('data-buskey');// 业务key
    		$("#js-busId-doAppendRemark").val(buskey);
    		list.doGetHanndleRecord(buskey);
    		$('#quickloan-modal-form').modal('show');
    	});
    	
    	//为快速贷款:追加意见
		$('#appendRemarkForList').on('click',function(){
			var buskey = $("#js-busId-doAppendRemark").val();;// 业务key
    		var reason = $("#quickloan_reason").val(); // 意见内容
			if (reason=='') {
				tools.openCT({
                    title: '不能为空',         // {String} required model tditle
                    text: '请输入处理意见',   // {String} required model text
                    type: 'warning',
                    buttons: [              // {Array} required buttons, 可以有一个 button
	                            {
	                                text: '确定',     // {String} required button text
	                                fn: function () {  
	                                	return false;// {Function} click function
	                                },
	                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
	                            }
	                        ]
                });
				return false;
			}
			list.doAppendRemark(buskey,reason);
		
		});
		
		//为办理绑定点击事件
		$("tbody").delegate('.flow-bootbox-options','click',function(){
			var taskId= $(this).attr('data-taskid');
			var businessId = $(this).attr('data-buskey');
			var taskDefKey = $(this).attr('data-taskDefKey');
			var procInsId =  $(this).attr('data-procInsId');
			list.banliaction(taskId,businessId,taskDefKey,procInsId);
		});
		
		
	}
	
};
jQuery(function($) {
	list.getlist();
	list.inEvent();
});

