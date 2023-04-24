## 1.两数之和

https://leetcode.cn/problems/two-sum/?favorite=2ckc81c

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[] {map.get(target - nums[i]), i};
            }
            map.put(nums[i], i);
        }
        return new int[] {};
    }
}
```

## 2.两数相加

https://leetcode.cn/problems/add-two-numbers/

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int c = 0;
        int sum = 0;
        ListNode head = new ListNode();
        ListNode cur = head, cur1 = l1, cur2 = l2;
        while (cur1 != null && cur2 != null) {
            sum = cur1.val + cur2.val + c;
            cur.next = new ListNode(sum % 10);
            c = sum / 10;
            sum = 0;
            cur = cur.next;
            cur1 = cur1.next;
            cur2 = cur2.next;
        }
        ListNode remain = cur1 == null ? cur2 : cur1;
        while (remain != null) {
            sum = remain.val + c;
            cur.next = new ListNode(sum % 10);
            c = sum / 10;
            sum = 0;
            cur = cur.next;
            remain = remain.next;
        }
        if (c != 0) {
            cur.next = new ListNode(c);
        }
        return head.next;
    }
}
```



## 3.无重复字符的最长子串

https://leetcode.cn/problems/longest-substring-without-repeating-characters/?favorite=2ckc81c

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        int pre = -1, len = 0;
        int[] map = new int[256];
        for (int i = 0; i < map.length; i++) {
            map[i] = -1;
        }
        for (int i = 0; i < str.length; i++) {
            pre = Math.max(pre, map[str[i]]);
            len = Math.max(len, i - pre);
            map[str[i]] = i;
        }
        return len;
    }
}
```

## 4.寻找两个正序数组的中位数

https://leetcode.cn/problems/median-of-two-sorted-arrays/

归并O(m + n)

```java
public double findMedianSortedArrays(int[] nums1, int[] nums2) {
    int Len = nums1.length + nums2.length;
    int[] temp = new int[Len];
    int i = 0, j = 0, k = 0;
    while (i < nums1.length && j < nums2.length) {
        if (nums1[i] <= nums2[j]) {
            temp[k] = nums1[i++];
        } else {
            temp[k] = nums2[j++];
        }
        k++;
    }
    while (i < nums1.length) {
        temp[k++] = nums1[i++];
    }
    while (j < nums2.length) {
        temp[k++] = nums2[j++];
    }
    return Len % 2 == 0 ? (double) (temp[(Len - 1) / 2] + temp[Len / 2]) / 2 : temp[Len / 2];
}
```

二分O(log(min(n, m)))

```java
public double findMedianSortedArrays(int[] nums1, int[] nums2) {
    if (nums1.length > nums2.length) {
        return findMedianSortedArrays(nums2, nums1);
    }
    int N = nums1.length;
    int M = nums2.length;
    int totalLeft = (N + M + 1) / 2;
    int left = 0, right = N;
    while (left < right) {
        int i = (left + right) / 2;
        int j = totalLeft - i;
        if (nums2[j - 1] > nums1[i]) {
            left = i + 1;
        } else {
            right = i;
        }
    }
    int i = left;
    int j = totalLeft - i;
    int nums1LeftMax = i == 0 ? Integer.MIN_VALUE : nums1[i - 1];
    int nums1RightMin = i == N ? Integer.MAX_VALUE : nums1[i];
    int nums2LeftMax = j == 0 ? Integer.MIN_VALUE : nums2[j - 1];
    int nums2RightMin = j == M ? Integer.MAX_VALUE : nums2[j];
    if ((N + M) % 2 == 1) {
        return Math.max(nums1LeftMax, nums2LeftMax);
    }
    return (double) (Math.max(nums1LeftMax, nums2LeftMax) + Math.min(nums1RightMin, nums2RightMin)) / 2;
}
```

```java
public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }
        int N = nums1.length;
        int M = nums2.length;
        int totalLeft = (N + M + 1) / 2;
        int left = 0, right = N;
        while (left < right) {
            int i = (left + right + 1) / 2;
            int j = totalLeft - i;
            if (nums1[i - 1] > nums2[j]) {
                right = i - 1;
            } else {
                left = i;
            }
        }
        int i = left;
        int j = totalLeft - i;
        int nums1LeftMax = i == 0 ? Integer.MIN_VALUE : nums1[i - 1];
        int nums1RightMin = i == N ? Integer.MAX_VALUE : nums1[i];
        int nums2LeftMax = j == 0 ? Integer.MIN_VALUE : nums2[j - 1];
        int nums2RightMin = j == M ? Integer.MAX_VALUE : nums2[j];
        if ((N + M) % 2 == 1) {
            return Math.max(nums1LeftMax, nums2LeftMax);
        }
        return (double) (Math.max(nums1LeftMax, nums2LeftMax) + Math.min(nums1RightMin, nums2RightMin)) / 2;
    }
