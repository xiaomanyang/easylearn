<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<div id="projectPanel" class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north'" class="search_banner">
		<div class="group">
			<!-- <label>关键字：</label> -->
			<input class="easyui-searchbox" style="width: 200px;" type="text" id="txtProjectKey" data-options="prompt:'编码/名称/编号',searcher:btnRefreshProject"></input>
		</div>
    </div>
    <div data-options="region:'center'">
    	<table id="dgProject"></table>
    </div>
    <button type="submit"></button>
</div>
<div id="projectAdd" style="display:none;">
<form id="projectForm" class="form-horizontal">
	<input id="id" name="id" type="hidden">
	<div class="form-group">
		<label class="col-sm-2 control-label" for="proCode">项目编码</label>
		<div class="col-sm-10">
			<input class="form-control" id="proCode" name="proCode" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="proName">项目名称</label>
		<div class="col-sm-10">
			<input class="form-control" id="proName" name="proName" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="proNameEn">英文名称</label>
		<div class="col-sm-10">
			<input class="form-control" id="proNameEn" name="proNameEn" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="proNumber">项目编号</label>
		<div class="col-sm-10">
			<input class="form-control" id="proNumber" name="proNumber" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="region">区域</label>
		<div class="col-sm-4">
			<select class="form-control easyui-combobox" style="width: 170px;" id="region" name="region"></select>
		</div>
		<label class="col-sm-2 control-label" for="project_serverId">服务器</label>
		<div class="col-sm-4">
			<select class="form-control easyui-combobox" id="project_serverId" name="serverId"></select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="address">地址</label>
		<div class="col-sm-10">
			<input class="form-control" id="address" name="address" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="description">描述</label>
		<div class="col-sm-10">
			<input class="form-control" id="description" name="description" type="text">
		</div>
	</div>
	<!-- <div class="form-group">
		<label class="col-sm-2 control-label" for="file">图片</label>
		<div class="col-sm-10">
			<input id="imgUrl" name="imgUrl" type="hidden">
			<input class="form-control" id="proImgfile" name="proImgfile" type="file">
		</div>
	</div> -->
	
	<div class="form-group">
		<label class="col-sm-2 control-label">图片</label>
		<div class="col-sm-10">
			<input class="form-control" id="proImgfile" name="proImgfile"/>
		</div>
	</div>
	<!-- <div class="form-group">
		<label class="col-sm-2 control-label" for="status">状态</label>
		<div class="col-sm-10">
			<input class="form-control" id="status" name="status" type="number" value="0">
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
console.info("project");
var baseUrl = '${pageContext.request.contextPath}';
$('#proImgfile').filebox({buttonText: '选择图片', width:'470px'});
$('#proName').validatebox({required: true});
$('#proNameEn').validatebox({required: true});
$('#proCode').validatebox({required: true});
$('#proNumber').validatebox({required: true});
$('#project_serverId').combobox({
	valueField: 'id',    
    textField: 'name',    
    width : 170,
    editable : false,
    panelHeight:'auto',
    panelMaxHeight : 400,
    url: baseUrl+'/server/listAll.do'
});

function pro_refreshValidatebox(){
	$('#proName').validatebox('validate');
	$('#proNameEn').validatebox('validate');
	$('#proCode').validatebox('validate');
	$('#proNumber').validatebox('validate');
}

