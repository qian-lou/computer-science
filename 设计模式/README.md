

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

