var list={
		oTable1:null,
	    getlist : function(){
		list.oTable1 = $('#table2').dataTable( {
		"bAutoWidth": false,
		"aaSorting": [],
		"ordering" : false,
		"bServerSide" : true,
		"searching": true,
		"sAjaxSource" : contentPath + "/activitiSxd/doGetQuickLoanList",
		"aoColumns" : [   
	                   {"mData" : "comName"},   
	                   {"mData" : "tel"},   
	                   {"mData" : "procInsId"},   
	                   {"mData" : "createTime"},   
	                   {"mData" : "loanId"}
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
					        		return "<span class='huanHangLeft' style='width:100%;'>"+c.comName+"</span>";
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
						    	if (c.tel)
					        	{
					        		return c.tel;
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
						    "mData":"contactName",  
						    "mRender":function(a,b,c,d){
						    	if (c.contactName)
					        	{
						    		return c.contactName;
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
						   
							"sClass":"center col-sm-1",  
						    "aTargets":[4],  
						    "mData":"CREATE_TIME",  
						    "mRender":function(a,b,c,d){
						    	 //显示“代偿”、“逾期通知”、“逾期历史记录” 
                         	   var html  =[];
						    	 if(c.assignee=='' || c.assignee==null){
						    		 /*签收*/
						    		 html.push('<a class="claim" href="#"  data-buskey="'+c.loanId+'"  data-task-id="'+c.taskId+'">签收</a>  &nbsp;');
						    	 }else{
						    		 if(c.suspensionState==2){
						    			 html.push('<span class="red" >此流程被挂起 &nbsp;</span>');
						    		 }else{
							    		 html.push('<a class="flow-bootbox-options" data-taskid="'+c.taskId+'" data-taskDefKey="'+c.taskDefKey+'" data-buskey="'+c.loanId+'" data-busComName="'+c.comName +'"data-procInsId="'+c.procInsId+'" href="#">办理</a> &nbsp;');
							    		 //不是签收的  都可以转办
							    		 html.push('<a class="flow-turn-todo" data-taskid="'+c.taskId+'" data-taskDefKey="'+c.taskDefKey+'" data-buskey="'+c.loanId+'" href="#">转办</a>  &nbsp;');
						    		 }
						    	 }
						    	
						    	 //查看资料
						    	 html.push('<a class="green flow-apply-detail" target="_blank" data-href="'+contentPath +'/activitiSxd/getActivitiList/getQuickloanDetail?loanId='+c.loanId+'" >	查看资料</a> &nbsp;');
                                /* //操作记录
						    	 html.push('<a data-procInsId="'+c.procInsId+'" class="green js-find-communicate-log" href="#" >操作记录</a>');*/
						    	 
				        		return html.join("");
					    }
					   }
	                   ], 
			} );
		},
		//签收
		claim: function(id,loanId){
			var param={};
			param.taskId=id;
			param.loanId=loanId;
			$.ajax({
				url : contentPath+'/activitiSxd/task/claim/quickloan',
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
					var loanId = $(this).attr('data-buskey');
					list.claim(id,loanId);
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
				$("#public-modal-form").load(contentPath+"/activitiSxd/otherGotoJsp?type=show_flow_chart&data="+data+"&procInsId="+procInsId,null,function(){
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