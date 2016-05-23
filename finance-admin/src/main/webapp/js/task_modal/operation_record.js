var operationRecord = {
		
		showFlowListModel : function(procInsId){
			$.getJSON(contentPath + "/workflowHistory/flowList", { procInsId: procInsId},
			  function(data){
				$("#rows").html("");
				$.each(data, function(i, o){  
					$("#rows").append(template('hisTemplate', o));    
				});  
				
			});
		},
		
		//初始化事件
		initEvent : function() {
			
		},
		
		// 页面加载成功的数据查询
		initLoad:function(){
			var procInsId = $('#procInsId').val();
			operationRecord.showFlowListModel(procInsId);
		},
};

jQuery(function($) {
	operationRecord.initEvent();
	operationRecord.initLoad();
});
