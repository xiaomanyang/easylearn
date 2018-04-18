<%@ page language="java" pageEncoding="UTF-8"%>
<!-- 搜索框 -->
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north'" class="search_banner">
    	<div class="group">
			<label>组织机构：</label>
			<input id="select_user_org" class="easyui-combobox" style="width:200px;"/>
		</div>
		<div class="group">
			<input id="dept_name" class="easyui-searchbox" data-options="prompt:'请搜索部门名称',searcher:event_select_dept" style="width:200px"></input>
		</div>
    </div>
    <div data-options="region:'center'">
    	<table id="datagrid_dept_list"></table>
    </div>
</div>
<div id="dialog_dept">
<form id="form_dept" class="form-horizontal" method="post">
	<div class="form-group">
		<label class="col-sm-2 control-label">部门名称</label>
		<div class="col-sm-10">
			<input class="form-control" id="departmentName" name="departmentName" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">部门名称</label>
		<div class="col-sm-10">
			<input class="form-control" id="departmentNameEn" name="departmentNameEn" type="text">
		</div>
	</div>
	<input id="dept_id_params" type="hidden" name="id">
	<input id="org_id_params" type="hidden" name="orgId">
</form>
</div>
<script type="text/javascript">
$('#departmentName').textbox({width:386,height:34,required:true,validType:'length[1,60]'});
$('#departmentNameEn').textbox({width:386,height:34,required:true,validType:'length[1,60]'});
var resUserOrg;
$.ajax({
	url:'${pageContext.request.contextPath}/org/list.do',
	type: "POST",
	async:false, 
	dataType: 'json',
	success: function(result){
		resUserOrg = result;
	}
});
//组织
$('#select_user_org').combobox({
    valueField:'id',
    textField:'text',
    panelHeight: 'auto',
    data:resUserOrg,
    editable:false,
    onSelect : function(record){
    	$('#org_id_params').val(record.id);
    	getDeptList();
    }/* ,
    onLoadSuccess: function () { //加载完成后,设置选中第一项
    	var val = $(this).combobox("getData");
        for (var item in val[0]) {
            if (item == "id") {
                $(this).combobox("select", val[0][item]);
            }
        }
    } */
});
$('#datagrid_dept_list').datagrid({
	fit : true,
	border : false,
	fitColumns : true,//真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动。
	rownumbers : true,//行号列
	striped : true,//是否显示斑马线
	singleSelect : false,//只能选择一行
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
        {field:'departmentName',title:'部门名称',width:40},
        {field:'departmentNameEn',title:'英文名称',width:40},
        {field:'createTime',title:'创建日期',width:45,formatter:function(value){return formatterdate(value);}},
        {field:'memo',title:'备注',width:350},
        /* {field:'_operate',title:'操作',width:30,align:'center',formatter:formatOper}, */
    ]],toolbar:[{
    	text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			btn_create_dept();
		}
    } ,'-',
    {
    	text : '编辑',
		iconCls : 'icon-edit',
		handler : function() {
			a_dept_edit();
		}
    } ,'-',{
    	text : '删除',
		iconCls : 'icon-remove',
		handler : function() {
			btn_delete_dept();
		}
	}],
	onLoadSuccess: function (data) {
	  if (data.total == 0) {
	    var body = $(this).data().datagrid.dc.body2;
	    body.find('table tbody').append(getEmptyTip()); 
	  }
	}
});
//查询
function event_select_dept(){
	getDeptList();
}
//创建
function btn_create_dept(){
	//请选择组织
	var org = $('#select_user_org').combobox('getValue');
	if(org==''){
		$.messager.alert('警告','请选择组织机构','warning');
		return;
	}
	$('#form_dept').form('clear');
	$('#dialog_dept').dialog('open');
}
function btn_delete_dept(){
	var rows = $('#datagrid_dept_list').datagrid("getSelections");    
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
	    	    url : "dept/deleteDepts.do",  
	    	    async:false, 
	    	    data : {ids : ids},  
	    	    success : function(items) {  
	    	    	var json = JSON.parse(items);
	    	    	if(!json.req){
	    	    		$.messager.alert('提示',json.msg,'error');
	    	    	}else{
	    	    		$.messager.alert('提示',json.msg,'success');
	    	    		getDeptListRefresh();
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
function a_dept_edit(){
	var rowData = $('#datagrid_dept_list').datagrid('getSelections');
	if(rowData && 1 == rowData.length){
		// 获得正在编辑的行
		$('#form_dept').form('clear');
		$('#form_dept').form('load',rowData[0]);
		$('#dept_id_params').val(rowData[0].id);
		$('#dialog_dept').dialog('open');
	}else{
		$.messager.alert('提示','请选择一行进行编辑', 'warning');
	}
}
//添加按鈕彈出框
$('#dialog_dept').dialog({  
    title: "维护部门",  
    width: 500,  
    height: 200,
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
			$('#org_id_params').val($('#select_user_org').combobox('getValue'));
			var nameLength = $('#departmentName').val();
			if($('#form_dept').form('validate')){
				if(nameLength.length>=60){
					$.messager.alert('提示','部门名称过长', 'warning');
					return;
				}
				$('#form_dept').form('submit',{
					url: '${pageContext.request.contextPath}/dept/addOrEditDept.do',
					success: function (data, status){
						if(JSON.parse(data).req){
							$.messager.alert('提示',JSON.parse(data).msg,'success');
				        	$('#dialog_dept').dialog('close');
				        	getDeptListRefresh();
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
			$('#dialog_dept').dialog('close');
		}
	}]
}); 
function getDeptList(){
	var name = $('#dept_name').val();
	var orgId = $('#org_id_params').val();
	$('#datagrid_dept_list').datagrid({
		url : '${pageContext.request.contextPath}/dept/listDept.do',
		queryParams : {
			'deptName':name,
	        'orgId':orgId
		}
	});
}
//刷新当前页
function getDeptListRefresh(){
	var name = $('#dept_name').val();
	var orgId = $('#org_id_params').val();
	$('#datagrid_dept_list').datagrid('reload',{
		'deptName':name,
        'orgId':orgId
	});
}
</script>
