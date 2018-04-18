<%@ page language="java" pageEncoding="UTF-8"%>
<!-- 搜索框 -->
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north'" class="search_banner">
		<div class="group">
			<input id="select_account" class="easyui-searchbox" data-options="prompt:'请搜索用户手机号码',searcher:event_select_user" style="width:200px"></input>
		</div>
    </div>
    <div data-options="region:'center'">
    	<table id="list_user"></table>
    </div>
</div>
<div id="opts_user_project">
	<div class="form-group">
		<ul id="tree">
	    </ul>
	</div>
</div>
<div id="user_group_rel_dialog">
	<table id="dgUserGroupRel"></table>
</div>
<div id="user_menu_rel_dialog">
	<table id="dgUserMenuRel"></table>
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
				<input class="form-control" id="mobile" type="text" name="mobile">
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
			<label class="col-sm-2 control-label">实时渲染等级</label>
			<div class="col-sm-10">
				<input class="form-control" id="render_level" type="text" style="width:386px;height:36px" type="text" name="renderLevel">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">用户状态</label>
			<div class="col-sm-10">
				<input class="form-control" id="opts_user_status" type="text" style="width:386px;height:36px" type="text" name="status">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">语音设置</label>
			<div class="col-sm-10">
				<input class="form-control" id="voice" type="text" style="width:386px;height:36px" type="text" name="voice">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">设备数量</label>
			<div class="col-sm-10">
				<label class="form-control" id="deviceCount" style="width:386px;height:36px"></label>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">备注</label>
			<div class="col-sm-10">
				<input class="form-control" id="memo" type="text" name="memo" style="width:386px;height:36px">
			</div>
		</div>
		<input id="user_id" name="id" type="hidden"/>
		<input id="purviewType" type="hidden"/>
	</form>
</div>
<script type="text/javascript">
console.info('user');
var baseUrl = '${pageContext.request.contextPath}';


// 组织下拉 user_sysOrgId
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
    	$(this).combobox('reload');
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
    	    	$('#opts_dept').combobox({data : data});
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
    required:true,
    editable:false
});

// 用户等级
$('#opts_user_level').combobox({    
    valueField:'id',  
    textField:'text',
    url: '${pageContext.request.contextPath}/user/userLevel.do',
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
    	    url: '${pageContext.request.contextPath}/user/userLevel.do', //用户请求数据的URL  
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

$('#render_level').combobox({    
	valueField:'id',  
	textField:'text',
	panelHeight:'auto',
	editable:false,
	data:[{    
	    "id":"render_level_1",    
	    "text":"1级"   
	},{    
	    "id":"render_level_2",    
	    "text":"2级"   
	}]
});

$('#voice').combobox({    
	valueField:'id',  
	textField:'text',
	panelHeight:'auto',
	editable:false,
	data:[{    
	    "id":"0",    
	    "text":"开启"   
	},{    
	    "id":"1",    
	    "text":"关闭"   
	}],
	onChange:function(newValue, oldValue){
		if(newValue==1){
			$('#deviceCount').text("");
			return;		
		}
		var rowData=$('#list_user').datagrid('getSelected');
		console.log(rowData);
		if(rowData){
			$('#deviceCount').text(rowData.deviceCount);
		}
	}
});

//必填项
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
$('#mobile').numberbox({width:385,height:36,required:true,validType:'length[11,11]'});
$('#email').textbox({width:385,height:36,validType:'email',required:true,validType:'length[10,60]'});
$('#surNames').textbox({width:385,height:36,required:true,validType:'length[1,60]'});
$('#realName').textbox({width:385,height:36,required:true,validType:'length[1,60]'});

// 项目权限分配
$('#opts_user_project').dialog({  
    title: "权限分配",  
    width: 500,  
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
			var p_nodes = $("#tree").tree('getChecked');
			var arr = new Array();
			if(p_nodes.length!=0){
				for (var i = 0; i < p_nodes.length;i++) {
					//证明是末节点
					var _childrens = $('#tree').tree('getChildren', p_nodes[i].target);
					if(_childrens.length==0){
						var projectId = $('#tree').tree('getParent', p_nodes[i].target);
						arr.push(projectId.id+'|'+p_nodes[i].id);
					}
	            }
			}else{
				arr.push(-1);
			}
			var edit = $('#user_id').val();
			var typePur = $('#purviewType').val();
			//区分是项目权限还是管理权限
			$.ajax({   
		        type: 'POST',   
		        url: '${pageContext.request.contextPath}/user/havePurview.do', 
		        async: false,
		        data : {  
		          'userId':edit,  
		          'type':typePur,
		          'ids':arr
		        },  
		        error: function (XMLHttpRequest, textStatus, errorThrown) {   
		            alert(textStatus);   
		        },   
		        success: function (data) { 
		        	$.messager.alert('提示',JSON.parse(data).msg,'success');
		        	$('#opts_user_project').dialog('close');
		        	getUserListRefresh();
		        }   
		    });
		}
	},{
		text:'关闭',
		handler:function(){
			$('#opts_user_project').dialog('close');
		}
	}]
});

