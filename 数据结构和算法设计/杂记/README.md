### 设计RandomPool结构 

【题目】 

​	设计一种结构，在该结构中有如下三个功能: 

- insert(key): 将某个key加入到该结构，做到不重复加入 
- delete(key): 将原本在结构中的某个key移除 
- getRandom(): 等概率随机返回结构中的任何一个key。 

【要求】 

​	Insert、delete和getRandom方法的时间复杂度都是O(1)

```java
public class Pool<K> {
    private HashMap<K, Integer> keyIndexMap = new HashMap<>();
    private HashMap<Integer, K> indexKeyMap = new HashMap<>();
    private int size;

    public void insert(K key) {
        if (!this.keyIndexMap.containsKey(key)) {
            this.keyIndexMap.put(key, this.size);
            this.indexKeyMap.put(this.size++, key);
        }
    }

    public void delete(K key) {
        if (this.keyIndexMap.containsKey(key)) {
            int deleteIndex = this.keyIndexMap.get(key);
            int lastIndex = --this.size;
            K lastKey = this.indexKeyMap.get(lastIndex);
            this.keyIndexMap.put(lastKey, deleteIndex);
            this.indexKeyMap.put(deleteIndex, lastKey);
            this.keyIndexMap.remove(key);
            this.indexKeyMap.remove(lastIndex);
        }
    }

    public K getRandom() {
        if (this.size == 0) {
            return null;
        }
        int randomIndex = (int) (Math.random() * this.size);
        return this.indexKeyMap.get(randomIndex);
    }
}
```

