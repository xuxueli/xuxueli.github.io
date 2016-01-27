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
<body>
<@netCommon.header />

<!-- content -->
<div class="container">
    
    <!--中央区域-->
    <div class="row">
    	<!--左侧-->
        <div class="col-md-9">
        
			<div class="panel panel-default">
		        <div class="panel-heading ot-tab-heading">
		            <ul class="nav nav-pills">
		            	<#if module?exists && module.children?exists && module.children?size gt 0>
		            		<#list module.children as groupItem>
								<li style="margin-right: 8px;" <#if groupItem.name == group.name>class="active"</#if> ><a href="${host_url}article/group/${groupItem.menuId}.html">${groupItem.name}</a></li>		            			
		            		</#list>
		            	</#if>
		            </ul>
		        </div>
		        
		        <div class="panel-body">
		        	<ul class="list-group">
		        	<#if pageList?exists>
		        	<#list pageList as article>
						<li class="list-group-item">
							<a href="${host_url}/article/article/${article.articleId}.html" target="_blank" >${article.title}.</a>
							<span class="pull-right" >${article.createTime?string('yyyy-MM-dd')}</span>
						</li>
						<div class="divide"></div>
					</#list>
					</#if>
					</ul>
					
					<!--html分页-->
				    <#import "/net/common/common.html.pagination.ftl" as pagination>
					<@pagination.htmlPaging pageNumAll=pageNumAll pageNum=pageNum html_base_url=host_url + filePath index=index/>
		                 
		        </div>
		        
		    </div>
		    
        </div>
        <!--右侧-->
		<div class="col-md-3" >
			<@netCommon.tips />
		</div>
    </div>
	<!--大标题-->
	<div class="jumbotron"><h4>Hey Boy.</h4></div>
</div>
<@netCommon.footer />
</body>
</html>
