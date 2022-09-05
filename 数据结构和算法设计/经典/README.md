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



## 题目七

给定一个二叉树的头节点head,路径的规定有以下三种不同的规定:

1)路径必须是头节点出发，到叶节点为止，返回最大路径和

写法一：

```java
public static class Node {
    public Node left;
    public Node right;
    public int value;

    public Node(int value) {
        this.value = value;
    }

    public Node() {
    }
}

public static int MAX_SUM;
public static int maxPath(Node root) {
    MAX_SUM = Integer.MIN_VALUE;
    process1(root, 0);
    return MAX_SUM;
}
public static void process1(Node root, int pre) {
    if (root == null) {
        return;
    }
    if (root.left == null && root.right ==  null) {
        MAX_SUM = Math.max(MAX_SUM, pre + root.value);
        return;
    }
    process1(root.left, pre + root.value);
    process1(root.right, pre + root.value);
}
```

写法二：

```Java
public static int maxPath2(Node root) {
        if (root == null) {
            return 0;
        }
        return process2(root);
    }

    public static int process2(Node root) {
        if (root.left == null  && root.right == null) {
            return root.value;
        }
        int next = Integer.MIN_VALUE;
        if (root.left != null) {
            next = Math.max(next, process2(root.left));
        }
        if (root.right != null) {
            next = Math.max(next, process2(root.right));
        }
        return root.value + next;
    }
```

2)路径可以从任何节点出发，但必须往下走到达任何节点，返回最大路径和

```java
public static class Info {
        public int allTreeMaxSum;
        public int fromHeadMaxSum;

        public Info(int allTreeMaxSum, int fromHeadMaxSum) {
            this.allTreeMaxSum = allTreeMaxSum;
            this.fromHeadMaxSum = fromHeadMaxSum;
        }
    }
public static int maxSum(Node root) {
    if (root == null) {
        return 0;
    }
    return process3(root).allTreeMaxSum;
}

public static Info process3(Node root) {
    if (root == null) {
        return null;
    }
    Info leftInfo = process3(root.left);
    Info rightInfo = process3(root.right);
    int p1 = Integer.MIN_VALUE;
    if (leftInfo != null) {
        p1 = leftInfo.allTreeMaxSum;
    }
    int p2 = Integer.MIN_VALUE;
    if (rightInfo != null) {
        p2 = rightInfo.allTreeMaxSum;
    }
    int p3 = root.value;
    int p4 = Integer.MIN_VALUE;
    if (leftInfo != null) {
        p4 = root.value + leftInfo.fromHeadMaxSum;
    }
    int p5 = Integer.MIN_VALUE;
    if (rightInfo != null) {
        p5 = root.value + rightInfo.fromHeadMaxSum;
    }
    int allTreeMaxSum = Math.max(Math.max(Math.max(p1, p2), p3), Math.max(p4, p5));
    int formHeadMaxSum = Math.max(Math.max(p3, p4), p5);
    return new Info(allTreeMaxSum, formHeadMaxSum);
}
```



3)路径可以从任何节点出发，到任何节点，返回最大路径和

```java
public static int maxSum2(Node root) {
    if (root == null) {
        return 0;
    }
    return process4(root).allTreeMaxSum;
}

public static Info process4(Node root) {
    if (root == null) {
        return null;
    }
    Info leftInfo = process3(root.left);
    Info rightInfo = process3(root.right);
    int p1 = Integer.MIN_VALUE;
    if (leftInfo != null) {
        p1 = leftInfo.allTreeMaxSum;
    }
    int p2 = Integer.MIN_VALUE;
    if (rightInfo != null) {
        p2 = rightInfo.allTreeMaxSum;
    }
    int p3 = root.value;
    int p4 = Integer.MIN_VALUE;
    if (leftInfo != null) {
        p4 = root.value + leftInfo.fromHeadMaxSum;
    }
    int p5 = Integer.MIN_VALUE;
    if (rightInfo != null) {
        p5 = root.value + rightInfo.fromHeadMaxSum;
    }
    int p6 = Integer.MIN_VALUE;
    if (leftInfo != null && rightInfo != null) {
        p6 = root.value + leftInfo.fromHeadMaxSum + rightInfo.fromHeadMaxSum;
    }
    int allTreeMaxSum = Math.max(Math.max(Math.max(p1, p2), p3), Math.max(Math.max(p4, p5), p6));
    int formHeadMaxSum = Math.max(Math.max(p3, p4), p5);
    return new Info(allTreeMaxSum, formHeadMaxSum);
}
```



