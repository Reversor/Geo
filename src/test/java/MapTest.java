import org.junit.Assert;
import org.junit.Before;
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
    public void get() {
        LOG.info("Start get test");
        assertNotNull(map.get(1));
        assertNull(map.get(3));
        assertNotNull(map.get(5));
        LOG.info("Success get test");
    }

    @Test
    public void forEach() {
        LOG.info("Start forEach test");
        //TODO
    }

    @Test
    public void keySet() {
        LOG.info("Start keySet test");
        Set<Integer> testKeys = new HashSet<>();
        testKeys.add(1);
        testKeys.add(2);
        testKeys.add(5);
        assertEquals(testKeys, map.keySet());
        LOG.info("Success keySet test");
    }

    @Test
    public void values() {
        LOG.info("Start values test");
        List<String> testValues = new ArrayList<>();
        testValues.add("Один");
        testValues.add("Два");
        testValues.add("пять");
        assertEquals(testValues, map.values());
        LOG.info("Success values test");
    }

    @Test
    public void entrySet() {
        LOG.info("Start entrySet test");
        assertNotNull(map.entrySet());
        LOG.info(map.entrySet().toString());
        LOG.info("Success entrySet test");
    }
}
