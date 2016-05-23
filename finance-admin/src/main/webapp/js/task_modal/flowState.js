var flowState = {
	setState : function() {
		var procInsId = $("#procInsId").val();
		if (procInsId != "") {
			jQuery.ajax({
				url : contentPath + "/workflowInstance/active",
				type : 'POST',
				data : {
					"procInsId" : procInsId
				},
				success : function(data) {
					if (data.state == "1") {
						$("#procInsId").val("");
						tools.openCT({
							title : '提示',
							text : '激活成功',
							buttons : [ {
								text : '关闭',
								isClose : true
							} ]
						});
					} else if(data.state == 2){
						tools.openCT({
							title : '提示',
							text : '该流程已经是激活状态',
							type : 'fail',
							buttons : [ {
								text : '关闭',
								isClose : true
							} ]
						});
					} else {
						tools.openCT({
							title : '提示',
							text : '激活失败',
							type : 'fail',
							buttons : [ {
								text : '关闭',
								isClose : true
							} ]
						});
					}
				}
			});
		} else {
			tools.openCT({
				title : '提示',
				text : '请输入流程实例ID',
				type : 'fail',
				buttons : [ {
					text : '关闭',
					isClose : true
				} ]
			});
		}
	},
	initEvent : function() {
		$("#flowStateBtn").click(flowState.setState);
	}
};
jQuery(function($) {
	flowState.initEvent();
});