```



## 5.最长回文子串

https://leetcode.cn/problems/longest-palindromic-substring/

```java
class Solution {
    public String longestPalindrome(String s) {
        int len = s.length();
        if (len < 2) {
            return s;
        }
        int maxLen = 1;
        int begin = 0;
        boolean[][] dp = new boolean[len][len];
        for (int i = 0; i < len; i++) {
            dp[i][i] = true;
        }
        char[] str = s.toCharArray();
        for (int subLen = 2; subLen <= len; subLen++) {
            for (int i = 0; i <= len - subLen; i++) {
              	//i + subLen - 1 < len => i <= len - subLen
                int j = i + subLen - 1;
                if (str[i] != str[j]) {
                    dp[i][j] = false;
                } else {
                    if (j - i < 3) {
                        dp[i][j] = true;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                }
                if (dp[i][j] && subLen > maxLen) {
                    maxLen = subLen;
                    begin = i;
                }
            }
        }
        return s.substring(begin, begin + maxLen);
    }
}
```



## 7.整数反转

https://leetcode.cn/problems/reverse-integer/

```java
class Solution {
    public int reverse(int x) {
        int ans = 0;
        int pop = 0;
        while (x != 0) {
            pop = x % 10;
            if (ans > Integer.MAX_VALUE / 10 || (ans == Integer.MAX_VALUE / 10 && pop > 7)) {
                return 0;
            }
            if (ans < Integer.MIN_VALUE / 10 || (ans == Integer.MIN_VALUE / 10 && pop < -8)) {
                return 0;
            }
            ans = ans * 10 + pop;
            x /= 10;
        }
        return ans;
    }
}
```





## 8.字符串转换整数 (atoi)

```java
class Solution {
    public int myAtoi(String s) {
        s = s.trim();
        char[] str = s.toCharArray();
        if (str.length == 0 || (!Character.isDigit(str[0]) && str[0] != '-' && str[0] != '+')) {
            return 0;
        }
        boolean neg = str[0] == '-';
        int ans = 0;
        int i = Character.isDigit(str[0]) ? 0 : 1;
        while (i < str.length && Character.isDigit(str[i])) {
            if (ans < ((neg ? Integer.MIN_VALUE : Integer.MIN_VALUE + 1) + str[i] - '0') / 10) {
                return neg ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }
            ans = ans * 10 - (str[i++] - '0');
        }
        return neg ? ans : -ans;
    }
}
```

```java
class Solution {
    public int myAtoi(String s) {
        s = s.trim();
        char[] str = s.toCharArray();
        if (str.length == 0 || !Character.isDigit(str[0]) && str[0] != '-' && str[0] != '+') {
            return 0;
        }
        boolean neg = str[0] == '-';
        long ans = 0;
        int i = Character.isDigit(str[0]) ? 0 : 1;
        while (i < str.length && Character.isDigit(str[i])) {
          	//ans必须是long，否则溢出
            if (ans * 10 - str[i] + '0' < (neg ? Integer.MIN_VALUE : Integer.MIN_VALUE + 1)) {
                return neg ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }
            ans = ans * 10 - (str[i++] - '0');
        }
        return (int) (neg ? ans : -ans);
    }
}
```



## 11.盛最多水的容器

https://leetcode.cn/problems/container-with-most-water/

```java
class Solution {
    public int maxArea(int[] height) {
        int l = 0, r = height.length - 1;
        int ans = 0;
        while (l < r) {
            ans = height[l] < height[r] ? 
                    Math.max(ans, (r - l) * height[l++]) 
                    : 
                    Math.max(ans, (r - l) * height[r--]);
        }
        return ans;
    }
}
```



## 10.正则表达式匹配

https://leetcode.cn/problems/regular-expression-matching/

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



## 12.整数转罗马数字

https://leetcode.cn/problems/integer-to-roman/

```java
class Solution {
    public String intToRoman(int num) {
        String[][] map = {
                {"", "I", "II", "III",  "IV", "V", "VI", "VII", "VIII", "IX"},
                {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"},
                {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"},
                {"", "M", "MM", "MMM", "MMM"}
        };

        StringBuilder roman = new StringBuilder();
        roman.append(map[3][num / 1000 % 10])
                .append(map[2][num / 100 % 10])
                .append(map[1][num / 10 % 10])
                .append(map[0][num % 10]);
        return roman.toString();
    }
}
```



## 13.罗马数字转整数

https://leetcode.cn/problems/roman-to-integer/?favorite=2ckc81c

```java
class Solution {
    public int romanToInt(String s) {
        char[] str = s.toCharArray();
        int N = str.length;
        int a = 0, b = 0, sum = 0;
        for (int i = 0; i < N; i++) {
            a = number(str[i]);
            if (i == N - 1) {
                sum += a;
            } else {
                b = number(str[i + 1]);
                sum += a >= b ? a : -a;
            }
        }
        return sum;
        
    }
    public int number(char c) {
        switch (c) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;
            default: return 0;
        }
    }
}
```



## 14.最长公共前缀

https://leetcode.cn/problems/longest-common-prefix/

```java
class Solution {
    public String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        char[] chs = strs[0].toCharArray();
        int min = Integer.MAX_VALUE;
        for (String str : strs) {
            char[] tmp = str.toCharArray();
            int index = 0;
            while (index < tmp.length && index < chs.length) {
                if (chs[index] != tmp[index]) {
                    break;
                }
                index++;
            }
            min = Math.min(min, index);
            if (min == 0) {
                return "";
            }
        }
        return strs[0].substring(0, min);
    }
}
```



## 15.三数之和

https://leetcode.cn/problems/3sum/

```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        int N = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        for (int k = 0; k < N - 2; k++) {
            if (k != 0 && nums[k] == nums[k - 1]) {
                continue;
            }
            int i = k + 1, j = N - 1;
            int rest = -nums[k];
            while (i < j) {
                if (i - 1 != k && nums[i] == nums[i - 1]) {
                    i++;
                    continue;
                }
                if (nums[i] + nums[j] == rest) {
                    List<Integer> r = new ArrayList();
                    r.add(nums[k]);
                    r.add(nums[i]);
                    r.add(nums[j]);
                    res.add(r);
                    i++;
                } else if (nums[i] + nums[j] > rest){
                    j--;
                } else {
                    i++;
                }
            }
        }
        return res;
    }
}
```



## 17.电话号码的字母组合

https://leetcode.cn/problems/letter-combinations-of-a-phone-number/?favorite=2ckc81c

```java
class Solution {
    public List<String> letterCombinations(String digits) {
        if (digits == null || digits.length() == 0) {
            return new ArrayList<>();
        }
        char[][] phone = {
                {},
                {},
                {'a', 'b', 'c'},
                {'d', 'e', 'f'},
                {'g', 'h', 'i'},
                {'j', 'k', 'l'},
                {'m', 'n', 'o'},
                {'p', 'q', 'r', 's'},
                {'t', 'u', 'v'},
                {'w', 'x', 'y', 'z'}
        };
        List<String> res = new ArrayList<>();
        process(digits.toCharArray(), 0, phone, "", res);
        return res;
    }

    public void process(char[] str, int index, char[][] phone, String path, List<String> res) {
        if (index == str.length) {
            res.add(path);
            return;
        }
        for (char ch : phone[str[index] - '0']) {
            process(str, index + 1, phone, path + ch, res);
        }
    }
}
```



## 18.删除链表的倒数第 N 个结点

https://leetcode.cn/problems/remove-nth-node-from-end-of-list/

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode newHead = new ListNode();
        newHead.next = head;
        ListNode fast = newHead;
        ListNode slow = newHead;
        while (n-- > 0) {
            fast = fast.next;
        }
        while (fast.next != null) {
            slow = slow.next;
            fast = fast.next;
        }
        slow.next = slow.next.next;
        return newHead.next;
    }
}
```



## 20.有效的括号

https://leetcode.cn/problems/valid-parentheses/?favorite=2ckc81c

```java
class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        char[] str = s.toCharArray();
        for (char ch : str) {
            if (ch == '(') {
                stack.push(')');
            } else if (ch == '[') {
                stack.push(']');
            } else if (ch == '{') {
                stack.push('}');
            } else if (stack.isEmpty() || stack.pop()!= ch) {
                return false;
            }
        }
        return stack.isEmpty();
    }
}
```



## 21.合并两个有序链表

