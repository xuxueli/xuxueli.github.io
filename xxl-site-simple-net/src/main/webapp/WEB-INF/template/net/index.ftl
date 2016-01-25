<#import "/net/common/common.macro.ftl" as netCommon>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>我爱</title>
	
	<@netCommon.common_hosturl />
	<@netCommon.common_style />
	
	<script type="text/javascript" src="${host_url}/js/index.1.js"></script>
	
</head>
<body>
<@netCommon.header />

<!-- content -->
<div class="container">
    
    <!--中央区域--3:1-->
    <div class="row">
    	<!--左侧--3/4-->
        <div class="col-md-8">
			<div id="myCarousel" class="carousel slide">
				<!-- 轮播（Carousel）指标 -->
			   	<ol class="carousel-indicators">
			    	<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
					<li data-target="#myCarousel" data-slide-to="1"></li>
			     	<li data-target="#myCarousel" data-slide-to="2"></li>
			   	</ol>   
			   	<!-- 轮播（Carousel）项目 -->
			   	<div class="carousel-inner">
			      	<div class="item active">
			      		<img src="${host_url}/images/slide1.png" alt="First slide">
			      		<div class="carousel-caption"><a href="${host_url}/wall" style="color:white;">宅8,balabala</a></div>
			      	</div>
			      	<div class="item">
			      		<img src="${host_url}/images/slide2.png" alt="Second slide">
			      		<div class="carousel-caption"><a href="${host_url}/wall" style="color:white;">一面墙,贴张小纸条</a></div>
			      	</div>
			      	<div class="item"><img src="${host_url}/images/slide3.png" alt="Third slide"></div>
			   	</div>
			   	<!-- 轮播（Carousel）导航 -->
			   	<a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a>
			   	<a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a>
			</div> 	
        </div>
        <!--右侧--1/4-->
		<div class="col-md-4" >
			<@netCommon.tips />
		</div>
    </div>
    
	<!--文章菜单-->
	<div class="panel panel-default"></div>
	<#if articleModule?exists>
	<#list articleModule as module>
	<div class="panel panel-default">
		<div class="panel-heading"><h3 class="panel-title">${module.name}</h3></div>
		<div class="panel-body">
			<#if module.children?exists>
			<div class="row">
				<#list module.children as group>
            	<div class="col-md-2"><a href="${host_url}/article/group/${group.menuId}.html">${group.name}</a></div>
            	</#list>
            </div>
			</#if>
		</div>
	</div>
	</#list>
	</#if>
    
	<!--大标题-->
	<div class="jumbotron"><h4>Hey Girl.</h4></div>
	
    
</div>

<@netCommon.footer />
</body>
</html>
