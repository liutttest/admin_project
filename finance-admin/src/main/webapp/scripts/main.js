/*
 * jQuery File Upload Plugin JS Example 8.9.1
 * https://github.com/blueimp/jQuery-File-Upload
 *
 * Copyright 2010, Sebastian Tschan
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * http://www.opensource.org/licenses/MIT
 */

/* global $, window */

$(function () {
	var tmplStr = "{% for (var i=0, file; file=o.files[i]; i++) { %} "+
   ' <tr class="template-upload"> <td><span class="preview"></span></td>' +
    ' <td><p class="name">{%=file.name%}</p>'+    
     '<strong class="error text-danger"></strong> </td>'+       
     ' <td> <p class="size">上传中...</p>'+   
     '<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>'+  
     '</td><td>'+  '</td> </tr>{% } %}';
	
    // Initialize the jQuery File Upload widget:
    $('.fileupload').fileupload({
        // Uncomment the following to send cross-domain cookies:
        //xhrFields: {withCredentials: true},
//        url: 'server/php/'
        	dataType: 'json',
        	autoUpload:true        	

    }).bind('fileuploadadd',function(e,data){
    	
    	
    	// 上传之前
    	var tmplData = {};
		console.log(data);
		tmplData = $.extend(tmplData, {files:[{
			
			//url:data.url + '/' +data.result.data.url,
			//thumbnail_url: "http://url.to/thumnail.jpg ",
	        name: data.files[0].name,
	        type: data.files[0].type,
	        size: data.files[0].size,
	        //delete_url: "http://url.to/delete/file/" + data.result.data.fileId,
	        //delete_type: "DELETE"
		}]});

		$('.table .files').append(tmpl(tmplStr, tmplData));
    })
    .bind('fileuploadprogress',function(e,data){
    	// 处理中
    	console.log(data);

    	var progress = parseInt(data.loaded / data.total * 100, 10);
		$(this).find('.size:last').text('正在上传');
    	$(this).find('.progress').attr('aria-valuenow', progress).find('.progress-bar').css('width', progress + '%');

    })
    .bind('fileuploadfail', function (e, data) {
    	$td = $(this).find('.progress').parent('td');
    	$td.find('div.progress').fadeOut('slow',function(){
    		$td.find('p').text('上传失败');
    	});
    	
//    	$td.next().empty().append('<button class="btn btn-danger" id='+ data.result.data.fileId+ '>删除</button>').on('click', function (e){
//			var that = this;
//			e.preventDefault();
//			$(that.parentElement).remove();
//
//		});
    	
    	$td.next().empty().append('<button class="btn btn-danger">删除</button>').on('click', function (e){
			var that = this;
			e.preventDefault();
			$(that.parentElement).remove();
			
		});
    })
    .bind('fileuploaddone', function (e, data) {
    	
    	if(data.result.status == 'error'){
    		$td = $(this).find('.progress').parent('td');
        	$td.find('div.progress').fadeOut('slow',function(){
        		$td.find('p').text('上传失败:' + data.result.msg);
        		$($(this)).remove();
        	});
        	
        	$td.next().empty().append('<button class="btn btn-danger">删除</button>').on('click', function (e){
    			var that = this;
    			e.preventDefault();
    			$(that.parentElement).remove();
    			
    		});
    		return false;
    	}
    	// 上传结束
    	$td = $(this).find('.progress').parent('td');
    	$td.find('div.progress').fadeOut('slow',function(){
    		$td.find('p').text('上传成功');
    		$($(this)).remove();
    	});

//    	$td.fadeIn('fast',function(){$td.append('<span>done</span>');});
//    	$td.empty().append("<span>done</span>");
    	$td.next().empty().append('<button class="btn btn-danger" id='+ data.result.data.fileId+ '>删除</button>').on('click', function (e){
			var that = this;
			e.preventDefault();
				$.ajax({
					url : contentPath+'/deleteFile?fileId='+ data.result.data.fileId,
					data : '',
					type : 'POST',
					dataType : 'json',
					success : function() {
					}
				});
				$(that.parentElement).remove();
		});
    	
    	});
    
   
});

function upload_clear(){
	$('.fileupload').find('.files').empty();
}

