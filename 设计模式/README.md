

#### 为什么要学习设计模式

- 公司项目需要重构，但是不知道如何下手，做到高內聚低耦合
- 为了装下武林高手，面试应对面试官的各种刁难（看招聘要求）
- 为了写些高大上的代码，让新手看不懂。或者为了看懂高手写的代码
- 为了提升自己内力，更好的理解框架源码设计思想，封装中间件
- 让代码更好重用、可读、可靠、可维护、可拓展

#### 学前基础

- 掌握`java/ python/js/c/c++/go`其中一门语言
- 课程基于`java`语言实现，重点是掌握其思想，语言只是实现工具

#### 适合人群

- 中高级客户端、前端、后端工程师、项目经理、`CTO`必备知识
- 从传统软件公司过渡到互联网公司的人员
- 只要是软件开发人员，都建议学这个课程，提升内力

#### 学后水平

- 掌握软件设计的六大原则
- 掌握**创建型模式**：**工厂模式**、**抽象工厂模式**、**单例模式**、**建造者模式**、**原型模式**
- 掌握**结构型模式**：**适配器模式**、**桥接模式**、**装饰器模式**、**代理模式**、**组合模式**、**外观模式**、**享元模式**
- 掌握**行为型模式**：**责任链模式**、**迭代器模式**、**观察者模式**、**状态模式**、**策略模式**、**模板模式**、**备忘录模式**、**命令模式**等
- 掌握20多种设计模式的应用场景、优点、缺点和需求案例实战
- 掌握多个源码里面设计模式的应用和面试题



#### 设计模式六大设计原则

为了让的代码更好重用性，可读性，可靠性，可维护性诞生出了很多软件设计的原则，这6大设计原则是我们

要掌握的，将六大原则的英文首字母拼在一起就是`SOLID`（稳定的），所以也称之为`SOLID`原则

##### 单一职责原则

- 一个类只负责一个功能领域中的相应职责，就一个类而应该只有一个引起它变化的原因
- 是实现高內聚、低耦合的指导方针
- 解释：

​		■高内聚

​				■尽可能类的每个成员方法只完成一件事（最大限度的聚合）

​				■模块内部的代码，相互之间的联系越强，内聚就越高，模块的独立性就越好

​		■低耦合:减少类内部，一个成员方法调用另一个成员方法，不要有牵一发动全身

##### 开闭原则

对扩展开放，对修改关闭，在程序需要进行拓展的时候，不能去修改原有的代码，实现一个热插拔的效果

##### 里氏替换原则`LSP`

- 任何基类可以出现的地方，子类一定可以出现
- 在程序中尽量使用基类类型来对对象进行定义，而在运行时再确定其子类类型，用子类对象来替换父类对象
- `controller->service->dao`

##### 依赖倒转原则

- 是开闭原则的基础，针对接口编程，依赖于抽象而不依赖于具体
- 高层模块不应该依赖低层模块，二者都应该依赖其抽象

##### 接口隔离原则

- 客户端不应该依赖那些它不需要的接口
- 使用多个隔离的接口，比使用单个接口要好，降低类之间的耦合度

##### 迪米特法则

- 最少知道原则，一个实体应当尽量少地与其他实体之间发生相互作用，使得系统功能模块相对独立
- 类之间的耦合度越低，就越有利于复用，一个处在松耦合中的类一旦被修改，不会对关联的类造成太大波及
- 通过引入一个合理的第三者来降低现有对象之间的耦合度



#### 设计模式到底是什么

**设计模式简介**：

- 由来:是软件开发人员在软件开发过程中面临的一般问题的解决方案。这些解决方案是众多软件开发人员经过相当长的一段时间的试验和错误总结出来的
- 好处:为了重用代码、让代码更容易被他人理解、保证代码可靠性
- 坏处:对不熟悉设计模式的同学，看起来更绕更难理解

**什么是GOF(Gang of Four)**

在1994年，由四位作者合称GOF（全拼 Gang of Four四人合著出版了一本名为 Design Patterns- Elements

of Reusable object- Oriented software。他们所提出的设计模式主要是基于以下的面向对象设计原则。

1. 对接口编程而不是对实现编程。
2. 优先使用对象组合而不是继承

#### 常见的三大设计模式分类

##### 创建型模式

​	**概念**：提供了一种在创建对象的同时隐藏创建逻辑的方式，使得程序在判断针对某个给定实例需要创建哪些对象时更加灵活

​	**常用**：工厂模式、抽象工厂模式、单例模式、建造者模式

​	**不常用**: 原型模式

##### 结构型模式

​	**概念**：关注类和对象的组合。继承的概念被用来组合接口和定义组合对象获得新功能的方式

​	**常用**∶ 适配器模式、桥接模式、装饰器模式、代理模式

​	**不常用**: 组合模式、外观模式、享元模式

##### 行为型模式

​	**概念**：特别关注对象之间的通信

​	**常用**: 责任链模式、迭代器模式、观察者模式、状态模式、策略模式、模板模式

​	**不常用**: 备忘录模式、命令模式

​	**几乎不用**: 访问者模式、中介者模式、解释器模式



#### 单例设计模式

所谓类的单例设计模式，就是采取一定的方法保证在整个的软件系统中，对某个类**只能存在一个对象实例**，并且该类只提供一个取得其对象实例的方法(静态方法)。

**使用场景**：

- 比如`Hibernate`的`SessionFactory`，它充当数据存储源的代理，并负责创建`Session`对象。`SessionFactory`并不是轻量级的，一般情况下，一个项目通常只需要一个`SessionFactory`就够，这是就会使用到单例模式。
- 业务系统全局只需要一个对象实例，比如发号器`redis`连接对象等
- `SpringIOC`容器中的`bean`默认就是单例
- `springboot`中的`controller`、`service`、`dao`层中通过`＠autowire`的依赖注入对象默认都是单例的

**分类**：

- 懒汉:  就是所谓的懒加载，延迟创建对象
- 饿汉:  与懒汉相反，提前创建对象

**八种实现方式**：

1. 饿汉式(静态常量)

   **步骤如下**：

   1) 构造器私有化 (防止 new )

   2) 类的内部创建对象

   3) 向外暴露一个静态的公共方法。`getInstance`

   4) 代码实现

   **代码实现**：

   ```java
   public class SingleTon01 {
       
       private SingleTon01(){}
       
       private static SingleTon01 instance = new SingleTon01();
       
       public static SingleTon01 getInstance() {
           return instance;
       }
   }
   ```

   **优缺点**：

   1) 优点：这种写法比较简单，就是在类装载的时候就完成实例化。避免了线程同步问题。

   2) 缺点：在类装载的时候就完成实例化，没有达到`LazyLoading`的效果。如果从始至终从未使用过这个实例，则会造成内存的浪费

   3) 这种方式基于`classloder`机制避免了多线程的同步问题，不过，`instance`在类装载时就实例化，在单例模式中大多数都是调用`getInstance`方法， 但是导致类装载的原因有很多种，因此不能确定有其他的方式（或者其他的静态方法）导致类装载，这时候初始化`instance`就没有达到`lazyloading`的效果

   4) 结论：这种单例模式**可用**，**可能**造成内存浪费

      

