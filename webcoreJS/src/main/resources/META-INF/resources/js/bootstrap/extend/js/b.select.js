/**
 * 基于Bootstrap-Select实现的下拉列表
 * 
 * @author Terry
 * created : 2016.08.02
 * 
 */
;(function($){
	var _className = '.bSelect';
	var _initProp = 'init';
	var _defaults = {
		iconBase : '',
		tickIcon : 'icon-ok',
		selectAllText : '全选',
		deselectAllText : '取消全选',
		noneSelectedText : '没有项目被选中',
		noneResultsText : '没有项目被匹配 {0}',
		selectedTextFormat : 'count',
		countSelectedText: function (numSelected, numTotal) {
			return "{0} 个项目被选中";
		},
		maxOptionsText: function (numAll, numGroup) {
			return ['选中项目达到上限：{n}',
			        '分组项目选中个数达到上限：<b>{n}</b>'];
		},
		dropupAuto : true
	};
	/**
	 * 初始化下拉列表
	 */
	$.fn.bSelect = function(){
		$(this).selectpicker(_defaults);
		var initProp = $(this).data(_initProp);
		if(initProp) $(this).selectpicker('val',initProp);
	};
	//无参数：获得下拉列表的值
	//带参数：设置下拉列表选中的项目
	$.fn.bSelectVal = function(value){
		if(value === undefined) return $(this).selectpicker('val');
		else $(this).selectpicker('val',value);
	};
	/**
	 * 刷新下拉列表项目，通常用于内容动态修改后的数据刷新
	 */
	$.fn.bSelectRefresh = function(){
		$(this).selectpicker('refresh');
	};
})(window.jQuery);