原题：[Sqrt(x)](https://leetcode-cn.com/problems/sqrtx/)

给你一个非负整数 `x` ，计算并返回 `x` 的 **算术平方根** 。

由于返回类型是整数，结果只保留 **整数部分** ，小数部分将被 **舍去 。**

**注意：**不允许使用任何内置指数函数和算符，例如 `pow(x, 0.5)` 或者 `x ** 0.5` 。

 

**示例 1：**

```
输入：x = 4
输出：2
```

**示例 2：**

```
输入：x = 8
输出：2
解释：8 的算术平方根是 2.82842..., 由于返回类型是整数，小数部分将被舍去。
```

**提示：**

- `0 <= x <= 2^31 - 1`



**思路**：

方法一：二分法

```java
    // 8
    // 1 2 3 4 5 6 7 8
    // l             r
    // l     m       r
    // l   r
    // l m r
    //     lr
    //   r l
     public int mySqrt(int x) {
        if (x == 0 || x == 1) {
            return x;
        }
        int left = 1, right = x, mid = 0;
         while (left <= right) {
             mid = left + (right - left) / 2;
             if (mid > x / mid) {
                 right = mid - 1;
             } else {
                 left = mid + 1;
             }
         }
         return right;
    }
```



方法二：牛顿迭代法

> [牛顿迭代法](https://baike.baidu.com/item/牛顿迭代法)是一种可以用来快速求解函数零点的方法。
>
> 为了叙述方便，我们用 C*C* 表示待求出平方根的那个整数。显然，C*C* 的平方根就是函数
>
> *y*=*f*(*x*)=*x*<sup>2</sup>−*C*
>
> 的零点。
>
> 牛顿迭代法的本质是借助泰勒级数，从初始值开始快速向零点逼近。我们任取一个X<sub>0</sub>作为初始值，在每一步的迭代中，我们找到函数图像上的点(x<sub>i</sub>,*f*(x<sub>i</sub>)), 过该点作一条斜率为该点导数 f'(x<sub>i</sub>)的直线，与横轴的交点记为X<sub>i+1</sub>, X<sub>i+1</sub> 相较于 x<sub>i</sub> 而言距离零点更近。在经过多次迭代后，我们就可以得到一个距离零点非常接近的交点。下图给出了从 X<sub>0</sub> 开始迭代两次，得到X<sub>1</sub> 和 X<sub>2</sub> 的过程。
>
> ![](https://gitee.com/JKcoding/imgs/raw/master/img/202111201141394.png)
>
> 我们选择 X<sub>0</sub> = *C* 作为初始值。在每一步迭代中，我们通过当前的交点X<sub>i</sub> ，找到函数图像上的点 (X<sub>i</sub> , X<sub>i</sub> <sup>2</sup> - C)，作一条斜率为 f'(X<sub>i</sub>) = 2X<sub>i</sub> 的直线，直线的方程为:   (y-X<sub>i</sub><sup>2</sup> + C ) / (x - X<sub>i</sub>) =  2X<sub>i</sub> 
>
> 整理：y =  2X<sub>i</sub> (x - X<sub>i</sub>) + X<sub>i</sub><sup>2</sup> - C = 2X<sub>i</sub> x - X<sub>i</sub><sup>2</sup> - C 
>
> 令y = 0, x = (X<sub>i</sub><sup>2</sup> + C) / 2X<sub>i</sub> = (X<sub>i</sub>  + C / X<sub>i</sub> ) / 2
>
> 经过k次迭代后  x<sup>2</sup> <= C， 此时x就是我们想要的答案

```java
  public int mySqrt(int x) {
        if (x == 0 || x == 1) {
            return x;
        }
        long r = x;
        while (r * r > x) {
            r = (r + x / r) / 2;
        }
        return (int)r;
    }
```

