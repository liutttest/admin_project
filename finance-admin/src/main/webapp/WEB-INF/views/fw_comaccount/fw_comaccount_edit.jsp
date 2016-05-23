<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">用户详情</h4>
			</div>

			<div class="modal-body">
				<div class="row">
                    <form  class="form-horizontal">
                        <div class="col-xs-12 col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">用户名:</label>

                                <div id="user-name" class="col-sm-8 control-div">

                                </div>
                            </div>
                            
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">电话:</label>

                                <div id="user-tel" class="col-sm-8 control-div">
                                </div>
                            </div>
                             <div class="space-4"></div>
                             <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">邮件:</label>

                                <div id="user-email" class="col-sm-8 control-div">

                                </div>
                            </div>
                             <!-- <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">公司名称:</label>

                                <div id="com-name" class="col-sm-8 control-div">

                                </div>
                            </div> -->
                            
                        </div>
                        <div class="col-xs-12 col-sm-6">
                           <div class="form-group">
                                <label class="col-sm-5 control-label no-padding-right">真实姓名:</label>

                                <div id="real-name" class="col-sm-7 control-div">

                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-5 control-label no-padding-right">电话是否验证:</label>

                                <div id="is-user-tel" class="col-sm-7 control-div">
                                   
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-5 control-label no-padding-right">邮件是否验证:</label>

                                <div id="is-user-email" class="col-sm-7 control-div">

                                </div>
                            </div>
                            
                        </div>
                    </form>
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm" data-dismiss="modal">
					<i class="ace-icon fa fa-times"></i>
					关闭
				</button>

			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->

