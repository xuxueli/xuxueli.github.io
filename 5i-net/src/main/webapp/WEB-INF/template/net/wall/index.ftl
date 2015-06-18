<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>我爱</title>
	
	<#include "/net/common/common.style.ftl">
	<#import "/net/net.common.ftl" as netCommon>

</head>
<body>
<@netCommon.header />

<!-- content -->
<div class="container">

	<!--中央区域-->
    <div class="row">
    	<!--左侧-->
        <div class="col-xs-8">
        
        	<#list pageList as item>
			<div class="well text-justify">${item.content}</div>
		    </#list>
		    
		    <!--html分页-->
		    <#import "/net/common/common.html.pagination.ftl" as pagination>
			<@pagination.htmlPaging pageNumAll=pageNumAll pageNum=pageNum html_base_url=base_url+filePath index=index />
			
        </div>
        <!--右侧-->
		<div class="col-xs-4" >
			<@netCommon.tips />
		</div>
    </div>
    
</div>

<@netCommon.footer />
</body>
</html>