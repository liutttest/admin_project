<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<c:set var="contentPath" value="${pageContext.request.contextPath}" scope="session"></c:set>
<!DOCTYPE html>
<html lang="zh-CN">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>一路贷管理</title>
		
		<link rel="shortcut icon" href="/img/favicon.ico" type="image/x-icon" />
		
		<script type="text/javascript">
            var contentPath = '${contentPath}';
        </script>

		<meta name="description" content="overview &amp; stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${contentPath}/assets/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${contentPath}/assets/css/font-awesome.min.css" />

		<!-- page specific plugin styles -->

		<!-- text fonts -->
		<link rel="stylesheet" href="${contentPath}/assets/css/ace-fonts.css" />

		<!-- color box : do not delete this style file and this file should be import before ace.min.css -->
		<link rel="stylesheet" href="${contentPath}/assets/css/colorbox.css"/>

		<!-- ace styles -->
		<link rel="stylesheet" href="${contentPath}/assets/css/ace.min.css" id="main-ace-style" />
		

        <link rel="stylesheet" href="${contentPath}/styles/css/common.css"/>
		<!--[if lte IE 9]>
			<link rel="stylesheet" href="${contentPath}/assets/css/ace-part2.min.css" />
		<![endif]-->
		<link rel="stylesheet" href="${contentPath}/assets/css/ace-skins.min.css" />
		<link rel="stylesheet" href="${contentPath}/assets/css/ace-rtl.min.css" />

		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${contentPath}/assets/css/ace-ie.min.css" />
		<![endif]-->

        <link rel="stylesheet" href="${contentPath}/assets/css/jquery.gritter.css"/>
        <link rel="stylesheet" href="${contentPath}/styles/tools.css"/>
        
        <!-- upload img -->
        <link rel="stylesheet" href="${contentPath}/plugins/fileupload/jquery.fileupload-ui.css"/>

		<!-- inline styles related to this page -->

		<!-- ace settings handler -->
		<script src="${contentPath}/assets/js/ace-extra.min.js"></script>

		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

		<!--[if lte IE 8]>
		<script src="${contentPath}/assets/js/html5shiv.min.js"></script>
		<script src="${contentPath}/assets/js/respond.min.js"></script>
		<![endif]-->
		<script src='${contentPath }/assets/js/jquery.min.js'></script>
		<script src="${contentPath}/assets/js/jquery.validate.min.js"></script>
		
		<style>
        	.color-green { color: rgb(3, 116, 52);}
        	#textareaEditor { height:200px; max-height:400px; width:100%; }
        	.margin-t {margin-top: 8px;}
        	input, textarea { color: black !important; }
        	.dataTable>thead>tr>th[class*=sorting_] { color: #555; }
        	table td { word-break:break-all; }
        	textarea {  resize: none; } 
        	a { cursor: pointer; }
        	.common-select { width: 60% }
        	.huanHangLeft{ word-break: break-all !important;text-align:left !important; }
        	
        	/* 处理查看图片modal中，X显示不全 */
        	.ace-thumbnails>li {
		    overflow: inherit !important;
		    margin: 16px !important; }
		</style>
	
	</head>

	<body class="no-skin">
		<!-- #section:basics/navbar.layout -->
		<tiles:insertAttribute name="header" />

		<!-- /section:basics/navbar.layout -->
		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>

			<tiles:insertAttribute name="menu" />
			<tiles:insertAttribute name="body" />
			<tiles:insertAttribute name="footer" />

            <!-- for test loading... when you done, please delete this -->
            <!-- <div class="row content-rl-common css-header">
                <div class="col-xs-12  col-sm-12  col-md-12  col-lg-12">
                    <h1>这个页面是 jack 用来测试 tools.js 的</h1>
                    <p>这个文件包如下功能：</p>
                    <p>1. loading...</p>
                    <p>2. 提示信息</p>
                    <p>3. 带确认的提示信息</p>
                </div>
                <div class="col-xs-12  col-sm-12  col-md-12  col-lg-12">
                    <button type="button" class="js-open-loading  btn btn-primary">打开 loading</button>
                    <button type="button" class="js-close-loading  btn btn-danger">关闭 loading</button>
                </div>
                <div class="col-xs-12  col-sm-12  col-md-12  col-lg-12">
                    <button type="button" class="js-open-ST  btn btn-primary">打开 simple model</button>
                    <button type="button" class="js-close-ST  btn btn-danger">关闭 simple model</button>
                </div>
                <div class="col-xs-12  col-sm-12  col-md-12  col-lg-12">
                    Button trigger modal
                    <button type="button" class="js-open-CT  btn btn-primary btn-lg">
                        Launch demo modal
                    </button>
                </div>
            </div> -->


        </div><!-- /.main-container -->

		<!-- basic scripts -->

		<!--[if !IE]> -->
            <script type="text/javascript">
                window.jQuery || document.write("<script src='${contentPath}/assets/js/jquery.min.js'>"+"<"+"/script>");
            </script>
		<!-- <![endif]-->

		<!--[if IE]>
            <script type="text/javascript">
             window.jQuery || document.write("<script src='${contentPath}/assets/js/jquery1x.min.js'>"+"<"+"/script>");
            </script>
        <![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${contentPath}/assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>
		<script src="${contentPath}/assets/js/bootstrap.min.js"></script>

		<!-- page specific plugin scripts -->

		<!--[if lte IE 8]>
		  <script src="${contentPath}/assets/js/excanvas.min.js"></script>
		<![endif]-->
		<script src="${contentPath}/assets/js/jquery-ui.custom.min.js"></script>
		<script src="${contentPath}/assets/js/jquery.ui.touch-punch.min.js"></script>
		<script src="${contentPath}/assets/js/jquery.easypiechart.min.js"></script>
		<script src="${contentPath}/assets/js/jquery.sparkline.min.js"></script>
		<script src="${contentPath}/assets/js/flot/jquery.flot.min.js"></script>
		<script src="${contentPath}/assets/js/flot/jquery.flot.pie.min.js"></script>
		<script src="${contentPath}/assets/js/flot/jquery.flot.resize.min.js"></script>

		<!-- ace scripts -->
		<script src="${contentPath}/assets/js/ace-elements.min.js"></script>
		<script src="${contentPath}/assets/js/ace.min.js"></script>

		<!-- inline scripts related to this page -->
		<%--<script type="text/javascript">--%>
			<%--jQuery(function($) {--%>
				<%--$('.easy-pie-chart.percentage').each(function(){--%>
					<%--var $box = $(this).closest('.infobox');--%>
					<%--var barColor = $(this).data('color') || (!$box.hasClass('infobox-dark') ? $box.css('color') : 'rgba(255,255,255,0.95)');--%>
					<%--var trackColor = barColor == 'rgba(255,255,255,0.95)' ? 'rgba(255,255,255,0.25)' : '#E2E2E2';--%>
					<%--var size = parseInt($(this).data('size')) || 50;--%>
					<%--$(this).easyPieChart({--%>
						<%--barColor: barColor,--%>
						<%--trackColor: trackColor,--%>
						<%--scaleColor: false,--%>
						<%--lineCap: 'butt',--%>
						<%--lineWidth: parseInt(size/10),--%>
						<%--animate: /msie\s*(8|7|6)/.test(navigator.userAgent.toLowerCase()) ? false : 1000,--%>
						<%--size: size--%>
					<%--});--%>
				<%--})--%>
			<%----%>
				<%--$('.sparkline').each(function(){--%>
					<%--var $box = $(this).closest('.infobox');--%>
					<%--var barColor = !$box.hasClass('infobox-dark') ? $box.css('color') : '#FFF';--%>
					<%--$(this).sparkline('html',--%>
									 <%--{--%>
										<%--tagValuesAttribute:'data-values',--%>
										<%--type: 'bar',--%>
										<%--barColor: barColor ,--%>
										<%--chartRangeMin:$(this).data('min') || 0--%>
									 <%--});--%>
				<%--});--%>
			<%----%>
			<%----%>
			  <%--//flot chart resize plugin, somehow manipulates default browser resize event to optimize it!--%>
			  <%--//but sometimes it brings up errors with normal resize event handlers--%>
			  <%--$.resize.throttleWindow = false;--%>
			<%----%>
			 <%--/**--%>
			 <%--we saved the drawing function and the data to redraw with different position later when switching to RTL mode dynamically--%>
			 <%--so that's not needed actually.--%>
			 <%--*/--%>
			 <%--placeholder.data('chart', data);--%>
			 <%--placeholder.data('draw', drawPieChart);--%>
			<%----%>
			<%----%>
			  <%--//pie chart tooltip example--%>
			  <%--var $tooltip = $("<div class='tooltip top in'><div class='tooltip-inner'></div></div>").hide().appendTo('body');--%>
			  <%--var previousPoint = null;--%>
			<%----%>
			  <%--placeholder.on('plothover', function (event, pos, item) {--%>
				<%--if(item) {--%>
					<%--if (previousPoint != item.seriesIndex) {--%>
						<%--previousPoint = item.seriesIndex;--%>
						<%--var tip = item.series['label'] + " : " + item.series['percent']+'%';--%>
						<%--$tooltip.show().children(0).text(tip);--%>
					<%--}--%>
					<%--$tooltip.css({top:pos.pageY + 10, left:pos.pageX + 10});--%>
				<%--} else {--%>
					<%--$tooltip.hide();--%>
					<%--previousPoint = null;--%>
				<%--}--%>
				<%----%>
			 <%--});--%>
			<%----%>
			<%----%>
			<%----%>
			<%----%>
			<%----%>
			<%----%>
				<%--var d1 = [];--%>
				<%--for (var i = 0; i < Math.PI * 2; i += 0.5) {--%>
					<%--d1.push([i, Math.sin(i)]);--%>
				<%--}--%>
			<%----%>
				<%--var d2 = [];--%>
				<%--for (var i = 0; i < Math.PI * 2; i += 0.5) {--%>
					<%--d2.push([i, Math.cos(i)]);--%>
				<%--}--%>
			<%----%>
				<%--var d3 = [];--%>
				<%--for (var i = 0; i < Math.PI * 2; i += 0.2) {--%>
					<%--d3.push([i, Math.tan(i)]);--%>
				<%--}--%>
				<%----%>
			<%----%>
			<%----%>
				<%--$('#recent-box [data-rel="tooltip"]').tooltip({placement: tooltip_placement});--%>
				<%--function tooltip_placement(context, source) {--%>
					<%--var $source = $(source);--%>
					<%--var $parent = $source.closest('.tab-content')--%>
					<%--var off1 = $parent.offset();--%>
					<%--var w1 = $parent.width();--%>
			<%----%>
					<%--var off2 = $source.offset();--%>
					<%--//var w2 = $source.width();--%>
			<%----%>
					<%--if( parseInt(off2.left) < parseInt(off1.left) + parseInt(w1 / 2) ) return 'right';--%>
					<%--return 'left';--%>
				<%--}--%>
			<%----%>
			<%----%>
				<%--$('.dialogs,.comments').ace_scroll({--%>
					<%--size: 300--%>
			    <%--});--%>
				<%----%>
				<%----%>
				<%--//Android's default browser somehow is confused when tapping on label which will lead to dragging the task--%>
				<%--//so disable dragging when clicking on label--%>
				<%--var agent = navigator.userAgent.toLowerCase();--%>
				<%--if("ontouchstart" in document && /applewebkit/.test(agent) && /android/.test(agent))--%>
				  <%--$('#tasks').on('touchstart', function(e){--%>
					<%--var li = $(e.target).closest('#tasks li');--%>
					<%--if(li.length == 0)return;--%>
					<%--var label = li.find('label.inline').get(0);--%>
					<%--if(label == e.target || $.contains(label, e.target)) e.stopImmediatePropagation() ;--%>
				<%--});--%>
			<%----%>
				<%--$('#tasks').sortable({--%>
					<%--opacity:0.8,--%>
					<%--revert:true,--%>
					<%--forceHelperSize:true,--%>
					<%--placeholder: 'draggable-placeholder',--%>
					<%--forcePlaceholderSize:true,--%>
					<%--tolerance:'pointer',--%>
					<%--stop: function( event, ui ) {--%>
						<%--//just for Chrome!!!! so that dropdowns on items don't appear below other items after being moved--%>
						<%--$(ui.item).css('z-index', 'auto');--%>
					<%--}--%>
					<%--}--%>
				<%--);--%>
				<%--$('#tasks').disableSelection();--%>
				<%--$('#tasks input:checkbox').removeAttr('checked').on('click', function(){--%>
					<%--if(this.checked) $(this).closest('li').addClass('selected');--%>
					<%--else $(this).closest('li').removeClass('selected');--%>
				<%--});--%>
			<%----%>
			<%----%>
				<%--//show the dropdowns on top or bottom depending on window height and menu position--%>
				<%--$('#task-tab .dropdown-hover').on('mouseenter', function(e) {--%>
					<%--var offset = $(this).offset();--%>
			<%----%>
					<%--var $w = $(window)--%>
					<%--if (offset.top > $w.scrollTop() + $w.innerHeight() - 100) --%>
						<%--$(this).addClass('dropup');--%>
					<%--else $(this).removeClass('dropup');--%>
				<%--});--%>
			<%----%>
			<%--})--%>
		<%--</script>--%>

        <%--<!-- tools.js module -->--%>
         <div class="tools" style="z-index: 9999999999999999999">
            <div class="loading  js-loading  hidden" >
                <div class="bg" style="z-index: 1052">&nbsp;</div>
                <div class="img" style="z-index: 1052">
                    <img src="${contentPath}/img/loading.gif" alt="loading"/>
                </div>
            </div>
            <!-- Modal -->
            <div class="modal fade  confirm-tip-model" id="confirm-tip-model" tabindex="-1" role="dialog" aria-labelledby="confirmTipModelLabel" aria-hidden="true"  data-backdrop="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title  js-model-title" id="confirmTipModelLabel">Modal title</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-xs-4  col-sm-2  col-md-2 col-lg-2 tool-div">
                                    <img  class="js-img-fail  img-responsive  hidden" src="${contentPath}/img/tip-fail.png" alt="失败"/>
                                    <img class="js-img-success  img-responsive  hidden" src="${contentPath}/img/tip-success.png" alt="成功"/>
                                    <img class="js-img-warning  img-responsive  hidden" src="${contentPath}/img/tip-warning.png" alt="警告"/>
                                </div>
                                <div class="js-model-text  col-xs-8  col-sm-10  col-md-10  col-lg-10">

                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="js-confirm-model-left-btn  btn btn-primary  hidden" >Close</button>
                            <button type="button" class="js-confirm-model-right-btn  btn btn-primary  hidden">Save</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

            <!-- the following scripts are used in demo only for onpage help and you don't need them -->
		<link rel="stylesheet" href="${contentPath}/assets/css/ace.onpage-help.css" />

		<script type="text/javascript"> ace.vars['base'] = '..'; </script>
		<script src="${contentPath}/assets/js/ace/elements.onpage-help.js"></script>
		<script src="${contentPath}/assets/js/ace/ace.onpage-help.js"></script>
        <script src="${contentPath}/assets/js/jquery.gritter.min.js"></script>
        <script src="${contentPath}/js/tools/tools.js"></script>

        <script>
            $(function () {
                $('.js-open-loading').on('click', function (e) {
                    e.preventDefault();
                    tools.openLoading();
                });
                $('.js-close-loading').on('click', function (e) {
                    e.preventDefault();
                    tools.closeLoading();
                });

                $('.js-open-ST').on('click', function (e) {
                    e.preventDefault();
                    /**
                     * 参考 Gritter 使用 http://boedesign.com/blog/2009/07/11/growl-for-jquery-gritter
                     * 增加了 一个 type 属性； 设置 type 就不需要设置 image 属性； type 可以设置 三个值 fail, success, waring
                     * 其他使用与 Gritter param， 其实常用的只有 text， title ， type 这三个属性
                     */
                    tools.openST({
                        title: 'a ooh',             // {String}
                        text: '这是失败信息',       // {String}
                        type: 'fail'                // {String}
                    });

                });
                $('.js-close-ST').on('click', function (e) {
                    e.preventDefault();
                    tools.closeST();

                });

                $('.js-open-CT').on('click', function (e) {
                    e.preventDefault();

                    tools.openCT({
                        title: 'hello',         // {String} required model title
                        text: '成功了！！！',   // {String} required model text
                        type: 'success',        // {String} required 取值 success， fail， warning， default success
                        buttons: [              // {Array} required buttons, 可以有一个 button
                            {
                                text: '返回上一页 isClose=true',     // {String} required button text
                                fn: function () {                   // {Function} click function
                                    alert(1); // you code here
                                },
                                isClose: true           // {Boolean} 执行完代码后是否关闭 model
                            },
                            {
                                text: '首页 isClose=false',
                                fn: function () {
                                    alert(2); // you code here
                                },
                                isClose: false
                            }
                        ]
                    });
                })
            });
            
            $.ajaxSetup({
                contentType:"application/x-www-form-urlencoded;charset=utf-8",
                complete:function(XMLHttpRequest,textStatus){
                	if(typeof(XMLHttpRequest.getResponseHeader)=="function"){
                        var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
                        if(sessionstatus == "timeout"){
                        	/* alert(3); */
                            window.location = contentPath+"/login";
                            
                        }  
                	}
                }   
            });
            
        </script>
	</body>
</html>
