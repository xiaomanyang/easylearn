<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<div id="coursePanel" class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north'" class="search_banner">
		<div class="group">
			<label>分类：</label>
			<input id="selectClassification" class="easyui-combobox" style="width: 200px;" data-options="valueField:'id',textField:'classificationName',url:'classification/select.do',panelHeight:'auto',panelMaxHeight:'400'"></input>
		</div>
    </div>
    <div data-options="region:'center'">
    	<table id="dt_course"></table>
    </div>
    <button type="submit"></button>
</div>

<div id="courseAdd">
<form id="courseForm" class="form-horizontal">
	<input id="id" name="id" type="hidden">
	<div class="form-group">
		<label class="col-sm-2 control-label" for="parentCode">上级代码</label>
		<div class="col-sm-10">
			<input class="form-control" id="parentCode" name="parentCode" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="code">字典代码</label>
		<div class="col-sm-10">
			<input class="form-control" id="code" name="code" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="showName">显示名称</label>
		<div class="col-sm-10">
			<input class="form-control" id="showName" name="showName" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="memo">备注</label>
		<div class="col-sm-10">
			<textarea rows="4" cols="" class="form-control" id="memo" name="memo"></textarea>
		</div>
	</div>
</form>
</div>



<script type="text/javascript">
var baseUrl = '${pageContext.request.contextPath}';

$('#dt_course').datagrid({
	url: '${pageContext.request.contextPath}/course/getListByPage.do',
	fit : true,
	border : false,
	fitColumns : true,
	rownumbers : true,
	striped : true,
	pagination : true,
	pageSize : 20,
	pageList : [ 10, 20, 50 ],
	loadMsg : '数据加载中...',
	sortOrder:'desc',
	sortName:'id',
	frozenColumns : [ [ {
		field : 'ck',
		checkbox : true
	}, {
		field : 'id',
		sortable : true,
		hidden:true
	} ] ],
    columns:[[    
    	{field:'parentCode',title:'上级代码',hidden:false, width:100},
        {field:'code',title:'字典代码',width:100},    
        {field:'showName',title:'字典名称',width:100},
        {field:'isDelete',title:'启用/禁用',width:100,formatter: function(value,row,index){
				if (value==0){
					return "已启用";
				} else {
					return "已禁用";
				}
			}
		},
        {field:'memo',title:'备注',width:100},
        /* {field:'_operate',title:'操作',width:110,formatter:formatOper}, */
    ]],
    toolbar:[{
    	text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			btnAddcourse();
			refreshcourseValid();
		}
    } ,'-',{
    	text : '修改',
		iconCls : 'icon-edit',
		handler : function() {
			btnEditcourse();
			refreshcourseValid();
		}
	},'-',{
    	text : '禁用',
		iconCls : 'icon-remove',
		handler : function() {
			btnBatchOnOff();
		}
	}],
	onLoadSuccess: function (data) {
		  if (data.total == 0) {
		    var body = $(this).data().datagrid.dc.body2;
		    body.find('table tbody').append(getEmptyTip()); 
		  }
	},
	onSelect:function(rowIndex, rowData){
		var btnEnable=$('#coursePanel span[class="l-btn-icon icon-remove"]').prev();
		if(rowData.isDelete==1){
			btnEnable.text("启用");
		}else{
			btnEnable.text("禁用");
		}
	},
	onUnselect:function(rowIndex, rowData){
		var rows = $('#dt_course').datagrid("getSelections");
		if(rows.length>0){
			$('#coursePanel span[class="l-btn-icon icon-remove"]').prev().text(rows[0].isDelete==1?"启用":"禁用");
		}
	}
});


/* 获取数据 */
function btnRefreshcourse()
{
	$('#dt_course').datagrid('load', {    
		'searchKey':$("#txtcourseKey").val()
	});
}

/* 增加 */
function btnAddcourse(){
	$('#id').val('');
	$('#courseForm')[0].reset();
	$('#courseAdd').dialog({title: "增加",iconCls: 'icon-add'});
	$('#courseAdd').dialog('open');
}

/* 修改 单条*/
function btnEditcourse(){
	var rows = $('#dt_course').datagrid("getSelections");    
	if(rows.length==0){
		$.messager.alert('警告','请选择要修改的行！','info');
		return;
	}
	if(rows.length>1){
		$.messager.alert('警告','请选择单条记录修改！','info');
		return;
	}
	setWebControlValueByForm('courseForm',rows[0]);
	$('#courseAdd').dialog({title: "编辑",iconCls: 'icon-edit'});
	$('#courseAdd').dialog('open');
}

/* 批量启用/禁用 */
function btnBatchOnOff(){
	var textEnable=$('#coursePanel span[class="l-btn-icon icon-remove"]').prev().text();
	var rows = $('#dt_course').datagrid("getSelections");    
	if(rows.length<=0){
		$.messager.alert('警告','请选择要'+textEnable+'的行','info');
		return;
	}
	var delIds=new Array();
	for(var index in rows){
		delIds.push(rows[index].id);
	}
	var paramIds=jQuery.param({'ids':delIds,'isEnable':textEnable=="禁用"?1:0},true);
	$.messager.confirm('确认','您确认想要'+textEnable+'当前选中记录吗？',function(r){    
	    if (r){    
	    	$.ajax({  
	    		type : "post",  
	    	    url : baseUrl+"/course/batchEnableByIds.do",   
	    	    data : paramIds,  
	    	    success : function(result) {
	    	    	var json = JSON.parse(result);
	    	    	if(!json.req){
	    	    		$.messager.alert('温馨提示',json.msg,'error');
	    	    	}else{
	    	    		$.messager.alert('温馨提示',json.msg,'success');
	    	    		btnRefreshcourse();
	    	    	}
	    	    }
	    	});	    	
	    }    
	});
}

$('#parentCode').validatebox({required: true});
$('#code').validatebox({required: true});
$('#showName').validatebox({required: true});

function refreshcourseValid(){
	$('#parentCode').validatebox('validate');
	$('#code').validatebox('validate');
	$('#showName').validatebox('validate');
}

/* 添加或編輯按鈕彈出框 */
$('#courseAdd').dialog({  
    title: "增加",  
    width: 500,  
    height: 350,
    collapsible:true,
    maximizable:true,
    iconCls: 'icon-add',
    modal: true, 
    closed : true,
    buttons:[{
		text:'保存',
		iconCls:'icon-ok',
		handler:function(){
			if(!$('#courseForm').form('validate')){
				return;
			}
			$.ajax({
				type : "post",  
	    	    url : baseUrl+"/course/submitForm.do",  
	    	    data:$("#courseForm").serializeArray(),
	    	    success : function(result) {  
	    	    	var json = JSON.parse(result);
	    	    	if(!json.req){
	    	    		$.messager.alert('温馨提示',json.msg,'error');
	    	    	}else{
	    	    		$('#courseId').val('');
	    	    		btnRefreshcourse();
		    	    	$.messager.progress('close');
		    	    	$('#courseAdd').dialog('close');
	    	    		$.messager.alert('温馨提示',json.msg,'success');
	    	    	}
	    	    },  
	    	    error : function(XMLHttpRequest, textStatus, errorThrown) {  
	    	    	$.messager.alert('温馨提示',XMLHttpRequest.responseText,'error');
	    	    } 
	    	});
		}
	},{
		text:'关闭',
		handler:function(){
			$('#courseAdd').dialog('close');
		}
	}]
}); 

</script>