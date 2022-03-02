### 自定义持久层框架

#### 分析`JDBC`操作问题

#### 问题解决思路

#### 自定义框架设计

#### 自定义框架实现

#### 自定义框架优化



### `Mybatis`相关概念

#### 对象/关系数据库映射`(ORM)`

#### `Mybatis`简介

#### `Mybatis`历史

#### `Mybatis`优势



### `Mybatis`基本应用

#### 快速入门

#### `Mybatis`的`Dao`层实现

##### 传统开发方式

##### 代理开发方式



### `Mybatis`配置文件深入

#### 核心配置文件`SqlMapConfig.xml`

##### `Mybatis`核心配置文件层级关系

##### `Mybatis`常用配置解析



#### 映射配置文件`mapper.xml`



### `Mybatis`复杂映射开发

#### 一对一查询

##### 一对一查询的模型

##### 一对一查询的语句

##### 创建`Order`和`User`实体

##### 创建`OrderMapper`接口

##### 配置`OrderMapper.xml`

##### 测试结果



#### 一对多查询

##### 一对多的模型

##### 一对多查询的语句

##### 修改`User`实体

##### 创建`UserMapper`接口

##### 配置`UserMapper.xml`

##### 测试结果



#### 多对多查询

##### 多对多查询的模型

##### 多对多查询的语句

##### 创建`Role`实体，修改`User`实体

##### 添加`UserMapper`接口方法

##### 配置`UserMapper.xml`

##### 测试结果



#### 知识小结



### `Mybatis`注解开发

#### `Mybatis`的常用注解

#### `Mybatis`的增删改查

#### `Mybatis`的注解实现复杂映射开发

#### 一对一查询

##### 一对一查询的模型

##### 一对一查询的语句

##### 创建`Order`和`User`实体

##### 创建`OrderMapper`接口

##### 配置`OrderMapper.xml`

##### 测试结果



#### 一对多查询

##### 一对多的模型

##### 一对多查询的语句

##### 修改`User`实体

##### 创建`UserMapper`接口

##### 配置`UserMapper.xml`

##### 测试结果



#### 多对多查询

##### 多对多查询的模型

##### 多对多查询的语句

##### 创建`Role`实体，修改`User`实体

##### 添加`UserMapper`接口方法

##### 配置`UserMapper.xml`

##### 测试结果



### `Mybatis`缓存

#### 一级缓存

#### 二级缓存

#### 二级缓存整合`redis`



### `Mybatis`插件

#### 插件简介

#### `Mybatis`插件介绍

#### `Mybatis`插件原理

#### 自定义插件

#### 源码分析

#### `pageHelper`分页插件

#### 通用`mapper`





### `Mybatis`架构原理

#### 架构设计

#### 主要构件以及其相互关系

#### 总体流程



### `Mybatis`源码剖析

#### 传统方式源码剖析

#### Mapper代理方式

#### 二级缓存源码剖析

#### 延迟加载源码剖析





### 设计模式

#### `Builder`构建者模式

#### 工厂模式

#### 代理模式



### `Mybatis-Plus`

#### `Mybatis-plus`概念

##### `Mybatis-plus`介绍

##### 特性

##### 架构

##### 作者



#### `Mybatis-plus`快速入门

##### 安装

###### `Springboot`

###### `Spring MVC`

##### 创建数据库以及表

##### 创建工程

##### `Mybatis + MP`

###### 创建子Module

###### Mybatis实现查询User

###### Mybatis+MP实现查询User



##### `Spring + Mybatis + MP`

###### 创建子`module`

###### 实现查询`User`



##### `SpringBoot + Mybatis + MP`

###### 创建工程

###### 导入依赖

###### 编写`application.properties`

###### 编写`pojo`

###### 编写`Mapper`

###### 编写启动类

###### 编写测试用例





#### 通用`CRUD`

##### 插入操作

###### @TableField



##### 更新操作

###### 根据`id`更新

###### 根据条件更新



##### 删除操作

###### `deleteById`

###### `deleteByMap`

###### `delete`

###### `deleteBatchIds`



##### 查询操作

###### `selectById`

###### `selectBatchIds`

###### `selectOne`

###### `selectCount`

###### `selectList`

###### `selectPage`



##### `SQL`注入的原理



#### 配置

##### 基本配置

###### `configLocation`

###### `mapperLocation`

###### `typeAliasePackage`



##### 进阶配置

###### `mapUnderscoreToCamelCase`

###### `cacheEnabled`



##### `DB`策略配置

###### `idType`

###### `tablePrefix`



#### 条件构造器

##### `allEq`

###### 说明

###### 测试用例



##### 基本比较操作

##### 模糊查询

##### 排序

##### 逻辑查询

##### select



#### `ActiveRecord`

##### 开启AR之旅

##### 根据主键查询

##### 新增操作

##### 更新操作

##### 删除操作

##### 根据条件查询





#### 插件

##### `Mybatis`的插件机制

##### 执行分析插件

##### 性能分析插件

##### 乐观锁插件

###### 主要适用场景

###### 插件配置

###### 注解实体字段

###### 测试

###### 特别说明



#### `SQL`注入器

##### 编写`MyBaseMapper`

##### 编写`MySqlInjector`

##### 编写`FindAll`

##### 注册到Spring容器

##### 测试





#### 自动填充功能

##### 添加`@TableField`注解

##### 编写`MyMetaObjectHandler`

##### 测试



#### 逻辑删除

##### 修改表结构

##### 配置

##### 测试



#### 代码生成器

##### 创建工程

##### 代码

##### 测试



#### `MybatisX`快速开发插件