$('#dgProject').datagrid({
	url: baseUrl+'/project/getListByPage.do', //用户请求数据的URL  
	queryParams:{ 
		'searchKey':$("#txtProjectKey").val()
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
	sortOrder:'desc',
	sortName:'id',
	frozenColumns : [[ 
		{field : 'ck',checkbox : true}, 
		{field : 'id',sortable : true, hidden:true} 
		]],
    columns:[[    
        {field:'proCode',title:'项目编码',width:100},    
        {field:'proName',title:'项目名称',hidden:false, width:100},
        {field:'proNameEn',title:'英文名称',hidden:false, width:100},
        {field:'proNumber',title:'项目编号',width:100},
        {field:'region',title:'项目区域',width:100},
        {field:'address',title:'项目地点',width:100},
        {field:'description',title:'项目描述',width:100},
        {field:'imgUrl',title:'图片',width:100,formatter: function(value,row,index){
        	/* var path=!value?'javascript:void(0);':'http://'+window.location.host+'/resource/'+value;
        	return '<a target="_blank" href="'+path+'"><img src="'+path+'" width="30" height="30"/></a>'; */
        		return !value?'': value;
			}
		},
		{field:'status',title:'状态',width:100,formatter:function(value,row,index){
			return value==0?"启用":value==1?"禁用":"未知";
		}},
        {field:'memo',title:'备注',width:100}
    ]],
    toolbar:[{
    	text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			$('#projectForm').form('clear');//[0].reset();
			$('#projectAdd').dialog({title: "增加",iconCls: 'icon-add'});
			$('#projectAdd').dialog('open');
			pro_refreshValidatebox();
		}
    } ,'-',{
    	text : '修改',
		iconCls : 'icon-edit',
		handler : function() {
			$('#projectAdd').dialog({title: "编辑",iconCls: 'icon-edit'});
			btnEditProject();
			pro_refreshValidatebox();
		}
	},'-',{
    	text : '禁用',
		iconCls : 'icon-remove',
		handler : function() {
	    	btnDelProject();
		}
	}
	],
	onLoadSuccess: function (data) {
		  if (data.total == 0) {
		    var body = $(this).data().datagrid.dc.body2;
		    body.find('table tbody').append(getEmptyTip());
		  }
	},
	onSelect:function(rowIndex, rowData){
		var btnEnable=$('#projectPanel span[class="l-btn-icon icon-remove"]').prev();
		if(rowData.status==1){
			btnEnable.text("启用");
		}else{
			btnEnable.text("禁用");
		}
	},
	onUnselect:function(rowIndex, rowData){
		var rows = $('#dgProject').datagrid("getSelections");
		if(rows.length>0){
			$('#projectPanel span[class="l-btn-icon icon-remove"]').prev().text(rowData.status==1?"启用":"禁用");
		}
	}
});

/* 分页插件初始化 */
var dgProjectPager = $('#dgProject').datagrid('getPager'); 
$(dgProjectPager).pagination({  
	pageSize: 10,//每页显示的记录条数，默认为10  
    pageList: [10,20],//可以设置每页记录条数的列表  
    showPageList: true,  
    showRefresh: true,
    beforePageText: '第',//页数文本框前显示的汉字  
    afterPageText: '页    共 {pages} 页',  
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
}); 

/* 获取数据 */
function btnRefreshProject()
{
	$('#dgProject').datagrid('load', {    
		'searchKey':$("#txtProjectKey").val()
	}); 
}

$(function(){
	bindOption('region','pro_region');
});

/* 修改 */
function btnEditProject()
{
	var rows = $('#dgProject').datagrid("getSelections");    
	if(rows.length==0){
		$.messager.alert('警告','请选择要修改的行','info');
		return;
	}
	//$('#projectForm')[0].reset();
	//setWebControlValueByForm('projectForm',rows[0]);
	$('#projectForm').form('clear');
	$('#projectForm').form('load',rows[0]);
	$('#proImgfile').filebox('setText', rows[0].imgUrl);
	$('#projectAdd').dialog('open');
}

