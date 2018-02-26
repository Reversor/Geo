import entity.Person;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyMap;
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
        avgTime = 0;
        personGenerator = new PersonGenerator();
    }

    @After
    public void afterTest() {
        myMap = null;
    }

    @Test
    public void findMaxAge() {
        testCount = 1;
        long time = 0;
        int size = 5_000_000;
        myMap = new MyMap<>(size, 1, null);
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
        testCount = 1;
        long time = 0;
        int size = 5_000_000;
        for (int c = 0; c <= testCount; c++) {
            myMap = new MyMap<>(size, 1, key -> key.hashCode() ^ (key.hashCode() >>> 16));
//            myMap = new HashMap<>();
            time = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                Person person = personGenerator.getNewPerson();
                myMap.put(i, person);
            }
            avgTime += System.currentTimeMillis() - time;
            assertEquals(size, myMap.size());
        }
        logger.info(Float.toString((float) avgTime / testCount));
    }

    @Test
    @Ignore
    public void some() {
        int count = 512;
        //System.out.println(count = -~count); // крайний изврат
        System.out.println(count & (count - 1)); //power of two
        System.out.println(1 << (int) StrictMath.log10(count)); // strange
        int capacity = 5_000_000;
        int n = capacity - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        System.out.println(n + 1);

        int hash = personGenerator.getNewPerson().hashCode();
        System.out.println(hash);
        System.out.println(hash ^ hash >>> 16);
        System.out.println((hash ^ hash >>> 16) ^ hash >>> 16); // yeah
        System.out.println(1 << 30);
        System.out.println((1 << 31 - 1) - Integer.MAX_VALUE);
    }

}
