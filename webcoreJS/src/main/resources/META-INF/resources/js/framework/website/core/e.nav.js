/**
 * e.nav.js
 * 菜单、导航相关功能集合
 */
;(function($,w) {
	"use strict"; // 使用严格模式Javascript
	var eNav = {
		//当前菜单节点在cookie中存数据的变量名
		menuForwordKey : 'currentPageMenuCode',
		//在cookie中存储的变量名
		subPageTextKey : 'subPageDescribe',
		//记录父级功能的访问路径，用于用户在转向具体功能页面并显示了指定的路径文本后，
		//使用了回退、前进等操作后，面包屑路径还显示具体功能路径的内容
		lastParentPathKey : 'lastParentPagePath',
		//设置菜单跳转定位
		setForword : function(menuCode){
			if(menuCode){
				$.cookie(eNav.menuForwordKey,menuCode,{path:"/"});
			}else{
				eNav.clearForword();
			}
		},
		//设置独立页面显示文本，由于是显示当前页面，所以不需要进行链接；例如：新增、修改、删除等页面
		setSubPageText : function(text){
			if(text){
				$.cookie(eNav.subPageTextKey,text,{path:"/"});
				$.cookie(eNav.lastParentPathKey,window.location.pathname,{path:"/"});
			} else eNav.clearSubPageText();
		},
		//获得cookie中记录的当前菜单节点的Code
		getForwordCode : function(){
			return $.cookie(eNav.menuForwordKey);
		},
		//获得独立页面显示文本
		getSubPageText : function(){
			return $.cookie(eNav.subPageTextKey);
		},
		//获得最后一次被记录的父功能节点的访问路径（setSubPageText时触发）
		getLastParentPath : function(){
			return $.cookie(eNav.lastParentPathKey);
		},
		//清除记录信息
		clearForword : function(){
			$.removeCookie(eNav.menuForwordKey,{ path: '/' });
		},
		//清除独立页面显示文本
		clearSubPageText : function(){
			$.removeCookie(eNav.subPageTextKey,{ path: '/' });
		},
		//清除最后被记录的父功能访问路径
		clearLastParentPath : function(){
			$.removeCookie(eNav.lastParentPathKey,{ path: '/' });
		},
		/**
		 * 高亮当前访问页面的菜单节点
		 * @param target       目标DOM
		 * @param attr         a标签上的属性，用于查找
		 * @param url          当前页面访问的路径
		 * @returns
		 */
		currentVisitNode : function(target,attr,url){
			if(!target || !attr || !url) return ;
			var node = $('a['+attr+'="'+url+'"]',$(target));
			return $(node).size()>0 ? node : null;
		}
	};
	w.eNav = eNav;
})(window.jQuery,window);