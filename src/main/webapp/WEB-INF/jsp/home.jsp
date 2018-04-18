<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>easylearn</title>
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
</head>
<body class="hold-transition skin-blue sidebar-mini fixed" style="overflow: hidden;overflow-x: hidden;overflow-y: hidden;">
  <!-- ===============头部==================start============== -->
  <header class="main-header">
    <!-- Logo -->
    <a href="#" class="logo">
      <!-- logo for regular state and mobile devices -->
      <span class="logo-lg"><b>BIM</b>管控系统</span>
    </a>
    <!-- 个人信息 -->
    <nav class="navbar navbar-static-top">
    	<div class="navbar-custom-menu" style="float:left; line-height:50px;">
    		<c:if test="${showBack==true}">
    			<a href="prolist.do" style="color:#fff; margin-left: 20px; cursor: pointer;"><c:out value="<<返回列表"></c:out></a>
    		</c:if>
    	</div>
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
	                	<a href="${pageContext.request.contextPath}/web/index.do">系统首页</a>
					</div>
                  <div class="col-xs-6 text-center">
                    <%-- <a href="${pageContext.request.contextPath}/login.do">退出</a> --%>
                    <a id="btnLogout" href="javascript:void(0);">退出</a>
                  </div>
                </div>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </nav>
  </header>
  <!-- ===============头部==================end============== -->
  <!-- ===============左侧菜单==================start============== -->
  <!-- Left side column. contains the sidebar -->
  <aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <!-- sidebar menu: : style can be found in sidebar.less -->
      <ul class="sidebar-menu">
        <li class="treeview">
          <a href="#">
            <i class="fa fa-cube"></i> <span>系统管理</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-right pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu menu-open" style="display: block;">
          	<c:forEach items="${menus}" var="menu">
          		<li id="btn_${menu.code}" tag="${menu.url}">
          			<a href="javascript:;"><i class="fa fa-circle-o"></i>${menu.menuName}</a>
          		</li>
          	</c:forEach>
            <!-- <li id="btn_org"><a href="javascript:;"><i class="fa fa-circle-o"></i> 组织管理</a></li>
            <li id="btn_dept"><a href="javascript:;"><i class="fa fa-circle-o"></i> 部门管理</a></li>
            <li id="btn_user"><a href="javascript:;"><i class="fa fa-circle-o"></i> 用户管理</a></li>
            <li id="btn_project"><a href="javascript:;"><i class="fa fa-circle-o"></i> 项目管理</a></li>
          	<li id="btn_dictionary"><a href="javascript:;"><i class="fa fa-circle-o"></i> 字典管理</a></li>
            <li id="btn_color"><a href="javascript:;"><i class="fa fa-circle-o"></i> 颜色管理</a></li>
            <li id="btn_displine"><a href="javascript:;"><i class="fa fa-circle-o"></i> 专业管理</a></li>
            <li id="btn_floor"><a href="javascript:;"><i class="fa fa-circle-o"></i> 楼层管理</a></li>
            <li id="btn_floor_displine"><a href="javascript:;"><i class="fa fa-circle-o"></i> 楼层-专业管理</a></li>
            <li id="btn_node"><a href="javascript:;"><i class="fa fa-circle-o"></i>节点管理</a></li>
            <li id="btn_model"><a href="javascript:;"><i class="fa fa-circle-o"></i>模型管理</a></li>
            <li id="btn_cloud"><a href="javascript:;"><i class="fa fa-circle-o"></i>点云管理</a></li>
            <li id="btn_panorama"><a href="javascript:;"><i class="fa fa-circle-o"></i>全景图管理</a></li> -->
          </ul>
        </li>
      </ul>
    </section>
    <!-- /.sidebar -->
  </aside>
  <!-- ===============左侧菜单=============end=================== -->
  
  <!-- =======================底部=====start=================== -->
  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content easyui-tabs" id="tabBanner" data-options="fit:true,border:false">
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
  <footer class="main-footer">
    <div class="pull-right hidden-xs">
      <b>Version</b> 1.0 beta
    </div>
    <strong>Copyright &copy; 2017-2018 bimtechnologie.com.</strong> All rights
    reserved.
  </footer>
<!-- =======================底部=======end================= -->
<input id="userId" type="hidden" value="1">
<script src="${pageContext.request.contextPath}/static/bim/js/common.js"></script>
<!-- jQuery 2.2.3 -->
<script src="${pageContext.request.contextPath}/static/plugins/jQuery/jquery-2.2.3.min.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/jQuery/jquery.form.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="${pageContext.request.contextPath}/static/bootstrap/js/bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="${pageContext.request.contextPath}/static/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="${pageContext.request.contextPath}/static/plugins/fastclick/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="${pageContext.request.contextPath}/static/dist/js/app.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="${pageContext.request.contextPath}/static/dist/js/demo.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/insdep-1.5/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/insdep-1.5/icon.css">
<link rel="stylesheet" type="text/css"	href="${pageContext.request.contextPath}/static/easyui/themes/bim.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/easyui/ajaxfileupload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/easyui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/insdep-1.5/insdep-extend.min.js"></script>
<script type="text/javascript">
jQuery.ajaxSetup({
	cache : false
});//ajax不缓存

