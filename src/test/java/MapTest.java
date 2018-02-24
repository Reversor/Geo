import entity.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyMap;
import util.PersonGenerator;

import java.util.Map;
import java.util.Set;

public class MapTest extends Assert {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass().getName());
    private Map<Integer, Person> map;
    private Person golovachLena = new Person("Лена", "Головач", Person.Gender.MALE, 20 );
    private Person pilnikYana = new Person("Яна", "Пильник", Person.Gender.FEMALE, 40);

    @Before
    public void init() {
        map = new MyMap<>();
        PersonGenerator personGenerator = new PersonGenerator();
        for (int i = 0; i < 15; i++ ) {
            map.put(i, personGenerator.getNewPerson());
        }
    }

    @Test
    public void put() {
        assertNotNull(map.put(4, golovachLena));
        assertEquals(golovachLena, map.put(4, pilnikYana));
        assertEquals(pilnikYana, pilnikYana);

    }

    @Test
    public void get() {
        assertNotNull(map.get(1));
        assertNull(map.get(15));
    }

    @Test
    public void remove() {
        int length = map.size();
        assertNotNull(map.remove(5));
        assertNull(map.remove(5));
        assertEquals(length - 1, map.size());
    }

    @Test(timeout = 1000)
    @Ignore
    public void forEach() {
        map.entrySet().parallelStream()
                .map(entry -> entry.getKey() + " " + entry.getValue())
                .forEach((e) -> {
                });
        assertFalse(
                map.entrySet().parallelStream()
                        .filter(entry -> !entry.getValue().equals("ответ"))
                        .anyMatch(entry -> entry.getValue().equals("ответ"))
        );
        map.values().parallelStream()
                .filter(value -> value.equals("ответ"))
                .forEach((e) -> {
                });
        map.keySet().parallelStream()
                .filter(key -> key % 2 == 0)
                .forEach((e) -> {
                });

    }

    @Test
    @Ignore
    public void keySet() {
        assertNotNull(map.keySet());
        map.keySet().remove(2);
    }

    @Test
    @Ignore
    public void values() {
        map.put(6, golovachLena);
        assertTrue(map.values().remove(golovachLena));
    }

    @Test
    @Ignore
    public void entrySet() {
        assertNotNull(map.entrySet());
        Set<Map.Entry<Integer, Person>> set = map.entrySet();
        assertTrue(set.remove(1));
        assertTrue(set.size() == map.size());
    }

}
