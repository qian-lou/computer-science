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



## 题目十六

最长公共字串

```java
public static String getLcs(String s1, String s2) {
    if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
        return "";
    }
    int row = 0;
    int col = s2.length() - 1;
    int end = 0;
    int max = 0;
    while (row < s1.length()) {
        int i = row;
        int j = col;
        int len = 0;
        while (i < s1.length() && j < s2.length()) {
            if (s1.charAt(i) != s2.charAt(j)) {
                len = 0;
            } else {
                len++;
            }
            if (len > max) {
                max = len;
                end = i;
            }
            i++;
            j++;
        }
        if (col > 0) {
            col--;
        } else {
            row++;
        }
    }
    return s1.substring(end - max + 1, end + 1);
}
```



## 题目十七

给定一个由字符串组成的数组String[] strs,给定一个正数K，返回词频最大的前K个字符串，假设结果是唯一的

```java
public static class Node {
    public String str;
    public int times;

    public Node(String str, int times) {
        this.str = str;
        this.times = times;
    }
}

public static class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        return o1.times - o2.times;
    }
}

public static void printTopKAndRank(String[] arr, int topK) {
    if (arr == null || arr.length == 0 || topK < 1) {
        return;
    }
    HashMap<String, Integer> map = new HashMap<>();
    for (String str : arr) {
        if (!map.containsKey(str)) {
            map.put(str, 1);
        } else {
            map.put(str, map.get(str) + 1);
        }
    }
    topK = Math.min(arr.length, topK);
    PriorityQueue<Node> heap = new PriorityQueue<>(new NodeComparator());
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
        Node node = new Node(entry.getKey(), entry.getValue());
        if (heap.size() < topK) {
            heap.add(node);
        } else {
            if (heap.peek().times < node.times) {
                heap.poll();
                heap.add(node);
            }
        }
    }
    while (!heap.isEmpty()) {
        System.out.println(heap.poll().str);
    }
}
```



## 题目十八 

请实现如下结构：

```java
TopRecord{
public TopRecord(intK):构造时事先指定好K的大小，构造后就固定不变了
public void add(Strinig str)）:向该结构中加入一个字符串，可以重复加入
public List<String> topK:返回之前加入的所有字符串中，词频最大的K个

}
```

要求:
add方法，复杂度O(logK)
top方法，复杂度O(K)

```java
public static class Node {
    public String str;
    public int times;

    public Node(String str, int times) {
        this.str = str;
        this.times = times;
    }
}

public static class TopKRecord {
    private Node[] heap;
    private int heapSize;
    private HashMap<String, Node> strNodeMap;
    private HashMap<Node, Integer> nodeIndexMap;

    public TopKRecord(int topK) {
        heap = new Node[topK];
        heapSize = 0;
        strNodeMap = new HashMap<>();
        nodeIndexMap = new HashMap<>();
    }

    public void add(String str) {
        Node curNode = null;
        int preIndex = -1;
        if (!strNodeMap.containsKey(str)) {
            curNode = new Node(str, 1);
            strNodeMap.put(str, curNode);
            nodeIndexMap.put(curNode, -1);
        } else {
            curNode = strNodeMap.get(str);
            curNode.times++;
            preIndex = nodeIndexMap.get(curNode);
        }
        if (preIndex == -1) {
            if (heapSize == heap.length) {
                if (heap[0].times < curNode.times) {
                    nodeIndexMap.put(heap[0], -1);
                    nodeIndexMap.put(curNode, 0);
                    heap[0] = curNode;
                    heapify(0, heapSize);
                }
            } else {
                nodeIndexMap.put(curNode, heapSize);
                heap[heapSize] = curNode;
                heapInsert(heapSize++);
            }
        } else {
            heapify(preIndex, heapSize);
        }

    }

    private void heapify(int index, int heapSize) {
        int l = index * 2 + 1;
        int r = index * 2 + 1;
        int smallest = index;
        while (l < heapSize) {
            if (heap[l].times < heap[index].times) {
                smallest = l;
            }
            if (r < heapSize && heap[r].times < heap[smallest].times) {
                smallest = r;
            }
            if (smallest != index) {
                swap(smallest, index);
            } else {
                break;
            }
            index = smallest;
            l = index * 2 + 1;
            r = index * 2 + 2;
        }
    }

    public void heapInsert(int index) {
        while (index != 0) {
            int parent = (index - 1) / 2;
            if (heap[index].times < heap[parent].times) {
                swap(parent, index);
              	index = parent;
            } else {
                break;
            }
        }
    }

    public void swap(int index1, int index2) {
        nodeIndexMap.put(heap[index1], index2);
        nodeIndexMap.put(heap[index2], index1);
        Node node = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = node;
    }

    public void printTopK() {
        for (int i = 0; i < heap.length; i++) {
            if (heap[i] == null) {
                break;
            }
            System.out.println("str: " + heap[i].str);
            System.out.println("times: " + heap[i].times);
        }
    }
```



## 题目十九

最长公共子序列

```java
public static int lcs2(String s1, String s2) {
    char[] str1 = s1.toCharArray();
    char[] str2 = s2.toCharArray();
    int N = str1.length;
    int M = str2.length;
    int[][] dp = new int[N][M];
    dp[0][0] = str1[0] == str2[0] ? 1 : 0;
    for (int i = 1; i < N; i++) {
        dp[i][0] = str1[0] == str2[i] ? 1 : dp[i - 1][0];
    }
    for (int j = 1; j < M; j++) {
        dp[0][j] = str2[0] == str1[j] ? 1 : dp[0][j - 1];
    }
    for (int i = 1; i < N; i++) {
        for (int j = 1; j < M; j++) {
            dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            if (str1[i] == str2[j]) {
                dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] + 1);
            }
        }
    }
    return dp[N - 1][M - 1];
}
```



## 题目二十

给你一个字符串类型的数组arr,譬如：
String arr ="b\st","d\","ald\e","a\b\c"}
把这些路径中蕴含的目录结构给打印出来，子目录直接列在父目录下面，并比父目录向右进两格，
同一级的需要按字母顺序排列不能乱。

```java
public static class Node {
    public String path;
    public TreeMap<String, Node> nextMap;
    public Node(String s) {
        this.path = s;
        nextMap = new TreeMap<>();
    }
}

public static void print(String[] folderPaths) {
    if (folderPaths == null || folderPaths.length == 0) {
        return;
    }
    printTree(generateFolderTree(folderPaths), 0);
}



public static Node generateFolderTree(String[]folderPaths) {
    Node head = new Node("");
    for (String foldPath : folderPaths) {
        String[] paths = foldPath.split("\\\\");
        Node cur = head;
        for (int i = 0; i < paths.length; i++) {
            if (!cur.nextMap.containsKey(paths[i])) {
                cur.nextMap.put(paths[i], new Node(paths[i]));
            }
            cur = cur.nextMap.get(paths[i]);
        }
    }
    return head;
}

public static void printTree(Node node, int level) {
    if (level != 0) {
        System.out.println(get4nSpace(level) + node.path);
    }
    for(Node next : node.nextMap.values()) {
        printTree(next, level + 1);
    }
}

public static String get4nSpace(int n) {
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i < n; i++) {
        sb.append("    ");
    }
    return sb.toString();
}


public static void main(String[] args) {
    print(new String[] {"a\\b\\c", "b\\v\\d\\c"});
}
```





## 题目二十一

已知一棵二叉树中没有重复节点，并且给定了这棵树的中序遍历数组和先序
遍历数组，返回后序遍历数组。
比如给定：
in[] pre={1,2,4,5,3,6,7};
int[] in={4,2,5,1,6,3,7}; 

返回：{4,5,2,6,7,3,1}

```java
public static void process(int[] pre, int L1, int R1,
                           int[] in, int L2, int R2,
                           int[] pos, int L3, int R3
                           ) {
    if (L1 > R1) {
        return;
    }
    if (L1 == R1) {
        pos[L3] = pre[L1];
        return;
    }
    pos[R3] = pre[L1];
    int mid = L2;
    for(; mid <= R2; mid++) {
        if (in[mid] == pre[L1]) {
            break;
        }
    }
    int leftSize = mid - L2;
    process(pre, L1 + 1, L1 + leftSize, in, L2, mid - 1, pos, L3, L3 + leftSize - 1);
    process(pre, L1 + leftSize + 1, R1, in, mid + 1, R2, pos, L3 + leftSize, R3 - 1);
}
```

优化

```java
public static void process1(int[] pre, int L1, int R1,
                           int[] in, int L2, int R2,
                           int[] pos, int L3, int R3,
                            HashMap<Integer, Integer> inMap
) {
    if (L1 > R1) {
        return;
    }
    if (L1 == R1) {
        pos[L3] = pre[L1];
        return;
    }
    pos[R3] = pre[L1];
    int mid = inMap.get(pre[L1]);
    int leftSize = mid - L2;
    process1(pre, L1 + 1, L1 + leftSize, in, L2, mid - 1, pos, L3, L3 + leftSize - 1, inMap);
    process1(pre, L1 + leftSize + 1, R1, in, mid + 1, R2, pos, L3 + leftSize, R3 - 1, inMap);
}

public static int[] f(int[] pre, int[] in) {
    HashMap<Integer, Integer> inMap = new HashMap<>();
    int N = in.length;
    for (int i = 0; i < N; i++) {
        inMap.put(in[i], i);
    }
    int[] pos = new int[N];
    process1(pre, 0, N - 1, in, 0, N - 1, pos, 0, N - 1, inMap);
    return pos;
}
```



## 题目二十二

最长递增子序列问题的O(N*logN)的解法

```java
public static int getDp1(int[] arr) {
    int N = arr.length;
    int[] dp = new int[N];
    dp[0] = 1;
    for (int i = 1; i < N; i++) {
        for (int j = 0; j < i; j++) {
            if (arr[j] < arr[i]) {
                dp[i] = Math.max(dp[i], dp[j]);
            }
        }
        dp[i]++;
    }
    return dp[N - 1];
}
```

```java
public static int[]  getDp2(int[] arr) {
    int[] dp = new int[arr.length];
    int[] ends = new int[arr.length];
    ends[0] = arr[0];
    dp[0] = 1;
    int right = 0;
    int l = 0;
    int r = 0;
    int m = 0;
    for (int i = 1; i < arr.length; i++) {
        l = 0;
        r = right;
        while (l <= r) {
            m = (l + r) / 2;
            if (arr[i] > ends[m]) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        right = Math.max(right, l);
        ends[l] = arr[i];
        dp[i] = l + 1;
    }
    return dp;
}
```



## 题目二十三

每个信封都有长和宽两个维度的数据，A信封如果想套在B信封里面，A信封必须在长和宽上都小于B信封才行。如果给你一批信封，返回最大的嵌套层数

```java
public static class Envelope {
    public int l;
    public int h;

    public Envelope(int l, int h) {
        this.l = l;
        this.h = h;
    }

    @Override
    public String toString() {
        return "Envelope{" +
                "l=" + l +
                ", h=" + h +
                '}';
    }
}

public static class EnvelopeComparator implements Comparator<Envelope> {

    @Override
    public int compare(Envelope o1, Envelope o2) {
        return o1.l == o2.l ? o1.h - o2.h : o1.l - o2.l;
    }

}

public static Envelope[] getSortedEnvelopes(int[][] matrix) {
    Envelope[] res = new Envelope[matrix.length];
    for (int i = 0; i < matrix.length; i++) {
        res[i] = new Envelope(matrix[i][0], matrix[i][1]);
    }
    Arrays.sort(res, new EnvelopeComparator());
    return res;
}

public static int maxEnvelopes(int[][] matrix) {
    Envelope[] envelopes = getSortedEnvelopes(matrix);
    int[] ends = new int[matrix.length];
    ends[0] = envelopes[0].h;
    int right = 0;
    int l = 0;
    int r = 0;
    int m = 0;
    for (int i = 0; i < envelopes.length; i++) {
        l = 0;
        r = right;
        while (l <= r) {
            m = (l + r) / 2;
            if (envelopes[i].h > ends[m]) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        right = Math.max(right, l);
        ends[l] = envelopes[i].h;
    }
    return right + 1;
}
```



## 题目二十四

给定一个数组arr,返回子数组的最大累加和。

```java
public static int maxSum(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int max = Integer.MIN_VALUE;
    int cur = 0;
    for (int i = 0; i < arr.length; i++) {
        cur += arr[i];
        max = Math.max(max, cur);
        cur = Math.max(cur, 0);
    }
    return max;
}
```



## 题目二十五

给定一个整型矩阵，返回子矩阵的最大累加和

```java
public static int maxSum(int[][] m) {
    if (m == null || m.length == 0 || m[0].length == 0) {
        return 0;
    }
    int max = Integer.MIN_VALUE;
    int[] sum = null;
    int cur = 0;
    for (int i = 0; i < m.length; i++) {
        sum = new int[m[0].length];
        for (int j = i; j < m.length; j++) {
            cur = 0;
            for (int k = 0; k < m[j].length; k++) {
                sum[k] += m[j][k];
                cur += sum[k];
                max = Math.max(max, cur);
                cur = Math.max(cur, 0);
            }
        }
    }
    return max;
}


public static int maxSum2(int[][] m) {
    if (m == null || m.length == 0 || m[0].length == 0) {
        return 0;
    }
    int max = Integer.MIN_VALUE;
    int[] sum = null;
    int cur = 0;
    boolean isColMax = m.length <= m[0].length;
    int N = isColMax ? m.length : m[0].length;
    int M = isColMax ? m[0].length : m.length;
    for (int i = 0; i < N; i++) {
        sum = new int[M];
        for (int j = i; j < N; j++) {
            cur = 0;
            for (int k = 0; k < M; k++) {
                sum[k] += isColMax ? m[j][k] : m[k][j];
                cur += sum[k];
                max = Math.max(max, cur);
                cur = Math.max(cur, 0);
            }
        }
    }
    return max;
}
```



## 题目二十六

双向链表节点结构和二叉树节点结构是一样的，如果你把last认为是left,next认为是next的话。给定一个搜索二叉树的头节点head,请转化成一条有序的双向链表，并返回链表的头节点。

```java
public static class Node {
    public int value;
    public Node left;
    public Node right;

    public Node(int value) {
        this.value = value;
    }
}

public static class Info {
    public Node start;
    public Node end;

    public Info(Node start, Node end) {
        this.start = start;
        this.end = end;
    }
}

public static Info process(Node X) {
    if (X == null) {
        return new Info(null, null);
    }
    Info leftInfo = process(X.left);
    Info rightInfo = process(X.right);
    if (leftInfo.end != null) {
        leftInfo.end.right = X;
    }
    X.left = leftInfo.end;
    X.right = rightInfo.start;
    if (rightInfo.start != null) {
        rightInfo.start.left = X;
    }
    return new Info(leftInfo.start != null ? leftInfo.start : X, rightInfo.end != null ? rightInfo.end : X);
}

public static Node treeToDoublyList(Node head) {
    if (head == null) {
        return null;
    }
    return process(head).start;
}
```



## 题目二十七

给定两个字符串str1和str2,再给定三个整数ic、dc和rc,分别代表插入、删除和替换一个字符的代价，返回将str1编辑成str2的最小代价。
【举例】
str1="abc",str2="adc",ic=5,dc=3,rc=2从"abc"编辑成"adc",把b替换成d'是代价最小的，所以返回2
strl="ahc",str2="adc",ic=5,dc=3,rc=100从"abc"编辑成"adc",先删除'b',然后插入'd'是代价最小的，所以返回8
str1="abc",str2="abc",ic=5,dc=3,rc=2不用编辑了，本来就是一样的字符串，所以返回0

```java
public static int minCost1(String s1, String s2, int ic, int dc, int rc) {
    if (s1 == null || s2 == null) {
        return 0;
    }
    char[] str1 = s1.toCharArray();
    char[] str2 = s2.toCharArray();
    int N = str1.length + 1;
    int M = str2.length + 1;
    int[][] dp = new int[N][M];
    for (int i = 1; i < N; i++) {
        dp[i][0] = i * dc;
    }
    for (int j = 0; j < M; j++) {
        dp[0][j] = j * ic;
    }

    for (int i = 1; i < N; i++) {
        for (int j = 1; j < M; j++) {
            if (str1[i - 1] == str2[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                dp[i][j] = dp[i - 1][j - 1] + rc;
            }
            dp[i][j] = Math.min(dp[i][j], dp[i][j - 1] + ic);
            dp[i][j] = Math.min(dp[i][j], dp[i - 1][j] + dc);
        }
    }
    return dp[N - 1][M - 1];
}
```



## 题目二十八

给定两个字符串s1和s2,问s2最少删除多少字符可以成为s1的子串？比如s1="abcde",s2="axbc" 返回1。s2删掉'x就是s1的子串了。

```java
public static class LenComp implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return o2.length() - o1.length();
    }
}

// 解法一
// 求出str2所有的子序列，然后按照长度排序，长度大的排在前面。
// 然后考察哪个子序列字符串和s1的某个子串相等(KMP)，答案就出来了。
// 分析：
// 因为题目原本的样本数据中，有特别说明s2的长度很小。所以这么做也没有太大问题，也几乎不会超时。
// 但是如果某一次考试给定的s2长度远大于s1，这么做就不合适了。
public static int minCost1(String s1, String s2) {
    List<String> s2Subs = new ArrayList<>();
    process(s2.toCharArray(), 0, "", s2Subs);
    s2Subs.sort(new LenComp());
    for (String str : s2Subs) {
        if (s1.contains(str)) {
            return s2.length() - str.length();
        }
    }
    return s2.length();
}

public static void process(char[] str2, int index, String path, List<String> list) {
    if (index == str2.length) {
        list.add(path);
        return;
    }
    process(str2, index + 1, path, list);
    process(str2, index + 1, path + str2[index], list);
}


// 解法二
// 生成所有s1的子串
// 然后考察每个子串和s2的编辑距离(假设编辑距离只有删除动作且删除一个字符的代价为1)
// 如果s1的长度较小，s2长度较大，这个方法比较合适
public static int minCost2(String s1, String s2) {
    int ans = Integer.MAX_VALUE;
    char[] str2 = s2.toCharArray();
    for (int start = 0; start < s1.length(); start++) {
        for (int end = start + 1; end <= s1.length(); end++) {
            ans = Math.min(ans, distance(str2, s1.substring(start, end).toCharArray()));
        }
    }
    return ans == Integer.MAX_VALUE ? s2.length() : ans;
}

private static int distance(char[] str2, char[] s1sub) {
    int row = str2.length;
    int col = s1sub.length;
    int[][] dp = new int[row][col];
    dp[0][0] = str2[0] == s1sub[0] ? 0 : Integer.MAX_VALUE;
    for (int j = 1; j < col; j++) {
        dp[0][j] = Integer.MAX_VALUE;
    }
    for (int i = 1; i < row; i++) {
        dp[i][0] = ((dp[i - 1][0] != Integer.MAX_VALUE) || str2[i] == s1sub[0]) ? i : Integer.MAX_VALUE;
    }
    for (int i = 1; i < row; i++) {
        for (int j = 1; j < col; j++) {
            dp[i][j] = Integer.MAX_VALUE;
            if (dp[i - 1][j] != Integer.MAX_VALUE) {
                dp[i][j] = dp[i - 1][j] + 1;
            }
            if (str2[i] == s1sub[j] && dp[i - 1][j - 1] != Integer.MAX_VALUE) {
                dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1]);
            }
        }
    }
    return dp[row - 1][col - 1];
}
```



## 题目二十八

求完全二叉树节点的个数  要求时间复杂度低于O(N)

//递归

```java
public static class Node {
    public TreeNode left;
    public TreeNode right;
}

public static int nodeNum(TreeNode head) {
    if (head == null) {
        return 0;
    }
    return bs(head, 1, mostLeftLevel(head, 1));
}

private static int bs(TreeNode node, int Level, int h) {
    if (Level == h) {
        return 1;
    }
    if (mostLeftLevel(node.right, Level + 1) == h) {
        return (1 << (h - Level)) + bs(node.right, Level + 1, h);
    }
    return (1 << (h - Level - 1)) + bs(node.left, Level + 1, h);
}

public static int mostLeftLevel(TreeNode node, int level) {
    while (node != null) {
        level++;
        node = node.left;
    }
    return level - 1;
}
```

//迭代

```java
public int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int level = 0;
        TreeNode node = root;
        while (node.left != null) {
            level++;
            node = node.left;
        }
        int low = 1 << level, high = (1 << (level + 1)) - 1;
        while (low < high) {
            int mid = (high - low + 1) / 2 + low;
            if (exists(root, level, mid)) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    public boolean exists(TreeNode root, int level, int k) {
        int bits = 1 << (level - 1);
        TreeNode node = root;
        while (node != null && bits > 0) {
            if ((bits & k) == 0) {
                node = node.left;
            } else {
                node = node.right;
            }
            bits >>= 1;
        }
        return node != null;
    }
```



## 题目二十九

LRU内存替换算法

```java
public static class Node<K, V> {
    public K key;
    public V value;
    public Node<K, V> last;
    public Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

public static class NodeDoubleLinkedList<K, V> {
    private Node<K, V> head;
    private Node<K, V> tail;

    //往尾部添加节点
    public void addNode(Node<K, V> newNode) {
        if (newNode == null) {
            return;
        }
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.last = tail;
            tail = newNode;
        }
    }

    //将节点移动到尾部
    public void moveNodeToTail(Node<K, V> node) {
        if (this.tail == node) {
            return;
        }
        if (this.head == node) {
            this.head = node.next;
            this.head.last = null;
        } else {
            node.last.next = node.next;
            node.next.last = node.last;
        }
        node.last = this.tail;
        node.next = null;
        this.tail.next = node;
        this.tail = node;
    }

    //把头结点删除并返回
    public Node<K, V> removeHead() {
        if (this.head == null) {
            return null;
        }
        Node<K, V> res = this.head;
        if (this.head == this.tail) {
            this.head = null;
            this.tail = null;
        } else {
            this.head = res.next;
            res.next = null;
            this.head.last = null;
        }
        return res;
    }
}


public static class MyCache<K, V> {
    private HashMap<K, Node<K, V>> keyNodeMap;
    private NodeDoubleLinkedList<K, V> nodeList;
    private final int capacity;

    public MyCache(int capacity) {
        if (capacity < 1) {
            throw new RuntimeException("The capacity should be more than 0.");
        }
        this.capacity = capacity;
        this.keyNodeMap = new HashMap<>();
        this.nodeList = new NodeDoubleLinkedList<>();
    }


    public V get(K key) {
        if (keyNodeMap.containsKey(key)) {
            Node<K, V> res = keyNodeMap.get(key);
            nodeList.moveNodeToTail(res);
            return res.value;
        }
        return null;
    }

    public void set(K key, V value) {
        if (keyNodeMap.containsKey(key)) {
            Node<K, V> node = keyNodeMap.get(key);
            node.value = value;
            nodeList.moveNodeToTail(node);
        } else {
            if (keyNodeMap.size() == capacity) {
                removeMostUnusedCache();
            }
            Node<K, V> newNode = new Node<>(key, value);
            keyNodeMap.put(key, newNode);
            nodeList.addNode(newNode);
        }
    }
    private void removeMostUnusedCache() {
        Node<K, V> removeNode = nodeList.removeHead();
        keyNodeMap.remove(removeNode.key);
    }
}
```



