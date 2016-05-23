<%@page import="com.evan.finance.admin.utils.WorkFlowUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<!-- 上传文件 -->
<div class="modal fade" id="upload-modal" role="dialog" aria-labelledby="gridSystemModalLabel"  data-backdrop="static">
    <div class="modal-dialog" role="document" style="margin-top: 0;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close js-uploadImg-close"  aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="gridSystemModalLabel">图片上传</h4>
            </div>
            <div class="modal-body">
                <div class='box-content'>
                    <form action='${contentPath}/uploadFile' enctype='multipart/form-data' class='fileupload' method='POST' autocomplete="off"/>
                    <input id="businessId-uplodad" name="businessId" type='hidden'/>
                    <input id="fileType" name="fileType" type='hidden'/>
                    <input id="fkId" name="fkId" type='hidden'/>
                    <input id="businessType" name="businessType" type='hidden' value="01"/>
                    <input id="fkType" name="fkType" type='hidden' value="01"/>
                    <input id="uploadCount" name="uploadCount" type='hidden'/>
                    <div class='row-fluid fileupload-buttonbar'>
                        <div class='span7'>
                             <span class='btn btn-success fileinput-button'>
                               <i class='icon-plus icon-white'></i>
                               <span>上传图片</span>
                               <input data-autoupload="true"  id="fileImg" multiple='' name='files[]' type='file'/>
                             </span>
                        </div>
                        <!--<div class='span5 fileupload-progress fade'>-->
                        <!--<div aria-valuemax='100' aria-valuemin='0'-->
                        <!--class='progress progress-success progress-striped active' role='progressbar'>-->
                        <!--<div class='bar' style='width:0%;'></div>-->
                        <!--</div>-->
                        <!--<div class='progress-extended'></div>-->
                        <!--</div>-->
                    </div>
                    <div class='fileupload-loading'></div>
                    <br/>
                    <div class="table-scroll">
                        <table class='table table-striped' role='presentation'>
                            <tbody class='files' data-target='#modal-gallery' data-toggle='modal-gallery'></tbody>
                        </table>
                    </div>
                    <div class="row img-cc">
                        <p>
                            <span class="">图片规格：</span>
                            <span>图片不小于200像素 X 200像素，不大于1M。</span>
                        </p>
                    </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button"  class="btn btn-default js-uploadImg-close">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>




<script src="${contentPath }/js/task_modal/upload_show_file.js"></script>

<!-- / fileupload -->
<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload">
        <td>
            <span class="preview"></span>
        </td>
        <td>
            <p class="name">{%=file.name%}</p>
            <strong class="error text-danger"></strong>
        </td>
        <td>
            <p class="size">Processing...</p>
            <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>
        </td>
        <td>
            <button class="btn btn-warning cancel">
                <i class="glyphicon glyphicon-ban-circle"></i>
                <span>Cancel</span>
            </button>

        </td>
    </tr>
{% } %}
</script>

<link rel="stylesheet" href="${contentPath}/plugins/fileupload/jquery.fileupload-ui.css"/>
<script src='${contentPath}/plugins/fileupload/vendor/jquery.ui.widget.js' type='text/javascript'></script>
<script src='${contentPath}/plugins/fileupload/jquery.iframe-transport.js' type='text/javascript'></script>
<script src='${contentPath}/plugins/fileupload/jquery.fileupload.js' type='text/javascript'></script>
<script src='${contentPath}/plugins/jsTemp/tmpl.js' type='text/javascript'></script>
<script src="${contentPath}/plugins/color-box/jquery.colorbox-min.js"></script>
<script src="${contentPath}/scripts/jquery.cookie.js"></script>
<script src="${contentPath}/scripts/main.js"></script>