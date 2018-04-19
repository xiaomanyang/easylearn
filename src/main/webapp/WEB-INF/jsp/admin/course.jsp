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
</div>

        {field:'image',title:'课程图片',width:100},
        {field:'person',title:'参与人数',width:100},
        {field:'praise',title:'好评值',width:100},


<div id="courseAdd">
<form id="courseForm" class="form-horizontal" method = "post" enctype="multipart/form-data">
	<input id="course_id" name="id" type="hidden">
	<div class="form-group">
		<label class="col-sm-2 control-label" for="courseName">课程名称</label>
		<div class="col-sm-10">
			<input class="form-control" name="courseName" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="courseNo">课程代码</label>
		<div class="col-sm-10">
			<input class="form-control" name="courseNo" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="days">课程天数</label>
		<div class="col-sm-10">
			<input class="form-control" name="days" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="person">参与人数</label>
		<div class="col-sm-10">
			<input class="form-control" name="person" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">图片</label>
		<div class="col-sm-10">
			<input class="form-control" id="courseImgfile" name="courseImgfile"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="praise">好评值</label>
		<div class="col-sm-10">
			<input class="form-control" name="praise" type="text">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="brief">课程简介</label>
		<div class="col-sm-10">
			<textarea rows="4" cols="" class="form-control"  name="brief"></textarea>
		</div>
	</div>
</form>
</div>



<script type="text/javascript">
console.info('course');
var baseUrl = '${pageContext.request.contextPath}';

$('#selectClassification').combobox({
	onChange : function(value){
		btnRefreshcourse();
	}
});
$('#courseImgfile').filebox({buttonText: '选择图片', width:'387px'});
$('#dt_course').datagrid({
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
    	{field:'courseName',title:'课程名称',hidden:false, width:100},
        {field:'courseNo',title:'课程代码',width:100},    
        {field:'brief',title:'课程简介',width:100},
        {field:'days',title:'课程天数',width:100},
        {field:'image',title:'课程图片',width:100},
        {field:'person',title:'参与人数',width:100},
        {field:'praise',title:'好评值',width:100},
        {field:'createTime',title:'创建时间',width:100,formatter: function(value,row,index){
        		return formatterdate(value);
			}
		}
    ]],
    toolbar:[{
    	text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			btnAddcourse();
		}
    } ,'-',{
    	text : '修改',
		iconCls : 'icon-edit',
		handler : function() {
			btnEditcourse();
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
	}
});


/* 获取数据 */
function btnRefreshcourse()
{
	$('#dt_course').datagrid({  
		url: baseUrl+'/course/list.do',
		queryParams: {classId : $('#selectClassification').combobox('getValue')}
	});
}

/* 增加 */
function btnAddcourse(){
	$('#courseForm').form('clear');
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

/* 添加或编辑 */
$('#courseAdd').dialog({  
    title: "增加",  
    width: 500,  
    height: 500,
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
			$('#courseForm').form('submit',{
				url : baseUrl+"/course/add.do",  
				success:function(data){    
					data = $.parseJSON(data);
					if(data.req){
						btnRefreshcourse();
						$('#courseAdd').dialog('close');
	    	    		$.messager.alert('温馨提示',json.msg,'success');
					}else{
						$.messager.alert('温馨提示',json.msg,'error');
					}
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