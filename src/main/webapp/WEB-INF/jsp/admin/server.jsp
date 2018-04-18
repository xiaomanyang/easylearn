<%@ page language="java" pageEncoding="UTF-8"%>

<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
		<table id="tb_server"></table>
    </div>
</div>

<div id="serverAdd" style="overflow:hidden;">
	<form id="serverAddForm" class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-3 control-label">服务器名称</label>
			<div class="col-sm-9">
				<input class="form-control" id="server_name" name="name" type="text">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label">访问地址</label>
			<div class="col-sm-9">
				<input class="form-control" id="server_address" name="address" type="text">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label">IP地址</label>
			<div class="col-sm-9">
				<input class="form-control" id="server_ip" name="ip" type="text">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label">端口号</label>
			<div class="col-sm-9">
				<input class="form-control" id="server_port" name="port" type="text">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label">服务器说明</label>
			<div class="col-sm-9">
				<input class="form-control" id="server_memo" name="memo" type="text">
			</div>
		</div>
		<input id="server_id" name="id" style="display: none;" />
	</form>
</div>

<script type="text/javascript">
console.info("server");

var baseUrl = '${pageContext.request.contextPath}';

$('#server_name').textbox({width: 308,required:true});
$('#server_address').textbox({width: 308,required:true});
$('#server_ip').textbox({width: 308});
$('#server_port').numberbox({width: 308});
$('#server_memo').textbox({width: 308});

$('#serverAddForm').form({
	iframe : false
});
$('#tb_server').datagrid({
	url: baseUrl+'/server/list.do',
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
        {field:'name',title:'服务器名称',width:100},    
        {field:'address',title:'访问地址',width:100},
        {field:'ip',title:'IP',width:100},
        {field:'port',title:'端口号',width:100},    
        {field:'memo',title:'说明',width:100}
    ]],
    toolbar:[{
    	text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			$('#serverAddForm').form('clear');
			$('#serverAdd').dialog('open');
		}
    } ,'-',
    {
    	text : '编辑',
		iconCls : 'icon-edit',
		handler : function() {
			var rowData = $('#tb_server').datagrid('getSelections');
			if(rowData && 1 == rowData.length){
				// 获得正在编辑的行
				$('#serverAddForm').form('clear');
				$('#serverAddForm').form('load',rowData[0]);
				$('#serverAdd').dialog('open');
			}else{
				$.messager.alert('提示','请选择一行进行编辑', 'info');
			}
		}
    } ,'-',{
    	text : '删除',
		iconCls : 'icon-remove',
		handler : function() {
	    	var rows = $('#tb_server').datagrid("getSelections");    
	    	if(rows.length==0){
	    		$.messager.alert('警告','请选择要删除的行', 'info');
	    		return;
	    	}
			$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
			    if (r){    
					$.ajax({  
						type : "post",  
			    	    url : baseUrl + "/server/delete.do",  
			    	    dataType : 'json',
			    	    data : {id : rows[0].id},  
			    	    success : function(items) {  
			    	    	console.info(items);
			    	    	if(!items.req){
			    	    		$.messager.alert('警告',items.msg, 'info');
			    	    	}else
			    	    		$('#tb_server').datagrid('reload');
			    	    }
			    	});
			    }    
			}); 
		}
	}],
	onLoadSuccess: function (data) {
	  if (data.total == 0) {
	    var body = $(this).data().datagrid.dc.body2;
	    body.find('table tbody').append(getEmptyTip()); 
	  }
	}
});

$('#serverAdd').dialog({  
    title: "服务器管理",  
    width: 470,  
    height: 330,
    collapsible:true,
    maximizable:true,
    iconCls: 'icon-add',                 //弹出框图标  
    modal: true, 
    closed : true,
    buttons:[{
		text:'保存',
		iconCls:'icon-ok',
		handler:function(){
			console.info($('#serverAddForm').serialize());
			if($('#serverAddForm').form('validate')){
				$('#serverAddForm').form('submit',{
					url: baseUrl + '/server/save.do',
					success: function (data, status)  //服务器成功响应处理函数  
			        {  
						data = $.parseJSON(data);
						$('#tb_server').datagrid('reload');
						$('#serverAdd').dialog('close');
						$.messager.alert('提示',data.msg, 'info',function(){
							if(!data.req)
								$('#serverAdd').dialog('open');
						});
			        }
				});
			}
		}
		}, {
			text : '关闭',
			handler : function() {
				$('#serverAdd').dialog('close');
			}
		} ]
	});
	
</script>
