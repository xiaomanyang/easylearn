	var projectProfessionData;
	var floorData;
	
	/* 项目选址下拉框 */
	$('#cb_model_projectid').combobox({    
	    url:'project/list.htmls', 
	    methord: 'post',
	    valueField:'id',    
	    textField:'projectname',
	    panelHeight: 'auto',
	    panelWidth: '200px',
	    onSelect : function(record){
	    	$.ajax({ // 获得模型树
	    		url: 'modeltree/list.htmls',
				data: {pid:record.id},
				type: "POST",
				dataType: 'json',
				success: function(result){
					$('#ttb_model_modeltree').treegrid('unselectAll');
					$('#ttb_model_modeltree').treegrid('loadData',{total:0,rows:[]}); 
					$('#ttb_model_modeltree').treegrid('loadData',result);
				}
	    	});
	    	$.ajax({ //获得项目专业s列表
	    		url: 'profession/singleProfessionList.htmls',
				data: {id:record.id},
				type: "POST",
				dataType: 'json',
				success: function(result){
					projectProfessionData = result;
				}
	    	});
	    },
	    onLoadSuccess: function () { //加载完成后,设置选中第一项
	    	var val = $(this).combobox("getData");
	        for (var item in val[0]) {
	            if (item == "id") {
	                $(this).combobox("select", val[0][item]);
	            }
	        }
	    }
	});
	
	/* 模型树树形表 */
	$('#ttb_model_modeltree').treegrid({
		idField:'id',    
	    treeField:'nodeName',
	    fit : true,
	    border : false,
		fitColumns : true,
		rownumbers : true,
		singleSelect : true,
		striped : true,
		queryParams: {
			pid: $('#cb_model_projectid').combobox()
		},
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'id',
			sortable : false,
			hidden : true,
			title : 'id'
		} ] ],
	    columns:[[    
	        {field:'nodeName',title:'模型树节点',width:180, editor:{type:'text'}},
	        {field: 'pid',hidden : true}
	    ]],
		onDblClickRow:function(rowIndex, rowData){
			$('#ttb_model_modeltree').treegrid('unselectAll'); 
		},
		onSelect:function(node){
			var tid = node.id;
			$.ajax({ //获得楼层列表
				url: 'floor/list.htmls',
				data: {tid:tid},
				type: "POST",
				dataType: 'json',
				success: function(result){
					floorData = result;
					$('#tb_model_floor').datagrid('loadData',{total:0,rows:[]});
					$('#tb_model_floor').datagrid('loadData',result);
				}
			});
			$.ajax({ //获得模型列表
	    		url: 'getBimModelFileList.htmls',
				data: {modelTreeId:tid},
				type: "POST",
				dataType: 'json',
				success: function(result){
					$('#tb_model').datagrid('loadData',{total:0,rows:[]});
					$('#tb_model').datagrid('loadData',result.obj);
				}
	    	});
		},
		onUnselectAll : function(){
			$('#tb_model_floor').datagrid('loadData',{total:0,rows:[]});
			$('#tb_model').datagrid('loadData',{total:0,rows:[]});
		},
		toolbar : [{
			text : '增加',
			iconCls : 'icon-add',
			handler : function() {
				// 获得正在编辑的行
				var editingRowsIndexs = $('#ttb_model_modeltree').treegrid('getEditingRowIndexs');
				// 如果没有正在编辑的行才插入行
				if(0 >= editingRowsIndexs.length){
					var node = $('#ttb_model_modeltree').treegrid('getSelected');
					/* 模型树不是树形结构就让pid=null
					var pid = null == node ? null : node.id; */
					var pid = null;
					
					var row = $('#ttb_model_modeltree').treegrid('append',{
						parent : pid,
						data:[{ id : '-1', nodeName: '', pid : pid }]
					});
					$('#ttb_model_modeltree').treegrid('beginEdit',-1);
				}
			}
		},'-',{
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				// 获得正在编辑的行
				var editingRowsIndexs = $('#ttb_model_modeltree').treegrid('getEditingRowIndexs');
				// 如果有正在编辑的行才插入行
				if(0 < editingRowsIndexs.length){
					$('#ttb_model_modeltree').treegrid('select',-1);
					$('#ttb_model_modeltree').treegrid('endEdit',-1);
					var rowData = $('#ttb_model_modeltree').treegrid('getSelected');
					$.ajax({
						url: 'modeltree/add.htmls',
						data: {
							nodeName : rowData.nodeName,
							// pid : rowData.pid, //如果模型树是棵树，就设置pid
							projectId : $('#cb_model_projectid').combobox('getValue')
						},
						type: "POST",
						dataType: 'json',
						success: function(result){
							if(result.success){
								$.ajax({
						    		url: 'modeltree/list.htmls',
									data: {pid:$('#cb_model_projectid').combobox('getValue')},
									type: "POST",
									dataType: 'json',
									success: function(result){
										$('#ttb_model_modeltree').treegrid('loadData',{total:0,rows:[]}); 
										$('#ttb_model_modeltree').treegrid('loadData',result);
									}
						    	});
								$('#ttb_model_modeltree').treegrid('unselectAll');
								$.messager.alert('提示','模型树节点增加成功');
							}else{
								$.messager.alert('提示',result.msg,'error');
								$('#ttb_model_modeltree').treegrid('beginEdit',-1);
							}
						}
					});
					
				}
			}
		},'-',{
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var rowData = $('#ttb_model_modeltree').treegrid('getSelected');
				if(rowData)
					$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
					    if (r){    
					    	$.ajax({
								url: 'modeltree/delete.htmls',
								data: rowData,
								type: "POST",
								dataType: 'json',
								success: function(result){
									if(result.success){
										$.ajax({
								    		url: 'modeltree/list.htmls',
											data: {pid:$('#cb_model_projectid').combobox('getValue')},
											type: "POST",
											dataType: 'json',
											success: function(result){
												$('#ttb_model_modeltree').treegrid('loadData',{total:0,rows:[]}); 
												$('#ttb_model_modeltree').treegrid('loadData',result);
											}
								    	});
										$('#ttb_model_modeltree').treegrid('unselectAll');
										//$.messager.alert('提示','模型节点 删除成功');
										$.messager.show({
											title:'提示',
											msg:'模型节点 删除成功',
											timeout:1000,
											showType:'slide'
										});

									}else
										$.messager.alert('提示',result.msg);
								}
							});   
					    }    
					});
				else
					$.messager.alert('提示','没有选择要删除的模型节点');
			}
		},'-',{
			text : '刷新',
			iconCls : 'icon-reload',
			handler : function() {
				$.ajax({
		    		url: 'modeltree/list.htmls',
					data: {pid:$('#cb_model_projectid').combobox('getValue')},
					type: "POST",
					dataType: 'json',
					success: function(result){
						$('#ttb_model_modeltree').treegrid('unselectAll');
						$('#ttb_model_modeltree').treegrid('loadData',{total:0,rows:[]}); 
						$('#ttb_model_modeltree').treegrid('loadData',result);
					}
		    	});
			}
		},'-',{
			text : '设置',
			iconCls : 'icon-ok',
			handler : function() {
				// 获得正在编辑的行
				var editingRowsIndexs = $('#tb_model').datagrid('getEditingRowIndexs');
				// 如果有正在编辑的行不能再编辑其他行
				if(0 >= editingRowsIndexs.length){
					var modelTree = $('#ttb_model_modeltree').treegrid('getSelected');
					if(null == modelTree){
						$.messager.alert('警告','没有选择模型树节点'); 
						return ;
					}
					
					$('#ttb_model_modeltree_setting').form('clear');
					$('#ttb_model_modeltree_setting').dialog('open');
					$('#ttb_model_modeltree_setting').panel({title: "模型树节点设置"});
					
					$('#click_from_sign').val('modelTree');
					$('#ttb_model_modeltree_pf').combobox({    
						data : projectProfessionData,
						multiple:true,
						valueField:'id',    
					    textField:'name',
					    panelHeight : 'auto',
					    onLoadSuccess : function() {
					    	var modelTreeId = 0;
				    		if($('#click_from_sign').val() == 'project'){
				    			modelTreeId = 0;
				    		}
				    		else{
				    			var getSelected_model = $('#ttb_model_modeltree').treegrid('getSelected');
					    		if(getSelected_model){
					    			modelTreeId = getSelected_model.id;
					    		}
				    		}
				    		
				    		var projectId = $('#cb_model_projectid').combobox('getValue');

				    		var data = {
				    				modelTreeId : modelTreeId,
				    				projectId:projectId
				    		};
				    		
					    	$.ajax({ //获得模型列表
					    		url: 'model/getInitSetting.htmls',
					    		data: data,
								type: "POST",
								dataType: 'json',
								success: function(result){
									//console.log(result.data.professionIds);
									$('#ttb_model_modeltree_pf').combobox('setValues',result.data.professionIds.split(","));
								}
					    	});
						}
					});
				}
			}
		}
		]
	});
	
	/* 楼层表 */
	$('#tb_model_floor').datagrid({
		fit : true,
		border : false,
		fitColumns : true,
		rownumbers : true,
		singleSelect : true,
		striped : true,
		loadMsg : '数据加载中...',
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'id',
			hidden : false,
			title : 'id'
		} ] ],
	    columns:[[    
	        {field:'name',title:'楼层名',width:100,editor:{type:'text'}},    
	        {field:'number',title:'楼层编号', width:100,editor:{type:'numberbox'}},
	        {field:'floorHeight',title:'楼层高度',width:100,editor:{type:'numberbox',options:{precision:2}}},
	        {field:'modelTreeId',hidden:true},
	        {field:'planId',hidden:true}
	  	]],
	  	onSelect : function(index,data){
	  		console.info(data);
	  		$.ajax({ //查询楼层专业集合
				url: 'floorprofession/list.htmls',
				data: {fid : data.id},
				type: "POST",
				dataType: 'json',
				success: function(result){
					$('#tb_model_floor_profession').datagrid('loadData',{total:0,rows:[]});
					$('#tb_model_floor_profession').datagrid('loadData',result);
				}
			});
	  		$.ajax({ //查询楼层平面图
				url: 'getBimPlanById.htmls',
				data: {floorId : data.id},
				type: "POST",
				dataType: 'json',
				success: function(result){
					$('#fm_model_plan').attr('hidden',false);
					$('#fm_model_plan').form('clear');
					$('#fm_model_plan').form('load',result.obj);
					// 启用只读模式
					$('#p_plan_name').textbox('readonly');
					$('#p_plan_path').textbox('readonly');
					$('#p_plan_width').numberbox('readonly');
					$('#p_plan_height').numberbox('readonly');
					$('#p_plan_offsetx').numberbox('readonly');
					$('#p_plan_offsety').numberbox('readonly');
				}
			});
	  		$('#form_file_proper').attr('hidden',true);
	  	},
	  	onUnselectAll : function(){
	  		$('#fm_model_plan').attr('hidden',true);
	  	},
	    toolbar:[{
	    	text : '增加',
			iconCls : 'icon-add',
			handler : function() {
				var rowData = $('#ttb_model_modeltree').treegrid('getSelected');				
				if(rowData){
					// 获得正在编辑的行
					var editingRowsIndexs = $('#tb_model_floor').datagrid('getEditingRowIndexs');
					// 如果没有正在编辑的行才插入行
					if(0 >= editingRowsIndexs.length){
						$('#tb_model_floor').datagrid('insertRow',{index:0,row:{name:'', number:'',floorHeight:'',modelTreeId:rowData.id }});
						$('#tb_model_floor').datagrid('beginEdit',0);
					}
				}else{
					$.messager.alert('提示','没有选择模型树节点');
				}
			}
	    },'-',{
	    	text : '编辑',
			iconCls : 'icon-edit',
			handler : function(){
				var rowData = $('#tb_model_floor').datagrid('getSelected');
				var rowDataIndex = $('#tb_model_floor').datagrid('getRowIndex',rowData);
				console.info(rowData);
				if(rowData){
					// 获得正在编辑的行
					var editingRowsIndexs = $('#tb_model_floor').datagrid('getEditingRowIndexs');
					// 如果有正在编辑的行不能再编辑其他行
					if(0 < editingRowsIndexs.length){
						$.messager.alert('提示','有正在编辑的数据');
						$('#tb_model_floor').datagrid('selectRow',editingRowsIndexs[0]);
						return ;
					}else{
						$('#tb_model_floor').datagrid('beginEdit',rowDataIndex);
					}
				}else
					$.messager.alert('提示','没有选择楼层');
			}
	    },'-',{
	    	text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				// 获得正在编辑的行
				var editingRowsIndexs = $('#tb_model_floor').datagrid('getEditingRowIndexs');
				// 如果有正在编辑的行才执行保存
				if(0 < editingRowsIndexs.length){
					$('#tb_model_floor').datagrid('selectRow',editingRowsIndexs[0])
					$('#tb_model_floor').datagrid('endEdit',editingRowsIndexs[0])
					var rowData = $('#tb_model_floor').datagrid('getSelected');
					$.ajax({
						url: 'floor/add.htmls',
						data: rowData,
						type: "POST",
						dataType: 'json',
						success: function(result){
							if(result.success){
								var tid = $('#ttb_model_modeltree').treegrid('getSelected').id;
								$.ajax({
									url: 'floor/list.htmls',
									data: {tid:tid},
									type: "POST",
									dataType: 'json',
									success: function(result){
										$('#tb_model_floor').datagrid('loadData',{total:0,rows:[]});
										$('#tb_model_floor').datagrid('loadData',result);
									}
								});
								$.messager.alert('提示','楼层增加成功');
							}else{
								$.messager.alert('提示',result.msg,'error');
								$('#tb_model_floor').datagrid('beginEdit',0)
							}
						}
					});
				}else{
					$.messager.alert('提示','没有增加信息');
				}
			}
	    },'-',{
	    	text : '删除',
	    	iconCls : 'icon-remove',
	    	handler : function(){
	    		var rowData = $('#tb_model_floor').datagrid('getSelected');
				if(rowData)
					$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
					    if (r){    
					    	$.ajax({
								url: 'floor/delete.htmls',
								data: rowData,
								type: "POST",
								dataType: 'json',
								success: function(result){
									if(result.success){
										var treeNode = $('#ttb_model_modeltree').treegrid('getSelected');
										if(treeNode){
											var tid = treeNode.id;
											$.ajax({
												url: 'floor/list.htmls',
												data: {tid:tid},
												type: "POST",
												dataType: 'json',
												success: function(result){
													$('#tb_model_floor').datagrid('loadData',{total:0,rows:[]});
													$('#tb_model_floor').datagrid('loadData',result);
												}
											});
										}
										$.messager.show({
											title:'提示',
											msg:'楼层 删除成功',
											timeout:1000,
											showType:'slide'
										});

									}else
										$.messager.alert('提示',result.msg);
								}
							});   
					    }    
					});
				else
					$.messager.alert('提示','没有选择要删除的楼层信息');
	    	}
	    },'-',{
	    	text : '刷新',
			iconCls : 'icon-reload',
			handler : function() {
				$('#tb_model_floor').datagrid('unselectAll');
				var treeNode = $('#ttb_model_modeltree').treegrid('getSelected');
				if(treeNode){
					var tid = treeNode.id;
					$.ajax({
						url: 'floor/list.htmls',
						data: {tid:tid},
						type: "POST",
						dataType: 'json',
						success: function(result){
							$('#tb_model_floor').datagrid('loadData',{total:0,rows:[]});
							$('#tb_model_floor').datagrid('loadData',result);
						}
					});
				}
			}
	    }]
	});
	
	/* 楼层专业表 */
	$('#tb_model_floor_profession').datagrid({
		fit : true,
		border : false,
		fitColumns : true,
		rownumbers : true,
		singleSelect : true,
		striped : true,
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
	        {field:'name',title:'楼层专业节点名称',width:100,editor:{type:'text'}},    
	        {field:'floorId',title:'楼层', width:100,formatter:function(value){return $('#tb_model_floor').datagrid('getSelected').name}},
	        {field:'professionId',title:'专业',width:100,formatter:function(value){
	        	for(var i = 0; i<projectProfessionData.length; i++){
	        		if(projectProfessionData[i].id == value)
	        			return projectProfessionData[i].name;
	        	}
	        	return value;
	        },editor:{type:'combobox',options:{
	        	valueField:'id',    
	            textField:'name',
	            panelHeight : 'auto',
	            onShowPanel : function(){
	            	$(this).combobox('loadData',projectProfessionData);
	            }
	        }}}
	  	]],
	  	toolbar:[{
	  		text : '增加',
			iconCls : 'icon-add',
			handler : function() {
				console.info(projectProfessionData);
				var rowData = $('#tb_model_floor').datagrid('getSelected');				
				if(rowData){
					// 获得正在编辑的行
					var editingRowsIndexs = $('#tb_model_floor_profession').datagrid('getEditingRowIndexs');
					// 如果没有正在编辑的行才插入行
					if(0 >= editingRowsIndexs.length){
						$('#tb_model_floor_profession').datagrid('insertRow',{index:0,row:{name:$('#tb_model_floor').datagrid('getSelected').name+'()', floorId:$('#tb_model_floor').datagrid('getSelected').id,professionId:'' }});
						$('#tb_model_floor_profession').datagrid('beginEdit',0);
					}
				}else{
					$.messager.alert('提示','没有选择楼层');
				}
			}
	  	},'-',{
	  		text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				// 获得正在编辑的行
				var editingRowsIndexs = $('#tb_model_floor_profession').datagrid('getEditingRowIndexs');
				// 如果有正在编辑的行才执行保存
				if(0 < editingRowsIndexs.length){
					$('#tb_model_floor_profession').datagrid('selectRow',0)
					$('#tb_model_floor_profession').datagrid('endEdit',0)
					var rowData = $('#tb_model_floor_profession').datagrid('getSelected');
					$.ajax({
						url: 'floorprofession/add.htmls',
						data: rowData,
						type: "POST",
						dataType: 'json',
						success: function(result){
							if(result.success){
								// 刷新楼层-专业列表
								var floorRow = $('#tb_model_floor').datagrid('getSelected');
								if(floorRow)
									$.ajax({
										url: 'floorprofession/list.htmls',
										data: {fid : floorRow.id},
										type: "POST",
										dataType: 'json',
										success: function(result){
											$('#tb_model_floor_profession').datagrid('loadData',{total:0,rows:[]});
											$('#tb_model_floor_profession').datagrid('loadData',result);
										}
									});
								$.messager.alert('提示','楼层-专业信息增加成功');
							}else{
								$.messager.alert('提示',result.msg,'error');
								$('#tb_model_floor_profession').datagrid('beginEdit',0)
							}
						}
					});
				}else{
					$.messager.alert('提示','没有增加信息');
				}
			}
	  	},'-',{
	  		text : '删除',
	    	iconCls : 'icon-remove',
	    	handler : function(){
	    		var rowData = $('#tb_model_floor_profession').datagrid('getSelected');
				if(rowData)
					$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
					    if (r){    
					    	$.ajax({
								url: 'floorprofession/delete.htmls',
								data: rowData,
								type: "POST",
								dataType: 'json',
								success: function(result){
									if(result.success){
										/* 刷新 start */
										var floorRow = $('#tb_model_floor').datagrid('getSelected');
										if(floorRow)
											$.ajax({
												url: 'floorprofession/list.htmls',
												data: {fid : floorRow.id},
												type: "POST",
												dataType: 'json',
												success: function(result){
													$('#tb_model_floor_profession').datagrid('loadData',{total:0,rows:[]});
													$('#tb_model_floor_profession').datagrid('loadData',result);
												}
											});
										/* 刷新 end */
										$.messager.show({
											title:'提示',
											msg:'楼层-专业信息 删除成功',
											timeout:1000,
											showType:'slide'
										});

									}else
										$.messager.alert('提示',result.msg);
								}
							});   
					    }    
					});
				else
					$.messager.alert('提示','没有选择要删除的楼层专业信息');
	    	}
	  	},'-',{
	    	text : '刷新',
			iconCls : 'icon-reload',
			handler : function() {
				var floorRow = $('#tb_model_floor').datagrid('getSelected');
				if(floorRow)
					$.ajax({
						url: 'floorprofession/list.htmls',
						data: {fid : floorRow.id},
						type: "POST",
						dataType: 'json',
						success: function(result){
							$('#tb_model_floor_profession').datagrid('loadData',{total:0,rows:[]});
							$('#tb_model_floor_profession').datagrid('loadData',result);
						}
					});
			}
	  	}]
	});
	
	// 平面图编辑
	$('#btn_plan_eidt').linkbutton({    
	    iconCls: 'icon-edit',
	    onClick : function(){
	    	// 启用编辑模式
			$('#p_plan_width').numberbox('readonly',false);
			$('#p_plan_height').numberbox('readonly',false);
			$('#p_plan_offsetx').numberbox('readonly',false);
			$('#p_plan_offsety').numberbox('readonly',false);	
			$('#form_file_proper').attr('hidden',false);
	    }
	});  
	
	// 平面图删除
	$('#btn_plan_delete').linkbutton({    
	    iconCls: 'icon-remove',
	    onClick : function(){
	    	var planId = $('#p_plan_id').val();
	    	if(null != planId && ''!=planId){
	    		$.ajax({ // 删除平面图
		    		url: 'plan/delete.htmls',
					data: {id:planId},
					type: "POST",
					dataType: 'json',
					success: function(result){
						if(result.success){
							$('#tb_model_floor').datagrid('unselectAll');
						}
					}
		    	});
	    	}else{
	    		
	    	}
	    }
	});  
	
	// 平面图保存
	$('#btn_plan_save').linkbutton({    
	    iconCls: 'icon-save',
	    onClick : function(){
	    	//上传文件
	    	var data = {
	    			'width':$('#p_plan_width').numberbox('getValue'),
	    			'height':$('#p_plan_height').numberbox('getValue'),
	    			'offsetx':$('#p_plan_offsetx').numberbox('getValue'),
	    			'offsety':$('#p_plan_offsety').numberbox('getValue'),
	    			'id' : $('#p_plan_id').val(),
	    			'floorId' : $('#tb_model_floor').datagrid('getSelected').id,
	    			'projectId' : $('#cb_model_projectid').combobox('getValue')
	    	};
            $.ajaxFileUpload({  
		        url: 'plan/add.htmls', //服务器端请求地址  
		        secureuri: false, //是否需要安全协议，一般设置为false  
		        fileElementId: 'p_plan_img', //文件上传域的ID  
		        dataType: 'json', //返回值类型 一般设置为json  
		        enctype:'multipart/form-data',//注意一定要有该参数 
		        data : data, 
		        success: function (data, status){  
		        	//data是从服务器返回来的值 
		        },  
		        error: function (data, status, e){  
		        	data = $.parseJSON(data.responseText);
		        	if(data.success){
		        		var selectedFloorRow = $('#tb_model_floor').datagrid('getSelected');
		        		var selectedFloorRowIndex = $('#tb_model_floor').datagrid('getRowIndex',selectedFloorRow);
		        		$('#tb_model_floor').datagrid('unselectAll');
						var treeNode = $('#ttb_model_modeltree').treegrid('getSelected');
						if(treeNode){
							var tid = treeNode.id;
							$.ajax({
								url: 'floor/list.htmls',
								data: {tid:tid},
								type: "POST",
								dataType: 'json',
								success: function(result){
									$('#tb_model_floor').datagrid('loadData',{total:0,rows:[]});
									$('#tb_model_floor').datagrid('loadData',result);
									$('#tb_model_floor').datagrid('selectRow',selectedFloorRowIndex);
								}
							});
						}
		        	}
		        }  
			});
	    }
	});  

	/* 模型管理区域 */
	$('#tb_model').datagrid({
		fit : true,
		border : false,
		fitColumns : false,
		rownumbers : true,
		singleSelect : true,
		striped : true,
		loadMsg : '数据加载中...',
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'id', sortable : true, hidden : true
		},{field:'modelname',title:'模型名',width:180}] ],
	    columns:[[    
	        {field:'filepath',title:'模型路径', width:200},
	        {field:'modelOrder',title:'模型顺序',width:60,align:'center',editor:{type:'numberbox'}} ,
	        {field:'startFloor',title:'起始楼层',width:80,align:'center',editor:{type:'numberbox'}},
	        {field:'endFloor',title:'结束楼层',width:80,align:'center',editor:{type:'numberbox'}},
	        {field:'offsetx',title:'offsetX',width:100,align:'center'},
	        {field:'offsety',title:'offsetY',width:100,align:'center'},
	        {field:'offsetz',title:'offsetZ',width:100,align:'center'},
	        {field:'professionId',title:'专业',width:100,formatter:function(value){
	        	for(var i = 0; i<projectProfessionData.length; i++){
	        		if(projectProfessionData[i].id == value)
	        			return projectProfessionData[i].name;
	        	}
	        	return value;
	        },editor:{
	        	type:'combobox',
	        	options:{
					valueField:'id',    
				    textField:'name',
				    panelHeight : 'auto'
	        	}
	        }},
	        {field:'cloud',title:'是否点云数据',width:100,align:'center',formatter:function(value){
	        	if(value){
	        		return '<font color="#0000FF">点云</font>';
	        	}else{
	        		return '否';
	        	}
	        }/*,editor:{
	        	type:'checkbox',
	        	options:{on: true,off: false}
	        }*/},
	        {field:'ownedByProject',title:'是否项目整体模型',width:100,align:'center',formatter:function(value){
	        	if(value){
	        		return '是';
	        	}
	        },editor:{
	        	type:'checkbox',
	        	options:{on: true,off: false}
	        }},
	        {field:'standardLayers',title:'标准层楼层',width:150,align:'center'
	        	/*,editor:{
		        	type:'combobox',
		        	options:{
		        		multiple:true,
		        	    valueField:'id',    
		        	    textField:'name',
		        	    panelHeight:'auto'
		        	}
	        	}*/
	        },
	        {field:'isClonedLayer',title:'是否是复制的楼层',width:150,align:'center',formatter:function(value){
	        	if(value){
	        		return '是';
	        	}
	        }},
	        {field:'modelTreeId',hidden:true}
	  	]],
	  	toolbar:[{
	  		text : '增加',
			iconCls : 'icon-add',
			handler : function() {
				// 获得正在编辑的行
				var editingRowsIndexs = $('#tb_model').datagrid('getEditingRowIndexs');
				// 如果有正在编辑的行不能再编辑其他行
				if(0 >= editingRowsIndexs.length){
					var modelTree = $('#ttb_model_modeltree').treegrid('getSelected');
					if(null == modelTree){
						$.messager.alert('警告','没有选择模型树节点'); 
						return ;
					}
					$('#panel_model_add').form('clear');
					$('#panel_model_add').dialog('open');
					$('#p_model_startFloor').numberbox({ });
					$('#p_model_endFloor').numberbox({ });
					$('#p_model_order').numberbox({ });
					$('#p_model_offsetx').numberbox({ precision:2 });
					$('#p_model_offsety').numberbox({ precision:2 });
					$('#p_model_offsetz').numberbox({ precision:2 });
					$('#p_model_professionId').combobox({    
						data : projectProfessionData,
						valueField:'id',    
					    textField:'name',
					    panelHeight : 'auto'
					});
					$('#p_model_standardLayers').combobox({    
						data : floorData,
						multiple:true,
		        	    valueField:'id',    
		        	    textField:'name',
		        	    panelHeight:'auto'
					});
				}
			}
	  	},'-',{
	  		text : '编辑',
	  		iconCls : 'icon-edit',
	  		handler : function(){
	  			var rowData = $('#tb_model').datagrid('getSelected');
				var rowDataIndex = $('#tb_model').datagrid('getRowIndex',rowData);
				if(rowData){
					// 获得正在编辑的行
					var editingRowsIndexs = $('#tb_model').datagrid('getEditingRowIndexs');
					// 如果有正在编辑的行不能再编辑其他行
					if(0 < editingRowsIndexs.length){
						$.messager.alert('提示','有正在编辑的数据');
						$('#tb_model').datagrid('selectRow',editingRowsIndexs[0]);
						return ;
					}else{
						$('#tb_model').datagrid('beginEdit',rowDataIndex);
						// 专业字段target
						var professionTarget  = $('#tb_model').datagrid('getEditor', {'index':rowDataIndex,'field':'professionId'}).target;
						professionTarget.combobox('loadData',projectProfessionData);
						// 是否点云target
						/*var cloudTarget  = $('#tb_model').datagrid('getEditor', {'index':rowDataIndex,'field':'cloud'}).target;
						if(rowData.cloud)
							cloudTarget[0].checked=true;*/
						// 是否项目整体模型target
						var projectTarget = $('#tb_model').datagrid('getEditor', {'index':rowDataIndex,'field':'ownedByProject'}).target;
						if(rowData.ownedByProject)
							projectTarget[0].checked=true;
						/*标准层编辑功能去掉*/
						/*var standardLayersTarget = $('#tb_model').datagrid('getEditor', {'index':rowDataIndex,'field':'standardLayers'}).target;
						standardLayersTarget.combobox('loadData',floorData);*/
					}
				}else
					$.messager.alert('提示','没有选择模型');
	  		}
	  	},'-',{
	  		text : '保存',
	  		iconCls : 'icon-save',
	  		handler : function(){
	  		// 获得正在编辑的行
				var editingRowsIndexs = $('#tb_model').datagrid('getEditingRowIndexs');
				// 如果有正在编辑的行才执行保存
				if(0 < editingRowsIndexs.length){
					$('#tb_model').datagrid('selectRow',editingRowsIndexs[0])
					$('#tb_model').datagrid('endEdit',editingRowsIndexs[0])
					var rowData = $('#tb_model').datagrid('getSelected');
					delete rowData["modelname"];
					delete rowData["filepath"];
					delete rowData["creratetime"];
					console.info(rowData);
					$.ajax({
						url: 'model/edit.htmls',
						data: rowData,
						type: "POST",
						dataType: 'json',
						success: function(result){
							var modeltreeRow = $('#ttb_model_modeltree').treegrid('getSelected');
							$('#ttb_model_modeltree').treegrid('select',modeltreeRow.id);
						}
					});
				}else{
					$.messager.alert('提示','没有修改信息');
				}
	  		}
	  	},'-',{
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var modelData = $('#tb_model').datagrid('getSelected');
				if(modelData){
					$.ajax({
						url: 'delBimModel.htmls',
						data: {id : modelData.id},
						type: "POST",
						dataType: 'json',
						success: function(result){
							$.ajax({ //获得模型列表
					    		url: 'getBimModelFileList.htmls',
								data: {modelTreeId:$('#ttb_model_modeltree').treegrid('getSelected').id},
								type: "POST",
								dataType: 'json',
								success: function(result){
									//$('#tb_model').datagrid('loadData',{total:0,rows:[]});
									$('#tb_model').datagrid('loadData',result.obj);
								}
					    	});
							if(result.success){
								$.messager.show({
									title:'提示',
									msg:'模型删除成功',
									timeout:1000,
									showType:'slide'
								});
							}
						}
					});
				}else
					$.messager.alert('提示','没有选择要删除的模型');
			}
	  	},'-',{
	  		text : '检测模型文件',
			iconCls : 'icon-search',
			handler : function() {
				var modelData = $('#tb_model').datagrid('getSelected');
				if(modelData==null){
					$.messager.alert('警告','请选择要检测的行');
					return;
				}
				$.ajax({ //获得模型列表
		    		url: 'checkBimModelFile.htmls',
					data: {"modelId":modelData.id,"type":1},
					type: "POST",
					dataType: 'json',
					success: function(result){
						$.messager.alert('警告',result.msg);
					}
		    	});
			}
	  	},'-',{
	  		text : '检测SC文件',
			iconCls : 'icon-tip',
			handler : function() {
				var modelData = $('#tb_model').datagrid('getSelected');
				if(modelData==null){
					$.messager.alert('警告','请选择要检测的行');
					return;
				}
				$.ajax({ //获得模型列表
		    		url: 'checkBimModelFile.htmls',
		    		data: {"modelId":modelData.id,"type":2},
					type: "POST",
					dataType: 'json',
					success: function(result){
						$.messager.alert('警告',result.msg);
					}
		    	});
			}
	  	}/*,'-',{
	  		text : '一键检测',
			iconCls : 'icon-sum',
			handler : function() {
				var projectId = $('#cb_model_projectid').combobox('getValue')
				if(projectId==null){
					$.messager.alert('警告','请选择项目');
					return;
				}
				$.ajax({ //获得模型列表
		    		url: 'oneKeyCheckBimModelFile.htmls',
		    		data: {"projectId":projectId},
					type: "POST",
					dataType: 'json',
					success: function(result){
						$.messager.alert('警告',result.msg);
					}
		    	});
			}
	  	}*/]
	});
	/* 模型增加窗口 */
	$('#panel_model_add').dialog({
		title: "增加模型",  
		width: 300,  
	    height: 380,
	    collapsible:true,
	    maximizable:true,
	    iconCls: 'icon-add',                 //弹出框图标  
	    modal: true, 
	    closed : true,
	    buttons: [{
	    	text : '保存',
	    	handler : function(){
	    		var startFloor = $('#p_model_startFloor').numberbox('getValue');
	    		var endFloor = $('#p_model_endFloor').numberbox('getValue');
	    		var offsetx = $('#p_model_offsetx').numberbox('getValue');
	    		var offsety = $('#p_model_offsety').numberbox('getValue');
	    		var offsetz = $('#p_model_offsetz').numberbox('getValue');
	    		var professionId = $('#p_model_professionId').combobox('getValue');
	    		var professionName = $('#p_model_professionId').combobox('getText');
	    		var standardLayers = $('#p_model_standardLayers').combobox('getValues');
	    		var modelTreeId = $('#ttb_model_modeltree').treegrid('getSelected').id;
	    		var imgPath = $('#p_model_file').val();
		    	var strExtension = imgPath.substr(imgPath.lastIndexOf('.') + 1);
		    	var project_Id = $('#cb_model_projectid').combobox('getValue');
		    	var p_model_order = $('#p_model_order').numberbox('getValue');
		    	if('' == offsetx)
		    		offsetx = 0;
		    	if('' == offsety)
		    		offsety = 0;
		    	if('' == offsetz)
		    		offsetz = 0;
	    		var data = {
	    				startFloor : startFloor,
	    				endFloor : endFloor,
	    				offsetx : offsetx,
	    				offsety : offsety,
	    				offsetz : offsetz,
	    				professionId : professionId,
	    				modelTreeId : modelTreeId,
	    				project_Id:project_Id,
	    				projectId:project_Id,
	    				standardLayers:standardLayers,
	    				modelOrder:p_model_order,
	    				professionCode:professionName
	    		};
	    		console.info(data);
	    		if($('#p_model_projectId')[0].checked){
	    			data.ownedByProject = true;
	    		}
	    		$('#panel_model_add').dialog('close');
	    		load();
	    		$.ajaxFileUpload({  
			        url: 'addBimModel.htmls', //服务器端请求地址  
			        secureuri: false, //是否需要安全协议，一般设置为false  
			        fileElementId: 'p_model_file', //文件上传域的ID  
			        dataType: 'json', //返回值类型 一般设置为json  
			        enctype:'multipart/form-data',//注意一定要有该参数 
			        data : data, 
			        success: function (data, status){  
			        	//data是从服务器返回来的值 
			        },  
			        error: function (data, status, e){  
			        	disLoad();
			        	data = $.parseJSON(data.responseText);
			        	if(data.success){
			        		// 刷新模型列表
			        		$.ajax({ //获得模型列表
					    		url: 'getBimModelFileList.htmls',
								data: {modelTreeId:$('#ttb_model_modeltree').treegrid('getSelected').id},
								type: "POST",
								dataType: 'json',
								success: function(result){
									//$('#tb_model').datagrid('loadData',{total:0,rows:[]});
									$('#tb_model').datagrid('loadData',result.obj);
								}
					    	});
			        		$.messager.show({
								title:'提示',
								msg:'模型增加成功',
								timeout:1000,
								showType:'slide'
							});
			        	}else{
			        		$.messager.alert('提示',data.msg);
			        	}
			        }  
				});
	    	}
	    },{
			text:'关闭',
			handler:function(){
				$('#panel_model_add').dialog('close');
			}
		}]
	});

	
	/* 模型树设置窗口 */
	$('#ttb_model_modeltree_setting').dialog({
		title: "模型设置",  
		width: 300,  
	    height: 260,
	    collapsible:true,
	    maximizable:true,
	    iconCls: 'icon-ok',                 //弹出框图标  
	    modal: true, 
	    closed : true,
	    buttons: [{
	    	text : '保存',
	    	handler : function(){
	    		var professionIds = $('#ttb_model_modeltree_pf').combobox('getValues');

	    		var modelTreeId = 0;
	    		if($('#click_from_sign').val() == 'project'){
	    			modelTreeId = 0;
	    		}
	    		else{
	    			var getSelected_model = $('#ttb_model_modeltree').treegrid('getSelected');
		    		if(getSelected_model){
		    			modelTreeId = getSelected_model.id;
		    		}
	    		}
	    		
	    		var projectId = $('#cb_model_projectid').combobox('getValue');

	    		var data = {
	    				professionIds : professionIds.join(","),
	    				modelTreeId : modelTreeId,
	    				projectId:projectId
	    		};
	    		console.info(data);
	    		
	    		$.ajax({ //获得模型列表
		    		url: 'model/initSetting.htmls',
		    		data: data,
					type: "POST",
					dataType: 'json',
					success: function(result){
						$('#ttb_model_modeltree_setting').dialog('close');
						$.messager.alert('提示','设置成功');
					}
		    	});
	    	}
	    },{
			text:'关闭',
			handler:function(){
				$('#ttb_model_modeltree_setting').dialog('close');
			}
		}]
	});
	
	$("#cb_model_project_setting").click(function(){
		$('#ttb_model_modeltree_setting').form('clear');
		$('#ttb_model_modeltree_setting').dialog('open');
		$('#ttb_model_modeltree_setting').panel({title: "项目整体设置"});
		
		$('#click_from_sign').val('project');
		$('#ttb_model_modeltree_pf').combobox({    
			data : projectProfessionData,
			multiple:true,
			valueField:'id',    
		    textField:'name',
		    panelHeight : 'auto',
		    onLoadSuccess : function() {
		    	var modelTreeId = 0;
	    		if($('#click_from_sign').val() == 'project'){
	    			modelTreeId = 0;
	    		}
	    		else{
	    			var getSelected_model = $('#ttb_model_modeltree').treegrid('getSelected');
		    		if(getSelected_model){
		    			modelTreeId = getSelected_model.id;
		    		}
	    		}
	    		
	    		var projectId = $('#cb_model_projectid').combobox('getValue');

	    		var data = {
	    				modelTreeId : modelTreeId,
	    				projectId:projectId
	    		};
	    		
		    	$.ajax({ //获得模型列表
		    		url: 'model/getInitSetting.htmls',
		    		data: data,
					type: "POST",
					dataType: 'json',
					success: function(result){
						//console.log(result.data.professionIds);
						$('#ttb_model_modeltree_pf').combobox('setValues',result.data.professionIds.split(","));
					}
		    	});
			}
		});
	});
	
	//加载遮罩
	function load() {  
	    $("<div class=\"datagrid-mask\"></div>").css({ display: "block", width: "100%", height: $(window).height() }).appendTo("body");  
	    $("<div class=\"datagrid-mask-msg\"></div>").html("文件上传中，请稍候。。。").appendTo("body").css({ display: "block", left: ($(document.body).outerWidth(true) - 190) / 2, top: ($(window).height() - 45) / 2 });  
	}
	//取消加载层  
	function disLoad() {  
	    $(".datagrid-mask").remove();  
	    $(".datagrid-mask-msg").remove();  
	}