/* 删除 */
function btnDelProject(delId){
	var txtEnabled=$('#projectPanel span[class="l-btn-icon icon-remove"]').prev().text();
	var rows = $('#dgProject').datagrid("getSelections");    
	if(rows.length==0){
		$.messager.alert('警告','请选择要操作的行！','info');
		return;
	}
	var delId= rows[0].id;
	$.messager.confirm('确认','您确认要'+txtEnabled+'当前项目吗？',function(r){    
	    if (r){    
	    	$.ajax({  
	    		type : "post",  
	    	    url : baseUrl+"/project/enabled.do",   
	    	    data : {'id':delId,'status':txtEnabled=="禁用"?1:0},  
	    	    success : function(result) {
	    	    	var json = JSON.parse(result);
	    	    	if(!json.req){
	    	    		$.messager.alert('温馨提示',json.msg,'warning');
	    	    	}else{
	    	    		$.messager.alert('温馨提示',json.msg,'success');
	    	    		var options=$('#dgProject').datagrid('options');
	    	    		btnRefreshProject();
	    	    	}
	    	    }
	    	});	    	
	    }    
	}); 
}

/* 检查图片是否符合规范 */
function checkIsImage(fileName){
	var index= fileName.lastIndexOf('.');
	if(index<0)
		return false;
	var strExtension = fileName.substring(index).toLowerCase();
	if (strExtension != '.jpg' && strExtension != '.gif' && strExtension != '.png') {
	    return false;
	}
	return true;
}


/* 添加或编辑按钮弹出框 */
$('#projectAdd').dialog({  
    title: "添加",  
    width: 600,  
    height: 590,
    iconCls: 'icon-add',                 //弹出框图标  
    modal: true, 
    closed : true,
    buttons:[{
		text:'保存',
		iconCls:'icon-ok',
		handler:function(){
			if(!$('#projectForm').form('validate')){
				return;
			}
			var projectForm= $('#projectForm');
			var projectId=projectForm.find('#id').val();
			var proCode=projectForm.find('#proCode').val();
			var proName=projectForm.find('#proName').val();
			var proNameEn=projectForm.find('#proNameEn').val();
			var proNumber=projectForm.find('#proNumber').val();
			var region=projectForm.find('#region').combobox('getValue');
			var serverId=$('#project_serverId').combobox('getValue');
			var address=projectForm.find('#address').val();
			var description=projectForm.find('#description').val();
			//var imgUrl=projectForm.find('#imgUrl').val();
			var memo=projectForm.find('#memo').val();
			
			var server=$('#project_serverId').combobox("getText");
			if(!server){
				$.messager.alert('警告','请选择服务器！','warning');
				return;
			}
			var fName=$('#proImgfile').val();
			if(fName && !checkIsImage(fName)){
				$.messager.alert('警告','请选择图片文件！(支持类型为:jpg,gif,png)','warning');
				return;
			}

			var colFileId=projectForm.find('input[type="file"]').attr('id');
			$.ajaxFileUpload({  
		        url: baseUrl+"/project/submitForm.do",  
		        secureuri: false, /* 是否需要安全协议，一般设置为false */  
		        fileElementId: colFileId, /* 文件上传域的ID */  
		        dataType: 'json', /* 返回值类型 一般设置为json */  
		        enctype:'multipart/form-data',/* 注意一定要有该参数 */ 
		        data:{'id':projectId,
		        	'proCode':proCode,
		        	'proName':proName,
		        	'proNameEn':proNameEn,
		        	'proNumber':proNumber,
		        	'region':region,
		        	'serverId':serverId,
		        	'address':address,
		        	'description':description,
		        	//'imgUrl':'',//imgUrl,
		        	'memo':memo
		        },
		        success: function (result, status){  
	    	    	if(!result.req){
	    	    		$.messager.alert('温馨提示',result.msg,'warning');
	    	    	}else{
	    	    		$('#id').val('');
	    	    		btnRefreshProject();
		    	    	$.messager.progress('close');
		    	    	$('#projectAdd').dialog('close');
	    	    		$.messager.alert('温馨提示',result.msg,'success');
	    	    	}
		        },
		        error: function (result, status, e){
		        	$.messager.alert('温馨提示','系统错误','error');
		        }  
			});
		}
	},{
		text:'关闭',
		handler:function(){
			$('#projectAdd').dialog('close');
		}
	}]
}); 

</script>
<script type="text/javascript" src="static/bim/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="static/bim/js/common.js"></script> 