## 题目三十

给定两个字符串，记为stat和to,再给定一个字符串列表list,list中一定包含to list中没有重复字符串，所有的字符串都是小写的。
规定：start每次只能改变一个字符，最终的目标是彻底变成to,但是每次变成的新字符串必须在list中存在。请返回所有最短的变换路径。
【举例】
start="abc",end="cab"list=("cab","acc","cbc""ccc","cac"."cbb","aab","abb")
转换路径的方法有很多种，但所有最短的转换路径如下：
abc ->abb->aab->cab
abc ->abb->cbb->cab
abc ->cbc->cac ->cab
abc->cbc ->cbb->cab

```java
public static void main(String[] args) {
    List<String> list = new ArrayList<>(Arrays.asList("cab","acc","cbc","ccc","cac","cbb","aab","abb"));
    List<List<String>> minPaths = findMinPaths("abc", "cab", list);
    System.out.println(minPaths);
}

public static List<List<String>> findMinPaths(String start, String end, List<String> list) {
     list.add(start);
     HashMap<String, List<String>> nexts = getNexts(list);
     HashMap<String, Integer> distances = getDistances(start, nexts);
     LinkedList<String> pathList = new LinkedList<>();
     List<List<String>> res = new ArrayList<>();
     getShortestPaths(start, end, nexts, distances, pathList, res);
     return res;
}
//DFS收集答案
private static void getShortestPaths(
        String cur,
        String to,
        HashMap<String, List<String>> nexts,
        HashMap<String, Integer> distances,
        LinkedList<String> path,
        List<List<String>> res) {
    path.add(cur);
    if (to.equals(cur)) {
        res.add(new LinkedList<>(path));
    } else {
        for (String next : nexts.get(cur)) {
            if (distances.get(next) == distances.get(cur) + 1) {
                getShortestPaths(next, to, nexts, distances, path, res);
            }
        }
    }
    path.pollLast();
}
//BFS
//获取其他字符串到开始串的距离
private static HashMap<String, Integer> getDistances(String start, HashMap<String, List<String>> nexts) {
    HashMap<String, Integer> distances = new HashMap<>();
    distances.put(start, 0);
    Queue<String> queue = new LinkedList<>();
    HashSet<String> set = new HashSet<>();
    set.add(start);
    queue.add(start);
    while (!queue.isEmpty()) {
        String cur = queue.poll();
        for (String next : nexts.get(cur)) {
            if (!set.contains(next)) {
                set.add(next);
                queue.add(next);
                distances.put(next, distances.get(cur) + 1);
            }
        }
    }
    return distances;
}
//获取每个字符串的邻居字符串
private static HashMap<String, List<String>> getNexts(List<String> words) {
    Set<String> dict = new HashSet<>(words);
    HashMap<String, List<String>> nexts = new HashMap<>();
    for (int i = 0; i < words.size(); i++) {
        nexts.put(words.get(i), getNext(words.get(i), dict));
    }
    return nexts;
}

private static List<String> getNext(String word, Set<String> dict) {
    List<String> res = new ArrayList<>();
    char[] chs = word.toCharArray();
    for (int i = 0; i < chs.length; i++) {
        for (char cur = 'a'; cur < 'z'; cur++) {
            if (chs[i] != cur) {
                char temp = chs[i];
                chs[i] = cur;
                if (dict.contains(String.valueOf(chs))) {
                    res.add(String.valueOf(chs));
                }
                chs[i] = temp;
            }
        }
    }
    return res;
}
```



## 题目三十一

一个数组的异或和是指数组中所有的数异或在一起的结果，给定一个数组arr,求最大子数组异或和。

```java
public static int maxXorSubArray1(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int[] eor = new int[arr.length];
    eor[0] = arr[0];
    for (int i = 1; i < arr.length; i++) {
        eor[i] = eor[i - 1] ^ arr[i];
    }
    int max = Integer.MIN_VALUE;
    for (int j = 0; j < arr.length; j++) {
        for (int i = 0; i <= j; i++) {
            max = Math.max(max, i == 0 ? eor[j] : eor[j] ^ eor[i - 1]);
        }
    }
    return max;
}

//利用前缀树做贪心
public static class Node {
    public Node[] nexts = new Node[2];
}

public static class NumTrie {
    public Node head = new Node();

    public void add(int num) {
        Node cur = head;
        for (int move = 31; move >= 0; move--) {
            int path = (num >> move) & 1;
            cur.nexts[path] = cur.nexts[path] == null ? new Node() : cur.nexts[path];
            cur = cur.nexts[path];
        }
    }
    public int maxXor(int sum) {
        Node cur = head;
        int res = 0;
        for (int move = 31; move >= 0; move--) {
            int path = (sum >> move) & 1;
            int best = move == 31 ? path : (path ^ 1);
            best = cur.nexts[best] != null? best : (best ^ 1);
            res |= (best ^ path) << move;
            cur = cur.nexts[best];
        }
        return res;
    }
}

public static int maxXorSubArray2(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int eor = 0;
    int max = Integer.MIN_VALUE;
    NumTrie numTrie = new NumTrie();
    numTrie.add(0);
    for (int i = 0; i < arr.length; i++) {
        eor = eor ^ arr[i];
        max = Math.max(max, numTrie.maxXor(eor));
        numTrie.add(eor);
    }
    return max;
}
```



## 题目三十二

将数组arr划分为多段，使得子数组的异或和为0的子数组数量尽量多，返回子数组最多是多少

```java
public int mostEOR(int[] arr) {
    int sum = 0;
    int N = arr.length;
    int[] dp = new int[N];
    HashMap<Integer, Integer> map = new HashMap<>();
    map.put(0, -1);
    for (int i = 0; i < N; i++) {
        sum ^= arr[i];
        if (map.containsKey(sum)) {
            int pre = map.get(sum);
            dp[i] = pre == -1 ? 1 : dp[pre] + 1;
        }
        if (i > 0) {
            dp[i] = Math.max(dp[i - 1], dp[i]);
        }
        map.put(sum, i);
    }
    return dp[N - 1];
}
```



## 题目三十三

给定一个只由0（假）、1（真）、&（逻辑与）、|（逻辑或）和∧（异或）五种字符组成的字符串express,.再给定一个布尔值desired。返回express能有多少种组合方式，可以达到desired的结果。
【举例】
express="1^0|0|1",desired=false  只有1^((0|0)|1)和1^(0|(0|1))的组合可以得到false,返回2。express="1",desired=false 无组合则可以得到false,返回0

递归

```java
public static int num1(String express, boolean desired) {
    if (express == null || express.equals("")) {
        return 0;
    }
    char[] exp = express.toCharArray();
    if (!isValid(exp)) {
        return 0;
    }
    return process(exp, desired, 0, exp.length - 1);
}

private static int process(char[] str, boolean desired, int L, int R) {
    if (L == R) {
        if (str[L] == '1') {
            return desired ? 1 : 0;
        } else {
            return desired ? 0 : 1;
        }
    }
    int res = 0;
    if (desired) {
        for (int i = L + 1; i < R; i = i + 2) {
            switch (str[i]) {
                case '&' :
                    res += process(str, true, L, i - 1) * process(str, true, i + 1, R);
                    break;
                case '|' :
                    res += process(str, true, L, i - 1) * process(str, true, i + 1, R);
                    res += process(str, false, L, i - 1) * process(str, true, i + 1, R);
                    res += process(str, true, L, i - 1) * process(str, false, i + 1, R);
                    break;
                case '^' :
                    res += process(str, true, L, i - 1) * process(str, false, i + 1, R);
                    res += process(str, false, L, i - 1) * process(str, true, i + 1, R);
                    break;
            }
        }
    } else {
        for (int i = L + 1; i < R; i = i + 2) {
            switch (str[i]) {
                case '&' :
                    res += process(str, false, L, i - 1) * process(str, true, i + 1, R);
                    res += process(str, true, L, i - 1) * process(str, false, i + 1, R);
                    res += process(str, false, L, i - 1) * process(str, false, i + 1, R);
                    break;
                case '|' :
                    res += process(str, false, L, i - 1) * process(str, false, i + 1, R);
                    break;
                case '^' :
                    res += process(str, true, L, i - 1) * process(str, true, i + 1, R);
                    res += process(str, false, L, i - 1) * process(str, false, i + 1, R);
                    break;
            }
        }
    }
    return res;
}

private static boolean isValid(char[] exp) {
    if ((exp.length & 1) == 0) {
        return false;
    }
    for (int i = 0; i < exp.length; i = i + 2) {
        if ((exp[i] != '1') && (exp[i] != '0')) {
            return false;
        }
    }
    for (int i = 1; i < exp.length; i = i + 2) {
        if ((exp[i] != '&') && (exp[i] != '|') && (exp[i] != '^')) {
            return false;
        }
    }
    return true;
}
```

递归改动态规划

```java
public static int dpLive(String express, boolean desired) {
    char[] str = express.toCharArray();
    int N = str.length;
    int[][] tMap = new int[N][N];
    int[][] fMap = new int[N][N];
    for (int i = 0; i < N; i = i + 2) {
        tMap[i][i] = str[i] == '1' ? 1 : 0;
        fMap[i][i] = str[i] == '0' ? 1 : 0;
    }
    for (int row = N - 3; row >= 0; row -= 2) {
        for (int col = row + 2; col < N; col += 2) {
            for (int i = row + 1; i < col; i += 2) {
                switch (str[i]) {
                    case '&' :
                        tMap[row][col] += tMap[row][i - 1] * tMap[i + 1][col];
                        break;
                    case '|' :
                        tMap[row][col] += tMap[row][i - 1] * tMap[i + 1][col];
                        tMap[row][col] += fMap[row][i - 1] * tMap[i + 1][col];
                        tMap[row][col] += tMap[row][i - 1] * fMap[i + 1][col];
                        break;
                    case '^' :
                        tMap[row][col] += tMap[row][i - 1] * fMap[i + 1][col];
                        tMap[row][col] += fMap[row][i - 1] * tMap[i + 1][col];
                        break;
                }
                switch (str[i]) {
                    case '&' :
                        fMap[row][col] += tMap[row][i - 1] * fMap[i + 1][col];
                        fMap[row][col] += fMap[row][i - 1] * tMap[i + 1][col];
                        fMap[row][col] += fMap[row][i - 1] * fMap[i + 1][col];
                        break;
                    case '|' :
                        fMap[row][col] += fMap[row][i - 1] * fMap[i + 1][col];
                        break;
                    case '^' :
                        fMap[row][col] += tMap[row][i - 1] * tMap[i + 1][col];
                        fMap[row][col] += fMap[row][i - 1] * fMap[i + 1][col];
                        break;
                }
            }
        }
    }
    return desired ? tMap[0][N - 1] : fMap[0][N - 1];
}
```



## 题目三十四

给出一组正整数arr,你从第0个数向最后一个数，每个数的值表示你从这个位置可以向右跳跃的最大长度，计算如何以最少的跳跃次数跳到最后一个数。  

```java
public static int jump(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int step = 0;
    int cur = 0;
    int next = -1;
    for (int i = 0; i < arr.length; i++) {
        if (cur < i) {
            step++;
            cur = next;
        }
        next = Math.max(next, i + arr[i]);
    }
    return step;
}
```



## 题目三十五

给定字符串str，将str分割成多份，使得每一份都是回文串，最少砍多少刀

```java
public static int minParts(String s) {
    if (s == null || s.length() == 0) {
        return 0;
    }
    if (s.length() == 1) {
        return 1;
    }
    char[] str = s.toCharArray();
    int N = str.length;
    boolean[][] isP = new boolean[N][N];
    for (int i = 0; i < N; i++) {
        isP[i][i] = true;
    }
    for (int i = 0; i < N - 1; i++) {
        isP[i][i + 1] = str[i] == str[i + 1];
    }

    for (int row = N - 3; row >= 0; row--) {
        for (int col = row + 2; col < N; col++) {
            isP[row][col] = str[row] == str[col] && isP[row + 1][col - 1];
        }
    }
    int[]  dp = new int[N + 1];
    for (int i = 0; i <= N; i++) {
        dp[i] = Integer.MAX_VALUE;
    }
    dp[N] = 0;
    for (int i = N - 1; i >= 0; i--) {
        for (int end = i; end < N; end++) {
            if (isP[i][end]) {
                dp[i] = Math.min(dp[i], dp[end + 1] + 1);
            }
        }
    }
    return dp[0];
}
```



## 题目三十六

给定两个有序数组arr1和arr2,再给定一个正数K，求两个数累加和最大的前K个，两个数必须分别来自arr1和arr2

```java
public static class Node {
    public int index1;
    public int index2;
    public int sum;

    public Node(int index1, int index2, int sum) {
        this.index1 = index1;
        this.index2 = index2;
        this.sum = sum;
    }
}

public static class MaxHeapComp implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        return o2.sum - o1.sum;
    }

}

public static int[] topKSum(int[] arr1, int[] arr2, int topK) {
    if (arr1 == null || arr2 == null || topK < 1) {
        return null;
    }
    topK = Math.min(topK, arr1.length + arr2.length);
    int[] res = new int[topK];
    int resIndex = 0;
    PriorityQueue<Node> maxHeap = new PriorityQueue<>(new MaxHeapComp());
    boolean[][] set = new boolean[arr1.length][arr2.length];
    int i1 = arr1.length - 1;
    int i2 = arr2.length - 1;
    maxHeap.add(new Node(i1, i2, arr1[i1] * arr2[i2]));
    set[i1][i2] = true;
    while (resIndex != topK) {
        Node cur = maxHeap.poll();
        res[resIndex++] = cur.sum;
        i1 = cur.index1;
        i2 = cur.index2;
        if (i1 - 1 >= 0 && !set[i1 - 1][i2]) {
            set[i1 - 1][i2] = true;
            maxHeap.add(new Node(i1 - 1, i2, arr1[i1 - 1] * arr2[i2]));
        }
        if (i2 - 1 >= 0 && !set[i1][i2 - 1]) {
            set[i1][i2 - 1] = true;
            maxHeap.add(new Node(i1, i2 - 1, arr1[i1] * arr2[i2 - 1]));
        }
    }
    return res;
}
```



## 题目三十七

给定一个正数数组arr,返回该数组能不能分成4个部分，并且每个部分的累加和相等，切分位置的数不要。
例如：arr=[3,2,4,1,4,9,5,10,1,2,2] 返回true，三个切割点下标为2,5,7。切出的四个子数组为[3,2]，[1,4]，[5]，[1,2,2]累加和都是5

```java
public static boolean splitArr(int[] arr) {
    if (arr == null || arr.length < 7) {
        return false;
    }
    int N = arr.length;
    int[] preSum = new int[N];
    int sum = 0;
    for (int i = 0; i < N; i++) {
        sum = sum + arr[i];
        preSum[i] = sum;
    }
    for (int i = 1; i < N - 6; i++) {
        int firstSum = preSum[i] - arr[i];
        for (int j = i + 2; j < N - 4; j++) {
            int secondSum = preSum[j] - preSum[i] - arr[j];
            if (firstSum != secondSum) {
                continue;
            }
            for (int k = j + 2; k < N - 2; k++) {
                int thirdSum = preSum[k] -preSum[j] - arr[k];
                int fourSum = sum - preSum[k] - arr[N - 1];
                if (secondSum != thirdSum && secondSum != fourSum) {
                    continue;
                }
                return true;
            }
        }
    }
    return false;
}
```



```java
public static boolean canSplit2(int[] arr) {
    if (arr == null || arr.length < 7) {
        return false;
    }
    HashMap<Integer, Integer> map = new HashMap<>();
    int sum = arr[0];
    for (int i = 1; i < arr.length; i++) {
        map.put(sum, i);
        sum += arr[i];
    }
    int lsum = arr[0];
    for (int s1 = 1; s1 < arr.length - 5; s1++) {
        int checkSum = lsum * 2 + arr[s1];
        if (map.containsKey(checkSum)) {
            int s2 = map.get(checkSum);
            checkSum += lsum + arr[s2];
            if (map.containsKey(checkSum)) {
                int s3 = map.get(checkSum);
                if (checkSum + arr[s3] + lsum == sum) {
                    return true;
                }
            }
        }
        lsum += arr[s1];
    }
    return false;
}
```

## 题目三十八

给定三个字符串str1、str2和aim,如果aim包含且仅包含来自str1和str2的所有字符，而且在aim中属于str1的字符之间保持原来在str1中的顺序，属于str2的字符之间保持原来在str2中的顺序，那么称aim是str1和str2的交错组成。实现一个函数，判断aim是否是str1和str2交错组成
【举例】str1="AB",Str2="12"。那么"AB12"、"A1B2"、"A12B"、"1A2B"和 "1AB2"等都是str1和str2的交错组成

```java
public static boolean isCross(String s1, String s2, String aim) {
    if (s1 == null || s2 == null || aim == null) {
        return false;
    }
    char[] str1 = s1.toCharArray();
    char[] str2 = s2.toCharArray();
    char[] aims = aim.toCharArray();
    int N = str1.length;
    int M = str2.length;
    if (N + M != aim.length()) {
        return false;
    }
    boolean[][] dp = new boolean[N + 1][M + 1];
    dp[0][0] = true;
    for (int i = 1; i <= N; i++) {
        if (str1[i - 1] != aims[i - 1]) {
            break;
        }
        dp[i][0] = true;
    }
    for (int j = 1; j <= M; j++) {
        if (str2[j - 1] != aims[j - 1]) {
            break;
        }
        dp[0][j] = true;
    }
    for (int i = 1; i <= N; i++) {
        for (int j = 1; j <= M; j++) {
            dp[i][j] = (str1[i - 1] == aims[i + j - 1] && dp[i - 1][j]) || (str2[j - 1] == aims[i + j - 1] && dp[i][j - 1]);
        }
    }
    return dp[N][M];
}
```



## 题目三十九

给定一个无序数组arr,如果只能再一个子数组上排序, 返回如果让arr整体有序，需要排序的最短子数组长度

```java
public static int getMinLength(int[] arr) {
    if (arr == null || arr.length < 2) {
        return 0;
    }
    int min = arr[arr.length - 1];
    int noMinIndex = -1;
    for (int i = arr.length - 2; i >= 0; i--) {
        if (arr[i] > min) {
            noMinIndex = i;
        } else {
            min = Math.min(min, arr[i]);
        }
    }
    if (noMinIndex == -1) {
        return 0;
    }
    int max = arr[0];
    int noMaxIndex = -1;
    for (int i = 1; i < arr.length; i++) {
        if (arr[i] < max) {
            noMaxIndex = i;
        } else {
            max = Math.max(max, arr[i]);
        }
    }
    return noMaxIndex - noMinIndex + 1;
}
```



## 题目四十

给定一个有序的正数数组arr和一个正数aim,如果可以自由选择arr中的数字，想累加得到1~aim范围上所有的数，返回arr最少还缺几个数。
【举例】

