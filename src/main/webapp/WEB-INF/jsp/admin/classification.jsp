<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
    	<table id="dt_classification"></table>
    </div>
</div>

<div id="classificationAdd">
	<form id="classificationForm" class="form-horizontal">
		<input id="classification_id" name="id" type="hidden">
		<div class="form-group">
			<label class="col-sm-2 control-label" for="parentCode">分类名称</label>
			<div class="col-sm-10">
				<input class="form-control" name="classificationName" type="text">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="parentCode">备注</label>
			<div class="col-sm-10">
				<input class="form-control" name="memo" type="text">
			</div>
		</div>
	</form>
</div>



<script type="text/javascript">
var baseUrl = '${pageContext.request.contextPath}';

$('#dt_classification').datagrid({
	url: baseUrl+'/classification/list.do',
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
    	{field:'classificationName',title:'分类名称',hidden:false, width:100},
        {field:'memo',title:'备注',width:100}
    ]],
    toolbar:[{
    	text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			$('#classificationForm').form('clear');
			$('#classificationAdd').dialog({title: "增加",iconCls: 'icon-add'});
			$('#classificationAdd').dialog('open');
		}
    },'-',{
    	text : '删除',
		iconCls : 'icon-remove',
		handler : function() {
			var rowData = $('#dt_classification').datagrid('getSelections');
			if(rowData && 1 == rowData.length){
				console.info(rowData[0].id);
				$.ajax({  
					type : "post",  
		    	    url : baseUrl + "/classification/delete.do",  
		    	    data : {id : rowData[0].id},  
		    	    dataType : 'json',
		    	    success : function(data) {  
		    	    	if(data.req){
		    	    		$('#dt_classification').datagrid('reload');
		    	    		$.messager.alert('提示',data.msg, 'info');
		    	    	}
		    	    }
		    	});
			}else{
				$.messager.alert('提示','请选择一行删除', 'info');
			}
		}
	}],
	onLoadSuccess: function (data) {
		  if (data.total == 0) {
		    var body = $(this).data().datagrid.dc.body2;
		    body.find('table tbody').append(getEmptyTip()); 
		  }
	}
});

/* 添加或编辑按钮弹出框 */
$('#classificationAdd').dialog({  
    title: "增加",  
    width: 400,  
    height: 200,
    collapsible:true,
    maximizable:true,
    iconCls: 'icon-add',
    modal: true, 
    closed : true,
    buttons:[{
		text:'保存',
		iconCls:'icon-ok',
		handler:function(){
			$('#classificationForm').form('submit',{
				url : baseUrl+'/classification/add.do',
				success: function(data){
					data = $.parseJSON(data);
					if(data.req){
						$('#dt_classification').datagrid('reload');
						$('#classificationAdd').dialog('close');
					}
				}
			});
		}
	},{
		text:'关闭',
		handler:function(){
			$('#classificationAdd').dialog('close');
		}
	}]
}); 

</script>