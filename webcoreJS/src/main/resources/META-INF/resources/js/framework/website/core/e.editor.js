/**
 * 富文本编辑器统一处理类
 */
;(function($){
	"use strict"; // 使用严格模式Javascript
	//富文本编辑器统一样式名称
	var className = 'bEditor';
	window.eEditor = {
		//初始化
		//container : 富文本的父容器，或是富文本自身(jQuery对象)
		init : function(container){
			if(window.KindEditor){
				KindEditor.ready(function(K) {
					var items = ['source','preview','|',
					             'undo', 'redo', '|',
					             'cut', 'copy', 'paste','plainpaste', 'wordpaste', '|',
					             'justifyleft', 'justifycenter', 'justifyright',
					             'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 
					             'bold','italic','underline','strikethrough','|',
					             'lineheight','formatblock','fontname','fontsize','forecolor','hilitecolor','link','unlink','|',
					             'image', 'multiimage','flash', 'media','insertfile','|',
					             'baidumap','fullscreen'];
					var editors = null;
					editors = ($(container).hasClass(className)) ? $(container) : editors = $("." + className, $(container));
					$(editors).each(function() {
						var option = {
							themeType : 'simple',
							items : items,
							//readonlyMode : true,
							//uploadJson: $webroot + '/ecpupload/publicFileUpload',
							//filePostName:'uploadFileObj',
							uploadJson : $webroot + 'upload/editorFileUpload',
							afterCreate : function() {
								this.sync();
							},
					        afterBlur:function(){
					        	this.sync();
					        }
						};
						//若富文本编辑器的文本域设置只读属性，则设置富文本编辑为只读模式
						if($(this).prop('readonly')) option.readonlyMode = true;
						var editor = K.create(this, option);
						editor.sync();
						//$(this).data(eEditor.className,editor);
					});
				});
			}
		},
		//设置富文本编辑器内容
		//obj : 富文本编辑器的选择表达式(jquery)，例：'#myeditor'、'.theEditor'
		//content : 需要填入富文本的内容
		setContent : function(obj,content){
			if($(obj).size()==0 || !content) return;
			if(window.KindEditor){
				KindEditor.html(obj,content);
				KindEditor.sync(obj);
			}else{
				//若没有找到富文本对象，则尝试使用jquery设置文本域的方式处理内容
				$(obj).val(content);
			}
		},
		//获得富文本编辑器内容
		getContent : function(obj){
			if($(obj).size()==0) return ;
			if(window.KindEditor) KindEditor.sync(obj);
			return window.KindEditor ? KindEditor(obj).val() : $(obj).val();
		},
		//设置富文本编辑器只读
		//使用API设置无效，需要设置textarea的readoloy属性时，会自动设置只读样式
		readonly : function(obj,readonly){
			if($(obj).size()==0) return ;
			if($.type(readonly)==undefined) readonly = true;
			if(window.KindEditor) KindEditor.editor.readonly(readonly);
		}
	};
})(window.jQuery);