// 项目组分配表格
$('#dgUserGroupRel').datagrid({
	fit : true,
	border : true,
	fitColumns : true,
	rownumbers : true,
	striped : true,
	loadMsg : '数据加载中...',
	frozenColumns : [[ 
		{field : 'ck',checkbox : true}, 
		{field : 'id',sortable : true, hidden:true} 
		]],
    columns:[[    
        {field:'name',title:'项目组名称',width:100},    
        {field:'createTime',title:'项目组创建时间',width:100, formatter: function(value,row,index){
    		return formatterdate(value);
		}}
    ]],
	onLoadSuccess: function (data) {
		  if (data.total == 0) {
		    var body = $(this).data().datagrid.dc.body2;
		    body.find('table tbody').append(getEmptyTip());
		  }else{
			  for(var i=0; i<data.total; i++){
				  if(null == data.rows[i].userId)
					  continue;
				  $(this).datagrid('selectRow',i);
			  }
		  }
	}
});

// 项目组分配
$('#user_group_rel_dialog').dialog({  
    title: "项目组分配",  
    width: 400,  
    height: 400,
    collapsible:false,//折叠
    maximizable:false,//最大化按钮
    modal: true, 
    closed : true,
    shadow : true,//显示阴影
    buttons:[{
		text:'保存',
		iconCls:'icon-ok',
		handler:function(){
			var groups = $('#dgUserGroupRel').datagrid("getSelections");
			var groupsIds=new Array();
			for(var index in groups){
				groupsIds.push(groups[index].id);
			}
			var paramData=jQuery.param({
		    	'userId' : $('#list_user').datagrid('getSelections')[0].id,
		    	'groupIds' : groupsIds.join(',')
		    },true);
			
			$.ajax({  
				type : "post",  
			    url : baseUrl+"/user/saveUserGroupRel.do",   
			    dataType : 'json',
			    data : paramData,  
			    success : function(result) {
			    	if(!result.req){
			    		$.messager.alert('温馨提示',result.msg,'warning');
			    	}else{
			    		$('#user_group_rel_dialog').dialog('close');
			    		$.messager.alert('温馨提示',result.msg,'success');
			    		$('#dgUserGroupRel').datagrid('reload');
			    	}
			    }
			});	
		}
	},{
		text:'关闭',
		handler:function(){
			$('#user_group_rel_dialog').dialog('close');
		}
	}]
});

