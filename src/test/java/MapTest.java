import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.MyMap;

import java.util.Map;

public class MapTest extends Assert {
    private Map map;
    @Before
    public void init() {
        map = new MyMap<Integer, String>(10);
        assertNotNull(map);
    }

    @Test
    public void put() {
        map.put(1, "Один");
    }
}
