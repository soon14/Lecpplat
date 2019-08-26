$(function(){
	$('#btnFileUpload').click(function(evt){
		/*
		bDialog.open({
			title : '文件上传',
			width : 400,
			height : 450,
			url : GLOBAL.WEBROOT + '/demo/open',
			params : {
				'userName' : 'zhangsan'
			},
			callback:function(data){
				if(data && data.result && data.result.length > 0 ){
					eDialog.alert('已完成弹出窗口操作！接收到弹出窗口传回的 userName 参数，值为：' + data.results[0].userName );
				}else{
					eDialog.alert('弹出窗口未回传参数',$.noop,'error');
				}
			}
		});
		*/
		busSelector.uploader({
			callback : function(data){
				if(data && data.results && data.results.length > 0){
					$('#imgPreview').attr('src',data.results[0].url);
				}
			}
		}, evt);
	});
	
	$('#textAutocomplete').autocomplete({
		source : [['boat', 'dog', 'drink', 'elephant', 'fruit', 'London']],
		minLength : 1,
		limit : 10,
		showHint : false
	});
	
	//***********************************************文件上传静默模式（非弹出窗口）*****************************************
	//返回的数组
	//var fileResults = new Array();
	//返回的文件信息
	var fileInfo = {};
    //单个文件上传成功后的回调处理，实现上传结果处理等信息均在此处理
    //最终结果可在onQueueComplete中统一显示
    var uploadSuccess = function(file,data,response){
		if(response){
			if(data){//data为后台返回的JSON内容
				var tmp = JSON.parse(data);
				//var fileInfo = {};
				//检查文件是否上传成功
				if($.fn.eUpload.onUploadSuccessCheck(tmp)){
					if(tmp && $.isPlainObject(tmp) && typeof(tmp.fileId) != 'undefined' && typeof(tmp.url) != 'undefined' ){
						fileInfo.fileId = tmp.fileId;
						fileInfo.url = tmp.url;
						//fileResults.push(fileInfo);
					}
				}
			}
		}
	};
    
	//更多的参数请参考e.upload.js中的详细参数
	$("#attachmentFileInput").eUploadSilentInit({
		'uploader' : $webroot + 'ecpupload/publicFileUpload',//后台接收文件处理的controller
		'onUploadSuccess' : uploadSuccess,
		//回调
		'callback' : function(){
			if(fileInfo) $('#imgPreview1').attr('src',fileInfo.url);
		}
	});
	//***********************************************文件上传静默模式（非弹出窗口）*****************************************
});