arr={1,2,3,7}, aim 15 想累加得到1~15范围上所有的数，arr还缺14这个数，所以返回1
arr={1,5,7,aim=15 想累加得到1~15范围上所有的数，arr还缺2和4，所以返回2

```java
public static int minPatches(int[] arr, int aim) {
    int patches = 0;
    long range = 0;
    Arrays.sort(arr);
    for (int i = 0; i < arr.length; i++) {
        while (arr[i] > range + 1) {
            range += range + 1;
            patches++;
            if (range >= aim) {
                return patches;
            }
        }
        range += arr[i];
        if (range >= aim) {
            return patches;
        }
    }
    while (aim >= range + 1) {
        range += range + 1;
        patches++;
    }
    return patches;
}
```



## 题目四十一

在一个字符串中找到没有重复字符子串中最长的长度。例如：abcabcbb没有重复字符的最长子串是abc,长度为3，bbbbb,答案是b,长度为1，pwwkew,答案是wke,长度是3
要求：答案必须是子串，"pwke"是一个子字符序列但不是一个子字符串。

```java
public static int getNoRepeatMaxLength(String s) {
    if (s == null || s.length() == 0) {
        return 0;
    }
    char[] str = s.toCharArray();
    int[] map = new int[256];
    for (int i = 0; i < 256; i++) {
        map[i] = -1;
    }
    map[str[0]] = 0;
    int pre = 1;
    int max = 1;
    for (int i = 1; i < str.length; i++) {
        pre = Math.min(i - map[str[i]], pre + 1);
        map[str[i]] = i;
        max = Math.max(max, pre);
    }
    return max;
}
```



## 题目四十二

一个数组中，如果两个数的最小公共因子大于1，则认为这两个数之间有通路，返回数组中最大的域

```java
public static int gcd(int a, int b) {
    return b == 0 ? a : gcd(b, a % b);
}

public static class UnionFind {
    private int[] parents;
    private int[] sizes;
    private int[] help;

    public UnionFind(int N) {
        parents = new int[N];
        sizes = new int[N];
        help = new int[N];
        for (int i = 0; i < N; i++) {
            parents[i] = i;
            sizes[i] = 1;
        }
    }

    public int maxSize() {
        int ans = 0;
        for (int size : sizes) {
            ans = Math.max(ans, size);
        }
        return ans;
    }

    public int find(int i) {
        int hi = 0;
        while (i != parents[i]) {
            help[hi++] = i;
            i = parents[i];
        }
        for (hi--; hi >= 0; hi--) {
            parents[help[hi]] = i;
        }
        return i;
    }

    public void union(int i, int j) {
        int f1 = find(i);
        int f2 = find(j);
        if (f1 != f2) {
            int big = sizes[f1] >= sizes[f2] ? f1 : f2;
            int small = big == f1 ? f2 : f1;
            parents[small] = big;
            sizes[big] = sizes[f1] + sizes[f2];
        }
    }
}


public static int largestComponentSize1(int[] arr) {
    int N = arr.length;
    UnionFind uf = new UnionFind(N);
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            if (gcd(arr[i], arr[j]) != 1) {
                uf.union(i, j);
            }
        }
    }
    return uf.maxSize();
}

public static int largestComponentSize2(int[] arr) {
    int N = arr.length;
    UnionFind uf = new UnionFind(N);
    HashMap<Integer, Integer> fatorsMap = new HashMap<>();
    for (int i = 0; i < N; i++) {
        int num = arr[i];
        int limit = (int)Math.sqrt(num);
        for (int j = 1; j <= limit; j++) {
            if (num % j == 0) {
                if (j != 1) {
                    if (!fatorsMap.containsKey(j)) {
                        fatorsMap.put(j, i);
                    } else {
                        uf.union(fatorsMap.get(j), i);
                    }
                }
                int other = num / j;
                if (other != 1) {
                    if (!fatorsMap.containsKey(other)) {
                        fatorsMap.put(other, i);
                    } else {
                        uf.union(fatorsMap.get(other), i);
                    }
                }
            }

        }
    }
    return uf.maxSize();
}
```



## 题目四十三

给定一个全是小写字母的字符串s，删除多余字符，使得每种字符只保留一个，并让最终结果字符串的字典序最小【举例】
str="acbc",删掉第一个'c',得到"abc",是所有结果字符串中字典序最小的。
str="dbcacbca", 删掉第一个b'、第一个'c'、第二个'c'、第二个'a',得到"dabc",是所有结果字符串中字典序最小的。

```java
public static String remove(String str) {
    if (str == null || str.length() < 2) {
        return str;
    }
    int[] map = new int[256];
    for (int i = 0; i < str.length(); i++) {
        map[str.charAt(i)]++;
    }
    int minACSIndex = 0;
    for (int i = 0; i < str.length(); i++) {
        minACSIndex = str.charAt(minACSIndex) > str.charAt(i) ? i : minACSIndex;
        if (--map[str.charAt(i)] == 0) {
            break;
        }
    }
    return str.charAt(minACSIndex) + remove(str.substring(minACSIndex + 1).replaceAll(String.valueOf(str.charAt(minACSIndex)), ""));
}
```

优化

```java
public String removeDuplicateLetters(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }
        int[] map = new int[26];
        char[] str = s.toCharArray();
        for (char c : str) {
            map[c - 'a']++;
        }
        char[] res = new char[26];
        int L = 0;
        int index = 0;
        while (L < str.length) {
            if (map[str[L] - 'a'] == -1) {
                L++;
                continue;
            }
            int minACSIndex = L;
            int i = L;
            for (; i < str.length; i++) {
                if (map[str[i] - 'a'] != -1) {
                    minACSIndex = str[minACSIndex] > str[i] ? i : minACSIndex;
                    if (--map[str[i] - 'a'] == 0) break;
                }
            }
            for (int j = minACSIndex + 1; j <= i; j++) {
                if (map[str[j] - 'a'] != -1) {
                    map[str[j] - 'a']++;
                }
            }
            res[index++] = str[minACSIndex];
            map[str[minACSIndex] - 'a'] = -1;
            L = minACSIndex + 1;
        }
        return String.valueOf(res, 0, index);
    }
```



## 题目四十四

给定两个数组arrx和arry,长度都为N。代表二维平面上有N个点，第i个点的x坐标和y坐标分别为arr[x]和arr[y],返回求一条直线最多能穿过多少个点？

```java
public static class Point{
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}


public static int maxPoints(Point[] points) {
    if (points == null) {
        return 0;
    }
    if (points.length <= 2) {
        return points.length;
    }
    Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
    int result = 0;
    for (int i = 0; i < points.length; i++) {
        map.clear();
        int samePosition = 1;
        int sameX = 0;
        int sameY = 0;
        int line = 0;
        for (int j = i; j < points.length; j++) {
            int x = points[j].x - points[i].x;
            int y = points[j].y - points[i].y;
            if (x == 0 && y == 0) {
                samePosition++;
            } else if (x == 0) {
                sameX++;
            } else if (y == 0) {
                sameY++;
            } else {
                int gcd = gcd(x, y);
                x /= gcd;
                y /= gcd;
                if (!map.containsKey(x)) {
                    map.put(x, new HashMap<>());
                }
                if (!map.get(x).containsKey(y)) {
                    map.get(x).put(y, 0);
                }
                map.get(x).put(y, map.get(x).get(y) + 1);
                line = Math.max(line, map.get(x).get(y));
            }
        }
        result = Math.max(result, Math.max(line, Math.max(sameX, sameY)) + samePosition);
    }
    return result;
}
```



## 题目四十五

int[] d,d[i]:i号怪兽的能力
int[] p,p[i]：i号怪兽要求的钱
开始时你的能力是0，你的目标是从0号怪兽开始，通过所有的怪兽。如果你当前的能力，小于号怪兽的能力，你必须付出p的钱，贿赂这个怪兽，然后怪兽就会加入你，他的能力直接累加到你的能力上；如果你当前的能力，大于等于号怪兽的能力，你可以选择直接通过，你的能力并不会下降，你也可以选择贿赂这个怪兽，然后怪兽就会加入你，他的能力直接累加到你的能力上。返回通过所有的怪兽，需要花的最小钱数。

基于能力进行暴力递归

```java
public static long process(int[] d, int[] p, int ability, int index) {
    if (index == d.length) {
        return 0;
    }
    if (ability < d[index]) {
        return p[index] + process(d, p, ability + d[index], index + 1);
    }
    return Math.min(p[index] + process(d, p, ability + d[index], index + 1),
            process(d, p, ability, index + 1));
}

public static long func1(int[] d, int[] p) {
    return process(d, p, 0, 0);
}
```

基于钱进行暴力递归

```java
public static long process1(int[] d, int[] p, int index, int money) {
    if (index == -1) {
        return money == 0 ? 0 : -1;
    }
    long preMaxAbility = process1(d, p, index - 1, money);
    long p1 = -1;
    if (preMaxAbility != -1 && preMaxAbility >= d[index]) {
        p1 = preMaxAbility;
    }
    long preMaxAbility2 = process1(d, p, index - 1, money - p[index]);
    long p2 = -1;
    if (preMaxAbility2 != -1) {
        p2 = d[index] + preMaxAbility2;
    }
    return Math.max(p1, p2);
}

public static long fun2(int[] d, int[] p) {
    int allMoney = 0;
    for (int i = 0; i < p.length; i++) {
        allMoney += p[i];
    }
    for (int money = 0; money < allMoney; money++) {
        if (process1(d, p, d.length - 1, money) != -1) {
            return money;
        }
    }
    return allMoney;
}
```

基于能力做动态规划

```java
public static long dp1(int[] d, int[] p) {
    int sum = 0;
    for (int num : d) {
        sum += num;
    }
    long[][] dp = new long[d.length + 1][sum + 1];
    for (int cur = d.length - 1; cur >= 0; cur--) {
        for (int hp = 0; hp <= sum; hp++) {
            if (hp + d[cur] > sum) {
                continue;
            }
            if (hp < d[cur]) {
                dp[cur][hp] = p[cur] + dp[cur + 1][hp + d[cur]];
            } else {
                dp[cur][hp] = Math.min(dp[cur + 1][hp], p[cur] + dp[cur + 1][hp + d[cur]]);
            }
        }
    }
    return dp[0][0];
}
```

基于钱做动态规划

```java
public static long dp2(int[] d, int[] p) {
    int allMoney = 0;
    for (int m : p) {
        allMoney += m;
    }
    int[][] dp = new int[d.length][allMoney + 1];
    for (int i = 0; i < dp.length; i++) {
        for (int j = 0; j <= allMoney; j++) {
            dp[i][j] = -1;
        }
    }
    dp[0][p[0]] = d[0];
    for (int i = 1; i < d.length; i++) {
        for (int j = 0; j <= allMoney; j++) {
            if (j >= p[i] && dp[i - 1][j - p[i]] != -1) {
                dp[i][j] = dp[i - 1][j - p[i]];
            }
            if (dp[i - 1][j] >= d[i]) {
                dp[i][j] = Math.max(dp[i][j], dp[i - 1][j]);
            }
        }
    }
    int ans = 0;
    for (int j = 0; j <= allMoney; j++) {
        if (dp[d.length - 1][j] != -1) {
            ans = j;
            break;
        }
    }
    return ans;
}
```



## 题目四十六

给定一个字符串，如果可以在字符串任意位置添加字符，最少添加几个能让字符串整体都是回文串。

```java
public static int fun(String s) {
    if (s == null || s.length() == 1) {
        return 0;
    }
    char[] str = s.toCharArray();
    int N = str.length;
    int[][] dp = new int[N][N];
    for (int i = 0; i < N - 1; i++) {
        dp[i][i + 1] = str[i] == str[i + 1] ? 0 : 1;
    }
    for (int i = N - 3; i >= 0; i--) {
        for (int j = i + 2; j < N; j++) {
            dp[i][j] = Integer.MAX_VALUE;
            if (str[i] == str[j]) {
                dp[i][j] = dp[i + 1][j - 1];
            }
            dp[i][j] = Math.min(dp[i][j], dp[i + 1][j] + 1);
            dp[i][j] = Math.min(dp[i][j], dp[i][j - 1] + 1);
        }
    }
    return dp[0][N - 1];
}
```



## 题目四十七

一种消息接收并打印的结构设计
已知一个消息流会不断地吐出整数1~N,但不一定按照顺序吐出。如果上次打印的数为i，那么当i+1出现时，请打印i+1及其之后接收过的并且连续的所有数，直到1~N全部接收并打印完，请设计这种接收并打印的结构。初始时默认i==0

```java
public static class Node {
    public String info;
    public Node next;

    public Node(String info) {
        this.info = info;
    }
}

public static class MessageBox {
    private HashMap<Integer, Node> headMap;
    private HashMap<Integer, Node> tailMap;
    private int waitPoint;

    public MessageBox() {
        headMap = new HashMap<>();
        tailMap = new HashMap<>();
        waitPoint = 1;
    }

    public void receive(int num, String info) {
        if (num < 1) {
            return;
        }
        Node cur = new Node(info);
        headMap.put(num, cur);
        tailMap.put(num, cur);

        if (tailMap.containsKey(num - 1)) {
            tailMap.get(num - 1).next = cur;
            tailMap.remove(num - 1);
            headMap.remove(num);
        }
        if (headMap.containsKey(num + 1)) {
            cur.next = headMap.get(num + 1);
            tailMap.remove(num);
            headMap.remove(num + 1);
        }
        if (num == waitPoint) {
            print();
        }
    }

    public void print() {
        Node cur = headMap.get(waitPoint);
        headMap.remove(waitPoint);
        while (cur != null) {
            System.out.print(cur.info + " ");
            cur = cur.next;
            waitPoint++;
        }
        tailMap.remove(waitPoint - 1);
        System.out.println();
    }
}

public static void main(String[] args) {
    MessageBox messageBox = new MessageBox();
    int[] arr = {2,3,5,4,7,8,1,6,9};
    for (int i = 0; i < arr.length; i++) {
        messageBox.receive(arr[i], String.valueOf(arr[i]));
    }
```

## 题目四十八

现有n1+n2种面值的硬币，其中前n1种为普通币，可以取任意枚，后n2种为纪念币，每种最多只能取一枚，每种硬币有一个面值，问能用多少种方法拼出m的面值？



```java
public static boolean canSplit2(int[] arr) {
    if (arr == null || arr.length < 7) {
        return false;
    }
    HashMap<Integer, Integer> map = new HashMap<>();
    int sum = arr[0];
    for (int i = 1; i < arr.length; i++) {
        map.put(sum, i);
        sum += arr[i];
    }
    int lsum = arr[0];
    for (int s1 = 1; s1 < arr.length - 5; s1++) {
        int checkSum = lsum * 2 + arr[s1];
        if (map.containsKey(checkSum)) {
            int s2 = map.get(checkSum);
            checkSum += lsum + arr[s2];
            if (map.containsKey(checkSum)) {
                int s3 = map.get(checkSum);
                if (checkSum + arr[s3] + lsum == sum) {
                    return true;
                }
            }
        }
        lsum += arr[s1];
    }
    return false;
}
```

## 题目三十八

给定三个字符串str1、str2和aim,如果aim包含且仅包含来自str1和str2的所有字符，而且在aim中属于str1的字符之间保持原来在str1中的顺序，属于str2的字符之间保持原来在str2中的顺序，那么称aim是str1和str2的交错组成。实现一个函数，判断aim是否是str1和str2交错组成
【举例】str1="AB",Str2="12"。那么"AB12"、"A1B2"、"A12B"、"1A2B"和 "1AB2"等都是str1和str2的交错组成

```java
public static boolean isCross(String s1, String s2, String aim) {
    if (s1 == null || s2 == null || aim == null) {
        return false;
    }
    char[] str1 = s1.toCharArray();
    char[] str2 = s2.toCharArray();
    char[] aims = aim.toCharArray();
    int N = str1.length;
    int M = str2.length;
    if (N + M != aim.length()) {
        return false;
    }
    boolean[][] dp = new boolean[N + 1][M + 1];
    dp[0][0] = true;
    for (int i = 1; i <= N; i++) {
        if (str1[i - 1] != aims[i - 1]) {
            break;
        }
        dp[i][0] = true;
    }
    for (int j = 1; j <= M; j++) {
        if (str2[j - 1] != aims[j - 1]) {
            break;
        }
        dp[0][j] = true;
    }
    for (int i = 1; i <= N; i++) {
        for (int j = 1; j <= M; j++) {
            dp[i][j] = (str1[i - 1] == aims[i + j - 1] && dp[i - 1][j]) || (str2[j - 1] == aims[i + j - 1] && dp[i][j - 1]);
        }
    }
    return dp[N][M];
}
```



## 题目三十九

给定一个无序数组arr,如果只能再一个子数组上排序, 返回如果让arr整体有序，需要排序的最短子数组长度

```java
public static int getMinLength(int[] arr) {
    if (arr == null || arr.length < 2) {
        return 0;
    }
    int min = arr[arr.length - 1];
    int noMinIndex = -1;
    for (int i = arr.length - 2; i >= 0; i--) {
        if (arr[i] > min) {
            noMinIndex = i;
        } else {
            min = Math.min(min, arr[i]);
        }
    }
    if (noMinIndex == -1) {
        return 0;
    }
    int max = arr[0];
    int noMaxIndex = -1;
    for (int i = 1; i < arr.length; i++) {
        if (arr[i] < max) {
            noMaxIndex = i;
        } else {
            max = Math.max(max, arr[i]);
        }
    }
    return noMaxIndex - noMinIndex + 1;
}
```



## 题目四十

给定一个有序的正数数组arr和一个正数aim,如果可以自由选择arr中的数字，想累加得到1~aim范围上所有的数，返回arr最少还缺几个数。
【举例】

arr={1,2,3,7}, aim 15 想累加得到1~15范围上所有的数，arr还缺14这个数，所以返回1
arr={1,5,7,aim=15 想累加得到1~15范围上所有的数，arr还缺2和4，所以返回2

```java
public static int minPatches(int[] arr, int aim) {
    int patches = 0;
    long range = 0;
    Arrays.sort(arr);
    for (int i = 0; i < arr.length; i++) {
        while (arr[i] > range + 1) {
            range += range + 1;
            patches++;
            if (range >= aim) {
                return patches;
            }
        }
        range += arr[i];
        if (range >= aim) {
            return patches;
        }
    }
    while (aim >= range + 1) {
        range += range + 1;
        patches++;
    }
    return patches;
}
```



## 题目四十一

在一个字符串中找到没有重复字符子串中最长的长度。例如：abcabcbb没有重复字符的最长子串是abc,长度为3，bbbbb,答案是b,长度为1，pwwkew,答案是wke,长度是3
要求：答案必须是子串，"pwke"是一个子字符序列但不是一个子字符串。

```java
public static int getNoRepeatMaxLength(String s) {
    if (s == null || s.length() == 0) {
        return 0;
    }
    char[] str = s.toCharArray();
    int[] map = new int[256];
    for (int i = 0; i < 256; i++) {
        map[i] = -1;
    }
    map[str[0]] = 0;
    int pre = 1;
    int max = 1;
    for (int i = 1; i < str.length; i++) {
        pre = Math.min(i - map[str[i]], pre + 1);
        map[str[i]] = i;
        max = Math.max(max, pre);
    }
    return max;
}
```



## 题目四十二

一个数组中，如果两个数的最小公共因子大于1，则认为这两个数之间有通路，返回数组中最大的域

```java
public static int gcd(int a, int b) {
    return b == 0 ? a : gcd(b, a % b);
}

public static class UnionFind {
    private int[] parents;
    private int[] sizes;
    private int[] help;

    public UnionFind(int N) {
        parents = new int[N];
        sizes = new int[N];
        help = new int[N];
        for (int i = 0; i < N; i++) {
            parents[i] = i;
            sizes[i] = 1;
        }
    }

    public int maxSize() {
        int ans = 0;
        for (int size : sizes) {
            ans = Math.max(ans, size);
        }
        return ans;
    }

    public int find(int i) {
        int hi = 0;
        while (i != parents[i]) {
            help[hi++] = i;
            i = parents[i];
        }
        for (hi--; hi >= 0; hi--) {
            parents[help[hi]] = i;
        }
        return i;
    }

    public void union(int i, int j) {
        int f1 = find(i);
        int f2 = find(j);
        if (f1 != f2) {
            int big = sizes[f1] >= sizes[f2] ? f1 : f2;
            int small = big == f1 ? f2 : f1;
            parents[small] = big;
            sizes[big] = sizes[f1] + sizes[f2];
        }
    }
}


public static int largestComponentSize1(int[] arr) {
    int N = arr.length;
    UnionFind uf = new UnionFind(N);
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            if (gcd(arr[i], arr[j]) != 1) {
                uf.union(i, j);
            }
        }
    }
    return uf.maxSize();
}

public static int largestComponentSize2(int[] arr) {
    int N = arr.length;
    UnionFind uf = new UnionFind(N);
    HashMap<Integer, Integer> fatorsMap = new HashMap<>();
    for (int i = 0; i < N; i++) {
        int num = arr[i];
        int limit = (int)Math.sqrt(num);
        for (int j = 1; j <= limit; j++) {
            if (num % j == 0) {
                if (j != 1) {
                    if (!fatorsMap.containsKey(j)) {
                        fatorsMap.put(j, i);
                    } else {
                        uf.union(fatorsMap.get(j), i);
                    }
                }
                int other = num / j;
                if (other != 1) {
                    if (!fatorsMap.containsKey(other)) {
                        fatorsMap.put(other, i);
                    } else {
                        uf.union(fatorsMap.get(other), i);
                    }
                }
            }

        }
    }
    return uf.maxSize();
}
```



## 题目四十三

给定一个全是小写字母的字符串s，删除多余字符，使得每种字符只保留一个，并让最终结果字符串的字典序最小【举例】
str="acbc",删掉第一个'c',得到"abc",是所有结果字符串中字典序最小的。
str="dbcacbca", 删掉第一个b'、第一个'c'、第二个'c'、第二个'a',得到"dabc",是所有结果字符串中字典序最小的。

```java
public static String remove(String str) {
    if (str == null || str.length() < 2) {
        return str;
    }
    int[] map = new int[256];
    for (int i = 0; i < str.length(); i++) {
        map[str.charAt(i)]++;
    }
    int minACSIndex = 0;
    for (int i = 0; i < str.length(); i++) {
        minACSIndex = str.charAt(minACSIndex) > str.charAt(i) ? i : minACSIndex;
        if (--map[str.charAt(i)] == 0) {
            break;
        }
    }
    return str.charAt(minACSIndex) + remove(str.substring(minACSIndex + 1).replaceAll(String.valueOf(str.charAt(minACSIndex)), ""));
}
```

优化

```java
public String removeDuplicateLetters(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }
        int[] map = new int[26];
        char[] str = s.toCharArray();
        for (char c : str) {
            map[c - 'a']++;
        }
        char[] res = new char[26];
        int L = 0;
        int index = 0;
        while (L < str.length) {
            if (map[str[L] - 'a'] == -1) {
                L++;
                continue;
            }
            int minACSIndex = L;
            int i = L;
            for (; i < str.length; i++) {
                if (map[str[i] - 'a'] != -1) {
                    minACSIndex = str[minACSIndex] > str[i] ? i : minACSIndex;
                    if (--map[str[i] - 'a'] == 0) break;
                }
            }
            for (int j = minACSIndex + 1; j <= i; j++) {
                if (map[str[j] - 'a'] != -1) {
                    map[str[j] - 'a']++;
                }
            }
            res[index++] = str[minACSIndex];
            map[str[minACSIndex] - 'a'] = -1;
            L = minACSIndex + 1;
        }
        return String.valueOf(res, 0, index);
    }
```



## 题目四十四

给定两个数组arrx和arry,长度都为N。代表二维平面上有N个点，第i个点的x坐标和y坐标分别为arr[x]和arr[y],返回求一条直线最多能穿过多少个点？

```java
public static class Point{
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}


public static int maxPoints(Point[] points) {
    if (points == null) {
        return 0;
    }
    if (points.length <= 2) {
        return points.length;
    }
    Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
    int result = 0;
    for (int i = 0; i < points.length; i++) {
        map.clear();
        int samePosition = 1;
        int sameX = 0;
        int sameY = 0;
        int line = 0;
        for (int j = i; j < points.length; j++) {
            int x = points[j].x - points[i].x;
            int y = points[j].y - points[i].y;
            if (x == 0 && y == 0) {
                samePosition++;
            } else if (x == 0) {
                sameX++;
            } else if (y == 0) {
                sameY++;
            } else {
                int gcd = gcd(x, y);
                x /= gcd;
                y /= gcd;
                if (!map.containsKey(x)) {
                    map.put(x, new HashMap<>());
                }
                if (!map.get(x).containsKey(y)) {
                    map.get(x).put(y, 0);
                }
                map.get(x).put(y, map.get(x).get(y) + 1);
                line = Math.max(line, map.get(x).get(y));
            }
        }
        result = Math.max(result, Math.max(line, Math.max(sameX, sameY)) + samePosition);
    }
    return result;
}
```



## 题目四十五

int[] d,d[i]:i号怪兽的能力
int[] p,p[i]：i号怪兽要求的钱
开始时你的能力是0，你的目标是从0号怪兽开始，通过所有的怪兽。如果你当前的能力，小于号怪兽的能力，你必须付出p的钱，贿赂这个怪兽，然后怪兽就会加入你，他的能力直接累加到你的能力上；如果你当前的能力，大于等于号怪兽的能力，你可以选择直接通过，你的能力并不会下降，你也可以选择贿赂这个怪兽，然后怪兽就会加入你，他的能力直接累加到你的能力上。返回通过所有的怪兽，需要花的最小钱数。

基于能力进行暴力递归

```java
public static long process(int[] d, int[] p, int ability, int index) {
    if (index == d.length) {
        return 0;
    }
    if (ability < d[index]) {
        return p[index] + process(d, p, ability + d[index], index + 1);
    }
    return Math.min(p[index] + process(d, p, ability + d[index], index + 1),
            process(d, p, ability, index + 1));
}

public static long func1(int[] d, int[] p) {
    return process(d, p, 0, 0);
}
```

基于钱进行暴力递归

```java
public static long process1(int[] d, int[] p, int index, int money) {
    if (index == -1) {
        return money == 0 ? 0 : -1;
    }
    long preMaxAbility = process1(d, p, index - 1, money);
    long p1 = -1;
    if (preMaxAbility != -1 && preMaxAbility >= d[index]) {
        p1 = preMaxAbility;
    }
    long preMaxAbility2 = process1(d, p, index - 1, money - p[index]);
    long p2 = -1;
    if (preMaxAbility2 != -1) {
        p2 = d[index] + preMaxAbility2;
    }
    return Math.max(p1, p2);
}

public static long fun2(int[] d, int[] p) {
    int allMoney = 0;
    for (int i = 0; i < p.length; i++) {
        allMoney += p[i];
    }
    for (int money = 0; money < allMoney; money++) {
        if (process1(d, p, d.length - 1, money) != -1) {
            return money;
        }
    }
    return allMoney;
}
```

基于能力做动态规划

```java
public static long dp1(int[] d, int[] p) {
    int sum = 0;
    for (int num : d) {
        sum += num;
    }
    long[][] dp = new long[d.length + 1][sum + 1];
    for (int cur = d.length - 1; cur >= 0; cur--) {
        for (int hp = 0; hp <= sum; hp++) {
            if (hp + d[cur] > sum) {
                continue;
            }
            if (hp < d[cur]) {
                dp[cur][hp] = p[cur] + dp[cur + 1][hp + d[cur]];
            } else {
                dp[cur][hp] = Math.min(dp[cur + 1][hp], p[cur] + dp[cur + 1][hp + d[cur]]);
            }
        }
    }
    return dp[0][0];
}
```

基于钱做动态规划

```java
public static long dp2(int[] d, int[] p) {
    int allMoney = 0;
    for (int m : p) {
        allMoney += m;
    }
    int[][] dp = new int[d.length][allMoney + 1];
    for (int i = 0; i < dp.length; i++) {
        for (int j = 0; j <= allMoney; j++) {
            dp[i][j] = -1;
        }
    }
    dp[0][p[0]] = d[0];
    for (int i = 1; i < d.length; i++) {
        for (int j = 0; j <= allMoney; j++) {
            if (j >= p[i] && dp[i - 1][j - p[i]] != -1) {
                dp[i][j] = dp[i - 1][j - p[i]];
            }
            if (dp[i - 1][j] >= d[i]) {
                dp[i][j] = Math.max(dp[i][j], dp[i - 1][j]);
            }
        }
    }
    int ans = 0;
    for (int j = 0; j <= allMoney; j++) {
        if (dp[d.length - 1][j] != -1) {
            ans = j;
            break;
        }
    }
    return ans;
}
```



## 题目四十六

给定一个字符串，如果可以在字符串任意位置添加字符，最少添加几个能让字符串整体都是回文串。

```java
public static int fun(String s) {
    if (s == null || s.length() == 1) {
        return 0;
    }
    char[] str = s.toCharArray();
    int N = str.length;
    int[][] dp = new int[N][N];
    for (int i = 0; i < N - 1; i++) {
        dp[i][i + 1] = str[i] == str[i + 1] ? 0 : 1;
    }
    for (int i = N - 3; i >= 0; i--) {
        for (int j = i + 2; j < N; j++) {
            dp[i][j] = Integer.MAX_VALUE;
            if (str[i] == str[j]) {
                dp[i][j] = dp[i + 1][j - 1];
            }
            dp[i][j] = Math.min(dp[i][j], dp[i + 1][j] + 1);
            dp[i][j] = Math.min(dp[i][j], dp[i][j - 1] + 1);
        }
    }
    return dp[0][N - 1];
}
```



## 题目四十七

一种消息接收并打印的结构设计
已知一个消息流会不断地吐出整数1~N,但不一定按照顺序吐出。如果上次打印的数为i，那么当i+1出现时，请打印i+1及其之后接收过的并且连续的所有数，直到1~N全部接收并打印完，请设计这种接收并打印的结构。初始时默认i==0

```java
public static class Node {
    public String info;
    public Node next;

    public Node(String info) {
        this.info = info;
    }
}

public static class MessageBox {
    private HashMap<Integer, Node> headMap;
    private HashMap<Integer, Node> tailMap;
    private int waitPoint;

    public MessageBox() {
        headMap = new HashMap<>();
        tailMap = new HashMap<>();
        waitPoint = 1;
    }

    public void receive(int num, String info) {
        if (num < 1) {
            return;
        }
        Node cur = new Node(info);
        headMap.put(num, cur);
        tailMap.put(num, cur);

        if (tailMap.containsKey(num - 1)) {
            tailMap.get(num - 1).next = cur;
            tailMap.remove(num - 1);
            headMap.remove(num);
        }
        if (headMap.containsKey(num + 1)) {
            cur.next = headMap.get(num + 1);
            tailMap.remove(num);
            headMap.remove(num + 1);
        }
        if (num == waitPoint) {
            print();
        }
    }

    public void print() {
        Node cur = headMap.get(waitPoint);
        headMap.remove(waitPoint);
        while (cur != null) {
            System.out.print(cur.info + " ");
            cur = cur.next;
            waitPoint++;
        }
        tailMap.remove(waitPoint - 1);
        System.out.println();
    }
}

public static void main(String[] args) {
    MessageBox messageBox = new MessageBox();
    int[] arr = {2,3,5,4,7,8,1,6,9};
    for (int i = 0; i < arr.length; i++) {
        messageBox.receive(arr[i], String.valueOf(arr[i]));
    }
```



## 题目四十八

现有n1+n2种面值的硬币，其中前n1种为普通币，可以取任意枚，后n2种为纪念币，每种最多只能取一枚，每种硬币有一个面值，问能用多少种方法拼出m的面值？

```java
public static int[][] getDpArb(int[] arr, int money) {
    if (arr == null || arr.length == 0) {
        return null;
    }
    int[][] dp = new int[arr.length][money + 1];
    for (int i = 0; i < arr.length; i++) {
        dp[i][0] = 1;
    }
    for (int j = 1; arr[0] * j <= money; j++) {
        dp[0][arr[0] * j] = 1;
    }
    for (int i = 1; i < arr.length; i++) {
        for (int j = 1; j <= money; j++) {
            dp[i][j] = dp[i - 1][j];
            dp[i][j] += j - arr[i] >= 0 ? dp[i][j - arr[i]] : 0;
        }
    }
    return dp;
}


public static int[][] getDpOne(int[] arr, int money) {
    if (arr == null || arr.length == 0) {
        return null;
    }
    int[][] dp = new int[arr.length][money + 1];
    for (int i = 0; i < arr.length; i++) {
        dp[i][0] = 1;
    }
    if (arr[0] <= money) {
        dp[0][arr[0]] = 1;
    }
    for (int i = 1; i < arr.length; i++) {
        for (int j = 1; j <= money; j++) {
            dp[i][j] = dp[i - 1][j];
            dp[i][j] += j - arr[i] >= 0 ? dp[i - 1][j - arr[i]] : 0;
        }
    }
    return dp;
}

public static int moneyWays(int[] arbitrary, int[] onlyone, int money) {
    if (money < 0) {
        return 0;
    }
    if ((arbitrary == null || arbitrary.length == 0) && (onlyone == null || onlyone.length == 0)) {
        return money == 0 ? 1 : 0;
    }
    int[][] dpArb = getDpArb(arbitrary, money);
    int[][] dpOne = getDpOne(onlyone, money);
    if (dpArb == null) {
        return dpOne[dpOne.length - 1][money];
    }
    if (dpOne == null) {
        return dpArb[dpArb.length - 1][money];
    }
    int res = 0;
    for (int i = 0; i <= money; i++) {
        res += dpArb[dpArb.length - 1][i] * dpOne[dpOne.length - 1][money - i];
    }
    return res;
}
```



## 题目四十九

给定一个正数N,表示你在纸上写下1~N所有的数字，返回在书写的过程中，一共写下了多少个1

```java
public static int f(int num) {
    if (num < 1) {
        return 0;
    }
    int len = getLenOfNum(num);
    if (len == 1) {
        return 1;
    }
    int base = powerBaseOf10(len - 1);
    int first = num / base;
    int firstOneNum = first == 1 ? num % base + 1: base;
    int otherOneNum = first * (base / 10) * (len - 1);
    return firstOneNum + otherOneNum + f(num % base);
}
private static int getLenOfNum(int num) {
        int len = 0;
        while (num != 0) {
            len++;
            num /= 10;
        }
        return len;
    }

    private static int powerBaseOf10(int base) {
        return (int) Math.pow(10, base);
    }
```



## 题目五十

先给出可整合数组的定义：如果一个数组在排序之后，每相邻两个数差的绝对值都为1，则该数组为可整合数组。例如，[5,3,4,6,2]排序之后为[2,3,4,5,6]，符合每相邻两个数差的绝对值都为1，所以这个数组为可整合数组。给定一个整型数组arr,请返回其中最大可整合子数组的长度。例如，[5,5,3,2,6,4,3]的最大可整合子数组为[5,3,2,6,4]，所以返回5。

```java
public static int longestIntegerSubArr(int[] arr) {
    int len = 0;
    int max = 0;
    int min = 0;
    HashSet<Integer> set = new HashSet<>();
    for (int L = 0; L < arr.length; L++) {
        set.clear();
        max = Integer.MIN_VALUE;
        min = Integer.MAX_VALUE;
        for (int R = L; R < arr.length; R++) {
            if (set.contains(arr[R])) {
                break;
            }
            set.add(arr[R]);
            max = Math.max(max, arr[R]);
            min = Math.min(min, arr[R]);
            if (max - min == R - L) {
                len = Math.max(len, R - L + 1);
            }
        }
    }
    return len;
}
```



## 题目五十一

给定一个数组arr,从左到右表示昨天从早到晚股票的价格。作为一个事后诸葛亮，你想知道如果只做一次交易，且每次交易只买卖一股，返回能挣到的最大钱数

```java
public static int maxProfit(int[] prices) {
    if (prices == null || prices.length == 0) {
        return 0;
    }
    int min = prices[0];
    int ans = 0;
    for (int price : prices) {
        min = Math.min(min, price);
        ans = Math.max(ans, price - min);
    }
    return ans;
}
```



## 题目五十二

给定一个数组arr,从左到右表示昨天从早到晚股票的价格，作为一个事后诸葛亮，你想知道如果随便交易且每次交易只买卖一股，返回能挣到的最大钱数

```java
public static int maxProfit1(int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        int ans = 0;
        for (int i = 1; i < prices.length; i++) {
            ans += Math.max(0, prices[i] - prices[i - 1]);
        }
        return ans;
    }
```



## 题目五十三

给定一个数组arr,从左到右表示昨天从早到晚股票的价格，作为一个事后诸葛亮，你想知道如果交易次数不超过K次，且每次交易只买卖一股，返回能挣到的最大钱数

```java
public static int maxProfit2(int[] prices, int k) {
    if (prices == null || prices.length == 0) {
        return 0;
    }
    int N = prices.length;
    k = Math.min(k, N / 2);
    int[][] dp = new int[prices.length][k + 1];
    for (int j = 1; j <= k; j++) {
        int t = dp[0][j - 1] - prices[0];
        for (int i = 1; i < N; i++) {
            t = Math.max(t, dp[i][j - 1] - prices[i]);
            dp[i][j] = Math.max(dp[i - 1][j], t + prices[i]);
        }
    }
    return dp[N - 1][k];
}
```



## 题目五十四

给定一个数组arr, 再给定一个k值，返回累加和小于等于k, 但是离k最近的子数组累加和。

```java
public static int getMaxLessOrEqualK(int[] arr, int k) {
    TreeSet<Integer> set = new TreeSet<>();
    set.add(0);
    int max = Integer.MIN_VALUE;
    int sum = 0;
    Integer more = null;
    for (int num : arr) {
        sum += num;
        more = set.ceiling(sum - k);
        if (more != null) {
            max = Math.max(max, sum - more);
        }
        set.add(sum);
    }
    return max;
}
```



## 题目五十五

给定一个二维数组matrix,可以从任何位置出发，每一步可以走向上、下、左、右四个方向。返回最大递增链的长度。
例子：
matrix

```
543
312
213
```

从最中心的1出发，是可以走出12345的链的，而且这是最长的递增链。所以返回长度5

```java
public static int maxPath(int[][] matrix) {
    int ans = Integer.MIN_VALUE;
    for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
            ans = Math.max(ans, process(matrix, i, j, Integer.MIN_VALUE));
        }
    }
    return ans;
}

public static int process(int[][] m, int i, int j, int pre) {
    if (i < 0 || i == m.length || j < 0 || j == m[0].length || pre >= m[i][j]) {
        return 0;
    }
    return Math.max(
            Math.max(process(m, i - 1, j, m[i][j]), process(m, i + 1, j, m[i][j])),
            Math.max(process(m, i, j - 1, m[i][j]), process(m, i, j + 1, m[i][j]))
    ) + 1;
}
```

记忆化搜索

```java
public static int maxPath1(int[][] matrix) {
    int ans = Integer.MIN_VALUE;
    int[][] dp = new int[matrix.length][matrix[0].length];
    for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
            ans = Math.max(ans, process(matrix, i, j, Integer.MIN_VALUE, dp));
        }
    }
    return ans;
}


public static int process(int[][] m, int i, int j, int pre, int[][] dp) {
    if (i < 0 || i == m.length || j < 0 || j == m[0].length || pre >= m[i][j]) {
        return 0;
    }
    if (dp[i][j] != 0) {
        return dp[i][j];
    }
    dp[i][j] = Math.max(
            Math.max(process(m, i - 1, j, m[i][j], dp), process(m, i + 1, j, m[i][j], dp)),
            Math.max(process(m, i, j - 1, m[i][j], dp), process(m, i, j + 1, m[i][j], dp))
    ) + 1;
    return dp[i][j];
}
```



## 题目五十六

给定一个字符类型的二维数组board,和一个字符串组成的列表words。可以从board任何位置出发，每一步可以走向上、下、左、右，四个方向，但是一条路径已经走过的位置，不能重复走。返回words哪些单词可以被走出来。
例子
board =

```
[o, a, a, n],
[e, t, a, e]
[i, h, k, r]
[i, f, l, v]
```

words ["oath","pea","eat","rain"]I
输出：["eat""oath]

```java
public class Code51 {

    static class TrieNode {
        public TrieNode[] next;
        public int pass;
        public int end;

        public TrieNode() {
            next = new TrieNode[26];
        }
    }
		//主函数
    public static List<String> findWords(char[][] board, String[] words) {
        TrieNode head = new TrieNode();
        HashSet<String> set = new HashSet<>();
        for (String word : words) {
            if (!set.contains(word)) {
                set.add(word);
                fillWord(head, word);
            }
        }
        List<String> ans = new ArrayList<>();
        LinkedList<Character> path = new LinkedList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                process(board, i, j, path, head, ans);
            }
        }
        return ans;
    }
		//DFS
    private static int process(char[][] board, int i, int j, LinkedList<Character> path, TrieNode cur, List<String> ans) {
        char cha = board[i][j];
        if (cha == 0) {
            return 0;
        }
        int index = cha - 'a';
        if (cur.next[index] == null || cur.next[index].pass == 0) {
            return 0;
        }
        cur = cur.next[index];
        path.addLast(cha);
        int fix = 0;
        if (cur.end > 0) {
            ans.add(generatePath(path));
            cur.end--;
            fix++;
        }
        board[i][j] = 0;
        if (i > 0) {
            fix += process(board, i - 1, j, path, cur, ans);
        }
        if (i < board.length - 1) {
            fix += process(board, i + 1, j, path, cur, ans);
        }
        if (j > 0) {
            fix += process(board, i, j - 1, path, cur, ans);
        }
        if (j < board[0].length - 1) {
            fix += process(board, i, j + 1, path, cur, ans);
        }
        board[i][j] = cha;
        path.pollLast();
        cur.pass -= fix;
        return fix;
    }

    private static String generatePath(LinkedList<Character> path) {
        char[] chs = new char[path.size()];
        for (int i = 0; i < chs.length; i++) {
            chs[i] = path.get(i);
        }
        return String.valueOf(chs);
    }
		//填充前缀树
    public static void fillWord(TrieNode head, String words) {
        head.pass++;
        char[] chs = words.toCharArray();
        int index = 0;
        TrieNode cur = head;
        for (int i = 0; i < chs.length; i++) {
            index = chs[i] - 'a';
            if (cur.next[index] == null) {
                cur.next[index] = new TrieNode();
            }
            cur = cur.next[index];
            cur.pass++;
        }
        cur.end++;
    }


    public static void main(String[] args) {
        char[][] b = {
                {'o', 'a', 'a', 'n'},
                {'e', 't', 'a', 'e'},
                {'i', 'h', 'k', 'r'},
                {'i', 'f', 'l', 'v'},
        };
        System.out.println(findWords(b, new String[]{"oath", "pea", "eat", "rain"}));
    }
}
```



## 题目五十七

给定两个字符串S和T,返回S子序列等于T的不同子序列个数有多少个？如果得到子序列A删除的位置与得到子序列B删除的位置不同，那么认为A和B就是不同的。
【例子】S="rabbbit",T="rabbit" 返回：3

```java
public static int f(String s, String t) {
    char[] str = s.toCharArray();
    char[] ttr = t.toCharArray();
    int N = str.length;
    int M = ttr.length;
    int[][] dp = new int[N][M];
    dp[0][0] = str[0] == ttr[0] ? 1 : 0;
    for (int i = 1; i < N; i++) {
        dp[i][0] = dp[i - 1][0] + str[i] == ttr[0] ? 1 : 0;
    }
    for (int i = 1; i < N; i++) {
        for (int j = 1; j < M; j++) {
            dp[i][j] = dp[i - 1][j];
            if (str[i] == ttr[j]) {
                dp[i][j] += dp[i - 1][j - 1];
            }
        }
    }
    return dp[N - 1][M - 1];
}
```



## 题目五十八

给定一个二维数组map,含义是一张地图，例如，如下矩阵：

```
-2  -3   3
-5  -10  1
0   30  -5
```

游戏的规则如下：
骑士从左上角出发，每次只能向右或向下走，最后到达右下角见到公主。地图中每个位置的值代表骑士要遭遇的事
，如果是负数，说明此处有怪兽，要让骑士损失血量。如果是非负数，代表此处有血瓶，能让骑士回血。骑士从左上角到右下角的过程中，走到任何一个位置时，血量都不能少于1。为了保证骑士能见到公主，初始血量至少是多少？根据map,返回至少的初始血量。

```java
public static int needMin(int[][] matrix) {
    if (matrix == null || matrix.length == 0) {
        return 0;
    }
    return process(matrix, 0, 0, matrix.length, matrix[0].length);
}


public static int process(int[][] matrix, int row, int col, int N, int M) {
    if (row == N - 1 && col == M - 1) {
        return matrix[N - 1][M - 1] < 0 ? Math.abs(matrix[N - 1][M - 1]) + 1 : 1;
    }

    if (row == N - 1) {
        int rightNeed = process(matrix, row, col + 1, N, M);
        if (matrix[row][col] < 0) {
            return Math.abs(matrix[row][col]) + rightNeed;
        }
        if (matrix[row][col] >= rightNeed) {
            return 1;
        }
        return rightNeed - matrix[row][col];
    }
    if (col == M - 1) {
        int downNeed = process(matrix, row + 1, col, N, M);
        if (matrix[row][col] < 0) {
            return Math.abs(matrix[row][col]) + downNeed;
        }
        if (matrix[row][col] >= downNeed) {
            return 1;
        }
        return downNeed - matrix[row][col];
    }
    int minNextNeed = Math.min(process(matrix, row + 1, col, N, M), process(matrix, row, col + 1, N, M));
    if (matrix[row][col] < 0) {
        return Math.abs(matrix[row][col]) + minNextNeed;
    }
    if (matrix[row][col] >= minNextNeed) {
        return 1;
    }
    return minNextNeed - matrix[row][col];
}
```

改动态规划

```java
public static int dp(int[][] matrix) {
    if (matrix == null || matrix.length == 0) {
        return 0;
    }
    int N = matrix.length;
    int M = matrix[0].length;
    int[][] dp = new int[N][M];
    for (int i = N - 1; i >= 0; i--) {
        for (int j = M - 1; j >= 0; j--) {
            if (i == N - 1 && j == M - 1) {
                dp[i][j] = 1 - Math.min(0, matrix[i][j]);
                continue;
            }
            if (i == N - 1) {
                if (matrix[i][j] < 0) {
                    dp[i][j] = Math.abs(matrix[i][j]) + dp[i][j + 1];
                    continue;
                }
                if (matrix[i][j] >= dp[i][j + 1]) {
                    dp[i][j] = 1;
                    continue;
                }
                dp[i][j] = dp[i][j + 1] - matrix[i][j];
                continue;
            }
            if (j == M - 1) {
                if (matrix[i][j] < 0) {
                    dp[i][j] = Math.abs(matrix[i][j]) + dp[i + 1][j];
                    continue;
                }
                if (matrix[i][j] >= dp[i + 1][j]) {
                    dp[i][j] = 1;
                    continue;
                }
                dp[i][j] = dp[i + 1][j] - matrix[i][j];
                continue;
            }
            int min = Math.min(dp[i + 1][j], dp[i][j + 1]);
            if (matrix[i][j] < 0) {
                dp[i][j] =  Math.abs(matrix[i][j]) + min;
                continue;
            }
            if (matrix[i][j] >= dp[i][j]) {
                dp[i][j] = 1;
                continue;
            }
            dp[i][j] = min - matrix[i][j];
        }
    }
    return dp[0][0];
}
```



## 题目五十九

给定一个矩阵matriⅸ，先从左上角开始，每一步只能往右或者往下走，走到右下角。然后从右下角出发，每一步只能往上或者往左走，再回到左上角。任何一个位置的数字，只能获得一遍。返回最大路径和。

```java
public class Code54 {


    public static int comeGoMaxPathSum(int[][] matrix) {
        return process(matrix, 0, 0, 0);
    }

    public static int process(int[][] matrix, int ar, int ac, int br) {
        int N = matrix.length;
        int M = matrix[0].length;
        if (ar == N - 1 && ac == M - 1) {
            return matrix[ar][ac];
        }
        int bc = ar + ac - br;

        int ADownBDown = 0;
        if (ar + 1 < N && br + 1 < N) {
            ADownBDown = process(matrix, ar + 1, ac, br + 1);
        }
        int ADownBRight = 0;
        if (ar + 1 < N && bc + 1 < M) {
            ADownBRight = process(matrix, ar + 1, ac, br);
        }
        int ARightBDown = 0;
        if (ac + 1 < M && br + 1 < N) {
            ARightBDown = process(matrix, ar, ac + 1, br + 1);
        }
        int ARightBRight = 0;
        if (ac + 1 < M && bc + 1 < M) {
            ARightBRight = process(matrix, ar , ac + 1, br);
        }

        int nextBest = Math.max(Math.max(ADownBDown, ADownBRight), Math.max(ARightBDown, ARightBRight));
        if (ar == br) {
            return matrix[ar][ac] + nextBest;
        }
        return matrix[ar][ac] + matrix[br][bc] + nextBest;

    }


    public static void main(String[] args) {
        int[][] m = {
                {1,1,1,1,1,0,0,0,0,0},
                {0,0,0,0,1,0,0,0,0,1},
                {1,0,0,0,1,0,0,0,0,0},
                {0,0,0,0,1,0,0,0,0,0},
                {0,0,0,0,1,0,0,0,0,0},
        };
        System.out.println(comeGoMaxPathSum(m));
    }
}
```

优化





## 题目六十

给定一个无序数组arr,返回如果排序之后，相邻数之间的最大差值 {3,1,7,9},如果排序后{1,3,7,9}，相邻数之间的最大差值来自3和7，返回6
要求：不能真的进行排序，并且要求在时间复杂度O(N)内解决

```java
public class Code55 {

    public static int maxGap(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int len = nums.length;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < len; i++) {
            min = Math.min(min, nums[i]);
            max = Math.max(max, nums[i]);
        }
        if (min == max) {
            return 0;
        }
        boolean[] hasNum = new boolean[len + 1];
        int[] maxs = new int[len + 1];
        int[] mins = new int[len + 1];
        int bid = 0;
        for (int i = 0; i < len; i++) {
            bid = bucket(nums[i], len, min, max);
            mins[bid] = hasNum[bid] ? Math.min(mins[bid], nums[i]) : nums[i];
            maxs[bid] = hasNum[bid] ? Math.max(maxs[bid], nums[i]) : nums[i];
            hasNum[bid] = true;
        }
        int res = 0;
        int lastMax = maxs[0];

        for (int i = 1; i <= len; i++) {
            if (hasNum[i]) {
                res = Math.max(res, mins[i] - lastMax);
                lastMax = maxs[i];
            }
        }
        return res;
    }
    public static int bucket(int num, int len, int min, int max) {
        return ((num - min) * len  / (max - min));
    }

    public static void main(String[] args) {
        int[] nums = {3,1,7,9};
        System.out.println(maxGap(nums));
    }

}
```



## 题目六十一

假设所有字符都是小写字母.大字符串是str， arr是去重的单词表，每个单词都不是空字符串且可以使用任意次使用arr中的单词有多少种拼接str的方式，返回方法数

递归实现

```java
//arr较多 str较短
public static int ways(String str, String[] arr) {
    HashSet<String> set = new HashSet<>();
    for (String candidate : arr) {
        set.add(candidate);
    }
    return process(str, 0, set);
}

public static int process(String str, int i, HashSet<String> set) {
    if (i == str.length()) {
        return 1;
    }
    int ways = 0;
    for (int end = i + 1; end < str.length(); end++) {
        String pre = str.substring(i, end);
        if (set.contains(pre)) {
            ways += process(str, end, set);
        }
    }
    return ways;
}

public static class Node {
    public boolean end;
    public Node[] nexts;

    public Node() {
        this.end = false;
        this.nexts = new Node[26];
    }
}

//arr较少，str较长
public static int ways2(String str, String[] arr) {
    if (str == null || str.length() == 0 || arr == null || arr.length == 0) {
        return 0;
    }
  	//构造前缀树
    Node root = new Node();
    for (String s : arr) {
        char[] chs = s.toCharArray();
        Node node = root;
        int index = 0;
        for (int i = 0; i < chs.length; i++) {
            index = chs[i] - 'a';
            if (node.nexts[index] == null) {
                node.nexts[index] = new Node();
            }
            node = node.nexts[index];
        }
        node.end = true;
    }
    return g(str.toCharArray(), root, 0);
}

public static int g(char[] str, Node root, int i) {
    if (i == str.length) {
        return 1;
    }
    int ways = 0;
    Node cur = root;
    for (int end = i; end < str.length; end++) {
        int path = str[end] - 'a';
        if (cur.nexts[path] == null) {
            break;
        }
        cur = cur.nexts[path];
        if (cur.end) {
            ways += g(str, root, end + 1);
        }
    }
    return ways;
}
```

动态规划

。。。



## 题目六十二

给定一棵二叉树的头节点head,和一个数K
路径的定义：可以从任何一个点开始，但是只能往下走，往下可以走到任何节点停止，返回路径累加和为K的所有路径中，最长的路径最多有几个节点？

```java
public static class Node {
    public int value;
    public Node left;
    public Node right;

    public Node(int value) {
        this.value = value;
    }
}

public static int ans = 0;

public static int longest(Node head, int K) {
    ans = 0;
    HashMap<Integer, Integer> sumMap = new HashMap<>();
    sumMap.put(0, -1);
    process(head, 0, 0, K, sumMap);
    return ans;
}

private static void process(Node X, int level, int preSum, int k, HashMap<Integer, Integer> sumMap) {
    if (X != null) {
        int allSum = preSum + X.value;
        if (sumMap.containsKey(allSum - k)) {
            ans = Math.max(ans, level - sumMap.get(allSum - k));
        }
      	//记录最早出现的
        if (!sumMap.containsKey(allSum)) {
            sumMap.put(allSum, level + 1);
        }
        process(X.left, level + 1, allSum, k, sumMap);
        process(X.right, level + 1, allSum, k, sumMap);
      	//擦除当前的影响
        if (sumMap.get(allSum) == level) {
            sumMap.remove(allSum);
        }
    }
}
```



## 题目六十三

给定一个数组arr,已知除了一种数只出现1次之外，剩下所有的数都出现了k次，如何使用O(1)的额外空间，找到这个数。

```java
public static int findOnceK(int[] arr, int k) {
    int res = 0;
    for (int i = 0; i < 32; ++i) {
        int bit = 0;
        //求二进制中i位所有的和
        for (int value : arr) {
            bit += (value >> i) & 1;
        }
        res |= (bit % k) << i;
    }
    return res;
}
```



## 题目六十四

给定一个数组r,如果有某个数出现次数超过了数组长度的一半，打印这个数，如果没有不打印

```java
public class Code59 {
    
    public static void printHalfMajor(int[] arr) {
        int cand = 0;
        int hp = 0;
        for (int i = 0; i < arr.length; i++) {
            if (hp == 0) {
                cand = arr[i];
                hp++;
            } else if (arr[i] == cand) {
                hp++;
            } else {
                hp--;
            }
        }

        if (hp == 0) {
            System.out.println("no such number.");
            return;
        }
        hp = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == cand) {
                hp++;
            }
        }
        if (hp > arr.length / 2) {
            System.out.println(cand);
        } else {
            System.out.println("no such number.");
        }
    }


    public static void main(String[] args) {
        int[] arr = {2,3,4,1,1,1,1,5,1,1};
        printHalfMajor(arr);
    }
}
```

给定一个数组arr和整数k,arr长度为N,如果有某些数出现次数超过了N/K, 打印这些数，如果没有不打印

```java
public static void printKMajor(int[] arr, int k) {
    if (k < 2) {
        System.out.println("The value of k is invalid.");
    }
    HashMap<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < arr.length; i++) {
        if (!map.containsKey(arr[i])) {
            if (map.size() < k - 1) {
                map.put(arr[i], 1);
            } else {
                Iterator<Integer> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    Integer key = iterator.next();
                    if (map.get(key) - 1 == 0) {
                        iterator.remove();
                    } else {
                        map.put(key, map.get(key) - 1);
                    }
                }

            }
        } else {
            map.put(arr[i], map.get(arr[i]) + 1);
        }
    }

    HashMap<Integer, Integer> realMap = new HashMap<>();
    for (int i = 0; i < arr.length; i++) {
        if (map.containsKey(arr[i])) {
            if (realMap.containsKey(arr[i])) {
                realMap.put(arr[i], realMap.get(arr[i]) + 1);
            } else {
                realMap.put(arr[i], 1);
            }
        }
    }
    for (Map.Entry<Integer, Integer> entry : realMap.entrySet()) {
        if (entry.getValue() > arr.length / k) {
            System.out.println(entry.getKey());
        }
    }
}
```



## 题目六十五

(最优解超难log(min(N, M)))给定两个整数数组A和B,  A是长度为m、元素从小到大排好序了, B是长度为n、元素从小到大排好序了, 希望从A和B数组中，找出最大的k个数字



## 题目六十六

约瑟夫环

```java
public static int getLive(int i, int m) {
    if (i == 1) {
        return 1;
    }
    return (getLive(i - 1, m) + m - 1) % i + 1;
}

public static void main(String[] args) {
    System.out.println(getLive(5, 7));
}
```



## 题目六十七

(Lintcode 131大楼轮廓)给定一个Nx3的矩阵matriⅸ，对于每一个长度为3的小数组arr, 都表示一个大楼的三个数据。arr[0]表示大楼的左边界，arr[1]表示大楼的右边界，arr[2]表示大楼的高度（一定大于0）。每座大楼的地基都在X轴上，大楼之间可能会有重叠，请返回整体的轮廓线数组。
【举例】matrix={{2,5,6}，{1,7,4}，{4,6,7}，{3,6,5}，{10,13,2}，{9,11,3}，{12,14,4}，{10,12,5}}
返回：{1,2,4}，{2,4,6}，{4,6,7}，{6,7,4}，{9,10,3}，{10,12,5}，{12,14,4}

```java
public static class Op {
    public int x;
    public boolean isAdd;
    public int h;

    public Op(int x, boolean isAdd, int h) {
        this.x = x;
        this.isAdd = isAdd;
        this.h = h;
    }
}

public static class NodeComparator implements Comparator<Op> {

    @Override
    public int compare(Op o1, Op o2) {
        if (o1.x != o2.x) {
            return o1.x - o2.x;
        }
        if (o1.isAdd != o2.isAdd) {
            return o1.isAdd ? 1 : -1;
        }
        return 0;
    }
}

public static List<List<Integer>> buildingOutline(int[][] matrix) {
    int N = matrix.length;
    Op[] ops = new Op[N << 1];
    for (int i = 0; i < matrix.length; i++) {
        ops[i * 2] = new Op(matrix[i][0], true, matrix[i][2]);
        ops[i * 2 + 1] = new Op(matrix[i][1], false, matrix[i][2]);
    }
    Arrays.sort(ops, new NodeComparator());

    TreeMap<Integer, Integer> mapHeightTimes = new TreeMap<>();
    TreeMap<Integer, Integer> mapXHeight = new TreeMap<>();

    for (int i = 0; i < ops.length; i++) {
        if (ops[i].isAdd) {
            if (!mapHeightTimes.containsKey(ops[i].h)) {
                mapHeightTimes.put(ops[i].h, 1);
            } else {
                mapHeightTimes.put(ops[i].h, mapHeightTimes.get(ops[i].h) + 1);
            }
        } else {
            if (mapHeightTimes.get(ops[i].h) == 1) {
                mapHeightTimes.remove(ops[i].h);
            } else {
                mapHeightTimes.put(ops[i].h, mapHeightTimes.get(ops[i].h) - 1);
            }
        }
        if (mapHeightTimes.isEmpty()) {
            mapXHeight.put(ops[i].x, 0);
        } else {
            mapXHeight.put(ops[i].x, mapHeightTimes.lastKey());
        }
    }
    List<List<Integer>> res = new ArrayList<>();
    int start = 0;
    int preHeight = 0;
    for (Map.Entry<Integer, Integer> entry : mapXHeight.entrySet()) {
        int curX = entry.getKey();
        int curMaxHeight = entry.getValue();
        if (preHeight != curMaxHeight) {
            if (preHeight != 0) {
                res.add(new ArrayList<>(Arrays.asList(start, curX, preHeight)));
            }
            start = curX;
            preHeight = curMaxHeight;
        }
    }
    return res;
}
```



## 题目六十八

Nim博弈问题
给定一个非负数组，每一个值代表该位置上有几个铜板。a和b玩游戏，a先手, b后手，轮到某个人的时候，只能在一个位置上拿任意数量的铜板，但是不能不拿。谁最先把铜板拿完谁赢。假设a和b都极度聪明，请返回获胜者的名字

```java
		// 保证arr是正数数组
    public static void printWinner(int[] arr) {
        int eor = 0;
        for (int num : arr) {
            eor ^= num;
        }
        if (eor == 0) {
            System.out.println("后手赢");
        } else {
            System.out.println("先手赢");
        }
    }
```



## 题目六十九

给定一个数组arr,长度为N且每个值都是正数，代表N个人的体重。再给定一个正数limit,代表一艘船的载重。
以下是坐船规则：
1)每艘船最多只能坐两人；
2)乘客的体重和不能超过limit
返回如果同时让这N个人过河最少需要几条船。

```java
public static int minBoat(int[] arr, int limit) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        Arrays.sort(arr);
        int N = arr.length;
        //最大的体重 比limit大，肯定无法全部过去
        if (arr[N - 1] > limit) {
            return -1;
        }
        int lessR = -1;
        for (int i = N - 1; i >= 0; i--) {
            if (arr[i] <= limit / 2) {
                lessR = i;
                break;
            }
        }
        //只能一个人坐
        if (lessR == -1) {
            return N;
        }
        int L = lessR;
        int R = lessR + 1;
        int noUsed = 0;
        while (L >= 0) {
            int solved = 0;
            while (R < N && arr[L] + arr[R] <= limit) {
                R++;
                solved++;
            }
            if (solved == 0) {
                //L没有让R向右动
                L--;
                noUsed++;
            } else {
                L = Math.max(-1, L - solved);
            }
        }
        int all = lessR + 1;
        int used = all - noUsed;
        int moreUnSolved = N - all - used;
        return used + ((noUsed + 1) >> 1) + moreUnSolved;
    }
```



## 题目七十

https://leetcode.cn/problems/longest-palindromic-subsequence

给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。

```java
class Solution {
    public int longestPalindromeSubseq(String s) {
        int N = s.length();
        char[] str = s.toCharArray();
        int[][] dp = new int[N][N];
        for (int i = 0; i < N; i++) {
            dp[i][i] = 1;
        }
        for (int i = N - 2; i >= 0; i --) {
            for (int j = i + 1; j < N; j++) {
                dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                if (str[i] == str[j]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i + 1][j - 1] + 2);
                }
            }
        }
        return dp[0][N - 1];
    }
}
```



## 题目七十一

给定一个二维数组matrix，每个单元都是一个整数，有正有负，最开始的时候小Q操纵一条长度为0的蛇蛇丛短阵最左侧任选个单元格进入地图蛇,每次只能够到达当前位置的上相邻有厕相邻和右下相邻的单元格，蛇蛇到达一个单元格后，自身的长度会瞬间加上该单元格的数值，任何情况下长度为负则游戏结束。小Q是个天才，他拥有一个超能方，可以在游戏开始的时候把地图中任何一个节点的值变为其相反数注最多只能改变一个节点)。问在小Q游戏过程中，他的蛇蛇长度可以到多少？
比如

