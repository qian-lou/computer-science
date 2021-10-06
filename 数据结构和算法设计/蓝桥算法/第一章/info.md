## 1.如何找数组中唯一成对的那个数

> 1~1000这1000个数放在含有1001个元素的数组中，只有唯一的一个元素值重复，其它均只出现一次。每个数组元素只能访问1次，设计一个算法，将它找出来;不用辅助存储空间，能否设计一个算法实现？
>
> 思路：1 2 3 4 5 5 6 7 8 9  =>   (1^ 2^ 3^ 4^ 5 ^5^ 6^ 7^ 8^ 9) ^ (1^ 2^ 3^ 4^ 5^ 6^ 7^ 8^ 9)  => 5
>
> 利用了 A ^ A = 0; B ^ 0 = B这个性质。
>
> 代码实现：

```java
public int p1() {
        int[] nums = {1, 2, 3, 4, 5, 5, 6, 7, 8, 9};
        int len = nums.length;
        int res = 0;
        for (int i = 1; i <= len - 1; i++) {
            res ^= i;
        }
        for (int i = 0; i < len; i++) {
            res ^= nums[i];
        }
        return res;
}
```

## 2.找出落单的数，这样编程就对了

> 一个数组里除了某一个数字之外，其他的数字都出现了两次。请写程序找出这个只出现一次的数字。
>
> 思路：利用了 A ^ A = 0; B ^ 0 = B这个性质。

代码实现：

```java
public int p2() {
        int[] nums = {1,1,2,2,3};
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            res ^= nums[i];
        }
        return res;
    }
```

## 3.一题三解：二进制中1的个数

> 请实现一个函数，输入一个整数，输出该数二进制表示中1的个数。例:9的二进制表示为1001，有2位是1

思路：

1）将这个数每次右移i位，然后跟1与，总共右移32次，累计1的个数

```java
public int p3() {
    int n = 8;
    int c = 0;
    for (int i = 0; i < 32; i++) {
        if (((n >> i) & 1) == 1) c++;
    }
    return c;
}
```

2）1左移i位，然后跟这个数与，总共右移32次，累计1的个数

```java
public int p3() {
    int n = 7;
    int c = 0;
    for (int i = 0; i < 32; i++) {
        if ((n & (1 << i)) == (1 << i)) c++;
    }
    return c;
}
```

3）原数和原数- 1进行与运算，然后将结果更新为原数，直到原数为0.

```java
public int p3() {
    int n = 8;
    int c = 0;
    while (n != 0) {
        n &= n - 1;
        c++;
    }
    return c;
}
```

## 4.一条语句判断整数是不是2的整数次方

> 用一条语句判断一个整数是不是2的整数次方。
>
> 思路：2的整数次方的二进制只有一个1，只需要和减一的值进行与运算即可

代码实现：

```java
public boolean p4(int n) {
    return (n & (n - 1)) == 0;
}
```

## 5.位运算思维：将整数的奇偶位互换

> 思路：二进制 
>
> xyxy & 1010 = x0x0  右移 1位 0x0x
>
> xyxy & 0101 =0y0y   左移 1位 y0y0 
>
> 0x0x^ y0y0 = yxyx

代码实现：

```java 
public int p5() {
    //0101 -> 1010 = 10
    int n = 5;
    int ou = n & 0xaaaaaaaa;
    int jn = n & 0x55555555;
    return (ou >> 1) ^ (jn << 1);
}
```

## 6.乘2挪整：二进制表示浮点实数

> 给定一个介于0和1之间的实数，（如0.625），类型为 double，打印它的二进制表示（0.101，因为小数点后的二进制分别表示0.5，0.25.0.125.  如果该数字无法精确地用32位以内的二进制表示，则打印“ERROR“

思路：

```java
public void p6() {
    double num = 0.625;
    StringBuilder sb = new StringBuilder("0.");
    while (num > 0) {
        double r = num * 2;
        if (r >= 1) {
            sb.append("1");
            num = r - 1;
        } else {
            sb.append("0");
            num = r;
        }
        if (sb.length() > 34) {
            System.out.println("ERROR");
            return;
        }
    }
    System.out.println(sb);
}
```

## 7.编程实战：出现k次与出现一次

> 数组中只有一个数出现了1次，其他的数都出现了k次，请输出只出现了1次的数。
>
> 思路：k个k进制的数做不进位加法等于0

```java
public int p7() {
    int[] nums = {1, 1, 1, 2, 2, 2, 8, 3, 3, 3, 4, 4, 4};
    int k = 3;
    int len = nums.length;
    char[][] kRadix = new char[len][k];
    for (int i = 0; i < len; i++) {
        StringBuilder c = new StringBuilder(Integer.toString(nums[i], k)).reverse();
        if (c.length() < k) {
            c.append("0".repeat(Math.max(0, k - c.length() + 1)));
        }
        kRadix[i] = c.toString().toCharArray();
    }
    for (int i = 1; i < len; i++) {
        for (int j = 0; j < k; j++) {
            kRadix[i][j] = (char) ((kRadix[i - 1][j] + kRadix[i][j]) - '0');
        }
    }
    int res = 0;
    for (int i = 0; i < k; i++) {
        res += (kRadix[len - 1][i] % k) * (int)(Math.pow(k, i));
    }
    return res;
}
```