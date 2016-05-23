var list={
		oTable1:null,
	    getlist : function(){
			list.oTable1 = $('#table2').dataTable( {
			"bAutoWidth": false,
			"aaSorting": [],
			"ordering" : false,
			"bServerSide" : true,
			"searching": true,
			"sAjaxSource" : contentPath + "/activitiSxd/getActivitiList/sxd",
			"aoColumns" : [   
		                   {"mData" : "comName"},   
		                   {"mData" : "name"},   
		                   {"mData" : "procInsId"},   
		                   {"mData" : "createTime"},   
		                   {"mData" : "businessId"}
		                   ],
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
		                           "data" : {  
		                               aoData : JSON.stringify(aoData)  
		                           },  
		                           "success" : function(resp) {  
		                               fnCallback(resp);  
		                           }  
		                       });  
		                   },
		   "aoColumnDefs":[  
						   {
								   
								"sClass":"center col-sm-1",  
							    "aTargets":[0],  
							    "mData":"comName",  
							    "mRender":function(a,b,c,d){
							    	if (c.comName)
						        	{
						        		return c.comName;
						        	}
						        	else
						        	{
						        		return "";
						        	}
							    }
						   },
						   {
							   
								"sClass":"center col-sm-1",  
							    "aTargets":[1],  
							    "mData":"name",  
							    "mRender":function(a,b,c,d){
							    	if (c.name)
						        	{
						        		return c.name;
						        	}
						        	else
						        	{
						        		return "";
						        	}
							    }
						   },
						   {
							   
								"sClass":"center col-sm-1",  
							    "aTargets":[2],  
							    "mData":"procInsId",  
							    "mRender":function(a,b,c,d){
							    	if (c.procInsId)
						        	{
							    		return '<a  href="#" class="process-definition" data-data="申请公司：'+c.comName +'；当前状态：'+c.name +'；" data-urlid="'+c.procInsId +'">查看流程</a>';
						        	}
						        	else
						        	{
						        		return "";
						        	}
							    }
						   },
						   {
							   
								"sClass":"center col-sm-1",  
							    "aTargets":[3],  
							    "mData":"createTime",  
							    "mRender":function(a,b,c,d){
							    	if (c.createTime)
						        	{
						        		return c.createTime;
						        	}
						        	else
						        	{
						        		return "";
						        	}
							    }
						   },
						   {
							   
								"sClass":"left col-sm-1",  
							    "aTargets":[4],  
							    "mData":"CREATE_TIME",  
							    "mRender":function(a,b,c,d){
							    	 //显示“代偿”、“逾期通知”、“逾期历史记录” 
	                         	   var html  =[];
							    	 if(c.assignee=='' || c.assignee==null){
							    		 /*签收*/
							    		 html.push('<a class="claim" href="#"  data-task-id="'+c.taskId+'">签收</a>  &nbsp;');
							    	 }else{
							    		 if(c.suspensionState==2){
							    			 html.push('<span class="red" >此流程被挂起 &nbsp;</span>');
							    		 }else{
								    		 if(c.taskDefKey=='role_task_assign'){
								    			 //<!--现场任务分派节点-->
								    			 html.push('<a class="flow-bootbox-options" data-taskid="'+c.taskId+'" data-taskDefKey="'+c.taskDefKey+'" data-buskey="'+c.businessId+'" data-procInsId="'+c.procInsId+'"  href="#">分派任务</a>  &nbsp;');
								    		 }else if(c.taskDefKey=='role_data_backfill'){
								    			// <!--数据回填节点-->
								    			 html.push('<a class="flow-bootbox-options" data-taskid="'+c.taskId+'" data-taskDefKey="'+c.taskDefKey+'" data-buskey="'+c.businessId+'" data-procInsId="'+c.procInsId+'"  href="#">信息回填</a> &nbsp; ');
								    			 html.push('<a class="print-detail" data-taskid="'+c.taskId+'" data-taskDefKey="'+c.taskDefKey+'" data-buskey="'+c.businessId+'"  href="#">打印清单</a>  &nbsp;');
								    		 }else{
								    			// <!--其他的则是办理功能-->
								    			 html.push('<a class="flow-bootbox-options" data-taskid="'+c.taskId+'" data-taskDefKey="'+c.taskDefKey+'" data-buskey="'+c.businessId+'" data-busComName="'+c.comName +'"data-procInsId="'+c.procInsId+'" href="#">办理</a> &nbsp;');
								    		 }
								    		 //不是签收的  都可以转办
								    		 html.push('<a class="flow-turn-todo" data-taskid="'+c.taskId+'" data-taskDefKey="'+c.taskDefKey+'" data-buskey="'+c.businessId+'" href="#">转办</a>  &nbsp;');
							    		 }
							    	}
							    	
							    	 //查看资料
							    	 html.push('<a class="green flow-apply-detail" style="white-space:nowrap;" target="_blank" data-href="'+contentPath +'/activitiSxd/getActivitiList/getfwApplyBusDetail?busId='+c.businessId+'" >	查看资料</a> &nbsp;');
	                                 //操作记录
							    	 html.push('<a data-procInsId="'+c.procInsId+'" class="green js-find-communicate-log" style="white-space:nowrap;" href="#" >操作记录</a>');
							    	 
					        		return html.join("");
						    }
						   }
		                   ], 
				} );
		},
		//签收
		claim: function(id){
			var param={};
			param.taskId=id;
			$.ajax({
				url : contentPath+'/activitiSxd/task/claim',
				data :param ,
				type : 'POST',
				dataType : 'json',
				success : function(data) {
					if(data.status=='success'){
						// 重新加载数据
						list.oTable1.fnClearTable(0);  
						list.oTable1.fnDraw();
					}else{
						tools.openST({
							title : '处理失败', // {String}
							text : '签收失败', // {String}
							type : 'fail' // {String}
						});
					}
				},
				error :function(){
					tools.openST({
						title : '服务器异常', // {String}
						text : '服务器异常', // {String}
						type : 'fail' // {String}
					});
				}
			
			});
		},
		/**
		 *liutt
		 *待办任务列表点击办理按钮绑定事件
		 **/
		banliaction : function(taskId,businessId,taskDefKey,procInsId){
			$("#public-modal-form").load(contentPath+"/activitiSxd/banliGotoJsp?businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey+"&procInsId="+procInsId,null,function(){
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
			list.oTable1.fnClearTable(0);  
			list.oTable1.fnDraw();
		},
		//初始化事件
		inventStart :function(){
			
			$(document).on('click', 'th input:checkbox' , function(){
				var that = this;
				$(this).closest('table').find('tr > td:first-child input:checkbox')
				.each(function(){
					this.checked = that.checked;
					$(this).closest('tr').toggleClass('selected');
				});
			});


			$('[data-rel="tooltip"]').tooltip({placement: tooltip_placement});
			function tooltip_placement(context, source) {
				var $source = $(source);
				var $parent = $source.closest('table');
				var off1 = $parent.offset();
				var w1 = $parent.width();

				var off2 = $source.offset();
				//var w2 = $source.width();

				if( parseInt(off2.left) < parseInt(off1.left) + parseInt(w1 / 2) ) return 'right';
				return 'left';
			}
			
			/**
			 *liutt
			 *为签收做事件绑定
			 */
			$("tbody").delegate('.claim','click',function(){
				if (confirm("确定签收么？")) {
					//location.href=$(this).attr('data-url');
					//ajax 发送签收请求\
					var id = $(this).attr('data-task-id');
					list.claim(id);
				} else {
					return false;
				}
			});
			
			//为办理绑定点击事件
			$("tbody").delegate('.flow-bootbox-options','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				var procInsId =  $(this).attr('data-procInsId');
				list.banliaction(taskId,businessId,taskDefKey,procInsId);
			});
			
			/**
			 *liutt
			 *为查看流程做事件绑定
			 */
			$("tbody").delegate('.process-definition','click',function(){
				var data = $(this).attr('data-data');
				var procInsId = $(this).attr('data-urlid');
				$("#public-modal-form").load(contentPath+"/activitiSxd/otherGotoJsp?type=show_flow_chart&data="+encodeURI(encodeURI(data))+"&procInsId="+procInsId,null,function(){
					list.showModel();
				});
			});
			
			/**
			 *liutt
			 *为转办做事件绑定
			 */
			$("tbody").delegate('.flow-turn-todo','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				$("#public-modal-form").load(contentPath+"/activitiSxd/otherGotoJsp?type=task_transferred&businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey,null,function(){
					list.showModel();
				});
			});
			
			/**
			 *liutt
			 *为打印清单做事件绑定
			 */
			$("tbody").delegate('.print-detail','click',function(){
				var taskId= $(this).attr('data-taskid');
				var businessId = $(this).attr('data-buskey');
				var taskDefKey = $(this).attr('data-taskDefKey');
				$("#public-modal-form").load(contentPath+"/activitiSxd/otherGotoJsp?type=openAccount_printList&businessId="+businessId+"&taskId="+taskId+"&taskDefKey="+taskDefKey,null,function(){
					list.showModel();
				});
			});
			
			/**
			 *liutt
			 *为操作记录做事件绑定
			 */
			$("tbody").delegate('.js-find-communicate-log','click',function(){
				var procInsId = $(this).attr('data-procInsId');
				$("#public-modal-form").load(contentPath+"/activitiSxd/otherGotoJsp?type=operation_record&procInsId="+procInsId,null,function(){
					list.showModel();
				});
			});
			
			/**
			 * 点击查看资料，清除cookie中存的tab连接
			 */
			 
			 $("tbody").delegate('.flow-apply-detail','click',function(){
				 
				$.cookie('the_cookie', '#accountInfo', {path: '/' }); 
				location.href = $(this).attr('data-href');
				
			});
			 
			// 为任务状态下拉菜单做事件绑定
			$("#suspension_state").on("change", function(e){ 
				//$("#day").val("18");
				$('.tabbable .active').removeClass('active');
				$('.nav-tabs li:first').addClass('active');
				
				// 重新加载数据
				list.oTable1.fnClearTable(0);  
				list.oTable1.fnDraw();
			});
		}
};
jQuery(function($) {
	list.inventStart();
	list.getlist();
});