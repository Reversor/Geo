import entity.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyMap;
import util.PersonGenerator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PerformanceTest extends Assert {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private Map<Integer, Person> myMap;
    private PersonGenerator personGenerator;
    int testCount;

    @Before
    public void init() {
        testCount = 0;
        personGenerator = new PersonGenerator();
    }

    @Test
    public void put5MillionPersonHashMap() {
        long sum = 0;
        testCount = 1;
        for (int c = 0; c <= testCount; c++) {
            myMap = new HashMap<>();
            long time = 0;
            int size = 5_000_000;
            for (int i = 0; i < size; i++) {
                Person person = personGenerator.getNewPerson();
                time = System.currentTimeMillis();
                myMap.put(i, person);
                time = System.currentTimeMillis() - time;
                sum += time;
            }
            assertEquals(size, myMap.size());
        }
//        System.out.println((float) sum / testCount);
        int max = 0;
        long time = System.currentTimeMillis();
        long buf = 0;
        for (int i = 0; i < 1000; i++) {
            time = System.currentTimeMillis();
            max = myMap.entrySet().stream().mapToInt(e -> e.getValue().getAge()).max().getAsInt();
            buf += System.currentTimeMillis() - time;
            System.out.println(System.currentTimeMillis() - time);
        }
        System.out.println((float) buf / 1000);
        System.out.println(max);
    }

    @Test
    public void put5MillionPersonMyMap() {
        int size = 5_000_000;
        myMap = new MyMap<>(5_000_000, 1f, null);
        long time = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            myMap.put(i, personGenerator.getNewPerson());
        }
        System.out.println(System.currentTimeMillis() - time);
        assertEquals(size, myMap.size());
        /*int max = 0;
        long buf = 0;
        for (int i = 0; i < 1000; i++) {
            time = System.currentTimeMillis();
            max = myMap.entrySet().stream().mapToInt(e -> e.getValue().getAge()).max().getAsInt();
            buf += System.currentTimeMillis() - time;
        }
        System.out.println((float) buf / 1000);
        System.out.println(max);*/
    }
}
