### `shardingsphere`

官网连接：[https://shardingsphere.apache.org/index_zh.html](https://shardingsphere.apache.org/index_zh.html)，可以先参考官网

#### `shardingsphere-jdbc`

##### **水平分表实现**：

1. 创建`springboot`项目，加入依赖

   ```java
   <dependencies>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter</artifactId>
       </dependency>
   
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-test</artifactId>
       </dependency>
   
       <dependency>
           <groupId>com.alibaba</groupId>
           <artifactId>druid-spring-boot-starter</artifactId>
           <version>1.1.20</version>
       </dependency>
       <dependency>
           <groupId>mysql</groupId>
           <artifactId>mysql-connector-java</artifactId>
       </dependency>
       <dependency>
           <groupId>org.apache.shardingsphere</groupId>
           <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
           <version>4.0.0-RC1</version>
       </dependency>
       <dependency>
           <groupId>com.baomidou</groupId>
           <artifactId>mybatis-plus-boot-starter</artifactId>
           <version>3.0.5</version>
       </dependency>
   
       <dependency>
           <groupId>org.projectlombok</groupId>
           <artifactId>lombok</artifactId>
       </dependency>
       <!-- https://mvnrepository.com/artifact/javax.xml.bind/jaxb-api -->
       <dependency>
           <groupId>javax.xml.bind</groupId>
           <artifactId>jaxb-api</artifactId>
           <version>2.3.0-b170201.1204</version>
       </dependency>
   
       <!-- https://mvnrepository.com/artifact/javax.activation/activation -->
       <dependency>
           <groupId>javax.activation</groupId>
           <artifactId>activation</artifactId>
           <version>1.1</version>
       </dependency>
   
       <!-- https://mvnrepository.com/artifact/org.glassfish.jaxb/jaxb-runtime -->
       <dependency>
           <groupId>org.glassfish.jaxb</groupId>
           <artifactId>jaxb-runtime</artifactId>
           <version>2.3.0-b170127.1453</version>
       </dependency>
   
   </dependencies>
   ```

2. 创建数据库`course`

   创建两张表：course_1和course_2

   ```sql
   CREATE TABLE `course_1` (
     `cid` bigint NOT NULL,
     `cname` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
     `user_id` bigint NOT NULL,
     `cstatus` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
     PRIMARY KEY (`cid`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
   ```

   ```sql
   CREATE TABLE `course_2` (
     `cid` bigint NOT NULL,
     `cname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
     `user_id` bigint NOT NULL,
     `cstatus` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
     PRIMARY KEY (`cid`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
   ```

3. `springboot`项目中配置`application.properties`

   ```yaml
   #一个实体类对应两张表，覆盖
   spring.main.allow-bean-definition-overriding=true
   #配置数据源，给数据源起名称
   spring.shardingsphere.datasource.names=m1
   #配置数据源具体内容，包含连接池，驱动，地址，用户名和密码
   spring.shardingsphere.datasource.m1.type=com.alibaba.druid.pool.DruidDataSource
   spring.shardingsphere.datasource.m1.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.shardingsphere.datasource.m1.url=jdbc:mysql://192.168.1.101:3306/course_db?serverTimezone=GMT%2B8
   spring.shardingsphere.datasource.m1.username=root
   spring.shardingsphere.datasource.m1.password=123456
   #指定course表分布情况，配置表在哪个数据库里面，表名称
   spring.shardingsphere.sharding.tables.course.actual-data-nodes=m1.course_$->{1..2}
   #指定course表里面主键生成策略，snowflake
   spring.shardingsphere.sharding.tables.course.key-generator.column=cid
   spring.shardingsphere.sharding.tables.course.key-generator.type=SNOWFLAKE
   #指定分片策略，约定cid值偶数添加到course_1表，如果cid是奇数添加到course_2表
   spring.shardingsphere.sharding.tables.course.table-strategy.inline.sharding-column=cid
   spring.shardingsphere.sharding.tables.course.table-strategy.inline.algorithm-expression=course_$->{cid%2+1}
   #打开sql输出日志
   spring.shardingsphere.props.sql.show=true
   ```

4. 创建实体类和`mapper`

   ```java
   @Data
   public class Course {
       private Long cid;
       private String cname;
       private Long userId;
       private String cstatus;
   }
   ```

   ```java
   @Repository
   public interface CourseMapper extends BaseMapper<Course> {
   }
   ```

5. 测试

   ```java
   /**
    * @Description TODO
    * @Author 千楼
    * @Date 2022-01-01 20:20
    * @Version 1.0
    **/
   @RunWith(SpringRunner.class)
   @SpringBootTest
   public class ShardingjdbcdemoApplicaiton {
   
       @Autowired
       private CourseMapper courseMapper;
   
       @Test
       public void addCourse() {
           for (int i = 0; i < 100; i++) {
               Course course = new Course();
               course.setCname("java");
               course.setUserId(100L);
               course.setCstatus("Normal");
               courseMapper.insert(course);
           }
       }
   
       @Test
       public void findCourse() {
           QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
           queryWrapper.eq("cid", 684152419671605249L);
           Course course = courseMapper.selectOne(queryWrapper);
           System.out.println(course);
       }
   
   }
   ```

   ```java
   022-01-01 23:06:58.245  INFO 24204 --- [           main] ShardingSphere-SQL                       : SQLStatement: SelectStatement(super=DQLStatement(super=AbstractSQLStatement(type=DQL, tables=Tables(tables=[Table(name=course, alias=Optional.absent())]), routeConditions=Conditions(orCondition=OrCondition(andConditions=[AndCondition(conditions=[Condition(column=Column(name=cid, tableName=course), operator=EQUAL, compareOperator==, positionValueMap={}, positionIndexMap={0=0})])])), encryptConditions=Conditions(orCondition=OrCondition(andConditions=[])), sqlTokens=[TableToken(tableName=course, quoteCharacter=NONE, schemaNameLength=0)], parametersIndex=1, logicSQL=SELECT  cid,cname,user_id,cstatus  FROM course  
    WHERE  cid = ?)), containStar=false, firstSelectItemStartIndex=8, selectListStopIndex=32, groupByLastIndex=0, items=[CommonSelectItem(expression=cid, alias=Optional.absent()), CommonSelectItem(expression=cname, alias=Optional.absent()), CommonSelectItem(expression=user_id, alias=Optional.absent()), CommonSelectItem(expression=cstatus, alias=Optional.absent())], groupByItems=[], orderByItems=[], limit=null, subqueryStatement=null, subqueryStatements=[], subqueryConditions=[])
   2022-01-01 23:06:58.246  INFO 24204 --- [           main] ShardingSphere-SQL                       : Actual SQL: m1 ::: SELECT  cid,cname,user_id,cstatus  FROM course_2  
    WHERE  cid = ? ::: [684152419671605249]
   Course(cid=684152419671605249, cname=java, userId=100, cstatus=Normal)
   2022-01-01 23:06:58.287  INFO 24204 --- [extShutdownHook] com.alibaba.druid.pool.DruidDataSource   : {dataSource-1} closing ...
   2022-01-01 23:06:58.293  INFO 24204 --- [extShutdownHook] com.alibaba.druid.pool.DruidDataSource   : {dataSource-1} closed
   ```

   可以看到，查询的sql查询了course_2， 奇数ID查询course_2

   ![image-20220102001420812](https://gitee.com/JKcoding/imgs/raw/master/img/202201020014379.png)

   ![image-20220102001444830](https://gitee.com/JKcoding/imgs/raw/master/img/202201020014746.png)



奇数ID的数据插入了`course_2`,  偶数ID的数据插入了`course_1`



##### 水平分库实现：

1. 在上面的`springboot`项目基础上修改，修改`application.properties`

   ```yaml
   #一个实体类对应两张表，覆盖
   spring.main.allow-bean-definition-overriding=true
   #配置数据源，给数据源起名称
   spring.shardingsphere.datasource.names=m1,m2
   #配置数据源具体内容，包含连接池，驱动，地址，用户名和密码
   spring.shardingsphere.datasource.m1.type=com.alibaba.druid.pool.DruidDataSource
   spring.shardingsphere.datasource.m1.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.shardingsphere.datasource.m1.url=jdbc:mysql://192.168.1.101:3306/edu_db_1?serverTimezone=GMT%2B8
   spring.shardingsphere.datasource.m1.username=root
   spring.shardingsphere.datasource.m1.password=123456
   
   spring.shardingsphere.datasource.m2.type=com.alibaba.druid.pool.DruidDataSource
   spring.shardingsphere.datasource.m2.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.shardingsphere.datasource.m2.url=jdbc:mysql://192.168.1.101:3306/edu_db_2?serverTimezone=GMT%2B8
   spring.shardingsphere.datasource.m2.username=root
   spring.shardingsphere.datasource.m2.password=123456
   
   #指定数据库分库情况，数据库里面表分布情况
   #m1 m2 course_1 course_2
   spring.shardingsphere.sharding.tables.course.actual-data-nodes=m$->{1..2}.course_$->{1..2}
   
   #指定course表里面主键生成策略，snowflake
   spring.shardingsphere.sharding.tables.course.key-generator.column=cid
   spring.shardingsphere.sharding.tables.course.key-generator.type=SNOWFLAKE
   
   #指定分表的分片策略，约定cid值偶数添加到course_1表，如果cid是奇数添加到course_2表
   spring.shardingsphere.sharding.tables.course.table-strategy.inline.sharding-column=cid
   spring.shardingsphere.sharding.tables.course.table-strategy.inline.algorithm-expression=course_$->{cid % 2 + 1}
   
   #指定数据库分片策略 约定user_id是偶数添加m1， 是奇数添加到m2
   #spring.shardingsphere.sharding.default-database-strategy.inline.sharding-column=user_id
   #spring.shardingsphere.sharding.default-database-strategy.inline.algorithm-expression=m$->{user_id%2+1}
   spring.shardingsphere.sharding.tables.course.database-strategy.inline.sharding-column=user_id
   spring.shardingsphere.sharding.tables.course.database-strategy.inline.algorithm-expression=m$->{user_id % 2 + 1}
   #打开sql输出日志
   spring.shardingsphere.props.sql.show=true
   ```

2. 创建两个数据库`edu_db_1`和`edu_db_2`

   `edu_db_1`中创建两张表：`course_1`和`course_2`

   ```sql
   CREATE TABLE `course_1` (
     `cid` bigint NOT NULL,
     `cname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
     `user_id` bigint NOT NULL,
     `cstatus` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
     PRIMARY KEY (`cid`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
   ```

   ```sql
   CREATE TABLE `course_2` (
     `cid` bigint NOT NULL,
     `cname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
     `user_id` bigint NOT NULL,
     `cstatus` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
     PRIMARY KEY (`cid`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
   ```

   `edu_db_2`同理创建两张表：`course_1`和`course_2`

3. 测试

   ```java
   @Test
   public void addCourse() {
       for (int i = 0; i < 10; i++) {
           Course course = new Course();
           course.setCname("java");
           course.setUserId(101L);
           course.setCstatus(UUID.randomUUID().toString().substring(0, 5));
           courseMapper.insert(course);
       }
   }
   ```

   根据规则，`user_id`为奇数，则插入`edu_db_2`数据库中， 奇数`cid`插入`course_2`中，偶数cid插入`course_1`中

   ![image-20220103162842350](https://gitee.com/JKcoding/imgs/raw/master/img/202201031628437.png)

​	![image-20220103162932273](https://gitee.com/JKcoding/imgs/raw/master/img/202201031629782.png)

​	

```java
@Test
public void addCourse() {
    for (int i = 0; i < 10; i++) {
        Course course = new Course();
        course.setCname("java");
        course.setUserId(100L);
        course.setCstatus(UUID.randomUUID().toString().substring(0, 5));
        courseMapper.insert(course);
    }
}
```

根据规则，`user_id`为偶数，则插入`edu_db_1`数据库中， 奇数`cid`插入`course_2`中，偶数cid插入`course_1`中

![image-20220103163102466](https://gitee.com/JKcoding/imgs/raw/master/img/202201031631276.png)

![image-20220103163121247](https://gitee.com/JKcoding/imgs/raw/master/img/202201031631766.png)



##### 垂直切分实现：

1. 在上面的`springboot`项目中，修改配置文件`application.properties`

   ```yaml
   #一个实体类对应两张表，覆盖
   spring.main.allow-bean-definition-overriding=true
   #配置数据源，给数据源起名称
   spring.shardingsphere.datasource.names=m0
   #配置数据源具体内容，包含连接池，驱动，地址，用户名和密码
   spring.shardingsphere.datasource.m0.type=com.alibaba.druid.pool.DruidDataSource
   spring.shardingsphere.datasource.m0.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.shardingsphere.datasource.m0.url=jdbc:mysql://192.168.1.101:3306/user_db?serverTimezone=GMT%2B8
   spring.shardingsphere.datasource.m0.username=root
   spring.shardingsphere.datasource.m0.password=123456
   #配置user_db数据库里面t_user专库专表
   spring.shardingsphere.sharding.tables.t_user.actual-data-nodes=m$->{0}.t_user
   #指定t_user表里面主键生成策略，snowflake
   spring.shardingsphere.sharding.tables.t_user.key-generator.column=user_id
   spring.shardingsphere.sharding.tables.t_user.key-generator.type=SNOWFLAKE
   spring.shardingsphere.sharding.tables.t_user.table-strategy.inline.sharding-column=user_id
   spring.shardingsphere.sharding.tables.t_user.table-strategy.inline.algorithm-expression=t_user
   #打开sql输出日志
   spring.shardingsphere.props.sql.show=true
   ```

2. 创建实体类`User`

   ```java
   @TableName("t_user")
   @Data
   public class User {
       private Long userId;
       private String username;
       private String ustatus;
   }
   ```

3. 创建数据库`user_db`和表`t_user`

   ```sql
   CREATE TABLE `t_user` (
     `user_id` bigint NOT NULL,
     `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
     `ustatus` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
     PRIMARY KEY (`user_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
   ```

4. 测试

   ```java
   @Test
   public void addUser() {
       User user = new User();
       user.setUsername("lzj");
       user.setUstatus("a");
       useMapper.insert(user);
   }
   ```

```java
2022-01-03 17:44:15.424  INFO 15740 --- [           main] ShardingSphere-SQL                       : Actual SQL: m0 ::: INSERT INTO t_user   (username, ustatus, user_id) VALUES (?, ?, ?) ::: [lzj, a, 684818507082235905]
```

可以看到插入到了指定的`m0（user_db）`库的`t_user`表中

```java
@Test
public void findUser() {
    QueryWrapper queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("user_id", 684812758394339329L);
    User user = useMapper.selectOne(queryWrapper);
    System.out.println(user);
}
```

```java
2022-01-03 17:46:50.857  INFO 27560 --- [           main] ShardingSphere-SQL                       : Actual SQL: m0 ::: SELECT  user_id,username,ustatus  FROM t_user  
 WHERE  user_id = ? ::: [684812758394339329]
User(userId=684812758394339329, username=lzj, ustatus=a)
```

可以看到，从指定的数据库`m0（user_db）`库的`t_user`表中查询到数据



##### 公共表操作：

1. 在上面`springboot`项目的基础上，修改`application.properties`

   ```yaml
   #一个实体类对应两张表，覆盖
   spring.main.allow-bean-definition-overriding=true
   #配置数据源，给数据源起名称
   spring.shardingsphere.datasource.names=m0,m1,m2
   #配置数据源具体内容，包含连接池，驱动，地址，用户名和密码
   spring.shardingsphere.datasource.m0.type=com.alibaba.druid.pool.DruidDataSource
   spring.shardingsphere.datasource.m0.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.shardingsphere.datasource.m0.url=jdbc:mysql://192.168.1.101:3306/user_db?serverTimezone=GMT%2B8
   spring.shardingsphere.datasource.m0.username=root
   spring.shardingsphere.datasource.m0.password=123456
   
   spring.shardingsphere.datasource.m1.type=com.alibaba.druid.pool.DruidDataSource
   spring.shardingsphere.datasource.m1.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.shardingsphere.datasource.m1.url=jdbc:mysql://192.168.1.101:3306/edu_db_1?serverTimezone=GMT%2B8
   spring.shardingsphere.datasource.m1.username=root
   spring.shardingsphere.datasource.m1.password=123456
   
   spring.shardingsphere.datasource.m2.type=com.alibaba.druid.pool.DruidDataSource
   spring.shardingsphere.datasource.m2.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.shardingsphere.datasource.m2.url=jdbc:mysql://192.168.1.101:3306/edu_db_2?serverTimezone=GMT%2B8
   spring.shardingsphere.datasource.m2.username=root
   spring.shardingsphere.datasource.m2.password=123456
   #配置公共表
   spring.shardingsphere.sharding.broadcast-tables=t_udict
   spring.shardingsphere.sharding.tables.t_udict.key-generator.column=dictid
   spring.shardingsphere.sharding.tables.t_udict.key-generator.type=SNOWFLAKE
   #打开sql输出日志
   spring.shardingsphere.props.sql.show=true
   ```

2. 新增实体类`Udict`

   ```java
   @Data
   @TableName("t_udict")
   public class Udict {
       private Long dictid;
       private String ustatus;
       private String uvalue;
   }
   ```

   ```java
   @Repository
   public interface UdictMapper extends BaseMapper<Udict> {
   }
   ```

3. 在三个数据库`user_db`、`edu_db_1`、`edu_db_2`中创建表`t_udict`

   ```sql
   CREATE TABLE `t_udict` (
     `dictid` bigint NOT NULL,
     `ustatus` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
     `uvalue` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
     PRIMARY KEY (`dictid`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
   ```

4. 测试

   ```java
   @Test
   public void addUdict() {
       Udict udict = new Udict();
       udict.setUstatus("a");
       udict.setUvalue("已启动");
       udictMapper.insert(udict);
   }
   
   @Test
   public void deleteUdict() {
       QueryWrapper<Udict> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("dictid", 684882142416601089L);
       udictMapper.delete(queryWrapper);
   }
   ```



##### 读写分离：

![image-20220103220725391](https://gitee.com/JKcoding/imgs/raw/master/img/202201032207180.png)

![image-20220103220803722](https://gitee.com/JKcoding/imgs/raw/master/img/202201032208775.png)

之前写的文章：[基于docker环境的mysql主从复制](https://blog.csdn.net/qq_36609994/article/details/105172446?spm=1001.2014.3001.5502)

1. 在开始之前，参考上面提供的文章，先实现MySQL的主从复制配置

2. 在上面的`springboot`项目修改`application.properties`

   ```yaml
   #一个实体类对应两张表，覆盖
   spring.main.allow-bean-definition-overriding=true
   #配置数据源，给数据源起名称
   spring.shardingsphere.datasource.names=m0,s0
   
   #配置数据源具体内容，包含连接池，驱动，地址，用户名和密码
   #主数据库
   spring.shardingsphere.datasource.m0.type=com.alibaba.druid.pool.DruidDataSource
   spring.shardingsphere.datasource.m0.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.shardingsphere.datasource.m0.url=jdbc:mysql://192.168.1.101:3307/user_db?serverTimezone=GMT%2B8
   spring.shardingsphere.datasource.m0.username=root
   spring.shardingsphere.datasource.m0.password=123456
   #从数据库
   spring.shardingsphere.datasource.s0.type=com.alibaba.druid.pool.DruidDataSource
   spring.shardingsphere.datasource.s0.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.shardingsphere.datasource.s0.url=jdbc:mysql://192.168.1.101:3308/user_db?serverTimezone=GMT%2B8
   spring.shardingsphere.datasource.s0.username=root
   spring.shardingsphere.datasource.s0.password=123456
   #主数据库
   spring.shardingsphere.sharding.master-slave-rules.ds0.master-data-source-name=m0
   #从数据库
   spring.shardingsphere.sharding.master-slave-rules.ds0.slave-data-source-names=s0
   spring.shardingsphere.sharding.tables.t_user.actual-data-nodes=ds0.t_user
   #打开sql输出日志
   spring.shardingsphere.props.sql.show=true
   ```

3. 数据库创建`user_db`数据库和表`t_user`

   ```sql
   CREATE TABLE `t_user` (
     `user_id` bigint NOT NULL,
     `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
     `ustatus` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
     PRIMARY KEY (`user_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
   ```

4. 测试

   ```java
   @Test
   public void addUser() {
       User user = new User();
       user.setUsername("lzj");
       user.setUstatus("a");
       user.setUserId(123456216L);
       useMapper.insert(user);
   }
   ```

   ```shell
   2022-01-04 01:27:40.982  INFO 31584 --- [           main] ShardingSphere-SQL                       : Actual SQL: m0 ::: INSERT INTO t_user   (user_id, username, ustatus) VALUES (?, ?, ?) ::: [123456216, lzj, a]
   ```

   可以看到数据新增到主数据库`m0`中

   ```java
   @Test
   public void findUser() {
       QueryWrapper queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("user_id", 684812758394339329L);
       User user = useMapper.selectOne(queryWrapper);
       System.out.println(user);
   }
   ```

   ```shell
   2022-01-04 01:28:38.715  INFO 28272 --- [           main] ShardingSphere-SQL                       : Actual SQL: s0 ::: SELECT  user_id,username,ustatus  FROM t_user  
    WHERE  user_id = ? ::: [684812758394339329]
   ```

   可以看到查询数据是在从数据库`s0`中读取的

   

#### `shardingsphere-proxy`

**代码**：

entity：

```java
@Data
public class Course {
    private Long cid;
    private String cname;
    private Long userId;
    private String cstatus;
}
```

```java
@Data
@TableName("t_udict")
public class Udict {

    private Long dictid;
    private String ustatus;
    private String uvalue;
}
```

```java
@TableName("t_user")
@Data
public class User {
    private Long userId;
    private String username;
    private String ustatus;
}
```

mapper:

```java
@Repository
public interface CourseMapper extends BaseMapper<Course> {
}
```

```java
@Repository
public interface UdictMapper extends BaseMapper<Udict> {
}
```

```java
@Repository
public interface UseMapper extends BaseMapper<User> {
}
```

pom.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.1.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.shardingjdbc</groupId>
    <artifactId>shardingjdbcdemo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>shardingjdbcdemo</name>
    <description>shardingjdbcdemo</description>
    <properties>
        <java.version>11</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.20</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
            <version>4.0.0-RC1</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.0.5</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/javax.xml.bind/jaxb-api -->
        <dependency>
            <groupId>javax.xml.bind</groupId>
            <artifactId>jaxb-api</artifactId>
            <version>2.3.0-b170201.1204</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/javax.activation/activation -->
        <dependency>
            <groupId>javax.activation</groupId>
            <artifactId>activation</artifactId>
            <version>1.1</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.glassfish.jaxb/jaxb-runtime -->
        <dependency>
            <groupId>org.glassfish.jaxb</groupId>
            <artifactId>jaxb-runtime</artifactId>
            <version>2.3.0-b170127.1453</version>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```