https://leetcode.cn/problems/merge-two-sorted-lists/

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode newHead = new ListNode();
        ListNode cur = newHead;
        ListNode cur1 = list1;
        ListNode cur2 = list2;
        while (cur1 != null && cur2 != null) {
            if (cur1.val <= cur2.val) {
                cur.next = cur1;
                cur1 = cur1.next;
            } else {
                cur.next = cur2;
                cur2 = cur2.next;
            }
            cur = cur.next;
        }
        cur.next = cur1 == null ? cur2 : cur1;
        return newHead.next;
    }
}
```



## 22.括号生成

https://leetcode.cn/problems/generate-parentheses/

```java
class Solution {
    public List<String> generateParenthesis(int n) {
        char[] chs = new char[n << 1];
        List<String> res = new ArrayList<>();
        process(chs, 0, n, 0, res);
        return res;
    }
    
    public void process(char[] chs, int index, int leftRest, int leftMinusRight, List<String> res) {
        if (index == chs.length) {
            res.add(String.valueOf(chs));
            return;
        }
        if (leftRest > 0) {
            chs[index] = '(';
            process(chs, index + 1, leftRest - 1, leftMinusRight + 1, res);
        } 
        if (leftMinusRight > 0) {
            chs[index] = ')';
            process(chs, index + 1, leftRest, leftMinusRight - 1, res);
        }
    }
}
```



## 23.合并K个升序链表

https://leetcode.cn/problems/merge-k-sorted-lists/?favorite=2ckc81c

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        PriorityQueue<ListNode> heap = new PriorityQueue<>(Comparator.comparingInt(o -> o.val));
        for (ListNode list : lists) {
            if (list != null) {
                heap.add(list);
            }
        }
        ListNode head = new ListNode();
        ListNode cur = head;
        while (!heap.isEmpty()) {
            ListNode node = heap.poll();
            cur.next = node;
            cur = cur.next;
            if (node.next != null) {
                heap.add(node.next);
            }
        }
        return head.next;
    }
}
```



## 26.删除有序数组中的重复项

https://leetcode.cn/problems/remove-duplicates-from-sorted-array/

```java
class Solution {
    public int removeDuplicates(int[] nums) {
        int index = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[i - 1]) {
                nums[++index] = nums[i];
            }
        }
        return index + 1;
    }
}
```



## 28.找出字符串中第一个匹配项的下标

https://leetcode.cn/problems/find-the-index-of-the-first-occurrence-in-a-string/?favorite=2ckc81c

Kmp



## 29.两数相除

https://leetcode.cn/problems/divide-two-integers/?favorite=2ckc81c

```java
class Solution {
    public int divide(int dividend, int divisor) {
        if (divisor == Integer.MIN_VALUE) {
            return dividend == Integer.MIN_VALUE ? 1 : 0;
        }
        if (dividend == Integer.MIN_VALUE) {
            if (divisor == negNum(1)) {
                return Integer.MAX_VALUE;
            }
            int res = div(add(dividend, 1), divisor);
            return add(res, div(minus(dividend, multi(res, divisor)), divisor));
        }
        return div(dividend, divisor);
    }

    public static int add(int a, int b) {
        int t = a;
        while (b != 0) {
            t = t ^ b;
            b = (a & b) << 1;
            a = t;
        }
        return a;
    }

    public static int minus(int a, int b) {
        return add(a, add(~b, 1));
    }

    public static int multi(int a, int b) {
        int res = 0;
        while (b != 0) {
            if ((b & 1) == 1) {
                res = add(res, a);
            }
            a <<= 1;
            b >>>= 1;
        }
        return res;
    }

    public static int div(int a, int b) {
        int x = isNeg(a) ? negNum(a) : a;
        int y = isNeg(b) ? negNum(b) : b;
        int res = 0;
        for (int i = 31; i >= 0; i = minus(i, 1)) {
            if ((x >> i) >= y) {
                res |= (1 << i);
                x = minus(x, y << i);
            }
        }
        return isNeg(a) ^ isNeg(b) ? negNum(res) : res;
    }

    public static boolean isNeg(int x) {
        return ((x >> 31) & 1) == 1;
    }
    public static int negNum(int x) {
        return add(~x, 1);
    }
}
```



## 33.搜索旋转排序数组

https://leetcode.cn/problems/search-in-rotated-sorted-array/

```java
class Solution {
    public int search(int[] nums, int target) {
        int L = 0;
        int R = nums.length - 1;
        while (L <= R) {
            int M = (L + R) / 2;
            if (nums[M] == target) {
                return M;
            }
            if (nums[L] == nums[M] && nums[R] == nums[M]) {
                while (L < M && nums[L] == nums[M]) {
                    L++;
                }
                if (L == M) {
                    L = M + 1;
                    continue;
                }
            }
            if (nums[L] != nums[M]) {
                if (nums[L] < nums[M]) {
                    if (target >= nums[L] && target < nums[M]) {
                        R = M - 1;
                    } else {
                        L = M + 1;
                    }
                } else {
                    if (target > nums[M] && target <= nums[R]) {
                        L = M + 1;
                    } else {
                        R = M - 1;
                    }
                }
            } else {
                if (nums[M] < nums[R]) {
                    if (target > nums[M] && target <= nums[R]) {
                        L = M + 1;
                    } else {
                        R = M - 1;
                    }
                } else {
                    if (target >= nums[L] && target < nums[M]) {
                        R = M - 1;
                    } else{
                        L = M + 1;
                    }
                }
            }
        }
        return -1;
    }
}
```



## 34.在排序数组中查找元素的第一个和最后一个位置

https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/?favorite=2ckc81c

```java
class Solution {
    public int[] searchRange(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return new int[] {-1, -1};
        }
        int[] ans = new int[2];
        ans[0] = find(nums, target, true);
        ans[1] = find(nums, target, false);
        return ans;
    }

    public int find(int[] nums, int target, boolean left) {
        int L = 0, R = nums.length - 1, mid = 0, ans = -1;
        while (L <= R) {
            mid = L + ((R - L) >> 1);
            if (nums[mid] < target) {
                L = mid + 1;
            } else if (nums[mid] > target) {
                R = mid - 1;
            } else {
                ans = mid;
                if (left) {
                    R = mid - 1;
                } else {
                    L = mid + 1;
                }
            }
        }
        return ans;
    }
}
```



## 36.有效的数独

https://leetcode.cn/problems/valid-sudoku/

