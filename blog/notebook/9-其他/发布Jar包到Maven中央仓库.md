<h2 style="color:#4db6ac !important" >发布Jar包到Maven中央仓库.md</h2>
> 创建时间：2016/09/06

[TOCM]

[TOC]


### 相关地址

名称 | 地址 | 说明
--- | --- | ---
工单管理 | https://issues.sonatype.org/ | 申请上传资格和groupId 的地方。注册账号、创建和管理issue，Jar包的发布是以解决issue的方式起步的。
构件仓库 | https://oss.sonatype.org/ | Jar包上传的位置，Release 之后就会同步到maven中央仓库。
仓库镜像 | http://search.maven.org/ 或 http://mvnrepository.com/ | 最终Jar包可以在这里搜索到。

### 一、申请上传资格和groupId
第一访问工单管理界面，需要提前注册，进行工单管理和构建仓库身份验证。

点击顶部“create”按钮创建一个Issue，提交你上传jar包的基本信息，主要是groupid。
如果你申请 "com.{域名}" 那么你最有有 "{域名}.com" 这个域名的所有权。如果没有域名可以使用github二级域名，比如 "com.github.{用户名}"。

创建成功后，接下来等待后台管理员审核，一般一个工作日以内，当Issue的Status变为RESOLVED后，就可以进行下一步操作了。

### 二、配置项目工程pom.xml
```
<!-- license -->
<licenses>
    <license>
        <name>GNU General Public License version 3</name>
        <url>https://opensource.org/licenses/GPL-3.0</url>
    </license>
</licenses>

<!-- scm -->
<scm>
    <tag>master</tag>
    <url>https://github.com/xxx/xxx.git</url>
    <connection>scm:git:https://github.com/xxx/xxx.git</connection>
    <developerConnection>scm:git:git@github.com:xxx/xxx.git</developerConnection>
</scm>

<!-- developers -->
<developers>
    <developer>
        <id>XXL</id>
        <name>xxx</name>
        <email>xxx@xx.com</email>
        <url>https://github.com/xxx</url>
    </developer>
</developers>

<profiles>
    <profile>
        <id>release</id>
        <build>
            <plugins>
                <!-- GPG -->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-gpg-plugin</artifactId>
                    <version>${maven-gpg-plugin.version}</version>
                    <configuration>
                        <useAgent>false</useAgent>
                    </configuration>
                    <executions>
                        <execution>
                            <phase>verify</phase>
                            <goals>
                                <goal>sign</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
        <!-- oss distributionManagement -->
        <distributionManagement>
            <snapshotRepository>
                <id>oss</id>
                <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
            </snapshotRepository>
            <repository>
                <id>oss</id>
                <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
            </repository>
        </distributionManagement>
    </profile>
</profiles>
```

### 三、配置Maven setting.xml
setting.xml放在Maven安装文件/conf目录下
```
// oss 配置
<servers>
    <server>
      <id>oss</id>
      <username>Sonatype 账号</username>
      <password>Sonatype 密码</password>
    </server>
</servers>

// profile 配置 （windows 可选配置，id 需要和pom一致，指定 gpg.exe目录）
<profile>
    <id>release</id>
    <properties>
        <gpg.executable>D:/ProgramFiles/gpg/GnuPG/bin/gpg.exe</gpg.executable>
        <!-- <gpg.passphrase>***</gpg.passphrase> -->
        </properties>
</profile>
```

### 四、配置gpg-key

配置gpg-key核心步骤。
```
// brew 安装 gpg （mac下神器）
brew install gpg

// 查看版本
gpg --version

// 生成密匙对（name=x******,email=9********@qq.com,Passphase=普通密码************4，命令行下小键盘有问题不要用）
gpg --gen-key

// 查看公钥
gpg --list-keys

// 输出如下
/Users/***user***/.gnupg/pubring.gpg
---------------------------------
pub   rsa2048 2016-09-05 [SC]
      {用户ID}
uid           [ 绝对 ] x****** <9********@qq.com>
sub   rsa2048 2016-09-05 [E]

// 将公钥发布到 PGP 密钥服务器，公钥服务器配置地址 "~/.gnupg/gpg.conf"，可使用默认值也可自定义；
gpg --keyserver hkp://keys.gnupg.net --send-keys {用户ID}

// 查询公钥是否发布成功
gpg --keyserver hkp://keys.gnupg.net --recv-keys {用户ID}

// 帮助，如修改密码等
gpg --help
```

gpg常用命令。
```
一、查看公钥
gpg --gen-key
二、查看公钥
$ gpg --list-key
三、查看私钥
$ gpg --list-secret-key
四、公钥删除
$ gpg --delete-keys 标识名
五、私钥删除
$ gpg --delete-secret-keys 标识名
六、公钥导出
$ gpg --export 标识名 > 导出文件名（多以asc为文件后缀）
七、私钥导出
$ gpg --export-secret-key 标识名 > 导出文件名（多以asc为文件后缀）
八、密钥导入
$ gpg --import 密钥文件
九、加密文件
$ gpg --recipient 标识名 --encrypt 文件名
十、解密文件
$ gpg --output 新文件名 --decrypt 加密文件名
十一、修改密钥
$ gpg --edit-key 标识名

---
报错处理 "gpg: signing failed: Inappropriate ioctl for device"： 
gpg版本较新，需要额外配置，在gpg安装目录（mac的是~/.gnup）下建立两个配置文件：gpg.conf、gpg-agent.conf；
    - 在gpg.conf添加：
        use-agent
        pinentry-mode loopback
    - 在gpg-agent.conf添加：
        allow-loopback-pinentry

```

### 五、发布JAR到构件仓库
默认使用环境变量配置的maven下的默认配置，修改器setting即可
```
mvn clean deploy -P release -Dgpg.keyname={用户ID} -Dgpg.passphrase=Passphase
```

### 五、发布JAR到正式仓库
登录OSS，在构建仓库，Staging菜单中，找到刚刚发布JAR包，依次进行Close，Release操作即可。延时2小时后自动同步到正式仓库。


注意："申请上传资格和groupId" 步骤只需要首次申请时操作，后续不需要。
只需要申请过一个groupId，之后该groupId下可自由发布，只需要 "Close + Release" 即可。


### 参考：
[参考文档](http://my.oschina.net/u/2335754/blog/476676)