```shell
1 -4 10
3 -2 -1
2 -1 0
0 5 -2
```

最优路径为从最左侧的3开始，3 -> -4（利用能力变成4）->10。所以返回17。

暴力递归

```java
public static int walk1(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
        return 0;
    }
    int res = Integer.MIN_VALUE;
    for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
            int[] ans = process(matrix, i, j);
            res = Math.max(res, Math.max(ans[0], ans[1]));
        }
    }
    return res;
}

public static int[] process(int[][] m, int i, int j) {
    if (j == 0) {
        return new int[]{m[i][j], -m[i][j]};
    }
    int[] preAns = process(m, i, j - 1);
    int preUnUse = preAns[0];
    int preUse = preAns[1];
    if (i - 1 >= 0) {
        preAns = process(m, i - 1, j - 1);
        preUnUse = Math.max(preUnUse, preAns[0]);
        preUse = Math.max(preUse, preAns[1]);
    }
    if (i + 1 < m.length) {
        preAns = process(m, i + 1, j - 1);
        preUnUse = Math.max(preUnUse, preAns[0]);
        preUse = Math.max(preUse, preAns[1]);
    }
    int no = -1;
    int yes = -1;
    if (preUnUse >= 0) {
        no = m[i][j] + preUnUse;
        yes = -m[i][j] + preUnUse;
    }
    if (preUse >= 0) {
        yes = Math.max(yes, m[i][j] + preUse);
    }
    return new int[]{no, yes};

}
```

