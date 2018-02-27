import entity.Person;
import entity.Person.Gender;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyMap;
import util.PersonGenerator;

public class StreamTest extends Assert {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private long avgTime;
    private int testCount;
    private int size;
    private Map<Integer, Person> myMap;
    private PersonGenerator personGenerator;

    @Before
    public void init() {
//        size = 5_000_000;
        size = 20;
        testCount = 1;
        avgTime = 0;
        personGenerator = new PersonGenerator();
        myMap = new MyMap<>();
//        myMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            myMap.put(i, personGenerator.getNewPerson());
        }
    }

    @Test
    public void mihailCount() {
        logger.info(Long.toString(myMap.values()
                        .parallelStream()
//                .stream()
                        .filter(v -> v.getFirstName().equals("Mihail"))
                        .count())
        );
    }

    @Test
    public void limitTest() {
        logger.info("limit test started:");
//        myMap.entrySet().parallelStream().limit(20).forEach(System.out::println);
//        myMap.entrySet().stream().limit(20).forEach(System.out::println);
        myMap.entrySet().stream()
                .sorted(Comparator.comparingInt(Entry::getKey))
                .limit(20).forEach(System.out::println);
    }

    @Test
    public void skipTest() {
        logger.info("skip test started");
        myMap.entrySet().parallelStream().skip(30).limit(20).forEach(System.out::println);
        myMap.entrySet().stream().skip(30).limit(20).forEach(System.out::println);
        myMap.entrySet().stream().sorted(Comparator.comparingInt(Entry::getKey))
                .skip(30).limit(20).forEach(System.out::println);
    }

    @Test
    public void summaryTable() {
        logger.info("summary test started");
        Map<Gender, Map<Integer, Long>> summary = myMap.values().stream()
                .collect(Collectors.groupingBy(Person::getGender,
                        Collectors.groupingBy(Person::getAge, Collectors.counting())));
        System.out.println(summary);
        long count = summary.values()
                .stream().flatMap(map -> map.values().stream())
                .mapToLong(v -> v)
                .sum();
        System.out.println(count);
    }

    @Test
    public void avgAge() {
        logger.info("AverAge test started");
        double avg = myMap.values().stream().collect(Collectors.averagingInt(Person::getAge));
        double avg2 = myMap.values().stream().mapToInt(Person::getAge).average().getAsDouble();
        System.out.println(avg + " " + avg2);
    }

    @Test
    public void median() {
        double median = myMap.values().stream()
                .mapToInt(Person::getAge)
                .sorted()
                .skip((myMap.size() % 2) == 0
                        ? myMap.size() / 2 - 1
                        : myMap.size() / 2 + 1
                )
                .limit((myMap.size() % 2) == 0 ? 2 : 1)
                .peek(System.out::println)
                .average().getAsDouble();
        System.out.println(median);
        System.out.println(((double) myMap.size() + 1) / 2);
    }

    @Test
    public void ageSpreading() {
        System.out.println(myMap.values().stream()
                .collect(Collectors.groupingBy(Person::getAge, Collectors.counting())));
    }

    @Test
    public void percentil() {
        int percent = 25;
        /*double median = myMap.values().stream()
                .mapToInt(Person::getAge)
                .sorted()
                .skip((myMap.size() % 2) == 0
                        ? myMap.size() / 2 - 1
                        : myMap.size() / 2 + 1
                )
                .limit((myMap.size() % 2) == 0 ? 2 : 1)
                .peek(System.out::println)
                .average().getAsDouble();*/
        /*double percentil = myMap.values().stream()
                .mapToInt(Person::getAge)
                .sorted()
                .skip((myMap.size() * percent / 100) - 1)
                .limit(1)
                .average().getAsDouble();*/
        System.out.println(percentil);
    }
}
