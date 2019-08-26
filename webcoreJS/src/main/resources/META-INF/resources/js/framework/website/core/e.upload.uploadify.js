/* ===========================================================
 * e.upload.js
 * ===========================================================
 * Terry 
 * created : 2015.08.28
 * 
 * 基于uploadify插件进行封装的上传功能插件
 * 
 * 更新记录：
 * 2015.11.19
 * 增加错误信息中文显示
 * 解决手动取消选择时报错的问题
 * 增加文件选择错误信息的中文处理
 * 优化插件选择文件按钮的样式
 * 
 * 2015.11.20
 * 解决错误提示信息完全替换原生的提示信息问题
 */
!function ($) {
	"use strict"; // 使用严格模式Javascript
	
	
	//初始化上插件
	$.fn.eUploadInit = function(p){
		return this.each(function(){
			var _this = $(this);
			var params = $.extend({}, $.fn.eUpload.defaults, p);
			params.onQueueComplete = function(queueData){
				if(typeof(p.onQueueComplete)!=undefined && $.isFunction(p.onQueueComplete)){
					p.onQueueComplete(queueData);
				}else{
					$.fn.eUpload.queueComplete(queueData);
				}
				params.callback();
			};
			if(!params.formData){
				var formData = {
					'fileSizeLimit' : params.fileSizeLimit,
					'fileTypeExts'  : params.fileTypeExts
				};
				params.formData = formData;
			}
			_this.uploadify(params);
		});
	};
	
	//提供简单上传方式
	//默认不使用批量上传、使用自动上传文件
	//集成Flash安装检查提示
	$.fn.eUploadSilentInit = function(p){
		return this.each(function(){
			var _this = $(this);
			var params = $.extend({}, $.fn.eUpload.defaults, p);
			params.multi = false;
			params.auto = true;
			var formData = {
				'fileSizeLimit' : params.fileSizeLimit,
				'fileTypeExts'  : params.fileTypeExts
			};
			params.formData = formData;
			params.onQueueComplete = function(queueData){
				if(typeof(p.onQueueComplete)!=undefined && $.isFunction(p.onQueueComplete)){
					p.onQueueComplete(queueData);
				}else{
					$.fn.eUpload.queueComplete(queueData);
				}
				params.callback();
			};
			_this.uploadify(params);
			//Flash安装检查
			var result = ebcUtils.flashCheck();
			if(!result.hasFlash){
				if(window.eDialog)
					eDialog.warning('<font color="red">文件上传功能依赖Flash插件，当前浏览器未安装，请在安装后刷新页面</font>');
				else
					alert('文件上传功能依赖Flash插件，当前浏览器未安装，请在安装后刷新页面');
			}
		});
	};
	
	//执行文件上传
	//params:传递到后台的参数集，格式为{}JSON对象
	$.fn.eUpload = function(p){
		return this.each(function(){
			var params = {};
			if(p && p.params) params = p.params;
			$(this).uploadify('settings','formData',params);
			$(this).uploadify('upload','*');
		});
	};
	
	//文件选择错误
	$.fn.eUpload.selectError = function(file, errorCode, errorMsg) {
		var msgText = "<b>文件选择不正确</b></br>";
		switch (errorCode) {
			case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
				//this.queueData.errorMsg = "每次最多上传 " + this.settings.queueSizeLimit + "个文件";
				msgText += "每次最多上传 " + this.settings.queueSizeLimit + "个文件";
			break;
			case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
				msgText += "文件大小超过限制( " + this.settings.fileSizeLimit + " )";
				break;
			case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
				msgText += "文件大小为0";
				break;
			case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
				msgText += "文件格式不正确，仅限 " + this.settings.fileTypeExts;
				break;
			default:
				msgText += "错误代码：" + errorCode + "\n" + errorMsg;
		}
		eDialog.warning(msgText);
		return false;
	};
	
	//上传错误的处理
	/*
	$.fn.eUpload.uploadError = function(event, queueId, fileObj, errorObj){
		alert("event:" + event + "\nqueueId:" + queueId + "\nfileObj.name:"
				+ fileObj.name + "\nerrorObj.type:" + errorObj.type
				+ "\nerrorObj.info:" + errorObj.info);
	};
	*/
	$.fn.eUpload.uploadError = function(file, errorCode, errorMsg, errorString) {
		// 手工取消不弹出提示
		if (errorCode == SWFUpload.UPLOAD_ERROR.FILE_CANCELLED || errorCode == SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED) {
			return;
		}
		var msgText = "<b>上传失败</b></br>";
		switch (errorCode) {
			case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
				msgText += "HTTP 错误\n" + errorMsg;
				break;
			case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
				msgText += "上传文件丢失，请重新上传";
				break;
			case SWFUpload.UPLOAD_ERROR.IO_ERROR:
				msgText += "IO错误";
				break;
			case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
				msgText += "安全性错误\n" + errorMsg;
				break;
			case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
				msgText += "每次最多上传 " + this.settings.uploadLimit + "个";
				break;
			case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
				msgText += errorMsg;
				break;
			case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
				msgText += "找不到指定文件，请重新操作";
				break;
			case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
				msgText += "参数错误";
				break;
			default:
				msgText += "文件:" + file.name + "\n错误码:" + errorCode + "\n" + errorMsg + "\n" + errorString;
		}
		eDialog.warning(msgText);
		//return parameters;
	};
	//使用通用上传处理后返回的信息处理
	$.fn.eUpload.onUploadSuccessCheck = function(data){
		//返回结果中：error字段0-成功；1-失败
		//fileId：文件ID
		//url：文件实际地址
		var result = false;
		if(data && $.isPlainObject(data) && $.type(data.error)!='undefined' && (data.error==0 || data.error=='0')){
			result = true;
		}else{
			result = false;
			var msg = '文件上传失败！';
			if(data && $.isPlainObject(data) && $.type(data.message)!='undefined') msg = data.message;
			eDialog.error(msg);
		}
		return result;
	};
	//所有上传队列完成后的处理
	//此处获取的文件上传成功仅为文件成功上传到后台，但不代表业务检查，数据检查成功，所以
	//此处不再提示文件上传成功
	$.fn.eUpload.queueComplete = function(queueData){
		/*
		if(queueData){
			var msg = "文件成功上传：<b style='color:green;'>" + queueData.uploadsSuccessful + "</b> 个<br>" +
			  "上传失败：<b style='color:red;'>" + queueData.uploadsErrored + "</b> 个<br>";
			if(window.eDialog){
				eDialog.alert(msg,$.noop,'confirmation');
			}else{
				alert(msg);
			}
		}
		*/
	};
	//默认参数
	$.fn.eUpload.defaults = {
		'swf' : $webroot + 'js/jquery/uploadify/uploadify.swf',
		'uploader' : '',
		'fileObjName' : 'uploadFileObj',//传递到后台，用于接收上传文件的对象名
		'overrideEvents' : ['onDialogClose', 'onUploadSuccess', 'onUploadError', 'onSelectError'], //设置完全替换原生事件处理的内容
		'queueID' : "attachmentFileQueue",//队列内容显示元素ID指定
		'buttonText' : '选择上传文件',  //按钮显示文本
		'multi' : false,               //是否为批量上传
		'auto' : false,                //是否选择完文件后自动开始上传
		'queueSizeLimit' : 20,          //文件待上传队列大小，默认设置20个
		'fileSizeLimit': '20MB',      //上传文件大小限制【KB、MB、GB】，必须只使用这三种格式
		'fileTypeExts' : '*.jpg;*.gif;*.bmp;*.png;*.pdf',//上传文件类型限制
		'fileTypeDesc' : '*.jpg;*.gif;*.bmp;*.png;*.pdf',//上传文件类型限制提示信息
		'method' : 'post',              //上传方式
		'height' : 26,                  //Flash按钮高度
		'width' : 100,                  //Flash按钮宽度
		'onSelectError' : $.fn.eUpload.selectError,
		'onUploadError' : $.fn.eUpload.uploadError,
		'onUploadSuccess' : $.noop,     //单个文件上传成功的回调
		'callback' : $.noop             //上传完成后用户处理回调
	};
}(window.jQuery);