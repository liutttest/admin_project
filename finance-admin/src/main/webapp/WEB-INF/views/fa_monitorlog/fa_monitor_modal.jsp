<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<div id="modal-form" class="modal" tabindex="-1" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">监控详情</h4>
			</div>

			<div class="modal-body">
				<div class="row">
                    <form  class="form-horizontal">
                        <div class="col-xs-12 col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">标题:</label>

                                <div id="monitor-title" class="col-sm-8 control-div">

                                </div>
                            </div>
                        </div>
                       <!--  <div class="col-xs-12 col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">银行接口:</label>

                                <div  class="col-sm-8 control-div" id="bank-service">

                                </div>
                            </div>
                          
                        </div> -->
                        <div class="col-xs-12 col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">公司名称:</label>

                                <div id="com-name" class="col-sm-8 control-div">

                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label no-padding-right">业务id:</label>

                                <div id="business-id" class="col-sm-8 control-div">

                                </div>
                            </div>
                          
                        </div>
                        <div class="col-xs-12 col-sm-12">
                            <div class="form-group">
                                <label class="col-sm-2 control-label no-padding-right">内容:</label>
                                <div class="col-sm-10 control-div">
                                	<!-- <textarea rows="" cols=""></textarea> -->
                                	<pre id="monitor-content"></pre>
                                </div>
                            </div>
                        </div>
                       <!--  <div class="col-xs-12 col-sm-12">
                            <div class="form-group">
                                <label class="col-sm-2 control-label no-padding-right">输入参数:</label>
                                <div class="col-sm-10 control-div">
                                	<textarea rows="" cols=""></textarea>
                                	<pre id="monitor-incontent"></pre>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12">
                            <div class="form-group">
                                <label class="col-sm-2 control-label no-padding-right">输出参数:</label>
                                <div class="col-sm-10 control-div">
                                	<textarea rows="" cols=""></textarea>
                                	<pre id="monitor-outcontent"></pre>
                                </div>
                            </div>
                        </div> -->
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
