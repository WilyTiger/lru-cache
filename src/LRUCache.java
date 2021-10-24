import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


final class MyEntry<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    public MyEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}

public class LRUCache<K, V> {
    public LRUCache() {
        capacity = STD_CAPACITY;
        hashMap= new HashMap<>();
        list = new LinkedList<>(this);
    }
    public LRUCache(int cap) {
        assert(0 < cap && cap < MAX_CAPACITY);
        capacity = cap;
        hashMap= new HashMap<>();
        list = new LinkedList<>(this);
    }

    public void insert(K key, V value) {
        assert(key != null);
        if (hashMap.containsKey(key)) {
            list.resetUsage(hashMap.get(key));
            hashMap.get(key).setValue(value);
        } else {
            if (list.size == capacity) {
                hashMap.remove(list.getHead().getKey());
            }
            list.pushData(key, value);
            hashMap.put(key, list.makeNode(key, value));
        }
    }
    public void remove(K key) {
        if (hashMap.containsKey(key)) {
            list.remove(hashMap.get(key));
            hashMap.remove(key);
        }
    }

    public V get(K key) {
        if (hashMap.containsKey(key)) {
            return hashMap.get(key).getValue();
        } else {
            return null;
        }
    }

    public ArrayList<Map.Entry<K, V>> toList() {
        ArrayList<Map.Entry<K, V>> res = new ArrayList<>();
        for (Map.Entry<K, LinkedList<K, V>.Node> entry: hashMap.entrySet()) {
            res.add(new MyEntry<>(entry.getKey(), entry.getValue().getValue()));
        }
        return res;
    }

    private static class LinkedList<K, V> {
        public LinkedList(LRUCache<K, V> cache) {
            size = 0;
            head = tail = null;
            capacity = cache.capacity;
        }
        public Node makeNode(K key, V value) {
            return new Node(key, value);
        }
        public void pushData(K key, V value) {
            push(makeNode(key, value));
        }
        public void push(Node node) {
            assert(0 <= size && size <= capacity);
            if (size == capacity) {
                pop();
            }
            if (tail != null) {
                tail.tieLeft(node);
                tail = node;
            } else {
                head = tail = node;
            }
            size++;
            assert(0 < size && size <= capacity);
        }
        public void pop() {
            if (head != null) {
                Node tmp = head.next;
                head.removeFromList();
                head = tmp;
                size--;
            }
            assert(size >= 0);
        }
        public void resetUsage(Node node) {
            assert(node != null);
            push(node.removeFromList());
        }
        public void remove(Node node) {
            assert(node != null && size > 0);
            node.removeFromList();
            size--;
        }
        public Node getHead() {
            assert(size > 0);
            return head;
        }
        private class Node {
            public Node(K k, V v) {
                key = k;
                value = v;
                next = prev = null;
            }
            public K getKey() {
                return key;
            }
            public V getValue() {
                return value;
            }
            public void setValue(V value) {
                this.value = value;
            }
            public void tieLeft(Node r) {
                assert(r != null);
                next = r;
                r.prev = this;
            }
            public Node removeFromList() {
                if (prev != null) {
                    prev.next = next;
                }
                if (next != null) {
                    next.prev = prev;
                }
                next = prev = null;
                return this;
            }
            private final K key;
            private V value;
            private Node next, prev;
        }
        private Node head, tail;
        private int size;
        private final int capacity;
    }

    private static final int STD_CAPACITY = 100;
    private static final int MAX_CAPACITY = 100000;
    public final int capacity;
    private final Map<K, LinkedList<K, V>.Node> hashMap;
    private final LinkedList<K, V> list;
}
