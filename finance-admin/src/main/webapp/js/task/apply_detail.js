$(function () {

    var submit = false;

    var action = {
    	/**
    	 * 页面加载，判断cookie中的之前被选中的选项卡是哪个，并赋给相应样式
    	 */
    	onLoadTabStyle :function(){
    		for(var i=0; $('.nav-tabs li').length>i; i++){
        		if($($('.nav-tabs li').find('a')[i]).attr('href') == $.cookie('the_cookie') ){
        			$('.nav-tabs li').removeClass('active');
        			$($('.nav-tabs li')[i]).addClass('active');
        			for(var i=0; $('.tab-content .tab-pane').length > i; i++) {
        				if($('.nav-tabs > .active > a').attr('href') == '#'+$($('.tab-content .tab-pane')[i]).attr('id')) {
        					$($('.tab-content .tab-pane')).removeClass(' in active');
        					$($('.tab-content .tab-pane')[i]).addClass(' in active');
        				}
        			}
        		}
        	}
    	},
    	
    	doUpdatePass : function(busId, infoKey){
			var url = contentPath + "/fwBusinessSxd/doUpdateNoPassToPass";
            var param = {};
            param.busId = busId; //业务id
            param.infoKey = infoKey; // 资料key

            $.ajax({
                url: url,
                data: param,
                type: 'POST',
                dataType: 'json',
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
	                                	window.location.reload();//刷新当前页面
	                                },
	                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
	                            }
	                        ]
	                    });
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
    	/**
    	 * 将资料“标记为不合格”
    	 * @param busId 业务id
    	 * @param infoName 资料名称
    	 * @param infoKey 资料key
    	 */
		doUpdateNoPass : function(busId,infoName,infoKey){
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
	                                	window.location.reload();//刷新当前页面
	                                },
	                                isClose: true          // {Boolean} 执行完代码后是否关闭 model
	                            }
	                        ]
	                    });
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
    	
    };

    var bind = {
        bindEvent: function () {
        	
        	/**
        	 * 点击"标记为不合格"(luy)
        	 */
        	$('.js-do-nopass').on('click', function (e) {
                e.preventDefault();
                var busId = $('#businessId').val();
                var infoName = $(this).attr("data-infoName");
                var infoKey = $(this).attr("data-infoKey");
                action.doUpdateNoPass(busId, infoName, infoKey);
            });
        	
        	/**
        	 * “已标记为不合格”的点击时间，将其再变回“标记为不合格”
        	 */
        	$('.js-do-pass').on('click', function (e) {
                e.preventDefault();
                var busId = $('#businessId').val();
                var infoKey = $(this).attr("data-infoKey");
                action.doUpdatePass(busId, infoKey);
            });
        	
        	/**
        	 * 选项卡的点击事件，将当前选项卡的href存入cookie
        	 */
        	$('.nav-tabs > li').on('click', function (e) {
        		e.preventDefault();
//        		$.cookie('path','/');
//        		$.cookie('the_cookie', $(this).find('a').attr('href'));
        		$.cookie('the_cookie', $(this).find('a').attr('href'), {path: '/' }); 
        	});
        	
        	
        	
        	
        },

        bindInit: function () {
        }
    };

    bind.bindEvent();
    bind.bindInit();
    
    action.onLoadTabStyle(); //页面加载，判断cookie中的之前被选中的选项卡是哪个，并赋给相应样式
    
    

});