```java
class Solution {
    public boolean isValidSudoku(char[][] board) {
        boolean[][] row = new boolean[9][10];
        boolean[][] col = new boolean[9][10];
        boolean[][] bucket = new boolean[9][10];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    int bid = 3 * (i / 3) + (j / 3);
                    int num = board[i][j] - '0';
                    if (row[i][num] || col[j][num] || bucket[bid][num]) {
                        return false;
                    }
                    row[i][num] = true;
                    col[j][num] = true;
                    bucket[bid][num] = true;
                }
            }
        }
        return true;
    }
}
```



## 37.解数独

https://leetcode.cn/problems/sudoku-solver/

```java
class Solution {
   public void solveSudoku(char[][] board) {
        boolean[][] row = new boolean[9][10];
        boolean[][] col = new boolean[9][10];
        boolean[][] bucket = new boolean[9][10];
        init(board, row, col, bucket);
        process(board, 0, 0, row, col, bucket);
    }

    public boolean process(char[][] board, int i, int j, boolean[][] row, boolean[][] col, boolean[][] bucket) {
        if (i == 9) {
            return true;
        }
        int nexti = j != 8 ? i : i + 1;
        int nextj = j != 8 ? j + 1 : 0;
        if (board[i][j] != '.') {
            return process(board, nexti, nextj, row ,col, bucket);
        }
        for (int num = 1; num <= 9; num++) {
            int bid = 3 * (i / 3) + j / 3;
            if (valid(i, j, bid, num, row, col, bucket)) {
                setCache(i, j, bid, num, row, col, bucket, true);
                board[i][j] = (char) (num + '0');
                if (process(board, nexti, nextj, row ,col, bucket)) {
                    return true;
                }
                setCache(i, j, bid, num, row, col, bucket, false);
                board[i][j] = '.';
            }
        }
        return false;
    }
    
    public void setCache(int i, int j, int bid, int num, boolean[][] row, boolean[][] col, boolean[][] bucket, boolean value) {
        row[i][num] = value;
        col[j][num] = value;
        bucket[bid][num] = value;
    }

    public boolean valid(int i, int j, int bid, int num, boolean[][] row, boolean[][] col, boolean[][] bucket) {
        return !row[i][num] && !col[j][num] && !bucket[bid][num];
    }

    public void init(char[][] board, boolean[][] row, boolean[][] col, boolean[][] bucket) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    int bid = 3 * (i / 3) + j / 3;
                    int num = board[i][j] - '0';
                    setCache(i, j, bid, num, row, col, bucket, true);
                }
            }
        }
    }
}
```



## 38.外观数列

https://leetcode.cn/problems/count-and-say/

```java
class Solution {
    public String countAndSay(int n) {
        String str = "1";
        for (int i = 2; i <= n; i++) {
            int index = 0, end = 0;
            int len = str.length();
            StringBuilder sb = new StringBuilder();
            while (end < len) {
                while (end < len && str.charAt(end) == str.charAt(index)) {
                    end++;
                }
                String sub = str.substring(index, end);
                sb.append(trans(sub));
                index = end;
            }
            str = sb.toString();
        }
        return str;
    }
    
    public String trans(String str) {
        return str.length() + "" + str.charAt(0);
    }
}
```



## 41.缺失的第一个正数

https://leetcode.cn/problems/first-missing-positive/

```java
class Solution {
    public int firstMissingPositive(int[] nums) {
        int L = 0, R = nums.length;
        while (L < R) {
            if (nums[L] == L + 1) {
                L++;
            } else if (nums[L] <= L || nums[L] > R || nums[nums[L] - 1] == nums[L]) {
                nums[L] = nums[--R];
            } else {
              	//交换 nums[L] 和 nums[nums[L] - 1]的值
                int tmp = nums[L];
                nums[L] = nums[tmp - 1];
                nums[tmp - 1] = tmp;
            }
        }
        return L + 1;
    }
}
```



## 42.接雨水

https://leetcode.cn/problems/trapping-rain-water/

```java
class Solution {
    public int trap(int[] height) {
        if (height.length < 3) {
            return 0;
        }
        int L = 0, R = height.length - 1;
        int leftMax = height[L++], rightMax = height[R--];
        int total = 0;
        while (L <= R) {
            if (leftMax < rightMax) {
                total += Math.max(0, leftMax - height[L]); 
                leftMax = Math.max(leftMax, height[L++]);
            } else {
                total += Math.max(0, rightMax - height[R]);
                rightMax = Math.max(rightMax, height[R--]);
            }
        }
        return total;
    }
}
```



## 44. 通配符匹配

https://leetcode.cn/problems/wildcard-matching/

暴力递归

```java
class Solution {
    public boolean isMatch(String s, String p) {
        return process(s.toCharArray(), p.toCharArray(), 0, 0);
    }

    public boolean process(char[] s, char[] p, int si, int pi) {
        if (si == s.length) {
            return pi == p.length || p[pi] == '*' && process(s, p, si, pi + 1);
        }
        if (pi == p.length) {
            return si == s.length;
        }
        if (p[pi] != '?' && p[pi] != '*') {
            return p[pi] == s[si] && process(s, p, si + 1, pi + 1);
        }
        if (p[pi] == '?') {
            return process(s, p, si + 1, pi + 1);
        }
        for (int len = 0; len <= s.length - si; len++) {
            if (process(s, p, si + len, pi + 1)) {
                return true;
            }
        }
        return false;
    }
}
```

记忆化搜索

```java
class Solution {
    public boolean isMatch(String s, String p) {
        return f(s.toCharArray(), p.toCharArray(), 0, 0, new int[s.length() + 1][p.length() + 1]);
    }

    public boolean f(char[] s, char[] p, int si, int pi, int[][] dp) {
        if (dp[si][pi] != 0) {
            return dp[si][pi] == 1;
        }
        if (si == s.length) {
            boolean ans = pi == p.length || p[pi] == '*' && f(s, p, si, pi + 1, dp);
            dp[si][pi] = ans ? 1 : -1;
            return ans;
        }
        if (pi == p.length) {
            boolean ans = si == s.length;
            dp[si][pi] = ans ? 1 : -1;
            return ans;
        }
        if (p[pi] != '?' && p[pi] != '*') {
            boolean ans = p[pi] == s[si] && f(s, p, si + 1, pi + 1, dp);
            dp[si][pi] = ans ? 1 : -1;
            return ans;
        }
        if (p[pi] == '?') {
            boolean ans = f(s, p, si + 1, pi + 1, dp);
            dp[si][pi] = ans ? 1 : -1;
            return ans;
        }
        for (int len = 0; len <= s.length - si; len++) {
            if (f(s, p, si + len, pi + 1, dp)) {
                dp[si][pi] = 1;
                return true;
            }
        }
        dp[si][pi] = -1;
        return false;
    }
}
```

动态规划

