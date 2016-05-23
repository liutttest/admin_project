<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.evan.jaron.com/tags/security"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- #section:basics/sidebar -->
<div id="sidebar" class="sidebar responsive">
	<script type="text/javascript">
		try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
	</script>

	<div class="sidebar-shortcuts" id="sidebar-shortcuts">
		<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
			<button class="btn btn-success">
				<i class="ace-icon fa fa-signal"></i>
			</button>

			<button class="btn btn-info">
				<i class="ace-icon fa fa-pencil"></i>
			</button>

			<!-- #section:basics/sidebar.layout.shortcuts -->
			<button class="btn btn-warning">
				<i class="ace-icon fa fa-users"></i>
			</button>

			<button class="btn btn-danger">
				<i class="ace-icon fa fa-cogs"></i>
			</button>

			<!-- /section:basics/sidebar.layout.shortcuts -->
		</div>

		<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
			<span class="btn btn-success"></span>

			<span class="btn btn-info"></span>

			<span class="btn btn-warning"></span>

			<span class="btn btn-danger"></span>
		</div>
	</div><!-- /.sidebar-shortcuts -->

	<ul class="nav nav-list">
		<li class="">
			<a href="${contentPath}/main">
				<i class="menu-icon fa fa-tachometer"></i>
				<span class="menu-text"> 首页 </span>
			</a>

			<b class="arrow"></b>
		</li>
		<security:permFilter var="funcs" parentCode="ROOT"/>
	    <c:forEach items="${funcs}" var="func">
	    <c:if test="${func.groupCode!='00' }">
	    <security:permFilter var="leaf" parentCode="${func.code}"/>
		<li class="">
			<a href="${contentPath}${func.url}" <c:if test="${not empty leaf}"> class="dropdown-toggle" </c:if>  >
				 <i class="menu-icon fa ${func.cssClass }"></i>
				<span class="menu-text"> ${func.name} </span>
				<c:if test="${not empty leaf}">
				  <b class="arrow fa fa-angle-down"></b>
				</c:if>
			</a>
			<b class="arrow"></b>
			<c:if test="${not empty leaf}">
			<%-- 无子集菜单不显示 --%>
				<ul class="submenu">
				<c:forEach items="${leaf}" var="progm">
					<li class="">
						<a href="${contentPath}${progm.url}">
							<i class="menu-icon fa fa-caret-right"></i>
							${progm.name}
						</a>
	
						<b class="arrow"></b>
					</li>
				 </c:forEach>
				</ul>
			</c:if>
		</li>
		</c:if>
		</c:forEach>
		
	</ul><!-- /.nav-list -->

	<!-- #section:basics/sidebar.layout.minimize -->
	<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
		<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
	</div>

	<!-- /section:basics/sidebar.layout.minimize -->
	<script type="text/javascript">
		try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
		$( function() {
		    $('.nav-list li').each(function(){
		        //var strLength = $('.nav-list li').length;
		        var url = window.location.href;
		        if (window.location.href.indexOf('#') != -1) {
		        	url = url.substring(0,url.length-1);
		        }
		        $(this).removeClass('active');
		        var str = (String(url)).substring('21',(String(url)).length);
		        	var href = $(this).children(":first").attr('href');
		        	str= str.replace(contentPath,'');
		        	href = href.replace(contentPath,"");
		            if((str+"/").indexOf(href+"/") >= 0 && !$.isEmptyObject(href)){
		                $(this).parent().parent().addClass('active open');
		                $(this).addClass('active');
		            }
		    });
		});
	</script>
	
</div>
