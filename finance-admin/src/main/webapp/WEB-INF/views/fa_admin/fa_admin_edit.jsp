<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="modal-form" class="modal" tabindex="-1"  data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="edit-admin-modal"></h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<form id="admin-form" class="form-horizontal">
                       <div class="col-xs-6">
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="username">用户名</label>

                                <div class="col-sm-9">
                                    <input class="input-large  col-xs-10 col-sm-5" type="text" id="username" name="username" placeholder="用户名" />
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="password">密码</label>

                                <div class="col-sm-9">
                                    <input class="input-large  col-xs-10 col-sm-5" type="password" id="password" placeholder="******" name="password" />
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="tel">电话</label>

                                <div class="col-sm-9">
                                    <input class="input-large  col-xs-10 col-sm-5" type="text" id="tel" name="tel" placeholder="电话"/>
                                </div>
                            </div>
                           <div class="space-4"></div>
                       </div>

                       <div class="col-xs-6">
                       <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="realName">真实姓名</label>

                                <div class="col-sm-9">
                                   <input class="input-large col-xs-10 col-sm-5" type="text" id="realName" name="realName" placeholder="真实姓名" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="email">邮件</label>

                                <div class="col-sm-9">
                                    <input class="input-large col-xs-10 col-sm-5" type="text" id="email" name="email" placeholder="邮件" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="role">岗位</label>

                                <div class="col-sm-9">
                                    <%-- <select id="role" name="role">
                                        <option value=""></option>
                                        <c:forEach items="${roles}" var="role">
                                            <option value="${role.roleId}">${role.roleName}</option>
                                        </c:forEach>
                                    </select> --%>
                                     <c:forEach items="${roles}" var="role">
                                           <%--  <option value="${role.roleId}">${role.roleName}</option> --%>
                                            <input type="checkbox" data-text="${role.roleName}" name="rolename" value="${role.roleId}"> ${role.roleName}</br>
                                     </c:forEach>
                                </div>
                            </div>
                        </div>

					</form>
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm" data-dismiss="modal">
					<i class="ace-icon fa fa-times"></i>
					取消
				</button>

				<button class="btn btn-sm btn-primary" id="save-admin">
					<i class="ace-icon fa fa-check"></i>
					保存
				</button>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->