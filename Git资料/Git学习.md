

### Git配置

#### 1、查看git配置信息

```
git config --list
```

#### 2、查看git用户名、密码、邮箱的配置

```
git config user.name
git config user.password
git config user.email
```

#### 3、设置git用户名、密码、邮箱

项目/仓库级

```
git config user.name "用户名"
git config user.password "密码"
git config user.email "邮箱"
```

系统级别(全局配置)

```
git config --global user.name "用户名"
git config --global user.password "密码"
git config --global user.email "邮箱
```

4、修改用户名、密码、邮箱的配置

```
和设置用户名、密码、邮箱一样。存在则修改，不存在则设置
git config user.name "用户名"
git config user.password "密码"
git config user.email "邮箱"
```



### Git基本操作

#### 1、状态查看操作

```
git status
```

作用：查看工作区、暂存区状态

#### 2、添加操作

```git
git add [file name]
例子：git add good.txt
将good.txt文件添加到暂存区
```

作用：将工作区的“新建/修改”添加到暂存区

#### 3、提交操作

```
git commit [file name]
git commit -m "commit message" [file name]
例子：git commit good.txt
说明：将文件good.txt提交到本地库
例子：git commit -m "第一次提交" good.txt
说明：将文件good.txt提交到本地库，"第一次提交"为当前提交的注释
```

作用：将暂存区内容提交到本地库

#### 4、查看历史记录

```git
git log 显示信息最为完整
git log --pretty=oneline 显示的信息比较简洁
git log --oneline 显示的信息最为简洁
git reflog 相比于git log --oneline多了HEAD@{1}
同时git reflog 会显示所有历史记录
```

#### 5、执行版本前进后退

1. 基于索引值操作

   ```git
   git reset --hard 索引值
   ```

2. 使用^符号

   ```git
   git reset --hard HEAD^	往后退一步
   git reset --hard HEAD^^ 往后退两步
   ```

3. 使用~符号

   ```
   git reset --hard HEAD~3	往后退三步
   ```

#### 6、为github地址添加别名

```git
git remote -v	用来查看已存在的别名
git remote add 别名 地址	用来保存添加别名
```

#### 7、推送操作

```git
git push 地址 分支名称(master)
```

#### 8、克隆操作

```git
git clone 地址
```

作用:

- 完整的把远程库下载到本地
- 创建远程地址别名
- 初始化本地库

#### 9、拉取操作

```
pull=fetch+merge

git pull 地址 分支名称(master)  下载文件，同时合并到本地库中

git fetch 地址 分支名称(master)  只下载文件，不修改工作区内容
查看下载的文件内容
1、git checkout 地址/分支名称  
2、cat 文件名(含后缀名)
返回本地仓库
1、git checkout master

git merge 地址/分支名称		合并到本地库中
```

#### 10、冲突

解决冲突之后，使用git commit 提交时不写文件名称的

#### 11、在github上下载单个文件夹

```
$mkdir project_folder
$cd project_folder
$git init
$git remote add -f origin <url>
```

上面的代码会帮助你创建一个空的本地仓库，同时将远程Git Server URL加入到Git Config文件中。

接下来，我们在Config中允许使用Sparse Checkout模式：

```
$git config core.sparsecheckout true
```

接下来你需要告诉Git哪些文件或者文件夹是你真正想Check Out的，你可以将它们作为一个列表保存在

.git/info/sparse-checkout 文件中。

例如：

```
$echo “libs” >> .git/info/sparse-checkout
$echo “apps/register.go” >> .git/info/sparse-checkout
$echo “resource/css” >> .git/info/sparse-checkout
```

最后，你只要以正常方式从你想要的分支中将你的项目拉下来就可以了：

```
$git pull origin master
```