## 题目八

有个打包机器从左到右一字排开，上方有一个自动装置会抓取一批放物品到每个打包机上，放到每个机器上的这些物品数量有多有少，由于物品数量不相同，需要工人将每个机器上的物品进行移动从而到达物品数量相等才能打包。每个物品重量太大、每次只能搬一个物品进
行移动，为了省力，只在相邻的机器上移动。请计算在搬动最小轮数的前提下，使每个机器上的物品数量相等。如果不能使每个机器上的物品相同，返回-1。例如[1,0,5]表示有3个机器，每个机器上分别有1、0、5个物品，经过这些轮后：
第一轮：10<-5=>1 1 4

第二轮：1<-1<-4=>2 1 3

第三轮：21<-3=>2 2 2
移动了3轮，每个机器上的物品相等，所以返回3
例如[2,2,3]表示有3个机器，每个机器上分别有2、2、3个物品，这些物品不管怎么移动，都不能使三个机器上物品数量相等，返回-1

```java
public static int minOps(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int size = arr.length;
    int sum = 0;
    for (int i = 0; i < arr.length; i++) {
        sum += arr[i];
    }
    if (sum % size != 0) {
        return -1;
    }
    int avg = sum / size;
    int leftSum = 0;
    int ans = 0;
    for (int i = 0; i < arr.length; i++) {
        int leftRest = leftSum - i * avg;
        int rightRest = (sum - leftSum - arr[i]) - (size - i - 1) * avg;
        if (leftRest < 0 && rightRest < 0) {
            ans = Math.max(ans, Math.abs(leftRest) + Math.abs(rightRest));
        } else {
            ans = Math.max(ans, Math.max(Math.abs(leftRest), Math.abs(rightRest)));
        }
        leftSum += arr[i];
    }
    return ans;
}
```



## 题目九

给定一个数组arr长度为N,你可以把任意长度大于0且小于N的前缀作为左部分，剩下的作为右部分。但是每种划分下都有左部分的最大值和右部分的最大值，请返回最大的，左部分最大值减去右部分最大值的绝对值。

```java
public static int maxLeftSubRight(int[] arr) {
    if (arr == null || arr.length < 2) {
        return 0;
    }
    int max = Integer.MIN_VALUE;
    for (int num : arr) {
        max = Math.max(max, num);
    }
    return max - Math.min(arr[0], arr[arr.length - 1]);
}
```



## 题目十

给定一个数组r,已知其中所有的值都是非负的，将这个数组看作一个容器，请返回容器能装多少水
比如，r={3,1,2,5,2,4},根据值画出的直方图就是容器形状，该容器可以装下5格水，再比如，arr={4,5,1,3,2}, 该容器可以装下2格水

```java
public static int water(int[] arr) {
    if (arr == null || arr.length < 2) {
        return 0;
    }
    int N = arr.length;
    int L = 1;
    int R = N - 2;
    int leftMax = arr[0];
    int rightMax = arr[N - 1];
    int water = 0;
    while (L <= R) {
        if (leftMax <= rightMax) {
            water += Math.max(0, leftMax - arr[L]);
            leftMax = Math.max(leftMax, arr[L++]);
        } else {
            water += Math.max(0, rightMax - arr[R]);
            rightMax = Math.max(rightMax, arr[R--]);
        }
    }
    return water;
}
```



## 题目十一