动态规划

```java
public static int walk2(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
        return 0;
    }
    int N = matrix.length;
    int M = matrix[0].length;
    int[][] dpNo = new int[N][M];
    int[][] dpYes = new int[N][M];
    int res = Integer.MIN_VALUE;
    //init first col
    for (int i = 0; i < N; i++) {
        dpNo[i][0] = matrix[i][0];
        dpYes[i][0] = -matrix[i][0];
        res = Math.max(res, Math.max(dpNo[i][0], dpYes[i][0]));
    }
    for (int j = 1; j < M; j++) {
        for (int i = 0; i < N; i++) {
            int preUnUse = dpNo[i][j - 1];
            int preUse = dpYes[i][j - 1];
            if (i > 0) {
                preUnUse = Math.max(preUnUse, dpNo[i - 1][j - 1]);
                preUse = Math.max(preUse, dpYes[i - 1][j - 1]);
            }
            if (i < N - 1) {
                preUnUse = Math.max(preUnUse, dpNo[i + 1][j - 1]);
                preUse = Math.max(preUse, dpYes[i + 1][j - 1]);
            }
            int no = -1;
            int yes = -1;
            if (preUnUse >= 0) {
                no = matrix[i][j] + preUnUse;
                yes = -matrix[i][j] + preUnUse;
            }
            if (preUse >= 0) {
                yes = Math.max(yes, preUse + matrix[i][j]);
            }
            dpNo[i][j] = no;
            dpYes[i][j] = yes;
            res = Math.max(res, Math.max(no, yes));
        }
    }
    return res;
}
```



## 题目七十二

计算表达式的值
str表示一个公式，公式里可能有整数、加减乘除符号和左右括号，返回公式的计算结果。
【举例】
str="48* ((70-65) - 43) + 8 * 1",返回 -1816。
str="3+1*4",返回7。
str="3+(1 * 4)",返回7。
【说明】

1. 可以认为给定的字符串一定是正确的公式，即不需要对str做公式有效性检查。

2. 如果是负数，就需要用括号括起来，比如“4*(-3)"。但如果负数作为公式的头或括号部分的开头，则可以没有括号，比如"-3 * 4"和"(-3 * 4)"都是合法的。

3. 不用考虑计算过程中会发生溢出的情况。

```java
public static int expressCompute(String s) {
    return f(s.toCharArray(), 0)[0];
}

public static int[] f(char[] str, int i) {
    LinkedList<String> queue = new LinkedList<>();
    int num = 0;
    int[] next = null;
    while (i < str.length && str[i] != ')') {
        if (isNum(str[i])) {
            //收集数字
            num = num * 10 + str[i++] - '0';
        } else if (str[i] != '(') {
            //遇到 + - * / 将前一个数字 和当前符号入栈 数字不能直接入栈
            addNum(queue, num);
            queue.addLast(String.valueOf(str[i++]));
            num = 0;
        } else {
            //遇到 (
            next = f(str, i + 1);
            num = next[0];
            i = next[1] + 1;
        }
    }
    addNum(queue, num);
    return new int[] {getNum(queue), i};
}
public static boolean isNum(char c) {
    return c >= '0' && c <= '9';
}

public static void addNum(LinkedList<String> queue, int num) {
    if (!queue.isEmpty()) {
        String top = queue.pollLast();
        //当前栈顶是+- 直接放回去
        if (top.equals("+") || top.equals("-")) {
            queue.addLast(top);
        } else {
            //如果是* / 需要再拿一个数出来和num计算 然后再将结果放回栈
            int cur = Integer.valueOf(queue.pollLast());
            num = top.equals("*") ? (cur * num) : (cur / num);
        }
    }
    queue.addLast(String.valueOf(num));
}

public static int getNum(LinkedList<String> queue) {
    int res = 0;
    int num = 0;
    String cur = null;
    boolean isAdd = true;
    while (!queue.isEmpty()) {
        cur = queue.pollFirst();
        if (cur.equals("+")) {
            isAdd = true;
        } else if (cur.equals("-")) {
            isAdd = false;
        } else {
            //数字
            num = Integer.parseInt(cur);
            res += isAdd ? num : (-num);
        }
    }
    return res;
}


public static void main(String[] args) {
    String str = "48*((70-65)-43)+8*1";
    System.out.println(expressCompute(str));
}
```



## 题目七十三

对于一个字符串，从前开始读和从后开始读是，一样的，我们就称这个字符串是回文串。例如"ABCBA""AA","A"是回文串，而"ABCD”,“AAB“不是回文串。牛牛特别喜欢回文串，他手中有一个字符串s，牛牛在思考能否从字符串中移除部分(0个或多个字符)使其变为回文串，并且牛牛认为空串不是回文串。牛牛发现移除的方案可能有很多种，希望你来帮他计算一下一共有多少种移除方案可以使s变为回文串。对于两种移除方案，如果移除的字符依次构成的序列不一样就是不同的方案。
例如，XXY4种ABA5种
【说明】这是今年的原题，提供的说明和例子都很让人费解。现在根据当时题目的所有测试用例，重新解释当时的题目
含义：
1)"1AB23CD21”,你可以选择删除A、B、C、D,然后剩下子序列{1,2,3,2,1}，只要剩下的子序列是同一个，那么就只算1种方法，和A、B、C、D选择什么样的删除顺序没有关系。
2)"121A1"其中有两个121的子序列，第一个{1,2,1}是由位置0，位置1，位置2构成，第二个{1,2,1}是由{位置0，位置1，位置4构成。这两
个子序列被认为是不同的子序列。
3)其实这道题是想求，str中有多少个不同的子序列，每一种子序列只对应一种删除方法，那就是把多余的东西去掉，而和去掉的顺序无关。
4)也许你觉得我的解释很荒谬，但真的是这样，不然解释不了为什么，XXY4种ABA5种，而且其他的测试用例都印证了这一点。

```java
public static int ways(String str) {
    char[] s = str.toCharArray();
    int N = s.length;
    int[][] dp = new int[N][N];
    for (int i = 0; i < N; i++) {
        dp[i][i] = 1;
    }
    for (int i = 0; i < N - 1; i++) {
        dp[i][i + 1] = s[i] == s[i + 1] ? 3 : 2;
    }
    for (int i = N - 3; i >= 0 ; i--) {
        for (int j = i + 2; j < N; j++) {
            dp[i][j] =dp[i + 1][j] + dp[i][j - 1] -  dp[i + 1][j - 1];
            if (s[i] == s[j]) {
                dp[i][j] += dp[i + 1][j - 1] + 1;
            }
        }
    }
    return dp[0][N - 1];
}

public static void main(String[] args) {
    System.out.println(ways("aba"));
}
```



## 题目七十四

给定一个正数1，裂开的方法有一种，(1)；给定一个正数2，裂开的方法有两种(1和1)、(2) ；给定一个正数3，裂开的方法有三种，(1、1、1)、(1、2)、(3)；给定一个正数4，裂开的方法有五种，(1、1、1、1)、(1、1、2)、(1、3)、(2、2)、(4)；  给定一个正数，求裂开的方法数。动态规划优化状态依赖的技巧

递归

