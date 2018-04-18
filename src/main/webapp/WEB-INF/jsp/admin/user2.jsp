<%@ page language="java" pageEncoding="UTF-8"%>

<div class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'west',split:true" style="width:250px;">
    	<ul id="user2_org_menuTree" class="easyui-tree"> </ul> 
    </div>   
    <div data-options="region:'center'" >
		<div class="easyui-layout" data-options="fit:true">
		    <div data-options="region:'north'" class="search_banner">
				<div class="group">
					<!-- 搜索框 -->
					<input id="select_account" class="easyui-searchbox" data-options="prompt:'请搜索用户手机号码',searcher:event_select_user" style="width:200px"></input>
				</div>
		    </div>
		    <div data-options="region:'center'">
		    	<table id="list_user"></table>
		    </div>
		</div>
    </div>   
</div> 
<div id="user2_userMenu_dialog">
	<ul id="user2_userMenu_tree"></ul>
</div>
<div id="dialog_user" style="display: none">
	<form id="form_user" class="form-horizontal" method="post">
		<div class="form-group">
			<label class="col-sm-2 control-label">区号</label>
			<div class="col-sm-10">
				<input class="form-control" id="mobileZone" type="text" name="mobileZone">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">移动电话</label>
			<div class="col-sm-10">
				<input class="form-control" id="user2_mobile" type="text" name="mobile">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">电子邮箱</label>
			<div class="col-sm-10">
				<input class="form-control" id="email" type="text" name="email">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">姓氏</label>
			<div class="col-sm-10">
				<input class="form-control" id="surNames" type="text" name="surNames">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">名字</label>
			<div class="col-sm-10">
				<input class="form-control" id="realName" type="text" name="realName">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">组织</label>
			<div class="col-sm-10">
				<input class="form-control" id="user_sysOrgId" style="width:386px;height:36px" type="text" name="sysOrgId">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">部门</label>
			<div class="col-sm-10">
				<input class="form-control" id="opts_dept" style="width:386px;height:36px" type="text" name="sysDepId">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">重要等级</label>
			<div class="col-sm-10">
				<input class="form-control" id="opts_user_level" type="text" style="width:386px;height:36px" type="text" name="userLevel">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">用户状态</label>
			<div class="col-sm-10">
				<input class="form-control" id="opts_user_status" type="text" style="width:386px;height:36px" type="text" name="status">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">备注</label>
			<div class="col-sm-10">
				<input class="form-control" id="memo" type="text" name="memo" style="width:386px;height:36px">
			</div>
		</div>
		<input id="user_id" name="id" type="hidden"/>
		<input id="user_editable" name="editable" type="hidden"/>
		<input id="purviewType" type="hidden"/>
	</form>
</div>
<script type="text/javascript">
console.info('user2');
var baseUrl = '${pageContext.request.contextPath}';
// 组织机构树
$('#user2_org_menuTree').tree({
	checkbox : true,
	onlyLeafCheck : true,
	lines : true,
	url: '${pageContext.request.contextPath}/user2/orgTree.do',   
	onCheck : function(node, checked){
		if(checked){
			var nodes = $(this).tree('getChecked');
			for(var i=0; i<nodes.length; i++){
				if(nodes[i].id != node.id)
					$(this).tree('uncheck', nodes[i].target);
			}
		}
		// 根据选择的部门查询用户
		queryUserByUserId();
	},
	onLoadSuccess : function(){
		queryUserByUserId();
	}
});

// 前后台权限分配树
$('#user2_userMenu_tree').tree({
	checkbox : true,
	lines : true
});

