var nPlatform = 0;		// 操作平台:0-PC、1-Android、2-IOS
(function(){
	
	// 操作平台:0-PC、1-Android、2-IOS
	if (navigator.userAgent.indexOf('Window') > -1) {
		nPlatform = 0;
	} else if(navigator.userAgent.indexOf('Android') > -1){
		nPlatform = 1;
	} else if(navigator.userAgent.indexOf('Mac OS') > -1){
		nPlatform = 2;
	}
	
})();