```java
public static int ways(int n) {
        if (n < 1) {
            return 0;
        }
        return process(1, n);
    }

    public static int process(int pre, int rest) {
        if (rest == 0) {
            return 1;
        }
        if (pre > rest) {
            return 0;
        }
        int ways = 0;
        for (int i = pre; i <= rest; i++) {
            ways += process(i, rest - i);
        }
        return ways;
    }
```

动态规划

```java
public static int dp(int n) {
    if (n < 1) {
        return 0;
    }
    int[][] dp = new int[n + 1][n + 1];
    for (int i = 1; i <= n; i++) {
        dp[i][0] = 1;
    }
    for (int pre = n; pre >= 1; pre--) {
        for (int rest = pre; rest <= n; rest++) {
            int ways = 0;
            for (int i = pre; i <= rest; i++) {
                ways += dp[i][rest - i];
            }
            dp[pre][rest] = ways;
        }
    }
    return dp[1][n];
}
```

斜率优化后不需要枚举

```java
public static int dp1(int n) {
    if (n < 1) {
        return 0;
    }
    int[][] dp = new int[n + 1][n + 1];
    for (int i = 1; i <= n; i++) {
        dp[i][0] = 1;
    }
    dp[n][n] = 1;
    for (int pre = n - 1; pre >= 1; pre--) {
        for (int rest = pre; rest <= n; rest++) {
            dp[pre][rest] = dp[pre + 1][rest] + dp[pre][rest - pre];
        }
    }
    return dp[1][n];
}
```



## 题目七十五

给定一个正数N,代表你有1~N这些数字。在给定一个整数K。
你可以随意排列这些数字，但是每一种排列都有若干个逆序对。返回有多少种排列，正好有K个逆序对
例子1：Input:n = 3,k =0   Output:1
解释 只有[1,2,3]这一个排列有0个逆序对。
例子2：Input:n = 3,k=1  Output:2
解释 [3,2,1]有(3,2)、(3,1)、(2,1)三个逆序对，所以不达标。达标的只有：[1,3,2] [2,1,3]

动态规划

```java
public static int dp(int N, int K) {
    if (N < 1 || K < 0 || N * (N - 1) / 2 < K) {
        return 0;
    }
    if (K == 0) {
        return 1;
    }
    int[][] dp = new int[N + 1][K + 1];
    for (int i = 1; i <= N; i++) {
        dp[i][0] = 1;
    }
    for (int i = 2; i <= N; i++) {
        for (int j = 1; j <= K; j++) {
            for (int k = j; k >= Math.max(0, j - i + 1); k--) {
                dp[i][j] += dp[i - 1][k];
            }
        }
    }
    return dp[N][K];
}
```

斜率优化 去掉枚举行为

```java
public static int dp2(int N, int K) {
    if (N < 1 || K < 0 || N * (N - 1) / 2 < K) {
        return 0;
    }
    if (K == 0) {
        return 1;
    }
    int[][] dp = new int[N + 1][K + 1];
    for (int i = 1; i <= N; i++) {
        dp[i][0] = 1;
    }
    for (int i = 2; i <= N; i++) {
        for (int j = 1; j <= K; j++) {
            dp[i][j] = dp[i][j - 1] + dp[i - 1][j] - (i <= j ? dp[i - 1][j - i] : 0);
        }
    }
    return dp[N][K];
}
```

for test

```java
public static void main(String[] args) {
    int N = 10000;
    int K = 2000;
    long start = System.currentTimeMillis();
    System.out.println(dp(N, K));
    long end = System.currentTimeMillis();
    System.out.println("耗时: " + (end - start) + " ms");
    System.out.println(dp2(N, K));
    System.out.println("耗时: " + (System.currentTimeMillis() - end) + " ms");
}
```



## 题目七十六

给定一棵二叉树的头节点head,已知所有节点的值都不一样，返回其中最大的且符合搜索二叉树条件的最大拓扑结构的大小。
拓扑结构：不是子树，只要能连起来的结构都算。

```java
public static class Node {
    public int value;
    public Node left;
    public Node right;

    public Node(int value) {
        this.value = value;
    }
}


public static class Record {
    public int l;
    public int r;

    public Record(int l, int r) {
        this.l = l;
        this.r = r;
    }
}


public static int bstTopsSize(Node head) {
    Map<Node, Record> map = new HashMap<>();
    return postOrder(head, map);
}
private static int postOrder(Node root, Map<Node, Record> map) {
    if (root == null) {
        return 0;
    }
    int ls = postOrder(root.left, map);
    int rs = postOrder(root.right, map);
    //生成其左孩子节点的记录
    modifyMap(root.left, root.value, map, true);
    //生成其右孩子节点的记录
    modifyMap(root.right, root.value, map, false);
    //拿到左孩子的记录
    Record lr = map.get(root.left);
    //拿到右孩子的记录
    Record rr = map.get(root.right);
    //计算左孩子为头的拓扑贡献记录
    int lbst = lr == null ? 0 : lr.l + lr.r + 1;
    //计算右孩子为头的拓扑贡献记录
    int rbst = rr == null ? 0 : rr.l + rr.r + 1;
    //生成当前节点为头的拓扑贡献记录
    map.put(root, new Record(lbst, rbst));
    //判断以当前节点为头、以左/右孩子为头，二者哪个更大，找出所有节点的最大拓扑结构中最大的那个
    return Math.max(lbst + rbst + 1, Math.max(ls, rs));
}

public static int modifyMap(Node root, int v, Map<Node, Record> map, boolean isLNode) {
    if (root == null || !map.containsKey(root)) {
        return 0;
    }
    Record r = map.get(root);
    //root是左孩子且比头结点的值大，或root是右孩子且比头结点的值小，说明不满足BST，故删除
    if ((isLNode && root.value > v) || (!isLNode && root.value < v)) {
        map.remove(root);
        //返回总共删掉的节点
        return r.l + r.r + 1;
    } else {
        //root满足bst
        // 如果是左子树，则递归其右边界；如果是右子树，则递归其左边界
        int minus = modifyMap(isLNode ? root.right : root.left, v, map, isLNode);
        if (isLNode) {
            //左子树，则其右子树的贡献记录被更新
            r.r = r.r - minus;
        } else {
            //右子树，则其左子树的贡献记录被更新
            r.l = r.l - minus;
        }
        //更新后的记录同步到map中
        map.put(root, r);
        return minus;
    }
}
```



## 题目七十七

给定一个长度为偶数的数组arr,长度记为2N。前N个为左部分，后N个为右部分。arr就可以表示为[L1,L2,Ln,R1,R2,,Rn]请将数组调整成
[R1,L1,R2,L2,Rn,Ln]的样子。要求：时间复杂度O(N) 空间复杂度O(1) (完美洗牌问题)

```java
public static int modifyIndex1(int i, int len) {
    if (i <= len / 2) {
        return i * 2;
    }
    return 2 * (i - len / 2) - 1;
}

public static int modifyIndex2(int i, int len) {
    return (2 * i) % (len + 1);
}


public static void shuffle(int[] arr) {
    if (arr != null && arr.length != 0 && (arr.length & 1) == 0) {
        shuffle(arr, 0, arr.length - 1);
    }
}

public static void shuffle(int[] arr, int L, int R) {
    while (R - L + 1 > 0) {
        int len = R - L + 1;
        int base = 3;
        int k = 1;
        while (base <= (len + 1) / 3) {
            base *= 3;
            k++;
        }
        int half = (base - 1) / 2;
        int mid = (L + R) / 2;
        rotate(arr, L + half, mid, mid + half);
        cycles(arr, L, base - 1, k);
        L = L + base - 1;
    }
}

public static void cycles(int[] arr, int start, int len, int k) {
    for (int i = 0, trigger = 1; i < k; i++, trigger *= 3) {
        int preValue = arr[trigger + start - 1];
        int cur = modifyIndex2(trigger, len);
        while (cur != trigger) {
            int temp = arr[cur + start - 1];
            arr[cur + start - 1] = preValue;
            preValue = temp;
            cur = modifyIndex2(cur, len);
        }
        arr[cur + start - 1] = preValue;
    }
}

public static void rotate(int[] arr, int L, int M, int R) {
    reverse(arr, L, M);
    reverse(arr, M + 1, R);
    reverse(arr, L, R);
}

public static void reverse(int[] arr, int L, int R) {
    while (L < R) {
        int temp = arr[L];
        arr[L++] = arr[R];
        arr[R--] = temp;
    }
}

public static void main(String[] args) {
    int[] arr = {1,2,3,4,5,6,7,8};
    shuffle(arr);
    System.out.println(Arrays.toString(arr));
}
```



## 题目七十八

一个不含有负数的数组可以代表一圈环形山，每个位置的值代表山的高度。比如，{3,1,2,4,5}、{4,5,3,1,2或{1,2,4,5,3}都代表同样结构的环形山。山峰A和山峰B能够相互看见的条件为：

1.如果A和B是同一座山，认为不能相互看见。

2.如果A和B是不同的山，并且在环中相邻，认为可以相互看见。

3.如果A和B是不同的山，并且在环中不相邻，假设两座山高度的最小值为min。
1)如果A通过顺时针方向到B的途中没有高度比min大的山峰，认为A和B可以相互看见
2)如果A通过逆时针方向到B的途中没有高度比min大的山峰，认为A和B可以相互看见
3)两个方向只要有一个能看见，就算A和B可以相互看见给定一个不含有负数且没有重复值的数组arr,请返回有多少对山峰能够相互看见。
进阶：给定一个不含有负数但可能含有重复值的数组r,返回有多少对山峰能够相互看见。

单调栈

```java
public static class Record {
    public int value;
    public int times;

    public Record(int value) {
        this.value = value;
        this.times = 1;
    }
}

public static int nextIndex(int i, int size) {
    return i < size - 1 ? i  + 1 : 0;
}
public static int nextIndex1(int i, int size) {
    return (i + 1) % size;
}
public static int lastIndex(int i, int size) {
    return i > 0 ? i - 1 : size - 1;
}
public static int lastIndex1(int i, int size) {
    return (i + size - 1) % size;
}



public static int getVisibleNum(int[] arr) {
    if (arr == null || arr.length < 2) {
        return 0;
    }
    int N = arr.length;
    int maxIndex = 0;
    for (int i = 0; i < N; i++) {
        maxIndex = arr[maxIndex] < arr[i] ? i : maxIndex;
    }
    Stack<Record> stack = new Stack<>();
    stack.push(new Record(arr[maxIndex]));

    int index = nextIndex1(maxIndex, N);
    int res = 0;
    while (index != maxIndex) {
        while (stack.peek().value < arr[index]) {
            int k = stack.pop().times;
            res += getInternalSum(k) + 2 * k;
        }
        if (stack.peek().value == arr[index]) {
            stack.peek().times++;
        } else {
            stack.push(new Record(arr[index]));
        }
        index = nextIndex1(index, N);
    }
    while (stack.size() > 2) {
        int times = stack.pop().times;
        res += getInternalSum(times) + 2 * times;
    }
    if (stack.size() == 2) {
        int times = stack.pop().times;
        res += getInternalSum(times) + stack.peek().times == 1 ? times : 2 * times;
    }
    res += getInternalSum(stack.pop().times);
    return res;
}

private static int getInternalSum(int k) {
    return k == 1 ? 0 : (k * (k - 1) / 2);
}
```



## 题目七十九

一棵二叉树原本是搜索二叉树，但是其中有两个节点调换了位置，使得这棵二叉树不再是搜索二叉树，请找到这两个错误节点并返回。已知二叉树中所有节点的值都不一样，给定二叉树的头节点head,返回一个长度为2的二叉树节点类型的数组errs,errs[0]表示一个错误节点，errs[1]表示另一个错误节点。
进阶：如果在原问题中得到了这两个错误节点，我们当然可以通过交换两个节点的节点值的方式让整棵二叉树重新成为搜索二叉树。但现在要求你不能这么做，而是在结构上完全交换两个节点的位置，请实现调整的函数

```java
private TreeNode firstElement = null;
    private TreeNode secondElement = null;
    private TreeNode lastElement = new TreeNode(Integer.MIN_VALUE); 
    
    private void traverse(TreeNode root) {
        if (root == null) {
            return;
        }
        traverse(root.left);
        if (firstElement == null && root.val < lastElement.val) {
            firstElement = lastElement;
        }
        if (firstElement != null && root.val < lastElement.val) {
            secondElement = root;
        }
        lastElement = root;
        traverse(root.right);
    }
    
    public void recoverTree(TreeNode root) {
        // traverse and get two elements
        traverse(root);
        // swap
        int temp = firstElement.val;
        firstElement.val = secondElement.val;
        secondElement.val = temp;
    }
```



## 题目八十

给定一个非负数组arr,和一个正数m，返回arr的所有子序列中累加和%m之后的最大值。

```java
//暴力解
    public static int max0(int[] arr, int m) {
        HashSet<Integer> set = new HashSet<>();
        process(arr, 0, 0, set);
        int max = 0;
        for (Integer sum : set) {
            max = Math.max(max, sum % m);
        }
        return max;
    }
    public static void process(int[] arr, int index, int sum, HashSet<Integer> set) {
        if (index == arr.length) {
            set.add(sum);
            return;
        }
        process(arr, index + 1, sum, set);
        process(arr, index + 1, sum + arr[index], set);
    }
    
    //m比较小 sum比较大
    public static int max1(int[] arr, int m) {
        int N = arr.length;
        boolean[][] dp = new boolean[N][m];
        for (int i = 0; i < N; i++) {
            dp[i][0] = true;
        }
        dp[0][arr[0] % m] = true;
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < m; j++) {
                dp[i][j] = dp[i - 1][j];
                int cur = arr[i] % m;
                if (j - cur >= 0) {
                    dp[i][j] = dp[i][j] | dp[i - 1][j - cur];
                }
                if (j - cur < 0) {
                    dp[i][j] = dp[i][j] | dp[i - 1][m + j - cur];
                }
            }
        }
        for (int j = m - 1; j >= 0; j--) {
            if (dp[N - 1][j]) {
                return j;
            }
        }
        return 0;
    }
    //sum比较小，m比较大
    public static int max2(int[] arr, int m) {
        int sum = 0;
        int N = arr.length;
        for (int i = 0; i < N; i++) {
            sum += arr[i];
        }
        boolean[][] dp = new boolean[N][sum + 1];
        for (int i = 0; i < N; i++) {
            dp[i][0] = true;
        }
        dp[0][arr[0]] = true;
        for (int i = 1; i < N; i++) {
            for (int j = 1; j <= sum; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j - arr[i] >= 0) {
                    dp[i][j] = dp[i][j] | dp[i - 1][j - arr[i]];
                }
            }
        }
        int ans = 0;
        for (int i = 0; i <= sum; i++) {
            if (dp[N - 1][i]) {
                ans = Math.max(ans, i % m);
            }
        }
        return ans;
    }

    //sum和m都比较大 数组的长度比较小
    //分治
    public static int max3(int[] arr, int m) {
        if (arr.length == 1) {
            return arr[0] % m;
        }
        int mid = (arr.length - 1) / 2;
        TreeSet<Integer> set1 = new TreeSet<>();
        process3(arr, 0, mid, 0, m, set1);
        TreeSet<Integer> set2 = new TreeSet<>();
        process3(arr, mid + 1, arr.length - 1, 0, m, set2);
        int ans = 0;
        for (Integer left : set1) {
            ans = Math.max(ans, left + set2.floor(m - 1 - left));
        }
        return ans;
    }
    public static void process3(int[] arr, int index, int end, int sum, int m, TreeSet<Integer> sortSet) {
        if (index == end + 1) {
            sortSet.add(sum % m);
            return;
        }
        process3(arr, index + 1, end, sum, m, sortSet);
        process3(arr, index + 1, end, sum + arr[index], m, sortSet);
    }


		//测试数据
    public static int[] generateRandomArray(int len, int value) {
        int[] ans = new int[(int) (Math.random() * len) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * value);
        }
        return ans;
    }

    public static void main(String[] args) {
        int len = 10;
        int value = 100;
        int m = 76;
        int testTime = 500000;
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(len, value);
            int ans1 = max1(arr, m);
            int ans2 = max2(arr, m);
            int ans3 = max3(arr, m);
            int ans4 = max0(arr, m);
            if (ans1 != ans2 || ans2 != ans3 || ans3 != ans4) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish!");

    }
```



## 题目八十一

项目有四个信息
1)哪个项目经理提的
2)被项目经理润色出来的时间点

3)项目优先级
4)项目花费的时间
项目经理们可以提交项目给程序员们，程序员可以做这些项目
比如长度为4的数组[1,3,2,2]，表示1号项目经理提的，被项目经理润色出来的时间点是3，优先级2，花费程序员2个时间。
所以给二个N*4的矩阵，就可以代表N个项目
给定一个正数pm,表示项目经理的数量，每个项目经理只负责自己的那些项目，并且一次只能提交一个项目给程序员们，这个提交的项目做完了才能更次提交。经理对项目越喜欢，就会越早提交。一个项自优先级越高越被喜欢；如果优先级一样，花费时间越少越喜欢：如果还一样，被项目经理润色出来的时间点越早越喜欢。给定一个正数sde, 表示程序员的数量，所有经理提交了的项目，程序员会选择自己喜欢的项目做，每个人做完了一个项目，然后才会再来挑选。当程序员在挑选项目时，有自己的喜欢标准，有自己的喜欢标准。一个项目花费时间越少越被喜欢；如果花费时间一样，该项目的负责人编号越小越被喜欢。返回一个长度为N的数组，表示N个项目的结束时间。
比如：
int pms 2:
int sde =2:
int programs={{1,1,1,2}, {1,2,1,1}, {1,3,2,2},  {2,1,1,2},  {2,3,5,5}}
返回{3,4,5,3,9}

```java
public class Code6 {

    public static class Program {
        public int index;
        public int pm;
        public int start;
        public int rank;
        public int cost;

        public Program(int index, int pmNum, int start, int rank, int cost) {
            this.index = index;
            this.start = start;
            this.rank = rank;
            this.cost = cost;
            this.pm = pmNum;
        }
    }

    public static class PmLoveRule implements Comparator<Program> {

        @Override
        public int compare(Program o1, Program o2) {
            if (o1.rank != o2.rank) {
                return o1.rank - o2.rank;
            }
            if (o1.cost != o2.cost) {
                return o1.cost - o2.cost;
            }
            return o1.start - o2.start;
        }
    }

    public static class BigQueues {
        private List<PriorityQueue<Program>> pmQueues;

        private Program[] sdeHeap;
        //indexes[i] -> i号pm的堆项目，在sde堆中的位置
        private int[] indexes;
        private int heapSize;

        public BigQueues(int pmNum) {
            this.heapSize = 0;
            this.sdeHeap = new Program[pmNum];
            this.indexes = new int[pmNum + 1];
            for (int i = 0; i <= pmNum; i++) {
                indexes[i] = -1;
            }
            this.pmQueues = new ArrayList<>();
            for (int i = 0; i <= pmNum; i++) {
                this.pmQueues.add(new PriorityQueue<>(new PmLoveRule()));
            }
        }


        public boolean isEmpty() {
            return heapSize == 0;
        }

        public void add(Program program) {
            PriorityQueue<Program> pmHeap = pmQueues.get(program.pm);
            pmHeap.add(program);
            Program head = pmHeap.peek();
            int heapIndex = indexes[head.pm];
            if (heapIndex == -1) {
                sdeHeap[heapSize] = head;
                indexes[head.pm] = heapSize;
                heapInsert(heapSize++);
            } else {
                sdeHeap[heapIndex] = head;
                heapInsert(heapIndex);
                heapify(heapIndex);
            }
        }




        public Program pop() {
            Program heap = sdeHeap[0];
            PriorityQueue<Program> queue = pmQueues.get(heap.pm);
            queue.poll();
            if (queue.isEmpty()) {
                swap(0, heapSize - 1);
                sdeHeap[--heapSize] = null;
                indexes[heap.pm] = -1;
            } else {
                sdeHeap[0] = queue.peek();
            }
            heapify(0);
            return heap;
        }

        private void heapInsert(int index) {
            while (index != 0) {
                int parent = (index - 1) / 2;
                if (sdeLoveRule(sdeHeap[parent], sdeHeap[index]) > 0) {
                    swap(parent, index);
                    index = parent;
                } else {
                    break;
                }
            }
        }
        private void heapify(int index) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int best = index;
            while (left < heapSize) {
                if (sdeLoveRule(sdeHeap[left], sdeHeap[index])< 0) {
                    best = left;
                }
                if (right < heapSize && sdeLoveRule(sdeHeap[right], sdeHeap[best]) < 0) {
                    best = right;
                }
                if (best == index) {
                    break;
                }
                swap(best, index);
                index = best;
                left = index * 2 + 1;
                right = index * 2 + 2;
            }
        }

        private void swap(int index1, int index2) {
            Program t = sdeHeap[index1];
            sdeHeap[index1] = sdeHeap[index2];
            sdeHeap[index2] = t;
        }

        private int sdeLoveRule(Program p1, Program p2) {
            if (p1.cost != p2.cost) {
                return p1.cost - p2.cost;
            } else {
                return p1.pm - p2.pm;
            }
        }

    }


    public static class StartRule implements Comparator<Program> {

        @Override
        public int compare(Program o1, Program o2) {
            return o1.start - o2.start;
        }

    }



    public static int[] workFinish(int pms, int sdes, int[][] programs) {
        PriorityQueue<Program> startQueue = new PriorityQueue<>(new StartRule());
        for (int i = 0; i < programs.length; i++) {
            Program program = new Program(i, programs[i][0], programs[i][1], programs[i][2], programs[i][3]);
            startQueue.add(program);
        }

        PriorityQueue<Integer> sdeWakeQueue = new PriorityQueue<>();
        for (int i = 0; i < sdes; i++) {
            sdeWakeQueue.add(1);
        }

        BigQueues bigQueues = new BigQueues(pms);
        int finish = 0;
        int[] ans = new int[programs.length];
        while (finish != ans.length) {
            int sdeWakeTime = sdeWakeQueue.poll();
            while (!startQueue.isEmpty()) {
                if (startQueue.peek().start > sdeWakeTime) {
                    break;
                }
                bigQueues.add(startQueue.poll());
            }
            if (bigQueues.isEmpty()) {
                sdeWakeQueue.add(startQueue.peek().start);
            } else {
                Program program = bigQueues.pop();
                ans[program.index] = sdeWakeTime + program.cost;
                sdeWakeQueue.add(ans[program.index]);
                finish++;
            }
        }
        return ans;
    }


    public static void main(String[] args) {
        int pms = 2;
        int sde = 2;
        int[][] programs = { { 1, 1, 1, 2 }, { 1, 2, 1, 1 }, { 1, 3, 2, 2 }, { 2, 1, 1, 2 }, { 2, 3, 5, 5 } };
        int[] ans = workFinish(pms, sde, programs);
        printArray(ans);
    }

    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}
```



## 题目八十二

自由之路https://leetcode.cn/problems/freedom-trail/

暴力递归(dfs)

