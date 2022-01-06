使用端：（项目）：引入自定义框架的jar包

提供两部分配置信息：数据库配置信息、`sql`配置信息、`sql`语句、参数类型、返回值类型

使用配置文件提供这两部分配置信息：

​	1、`sqlMapConfig.xml`：存放数据库配置信息，存放`mapper.xml`的全路径

​	2、`mapper.xml`：存放`sql`配置信息



自定义持久层框架本身：工程：本质就是对`JDBC`代码进行了封装

（1）加载配置文件：根据配置文件的路径，加载配置文件成字节输入流，存储在内存中，创建`Resources`类 方法：`InputSteam` `getResourceAsStream(String path)`

（2）创建两个`javaBean`：（容器对象）：存放的就是配置文件解析出来的内容

​		`Configuration`：核心配置类：存放`sqlMapConfig.xml`解析出来的内容

​		`MappedStatement`：映射配置类：存放`mapper.xml`解析出来的内容

（3）解析配置文件：`dom4j`

​		创建类：`SqlSessionFactoryBuilder`      方法：`build(InputSteam in)`

​			第一：使用`dom4j`解析配置文件，将解析出来的内容封装到容器对象中

​			第二：创建`SqlSessionFactory`对象；生成`sqlSession`：会话对象（工厂模式）

（4）创建`SqlSessionFactory`接口以及实现类`DefaultSqlSessionFactory`

​			第一：`openSession()`：生产`sqlSession`

（5）创建`SqlSession`接口以及实现类`DefaultSession`

​			定义对数据库的`crud`操作：`selectList()` `selectOne()`  `update()`  `delete()`

（6）创建`Executor`接口以及实现类`SimpleExecutor`实现类

​			`query(Configuration configuration, MappedStatement mappedStatement, Object ... params)`：执行的就是`JDBC`代码

