<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>easylearn</title>
	<!-- Bootstrap 3.3.6 -->
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  	<link rel="stylesheet" href="static/bootstrap/css/bootstrap.min.css">
  	<!-- jQuery 2.2.3 -->
	  <script src="static/plugins/jQuery/jquery-2.2.3.min.js"></script>
	 
	  <script type="text/javascript" src="static/bim/js/jquery.cookie.js"></script>
	  
	  <script type="text/javascript">
		$(document).ready(function () {
		 	if ($.cookie("rmbUser") =="true") {
			 	$("#ck_rmbUser").attr("checked", true);
				$("#account").val($.cookie("username"));
				$("#pass").val($.cookie("password"));
			}
		});
	  </script>
	<style type="text/css">
				*{font-family: Arial;}
				body {
				    margin: 0;
				    padding: 0;
				    min-height: 100vh;
				    background-size: cover;
				    background: url(static/bim/img/login/bg_icon.png) no-repeat; background-position-y: bottom; background-color: #EFEFEF; 
				}
				.container-fluid{
					margin: auto 64px;
				}
				.logo-box{
					margin-top: 64px;
				}
				.logo{
					/* background: url(static/bim/img/login/logo_icon.png) no-repeat; */ 
					height: 50px;
				} 
				input{margin-top: 10px;}
				.login-box{
					/* background: url(static/bim/img/login/erweima_icon.png) no-repeat top right; */
					margin: 50px auto auto auto;
					padding: 40px; 
					padding-bottom: 60px;
					background-color: white; 
					max-width: 400px;
					border-radius: 4px;
					box-shadow: 4px 4px 4px #E3E3E3;
					position: relative;
				}
				
				.login-box-tab{
					background: url(static/bim/img/login/erweima_icon.png) no-repeat top right;
				    position: absolute; z-index:100; 
				    width: 103px; 
				    height: 109px; top: 0px; right: 0px;
				}
				
				.login-box li{
					width: 100%;
					border-bottom: #EEEEEE 2px solid;
					line-height: 40px;
					margin-bottom: 29px;
					text-align: center;
					font-weight: bold;
					font-size: 20px;
				}
				.login-box .tab-active{
					border-bottom-color: #ffb211;
				}
				.btn-login{
					width: 100%;
					max-width: 320px;
					height: 40px;
					color: white;
					font-size: 16px;
					background-color: #306ADE;
					border: 0;
					border-radius: 4px;
					outline: none;
				}
				.btn-login:hover{
					background-color: #103b90;
				}
				.download-box{
					width: 160px;
					margin-top: 50px;
					background-color: #d7d7d7;
					padding: auto 30px;
				}
				.download-box-item{
					margin-top: 40px;
					text-align: center;
				}
				
				.login-er-box{
					/* background: url(static/bim/img/login/zhanghao_icon.png) no-repeat top right; */
					margin: 50px auto auto auto;
					padding: 40px; 
					padding-bottom: 60px;
					background-color: white; 
					max-width: 400px;
					border-radius: 4px;
					box-shadow: 4px 4px 4px #E3E3E3;
					position: relative;
				}
				
				.login-er-box-tab{
				    background: url(static/bim/img/login/zhanghao_icon.png) no-repeat top right;
				    position: absolute; z-index:100; 
				    width: 103px; 
				    height: 109px; top: 0px; right: 0px;
				}
				
				.login-er-box H5{
					width: 100%;
					line-height: 40px;
					margin-bottom: 29px;
					text-align: center;
					font-weight: bold;
					font-size: 20px;
					font-family: "微软雅黑";
				}
				.sms-vcode{
					    border: 1px solid #ccc;
					    border-radius: 4px;
					    height: 100%;
					    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075); 
					    box-shadow: inset 0 1px 1px rgba(0,0,0,.075); 
					    outline: none;
					    text-indent: 20px;
					    width: 160px; margin: 0;
					    background:url(static/bim/img/login/mobile_valid.png) center left no-repeat;
					    background-position-x: 5px; 
				}
				
				#account{background:url(static/bim/img/login/account.png) center left no-repeat;text-indent: 20px; height: 40px;background-position-x: 5px;}
				#mobile{background:url(static/bim/img/login/mobile.png) center left no-repeat;text-indent: 20px; height: 40px;background-position-x: 5px;}
				#pass{background:url(static/bim/img/login/pwd.png) center left no-repeat;text-indent: 21px; height: 40px;background-position-x: 5px;}
				#btn-sendsms{width: 130px; height: 100%; background-color: #FFB211; border: 0; cursor: pointer;border-radius: 4px;outline: none;float: right;}
				#btn-sendsms:hover{background-color: #e9a421;}
				#btnLoginBySms{margin-top: 36px;}
				/* #langTags a{cursor: pointer;} */
				.langBox{font-size:20px; text-align: right;}
				a{color:black; text-decoration: none;}
			</style>
