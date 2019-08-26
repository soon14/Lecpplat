;(function($){
	"use strict";
	window.bUI = {
		//初始化页面对象、控件、jQuery插件等
		init : function(_box){
			var $p = $(_box || document);
			
			//表单校验配置
			if(window.bValidate){
				$("form.required-validate", $p).each(function(i,form) {
					//若表单设置了'ignore-validate'的样式表，则会跳过表单校验初始化
					if(!$(form).hasClass('ignore-validate')) bValidate.labelValidate(this);
				});
			}
			
			if(window.bForm){
				$("form", $p).each(function(){
					//为表单的输入框的标题设置for属性，达到点击标题后，自动将焦点设置到输入框上的功能
					bForm.setHeadNav($(this));
					//设置了required样式的输入控件，为标题增加红色*号标识
					bForm.setRequiredHead($(this));
					if(window.ebcForm){
						var searchBtnId = $(this).data('search-btn-id');
						//form设置了data-search-btn-id属性
						if(searchBtnId) ebcForm.enterSubmitForm($(this),$('#' + searchBtnId));
						else ebcForm.enterSubmitForm();
					}
				});
			}
			
			/**
			 * 下拉列表自动应用插件
			 * 设置noPlugin样式，不自动使用插件
			 */
			//if($.fn.selectpicker) $("select", $p).not('.noPlugin').addClass('selectpicker');
			
			/**
			 * 专用处理在Tab下的DataTables在非默认Tab下表格标题栏会对不齐的问题
			 */
			$('ul.nav-tabs',$p).each(function(){
				var tabBar = $(this);
				var contentBar = $(this).next('div.tab-content');
				$('a[data-toggle="tab"]',$(this)).on('shown',function(e){
					var activeBox = $('div.tab-pane.active',$(contentBar));
					//判断datatables是否打开了水平滚动条
					if($.fn.dataTable && 
							$.fn.dataTable.tables({visible: true, api: true}).context.length > 0 &&
							$.fn.dataTable.tables({visible: true, api: true}).context[0].oInit.scrollX)
						$.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
				});
			});
			
			//表单后台校验显示区域
			$('div.formValidateMessages' , $p).each(function(){
				if($.trim($(this).text())) $(this).slideDown('fast');
			});
			
			
			$(".Wdate", $p).each(function() {
				//$(this).prop('readonly',true).css('cursor','pointer');
				$(this).css('cursor','pointer');
			});
			
			if($.fn.bSelect){
				$(".bSelect", $p).each(function(){
					$(this).bSelect();
				});				
			}

			
			
			//设置输入框的样式为Bdate时，自动为其应用datetimepicker日期插件，该控件为针对Bootstrap这个UI框架特别开发的
			//若在输入框中设置了data-date-format属性来设置日期格式，则优先读取该日期格式，否则默认使用yyyy-mm-dd
			$("input.bDate", $p).each(function() {
				var userFormat = $(this).attr('data-date-format');
				$(this).css('cursor','pointer');
				$(this).datetimepicker({
			        language:  'zh-CN',
			        weekStart: 7,
			        todayBtn:  1,
					autoclose: 1,
					todayHighlight: 1,
					startView: 2,
					format : userFormat?userFormat:'yyyy-mm-dd',
					minView: 2
			    });
			});
			
			//富文本编辑器处理渲染，文本域样式必须设置为"bEditor"
			if(window.eEditor) eEditor.init($p);
			
			if($.fn.bTable){
				$('table.bTable',$p).each(function(){
					$(this).bTable();
				});				
			}
			/**
			 * 为容器设置fullHeightScroll样式，则自动应用滚动条样式
			 */
			if($.fn.slimscroll){
				$('.fullHeightScroll').slimscroll({
	                height: '100%',
	                opacity : 0.3,
	                wheelStep : 5
				});
			}
			
			//管理平台不使用回到顶部功能
			/*
			if($.fn.UItoTop){
				//回到页面顶部
				$().UItoTop({
					text : '回到顶部',
					easingType: 'easeOutElastic',
					scrollSpeed: 200
				});
			}
			*/
			
			//业务公用组件初始化
			if(window.commonInit) commonInit.init($p);
		}
	};
})(window.jQuery);

$(function(){
	bUI.init();
});