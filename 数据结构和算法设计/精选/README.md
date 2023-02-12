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

## 2. 两数相加

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



## 14. 最长公共前缀

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



## 17. 电话号码的字母组合

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

