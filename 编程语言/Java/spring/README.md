### `Spring`概述

#### Spring简介

#### Spring发展历程

#### Spring的优势

#### Spring的核心架构

#### Spring框架版本



### 核心思想`IOC`和`AOP`

#### `IOC`

##### 什么是`IOC`？

##### `IOC`解决了什么问题

##### `IOC`和`DI`的区别



#### `AOP`

##### 什么是`AOP`

##### `AOP`在解决什么问题

##### 为什么叫做面向切面编程



### 手写`IOC`和`AOP`(自定义`spring`框架)

#### 银行转账案例界面

#### 银行转账案例表结构

#### 银行转账案例代码调用关系

#### 银行转账案例关键代码

#### 银行转账案例代码问题分析

#### 问题解决思路

#### 案例代码改造

### `SringIOC`高级应用

#### 基础特性

##### `BeanFactory`与`ApplicationContext`区别

##### 纯`xml`模式

##### 纯注解模式



#### 高级特性

##### `lazy-Init`延迟加载

##### `FactoryBean`和`BeanFactory`

##### 后置处理器

###### `BeanPostProcessor`

###### `BeanFactoryPostProcessor`



### `Spring IOC`源码深度剖析

#### `Spring IOC`容器初始化主体流程

##### Spring IOC的容器体系

##### Bean生命周期关键时机点

##### Spring IOC容器初始化主流程



#### `BeanFactory`创建流程

##### 获取`BeanFactory`子流程

##### `BeanDefinition`加载解析以及注册子流程



#### `Bean`创建流程

#### `lazy-int`延迟加载机制原理

#### `Spring IOC`循环依赖问题

##### 什么是循环依赖

##### 循环依赖处理机制



### `Spring AOP`高级应用

#### `AOP`相关术语

##### 业务主线

##### `AOP`术语



#### `Spring`中`AOP`的代理选择

#### `Spring`中`AOP`的配置方式

#### `Spring`中`AOP`实现

##### `XML`模式

##### `XML`+注解模式

##### 注解模式



#### `Spring`声明式事务的支持

##### 事务回顾

###### 事务的概念

###### 事务的四大特性

###### 事务的隔离级别

###### 事务的传播行为



##### `Spring`中事务的`API`

##### `Spring`声明式事务配置



### `Spring AOP`源码深度剖析

#### 代理对象创建

##### `AOP`基础用例准备

##### 时机点分析

##### 代理对象创建流程



#### `Spring`声明式事务控制

##### `@EnableTransactionManagement`

##### 加载事务控制组件