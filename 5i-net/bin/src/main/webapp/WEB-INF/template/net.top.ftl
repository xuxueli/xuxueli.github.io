<!-- 导航栏 -->
<div class="container">
	<!-- 导航栏default  navbar-fixed-top-->
	<nav class="navbar navbar-inverse " role="navigation">
		<!-- 标题 -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#net-navbar-collapse">
				<span class="sr-only"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
	      	</button>
			<a class="navbar-brand" ><span class="glyphicon glyphicon-home"></span></a>
		</div>
		<!-- 响应式导航 -->
		<div class="collapse navbar-collapse" id="net-navbar-collapse">
			<!-- 左对齐 -->
			<ul class="nav navbar-nav navbar-left active-nav" >
				<li><a href="#" url="index.do" >宅吧</a></li>
				<li><a href="#" url="wall.do" >小纸条</a></li>
			</ul>
			<!--右对齐-->
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
				    <a href="#" class="dropdown-toggle" data-toggle="dropdown">小窝 <b class="caret"></b></a>
				    <ul class="dropdown-menu">
				       <li><a href="#" data-toggle="modal" data-target="#loginModal" >登陆</a></li>
				       <li class="divider"></li>
				       <li><a href="#" data-toggle="modal" data-target="#regModal" >注册</a></li>
				    </ul>
				</li>
			</ul>
	   </div>
	</nav>
</div>


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
						<label for="firstname" class="col-sm-2 control-label">账号</label>
						<div class="col-sm-10"><input type="text" class="form-control" name="userName" placeholder="请输入账号" minlength="6" maxlength="18" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">密码</label>
						<div class="col-sm-10"><input type="password" class="form-control" name="password" placeholder="请输入密码" minlength="6" maxlength="18" ></div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
							<button type="submit" class="btn btn-primary"  >登陆</button>
							<label><a href="#">忘记密码</a></label>
						</div>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>

<!-- 注册.模态框 -->
<div class="modal fade" id="regModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >用户注册</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal" role="form" id="regForm">
					<div class="form-group">
						<label for="firstname" class="col-sm-2 control-label">账号</label>
						<div class="col-sm-10"><input type="text" class="form-control" name="userName" placeholder="请输入账号" minlength="6" maxlength="18" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">密码</label>
						<div class="col-sm-10"><input type="password" class="form-control" name="password" placeholder="请输入密码" minlength="6" maxlength="18" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">确认</label>
						<div class="col-sm-10"><input type="password" class="form-control" name="rePassword" placeholder="请输入密码" minlength="6" maxlength="18" ></div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
							<button type="submit" class="btn btn-primary"  >注册</button>
						</div>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>