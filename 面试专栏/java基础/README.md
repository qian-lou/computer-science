#### 8种基本数据类型

| 序号 | 数据类型        | 位数 | 默认值 | 取值范围       | 举例说明          |
| ---- | --------------- | ---- | ------ | -------------- | ----------------- |
| 1    | byte(位)        | 8    | 0      | -2^7 - 2^7-1   | byte b = 10;      |
| 2    | short(短整数)   | 16   | 0      | -2^15 - 2^15-1 | short s = 10;     |
| 3    | int(整数)       | 32   | 0      | -2^31 - 2^31-1 | int i = 10;       |
| 4    | long(长整数)    | 64   | 0      | -2^63 - 2^63-1 | long l = 10l;     |
| 5    | float(单精度)   | 32   | 0.0    | -2^31 - 2^31-1 | float f = 10.0f;  |
| 6    | double(双精度)  | 64   | 0.0    | -2^63 - 2^63-1 | double d = 10.0d; |
| 7    | char(字符)      | 16   | 空     | 0 - 2^16-1     | char c = 'c';     |
| 8    | boolean(布尔值) | 8    | false  | true、false    | boolean b = true; |



#### 装箱和插箱

自动装箱是Java编译器在基本数据类型和对应的对象包装类型之间做的一个转化。比如，把int转化成Integer，double转化成Double，等等。反之就是自动拆箱。

原始类型：boolean char byte short int long float double

封装类型：Boolean Character Byte Short Integer Long Float Double



#### String转int型，判断能不能转，如何转

可以转，得处理异常`Integer.parseInt(s)` 主要为`NumberFormatException`

1)当输入字母，如`abcd`

2)当输入为空

3)当输入超过`int`上限时 `Long.parseLong("123")` 转为`long`



#### short s1 = 1; s1 = s1 + 1; 有什么错

在s1 + 1运算时会自动提升表达式的类型为int，那么将int赋予short类型的变量s1会出现类型转换错误



#### short s1 = 1; s1 += 1;有什么错

+=是java语言规定的运算符，java编译器会对它进行特殊处理，因此可以正确编译



#### int和Integer区别

- 1、Integer是int的包装类，int则是java的一种基本数据类型
- 2、Integer变量必须实例化后才能使用，而int变量不需要
- 3、Integer实际是对象的引用，当new一个Integer时，实际上是生成一个指针指向此对象；而int则是直接存储数据值
- 4、Integer的默认值是null，int的默认值是0

1、由于Integer变量实际上是对一个Integer对象的引用，所以两个通过new生成的Integer变量永远是不相等的（因为new生成的是两个对象，其内存地址不同）。

```java
Integer i = new Integer(100);
Integer j = new Integer(100);
System.out.print(i == j); //false
```

2、Integer变量和int变量比较时，只要两个变量的值是向等的，则结果为true（因为包装类Integer和基本数据类型int比较时，java会自动拆包装为int，然后进行比较，实际上就变为两个int变量的比较）

```java
Integer i = new Integer(100);
int j = 100；
System.out.print(i == j); //true
```

3、非new生成的Integer变量和new Integer()生成的变量比较时，结果为false。（因为非new生成的Integer变量指向的是java常量池中的对象，而new Integer()生成的变量指向堆中新建的对象，两者在内存中的地址不同）

```java
Integer i = new Integer(100);
Integer j = 100;
System.out.print(i == j); //false
```

4、对于两个非new生成的Integer对象，进行比较时，如果两个变量的值在区间-128到127之间，则比较结果为true，如果两个变量的值不在此区间，则比较结果为false

```java
Integer i = 100;
Integer j = 100;
System.out.print(i == j); //true

Integer i = 128;
Integer j = 128;
System.out.print(i == j); //false
```

对于第4条的原因：
java在编译Integer i = 100;时，会翻译成为Integer i = Integer.valueOf(100)；，而java API中对Integer类型的valueOf的定义如下：

```java
public static Integer valueOf(int i){
    assert IntegerCache.high >= 127;
    if (i >= IntegerCache.low && i <= IntegerCache.high){
        return IntegerCache.cache[i + (-IntegerCache.low)];
    }
    return new Integer(i);
}
```

java对于-128到127之间的数，会进行缓存，Integer i = 127时，会将127进行缓存，下次再写Integer j = 127时，就会直接从缓存中取，就不会new了

参考https://zhuanlan.zhihu.com/p/53375745