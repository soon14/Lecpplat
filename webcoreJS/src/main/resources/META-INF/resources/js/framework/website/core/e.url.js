/**
 * e.url.js
 * 文件路径相关工具
 */
;(function(){
	window.eUrl = {
		/**
		 * 获得脚本文本当前被引用的路径
		 * @param script
		 */
		getScriptUrl : function(script){
			if(!script || $.type(script) != 'string') return null;
			var result = '';
			$.each(document.scripts, function(i, n) {
				if(n.src.indexOf(script) != -1){
					result = n.src.substr(0,n.src.lastIndexOf(script));
					return false;
				}
			});
			return result;
		}
	};
})(window.jQuery);