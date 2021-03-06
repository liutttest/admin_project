<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="modal-form" class="modal" tabindex="-1"  data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger" id="edit-modal"></h4>
			</div>

			<div class="modal-body">
				<div class="row">
				<div class="col-sm-12">
					<form id="admin-form" class="form-horizontal">
                       <div class="col-xs-12">
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="tempName">模板标题</label>
                                <div class="col-sm-9">
                                    <input class="input-large  col-xs-10 col-sm-5" type="text" id="tempName" name="tempName"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="tempSceneName">场景</label>
                                <div class="col-sm-9">
                                	<select id="tempSceneName" name="tempSceneName">
                                		<c:forEach items="${faTempScenes }" var="faTempScene">
                                			<option value="${faTempScene.tsId }">${faTempScene.sceneName }</option>
                                		</c:forEach>
		                            </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="isDefault">是否默认</label>
                                <div class="col-sm-9">
                                	<select id="isDefault" name="isDefault">
                                		<option value="1">是</option>
                                		<option value="0">否</option>
		                            </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label no-padding-right" for="title">站内信标题</label>
                                <div class="col-sm-9">
                                    <input class="input-large  col-xs-10 col-sm-5" type="text" id="title" name="title"/>
                                </div>
                            </div>
                            <div class="form-group">
                            	<textarea rows="100%" class="col-sm-12" style="height:240px;" id="content" name="content"></textarea>
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

				<button class="btn btn-sm btn-primary" id="save">
					<i class="ace-icon fa fa-check"></i>
					保存
				</button>
			</div>
		</div>
	</div>
</div><!-- PAGE CONTENT ENDS -->
<input type="hidden" name="tid" id="tid">
<%-- <link rel="stylesheet" type="text/css" href="${contentPath}/plugins/editor/css/wangEditor-1.3.11.css">
<script type="text/javascript" src='${contentPath}/plugins/editor/js/wangEditor-1.3.11.js'></script>
<script type="text/javascript">
	$('#content').wangEditor();
</script> --%>

