import entity.Person;
import entity.Person.Gender;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.PersonGenerator;

public class MapTest extends Assert {

    private final Person golovachLena = new Person("Лена", "Головач", Person.Gender.MALE, 20);
    private final Person pilnikYana = new Person("Яна", "Пильник", Person.Gender.FEMALE, 40);
    private Map<Integer, Person> map;

    @Before
    public void init() {
        map = new HashMap<>();
        PersonGenerator personGenerator = new PersonGenerator();
        for (int i = 0; i < 5_000_000; i++) {
            map.put(i, personGenerator.getNewPerson());
        }
    }

    @Test
    public void put() {
        assertNotNull(map.put(4, golovachLena));
        assertEquals(golovachLena, map.put(4, pilnikYana));
        assertEquals(pilnikYana, map.get(4));
//        System.out.println(map);
        assertEquals(5_000_000, map.size());
    }

    @Test
    public void get() {
        assertNotNull(map.get(1));
        assertNull(map.get(5_000_001));
    }

    @Test
    public void remove() {
        int length = map.size();
        assertNotNull(map.remove(1));
        assertNotNull(map.remove(5));
        assertNull(map.remove(5));
        assertEquals(length - 2, map.size());
    }

    @Test(timeout = 20_000)
    public void forEach() {
        long time = System.currentTimeMillis();
        map.entrySet().spliterator().forEachRemaining((entry) -> {
        });
        System.out.println(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        map.entrySet().stream()//.parallel()
                .map(entry -> entry.getKey() + " " + entry.getValue())
                .forEach((entry) -> {
                });
        System.out.println(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        assertFalse(
                map.entrySet().stream()//.parallel()
                        .filter(entry -> entry.getValue().getGender() == Person.Gender.MALE)
                        .anyMatch(entry -> entry.getValue().getGender() == Gender.FEMALE)
        );
        System.out.println(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        map.values().stream()//.parallel()
                .filter(value -> value.getAge() > 50)
                .forEach(value -> assertTrue(value.getAge() > 50));
        System.out.println(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        map.keySet().stream()//.parallel()
                .filter(key -> key % 2 == 0)
                .forEach(k -> assertFalse(k % 2 == 1));
        System.out.println(System.currentTimeMillis() - time);
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
        assertFalse(set.remove(golovachLena));
        assertTrue(set.size() == map.size());
    }
}
