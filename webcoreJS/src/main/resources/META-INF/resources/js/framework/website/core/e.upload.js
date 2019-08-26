/* ===========================================================
 * e.upload.js
 * ===========================================================
 * Terry 
 * created : 2015.08.28
 * 
 * 基于fine-uploader插件(原来使用uploadify)进行封装的上传功能插件
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
 * 
 * 2016.06.20
 * 将上传插件从uploadify更换为fine-uploader
 * 2016.06.23
 * 完成使用fine-uploader对基础版上传（非弹出统一上传窗口）的改造
 * 2016.07.26
 * 解决深度复制参数时，会出现文件限制类型与默认参数全并的问题，如果用户传递了参数，就不再使用默认参数
 * 2016.07.29
 * 解决插件自带的点位图片的路径设置问题
 * 解决文件尺寸上限提示问题（容量算法问题，现统一使用1000为单位）
 * 2017.04.14
 * 引用fineuploader的文件从原来的min压缩文件换为源码文件，否则在低版本IE下不能正常初始化
 */
;(function ($) {
	"use strict"; // 使用严格模式Javascript
	
	var scriptName = 'jquery.fine-uploader.js';
	var scriptUrl = eUrl.getScriptUrl(scriptName);
	
	
	//默认对象，暂时设置为空
	$.fn.eUpload = {};

	//插件默认参数
	$.fn.eUpload.defaults = {
		'template' : 'eUploadTemplate',
		/**
		 * 是否允许多文件上传
		 */
		'multiple' : true,
		'request' : {
			'endpoint' : $webroot + 'upload/publicFileUpload',
			'inputName' : 'uploadFileObj'
		},
		'thumbnails' : {
			'placeholders' : {
				'waitingPath' : scriptUrl + 'placeholders/waiting-generic.png',
				'notAvailablePath' : scriptUrl + 'placeholders/not_available-generic.png'
			}
		},
		'text' : {
			'failUpload' : '上传失败',
			'fileInputTitle' : '选择文件进行上传'
		},
		/**
		 * 提示信息本地化
		 */
		'messages' : {
			'typeError' : "{file} 文件格式不正确。有效格式： {extensions}",
			'sizeError' : "{file} 文件容量超过限制, 文件最大容量为： {sizeLimit}",
			'minSizeError' : "{file} 文件容量低于限制, 上传的文件最小容量为： {minSizeLimit}.",
			'emptyError' : "{file} 文件为空，请选择其他文件进行上传。",
			'noFilesError' : "未选择任何文件进行上传",
			'tooManyItemsError' : "太多文件 ({netItems}) 需要被上传，上传文件数量限制为： {itemLimit}个",
			'maxHeightImageError' : "图片高度超过限制",
			'maxWidthImageError' : "图片宽度超过限制",
			'minHeightImageError' : "图片高度不足",
			'minWidthImageError' : "图片宽度不足",
			'retryFailTooManyItems' : "重试失败 - 您已达到文件数量上限。",
			'onLeave' : "文件正在上传，若离开该页面，正在上传的文件将被取消",
			'unsupportedBrowserIos8Safari' : "Unrecoverable error - this browser does not permit file uploading of any kind due to serious bugs in iOS8 Safari.  Please use iOS8 Chrome until Apple fixes these issues."
		},
		'validation' : {
			'allowedExtensions' : ['jpeg', 'jpg', 'gif', 'png'],
			'sizeLimit' : ebcUtils.fileSize2Bytes('5MB',true),
			'sizeLimitStr' : '5MB'
		},
		/**
		 * 删除文件，目前服务端为空请求
		 */
        'deleteFile' : {
            'enabled' : true,
            'method' : "POST",
            'endpoint' : $webroot + 'upload/deleteUploadFile'
        },
        /**
         * 调试模式，在浏览器的console控制台中打印日志
         */
		'debug' : true,
		/**
		 * 消息提示统一调用方法
		 * UI模式使用，在Basic模式下不执行该事件处理
		 * Basic模式需要处理onError中获得errorReason进行错误信息展示
		 */		
		'showMessage' : function(message) {
			eDialog.alert(message,$.noop,'error');
		},
		/**
		 * 事件回调
		 */
		'callbacks' : {
			//单个文件上传成功后的回调处理
			'onComplete' : function(id,name,json,xhr){},
			//删除上传文件前的回调，可以阻止删除(return false)
			'onSubmitDelete' : function(id){}
		}
	};
	
	/**
	 * 用户传递默认参数
	 */
	$.fn.eUpload.userDefaults = {
		uploadUrl : undefined, //自定义上传后台处理路径
		uploadFileObjName : undefined,//自定义上传后台接收对象名称
		fileSizeLimit : undefined,//默认设置10MB
		fileTypeExts : undefined,//限制上传文件的文件类型
		imageMaxHeight : 0,//图片最大高度
		imageMaxWidth : 0,//图片最大宽度
		imageMinHeight : 0,//图片最小高度
		imageMinWidth : 0,//图片最小宽度
		//回调函数function(data)返回的数据结构：
		//格式：{fileId:'xxxxxxx',url:'http://xxxx'}
		callback : $.noop
	};
	
	//文件上传类型处理，统一处理因深度复制导致合并的问题，并统一处理默认参数问题
	var _fileTypeExtHandle = function(p,srcP){
		if(undefined!==srcP.validation.allowedExtensions && 
				$.isArray(srcP.validation.allowedExtensions) && 
				srcP.validation.allowedExtensions.length > 0){
			p.validation.allowedExtensions = srcP.validation.allowedExtensions;
		}else{
			p.validation.allowedExtensions = $.fn.eUpload.defaults.validation.allowedExtensions;
		}
	};
	
	//初始化上插件
	$.fn.eUploadInit = function(p){
		return this.each(function(){
			var _this = $(this);
			var params = $.extend(true, {}, $.fn.eUpload.defaults, p);
			_fileTypeExtHandle(params,p);
			//设置服务端校验参数
			params.request.params = {
				'fileSizeLimit' : params.validation.sizeLimitStr,
				'fileTypeExts' : params.validation.allowedExtensions.toString()
			};
			_this.fineUploader(params);
		});
	};
	
	//提供简单上传方式
	//默认不使用批量上传、使用自动上传文件
	//集成Flash安装检查提示
	$.fn.eUploadBaseInit = function(p){
		return this.each(function(){
			//合并用户传递参数与默认参数
			var userParams = $.extend({}, $.fn.eUpload.userDefaults, p);
			//组装成为fine-uploader初始参数
			var preParams = {
				request : {
					endpoint : userParams.uploadUrl ? userParams.uploadUrl : undefined,
					inputName : userParams.uploadFileObjName ? userParams.uploadFileObjName : undefined
				},
				multiple : false,
				validation : {
					allowedExtensions : userParams.fileTypeExts,
					sizeLimit : userParams.fileSizeLimit ? ebcUtils.fileSize2Bytes(userParams.fileSizeLimit,true) : undefined,
					sizeLimitStr : userParams.fileSizeLimit ? userParams.fileSizeLimit : undefined,
					image: {
		                maxHeight: userParams.imageMaxHeight,
		                maxWidth: userParams.imageMaxWidth,
		                minHeight: userParams.imageMinHeight,
		                minWidth: userParams.imageMinWidth
		            }
				}
			};
			//与默认参数合并
			var params = $.extend(true, {}, $.fn.eUpload.defaults, preParams);
			_fileTypeExtHandle(params,preParams);
			//设置服务端校验参数
			params.request.params = {
				'fileSizeLimit' : params.validation.sizeLimitStr,
				'fileTypeExts' : params.validation.allowedExtensions.toString()
			};
			
			delete params.template;
			params.button = $(this)[0];//document.getElementById('uploadDivButton');
			
			//错误事件回调
			params.callbacks.onError = function(id,name,errorReason,xhr){
				eDialog.alert(errorReason,$.noop,'error');
			};
			
			if(p.callback && $.isFunction(p.callback)){
				params.callbacks.onComplete = function(id,name,json,xhr){
					if(json && $.isPlainObject(json)){
						p.callback({
							'fileId' : json.fileId,
							'fileName' : name,
							'url' : json.url,
							'success' : json.success
						},json,id,name);
					}
				};
			}
			var upload = new qq.FineUploaderBasic(params);
		});
	};
	

})(window.jQuery);