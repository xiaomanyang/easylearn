<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
        <meta charset="UTF-8">
        <title>BIM管控系统</title>
        <link rel="stylesheet" href="${basePath}/webhtml/css/projectList.css">
        <script src="${basePath}/static/plugins/jQuery/jquery-2.2.3.min.js"></script>
        <script src="${basePath}/webhtml/js/mouseWheelForbid.js"></script>
        <style type="text/css">
			.model-panel{width: 360px; background-color: #b4cefb; 
			border-radius: 5px; padding-bottom: 30px; 
			position: absolute;
			left:40%;
			top:30%;box-shadow: 1px 1px 5px #8C8C8C;}
			.model-panel .model-header{width: 100%; height: 40px; text-align: center; line-height: 40px;}
			.model-panel .model-content{background-color: white; margin:auto 10px; text-align: center; border-radius: 5px;}
			/* .model-panel .model-footer{height: 40px;} */
			.form-group{margin-top: 10px;}	
			label{font-size: 16px; font-family: "微软雅黑";}
		</style>
</head>
<body>
<div class="header">
        <span><spring:message code="logo"/></span>
        <div class="headPortrait"></div>
        <div class="msgbox">
                <div>${cUser.surNames}${cUser.realName} &nbsp;(<i id="userName"></i>)</div>
                <div id="backstage"><spring:message code="guankongxitong"/></div>
                <div id="updatePwd"><spring:message code="xiugaimima"/></div>
                <div id="quit"><spring:message code="tuichu"/></div>
        </div>
</div>
<div class="projectBody">
        <div class="projectHeader">
                <span></span><spring:message  code="xiangmuliebiao"/>
        </div>
        <div class="projectList">
                <!--<ul>-->
                <!--<li class="projectLi"><span>广州万达茂滑雪乐园项目</span></li>-->
                <!--</ul>-->
        </div>
</div>
<!--保存视点弹窗-->
<div id="viewpointShade" class="viewpoint">
        <div class="viewpointHeader" >
                <span><spring:message code="tuichu"/></span>
                <span id="viewpointClose"></span>
        </div>
        <div class="viewpointDiv">
			<spring:message code="shifoutuichuxitong"/>
            <span id="exitIs"><spring:message code="shi"/></span>
            <span id="exitNo"><spring:message code="fou"/></span>
        </div>
</div>
<!--修改密码-->
<div id="pwdShade" class="userPwd">
    <div class="userPwdHeader">
        <span><spring:message code="xiugaimima"/></span>
        <span id="userPwdClose"></span>
    </div>
    <div class="userPwdDiv">
        <form action="" method="post" enctype="" autocomplete="on" id="userPassWordForm">
        	
            <label for="user"><spring:message code="yuanshimima"/></label>
            <input type="password" name="originalPwd" id="originalPwd" placeholder="<spring:message code="QinShuRuYSMM"/>">
           
            <label for="user" style="margin-top: 10px;"><spring:message code="xinmima"/></label>
            <input type="password" name="newPwd" id="newPwd" placeholder="<spring:message code="QingShuRuXinMiMa"/>">
            
            <label for="user"><spring:message code="querenmima"/></label>
            <input type="password" name="confirmPwd" id="confirmPwd" placeholder="<spring:message code="QingShuRuQueRenMiMa"/>">
            <span id="userPwdWaring" style="position: absolute;top: 134px;left: 107px;font-size: 14px;color: red;"></span>
            <input type="button" name="" id="btn_save_pwd_index" value="<spring:message code="baocun"/>">
            <input type="button" name="" id="btn_cancel_pwd" value="<spring:message code="quxiao"/>">
        </form>
    </div>
</div>

		<!--设置密码-->
		<div class="maskLayer" style="display:none; width:100%;height:100%; position: absolute; left: 0; top: 0; z-index:100;opacity: 0.5; background-color: darkgray;"></div>
		<div class="model-panel" style="display:none; z-index: 101;">
		    <div class="model-header">
		        <label><spring:message code="SheZhiMiMa"/></label>
		    </div>
		    <div class="model-content" style="padding-top: 30px; text-align:left;">
		        <form action="" method="post" enctype="" autocomplete="on">
		        	<div class="form-group">
		        		<label for="user" style="width: 100px; display: inline-block; text-align: right;"><spring:message code="xinmima"/></label>
		            	<input type="password" name="newPwd" id="setNewPwd" placeholder="<spring:message code="QingShuRuXinMiMa"/>">	
		        	</div>
		            <div class="form-group">
		            	<label for="user" style="width: 100px; display: inline-block; text-align: right;"><spring:message code="querenmima"/></label>
			            <input type="password" name="confirmPwd" id="setConfirmPwd" placeholder="<spring:message code="QingShuRuQueRenMiMa"/>">
		            </div>
		            <label id="error-message-panel" style="color:red; font-size:12px; margin-left:106px; "></label>
		        </form>
		        <input style="width: 150px; margin:20px auto 10px 100px; height: 30px;" type="button" id="btnSavePwd" value="<spring:message code="baocun"/>">
		    </div>
		</div>
</body>
<script>
	var XinMiMaBuNengWeiKong='<spring:message code="XinMiMaBuNengWeiKong"/>';
	var QueRenMiMaBuNengWeiKong='<spring:message code="QueRenMiMaBuNengWeiKong"/>';
	var LiangCiShuRuMiMaBuYiZhi='<spring:message code="LiangCiShuRuMiMaBuYiZhi"/>';
	var YuanMiMaBuNengWeiKong='<spring:message code="YuanMiMaBuNengWeiKong"/>';
	var XiuGaiMiMaChengGong='<spring:message code="XiuGaiMiMaChengGong"/>';

    var basePath = "${basePath}";
    var user = "${cUser.account}";
    var roles="${roles}";
    var isManager=${isManager};
    var managerUrl="${managerUrl}";
    var token="${token}";
    var lang="${lang}";
    var isFirstLogin=${isFirstLogin};
    if(!isManager){
    	$("#backstage").remove();
    }
    console.log(roles);
    //首次登录强制改密码
    if(isFirstLogin){
    	$(".maskLayer").show();
    	$(".model-panel").show();
    }
	
	$("#btnSavePwd").click(function(){
		var pwd=$('#setNewPwd').val();
		var confirmPwd=$('#setConfirmPwd').val();
		if(!pwd || pwd.length<=0){
			$("#error-message-panel").text(XinMiMaBuNengWeiKong+"!");
			return;
		}
		if(!confirmPwd || confirmPwd.length<=0){
			$("#error-message-panel").text(QueRenMiMaBuNengWeiKong+"!");
			return;
		}
		if(pwd!=confirmPwd){
			$("#error-message-panel").text(LiangCiShuRuMiMaBuYiZhi+"!");
			return;
		}
		$.ajax({
            type: 'POST',
            url: basePath +'/api/user/setPwd.do',
            data : {
                'newPwd':pwd,
                'confirmPwd':confirmPwd
            },
            dataType:"json",
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus);
            },
            success: function (data) {
                if(data.success){
                	$(".maskLayer").hide();
                	$(".model-panel").hide();
                }else{
                	$("#error-message-panel").text(data.msg);
                }
            }
        });
	});
</script>
<script src="${basePath}/static/bim/js/jquery.cookie.js"></script>
<script src="${basePath}/static/bim/js/jquery.extend.js"></script>
<script src="${basePath}/webhtml/js/projectList.js"></script>
</html>