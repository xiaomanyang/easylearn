$(window).resize(function() {
	$('#iframe_box iframe').css({
		height : $(window).height()-86
	});
});

$(function(){
	var projectId = GetQueryString("projectId");
	$('#iframe_box iframe').css({
		height : $(window).height()-86
	});
	$.ajax({
		url: '../modelTree.htmls',
		data: {'id':projectId},
		type: "POST",
		dataType: 'json',
		success: function(result){
			var modelTree = new Array();
			var panoramaTree = new Array();
			if (result.obj != null && result.obj.length > 0) {
				// 模型树
				modelTree.push('<li class="treeview">');
				modelTree.push('<a href="#">');
				modelTree
					.push('<i class="fa fa-cube"></i> <span>模型树</span>');
				modelTree.push('<span class="pull-right-container">');
				modelTree
					.push('<i class="fa fa-angle-right pull-right"></i>');
				modelTree.push('</span>');
				modelTree.push('</a>');

				modelTree.push('<ul class="treeview-menu">');
				modelTree
					.push('<li><a href="#" id="wholeModel" onclick="wholeModel('
						+ projectId
						+ ')"><i class="fa fa-circle-o"></i>整体</a></li>');
				for (var i = 0; i < result.obj.length; i++) {
					var oneLayerId = result.obj[i].id;// 部分整体id(模型树节点id)
					var oneLayerName = result.obj[i].nodeName;
					var childLayer = result.obj[i].bimfloors;
					modelTree
						.push('<li><a href="resources/index2.html"><i class="fa fa-circle-o"></i> '
							+ oneLayerName + '');
					modelTree
						.push('<span class="pull-right-container"><i class="fa fa-angle-right pull-right"></i></span>');
					modelTree.push('</a>');
					modelTree.push('<ul class="treeview-menu">');
					modelTree
						.push('<li><a href="#" id="partModel" onclick="partModel('
							+ oneLayerId + ',' + '\'' + oneLayerName + '\''
							+ ')"><i class="fa fa-circle-o"></i> '
							+ oneLayerName + '整体</a></li>');
					if (childLayer != null && childLayer.length > 0) {
						for (var int = 0; int < childLayer.length; int++) {
							// childLayer[int].id 楼层id
							modelTree
								.push('<li><a href="#" id="floorModel" onclick="floorModel('
									+ childLayer[int].id
									+', '+projectId+ ')"><i class="fa fa-circle-o"></i> '
									+ childLayer[int].name
									+ '</a></li>')
						}
					}
					modelTree.push('</ul>');
				}
				modelTree.push('</li>');
				modelTree.push('</ul>');
				modelTree.push('</li>');
				// 全景图树
				panoramaTree.push('<li class="treeview">');
				panoramaTree.push('<a href="#">');
				panoramaTree
					.push('<i class="fa fa-street-view"></i> <span>全景图树</span>');
				panoramaTree
					.push('<span class="pull-right-container">');
				panoramaTree
					.push('<i class="fa fa-angle-right pull-right"></i>');
				panoramaTree.push('</span>');
				panoramaTree.push('</a>');

				panoramaTree.push('<ul class="treeview-menu">');
				/*
				 * panoramaTree.push('<li><a href="#" id="wholeModel"
				 * onclick="wholeModel('+projectId+')"><i class="fa
				 * fa-circle-o"></i>整体</a></li>');
				 */
				for (var i = 0; i < result.obj.length; i++) {
					var oneLayerId = result.obj[i].id;// 部分整体id(模型树节点id)
					var oneLayerName = result.obj[i].nodeName;
					var childLayer = result.obj[i].bimfloors;
					panoramaTree
						.push('<li><a href="resources/index2.html"><i class="fa fa-circle-o"></i> '
							+ oneLayerName + '');
					panoramaTree
						.push('<span class="pull-right-container"><i class="fa fa-angle-right pull-right"></i></span>');
					panoramaTree.push('</a>');
					panoramaTree.push('<ul class="treeview-menu">');
					/*
					 * panoramaTree.push('<li><a href="#"
					 * id="partModel"
					 * onclick="partModel('+oneLayerId+')"><i class="fa
					 * fa-circle-o"></i> '+oneLayerName+'整体</a></li>');
					 */
					if (childLayer != null && childLayer.length > 0) {
						for (var int = 0; int < childLayer.length; int++) {
							// childLayer[int].id 楼层id
							panoramaTree
								.push('<li><a href="#" id="floorPanorama" onclick="floorPanorama('
									+ childLayer[int].id
									+', '+projectId+ ')"><i class="fa fa-circle-o"></i> '
									+ childLayer[int].name
									+ '</a></li>');
						}
					}
					panoramaTree.push('</ul>');
				}
				panoramaTree.push('</li>');
				panoramaTree.push('</ul>');
				panoramaTree.push('</li>');
			}
			$(".sidebar-menu").append(modelTree.join(' '));
			$(".sidebar-menu").append(panoramaTree.join(' '));
			var _StrPlanTree = panoramaTree.join(' ');
			_StrPlanTree = _StrPlanTree.replace('全景图树','平面图列表');
			_StrPlanTree = _StrPlanTree.replace(/floorPanorama/g,'floorPlan');
			$(".sidebar-menu").append(_StrPlanTree);
			
		}
	});
});
//$(); end


//加载整体模型
function wholeModel(projectId){
	// alert("项目id为--"+projectId);
	$('#iframe_box iframe').attr('src','overallModel.html?projectId='+projectId);
}

//加载部分整体模型
function partModel(nodeId,oneLayerName){
	// alert("节点id为--"+nodeId);
	oneLayerName = encodeURI(oneLayerName)//decodeURI
	$('#iframe_box iframe').attr('src','overallModel.html?modelTreeId='+nodeId + '&oneLayerName=' + oneLayerName);
	// setTimeout(function () {
	// 	$('#iframe_box iframe').contents().find("#text").html(oneLayerName + "整体模型")
	// },1000)
}

//某一楼层模型
function floorModel(floorId,projectId){
	$('#iframe_box iframe').attr('src','floorModelShowW.html?floorId='+floorId+'&projectId='+projectId);
}

//全景图
function floorPanorama(floorId){
	$('#iframe_box iframe').attr('src','panorama.html?floorId='+floorId);
}

//平面图
function floorPlan(floorId,projectId){
	$('#iframe_box iframe').attr('src','planWeb.html?floorId='+floorId+'&projectId='+projectId);
}

function GetQueryString(name){
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
}
