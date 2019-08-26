/**
 * 表单错误信息定义
 */
;(function($){
	// jQuery validate
	$.extend($.validator.messages, {
		required: "必填字段",
		remote: "请修正该字段",
		email: "请输入正确的邮箱格式",
		url: "请输入合法的网址，例如：http://www.163.com",
		date: "请输入合法的日期",
		dateISO: "请输入合法的日期 (ISO).",
		number: "请输入合法的数字",
		digits: "只能输入整数",
		creditcard: "请输入合法的信用卡号",
		equalTo: "请再次输入相同的值",
		accept: "请输入拥有合法后缀名的字符串",
		maxlength: $.validator.format("长度最多是 {0} 的字符串"),
		minlength: $.validator.format("长度最少是 {0} 的字符串"),
		rangelength: $.validator.format("长度介于 {0} 和 {1} 之间的字符串"),
		range: $.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
		max: $.validator.format("请输入一个最大为 {0} 的值"),
		min: $.validator.format("请输入一个最小为 {0} 的值"),
		
		alphanumeric: "字母、数字、下划线",
		lettersonly: "必须是字母",
		phone: "请按照xxxx-xxxxxxxx格式填写固定电话",
		
		txtIdCard : "请填写正确的身份证号码",
		checkPhone : "请填写正确的电话号码",
		numberLetterOnly :"只能输入数字或字母",
		priceNumber :"价格格式不合法",
		moneyNumber :"金额格式不合法，请保留两位小数",
		checkDisc : "格式不合法",
		checkInteger : "输入不合格",
		checkSortNo : "请输入5位以内整数",
		priceLength : "价格太大（请输入小于1000000000）",
			
		fullTypeUrl : '请输入合法的网址'
	});
})(window.jQuery);