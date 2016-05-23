<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--扶持贷 税信贷  申请放款  欢还款列表的企业详情-->
<div id="modal-form" class="modal" tabindex="-1" data-backdrop="static">
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




<div id="modal-form-2" class="modal" tabindex="-1" data-backdrop="static">
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

<!-- 还款记录modal -->
<div id="repayment-history-detail" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">还款记录</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12">
						<div>
							<table class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th class="c-align">还款日期</th>
										<th class="c-align">
											还款方式
										</th>
										<th class="c-align">
											还款金额
										</th>
										<th class="c-align">账单月</th>
										<th class="c-align">状态</th>
									</tr>
								</thead>
							
								<tbody>
								</tbody>
							</table>
							</div>
							</div>
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



<!-- 放款记录modal -->
<div id="loanapply-history-detail" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">放款记录</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12">
						<div>
							<table class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th class="c-align">申请金额</th>
										<th class="c-align">
											业务类型
										</th>
										<th class="c-align">
											
											年利率
										</th>
										<th class="c-align">
										<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
										交易日期
										</th>
										<th class="c-align">状态</th>
									</tr>
								</thead>
							
								<tbody>
								</tbody>
							</table>
							</div>
							</div>
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




<!-- 快速贷款详情-->
<div id="quickloan-modal-detail-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" > 快速贷款详情</h4>
			</div>

			<div class="modal-body">
				<div class="row">
                    <form class="form-horizontal">
                        <div class="col-xs-12 col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-6 control-label no-padding-right">企业名称:</label>
                                <div id="quickloan-com-name" class="col-sm-6 control-div">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-6 control-label no-padding-right">联系电话:</label>
                                <div id="quickloan-tel" class="col-sm-6 control-div">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-6 control-label no-padding-right">贷款金额:</label>

                                <div id="quickloan-money" class="col-sm-6 control-div">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-6 control-label no-padding-right">企业缴纳记录良好:</label>

                                <div id="quickloan-is-credit-good" class="col-sm-6 control-div">
                                </div>
                            </div>
                             <div class="form-group">
                                <label  class="col-sm-6 control-label no-padding-right">享受企业扶持政策:</label>
                                <div id="quickloan-is-com-support" class="col-sm-6 control-div">
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

<!-- 商务洽谈详情 -->
<div id="businesstalks-modal-detail-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" > 商务洽谈详情</h4>
			</div>

			<div class="modal-body">
				<div class="row">
                    <form class="form-horizontal">
                        <div class="col-xs-12 col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-6 control-label no-padding-right">姓名:</label>
                                <div id="businesstalks-name" class="col-sm-6 control-div">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-6 control-label no-padding-right">联系电话:</label>
                                <div id="businesstalks-tel" class="col-sm-6 control-div">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-6 control-label no-padding-right">机构名称:</label>

                                <div id="businesstalks-com-name" class="col-sm-6 control-div">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-6 control-label no-padding-right">事由:</label>

                                <div id="businesstalks-reason" class="col-sm-6 control-div">
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





<!-- 意见反馈详情-->
<div id="feedback-modal-detail-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" > 意见反馈</h4>
			</div>

			<div class="modal-body">
				<div class="row">
                    <form class="form-horizontal">
                        <div class="col-xs-12 col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-6 control-label no-padding-right">姓名:</label>
                                <div id="feedback-name" class="col-sm-6 control-div">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-6 control-label no-padding-right">联系电话:</label>
                                <div id="feedback-tel" class="col-sm-6 control-div">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-6 control-label no-padding-right">问题类型:</label>

                                <div id="feedback-feedtype" class="col-sm-6 control-div">
                                </div>
                            </div>
                            <div class="space-4"></div>
                            <div class="form-group">
                                <label class="col-sm-6 control-label no-padding-right">事由:</label>

                                <div id="feedback-reason" class="col-sm-6 control-div">
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