```java
class Solution {
    public int findRotateSteps(String r, String key) {
        char[] ring = r.toCharArray();
        int size = ring.length;
        HashMap<Character, ArrayList<Integer>> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            if (!map.containsKey(ring[i])) {
                map.put(ring[i], new ArrayList<>());
            }
            map.get(ring[i]).add(i);
        }
        return minSteps1(0, 0, key.toCharArray(), map, size);
    }

    public int minSteps1(int preStrIndex, int keyIndex, char[] key, HashMap<Character, ArrayList<Integer>> map, int size) {
        if (keyIndex == key.length) {
            return 0;
        }
        int ans = Integer.MAX_VALUE;
        for (int curStrIndex : map.get(key[keyIndex])) {
            int step = dial(preStrIndex, curStrIndex, size) + 1 + minSteps1(curStrIndex, keyIndex + 1, key, map, size);
            ans = Math.min(ans, step);
        }
        return ans;
    }

    
    public int dial(int index1, int index2, int size) {
        return Math.min(Math.abs(index1 - index2), Math.min(index1, index2) + size - Math.max(index1, index2));
    }
}
```

记忆化搜索

```java
class Solution {
    public int findRotateSteps(String r, String key) {
        char[] ring = r.toCharArray();
        int size = ring.length;
        HashMap<Character, ArrayList<Integer>> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            if (!map.containsKey(ring[i])) {
                map.put(ring[i], new ArrayList<>());
            }
            map.get(ring[i]).add(i);
        }
        return minSteps2(0, 0, key.toCharArray(), map, size, new int[size][key.length()]);
    }

    public int minSteps2(int preStrIndex, int keyIndex, char[] key, HashMap<Character, ArrayList<Integer>> map, int size, int[][] cache) {
        if (keyIndex == key.length) {
            return 0;
        }
        if (cache[preStrIndex][keyIndex] != 0) {
            return cache[preStrIndex][keyIndex];
        }
        int ans = Integer.MAX_VALUE;
        for (int curStrIndex : map.get(key[keyIndex])) {
            int step = dial(preStrIndex, curStrIndex, size) + 1 + minSteps2(curStrIndex, keyIndex + 1, key, map, size, cache);
            ans = Math.min(ans, step);
        }
        cache[preStrIndex][keyIndex] = ans;
        return ans;
    }


    public int dial(int index1, int index2, int size) {
        return Math.min(Math.abs(index1 - index2), Math.min(index1, index2) + size - Math.max(index1, index2));
    }
}
```

动态规划

```java
class Solution {
    public int findRotateSteps(String r, String key) {
        char[] ring = r.toCharArray();
        int size = ring.length;
        HashMap<Character, ArrayList<Integer>> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            if (!map.containsKey(ring[i])) {
                map.put(ring[i], new ArrayList<>());
            }
            map.get(ring[i]).add(i);
        }
        return dp(size, key.length(), key.toCharArray(), map);
    }

    
    public int dp(int size, int len, char[] key, HashMap<Character, ArrayList<Integer>> map) {
        int[][] dp = new int[size][len + 1];
        for (int j = len - 1; j >= 0; j--) {
            for (int i = size - 1; i >= 0; i--) {
                int ans = Integer.MAX_VALUE;
                for (int curStrIndex : map.get(key[j])) {
                    int step = dial(i, curStrIndex, size) + 1 + dp[curStrIndex][j + 1];
                    ans = Math.min(ans, step);
                }
                dp[i][j] = ans;
            }
        }
        return dp[0][0];
    }


    public int dial(int index1, int index2, int size) {
        return Math.min(Math.abs(index1 - index2), Math.min(index1, index2) + size - Math.max(index1, index2));
    }
}
```



## 题目八十三

有 `n` 个气球，编号为`0` 到 `n - 1`，每个气球上都标有一个数字，这些数字存在数组 `nums` 中。

现在要求你戳破所有的气球。戳破第 `i` 个气球，你可以获得 `nums[i - 1] * nums[i] * nums[i + 1]` 枚硬币。 这里的 `i - 1` 和 `i + 1` 代表和 `i` 相邻的两个气球的序号。如果 `i - 1`或 `i + 1` 超出了数组的边界，那么就当它是一个数字为 `1` 的气球。

求所能获得硬币的最大数量。

**示例 1：**

```
输入：nums = [3,1,5,8]
输出：167
解释：
nums = [3,1,5,8] --> [3,5,8] --> [3,8] --> [8] --> []
coins =  3*1*5    +   3*5*8   +  1*3*8  + 1*8*1 = 167
```

**示例 2：**

```
输入：nums = [1,5]
输出：10
```

**提示：**

- `n == nums.length`
- `1 <= n <= 300`
- `0 <= nums[i] <= 100`



暴力递归

```java
public static int process(int[] arr, int L, int R) {
    if (L == R) {
        return arr[L - 1] * arr[L] * arr[R + 1];
    }
    int max = Math.max(
            arr[L - 1] * arr[L] * arr[R + 1] + process(arr, L + 1, R),
            arr[L - 1] * arr[R] * arr[R + 1] + process(arr, L, R - 1));
    for (int i = L + 1; i < R; i++) {
        max = Math.max(max, arr[L - 1] * arr[i] * arr[R + 1] + process(arr, L, i - 1) + process(arr, i + 1, R));
    }
    return max;
}

public static int maxCoins1(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    if (arr.length == 1) {
        return arr[0];
    }
    int N = arr.length;
    int[] help = new int[N + 2];
    help[0] = 1;
    help[N + 1] = 1;
    for (int i = 0; i < N; i++) {
        help[i + 1] = arr[i];
    }
    return process(help, 1, N);
}
```

记忆化搜索

```java
public static int maxCoins2(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    if (arr.length == 1) {
        return arr[0];
    }
    int N = arr.length;
    int[] help = new int[N + 2];
    help[0] = 1;
    help[N + 1] = 1;
    for (int i = 0; i < N; i++) {
        help[i + 1] = arr[i];
    }
    return process2(help, 1, N, new int[help.length][help.length]);
}

public static int process2(int[] arr, int L, int R, int[][] cache) {
    if (L == R) {
        return arr[L - 1] * arr[L] * arr[R + 1];
    }
    if (cache[L][R] != 0) {
        return cache[L][R];
    }
    int max = Math.max(
            arr[L - 1] * arr[L] * arr[R + 1] + process2(arr, L + 1, R, cache),
            arr[L - 1] * arr[R] * arr[R + 1] + process2(arr, L, R - 1, cache));
    for (int i = L + 1; i < R; i++) {
        max = Math.max(max, arr[L - 1] * arr[i] * arr[R + 1] + process2(arr, L, i - 1, cache) + process2(arr, i + 1, R, cache));
    }
    cache[L][R] = max;
    return max;
}
```

动态规划

```java
public int maxCoins(int[] nums) {
        int N = nums.length;
        int[][] dp = new int[N + 2][N + 2];
        int[] help = new int[N + 2];
        help[0] = help[N + 1] = 1;
        for (int i = 0; i < N; i++) {
            help[i + 1] = nums[i];
        }
        for (int L = N - 1; L >= 0; L--) {
            for (int R = L + 2; R <= N + 1; R++) {
                for (int i = L + 1; i < R; i++) {
                    dp[L][R] = Math.max(dp[L][R], help[L] * help[i] * help[R] + dp[L][i] + dp[i][R]);
                }
            }
        }
        return dp[0][N + 1];
    }
```



## 题目八十四

正则表达式匹配

给你一个字符串 `s` 和一个字符规律 `p`，请你来实现一个支持 `'.'` 和 `'*'` 的正则表达式匹配。

- `'.'` 匹配任意单个字符
- `'*'` 匹配零个或多个前面的那一个元素

所谓匹配，是要涵盖 **整个** 字符串 `s`的，而不是部分字符串。

**示例 1：**

```
输入：s = "aa", p = "a"
输出：false
解释："a" 无法匹配 "aa" 整个字符串。
```

**示例 2:**

```
输入：s = "aa", p = "a*"
输出：true
解释：因为 '*' 代表可以匹配零个或多个前面的那一个元素, 在这里前面的元素就是 'a'。因此，字符串 "aa" 可被视为 'a' 重复了一次。
```

**示例 3：**

```
输入：s = "ab", p = ".*"
输出：true
解释：".*" 表示可匹配零个或多个（'*'）任意字符（'.'）。
```

**提示：**

- `1 <= s.length <= 20`
- `1 <= p.length <= 30`
- `s` 只包含从 `a-z` 的小写字母。
- `p` 只包含从 `a-z` 的小写字母，以及字符 `.` 和 `*`。
- 保证每次出现字符 `*` 时，前面都匹配到有效的字符



暴力递归

```java
public static boolean isMatch(String s, String p) {
        if (s == null || p == null) {
            return false;
        }
        char[] str = s.toCharArray();
        char[] exp = p.toCharArray();
        return isValid(str, exp) && process(str, exp, 0, 0);
    }
public static boolean isValid(char[] str, char[] exp) {
        for (int i = 0; i < str.length; i++) {
            if (str[i] == '*' || str[i] == '.') {
                return false;
            }
        }
        for (int i = 0; i < exp.length; i++) {
            if (exp[i] == '*' && (i == 0 || exp[i - 1] == '*')) {
                return false;
            }
        }
        return true;
    }
public static boolean process(char[] str, char[] exp, int si, int ei) {
    if (ei == exp.length) {
        return si == str.length;
    }
    //ei + 1 的位置不是*
    if (ei + 1 == exp.length || exp[ei + 1] != '*') {
        return si != str.length && (exp[ei] == str[si] || exp[ei] == '.') && process(str, exp, si + 1, ei + 1);
    }
    while (si != str.length && (exp[ei] == str[si] || exp[ei] == '.')) {
         if (process(str, exp, si, ei + 2)) {
             return true;
         }
         si++;
    }
    return process(str, exp, si, ei + 2);
}
```

记忆化搜索

```java
class Solution {
    public static boolean isMatch(String s, String p) {
        if (s == null || p == null) {
            return false;
        }
        char[] str = s.toCharArray();
        char[] exp = p.toCharArray();
        return isValid(str, exp) && process2(str, exp, 0, 0, new int[str.length + 1][exp.length + 1]);
    }

    public static boolean isValid(char[] str, char[] exp) {
        for (int i = 0; i < str.length; i++) {
            if (str[i] == '*' || str[i] == '.') {
                return false;
            }
        }
        for (int i = 0; i < exp.length; i++) {
            if (exp[i] == '*' && (i == 0 || exp[i - 1] == '*')) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean process2(char[] str, char[] exp, int si, int ei, int[][] cache) {
        if (cache[si][ei] != 0) {
            return cache[si][ei] == 1;
        }
        boolean ans = false;
        if (ei == exp.length) {
            ans = si == str.length;
            cache[si][ei] = ans ? 1 : -1;
            return ans;
        }
        //ei + 1 的位置不是*
        if (ei + 1 == exp.length || exp[ei + 1] != '*') {
            ans = si != str.length && (exp[ei] == str[si] || exp[ei] == '.') && process2(str, exp, si + 1, ei + 1, cache);
            cache[si][ei] = ans ? 1 : -1;
            return ans;
        }
        while (si != str.length && (exp[ei] == str[si] || exp[ei] == '.')) {
            if (process2(str, exp, si, ei + 2, cache)) {
                cache[si][ei] = 1;
                return true;
            }
            si++;
        }
        ans = process2(str, exp, si, ei + 2, cache);
        cache[si][ei] = ans ? 1 : -1;
        return ans;
    }
}
```

动态规划

```java
public static boolean isMatch3(String str, String pattern) {
		if (str == null || pattern == null) {
			return false;
		}
		char[] s = str.toCharArray();
		char[] p = pattern.toCharArray();
		if (!isValid(s, p)) {
			return false;
		}
		int N = s.length;
		int M = p.length;
		boolean[][] dp = new boolean[N + 1][M + 1];
		dp[N][M] = true;
		for (int j = M - 1; j >= 0; j--) {
			dp[N][j] = (j + 1 < M && p[j + 1] == '*') && dp[N][j + 2];
		}
		// dp[0..N-2][M-1]都等于false，只有dp[N-1][M-1]需要讨论
		if (N > 0 && M > 0) {
			dp[N - 1][M - 1] = (s[N - 1] == p[M - 1] || p[M - 1] == '.');
		}
		for (int i = N - 1; i >= 0; i--) {
			for (int j = M - 2; j >= 0; j--) {
				if (p[j + 1] != '*') {
					dp[i][j] = ((s[i] == p[j]) || (p[j] == '.')) && dp[i + 1][j + 1];
				} else {
					if ((s[i] == p[j] || p[j] == '.') && dp[i + 1][j]) {
						dp[i][j] = true;
					} else {
						dp[i][j] = dp[i][j + 2];
					}
				}
			}
		}
		return dp[0][0];
	}
```



## 题目八十五

数组arr相邻的k个合并为1个数字，最终将arr合并为1个数字的最小代价

暴力递归

```java
public static int mergeStones1(int[] stones, int K) {
    int N = stones.length;
    if ((N - 1) % (K - 1) > 0) {
        return -1;
    }
    int[] presum  = new int[N + 1];
    for (int i = 0; i < N; i++) {
        presum[i + 1] = presum[i] + stones[i];
    }
    return process1(0, N - 1, 1, presum, K, presum);
}

public static int process1(int L, int R, int part, int[] arr, int K, int[] presum) {
    if (L == R) {
        return part == 1 ? 0 : -1;
    }
    if (part == 1) {
        int next = process1(L, R, K, arr, K, presum);
        if (next == -1) {
            return -1;
        }
        return next + presum[R + 1] - presum[L];
    }
    int ans = Integer.MAX_VALUE;
    for (int mid = L; mid < R; mid += K - 1) {
        int next1 = process1(L, mid, 1, arr, K, presum);
        int next2 = process1(mid + 1, R, part - 1, arr, K, presum);
        if (next1 != -1 && next2 != -1) {
            ans = Math.min(ans, next1 + next2);
        }
    }
    return ans;
}
```

记忆化搜索

```java
public static int mergeStones2(int[] stones, int K) {
    int N = stones.length;
    if ((N - 1) % (K - 1) > 0) {
        return -1;
    }
    int[] presum = new int[N + 1];
    for (int i = 0; i < N; i++) {
        presum[i + 1] = presum[i] + stones[i];
    }
    int[][][] dp = new int[N][N][K + 1];
    return process2(0, N - 1, 1, stones, K, presum, dp);
}

public static int process2(int L, int R, int part, int[] arr, int K, int[] presum, int[][][] dp) {
    if (dp[L][R][part] != 0) {
        return dp[L][R][part];
    }
    if (L == R) {
        return part == 1 ? 0 : -1;
    }
    if (part == 1) {
        int next = process2(L, R, K, arr, K, presum, dp);
        if (next == -1) {
            dp[L][R][part] = -1;
            return -1;
        }
        dp[L][R][part] = next + presum[R + 1] - presum[L];
        return dp[L][R][part];
    }
    int ans = Integer.MAX_VALUE;
    for (int mid = L; mid < R; mid += K - 1) {
        int next1 = process2(L, mid, 1, arr, K, presum, dp);
        int next2 = process2(mid + 1, R, part - 1, arr, K, presum, dp);
        if (next1 != -1 && next2 != -1) {
            ans = Math.min(ans, next1 + next2);
        } else {
            dp[L][R][part] = -1;
            return -1;
        }
    }
    dp[L][R][part] = ans;
    return ans;
}
```

测试

```java
// for test
public static int[] generateRandomArray(int maxSize, int maxValue) {
    int[] arr = new int[(int) (maxSize * Math.random()) + 1];
    for (int i = 0; i < arr.length; i++) {
        arr[i] = (int) ((maxValue + 1) * Math.random());
    }
    return arr;
}

// for test
public static void printArray(int[] arr) {
    if (arr == null) {
        return;
    }
    for (int i = 0; i < arr.length; i++) {
        System.out.print(arr[i] + " ");
    }
    System.out.println();
}

public static void main(String[] args) {
    int maxSize = 12;
    int maxValue = 100;
    System.out.println("Test begin");
    for (int testTime = 0; testTime < 100000; testTime++) {
        int[] arr = generateRandomArray(maxSize, maxValue);
        int K = (int) (Math.random() * 7) + 2;
        int ans1 = mergeStones1(arr, K);
        int ans2 = mergeStones2(arr, K);
        if (ans1 != ans2) {
            System.out.println(ans1);
            System.out.println(ans2);
        }
    }

}
```



## 题目八十六

给定字符串str1和str2,求str1的子串中含有str2所有字符的最小子串长度
【举例】str1="abcde",str2="ac"，因为"abc"包含str2所有的字符，并且在满足这一条件的str1的所有子串中，"abc“是最短的，返回3；str1="12345",str2="344"最小包含子串不存在，返回0。

滑动窗口

```java
public static int minLength(String s1, String s2) {
    if (s1 == null || s2 == null || s1.length() < s2.length()) {
        return -1;
    }
    char[] str1 = s1.toCharArray();
    char[] str2 = s2.toCharArray();
    int[] map = new int[256];
    for (int i = 0; i < str2.length; i++) {
        map[str2[i]]++;
    }
    int left = 0;
    int right = 0;
    int all = str2.length;
    int ans = Integer.MAX_VALUE;
    while (right < str1.length) {
        map[str1[right]]--;
        if (map[str1[right]] >= 0) {
            all--;
        }
        if (all == 0) {
            while (map[str1[left]] < 0) {
                map[str1[left++]]++;
            }
            ans = Math.min(ans, right - left + 1);
            all++;
            map[str1[left++]]++;
        }
        right++;
    }
    return ans == Integer.MAX_VALUE ? 0 : ans;
}
```



## 题目八十七

互为旋变串

## 题目八十八

