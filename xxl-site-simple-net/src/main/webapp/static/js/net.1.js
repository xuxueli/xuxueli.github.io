$(function(){
	// 导航栏,选中样式处理
	$(".nav-click").each(function(){
		if( $('body').attr('navKey') == $(this).attr('navKey')){
			$(this).siblings().removeClass("active");  
			if (!$(this).hasClass("active")) {
				$(this).addClass("active");
				return;
			}
		}
	});
	
	// scrollup
	$.scrollUp({
		animation: 'fade',	// fade/slide/none
		scrollImg: true
	});
	
	// 图片视频，圆角
	$("img,video").addClass("img-rounded");	// 响应式 : "carousel-inner img-responsive img-rounded"
	
	// 排版样式,手动自适应
	if (nPlatform != 0) {
		$("body>.container>.row>div").each(function(){
			$(this).attr("class", "col-xs-12");
		});
	}
	
	// 登陆.规则校验
	var loginFormValid = $("#loginForm").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {  
        	email : {  
                required : true ,
                email: true
            },  
            password : {  
            	required : true ,
                minlength: 6,
                maxlength: 18
            } 
        }, 
        messages : {  
        	email : {  
                required :"请输入邮箱账号."  ,
                email: "请输入格式正确的邮箱账号"
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
			$.post(base_url + "/user/login", $("#loginForm").serialize(), function(data, status) {
				if (data.code == "S") {
					$('#loginModal').modal('hide');
					
					ComAlert.show(1, "登陆成功,《我爱》致力于为您提供最精致的服务");
					ComAlert.callback = function (){
						window.location.reload();
					}
				} else {
					ComAlert.show(0, "登陆失败:" + data.msg);
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
            email : {  
                required : true ,
                email: true
            },  
            password : {  
            	required : true ,
                minlength: 6,
                maxlength: 18
            } ,  
            rePassword : { 
            	required : true ,
                equalTo:"#regForm input[name='password']"
            }
        }, 
        messages : {  
            email : {  
                required :"请输入邮箱."  ,
                email: "请输入正确的邮箱地址"
            },
            password : {
            	required :"请输入密码."  ,
                minlength:"密码不可低于6位",
                maxlength:"密码不可超过18位"
            },
            rePassword:{
            	required :"请输入确认密码."  ,
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
			$.post(base_url + "/user/reg", $("#regForm").serialize(), function(data, status) {
				if (data.code == "S") {
					$('#regModal').modal('hide');
					ComAlert.show(1, "恭喜您注册成功,请前往邮箱激活账号");
				} else {
					ComAlert.show(0, "注册失败:" + data.msg);
				}
			});
		}
	});
	// 登陆表单清空,登陆框隐藏时
	$("#regModal").on('hide.bs.modal', function () {
		$("#regForm")[0].reset()
	});
	
	// 校验登陆状态
	$(".login-true").hide();
	$(".login-false").show();
	if ($.cookie('login_identity')) {
		$.post(base_url + "user/loginCheck", function(data, status) {
			if (data.code == "S") {
				$(".login-false").hide();
				$(".login-true").show();
				$(".login-true .loginEmail").html(data.returnContent.email + '<b class="caret"></b>');
			}
		}, 'json');
	}
	
	// 注销登陆
	$(".logout").click(function(){
		$.post(base_url + "/user/logout", function(data, status) {
			if (data.code == "S") {
				ComAlert.show(1, "注销登陆成功");
				window.location.reload();
			}
		}, 'json');
	});
	
});

//通用提示
var ComAlert = {
	show:function(type, msg){
		// 弹框初始
		if (type == 1) {
			$('#comAlert .alert').attr('class', 'alert alert-success');
		} else {
			$('#comAlert .alert').attr('class', 'alert alert-warning');
		}
		$('#comAlert .alert').html(msg);
		$('#comAlert').modal('show');
		// 监听关闭
		$("#comAlert").on('hide.bs.modal', function () {
			ComAlert.callback();
		});
	},
	hide:function(){
		$('#comAlert').modal('hide');
	},
	callback:function(){
		// TODO
	}
};
