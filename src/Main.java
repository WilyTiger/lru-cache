import java.util.Map;

public class Main {

    public static void main(String[] args) {
        LRUCache<Integer, Integer> cache = new LRUCache<>(5);
        for (int i = 0; i < 10; i++) {
            cache.insert(i, i);
        }
        for (Map.Entry<Integer, Integer> el : cache.toList()) {
            System.out.println(el.getKey().toString() + el.getValue().toString());
        }
    }
}
