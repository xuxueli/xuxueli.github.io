<#import "/common/common.macro.ftl" as netCommon>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>XXL-BLOG</title>
	<@netCommon.common_style />
</head>
<body>
<@netCommon.header groupAId />

<!-- content -->
<div class="container">
    
    <!--中央区域-->
    <div class="row">
    	<!--左侧-->
        <div class="col-md-9">
        
			<div class="panel panel-default">
		        <div class="panel-heading" style="background-color: #FFF;padding-top: 15px;padding-bottom: 15px;" >
		            <ul class="nav nav-pills">

						<#if groupList?exists>
							<#list groupList as groupA >
							<#if groupAId == groupA.id && groupA.children?exists && groupA.children?size gt 0 >
								<#list groupA.children as groupB>
                                    <li style="margin-right: 8px;" <#if groupB.id == groupBId >class="active"</#if> >
                                        <a style="padding: 0 5px 0 5px;" href="${request.contextPath}/group?g=${groupB.id}">${groupB.name}</a>
                                    </li>
								</#list>
							</#if>
							</#list>
						</#if>
		            </ul>
		        </div>
		        
		        <div class="panel-body">

					<#-- 文章列表 -->
		        	<#if pageList?exists>
		        	<#list pageList as article>
						<div class="media">
		                    <div class="media-body">
		                        <div class="media-heading">
		                            <a href="${request.contextPath}/article/?a=${article.id}" target="_blank" >
		                            	${article.title}
		                            	<span class="pull-right" >${article.addTime?string('yyyy-MM-dd')}</span>
		                            </a>
		                        </div>
		                        <p style="color: #ccc;font-size: 11px;" >• 47 个赞 • 3835 次浏览 </p>
		                    </div>
	                        
		                </div>
						<div style="margin-top: 10px;border-top: 1px solid #f5f5f5;" ></div>
					</#list>
					</#if>

					<#-- 分页 -->
					<#if pageTotal gt 0 >

						<#if groupBId gt 0>
							<#assign pageBaseURL = request.contextPath + "/group?g=" + groupBId + "&p=" />
						<#else>
							<#assign pageBaseURL = request.contextPath + "/group?g=" + groupAId + "&p=" />
						</#if>

						<ul class="pagination">
							<!--pre-->
							<#if pageNum gt 1><li><a href="${pageBaseURL}${pageNum-1}" >上一页</a></li>
							<#else><li class="disabled"><a>上一页</a></li></#if>
							<!--every pre-->
							<#if pageNum - 1 gt 4 >
								<li><a href="${pageBaseURL}1" >1</a></li>
								<li><a href="${pageBaseURL}2" >2</a></li>
								<li><a>...</a></li>
								<li><a href="${pageBaseURL}${pageNum-2}" >${pageNum-2}</a></li>
								<li><a href="${pageBaseURL}${pageNum-1}" >${pageNum-1}</a></li>
							<#elseif pageNum gt 1 >
								<#list 1..(pageNum-1) as item>
									<li><a href="${pageBaseURL}${item}" >${item}</a></li>
								</#list>
							</#if>
							<!--every now-->
							<li class="active" ><a href="${pageBaseURL}${pageNum}" >${pageNum}</a></li>
							<!--every next-->
							<#if pageTotal - pageNum gt 4 >
								<li><a href="${pageBaseURL}${pageNum+1}" >${pageNum+1}</a></li>
								<li><a href="${pageBaseURL}${pageNum+2}" >${pageNum+2}</a></li>
								<li><a>...</a></li>
								<li><a href="${pageBaseURL}${pageTotal-1} >${pageTotal-1}</a></li>
								<li><a href="${pageBaseURL}${pageTotal}" >${pageTotal}</a></li>
							<#elseif pageTotal gt pageNum >
								<#list (pageNum+1)..pageTotal as item>
									<li><a href="${pageBaseURL}${item}" >${item}</a></li>
								</#list>
							</#if>
							<!--next-->
							<#if pageNum lt pageTotal><li><a href="${pageBaseURL}${pageNum+1}" >下一页</a></li>
							<#else><li class="disabled"><a>下一页</a></li></#if>
						</ul>
					</#if>

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
