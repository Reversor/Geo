import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.MyMap;

import java.util.Map;

public class MapTest extends Assert {
    private Map<Integer, String> map;

    @Before
    public void init() {
        map = new MyMap<>();
        assertNotNull(map);
        map.put(1, "Один");
        map.put(2, "Два");

    }

    @Test
    public void get() {
        String value = map.get(2);
        assertNotNull(value);
    }
}
