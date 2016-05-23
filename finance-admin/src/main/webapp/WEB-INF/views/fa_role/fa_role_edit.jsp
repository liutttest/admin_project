<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="edit-role-modal"></h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12">
					   <form id="admin-form" class="form-horizontal">
                           <div class="form-group">
                               <label class="col-sm-3 control-label no-padding-right" for="role-name">岗位名称</label>

                               <div  class="col-sm-9">
                                   <input class="input-large col-xs-10 col-sm-5 " type="text" id="role-name" name="role-name" placeholder="岗位"/>
                               </div>
                           </div>
						<div class="space-4"></div>
						<div class="form-group">
                           <label class="col-sm-3 control-label no-padding-right" for="dept">部门</label>

                           <div class="col-sm-9">
                               <select id="dept" name="dept">
                                   <option value=""></option>
                                   <c:forEach items="${ depts}" var="dept">
                                       <option value="${dept.deptId}">${dept.deptName}</option>
                                   </c:forEach>
                               </select>
                           </div>
                       </div>
                       <div class="space-4"></div>
						<div class="form-group">
							<label class="col-sm-9 form-field-username col-sm-3 control-label no-padding-right">权限：
							</label>
							<div class="zTreeDemoBackground left">
								<ul id="treeDemo" class="ztree" style="margin-left:33%"></ul>
							</div>
							<div class="right" style="display: none">
								<input type="checkbox" id="py" class="checkbox first" checked /><span></span>
								<input type="checkbox" id="sy" class="checkbox first" checked /><span></span><br />
								<input type="checkbox" id="pn" class="checkbox first" checked /><span></span>
								<input type="checkbox" id="sn" class="checkbox first" checked /><span></span><br />
							</div>
						
						
						
						   <div class="space-4"></div>
							<!-- <label for="form-field-username">密码</label>

							<div>
								<input class="input-large" type="password" id="password" name="password" placeholder="密码" />
							</div> -->
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right" for="remark">备注</label>

							<div  class="col-sm-9">
								<input class="input-large col-xs-10 col-sm-5 " type="text" id="remark" name="remark" placeholder="备注"/>
							</div>
						</div>
						</form>
					</div>
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