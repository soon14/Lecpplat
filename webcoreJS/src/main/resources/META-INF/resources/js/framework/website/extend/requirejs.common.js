//设置核心依赖脚本库
require.config({
	//设置一个时间 30，单位：秒；
	//等加载的超时时间
	waitSeconds : 30,
	// 基础路径
	baseUrl : $webroot + 'js',
	// 脚本对应的文件路径，以baseUrl为基础
	paths : {
		// 基础脚本库
		// 'jquery' : 'jquery/jquery-1.11.3.min',
		// 'bootstrap2' : 'bootstrap/core/2.3.2/js/bootstrap.min',
		// 'ZebraDialog' : 'zebra_dialog/zebra_dialog',
		'css' : 'javascript/requirejs/css',
		'text' : 'javascript/requirejs/text',
		'domReady' : 'javascript/requirejs/domReady',
		'jqueryValidate' : 'jquery/jquery.validate',
		'my97datepicker' : 'jquery/my97datepicker/WdatePicker',
		'fineUploader' : 'jquery/jquery.fine-uploader/jquery.fine-uploader.min',
		'holder' : 'javascript/holder/holder',
		'kindeditor' : 'jquery/kindeditor/kindeditor-min',
		'infinitescroll' : 'jquery/datashow/infinite-scroll/jquery.infinitescroll',

		// 功能二次封装扩展库
		// 'eDialog' : 'framework/website/core/e.dialog',
		'ebcForm' : 'framework/manage/extend/ebc.form',
		'ebcDate' : 'framework/manage/extend/ebc.date',
		'eUpload' : 'framework/website/core/e.upload',
		'eValidateMethod' : 'framework/website/core/e.validate.method',
		'eRegional' : 'framework/website/core/e.regional.zh',
		'ePageTop' : 'framework/website/core/e.pagetop',

		// 基于Bootstrap进行二次封装的扩展库
		'bValidate' : 'bootstrap/extend/js/b.validate',
		'btForm' : 'bootstrap/extend/js/b.form',
		'bPlugin' : 'bootstrap/extend/js/b.plugin',
		'bPage' : 'bootstrap/extend/js/b.page',
		'bForm' : 'bootstrap/extend/js/b.ui',

		// 业务组件封装库
		'busSelector' : 'framework/website/extend/bus.selector',

		// 图片轮播
		'jquery-slide' : 'jquery/slides/slides.jquery',
		/* add by gongxq。use in goods detail for goods pictrue for slide */
		'jquery-icheck' : 'jquery/iCheck/icheck',
		'jquery-jqzoom' : 'jquery/jqzoom/jquery.jqzoom-core',

		// 图片轮播
		'jquery-powerSwitch' : 'jquery/powerSwitch/jquery-powerSwitch',

		'scrollTo' : 'jquery/scrollTo/jquery.scrollTo',
		'onePageNav' : 'jquery/nav/jquery.nav',

		// ecp的JS组件；
		'ecp-component' : $webroot + 'js-busi/component'
	},
	// 设置CSS依赖、脚本依赖
	shim : {
		/*
		 * 'bootstrap2' : { //模块化加载CSS，模块化依赖jquery核心脚本 deps : ['jquery'],exports : 'bt' },
		 * 'ZebraDialog' : {
		 * 	  deps:['css!zebra_dialog/css/flat/zebra_dialog.css','css!zebra_dialog/zebra_dialog_extend.css'], 
		 *    exports : 'zdialog' 
		 * }, 'eDialog' : { deps : ['ZebraDialog'], init :
		 * function(){//加载完成后执行的事件 //eDialog.error("我是测试信息弹出框，eDialog已加载完成！"); } },
		 */
		'eValidateMethod' : {
			deps : [ 'jqueryValidate' ]
		},
		'eRegional' : {
			deps : [ 'jqueryValidate' ]
		},
		'kindeditor' : {
			deps : [ 'jquery/kindeditor/lang/zh_CN' ]
		},
		'fineUploader' : {
			deps : [ 'css!jquery/jquery.fine-uploader/fine-uploader-gallery.min.css' ],
			exports : 'fineUploader'//作为在require时，输出到function中的参数，可以将该插件的内容作为参数使用，而不污染全局
		},
		'eUpload' : {
			deps : [ 'fineUploader' ],
			init : function(fineUploader){
				//上传插件已被制作成组件，需要被任意位置调用，所以需要进行显示声明
				window.qq = fineUploader;
			}
		},
		'bPage' : {
			deps : [ 'css!bootstrap/extend/css/bootstrap_page.css' ]
		},
		'busSelector' : {
			deps : [ 'eUpload', 'holder' ]
		},
		'bForm' : {// 这里没有强依赖，仅为设置相关联的脚本自动引入
			deps : [ 'jqueryValidate', 'ebcForm', 'ebcDate', 'eValidateMethod',
					'eRegional', 'my97datepicker', 'bValidate', 'bPlugin',
					'btForm' ]
		}
	}
});
// 页面配置对象
var pageConfig = {

	"config" : function(p) {
		// /初始化，从页面中获取所需要的模块，并增加到依赖中；
		$(".ecp-component").each(function(n) {
			var c = $(this).data("module");
			if (!c || c == "" || c == null || c == "undefined") {
				// 没有该值；
			} else {
				// 避免重复了；否则会初始化多次；
				if ($.isArray(p.plugin) && $.inArray(c, p.plugin) < 0) {
					p.plugin.push(c);
				}
			}
		});

		if (p.plugin && $.isArray(p.plugin) && p.plugin.length > 0) {
			if (require) {
				/**
				 * 使用 domReady! 会等待 dom加载完成，但是在网络环境下，可能出现 domReady 超时的情况；
				 * 修改，不是domReady! 而是将 domReady 做模块引入，并在方法中，增加 domReady的调用；
				 * 
				 * domReady要补充在第一个，后续需要动态获取参数，并传入domReady中；
				 * modify by yugn 2015.11.20
				 */
				//p.plugin.push('domReady!');
				p.plugin = ['domReady'].concat(p.plugin);
				require(p.plugin, function() {
					// /补充了，循环调用插件的 componentInit的方法；
					var domReady = arguments[0];
					var args = new Array();
					for ( var i = 1; i < arguments.length; i++){
						args.push(arguments[i]);
					};
					
					domReady(function() {
						for ( var i = 0; i < args.length; i++) {
							var arg = args[i];
							if ($.isEmptyObject(arg)) {
								// 空的入参，不处理；
							} else {
								if ($.isFunction(arg.componentInit)) {
									// /有该方法；
									arg.componentInit.call();
								}
							}
						}
						if (p.init && $.isFunction(p.init)) p.init(arguments);
					});
				}, function(err) {
					var failedId = err.requireModules && err.requireModules[0];
					var msg = "异常信息：" + err.requireType + ";所需插件：[" + p.plugin + "]; 其中 [" + failedId + "] 加载失败，请重新刷新页面！";
					console.error(msg);
				});
			} else {
				console.error('未加载requireJS脚本库！');
			}
		} else {
			if (p.init && $.isFunction(p.init)) p.init();
		}
	}
};

// //扩展一些公共方法；
(function($) {

	$.ecpPlugin = {
		/**
		 * 用于初始化组件的参数； 入参：el :组件的选择器； defaultOpts : 该组件的默认参数，一般定义在组件的JS文件中；
		 * opts:调用该组件初始化的时候的入参，一般是调用的时候写入的； 处理方式： 1.获取组件定义在HTML上的入参；
		 * 2.如果组件有默认参数，则将 默认参数、HTML入参合并；否则，即为html入参；
		 * 3.如果有调用初始入参，则将第二步的结果与初始入参合并；否则返回第二步结果；
		 */
		parseOptions : function(el, defaultOpts, opts) {
			var htmlOpts = $(el).data();
			var tmpOpts;
			if (defaultOpts) {
				tmpOpts = $.extend({}, defaultOpts, htmlOpts);
			} else {
				tmpOpts = $.extend({}, htmlOpts);
			}

			if (opts) {
				return $.extend({}, tmpOpts, opts);
			} else {
				return tmpOpts;
			}

		}
	};
}(jQuery));