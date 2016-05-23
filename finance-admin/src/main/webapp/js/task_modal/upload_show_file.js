var uploadShowFile = {
		//初始化事件
		initEvent : function() {
	    	
	    	/**
			 * 点击 '企业纳税信用等级证明 34'--上传图片 (luy)
			 */
//			$('#qinsxydjzm-a').on('click', function() {
			$("div").delegate('#qinsxydjzm-a', "click", function(e){
				$('#businessId-uplodad').val($('#businessId').val());
				$('#fileType').val('34');
				$('#fkId').val($('#businessId').val());
				$('.table .files').empty();
				$('#upload-modal').modal('show');
				
				$('#upload-modal').on('hidden.bs.modal', function () {
					$('body').addClass('modal-open');
				});
			});
			
			/**
			 * 关闭上传图片的modal
			 */
			$("div").delegate('.js-uploadImg-close', "click", function(e){
				$('#upload-modal').modal('hide');
			});
			
		},
		
		
		initLoad:function(){
		},
};

jQuery(function($) {
	uploadShowFile.initEvent();
	uploadShowFile.initLoad();
});