给你一个 `m x n` 的矩阵，其中的值均为非负整数，代表二维高度图每个单元的高度，请计算图中形状最多能接多少体积的雨水。

```java
public static int trapRainWater(int[][] heightMap) {
    if (heightMap == null || heightMap.length == 0 || heightMap[0] == null || heightMap[0].length == 0) {
        return 0;
    }
    int N = heightMap.length;
    int M = heightMap[0].length;

    boolean[][] isEnter = new boolean[N][M];

    PriorityQueue<Node> heap = new PriorityQueue<>(new NodeComparator());

    for (int col = 0; col < M - 1; col++) {
        isEnter[0][col] = true;
        heap.add(new Node(heightMap[0][col], 0, col));
    }
    for (int row = 0; row < N - 1; row++) {
        isEnter[row][M - 1] = true;
        heap.add(new Node(heightMap[row][M - 1], row, M - 1));
    }
    for (int col = M - 1; col > 0; col--) {
        isEnter[N - 1][col] = true;
        heap.add(new Node(heightMap[N - 1][col], N - 1, col));
    }
    for (int row = N - 1; row > 0; row--) {
        isEnter[row][0] = true;
        heap.add(new Node(heightMap[row][0], row, 0));
    }

    int water = 0;
    int max = 0;
    while (!heap.isEmpty()) {
        Node cur = heap.poll();
        int r = cur.row;
        int c = cur.col;
        max = Math.max(max, cur.value);
        if (r > 0 && !isEnter[r - 1][c]) {
            water += Math.max(0, max - heightMap[r - 1][c]);
            isEnter[r - 1][c] = true;
            heap.add(new Node(heightMap[r - 1][c], r - 1, c));
        }
        if (r < N - 1 && !isEnter[r + 1][c]) {
            water += Math.max(0, max - heightMap[r + 1][c]);
            isEnter[r + 1][c] = true;
            heap.add(new Node(heightMap[r + 1][c], r + 1, c));
        }
        if (c > 0 && !isEnter[r][c - 1]) {
            water += Math.max(0, max - heightMap[r][c - 1]);
            isEnter[r][c - 1] = true;
            heap.add(new Node(heightMap[r][c - 1], r, c - 1));
        }
        if (c < M - 1 && !isEnter[r][c + 1]) {
            water += Math.max(0, max - heightMap[r][c + 1]);
            isEnter[r][c + 1] = true;
            heap.add(new Node(heightMap[r][c + 1], r, c + 1));
        }
    }
    return water;
}
```



## 题目十二

给定一个有序数组arr,给定一个正数aim
1)返回累加和为aim的，所有不同二元组
2)返回累加和为aim的，所有不同三元组



## 题目十三

长度为N的数组arr,一定可以组成N^2个数值对。
例如arr=[3,1,2],
数值对有(3,3)(3,1)(3,2)(1,3)(1,1)(1,2)(2,3)(2,1)(2,2)
也就是任意两个数都有数值对，而且自己和自己也算数值对。
数值对怎么排序？规定，第一维数据从小到大，第一维数据一样的，第二维数组也
从小到大。所以上面的数值对排序的结果为：
(1,1)(1,2)1,3)2,1)2,2)2,3)3,1)3,2)3,3)
给定一个数组arr,和整数k,返回第k小的数值对。

