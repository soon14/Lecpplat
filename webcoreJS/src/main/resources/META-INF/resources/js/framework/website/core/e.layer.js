/**
 * ===========================================================
 * e.layer.js
 * ===========================================================
 * 
 * 基于jquery.blockUI封装的系统统一遮罩层
 * created : 2016.06.28
 * 
 * 
 */
;(function($) {
	"use strict"; // 使用严格模式Javascript
	/**
	 * 默认参数
	 */
	var defaults = {
		css: {
			'border' : 'none',
			'padding' : '15px',
			'backgroundColor' : 'none',
			'-webkit-border-radius' : '10px',
			'-moz-border-radius' : '10px',
			'color' : '#ffffff',
			'font-size' : '40px'
		},
		baseZ : 3000,//设置z-index
		text : '数据加载中……',//遮罩中除动态图标外显示的文本内容
		iconSize : 48,//显示动态图标大小
		//iconUrl : $webroot + 'js/framework/website/images/squares.svg',//动态图标路径(ecp)
		iconUrl : $webroot + 'framework/shell/website/images/squares.svg',//动态图标路径(Prj)
		contentTemplate : '<img src="{{iconUrl}}" width="{{iconSize}}" height="{{iconSize}}"> {{textContent}}',
		
		target : undefined,//目标区域（jquery对象，若不传递该参数则显示全屏遮罩）
		callback : false
	};
	//文本全替换
	var _replaceAll = function(srcStr,substr,newstr){
		if(!srcStr || !typeof(srcStr)=='string') return;
		return srcStr.replace(new RegExp(substr,"gm"),newstr);
	};
	window.eLayer = {
		/**
		 * 显示遮罩层
		 * @param p        默认参数
		 */
		show : function(p){
			var _p = $.extend(true,{},defaults, p);
			var _text = _replaceAll(_p.contentTemplate,'{{iconUrl}}',_p.iconUrl);
			var _size = _p.iconSize;
			//区域遮罩模式时若目标区域高度太小，则需要将动态图标、字体等内容设置小，以适应容器
			if(undefined !== _p.target && _p.target instanceof jQuery){
				var h = $(_p.target).innerHeight();
				if(h < 70){
					_size = 24;
					_p.css['font-size'] = '16px';
					_p.css['padding-top'] = '5px';
				}
			}
			_text = _replaceAll(_text,'{{iconSize}}',_size);
			_text = _replaceAll(_text,'{{textContent}}',_p.text);
			var params = {
				css : _p.css,
				message : _text,
				baseZ : _p.baseZ,
				onBlock : (_p.callback && $.isFunction(_p.callback)) ? _p.callback : $.noop
			};
			if(undefined !== _p.target && _p.target instanceof jQuery){//指定区域遮罩
				$(_p.target).block(params);
			}else $.blockUI(params);//全屏遮罩
		},
		/**
		 * 关闭遮罩层
		 * @param target   目标区域（jquery对象，若不传递该参数则关闭全屏遮罩）
		 */
		hide : function(p){
			var params = {
				onUnblock : (p.callback && $.isFunction(p.callback)) ? p.callback : $.noop
			};
			if(undefined !== p.target && p.target instanceof jQuery) $(p.target).unblock(params);
			else $.unblockUI(params);
		}
	};
})(window.jQuery);