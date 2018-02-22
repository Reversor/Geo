import entity.Person;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.PersonGenerator;

import java.util.HashMap;
import java.util.Map;

public class PerformanceTest extends Assert {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private int testCount;
    private long avgTime;
    private Map<Integer, Person> myMap;
    private PersonGenerator personGenerator;

    @Before
    public void init() {
        myMap = new HashMap<>();
        avgTime = 0;
        personGenerator = new PersonGenerator();
    }

    @After
    public void afterTest() {
        myMap = null;
    }

    @Test
    public void findMaxAge() {
        testCount = 1_000;
        long time = 0;
        int size = 5_000_000;
        for (int i = 0; i < size; i++) {
            Person person = personGenerator.getNewPerson();
            myMap.put(i, person);
        }
        int max = 0;
        for (int i = 0; i < testCount; i++) {
            time = System.currentTimeMillis();
            max = myMap.entrySet().stream().mapToInt(e -> e.getValue().getAge()).max().getAsInt();
            avgTime += System.currentTimeMillis() - time;
        }
        logger.info(Float.toString((float) avgTime / testCount));
        assertEquals(60, max);
    }

    @Test
    public void put5MillionPersonToMap() {
        testCount = 100;
        long time = 0;
        int size = 5_000_000;
        for (int c = 0; c <= testCount; c++) {
            for (int i = 0; i < size; i++) {
                Person person = personGenerator.getNewPerson();
                time = System.currentTimeMillis();
                myMap.put(i, person);
                avgTime += System.currentTimeMillis() - time;
            }
            assertEquals(size, myMap.size());
        }
        logger.info(Float.toString((float) avgTime / testCount));
    }

}
