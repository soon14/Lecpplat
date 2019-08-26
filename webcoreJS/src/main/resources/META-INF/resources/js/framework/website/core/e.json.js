/**
 * json数据格式处理类
 * created by Terry
 * date : 2016.01.27
 * 
 * 
 */
;(function($,w) {
	"use strict"; // 使用严格模式Javascript
	var eJson = {
		/**
		 * 处理标准与后台数据交互的数组格式处理
		 * 支持数据格式：[{name:'',value:''},{...}]
		 * 注意：仅支持
		 */
		//根据value值获得name名称
		getNameByValue : function(data,value){
			var result;
			if(data && $.isArray(data) && data.length > 0 && $.type(value)!='undefined'){
				$.each(data,function(i,row){
					if(row.value == value){
						result = row.name;
						return false;
					}
				});
			}
			return result;
		},
		//根据name名称获得value值
		getValueByName : function(data,name){
			var result = '';
			if(data && $.isArray(data) && data.length > 0 && $.type(name)=='string'){
				$.each(data,function(i,row){
					if(row.name === name){
						result = row.value;
						return false;
					}
				});
			}
			return result;
		},
		/**
		 * 处理普通格式JSON数据
		 */
		//序列化json对象为表单提交的格式
		//obj：json数据对象格式如下
		//     {aa:1,bb:2,cc:{a:11,b:22,c:33}}
		//nameArr：前缀数组，正常使用不需要传递，仅为递归时使用
		//return：返回数据格式如下
		//        [{name:'a',value:1},{name:'b',value:2},{...}]
		serializeObject : function(obj,nameArr){
			if(!obj) return null;
			var result = new Array();
			var namePre = '';
			if(nameArr && $.isArray(nameArr) && nameArr.length > 0 ){
				$.each(nameArr,function(i,row){
					if(row && $.isPlainObject(row)){
						//处理下标
						namePre += (row.index == -1) ? row.name + '.' : row.name + '[' + row.index + '].';
					}
				});
			}
			$.each(obj,function(i,row){
				if($.type(row) == 'object'){//处理子对象
					var tmpArr = (nameArr && $.isArray(nameArr) && nameArr.length > 0)? nameArr : new Array();
					tmpArr = tmpArr.concat([{name : i,index : -1}]);
					result = result.concat(ebcUtils.serializeObject(row,tmpArr));
				}else if($.isArray(row)){//处理数组
					if(row.length > 0){
						$.each(row,function(arrIdx,arrRow){
							var tmpArr = (nameArr && $.isArray(nameArr) && nameArr.length > 0)? nameArr : new Array();
							tmpArr = tmpArr.concat([{name : i,index : arrIdx}]);
							result = result.concat(ebcUtils.serializeObject(arrRow,tmpArr));
						});
					}
				}else{//处理对象
					var itemObj = {};
					itemObj.name = namePre + i;
					itemObj.value = row;
					result.push(itemObj);
				}
			});
			return result;
		}
	};
	w.eJson = eJson;
})(window.jQuery,window);