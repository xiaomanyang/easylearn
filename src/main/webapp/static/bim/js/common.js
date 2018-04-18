function getEmptyTip(){
	return '<div style="width:100%;border:0px;text-align:center;margin:0 auto;position: absolute;"><span style="width:100%;float:left;font-size:14px;">暂无数据</span></div>';
}

/**
 * 为控件赋值
 * @param data [json对象]
 * @returns 无
 */
function setWebControlValue(data){
	for(var key in data){
		var control=$('#'+key);
		if(control.length<0) continue;
		var type=control.prop('type');
		var value=$.trim(data[key]).replace('&nbsp;','')
		if('text'==type || 'textarea'==type || 'hidden'==type){
			control.val(value);
		}else if('select-one'==type){
			//setValue方法与 控件的editable:false属性冲突 这里使用select
			control.combobox('select', value);
		}
	}
}

/**
 * 为某个表单中的控件赋值 
 * 这里避免同一页面id重复问题 因此根据name属性赋值
 * @param formId
 * @param data [json对象]
 * @returns
 */
function setWebControlValueByForm(formId,data){
	var currForm=$('#'+formId);
	for(var key in data){
		var control=currForm.find('#'+key);
		/*var control=currForm.find('[name="'+key+'"]');*/
		if(control.length<0) continue;
		var type=control.prop('type');
		var value=$.trim(data[key]).replace('&nbsp;','')
		if('text'==type || 'textarea'==type || 'hidden'==type){
			control.val(value);
		}else if('select-one'==type){
			//setValue方法与 控件的editable:false属性冲突 这里使用select
			control.combobox('select', value);
		}
	}
}

/**
 * 绑定下拉选项
 * 控件id controlId
 * 上级字典编码 parentCode
 */
function bindOption(controlId,parentCode)
{
	$('#'+controlId).combobox({    
	    url:baseUrl+'/dictionary/getListByParentCode.do',
	    valueField:'code',    
	    textField:'showName',
	    //editable:false,//禁止手动输入
	    onBeforeLoad: function(param){
	    	param.parentCode = parentCode;
	    }
	});
}