//全局的ajax访问，处理ajax清求时sesion超时  
$(function(){
	$.ajaxSetup({   
		cache : false,
	    contentType:"application/x-www-form-urlencoded;charset=utf-8",   
	    complete:function(XMLHttpRequest,textStatus){
	    	if(XMLHttpRequest.status=="302"){
	    		//通过XMLHttpRequest取得响应头，sessionstatus，    
			   	var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus");   
			    if(sessionstatus=="timeout"){
				    //如果超时就处理 ，指定要跳转的页面    
			    	//window.location.replace("${pageContext.request.contextPath}/login.do");
				    //$.messager.alert('警告','登录超时,请重新登录...','info');
			    	window.location.replace("login.do"); 
			    }
	    	}
	    	/* if(XMLHttpRequest.responseText=="301"){   
		     	//如果没有登录 ，指定要跳转的页面                             
		     	window.location.replace("login.do");   
		    }else if(XMLHttpRequest.status=="302"){  
			    //window.location.replace("login.do");   
		    }else if(XMLHttpRequest.status=="0"){   
		     	window.location.replace("login.do");   
		    }else if(textStatus=='error'){
		     //window.location.replace("login.do");   
	    	} */
	    }
	});
	loadMenu();
});

$('#btnLogout').click(function (){
	$.post('${pageContext.request.contextPath}/api/user/logout.do',function(result){
		window.location.href="${pageContext.request.contextPath}/login.do";	
	},'json');
});

/**
 * 加载功能菜单
 */
function loadMenu(){
	/* $.post('${pageContext.request.contextPath}/menu/getMenuList.do',function(result){
		var data= JSON.parse(result);
		var element='';
		$.each(data,function(index,item){
			element='<li id="btn_'+item.code+'" tag="'+item.url+'"><a href="javascript:;"><i class="fa fa-circle-o"></i>'+item.menuName+'</a></li>';
			$(element).appendTo('.treeview-menu');
			var menu_id=$(element).attr('id');
			$('#'+menu_id).click(function (){
				var menuName= $(this).find('a').text();
				var url=$(this).attr('tag');
				addTab(menuName,'${pageContext.request.contextPath}'+url);
			});
		});
	}); */
	$('.treeview-menu').on('click','li',function(){
		var menuName= $(this).find('a').text();
		var url=$(this).attr('tag');
		addTab(menuName,'${pageContext.request.contextPath}'+url);
	});
}


// 添加tab方法
function addTab(title, url, iconCls){
    if ($('#tabBanner').tabs('exists', title)){  
        $('#tabBanner').tabs('select', title);  
    } else {
    	var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
        $('#tabBanner').tabs('add',{  
            title:title,  
            href : url,
            iconCls : iconCls,
            //content:content,
            closable: false,
            cache : true,
            closable:true
        });  
    }  
}

/*
*  datagrid 获取正在编辑状态的行，使用如下：
*  $('#id').datagrid('getEditingRowIndexs'); //获取当前datagrid中在编辑状态的行编号列表
*/
$.extend($.fn.datagrid.methods, {
    getEditingRowIndexs: function(jq) {
        var rows = $.data(jq[0], "datagrid").panel.find('.datagrid-row-editing');
        var indexs = [];
        rows.each(function(i, row) {
            var index = row.sectionRowIndex;
            if (indexs.indexOf(index) == -1) {
                indexs.push(index);
            }
        });
        return indexs;
    }
});

$.fn.serializeObject = function()
{
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [o[this.name]];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};

// 对Date的扩展，将 Date 转化为指定格式的String   
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
// 例子：   
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function(fmt) { //author: meizz   
	var o = {
		"M+" : this.getMonth() + 1, //月份   
		"d+" : this.getDate(), //日   
		"h+" : this.getHours(), //小时   
		"m+" : this.getMinutes(), //分   
		"s+" : this.getSeconds(), //秒   
		"q+" : Math.floor((this.getMonth() + 3)/3),
		"S" : this.getMilliseconds()
	//毫秒   
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};

function formatterdate(val) {
	if (val != null) {
	var date = new Date(val);
	return date.getFullYear() + '/' + (date.getMonth() + 1) + '/'+ date.getDate();
	}
}

</script>

<script>
	/* $("#btn_dictionary").bind('click',function(){
		addTab('字典管理','${pageContext.request.contextPath}/dictionary/init.do');
	});
	$("#btn_user").bind('click',function(){
		addTab('用户管理','${pageContext.request.contextPath}/user/init.do');
	});
	$("#btn_project").bind('click',function(){
		addTab('项目管理','${pageContext.request.contextPath}/project/init.do');
	});
	$("#btn_org").bind('click',function(){
		addTab('组织管理','${pageContext.request.contextPath}/org/init.do');
	});
	$("#btn_color").bind('click',function(){
		addTab('颜色管理','${pageContext.request.contextPath}/color/init.do');
	});
	$("#btn_dept").bind('click',function(){
		addTab('部门管理','${pageContext.request.contextPath}/dept/init.do');
	});
	$("#btn_displine").bind('click',function(){
		addTab('专业管理','${pageContext.request.contextPath}/displine/init.do');
	});
	$("#btn_node").bind('click',function(){
		addTab('节点管理','${pageContext.request.contextPath}/node/init.do');
	});
	$("#btn_floor").bind('click',function(){
		addTab('楼层管理','${pageContext.request.contextPath}/floor/init.do');
	});
	$("#btn_floor_displine").bind('click',function(){
		addTab('楼层-专业管理','${pageContext.request.contextPath}/floor_displine/init.do');
	});
	$("#btn_model").bind('click',function(){
		addTab('模型管理','${pageContext.request.contextPath}/model/init.do');
	});
	$("#btn_cloud").bind('click',function(){
		addTab('点云管理','${pageContext.request.contextPath}/cloud/init.do');
	});
	$("#btn_panorama").bind('click',function(){
		addTab('全景图管理','${pageContext.request.contextPath}/panorama/init.do');
	}); */
</script>
</body>
</html>