</head>
<body>
	<div class="container-fluid">
			<div class="row logo-box">
				<div class="col-lg-12 logo">
					<div class="col-lg-12 langBox">
						<a href="login.do?lang=zh_CN">简体中文</a> |
						<a href="login.do?lang=en_US">English</a>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-4"></div>
				<div class="col-lg-4 text-center">
						<!-- 账号登录 -->
						<div class="login-box">
							<span class="login-box-tab"></span>
							<ul class="list-unstyled tab-title">
								<li class="pull-left tab-active" style=" width: 50%;"><spring:message code="zhanghaodenglu"/></li>
								<li><spring:message code="duanxindenglu"/></li>
							</ul>
							<div class="tab-content">
								<div class="tab-content-item">
									<form class="form-horizontal text-left">
										<input type="text" class="input-icon form-control" placeholder="<spring:message code='shoujihao'/>" id="account">
										<input type="password" class="input-icon form-control" autocomplete="off" placeholder="<spring:message code='mima'/>" id="pass">
										<div class="checkbox">
										    <label>
										      <input type="checkbox" id="ck_rmbUser"> <spring:message code="jizhudengluzhuangtai"/>
										    </label>
										</div>
									</form>
									<input type="button" id="btnLogin" onclick="login()" value="<spring:message code='denglu'/>" class="btn-login"/>
								</div>
								<div class="tab-content-item" style="display: none;">
									<form class="form-horizontal text-left">
										<input class="form-control" type="text" id="mobile" name="mobile" autocomplete="off" placeholder="<spring:message code='shoujihao'/>" />
										<div class="col-lg-12" style="margin-top: 10px; height: 40px; margin-left: 0; padding: 0px;">
											<div class="col-lg-7 col-md-7 col-sm-7 col-xs-7" style="padding-left: 0px;height: 40px;">
												<input class="sms-vcode form-control" autocomplete="off" type="text" id="txtSms" placeholder="<spring:message code='duanxinyanzhengma'/>" />		
											</div>
											<div class="col-lg-5 col-md-5 col-sm-5 pull-right" style="height: 40px;padding: 0px;">
												<button type="button" id="btn-sendsms"><spring:message code='huoquduanxinyanzhengma'/></button>
											</div>
										</div>
										<!-- <input type="button" style="width: 120px;" id="btn-sendsms" value="发送短信验证码"/>
										<input class="sms-vcode" type="text" id="txtSms" placeholder="短信验证码" /> -->
									</form>
									<input type="button" id="btnLoginBySms" value="<spring:message code='denglu'/>" class="btn-login"/>
								</div>
							</div>
						</div>
						<!-- 二维码登录 -->
						<div class="login-er-box" style="display:none;">
							<span class="login-er-box-tab"></span>
							<h5><spring:message code='erweimadenglu'/></h5>
							<img id="shareImg" style="margin: auto auto 20px auto;width:200px;height:200px">
							<!-- <p>请打开手机客户端BIM管控系统</p>
							<p>扫描二维码登录使用</p> -->
							<p><spring:message code='qingdakaishoujikehuduansaomiaodenglu'/></p>
						</div>
				</div>
				<div class="col-lg-4">
					<div class="download-box pull-right">
						<div class="download-box-item">
				      		<!-- <img src="static/ercode/download_qr.png" style="margin-bottom: 10px;width:100px;"> -->
				      		<img src="loginDownloadQRCode.do" style="margin-bottom: 10px;width:100px;">
				      		<p><a id="android-v-url" href="#"><spring:message code='android'/></a></p>
				      	</div>
				      	<div class="download-box-item">
				      		<img src="static/ercode/ios_download_qr.png" style="margin-bottom: 10px;width:100px;">
				      		<p><a id="ios-v-url" href="#"><spring:message code='iphone'/></a></p>
				      	</div>
					</div>
				</div>
			</div>
		</div>

	<!-- 提示框begin -->
	<div class="modal fade" id="myModal" tabindex="-10" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">  
	    <div class="modal-dialog">  
	        <div class="modal-content">  
	            <div class="modal-header">  
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">  
	                    &times;  
	                </button>  
	                <h4 class="modal-title" id="myModalLabel">  
	                    <spring:message code='denglushibai'/> 
	                </h4>  
	            </div>  
	            <div class="modal-body">  
					<spring:message code='yonghuminghemimabupipei'/> 
	            </div>  
	            <div class="modal-footer">  
	                <button type="button" class="btn btn-primary" data-dismiss="modal" ><spring:message code='guanbi'/>
	                </button>  
	                </div>  
	        </div><!-- /.modal-content -->
	    </div><!-- /.modal -->  
	</div> 
	<!-- 提示框end -->
