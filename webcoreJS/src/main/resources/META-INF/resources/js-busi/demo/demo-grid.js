$(function(){
	$("#dataGridTable").initDT({
        'pTableTools' : false,
        'pSingleCheckClean' : false,
        "sAjaxSource": GLOBAL.WEBROOT + '/demo/gridlist',
        //指定列数据位置
        "aoColumns": [
			{ "mData": "id", "sTitle":"ID","sWidth":"80px","bSortable":true},
			{ "mData": "code", "sTitle":"编码","sWidth":"80px","bSortable":false},
			{ "mData": "info", "sTitle":"编码描述","sWidth":"280px","bSortable":false},
			{ "mData": "createTime", "sTitle":"创建时间","bSortable":false,"mRender": function(data,type,row){
				return ebcDate.dateFormat(data,"yyyy-MM-dd hh:mm:ss");
			}}
			/**
			 * ,
			{ "mData": "prop1", "sTitle":"其他属性1","sWidth":"90px","bSortable":false},
			{ "mData": "prop2", "sTitle":"其他属性2","sWidth":"90px","bSortable":false},
			{ "mData": "prop3", "sTitle":"其他属性3","sWidth":"90px","bSortable":false},
			{ "mData": "statusName", "sTitle":"状态","sWidth":"30px","bSortable":false},
			{ "mData": "sortno", "sTitle":"排序","sWidth":"40px","bSortable":false}
			 */
        ],
        "eDbClick" : function(){
        	modifyBiz();
        }
	});
	
	var modifyBiz = function(){
		var ids = $('#dataGridTable').getCheckIds();
		if(ids && ids.length==1){
			window.location.href = GLOBAL.WEBROOT+'/demo/edit';
		}else if(ids && ids.length>1){
			eDialog.alert('只能选择一个项目进行操作！');
		}else if(!ids || ids.length==0){
			eDialog.alert('请选择至少选择一个项目进行操作！');
		}
	};
	
	$('#btnFormSearch').click(function(){
		if(!$("#searchForm").valid()) return false;
		var p = ebcForm.formParams($("#searchForm"));
		$('#dataGridTable').gridSearch(p);
	});
	
	$('#btnFormReset').click(function(){
		ebcForm.resetForm('#searchForm');
	});
	
	$('#btn_code_add').click(function(){
		window.location.href = GLOBAL.WEBROOT+'/demo/edit';
	});
	
	$('#btn_code_modify').click(function(){
		modifyBiz();
	});
	
	$("#btn_code_more").on("click",function(){
		window.location.href = GLOBAL.WEBROOT+'/demo/more';
	});
	
	
	$('#btn_code_del').click(function(){
		var ids = $('#dataGridTable').getCheckIds();
		if(ids && ids.length==1){
			eDialog.confirm("您确认删除该字典项目吗？", {
				buttons : [{
					caption : '确认',
					callback : function(){
						eDialog.alert('success','删除成功！');
						/*
						$.eAjax({
							url : $webroot + 'action/dml?op=code:2&code.id=' + ids[0],
							success : function(returnInfo) {
								if(returnInfo.code){
									
									window.location.reload();
								}
							}
						});
						*/
					}
				},{
					caption : '取消',
					callback : $.noop
				}]
			});
		}else if(ids && ids.length>1){
			eDialog.alert('只能选择一个项目进行操作！');
		}else if(!ids || ids.length==0){
			eDialog.alert('请选择至少选择一个项目进行操作！');
		}
	});
	
	$("#btn_loading").click(function(){
		$.gridLoading({"message":"正在加载中。。。"});
	});
	
	$("#btn_part_loading").click(function(){
		$.gridLoading({"el":"#dataGridTable","message":"正在加载中。。。"});
	});
	
	$("#btn_part_unload").click(function(){
		$.gridUnLoading({"el":"#dataGridTable"});
	});
});