<#import "/common/common.macro.ftl" as netCommon>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>XXL-BLOG</title>
	<@netCommon.common_hosturl />
	<@netCommon.common_style />
</head>
<body navKey="module_${module.menuId}" >
<@netCommon.header login />

<!-- content -->
<div class="container">
    
    <!--中央区域-->
    <div class="row">
    	<!--左侧-->
        <div class="col-md-12">
        
			<div class="panel panel-default">
		        <div class="panel-heading" style="background-color: #FFF;padding-top: 15px;padding-bottom: 15px;" >
		            <ul class="nav nav-pills">
                        <li style="margin-right: 8px;" <#if groupItem.name == group.name>class="active"</#if> >
                            请登录
                        </li>
					</ul>
		        </div>
		        
		        <div class="panel-body">
					请登录
		        </div>
		        
		    </div>
        </div>

    </div>
    

</div>
</body>
</html>