```java
	public static class Pair {
		public int x;
		public int y;
    Pair(int a, int b) {
		x = a;
		y = b;
	}
}

public static class PairComparator implements Comparator<Pair> {

	@Override
	public int compare(Pair arg0, Pair arg1) {
		return arg0.x != arg1.x ? arg0.x - arg1.x : arg0.y - arg1.y;
	}

}

// O(N^2 * log (N^2))的复杂度，你肯定过不了
// 返回的int[] 长度是2，{3,1} int[2] = [3,1]
public static int[] kthMinPair1(int[] arr, int k) {
	int N = arr.length;
	if (k > N * N) {
		return null;
	}
	Pair[] pairs = new Pair[N * N];
	int index = 0;
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			pairs[index++] = new Pair(arr[i], arr[j]);
		}
	}
	Arrays.sort(pairs, new PairComparator());
	return new int[] { pairs[k - 1].x, pairs[k - 1].y };
}

// O(N*logN)的复杂度，你肯定过了
public static int[] kthMinPair2(int[] arr, int k) {
	int N = arr.length;
	if (k > N * N) {
		return null;
	}
	// O(N*logN)
	Arrays.sort(arr);
	// 第K小的数值对，第一维数字，是什么 是arr中
	int fristNum = arr[(k - 1) / N];
	int lessFristNumSize = 0;// 数出比fristNum小的数有几个
	int fristNumSize = 0; // 数出==fristNum的数有几个
	// <= fristNum
	for (int i = 0; i < N && arr[i] <= fristNum; i++) {
		if (arr[i] < fristNum) {
			lessFristNumSize++;
		} else {
			fristNumSize++;
		}
	}
	int rest = k - (lessFristNumSize * N);
	return new int[] { fristNum, arr[(rest - 1) / fristNumSize] };
}

// O(N)的复杂度，你肯定蒙了
public static int[] kthMinPair3(int[] arr, int k) {
	int N = arr.length;
	if (k > N * N) {
		return null;
	}
	// 在无序数组中，找到第K小的数，返回值
	// 第K小，以1作为开始
	int fristNum = getMinKth(arr, (k - 1) / N);
	// 第1维数字
	int lessFristNumSize = 0;
	int fristNumSize = 0;
	for (int i = 0; i < N; i++) {
		if (arr[i] < fristNum) {
			lessFristNumSize++;
		}
		if (arr[i] == fristNum) {
			fristNumSize++;
		}
	}
	int rest = k - (lessFristNumSize * N);
	return new int[] { fristNum, getMinKth(arr, (rest - 1) / fristNumSize) };
}

// 改写快排，时间复杂度O(N)
// 在无序数组arr中，找到，如果排序的话，arr[index]的数是什么？
public static int getMinKth(int[] arr, int index) {
	int L = 0;
	int R = arr.length - 1;
	int pivot = 0;
	int[] range = null;
	while (L < R) {
		pivot = arr[L + (int) (Math.random() * (R - L + 1))];
		range = partition(arr, L, R, pivot);
		if (index < range[0]) {
			R = range[0] - 1;
		} else if (index > range[1]) {
			L = range[1] + 1;
		} else {
			return pivot;
		}
	}
	return arr[L];
}

public static int[] partition(int[] arr, int L, int R, int pivot) {
	int less = L - 1;
	int more = R + 1;
	int cur = L;
	while (cur < more) {
		if (arr[cur] < pivot) {
			swap(arr, ++less, cur++);
		} else if (arr[cur] > pivot) {
			swap(arr, cur, --more);
		} else {
			cur++;
		}
	}
	return new int[] { less + 1, more - 1 };
}

public static void swap(int[] arr, int i, int j) {
	int tmp = arr[i];
	arr[i] = arr[j];
	arr[j] = tmp;
}

// 为了测试，生成值也随机，长度也随机的随机数组
public static int[] getRandomArray(int max, int len) {
	int[] arr = new int[(int) (Math.random() * len) + 1];
	for (int i = 0; i < arr.length; i++) {
		arr[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
	}
	return arr;
}

// 为了测试
public static int[] copyArray(int[] arr) {
	if (arr == null) {
		return null;
	}
	int[] res = new int[arr.length];
	for (int i = 0; i < arr.length; i++) {
		res[i] = arr[i];
	}
	return res;
}

// 随机测试了百万组，保证三种方法都是对的
public static void main(String[] args) {
	int max = 100;
	int len = 30;
	int testTimes = 100000;
	System.out.println("test bagin, test times : " + testTimes);
	for (int i = 0; i < testTimes; i++) {
		int[] arr = getRandomArray(max, len);
		int[] arr1 = copyArray(arr);
		int[] arr2 = copyArray(arr);
		int[] arr3 = copyArray(arr);
		int N = arr.length * arr.length;
		int k = (int) (Math.random() * N) + 1;
		int[] ans1 = kthMinPair1(arr1, k);
		int[] ans2 = kthMinPair2(arr2, k);
		int[] ans3 = kthMinPair3(arr3, k);
		if (ans1[0] != ans2[0] || ans2[0] != ans3[0] || ans1[1] != ans2[1] || ans2[1] != ans3[1]) {
			System.out.println("Oops!");
		}
	}
	System.out.println("test finish");
}
```



