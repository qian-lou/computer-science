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

在设计上我们使用两个栈，一个栈用来保存当前栈中的元素，其功能和一个正常的栈没有区别，这个栈记为 `stackData`，另一个栈用于保存每一步的最小值，这个栈记为`stackMin`，具体的实现方式有两种。

## 方案一

### 压入数据规则

1. 假设当前数据为 `num`，先将其压入 `stackData`。然后判断 `stackMin`是否为空
2. 如果为空，则 `num`也压入 `stackMin`
3. 如果不为空，则比较 `num`和 `stackMin`的栈顶元素中哪一个更小
4. 如果`num`更小或两者相等，则 `num`也压入 `stackMin`;
5. 如果 `stackMin`中栈顶元素小，则 `stackMin`不压入任何内容。

### 弹出数据规则

先在 `stackData`中弹出栈顶元素，记为 `value`然后比较当前 `stackMin`的栈顶元素和 `value`哪一个更小。通过压入规则可知， `stackMin`中存在的元素是从栈底到栈顶逐渐变小的，`stackMin`栈顶的元素既是 `stackMin`栈的最小值，也是当前 `stackData`栈的最小值，所以不会出现 `value`比 `stackMin`的栈顶元素更小的情况，`vaue`只可能大于或等于 `stackMin`的栈顶元素，当 `value`等于 `stackMin`的栈顶元素时， `stackMin`弹出栈顶元素，当 value大于 `stackMin`的栈顶元素时， `stackMin`不弹出栈顶元素，返回`vaue`。很明显可以看出，压入与弹出规则是对应的。

### 查询当前栈中的最小值操作

由上文的压入数据规则和弹出数据规则可知， `stackMin`始终记录着 `stackData`中的最小值，所以， `stackMin`的栈顶元素始终是当前 `stackData`中的最小值。

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



## 方案二

### 压入数据规则

假设当前数据为 `newNum`，先将其压入 `stackData`，然后判断 `stackMin`是否为空。如果为空，则 `newNum`也压入 `stackMin`，如果不为空，则比较 `newNun`和 `stackMin`的栈顶元素中哪一个更小。如果 `newNum`更小或两者相等，则 `newNum`也压入 `stackMin`，如果 `stackMin`中栈顶元素小，则把 `stackMin`的栈顶元素重复压入 `stackMin`，即在栈顶元素上再压入一个栈顶元素。如下图，

![](https://gitee.com/JKcoding/imgs/raw/master/img/202111020057604.png)

### 弹出数据规则

在 `stackData`中弹出数据，弹出的数据记为 `value`; 弹出 `stackMin`中的栈顶，返回 value

很明显可以看出，压入与弹出规则是对应的。

### 查询当前栈中的最小值操作

由压入数据规则和弹出数据规则可知， `stackMin`始终记录着 `stackData`中的最小值，所以 `stackMin`的栈顶元素始终是当前 `stackData`中的最小值。

```java
public class MyStack2 {
    private Stack<Integer> stackData;
    private Stack<Integer> stackMin;

    public MyStack1() {
        stackData = new Stack<Integer>();
        stackMin = new Stack<Integer>();
    }

    public void push(int newNum) {
        if (this.stackData.isEmpty()) {
            this.stackMin.push(newNum);
        } else if (newNum <= this.stackMin.peek()) {
            this.stackMin.push(newNum);
        } else {
            this.stackMin.push(this.stackMin.peek());
        }
        this.stackData.push(newNum);
    }

    public int pop() {
        if (this.stackData.isEmpty()) {
            throw new RuntimeException("Your stack is empty.");
        }
        this.stackMin.pop();
        return this.stackData.pop();
    }
    
    public int getmin() {
        if (this.stackMin.isEmpty()) {
            throw new RuntimeException("Your stack is empty.");
        }
        return this.stackMin.peek();
    }
}
```

## 比较

共同点：时间复杂度`O(1)`, 空间复杂度`O(n)`

不同点：

- ​	方案一比较省空间，但是弹出操作时间更多，因为需要比较`stackData`弹出值和`stackMin`的栈顶值。
- ​	方案二`stackMin`需要消耗更多的空间，但是弹出操作时间更快。
