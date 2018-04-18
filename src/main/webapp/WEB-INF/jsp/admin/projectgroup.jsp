<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north'" class="search_banner">
		<div class="group">
			<input class="easyui-searchbox" style="width: 200px;" type="text" id="txtProjectGroupKey" data-options="prompt:'编码/名称/编号',searcher:btnRefreshProjectGroup"></input>
		</div>
    </div>
    <div data-options="region:'center'">
    	<table id="dgProjectGroup"></table>
    </div>
</div>
<div id="projectGroupAdd" style="display:none;">
	<form id="projectGroupForm" class="form-horizontal">
		<input id="projectGroup_id" name="id" type="hidden">
		<div class="form-group">
			<label class="col-sm-3 control-label" for="proCode">项目组名称</label>
			<div class="col-sm-9">
				<input class="form-control" id="sysProGroup_name" name="name" type="text">
			</div>
		</div>
	</form>
</div>

<!-- 分配窗口 -->
<div id="projectGroupRelationDialog" style="display: none;">
	<table id="dgProjectGroupRel"></table>
</div>

<script type="text/javascript">
var baseUrl = '${pageContext.request.contextPath}';

$('#sysProGroup_name').validatebox({required: true});

$('#dgProjectGroup').datagrid({
	url: baseUrl+'/projectGroup/list.do', //用户请求数据的URL  
	queryParams:{ 
		'searchKey':$("#txtProjectGroupKey").val()
	},
	fit : true,
	border : false,
	fitColumns : true,
	rownumbers : true,
	striped : true,
	pagination : true,
	pageSize : 20,
	pageList : [ 10, 20, 50 ],
	loadMsg : '数据加载中...',
	singleSelect : true,//只能选择一行
	frozenColumns : [[ 
		{field : 'ck',checkbox : true}, 
		{field : 'id',sortable : true, hidden:true} 
		]],
    columns:[[    
        {field:'name',title:'项目组名称',width:100},    
        {field:'createTime',title:'创建时间',width:100,formatter: function(value,row,index){
        		return formatterdate(value);
			}
		}
    ]],
    toolbar:[{
    	text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			$('#projectGroupForm').form('clear');
			$('#projectGroupAdd').dialog({title: "增加",iconCls: 'icon-add'});
			$('#projectGroupAdd').dialog('open');
		}
    } ,'-',{
    	text : '修改',
		iconCls : 'icon-edit',
		handler : function() {
			$('#projectGroupAdd').dialog({title: "编辑",iconCls: 'icon-edit'});
			btnEditProjectGroup();
		}
	},'-',{
    	text : '删除',
		iconCls : 'icon-remove',
		handler : function() {
	    	btnDelProjectGroup();
		}
	} ,'-',{
    	text : '关联项目',
		iconCls : 'icon-propwoer',
		handler : function() {
			var rows = $('#dgProjectGroup').datagrid("getSelections");    
			if(rows.length==0){
				$.messager.alert('警告','请选择要操作的项目组','info');
				return;
			}
			$('#dgProjectGroupRel').datagrid('loadData',{total : 0, rows:{}});
			$('#dgProjectGroupRel').datagrid({
				url : baseUrl + "/projectGroup/groupProject.do",  
				queryParams : {groupId : rows[0].id}
			});
			$('#projectGroupRelationDialog').dialog('open');
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

// 项目组-项目，分配情况
$('#dgProjectGroupRel').datagrid({
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
        {field:'proName',title:'项目名称',width:100},    
        {field:'proCode',title:'项目编码',width:100},    
        {field:'region',title:'项目区域',width:100}
    ]],
	onLoadSuccess: function (data) {
		  if (data.total == 0) {
		    var body = $(this).data().datagrid.dc.body2;
		    body.find('table tbody').append(getEmptyTip());
		  }else{
			  for(var i=0; i<data.total; i++){
				  if(null == data.rows[i].groupId)
					  continue;
				  $(this).datagrid('selectRow',i);
			  }
		  }
	}
});

/* 获取数据 */
function btnRefreshProjectGroup()
{
	$('#dgProjectGroup').datagrid('load', {    
		'searchKey':$("#txtProjectGroupKey").val()
	}); 
}

/* 修改 */
function btnEditProjectGroup()
{
	var rows = $('#dgProjectGroup').datagrid("getSelections");    
	if(rows.length==0){
		$.messager.alert('警告','请选择要修改的行','info');
		return;
	}
	$('#projectGroupForm').form('load',rows[0]);
	$('#projectGroupAdd').dialog('open');
}

/* 删除 */
function btnDelProjectGroup(delId){
	var rows = $('#dgProjectGroup').datagrid("getSelections");    
	if(rows.length==0){
		$.messager.alert('警告','请选择要删除的行','info');
		return;
	}
	var delId= rows[0].id;
 	$.messager.confirm('提示','删除项目组，则与项目组相关的项目和用户的关联关系同时被删除，需要重新关联，是否删除?',function(r){    
	    if (r){    
	    	$.ajax({  
	    		type : "post",  
	    	    url : baseUrl+"/projectGroup/delete.do",   
	    	    data : {'id':delId},  
	    	    success : function(result) {
	    	    	var json = JSON.parse(result);
	    	    	if(!json.req){
	    	    		$.messager.alert('温馨提示',json.msg,'warning');
	    	    	}else{
	    	    		$.messager.alert('温馨提示',json.msg,'success');
	    	    		var options=$('#dgProjectGroup').datagrid('options');
	    	    		btnRefreshProjectGroup();
	    	    	}
	    	    }
	    	});	    	
	    }    
	});  
}



/* 添加或编辑按钮弹出框 */
$('#projectGroupAdd').dialog({  
    title: "添加",  
    width: 400,  
    height: 150,
    iconCls: 'icon-add',                 //弹出框图标  
    modal: true, 
    closed : true,
    buttons:[{
		text:'保存',
		iconCls:'icon-ok',
		handler:function(){
			if($('#projectGroupForm').form('validate')){
				$('#projectGroupForm').form('submit',{
					url: baseUrl + '/projectGroup/save.do',
					success: function (data, status)  //服务器成功响应处理函数  
			        {  
						data = $.parseJSON(data);
						btnRefreshProjectGroup();
						$('#projectGroupAdd').dialog('close');
						$.messager.alert('提示',data.msg, 'info',function(){
							if(!data.req)
								$('#projectGroupAdd').dialog('open');
						});
			        }
				});
			}
		}
	},{
		text:'关闭',
		handler:function(){
			$('#projectGroupAdd').dialog('close');
		}
	}]
}); 

/* 分配 */
$('#projectGroupRelationDialog').dialog({  
    title: "分配",  
    width: 500,  
    height: 400,
    iconCls: 'icon-propwoer',                 //弹出框图标  
    modal: true, 
    closed : true,
    buttons:[{
		text:'保存',
		iconCls:'icon-ok',
		handler:function(){
			var groupId = $('#dgProjectGroup').datagrid("getSelections")[0].id;
			var projects = $('#dgProjectGroupRel').datagrid("getSelections");
			var projectIds=new Array();
			for(var index in projects){
				projectIds.push(projects[index].id);
			}
			var paramData=jQuery.param({
		    	'groupId' : groupId,
		    	'projectIds' : projectIds.join(',')
		    },true);
			$.ajax({  
				type : "post",  
			    url : baseUrl+"/projectGroup/saveGroupProject.do",   
			    dataType : 'json',
			    data : paramData,  
			    success : function(result) {
			    	if(!result.req){
			    		$.messager.alert('温馨提示',result.msg,'warning');
			    	}else{
			    		$.messager.alert('温馨提示',result.msg,'success');
			    		$('#dgProjectGroupRel').datagrid('reload');
			    	}
			    }
			});	
		}
	},{
		text:'关闭',
		handler:function(){
			$('#projectGroupRelationDialog').dialog('close');
		}
	}]
}); 

</script>
<script type="text/javascript" src="static/bim/js/common.js"></script> 