<#import "/net/common/common.macro.ftl" as netCommon>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>我爱</title>
	
	<@netCommon.common_hosturl />
	<@netCommon.common_style />

</head>
<body navKey="wall" >
<@netCommon.header />

<!-- content -->
<div class="container">

	<!--中央区域-->
    <div class="row">
    	<!--左侧-->
        <div class="col-md-9">

			<#if pageList?exists >        
	        	<#list pageList as item>
				<div class="well text-justify wall-content">${item.content}</div>
			    </#list>
			    
			    <!--html分页-->
			    <#import "/net/common/common.html.pagination.ftl" as pagination>
				<@pagination.htmlPaging pageNumAll=pageNumAll pageNum=pageNum html_base_url=host_url + "/" + filePath index=index />
			<#else>
	    		<!--大标题-->
				<div class="jumbotron"><h4>相传，很久很久以前.</h4></div>
				<!--大标题-->
				<div class="jumbotron"><h4>在一座山城.</h4></div>
				<!--大标题-->
				<div class="jumbotron"><h4>存在一段迷人的爱情.</h4></div>
			</#if>
			
        </div>
        <!--右侧-->
		<div class="col-md-3" >
			<@netCommon.right />
		</div>
    </div>
    
</div>

<@netCommon.footer />
</body>
</html>
