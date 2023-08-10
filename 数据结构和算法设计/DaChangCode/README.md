#### 1
给定一个有序数组arr，代表坐落在X轴上的点, 给定一个正数K，代表绳子的长度, 返回绳子最多压中几个点？即使绳子边缘处盖住点也算盖住

```java
public static int maxPoint(int[] arr, int k) {
        int left = 0, right = 0, N = arr.length;
        int max = 0;
        while (left < N) {
            while (right < N && arr[right] - arr[left] <= k) {
                right++;
            }
            max = Math.max(max, right - left);
            left++;
        }
        return max;
    }
```



#### 2

给定一个文件目录的路径，写一个函数统计这个目录下所有的文件数量并返回隐藏文件也算，但是文件夹不算

```java
		//DFS
    public static int getFileNumber1(String folderPath) {
        File root = new File(folderPath);
        if (!root.isDirectory() && !root.isFile()) {
            return 0;
        }
        if (root.isFile()) {
            return 1;
        }
        Stack<File> stack = new Stack<>();
        stack.push(root);
        int files = 0;
        while (!stack.isEmpty()) {
            File folder = stack.pop();
            for (File next : folder.listFiles()) {
                if (next.isFile()) {
                    files++;
                    continue;
                }
                if (next.isDirectory()) {
                    stack.push(next);
                }
            }
        }
        return files;
    }

    //BFS
    public static int getFileNumber2(String folderPath) {
        File root = new File(folderPath);
        if (!root.isDirectory() && !root.isFile()) {
            return 0;
        }
        if (root.isFile()) {
            return 1;
        }
        LinkedList<File> queue = new LinkedList<>();
        queue.addLast(root);
        int files = 0;
        while (!queue.isEmpty()) {
            File cur = queue.pollFirst();
            if (cur.isFile()) {
                files++;
                continue;
            }
            if (cur.isDirectory()) {
                for (File next : cur.listFiles()) {
                    queue.addLast(next);
                }
            }
        }
        return files;
    }
```



#### 3

给定一个非负整数num，如何不用循环语句，返回>=num，并且离num最近的，2的某次方

```java
public static int near2Power(int n) {
        n--;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n < 0 ? 1 : n + 1;
    }
```

#### 4

一个数组中只有两种字符'G'和’B’，可以让所有的G都放在左侧，所有的B都放在右侧或者可以让所有的G都放在右侧，所有的B都放在左侧但是只能在相邻字符之间进行交换操作，返回至少需要交换几次

```java
public static int minStep(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        int step1 = 0, step2 = 0;
        int gi = 0, bi = 0;
        for (int i = 0; i < str.length; i++) {
            if (str[i] == 'G') {
                //当前的G，去左边   方案1
                step1 += i - (gi++);
            } else {
                //当前的B，去左边   方案2
                step2 += i - (bi++);
            }
        }
        return Math.min(step1, step2);
    }
```



#### 5

给定一个二维数组matrix，你可以从任何位置出发，走向上下左右四个方向返回能走出来的最长的递增链长度

#### 6

给定两个非负数组x和hp，长度都是N，再给定一个正数rangex有序，x[i]表示i号怪兽在x轴上的位置；hp[i]表示i号怪兽的血量再给定一个正数range，表示如果法师释放技能的范围长度被打到的每只怪兽损失1点血量。返回要把所有怪兽血量清空，至少需要释放多少次AOE技能？

#### 7

给定一个数组arr，你可以在每个数字之前决定+或者-但是必须所有数字都参与再给定一个数target，请问最后算出target的方法数是多少？

