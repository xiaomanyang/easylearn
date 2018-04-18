$(function(){
	var userId = GetQueryString('uid');
	$.ajax({
		url: '../project/userp.htmls',
		data: {'uid':userId},
		type: "POST",
		dataType: 'json',
		success: function(result){
			var arr = new Array();
			if(result.length>0){
				for (var i=0;i<result.length;i++){
					var name = result[i].projectname;
					var img = result[i].img;
					arr.push('<div class="col-lg-3 col-xs-6">');
					arr.push('<div class="small-box bg-aqua">');
					arr.push('<div class="inner">');
					arr.push('<a href="./modelTree.jsp?projectId='+result[i].id+'">');
					arr.push('<img src="');
					switch(true){
					case img.indexOf("ZK") > 0:
						arr.push('../web-bim/img/中科.jpg');
					  break;
					case name.indexOf("万达") > 0:
						arr.push('../web-bim/img/万达.jpg');	
					  break;
					case name.indexOf("北京") > 0:
						arr.push('../web-bim/img/新北京.jpg');
						break;
					default:
						arr.push('../web-bim/img/no_data.jpg');
					}
					arr.push('" style="width:100%;width:100%">');
					arr.push('</a>');
					arr.push('</div>');
					arr.push('<a href="./modelTree.jsp?projectId='+result[i].id+'" class="small-box-footer">');
					arr.push(name);
					arr.push('<i class="fa fa-arrow-circle-right"></i></a>');
					arr.push('</div>');
					arr.push('</div>');
				}
			}else{
				arr.push('<div class="col-lg-3 col-xs-6">');
				arr.push('<div class="small-box bg-aqua">');
				arr.push('<div class="inner">');
				arr.push('<img src="');
				arr.push('../web-bim/img/no_data.jpg');
				arr.push('" style="width:100%;width:100%">');
				arr.push('</div>');
				arr.push('<a href="javascript:void(0);" class="small-box-footer">');
				arr.push('暂无数据');
				arr.push('<i class="fa fa-arrow-circle-right"></i></a>');
				arr.push('</div>');
				arr.push('</div>');
			}
			$(".row").append(arr.join(' '));
			$('#total_project').empty();
			$('#total_project').append('共'+result.length+'个');
		}
	});
});
//获取url参数
function GetQueryString(name){
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}