//***************
// ebc.utils.js
// 常用函数工具类
// 曾海沥[Terry]
//***************


//为Javascript原生的Array对象增加indexOf方法，获得对象在Array中的索引号
/*
if(!Array.prototype.indexOf){
	Array.prototype.indexOf = function(elt){
		var len = this.length;
		var from  = Number(arguments[1]) || 0;
		from  = (from < 0)? Math.ceil(from): Math.floor(from);
		if(from < 0){
			from += len;
		}
		for(; from < len; from++){
			if(from in this && this[from] === elt){
				return from;
			}
		}
		return -1;
	};
}
*/

//为Javascript原生的Array对象增加remove()方法
//网上的做法是直接this.indexOf(obj)这样做，在FF下是正确的，但在IE下会报
//不支持该属性或方法,原因是Array不能够直接indexOf()，使用join('')后，将
//会返回所有内容，且未添加任何分割符
/*
Array.prototype.remove = function(obj){
	var index = this.indexOf(obj);
	if(index >= 0){
		this.splice(index,1);
		return true;
	}
	return false;
};
*/

/** 
 * 功能与Java一样的MAP对象
 * 使用方式:
 * var myMap = new Map();
 * myMap.put("key","value");
 * var key = myMap.get("key");
 * myMap.remove("key");
 */
"use strict";
function Map(){
	this.elements = new Array();
	this.size = function(){
		return this.elements.length;
	};
	this.isEmpty = function(){
		return (this.elements.length < 1);
	};
	this.clear = function(){
		this.elements = new Array();
	};
	this.put = function(_key, _value){
		this.remove(_key);
		this.elements.push({key: _key, value: _value});
	};
	this.remove = function(_key){
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					this.elements.splice(i, 1);
					return true;
				}
			}
		} catch (e) {
			return false;
		}
		return false;
	};
	this.get = function(_key){
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) { return this.elements[i].value; }
			}
		} catch (e) {
			return null;
		}
	};
	this.element = function(_index){
		if (_index < 0 || _index >= this.elements.length) { return null; }
		return this.elements[_index];
	};
	this.containsKey = function(_key){
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) return true;
			}
		} catch (e){
			return false;
		}
		return false;
	};
	this.values = function(){
		var arr = new Array();
		for (i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].value);
		}
		return arr;
	};
	this.keys = function(){
		var arr = new Array();
		for (i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].key);
		}
		return arr;
	};
}

//+---------------------------------------------------   
//| 判断对象/变量内容是否不为空
//+---------------------------------------------------
function isNotEmpty(obj){	
	if(typeof obj != 'undefined' && obj != null && obj.length != 0){
		if(typeof obj == 'string'){
			return (obj.toUpperCase()!='NULL' && obj.trim()!='') ? true : false;
		}else return true;
	}else return false;
}

/**
 * 根据url打开窗口
 * @param {} url : 链接路径
 * @param {} linkType : href,open
 * @param {} openType : _blank..
 */
function windowOpenUrl(url,linkType,openType){
	if(linkType=="href"){
		window.location.href = url;
	}else if(linkType=="open"){
		window.open(url,openType);
	}
}

/**
 * 判断字数
 * @param {} thisObj  当前对象
 * @param {} showObj  要显示提示的对象
 * @param {} maxChars  最大字符长度
 */
function checkLen(thisObj,showObj,maxChars){
	//var maxChars = 200;
	if (thisObj.value.length > maxChars) thisObj.value = thisObj.value.substring(0,maxChars);  
	var curr = maxChars - thisObj.value.length;  
	$("#"+showObj).html(curr.toString());
}

//+---------------------------------------------------   
//| 判断对象/变量内容是否为空
//+---------------------------------------------------
function isEmpty(obj){
	return !isNotEmpty(obj);
}

//设置在窗口关闭时自动加载的内存释放的操作
/*
$(window).unload(function(){
	if(isIE()){
		CollectGarbage();
	}
});*/