N个加油站组成一个环形，给定两个长度都是N的非负数组oil和dis(N>1), oil[代表第i个加油站存的油可以跑多少千米，dis代表第i个加油站到环中下一个加油站相隔多少千米。假设你有一辆油箱足够大的车，初始时车里没有油。如果车从第i个加油站出发，最终可以回到这个加油站，那么第i个加油站就算良好出发点，否则就不算。请返回长度为N的boolean型数组res,res[i]代表第ⅰ个加油站是不是良好出发点。



## 题目八十九

LFU  O(1)的解



## 题目九十

给定一个数组arr,给定一个正数k。选出3个不重叠的子数组，每个子数组长度都是k,返回最大的三子数组的最大和。



## 题目九十一

一群孩子做游戏，现在请你根据游戏得分来发糖果，
要求如下：1.每个孩子不管得分多少，起
码分到1个糖果。2任意两个相邻的孩子之间，得分较多的孩子必须拿多一些的糖果。给定
一个数组arr代表得分数组，请返回最少需要多少糖果。例如：arr=[1,2,2],
糖果分配为[1,2,1]
即可满足要求且数量最少，所以返回4。
【进阶】
原题目中的两个规则不变，再加一条规则：3.任意两个相邻的孩子之间如果得分一样，糖果数
必须相同。
给定
一个数组，arr代表得分数组，返回最少需要多少糖果。例如arr=[1,2,2],
糖果分配为
「1,2,21，即可满足要求且数量最少，所以返回5。
【要求】
r长度为N,原题与进阶题都要求时间复杂度为O(N),额外空间复杂度为O(1)。



## 题目九十二

给定一棵二叉树的头节点head,如果在某一个节点x上放置相机，那么x的父节点、x的所有子节点以及x都可以被覆盖。返回如果要把所有数都覆盖，至少需要多少个相机。

暴力解

```java
public static class Node {
    public Node left;
    public Node right;
}

public static class Info {
    //没被覆盖
    public long uncovered;
    //覆盖了但是没相机
    public long coveredNoCamera;
    //覆盖了但是有相机
    public long coveredHasCamera;

    public Info(long uncovered, long coveredNoCamera, long coveredHasCamera) {
        this.uncovered = uncovered;
        this.coveredNoCamera = coveredNoCamera;
        this.coveredHasCamera = coveredHasCamera;
    }
}

public static Info process1(Node x) {
    if (x == null) {
        return new Info(Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
    }
    Info left = process1(x.left);
    Info right = process1(x.right);

    long uncovered = left.coveredNoCamera + right.coveredNoCamera;

    long coveredNoCamera = Math.min(
            left.coveredHasCamera + right.coveredHasCamera,
            Math.min(
                        left.coveredHasCamera + right.coveredNoCamera,
                        left.coveredNoCamera + right.coveredHasCamera
            )
    );
    long coveredHasCamera = Math.min(left.uncovered, Math.min(left.coveredHasCamera, left.coveredNoCamera))
            +
            Math.min(right.uncovered, Math.min(right.coveredNoCamera, right.coveredHasCamera))
            + 1;
    return new Info(uncovered, coveredNoCamera, coveredHasCamera);
}
```



贪心

```java
public static enum Status {
    UNCOVERED, COVERED_HAS_CAMERA, COVERED_NO_CAMERA;
}
public static class Data {
    public Status status;
    public long cameras;

    public Data(Status status, long cameras) {
        this.status = status;
        this.cameras = cameras;
    }
}

public static Data process(Node x) {
    if (x == null) {
        return new Data(Status.COVERED_NO_CAMERA, 0);
    }
    Data left = process(x.left);
    Data right = process(x.right);
    long cameras = left.cameras + right.cameras;
    //左右还子存在未覆盖
    if (left.status == Status.UNCOVERED || right.status == Status.UNCOVERED) {
        return new Data(Status.COVERED_HAS_CAMERA, cameras + 1);
    }
    //左右孩子都已经覆盖
    //左右孩子只要有其中一个放了相机
    if (left.status == Status.COVERED_HAS_CAMERA || right.status == Status.COVERED_HAS_CAMERA) {
        return new Data(Status.COVERED_NO_CAMERA, cameras);
    }
    //左右孩子都没放相机
    return new Data(Status.UNCOVERED, cameras);
}
```



优化

```java
//0: 未覆盖
//1: 已经被覆盖 但是没放相机
//2: 已经被覆盖 但是放相机
public int[] process2(Node x) {
    if (x == null) {
        return new int[] {1, 0};
    }
    int[] left = process2(x.left);
    int[] right = process2(x.right);
    int cameras = left[1] + right[1];
    //左右孩子存在未覆盖的，当前x必须放相机
    if (left[0] == 0 || right[0] == 0) {
        return new int[] {2, cameras + 1};
    }
    //左右孩子都已经覆盖 左右孩子至少有一个放了相机，那么当前x不用放相机
    if (left[0] == 2 || right[0] == 2) {
        return new int[] {1, cameras};
    }
    //左右孩子都已经覆盖，左右孩子都没放相机，那么当前x不放相机，由其父亲决定
    return new int[] {0, cameras};
}
```



## 题目九十三

牛牛和15个朋友来玩打士豪分田地的游戏，牛牛决定让你来分田地，地主的田地可以看成是一个矩形，每个位置有一个价值分割田地的方法是横竖各切三刀，分成16份，作为领导干部，牛牛总是会选择其中总价值最小的一份田地，作为牛牛最好的朋友，你希望牛牛取得的田地的价值和尽可能大，你知道这个值最大可以是多少吗？
输入描述：
每个输入包贪1个测试用例。，每个测试用例的第一行包含两个整数n和m(1<=n,m<=75),表示田地的大小，接下来的n行包含个0-9之间的数字，表示每块位置的价值。
输出描述：输出一行表示年牛所能取得的最大的价值。
输入例子：44

```
3 3 3 2
3 2 3 3
3 3 3 2
2 3 2 3
```

输出例子：2





## 题目九十四

给定一个只含0和1二维数组matrixⅸ，第0行表示天花板。每个位置认为与上、下、左、右四个方向有粘性，比如：
matrix

```
10010
10011
11011
10000
00110
```

注意到0行0列是 1，然后能延伸出5个1的一片。同理0行3列也是1，也能延伸出5个1的一片。注意到4行2列是1，然后能延伸出2个1的一片。其中有两片1是粘在天花板上的，而4行2列延伸出来的这片，认为粘不住就掉下来了。在给定一个二维数组bomb,表示炸弹的位置，比如：
bomb

```
20
13
14
03
```

第一枚炮弹在2行0列，该处的1直接被打碎，然后会有2个1掉下来
第二枚炮弹在1行3列，该处的1直接被打碎，不会有1掉下来，因为这一片1还能粘在一起。
第三枚炮弹在1行4列，该处的1直接被打碎，然后会有2个1掉下来。
第四枚炮弹在0行3列，该处的1直接被打碎，不会有1掉下来，因为这一片1只剩这一个了。
根据matrix和bomb,返回结果[2，3，0，0]。

```java
public class Code18 {

    public static class Dot {

    }
    public static class UnionFind {
        private int[][] grid;
        private int N;
        private int M;
        private int cellingAll;
        private Dot[][] dots;
        private HashSet<Dot> cellingSet;
        private HashMap<Dot, Dot> fatherMap;
        private HashMap<Dot, Integer> sizeMap;

        public UnionFind(int[][] matrix) {
            initSpace(matrix);
            initConnect();
        }

        private void initSpace(int[][] matrix) {
            grid = matrix;
            N = grid.length;
            M = grid[0].length;
            int all = N * M;
            cellingAll = 0;
            cellingSet = new HashSet<>();
            fatherMap = new HashMap<>();
            sizeMap = new HashMap<>();
            dots = new Dot[N][M];
            for (int row = 0; row < N; row++) {
                for (int col = 0; col < M; col++) {
                    if (grid[row][col] == 1) {
                        Dot cur = new Dot();
                        dots[row][col] = cur;
                        fatherMap.put(cur, cur);
                        sizeMap.put(cur, 1);
                        if (row == 0) {
                            cellingSet.add(cur);
                            cellingAll++;
                        }
                    }
                }
            }
        }

        private void initConnect() {
            for (int row = 0; row < N; row++) {
                for (int col = 0; col < M; col++) {
                    union(row, col, row - 1, col);
                    union(row, col, row + 1, col);
                    union(row, col, row, col - 1);
                    union(row, col, row, col + 1);
                }
            }
        }

        public void union(int r1, int c1, int r2, int c2) {
            if (valid(r1, c1) && valid(r2, c2)) {
                Dot father1 = findParent(r1, c1);
                Dot father2 = findParent(r2, c2);
                if (father1 != father2) {
                    Integer size1 = sizeMap.get(father1);
                    Integer size2 = sizeMap.get(father2);
                    Dot big = size1 >= size2 ? father1 : father2;
                    Dot small = big == father1 ? father2 : father1;

                    fatherMap.put(small, big);
                    sizeMap.put(big, size1 + size2);

                    boolean status1 = cellingSet.contains(father1);
                    boolean status2 = cellingSet.contains(father2);
                    if (status1 ^ status2) {
                        cellingSet.add(big);
                        cellingAll += status1 ? size2 : size1;
                    }

                }
            }
        }

        private boolean valid(int row, int col) {
            return row >= 0 && row < N && col >= 0 && col < M && grid[row][col] == 1;
        }

        public Dot findParent(int row, int col) {
            Dot cur = dots[row][col];
            Stack<Dot> stack = new Stack<>();
            while (cur != fatherMap.get(cur)) {
                stack.add(cur);
                cur = fatherMap.get(cur);
            }
            while (!stack.isEmpty()) {
                fatherMap.put(stack.pop(), cur);
            }
            return cur;
        }

        public int finger(int row, int col) {
            int pre = cellingAll;
            grid[row][col] = 1;
            Dot cur = new Dot();
            dots[row][col] = cur;
            if (row == 0) {
                cellingSet.add(cur);
                cellingAll++;
            }
            fatherMap.put(cur, cur);
            sizeMap.put(cur, 1);
            union(row, col, row - 1, col);
            union(row, col, row + 1, col);
            union(row, col, row, col - 1);
            union(row, col, row, col + 1);
            int now = cellingAll;
            return now == pre ? 0 : now - pre - 1;
        }
    }

    public static int[] hitBricks(int[][] grid, int[][] hits) {
        for (int[] hit : hits) {
            if (grid[hit[0]][hit[1]] == 1) {
                grid[hit[0]][hit[1]] = 2;
            }
        }
        UnionFind unionFind = new UnionFind(grid);
        int[] ans = new int[hits.length];
        for (int i = hits.length - 1; i >= 0; i--) {
            if (grid[hits[i][0]][hits[i][1]] == 2) {
                ans[i] = unionFind.finger(hits[i][0], hits[i][1]);
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[][] grid = {
                {1, 0, 0, 1, 0},
                {1, 0, 0, 1, 1},
                {1, 1, 0, 1, 1},
                {1, 0, 0, 0, 0},
                {0, 0, 1, 1, 0}
        };
        int[][] hits = {
                {2, 0},
                {1, 3},
                {1, 4},
                {0, 3}};
        System.out.println(Arrays.toString(hitBricks(grid, hits)));
    }

}
```



## 题目九十五

给定一个数组arr,如果其中有两个集合的累加和相等，并且两个集合使用的数没有相容的部分（也就是arr中某数不能同时进这个两个集合），那么这两个集合叫作等累加和集合对。返回等累加和集合对中，最大的累加和。
举例：arr={1,2,3,6} {1,2}和{3}，是等累加和集合对, {1,2,3}和{6}，也是等累加和集合对,返回6

```Java
class Solution {
    public int tallestBillboard(int[] rods) {
        //k:差值 v:累加和对中较小的值
        HashMap<Integer, Integer> dp = new HashMap<>();
        HashMap<Integer, Integer> cur = new HashMap<>();
        dp.put(0, 0);
        for (int num : rods) {
            if (num != 0) {
                cur.clear();
                cur.putAll(dp);
                for (int d : cur.keySet()) {
                    //num 放到较大的集合
                    int diffMore = cur.get(d);
                    dp.put(d + num, Math.max(diffMore, dp.getOrDefault(d + num, 0)));
                    //num 放到较小的集合 
                    // d, 较小的 diffMore, 较大的 diffMore + d
                    // num 放到 较小的 diffMore + num, 那么新的较小就是 Math.min(diffMore + num, diffMore + d)
                    dp.put(Math.abs(d - num), Math.max(diffMore + Math.min(num, d), dp.getOrDefault(Math.abs(num - d), 0)));
                }
            }
        }
        return dp.get(0);
    }
}
```

测试https://leetcode.cn/problems/tallest-billboard/



## 题目九十六

给定一个字符串S，求S中有多少个字面值不相同的子序列。

```java
public static int ketang(String s) {
    if (s == null || s.length() == 0) {
        return 0;
    }
    char[] str = s.toCharArray();
    int[] count = new int[26];
    int all = 1;
    for (char ch : str) {
        int add = all - count[ch - 'a'];
        all += add;
        count[ch - 'a'] += add;
    }
    return all;
}
```

## 题目九十七

```java
/*
	 * 腾讯原题
	 * 
	 * 给定整数power，给定一个数组arr，给定一个数组reverse。含义如下：
	 * arr的长度一定是2的power次方，reverse中的每个值一定都在0~power范围。
	 * 例如power = 2, arr = {3, 1, 4, 2}，reverse = {0, 1, 0, 2}
	 * 任何一个在前的数字可以和任何一个在后的数组，构成一对数。可能是升序关系、相等关系或者降序关系。
	 * 比如arr开始时有如下的降序对：(3,1)、(3,2)、(4,2)，一共3个。
	 * 接下来根据reverse对arr进行调整：
	 * reverse[0] = 0, 表示在arr中，划分每1(2的0次方)个数一组，然后每个小组内部逆序，那么arr变成
	 * [3,1,4,2]，此时有3个逆序对。
	 * reverse[1] = 1, 表示在arr中，划分每2(2的1次方)个数一组，然后每个小组内部逆序，那么arr变成
	 * [1,3,2,4]，此时有1个逆序对
	 * reverse[2] = 0, 表示在arr中，划分每1(2的0次方)个数一组，然后每个小组内部逆序，那么arr变成
	 * [1,3,2,4]，此时有1个逆序对。
	 * reverse[3] = 2, 表示在arr中，划分每4(2的2次方)个数一组，然后每个小组内部逆序，那么arr变成
	 * [4,2,3,1]，此时有5个逆序对。
	 * 所以返回[3,1,1,5]，表示每次调整之后的逆序对数量。
	 * 
	 * 输入数据状况：
	 * power的范围[0,20]
	 * arr长度范围[1,10的7次方]
	 * reverse长度范围[1,10的6次方]
	 * 
	 * */

public static int[] reversePair2(int[] originArr, int[] reverseArr, int power) {
    int[] reverse = copyArray(originArr);
    reversePart(reverse, 0, reverse.length - 1);
    int[] recordDown = new int[reverseArr.length + 1];
    int[] recordUp = new int[reverseArr.length + 1];
    process(originArr, 0, originArr.length - 1, power, recordDown);
    process(reverse, 0, reverse.length - 1, power, recordUp);
    int[] ans = new int[reverseArr.length];
    for (int i = 0; i < reverseArr.length; i++) {
        int curPower = reverseArr[i];
        for (int p = 1; p <= curPower; p++) {
            int tmp = recordDown[p];
            recordDown[p] = recordUp[p];
            recordUp[p] = tmp;
        }
        for (int p = 1; p <= power; p++) {
            ans[i] += recordDown[p];
        }
    }
    return ans;
}

public static void process(int[] arr, int L, int R, int power, int[] record) {
    if (L == R) {
        return;
    }
    int mid = L + ((R - L) >> 1);
    process(arr, L, mid, power - 1, record);
    process(arr, mid + 1, R, power - 1, record);
    record[power] += merge(arr, L, mid, R);
}

public static int merge(int[] arr, int L, int m, int R) {
    int[] help = new int[R - L + 1];
    int index = 0;
    int p1 = L;
    int p2 = m + 1;
    int ans = 0;
    while (p1 <= m && p2 <= R) {
        ans += arr[p1] > arr[p2] ? (m - p1 + 1) : 0;
        help[index++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
    }
    while (p1 <= m) {
        help[index++] = arr[p1++];
    }
    while (p2 <= R) {
        help[index++] = arr[p2++];
    }
    for (int i = 0; i < help.length; i++) {
        arr[L + i] = help[i];
    }
    return ans;
}

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
public static void reversePart(int[] arr, int L, int R) {
    while (L < R) {
        int tmp = arr[L];
        arr[L++] = arr[R];
        arr[R--] = tmp;
    }
}
```

## 题目九十八

给定字符串数组words,其中所有字符串都不同，如果words门+wordsI]是回文串就记录(i，j)，找到所有记录并返回
例子一：
输入：["abcd","dcba","lls","s","sssll"]
输出：[ [ 0,1],[1,0],[3,2],[2,4]]
解释：输出的每一组数组，两个下标代表字符串拼接在一起，都是回文串abcddcba、dcbaabcd、slls、llssssll

```java
public static List<List<Integer>> palindromePairs(String[]  words) {
    HashMap<String, Integer> wordset = new HashMap<>();
    for (int i = 0; i < words.length; i++) {
        wordset.put(words[i], i);
    }
    List<List<Integer>> res = new ArrayList<>();
    for (int i = 0; i < words.length; i++) {
        res.addAll(findAll(words[i], i, wordset));
    }
    return res;
}

public static List<List<Integer>> findAll(String word, int index, HashMap<String, Integer> words) {
    List<List<Integer>> res = new ArrayList<>();
    String reverse = reverse(word);
    StringBuilder sb = new StringBuilder();
    Integer rest = words.get("");
    if (rest != null && rest != index && word.equals(reverse)) {
        addRecord(res, rest, index);
        addRecord(res, index, rest);
    }
    rest = words.get(reverse);
    if (rest != null && rest != index) {
        addRecord(res, index, rest);
    }
    char[] str = word.toCharArray();
    int Len = str.length;
    for (int i = 0; i < Len - 1; i++) {
        sb.append(str[i]);
        if (isPalindrome(sb.toString())) {
            String suffix = new String(str, i + 1, Len - i - 1);
            rest = words.get(reverse(suffix));
            if (rest != null && rest != index) {
                addRecord(res, rest, index);
            }
        }
    }
    sb = new StringBuilder();
    for (int i = Len - 1; i > 0; i--) {
        sb.insert(0, str[i]);
        if (isPalindrome(sb.toString())) {
            String prefix = new String(str, 0, i);
            rest = words.get(reverse(prefix));
            if (rest != null && rest != index) {
                addRecord(res, index, rest);
            }
        }
    }
    return res;
}

public static boolean isPalindrome(String word) {
    int L = 0, R = word.length() - 1;
    char[] str = word.toCharArray();
    while (L <= R) {
        if (str[L] != str[R]) return false;
        L++;
        R--;
    }
    return true;
}

public static void addRecord(List<List<Integer>> res, int left, int right) {
    List<Integer> rec = new ArrayList<>();
    rec.add(left);
    rec.add(right);
    res.add(rec);
}

public static String reverse(String word) {
    char[] chs = word.toCharArray();
    int L = 0, R = chs.length - 1;
    while (L < R) {
        char tmp = chs[L];
        chs[L++] = chs[R];
        chs[R--] = tmp;
    }
    return String.valueOf(chs);
}
```

manacher算法优化，生成回文半径数组

```java
class Solution {
    public List<List<Integer>> palindromePairs(String[]  words) {
        HashMap<String, Integer> wordset = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            wordset.put(words[i], i);
        }
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            res.addAll(findAll(words[i], i, wordset));
        }
        return res;
    }

    public static List<List<Integer>> findAll(String word, int index, HashMap<String, Integer> words) {
        List<List<Integer>> res = new ArrayList<>();
		String reverse = reverse(word);
		Integer rest = words.get("");
		if (rest != null && rest != index && word.equals(reverse)) {
			addRecord(res, rest, index);
			addRecord(res, index, rest);
		}
		int[] rs = manacherss(word);
		int mid = rs.length >> 1;
		for (int i = 1; i < mid; i++) {
			if (i - rs[i] == -1) {
				rest = words.get(reverse.substring(0, mid - i));
				if (rest != null && rest != index) {
					addRecord(res, rest, index);
				}
			}
		}
		for (int i = mid + 1; i < rs.length; i++) {
			if (i + rs[i] == rs.length) {
				rest = words.get(reverse.substring((mid << 1) - i));
				if (rest != null && rest != index) {
					addRecord(res, index, rest);
				}
			}
		}
		return res;
    }
public static int[] manacherss(String word) {
		char[] mchs = manachercs(word);
		int[] rs = new int[mchs.length];
		int center = -1;
		int pr = -1;
		for (int i = 0; i != mchs.length; i++) {
			rs[i] = pr > i ? Math.min(rs[(center << 1) - i], pr - i) : 1;
			while (i + rs[i] < mchs.length && i - rs[i] > -1) {
				if (mchs[i + rs[i]] != mchs[i - rs[i]]) {
					break;
				}
				rs[i]++;
			}
			if (i + rs[i] > pr) {
				pr = i + rs[i];
				center = i;
			}
		}
		return rs;
	}
    public static char[] manachercs(String word) {
		char[] chs = word.toCharArray();
		char[] mchs = new char[chs.length * 2 + 1];
		int index = 0;
		for (int i = 0; i != mchs.length; i++) {
			mchs[i] = (i & 1) == 0 ? '#' : chs[index++];
		}
		return mchs;
	}
    public static boolean isPalindrome(String word) {
        int L = 0, R = word.length() - 1;
        char[] str = word.toCharArray();
        while (L <= R) {
            if (str[L] != str[R]) return false;
            L++;
            R--;
        }
        return true;
    }

    public static void addRecord(List<List<Integer>> res, int left, int right) {
        List<Integer> rec = new ArrayList<>();
        rec.add(left);
        rec.add(right);
        res.add(rec);
    }

    public static String reverse(String word) {
        char[] chs = word.toCharArray();
        int L = 0, R = chs.length - 1;
        while (L < R) {
            char tmp = chs[L];
            chs[L++] = chs[R];
            chs[R--] = tmp;
        }
        return String.valueOf(chs);
    }
}
```



## 题目九十九

给定无序数组arr,返回其中最长的连续序列的长度
【举例】arr=[100,4,200,1,3,2],最长的连续序列为[1,2,3,4]，所以返回4。

```java
public static int longestConsecutive(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int max = 1;
    HashMap<Integer, Integer> map = new HashMap<>();
    for (int num : arr) {
        if (!map.containsKey(num)) {
            map.put(num, 1);
            if (map.containsKey(num - 1)) {
                max = Math.max(max, merge(map, num - 1, num));
            }
            if (map.containsKey(num + 1)) {
                max = Math.max(max, merge(map, num, num + 1));
            }
        }
    }
    return max;
}

public static int merge(HashMap<Integer, Integer> map, int preRangeEnd, int curRangeStart) {
    int preRangeStart = preRangeEnd - map.get(preRangeEnd) + 1;
    int curRangeEnd = curRangeStart + map.get(curRangeStart) - 1;
    int len = curRangeEnd - preRangeStart + 1;
    map.put(preRangeStart, len);
    map.put(curRangeEnd, len);
    return len;
}
```



## 题目一百

给定一个二维数组matrⅸ，其中的值不是0就是1，其中内部全是1的所有子矩阵中，含有最多1的子矩阵中，含有几个1？

单调栈

```java
public static int maxRecSize(int[][] map) {
    if (map == null || map.length == 0 || map[0].length == 0) {
        return 0;
    }
    int maxArea = 0;
    int[] height = new int[map[0].length];
    for (int i = 0; i < map.length; i++) {
        for (int j = 0; j < map[0].length; j++) {
            height[j] = map[i][j] == 0 ? 0 : height[j] + 1;
        }
        maxArea = Math.max(maxArea, maxRecFromBottom(height));
    }
    return maxArea;
}

public static int maxRecFromBottom(int[] height) {
    if (height == null || height.length == 0) {
        return 0;
    }
    int maxArea = 0;
    Stack<Integer> stack = new Stack<>();
    for (int i = 0; i < height.length; i++) {
        while (!stack.isEmpty() && height[i] <= height[stack.peek()]) {
            int j = stack.pop();
            int k = stack.isEmpty() ? -1 : stack.peek();
            int curArea = (i - k - 1) * height[j];
            maxArea = Math.max(maxArea, curArea);
        }
        stack.push(i);
    }
    while (!stack.isEmpty()) {
        int j = stack.pop();
        int k = stack.isEmpty() ? -1 : stack.peek();
        int curArea = (height.length - k - 1) * height[j];
        maxArea = Math.max(maxArea, curArea);
    }
    return maxArea;
}

public static void main(String[] args) {
    int[][] arr = {
            {1, 1, 1, 1, 1},
            {1, 1, 1, 0, 1},
            {1, 1, 0, 1, 1},
            {1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1}};
    System.out.println(maxRecSize(arr));
}
```



## 题目一百01

public class Query
public Node o1:
public Node o2:
public Query(Node o1,Node o2){
this.o1 o1;
this.o2 02;
一个
Quey类的实例表示一条查询语句，表示想要查询o1节点和o2节点的最近公共祖先节点。
给定二棵二叉树的头节点head,并给定所有的查询语句，即一个Quey类型的数组Quey几ques,
请返回
Noae类型的数组Node[]ans,ans0代表ques[i]这条查询的答案，即ques可.o1和ques.o2的最近公共祖先
【要求
如果二叉树的节点数为N,查询语句的条数为M,整个处理过程的时间复杂度要求达到ON+M)。



## 题目一百02

TSP问题 有N个城市，任何两个城市之间的都有距离，任何一座城市到自己的距离都为0。所有点到点的距离都存在一个N*N的二维数组matrix里，也就是整张图由邻接矩阵表示。现要求一旅行商从k城市出发必须经过每一个城市且只在一个城市逗留一次，最后回到出发的k城，返回总距离最短的路的距离。参数给定一个matrⅸ，给定k。

```java
public static int g1(int[][] matrix) {
    return dp(matrix, (1 << matrix.length) - 1, 0);
}

public static int dp(int[][] matrix, int cityStatus, int start) {
    if (cityStatus == (cityStatus & (~cityStatus + 1))) {
        return matrix[start][0];
    }
    cityStatus &= (~(1 << start));
    int min = Integer.MAX_VALUE;
    for (int i = 0; i < matrix.length; i++) {
        if (i != start && (cityStatus & (1 << i)) != 0) {
            int cur = matrix[start][i] + dp(matrix, cityStatus, i);
            min = Math.min(min, cur);
        }
    }
    return min;
}
```

```java
public static int g2(int[][] matrix) {
    int N = matrix.length;
    int statusNums = 1 << N;
    int[][] dp = new int[statusNums][N];
    for (int status = 0; status < statusNums; status++) {
        for (int start = 0; start < N; start++) {
            if ((status & (1 << start)) != 0) {
                if (status == (status & (~status + 1))) {
                    dp[status][start] = matrix[start][0];
                } else {
                  	//例如 status=110101 start=5
                  	//  preStatus=010101
                    int preStatus = status & (~(1 << start));
                    dp[status][start] = Integer.MAX_VALUE;
                    //从start -> i  i作为变成起点
                    for (int i = 0; i < N; i++) {
                        if ((preStatus & (1 << i)) != 0) {
                           //枚举起点 比如 preStatus=010101 枚举这3个1为起点
                           dp[status][start] = Math.min(dp[status][start], matrix[start][i] + dp[preStatus][i]);
                        }
                    }
                }

            }
        }
    }
    return dp[statusNums - 1][0];
}
```



## 题目一百03

贴瓷砖问题你有无限的1 * 2的砖块，要铺满2*N的区域，不同的铺法有多少种？
你有无限的1 * 2的砖块，要铺满M*N的区域，不同的铺法有多少种？





## 题目一百04

移除盒子https://leetcode.cn/problems/remove-boxes/