```java
class Solution {
    public boolean isMatch(String s, String p) {
        char[] str = s.toCharArray();
        char[] pattern = p.toCharArray();
        int N = str.length;
        int M = pattern.length;
        boolean[][] dp = new boolean[N + 1][M + 1];
        dp[N][M] = true;
        for (int j = M - 1; j >= 0; j--) {
            dp[N][j] = pattern[j] == '*' && dp[N][j + 1];
        }
        for (int i = N - 1; i >= 0; i--) {
            for (int j = M - 1; j >= 0; j--) {
                if (pattern[j] != '?' && pattern[j] != '*') {
                    dp[i][j] = str[i] == pattern[j] && dp[i + 1][j + 1];
                    continue;
                }
                if (pattern[j] == '?') {
                    dp[i][j] = dp[i + 1][j + 1];
                    continue;
                }
                dp[i][j] = dp[i + 1][j] || dp[i][j + 1];
            }
        }
        return dp[0][0];
    }
}
```





## 55.跳跃游戏

https://leetcode.cn/problems/jump-game/

```java
class Solution {
    public boolean canJump(int[] nums) {
        int max = nums[0], N = nums.length;
        for (int i = 1; i < N; i++) {
            if (i > max) {
                return false;
            }
            if (max >= N - 1) {
                return true;
            }
            max = Math.max(max, i + nums[i]);
        }
        return true;
    }
}
```

```java
class Solution {
    public boolean canJump(int[] nums) {
        int endReachIndex = nums.length - 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (nums[i] + i >= endReachIndex) {
                endReachIndex = i;
            }
        }
        return endReachIndex == 0;
    }
}
```



## 45.跳跃游戏

https://leetcode.cn/problems/jump-game-ii/

```java
class Solution {
    public int jump(int[] nums) {
        int step = 0, cur = 0, next = 0, N = nums.length;
        for (int i = 0; i < N; i++) {
            if (i > cur) {
                step++;
                cur = next;
            }
            next = Math.max(next, i + nums[i]);
            if (cur >= N) break;
        }
        return step;
    }
}
```



## 46.全排列

https://leetcode.cn/problems/permutations/

```java
class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        p(nums, 0, ans);
        return ans;
    }
    public void p(int[] nums, int index, List<List<Integer>> ans) {
        if (index == nums.length) {
            List<Integer> res = new ArrayList<>();
            for (int num : nums) {
                res.add(num);
            }
            ans.add(res);
            return;
        }
        for (int i = index; i < nums.length; i++) {
            swap(nums, i, index);
            p(nums, index + 1, ans);
            swap(nums, i, index);
        }
    }
    public void swap(int[] nums, int i, int j) {
        if (i != j) {
            nums[i] = nums[i] ^ nums[j];
            nums[j] = nums[i] ^ nums[j];
            nums[i] = nums[i] ^ nums[j];
        }
    }
}
```





## 687. 最长同值路径

https://leetcode.cn/problems/longest-univalue-path/

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public int longestUnivaluePath(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return dfs(root).max - 1;
    }

    public Info dfs(TreeNode node) {
        if (node == null) {
            return new Info(0, 0);
        }
        Info leftInfo = dfs(node.left);
        Info rightInfo = dfs(node.right);
        int len = 1;
        if (node.left != null && node.left.val == node.val) {
            len = leftInfo.len + 1;
        }
        if (node.right != null && node.right.val == node.val) {
            len = Math.max(len, rightInfo.len + 1);
        }
        int max = Math.max(Math.max(leftInfo.max, rightInfo.max), len);
        if (node.left != null && node.right != null && node.left.val == node.val && node.right.val == node.val) {
            max = Math.max(max, leftInfo.len + rightInfo.len + 1);
        }
        return new Info(len, max);
    }

    public class Info {
      	//包含当前节点的最大同值路径，单边的
        public int len;
      	//当前节点的最大同值路径
        public int max;
        public Info(int len, int max) {
            this.len = len;
            this.max = max;
        }
    }


}
```

优化版本

```java
class Solution {
    int max = 1;
    public int longestUnivaluePath(TreeNode root) {
        dfs(root);
        return max - 1;
    }

    public int dfs(TreeNode node) {
        if (node == null) {
            return 0;
        }
        TreeNode left = node.left;
        TreeNode right = node.right;
        int l = dfs(left);
        int r = dfs(right);
        int cur = 1;
        if (left != null && left.val == node.val) {
            cur = l + 1;
        }
        if (right != null && right.val == node.val) {
            cur = Math.max(cur, r + 1);
        }
        max = Math.max(max, cur);
        if (left != null && right != null && left.val == node.val && right.val == node.val) {
            max = Math.max(max, l + r + 1);
        }
        return cur;
    }
}
```



## 48.旋转图像

https://leetcode.cn/problems/rotate-image/

题解一 旋转

```java
class Solution {
    public void rotate(int[][] matrix) {
        int i1 = 0, j1 = 0;
        int i2 = matrix.length - 1, j2 = i2;
        while (i1 < i2) {
            rotate(matrix, i1++, j1++, i2--, j2--);
        }
    }


    public void rotate(int[][] matrix, int i1, int j1, int i2, int j2) {
        int times = j2 - j1;
        int tmp = 0;
        for (int i = 0; i < times; i++) {
            tmp = matrix[i1][j1 + i];
            matrix[i1][j1 + i] = matrix[i2 - i][j1];
            matrix[i2 - i][j1] = matrix[i2][j2 - i];
            matrix[i2][j2 - i] = matrix[i1 + i][j2];
            matrix[i1 + i][j2] = tmp;
        }
    }
}
```

题解二 反转

```java
class Solution {
    public void rotate(int[][] matrix) {
        int N = matrix.length;
        //上下反转
        for (int i = 0; i < N / 2; i++) {
            for (int j = 0; j < N; j++) {
                swap(matrix, i, j, N - 1 - i, j);
            }
        }
        //对角线反转
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                swap(matrix, i, j, j, i);
            }
        }
    }

    public void swap(int[][] matrix, int i1, int j1, int i2, int j2) {
        int tmp = matrix[i1][j1];
        matrix[i1][j1] = matrix[i2][j2];
        matrix[i2][j2] = tmp;
    }
}
```



## 50. Pow(x, n)

https://leetcode.cn/problems/powx-n/

```java
class Solution {
    public double myPow(double x, int n) {
        if (n == 0) {
            return 1d;
        }
        int pow = Math.abs(n == Integer.MIN_VALUE ? n + 1 : n);
        double ans = 1d;
        double t = x;
        while (pow != 0) {
            if ((pow & 1) == 1) {
                ans *= t;
            }
            t *= t;
            pow >>= 1;
        }
        if (n == Integer.MIN_VALUE) {
            ans *= x;
        }
        return n < 0 ? (1d / ans) : ans;
    }
}
```



## 53. 最大子数组和

https://leetcode.cn/problems/maximum-subarray/?favorite=2ckc81c

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int dp = nums[0];
        int max = dp;
        for (int i = 1; i < nums.length; i++) {
            dp = Math.max(nums[i], dp + nums[i]);
            max = Math.max(max, dp);
        }
        return max;
    }
}
```





