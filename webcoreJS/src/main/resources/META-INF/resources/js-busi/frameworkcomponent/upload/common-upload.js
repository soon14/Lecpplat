$(function(){
	//获得当前弹出窗口对象
	var _dlg = bDialog.getDialog();
	//获得父窗口传递给弹出窗口的参数集
	var _param = bDialog.getDialogParams(_dlg);
	//参数调用者若对参数没有设置值，则设置默认值为undefined，保证该属性会被默认属性所替换
	$('#fileUploaderMainBox').eUploadInit({
		request : {
			endpoint : _param.uploadUrl ? _param.uploadUrl : undefined,
			inputName : _param.uploadFileObjName ? _param.uploadFileObjName : undefined
		},
		multiple:_param.checktype=='multi' ? true : false,
		validation : {
			allowedExtensions : _param.fileTypeExts,
			sizeLimit : _param.fileSizeLimit ? ebcUtils.fileSize2Bytes(_param.fileSizeLimit,true) : undefined,
			sizeLimitStr : _param.fileSizeLimit ? _param.fileSizeLimit : undefined,
			image: {
                maxHeight: _param.imageMaxHeight,
                maxWidth: _param.imageMaxWidth,
                minHeight: _param.imageMinHeight,
                minWidth: _param.imageMinWidth
            }
		},
		callbacks : {
			onComplete : function(id,name,json,xhr){
				//文件上传成功后，将文件的fileId和URL，设置到块对象中
				var ul = $('ul.qq-upload-list-selector');
				var li = $('li.qq-file-id-'+id,$(ul));
				$(li).data('fileId', json.fileId);
				$(li).data('fileName', name);
				$(li).data('url', json.url);
				
				$(li).data('json', json);
				$(li).data('id', id);
			}
		}
	});
	
	$('#fileSizeLimit').text(_param.fileSizeLimit ? _param.fileSizeLimit : $.fn.eUpload.defaults.validation.sizeLimitStr);
	var exts = !$.isArray(_param.fileTypeExts)?$.fn.eUpload.defaults.validation.allowedExtensions.toString():_param.fileTypeExts.toString();
	$('#fileTypeExts').text(exts);
	
	//完成文件上传并返回已上传文件信息
	$('div.file-upload-finish').click(function(e) {
		var fileList = new Array();
		var ul = $('ul.qq-upload-list-selector');
		$('li.qq-upload-success',$(ul)).each(function(i,row){
			var fileInfo = {};
			//将原生参数返回
			fileInfo.id = $(row).data('id');
			fileInfo.name = $(row).data('fileName');
			fileInfo.json = $(row).data('json');
			
			fileInfo.fileId = $(row).data('fileId');
			fileInfo.fileName = $(row).data('fileName');
			fileInfo.url = $(row).data('url');
			fileInfo.success = fileInfo.json.success;

			fileList.push(fileInfo);
		});
		bDialog.closeCurrent(fileList);
	});
});