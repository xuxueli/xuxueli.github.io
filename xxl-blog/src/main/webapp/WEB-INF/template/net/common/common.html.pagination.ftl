<#-- 
html分页模板,文件名称
------------------
pageNum			:	当前页数、(1-max)
html_base_url	:	html文件路径、
-->
<#macro htmlPagingName pageNum html_base_url index  >
	<#if pageNum == 1 >${html_base_url}${index}.html
	<#else>${html_base_url}${index}_${pageNum}.html</#if>
</#macro>
<#-- 
html分页模板
------------------
pageNumAll		:	总页数、
pageNum			:	当前页数、
html_base_url	:	html文件路径、
-->
<#macro htmlPaging pageNumAll pageNum html_base_url index >
	<#if pageNumAll gt 0 >
		<ul class="pagination">
			<!--pre-->
			<#if pageNum-1 gte 1><li><a href="<@htmlPagingName pageNum=pageNum-1 html_base_url=html_base_url index=index />" >上一页</a></li>
			<#else><li class="disabled"><a>上一页</a></li></#if>
			<!--every pre-->
			<#if pageNum-1 gte 5>
				<li><a href="<@htmlPagingName pageNum=1 html_base_url=html_base_url index=index />" >1</a></li>
				<li><a href="<@htmlPagingName pageNum=2 html_base_url=html_base_url index=index />" >2</a></li>
				<li><a>...</a></li>
				<li><a href="<@htmlPagingName pageNum=pageNum-2 html_base_url=html_base_url index=index />" >${pageNum-2}</a></li>
				<li><a href="<@htmlPagingName pageNum=pageNum-1 html_base_url=html_base_url index=index />" >${pageNum-1}</a></li>
			<#elseif 1 lte (pageNum-1) >
				<#list 1..(pageNum-1) as item>
					<li><a href="<@htmlPagingName pageNum=item html_base_url=html_base_url index=index />" >${item}</a></li>
				</#list>
			</#if>
			<!--every now-->
			<li class="active" ><a href="<@htmlPagingName pageNum=pageNum html_base_url=html_base_url index=index />" >${pageNum}</a></li>
			<!--every next-->
			<#if pageNumAll-pageNum gte 5>
				<li><a href="<@htmlPagingName pageNum=pageNum+1 html_base_url=html_base_url index=index />" >${pageNum+1}</a></li>
				<li><a href="<@htmlPagingName pageNum=pageNum+2 html_base_url=html_base_url index=index />" >${pageNum+2}</a></li>
				<li><a>...</a></li>
				<li><a href="<@htmlPagingName pageNum=pageNumAll-1 html_base_url=html_base_url index=index />" >${pageNumAll-1}</a></li>
				<li><a href="<@htmlPagingName pageNum=pageNumAll html_base_url=html_base_url index=index />" >${pageNumAll}</a></li>
			<#elseif (pageNum+1) lte pageNumAll >
				<#list (pageNum+1)..pageNumAll as item>
					<li><a href="<@htmlPagingName pageNum=item html_base_url=html_base_url index=index />" >${item}</a></li>
				</#list>
			</#if>
		  	<!--next-->
		  	<#if pageNum+1 lte pageNumAll><li><a href="<@htmlPagingName pageNum=pageNum+1 html_base_url=html_base_url index=index  />" >下一页</a></li>
		  	<#else><li class="disabled"><a>下一页</a></li></#if>
		</ul>
	</#if>
</#macro>