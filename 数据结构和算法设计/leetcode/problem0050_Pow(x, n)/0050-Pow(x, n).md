原题：[Pow(x, n)](https://leetcode-cn.com/problems/powx-n/)

实现 [pow(*x*, *n*)](https://www.cplusplus.com/reference/valarray/pow/) ，即计算 x 的 n 次幂函数（即，xn）。

**示例 1：**

```
输入：x = 2.00000, n = 10
输出：1024.00000
```

**示例 2：**

```
输入：x = 2.10000, n = 3
输出：9.26100
```

**示例 3：**

```
输入：x = 2.00000, n = -2
输出：0.25000
解释：2-2 = 1/22 = 1/4 = 0.25
```

**提示：**

- `-100.0 < x < 100.0`
- `-2^31 <= n <= 2^31-1`
- `-10^4 <= x^n <= 10^4`



**思路**：

方法一：递归，分治思想

如果n是偶数，如图

![image-20211106183812809](https://gitee.com/JKcoding/imgs/raw/master/img/202111061838354.png)

当n是奇数的时候，只需要在合并的时候再多乘一个x即可

```java
class Solution {
    public double myPow(double x, int n) {
        long N = n;
        return n >= 0 ? helper(x, N) : 1.0 / helper(x, -N);
    }

    public double helper(double x, long N) {
        if (N == 0) return 1.0;
        double sub = helper(x, N / 2);
        return N % 2 == 0 ? sub * sub : sub * sub * x;
    }
}
```

方法二：迭代

如图，n=85可以分解成二进制，所以x<sup>n</sup> = x<sup>64</sup> *  x<sup>16</sup>  *  x<sup>4</sup>  *  x<sup>1</sup> = x <sup>64 + 16 + 4 + 1</sup> = x<sup>85</sup>

所以，只需要统计n的二进制位为1的即可

![image-20211106191317826](https://gitee.com/JKcoding/imgs/raw/master/img/202111061913115.png)

```java
class Solution {
    public double myPow(double x, int n) {
        long N = n;
        return n >= 0 ? helper(x, N) : 1.0 / helper(x, -N);
    }

    public double helper(double x, long N) {
       double res = 1.0;
       while (N > 0) {
           if ((N & 1) == 1) {
               //统计二进制位为1的
               res *= x;
           }
           x *= x;
           N >>= 1;
       }
       return res;
    }
}
```

