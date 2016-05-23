var sys_list = {
	oTable : null,
	getlist : function() {
		sys_list.oTable = $('#sys-task').dataTable({
			"bAutoWidth" : false,
			"aaSorting" : [],
			"ordering" : false,
			"bServerSide" : true,
			"searching" : true,
			"sAjaxSource" : contentPath + "/sysAdmin/list",
			"aoColumns" : [ 
			     {"mData" : "comName"},   
                 {"mData" : "name"},   
                 {"mData" : function(source){
                	 return '<a  href="#" class="process-definition" data-data="申请公司:'+source.comName +'; 当前状态:'+source.name +'" data-urlid="'+source.procInsId+'">查看流程</a>';
                 }},   
                 {"mData" : "createTime"},   
				 {"mData" : function(source) {
					 var html  =[];
					 if(source.suspensionState==2){
		    			 html.push('<span class="red" >此流程被挂起 &nbsp;</span>');
		    		 }
					 html.push('<a class="green flow-apply-detail" style="white-space:nowrap;" target="_blank" data-href="'+contentPath +'/sysAdmin/task/getfwApplyBusDetail?busId='+source.businessId+'" >查看资料</a>');
					return html.join('');
				 }
			}, ],
			 "fnServerParams" : function(aoData) { 
		    	 aoData.push({  
		            "name" : "suspensionState",
		            "value" : $('#suspension_state').val()
		        });
			 },
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
	inEvent:function(){
		/**
		 *为待办任务下拉选添加事件绑定
		 *
		 */
		$("#suspension_state").on("change", function(e){ 
			// 重新加载数据
			sys_list.oTable.fnClearTable(0);  
			sys_list.oTable.fnDraw();
		});
		/**
		 *为待办任务列表流程跟踪图片绑定点击事件
		 *
		 */
		 $("tbody").delegate('.process-definition', "click", function(){
			var dataUrl="?proInsId="+$(this).attr('data-urlid')+"&d="+new Date().getTime();
			$('#processdefinition-modal-title').html("流程跟踪图("+$(this).attr('data-data')+")");
			$('#processdefinition-picture').attr('src',contentPath+'/workflow/instanceDiagram/'+dataUrl);
			$('#picture-modal-form').modal('show');
		});
		
		$("tbody").delegate('.flow-apply-detail',"click",function(){
			$.cookie('the_cookie', '#accountInfo', {path: '/' }); 
			location.href = $(this).attr('data-href');
		});
		
		$('#picture-modal-form').on('hidden.bs.modal', function () {
			$('#processdefinition-picture').attr('src','');
		});
		
	}
	
};
jQuery(function($) {
	sys_list.getlist();
	sys_list.inEvent();
});

