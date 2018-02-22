package util;

import entity.Person;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PersonGenerator {
    private final List<String> MALE_NAMES = Collections.unmodifiableList(Arrays.asList(
            "Alexander",
            "Maxim",
            "Ivan",
            "Artem",
            "Nikita",
            "Dmitriy",
            "Egor",
            "Daniil",
            "Mihail",
            "Andrey"
    ));
    ;
    private final List<String> FEMALE_NAMES = Collections.unmodifiableList(Arrays.asList(
            "Anastasia",
            "Maria",
            "Daria",
            "Anna",
            "Elizaveta",
            "Victoriya",
            "Polina",
            "Ekaterina",
            "Sofia",
            "Alexandra"
    ));
    private final List<String> LAST_NAMES = Collections.unmodifiableList(Arrays.asList(
            "Smirnov", "Ivanov", "Kuznetsov", "Sokolov",
            "Popov", "Lebedev", "Kozlov", "Novikov",
            "Morozov", "Petrov", "Volkov", "Soloviev",
            "Vasiliev", "Zaicev", "Pavlov", "Semenov",
            "Golubev", "Vinogradov", "Bogdanov", "Vorobiev",
            "Fedorov", "Mikhailov", "Belyaev", "Tarasov",
            "Belov", "Komarov", "Orlov", "Kisilev",
            "Makarov", "Andreev", "Kovalev", "Ilyin",
            "Gusev", "Titov", "Kuzmin", "Kudryavcev",
            "Baranov", "Kulikov", "Alekseev", "Stepanov",
            "Yakovlev", "Sorokin", "Sergeev", "Romanov",
            "Zakharov", "Borisov", "Korolev", "Gerasimov",
            "Ponomarev", "Grigoryev", "Zhikharev", "Nikulin"
    ));
    private Random random;

    public PersonGenerator() {
        random  = new Random(1);
    }

    public Person getNewPerson() {
        String firstName = "UNDEFINED";
        String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
        Person.Gender gender = Person.Gender.values()[random.nextInt(Person.Gender.values().length)];
        int age = random.nextInt(40) + 21;
        switch (gender) {
            case MALE:
                firstName = MALE_NAMES.get(random.nextInt(MALE_NAMES.size()));
                break;
            case FEMALE:
                firstName = FEMALE_NAMES.get(random.nextInt(FEMALE_NAMES.size()));
                lastName += 'a';
                break;
            case UNDEFINED:
                firstName = FEMALE_NAMES.get(random.nextInt(FEMALE_NAMES.size()));
                break;
        }
        return new Person(firstName, lastName, gender, age);
    }
}
