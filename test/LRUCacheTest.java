import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LRUCacheTest {
    private LRUCache<String, Integer> cache;
    @BeforeEach
    void initLRU() {
        cache = new LRUCache<>(5);
        int cnt = 1;
        for (char c = 'a'; c <= 'd'; c++) {
            cache.insert(String.valueOf(c), cnt);
            cnt *= 2;
        }
    }

    @Test
    void insertSimple() {
        cache.insert("e", 123);
        assertEquals(cache.get("e"), 123);
        cache.insert("f", 20);
        assertEquals(cache.get("f"), 20);
        assertNull(cache.get("a"));
    }


    @Test
    void insertReplace() {
        cache.insert("e", 123);
        assertEquals(cache.get("e"), 123);
        assertEquals(cache.get("a"), 1);
        cache.insert("a", 10);
        cache.insert("f", 20);
        assertEquals(cache.get("f"), 20);
        assertEquals(cache.get("a"), 10);
        assertNull(cache.get("b"));
    }


    @Test
    void removeSimple() {
        assertEquals(cache.get("a"), 1);
        cache.remove("a");
        assertNull(cache.get("a"));
    }

    @Test
    void removeRedundant() {
        for (char c = 'a';  c <= 'z'; c++) {
            cache.remove(String.valueOf(c));
        }
        assertNull(cache.get("a"));
        assertNull(cache.get("z"));
        cache.insert("a", 1);
        assertEquals(cache.get("a"), 1);
    }

    @Test
    void get() {
        assertEquals(cache.get("a"), 1);
        assertNull(cache.get("fx"));
    }

    @Test
    void toList() {
        ArrayList<Map.Entry<String, Integer>> tmp = new ArrayList<>();
        int cnt = 1;
        for (char c = 'a'; c <= 'd'; c++) {
            tmp.add(new MyEntry<>(String.valueOf(c), cnt));
            cnt *= 2;
        }
        ArrayList<Map.Entry<String, Integer>> cacheList = cache.toList();
        assertEquals(cacheList.size(), tmp.size());
        for (int i = 0; i < cacheList.size(); i++) {
            assertEquals(cacheList.get(i).getKey(), tmp.get(i).getKey());
            assertEquals(cacheList.get(i).getValue(), tmp.get(i).getValue());
        }
    }
}