## 54. 螺旋矩阵

https://leetcode.cn/problems/spiral-matrix/

```java
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> ans = new ArrayList<>();
        int i1 = 0, j1 = 0, i2 = matrix.length - 1, j2 = matrix[0].length - 1;
        while (i1 <= i2 && j1 <= j2) {
            spiral(matrix, i1++, j1++, i2--, j2--, ans);
        }
        return ans;
    }

    public void spiral(int[][] matrix, int i1, int j1, int i2, int j2, List<Integer> ans) {
        if (i1 == i2) {
            for (int j = j1; j <= j2; j++) {
                ans.add(matrix[i1][j]);
            }
            return;
        }
        if (j1 == j2) {
            for (int i = i1; i <= i2; i++) {
                ans.add(matrix[i][j1]);
            }
            return;
        }
        for (int j = j1; j < j2; j++) {
            ans.add(matrix[i1][j]);
        }
        for (int i = i1; i < i2; i++) {
            ans.add(matrix[i][j2]);
        }
        for (int j = j2; j > j1; j--) {
            ans.add(matrix[i2][j]);
        }
        for (int i = i2; i > i1; i--) {
            ans.add(matrix[i][j1]);
        }
    }
}
```



## 56. 合并区间

https://leetcode.cn/problems/merge-intervals/

```java
class Solution {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (o1, o2) -> o1[0] - o2[0]);
        int start = intervals[0][0];
        int end = intervals[0][1];
        int[][] tmp = new int[intervals.length][2];
        int index = 0;
        for (int i = 1; i < intervals.length; i++) {
            int[] interval = intervals[i];
            if (interval[0] > end) {
                tmp[index++] = new int[] {start, end};
                start = interval[0];
                end = interval[1];
            } else if (interval[1] > end){
                end = interval[1];
            }
        }
        tmp[index++] = new int[] {start, end};
        return Arrays.copyOf(tmp, index);
    }
}
```



## 62.不同路径

https://leetcode.cn/problems/unique-paths/

动态规划

```java
class Solution {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }
}
```

数学

```java
class Solution {
    public int uniquePaths(int m, int n) {
       int all = m + n - 2;
        long vi = 1, vj = 1;
        int min = Math.min(m, n);
        int max = Math.max(m, n);
        for (int i = 1, j = max; i < min || j <= all;i++, j++) {
            if (i < min) {
                vi *= i;
            }
            if (j <= all) {
                vj *= j;
            }
            long gcd = gcd(vi, vj);
            vi = vi / gcd;
            vj = vj / gcd;
        }
        return (int)vj;
    }
    public long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
```



## 66.加一

https://leetcode.cn/problems/plus-one/

```java
class Solution {
    public int[] plusOne(int[] digits) {
        int N = digits.length;
        for (int i = N - 1; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i]++;
                return digits;
            }
            digits[i] = 0;
        }
        int[] ans = new int[N + 1];
        ans[0] = 1;
        return ans;
    }
}
```



## 69. x 的平方根

https://leetcode.cn/problems/sqrtx/

```java
class Solution {
    public int mySqrt(int x) {
        if (x == 0) {
            return 0;
        }
        long ans = 1, L = 1, R = x, M = 0;
        while (L <= R) {
            M = L + (R - L) / 2;
            if (M * M <= x) {
                ans = M;
                L = M + 1;
            } else {
                R = M - 1;
            }
        }
        return (int) ans;
    }
}
```



## 70. 爬楼梯

https://leetcode.cn/problems/climbing-stairs/

```java
class Solution {
    public int climbStairs(int n) {
        if (n <= 2) {
            return n;
        }
        int one = 2;
        int two = 1;
        int sum = 0;
        for (int i = 3; i <= n; i++) {
            sum = one + two;
            two = one;
            one = sum;
        }
        return sum;
    }
}
```



## 73. 矩阵置零

https://leetcode.cn/problems/set-matrix-zeroes/

```java
class Solution {
    public void setZeroes(int[][] matrix) {
        int N = matrix.length;
        int M = matrix[0].length;
        boolean isRowZero = false;
        boolean isColZero = false;
        for (int i = 0; i < N; i++) {
            if (matrix[i][0] == 0) {
                isColZero = true;
                break;
            }
        }
        for (int j = 0; j < M; j++) {
            if (matrix[0][j] == 0) {
                isRowZero = true;
                break;
            }
        }
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }

        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }

        if (isColZero) {
            for (int i = 0; i < N; i++) {
                matrix[i][0] = 0;
            }
        }
        if (isRowZero) {
            for (int j = 0; j < M; j++) {
               matrix[0][j] = 0;
            }
        }
    }
}
```





## 75. 颜色分类

https://leetcode.cn/problems/sort-colors/?favorite=2ckc81c

```java
class Solution {
    public void sortColors(int[] nums) {
        int p0 = 0, p2 = nums.length - 1, index = 0;
        while (index <= p2) {
            if (nums[index] == 0) {
                swap(nums, index++, p0++);
            } else if (nums[index] == 1) {
                index++;
            } else {
                swap(nums, index, p2--);
            }
        }
    }
    public void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
```



## 76. 最小覆盖子串

https://leetcode.cn/problems/minimum-window-substring/

```java
class Solution {
    public String minWindow(String s, String t) {
        if (s.length() < t.length()) {
            return "";
        }
        char[] str = s.toCharArray();
        char[] target = t.toCharArray();
        int[] map = new int[256];
        for (char ch : target) {
            map[ch]++;
        }
        int all = target.length;
        int L = 0, R = 0;
        int minLen = -1;
        int i = -1, j = -1;
        while (R < str.length) {
            map[str[R]]--;
            if (map[str[R]] >= 0) {
                all--;
            }
            if (all == 0) {
                while (map[str[L]] < 0) {
                    map[str[L++]]++;
                }
                if (minLen == -1 || (R - L + 1 < minLen)) {
                    i = L;
                    j = R;
                    minLen = j - i + 1;
                }
                all++;
                map[str[L++]]++;
            }
            R++;
        }
        return minLen == -1 ? "" : s.substring(i, j + 1);
    }
}
```



## 78. 子集

https://leetcode.cn/problems/subsets/

