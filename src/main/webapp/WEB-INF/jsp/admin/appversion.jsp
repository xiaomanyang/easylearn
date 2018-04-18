<%@ page language="java" pageEncoding="UTF-8"%>

<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
		<table id="tb_app_version"></table>
    </div>
</div>

<div id="appAdd" style="overflow:hidden;">
	<form id="appAddForm" class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-3 control-label">IOS版本</label>
			<div class="col-sm-9">
				<input class="form-control" id="app_iosVersion" name="iosVersion" type="text">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label">IOS说明</label>
			<div class="col-sm-9">
				<input class="form-control" id="app_iosNote" name="iosNote" type="text">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label">IOS地址</label>
			<div class="col-sm-9">
				<input class="form-control" id="app_iosAddress" name="iosAddress" type="text">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label">Android版本</label>
			<div class="col-sm-9">
				<input class="form-control" id="app_androidVersion" name="androidVersion" type="text">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label">Android说明</label>
			<div class="col-sm-9">
				<input class="form-control" id="app_androidNote" name="androidNote" type="text">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label">Android apk</label>
			<div class="col-sm-9">
				<input class="form-control" id="app_file" name="file" />
			</div>
		</div>
		<input id="app_id" name="id" style="display: none;" />
	</form>
</div>

<script type="text/javascript">
console.info("appversion");

var baseUrl = '${pageContext.request.contextPath}';

$('#app_iosVersion').textbox({width: 308,required:true});
$('#app_iosNote').textbox({width: 308,required:true});
$('#app_iosAddress').textbox({width: 308,required:true});
$('#app_androidVersion').textbox({width: 308,required:true});
$('#app_androidNote').textbox({width: 308,required:true});
$('#app_file').filebox({width: 308, buttonText: '选择文件'});

$('#appAddForm').form({
	iframe : false
});
$('#tb_app_version').datagrid({
	url: baseUrl+'/app/list.do',
	fit : true,
	border : false,
	fitColumns : true,
	rownumbers : true,
	striped : true,
	singleSelect : true,
	pagination : true,
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
	} ] ],
    columns:[[    
        {field:'iosVersion',title:'IOS版本',width:100},    
        {field:'iosNote',title:'IOS说明',width:100},
        {field:'iosAddress',title:'IOS地址',width:100},
        {field:'androidVersion',title:'Android版本',width:100},    
        {field:'androidNote',title:'Android说明',width:100},
        {field:'androidAddress',title:'Android地址',width:100}
    ]],
    toolbar:[
    	/* {
    	text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			$('#appAddForm').form('clear');
			$('#appAdd').dialog('open');
		}
    } ,'-', */
    {
    	text : '编辑',
		iconCls : 'icon-edit',
		handler : function() {
			var rowData = $('#tb_app_version').datagrid('getSelections');
			if(rowData && 1 == rowData.length){
				// 获得正在编辑的行
				$('#appAddForm').form('clear');
				$('#appAddForm').form('load',rowData[0]);
				$('#app_file').filebox('setText', rowData[0].androidAddress);
				$('#appAdd').dialog('open');
			}else{
				$.messager.alert('提示','请选择一行进行编辑', 'info');
			}
		}
    } 
    /* ,'-',{
    	text : '删除',
		iconCls : 'icon-remove',
		handler : function() {
	    	var rows = $('#tb_app_version').datagrid("getSelections");    
	    	if(rows.length==0){
	    		$.messager.alert('警告','请选择要删除的行', 'info');
	    		return;
	    	}
			$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
			    if (r){    
					$.ajax({  
						type : "post",  
			    	    url : baseUrl + "/app/delete.do",  
			    	    data : {id : rows[0].id},  
			    	    success : function(items) {  
			    	    	$('#tb_app_version').datagrid('reload');
			    	    }
			    	});
			    }    
			}); 
		}
	} */
    ],
	onLoadSuccess: function (data) {
	  if (data.total == 0) {
	    var body = $(this).data().datagrid.dc.body2;
	    body.find('table tbody').append(getEmptyTip()); 
	  }
	  $(this).datagrid('selectRow',0);
	}
});

$('#appAdd').dialog({  
    title: "APP版本信息",  
    width: 470,  
    height: 420,
    collapsible:true,
    maximizable:true,
    iconCls: 'icon-add',                 //弹出框图标  
    modal: true, 
    closed : true,
    buttons:[{
		text:'保存',
		iconCls:'icon-ok',
		handler:function(){
			console.info($('#appAddForm').serialize());
			if($('#appAddForm').form('validate')){
				$('#appAddForm').form('submit',{
					url: baseUrl + '/app/save.do',
					success: function (data, status)  //服务器成功响应处理函数  
			        {  
						data = $.parseJSON(data);
						$('#tb_app_version').datagrid('reload');
						$('#appAdd').dialog('close');
						$.messager.alert('提示',data.msg, 'info',function(){
							if(!data.req)
								$('#appAdd').dialog('open');
						});
			        }
				});
			}
		}
		}, {
			text : '关闭',
			handler : function() {
				$('#appAdd').dialog('close');
			}
		} ]
	});
	
</script>
