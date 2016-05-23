var his = {
	oTable : null,
	getlist : function() {
		his.oTable = $('#historyTable').dataTable({
			"bAutoWidth" : false,
			"aaSorting" : [],
			"ordering" : false,
			"bServerSide" : true,
			"searching" : false,
			"sAjaxSource" : contentPath + "/workflowHistory/list",
			"aoColumns" : [ 
			    {"mData" : "comName"}, 
				{"mData" : "assignee"},
				{"mData" : "name"}, 
				{"mData" : "pdName"}, 
				{"mData" : "endTime"}, 
				{"mData" : function(source) {
					return "<span style=\"cursor:pointer\" onclick=\"his.showFlowListModel('"+source.procInsId+"','"+source.procDefId+"')\">详细</span>";
				}
			}, ],
			"fnServerData" : function(sSource, aoData, fnCallback) {
				$.ajax({
					"type" : 'get',
					"url" : sSource,
					"dataType" : "json",
					"data" : aoData,
					"success" : function(resp) {
						fnCallback(resp);
					}
				});
			},
		});
	},
	showFlowListModel : function(procInsId, procDefId){
		$.getJSON(contentPath + "/workflowHistory/flowList", { procInsId: procInsId, procDefId : procDefId},
		  function(data){
			$("#rows").html("");
			$.each(data, function(i, o){
				$("#rows").append(template('hisTemplate', o));  
			});
			
		});
		$('#modal-detailed').modal('show');
	},
	closeFlowListModel : function(){
		$('#modal-detailed').modal('hide');
	},
	initEvent : function(){
		$("#closeModel").click(his.closeFlowListModel);
	}
	
};
jQuery(function($) {
	his.initEvent();
	his.getlist();
});