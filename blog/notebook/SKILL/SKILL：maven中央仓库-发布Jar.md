中央仓库，申请系统：https://issues.sonatype.org/

中央仓库，发布系统：https://oss.sonatype.org/

中央仓库，查询地址：http://mvnrepository.com/

gpg	：brew install gpg

文档：http://my.oschina.net/u/2335754/blog/476676

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

// oss 配置
<!-- oss -->
<server>
    <id>oss</id>
    <username>xuxueli</username>
    <password>***</password>
</server>
<!-- oss -->

// profile 配置 （windows 可选配置，id 需要和pom一致，指定 gpg.exe目录）
<profile>
    <id>release</id>
    <properties>
        <gpg.executable>D:/ProgramFiles/gpg/GnuPG/bin/gpg.exe</gpg.executable>
        <!-- <gpg.passphrase>***</gpg.passphrase> -->
        </properties>
</profile>

// 默认使用环境变量配置的maven下的默认配置，修改器setting即可
mvn clean deploy -P release -Dgpg.keyname=F7771387 -Dgpg.passphrase=Passphase

// oss
登录OSS，close，release，等2小时同步即可；
（之后，同一个groupid下可自由发布，只需要close + release即可）
```

备份：
```
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


