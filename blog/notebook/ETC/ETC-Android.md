
- [3G手机开发之Android应用开发黎活明（8天）](https://wenku.baidu.com/view/84d34e0e4a7302768e99392d.html?from=search)
- [Android开发权威指南](https://book.douban.com/subject/6801007/)

- 官网：http://developer.android.com/sdk/installing/studio.html    
- 国内util站：http://www.androiddevtools.cn/    （SDK和AS都可以从这里下载）
- 中科开源镜像地址：IPV4/IPV6: mirrors.opencas.cn 端口：80
- 中科开源镜像更新地址：http://mirrors.opencas.cn/android/repository/repository-12.xml 

### IDE环境搭建（android studio）

```
0. JDK: 注意：Android SDK 和 JDK版本需要对应，SDK4.0对应JDK1.6，SDK4.4对应JDK1.7
1. SDK: 解压缩安装（如“android-sdk_r24.3.4-windows”）；
    配置AS：File》Setting》Appearance&Behavior》System Settings》Android SDK设置Location；
    SDK配置镜像地址：双击SDK Manager.exe》Tools》配置http代理跟地址mirrors.opencas.cn（镜像地址）；
    AS继承SDK配置镜像地址：File》Setting》Appearance&Behavior》System Settings》Android SDK》选择SDK Update Sites，新增地址http://mirrors.opencas.cn/android/repository/repository-12.xml ；
2. Gradle: 解压缩安装，android项目构建工具（貌似AS新版已经集成，不必须单独下载）；（如“gradle-2.10-all.zip”）
官网 : http://www.gradle.org/downloads
    GRADLE_HOME ： gradle根目录
    PATH ： PATH + %GRADLE_HOME%/bin
    校验：gradle -version
    AS配置：Build,Execution,Deployment》Build Tools》Gradle》配置本地安装路径；
3. IDE: 解压缩安装，Android Studio，配置SDK和Gradle；（如“android-studio-ide-141.2456560-windows.zip”）
更新Android SDK，新建AVD；、
4. 新建HelloWorld项目，运行，查看效果；（20160203--流行配置：target/compile最新稳定版 + jdk-1.7 + mini-4.0.3）
    Compile SDK Version：
    Build Tools Version：
    Source Compatibility：
    Target Compatibility：
    Min Sdk Version：
    Application Id：
    Target Sdk Version：
```

