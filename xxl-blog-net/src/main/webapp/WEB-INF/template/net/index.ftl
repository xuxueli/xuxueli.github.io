Loading...
<script>
	<#if articleModule?exists && articleModule?size gt 0 && articleModule[0].children?exists && articleModule[0].children?size gt 0>
		window.location.href = "${host_url}article/group/${articleModule[0].children[0].menuId}.html";
	<#else>
		window.location.href = "${host_url}wall/";
	</#if>
</script>