2. 饿汉式（静态代码块）

   **代码实现**：

   ```java
   public class SingleTon01 {
   
       private SingleTon01(){}
   
       private static SingleTon01 instance;
       static {
           instance = new SingleTon01();
       }
   
       public static SingleTon01 getInstance() {
           return instance;
       }
   }
   ```

   **优缺点说明**：

   1) 这种方式和上面的方式其实类似，只不过将类实例化的过程放在了静态代码块中，也是在类装载的时候，就执行静态代码块中的代码，初始化类的实例。优缺点和上面是一样的。

   2) 结论：这种单例模式可用，但是可能造成内存浪费

   

3. 懒汉式(线程不安全)

   **代码实现**：

   ```java
   public class SingleTon01 {
   
       private SingleTon01(){}
   
       private static SingleTon01 instance;
   
       public static SingleTon01 getInstance() {
           if (instance == null) {
               instance = new SingleTon01();
           }
           return instance;
       }
   }
   ```

   **优缺点说明**：

   1) 起到了Lazy Loading的效果，但是只能在单线程下使用。

   2) 如果在多线程下，一个线程进入了if (singleton == null)判断语句块，还未来得及往下执行，另一个线程也通过了这个判断语句，这时便会产生多个实例。所以在多线程环境下不可使用这种方式

   3) 结论：在实际开发中，不要使用这种方式.

   

4. 懒汉式(线程安全，同步方法)

   **代码实现**：

   ```java
   public class SingleTon01 {
   
       private SingleTon01(){}
   
       private static SingleTon01 instance;
       //加入了同步代码 解决了线程不安全问题
       public static synchronized SingleTon01 getInstance() {
           if (instance == null) {
               instance = new SingleTon01();
           }
           return instance;
       }
   }
   ```

   **优缺点说明**：

   1) 解决了线程不安全问题

   2) 效率太低了，每个线程在想获得类的实例时候，执行`getInstance()`方法都要进行同步。而其实这个方法只执行一次实例化代码就够了，后面的想获得该类实例，直接return就行了。方法进行同步效率太低

   3) 结论：在实际开发中，不推荐使用这种方式

   

5. 懒汉式(线程安全，同步代码块)

   **代码实现**：

   ```java
   public class SingleTon01 {
   
       private SingleTon01(){}
   
       private static SingleTon01 instance;
      
       public static SingleTon01 getInstance() {
           if (instance == null) {
               //多个线程同时到达这里
               synchronized (SingleTon01.class) {
                   instance = new SingleTon01();
               }
           }
           return instance;
       }
   }
   ```

   **优缺点说明**：

   1) 这种方式，本意是想对第四种实现方式的改进，因为前面同步方法效率太低，改为同步产生实例化的的代码块

   2) **但是这种同步并不能起到线程同步的作用**。跟第3种实现方式遇到的情形一致，假如一个线程进入了`if (singleton == null)`判断语句块，还未来得及往下执行，另一个线程也通过了这个判断语句，这时便会产生多个实例

   3) 结论：在**实际开发中，不能使用这种方**式

   

6. 双重检查

   **代码实现**：

   ```java
   public class SingleTon01 {
   
       private SingleTon01(){}
   
       private static SingleTon01 instance;
   
       public static SingleTon01 getInstance() {
           if (instance == null) {
               synchronized (SingleTon01.class) {
                   if (instance == null) {
                       instance = new SingleTon01();
                   }
               }
           }
           return instance;
       }
   }
   ```

   **优缺点说明**：

   1) Double-Check概念是多线程开发中常使用到的，如代码中所示，我们进行了两次`if (singleton == null)`检查，这样就可以保证线程安全了。

   2) 这样，实例化代码只用执行一次，后面再次访问时，判断`if (singleton == null)`，直接return实例化对象，也避免的反复进行方法同步.

   3) 线程安全；延迟加载；效率较高

   4) 结论：在实际开发中，推荐使用这种单例设计模式

   

7. 静态内部类

   **代码实现**：

   ```java
   public class SingleTon01 {
   
       private SingleTon01(){}
   
       private static SingleTon01 instance;
       
       public static SingleTon01 getInstance() {
           return SingleTonInstance.INSTANCE;
       }
       
       private static class SingleTonInstance{
           private static final SingleTon01 INSTANCE = new SingleTon01();
       }
   }
   ```

   **优缺点说明**：

   1) 这种方式采用了类装载的机制来保证初始化实例时只有一个线程。

   2) 静态内部类方式在`Singleton01`类被装载时并不会立即实例化，而是在需要实例化时，调用`getInstance`方法，才会装载`SingletonInstance`类，从而完成`Singleton01`的实例化。

   3) 类的静态属性只会在第一次加载类的时候初始化，所以在这里，`JVM`帮助我们保证了线程的安全性，在类进行初始化时，别的线程是无法进入的。

   4) 优点：避免了**线程不安全**，利用静态内部类特点实现延迟加载，效率高

   5) 结论：推荐使用.

   

8. 枚举

   **代码实现**：

   ```java
   public class SingleTon01 {
   
       private SingleTon01() {
       }
   
       private static SingleTon01 instance;
   
       public static SingleTon getInstance() {
           return SingleTon.INSTANCE;
       }
   }   
   enum SingleTon {
       INSTANCE;
       public void method() {
           
       }
   }
   ```

   **优缺点说明**：

   1) 这借助`JDK1.5`中添加的枚举来实现单例模式。不仅能避免多线程同步问题，而且还能防止反序列化重新创建新的对象。

   2) 这种方式是Effective Java作者Josh Bloch 提倡的方式

   3) 结论：推荐使用

   

   **单例模式注意事项和细节说明**

1) 单例模式保证了 系统内存中该类只存在一个对象，节省了系统资源，对于一些需要频繁创建销毁的对象，使用单例模式可以提高系统性能

2) 当想实例化一个单例类的时候，必须要记住使用相应的获取对象的方法，而不是使用new

3) 单例模式使用的场景：需要频繁的进行创建和销毁的对象、创建对象时耗时过多或耗费资源过多(即：重量级对象)，但又经常用到的对象、工具类对象、频繁访问数据库或文件的对象(比如数据源、session工厂等)



#### 工厂模式

##### **简单工厂模式**

1）简单工厂模式是属于**创建型模式**，是工厂模式的一种。简单工厂模式是由一个工厂对象决定创建岀哪一种产品

类的实例。简单工厂模式是工厂模式家族中最简单实用的模式

2）简单工厂模式:定义了一个创建对象的类，由这个类来封装实例化对象的行为代码

3）在软件开发中，当我们会用到大量的创建某种、某类或者某批对象时，就会使用到工厂模式.



**使用**：

