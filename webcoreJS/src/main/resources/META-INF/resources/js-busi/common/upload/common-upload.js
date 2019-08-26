$(function(){
	//获得当前弹出窗口对象
	var _dlg = bDialog.getDialog();
	//获得父窗口传递给弹出窗口的参数集
	var _param = bDialog.getDialogParams(_dlg);
	//返回的数组
	var fileResults = new Array();
	//统计上传成功文件个数
	var successCount = 0;
	//上传处理URL
	var url = _param.uploadUrl ? _param.uploadUrl : $webroot + 'ecpupload/publicFileUpload';
	var fileIco = $webroot + 'js/framework/website/images/documents.png';
    //单个文件上传成功后的回调处理，实现上传结果处理等信息均在此处理
    //最终结果可在onQueueComplete中统一显示
    var uploadSuccess = function(file,data,response){
		if(response){
			if(data){//data为后台返回的JSON内容
				var tmp = JSON.parse(data);
				var fileInfo = {};
				if($.fn.eUpload.onUploadSuccessCheck(tmp)){
					if(tmp && $.isPlainObject(tmp) && typeof(tmp.fileId) != 'undefined' && typeof(tmp.url) != 'undefined' ){
						fileInfo.fileId = tmp.fileId;
						fileInfo.url = tmp.url;
						fileResults.push(fileInfo);
						
						if(file && file.type){
							var ext = file.type.split('.')[1];
							//生成缩略图
							if(ext){
								if(ebcUtils.isImage(ext)){
									$('#imgPreviewBox').append('<img src="' + fileInfo.url + '" fileId="' + fileInfo.fileId + '" >');
								}else{
									$('#imgPreviewBox').append('<img src="' + fileIco + '" fileId="' + fileInfo.fileId + '" >');
								}
							}
						}
						
						//显示总数量
						refreshCount();
					}
				}
			}
		}
	};
	//刷新统计数字
	var refreshCount = function(){
		$('#uploadSuccessCount').text(fileResults.length);
	};
	
	//队列项目模板
	var itemTemp = '<div id="${fileID}" class="uploadify-queue-item">\
						<span class="fileName">${fileName} (${fileSize})</span><span class="data"></span>\
						<div class="uploadify-progress">\
							<div class="uploadify-progress-bar"><!--Progress Bar--></div>\
						</div>\
					</div>';
	//上传插件初始化参数
	var uploadInitParams = {
		'uploader' : url + ';jsessionid=' + $('#pageSessionId').val(),//后台接收文件处理的controller
		'fileObjName' : _param.uploadFileObjName ? _param.uploadFileObjName : 'uploadFileObj',//后台接收文件的对象名称
		'fileTypeDesc' : _param.fileTypeExts,  //文件选择类型描述
		'fileTypeExts' : _param.fileTypeExts,  //文件选择类型限制
		'fileSizeLimit' : _param.fileSizeLimit, //文件大小限制
		'removeCompleted' : false,//上传完成后是否移除项目
		'auto' : true,//是否在选择文件后自动开始上传
		'itemTemplate' : itemTemp,
		'params' : {
			'token' : $('#pageSessionId').val(),
			'fileSizeLimit' : _param.fileSizeLimit,
			'fileTypeExts'  : _param.fileTypeExts
		},
		'multi' : _param.checktype=='multi'?true:false,  //是否批量上传
		'queueID' : "attachmentFileQueue",//队列内容显示元素ID指定默认ID为attachmentFileQueue
		'onUploadSuccess' : uploadSuccess,
		//回调
		'callback' : function(){
			$('#btnFileUploaderUpload').button('reset');//设置状态按钮的状态为恢复
		}
	};
	

    $('#btnFileUploaderUpload').click(function(){
    	var itemList = $('#attachmentFileQueue .uploadify-queue-item');
    	if($(itemList).size() > 0){
        	$(this).button('loading');//设置状态按钮的状态为处理中
        	successCount = 0;
    		$("#attachmentFileInput").eUpload(uploadInitParams);
    	}else{
    		eDialog.alert('请选择需要上传的文件！');
    	}
    });
    
	
	//更多的参数请参考e.upload.js中的详细参数
	$("#attachmentFileInput").eUploadInit(uploadInitParams);
	
	//完成上传，关闭窗口，返回数据
	$('#btnUploadDone').click(function(){
		bDialog.closeCurrent(fileResults);
	});
	//为动态生成的图片框设置点击事件
	$('#imgPreviewBox').delegate('img','click',function(e){
		e.stopPropagation();
		$(this).toggleClass("selected");
	});
	//移除已上传的内容
	$('#btnUploadRemove').click(function(){
		var selectedItems = $('#imgPreviewBox img.selected');
		if($(selectedItems).size() > 0 && fileResults && fileResults.length > 0){
			//console.log(JSON.stringify(fileResults));
			$(selectedItems).each(function(i,row){
				var fileId = $(row).attr('fileId');
				if(fileId){
					var index = 0;
					//遍历获得选中的图片，在数组对象中的下标位置
					$.each(fileResults,function(j,item){
						if(item.fileId == fileId){
							index = j;
							return false;
						}
					});
					ebcUtils.arrayItemRemove(fileResults,index);
				}
				$(this).remove();
			});
			refreshCount();
			//console.log(fileResults);
			//console.log(JSON.stringify(fileResults));
		}else{
			eDialog.alert('请在 " 已上传文件/图片 " 区域点击选择文件/图片后，再进行移除操作！',$.noop,'warning');
		}
	});
	
	//Flash安装检查
	var result = ebcUtils.flashCheck();
	if(!result.hasFlash){
		$('#checkMessage').text('文件上传功能依赖Flash插件，当前浏览器未安装，请在安装后刷新页面');
	}
});