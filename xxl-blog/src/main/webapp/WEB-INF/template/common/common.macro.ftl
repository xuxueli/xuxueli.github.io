<#macro common_style>
<!-- jquery -->
<script type="text/javascript" src="${request.contextPath}/static/plugin/jquery/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/plugin/jquery/jquery.validate.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/plugin/jquery/jquery.cookie.js"></script>

<!-- bootstrap -->
<link rel="stylesheet" href="${request.contextPath}/static/plugin/bootstrap-3.3.4/css/bootstrap.min.css" >
<!--	<link rel="stylesheet" href="${request.contextPath}/static/plugin/bootstrap-3.3.4/static/css/bootstrap-theme.min.css" >	-->
<!-- HTML5 Shim 和 Respond.js 用于让 IE8 支持 HTML5元素和媒体查询 -->
<!-- 注意： 如果通过 file://  引入 Respond.js 文件，则该文件无法起效果 -->
<!--[if lt IE 9]>
   <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
   <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
<![endif]-->
<script type="text/javascript" src="${request.contextPath}/static/plugin/bootstrap-3.3.4/js/bootstrap.min.js"></script>

<!-- scrollup -->
<link rel="stylesheet" href="${request.contextPath}/static/plugin/jquery/scrollup/image.css" >
<script type="text/javascript" src="${request.contextPath}/static/plugin/jquery/scrollup/jquery.scrollUp.min.js"></script>

<!-- net -->
<link rel="stylesheet" href="${request.contextPath}/static/css/net.1.css" >
<script type="text/javascript" src="${request.contextPath}/static/js/net.1.js"></script>

<script type="text/javascript">
    var base_url = "${request.contextPath}";
</script>

</#macro>



<#macro header pageName >
<!-- 顶部导航：default  -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	
	<div class="container">
		<!-- ICON -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#net-navbar-collapse">
				<span class="sr-only"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
	      	</button>
			<a class="navbar-brand" style="color:#fff;" href="${request.contextPath}" ><span class="glyphicon glyphicon-home"></span></a>
		</div>
        
		<!-- 导航内容 -->
		<div class="collapse navbar-collapse" id="net-navbar-collapse">
			<!-- 左对齐 -->
			<ul class="nav navbar-nav navbar-left active-nav" >
				<#if groupList?exists>
				<#list groupList as groupA >
                    <li class="nav-click <#if groupA.id == pageName >active</#if> " ><a href="${request.contextPath}/group?g=${groupA.id}" >${groupA.name}</a></li>
				</#list>
				</#if>
			</ul>
			<!--右对齐-->
			<ul class="nav navbar-nav navbar-right login-false" >
                <li><a href="javascript:;" data-toggle="modal" data-target="#loginModal" >登陆</a></li>
        	</ul>
        	<ul class="nav navbar-nav navbar-right login-true" style="display: none" >
				<li>
				    <a href="javascript:;" class="dropdown-toggle loginEmail" data-toggle="dropdown">个人中心<b class="caret"></b></a>
				    <ul class="dropdown-menu">
				       <li class="divider"></li>
				       <li><a href="javascript:;" class="logout" >注销登陆</a></li>
				    </ul>
				</li>
			</ul>
	   </div>
	</div>
</nav>

<!-- 登陆.模态框 -->
<div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="loginModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<!--	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>	-->
            	<h4 class="modal-title" id="loginModalLabel">用户登陆</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal" role="form" id="loginForm">
					<div class="form-group">
						<label for="firstname" class="col-sm-2 control-label">邮箱</label>
						<div class="col-sm-10"><input type="text" class="form-control" name="userName" placeholder="请输入登录账号" minlength="4" maxlength="20" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">密码</label>
						<div class="col-sm-10"><input type="password" class="form-control" name="password" placeholder="请输入登录密码" minlength="4" maxlength="20" ></div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-primary"  >登陆</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
							<#--<label><a href="${request.contextPath}/safe/emailFindPwd.html">忘记密码</a></label>-->
						</div>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>

<!-- 通用提示框.模态框Modal -->
<div class="modal fade" id="comAlert" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<!--	<div class="modal-header"><h4 class="modal-title"><strong>提示:</strong></h4></div>	-->
         	<div class="modal-body"><div class="alert alert-success"></div></div>
         	<div class="modal-footer">
         		<div class="text-center" >
            		<button type="button" class="btn btn-default" data-dismiss="modal" >确认</button>
            	</div>
         	</div>
		</div>
	</div>
</div>
</#macro>



<#macro footer>
<footer class="footer" >
	<!-- footerlinks -->
	<section class="footerlinks">
		<div class="container">
			<div class="info">
				<ul>
					<li><b><span class="glyphicon glyphicon-leaf"></span></b></li>
	          		<li><a href="#">关于我们</a></li>
			    	<li><a href="#">在线反馈</a></li>
			   		<li><a href="#">用户协议</a></li>
			        <li><a href="#">隐私政策</a></li>
	        	</ul>
	      	</div>
	    </div>
	</section>
	<!-- copyrightbottom -->
	<section class="copyrightbottom">
		<div class="container">
			<div class="info">《XXL-BLOG》所有内容来源自网络和网友提供。如站内内容涉及版权、著作权等问题,请及时联系管理员进行处理。</div>
			<div class="info">Copyright © 2015 XXL</div>
		</div>
	</section>
</footer>
</#macro>



<#macro right>
<#--公告板-->
<div class="panel panel-default">
    <div class="panel-body">
        <h5>热米饭里拌什么最好吃？</h5>
        <p>土豆烧牛肉。赫鲁晓夫说土豆烧牛肉是共产主义，此言不虚。</p>
    </div>
</div>

<#--友链-->
<div class="panel panel-default">
    <div class="panel-heading">
        <span class="glyphicon glyphicon-th-list"></span>友情链接
    </div>
    <div class="panel-body">
        <div class="media" style="width: 100%;height: 20px;line-height: 20px;overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-line-clamp: 1;-webkit-box-orient: vertical;" >
            <a href="http://www.mop.com/" target="_blank" >猫扑社区</a>
        </div>
        <div class="media" style="width: 100%;height: 20px;line-height: 20px;overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-line-clamp: 1;-webkit-box-orient: vertical;" >
            <a href="http://tu.duowan.com/m/bxgif/" target="_blank" >爆笑GIT图</a>
        </div>
        <div class="media" style="width: 100%;height: 20px;line-height: 20px;overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-line-clamp: 1;-webkit-box-orient: vertical;" >
            <a href="http://news.baidu.com/" target="_blank" >百度新闻</a>
        </div>
        <div class="media" style="width: 100%;height: 20px;line-height: 20px;overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-line-clamp: 1;-webkit-box-orient: vertical;" >
            <a href="http://www.qiushibaike.com/" target="_blank" >糗事百科</a>
        </div>
    </div>
</div>
</#macro>


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