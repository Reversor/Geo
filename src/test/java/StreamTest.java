import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import entity.Person;
import entity.Person.Gender;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MyMap;
import util.PersonGenerator;

@Ignore
public class StreamTest extends Assert {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private Map<Integer, Person> myMap;

    @Before
    public void init() {
        int size = 5_000_000;
        PersonGenerator personGenerator = new PersonGenerator();
        myMap = new MyMap<>();
        for (int i = 0; i < size; i++) {
            myMap.put(i, personGenerator.getNewPerson());
        }
    }

    @Test
    public void mihailCount() {
        logger.info(Long.toString(myMap.values()
                .parallelStream()
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
                .collect(groupingBy(Person::getGender, groupingBy(Person::getAge, counting())));
        System.out.println(summary);
        long count = summary.values()
                .stream()
                .flatMap(map -> map.values().stream())
                .reduce((a, v) -> a += v)
                .orElse(0L);
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
                        : myMap.size() / 2 + 1)
                .limit((myMap.size() % 2) == 0 ? 2 : 1)
                .peek(System.out::println)
                .average().getAsDouble();
        System.out.println(median);
        System.out.println(((double) myMap.size() + 1) / 2);
    }

    @Test
    public void ageSpreading() {
        System.out.println(myMap.values().stream()
                .collect(Collectors.groupingBy(Person::getAge, counting())));
    }

    @Test
    public void percentil() {
        int percent = 25;
        Map<Integer, Long> map = myMap.values().stream()
                .collect(Collectors.groupingBy(Person::getAge, counting()));
        double percentil = map.values().stream()
                .mapToLong(Long::longValue)
                .sorted()
                .skip(map.size() % (100 / percent) == 0
                        ? (map.size() * percent / 100) - 1
                        : (map.size() * percent / 100) + 1)
                .limit(map.size() % (100 / percent) == 0 ? 2 : 1)
                .peek(System.out::println)
                .average()
                .getAsDouble();
        System.out.println(percentil);
    }

    @Test
    public void percentrank() {
        Map<Integer, Double> percentrank = myMap.values().stream()
                .collect(Collectors.groupingBy(Person::getAge,
                        collectingAndThen(counting(), e -> ((double) e / myMap.size()) * 100)));

        System.out.println(percentrank);
        System.out.println(percentrank.values()
                .stream().reduce(0d, (a, v) -> a += v).byteValue());
    }
}
