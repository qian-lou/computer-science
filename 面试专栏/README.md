## 一、Java基础

#### 1、8种基本数据类型

#### 2、装箱和拆箱

#### 3、String转int，能转？，如何转？

#### 4、short s1 = 1; s1 = s2 + 1; 有什么错？

#### 5、int和Integer区别

#### 6、字节和字符区别

#### 7、基本类型和引用类型的区别

#### 8、重写重载封装继承多态

#### 9、队列Queue和栈Stack

#### 10、面向对象

#### 11、String、StringBuffer、StringBuilder

#### 12、HashCode和equal

#### 13、String中的hashcode以及toString

#### 14、java文件读取

#### 15、java反射

#### 16、JDK JRE JNI

#### 17、static和final的区别

#### 18、map list set 区别

#### 19、session 和 cookie

#### 20、IO NIO BIO AIO select epoll

#### 21、NIO的原理

#### 22、ThreadLocal

#### 23、finalize finalization finally

#### 24、public private default protected

#### 25、Object

#### 26、equal 和 ==的区别

#### 27、异常

#### 28、序列化

#### 29、comparable接口和comparator接口

#### 30、接口和抽象类

#### 31、Socket

#### 32、Runtime类

#### 33、值传递和引用传递

#### 34、泛型 ？ 与 T的区别

#### 35、枚举类型字节码分析

#### 36、注解

#### 37、字节流和字符流的区别

#### 38、静态内部类 匿名类

## 二、集合类

## 三、锁

## 四、多线程

## 五、框架

## 六、内存模型和垃圾回收

## 七、`JUC`包

## 八、设计模式

#### 1、设计模式

#### 2、常见的设计模式以及案例

#### 3、适配器模式

#### 4、迭代器模式

#### 5、代理模式

#### 6、观察者模式

#### 7、装饰器模式

#### 8、工厂模式

#### 9、建造者模式

#### 10、命令模式

#### 11、责任链模式

#### 12、享元模式

#### 13、中介者模式

#### 14、备忘录模式

#### 15、组合模式

#### 16、模板方法模式

#### 17、单例模式

​	1.非线程安全懒汉模式

​	2、线程安全懒汉模式

​	3.饿汉模式

​	4.静态类内部加载

​	5双重锁校验模式

​	7.懒汉模式与饿汉模式区别

​	8.双重校验锁方法与线程安全的懒汉模式区别.

#### 18、设计模式六大原则

#### 19、Java动态代理

## 九、算法

#### 1、排序

冒泡排序

**快速排序**

```java
public static void quickSort(int[] arr, int left, int right) {
    if (left >= right) return;
    int x = arr[left], i = left - 1, j = right + 1;
    while (i < j) {
        do i++; while (arr[i] < x);
        do j--; while (arr[j] > x);
        if (i < j) swap(arr, i, j);
    }
    quickSort(arr, left, j);
    quickSort(arr, j + 1, right);
}

private static void swap(int[] arr, int i, int j) {
    int t = arr[i];
    arr[i] = arr[j];
    arr[j] = t;
}
```

**归并排序**

```java
public static void mergeSort(int[] arr, int left, int right) {
    if (left >= right) return;
    int mid = left + right >> 1;
    mergeSort(arr, left, mid);
    mergeSort(arr, mid + 1, right);
    int len = right - left + 1;
    int[] temp = new int[len];
    int i = left, j = mid + 1, k = 0;
    while (i <= mid && j <= right) {
        if (arr[i] < arr[j]) {
            temp[k++] = arr[i++];
        } else {
            temp[k++] = arr[j++];
        }
    }
    while (i <= mid) {
        temp[k++] = arr[i++];
    }
    while (j <= right) {
        temp[k++] = arr[j++];
    }
    for (i = 0, j = left; j <= right; i++, j++) {
        arr[j] = temp[i];
    }
}
```



#### 2、栈和队列

#### 3、链表

#### 4、二叉树



## 十、数据库

## 十一、网络

## 十二、操作系统

## 十三、`Linux`命令

## 十四、安全加密

## 十五、代码

## 十六、面经

## 十七、项目

## 十八、`Git`

## 十九、其他