//系统权限分配表格
$('#dgUserMenuRel').datagrid({
	fit : true,
	border : true,
	fitColumns : true,
	rownumbers : true,
	striped : true,
	loadMsg : '数据加载中...',
	frozenColumns : [[ 
		{field : 'ck',checkbox : true}, 
		{field : 'id',sortable : true, hidden:true} 
		]],
    columns:[[    
        {field:'menuName',title:'系统权限名称',width:100},    
        {field:'code',title:'系统权限编码',width:100}
    ]],
	onLoadSuccess: function (data) {
		  if (data.total == 0) {
		    var body = $(this).data().datagrid.dc.body2;
		    body.find('table tbody').append(getEmptyTip());
		  }else{
			  for(var i=0; i<data.total; i++){
				  if(null == data.rows[i].userId)
					  continue;
				  $(this).datagrid('selectRow',i);
			  }
		  }
	}
});
// 系统权限分配
$('#user_menu_rel_dialog').dialog({  
    title: "系统权限分配",  
    width: 400,  
    height: 410,
    collapsible:false,//折叠
    maximizable:false,//最大化按钮
    modal: true, 
    closed : true,
    shadow : true,//显示阴影
    buttons:[{
		text:'保存',
		iconCls:'icon-ok',
		handler:function(){
			var menus = $('#dgUserMenuRel').datagrid("getSelections");
			var menuIds=new Array();
			for(var index in menus){
				menuIds.push(menus[index].id);
			}
			var paramData=jQuery.param({
		    	'userId' : $('#list_user').datagrid('getSelections')[0].id,
		    	'menuIds' : menuIds.join(',')
		    },true);
			
			$.ajax({  
				type : "post",  
			    url : baseUrl+"/user/saveUserMenuRel.do",   
			    dataType : 'json',
			    data : paramData,  
			    success : function(result) {
			    	if(!result.req){
			    		$.messager.alert('温馨提示',result.msg,'warning');
			    	}else{
			    		$('#user_menu_rel_dialog').dialog('close');
			    		$.messager.alert('温馨提示',result.msg,'success');
			    		$('#dgUserMenuRel').datagrid('reload');
			    	}
			    }
			});
		}
	},{
		text:'关闭',
		handler:function(){
			$('#user_menu_rel_dialog').dialog('close');
		}
	}]
});
//加载项目权限树,管理页面权限树
function powerTree(userId,purviewType){
	var datas ;
	$.ajax({   
        type: 'POST',   
        url: '${pageContext.request.contextPath}/user/listTree.do', 
        async: false,
        data : {  
          'userId':userId,  
          'type':purviewType
        },  
        error: function (XMLHttpRequest, textStatus, errorThrown) {   
            alert(textStatus);   
        },   
        success: function (data) { 
        	datas = JSON.parse(data);
        }   
    });
	$('#tree').tree({
          data:datas,
          animate: true,
          cascadeCheck:true,//层叠选中  
          checkbox:true
    });
	$('#opts_user_project').dialog('open');
}
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
    	{field:'sysOrgName',title:'单位',width:60},
        {field:'sysDepName',title:'部门',width:40},
        {field:'surNames',title:'姓氏',width:30},
        {field:'realName',title:'名字',width:30},
        {field:'mobile',title:'手机',width:45},
        {field:'email',title:'电子邮箱',width:70},
        {field:'createTime',title:'创建日期',width:40,formatter:function(value){return formatterdate(value);}},
        {field:'status',title:'状态',width:25,formatter:function(value){
        	return value==0?"启用":"禁用";
        }},
        {field:'userLevelName',title:'重要等级',width:40},
        {field:'renderLevel',title:'实时渲染等级',width:40,formatter:function(value,rowData,rowIndex){
        	/* consloe.log(value.substring(value.length-1)+"级"); */
        	return value.substring(value.length-1)+"级";
        }},
        {field:'voice',title:'语音',width:40,formatter:function(value,rowData,rowIndex){
        	return value==0?"开启":"关闭";
        }},
        {field:'deviceCount',title:'设备数量',width:40,formatter:function(value,rowData,rowIndex){
        	return rowData.voice==0?rowData.deviceCount:"";
        }},
        {field:'memo',title:'备注',width:110},
    ]],
    toolbar:[{
    	text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			$('#form_user').form('clear');
			$('#opts_user_status').combobox('setValue','0');
			$('#mobileZone').combobox('select', '+86');
			$('#dialog_user').dialog('open');
		}
    } ,'-',
    {
    	text : '编辑',
		iconCls : 'icon-edit',
		handler : function() {
			var rowData = $('#list_user').datagrid('getSelections');
			if(rowData && 1 == rowData.length){
				// 获得正在编辑的行
				$('#form_user').form('clear');
				$('#form_user').form('load',rowData[0]);
				$('#opts_dept').combobox('setValue', rowData[0].sysDepId);
				$("#user_id").val(rowData[0].id);
				$('#dialog_user').dialog('open');
			}else{
				$.messager.alert('提示','请选择一行进行编辑', 'warning');
			}
		}
    } ,'-',{
    	text : '项目组分配',
    	iconCls : 'icon-propwoer',
    	handler : function(){
    		var rowData = $('#list_user').datagrid('getSelections');
			if(rowData && 1 == rowData.length){
				$('#dgUserGroupRel').datagrid({
					url : baseUrl + "/user/userGroupRel.do",  
					queryParams : {userId : rowData[0].id}
				});
	    		$('#user_group_rel_dialog').dialog('open');
			}else{
				$.messager.alert('提示','请选择一行进行权限分配', 'warning');
			}
    	}
    } ,'-',{
    	text : '系统权限分配',
    	iconCls : 'icon-propage',
    	handler : function(){
    		var rowData = $('#list_user').datagrid('getSelections');
			if(rowData && 1 == rowData.length){
				$('#dgUserMenuRel').datagrid({
					url : baseUrl + "/user/managerMenu.do",  
					queryParams : {userId : rowData[0].id}
				});
	    		$('#user_menu_rel_dialog').dialog('open');
			}else{
				$.messager.alert('提示','请选择一行进行权限分配', 'warning');
			}
    	}
    }
    /* {
    	text : '项目权限分配',
		iconCls : 'icon-propwoer',
		handler : function() {
			var rowData = $('#list_user').datagrid('getSelections');
			if(rowData && 1 == rowData.length){
				$('#purviewType').val('1');
				$('#user_id').val(rowData[0].id);
				powerTree(rowData[0].id,1);
			}else{
				$.messager.alert('提示','请选择一行进行权限分配', 'warning');
			}
		}
	}, '-', {
		text : '管理页面权限分配', 
		iconCls : 'icon-propage',
		handler : function(){
			var rowData = $('#list_user').datagrid('getSelections');
			if(rowData && 1 == rowData.length){
				$('#purviewType').val('0');
				$('#user_id').val(rowData[0].id);
				powerTree(rowData[0].id,0);
			}else{
				$.messager.alert('提示','请选择一行进行权限分配', 'warning');
			}
		}
	} */
	],
	onLoadSuccess: function (data) {
	  if (data.total == 0) {
	    var body = $(this).data().datagrid.dc.body2;
	    body.find('table tbody').append(getEmptyTip()); 
	  }
	}
});
queryUserByUserId();

//根据账号查询用户
function event_select_user(){
	queryUserByUserId();
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
			$('#opts_dept').val($('#opts_dept').combobox('getValue'));
			$('#opts_user_level').val($('#opts_user_level').combobox('getValue'));
			/* $('#opts_user_status').val($('#opts_user_status').combobox('getValue')); */
			if($('#form_user').form('validate')){
				$('#form_user').form('submit',{
					url: '${pageContext.request.contextPath}/user/addOrEditUser.do',
					success: function (data, status){
						$("div").removeClass("tooltip").removeClass("tooltip-right");
						if(JSON.parse(data).req){
							$.messager.alert('提示',JSON.parse(data).msg,'success');
				        	$('#dialog_user').dialog('close');
				        	getUserListRefresh();
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
	$('#list_user').datagrid({
		url : '${pageContext.request.contextPath}/user/listUser.do',
		queryParams : {
			account : account
		}
	});
}
//刷新当前页
function getUserListRefresh(){
	var account = $('#select_account').val();
	$('#list_user').datagrid('reload',{
		account : account
	});
}
</script>
