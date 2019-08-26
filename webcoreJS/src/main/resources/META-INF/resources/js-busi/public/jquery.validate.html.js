$(function(){
	$.validator.addMethod("htmlcheck",function(value, element, param){
		var val = $htmlCheck.isHtml(value);
		return !val;
	},"输入的信息中存在不符合要求的字符");
	
	$(":input.htmlcheck").each(function(){
		//validate 的 rules 只对第一个有效，所以采用循环的方式；
		$(this).rules("add","htmlcheck");
	});
});

$htmlCheck = {
		reg : new Array(
		     /.*[<]\s*.*[>].*/,
		     /.*[<][\/]\s*.*[>].*/,
		     /.*[A|a][L|l][E|e][R|r][T|t]\s*\(.*\).*/,
			 /.*[W|w][I|i][N|n][D|d][O|o][W|w]\.[L|l][O|o][C|c][A|a][T|t][I|i][O|o][N|n]\s*=.*/,
			 /.*[S|s][T|t][Y|y][L|l][E|e]\s*=.*[X|x]:[E|e][X|x].*[P|p][R|r][E|e][S|s]{1,2}[I|i][O|o][N|n]\s*\(.*\).*/,
			 /.*[D|d][O|o][C|c][U|u][M|m][E|e][N|n][T|t]\.[C|c][O|o]{2}[K|k][I|i][E|e].*/,
			 /.*[E|e][V|v][A|a][L|l]\s*\(.*\).*/,
			 /.*[U|u][N|n][E|e][S|s][C|c][A|a][P|p][E|e]\s*\(.*\).*/,
			 /.*[E|e][X|x][E|e][C|c][S|s][C|c][R|r][I|i][P|p][T|t]\s*\(.*\).*/,
			 /.*[M|m][S|s][G|g][B|b][O|o][X|x]\s*\(.*\).*/,
			 /.*[C|c][O|o][N|n][F|f][I|i][R|r][M|m]\s*\(.*\).*/,
			 /.*[P|p][R|r][O|o][M|m][P|p][T|t]\s*\(.*\).*/,
			 /.*<[S|s][C|c][R|r][I|i][P|p][T|t]>.*<\/[S|s][C|c][R|r][I|i][P|p][T|t]>.*/,
			 /.&[^\"]]*\"[.&[^\"]]*/,
			 /.&[^']]*'[.&[^']]*/
		  ),
		isHtml : function(str){
			for(var i = 0 ;i < $htmlCheck.reg.length ;i++){
			    if($htmlCheck.reg[i].test(str)){
				   // alert($htmlCheck.reg[i]+"   true");
				    return true;
			    }
		    }
			return false;
		}
	};