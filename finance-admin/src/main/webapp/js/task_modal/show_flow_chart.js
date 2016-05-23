var showFlowChart = {
		
		/**
		 * 查看图片的大图
		 * @type {string}
		 */
        showBigImg : function(){
			var $overflow = '';
			var colorbox_params = {
				rel: 'colorbox',
				reposition:true,
				scalePhotos:true,
				scrolling:false,
				close:'&times;',
				maxWidth:'100%',
				maxHeight:'100%',
				current: '',
				previous: '<i class="glyphicon glyphicon-arrow-left"></i>',
				next:'<i class="glyphicon glyphicon-arrow-right"></i>',
				onOpen:function(){
					$overflow = document.body.style.overflow;
					document.body.style.overflow = 'hidden';
				},
				onClosed:function(){
					document.body.style.overflow = $overflow;
				},
				onComplete:function(){
					$.colorbox.resize();
				}
			};
			$('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);
        },
		
		// 查询流程图
	  	getPicture:function(procInsId){
	  		var dataUrl="?proInsId="+procInsId+"&d="+new Date().getTime();
			$('#processdefinition-picture').attr('src',contentPath+'/workflow/instanceDiagram/'+dataUrl);
			$('.cboxElement').attr('href',contentPath+'/workflow/instanceDiagram/'+dataUrl);
	  	},
		
		//初始化事件
		initEvent : function() {
			
		},
		
		// 页面加载成功的数据查询
		initLoad:function(){
			var procInsId = $('#procInsId').val();
			showFlowChart.getPicture(procInsId);
			
			showFlowChart.showBigImg();
		},
};

jQuery(function($) {
	showFlowChart.initEvent();
	showFlowChart.initLoad();
});
