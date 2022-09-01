## 题目一

给定一个有序数组arr，代表坐落在X轴上的点，给定一个正数K，代表绳子的长度，返回绳子最多压中几个点？绳子边缘处盖住点也算盖住

方法一: O(NLog(N))

```java
public static int maxPoint1(int[] arr, int L) {
    int res = 1;
    for (int i = 0; i < arr.length; i++) {
        int nearest = nearestIndex(arr, i, arr[i] - L);
        res = Math.max(res, i - nearest + 1);
    }
    return res;
}

private static int nearestIndex(int[] arr, int R, int value) {
    int L = 0;
    int index = R;
    while (L <= R) {
        int mid = L + ((R - L) >> 1);
        if (arr[mid] >= value) {
            index = mid;
            R = mid - 1;
        } else {
            L = mid + 1;
        }
    }
    return index;
}
```

方法二：O(N)

```java
public static int maxPoint2(int[] arr, int L) {
    int left = 0;
    int right = 0;
    int N = arr.length;
    int max = 0;
    while (left < N) {
        while (right < N && arr[right] - arr[left] <= L) {
            right++;
        }
        max = Math.max(max, right - (left++));
    }
    return max;
}
```

对数器:

```java
// for test
public static int test(int[] arr, int L) {
    int max = 0;
    for (int i = 0; i < arr.length; i++) {
        int pre = i - 1;
        while (pre >= 0 && arr[i] - arr[pre] <= L) {
            pre--;
        }
        max = Math.max(max, i - pre);
    }
    return max;
}

// for test
public static int[] generateArray(int len, int max) {
    int[] ans = new int[(int) (Math.random() * len) + 1];
    for (int i = 0; i < ans.length; i++) {
        ans[i] = (int) (Math.random() * max);
    }
    Arrays.sort(ans);
    return ans;
}

public static void main(String[] args) {
    int len = 100;
    int max = 1000;
    int testTime = 100000;
    System.out.println("测试开始");
    for (int i = 0; i < testTime; i++) {
        int L = (int) (Math.random() * max);
        int[] arr = generateArray(len, max);
        int ans1 = maxPoint1(arr, L);
        int ans2 = maxPoint2(arr, L);
        int ans3 = test(arr, L);
        if (ans1 != ans2 || ans2 != ans3) {
            System.out.println("oops!");
            break;
        }
    }

}
```





## 题目二

括号有效配对是指:

1)任何一个左括号都能找到和其正确配对的右括号

2)任何一个右括号都能找到和其正确配对的左括号

有效的：(()) ()() (()())等

无效的: (()   )(等

问题一： 怎么判断一个括号字符串有效

```Java
public static boolean valid(String s) {
    char[] str = s.toCharArray();
    int count = 0;
    for (char c : str) {
        count += c == '(' ? 1 : -1;
        if (count < 0) {
            return false;
        }
    }
    return count == 0;
}
```

问题二：如何一个括号字符串无效，返回至少填几个字符能让其整体有效

```java
public static int minParentheses(String s) {
    char[] str = s.toCharArray();
    int count = 0;
    int need = 0;
    for (int i = 0; i < str.length; i++) {
        if (str[i] == '(') {
            count++;
        } else {
            if (count == 0) {
                need++;
            } else {
                count--;
            }
        }
    }
    return count + need;
}
```





## 题目三

括号有效配对是指:

1)任何一个左括号都能找到和其正确配对的右括号

2)任何一个右括号都能找到和其正确配对的左括号

返回一个括号字符串中，最长的括号有效字串的长度

```Java
public static int maxLength(String s) {
    if (s == null || s.equals("")) {
        return 0;
    }
    char[] str = s.toCharArray();
    int[] dp = new int[str.length];
    int pre = 0;
    int ans = 0;
    for (int i = 1; i < str.length; i++) {
        if (str[i] == ')') {
            pre = i - dp[i - 1] - 1;
            if (pre >= 0 && str[pre] == '(') {
                dp[i] = dp[i - 1] + 2 + (pre > 0 ? dp[pre - 1] : 0);
            }
        }
        ans = Math.max(ans, dp[i]);
    }
    return ans;
}
```



## 题目四

