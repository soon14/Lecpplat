$(function(){
	var url = 'action/dml?op=code:1';
	var checkUrl = function(){
		if($('#operation').val()=='3') url = 'action/dml?op=code:3';
	};
	$('#btnFormSave').click(function(){ 
		if(!$("#detailInfoForm").valid())return false;
		eDialog.success('字典保存成功！'); 
		/*
		checkUrl();
		$.eAjax({
			url : $webroot + url,
			data : ebcForm.formParams($("#code_form")),
			success : function(returnInfo) {
				
				//刷新缓存
				$.eAjax({
					url : $webroot + 'code/refreshDictCache',
					success : function(returnInfo) { 
						window.location.href = $webroot + 'jsp/manage/system/code/codeMain.jsp';
					}
				});
			}
		});
		*/
	});
	$('#btnReturn').click(function(){
		window.location.href = 'manage_index.html';
	});
});