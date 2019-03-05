### GIT

- [windows下Git安装与配置](http://blog.csdn.net/renfufei/article/details/41647875)
- [Mac-OSX下安装Git](http://blog.csdn.net/zhangkongzhongyun/article/details/7903148)
- [Git 常用命令速查表(图文+表格)](http://www.jb51.net/article/55442.htm)
- [Git 教程](http://www.runoob.com/git/git-tutorial.html)

> GIT常用命令汇总
```
// 初始化
cd git-workspace
git init
git clone http://xxxxx.git
cd xxxxx

// 查看
git status
git branch -a
git branch -r

// 拉取master分支到本地
git fetch origin master:master
git checkout master

// 获取远程分支master并merge到当前分支 
git fetch origin master
git pull origin master 

// 获取所有远程分支，并merge到本地分支
git fetch
git pull

// 在master基础上，新建分支，推送分支
git checkout master
git fetch origin master
git checkout -b XXX
git push origin XXX

// 加入缓存，提交代码，并push分支
git add xxx.java
git commit -m "init project"
git push orgin XXX

// 在 branch_a 分支上 merge 分支 master
git checkout branch_a
git merge master
git push orgin branch_a

// 在 branch_a 分支上 rebase 分支 master （不推荐）
git checkout branch_a
git rebase master
git push orgin branch_a
// (merge操作会生成一个新的节点，之前的提交分开显示。而rebase操作不会生成新的节点，是将两个分支融合成一个线性的提交，之前分支就没有了。)

// 删除分支，大写D强制删除，push远程删除
git branch -d XXX
git branch -D XXX
git push origin  :XXXX

// 文件加入/移除stage（加入stage才可commit和push）
git add xxx.imi
git reset HEAD xxl.imi

// .gitignore文件
加入.gitingore文件中的文件，不会被 “git status(检测未被git管理、git管理下被修改但未被commit和push的文件)”检测到；
git rm --cached file/path/to/be/ignored

// 冲突解决
add 
commit

// 撤消上一个commit，但保留add的文件
git reset --soft HEAD~1

// 生成公钥，默认位置：~/.ssh
$ ssh-keygen -t rsa -C "xxx@gmail.com"
cat .\.ssh\id_rsa.pub
》》New SSH key
ssh -T git@github.com

// 更新仓库地址
git remote set-url origin remote_git_address

// 更新config
git config --list
git config user.name
git config user.email   // query
git config user.email "email info"  // update each
git config --global user.email "email info"  // update global

// 回滚commit
git log
git reset --hard <commit_id>
git push origin HEAD --force

// 放弃本地的修改，用远程的库覆盖本地
git fetch --all
git reset --hard origin/master

// 强制覆盖推送
git push -f origin/bbbbbb
```
##### Git常用命令
```
// 常用命令汇总
git clone <url>	clone远程版本库
git status	查看状态
git diff	查看变更内容
git add .	跟踪所有改动过的文件
git add <file>	跟踪指定的文件
git mv <old> <new>	文件改名
git rm <file>	删除文件
git rm --cached <file>	停止跟踪文件但不删除
git commit -m "commit message"	提交所有更新过的文件
git log	查看提交历史
git reset --hard HEAD	撤销工作目录中所有未提交文件的修改内容
git checkout HEAD <file>	撤销指定的未提交文件的修改内容
git revert <commit>	撤销指定的提交
git branch	显示所有本地分支
git checkout <branch/tag>	切换到指定分支或标签
git branch <new-branch>	创建新分支
git branch -d <branch>	删除本地分支
git merge <branch>	合并指定分支到当前分支
git rebase <branch>	Rebase指定分支到当前分支
git remote -v	查看远程版本库信息
git remote show <remote>	查看指定远程版本库信息
git remote add <remote> <url>	添加远程版本库
git fetch <remote>	从远程库获取代码
git pull <remote> <branch>	下载代码合并到当前分支
git push <remote> <branch>	上传代码到远程
git push <remote> :<branch/tag>	删除远程分支或标签

```

### SVN

- [windows7环境下svn服务器的配置及使用](http://www.cnblogs.com/xingma0910/p/3772936.html)
- [CentOS-6.3安装配置SVN](http://www.cnblogs.com/zhoulf/archive/2013/02/02/2889949.html)