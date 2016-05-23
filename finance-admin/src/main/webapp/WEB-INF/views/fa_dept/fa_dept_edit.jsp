<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="modal-form" class="modal" tabindex="-1"  data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="edit-dept-modal"></h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<form id="admin-form" class="form-horizontal">
                       <div class="col-xs-6">
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="deptName">部门名称</label>
                                <div class="col-sm-9">
                                    <input class="input-large  col-xs-10 col-sm-5" type="text" id="deptName" name="deptName" placeholder="请填写部门名称" />
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

				<button class="btn btn-sm btn-primary" id="save-dept">
					<i class="ace-icon fa fa-check"></i>
					保存
				</button>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->