<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger"> 投诉意见处理</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<form id="admin-form" class="form-horizontal">
                       <div class="col-xs-6">
                            <div class="form-group">
                                <div class="col-sm-9">
                                   	<textarea rows="5" cols="60" placeholder="处理意见" id="feed-remark"></textarea>
					
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
					确认处理
				</button>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->