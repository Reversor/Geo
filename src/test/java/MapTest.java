import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyMap;

import java.util.Map;
import java.util.Set;

public class MapTest extends Assert {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass().getName());
    private Map<Integer, String> map;

    @Before
    public void init() {
        map = new MyMap<>(500, 0.5f, (i) -> (i.hashCode() * 71 % 23));
    }

    @Test
    public void put() {
        long time = System.nanoTime();
        map.put(1, "Один");
        map.put(2, "Два");
        map.put(10, "Синхрофазатрон");
        map.put(5, "пять");
        map.put(42, "ответ");
        map.put(12, "ответ");
        map.put(25, "ответ");
        map.put(401, "ответ");
        map.put(140, "ответ");
        map.put(4010, "ответ");
        map.put(401204, "ответ");
        map.put(401211, "ответ");
        map.put(40151, "ответ");
        map.put(7_000_000, "семьлион");
        System.out.println(System.nanoTime() - time);
        assertNull(map.put(4, "Три"));
        assertEquals("Три", map.put(4, "Четыре"));
        assertEquals("Четыре", map.get(4));

    }

    @Test
    public void get() {
        assertNotNull(map.get(1));
        assertNull(map.get(3));
    }

    @Test
    public void remove() {
        int length = map.size();
        assertEquals("пять", map.remove(5));
        assertEquals("ответ", map.remove(42));
        assertEquals(length - 2, map.size());
    }

    @Test(timeout = 1000)
    public void forEach() {
        map.entrySet().parallelStream()
                .map(entry -> entry.getKey() + " " + entry.getValue())
                .forEach((e) -> {
                });
        assertFalse(
                map.entrySet().parallelStream()
                        .filter(entry -> !entry.getValue().equals("ответ"))
                        .anyMatch(entry -> entry.getValue().equals("ответ"))
        );
        map.values().parallelStream()
                .filter(value -> value.equals("ответ"))
                .forEach((e) -> {
                });
        map.keySet().parallelStream()
                .filter(key -> key % 2 == 0)
                .forEach((e) -> {
                });

    }

    @Test
    public void keySet() {
        assertNotNull(map.keySet());
        map.keySet().remove(2);
    }

    @Test
    public void values() {
        assertTrue(map.values().remove("семьлион"));
        assertNull(map.get(7_000_000));
    }

    @Test
    public void entrySet() {
        assertNotNull(map.entrySet());
        Set<Map.Entry<Integer, String>> set = map.entrySet();
        assertTrue(set.remove(1));
        assertTrue(set.remove(2));
    }

}