## 题目十四

每种工作有难度和报酬，规定如下

```java
class Job
public int money;//该工作的报酬
public int hard;//该工作的难度
```

给定一个Job类型的数组jobarr, 表示所有岗位，每个岗位都可以提供任意份工作，选工作的标准是在难度不超过自身能力值的情况下，选择报酬最高的岗位，给定一个int类型的数组arr,表示所有人的能力，返回int类型的数组，表示每个人按照标准选工作后所能获得的最高报酬

```java
public static class Job {
    public int money;
    public int hard;

    public Job(int money, int hard) {
        this.money = money;
        this.hard = hard;
    }
}

public static class JobComparator implements Comparator<Job> {
    @Override
    public int compare(Job o1, Job o2) {
        return o1.hard == o2.hard ? (o2.money - o1.money) : (o1.hard - o2.hard);
    }
}

public static int[] getMoneys(Job[] job, int[] ability) {
    Arrays.sort(job, new JobComparator());
    TreeMap<Integer, Integer> map = new TreeMap<>();
    map.put(job[0].hard, job[0].money);
    Job pre = job[0];
    for (int i = 1; i < job.length; i++) {
        if (job[i].hard != pre.hard && job[i].money > pre.hard) {
            pre = job[i];
            map.put(pre.hard, pre.money);
        }
    }
    int[] ans = new int[ability.length];
    for (int i = 0; i < ability.length; i++) {
        Integer key = map.floorKey(ability[i]);
        ans[i] = key != null ? map.get(key) : 0;
    }
    return ans;
}
```



## 题目十五

背包容量为W，一共有n袋零食，第i袋零食体积为v[i] > 0，总体积不超过背包容量的情况下，一共有多少种零食放法？（总体积为0也算一种放法）。

```java
public static int ways1(int[] arr, int w) {
    return process(arr, 0, w);
}

public static int process(int[] arr, int index, int rest) {
    if (rest < 0) {
        return 0;
    }
    if (index == arr.length) {
        return 1;
    }
    return process(arr, index + 1, rest) + process(arr, index + 1, rest - arr[index]);
}


public static int ways2(int[] arr, int w) {
    int N = arr.length;
    int[][] dp = new int[N + 1][w + 1];
    for (int i = 0; i <= w; i++) {
        dp[N][i] = 1;
    }
    for (int i = N - 1; i >= 0; i--) {
        for (int j = 0; j <= w; j++) {
            dp[i][j] = dp[i + 1][j] + (j - arr[i] >= 0 ? dp[i + 1][j - arr[i]] : 0);
        }
    }
    return dp[0][w];
}

public static int ways3(int[] arr, int w) {
    int N = arr.length;
    int[][] dp = new int[N][w + 1];
    for (int i = 0; i < N; i++) {
        dp[i][0] = 1;
    }
    if (arr[0] <= w) {
        dp[0][arr[0]] = 1;
    }
    for (int i = 1; i < N; i++) {
        for (int j = 1; j <= w; j++) {
            dp[i][j] = dp[i - 1][j] + (j - arr[i] >= 0 ? dp[i - 1][j - arr[i]] : 0);
        }
    }
    int ans = 0;
    for (int i = 0; i <= w; i++) {
        ans += dp[N - 1][i];
    }
    return ans;
}
```