// 区号
$('#mobileZone').combobox({
	valueField: 'code',    
  textField: 'showName',    
  required:true,
  width : 385,
  height:36,
  editable : false,
  panelHeight:'auto',
  panelMaxHeight : 400,
  url: baseUrl+'/dictionary/getListByParentCode.do',
  queryParams : { 'parentCode' : 'phone_region' }
});
$('#user2_mobile').numberbox({width:385,height:36,required:true,validType:'length[11,11]',onChange:function(newValue,oldValue){
	if($(this).numberbox('isValid')){
		//查询用户信息
		$.ajax({   
    	    type: 'POST', 
    	    async: false,
    	    url: '${pageContext.request.contextPath}/user2/userInfo.do', 
			data : { mobile : newValue},
			dataType : 'json',
    	    error: function (XMLHttpRequest, textStatus, errorThrown) {   
    	    	console.info(textStatus);
    	    },   
    	    success: function (data) { 
    	    	var user = data.rows;
    	    	if(user != null && '' != user){ 
	    	    	console.info(user);
	    	    	delete user.mobile;
   	    			$('#form_user').form('load',user);
   	    			$('#opts_dept').combobox('setValue', user.sysDepId);
    	    		if(user.editable){ // 用户存在 - 可以编辑
    	    			disableUserInfo(false);
    	    		}else{ // 用户存在 -不可以编辑
    	    			disableUserInfo(true);
    	    			$('#opts_user_status').textbox('setText','***');
    	    		}
    	    	}else{ // 用户不存在
    	    		disableUserInfo(false);
    	    		var depNodes = $('#user2_org_menuTree').tree('getChecked')
    	    		var depId = '';
    	    		var orgId = '';
    				if(0 < depNodes.length){
    					depId = depNodes[0].id;
    					orgId = $('#user2_org_menuTree').tree('getParent', depNodes[0].target).id*-1;
    				}
    	    		user = {
    	    			email:'',
    	    			surNames:'',
    	    			realName:'',
    	    			sysOrgId:orgId,
    	    			sysDepId:depId,
    	    			userLevel:'',
    	    			status:0,
    	    			memo:'',
    	    			id:null,
    	    			editable : 1
    	    		}
    	    		$('#form_user').form('load',user);
    	    	}
    	    }   
    	});
	}
}});
$('#email').textbox({width:385,height:36,validType:'email',required:true,validType:'length[10,60]'});
$('#surNames').textbox({width:385,height:36,required:true,validType:'length[1,60]'});
$('#realName').textbox({width:385,height:36,required:true,validType:'length[1,60]'});
//组织下拉 user_sysOrgId
$('#user_sysOrgId').combobox({
	valueField: 'id',    
    textField: 'name',    
    required:true,
    width : 385,
    height:36,
    editable : false,
    panelHeight:'auto',
    panelMaxHeight : 400,
    url: baseUrl+'/org/select.do',
    onShowPanel:function(){
    	//$(this).combobox('reload');
    },
    onSelect : function(value){
    	console.info(value);
    	$.ajax({   
    	    type: 'POST', 
    	    async: false,
    	    dataType:'json',
    	    url: '${pageContext.request.contextPath}/dept/selectDep.do',   
    	    data : { orgId : value.id },
    	    error: function (XMLHttpRequest, textStatus, errorThrown) {   
    	        alert(textStatus);   
    	    },   
    	    success: function (data) { 
    	    	$('#opts_dept').combobox('loadData', data);
    	    }   
    	});
    }
});
//部门下拉框
$('#opts_dept').combobox({    
	valueField:'id',
    textField:'departmentName',
    panelHeight:'auto',
    panelMaxHeight : 400,
    width:385,
    height:36,
    editable : false,
    required:true
});
// 用户等级
$('#opts_user_level').combobox({    
    valueField:'id',  
    textField:'text',
    url: '${pageContext.request.contextPath}/user2/userLevel.do',
    panelHeight:'auto',
    editable:false,
    width:385,
    height:36,
    required:true,
    onShowPanel:function(){
    	//用户等级下拉框
    	$.ajax({   
    	    type: 'POST', 
    	    async: false,
    	    url: '${pageContext.request.contextPath}/user2/userLevel.do', //用户请求数据的URL  
    	    error: function (XMLHttpRequest, textStatus, errorThrown) {   
    	        alert(textStatus);   
    	    },   
    	    success: function (data) { 
    	    	$('#opts_user_level').combobox({data : JSON.parse(data)});
    	    }   
    	});
    },
    onSelect:function(){
    	$("div").removeClass("tooltip").removeClass("tooltip-right");
    }
});
//用户状态
$('#opts_user_status').combobox({    
	valueField:'id',  
	textField:'text',
	panelHeight:'auto',
	editable:false,
	data:[{    
	    "id":"0",    
	    "text":"启用"   
	},{    
	    "id":"1",    
	    "text":"禁用"   
	}]
});
$('#memo').textbox({});

