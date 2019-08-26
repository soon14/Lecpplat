/* ===========================================================
 * bus.selector.js
 * ===========================================================
 * Terry 
 * 依赖：
 * bus.selector.js
 * 
 * created : 2015.09.03
 * 
 * 公用选择器
 * 
 * 更新记录：
 */
;(function($) {
	"use strict";
	window.busSelector = {
		//全局文件上传组件
		uploader : function(p,evt){
			if(evt){
				try{
					evt.stopPropagation();
				}catch(e){}	
			}
			p = $.extend({
				url : $webroot + 'upload/uploadSelector',
				uploadUrl : undefined, //自定义上传后台处理路径
				uploadFileObjName : undefined,//自定义上传后台接收对象名称
				width : 800,
				height : 450,
				title : '文件上传',
				fileSizeLimit : undefined,//默认设置10MB
				fileTypeExts : undefined,//限制上传文件的文件类型，默认'jpg','jpeg','gif','png','pdf'
				imageMaxHeight : 0,//图片最大高度
				imageMaxWidth : 0,//图片最大宽度
				imageMinHeight : 0,//图片最小高度
				imageMinWidth : 0,//图片最小宽度
				checktype : "multi",//数据选择模式multi：多选；single：单选模式
				//回调函数function(data)返回的数据结构：
				//标准数据格式：results : [{fileId:'xxxxx',url:'http://xxxxxx'},{...}]
				callback : $.noop
			},p);
			//设置传递参数
			var params = {
				checktype : p.checktype,
				fileTypeExts : p.fileTypeExts,
				uploadUrl : p.uploadUrl,
				uploadFileObjName : p.uploadFileObjName,
				fileSizeLimit : p.fileSizeLimit,
				imageMaxHeight : p.imageMaxHeight,
				imageMaxWidth : p.imageMaxWidth,
				imageMinHeight : p.imageMinHeight,
				imageMinWidth : p.imageMinWidth
			};
			bDialog.open({
				title : p.title,
				width : p.width,
				height : p.height,
				url : p.url,
				params : params,
				callback : p.callback
			});
		}
	};
})(window.jQuery);