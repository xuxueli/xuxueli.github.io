<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>我爱</title>
	<#include "./common/common.style.ftl">
	<script type="text/javascript" src="${base_url}/plugin/jquery/jquery.validate.min.js"></script>
	
	<link rel="stylesheet" href="${base_url}/css/index.1.css" >
	<script type="text/javascript" src="${base_url}/js/index.1.js"></script>

</head>
<body>
<!--top -->
<#include "./net.top.ftl">

<!-- content -->
<div class="container">
	<!--大标题-->
	<div class="jumbotron"><h1>Hello World.</h1></div>
    
    <!--中央区域--3:1-->
    <div class="row">
    	<!--左侧--3/4-->
        <div class="col-xs-9">
        	
			<div class="panel panel-default">
				<div class="panel-heading"><h3 class="panel-title">模块1</h3></div>
				<div class="panel-body">
				   	<div class="row">
		            	<div class="col-xs-3"><a href="#">分组1</a><span class="badge">50</span></div>
		            	<div class="col-xs-3"><a href="#">分组2</a><span class="badge">50</span></div>
		            	<div class="col-xs-3"><a href="#">分组3</a><span class="badge">50</span></div>
		            	<div class="col-xs-3"><a href="#">分组4</a><span class="badge">50</span></div>
		            </div>
		            <hr>
		            <div class="row">
		            	<div class="col-xs-3"><a href="#">分组1</a><span class="badge">50</span></div>
		            	<div class="col-xs-3"><a href="#">分组2</a><span class="badge">50</span></div>
		            	<div class="col-xs-3"><a href="#">分组3</a><span class="badge">50</span></div>
		            	<div class="col-xs-3"><a href="#">分组4</a><span class="badge">50</span></div>
		            </div>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading"><h3 class="panel-title">模块2</h3></div>
				<div class="panel-body">
				   	<div class="row">
		            	<div class="col-xs-3"><a href="#">分组1</a><span class="badge">50</span></div>
		            	<div class="col-xs-3"><a href="#">分组2</a><span class="badge">50</span></div>
		            	<div class="col-xs-3"><a href="#">分组3</a><span class="badge">50</span></div>
		            	<div class="col-xs-3"><a href="#">分组4</a><span class="badge">50</span></div>
		            </div>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading"><h3 class="panel-title">模块3</h3></div>
				<div class="panel-body">
				   	<div class="row">
		            	<div class="col-xs-3"><a href="#">分组1</a><span class="badge">50</span></div>
		            	<div class="col-xs-3"><a href="#">分组2</a><span class="badge">50</span></div>
		            	<div class="col-xs-3"><a href="#">分组3</a><span class="badge">50</span></div>
		            	<div class="col-xs-3"><a href="#">分组4</a><span class="badge">50</span></div>
		            </div>
				</div>
			</div>
			
        </div>
        <!--右侧--1/4-->
		<div class="col-xs-3" >
			<div class="panel-group" id="accordion">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapseoOne">系统消息1</a></h4>
				    </div>
				    <div id="collapseoOne" class="panel-collapse collapse">
						<div class="panel-body">Nihil anim keffiyeh helvetica, craft beer labore wes anderson .</div>
				    </div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">系统消息2</a></h4>
				    </div>
				    <div id="collapseTwo" class="panel-collapse collapse">
						<div class="panel-body">Nihil anim keffiyeh helvetica, craft beer labore wes anderson .</div>
				    </div>
				</div>
			</div>
		</div>
    </div>
    
	<!--大标题-->
	<div class="jumbotron"><h1>Hey Girl.</h1></div>
    
</div>

<!-- bottom -->
<#include "./net.bottom.ftl">

</body>
</html>