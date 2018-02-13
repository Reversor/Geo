import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyMap;

import java.util.*;

public class MapTest extends Assert {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass().getName());
    private Map<Integer, String> map;

    @Before
    public void init() {
        map = new MyMap<>();
        map.put(1, "Один");
        map.put(2, "Два");
        map.put(5, "пять");
    }

    @Test
    public void put() {
        assertEquals(3, map.size());
        assertEquals("Один", map.put(1, "Семь"));
        assertEquals(3, map.size());
        map.put(4, "Три");
        assertEquals(4, map.size());
        assertEquals("Три", map.get(4));
    }

    @Test
    public void get() {
        assertNotNull(map.get(1));
        assertNull(map.get(3));
    }

    @Test
    public void remove() {

    }

    @Test
    @Ignore
    public void forEach() {
        //TODO
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
        List<String> testValues = new ArrayList<>();
        testValues.add("Один");
        testValues.add("Два");
        testValues.add("пять");
        assertEquals(testValues, map.values());
    }

    @Ignore
    @Test
    public void entrySet() {
        assertNotNull(map.entrySet());
        LOG.info(map.entrySet().toString());
    }
}
