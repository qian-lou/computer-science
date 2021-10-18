# 1.代码仓库

## 1.1创建仓库

进入需要创建代码库的文件夹

```shell
cd 文件夹
```

创建/初始化仓库

```shell
git init
```

拉去远程仓库到本地

```shell
git clone
```



## 1.2添加文件到仓库

#### 1.2.1添加文件到暂存区

添加单个文件

```shell
git add
```

添加所有文件

```shell
git add .
```

忽略文件

```
.gitignore中指定的文件会被忽略
```

```
空目录
```



#### 1.2.2提交到本地仓库

```shell
git commit 
```

然后填写commit message，然后保存

不建议使用 :

```shell
git commit -m "commit message"
```

建议提交遵循commit message规范



#### 1.2.3查看工作区状态

```shell
git status
```



#### 1.2.4对比工作区文件变化

```shell
git diff
```

建议将Beyondcompare配置为diff工具，用于diff以及merge冲突



## 1.3仓库配置

#### 1.3.1配置全局用户名和邮箱

```shell
git config --global user.name "name" 例如：git config --global user.name "zeny"
```

```shell
git config --global user.email "email address" 
```

若是个人开发机可以这样配置，若是公共编译机则不能这样配置



#### 1.3.2配置当前仓库用户名和邮箱

```shell
git config user.name "name"
```

```shell
git config user.name "email address"
```



# 2.代码版本/提交切换

## 2.1查看过去版本/提交

#### 2.1.1提交详情

```shell
git log
```



#### 2.1.2提交简介

```shell
git log --pretty=online
```



## 2.2回退版本/提交

#### 2.2.1回退到当前最新提交

```shell
git reset --hard HEAD
```



#### 2.2.2回退到上次提交

```shell
git reset --hard HEAD^
```



#### 2.2.3回退到上n次提交

```shell
git reset --hard HEAD~n
```



#### 2.2.4回退到某次提交

```shell
git reset --hard commitid
```



## 2.3重返未来版本

#### 2.3.1查看历史提交以及被回退的提交

```
git reflog
```

注意：该记录有时限，且只在本地



#### 2.3.2回到未来版本

```shell
git reset --hard commitid
```



## 2.4撤销修改

#### 2.4.1工作区文件撤销，没有提交到暂存区/没有git add, 撤销修改

```shell
git checkout 文件名
```



#### 2.4.2暂存区文件撤销

将暂存区文件撤销到工作区

```shell
git reset HEAD 文件 
不带 --hard
```

撤销修改

```shell
git checkout 文件名
```



#### 2.4.3提交了到版本库

参考回`2.2`退版本/提交



## 2.5删除文件

#### 2.5.1删除文件，从版本库中删除文件

```shell
git rm 文件名
```

修改后需要提交



#### 2.5.2恢复删除

参考`2.4`撤销修改



#### 2.5.3从版本库中删除文件，但是本地不删除文件

```shell
git rm --cached 文件名
```



## 2.6重命名文件

#### 2.6.1将文件重命名

```shell
git mv
```



#### 2.6.2将文件夹重命名

```shell
git mv
```



## 2.7忽略文件

通过git仓库下的.gitignore文件屏蔽某些中间文件/生成文件



# 3.分支

## 3.1创建与合并分支 

#### 3.1.1创建分支

仅创建

```shell
git branch 分支名
```

创建并切换

```shell
git checkout -b 分支名
```

注意：在本地仓库操作，创建的都是本地分支



#### 3.1.2切换分支

```shell
git checkout 分支名
```



#### 3.1.3合并分支

合并某分支到当前分支

```shell
git merge 分支名
```

注意：合并分支时禁用fast forward

```shell
git merge --no-ff 分支名
```

下面的若无特殊需要不建议使用：

```shell
git rebase
```



#### 3.1.4删除分支

**删除本地分支**：

- 删除未合并分支

  ```shell
  git branch -D 分支名
  ```

-  删除已合并分支

  ```shell
  git branch -d 分支名
  ```

**删除远程分支**:

- 删除远程分支

  ```shell
  git push origin -d 分支名
  ```

  ```shell
  git push <远程仓库名> -d 分支名
  ```

- 建议界面操作



#### 3.1.5查看分支

查看当前分支：

```shell
git branch
```

查看所有分支信息：

```shell
git branch -a
```

本地分支为本地分支名；远程分支为<远程仓库名>/分支名



#### 3.1.6合并分支，解决分支冲突

- 将要合并非分支更新到最新

- 切换到主分支

- 合并分支

- 解决合并时的conflict

- 提交到版本库

- 合并成功

- 查看分支状态

  ```shell
  git log --graph
  ```

  ```shell
  git log --graph --pretty=oneline --abbrey-commit
  ```



## 3.2暂存修改

#### 3.2.1暂存工作线程

```shell
git stash
```

#### 3.2.2恢复工作现场

恢复：

```shell
git stash apply
```

删除：

```shell
git stash drop
```

恢复 + 删除：

```shell
git stash drop
```



## 3.3多人协作

#### 3.3.1查看远程库信息

详细：

```shell
git remote -v
```

不详细：

```shell
git remote
```



#### 3.3.2更新/推送远程库

更新远程库信息：

```shell
git fetch
```

将远程库最新修改更新到本地：

```shell
git pull
git pull 可以认为是
git fetch + git merge
```

将本地修改推送到远程库：

```shell
git push
```

```shell
git push origin 分支名
```



#### 3.3.3本地分支与远程分支交互

使用远程分支A创建本地分支：

```shell
git checkout -b A origin/A
```

origin是远程仓库名，若名字一样origin/A可以省略



将本地分支与远程分支关联：

```shell
git branch --set-upstream A origin/A
```

提示no tracking information错误



# 4.代码版本tag

## 4.1查看tag

本地tag：

```shell
git tag
```

远程tag：

```shell
git tag -r
```

## 4.2操作tag

#### 4.2.1添加tag

给当前版本添加tag：

```shell
git tag 标签名
```

给历史版本添加tag：

```shell
git tag 标签名 commitid
```



#### 4.2.2删除tag

删除本地标签：

```shell
git tag -d 标签名
```

删除远程标签：

```shell
git push origin -d 标签名
```



#### 4.2.3推送到远端仓库

```shell
git push origin 标签名
```

推送所有未提交的tag：

```shell
git push origin --tags
```



#### 4.2.4更新到本地

```shell
git pull origin --tags
```



tag与branch的操作基本一致，因为tag就是一个仅可读的branch



# 5.其他生僻命令

```shell
git blame
git bisect  //过二分查找定位引入bug的变更
git relog
```

可以使用git help查看git常用命令，使用git help -a查看git可用的所有命令

# 6.思维导图

![git常用命令.jpg](https://i.loli.net/2021/10/19/AweybYtX6KmcLJ5.jpg)