// 前后台权限分配
$('#user2_userMenu_dialog').dialog({  
    title: "权限分配",  
    width: 300,  
    height: 500,
    collapsible:false,//折叠
    maximizable:false,//最大化按钮
    iconCls: 'icon-add',//弹出框图标  
    modal: true, 
    closed : true,
    shadow : true,//显示阴影
    closable : false,
    buttons:[{
		text:'保存',
		iconCls:'icon-ok',
		handler:function(){
			// 树节点
			var menuTreeNodes = $('#user2_userMenu_tree').tree('getChecked');
			// 项目-菜单   的id集合
			var projectMenuIds = new Array();
			if(menuTreeNodes.length > 0)
				for(var i = 0; i < menuTreeNodes.length;i++){
					if(null == menuTreeNodes[i].children){
						console.info(menuTreeNodes[i].id);
						projectMenuIds.push(menuTreeNodes[i].id);
					}
				}
			else{
				projectMenuIds.push("-1");
			}
			
			$.ajax({   
		        type: 'POST',   
		        url: '${pageContext.request.contextPath}/user2/havePurview.do', 
		        async: false,
		        data : {  
		          'userId':$('#user_id').val(),  
		          'type':$('#purviewType').val(),
		          'ids':projectMenuIds
		        },  
		        error: function (XMLHttpRequest, textStatus, errorThrown) {   
		            alert(textStatus);   
		        },   
		        success: function (data) { 
		        	$.messager.alert('提示',JSON.parse(data).msg,'success');
		        	$('#user2_userMenu_dialog').dialog('close');
		        }   
		    });
		}
	},{
		text:'关闭',
		handler:function(){
			$('#user2_userMenu_dialog').dialog('close');
		}
	}]
});

//list_user
$('#list_user').datagrid({
	fit : true,
	border : false,
	fitColumns : true,//真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动。
	rownumbers : true,//行号列
	striped : true,//是否显示斑马线
	singleSelect : true,//只能选择一行
	pagination : true,//分页工具栏
	pageSize : 20,
	pageList : [ 10, 20, 50 ],
	loadMsg : '数据加载中...',
	frozenColumns : [ [ {
		field : 'ck',
		checkbox : true
	}, {
		field : 'id',
		sortable : true,
		hidden : true
	},{
		field : 'sysDepId',
		sortable : true,
		hidden : true
	},{
		field : 'userLevel',
		sortable : true,
		hidden : true
	} ] ],
    columns:[[    
    	{field:'sysOrgName',title:'单位',width:80},
        {field:'sysDepName',title:'部门',width:40},
        {field:'surNames',title:'姓氏',width:30},
        {field:'realName',title:'名字',width:30},
        {field:'mobile',title:'手机',width:45},
        {field:'email',title:'电子邮箱',width:70,align:'center'},
        {field:'createTime',title:'创建日期',width:40,align:'center',formatter:function(value){
        	if(null == value)
        		return "***";
        	return formatterdate(value);}},
        {field:'status',title:'状态',width:25,formatter:function(value){
        	if(-1 == value)
        		return "***";
        	return value==0?"启用":"禁用";
        }},
        {field:'userLevelName',title:'重要等级',width:40},
        {field:'memo',title:'备注',width:110},
    ]],
    toolbar:[{
    	text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			$('#form_user').form('clear');
			$('#opts_user_status').combobox('setValue','0');
			$('#mobileZone').combobox('select', '+86');
			var depNodes = $('#user2_org_menuTree').tree('getChecked')
			if(0 < depNodes.length){
				var depId = depNodes[0].id;
				var orgId = $('#user2_org_menuTree').tree('getParent', depNodes[0].target).id*-1;
				$('#user_sysOrgId').combobox('setValue', orgId);
				$('#opts_dept').combobox('setValue', depId);
			}
			disableUserInfo(true);
			$('#dialog_user').dialog('open');
		}
    } ,'-',
    {
    	text : '编辑',
		iconCls : 'icon-edit',
		handler : function() {
			var rowData = $('#list_user').datagrid('getSelections');
			if(rowData && 1 == rowData.length){
				if(rowData[0].editable){
					// 获得正在编辑的行
					$('#form_user').form('clear');
					$('#form_user').form('load',rowData[0]);
					$("#user_id").val(rowData[0].id);
					$('#user_sysOrgId').combobox('setValue', rowData[0].sysOrgId);
					$('#opts_dept').combobox('setValue', rowData[0].sysDepId);
					
					$('#dialog_user').dialog('open');
				}else{
					$.messager.alert('提示','无权限编辑', 'warning');
				}
			}else{
				$.messager.alert('提示','请选择用户', 'warning');
			}
		}
    } ,'-',
    
     {
    	text : '前端权限管理',
		iconCls : 'icon-propwoer',
		handler : function() {
			var rowData = $('#list_user').datagrid('getSelections');
			if(rowData && 1 == rowData.length){
				$('#purviewType').val('1');
				$('#user_id').val(rowData[0].id);
				$('#user2_userMenu_tree').tree({
					url : '${pageContext.request.contextPath}/user2/menuTree.do', 
					queryParams : {
						userId : rowData[0].id,
				    	type : 1
					}
				});
				$('#user2_userMenu_dialog').dialog({title : '前端权限分配'});
				$('#user2_userMenu_dialog').dialog('open');
			}else{
				$.messager.alert('提示','请选择用户', 'warning');
			}
		}
	}, '-', {
		text : '后端权限管理', 
		iconCls : 'icon-propage',
		handler : function(){
			var rowData = $('#list_user').datagrid('getSelections');
			if(rowData && 1 == rowData.length){
				$('#purviewType').val('0');
				$('#user_id').val(rowData[0].id);
				$('#user2_userMenu_tree').tree({
					url : '${pageContext.request.contextPath}/user2/menuTree.do', 
					queryParams : {
						userId : rowData[0].id,
				    	type : 0
					}
				});
				$('#user2_userMenu_dialog').dialog({title : '后端权限分配'});
				$('#user2_userMenu_dialog').dialog('open');
			}else{
				$.messager.alert('提示','请选择用户', 'warning');
			}
		}
	} 
	],
	onLoadSuccess: function (data) {
	  if (data.total == 0) {
	    var body = $(this).data().datagrid.dc.body2;
	    body.find('table tbody').append(getEmptyTip()); 
	  }
	}
});
//queryUserByUserId();

