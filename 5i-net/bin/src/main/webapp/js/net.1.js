$(function(){
	// 导航栏,点击事件
	$(".active-nav li").click(function () {
		// 选中样式
		$(this).siblings().removeClass("active");  
		//$(this).toggleClass("active");
		if (!$(this).hasClass("active")) {
			$(this).addClass("active");
		}
		// 目标地址跳转
		var url = $(this).find("a").attr("url");
		if (url) {
			window.location.href = url;
		}
	});
	// 导航栏,默认显示处理
	$(".active-nav li").each(function(){
		var url = $(this).find("a").attr("url");
		if(window.location.pathname.indexOf(url) > -1){
			$(this).siblings().removeClass("active");  
			if (!$(this).hasClass("active")) {
				$(this).addClass("active");
				return;
			}
		}
	});
	
	// 登陆.规则校验
	var loginFormValid = $("#loginForm").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {  
        	userName : {  
                required : true ,
                minlength: 6,
                maxlength: 18
            },  
            password : {  
            	required : true ,
                minlength: 6,
                maxlength: 18
            } 
        }, 
        messages : {  
        	userName : {  
                required :"请输入账号."  ,
                minlength:"账号不应低于6位",
                maxlength:"账号不应超过18位",
            },  
            password : {
            	required :"请输入密码."  ,
                minlength:"密码不应低于6位",
                maxlength:"密码不应超过18位",}
        }, 
		highlight : function(element) {  
            $(element).closest('.form-group').addClass('has-error');  
        },
        success : function(label) {  
            label.closest('.form-group').removeClass('has-error');  
            label.remove();  
        },
        errorPlacement : function(error, element) {  
            element.parent('div').append(error);  
        },
        submitHandler : function(form) {
			$.post("user/login.do", $("#loginForm").serialize(), function(data, status) {
				if (data.code == "S") {
					$('#loginModal').modal('hide');
				} else {
					alert("登陆失败:" + data.msg);
				}
			});
		}
	});
	// 登陆表单清空,登陆框隐藏时
	$("#loginModal").on('hide.bs.modal', function () {
		$("#loginForm")[0].reset()
	})
	
	// 注册.规则校验
	var regFormValid = $("#regForm").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {  
        	userName : {  
                required : true ,
                minlength: 6,
                maxlength: 18
            },  
            password : {  
            	required : true ,
                minlength: 6,
                maxlength: 18
            } ,  
            rePassword : {  
                equalTo:"#regForm input[name='password']"
            }
        }, 
        messages : {  
        	userName : {  
                required :"请输入账号."  ,
                minlength:"账号不可低于6位",
                maxlength:"账号不可超过18位"
            },  
            password : {
            	required :"请输入密码."  ,
                minlength:"密码不可低于6位",
                maxlength:"密码不可超过18位"
            },
            rePassword:{
                equalTo:"两次密码输入不一致"
            }
        }, 
		highlight : function(element) {  
            $(element).closest('.form-group').addClass('has-error');  
        },
        success : function(label) {  
            label.closest('.form-group').removeClass('has-error');  
            label.remove();  
        },
        errorPlacement : function(error, element) {  
            element.parent('div').append(error);  
        },
        submitHandler : function(form) {
			$.post("user/reg.do", $("#regForm").serialize(), function(data, status) {
				if (data.code == "S") {
					$('#regModal').modal('hide');
				} else {
					alert("注册失败:" + data.msg);
				}
			});
		}
	});
	// 登陆表单清空,登陆框隐藏时
	$("#regModal").on('hide.bs.modal', function () {
		$("#regForm")[0].reset()
	})
      
});