```java
class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        dfs(nums, 0, new LinkedList<>(), ans);
        return ans;
    }

    public void dfs(int[] nums, int index, LinkedList<Integer> path, List<List<Integer>> ans) {
        if (index == nums.length) {
            ans.add(new ArrayList<>(path));
            return;
        }
        path.addLast(nums[index]);
        dfs(nums, index + 1, path, ans);
        path.removeLast();
        dfs(nums, index + 1, path, ans);
    }
}
```



## 79. 单词搜索

https://leetcode.cn/problems/word-search/

```java
class Solution {
    public boolean exist(char[][] board, String word) {
        int N = board.length;
        int M = board[0].length;
        char[] w = word.toCharArray();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (dfs(board, i, j, w, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean dfs(char[][] board, int i, int j, char[] word, int index) {
        if (index == word.length) {
            return true;
        }
        if (i < 0 || i == board.length || j < 0 || j == board[0].length || board[i][j] != word[index]) {
            return false;
        }
        char cha = board[i][j];
        board[i][j] = 0;
        boolean ans = dfs(board, i + 1, j, word, index + 1)
                || dfs(board, i - 1, j, word, index + 1)
                || dfs(board, i, j + 1, word, index + 1)
                || dfs(board, i, j - 1, word, index + 1);
        board[i][j] = cha;
        return ans;
    }
}
```





## 88. 合并两个有序数组

https://leetcode.cn/problems/merge-sorted-array/

```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int index = nums1.length - 1;
        while (m > 0 && n > 0) {
            if (nums1[m - 1] > nums2[n - 1]) {
                nums1[index--] = nums1[--m];
            } else {
                nums1[index--] = nums2[--n];
            }
        }
        while (m > 0) {
            nums1[index--] = nums1[--m];
        }
        while (n > 0) {
            nums1[index--] = nums2[--n];
        }
    }
}
```

## 91. 解码方法

https://leetcode.cn/problems/decode-ways/

```java
class Solution {
    public int numDecodings(String s) {
        char[] str = s.toCharArray();
        int N = str.length;
        int[] dp = new int[N + 1];
        dp[N] = 1;
        for (int i = N - 1; i >= 0; i--) {
            if (str[i] != '0') {
                dp[i] = dp[i + 1];
                if (i < str.length - 1 && ((str[i] - '0') * 10 + str[i + 1] - '0') <= 26) {
                    dp[i] += dp[i + 2];
                }
            }
        }
        return dp[0];
    }
}
```

```java
class Solution {
    public int numDecodings(String s) {
        char[] str = s.toCharArray();
        int N = str.length;
        int a = 1, b = 0, dp = 0;
        for (int i = N - 1; i >= 0; i--) {
            if (str[i] != '0') {
                dp = a;
                if (i < str.length - 1 && ((str[i] - '0') * 10 + str[i + 1] - '0') <= 26) {
                    dp += b;
                }
            } else {
                dp = 0;
            }
            b = a;
            a = dp;
        }
        return dp;
    }
}
```



## 101. 对称二叉树

https://leetcode.cn/problems/symmetric-tree/

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeN\ode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public boolean isSymmetric(TreeNode root) {
        return isMirror(root, root);
    }


    public boolean isMirror(TreeNode head1, TreeNode head2) {
        if (head1 == null && head2 == null) {
            return true;
        }
        if (head1 == null || head2 == null) {
            return false;
        }
        return head1.val == head2.val && isMirror(head1.left, head2.right) && isMirror(head1.right, head2.left);
    }
}
```



## 102. 二叉树的层序遍历

https://leetcode.cn/problems/binary-tree-level-order-traversal/

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        if (root == null) {
            return new ArrayList();
        }
        List<List<Integer>> res = new ArrayList();
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.addLast(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> ans = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.removeFirst();
                if (cur.left != null) {
                    queue.addLast(cur.left);
                }
                if (cur.right != null) {
                    queue.addLast(cur.right);
                }
                ans.add(cur.val);
            }
            res.add(ans);
        }
        return res;
    }
}
```



## 103. 二叉树的锯齿形层序遍历

https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        boolean flag = true;
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> res = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (flag) {
                    TreeNode cur = queue.removeFirst();
                    res.add(cur.val);
                    if (cur.left != null) {
                        queue.addLast(cur.left);
                    }
                    if (cur.right != null) {
                        queue.addLast(cur.right);
                    }
                } else {
                    TreeNode cur = queue.removeLast();
                    res.add(cur.val);
                    if (cur.right != null) {
                        queue.addFirst(cur.right);
                    }
                    if (cur.left != null) {
                        queue.addFirst(cur.left);
                    }
                }
            }
            flag = !flag;
            ans.add(res);
        }
        return ans;
    }
}
```



## 105. 从前序与中序遍历序列构造二叉树

https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return process(preorder, inorder, 0, preorder.length - 1, 0, inorder.length - 1, map);
    }
    public TreeNode process(int[] preorder, int[] inorder, int L1, int R1, int L2, int R2, HashMap<Integer, Integer> map) {
       if (L1 > R1) {
           return null;
       }
       TreeNode head = new TreeNode(preorder[L1]);
       int inRootIndex = map.get(preorder[L1]);
       head.left = process(preorder, inorder, L1 + 1, L1 + inRootIndex - L2, L2, inRootIndex - 1, map);
       head.right = process(preorder, inorder, L1 + inRootIndex - L2 + 1, R1, inRootIndex + 1, R2, map);
       return head;
    }
}
```



## 108. 将有序数组转换为二叉搜索树

https://leetcode.cn/problems/convert-sorted-array-to-binary-search-tree/

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public TreeNode sortedArrayToBST(int[] nums) {
       return process(nums, 0, nums.length - 1); 
    }
    
    public TreeNode process(int[] nums, int L, int R) {
       if (L > R) {
           return null;
       }
       if (L == R) {
           return new TreeNode(nums[L]);
       }
       int mid = (L + R) / 2;
       TreeNode head = new TreeNode(nums[mid]);
       head.left = process(nums, L, mid - 1);
       head.right = process(nums, mid + 1, R);
       return head;
    }
}
```



## 116. 填充每个节点的下一个右侧节点指针

https://leetcode.cn/problems/populating-next-right-pointers-in-each-node/

```java
/*
// Definition for a Node.
class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}
    
    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
};
*/

class Solution {
    public Node connect(Node root) {
        if (root == null) {
            return root;
        }
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node cur = queue.removeFirst();
                cur.next = i == size - 1 ?  null : queue.peekFirst();
                if (cur.left != null) {
                    queue.addLast(cur.left);
                }
                if (cur.right != null) {
                    queue.add(cur.right);
                }
            }
        }
        return root;
    }
}
```

优化

```java
class Solution {
    class MyQueue {
       private Node head;
       private Node tail;
       
