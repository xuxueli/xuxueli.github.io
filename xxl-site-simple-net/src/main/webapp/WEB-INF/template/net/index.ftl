Loading...
<script>
	<#if articleModule?exists>
	<#list articleModule as module>
		<#if module.children?exists>
		<#list module.children as group>
		<#if group_index = 0>
			window.location.href = "${host_url}article/group/${group.menuId}.html";
			return;	
		</#if>
		</#list>
		</#if>
	</#list>
	</#if>
	window.location.href = "${host_url}wall/";
</script>