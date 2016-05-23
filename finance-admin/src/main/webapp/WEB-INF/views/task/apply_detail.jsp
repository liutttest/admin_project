<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="dict" uri="http://www.evan.jaron.com/tags/dict" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- /section:basics/sidebar -->
<div class="main-content">
  <!-- #section:basics/content.breadcrumbs -->
  <div class="breadcrumbs" id="breadcrumbs">
    <script type="text/javascript">
      try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
    </script>

    <ul class="breadcrumb">
      <!-- <li>
        <i class="ace-icon fa fa-check-square-o home-icon"></i>
        待办任务
      </li> -->
      <li class="active">查看资料</li>
    </ul><!-- /.breadcrumb -->
  </div>

  <!-- /section:basics/content.breadcrumbs -->
  <div class="page-content">
    <div class="row">
      <div class="col-xs-12 tabbable">
        <ul class="nav nav-tabs">
          <li role="presentation" class="active"><a data-toggle="tab" href="#accountInfo">账户信息</a></li>
          <li role="presentation"><a data-toggle="tab" href="#taxInfo">税务信息</a></li>
          <li role="presentation"><a data-toggle="tab" href="#companyInfo">企业信息</a></li>
          <li role="presentation"><a data-toggle="tab" href="#legalInfo">法人信息</a></li>
          <li role="presentation"><a data-toggle="tab" href="#contrlInfo">控制人及配偶信息</a></li>
          <li role="presentation"><a data-toggle="tab" href="#buchongInfo">补充材料</a></li>
          <li role="presentation"><a data-toggle="tab" href="#qitaInfo">其他</a></li>
        </ul>

        <!-- <div class="page-header">
            <h1>账户信息</h1>
        </div> -->
        <div class="tab-content">
          <div class="tab-pane fade active in" id="accountInfo">
            <div class="form-horizontal">
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 用户名 </label>
                <div class="col-sm-3">
                  <label class="control-label"> ${fwComAccount.userName} </label>
                </div>
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 真实姓名 </label>
                <div class="col-sm-5">
                  <label class="control-label">${fwComAccount.realName} </label>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 电话 </label>
                <div class="col-sm-3">
                  <label class="control-label">  ${fwComAccount.tel} </label>
                </div>
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 邮箱 </label>
                <div class="col-sm-5">
                  <label class="control-label"> ${fwComAccount.email} </label>
                </div>
              </div>
            </div>
          </div>

          <div class="tab-pane fade" id="taxInfo">
           	<div class="form-horizontal">
	            <div class="form-group">
	                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 税号  </label>
	                <div class="col-sm-3">
	                  <label class="control-label" style="word-break: break-all;text-align:left;">${fwBusinessSxd.taxNum} </label>
	                  <%-- <c:choose>
	                    <c:when test="${taxNum_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="002" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="税号" data-infoKey="002">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </div>
	                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 上一年纳税总额 </label>
	                <div class="col-sm-5">
	                  <label class="control-label"> ${fwBusinessSxd.lastyTaxmoney} 元</label>
	                </div>
	              </div>
	              
	              
	              <div class="form-group">
	                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 税务登记证</label>
	                <div class="col-sm-10">
	                	<c:if test="${fn:length(taxRegistartion)==0}">
	                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
	                    </c:if>
	                    <c:if test="${fn:length(taxRegistartion)>0}">
		                  <ul class="ace-thumbnails clearfix">
		                    <!-- #section:pages/gallery -->
		                    <c:forEach items="${taxRegistartion}" var="taxRegistartion">
		                      <li>
		                        <a href=".downloadImg?id=${taxRegistartion.fileId}" title="税务登记证" data-rel="colorbox" class="cboxElement">
		                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${taxRegistartion.fileId}">
		                          <div class="text">
		                            <div class="inner">税务登记证</div>
		                          </div>
		                        </a>
		                      </li>
		                    </c:forEach>
		                  </ul>
		                  <%-- <c:choose>
		                    <c:when test="${taxRegistartion_ispass==0}">
		                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="03" href="#">已标记为不合格</a></label>
		                    </c:when>
		                    <c:otherwise>
		                      <label class="js-do-nopass control-label" data-infoName="税务登记证" data-infoKey="03">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
		                    </c:otherwise>
		                  </c:choose> --%>
		                </c:if>
	                </div>
	              </div>
	              
	              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 近三年财务报表</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(lastYearStatements)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(lastYearStatements)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <c:forEach items="${lastYearStatements}" var="lastYearStatements">
	                      <li>
	                        <a href=".downloadImg?id=${lastYearStatements.fileId}" title="近三年财务报表" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${lastYearStatements.fileId}">
	                          <div class="text">
	                            <div class="inner">近三年财务报表</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${lastYearStatements_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="06" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="上一年财务报表" data-infoKey="06">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 涉税保密信息查询申请表</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(taxRelatedQuery)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(taxRelatedQuery)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <c:forEach items="${taxRelatedQuery}" var="taxRelatedQuery">
	                      <li>
	                        <a href=".downloadImg?id=${taxRelatedQuery.fileId}" title="涉税保密信息查询申请表" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${taxRelatedQuery.fileId}">
	                          <div class="text">
	                            <div class="inner">涉税保密信息查询申请表</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${taxRelatedQuery_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="01" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="涉税保密信息查询申请表" data-infoKey="01">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 征信查询授权书</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(companyCredit)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(companyCredit)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${companyCredit}" var="companyCredit">
	                      <li>
	                        <a href=".downloadImg?id=${companyCredit.fileId}" title="征信查询授权书" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${companyCredit.fileId}">
	                          <div class="text">
	                            <div class="inner">征信查询授权书</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                 <%--  <c:choose>
	                    <c:when test="${companyCredit_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="05" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="企业征信查询授权书" data-infoKey="05">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>					
              
              
	          </div>
		    </div>

          <!-- <div class="page-header">
              <h1>企业信息</h1>
          </div> -->
          <div class="tab-pane fade" id="companyInfo">
            <div class="form-horizontal">
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 企业名 </label>
                <div class="col-sm-3">
                  <label class="control-label"> ${fwBusinessSxd.comName } </label>
                </div>
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 成立时间 </label>
                <div class="col-sm-5">
                  <label class="control-label"> ${fwBusinessSxd.setUpTime} </label>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 营业执照注册号 </label>
                <div class="col-sm-3">
                  <label class="control-label" style="word-break: break-all;text-align:left;"> ${fwBusinessSxd.licenseNum}</label>
                  <%-- <c:choose>
                    <c:when test="${licenseNum_ispass==0}">
                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="001" href="#">已标记为不合格</a></label>
                    </c:when>
                    <c:otherwise>
                      <label class="js-do-nopass control-label" data-infoName="营业执照注册号" data-infoKey="001">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
                    </c:otherwise>
                  </c:choose> --%>

                </div>

                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;">组织机构代码 </label>
                <div class="col-sm-5">
                  <label class="control-label" style="word-break: break-all;text-align:left;">${fwBusinessSxd.cardCode}  </label>
                  <%-- <c:choose>
                    <c:when test="${catdCode_ispass==0}">
                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="003" href="#">已标记为不合格</a></label>
                    </c:when>
                    <c:otherwise>
                      <label class="js-do-nopass control-label" data-infoName="组织机构代码证代码 " data-infoKey="003">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
                    </c:otherwise>
                  </c:choose> --%>
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;">贷款金额 </label>
                <div class="col-sm-3">
                  <label class="control-label">${fwBusinessSxd.intentMoney} </label>
                </div>
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 营业执照注册地址 </label>
                <div class="col-sm-5">
                  <label class="control-label" style="text-align:left;">${fwBusinessSxd.licenseAddr}</label>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 贷款卡号  </label>
                <div class="col-sm-3">
                  <label class="control-label">${fwBusinessSxd.loanCardCode} </label>
                </div>
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 经营场所 </label>
                <div class="col-sm-5">
                  <label class="control-label"><dict:lookupDictValue type="PREMISES" key="${fwBusinessSxd.premises}" /></label>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 企业营业执照</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(businessLicense)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(businessLicense)>0}">
	                  	<ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    
	                    <c:forEach items="${businessLicense}" var="businessLicense">
	                      <li>
	                        <a href=".downloadImg?id=${businessLicense.fileId}" title="企业营业执照" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${businessLicense.fileId}">
	                          <div class="text">
	                            <div class="inner">企业营业执照</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${businessLicense_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="02" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="营业执照" data-infoKey="02">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
                  </c:if>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 组织机构代码证</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(organiztionCode)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(organiztionCode)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <c:forEach items="${organiztionCode}" var="organiztionCode">
	                      <li>
	                        <a href=".downloadImg?id=${organiztionCode.fileId}" title="组织机构代码证" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${organiztionCode.fileId}">
	                          <div class="text">
	                            <div class="inner">组织机构代码证</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${organiztionCode_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="04" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="组织机构代码证" data-infoKey="04">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
                  	</c:if>
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 纳税查询授权书</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(enterprisePayTaxesThrough)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(enterprisePayTaxesThrough)>0}">
	                  	<ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    
	                    <c:forEach items="${enterprisePayTaxesThrough}" var="item">
	                      <li>
	                        <a href=".downloadImg?id=${item.fileId}" title="纳税查询授权书" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${item.fileId}">
	                          <div class="text">
	                            <div class="inner">纳税查询授权书</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${businessLicense_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="02" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="营业执照" data-infoKey="02">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
                  </c:if>
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 贷款卡扫描件</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(theLoanCardCopy)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(theLoanCardCopy)>0}">
	                  	<ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    
	                    <c:forEach items="${theLoanCardCopy}" var="item">
	                      <li>
	                        <a href=".downloadImg?id=${item.fileId}" title="贷款卡复印件" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${item.fileId}">
	                          <div class="text">
	                            <div class="inner">贷款卡扫描件</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
                  </c:if>
                </div>
              </div>
              
              
            </div>
          </div>

          <!-- <div class="page-header">
              <h1>法人信息</h1>
          </div> -->
          <div class="tab-pane fade" id="legalInfo">
            <div class="form-horizontal">
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 姓名 </label>
                <div class="col-sm-3">
                  <label class="control-label"> ${fwComPerBus.perName} </label>
                </div>
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 电话号码 </label>
                <div class="col-sm-5">
                  <label class="control-label"> ${fwComPerBus.phone} </label>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 证件号码 </label>
                <div class="col-sm-3">
                  <label class="control-label"> ${fwComPerBus.documentNum} </label>
                </div>
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 婚姻状况 </label>
                <div class="col-sm-5">
                  <label class="control-label">
                    <c:if test="${fwComPerBus.maritalState=='2'}">已婚</c:if>
                    <c:if test="${fwComPerBus.maritalState=='0'}">未婚或离异</c:if>
                  </label>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 法人身份证照片</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(ledalCard)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(ledalCard)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${ledalCard}" var="item">
	                      <li>
	                        <a href=".downloadImg?id=${item.fileId}" title="控制人证件照片" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${item.fileId}">
	                          <div class="text">
	                            <div class="inner">法人身份证照片</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${controllerCard_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="08" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="控制人证件照片" data-infoKey="08">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>
              <%-- <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 征信报告查询授权书</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(ledalCredit)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(ledalCredit)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${ledalCredit}" var="ledalCredit">
	                      <li>
	                        <a href=".downloadImg?id=${ledalCredit.fileId}" title="征信报告查询授权书" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${ledalCredit.fileId}">
	                          <div class="text">
	                            <div class="inner">征信报告查询授权书</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <c:choose>
	                    <c:when test="${ledalCredit_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="10" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="法人征信查询授权书" data-infoKey="10">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose>
	                </c:if>
                </div>
              </div> --%>
              
              
              <%-- <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 身份证照片</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(ledalCard)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(ledalCard)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${ledalCard}" var="ledalCard">
	                      <li>
	                        <a href=".downloadImg?id=${ledalCard.fileId}" title="身份证照片" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${ledalCard.fileId}">
	                          <div class="text">
	                            <div class="inner">身份证照片</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <c:choose>
	                    <c:when test="${ledalCard_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="07" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="法人证件照片" data-infoKey="07">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose>
	                </c:if>
                </div>
              </div> --%>
              
              
            </div>
          </div>

          <!-- <div class="page-header">
              <h1>实际控制人及配偶信息</h1>
          </div> -->
          <div class="tab-pane fade" id="contrlInfo">
            <div class="form-horizontal">

              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 姓名 </label>
                <div class="col-sm-3">
                  <label class="control-label"> ${controller.perName} </label>
                </div>
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 证件号码 </label>
                <div class="col-sm-5">
                  <label class="control-label"> ${controller.documentNum} </label>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;">学历情况</label>
                <div class="col-sm-3">
                  <label class="control-label">
                    <dict:lookupDictValue type="EDUCATION" key="${controller.education}" />
                  </label>
                </div>
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;">户籍情况</label>
                <div class="col-sm-5">
                  <label class="control-label">
                    <dict:lookupDictValue type="DOMICILE_PLACE" key="${controller.domicilePlace}" />
                  </label>
                </div>

              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;">净资产情况</label>
                <div class="col-sm-3">
                  <label class="control-label">
                    <dict:lookupDictValue type="HOUSEHOLD_ASSETS" key="${controller.householdAssets}" />
                  </label>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 婚姻状况 </label>
                <div class="col-sm-3">
                  <label class="control-label">
                    <c:if test="${controller.maritalState=='2'}">已婚</c:if>
                    <c:if test="${controller.maritalState=='0'}">未婚或离异</c:if>
                  </label>
                </div>
              </div>
              <c:if test="${controller.maritalState=='2'}">
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 配偶姓名</label>
                <div class="col-sm-3">
                  <label class="control-label">
                    ${controller.spoName}
                  </label>
                </div>
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 配偶证件号码 </label>
                <div class="col-sm-5">
                  <label class="control-label"> ${controller.spoDocumentNum} </label>
                </div>

              </div>
              </c:if>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 征信报告查询授权书</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(controllerCredit)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(controllerCredit)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${controllerCredit}" var="controllerCredit">
	                      <li>
	                        <a href=".downloadImg?id=${controllerCredit.fileId}" title="征信报告查询授权书" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${controllerCredit.fileId}">
	                          <div class="text">
	                            <div class="inner">征信报告查询授权书</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                 <%--  <c:choose>
	                    <c:when test="${controllerCredit_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="11" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="控制人及配偶征信报告查询授权书" data-infoKey="11">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 控制人身份证照片</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(controllerCard)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(controllerCard)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${controllerCard}" var="controllerCard">
	                      <li>
	                        <a href=".downloadImg?id=${controllerCard.fileId}" title="控制人证件照片" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${controllerCard.fileId}">
	                          <div class="text">
	                            <div class="inner">控制人证件照片</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${controllerCard_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="08" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="控制人证件照片" data-infoKey="08">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 控制人配偶身份证照片</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(controllerSpoCard)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(controllerSpoCard)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${controllerSpoCard}" var="controllerSpoCard">
	                      <li>
	                        <a href=".downloadImg?id=${controllerSpoCard.fileId}" title="控制人配偶身份证照片" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${controllerSpoCard.fileId}">
	                          <div class="text">
	                            <div class="inner">控制人配偶身份证照片</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${controllerSpoCard_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="09" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="控制人配偶证件照片" data-infoKey="09">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;">学历证明</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(controllerEducation)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(controllerEducation)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${controllerEducation}" var="item">
	                      <li>
	                        <a href=".downloadImg?id=${item.fileId}" title="控制人学历证明" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${item.fileId}">
	                          <div class="text">
	                            <div class="inner">控制人学历证明</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${controllerEducation_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="30" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="控制人学历证明" data-infoKey="30">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;">户籍证明</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(controllerDomicilePlace)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(controllerDomicilePlace)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${controllerDomicilePlace}" var="item">
	                      <li>
	                        <a href=".downloadImg?id=${item.fileId}" title="控制人户籍证明" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${item.fileId}">
	                          <div class="text">
	                            <div class="inner">控制人户籍证明</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${controllerDomicilePlace_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="31" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="控制人户籍证明" data-infoKey="31">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>
              
              
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;">净资产证明</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(householdAssets)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(householdAssets)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${householdAssets}" var="item">
	                      <li>
	                        <a href=".downloadImg?id=${item.fileId}" title="净资产证明" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${item.fileId}">
	                          <div class="text">
	                            <div class="inner">净资产证明</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                </c:if>
                </div>
              </div>
              
              
            </div>
          </div>

          <!-- <div class="page-header">
              <h1>补充材料</h1>
          </div> -->
          <div class="tab-pane fade" id="buchongInfo">
            <div class="form-horizontal">
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 授信业务申请书</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(creditBusinessApp)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(creditBusinessApp)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    
	                    <c:forEach items="${creditBusinessApp}" var="creditBusinessApp">
	                      <li>
	                        <a href=".downloadImg?id=${creditBusinessApp.fileId}" title="授信业务申请书" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${creditBusinessApp.fileId}">
	                          <div class="text">
	                            <div class="inner">授信业务申请书</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${creditBusinessApp_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="12" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="授信业务申请书" data-infoKey="12">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
                    </c:if>
                </div>
              </div>
              
              
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 公司简介 </label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(companyProfile)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(companyProfile)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${companyProfile}" var="item">
	                      <li>
	                        <a href=".downloadImg?id=${item.fileId}" title="公司简介" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${item.fileId}">
	                          <div class="text">
	                            <div class="inner">公司简介</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${companyProfile_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="13" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="公司简介" data-infoKey="36">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>
              
              
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 股东会或董事会决议</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(directorsBoard)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(directorsBoard)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${directorsBoard}" var="directorsBoard">
	                      <li>
	                        <a href=".downloadImg?id=${directorsBoard.fileId}" title="股东会或董事会决议" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${directorsBoard.fileId}">
	                          <div class="text">
	                            <div class="inner">股东会或董事会决议</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${directorsBoard_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="13" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="股东会或董事会决议" data-infoKey="13">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>
              
              <%-- <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 资质证书或经营许可证</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(specicalIndustryCre)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(specicalIndustryCre)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${specicalIndustryCre}" var="specicalIndustryCre">
	                      <li>
	                        <a href=".downloadImg?id=${specicalIndustryCre.fileId}" title="资质证书或经营许可证" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${specicalIndustryCre.fileId}">
	                          <div class="text">
	                            <div class="inner">资质证书或经营许可证</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  	<c:choose>
	                    <c:when test="${specicalIndustryCre_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="14" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="资质证书或经营许可证" data-infoKey="14">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
                  		</c:choose>
                  	</c:if>
                </div>
              </div> --%>
              
              

              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 公司章程、验资报告</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(capitalVerification)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(capitalVerification)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${capitalVerification}" var="capitalVerification">
	                      <li>
	                        <a href=".downloadImg?id=${capitalVerification.fileId}" title="公司章程、验资报告" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${capitalVerification.fileId}">
	                          <div class="text">
	                            <div class="inner"> 公司章程、验资报告</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${capitalVerification_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="15" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="公司章程、验资报告" data-infoKey="15">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 购销合同</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(purchaseSaleContract)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(purchaseSaleContract)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${purchaseSaleContract}" var="purchaseSaleContract">
	                      <li>
	                        <a href=".downloadImg?id=${purchaseSaleContract.fileId}" title="购销合同" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${purchaseSaleContract.fileId}">
	                          <div class="text">
	                            <div class="inner">购销合同</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                 <%--  <c:choose>
	                    <c:when test="${purchaseSaleContract_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="18" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="购销合同" data-infoKey="18">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>

              <%-- <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 客户清单</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(customerList)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(customerList)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${customerList}" var="customerList">
	                      <li>
	                        <a href=".downloadImg?id=${customerList.fileId}" title="客户清单" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${customerList.fileId}">
	                          <div class="text">
	                            <div class="inner">客户清单</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <c:choose>
	                    <c:when test="${customerList_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="19" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="客户清单" data-infoKey="19">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose>
	                 </c:if>
                </div>
              </div> --%>

              <%-- <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 税务报表</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(lastTaxStatements)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(lastTaxStatements)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${lastTaxStatements}" var="lastTaxStatements">
	                      <li>
	                        <a href=".downloadImg?id=${lastTaxStatements.fileId}" title="税务报表" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${lastTaxStatements.fileId}">
	                          <div class="text">
	                            <div class="inner">税务报表</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <c:choose>
	                    <c:when test="${lastTaxStatements_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="20" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="税务报表" data-infoKey="20">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose>
	                </c:if>
                </div>
              </div> --%>
              
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 个人所得税</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(theIndividualIncomeTax)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(theIndividualIncomeTax)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${theIndividualIncomeTax}" var="item">
	                      <li>
	                        <a href=".downloadImg?id=${item.fileId}" title="个人所得税" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${item.fileId}">
	                          <div class="text">
	                            <div class="inner">个人所得税</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${companyProfile_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="13" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="公司简介" data-infoKey="36">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 国地税纳税证明</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(lastYearThidYearAnnualLandTaxCertificate)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(lastYearThidYearAnnualLandTaxCertificate)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${lastYearThidYearAnnualLandTaxCertificate}" var="item">
	                      <li>
	                        <a href=".downloadImg?id=${item.fileId}" title="国地税纳税证明" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${item.fileId}">
	                          <div class="text">
	                            <div class="inner">国地税纳税证明</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${companyProfile_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="13" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="公司简介" data-infoKey="36">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 银行对账单</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(lastCompanyStatements)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(lastCompanyStatements)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${lastCompanyStatements}" var="lastCompanyStatements">
	                      <li>
	                        <a href=".downloadImg?id=${lastCompanyStatements.fileId}" title="银行对账单" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${lastCompanyStatements.fileId}">
	                          <div class="text">
	                            <div class="inner">银行对账单</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${lastCompanyStatements_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="21" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="银行对账单" data-infoKey="21">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if> 
	                
                </div>
              </div>

              
              <%-- <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 个人银行对账单</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(lastControllerStatements)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(lastControllerStatements)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${lastControllerStatements}" var="lastControllerStatements">
	                      <li>
	                        <a href=".downloadImg?id=${lastControllerStatements.fileId}" title="个人银行对账单" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${lastControllerStatements.fileId}">
	                          <div class="text">
	                            <div class="inner">个人银行对账单</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <c:choose>
	                    <c:when test="${lastControllerStatements_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="22" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="个人银行对账单" data-infoKey="22">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose>
	                </c:if>
                </div>
              </div> --%>

              
              <%-- <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 纳税凭证和申请表</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(lastTaxCerifcate)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(lastTaxCerifcate)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${lastTaxCerifcate}" var="lastTaxCerifcate">
	                      <li>
	                        <a href=".downloadImg?id=${lastTaxCerifcate.fileId}" title="纳税凭证和申请表" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${lastTaxCerifcate.fileId}">
	                          <div class="text">
	                            <div class="inner">纳税凭证和申请表</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <c:choose>
	                    <c:when test="${lastTaxCerifcate_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="23" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="纳税凭证和申请表" data-infoKey="23">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose>
	                </c:if>
                </div>
              </div> --%>

              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 缴费凭证</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(lastPaymentVoucher)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(lastPaymentVoucher)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${lastPaymentVoucher}" var="lastPaymentVoucher">
	                      <li>
	                        <a href=".downloadImg?id=${lastPaymentVoucher.fileId}" title="缴费凭证" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${lastPaymentVoucher.fileId}">
	                          <div class="text">
	                            <div class="inner">缴费凭证</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${lastPaymentVoucher_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="24" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="缴费凭证" data-infoKey="24">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>

              <%-- <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 资信和技术产品证书</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(creditTechnology)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(creditTechnology)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${creditTechnology}" var="creditTechnology">
	                      <li>
	                        <a href=".downloadImg?id=${creditTechnology.fileId}" title="资信和技术产品证书" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${creditTechnology.fileId}">
	                          <div class="text">
	                            <div class="inner">资信和技术产品证书</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <c:choose>
	                    <c:when test="${creditTechnology_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="25" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="资信和技术产品证书" data-infoKey="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose>
	                </c:if>
                </div>
              </div> --%>

              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 场地证明</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(propertyRightCard)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(propertyRightCard)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${propertyRightCard}" var="propertyRightCard">
	                      <li>
	                        <a href=".downloadImg?id=${propertyRightCard.fileId}" title="场地证明" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${propertyRightCard.fileId}">
	                          <div class="text">
	                            <div class="inner">场地证明</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${propertyRightCard_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="26" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="场地证明" data-infoKey="26">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 资产负债申请表</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(controllerAssetsLiaApply)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(controllerAssetsLiaApply)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${controllerAssetsLiaApply}" var="controllerAssetsLiaApply">
	                      <li>
	                        <a href=".downloadImg?id=${controllerAssetsLiaApply.fileId}" title="资产负债申请表" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${controllerAssetsLiaApply.fileId}">
	                          <div class="text">
	                            <div class="inner">资产负债申请表</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${controllerAssetsLiaApply_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="29" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="资产负债申请表" data-infoKey="29">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 婚姻状况证明</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(marriageLicense)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(marriageLicense)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${marriageLicense}" var="marriageLicense">
	                      <li>
	                        <a href=".downloadImg?id=${marriageLicense.fileId}" title="婚姻状况证明" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${marriageLicense.fileId}">
	                          <div class="text">
	                            <div class="inner">婚姻状况证明</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${marriageLicense_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="27" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="婚姻状况证明" data-infoKey="27">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 实际控制人及配偶的担保承诺书</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(controllerSpouseGuarantee)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(controllerSpouseGuarantee)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${controllerSpouseGuarantee}" var="controllerSpouseGuarantee">
	                      <li>
	                        <a href=".downloadImg?id=${controllerSpouseGuarantee.fileId}" title="实际控制人及配偶的担保承诺书" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${controllerSpouseGuarantee.fileId}">
	                          <div class="text">
	                            <div class="inner">实际控制人及配偶的担保承诺书</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${controllerSpouseGuarantee_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="28" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="配偶的担保承诺书" data-infoKey="28">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 实际控制人及配偶的担保确认书</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(confirmation)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(confirmation)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${confirmation}" var="item">
	                      <li>
	                        <a href=".downloadImg?id=${item.fileId}" title="实际控制人及配偶的担保确认书" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${item.fileId}">
	                          <div class="text">
	                            <div class="inner">实际控制人及配偶的担保确认书</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${companyProfile_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="13" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="公司简介" data-infoKey="36">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;"> 控制人及家庭房产证明</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(housePropertyCard)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(housePropertyCard)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${housePropertyCard}" var="item">
	                      <li>
	                        <a href=".downloadImg?id=${item.fileId}" title="控制人及家庭房产证明" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${item.fileId}">
	                          <div class="text">
	                            <div class="inner">控制人及家庭房产证明</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                  <%-- <c:choose>
	                    <c:when test="${companyProfile_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="13" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="公司简介" data-infoKey="36">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right" style="padding-top:7px;">企业纳税信用等级证明</label>
                <div class="col-sm-10">
                	<c:if test="${fn:length(enterpriseCreditRating)==0}">
                    	<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无</label>
                    </c:if>
                    <c:if test="${fn:length(enterpriseCreditRating)>0}">
	                  <ul class="ace-thumbnails clearfix">
	                    <!-- #section:pages/gallery -->
	                    <!-- #section:pages/gallery.caption -->
	                    <c:forEach items="${enterpriseCreditRating}" var="item">
	                      <li>
	                        <a href=".downloadImg?id=${item.fileId}" title="企业纳税信用等级证明" data-rel="colorbox" class="cboxElement">
	                          <img width="100" height="100" alt="100x100" src=".downloadImg?id=${item.fileId}">
	                          <div class="text">
	                            <div class="inner">企业纳税信用等级证明</div>
	                          </div>
	                        </a>
	                      </li>
	                    </c:forEach>
	                  </ul>
	                 <%--  <c:choose>
	                    <c:when test="${enterpriseCreditRating_ispass==0}">
	                      <label class="control-label red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="js-do-pass red" data-infoKey="34" href="#">已标记为不合格</a></label>
	                    </c:when>
	                    <c:otherwise>
	                      <label class="js-do-nopass control-label" data-infoName="企业纳税信用等级证明" data-infoKey="34">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">标记为不合格</a></label>
	                    </c:otherwise>
	                  </c:choose> --%>
	                </c:if>
                </div>
              </div>


            </div>
          </div>
         
          <div class="tab-pane fade" id="qitaInfo">
              <h3 class="text-center">敬请期待</h3>
          </div>
      </div>
      </div>
    </div>
  </div><!-- /.page-content -->
</div><!-- /.main-content -->


<input type="hidden" id="businessId" value="${fwBusinessSxd.bsId}"/>

<!-- page specific plugin scripts -->

<script src="${contentPath}/plugins/color-box/jquery.colorbox-min.js"></script>


<script type="text/javascript">
  jQuery(function($) {
    var $overflow = '';
    var colorbox_params = {
      rel: 'colorbox',
      reposition:true,
      scalePhotos:true,
      photo: true,
      scrolling:false,
      previous:'<i class="ace-icon fa fa-arrow-left"></i>',
      next:'<i class="ace-icon fa fa-arrow-right"></i>',
      close:'&times;',
      current:'{current} of {total}',
      maxWidth:'100%',
      maxHeight:'100%',
      photo: true,
      onOpen:function(){
        $overflow = document.body.style.overflow;
        document.body.style.overflow = 'hidden';
      },
      onClosed:function(){
        document.body.style.overflow = $overflow;
      },
      onComplete:function(){
        $.colorbox.resize();
      }
    };

    $('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);
    $("#cboxLoadingGraphic").html("<i class='ace-icon fa fa-spinner orange'></i>");//let's add a custom loading icon、
    
    
    
    
    
  });
</script>
<script src="${contentPath}/scripts/jquery.cookie.js"></script>

<script src="${contentPath}/js/task/apply_detail.js"></script>
