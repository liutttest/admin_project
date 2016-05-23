var setting = {
	check : {
		enable : true
	},
	data : {
		simpleData : {
			enable : true
		}
	}
};

var zNodes = [];
function getAllSysFunk() {
	var url = contentPath + "/faRole/getAllFunc";
	$.ajax({
		type : "POST",
		url : url,
		dataType : "json",
		cache : false,
		success : function(data) {
			if (data.data != null && data.data.length > 0) {
				var j = 0;
				for (var i = 0; i < data.data.length; i++) {
					zNodes[j] = {
						'id' : data.data[i].funcCode,
						'pId' : data.data[i].parentId,
						'name' : data.data[i].name,
						'open' : true,
						'value': data.data[i].funcId
					};
					j++;
				}
			}
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			setCheck();
			$("#py").bind("change", setCheck);
			$("#sy").bind("change", setCheck);
			$("#pn").bind("change", setCheck);
			$("#sn").bind("change", setCheck);
		}
	});
}

function setCheck() {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo"), py = $("#py").attr(
			"checked") ? "p" : "", sy = $("#sy").attr("checked") ? "s" : "", pn = $(
			"#pn").attr("checked") ? "p" : "", sn = $("#sn").attr("checked") ? "s"
			: "", type = {
		"Y" : py + sy,
		"N" : pn + sn
	};
	zTree.setting.check.chkboxType = type;
	showCode('setting.check.chkboxType = { "Y" : "' + type.Y + '", "N" : "'
			+ type.N + '" };');
}

var code;
function showCode(str) {
	if (!code)
		code = $("#code");
	code.empty();
	code.append("<li>" + str + "</li>");
}

$(document).ready(function() {
	getAllSysFunk();// 设置节点内容，查询所有sysFunc
	// 创建zTree

});