       public int size;
       
       public boolean isEmpty() {
           return size == 0;
       }
       
       public void addLast(Node node) {
           if (head == null) {
               head = node;
               tail = node;
           } else {
               tail.next = node;
               tail = node;
           }
           size++;
       }
       
       public Node removeFirst() {
           Node cur = head;
           head = head.next;
           cur.next = null;
           size--;
           return cur;
       }
    }
    
    public Node connect(Node root) {
        if (root == null) {
            return root;
        }
        MyQueue queue = new MyQueue();
        queue.addLast(root);
        while (!queue.isEmpty()) {
            int size = queue.size;
            Node pre = null;
            for (int i = 0; i < size; i++) {
                Node cur = queue.removeFirst();
                if (cur.left != null) {
                    queue.addLast(cur.left);
                }
                if (cur.right != null) {
                    queue.addLast(cur.right);
                }
                if (pre != null) {
                    pre.next = cur;
                }
                pre = cur;
            }
        }
        return root;
    }
}
```

递归实现

```java
class Solution {
    public Node connect(Node root) {
        if (root == null) {
            return root;
        }
        if (root.left != null) {
            root.left.next = root.right;
            if (root.next != null) {
                root.right.next = root.next.left;
            }
        }
        connect(root.left);
        connect(root.right);
        return root;
    }
}
```

改为非递归(迭代版本)

```java
class Solution {
    public Node connect(Node root) {
        if (root == null) {
            return root;
        }
      	//leftMost为每一层最左的节点
        Node leftMost = root;
        while (leftMost.left != null) {
            Node cur = leftMost;
            while (cur != null) {
              	//左子树连接右子树
                cur.left.next = cur.right;
              	//如果当前节点存在下一个节点，那么将当前节点的右子树连接下一个节点的左子树
                if (cur.next != null) {
                    cur.right.next = cur.next.left;
                }
                cur = cur.next;
            }
            leftMost = leftMost.left;
        }
        return root;
    }
}
```





## 118. 杨辉三角

https://leetcode.cn/problems/pascals-triangle/

```java
class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> ans = new ArrayList<>();
        ans.add(Arrays.asList(1));
        if (numRows == 1) {
            return ans;
        }
        ans.add(Arrays.asList(1, 1));
        if (numRows == 2) {
            return ans;
        }
        for (int i = 2; i < numRows; i++) {
            List<Integer> res = new ArrayList<>();
            res.add(1);
            for (int j = 1; j < i; j++) {
                res.add(ans.get(i - 1).get(j - 1) + ans.get(i - 1).get(j));
            }
            res.add(1);
            ans.add(res);
        }
        return ans;
    }
}
```



## 121. 买卖股票的最佳时机

https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/

```java
class Solution {
    public int maxProfit(int[] prices) {
        int min = prices[0];
        int max = 0;
        for (int i = 0; i < prices.length; i++) {
            min = Math.min(min, prices[i]);
            max = Math.max(max, prices[i] - min);
        }
        return max;
    }
}
```



## 122. 买卖股票的最佳时机 II

https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/

```java
class Solution {
    public int maxProfit(int[] prices) {
        int ans = 0;
        for (int i = 1; i < prices.length; i++) {
            ans += Math.max(0, prices[i] - prices[i - 1]);
        }
        return ans;
    }
}
```

## 123. 买卖股票的最佳时机 III

https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/

```java
class Solution {
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }
        int onceBuy = 0;
        int onceBuyMinus = -prices[0];
        int ans = 0;
        int min = prices[0];
        for (int i = 1; i < prices.length; i++) {
            ans = Math.max(ans, onceBuyMinus + prices[i]);
            min = Math.min(min, prices[i]);
            onceBuy = Math.max(onceBuy, prices[i] - min);
            onceBuyMinus = Math.max(onceBuyMinus, onceBuy - prices[i]);
        }
        return ans;
    }
}
```

## 188. 买卖股票的最佳时机 IV

https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/

```java
class Solution {
    public int maxProfit(int k, int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        int N = prices.length;
        k = Math.min(k, N / 2);  
        int[][] dp = new int[N][k + 1];
        for (int j = 1; j <= k; j++) {
            int pre = dp[0][j - 1] - prices[0];
            for (int i = 1; i < N; i++) {
                pre = Math.max(pre, dp[i][j - 1] - prices[i]);
                dp[i][j] = Math.max(dp[i - 1][j], prices[i] + pre);
            }
        }
        return dp[N - 1][k];
    }
}
```



## 309. 最佳买卖股票时机含冷冻期

https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-cooldown/

```java
class Solution {
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }
        int N = prices.length;
        int[] buy = new int[N];
        int[] sell = new int[N];
        buy[1] = Math.max(-prices[0], -prices[1]);
        sell[1] = Math.max(0, prices[1] - prices[0]);
        for (int i = 2; i < N; i++) {
            buy[i] = Math.max(buy[i - 1], sell[i - 2] - prices[i]);
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i]);
        }
        return sell[N - 1];
    }
}
```

优化

```java
class Solution {
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }
        int N = prices.length;
        int buy = Math.max(-prices[0], -prices[1]);
        int sell1 = Math.max(0, prices[1] - prices[0]);
        int sell2 = 0;
        int tmp = 0;
        for (int i = 2; i < N; i++) {
            tmp = buy;
            buy = Math.max(buy, sell2 - prices[i]);
            sell2 = sell1;
            sell1 = Math.max(sell1, tmp + prices[i]);
        }
        return sell1;
    }
}
```



## 130. 被围绕的区域

https://leetcode.cn/problems/surrounded-regions/?favorite=2ckc81c

```java
class Solution {
    public void solve(char[][] board) {
        int N = board.length;
        int M = board[0].length;
        for (int i = 0; i < N; i++) {
            dfs(board, i, 0);
            dfs(board, i, M - 1);
        }
        for (int j = 0; j < M; j++) {
            dfs(board, 0, j);
            dfs(board, N - 1, j);
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
                if (board[i][j] == '.') {
                    board[i][j] = 'O';
                }
            }
        }
    }

    public void dfs(char[][] board, int i, int j) {
       if (i < 0 || i == board.length || j < 0 || j == board[0].length || board[i][j] == 'X' || board[i][j] == '.') {
           return;
       }
       board[i][j] = '.';
       dfs(board, i + 1, j);
       dfs(board, i - 1, j);
       dfs(board, i, j + 1);
       dfs(board, i, j - 1);
    }
}
```