//除法函数，用来得到精确的除法结果
//说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
//调用：accDiv(arg1,arg2)
//返回值：arg1除以arg2的精确结果
function accDiv(arg1,arg2){
	var t1=0,t2=0,r1,r2;
    try{t1=arg1.toString().split(".")[1].length;}catch(e){}
    try{t2=arg2.toString().split(".")[1].length;}catch(e){}
	r1=Number(arg1.toString().replace(".",""));
    r2=Number(arg2.toString().replace(".",""));
    return (r1/r2)*Math.pow(10,t2-t1);
}

//给Number类型增加一个div方法，调用起来更加方便。
Number.prototype.div = function (arg){
	return accDiv(this, arg);
};

//乘法函数，用来得到精确的乘法结果
//说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
//调用：accMul(arg1,arg2)
//返回值：arg1乘以arg2的精确结果
function accMul(arg1,arg2){
	var m=0,s1=arg1.toString(),s2=arg2.toString();
	try{m+=s1.split(".")[1].length;}catch(e){}
	try{m+=s2.split(".")[1].length;}catch(e){}
	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
}

//给Number类型增加一个mul方法，调用起来更加方便。
Number.prototype.mul = function (arg){
	return accMul(arg, this);
};

//加法函数，用来得到精确的加法结果
//说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
//调用：accAdd(arg1,arg2)
//返回值：arg1加上arg2的精确结果
function accAdd(arg1,arg2){
	var r1,r2,m;
	try{r1=arg1.toString().split(".")[1].length;}catch(e){r1=0;}
	try{r2=arg2.toString().split(".")[1].length;}catch(e){r2=0;}
	m=Math.pow(10,Math.max(r1,r2));
	return (Number(arg1)*m + Number(arg2)*m)/m;
}

//给Number类型增加一个add方法，调用起来更加方便。
Number.prototype.add = function (arg){
	return accAdd(arg,this);
};

//减法函数，用来得到精确的加法结果
//说明：javascript的减法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的减法结果。
//调用：accSub(arg1,arg2)
//返回值：arg1减上arg2的精确结果
function accSub(arg1,arg2){
	return accAdd(arg1,-arg2);
}

//给Number类型增加一个sub方法，调用起来更加方便。
Number.prototype.sub = function (arg){
	return accSub(this,arg);
};












/**
 * 工具集合对象
 */
