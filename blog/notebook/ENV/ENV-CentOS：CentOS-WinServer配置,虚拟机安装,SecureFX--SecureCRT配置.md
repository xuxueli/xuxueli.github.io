### linux常用命令
```
// 为文件的用户添加执行权限（u代表所有者，x代表执行权限）
chmod u+x *.sh

// 创建目录，分配目录权限给用户
sudo mkdir data     (root权限目录) 
sudo chown xuxueli data     （目录权限分配给xuxueli）


// 查看磁盘、文件大小（Mac有效）
df -h  ：命令查看整个硬盘的大小 ，-h表示人可读的
du -d 1 -h  ：命令查看当前目录下所有文件夹的大小 -d 指深度，后面加一个数值
ls -all :查看文件大小
du -h --max-depth=1 /data：linux可用，查看目录大小；
```

### VirtualBox安装CentOS
***
> VirtualBox获取：[官网](https://www.virtualbox.org/)下载virtualBox（从vmware转到virtualBox，首先virtualBox免费，其次virtualBox在sun公司手下，牌子很硬）。

> CentOS镜像获取：

    搜狐镜像：http://mirrors.sohu.com/
    官网：http://wiki.centos.org/Download    
    阿里云镜像：http://mirrors.aliyun.com/centos/6.6/isos/i386/            （32/64位的，请注意，请注意）
    

* 1、安装VirtualBox，一路点下去，配置文件路径为安装路径（洁癖）；
* 2、下载centos6.6精简版镜像；
* 3、新建虚拟机，默认即可，others linux 64位；
    * 常规》高级》备份位置设置为同安装路径（洁癖）；
    * 系统》处理器》勾选“启用PAE/NX”（报错：please usea kernel appropriate for your cpu时启用）
    * 网络》连接方式》桥接；
    * 存储》控制器》光盘》分配光驱》选择...centos镜像文件；
* 4、安装:
    * 启动、按住 “↑”不停，选择 “Install system with basic video driver”选项安装DVD中IOS文件（重要，否则打开的是liveDVD，每次重启都会被还原）；
    * 按照步骤一步一步安装，时间大概十分钟（注意设置root密码，并牢记），最后一步貌似卡主，实际上是在初始化文件系统，慢慢等几分钟即可，莫急躁；
    》Skip ... 》us》Re-inisitalize》Asia/Shanghai……》write change to desk……wait minutes……》reboot
* 5、测试联网：ping www.baidu.com
    ```
    账号：root 
    密码：root_pwd
    ```


### centos更新yum源
```
# cp /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
# 阿里云
# wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-5.repo
# wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-6.repo        // centos6.6使用这个
# 163
# wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.163.com/.help/CentOS5-Base-163.repo
# wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.163.com/.help/CentOS6-Base-163.repo

# yum clean all
# yum makecache
```

### centos版本介绍：
* BinDVD版：普通安装版，需安装到计算机硬盘才能用，bin一般都比较大，而且包含大量的常用软件，安装时无需再在线下载（大部分情况）。

* LiveDVD版：一个光盘CentOS系统，可通过光盘启动电脑，启动出CentOS系统，有图形界面，也有终端。也可以安装到计算机，但有些内容还需要再次到网站下载（自动）。

* LiveCD版：相比LiveDVD，略精简，自带一些比较常用的软件。

* minimal版：精简版，自带软件较少，没有图像界面，也没有很多命令。（我个人最喜欢这一款）

* netinstall版：最精简，网络安装，需要联网。


### centos常规操作
##### 启动级别
* 1、设置centos启动级别：vi /etc/inittab    
* 2、理解Runlevel：runlevel 用来表示在init进程结束之后的系统状态，在系统的硬件中没有固定的信息来表示runlevel，它纯粹是一种软件结构。init和 inittab是runlevel影响系统状态的唯一原因。在上述例子中inittab文件起始阶段的注释主要用来描述runlevel：

    * Runlevel 0是让init关闭所有进程并终止系统。
    * Runlevel 1是单用户模式，只能系统管理员进入，在该模式下处理在有登录用户时不能进行更改的文件，
    * Runlevel 2是允许系统进入多用户的模式，但并不支持文件共享，这种模式很少应用。
    * Runlevel 3是最常用的运行模式，主要用来提供真正的多用户模式，也是多数服务器的缺省模式。（字符终端）
    * Runlevel 4一般不被系统使用，
    * Runlevel 5是将系统初始化为专用的X Window终端。(图形界面)
    * Runlevel 6是关闭所有运行的进程并重新启动系统。
    

##### 关机
> Linux centos重启命令： 
* 1、reboot                重启
* 2、shutdown -r now     立刻重启(root用户使用)
* 3、shutdown -r 10         过10分钟自动重启(root用户使用)
* 4、shutdown -r 20:35     在时间为20:35时候重启(root用户使用)

　　
>如果是通过shutdown命令设置重启的话，可以用【shutdown -c】命令取消重启 

##### 1.shutdown
>shutdown命令安全地将系统关机。

>有些用户会使用直接断掉电源的方式来关闭linux，这是十分危险的。因为linux与windows不同，其后台运行着许多进程，所以强制关机可能会导致进程的数据丢失﹐使系统处于不稳定的状态﹐甚至在有的系统中会损坏硬件设备。

>而在系统关机前使用shutdown命令﹐系统管理员会通知所有登录的用户系统将要关闭。并且login指令会被冻结﹐即新的用户不能再登录。直接关机或者延迟一定的时间才关机都是可能的﹐还可能重启。这是由所有进程〔process〕都会收到系统所送达的信号〔signal〕决定的。这让像vi之类的程序有时间储存目前正在编辑的文档﹐而像处理邮件〔mail〕和新闻〔news〕的程序则可以正常地离开等等。shutdown执行它的工作是送信号〔signal〕给init程序﹐要求它改变runlevel。Runlevel0被用来停机〔halt〕﹐runlevel 6是用来重新激活〔reboot〕系统﹐而runlevel1则是被用来让系统进入管理工作可以进行的状态﹔这是预设的﹐假定没有-h也没有-r参数给shutdown。要想了解在停机〔halt〕或者重新开机〔reboot〕过程中做了哪些动作﹐你可以在这个文件/etc/inittab里看到这些runlevels相关的资料。

>shutdown 参数说明:

* [-t] 在改变到其它runlevel之前﹐告诉init多久以后关机。
　* [-r] 重启计算器。
* [-k] 并不真正关机﹐只是送警告信号给每位登录者〔login〕。
* [-h] 关机后关闭电源〔halt〕。
* [-n] 不用init﹐而是自己来关机。不鼓励使用这个选项﹐而且该选项所产生的后果往 往不总是你所预期得到的。 
* [-c] cancel current process取消目前正在执行的关机程序。所以这个选项当然没有 时间参数﹐但是可以输入一个用来解释的讯息﹐而这信息将会送到每位使用者。 
* [-f] 在重启计算器〔reboot〕时忽略fsck。
* [-F] 在重启计算器〔reboot〕时强迫fsck。
* [-time] 设定关机〔shutdown〕前的时间。

##### 2.halt—-最简单的关机命令
其实halt就是调用shutdown -h。halt执行时﹐杀死应用进程﹐执行sync系统调用﹐文件系统写操作完成后就会停止内核。

参数说明:
* [-n] 防止sync系统调用﹐它用在用fsck修补根分区之后﹐以阻止内核用老版本的超 级块〔superblock〕覆盖修补过的超级块。 
* [-w] 并不是真正的重启或关机﹐只是写wtmp〔/var/log/wtmp〕纪录。
* [-d] 不写wtmp纪录〔已包含在选项[-n]中〕。
* [-f] 没有调用shutdown而强制关机或重启。
* [-i] 关机〔或重启〕前﹐关掉所有的网络接口。
* [-p] 该选项为缺省选项。就是关机时调用poweroff。
* 
##### 3.reboot
reboot的工作过程差不多跟halt一样﹐不过它是引发主机重启﹐而halt是关机。它的参数与halt相差不多。

##### 4.init
init是所有进程的祖先﹐它的进程号始终为1﹐所以发送TERM信号给init会终止所有的用户进程﹑守护进程等。shutdown 就是使用这种机制。init定义了8个运行级别(runlevel)，init 0为关机﹐init1为重启。关于init可以长篇大论﹐这里就不再叙述。另外还有telinit命令可以改变init的运行级别﹐比如﹐telinit -iS可使系统进入单用户模式﹐并且得不到使用shutdown时的信息和等待时间。

##### 5.passwd 
linux如何修改root管理员密码
* 以root 身份登录(SSH操作)
* 输入 passwd 命令 就可以看到提示输入新密码了
* 输入密码的时候是看不到字符的。
 
### 开机启动SSH
>检查是否装了SSH包：#rpm -qa |grep ssh

>安装SSH：#yum install openssh-server
```
#chkconfig --list sshd 检查SSHD是否在本运行级别下设置为开机启动
#chkconfig --level 2345 sshd on 如果没设置开机启动就设置下.
#service sshd restart 重新启动
#netstat -antp |grep sshd 看是否启动了22端口.确认下.
#iptables -nL 看看是否放行了22口.
#setup---->防火墙设置 如果没放行就设置放行.
```

### 防火墙设置    
```
// 安装防火墙
# yum install iptables

// 设置默认策略
# iptables -P INPUT DROP
# iptables -P FORWARD DROP
# iptables -P OUTPUT DROP

// 查看防火墙端口
/etc/init.d/iptables status   （或：service iptables status）

// 开启端口（临时，需要save后永久）
/sbin/iptables -I INPUT -p tcp --dport 22 -j ACCEPT
/sbin/iptables -I OUTPUT -p tcp --sport 22 -j ACCEPT    （出站规则，通常不配置）

// 余下的任何TCP协议的数据包都将被拒绝
# iptables -A INPUT -p tcp -j REJECT --reject-with tcp-reset


// 删除端口：删除INPUT链中的iptables规则（临时，需要save后永久）
iptables -D INPUT 1     删除指定的第1行规则
iptables -F     清除预设表filter中的所有规则链的规则
iptables -X     清除预设表filter中使用者自定链中的规则

// 保存修改（save后永久）
/etc/rc.d/init.d/iptables save

// 重启防火墙
方式一：/etc/init.d/iptables restart
方式二：service iptables restart

// 关闭防火墙:
方式一：/etc/init.d/iptables stop
方式二：service iptables stop

// 启动防火墙
方式一：/etc/init.d/iptables start
方式二：service iptables start

// 防火墙，启动/关闭：临时，重启不复原
开启：chkconfig iptables on    （chkconfig –level 345 iptables on ）
关闭：chkconfig iptables off

// 查看端口号
查看进程的端口号：# netstat -lnp | grep mysql
tcp        0      0 :::3306                     :::*                        LISTEN      1195/mysqld 
注释：上面命令中的mysqld是进行名称.上面的3306就是监控的端口号，1195是进程的进程id。
如果进程没有像mysqld一样监控一个端口，如何查看进程的id：# ps aux | grep svnserve （ps -ef| grep svnserve）
root      1709     1  0 22:05 ?        00:00:00 svnserve -d -r /home/root/subversion_repository/5i
注释：第一行，第二列的1709就是进程id。第一行的最后列出了启动时的命令和参数。
```

### 虚拟机联网（桥接/NAT）

##### VirtualBox：Nat（虚拟机联网，ping主机）+ Host-only（主机ping虚拟机）
- 1、虚拟机网卡配置：
    - 网卡1：NAT，用户联网，和ping主机；
    - 网卡2：Host-Only，用于主机ping虚拟机。 创建一个和外部主机处于同一网段的网卡；
        - 在virtualbox偏好设置里面添加一个host-only网卡，名字为vboxnet0；
        ```
        // 因为这个网络是dhcp分配的ip的，为了方便把vboxnet0网络，dhcp ip地址段设置为一个固定ip, 配置如下：
        DHCP服务器》
        服务器地址：192.168.56.100
        服务器网络掩码：255.255.255.0
        最小地址：192.168.56.101
        最大地址：192.168.56.101
        // 以后在mac host, 每次ssh dengpan@192.168.56.101, 即可连接该机器。
        ```
        - 给该linux host的第二网卡上，绑定刚创建的vboxnet0 的 host-only 网卡
        - 在linux-guest里面，ifconfig查看还是一个网卡，手动编辑
        ```
        touch /etc/sysconfig/network/ifcfg-eth1
        // 写入以下内容
        DEVICE=eth1
        BOOTPROTO=dhcp
        BROADCAST=
        ETHTOOL_OPTIONS=
        IPADDR=
        MTU=
        NAME=
        NETMASK=
        NETWORK=
        REMOTE_IPADDR=
        STARTMODE=auto
        DHCLIENT_SET_DEFAULT_ROUTE=yes
        ```
        - 然后reboot，通过ifcofig可以看到eth1网卡, ip是192.168.56.101,可以在mac host用ssh连接。

- 2、进入CentOS，网络配置：
    ```
    // 修改网络配置
    vi /etc/sysconfig/network-scripts/ifcfg-eth0
    // 修改以下属性
    ONBOOT=yes
    NM_Controlled=no
    ```
- 3、重启服务器
    ```
    # service network restart
    ```

[参考文档1](https://dplord.com/tag/mac-os/)
[参考文档2](http://www.th7.cn/system/mac/201603/156422.shtml)

##### vmware两种方式都OK

- 桥接：    
    - 1、虚拟机设置“桥接”模式；
    - 2、Windows网络：VMware Network Adapter VMnet1设置为自动获取IP；
    - 3、CentOS设置：
    ```
    // 修改网络配置
    vi /etc/sysconfig/network-scripts/ifcfg-eth0
    // 修改以下属性
    ONBOOT=yes
    NM_Controlled=no
    
    // 1、Centos 6的Minimal下，网卡默认onboot="no"，修改为yes
    // 2、设置了依赖 NetworkManager的选项，NM_CONTROLLED="yes"，因为minimal的情况下并没有安装系统默认提供的网络管理工具NetworkManger。所以我们需要修改为no；
    ```
    - 4、重启网络配置： 
    ```
    # service network restart
    ```

- NAT：
    - 1、虚拟机选择“NAT”模式；
    - 2、Windows网络：VMware Network Adapter VMnet8 设置成自动获取IP；
     Windows服务：VMnetDHCP、VMware NAT Service两个服务，顺序启动；
    - 3、CentOS设置：
    ```
    // 修改网络配置
    vi /etc/sysconfig/network-scripts/ifcfg-eth0
    // 修改以下属性
    ONBOOT=yes
    NM_Controlled=no
    ```
    - 4、重启网络配置： 
    ```
    # service network restart
    ```

##### 报错：MAC地址修改报错
    ```
    # vi /etc/sysconfig/network-scripts/ifcfg-eth0   （把“HWADDR:-----”这一行删除）
    # rm -rf /etc/udev/rules.d/70-persistent-net.rules
    # reboot ……………
    # service network restart
    # ifup eth0
    ```

>VirtualBox的网络连接如果是NAT方式，host不能ping guest机器，所以选择bridge的方式更方便。

* （1）Bridged Networking(即网桥)：网桥允许用户将虚拟机连接到主机所在的局域网(LAN)。此方式连接虚拟机中的虚拟以太网交换机到主机中的物理以太网适配器；

* （2）NAT：网络地址翻译(NAT)设备允许用户将虚拟机连接到一个外部网络，在该网络中只有一个IP网络地址并且该地址已经被主机使用。

* （3）Host-only：这种技术提供了主机和虚拟机、虚拟机和虚拟机之前的网络通信，而不是虚拟机访问Internet，在这种模式下相当于使虚拟机和主机、虚拟机和虚拟机处在一个和外网隔离的网络中。在vmware workstation中虚拟网卡VMnet1的默认属性为Host-only

![image](http://static.oschina.net/uploads/space/2016/0624/231416_5jBN_1046342.jpeg)

![image](http://static.oschina.net/uploads/space/2016/0624/231457_XyiR_1046342.png)


### windows server2012使用
* 1、远程管理-开启、远程桌面-开启：仪表盘-本地服务器；
* 2、防火墙-开启：工具-本地安全策略-公用网络/专用网络；
* 3、配置入站规则-TCP/IP端口80/3306/11211等：工具-本地安全策略-高级安全windows防火墙；


    查看所有端口占用：netstat -ano    
    查看指定端口占用：netstat -aon|findstr "8201"    
    查看指定端口占用任务：tasklist|findstr "8201"    

### windows下SSH、FTP工具: SecureFX + SecureCRT
SSH 是目前较可靠，专为远程登录会话和其他网络服务提供安全性的协议。

下载：搜索下载绿色版，开箱即用；

前提：CentOS开启SSH服务，并配置防火墙

配置：

    1、外观
    ==》字体：新宋体 + 字符集：中文GB2312 
    ==》字符编码UTF-8


    2、找到SecureFX配置文件夹（选项--全局选项，常规下的配置文件夹），比如：D:\Program files\SecureCRT\DATA；
    ==》 在配置文件夹下的Sessions子目录中，找到FTP站点对应的Session文件（.ini扩展名），双击打开；
    ==》查找Filenames Always Use UTF8，将=号后面的参数改成00000001，保存退出即可。

保存登录密码：
    
    选项》全局选项》SSH主机密匙》主机密匙数据库位置，填写为：F:\ProgramFiles\SecureCRT_FX\Data\Settings\Config\KnownHosts

报错SecureCRT：“securecrt failed to open the host key database file”
    
    解决：绿色版，编辑一下"./Data/Settings/Config/SSH2.ini"文件中的"Host Key Database Location"=应用根目录/Data/Settings/Config/KnownHosts；注意等号右边不需要引号。你甚至可以将KnownHosts目录下的*.pub文件和HostKeyDB.txt删掉，下次启动时它们会再次自动生成。


su admin：更换用户
sudo ./startup.sh    管理员权限执行

#### 启动报错处理
```
报错内容：kernel panic - not syncing: Attempted to kill init!
```
解决本次启动报错：
- 1、重启时候，不听按“E”键，进入选择三选一界面；
- 2、选择“kernel /vmlinuz-2.6.21-.....”一行，点击“E”键，回车编辑，在末尾加上命令“enforcing=0”，回车保存；
- 3、按住B键，重新启动，成功；
永久解决该报错：
- 1、编辑“/boot/grub/grub.conf”系统文件，找到“kernel /vmlinuz-2.6.21-.....”一行，在末尾加上命令“enforcing=0”，保存；



### Mac下SSH、FTP工具：SecureFX + SecureCRT(或者Terminal.app + SCP命令)
##### terminal + sftp
- terminal.app （个人比较喜欢，简单）
- scp
```
scp xueli.xue@10.3.18.142:/data/applogs/wed-job-service/logs/WedHotelPicUpload-1459248088557.xlsx /tmp
```
##### SecureFX + SecureCRT

[下载地址](http://xclient.info/search/s/secure/)    （推荐使用正版哈）

按照下载页面教程安装即可

- 记住密码：打开SecureCRT的全局选项，在主菜单Preferences（或者COMMAND键加逗号），取消掉Use Keychain即可。
- 谷歌验证，登录配置：属性》SSH2》
Authentication，只保留KeyBoard，其余全部取消勾选；这样会弹出谷歌验证框，在弹出密码框，进行输入；
- 谷歌验证，自动登录配置：复制上面配置，然后：Connection》Logon Actions》Automatelogon》设置ogin的send为“ssh xueli.xue@10.3.19.141”，设置assword:为登录面，即可；