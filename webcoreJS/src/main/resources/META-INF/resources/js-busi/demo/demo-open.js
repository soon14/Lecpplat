$(function(){
	//获得当前弹出窗口对象
	var _dlg = bDialog.getDialog();
	//获得父窗口传递给弹出窗口的参数集
	var _param = bDialog.getDialogParams(_dlg);
	//获得父窗口设置的回调函数
	var _callback = bDialog.getDialogCallback(_dlg);
	
	
	$('#btnReturn').click(function(){
		bDialog.closeCurrent();
	});
	$('#btnFormSave').click(function(){
		/*
		if($.isFunction(_callback)){
			_callback({
				'userName' : 'wangwu'
			});
		}
		*/
		bDialog.closeCurrent({
			'userName' : 'wangwu'
		});
	});
	eDialog.alert('已接收到父窗口传递的 userName 参数，值为：' + _param.userName);
});