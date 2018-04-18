<%@ page language="java" import="com.bim.util.Constant" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>BIM管理系统-后台项目列表</title>
	<!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.6 -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugins/Font-Awesome-master/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugins/ionicons-2.0.1/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/dist/css/AdminLTE.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/dist/css/skins/_all-skins.min.css">
  <style>
  	.body{background-color: #ecf0f5;}
  	.content-box{margin-top:50px;}
  	.proList{list-style: none; margin:10px auto;padding: 0px;}
  	.proList li{width: 260px; height: 260px; border:1px solid #dddddd; float:left; margin:10px 5px 0px 5px;cursor: pointer;}
  	.proList li:hover{
  		-webkit-box-shadow:0 0 5px #D7D7D7;
        -moz-box-shadow:0 0 5px #D7D7D7;
        box-shadow:0 0 5px #D7D7D7;
  	}
  	.proList li img{width:240px;margin-top:5px;}
  	.main-header .navbar-custom-menu .btn-sm{}
  	.roles-list span{cursor: pointer;line-height:50px;}
  </style>
</head>
<body class="hold-transition skin-blue sidebar-mini fixed">
<!-- ===============头部==================start============== -->
  <header class="main-header">
    <!-- Logo -->
    <a href="#" class="logo">
      <!-- logo for regular state and mobile devices -->
      <span class="logo-lg"><b>BIM</b><spring:message code="guankongxitong"/></span>
    </a>
    <!-- 个人信息 -->
    <nav class="navbar navbar-static-top">
      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
          <li class="dropdown user user-menu">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <img src="${pageContext.request.contextPath}/webhtml/css/icon/user.png" class="user-image" alt="User Image">
              <span class="hidden-xs">${user.user.surNames}${user.user.realName}</span>
            </a>
            <ul class="dropdown-menu">
              <!-- User image -->
              <li class="user-header" style="height: 125px;">
                <img src="${pageContext.request.contextPath}/webhtml/css/icon/user.png" class="img-circle" alt="User Image" style="height: 50px;width: 50px;">
                <p>
                  ${user.user.surNames}${user.user.realName}
                  <small>${user.organization.shortName} ${user.dept.departmentName}</small>
                </p>
              </li>
              <!-- Menu Body -->
              <li class="user-body">
                <div class="row">
	                <div class="col-xs-6 text-center">
	                	<a href="${pageContext.request.contextPath}/web/index.do"><spring:message code="XiTongShouYe"/></a>
					</div>
                  <div class="col-xs-6 text-center">
                    <%-- <a href="${pageContext.request.contextPath}/login.do">退出</a> --%>
                    <a id="btnLogout" href="javascript:void(0);"><spring:message code="tuichu"/></a>
                  </div>
                </div>
              </li>
            </ul>
          </li>
        </ul>
      </div>
      <div class="navbar-custom-menu roles-list" >
      	<c:forEach items="${roles}" var="role">
      		<c:choose>
      			<c:when test="${role==Constant.SYSTEM_ROLE_0}">
      				<span tag="${role}" class="btn-sm btn-default"><spring:message code="XiTongGLY"/></span>
      			</c:when>
      			<c:when test="${role==Constant.SYSTEM_ROLE_1}">
      				<span tag="${role}" class="btn-sm btn-default"><spring:message code="XiangMuQuanXianGLY"/></span>
      			</c:when>
      			<c:otherwise>
      			</c:otherwise>
      		</c:choose>
      	</c:forEach>
      </div>
    </nav>
  </header>
  <!-- ===============头部==================end============== -->
  <!-- Content Wrapper. Contains page content -->
  <div class="container content-box">
  	<div class="row">
  		<div class="col-md-12">
  			<h3><spring:message code="xiangmuliebiao"/></h3>
  		</div>
  	</div>
  	<div class="row">
  		<div class="col-md-12 text-center">
  			<ul class="proList">
  			<c:forEach items="${proList}" var="item">  
		        <li server-url="${item.address}/home.do" id="${item.id}">
		   			<img alt="" src="${item.imgUrl}">
		   			<h4>${item.proName}</h4>
		   		</li>
		    </c:forEach>
	   	</ul>
  		</div>
  	</div>
  </div>
  <!-- /.content-wrapper -->
  <!-- <footer class="main-footer">
    <div class="pull-right hidden-xs">
      <b>Version</b> 1.0 beta
    </div>
    <strong>Copyright &copy; 2017-2018 bimtechnologie.com.</strong> All rights
    reserved.
  </footer> -->
  	<!-- jQuery 2.2.3 -->
	<script src="${pageContext.request.contextPath}/static/plugins/jQuery/jquery-2.2.3.min.js"></script>
	<!-- Bootstrap 3.3.6 -->
	<script src="${pageContext.request.contextPath}/static/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		var token="${token}";
		$(".roles-list").on("click","span",function(){
			location.href="home.do?currRole="+$(this).attr("tag");		
		})
		$(".proList").on("click","li",function(){
			location.href=$(this).attr("server-url")+"?proid="+ this.id + "&proName=" +encodeURI($(this).find("h4").text()+"&token="+token);		
		})
		$('#btnLogout').click(function (){
			$.post('${pageContext.request.contextPath}/api/user/logout.do',function(result){
				window.location.href="${pageContext.request.contextPath}/login.do";	
			},'json');
		});
	</script>
</body>

</html>