![image-20220130181825116](https://gitee.com/JKcoding/imgs/raw/master/img/202201301818372.png)

1）简单工厂模式的设计方案：定义一个可以实例化`Pizaa`对象的类，封装创建对象的代码

2）代码实现(pizza工厂一共生产三种类型的pizza：chesse,pepper,greak。通过工厂类（SimplePizzaFactory）实例化这三种类型的对象)

```java
public class SimpleFactory {

    public Pizza createPizza(String orderType) {
        Pizza pizza = null;
        System.out.println("使用简单工厂模式");
        if (orderType.equals("greek")) {
            pizza = new GreekPizza();
            pizza.setName("希腊披萨");
        } else if (orderType.equals("奶酪披萨")) {
            pizza = new CheesePizza();
            pizza.setName("奶酪披萨");
        } else if (orderType.equals("pepper")) {
            pizza = new PepperPizza();
            pizza.setName("胡椒披萨");
        }
        return pizza;
    }
}
```

**优点**

1. 将对象的创建和对象本身业务处理分离可以降低系统的耦合度，使得两者修改起来都相对容易

**缺点**

1. 工厂类的职责相对过重，增加新的产品需要修改工厂类的判断逻辑，这一点与开闭原则是相违背
2. 即开闭原则（ （Open Close Principle）对扩展开放，对修改关闭，程序需要进行拓展的时候，不能去修改原有的代码，实现一个热插拔的效果
3. 将会增加系统中类的个数，在一定程度上增加了系统的复杂度和理解难度，不利于系统的扩展和维护，创建简单对象就不用模式



##### 工厂方法模式

- 又称工厂模式，是对简单工厂模式的进一步抽象化，其好处是可以使系统在不修改原来代码的情况下引进新的产品，即满足开闭原则
- 通过工厂父类定义负责创建产品的公共接口，通过子类来确定所需要创建的类型
- 相比简单工厂而言，此种方法具有更多的可扩展性和复用性，同时也增强了代码的可读性
- 将类的实例化（具体产品的创建）延迟到工厂类的子类（具体工厂）中完成，即由子类来决定应该实例化哪一个类。

**核心组成**

- `IProduct`:抽象产品类，描述所有实例所共有的公共接
- `Product`:具体产品类，实现抽象产品类的接口，工厂类创建对象，如果有多个需要定义多个
- `IFactory`:抽象工厂类，描述具体工厂的公共接口
- `Factory`:具体工场类，实现创建产品类对象，实现抽象工厂类的接口，如果有多个需要定义多个

![image-20220130184952137](https://gitee.com/JKcoding/imgs/raw/master/img/202201301849138.png)

**编码实践**

```java
public interface Pay {
    void unifiedorder();
}
```

```java
public interface PayFactory {
    Pay getPay();
}
```

```java
public class AliPay implements Pay{
    @Override
    public void unifiedorder() {
        System.out.println("支付宝支付，统一下单接口");
    }
}
```

```java
public class WeichatPay implements Pay{
    @Override
    public void unifiedorder() {
        System.out.println("微信支付，统一下单接口");
    }
}
```

**优点**:

o符合开闭原则，增加一个产品类，只需要实现其他具体的产品类和具体的工厂类

o符合单一职责原则，每个工厂只负责生产对应的产品

o使用者只需要知道产品的抽象类，无须关心其他实现类，满足迪米特法则、依赖倒置原则和里氏替换原则

​	■迪米特法则:最少知道原则，实体应当尽量少地与其他实体之间发生相互作用

​	■依赖倒置原则:针对接口编程，依赖于抽象而不依赖于具体

​	■里氏替换原则:俗称`LSP`，任何基类可以出现的地方，子类一定可以出现，对实现抽象化的具体步骤的规范

**缺点**

o增加一个产品，需要实现对应的具体工厂类和具体产品

o每个产品需要有对应的具体工厂和具体产品类



##### 抽象工厂方法模式

**工厂模式有3种不同的实现方式**

o **简单工厂模式**:通过传入相关的类型来返回相应的类这种方式比较单一，可扩展性相对较差

σ **工厂方法模式**:通过实现类实现相应的方法来决定相应的返回结果，这种方式的可扩展性比较强

σ **抽象工厂模式**:基于上述两种模式的拓展，是工厂方法模式的升级版，当需要创建的产品有多个产品线时使用抽象工厂模式是比较好的选择

σ抽象工厂模式在 Spring中应用得最为广泛的一种设计模式



**背景**

o 工厂方法模式引入工厂等级结构，解决了简单工厂模式中工厂类职责过重的问题

o 但工厂方法模式中每个工厂只创建一类具体类的对象，后续发展可能会导致工厂类过多，因此将一些相关的具体类组成一个“具体类族“，由同一个工厂来统一生产，强调的是一系列相关的产品对象！！！



![image-20220130210200889](https://gitee.com/JKcoding/imgs/raw/master/img/202201302102868.png)

**实现步骤**

1、定义两个接口`Pay`、 `Refund`

2、创建具体的`Pay`产品、创建具体的 `Refund`产品

3、创建抽象工厂 `OrderFactory`接口，里面两个方法 createPay/ createRefund

4、创建支付宝产品族`AliOrderFactory`，实现 `OrderFactory`抽象工厂

5、创建微信支付产品族 `WechatOrderFactory`，实现`OrderFactory`抽象工厂

6、定义一个超级工厂创造器，通过传递参数获取对应的工厂

![image-20220130214041062](https://gitee.com/JKcoding/imgs/raw/master/img/202201302140082.png)

**代码实现**

```java
public interface Pay {
}
```

```java
public interface Refund {
}
```

```java
public interface OrderFactory {
    Pay createPay();
    Refund createRefund();
}
```

```java
public class AliPay implements Pay{
}
```

```java
public class AliRefund implements Refund{
}
```

```java
public class AliOrderFactroy implements OrderFactory{
    @Override
    public Pay createPay() {
        return new AliPay();
    }

    @Override
    public Refund createRefund() {
        return new AliRefund();
    }
}
```

```java
public class WechatPay implements Pay{
}
```

```java
public class WechatRefund implements Refund{
}
```

```java
public class WechatOrderFactory implements OrderFactory{
    @Override
    public Pay createPay() {
        return new WechatPay();
    }

    @Override
    public Refund createRefund() {
        return new WechatRefund();
    }
}
```

```java
public class FactoryProducer {
    
    public static OrderFactory getFactory(String type) {
        if (type.equals("wechat")) {
            return new WechatOrderFactory();
        }
        if (type.equals("ali")) {
            return new AliOrderFactroy();
        }
        return null;
    }
}
```

**工厂方法模式和抽象工厂方法模式**

o 当抽象工厂模式中每一个具体工厂类只创建一个产品对象，抽象工厂模式退化成工厂方法模式

**优点**

o 当一个产品族中的多个对象被设计成一起工作时，它能保证使用方始终只使用同一个产品族中的对象

o 产品等级结构扩展容易，如果需要增加多一个产品等级，只需要增加新的工厂类和产品类即可，比如增加银行支付、退款

**缺点**

o 产品族扩展困难，要增加一个系列的某一产品，既要在抽象的工厂和抽象产品里修改代码，不是很符合开闭原则

σ 增加了系统的抽象性和理解难度



#### 原型模式

●原型设计模式 `Prototype`

- 是一种对象创建型模式，使用原型实例指定创建对象的种类，并且通过拷贝这些原型创建新的对象，主要用于创建重复的对象，同时又能保证性能
- 工作原理是将一个原型对象传给那个要发动创建的对象，这个要发动创建的对象通过请求原型对象拷贝自己来实现创建过程
- 应该是最简单的设计模式了，实现一个接口，重写一个方法即完成了原型模式

●**核心组成**

- `Prototype`:声明克隆方法的接口，是所有具体原型类的公共父类， `Cloneable`接口
- `ConcretePrototype`:具体原型类
- `Client`:让一个原型对象克隆自身从而创建一个新的对象

**应用场景**

- 创建新对象成本较大，新的对象可以通过原型模式对已有对象进行复制来获得
- 如果系统要保存对象的状态，做备份使用

**遗留问题**

- 通过对一个类进行实例化来构造新对象不同的是，原型模式是通过拷贝一个现有对象生成新对象的
- 浅拷贝实现 `Cloneable`，深拷贝是通过实现`Serializable`读取二进制流

**拓展**

**浅拷贝**

如果原型对象的成员变量是基本数据类型（int、double、byte、 boolean、char等），将复制一份给克隆对象，如果原型对象的成员变量是引用类型，则将引用对象的地址复制一份给克隆对象，也就是说原型对象和克隆对象的成员变量指向相同的内存地址

**通过覆盖 `Object`类的`clone()`方法可以实现浅克隆**

**深拷贝**

无论原型对象的成员变量是基本数据类型还是引用类型，都将复制一份给克隆对象，如果需要实现深克隆，可以通过序列化（ `Serializable`）等方式来实现

**原型模式是内存二进制流的拷贝，比new对象性能高很多，使用的时候记得注意是选择浅拷贝还是深拷贝**

**优点**

- 当创建新的对象实例较为复杂时，使用原型模式可以简化对象的创建过程，可以提高新实例的创建效率
- 可辅助实现撤销操作，使用深克隆的方式保存对象的状态，使用原型模式将对象复制一份并将其状态保存起来，以便在需要的时候使用恢复到历史状态

**缺点**

- 需要为每一个类配备一个克隆方法，对已有的类进行改造时，需要修改源代码，违背了“开闭原则"
- 在实现深克隆时需要编写较为复杂的代码，且当对象之间存在多重的嵌套引用时，需要对每一层对象对应的类都必须支持深克隆

```java
public class Person implements Cloneable, Serializable {

    private String name;
    private int age;
    private List<String> list = new ArrayList<>();

    public Person() {
        System.out.println("构造函数调用");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    protected Person clone() throws CloneNotSupportedException {
        return (Person) super.clone();
    }

    /**
     * 深拷贝
     */
    public Object deepClone() {
        try {
            //输出 序列化
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            //输入 反序列化
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new java.io.ObjectInputStream(bais);
            Person copyObject = (Person) ois.readObject();
            return copyObject;
        } catch (Exception e) {
            
        }
        return null;
    }
}
```



#### 建造者模式

- 使用多个简单的对象一步一步构建成一个复杂的对象，将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示
- 允许用户只通过指定复杂对象的类型和内容就可以构建它们，不需要知道内部的具体构建细节

**场景举例**

- `KFC`创建套餐:套餐是一个复杂对象，它一般包含主食如汉堡、烤翅等和饮料如果汁、可乐等组成部分，不同的套餐有不同的组合，而`KFC`的服务员可以根据顾客的要求，一步一步装配这些组成部分，构造一份完整的套餐
- 电脑有低配、高配，组装需要`CPU`、内存、电源、硬盘、主板等

![image-20220201145249525](https://gitee.com/JKcoding/imgs/raw/master/img/202202011452881.png)

**核心组成**

- `Builder`:抽象建造者，定义多个通用方法和构建方法
- `ConcreteBuilder`:具体建造者，可以有多个
- `Director`:指挥者，控制整个组合过程，将需求交给建造者，由建造者去创建对象
- `Product`:产品角色

**编码实践**

```java
public interface Builder {
    void buildCpu();
    void buildMainboard();
    void buildDisk();
    void buildPower();
    void buildMemory();
    Computer createComputer();
}
```

```java
public class Computer {
    private String cpu;
    private String mainboard;
    private String disk;
    private String power;
    private String memory;

	//set、get方法
}
```

```java
public class HighComputerBuilder implements Builder{

    private Computer computer = new Computer();

    @Override
    public void buildCpu() {
        computer.setCpu("高配cpu");
    }

    @Override
    public void buildMainboard() {
        computer.setMainboard("高配mainboard");
    }

    @Override
    public void buildDisk() {
        computer.setDisk("高配disk");
    }

    @Override
    public void buildPower() {
        computer.setPower("高配power");
    }

    @Override
    public void buildMemory() {
        computer.setMemory("高配memory");
    }

    @Override
    public Computer createComputer() {
        return computer;
    }
}
```

```java
public class Director {

    public Computer create(Builder builder) {
        builder.buildCpu();
        builder.buildDisk();
        builder.buildMemory();
        builder.buildMainboard();
        builder.buildPower();
        return builder.createComputer();
    }
}
```

**优点**

- 客户端不必知道产品内部组成的细节，将产品本身与产品的创建过程解耦
- 每一个具体建造者都相对独立，而与其他的具体建造者无关，更加精细地控制产品的创建过程
- 增加新的具体建造者无须修改原有类库的代码，符合开闭原则
- 建造者模式结合链式编程来使用，代码上更加美观

**缺点**

- 建造者模式所创建的产品一般具有较多的共同点，如果产品差异大则不建议使用

**建造者模式与抽象工厂模式的比较**:

​	建造者模式返回一个组装好的完整产品，抽象工厂模式返回一系列相关的产品，这些产品位于不同的产品等级结构，构成了一个产品族



#### 适配器模式

- 见名知意，是作为两个不兼容的接口之间的桥梁，属于结构型模式
- 适配器模式使得原本由于接口不兼容而不能一起工作的那些类可以一起工作
- 常见的几类适配器

​	■**类的适配器模式**

​		想将一个类转换成满足另一个新接口的类时，可以使用类的适配器模式，创建一个新类，继承原有的类，实现新的接口即可

​	■**对象的适配器模式**

​		■想将一个对象转换成满足另一个新接口的对象时，可以创建一个适配器类，持有原类的一个实例，在适配器类的方法中，调用实例的方法就行

​	■**接口的适配器模式**

​		■不想实现一个接口中所有的方法时，可以创建个 Adapter，实现所有方法，在写别的类的时候，继承 Adapter类即



**应用场景**

- 电脑需要读取内存卡的数据，读卡器就是适配器
- 日常使用的转换头，如电源转换头，电压转换头
- 系统需要使用现有的类，而这些类的接口不符合系统的需要
- `JDK`中 `InputStreamReader`就是适配器
- `JDBC`就是我们用的最多的适配器模式

> JDBC给出一个客户端通用的抽象接口，每一个具体数据库厂商如 SQL Server、 Oracle、Mysql等，就会开发JDBC驱动，就是一个介于JDBC接口和数据库引擎接口之间的适配器软件

![image-20220201152219432](https://gitee.com/JKcoding/imgs/raw/master/img/202202011522323.png)

##### **接口适配器**

有些接口中有多个抽象方法，当我们写该接口的实现类时，必须实现该接口的所有方法，这明显有时比较浪费，因为并不是所有的方法都是我们需要的，有时只需要实现部分接口就可以了

##### **生产环境接口-需要兼容新的业务怎么办？**

**需求背景**：

某课堂里面有个电商支付项目，里面有个登录功能，已经线上运行了，客户端A调用生产环境的登录接口B，且线上稳定运行了好几年。某天，公司接到收购了别的公司的项目，需要把这套系统部署在起来，且收购的项目也有对应的客户端C，但是两个客户端和服务端的协议不一样

**需求:收购的项目客户端c，需要做公司原来的项目用户数据打通，连接公司的服务端登录接口B，你能想到几个解决方案？**

1. 修改就项目B的登录接口，兼容C客户端协议（可能影响线上接口，不稳定
2. 新增全新的登录接口F，采用C客户端协议（和旧的业务逻辑会重复
3. 新增一个转换协议接口，客户端C调用旧的B接口之前，使用转换接口转换下协议（适配器模式，推荐这个方式）

![image-20220201153020424](https://gitee.com/JKcoding/imgs/raw/master/img/202202011530726.png)

**总结**

- 在使用一些旧系统或者是类库时，经常会出现接口不兼容的问题，适配器模式在解决这类问题具有优势
- 学习设计模式一定不要局限代码层面，要从软件系统整体去考虑而不是为了使用设计模式，而去使用设计模式

**优点**

- 可以让任何两个没有关联的类一起运行，使得原本由于接口不兼容而不能一起工作的那些类可以一起工作
- 增加灵活度，提高复用性，适配器类可以在多个系统使用，符合开闭原则

**缺点**

- 整体类的调用链路增加，本来A可以直接调用C，使用适配器后是A调用B，B再调用C



##### **类的适配器**

想将一个类转换成满足另一个新接口的类时，可以使用类的适配器模式，创建一个新类，继承原有的类，实现新的接口即可

![image-20220201153628770](https://gitee.com/JKcoding/imgs/raw/master/img/202202011536923.png)

```java
public class OldModule {
    public void methodA() {
        
    }
}
```

```java
public interface TargetModule {
    void methodA();
    void methodB();
}
```

```java
public class Adapter extends OldModule implements TargetModule{
    @Override
    public void methodB() {

    }
}
```



#### 桥接设计模式

- 适配器模式类似，包括以后经常会遇到意思接近一样的设计模式，因为大神往往就是多个模式混用，且根据不同的场景进行搭配，桥接设计模式也是结构型模式
- 将抽象部分与实现部分分离，使它们都可以独立的变化
- 通俗来说，是通过组合来桥接其它的行为/维度



**应用场景**

- 系统需要在构件的抽象化角色和具体化角色之间增加更多的灵活性
- 不想使用继承导致系统类的个数急剧增加的系统
- 有时候一个类，可能会拥有多个变化维度，比如啤酒，有不同的容量和品牌，可以使用继承组合的方式进行开发，假如维度很多，就容易岀现类的膨胀，使用桥接模式就可以解决这个问题，且解耦

**业务场景**

我们需要构建一个手机类，我们知道手机有很多品牌，苹果、华为等，从另外一个颜色维度，又有多种颜色，红、黄、蓝等，那如果描述这些类的话，传统方式就直接通过继承，就需要特别多的类，品牌2，颜色3，就是6个类了，如果后续再增加品牌就更多了，类数目将会激增，即所谓的类爆炸，使用桥接模式就可以解决这个问题，且灵活度大大提高

![image-20220203021400295](https://gitee.com/JKcoding/imgs/raw/master/img/202202030214631.png)

![image-20220203021425136](https://gitee.com/JKcoding/imgs/raw/master/img/202202030214055.png)

​	**编码实战**

```java
public interface Color {
    void useColor();
}
```

```java
public class BlueColor implements Color{
    @Override
    public void useColor() {
        System.out.println("蓝色");
    }
}
```

```java
public class RedColor implements Color{
    @Override
    public void useColor() {
        System.out.println("红色");
    }
}
```

```java
public abstract class Phone {

    protected Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    abstract public void run();
}
```

```java
public class HWPhone extends Phone{

    public HWPhone(Color color) {
        super.setColor(color);
    }

    @Override
    public void run() {
        color.useColor();
        System.out.println("华为手机");
    }
}
```

```java
public class ApplePhone extends Phone{

    public ApplePhone(Color color) {
        super.setColor(color);
    }

    @Override
    public void run() {
        color.useColor();
        System.out.println("苹果手机");
    }
}
```

```java
public static void main(String[] args) {
    HWPhone blueHwPhone = new HWPhone(new BlueColor());
    blueHwPhone.run();
    HWPhone redHwPhone = new HWPhone(new RedColor());
    redHwPhone.run();
    ApplePhone blueApplePhone = new ApplePhone(new BlueColor());
    blueApplePhone.run();
    ApplePhone redApplePhone = new ApplePhone(new RedColor());
    redApplePhone.run();
}
```

![image-20220203024234927](https://gitee.com/JKcoding/imgs/raw/master/img/202202030242840.png)

**优点**

- 抽象和实现的分离。
- 优秀的扩展能力，符合开闭原则

**缺点**

- 增加系统的理解与设计难度
- 使用聚合关联关系建立在抽象层，要求开发者针对抽象进行设计与编程，比如抽象类汽车，里面聚合了颜色类，有点像对象适配器

**总结和对比**

1、按`GOF`的说法，桥接模式和适配器模式用于设计的不同阶段，

- ​	桥接模式用于设计的前期，精细化的设计，让系统更加灵活
- ​	适配器模式用于设计完成之后，发现类、接口之间无法一起工作，需要进行填坑

2、适配器模式经常用在第三方AP协同工作的场合，在功能集成需求越来越多的今天，这种模式的使用频度越来越高，包括有些同学听过外观设计模式，这个也是某些场景和适配器模式一样



#### 组合设计模式

- 又叫部分整体模式，将对象组合成树形结构以表示“部分-整体“的层次结构，可以更好的实现管理操作
- 组合模式使得用户可以使用一致的方法操作单个对象和组合对象
- 部分-整体对象的基本操作多数是一样的，但是应该还会有不一样的地方
- 核心:组合模式可以使用一棵树来表示

**应用场景**

- 银行总行，总行有前台、后勤、网络部门等，辖区下还有地方分行，也有前台、后勤、网络部门，最小的分行就没有子分行了
- 公司也是，总公司下有子公司，每个公司大部分的部门都类似文件夹和文件，都有增加、删除等api，也有层级管理关系
- 当想表达对象的部分-整体的层次结构
- 当我们的要处理的对象可以生成一颗树形结构，我们要对树上的节点和叶子进行操作时，它能够提供一致的方式，而不用考虑它是节点还是叶子

**角色**

- 组合部件（ Component）:它是一个抽象接口，表示树根，例子:总行
- 合成部件（ Composite）:和组合部件类似，也有自己的子节点，例子:总行下的分行
- 叶子（Leaf）:在组合中表示子节点对象，注意是没有子节点，例子:最小地方的分行

 **编码实战**

```java
public abstract class Root {

    private String name;

    public Root(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void addFile(Root root);
    public abstract void removeFile(Root root);
    public abstract void display(int depth);
}
```

```java
public class Folder extends Root{

    private List<Root> folders = new ArrayList<>();

    public List<Root> getFolders() {
        return folders;
    }

    public void setFolders(List<Root> folders) {
        this.folders = folders;
    }

    public Folder(String name) {
        super(name);
    }

    @Override
    public void addFile(Root root) {
        folders.add(root);
    }

    @Override
    public void removeFile(Root root) {
        folders.remove(root);
    }

    @Override
    public void display(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("-");
        }
        System.out.println(sb.toString() + this.getName());
        for (Root r : folders) {
            r.display(depth + 2);
        }
    }
}
```

```java 
public class File extends Root{

    public File(String name) {
        super(name);
    }

    @Override
    public void addFile(Root root) {

    }

    @Override
    public void removeFile(Root root) {

    }

    @Override
    public void display(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("-");
        }
        System.out.println(sb + this.getName());
    }
}
```

```java
public static void main(String[] args) {
    Root root = new Folder("c://");
    Root desktop = new Folder("桌面");
    Root myComputer = new Folder("我的电脑");
    Root javaFile = new File("HelloWorld.java");
    root.addFile(desktop);
    root.addFile(myComputer);
    myComputer.addFile(javaFile);
    root.display(0);
}
```

**缺点**

- 客户端需要花更多时间理清类之间的层次关系

**优点**

- 客户端只需要面对一致的对象而不用考虑整体部分或者节点叶子的问题
- 方便创建出复杂的层次结构



#### 装饰器设计模式

- 也叫包装设计模式，属于结构型模式，它是作为现有的类的一个包装，允许向一个现有的对象添加新的功能，同时又不改变其结构
- 给对象增加功能，一般两种方式继承或关联组合，将一个类的对象嵌入另一个对象中，由另一个对象来决定是否调用嵌入对象的行为来增强功能，这个就是装饰器模式，比继承模式更加灵活

**应用场景**

- 老王，本来计划买跑车撩妹的，结果口袋没钱，改买自行车，为了显得突出，店家提供多种改装方案，加个大的喇叭、加个防爆胎等，经过装饰之后成为目的更明确的自行车，更能解决问题。像这种不断为对象添加装饰的模式就叫 `Decorator`模式， `Decorator`指的是装饰物
- 以动态、透明的方式给单个对象添加职责，但又能不改变其结构
- `JDK`源码里面应用的最多的就是`IO`流，大量使用装饰设计模式

![image-20220206163941230](https://gitee.com/JKcoding/imgs/raw/master/img/202202061639402.png)

角色（装饰者和被装饰者有相同的超类（ `Component`）

​	抽象组件（ `Component`）

​		定义装饰方法的规范，最初的自行车，仅仅定义了自行车的AP

​	被装饰者（ `ConcreteComponent`）

​		■`Component`的具体实现，也就是我们要装饰的具体对象

​		■实现了核心角色的具体自行车

​	装饰者组件（ `Decorator`）

​		■定义具体装饰者的行为规范，和 `Component`角色有相同的接口，持有组件（ `component`）对象的实例引用

​		■自行车组件都有名称和价格

​	具体装饰物（ `ConcreteDecorator`）

​		■负责给构件对象装饰附加的功能

​		■比如喇叭，防爆胎

**编码实战**

老王，由于公司发了项目奖金，不够买跑车，就先买自行车，店家里面有小号、中号、大号等规格的自行车。然后改造加一个喇叭，后来不够又要加多一个，一个防爆胎不够，又有两个，存在很多个随机组合的改装。店家就苦恼了，这样的结构难搞，价格也难算，而且需求再变动，就更麻烦了。使用装饰者就可以解决这个问题

```java
public interface Bike {
    String getDescription();
    int getPrice();
}
```

```java
public class SmallBike implements Bike{
    private String description = "小号自行车";
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public int getPrice() {
        return 100;
    }
}
```

```java
public class BigBike implements Bike{
    private String description = "大号自行车";
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public int getPrice() {
        return 200;
    }
}
```

```java
public class BikeDecorator implements Bike{
    private String description = "我只是装饰器";
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public int getPrice() {
        return 0;
    }
}
```

```java
public class RSCBikeDecorator extends BikeDecorator{

    private String description = "增加一个防爆胎";

    private Bike bike;

    public RSCBikeDecorator(Bike bike) {
        this.bike = bike;
    }

    @Override
    public String getDescription() {
        return bike.getDescription() + ", " + this.description;
    }

    @Override
    public int getPrice() {
        return bike.getPrice() +  100;
    }

    @Override
    public String toString() {
        return "RSCBikeDecorator{" +
                "description='" + this.getDescription() + '\'' +
                ", price=" + this.getPrice() +
                '}';
    }
}
```

```java
public class SuonaBikeDecorator extends BikeDecorator{

    private String description = "增加一个唢呐";

    private Bike bike;

    public SuonaBikeDecorator(Bike bike) {
        this.bike = bike;
    }

    @Override
    public String getDescription() {
        return bike.getDescription() + ", " + this.description;
    }

    @Override
    public int getPrice() {
        return bike.getPrice() +  50;
    }

    @Override
    public String toString() {
        return "SuonaBikeDecorator{" +
                "description='" + this.getDescription() + '\'' +
                ", price=" + this.getPrice() +
                '}';
    }
}
```

```java
public static void main(String[] args) {
    Bike bike = new BigBike();
    bike = new RSCBikeDecorator(bike);
    System.out.println(bike);
    bike = new  SuonaBikeDecorator(bike);
    System.out.println(bike);
    bike = new RSCBikeDecorator(bike);
    System.out.println(bike);
}
```

**优点**

- 裝饰模式与继承关系的目的都是要扩展对象的功能，但装饰模式可以提供比继承更多的灵活性。
- 使用不同的具体装饰类以及这些装饰类的排列组合，可以创造出很多不同行为的组合，原有代码无须改变，符合“开闭原则

**缺点**

- 装饰模式增加了许多子类，如果过度使用会使程序变得很复杂（多层包装
- 增加系统的复杂度，加大学习与理解的难度

**装饰器模式和桥接模式对比**

- 相同点都是通过封装其他对象达到设计的目的，和对象适配器也类似，有时也叫半装饰设计模式，没有装饰者和被装饰者的主次区别，桥接和被桥接者是平等的，桥接可以互换，不用继承自同一个父类，比如例子里面的，可以是 Phone持有 Color，也可以是Color持有 Phone
- 桥接模式不用使用同一个接口;装饰模式用同一个接口装饰，接口在父类中定义

**`JDK`源码里面的 `Stream IO`流-装饰器设计模式应用**

抽象组件（ `Component`）: `Inputstream`

​	定义装饰方法的规范

被装饰者（ `ConcreteComponent`）: `FileInputstream`、`ByteArrayInputstream`

​	`Component`的具体实现，也就是我们要装饰的具体对象

装饰者组件（ `Decorator`）: `FilterInputstream`

​	定义具体装饰者的行为规范，和 `Component`角色有相同的接口，持有组件`Component`对象的实例引用

​	自行车组件都有名称和价格

具体装饰物（`ConcreteDecorator`）: `BufferedInputStream`、`DataInputstream`

```java
InputStream inputStream = new BufferedInputStream(new FileInputStream(""));
```



#### 代理设计模式

- 为其他对象提供一种代理以控制对这个对象的访问，属于结构型模式
- 客户端并不直接调用实际的对象，而是通过调用代理来间接的调用实际的对象

**应用场景**

- 各大数码专营店，代理厂商进行销售对应的产品，代理商持有真正的授权代理书
- 客户端不想直接访问实际的对象，或者访问实际的对象存在困难，通过一个代理对象来完成间接的访问
- 想在访问一个类时做一些控制，或者增强功能

角色

- `Subject`: 抽象接口，真实对象和代理对象都要实现的一个抽象接口，好比销售数码产品
- `Proxy`: 包含了对真实对象的引用，从而可以随意的操作真实对象的方法，好比代理加盟店
-  `RealProject`: 真实对象，好比厂商销售数码产品

**编码实战**

> 老王想开个数码小卖部，为以后退休生活做准备代理各大厂商的手机和电脑，用代理设计模式帮他实现下
>
> Subject卖手机
>
> RealProject苹果、华为厂商，核心是卖手机，但是选址不熟悉
>
> Proxy老王数码专卖店:代理卖手机，附加选地址，增加广告等

```java
public interface DigitalSell {
    void sell();
}
```

```java
public class DigitalSellReal implements DigitalSell{
    @Override
    public void sell() {
        System.out.println("销售华为手机");
    }
}
```

```java
public class DigitalSellProxy implements DigitalSell{

    private DigitalSell realObj = new DigitalSellReal();

    @Override
    public void sell() {
        makeAddress();
        realObj.sell();
        makeAD();
    }
    private void makeAddress() {
        System.out.println("一个人流量很高的地址");
    }
    private void makeAD() {
        System.out.println("投放广告");
    }
}
```

```java
public static void main(String[] args) {
    DigitalSell realObj = new DigitalSellReal();
    realObj.sell();
    DigitalSell proxy = new DigitalSellProxy();
    proxy.sell();
}
```

**优点**

- 可以在访问一个类时做一些控制，或增加功能
- 操作代理类无须修改原本的源代码，符合开閉原则，系统具有较好的灵活性和可扩展性

**缺点**

- 增加系统复杂性和调用链路

**有静态代理和动态代理两种**

​	o动态代理也有多种方式，cgib、jdk，可以看看Springboot的 spring5模块

**和装饰器模式的区别**

​	o代理模式主要是两个功能

​		1、保护目标对象

​		2、增强目标对象，和装饰模式类似了



#### 外观设计模式

- 门面模式，隐藏系统的复杂性，并向客户端提供了一个客户端可以访问系统的接口
- 定义了一个高层接口，这个接口使得这系统更加容易使用

**应用场景**

- 在外人看来，老王是负责消息推送这个工作，看起来很轻松，但他们不知道里面有多复杂，老王加班多久才输出一个统一的接口，只要轻松操作就可以完成复杂的事情
- 开发里面`MVC`三层架构，在数据访问层和业务逻辑层、业务逻辑层和表示层的层与层之间使用 `interface`接口进行交互，不用担心内部逻辑，降低耦合性
- 各种第三方`SDK`大多会使用外观模式，通过一个外观类，也就是整个系统的接口只有一个统一的高层接口，这对用户屏蔽很多实现细节，外观模式经常用在封裝AP的常用手段
- 对于复杂难以维护的老系统进行拓展，可以使用外观设计模式
- 需要对一个复杂的模块或子系统提供一个外界访问的接口，外界对子系统的访问只要黑盒操作

**角色**

- 外观角色`(Facade)`:客户端可以调用这个角色的方法，这个外观方法知道多个子系统的功能和实际调用
- 子系统角色`(Sub System)`:每个子系统都可以被客户端直接调用，子系统并不知道门面的存在

![image-20220208015851518](https://gitee.com/JKcoding/imgs/raw/master/img/202202080158577.png)

**业务需求**

> 在外人看来，老王是负责消息推送这个工作，看起来很轻松，但他们不知道里面有多复杂
>
> 需要对接微信消息、邮件消息、钉钉消息等，老王加班长期加班没有女友，才输出一个统一的接口，只要轻松操作就可以完成复杂的事情
>
> 用外观设计模式帮老王完成这个需求

**编码实战**

```java
public interface ImessageManager {
    void pushMessage();
}
```

```java
public class DingDingMessageManager implements ImessageManager{
    @Override
    public void pushMessage() {
        System.out.println("推送钉钉消息");
    }
}
```

```java
public class SmsMessageManager implements ImessageManager{
    @Override
    public void pushMessage() {
        System.out.println("推送短信消息");
    }
}
```

```java
public class WechatMessageManager implements ImessageManager{
    @Override
    public void pushMessage() {
        System.out.println("推送微信消息");
    }
}
```

```java
public class MailMessageManager implements ImessageManager {
    @Override
    public void pushMessage() {
        System.out.println("推送邮件信息");
    }
}
```

```java
public class MessageFacade implements ImessageManager{

    private ImessageManager wechatMsgManager = new WechatMessageManager();
    private ImessageManager smsMessageManager = new SmsMessageManager();
    private ImessageManager mailMessageManager = new MailMessageManager();
    private ImessageManager dingDingMessageManager = new DingDingMessageManager();


    @Override
    public void pushMessage() {
        wechatMsgManager.pushMessage();
        smsMessageManager.pushMessage();
        mailMessageManager.pushMessage();
        dingDingMessageManager.pushMessage();
    }
}
```

```java
public static void main(String[] args) {
    ImessageManager imessageManager = new MessageFacade();
    imessageManager.pushMessage();
}
```

**优点**

- 减少了系统的相互依赖，提高了灵活性
- 符合依赖倒转原则，针对接口编程，依赖于抽象而不依赖于具体
- 符合迪米特法则，最少知道原则，一个实体应当尽量少地与其他实体之间发生相互作用

**缺点**

- 增加了系统的类和链路
- 不是很符合开闭原则，如果增加了新的逻辑，需要修改`facade`外观类



#### 享元设计模式

- 属于结构型模式，主要用于减少创建对象的数量，以减少内存占用和提高性能，它提供了减少对象数量从而改善应用所需的对象结构的方式
- 享元模式尝试重用现有的同类对象，如果未找到匹配的对象，则创建新对象

**应用场景**

- `JAVA`中的 `String`，如果字符串常量池里有则返回，如果没有则创建一个字符串保存在字符串常量池里面
- 数据库连接池、线程池等
- 如果系统有大量相似对象，或者需要用需要缓冲池的时候可以使用享元设计模式，也就是大家说的池化技术
- 如果发现某个对象的生成了大量细粒度的实例，并且这些实例除了几个参数外基本是相同的，如果把那些共享参数移到类外面，在方法调用时将他们传递进来，就可以通过共享对象，减少实例的个数

**内部状态**

​	不会随环境的改变而有所不同，是可以共享的

**外部状态**

​	不可以共享的，它随环境的改变而改变的，因此外部状态是由客户端来保持（因为环境的变化一般是由客户端引起的）

**角色**

- 抽象享元角色: 为具体享元角色规定了必须实现的方法，而外部状态就是以参数的形式通过此方法传入
- 具体享元角色: 实现抽象角色规定的方法。如果存在內部状态，就负责为内部状态提供存储空间
- 享元工厂角色: 负责创建和管理享元角色。要想达到共享的目的，这个角色的实现是关键
- 客户端角色: 维护对所有享元对象的引用，而且还需要存储对应的外部状态

![image-20220208200519613](https://gitee.com/JKcoding/imgs/raw/master/img/202202082005886.png)

**需求**：

> 老王为了增加收入，开始接了外包项目，开发了一个AI网站模板，可以根据不同的客户需求自动生成不同类型的网站电商类、企业产品展示、信息流等；在部署的时候就麻烦了，是不是每个机器都用租用云服务器，购买独立域名呢，这些网站结构相似度很高，而且都不是高访问量网站，可以先公用服务器资源，減少服务器资源成本，类似虚拟机或者 Docker



**编码实战**

```java
public class Company {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company(String name) {
        this.name = name;
    }

    public Company() {
    }
}
```

```java
public abstract class CloudWebSite {

    public abstract void run(Company company);
}
```

```java
public class ConcreteWebSite extends CloudWebSite{

    private String category;

    public ConcreteWebSite(String category) {
        this.category = category;
    }

    @Override
    public void run(Company company) {
        System.out.println("网站分类: " + category + ", 公司: " + company.getName());
    }
}
```

```java
public class WebSiteFactory {

    private Map<String, ConcreteWebSite> map = new HashMap<>();

    public CloudWebSite getWebSiteByCategory(String category) {
        if (map.containsKey(category)) {
            return map.get(category);
        } else {
            ConcreteWebSite site = new ConcreteWebSite(category);
            map.put(category, site);
            return site;
        }
    }

    public int getWebSiteCategorySite() {
        return map.size();
    }
}
```

**优点**

- 大大減少了对象的创建，降低了程序内存的占用，提高效率

**缺点**

- 提高了系统的复杂度，需要分离出内部状态和外部状态

**注意划分内部状态和外部状态，否则可能会引起线程安全问题，必须有一个工厂类加以控制**

**享元设计模式和原型、单例模式的区别**

- 原型设计模式是指定创建对象的种类，然后通过拷贝这些原型来创建新的对象
- 单例设计模式保证一个类仅有一个实例



#### 策略模式

- 定义一系列的算法把它们一个个封装起来，并且使它们可相互替换
- 淘宝天猫双十一，正在搞活动有打折的、有满减的、有返利的等等，这些算法只是一种策略，并且是随时都可能互相替换的，我们就可以定义一组算法，将每个算法都封装起来，并且使它们之间可以互换

**应用场景**

- 老王计划外出旅游，选择骑自行车、坐汽车、飞机等，每一种旅行方式都是一个策略
-  `Java AWT`中的 `LayoutManager`，即布局管理器
- 如果在一个系统里面有许多类，它们之间的区别仅在于它们的行为，那么可以使用策略模式
- 不希望暴露复杂的、与算法有关的数据结构，那么可以使用策略模式来封装算法

**角色**

-  `Context`上下文:屏蔽高层模块对策略、算法的直接访可，封装可能存在的变化
-  `Strategy`策略角色∶抽象策略角色，是对策略、算法家族的抽象，定义每个策略或算法必须具有的方法和属性
- `ConcreteStrategy`具体策略角色:用于实现抽象策略中的操作，即实现具体的算法

![image-20220208215302434](https://gitee.com/JKcoding/imgs/raw/master/img/202202082153526.png)

**业务需求**

> 老王面试进了大厂，是电商项目的营销活动组，负责多个营销活动，有折扣、优惠券抵扣、满减等，项目上线后，产品经理找茬，经常新增营销活动，导致代码改动多，加班严重搞的老王很恼火。
>
> 他发现这些都是活动策略，商品的价格是根据不同的活动策略进行计算的，因此用策略设计模式进行了优化，后续新增策略后只要简单配置就行了，不用大动干戈

**编码实战**

```java
public class ProductOrder {
    private double oldPrice;
    private int userId;
    private int productId;

    public ProductOrder(double oldPrice, int userId, int productId) {
        this.oldPrice = oldPrice;
        this.userId = userId;
        this.productId = productId;
    }
//set get省略
}
```

```java
public abstract class Strategy {
    public abstract double computePrice(ProductOrder productOrder);
}
```

```java
public class NormalActivity extends Strategy{
    @Override
    public double computePrice(ProductOrder productOrder) {
        return productOrder.getOldPrice();
    }
}
```

```java
public class DiscountActivity extends Strategy{

    private double rate;

    public DiscountActivity(double rate) {
        this.rate = rate;
    }

    @Override
    public double computePrice(ProductOrder productOrder) {
        return productOrder.getOldPrice() * rate;
    }
}
```

```java
public class VoucherActivity extends Strategy{

    private double voucher;

    public VoucherActivity(double voucher) {
        this.voucher = voucher;
    }

    @Override
    public double computePrice(ProductOrder productOrder) {
        if (productOrder.getOldPrice() > voucher) {
            return productOrder.getOldPrice() - voucher;
        }
        return 0;
    }
}
```

```java
public class PromotionContext {
    private Strategy strategy;

    public PromotionContext(Strategy strategy) {
        this.strategy = strategy;
    }

    public double executeStrategy(ProductOrder productOrder) {
        return strategy.computePrice(productOrder);
    }

}
```

```java
public static void main(String[] args) {
    ProductOrder productOrder = new ProductOrder(800,1,32);
    //折扣
    PromotionContext context = new PromotionContext(new DiscountActivity(0.5));
    double price = context.executeStrategy(productOrder);
    System.out.println(price);
    //没活动
    context = new PromotionContext(new NormalActivity());
    price = context.executeStrategy(productOrder);
    System.out.println(price);
    //优惠券
    context = new PromotionContext(new VoucherActivity(100));
    price = context.executeStrategy(productOrder);
    System.out.println(price);
}
```

**优点**

- 满足开闭原则，当增加新的具体策略时，不需要修改上下文类的代码，上下文就可以引用新的具体策略的实例
- 避免使用多重条件判断，如果不用策略模式可能会使用多重条件语句不利于维护，和工厂模式的搭配使用可以很好地消除代码if-else的多层嵌套（工厂模式主要是根据参数，获取不同的策略）

**缺点**

- 策略类数量会增多，每个策略都是一个类，复用的可能性很小
- 对外暴露了类所有的行为和算法，行为过多导致策略类膨胀

