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

        for (int i = 1; i < len; i++) {
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
