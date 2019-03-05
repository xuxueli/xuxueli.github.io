### html编辑器—kindeditor
官网地址：http://kindeditor.net/demo.php

使用步骤：
- 1、下载编辑器；
- 2、解压至项目指定目录；
- 3、修改HTML页面
```
<script charset="utf-8"src="/editor/kindeditor.js"></script>
<script charset="utf-8"src="/editor/lang/zh_CN.js"></script>
<script>KindEditor.ready(function(K){window.editor=K.create('#editor_id');});</script>
<textareaid="editor_id"name="content"style="width:700px;height:300px;">&lt;strong&gt;HTML内容&lt;/strong&gt;</textarea>
```
上传图片地址配置：upload_json.jsp、file_manager_json.jsp

### html编辑器—UEditor（图片上传至，本项目/七牛云）
官网：http://ueditor.baidu.com/website/

**maven依赖：**
```
<!-- org.json (ueditor依赖) -->
<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20140107</version>
</dependency>
<!-- ueditor (上传部分源码已被我修改) -->
<dependency>
    <groupId>com.baidu.ueditor</groupId>
    <artifactId>ueditor</artifactId>
    <version>1.1.1</version>
    <scope>system</scope>
    <systemPath>${basedir}/src/main/webapp/WEB-INF/lib/ueditor-1.1.1-qiniu-xxl.jar</systemPath>
</dependency>
<!-- qiniu -->
<dependency>
    <groupId>com.qiniu</groupId>
    <artifactId>qiniu-java-sdk</artifactId>
    <version>6.1.9</version>
</dependency>
```

**配置步骤：**
- 1、下载最新版；
- 2、放置在项目制定目录；
- 3、jsp/lib目录下JARcopy到项目lib目录下 （图片上传七牛云，ueditor.xxx.jar需要定制）；

**图片上传本项目目录下:**
- 01：引入ueditor的JAR：ueditor-1.1.1.jar
- 02：修改：config.json：常规配置,文件上传/管理路径：修改*UrlPrefix("/5i-admin"=项目域名)和*PathFormat("/uploadfiles/im..."=路径加文件配置);
```
-------localhost
"imageUrlPrefix": "/5i-admin（或http://localhost:8080/5i-admin）", /* 图片访问路径前缀 */ （*UrlPrefix 修改为项目名，因为默认项目前缀为空，所以通常图片路径不正确）
"imagePathFormat": "/uploadfiles/image/{yyyy}{mm}{dd}/{time}{rand:6}", /* 上传保存路径,可以自定义保存路径和文件名格式 */
--------线上
"imageUrlPrefix": "http://xuxueli25.x9.fjjsp01.com", /* 图片访问路径前缀 */
"imagePathFormat": "http://xuxueli25.x9.fjjsp01.com/uploadfiles/image/{yyyy}{mm}{dd}/{time}{rand:6}",
```
- 03：修复bug：controller.jsp：添加部分代码,解决插件自身获取绝对路径bug（也可以通过修改JAR实现，有空再研究）;
```
//out.write( new ActionEnter( request, rootPath ).exec() );
// 在线管理文件,获取绝对路径bug解决 start
String result = new ActionEnter(request, rootPath).exec();
String action = request.getParameter("action");
if (action != null && (action.equals("listfile") || action.equals("listimage"))) {
rootPath = rootPath.replace("\\", "/");
result = result.replaceAll(rootPath, "/");//把返回路径中的物理路径替换为 '/'
}
out.write(result);
// 在线管理文件,获取绝对路径bug解决 stop
```

- 04：修复bug：ueditor.all.min.js：解决弹框层级冲突：zIndex:999改为zIndex:99999,解决与easyui的遮罩9000冲突;	


**七牛云存储 (整合ueditor) :**
- 01：引入七牛的JAR：qiniu-java-sdk.Jar;
- 02：修改JAR源码，加配置文件：修改ueditor的JAR + 加新JAR配置文件（官方下载src源文件修改并打包）：ueditor-1.1.1-qiniu-xxl.jar/ueditor.properties (保存文件时,优先判断保存七牛云)

