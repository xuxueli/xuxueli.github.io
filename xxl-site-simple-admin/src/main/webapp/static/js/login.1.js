$(function(){
	
	// 刷新验证码
	$("#randCodeImg").click(function(){
		$(this).attr("src", "kaptcha.do?v=" + Math.random() );
	});
	$("#randCodeImg").click();
	
});

// 重置
function clearForm(){
	$('#loginForm').form('reset');
}

// 登陆
function submitForm(){
	
	// 校验参数
	if (!$('#loginForm').form('validate')) {
		return;
	}
	// 登陆
	$.ajax({
    	url : 'login.do',
	    type : 'post',
	    async : true,
	    data : $('#loginForm').serialize(),
	    dataType:'json',
	    beforeSend : function() {
			$.messager.progress({title:'请稍后', msg:'请求中...'});
		},
		complete:function(){
			$.messager.progress('close');
		},
	    success: function(data){
	    	if(data.code == "S"){
				window.location.href = "./home.do";
	    	} else{
	    		$.messager.alert('系统提示', data.msg, 'warning');
		    }
        },
        error: function(){
        	alert("网络异常");
        }
    });
	
	// 提交表单
	/*$.messager.progress({title:'玩儿命登录中……'});	// 显示进度条	
	$('#loginForm').form('submit', {
		url : 'login.do',
		dataType:'json',
		onSubmit: function(){
			// 参数校验
			var isValid = $(this).form('validate');
			if (!isValid){
				$.messager.progress('close');
			}
			return isValid;	// 返回false终止表单提交
		},
		success: function(data){
			var data = eval('(' + data + ')');
			
			$.messager.progress('close');
			if(data.code == "S"){
				window.location.href = "./home.do";
	    	} else{
	    		$.messager.alert('系统提示', data.msg, 'warning');
		    }
		}
	});*/
	
}
