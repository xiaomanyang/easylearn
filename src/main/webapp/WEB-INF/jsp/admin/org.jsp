<%@ page language="java" pageEncoding="UTF-8"%>
<!-- 搜索框 -->
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north'" class="search_banner" >
		<div class="group">
			<input id="org_name" class="easyui-searchbox" data-options="prompt:'请搜索组织名称',searcher:event_select_org" style="width:200px"></input>
		</div>
    </div>
    <div data-options="region:'center'">
    	<table id="datagrid_org_list"></table>
    </div>
</div>
<div id="dialog_org">
<form id="from_org" class="form-horizontal" method="post">
	<div class="form-group">
		<label class="col-sm-2 control-label">组织名称</label>
		<div class="col-sm-10">
			<input class="form-control" type="text" name="name" id = "name">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">英文名称</label>
		<div class="col-sm-10">
			<input class="form-control" type="text" name="nameEn" id = "nameEn">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">组织简称</label>
		<div class="col-sm-10">
			<input class="form-control" id="shortName" type="text" name="shortName">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">行政区域</label>
		<div class="col-sm-10">
			<input class="form-control" id="region" type="text" name="region">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">备注</label>
		<div class="col-sm-10">
			<textarea class="form-control" id="memo" type="text" rows="4" cols="备注" name="memo"/>
		</div>
	</div>
	<input id="org_id" name="id" type="hidden">
</form>
</div>
<script type="text/javascript">
//必填项
$('#name').textbox({width:386,height:34,required:true,validType:'length[1,60]'});
$('#nameEn').textbox({width:386,height:34,required:true,validType:'length[1,60]'});
$('#shortName').textbox({width:386,height:34,required:true,validType:'length[1,4]'});
$(function(){
	$('#datagrid_org_list').datagrid({
		fit : true,
		border : false,
		fitColumns : true,//真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动。
		rownumbers : true,//行号列
		striped : true,//是否显示斑马线
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
		}]],
	    columns:[[    
	        {field:'name',title:'组织名称',width:40},
	        {field:'nameEn',title:'英文名称',width:40},
	        {field:'shortName',title:'组织简称',width:40},
	        {field:'region',title:'行政区域',width:70},
	        {field:'createTime',title:'创建日期',width:45,formatter:function(value){return formatterdate(value);}},
	        {field:'memo',title:'备注',width:100},
	       /*  {field:'_operate',title:'操作',width:30,align:'center',formatter:formatOper}, */
	    ]],
	    toolbar:[{
	    	text : '增加',
			iconCls : 'icon-add',
			handler : function() {
				btn_create_org();
			}
	    } ,'-',
	    {
	    	text : '编辑',
			iconCls : 'icon-edit',
			handler : function() {
				a_edit_org();
			}
	    } ,'-',{
	    	text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				btn_delete_org();
			}
		}],
		onLoadSuccess: function (data) {
		  if (data.total == 0) {
		    var body = $(this).data().datagrid.dc.body2;
		    body.find('table tbody').append(getEmptyTip()); 
		  }
		}
	});
	getOrgList();
});
//查询
function event_select_org(){
	getOrgList();
}
//创建
function btn_create_org(){
	$('#from_org').form('clear');
	$('#dialog_org').dialog('open');
}
function btn_delete_org(){
	var rows = $('#datagrid_org_list').datagrid("getSelections");    
	if(rows.length==0){
		$.messager.alert('警告','请选择要删除的行','error');
		return;
	}
	$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
	    if (r){ 
	    	var ids = [];
	    	for(var i=0; i<rows.length; i++){
				ids.push(rows[i].id);
			}
			$.ajax({  
				type : "post",  
	    	    url : "org/deleteOrgs.do",  
	    	    data : {ids : ids},  
	    	    success : function(items) {  
	    	    	var json = JSON.parse(items);
	    	    	if(!json.req){
	    	    		$.messager.alert('提示',json.msg,'error');
	    	    	}else{
	    	    		$.messager.alert('提示',json.msg,'success');
	    	    		getOrgListRefresh();
	    	    	}
	    	    },  
	    	    error : function(XMLHttpRequest, textStatus, errorThrown) {  
	    	        alert("系统错误！");  
	    	    } 
	    	});
	    }
	});
}
//修改
function a_edit_org(){
	var rowData = $('#datagrid_org_list').datagrid('getSelections');
	if(rowData && 1 == rowData.length){
		// 获得正在编辑的行
		$('#from_org').form('clear');
		$('#from_org').form('load',rowData[0]);
		$('#org_id').val(rowData[0].id);
		$('#dialog_org').dialog('open');
	}else{
		$.messager.alert('提示','请选择一行进行编辑', 'warning');
	}
}
//添加按鈕彈出框
$('#dialog_org').dialog({  
    title: "维护组织",  
    width: 500,  
    height: 400,
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
			if($('#from_org').form('validate')){
				$('#from_org').form('submit',{
					url: '${pageContext.request.contextPath}/org/addOrEditOrg.do',
					success: function (data, status){
						if(JSON.parse(data).req){
							$.messager.alert('提示',JSON.parse(data).msg,'success');
				        	$('#dialog_org').dialog('close');
				        	getOrgListRefresh();
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
			$('#dialog_org').dialog('close');
		}
	}]
});
function getOrgList(){
	//===================加个查询条件
	var name = $('#org_name').val();
	$('#datagrid_org_list').datagrid({
		url : '${pageContext.request.contextPath}/org/listOrg.do',
		queryParams : {
			'orgName':name
		}
	});
}
//刷新当前页
function getOrgListRefresh(){
	var name = $('#org_name').val();
	$('#datagrid_org_list').datagrid('reload',{
		'orgName':name
	});
}
</script>
