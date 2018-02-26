import entity.Person;
import entity.Person.Gender;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyMap;
import util.PersonGenerator;

public class MapTest extends Assert {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass().getName());
    private Map<Integer, Person> map;
    private Person golovachLena = new Person("Лена", "Головач", Person.Gender.MALE, 20);
    private Person pilnikYana = new Person("Яна", "Пильник", Person.Gender.FEMALE, 40);

    @Before
    public void init() {
        map = new MyMap<>();
        PersonGenerator personGenerator = new PersonGenerator();
        for (int i = 0; i < 15; i++) {
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
        assertNotNull(map.remove(1));
        assertNotNull(map.remove(5));
        assertNull(map.remove(5));
        assertEquals(length - 2, map.size());
    }

    @Test(timeout = 1_000)
    public void forEach() {
        map.entrySet().spliterator().forEachRemaining((entry) -> {
        });
        map.entrySet().parallelStream()
                .map(entry -> entry.getKey() + " " + entry.getValue())
                .forEach((entry) -> {
                });
        assertFalse(
                map.entrySet().parallelStream()
                        .filter(entry -> entry.getValue().getGender() == Person.Gender.MALE)
                        .anyMatch(entry -> entry.getValue().getGender() == Gender.FEMALE)
        );
        map.values().parallelStream()
                .filter(value -> value.getAge() > 50)
                .forEach((value) -> {
                    assertTrue(value.getAge() > 50);
                });
        map.keySet().parallelStream()
                .filter(key -> key % 2 == 0)
                .forEach((k) -> {
                    assertFalse(k % 2 == 1);
                });
    }

    @Test
    public void keySet() {
        int size = map.size();
        assertNotNull(map.keySet());
        map.keySet().remove(2);
        assertEquals(size - 1, map.size());
    }

    @Test
    public void values() {
        map.put(6, golovachLena);
        assertTrue(map.values().remove(golovachLena));
    }

    @Test
    public void entrySet() {
        assertNotNull(map.entrySet());
        Set<Map.Entry<Integer, Person>> set = map.entrySet();
        assertTrue(set.remove(1));
        assertTrue(set.size() == map.size());
    }

}
