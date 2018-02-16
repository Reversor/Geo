import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapTest extends Assert {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass().getName());
    private Map<Integer, String> map;

    @Before
    public void init() {
        map = new MyMap<>();
        map.put(1, "Один");
        map.put(2, "Два");
        map.put(10, "Синхрофазатрон");
        map.put(5, "пять");
        map.put(42, "ответ");
    }

    @Test
    public void array() {
        int[] arr = new int[20];
        for (Integer i : arr) {
            System.out.println(i);
        }
    }

    @Test
    public void put() {
        assertEquals(3, map.size());
        assertEquals("Один", map.put(1, "Семь"));
        assertEquals(3, map.size());
        map.put(4, "Три");
        assertEquals(4, map.size());
        assertEquals("Три", map.get(4));
        assertEquals("Три", map.put(4, "Четыре"));
        map.clear();
        map.put(2, "Велосипед");
        map.put(2, "Трактор");
        assertEquals("Трактор", map.get(2));
    }

    @Test
    public void get() {
        assertNotNull(map.get(1));
        assertNull(map.get(3));
        System.out.println(map.get(42));
    }

    @Test
    public void remove() {
        assertEquals("пять", map.remove(5));
        assertEquals(2, map.size());
    }

    @Test(timeout = 1000)
    @Ignore
    public void forEach() {
//        map.entrySet().spliterator().forEachRemaining(System.out::println);
        map.forEach((k, v) -> System.out.println(k + " " + v));
//        map.keySet().stream().parallel().forEach(System.out::println);
//        map.keySet().spliterator().forEachRemaining(System.out::println);
//        map.values().spliterator().forEachRemaining(System.out::println);
    }

    @Test
    @Ignore
    public void keySet() {
        Set<Integer> testKeys = new HashSet<>();
        testKeys.add(1);
        testKeys.add(2);
        testKeys.add(5);
        assertEquals(testKeys, map.keySet());
    }

    @Test
    @Ignore
    public void values() {
        System.out.println(map.toString());
//        assertNotNull(map.values());
    }

    @Test
    @Ignore
    public void entrySet() {
        assertNotNull(map.entrySet());
        assertEquals(3, map.entrySet().size());
//        map.entrySet().remove();
        LOG.info(map.entrySet().toString());
    }
}
