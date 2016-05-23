<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contentPath" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>一路贷</title>

		<meta name="description" content="User login page" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<script type="text/javascript">
            var contentPath = '${contentPath}';
        </script>
		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${contentPath }/assets/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${contentPath }/assets/css/font-awesome.min.css" />

		<!-- text fonts -->
		<link rel="stylesheet" href="${contentPath }/assets/css/ace-fonts.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="${contentPath }/assets/css/ace.min.css" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="${contentPath }/assets/css/ace-part2.min.css" />
		<![endif]-->
		<link rel="stylesheet" href="${contentPath }/assets/css/ace-rtl.min.css" />

		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${contentPath }/assets/css/ace-ie.min.css" />
		<![endif]-->

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <link rel="stylesheet" href="${contentPath }/styles/css/login.css" />
        <!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${contentPath }/assets/js/jquery.min.js'>"+"<"+"/script>");
		</script>
         
		<!-- <![endif]-->
		<!--[if IE]>
		<script type="text/javascript">
		 window.jQuery || document.write("<script src='${contentPath }/assets/js/jquery1x.min.js'>"+"<"+"/script>");
		</script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${contentPath }/assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>
        
        
		<!--[if lt IE 9]>
		<script src="${contentPath }/assets/js/html5shiv.js"></script>
		<script src="${contentPath }/assets/js/respond.min.js"></script>
		<![endif]-->
		
		<script src="${contentPath }/assets/js/jquery.validate.min.js"></script>
	</head>

	<body class="login-layout" style="background:#ffffff">
		<div class="main-container">
			<div class="main-content">
				<div class="row">
					<div class="col-sm-10 col-sm-offset-1" style="margin-top: 100px;">
						<div class="login-container">
							<div class="center">
								<h1>
                                    <img src="${contentPath }/img/logo.png" onerror="${contentPath }/img/logo.png" style="width:300px;height: 100px;margin-top: -4px;">
                                </h1>
							</div>

							<div class="space-6"></div>

							<div class="position-relative">
								<div id="login-box" class="login-box visible widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header blue lighter bigger">
												账户密码登录
											</h4>

											<div class="space-6"></div>

											<form method="post" name="form_login" id="form_login" action="${contentPath }/login">
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" name="userName" id="userName" class="form-control" placeholder="用户名" />
															<i class="ace-icon fa fa-user"></i>
														</span>
													</label>

													<label class="block clearfix" style="margin-bottom: -2px;">
														<span class="block input-icon input-icon-right">
															<span>
															<input type="password" name="password" id="password"  class="form-control" placeholder="密码" />
															</span>
															<i class="ace-icon fa fa-lock"></i>
														</span>
													</label>

													<div class="clearfix" style="margin-top: 15px;">
                                                        <button type="button" id="btn_do_login" class="col-md-12 pull-right btn btn-sm btn-primary">
                                                            <i class="ace-icon fa fa-key"></i>
                                                            <span class="bigger-110">登录</span>
                                                        </button>
                                                        <!-- <label class="rememberMe" style="display: none">
                                                            <input type="checkbox" class="ace" />
                                                            <span class="lbl"> Remember Me</span>
                                                        </label>&nbsp;&nbsp;&nbsp;
                                                         <label class="rememberMe" style="float: right;">
                                                            <a  target="_blank" href="#"><span class="lbl">Forgot Password?</span></a>
                                                        </label> -->

													</div>

												</fieldset>
											</form>

											<div class="space-6"></div>

										</div><!-- /.widget-main -->

									</div><!-- /.widget-body -->
								</div><!-- /.login-box -->

								<div id="forgot-box" class="forgot-box widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header red lighter bigger">
												<i class="ace-icon fa fa-key"></i>
												Retrieve Password
											</h4>

											<div class="space-6"></div>
											<p>
												Enter your email and to receive instructions
											</p>

											<form>
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="email" class="form-control" placeholder="Email" />
															<i class="ace-icon fa fa-envelope"></i>
														</span>
													</label>

													<div class="clearfix">
														<button type="button" class="width-35 pull-right btn btn-sm btn-danger">
															<i class="ace-icon fa fa-lightbulb-o"></i>
															<span class="bigger-110">Send Me!</span>
														</button>
													</div>
												</fieldset>
											</form>
										</div>/.widget-main

										<div class="toolbar center">
											<a href="#" data-target="#login-box" class="back-to-login-link">
												Back to login
												<i class="ace-icon fa fa-arrow-right"></i>
											</a>
										</div>
									</div><!-- /.widget-body -->
								</div><!-- /.forgot-box -->

								 <div id="signup-box" class="signup-box widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header green lighter bigger">
												<i class="ace-icon fa fa-users blue"></i>
												New User Registration
											</h4>

											<div class="space-6"></div>
											<p> Enter your details to begin: </p>

											<form>
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="email" class="form-control" placeholder="Email" />
															<i class="ace-icon fa fa-envelope"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" class="form-control" placeholder="Username" />
															<i class="ace-icon fa fa-user"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" class="form-control" placeholder="Password" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" class="form-control" placeholder="Repeat password" />
															<i class="ace-icon fa fa-retweet"></i>
														</span>
													</label>

													<label class="block">
														<input type="checkbox" class="ace" />
														<span class="lbl">
															I accept the
															<a href="#">User Agreement</a>
														</span>
													</label>

													<div class="space-24"></div>

													<div class="clearfix">
														<button type="reset" class="width-30 pull-left btn btn-sm">
															<i class="ace-icon fa fa-refresh"></i>
															<span class="bigger-110">Reset</span>
														</button>

														<button type="button" class="width-65 pull-right btn btn-sm btn-success">
															<span class="bigger-110">Register</span>

															<i class="ace-icon fa fa-arrow-right icon-on-right"></i>
														</button>
													</div>
												</fieldset>
											</form>
										</div>

										<div class="toolbar center">
											<a href="#" data-target="#login-box" class="back-to-login-link">
												<i class="ace-icon fa fa-arrow-left"></i>
												Back to login
											</a>
										</div>
									</div><!-- /.widget-body-->
								</div><!-- /.signup-box-->
							</div><!-- /.position-relative -->
						</div>
					</div><!-- /.col -->
				</div><!-- /.row -->
			</div><!-- /.main-content -->
		</div><!-- /.main-container -->
	</body>
</html>


<input type="hidden" id="type" name="type" value="0" />
<!-- 用户类型：3：代表是企业用户登录  0:代表后台用户登陆-->
<script src="${contentPath}/js/main/login.js"></script>
