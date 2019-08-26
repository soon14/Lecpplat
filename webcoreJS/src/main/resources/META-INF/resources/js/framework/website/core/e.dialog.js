//require(['ZebraDialog']);
/**
 * 消息对话框
 * 
 * Author：Terry
 * Created：2012.02
 * 
 * 代码重构：2016.07.19
 * 2016.07.30
 * 解决使用eDialog.confrim后，在确认按钮的事件里又进行了alert时，alert不显示或关闭不了的问题
 * 2017.01.28
 * 修改eDialog.alert第三个参数string为{string|object}，string模式依然支持旧方式设置窗口类型，增加object设置窗口类型和打开modernUI模式
 */
;(function($) {
	'use strict';
	var _defaults = {
		url : '',
		title : '',
		closeable : true,
		closeText : '关闭窗口',
		modal : false,//是否模式窗口
		width : 400,
		height : 300,		
		callback : false
	};
	//弹出式通知窗口默认参数
	var _alertDefault = {
	    'type':'information',
	    'title':false,//(p && p.title)?p.title:'提示',
	    'overlay_close' : false,
	    'show_close_button':false,//是否显示关闭按钮
	    'overlay_opacity' : .6,
	    'animation_speed_hide' : 0,
	    'animation_speed_show' : 0,
	    'buttons' : [{
	    	'caption' : '确定',
	    	'callback' : $.noop//callback参数：dialog，弹出窗口的DOM对象，return false，点击按钮后，不关闭对话框
	    }],
	    'onClose' : $.noop
	};
	//获得作用域的jQuery对象，解决在弹出窗口(iframe)中使用消息框位置显示不正确的问题
	var _getDomain = function(){
		return window.self == window.top?window.self.$:window.top.$;
	};
	/**
	 * 默认公共弹出窗口（内部私有，不对外开放访问）
	 * @param content
	 * @param p
	 * @param type
	 */
	var _publicAlert = function(content,p,type){
		var dlg = _getDomain();
		var param = $.extend({},_alertDefault,p);
		var text = content ? content : '';
		var _title = '';
		switch (type) {
			case 'information':
				_title = '提示';
				break;
			case 'warning':
				_title = '警告';
				break;
			case 'error':
				_title = '错误';
				break;
			case 'confirmation':
				_title = '成功';
				break;
			case 'question':
				_title = '确认';
				break;
		}
		param.type = type;
		param.title = _title;		
		if(p && undefined != p.buttons && $.isArray(p.buttons) && p.buttons.length > 0 && type=='question'){
			param.buttons = [];
			$.each(p.buttons,function(i,n){
				param.buttons.push({caption:n.caption,callback:$.noop});
			});
		}
		param.onClose = function(whichBtn){
			if(p && p.buttons && $.isArray(p.buttons) && p.buttons.length > 0 && whichBtn && type == 'question'){
				$.each(p.buttons,function(i,n){
					if(n.caption == whichBtn){
						if(n.callback && $.isFunction(n.callback)){
							n.callback();
							return false;
						}
					}
				});
			}
			if(p && p.onClose && $.isFunction(p.onClose)) p.onClose(whichBtn);
		};
		//当文字内容较多时，自动应用大型消息窗口设置
		if(text.length > 200) param.custom_class = 'hugeZebraDialog';
		dlg.Zebra_Dialog(text.toString(), param);
	};
	//网站前台弹出窗口
	window.eDialog = {
		constant : {
			info : 'information',//消息
			warning : 'warning',//警告
			error : 'error',//错误
			confirm : 'question',//结果确认
			success : 'confirmation'//成功
		},
		//消息通知统一入口
		//param：窗口参数（string || object）
		//       string型参数：窗口类型（图标类型）
		//           【'information','warning','error','confirmation'】
		//           其中默认为'information'不需要设置，'warning'为警告，'error'为错误，'confirmation'为成功
		//       object型参数：综合参数
		//           type - 窗口类型，设置参考string型参数的内容
		//           modernUI - {boolean}值为true时，使用Windows ModernUI风格提示窗口（全宽度）
		//content：消息框显示内容
		//callback：点击'确定'按钮后的回调函数
		alert : function(content,callback,param){
			var defaultType = 'information';
			var _dlg = _getDomain();
			//检查窗口类型是否在范围内，不在范围则返回默认类型
			var getType = function(type){
				if(!type) return defaultType;
				var hasIn = false;
				var str = 'information,warning,error,confirmation';
				return str.indexOf(type)!=-1 ? type : defaultType;
			}
			var _callback = $.noop,_title,_type;
			if($.type(param) != 'undefined'){
				if($.type(param) == 'string'){
					_type = getType(param);
				}else if($.type(param) == 'object'){
					_type = $.type(param.type) != 'undefined' ? getType(param.type) : defaultType;
				}else{
					_type = defaultType;
				}
			}else _type = defaultType;
			switch (_type) {
			case 'information':
				_title = '提示';
				break;
			case 'warning':
				_title = '警告';
				break;
			case 'error':
				_title = '错误';
				break;
			case 'confirmation':
				_title = '成功';
				break;
			}
			if(callback && $.isFunction(callback)) _callback = callback;
			var p = $.extend({}, _alertDefault);
			p.type = _type;
			p.title = _title;
			p.buttons = [{caption : '确定',callback : $.noop}];
			//p.buttons = ['<i class="icon-ok"></i> 确定'];
			p.onClose = function(obj){
				_callback();
			};			
			if(param && param.modernUI) p.custom_class = 'fullWidthZebraDialog';
			//当文字内容较多时，自动应用大型消息窗口设置
			else if(content && content.length > 200) p.custom_class = 'hugeZebraDialog';
			
			_dlg.Zebra_Dialog(content?content.toString():'', p);
		},
		//信息提示
		info : function(content,p){
			_publicAlert(content, p, 'information');
		},
		//警告信息
		warning : function(content,p){
			_publicAlert(content, p, 'warning');
		},
		//错误信息
		error : function(content,p){
			_publicAlert(content, p, 'error');
		},
		//成功信息
		success : function(content,p){
			_publicAlert(content, p, 'confirmation');
		},
		//对话框模式
		//按钮定义格式：buttons: [{caption: 'Yes', callback: function() { alert('"Yes" was clicked')}},{...}]
		confirm : function(content,p){
			_publicAlert(content, p, 'question');
		}
	};
})(window.jQuery);
//指定脚本样式依赖
//引入公用脚本库定义
//层级说明：内层依赖外层
//加载顺序：外层优先加载
//require(['/ecp-web-demo/js/common.js'],function(){
	
//});
//异步加载指定插件，插件加载顺序从左至右