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
        	<div class="well">
        		<div class="page-header">
					<h4>${article.title}<small class="pull-right">${article.createTime?string('yyyy-MM-dd')}</small></h4>
				</div>
				<p>${article.content}</p>
        	</div>
        </div>
        
        <!--右侧-->
		<div class="col-md-3" >
			<@netCommon.right />
		</div>
		
    </div>
    
	<!--大标题-->
	<div class="jumbotron"><h4>Hey Boy.</h4></div>
    
</div>

<@netCommon.footer />
</body>
</html>