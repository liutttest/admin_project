<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="modal-form" class="modal" tabindex="-1"  data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">开票邮寄</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<form id="invoice-form" class="form-horizontal">
                       <div class="col-xs-6">
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">发票代码</label>
                                <div class="col-sm-9">
                                    <input class="input-large  col-xs-10 col-sm-5" type="text" id="invoiceNum" name="invoiceNum" placeholder="发票代码" />
                                </div>
                             </div>
                             <div class="form-group">
                             <label class="col-sm-3 control-label no-padding-right" >发票金额</label>
                             <div class="col-sm-9">
                                 <input class="input-large  col-xs-10 col-sm-5" type="text" id="money" name="money" placeholder="发票金额" />
                             </div>
                           </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">快递公司</label>
                                <div class="col-sm-9">
                                    <input class="input-large  col-xs-10 col-sm-5" type="text" id="courierCompany" name="courierCompany" placeholder="快递公司" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" >快递单号</label>
                                <div class="col-sm-9">
                                    <input class="input-large  col-xs-10 col-sm-5" type="text" id="courierNum" name="courierNum" placeholder="快递单号" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">备注</label>
                                <div class="col-sm-9">
                                <textarea rows="5" cols="60" placeholder="备注" id="remarks" name="remarks"></textarea>
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


<div id="modal-form-detail" class="modal" tabindex="-1"  data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">详情</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<form id="invoice-form" class="form-horizontal">
                       <div class="col-xs-6">
                       		<div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">业务编码</label>
                                <div class="col-sm-9" id="detail-bus-id">
                                </div>
                             </div>
                       		<div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">公司名称</label>
                                <div class="col-sm-9" id="detail-com-name">
                                </div>
                             </div>
                             <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">邮寄地址</label>
                                <div class="col-sm-9" id="detail-mail-address">
                                </div>
                             </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">发票代码</label>
                                <div class="col-sm-9" id="detail-invoice-num">
                                </div>
                             </div>
                             <div class="form-group">
                             <label class="col-sm-3 control-label no-padding-right" >发票金额</label>
                             <div class="col-sm-9" id="detail-money">
                             </div>
                           </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">快递公司</label>
                                <div class="col-sm-9" id="detail-courier-company">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" >快递单号</label>
                                <div class="col-sm-9" id="courier-num">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right">备注</label>
                                <div class="col-sm-9" id="detail-remark">
                                </div>
                            </div>
                           
                        </div>
					</form>
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm btn-primary" data-dismiss="modal">
					<i class="ace-icon fa fa-check"></i>
					确定
				</button>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->