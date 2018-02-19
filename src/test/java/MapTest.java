import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyMap;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

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
        map.put(12, "ответ");
        map.put(25, "ответ");
        map.put(401, "ответ");
        map.put(140, "ответ");
        map.put(4010, "ответ");
        map.put(401204, "ответ");
        map.put(401211, "ответ");
        map.put(40151, "ответ");
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
        map.put(4, "Три");
        assertEquals("Три", map.get(4));
        assertEquals("Три", map.put(4, "Четыре"));
        map.put(2, "Велосипед");
        map.put(2, "Трактор");
        assertEquals("Трактор", map.get(2));
        System.out.println(((MyMap)map).find(10));
        System.out.println(map);

    }

    @Test
    public void get() {
        assertNotNull(map.get(1));
        assertNull(map.get(3));
        System.out.println(map.get(42));
    }

    @Test
    public void hashCodeTest() {
        Map<String, String> stringMap = new MyMap<>(true);
        stringMap.put("Синий", "Человек");
        stringMap.put("Желтый", "Человек");
        stringMap.put("Красный", "Паук");
        stringMap.put("Фиолетовый", "Дровосек");
        stringMap.put("Дровосек", "Фиолетовый");
        System.out.println(stringMap);
        ((MyMap)stringMap).sort();
        System.out.println("Желтый".hashCode());
        System.out.println(((MyMap)stringMap).find("Желтый"));
        for (Object entry : stringMap.keySet()) {
            System.out.print(entry.hashCode() + " ");
        }
        System.out.println(stringMap);
    }

    @Test
    public void remove() {
        assertEquals("пять", map.remove(5));
        map.remove(42);
        map.remove(2);
        map.remove(1);
        System.out.println(map);
        map.remove(10);
        System.out.println(map);
    }

    @Test(timeout = 1000)
    @Ignore
    public void forEach() {
        map.entrySet().stream().parallel().forEach(System.out::println);
    }

    @Test
    @Ignore
    public void keySet() {
        assertNotNull(map.keySet());
        map.keySet().remove(2);
        System.out.println(map.keySet());
    }

    @Test
    @Ignore
    public void values() {
        System.out.println(map.values());
        Collection<String> list = map.values();
        System.out.println(list.remove("Синхрофазатрон"));
        System.out.println(map.values());
        System.out.println(map);
//        assertNotNull(map.values());
    }

    @Test
    @Ignore
    public void entrySet() {
        assertNotNull(map.entrySet());
        LOG.info(map.entrySet().toString());
        Set<Map.Entry<Integer, String>> set = map.entrySet();
        set.remove(1);
        set.remove(2);
        System.out.println(map);
    }

    @Test
    @Ignore
    public void someTest() {
        System.out.println();
        /*UnaryOperator<Integer> l = (k) -> {
            System.out.println(k);
            return 4+k;
        };
        UnaryOperator<Integer> i = (k) -> {
            System.out.println(k);
            return 5*k;
        };
        System.out.println(i.andThen(l).apply(5));*/
    }
}
