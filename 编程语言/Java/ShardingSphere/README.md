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

