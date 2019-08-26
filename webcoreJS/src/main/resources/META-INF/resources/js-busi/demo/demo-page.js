$(function() {
	$('#pageControlbar').bPage({
		url : $webroot+"/demo/page/data",
		asyncLoad : true,
		asyncTarget : '#pageMainBox',
		params : {
			userName : 'zhangsan',
			age : 42
		}
	});
	
	$('#pageControlbar2').bPage({
		url : $webroot+"/demo/page/init2",
		asyncLoad : false,
		params : {
			userName : 'zhangsan',
			age : 42
		}
	});
});