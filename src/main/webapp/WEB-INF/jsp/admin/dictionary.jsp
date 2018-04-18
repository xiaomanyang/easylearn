<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<div id="dictionaryPanel" class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north'" class="search_banner">
		<div class="group">
			<!-- <label>关键字：</label> -->
			<input class="easyui-searchbox" style="width: 200px;" type="text" id="txtDictionaryKey" data-options="prompt:'代码/名称',searcher:btnRefreshDictionary"></input>
		</div>
		<!-- <div class="group">
			<button type="button" id="btnSearch" onclick="btnRefreshDictionary()" class="btn btn-primary">查询</button>
		</div> -->
    </div>
    <div data-options="region:'center'">
    	<table id="dt_dictionary"></table>
    </div>
    <button type="submit"></button>
</div>

<div id="dictionaryAdd">
<form id="dictionaryForm" class="form-horizontal">
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
	<!-- <div class="form-group">
		<label class="col-sm-2 control-label" for="isDelete">是否删除</label>
		<div class="col-sm-10">
			<input class="form-control" id="isDelete" type="checkbox">
		</div>
	</div> -->
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

$('#dt_dictionary').datagrid({
	url: '${pageContext.request.contextPath}/dictionary/getListByPage.do',
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
			btnAddDictionary();
			refreshDictionaryValid();
		}
    } ,'-',{
    	text : '修改',
		iconCls : 'icon-edit',
		handler : function() {
			btnEditDictionary();
			refreshDictionaryValid();
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
		var btnEnable=$('#dictionaryPanel span[class="l-btn-icon icon-remove"]').prev();
		if(rowData.isDelete==1){
			btnEnable.text("启用");
		}else{
			btnEnable.text("禁用");
		}
	},
	onUnselect:function(rowIndex, rowData){
		var rows = $('#dt_dictionary').datagrid("getSelections");
		if(rows.length>0){
			$('#dictionaryPanel span[class="l-btn-icon icon-remove"]').prev().text(rows[0].isDelete==1?"启用":"禁用");
		}
	}
});

/* 分页插件初始化 */
var p = $('#dt_dictionary').datagrid('getPager'); 
$(p).pagination({  
	pageSize: 10,  
    pageList: [10,20],  
    showPageList: true,  
    showRefresh: true,
    beforePageText: '第',  
    afterPageText: '页    共 {pages} 页',  
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
});  

/* 获取数据 */
function btnRefreshDictionary()
{
	$('#dt_dictionary').datagrid('load', {    
		'searchKey':$("#txtDictionaryKey").val()
	});
}

/* 增加 */
function btnAddDictionary(){
	$('#id').val('');
	$('#dictionaryForm')[0].reset();
	$('#dictionaryAdd').dialog({title: "增加",iconCls: 'icon-add'});
	$('#dictionaryAdd').dialog('open');
}

/* 修改 单条*/
function btnEditDictionary(){
	var rows = $('#dt_dictionary').datagrid("getSelections");    
	if(rows.length==0){
		$.messager.alert('警告','请选择要修改的行！','info');
		return;
	}
	if(rows.length>1){
		$.messager.alert('警告','请选择单条记录修改！','info');
		return;
	}
	setWebControlValueByForm('dictionaryForm',rows[0]);
	$('#dictionaryAdd').dialog({title: "编辑",iconCls: 'icon-edit'});
	$('#dictionaryAdd').dialog('open');
}

/* 单个删除 */
/* function btnDel(delId){
	$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
	    if (r){    
	    	$.ajax({  
	    		type : "post",  
	    	    url : baseUrl+"/dictionary/delete.do",   
	    	    data : {'id':delId},  
	    	    success : function(result) {
	    	    	var json = JSON.parse(result);
	    	    	if(!json.req){
	    	    		$.messager.alert('温馨提示',json.msg,'error');
	    	    	}else{
	    	    		$.messager.alert('温馨提示',json.msg,'info');
	    	    		btnRefreshDictionary(1,10);
	    	    	}
	    	    }
	    	});	    	
	    }
	});
} */

/* 批量启用/禁用 */
function btnBatchOnOff(){
	var textEnable=$('#dictionaryPanel span[class="l-btn-icon icon-remove"]').prev().text();
	var rows = $('#dt_dictionary').datagrid("getSelections");    
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
	    	    url : baseUrl+"/dictionary/batchEnableByIds.do",   
	    	    data : paramIds,  
	    	    success : function(result) {
	    	    	var json = JSON.parse(result);
	    	    	if(!json.req){
	    	    		$.messager.alert('温馨提示',json.msg,'error');
	    	    	}else{
	    	    		$.messager.alert('温馨提示',json.msg,'success');
	    	    		btnRefreshDictionary();
	    	    	}
	    	    }
	    	});	    	
	    }    
	});
}
/* 批量删除 */
/* function btnBatchDel(){
	var rows = $('#dt_dictionary').datagrid("getSelections");    
	if(rows.length<=0){
		$.messager.alert('警告','请选择要删除的行','info');
		return;
	}
	var delIds=new Array();
	for(var index in rows){
		delIds.push(rows[index].id);
	}
	var paramIds=jQuery.param({'ids':delIds},true);
	$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
	    if (r){    
	    	$.ajax({  
	    		type : "post",  
	    	    url : baseUrl+"/dictionary/batchDeleteByIds.do",   
	    	    data : paramIds,  
	    	    success : function(result) {
	    	    	var json = JSON.parse(result);
	    	    	if(!json.req){
	    	    		$.messager.alert('温馨提示',json.msg,'error');
	    	    	}else{
	    	    		$.messager.alert('温馨提示',json.msg,'success');
	    	    		btnRefreshDictionary();
	    	    	}
	    	    }
	    	});	    	
	    }    
	});
} */

/* 操作
function formatOper(val,row,index){  
	var lab_edit = '<a href="#" onclick="btn_edit('+row.id+')">修改</a>';
    return lab_edit;
} */ 

$('#parentCode').validatebox({required: true});
$('#code').validatebox({required: true});
$('#showName').validatebox({required: true});

function refreshDictionaryValid(){
	$('#parentCode').validatebox('validate');
	$('#code').validatebox('validate');
	$('#showName').validatebox('validate');
}

/* 添加或編輯按鈕彈出框 */
$('#dictionaryAdd').dialog({  
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
			if(!$('#dictionaryForm').form('validate')){
				return;
			}
			/* var dictionaryId = $('#dictionaryId').val();
			var code = $('#code').val();
			var parentCode = $('#parentCode').val();
			var showName = $('#showName').val();
			var isDelete = $('#isDelete').val();
			var memo = $('#memo').val(); */
			/* data : {'id':dictionaryId,'code':code,'parentCode':parentCode,'showName':showName,'isDelete':isDelete,'memo':memo}, */
			$.ajax({
				type : "post",  
	    	    url : baseUrl+"/dictionary/submitForm.do",  
	    	    data:$("#dictionaryForm").serializeArray(),
	    	    success : function(result) {  
	    	    	var json = JSON.parse(result);
	    	    	if(!json.req){
	    	    		$.messager.alert('温馨提示',json.msg,'error');
	    	    	}else{
	    	    		$('#dictionaryId').val('');
	    	    		btnRefreshDictionary();
		    	    	$.messager.progress('close');
		    	    	$('#dictionaryAdd').dialog('close');
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
			$('#dictionaryAdd').dialog('close');
		}
	}]
}); 

</script>