var ebcUtils = {
	/****************************************************内容处理**********************************************************/
	//+---------------------------------------------------   
	//| 获得对象本身的HTML代码
	//| obj：JQuery对象
	//+---------------------------------------------------
	getSelfHtml : function(obj){
		return $('<div>').append(obj.clone()).remove().html();
	},
	/**
	 * 去除字符串中的HTML标签、元素等内容
	 */
	noHTML : function(str){
		var result = '';
		if(str && $.type(str)=='string'){
			result = str.replace(/<(script)[\S\s]*?\1>|<\/?(a|img)[^>]*>/gi, "");
		}
		return result;
	},
	/**
	 * 将html标签转换unicode码显示
	 */
	htmlEncode : function(str){
		if(!str || $.type(str) != 'string') return '';
	    var hex = new Array('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f');
	    var preescape = str;
	    var escaped = "";
	    for(var i = 0; i < preescape.length; i++){
	        var p = preescape.charAt(i);
	        escaped = escaped + escapeCharx(p);
	    }
	    
	    return escaped;
	    
	    function escapeCharx(original){
	        var found=true;
	        var thechar=original.charCodeAt(0);
	        switch(thechar) {
	            case 10: return "<br/>"; break;//newline
	            case 32: return "&nbsp;"; break;//space
	            case 34:return "&quot;"; break;//"
	            case 38:return "&amp;"; break;//&
	            case 39:return "&#x27;"; break;//'
	            case 47:return "&#x2F;"; break;// /
	            case 60:return "&lt;"; break;//<
	            case 62:return "&gt;"; break;//>
	            case 198:return "&AElig;"; break;
	            case 193:return "&Aacute;"; break;
	            case 194:return "&Acirc;"; break; 
	            case 192:return "&Agrave;"; break; 
	            case 197:return "&Aring;"; break; 
	            case 195:return "&Atilde;"; break; 
	            case 196:return "&Auml;"; break; 
	            case 199:return "&Ccedil;"; break; 
	            case 208:return "&ETH;"; break;
	            case 201:return "&Eacute;"; break; 
	            case 202:return "&Ecirc;"; break; 
	            case 200:return "&Egrave;"; break; 
	            case 203:return "&Euml;"; break;
	            case 205:return "&Iacute;"; break;
	            case 206:return "&Icirc;"; break; 
	            case 204:return "&Igrave;"; break; 
	            case 207:return "&Iuml;"; break;
	            case 209:return "&Ntilde;"; break; 
	            case 211:return "&Oacute;"; break;
	            case 212:return "&Ocirc;"; break; 
	            case 210:return "&Ograve;"; break; 
	            case 216:return "&Oslash;"; break; 
	            case 213:return "&Otilde;"; break; 
	            case 214:return "&Ouml;"; break;
	            case 222:return "&THORN;"; break; 
	            case 218:return "&Uacute;"; break; 
	            case 219:return "&Ucirc;"; break; 
	            case 217:return "&Ugrave;"; break; 
	            case 220:return "&Uuml;"; break; 
	            case 221:return "&Yacute;"; break;
	            case 225:return "&aacute;"; break; 
	            case 226:return "&acirc;"; break; 
	            case 230:return "&aelig;"; break; 
	            case 224:return "&agrave;"; break; 
	            case 229:return "&aring;"; break; 
	            case 227:return "&atilde;"; break; 
	            case 228:return "&auml;"; break; 
	            case 231:return "&ccedil;"; break; 
	            case 233:return "&eacute;"; break;
	            case 234:return "&ecirc;"; break; 
	            case 232:return "&egrave;"; break; 
	            case 240:return "&eth;"; break; 
	            case 235:return "&euml;"; break; 
	            case 237:return "&iacute;"; break; 
	            case 238:return "&icirc;"; break; 
	            case 236:return "&igrave;"; break; 
	            case 239:return "&iuml;"; break; 
	            case 241:return "&ntilde;"; break; 
	            case 243:return "&oacute;"; break;
	            case 244:return "&ocirc;"; break; 
	            case 242:return "&ograve;"; break; 
	            case 248:return "&oslash;"; break; 
	            case 245:return "&otilde;"; break;
	            case 246:return "&ouml;"; break; 
	            case 223:return "&szlig;"; break; 
	            case 254:return "&thorn;"; break; 
	            case 250:return "&uacute;"; break; 
	            case 251:return "&ucirc;"; break; 
	            case 249:return "&ugrave;"; break; 
	            case 252:return "&uuml;"; break; 
	            case 253:return "&yacute;"; break; 
	            case 255:return "&yuml;"; break;
	            case 162:return "&cent;"; break; 
	            case '\r': break;
	            default:
	                found=false;
	                break;
	        }
	        if(!found){
	            if(thechar>127) {
	                var c=thechar;
	                var a4=c%16;
	                c=Math.floor(c/16); 
	                var a3=c%16;
	                c=Math.floor(c/16);
	                var a2=c%16;
	                c=Math.floor(c/16);
	                var a1=c%16;
	                return "&#x"+hex[a1]+hex[a2]+hex[a3]+hex[a4]+";";        
	            }else return original;
	        }
	    }
	},
	/**
	 * 使用“\”对特殊字符进行转义，除数字字母之外，小于127使用16进制“\xHH”的方式进行编码，大于用unicode（非常严格模式）
	 * 具体测试，该方法对中文的转换并不友好，暂时不建议使用
	 */
	javascriptEncode : function(str){
	    var hex=new Array('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f');
        
	    function changeTo16Hex(charCode){
	        return "\\x" + charCode.charCodeAt(0).toString(16);
	    }
	    
	    function encodeCharx(original) {
	        var found = true;
	        var thecharchar = original.charAt(0);
	        var thechar = original.charCodeAt(0);
	        switch(thecharchar) {
	            case '\n': return "\\n"; break; //newline
	            case '\r': return "\\r"; break; //Carriage return
	            case '\'': return "\\'"; break;
	            case '"': return "\\\""; break;
	            case '\&': return "\\&"; break;
	            case '\\': return "\\\\"; break;
	            case '\t': return "\\t"; break;
	            case '\b': return "\\b"; break;
	            case '\f': return "\\f"; break;
	            case '/': return "\\x2F"; break;
	            case '<': return "\\x3C"; break;
	            case '>': return "\\x3E"; break;
	            default:
	                found=false;
	                break;
	        }
	        if(!found){
	        	//数字
	            if(thechar > 47 && thechar < 58) return original;
	            //大写字母
	            if(thechar > 64 && thechar < 91) return original;
	            //小写字母
	            if(thechar > 96 && thechar < 123) return original;
	            //大于127用unicode
	            if(thechar>127) {
	                var c = thechar;
	                var a4 = c%16;
	                c = Math.floor(c/16); 
	                var a3 = c%16;
	                c = Math.floor(c/16);
	                var a2 = c%16;
	                c = Math.floor(c/16);
	                var a1 = c%16;
	                return "\\u"+hex[a1]+hex[a2]+hex[a3]+hex[a4]+"";        
	            }else{
	                return changeTo16Hex(original);
	            }
	            
	        }
	    }     
	  
	    var preescape = str;
	    var escaped = "";
	    var i=0;
	    for(i=0; i < preescape.length; i++){
	        escaped = escaped + encodeCharx(preescape.charAt(i));
	    }
	    return escaped;
	},
	//序列化json对象为表单提交的格式
	//obj：json数据对象
	//nameArr：前缀数组，正常使用不需要传递，仅为递归时使用
	serializeObject : function(obj,nameArr){
		if(!obj) return null;
		var result = new Array();
		var namePre = '';
		if(nameArr && $.isArray(nameArr) && nameArr.length > 0 ){
			$.each(nameArr,function(i,row){
				if(row && $.isPlainObject(row)){
					if(row.index == -1){//不设置下标
						namePre += row.name + '.';
					}else{//设置下标
						namePre += row.name + '[' + row.index + '].';
					}					
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
	},
	//数字格式化
	//num :源数字，支持数字或数字字符串 
	//n   :保留小数点倍数，默认为2位小数
	numFormat : function(num,n){
		var temp = 1;
		var temp2 = "";
		if($.type(n)!='number' && !n) n=2;
		for(var i=0;i<n;i++){
			temp = temp * 10;
			temp2 = temp2 + "0";
		}
		if(num==0){
			if(n==0) return "0";
			else return "0."+temp2;
		}
		if(isNaN(num) || num==""){return "";}
		var result = (Math.round(parseFloat(num)*temp)/temp).toString();
		if(result.indexOf(".")==-1){
			if(n==0) return result;
			else return result + "." + temp2;
		}
		else return (result+temp2).substr(0,1 + parseInt(n) + parseInt((result+temp2).indexOf(".")));
	},
	//为数字添加千位符，返回字符串结果
	thousandSeparator : function(num,separator){
		var scode = separator?separator:',';
		if(!/^(\+|-)?(\d+)(\.\d+)?$/.test(num)){  
			return num;  
		}  
		var a = RegExp.$1,b = RegExp.$2,c = RegExp.$3;  
		var re = new RegExp();
		re.compile("(\\d)(\\d{3})("+scode+"|$)");
		while(re.test(b)){
			b = b.replace(re,"$1"+scode+"$2$3");  
		}  
		return a +""+ b +""+ c;  
	},
	//去除千位符
	unThousandSeparator : function(num,separator){
		var result='';
		var scode = separator?separator:',';
		if(num && typeof(num)=='string'){
			result = this.replaceAll(num, scode, '');
		}
		return result;
	},
	//全部替换
	replaceAll : function(srcStr,substr,newstr){
		if(!srcStr || !typeof(srcStr)=='string') return;
		return srcStr.replace(new RegExp(substr,"gm"),newstr);
	},
	//+---------------------------------------------------   
	//| 将JSON对象指定列的所有行数据转换为Array对象
	//| jsonObj:JSON格式变量(该对象须是Array类型)
	//| columnName:JSON对象的属性名
	//+---------------------------------------------------
	convertJsonToArray:function(jsonObj,columnName){
		var array = new Array(jsonObj.lenght);
		for (var i = 0; i < jsonObj.length; i++) {
			array[i] = eval('jsonObj[i].'+columnName);
		}
		return array;
	},
	//+---------------------------------------------------   
	//| 将后台查询返回的JSON数据转换为Javascript能识别的Json格式
	//| obj:JSON格式数据内容
	//+---------------------------------------------------
	parseJsonObject:function(obj){
		return isNotEmpty(obj)?eval('('+obj+')'):'';
	},
	//+---------------------------------------------------   
	//| 将数字字符串转换为数字
	//+---------------------------------------------------
	parseToNum:function(str){
		return isNotEmpty(str) ? parseFloat(str) : 0;
	},
	//对象内容标准化，用于直接alert输出直接调试查看，也用于格式化需要提交到后台的参数
	//JSON对象若报错，则是浏览器默认没有提供JSON对象，通常是IE8以前的浏览器，这时需要手动添加
	toString : function(obj){
		return JSON.stringify(obj);
	},
	/**
	 * 判断文件后缀是否为图片
	 * @param ext 文件后缀，不带*.等，例如：jpg
	 * @returns {Boolean}
	 */
	isImage : function(ext){
    	if(!ext) return false;
    	var imgType = "gif,jpg,jpeg,png,bmp";
    	if(imgType.indexOf(ext.toLowerCase()) == -1) return false;
    	else return true;
	},
	//+---------------------------------------------------   
	//| 将空内容转换为空字符串
	//+---------------------------------------------------
	nullToEmpty:function(str){
		return isNotEmpty(str) ? str : '';
	},
	//限制只输入数字，调用代码为：[ onkeypress="javascript:return checkNum(event,this);"]
	//目前无法解决中文输入的问题，若要解决可以使用onpropertychange来解决，FF下是onInput的事件来解决
	//eventObj：事件对象
	//obj：控件对象自身，通常传递this即可
	//integer：是否为整数，默认为true
	//positive：是否为正数，默认为true
	checkNum:function(eventObj,obj,integer,positive) {
		//获得当前按键的键值
		var key_code = window.event ? window.event.keyCode : eventObj.which ? eventObj.which : eventObj.charCode;
		var int = true,pos = true;
		if($.type(integer)!='undefined' && $.type(integer)=='boolean') int = integer;
		if($.type(positive)!='undefined' && $.type(positive)=='boolean') pos = positive;
		if (!(((key_code >= 48) && (key_code <= 57)) || 
			   (key_code == 13) || 
			   (key_code == 8) || 
			   (key_code == 46 && !int) || 
			   (key_code == 45 && !pos))) {
			try{
				window.event.keyCode = 0 ;
			}catch(e){
				eventObj.cancelBubble = true;
			}		
			return false;
		}
		//限制只允许输入一个小数点符号
		if (key_code == 46 && $(obj).val().indexOf(".") != -1) {
			try{
				window.event.keyCode = 0 ;
			}catch(e){
				eventObj.cancelBubble = true;
			}
			return false;
		}

		//限制只允许输入一个负数符号
		if (key_code == 45 && $(obj).val().indexOf("-") != -1) {
			try{
				window.event.keyCode = 0 ;
			}catch(e){
				eventObj.cancelBubble = true;
			}
			return false;
		}

		return true;
	},
	//通过调用该方法防止事件冒泡
	blockEventUp:function(){
		try{
			window.event.stopPropagation();
		}catch(e){
			//event.cancelBubble = true;
			window.event.cancelBubble=true;
		}
	},
	//将一个数组对象的所有值转换成"aa,bb,cc"格式的字符串
	//若只需要转换成'aaa,bbb,ccc'此类以','分割的字符串时，只需要使用array.toString()就可以达到效果
	arrayToString:function(array,splitKey){
		var key = isNotEmpty(splitKey)?splitKey:',';
		return isNotEmpty(array)?array.join(key):'';
	},
	//将"aa,bb,cc"格式的字符串转换为数组对象
	//splitKey:分割关键字符，若用户未设置，则默认使用','
	stringToArray:function(str,splitKey){
		var key = isNotEmpty(splitKey)?splitKey:',';
		if(isNotEmpty(str)){
			return str.indexOf(key)!=-1 ? str.split(key) : new Array(str); 
		}else{
			return new Array();
		}
	},
	/**
	 * 移除数组中指定下标的内容
	 * @param arr 数组对象
	 * @param index 下标
	 */
	arrayItemRemove : function(arr,index){
		if(arr && $.isArray(arr) && arr.length > 0 && $.type(index)=='number'){
			arr.splice(index,1);
		}
	},
	//根据URL内容返回当前连接符号应该是?还是&
	getUrlConnCode:function(url){
		return url.indexOf('?')!=-1 ? '&' : '?';
	},
	//在指定秒数后清空指定对象内的内容
	delayClearText:function(objId,second){
		if(isNotEmpty(objId) && isNotEmpty(second)){
			var tTime;
			try{
				tTime = parseInt(second);
			}catch(e){
				tTime = 3;
			}
			window.setTimeout("$(\"#"+objId+"\").html('')", tTime * 1000);
		}
	},
	//将对象转换为boolean型数据
	obj2boolean:function(obj){
		if(typeof(obj)=='string'){
			if(obj.toUpperCase()=="TRUE"){
				return true;
			}else if(obj.toUpperCase()=="FALSE"){
				return false;
			}
		}
		return Boolean(obj);
	},
	//获得键盘事件的按键码，兼容IE，FF
	getKeyCode : function (){
		var e = window.event;
		return e.keyCode ? e.keyCode : e.which; //兼容IE和Firefox获得keyBoardEvent对象的键值
	},
	/****************************************************内容转换**********************************************************/
	//URL传递参数中有中文内容时需要进行编码
	encode : function(str){
		return encodeURIComponent(str);
	},
	//对编码进行解码
	decode : function(str){
		return decodeURIComponent(str);
	},
	//数字转字母
	//下标从1开始
	number2letter : function(num){
		if(typeof(num)=='number' && num > 0 && num <= 26){
			var str = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
			return str.substring(num-1, num);
		}
	},
	//数字转大写金额
	digitUppercase : function(n) {
	    var fraction = ['角', '分'];
	    var digit = ['零', '壹', '贰', '叁', '肆','伍', '陆', '柒', '捌', '玖'];
	    var unit = [
	        ['元', '万', '亿'],
	        ['', '拾', '佰', '仟']
	    ];
	    var head = n < 0 ? '欠' : '';
	    n = Math.abs(n);
	    var s = '';
	    for (var i = 0; i < fraction.length; i++) {
	        s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
	    }
	    s = s || '整';
	    n = Math.floor(n);
	    for (var i = 0; i < unit[0].length && n > 0; i++) {
	        var p = '';
	        for (var j = 0; j < unit[1].length && n > 0; j++) {
	            p = digit[n % 10] + unit[1][j] + p;
	            n = Math.floor(n / 10);
	        }
	        s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;
	    }
	    return head + s.replace(/(零.)*零元/, '元').replace(/(零.)+/g, '零').replace(/^整$/, '零元整');
	},
	/**
	 * 文件容量转换具体的bytes数值
	 * @param fileSize 参数类型（String），例：2MB，230KB，1TB等
	 * @param thousand 参数类型（boolean），是否以1000为计算单位，否则以默认的1024为计算单位
	 */
	fileSize2Bytes : function(fileSize,thousand){
    	if(!fileSize) return null;
    	var baseUnit = ($.type(thousand)=='boolean' && thousand===true) ? 1000 : 1024;
		var kb = baseUnit;
        var mb = kb * baseUnit;
        var gb = mb * baseUnit;
    	var tmpCode = fileSize.substring(fileSize.length - 2).toUpperCase();
    	var sizeNumber = fileSize.substring(0, fileSize.length - 2);
    	var num = parseInt(sizeNumber);
    	var result = 0;
    	switch (tmpCode) {
		case 'KB':
			result = accMul(num, kb);
			break;
		case 'MB':
			result = accMul(num, mb);
			break;
		case 'GB':
			result = accMul(num, gb);
			break;
		}
    	return result;
	},
	/****************************************************浏览器系统功能**********************************************************/
	//获得服务器虚拟路径
	getRootPath : function(){
		var strFullPath=window.document.location.href;
		var strPath=window.document.location.pathname;
		var pos=strFullPath.indexOf(strPath);
		var prePath=strFullPath.substring(0,pos);
		var postPath=strPath.substring(0,strPath.substr(1).indexOf('/')+1);
		return(prePath+postPath);
	},
	//获得父窗口文档对象
	getParentDoc:function(){
		var relation = $.getRelationDocument();	
		return relation?relation:window.parent.document;
	},
	//最大化窗口
	maxWindow:function(){
		setTimeout('top.moveTo(0,0)',1);
		setTimeout('top.resizeTo(screen.availWidth,screen.availHeight)',1);
	},
	//关闭当前窗口
	//当前可用度：
	//IE：功能可完全实现
	//Chrome、Firefox等非IE类的浏览器，仅在部分情况下起作用（需要关闭的目标窗口是被打开的或是界面是经过跳转的）
	//否则非IE类的浏览器仅出作出警告后，并不会关闭当前界面
	closeWindow:function(){
		if(ebcUtils.isIE()){
			window.opener=null;
			//window.open("","_self");
			window.close();
		}else{
			try{
				window.open('location', '_self', '');
				window.close();	
			} catch(e){}
            try{
                this.focus();
                self.opener = this;
                self.close();
            } catch(e){}
            try{
            	open(location, '_self').close();
            } catch(e){}
            //当尝试了各种方式都关闭不了窗口，只能将窗口设置为空白页面
            window.location.replace("about:blank");
		}
	},
	//弹出新窗口
	windowOpen : function(url,target){
		var a = document.createElement("a");
		a.setAttribute("href", url);
		if(target == null) target = '';
		a.setAttribute("target", target);
		document.body.appendChild(a);
		if(a.click) a.click();
		else{
			try{
				var evt = document.createEvent('Event');
				a.initEvent('click', true, true);
				a.dispatchEvent(evt);
			}catch(e){
				window.open(url);
			}
		}
		document.body.removeChild(a);
	},
	//判断当前浏览器是否为IE
	isIE:function(){
		return navigator.userAgent.indexOf("MSIE") !== -1 || navigator.userAgent.indexOf("Trident") !== -1;
	},
	//检查flash安装情况
	flashCheck : function (){
		var hasFlash=false;// 是否安装了flash
		var flashVersion=0;// flash版本
	
		if(document.all){
			var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash'); 
			if(swf) {
				hasFlash=true;
				VSwf=swf.GetVariable("$version");
				flashVersion=parseInt(VSwf.split(" ")[1].split(",")[0]); 
			}
		}else{
			if (navigator.plugins && navigator.plugins.length > 0) {
				var swf=navigator.plugins["Shockwave Flash"];
				if (swf) {
					hasFlash=true;
					var words = swf.description.split(" ");
					for (var i = 0; i < words.length; ++i){
						if (isNaN(parseInt(words[i]))) continue;
						flashVersion = parseInt(words[i]);
					}
				}
			}
		}
		return {hasFlash:hasFlash,flashVersion:flashVersion};
	},
	//添加到收藏夹，已经兼容IE、FireFox，Chrome系列暂不支持
	//obj：调用对象，调用者传递this即可
	//opts：自定义内容。opts.title标题，opts.url标题
	addFavorite : function (obj, opts){
	    var _t, _u;
	    if(typeof opts != 'object'){
	        _t = document.title;
	        _u = location.href;
	    }else{
	        _t = opts.title || document.title;
	        _u = opts.url || location.href;
	    }
	    try{
	    	//IE系列浏览器执行
	        window.external.addFavorite(_u, _t);
	    }catch(e){
	    	//FF浏览器执行
	        if(window.sidebar){
	            obj.href = _u;
	            obj.title = _t;
	            obj.rel = 'sidebar';
	        }else{
	        	/*
	        	try{
	        		//chrome核心浏览器
	        		//模拟键盘按下ctrl+d（无效）
	        		var evt = $.Event("keyup",{keyCode:68});
	        		$("#aabb").trigger(evt);
	        	}catch(ee){
	            	alert('抱歉，您所使用的浏览器无法完成此操作。\n\n请使用 Ctrl + D 将本页加入收藏夹！');
	        	}
	        	*/
	        	var info = '抱歉，您所使用的浏览器无法完成此操作。\n\n请使用 Ctrl + D 将本页加入收藏夹！';
	        	window.eDialog ? eDialog.alert(info) : alert(info);
	        }
	    }
	}
};