//根据账号查询用户
function event_select_user(){
	queryUserByUserId();
}

// 用户信息不可编辑设置  b=true 不可编辑 
function disableUserInfo(b){
	if(b){ // 搞灰他
		$('#email').textbox('disable');
		$('#surNames').textbox('disable');
		$('#realName').textbox('disable');
		$('#user_sysOrgId').combobox('disable');
		$('#opts_dept').combobox('disable');
		$('#opts_user_level').combobox('disable');
		$('#opts_user_status').combobox('disable');
		$('#memo').textbox('disable');
	}else{ // 可以操作
		$('#email').textbox('enable');
		$('#surNames').textbox('enable');
		$('#realName').textbox('enable');
		$('#user_sysOrgId').combobox('enable');
		$('#opts_dept').combobox('enable');
		$('#opts_user_level').combobox('enable');
		$('#opts_user_status').combobox('enable');
		$('#memo').textbox('enable');
		
	}
}

// 用户信息表单
$('#dialog_user').dialog({  
    title: "用户信息",  
    width: 500,  
    height: 610,
    collapsible:false,//折叠
    maximizable:false,//最大化按钮
    iconCls: 'icon-add',//弹出框图标  
    modal: true, 
    closed : true,
    shadow : true,//显示阴影
    closable : false,
    buttons:[{
		text:'保存',
		iconCls:'icon-ok',
		handler:function(){
			if($('#form_user').form('validate')){
				$('#form_user').form('submit',{
					url: '${pageContext.request.contextPath}/user2/addOrEditUser2.do',
					success: function (data, status){
						$("div").removeClass("tooltip").removeClass("tooltip-right");
						if(JSON.parse(data).req){
							$.messager.alert('提示',JSON.parse(data).msg,'success');
				        	$('#dialog_user').dialog('close');
				        	$('#user2_org_menuTree').tree('reload');
						}else{
							$.messager.alert('提示',JSON.parse(data).msg,'error');
						}
			        }
				});
			}
		}
	},{
		text:'关闭',
		handler:function(){
			$('#dialog_user').dialog('close');
			$("div").removeClass("tooltip").removeClass("tooltip-right");
		}
	}]
});
//查询用户列表
function queryUserByUserId(){
	var account = $('#select_account').val();
	var sysDepId = '';
	var nodes = $('#user2_org_menuTree').tree('getChecked');
	if(nodes.length > 0)
		sysDepId = nodes[0].id;
	$('#list_user').datagrid({
		url : '${pageContext.request.contextPath}/user2/listUser.do',
		queryParams : {
			account : account,
			sysDepId : sysDepId
		}
	});
}
</script>
