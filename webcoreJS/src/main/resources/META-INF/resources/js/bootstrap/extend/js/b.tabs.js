/**
 * Bootstrap Tab导航功能扩展，可动态添加，可关闭
 * 
 * @author Terry
 * created : 2016.03.17
 * 
 * 
 */
;(function($){
	"use strict"; // 使用严格模式Javascript
	//常量
	var _constants = {
		tabsMainBox : '#mainFrameTabs',
		closeBtnTemplate : '<i class="icon-remove navTabsCloseBtn" title="关闭"></i>',
		noCloseClass : 'noclose',
		openTabsKey : 'openTabs'
	};
	var bTabs = {
		//处理内容区域占用所有剩余空间
		_resize : function(){
			
		},
		/**
		 * 初始化
		 */
		init : function(){
			var c = _constants;
			var $tabs = $(c.tabsMainBox);
			$('ul.nav-tabs li a',$tabs).each(function(i,row){
				var li = $(this).closest('li');
				if(li && !$(li).hasClass(c.noCloseClass)) $(row).append(c.closeBtnTemplate);
			});
			var openTabs = new Array();
			$('div.tab-content div.tab-pane',$tabs).each(function(i,row){
				openTabs.push($(this).attr('id'));
			});
			//将已打开的Tabs的ID进行缓存
			$($tabs).data(c.openTabsKey,openTabs);
			//为关闭图标绑定事件，并适用于动态新增的
			$('ul.nav-tabs',$tabs).delegate('i', 'click', function(e) {
				var id = $(this).parent().attr('href').replace('#', '');
				bTabs.closeTab(id);
			})
			bTabs._resize();
		},
		/**
		 * 新增一个tab，但如果是已存在的tab则只是激活它，而不再新增
		 * @param id
		 * @param title
		 * @param url
		 */
		addTab : function(id,title,url){
			if(!id || !title || !url) console.error('新增tab时，id,title,url参数为必须传递参数！');
			var c = _constants;
			var $tabs = $(c.tabsMainBox);
			var openTabs = $($tabs).data(c.openTabsKey);
			if(openTabs && $.isArray(openTabs) && openTabs.length>0){
				var exist = false;//是否已存在
				$.each(openTabs,function(i,row){
					if(row == id){
						exist = true;
						return false;
					}
				});
				//若功能已存在，则直接切换
				if(exist){
					$('ul.nav-tabs a[href="#'+id+'"]',$tabs).tab('show');
					return;
				}
			}else openTabs = new Array();
			$('ul.nav-tabs',$tabs).append('<li><a href="#'+id+'" data-toggle="tab">'+title+c.closeBtnTemplate+'</a></li>');
			var content = $('<div class="tab-pane" id="'+id+'"></div>');
			$('div.tab-content',$tabs).append(content);
			//切换到新增加的tab上
			$('ul.nav-tabs li:last a',$tabs).tab('show');
			openTabs.push(id);
			$($tabs).data(c.openTabsKey,openTabs);
			/*
			//读取页面内容
			$.eAjax({
				dataType : 'text',
				url : url,
				success : function(returnInfo){
					$(content).append(returnInfo);
					//初始化页面插件
					bUI.init(content);
				}
			});
			*/
			//iframe方式
			$(content).append('<iframe frameborder="0" scrolling="yes" style="width:100%;height:100%;border:0px;" src="'+url+'"></iframe>');
		},
		/**
		 * 关闭tab
		 * @param id
		 */
		closeTab : function(id){
			var c = _constants;
			var $tabs = $(c.tabsMainBox);
			//移除内容区
			$('#'+id).remove();
			var a = $('ul.nav-tabs a[href="#'+id+'"]',$tabs);
			var li = $(a).closest('li');
			//获得当前tab的前一个tab
			var prevLi = $(li).prev();
			//移除Tab
			li.remove();
			var openTabs = $($tabs).data(c.openTabsKey);
			if(openTabs && $.isArray(openTabs) && openTabs.length>0){
				var index = -1;
				$.each(openTabs,function(i,d){
					if(d == id){
						index = i;
						return false;
					}
				});
				if(index != -1) ebcUtils.arrayItemRemove(openTabs, index);
				$($tabs).data(c.openTabsKey,openTabs);
			}
			//激活被关闭Tab邻的Tab，若没有则不处理
			if(prevLi.size() > 0 ) $('a',$(prevLi)).tab('show');
		}
	};
	window.bTabs = bTabs;
})(window.jQuery);