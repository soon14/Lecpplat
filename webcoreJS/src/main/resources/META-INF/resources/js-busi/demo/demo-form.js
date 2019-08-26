$(function(){
	var url = '/demo/save';
	/*var checkUrl = function(){
		if($('#operation').val()=='3') url = 'action/dml?op=code:3';
	};*/
	$('#btnFormSave').click(function(){ 
		if(!$("#detailInfoForm").valid())return false;
		//eDialog.success('字典保存成功！'); 
		
		//checkUrl();
		$.eAjax({
			url : GLOBAL.WEBROOT + "/demo/save",
			data : ebcForm.formParams($("#detailInfoForm")),
			success : function(returnInfo) {
				eDialog.success('字典保存成功！',{
					buttons:[{
						caption:"确定",
						callback:function(){
							window.location.href = $webroot + '/demo/grid';
				        }
					}]
				}); 
				
				//刷新缓存
				/*$.eAjax({
					url : $webroot + 'code/refreshDictCache',
					success : function(returnInfo) { 
						eDialog.success('字典保存成功！'); 
						window.location.href = $webroot + 'jsp/manage/system/code/codeMain.jsp';
					}
				});*/
			}
		});
		
	});
	
	$("#btnFormSave2").on('click',function(){
		$.eAjax({
			url : GLOBAL.WEBROOT + "/demo/save2",
			data : ebcForm.formParams($("#detailInfoForm")),
			success : function(returnInfo) {
				eDialog.success('字典保存成功！'); 
				window.location.href = $webroot + '/demo/grid';
				//刷新缓存
				/*$.eAjax({
					url : $webroot + 'code/refreshDictCache',
					success : function(returnInfo) { 
						eDialog.success('字典保存成功！'); 
						window.location.href = $webroot + 'jsp/manage/system/code/codeMain.jsp';
					}
				});*/
			}
		});
	});
	
	//页面提交
	$("#btnFormSave3").on('click',function(){
		alert("submit");
		$("#detailInfoForm").submit();
	});
	
	$('#btnReturn').click(function(){
		window.location.href = GLOBAL.WEBROOT+'/demo/grid';
	});
	$('#btnShowError').click(function(){
		$('div.formValidateMessages').slideToggle('fast');
	});
	
	$("#btnJsonClick").on('click',function(){
		$.eAjax({
			url : GLOBAL.WEBROOT + "/demo/gridlist",
			success : function(returnInfo) {
				$("#json-msg").html(returnInfo);
			}
		});
	});
	
});