<!-- Bootstrap 3.3.6 -->
<script src="static/bootstrap/js/bootstrap.min.js"></script>

<script src="static/bim/js/login.js"></script>

<script type="text/javascript">
	//手机号不能为空
	var ShouJiHaoBuNengWeiKong='<spring:message code="ShouJiHaoBuNengWeiKong"/>';
	var QingShuRuShouJiHao='<spring:message code="QingShuRuShouJiHao"/>';
	var MiMaBuNengWeiKong='<spring:message code="MiMaBuNengWeiKong"/>';
	var QingShuRuMiMa='<spring:message code="QingShuRuMiMa"/>';
	var DengLuZhong='<spring:message code="DengLuZhong"/>';
	var denglu='<spring:message code="denglu"/>';
	var ShouJiHaoGeShiBuZhengQue='<spring:message code="ShouJiHaoGeShiBuZhengQue"/>';
	var huoquduanxinyanzhengma='<spring:message code="huoquduanxinyanzhengma"/>';
	var DuanXiYanZhengMaBNWK='<spring:message code="DuanXiYanZhengMaBNWK"/>';
	var YanZhengMaGeShiBZQ='<spring:message code="YanZhengMaGeShiBZQ"/>';
	var YanZhengMaYiGuoQiQingCXFS='<spring:message code="YanZhengMaYiGuoQiQingCXFS"/>';
	var logo='<spring:message code="logoIcon"/>';
	$('.logo').css('background','url(static/bim/img/login/'+logo+'.png) no-repeat');
	
	(function(){
		//登录页面禁止后退
		if (window.history && window.history.pushState) {
			$(window).on('popstate', function () {
				window.history.pushState('forward', null, '#');
				window.history.forward(1);
			});
		}
		window.history.pushState('forward', null, '#'); //在IE中必须得有这两行
		window.history.forward(1);
		
		$.post('api/appversion/current.do',function(result){
			if(result && result.success){
				$('#android-v-url').attr('href',result.data.androidAddress);
				$('#ios-v-url').attr('href',result.data.iosAddress);	
			}
		},'json');
		
		function showErrorMsg(msg){
			$('.modal-body').text(msg);
			$('#myModal').modal('show'); 
		}
		
		var $tab_title=$('.tab-title li');
		var $tab_content=$('.tab-content .tab-content-item');
		$tab_title.click(function () {
			$tab_title.removeClass("tab-active");
			$(this).addClass("tab-active");
			$tab_content.hide();
			$tab_content.eq($(this).index()).show();
		});
		
		$('#btn-sendsms').click(function() {
			var mobile = $.trim($('#mobile').val());
			if(mobile=='' || mobile.length<=0){
				showErrorMsg(ShouJiHaoBuNengWeiKong);
				return;
			}
			if(!/^1[3|4|5|7|8][9][0-9]\d{8}$/.test(mobile)){
				showErrorMsg(ShouJiHaoGeShiBuZhengQue);
				return;
			}
			$.ajax({
				type: "post",
				url: baseUrl + "/api/user/loginSms.do",
				data: {"mobile": mobile},
				dataType: 'json',
				success: function(result) {
					if(result.success) {
						var t = 59;
						var $btnSendSMS=$('#btn-sendsms');
						$btnSendSMS.attr("disabled", true).css({"cursor":"no-drop","color":"#de3030","background-color":"#cccccc"});
						$btnSendSMS.text("60 s");
						var timerSms = window.setInterval(function() {
							if(t > 1) {
								$btnSendSMS.text(t-- + " s");
							} else {
								$btnSendSMS.attr("disabled", false).css({"cursor":"pointer","color":"black","background-color":"#FFB211"}).text(huoquduanxinyanzhengma);//.css("cursor", "pointer")
								if(timerSms) window.clearInterval(timerSms);
							}
						}, 1000);
					}else{
						showErrorMsg(result.msg);
					}
				}
			});
		});
		
		$('#btnLoginBySms').click(function() {
			var mobile = $.trim($('#mobile').val());
			var txtSms = $.trim($('#txtSms').val());
			if(mobile=='' || mobile.length<=0){
				showErrorMsg(ShouJiHaoBuNengWeiKong+"!");
				return;
			}
			if(!/^1[3|4|5|7|8][0-9]\d{8}$/.test(mobile)){
				showErrorMsg(ShouJiHaoGeShiBuZhengQue+"!");
				return;
			}
			if(txtSms=='' || txtSms.length<=0){
				showErrorMsg(DuanXiYanZhengMaBNWK+"!");
				return;
			}
			if(!/\d{6}/.test(txtSms)){
				showErrorMsg(YanZhengMaGeShiBZQ+"!");
				return;
			}
			if($('#btn-sendsms').val()==huoquduanxinyanzhengma){
				showErrorMsg(YanZhengMaYiGuoQiQingCXFS+"!");
				return;
			}
			$('#btnLoginBySms').attr("disabled", true).css({"cursor":"no-drop","background-color":"#103b90"}).val(DengLuZhong);
			$.ajax({
				type: "post",
				url: baseUrl + "/doLoginByMobile.do",
				data: {"mobile": mobile,"sms":txtSms},
				dataType: 'json',
				success: function(result) {
					if(result.success) {
						location.href="home.do";
					}else{
						showErrorMsg(result.msg);
						$('#btnLoginBySms').attr("disabled", false).css({"cursor":"pointer","background-color":"#306ADE"}).val(denglu);
					}
				},
				error:function(){
					$('#btnLoginBySms').attr("disabled", false).css({"cursor":"pointer","background-color":"#306ADE"}).val(denglu);
				}
			});
		});
	})();

	/* 切换到二维码登录 */
	$(".login-box-tab").click(function() {
		$('.login-box').hide();
		$('.login-er-box').show();

		initERTimer();
		is_scan = false;
	});

	/* 切换到账号登录 */
	$(".login-er-box-tab").click(function() {
		$('.login-er-box').hide();
		$('.login-box').show();

		clearERTimer();
	});

	var baseUrl = "${pageContext.request.contextPath}";
	function gen() {
		token = generateUUID();
		$("#shareImg").attr("src",
				baseUrl + "/loginQRCode.do?width=200&height=200&uuid=" + token);
	}
	var is_scan = false;
	function checkLogin() {
		$.ajax({
			type : "post",
			url : baseUrl + "/loginByQRCode.do",
			data : {
				"uuid" : token
			},
			dataType : 'json',
			success : function(result) {
				//var json = JSON.parse(result);
				if (!result.success) {
					//setTimeout(checkLogin(),1000*1);		
				} else {
					//如果扫描成功，则显示待确认图片
					if (result.data == 0) {
						clearERTimer();
						window.location.href = "home.do";
					} else if (!is_scan) {
						if (timerGen) {
							window.clearInterval(timerGen);
						}
						$('#shareImg').attr("src","static/bim/img/login/<spring:message code='scanSuccessIcon'/>.png");
						is_scan = true;
					}
					//window.clearInterval(timerGen);
					//window.clearInterval(timerCheckLogin);
				}
			}
		});
	}
	var timerGen;
	var timerCheckLogin;
	$(function() {
		/* gen();
		timerGen=window.setInterval('gen()',1000*60*3);
		//setTimeout(checkLogin(),1000*3);
		timerCheckLogin=window.setInterval('checkLogin()',1000*3); */
	});

	function initERTimer() {
		gen();

		timerGen = window.setInterval('gen()', 1000 * 60 * 3);
		timerCheckLogin = window.setInterval('checkLogin()', 1000 * 3);
		/* if(!timerGen){
			timerGen=window.setInterval('gen()',1000*60*3);
		}
		if(!timerCheckLogin){
			timerCheckLogin=window.setInterval('checkLogin()',1000*3);
		} */
	}

	function clearERTimer() {
		if (timerGen) {
			window.clearInterval(timerGen);
		}
		if (timerCheckLogin) {
			window.clearInterval(timerCheckLogin);
		}
	}

	function generateUUID() {
		var d = new Date().getTime();
		var uuid = 'xxxxxxxxxxxx8xxxyxxxxxxxxxxxxxxx'.replace(/[xy]/g,
				function(c) {
					var r = (d + Math.random() * 16) % 16 | 0;
					d = Math.floor(d / 16);
					return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
				});
		return "BIM_" + uuid;
	};

	var token = "";
</script>
</body>
</html>