#### 1、Redis初识

##### 		1.1、Redis是什么

##### 		1.2、Redis的特性回顾

1. 速度快

   支持10w ops、数据存在内存、使用c语言、线性模型-单线程

2. 持久化

   RDB和AOF

3. 多种数据结构

   ![image-20220204195543554](https://gitee.com/JKcoding/imgs/raw/master/img/202202041955879.png)

   BitMaps：位图

   HyperLogLog：超小内存唯一值计数

   GEO：地理信息定位

4. 支持多种编程语言

   Java、Php、Python、Ruby、Lua、node.js

5. 功能丰富

   发布订阅

   Lua脚本

   事务

   pipeline

6. 简单

   23000 lines of code、不依赖外部库(like  libevent)

   单线程模型

7. 主从复制

   ![image-20220204200152051](https://gitee.com/JKcoding/imgs/raw/master/img/202202042001823.png)

8. 高可用、分布式

   Redis-Sentinel(v2.8)支持高可用

   Redis-Cluster(v3.0)支持分布式

##### 		1.3、Redis单机安装

```shell
wget http://download.redis.io/releases/redis-3.0.7.tar.gz
tar -xzf redis-3.0.7.tar. gz
ln -s redis-3.0.7 redis
cd redis
make && make install
```

redis-server: redis服务器

redis-cli：Redis命令行客户端

redis-benchmark：Redis性能测试工具

redis-check-aof：AOF文件修复工具

redis-check-dump：RDB文件检查工具

redis-sentinel：sentinel服务器(2.8以后)

三种启动方法：

最简启动：redis-server 

验证：

```shell
ps -ef | grep redis
netstat -antpl | grep redis
redis-cli -h ip -p port ping
```

动态参数启动：redis-server --port 6380

配置文件启动：redis-server [configpath配置文件路径]

三种方式比较：

生产环境选择配置启动、单机多实例配置文件可以用端口区分开

客户端连接：

```shell
redis-cli -h 192.168.1.10 -p 6379
```



常用配置：

daemonize：是否是守护线程(no | yes)

port：端口

logfile：redis系统日志

dir：redis工作目录

还有RDB config、AOF config 、slow Log config、maxMemory and so on。。。

##### 		1.4、Redis典型使用场景

- 缓存系统

  ![image-20220204200551435](https://gitee.com/JKcoding/imgs/raw/master/img/202202042005625.png)

  请求到达服务端，服务端请求redis，如果redis存在数据，则直接返回，如果不存在，则查询数据库，然后更新到redis，同时返回数据

- 计数器

​	![image-20220204200833999](https://gitee.com/JKcoding/imgs/raw/master/img/202202042008198.png)

- 消息队列系统

​	![image-20220204200941239](https://gitee.com/JKcoding/imgs/raw/master/img/202202042009552.png)

- 排行榜

  ![image-20220204201008613](https://gitee.com/JKcoding/imgs/raw/master/img/202202042010637.png)

- 社交网络

  ![image-20220204201032469](https://gitee.com/JKcoding/imgs/raw/master/img/202202042010446.png)

- 实时系统

#### 2、API的理解和使用

#### 3、Redis客户端的使用

#### 4、瑞士军刀Redis

#### 5、Redis持久化的取舍和选择

#### 6、Redis复制的原理和优化

#### 7、Redis Sentinel

#### 8、Redis Cluster

