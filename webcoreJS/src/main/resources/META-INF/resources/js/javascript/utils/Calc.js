/**
 * 加减乘除
 */
var Calc = Calc = Calc || {};

/**
 * 加法
 */
Calc.add = function(n1, n2, precision) {
	var baseNum, baseNum1, baseNum2;
	try {
		baseNum1 = n1.toString().split(".")[1].length;
	} catch (e) {
		baseNum1 = 0;
	}
	try {
		baseNum2 = n2.toString().split(".")[1].length;
	} catch (e) {
		baseNum2 = 0;
	}
	baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
	var n = (n1 * baseNum + n2 * baseNum) / baseNum;
	if (precision == undefined) return n;
	return new Number(n).toFixed(precision);
}

/**
 * 减法
 */
Calc.sub = function(n1, n2, precision) {
	var baseNum, baseNum1, baseNum2;
	var _precision;// 精度
	try {
		baseNum1 = n1.toString().split(".")[1].length;
	} catch (e) {
		baseNum1 = 0;
	}
	try {
		baseNum2 = n2.toString().split(".")[1].length;
	} catch (e) {
		baseNum2 = 0;
	}
	baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
	_precision = (baseNum1 >= baseNum2) ? baseNum1 : baseNum2;
	var n = ((n1 * baseNum - n2 * baseNum) / baseNum).toFixed(_precision);
	if (precision == undefined) return n;
	return new Number(n).toFixed(precision);
}
/**
 * 乘法
 */
Calc.mul = function(n1, n2, precision) {
	var baseNum = 0;
	try {
		baseNum += n1.toString().split(".")[1].length;
	} catch (e) {
		baseNum+= 0;
	}
	try {
		baseNum += n2.toString().split(".")[1].length;
	} catch (e) {
		baseNum+=0;
	}
	var n = Number(n1.toString().replace(".", "")) * Number(n2.toString().replace(".", "")) / Math.pow(10, baseNum);
	if (precision == undefined) return n;
	return new Number(n).toFixed(precision);

}
/**
 * 除法
 */
Calc.div = function(n1, n2, precision) {

	var baseNum1 = 0, baseNum2 = 0;
	var baseNum3, baseNum4;
	try {
		baseNum1 = n1.toString().split(".")[1].length;
	} catch (e) {
		baseNum1 = 0;
	}
	try {
		baseNum2 = n2.toString().split(".")[1].length;
	} catch (e) {
		baseNum2 = 0;
	}
	with (Math) {
		baseNum3 = Number(n1.toString().replace(".", ""));
		baseNum4 = Number(n2.toString().replace(".", ""));
		var n = (baseNum3 / baseNum4) * pow(10, baseNum2 - baseNum1);
		if (precision == undefined) return n;
		return new Number(n).toFixed(precision);
	}
}