import entity.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyMap;
import util.PersonGenerator;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PerformanceTest extends Assert {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    Map<Integer, Person> myMap;
    PersonGenerator personGenerator;

    @Before
    public void init() {
        myMap = new MyMap<>(true);
        personGenerator = new PersonGenerator();
    }

    @Test
    public void getMaleName() {
        System.out.println(personGenerator.getMaleName());
    }

    @Test
    public void put5MillionPerson() {
        AtomicInteger id = new AtomicInteger(0);
        myMap.put(id.getAndIncrement(), new Person("Oleg", "Machine", Person.Gender.MALE, 24));
        myMap.put(id.getAndIncrement(), new Person("Eugen", "Alert", Person.Gender.MALE, 49));
        System.out.println(myMap);
    }
}
