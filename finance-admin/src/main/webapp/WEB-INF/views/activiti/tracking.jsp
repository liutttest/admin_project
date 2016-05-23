<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<!-- 1.获取到规则流程图 这里是用的strust2的标签得到上面上面放入值栈的值-->
<img style="position: absolute;top: 0px;left: 0px;" src="http://localhost:8080/workflow/resource/read?processDefinitionId=fuchidai:1:5&resourceType=image">
<!-- 2.根据当前活动的坐标，动态绘制DIV -->
<div style="position: absolute;border:1px solid red;top: ${acs.y}px;left: ${acs.x}px;width: ${acs.width}px;height:${acs.height }px;"></div>
