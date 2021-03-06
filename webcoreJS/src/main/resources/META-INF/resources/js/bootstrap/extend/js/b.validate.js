/**
 * 表单验证工具类
 */
;(function($){
	"use strict";
	var bValidate = {
		//使用文字块方式显示错误信息
		labelValidate : function(form){
			$(form).validate({
				focusInvalid : false,
				focusCleanup : false,
				errorElement : "span",//提示信息段的标签类型
				errorClass: 'error',//提示信息段的样式
				validClass: 'valid',
				ignore : '.ignore,:hidden,:file',
				highlight: function(element) {
					$(element).closest('div').addClass("f_error");
				},
				unhighlight: function(element) {
					$(element).closest('div').removeClass("f_error");
				},
	            errorPlacement: function(error, element) {
	                $(element).closest('div').append(error);
	            },
	            invalidHandler: function(form, validator) {}
	        });
		},
		//使用气泡形式显示错误信息
		tipValidate : function(form){
			$(form).validate({
				focusInvalid : false,
				focusCleanup : true,
				errorElement : "span",//生成span对象
				errorClass : "error",
				ignore : ".ignore,:hidden,:file",//设置class中有ignore时，忽略校验
				success : function(label,ele){
					if(ele){
						$(ele).removeClass('error');
						$(ele).closest(".control-group").removeClass('error');
						//$(ele).next("span").remove();
						//$(ele).after('<span class="help-inline"></span>');
						eTip.closeTip($(ele));
					}
				},
				errorPlacement : function(label, ele) {
					if(ele && label.text()){
						$(ele).removeClass('error');
						$(ele).closest(".control-group").addClass('error');
						
						//$(ele).next("span").remove();
						//$(ele).after('<span class="help-inline">'+label.text()+'</span>');
						var tip = $(ele).data('qtip');
						if(!tip){
							eTip.errorTip($(ele), label.text());
						}
					}
				},
				invalidHandler : function(form, validator) {
	//				var errors = validator.numberOfInvalids();
	//				if (errors) {
	//					var message = DWZ.msg("validateFormError", [errors]);
	//					alertMsg.error(message);
	//				}
				}
			});
		},
		//清除表单验证
		clearValidate : function(form){
			if(form){
				$('.control-group',$(form)).each(function(i,row){
					var controlBox = $('.controls',$(row));
					$('[generated],.error',$(controlBox)).not(':input').remove();
					$(controlBox).removeClass('f_error');
				});
				$(form).removeData('validator');
			}
		}
	};
	window.bValidate = bValidate;
})(window.jQuery);