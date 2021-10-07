# 【题目】

> 实现一个特殊的栈，在实现栈的基本功能的基础上，再实现返回栈中最小元素的操作。
>
> 【要求】
>
> 1.pop、push、 getTin操作的时间复杂度都是O（1）。
>
> 2.设计的栈类型可以使用现成的栈结构
>
> 【难度】
>
> 士★☆☆☆

# 【解答】

在设计上我们使用两个栈，一个栈用来保存当前栈中的元素，其功能和一个正常的栈没有区别，这个栈记为 stack Data，另一个栈用于保存每一步的最小值，这个栈记为stackMin，具体的实现方式有两种。

## 方案一

### 压入数据规则

1. 假设当前数据为 num，先将其压入 stackData。然后判断 stackMin是否为空
2. 如果为空，则 num也压入 stackMin
3. 如果不为空，则比较 num和 stackMin的栈顶元素中哪一个更小
4. 如果num更小或两者相等，则 num也压入 stackMin;
5. 如果 stackMin中栈顶元素小，则 stackMin不压入任何内容。

### 弹出数据规则

先在 stackData中弹出栈顶元素，记为 value然后比较当前 stackMin的栈顶元素和 value哪一个更小。通过上文提到的压入规则可知， stackMin中存在的元素是从栈底到栈顶逐渐变小的，stackMin栈顶的元素既是 stackMin栈的最小值，也是当前 stackData栈的最小值，所以不会出现 value比 stackMin的栈顶元素更小的情况，vaue只可能大于或等于 stackMin的栈顶元素，当 value等于 stackMin的栈顶元素时， stackMin弹出栈顶元素，当 value大于 stackMin的栈顶元素时， stackMin不弹出栈顶元素，返回vaue。很明显可以看出，压入与弹出规则是对应的。

### 查询当前栈中的最小值操作

由上文的压入数据规则和弹出数据规则可知， stackMin始终记录着 stackData中的最小值，所以， stackMin的栈顶元素始终是当前 stack Data中的最小值。

代码实现：

```java
public class MyStack1 {

    private Stack<Integer> stackData;
    private Stack<Integer> stackMin;

    public MyStack1() {
        stackData = new Stack<>();
        stackMin = new Stack<>();
    }

    public void push(int num) {
        stackData.push(num);
        if (stackMin.isEmpty() || stackMin.peek() >= num) {
            stackMin.push(num);
        }
    }

    public int pop() {
        if (stackData.isEmpty()) {
            throw new RuntimeException("Your stack is empty.");
        }
        int value = stackData.pop();
        if (value == stackMin.peek()) {
            stackMin.pop();
        }
        return value;
    }

    public int  getMin() {
        if (stackMin.isEmpty()) {
            throw new RuntimeException("Your stack is empty.");
        }
        return stackMin.peek();
    }
}
```

