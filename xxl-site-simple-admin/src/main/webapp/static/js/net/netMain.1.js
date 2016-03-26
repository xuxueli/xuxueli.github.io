$(function() {
	$(".wallClaw").click(function(){
		$.ajax({
	    	url : base_url + '/net/wallClaw',
		    type : 'post',
		    async : true,
		    data : {
		    	runType : $(this).attr("runType")
		    },
		    dataType:'json',
		    beforeSend : function() {
				$.messager.progress({title:'请稍后'});
			},
			complete:function(){
				$.messager.progress('close');
			},
		    success: function(data){
		    	if (data.code == "S") {
					$.messager.alert('系统提示', '操作成功', 'info');
				} else {
					$.messager.alert('系统提示', data.msg, 'warning');
				}
	        },
	        error: function(){
	        	$.messager.alert('系统提示', '网络异常', 'error');
	        }
	    });
	});
});