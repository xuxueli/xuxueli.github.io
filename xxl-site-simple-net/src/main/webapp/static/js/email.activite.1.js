$(function(){
	
	$("#emailActivate").click(function(){
		$.post(base_url + "/user/emailActivate", $("#emailActivateForm").serialize(), function(data, status) {
			if (data.code == "S") {
				ComAlert.show(1, "激活成功");
				ComAlert.callback = function (){
					window.location.href = base_url;
				}
			} else {
				ComAlert.show(0, "激活失败:" + data.msg);
			}
		});
	});
	
	var email = request.QueryString("email");
	var sendCode = request.QueryString("sendCode");
	
	$("#email").val(email);
	$("#sendCode").val(sendCode);
	
	if ($("#email").val() && $("#sendCode").val()) {
		$("#emailActivate").click();
	}
	
});
