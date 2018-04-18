/**
 * Created by tianzhen on 2017/8/15.
 */

$(function () {
    //固定容器高度
    $(".projectBody").css({
        height:$(document).height() - 52
    });
    //请求接口数据
    $.ajax({
        url: basePath + '/api/project/getList.do',
        data: {'accessToken':''},
        type: "POST",
        dataType: 'json',
        success: function(result){
            var proListArray = result['data'];
            //动态创建项目列表
            creatProjectList(proListArray);
        }
    });
    //根据项目数组长度动态创建li列表
    function creatProjectList(arr) {
        var list = "<ul>";
        $.each(arr,function (i,obj) {
            list += '<li id=' + obj["id"] +' class="projectLi" style="background-image:url('+ backgroundImgUrl(obj["imgUrl"]) +')" server-url="'+obj.serverUrl+'"><span>'+ obj['proName'] + '</span></li>';
        });
        list = list+"</ul>";
        $(".projectList").append(list);
    }
    //判断项目数组中的imgUrl 字段是否为空  并返回背景图片url
    function backgroundImgUrl(imgUrl) {
        if (imgUrl == '') {
            return basePath + "/webhtml/css/icon/zanwutupian_bg.png";
        } else {
            return imgUrl;
        }
    }

    //添加点击监听时间  跳转页面这modelTree
    window.addEventListener("click",function (event) {
        var target = event.target || event.srcElement;
        if (target.nodeName == 'LI') {
        	if(!token){
        		window.location.href="login.do";
        	}
        	window.location.href = target.getAttribute("server-url")+"/web/modeltree.do?lang="+lang+"&proid="+ target.id + "&proname=" +encodeURI($(target).find("span").html()+"&token="+token);
            /*window.location.href =  basePath + "/web/modeltree.do?proid="+ target.id + "&proname=" +encodeURI($(target).find("span").html());*/
        }
    });
    //用户名显示
    $("#userName").html(user);
    //headPortrait  头像按钮点击弹窗
    $(".headPortrait").click(function () {
        $(".msgbox").toggle("normal")
    });
    //用户按钮点击跳入后台管理系统
    $("#backstage").on("click",function () {
    	window.open(basePath + managerUrl);
    	//window.open(basePath + (isProdataManager?"/prolist.do":"/home.do"));
    });
    //修改密码弹框
    $("#updatePwd,#userPwdClose,#btn_cancel_pwd").on("click",function () {
        $("#originalPwd").val('');
        $("#newPwd").val('');
        $("#confirmPwd").val('');
        $("#userPwdWaring").css('display','none');
        ConfirmationShadeSwitch("pwdShade");
        $(".userPwd").toggle("normal");
    });
    //修改密码事件
    $("#btn_save_pwd_index").click(function () {
        var originalPwd = $("#originalPwd").val();
        if(originalPwd==''){return userPwdWaring(YuanMiMaBuNengWeiKong+"！")};
        var newPwd = $("#newPwd").val();
        if(newPwd==''){return userPwdWaring(XinMiMaBuNengWeiKong+"！")};
        var confirmPwd = $("#confirmPwd").val();
        if(confirmPwd==''){return userPwdWaring(QueRenMiMaBuNengWeiKong+"！")};
        $.ajax({
            type: 'POST',
            url: basePath +'/user/modifyPassword.do',
            data : {
                'newPwd':newPwd,
                'confirmPwd':confirmPwd,
                'originalPwd':originalPwd
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus);
            },
            success: function (data) {
                if(JSON.parse(data).req){
                    ConfirmationShadeSwitch("pwdShade");
                    $(".userPwd").toggle("normal");
                    userPwdWaring(XiuGaiMiMaChengGong+"！");
                }else{
                    userPwdWaring(JSON.parse(data).msg);
                }
            }
        });
    });
    //修改密码提示警告
    function userPwdWaring(mes) {
        $("#userPwdWaring").html(mes).fadeIn(1000,function () {
        });
    }
    //修改密码弹窗遮罩层开关
    function ConfirmationShadeSwitch(id) {
        if ($("#"+id).css("display") == "none") {
            $("#ConfirmationShade").css({
                "display":"block"
            });
        } else {
            $("#ConfirmationShade").css({
                "display":"none"
            });
        }
    }
    //退出账号
    $("#quit,#viewpointClose,#exitNo").click(function () {
        $("#viewpointShade").toggle("normal");
    });
    $("#exitIs").click(function () {
        $.post(basePath + '/doLogout.do');
        window.location.href =  basePath + "/login.do";
    });

    //body绑定点击事件，拿到点击元素（window.event.target）
    $("body").on("click", function (event) {
        var winEv = event.target.className;
        //点击children太多，拿到他们的父元素（id或class）
        var sonWinEv = event.target.offsetParent?event.target.offsetParent.className:null;
        var sonWinEvId = event.target.offsetParent?event.target.offsetParent.id:null;
        //判断点击元素不是用户信息弹窗，修改密码弹窗，退出账号弹窗
        if(winEv != "headPortrait" && winEv != "msgbox" && winEv != "viewpoint" && winEv != "userPwd" && sonWinEv != "msgbox" && sonWinEv != "viewpoint" && sonWinEv != "viewpointDiv"&& sonWinEvId != "userPassWordForm" && sonWinEv != "userPwd" && sonWinEv != "userPwdDiv" && sonWinEv != "pwdShade"){
            //隐藏用户信息弹窗
            $(".msgbox").css("display", "none");
        }
    });
});