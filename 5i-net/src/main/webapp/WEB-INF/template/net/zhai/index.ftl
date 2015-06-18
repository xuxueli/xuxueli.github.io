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
        	<#if articleModule?exists>
        	<#list articleModule as module>
        	<div class="panel panel-default">
				<div class="panel-heading"><h3 class="panel-title">${module.name}</h3></div>
				<div class="panel-body">
					<#if module.children?exists>
					<div class="row">
					<#list module.children as group>
		            	<div class="col-xs-3"><a href="${base_url}zhai/group/${group.menuId}.html">${group.name}</a></div>
		            </#list>
		            </div>
					</#if>
				</div>
			</div>
			</#list>
			</#if>
        </div>
        <!--右侧-->
		<div class="col-xs-4" >
			<@netCommon.tips />
		</div>
    </div>
    
	<!--大标题-->
	<div class="jumbotron"><h4>Hey Boy.</h4></div>
    
</div>

<@netCommon.footer />
</body>
</html>