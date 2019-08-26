$(function() {
	
	$("#fresh").on("click",function(){
		$('#pageControlbar').bPageRefresh({

			asyncLoad : true,
			asyncTarget : '#pageMainBox',
			pageNumber : $("#page").val()
		});
	});
	
});