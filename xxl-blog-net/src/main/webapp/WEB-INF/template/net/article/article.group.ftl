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
<body navKey="module_${module.menuId}" >
<@netCommon.header />

<!-- content -->
<div class="container">
    
    <!--中央区域-->
    <div class="row">
    	<!--左侧-->
        <div class="col-md-9">
        
			<div class="panel panel-default">
		        <div class="panel-heading" style="background-color: #FFF;padding-top: 15px;padding-bottom: 15px;" >
		            <ul class="nav nav-pills">
		            	<#if module?exists && module.children?exists && module.children?size gt 0>
		            		<#list module.children as groupItem>
								<li style="margin-right: 8px;" <#if groupItem.name == group.name>class="active"</#if> >
									<a style="padding: 0 5px 0 5px;" href="${host_url}article/group/${groupItem.menuId}.html">${groupItem.name}</a>
								</li>		            			
		            		</#list>
		            	</#if>
		            </ul>
		        </div>
		        
		        <div class="panel-body">
		        	<#if pageList?exists>
		        	<#list pageList as article>
						<div class="media">
		                    <div class="media-body">
		                        <div class="media-heading">
		                            <a href="${host_url}/article/article/${article.articleId}.html" target="_blank" >
		                            	${article.title}
		                            	<span class="pull-right" >${article.createTime?string('yyyy-MM-dd')}</span>
		                            </a>
		                        </div>
		                        <p style="color: #ccc;font-size: 11px;" >• 47 个赞 • 47 个回复 • 3835 次浏览 </p>
		                    </div>
	                        
		                </div>
						<div style="margin-top: 10px;border-top: 1px solid #f5f5f5;" ></div>
					</#list>
					</#if>
					
					<!--html分页-->
				    <#import "/net/common/common.html.pagination.ftl" as pagination>
					<@pagination.htmlPaging pageNumAll=pageNumAll pageNum=pageNum html_base_url=host_url + filePath index=index/>
		        </div>
		        
		    </div>
        </div>
        <!--右侧-->
		<div class="col-md-3" >
			<@netCommon.right />
		</div>
    </div>
    
	<!--百度新闻-->
	<div class="panel panel-default">
	  	<div class="panel-heading">
	    	<span class="glyphicon glyphicon-th-list"></span>新闻
	  	</div>
	  	<div class="panel-body">
	    	<style type=text/css> 
	    		.baidu{font-size:14px;line-height:24px;font-family:arial} 
				.baidu span{color:#6f6f6f;font-size:12px} a.more{color:#008000;}a.blk{color:#000;font-weight:bold;}
			</style>
			<script language="JavaScript" type="text/JavaScript" src="http://news.baidu.com/n?cmd=1&class=internet&pn=1&tn=newsbrofcu"></script>
	  	</div>
	</div>

</div>
<@netCommon.footer />
</body>
</html>
