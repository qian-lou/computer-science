![image-20220425114123624](https://gitee.com/JKcoding/imgs/raw/master/img01/image-20220425114123624.png)

链接：[https://leetcode-cn.com/leetbook/detail/top-interview-questions-easy/](https://leetcode-cn.com/leetbook/detail/top-interview-questions-easy/)

#### 数组

##### 删除排序数组中的重复项

---

给你一个 升序排列 的数组 `nums` ，请你原地删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。元素的相对顺序应该保持 一致 。

由于在某些语言中不能改变数组的长度，所以必须将结果放在数组`nums`的第一部分。更规范地说，如果在删除重复项之后有 k 个元素，那么 `nums` 的前 k 个元素应该保存最终结果。

将最终结果插入 `nums` 的前 k 个位置后返回 k 。

不要使用额外的空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。

> 示例 1：
>
> 输入：nums = [1,1,2]
> 输出：2, nums = [1,2,_]
> 解释：函数应该返回新的长度 2 ，并且原数组 nums 的前两个元素被修改为 1, 2 。不需要考虑数组中超出新长度后面的元素。
>
> 示例 2：
>
> 输入：nums = [0,0,1,1,1,2,2,3,3,4]
> 输出：5, nums = [0,1,2,3,4]
> 解释：函数应该返回新的长度 5 ， 并且原数组 nums 的前五个元素被修改为 0, 1, 2, 3, 4 。不需要考虑数组中超出新长度后面的元素。


提示：

0 <= nums.length <= 3 * 10^4
-10^4 <= nums[i] <= 10^4
nums 已按 升序 排列

```java
class Solution {
    public int removeDuplicates(int[] nums) {
        int index = 0, end = 0;
        int len = nums.length;
        while (end < len) {
            while (end < len && nums[index] == nums[end]) {
                end++;
            }
            if (end < len) {
                nums[++index] = nums[end++];
            }
        }
        return index + 1;
    }
}
```



#### 字符串

#### 链表

#### 树

#### 排序和搜索

#### 动态规划

#### 设计问题

#### 数学

#### 其他