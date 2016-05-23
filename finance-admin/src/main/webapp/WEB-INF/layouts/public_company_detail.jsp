<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="modal-form" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">企业详情</h4>
			</div>

			<div class="modal-body">
				<div class="row">
                    <form  class="form-horizontal">
                        <div class="col-xs-12 col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">企业名称:</label>

                                <div id="com-name" class="col-sm-8 control-div">

                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">状态:</label>

                                <div id="com-status" class="col-sm-8 control-div">

                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">积分:</label>

                                <div id="com-record" class="col-sm-8 control-div">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">等级:</label>

                                <div id="com-level" class="col-sm-8 control-div">
                                    <input class="input-large" type="text" id="email" name="email" placeholder="邮件" />
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">税号:</label>

                                <div id="com-tax" class="col-sm-8 control-div">

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">上年净利润:</label>

                                <div id="last-year-profit" class="col-sm-8 control-div">

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">上年营业额:</label>

                                <div id="last-year-money" class="col-sm-8 control-div">

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">行业:</label>

                                <div id="com-business" class="col-sm-8 control-div">

                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12">
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">企业信用记录查询授权书:</label>

                                <div class="col-sm-8 control-div">
                                    <img src="" id="com-credit-power">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">企业年度财务报表:</label>

                                <div class="col-sm-8 control-div">
                                    <img src="" id="com-financial">
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




<div id="modal-form-2" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="com-company-per"></h4>
			</div>

			<div class="modal-body">
				<div class="row">
                    <form class="form-horizontal">
                        <div class="col-xs-12 col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">姓名:</label>
                                <div id="com-per-name" class="col-sm-8 control-div">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">性别:</label>
                                <div id="com-per-sex" class="col-sm-8 control-div">
                                </div>
                            </div>
                            <div class="space-4"></div>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">证件类型:</label>

                                <div id="com-per-card-type" class="col-sm-8 control-div">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">证件号码:</label>

                                <div id="com-per-card-num" class="col-sm-8 control-div">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12">
                            <div class="form-group">
                                <label  class="col-sm-4 control-label no-padding-right">身份证:</label>
                                <div  class="col-sm-8 control-div" >
                                    <img src="" id="per-card">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">个人信用记录查询授权书:</label>
                                <div class="col-sm-8 control-div" >
                                    <img src="" id="per-credit-power">
                                </div>
                            </div>
                            <div class="space-4"></div>
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