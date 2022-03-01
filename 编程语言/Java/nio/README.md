### `NIO`概述

#### 阻塞IO

#### 非阻塞IO(NIO)

#### NIO概述

##### Channel

##### Buffer

##### Selector



### `Channel`

#### Channel概述

#### Channel实现

#### FileChannel介绍和示例

#### FileChannel操作详解

##### 打开FileChannel

##### 从FileChannel读取数据

##### 向FileChannel写数据

##### 关闭FileChannel

##### FileChannel的position方法

##### FileChannel的size方法

##### FileChannel的truncate方法

##### FileChannel的force方法

##### FileChannel的transferTo和transferFrom方法

#### Socket通道

##### ServerSocketChannel

##### SocketChannel

##### DatagramChannel

#### Scatter/Gather

##### Scattering Reads

##### Scattering Writes



### `Buffer`

#### Buffer简介

#### Buffer的基本用法

#### Buffer的capacity、position和limit

#### Buffer的类型

#### Buffer分配和写数据

#### 从Buffer中读取数据

#### Buffer几个方法

#### 缓冲区操作

##### 缓存区分片

##### 只读缓冲区

##### 直接缓冲区

##### 内存映射文件I/O



### `Selector`

#### Selector简介

##### Selector和Channel关系

##### 可选择通道(SelectableChannel)

##### Channel注册到Selector

##### 选择键(SelectionKey)



#### Selector的使用方法

##### Selector的创建

##### 注册Channel到Selector

##### 轮询查询就绪操作

##### 停止选择的方法

##### 示例代码

###### 服务端

###### 客户端

###### 总结



### `Pipe`和`FileLock`

#### Pipe

##### 创建管道

##### 写入管道

##### 从管道读取数据

##### 示例代码



#### FileLock

##### FileLock简介

##### 文件锁分类

##### 示例代码

##### 获取文件锁方法

##### lock和tryLock的区别

##### FileLock两个方法

##### 代码示例





### 其他

#### `Path`和`Files`

##### Path

###### Path简介

###### 创建Path实例

###### 创建绝对路径

###### 创建相对路径

###### Path.normalize()



##### Files

###### Files.createDirectory()

###### Files.copy()

###### Files.move()

###### Files.delete()

###### Files.walkFileTree()



#### `AsynchronousFileChannel`

##### 创建AsynchronousFileChannel

##### 通过Future读取数据

##### 通过CompletionHandler读取数据

##### 通过Future写数据

##### 通过CompletionHandler写数据



#### 字符集(Charset)



### 实战