#### 两数之和

给定一个整数数组 `nums` 和一个整数目标值 `target`，请你在该数组中找出 **和为目标值** *`target`* 的那 **两个** 整数，并返回它们的数组下标。你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。你可以按任意顺序返回答案。

**示例 1：**

```
输入：nums = [2,7,11,15], target = 9
输出：[0,1]
解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
```

**示例 2：**

```
输入：nums = [3,2,4], target = 6
输出：[1,2]
```

**示例 3：**

```
输入：nums = [3,3], target = 6
输出：[0,1]
```

**提示：**

- `2 <= nums.length <= 104`
- `-109 <= nums[i] <= 109`
- `-109 <= target <= 109`
- **只会存在一个有效答案**

**进阶：**你可以想出一个时间复杂度小于 `O(n2)` 的算法吗？

---

暴力枚举

```java
public int[] twoSum(int[] nums, int target) {
    for (int i = 0; i < nums.length; i++) {
        for (int j = i + 1; j < nums.length; j++) {
            if (nums[i] + nums[j] == target) {
                return new int[] {i, j};
            }
        }
    }
    return new int[2];
}
```

使用哈希表

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer num = map.get(nums[i]);
            if (num == null) {
                map.put(target - nums[i], i);
            } else {
                return new int[] {num,i};
            }
        }
        return new int[2];
    }
}
```

----

#### 有效的括号

#### 合并两个有序链表

#### 最大子数组和

#### 爬楼梯

#### 二叉树的中序遍历

#### 对称二叉树

#### 二叉树的最大深度

#### 买卖股票的最佳时机

#### 只出现一次的数字

#### 环形链表

#### 最小栈

#### 相交链表

#### 多数元素

#### 反转链表

#### 翻转二叉树

#### 回文链表

#### 移动零

#### 比特位计数

#### 找到所有数组中消失的数字

#### 汉明距离

#### 二叉树的直径

#### 合并二叉树

