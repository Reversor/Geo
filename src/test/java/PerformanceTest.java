import entity.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyMap;
import util.PersonGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PerformanceTest extends Assert {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    Map<Integer, Person> myMap;
    PersonGenerator personGenerator;

    @Before
    public void init() {
        personGenerator = new PersonGenerator();
    }

    @Test
    public void put5MillionPersonHashMap() {
        long sum = 0;
        for (int c = 0; c <= 100; c++) {
            myMap = new HashMap<>();
            int size = 5_000_000;
            long time = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                myMap.put(i, personGenerator.getNewPerson());
            }
            assertEquals(size, myMap.size());
            sum += System.currentTimeMillis() - time;
        }
        System.out.println( (float) sum / 100);
    }

    @Test
    public void put5MillionPersonMyMap() {
        myMap = new MyMap<>(true);
        Map<Integer, Person> buf = new HashMap<>();
        int size = 5_000_000;
        long time = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            myMap.put(i, personGenerator.getNewPerson());
        }
        myMap.putAll(buf);
        assertEquals(size, myMap.size());
        System.out.println(myMap);

    }
}