```
// 文件01：ueditor.properties
ueditor.qiniu=true
ueditor.qiniu.ak=_lL7ExPz_OErbXqD5smnP9JGhM425NzP5FB3xmii
ueditor.qiniu.sk=BPCdj7gHi2EPdJogZaUSN1pV9uBYDKrR04C13YNu
ueditor.qiniu.bucket=xxl-5i

// 文件02：StorageManager.java
package com.baidu.ueditor.upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.util.QiniuUtil;

public class StorageManager {
	public static final int BUFFER_SIZE = 8192;

	public StorageManager() {
	}

	public static State saveBinaryFile(byte[] data, String path) {
		File file = new File(path);

		State state = valid(file);

		if (!state.isSuccess()) {
			return state;
		}

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bos.write(data);
			bos.flush();
			bos.close();
		} catch (IOException ioe) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}

		state = new BaseState(true, file.getAbsolutePath());
		state.putInfo( "size", data.length );
		state.putInfo( "title", file.getName() );
		return state;
	}

	public static State saveFileByInputStream(InputStream is, String path,
			long maxSize) {
		State state = null;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[ 2048 ];
		BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			if (tmpFile.length() > maxSize) {
				tmpFile.delete();
				return new BaseState(false, AppInfo.MAX_SIZE);
			}

			state = saveTmpFile(tmpFile, path);

			if (!state.isSuccess()) {
				tmpFile.delete();
			}

			return state;
			
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	public static State saveFileByInputStream(InputStream is, String path) {
		State state = null;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[ 2048 ];
		BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			state = saveTmpFile(tmpFile, path);

			if (!state.isSuccess()) {
				tmpFile.delete();
			}

			return state;
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static File getTmpFile() {
		File tmpDir = FileUtils.getTempDirectory();
		String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
		return new File(tmpDir, tmpFileName);
	}

	// 保存上传文件,为避免侵入性,此类为唯一修改类方法
	private static State saveTmpFile(File tmpFile, String path) {
		State state = null;
		File targetFile = new File(path);

		if (targetFile.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}
		
		if (QiniuUtil.ifQiniu()) {
			String qpath = "/" + path.split("//")[1];
			QiniuUtil.uploadFile(qpath, tmpFile.getAbsolutePath());
		} else {
			try {
				FileUtils.moveFile(tmpFile, targetFile);
			} catch (IOException e) {
				return new BaseState(false, AppInfo.IO_ERROR);
			}
		}

		state = new BaseState(true);
		state.putInfo( "size", targetFile.length() );
		state.putInfo( "title", targetFile.getName() );
		
		return state;
	}

	private static State valid(File file) {
		File parentPath = file.getParentFile();

		if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
			return new BaseState(false, AppInfo.FAILED_CREATE_FILE);
		}

		if (!parentPath.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}

		return new BaseState(true);
	}
}

// 文件03：QiniuUtil.java
package com.baidu.ueditor.util;

import java.util.Properties;

import org.json.JSONException;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.rs.PutPolicy;

/**
 * 七牛云UTIL
 * @author xuxueli $2015-4-27 00:59:30
 */
public class QiniuUtil {

	private static String token = null; 
	private static String getToken() {
		if (token == null || token.trim().length() == 0) {
			Properties prop = PropertiesUtil.loadProperties("ueditor.properties");
			String ueditorQiniuAk = PropertiesUtil.getString(prop, "ueditor.qiniu.ak");
			String ueditorQiniuSk = PropertiesUtil.getString(prop, "ueditor.qiniu.sk");
			String ueditorQiniuBucket = PropertiesUtil.getString(prop, "ueditor.qiniu.bucket");
			
			try {
				return new PutPolicy(ueditorQiniuBucket).token(new Mac(ueditorQiniuAk, ueditorQiniuSk));
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (AuthException e) {
				e.printStackTrace();
			}
		}
		return token;
	}
	
	private static Boolean ifQiniu = null; 
	public static boolean ifQiniu() {
		if (ifQiniu == null) {
			Properties prop = PropertiesUtil.loadProperties("ueditor.properties");
			ifQiniu = PropertiesUtil.getBoolean(prop, "ueditor.qiniu");
		}
		return ifQiniu;
	}

	public static void uploadFile(String path, String file) {
		IoApi.putFile(getToken(), path, file, new PutExtra());
	}
}

// 文件04：PropertiesUtil.java
package com.baidu.ueditor.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Properties.UTIL
 * @author xuxueli $2015-4-27 00:26:46
 */
public class PropertiesUtil {

	/**
	 * 加载Properties
	 * @param propertyFileName
	 * @return
	 */
	public static Properties loadProperties(String propertyFileName) {
		Properties prop = new Properties();
		InputStream in = null;
		try {
			ClassLoader loder = Thread.currentThread().getContextClassLoader();
			URL url = loder.getResource(propertyFileName); // 方式1：配置更新不需要重启JVM
			in = new FileInputStream(url.getPath());
			// in = loder.getResourceAsStream(propertyFileName); //
			// 方式2：配置更新需重启JVM
			if (in != null) {
				prop.load(in);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	/**
	 * 获取配置String
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getString(Properties prop, String key) {
		if (prop == null || key == null || key.trim().length() == 0) {
			return null;
		}
		return prop.getProperty(key);
	}

	/**
	 * 获取配置boolean
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBoolean(Properties prop, String key) {
		return Boolean.parseBoolean(getString(prop, key));
	}

	
	public static void main(String[] args) {
		Properties prop = loadProperties("memcached.properties");
		System.out.println(getString(prop, "server.address"));
	}

}


```

- 03：修改：config.json：修改*UrlPrefix地址：http://7xir7k.com1.z0.glb.clouddn.com/@;

- 04：使用
```
<!-- 加载编辑器的容器-->
<scriptid="container"name="content"type="text/plain">这里写你的初始化内容</script><
!-- 配置文件 -->
<scripttype="text/javascript"src="ueditor.config.js"></script>
<!-- 编辑器源码文件-->
<scripttype="text/javascript"src="ueditor.all.js"></script>
<!-- 实例化编辑器 -->
<scripttype="text/javascript">var ue = UE.getEditor('container');</script>
```