有一些排成一行的正方形。每个正方形已经被染成红色或者绿色。现在可以选择任意一个正方形然后用这两种颜色的任意一种进行染色，这个正方形的颜色将会被覆盖。目标是在完成染色之后，每个红色R都比每个绿色G距离最左侧近。返回最少需要涂染几个正方形。如样例所示:s = RGRGR我们涂染之后变成RRRGG满足要求了，涂染的个数为2,没有比这个更好的涂染方案。

```java
public static int minPaint1(String s) {
    if (s == null || s.length() < 2) {
        return 0;
    }
    char[] chs = s.toCharArray();
    int N = chs.length;

    int[] right = new int[N];
    right[N - 1] = chs[N - 1] == 'R' ? 1 : 0;
    for (int i = N - 2; i >= 0; i--) {
        right[i] = right[i + 1] + (chs[i] == 'R' ? 1 : 0);
    }
    int ans = right[0];
    int left = 0;
    for (int i = 0; i < N - 1; i++) {
        left += chs[i] == 'G' ? 1 : 0;
        ans = Math.min(ans, left + right[i + 1]);
    }
    ans = Math.min(ans, left + (chs[N - 1] == 'G' ? 1 : 0));
    return ans;
}
//优化 去掉辅助数组
public static int minPaint2(String s) {
    if (s == null || s.length() < 2) {
        return 0;
    }
    char[] chs = s.toCharArray();
    int N = chs.length;

    int right = 0;
    for (int i = 0; i < N; i++) {
        right += chs[i] == 'R' ? 1 : 0;
    }
    int ans = right;
    int left = 0;
    for (int i = 0; i < N - 1; i++) {
        left += chs[i] == 'G' ? 1 : 0;
        right -= chs[i] == 'R' ? 1 : 0;
        ans = Math.min(ans, left + right);
    }
    ans = Math.min(ans, left + (chs[N - 1] == 'G' ? 1 : 0));
    return ans;
}
```



## 题目五

给定一个N*N的矩阵matrix，只有0和1两种值，返回边框全是1的最大正方形的边长。

```java
public static void setBorderMap(int[][] m, int[][] right, int[][] down) {
    int r = m.length;
    int c = m[0].length;
    if (m[r - 1][c - 1] == 1) {
        right[r - 1][c - 1] = 1;
        down[r - 1][c - 1] = 1;
    }
    for (int i = r - 2; i >= 0; i--) {
        if (m[i][c - 1] == 1) {
            right[i][c - 1] = 1;
            down[i][c - 1] = down[i + 1][c - 1] + 1;
        }
    }
    for (int j = c - 2; j >= 0; j--) {
        if (m[r - 1][j] == 1) {
            right[r - 1][j] = right[r - 1][j + 1] + 1;
            down[r - 1][j] = 1;
        }
    }
    for (int i = r - 2; i >= 0; i--) {
        for (int j = c - 2; j >= 0; j--) {
            if (m[i][j] == 1) {
                right[i][j] = right[i][j + 1] + 1;
                down[i][j] = down[i + 1][j] + 1;
            }
        }
    }
}


public static int getMaxSize(int[][] m) {
    int N = m.length;
    int M = m[0].length;

    int[][] right = new int[N][M];
    int[][] down = new int[N][M];

    setBorderMap(m, right, down);

    for (int edge = Math.min(N, M); edge > 0; edge--) {
        for (int i = 0; i < N - edge + 1; i++) {
            for (int j = 0; j < M - edge + 1; j++) {
                if (right[i][j] >= edge && down[i][j] >= edge
                        && right[i + edge - 1][j] >= edge && down[i][j + edge - 1] >= edge) {
                    return edge;
                }
            }
        }
    }
    return 0;
}
```



## 题目六

给定一个正整数M，请构造出一个长度为M的数组arr， 要求对任意的i、j、k三个位置，如果i<j<k， 都有arr[] + arr[k] != 2*arr[j]返回构造出的arr

```java
public static int[] makeNo(int size) {
    if (size == 1) {
        return new int[] {1};
    }
    int halfSize = (size + 1) / 2;
    int[] base = makeNo(halfSize);
    int[] ans = new int[size];
    int index = 0;
    for (; index < halfSize; index++) {
        ans[index] = base[index] * 2 - 1;
    }
    for (int i = 0; index < size; i++, index++) {
        ans[index] = base[i] * 2;
    }
    return ans;
}
```