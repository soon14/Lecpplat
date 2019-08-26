$(function(){
	$('#btnOpenWindow').click(function(){
		bDialog.open({
			title : '弹出窗口',
			width : 400,
			height : 450,
			url : GLOBAL.WEBROOT + '/demo/open',
			scroll : true,
			params : {
				'userName' : 'zhangsan'
			},
			callback:function(data){
				if(data && data.result && data.result.length > 0 ){
					eDialog.alert('已完成弹出窗口操作！接收到弹出窗口传回的 userName 参数，值为：' + data.results[0].userName );
				}else{
					eDialog.alert('弹出窗口未回传参数',$.noop,'error');
				}
			}
		});
	});
	$('#btnAlert').click(function(){
		eDialog.alert("这是一个消息提示框！");
	});
	$('#btnStatus').click(function(){
		var btn = $(this);
		btn.button('loading');//设置按钮为处理状态，并为中读，不允许点击
		setTimeout(function () {
			btn.button('reset');
		}, 3000);
	});
	$('.btns_state').find('.btn').click(function(e) {
		$this_btn = $(this);
		$this = $this_btn.closest('.btns_state');
		if(!$this_btn.hasClass('active')) {
			$this.find('.btn_txt').html($this_btn.text()+" 激活");
		} else {
			$this.find('.btn_txt').html($this_btn.text()+" 空闲");
		}
		e.preventDefault();
	});
	$('#btnGetEditorText').click(function(){
		alert($('#editor').val());
	});
	$('#btnSetEditorText').click(function(){
		//$('#editor').val('test text');
		KindEditor.html('.bEditor','aaaabbbb');
		KindEditor.sync('.bEditor');
	});
	$('#pageControlbar').bPage({
		url : $webroot + '/demo/openPage',
		totalPage : 3,
		totalRow : 18,
		pageSize : 6,
		pageNumber : 1,
		asyncLoad : true,
		asyncTarget : '#pageMainBox',
		params : {
			userName : 'zhangsan',
			age : 42
		}
	});
	$('.bAutocomplete').bAutocomplete(['aaa','bbb','abc']);
	hljs.initHighlightingOnLoad();
});