package util;

import entity.Person;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PersonGenerator {
    private List<String> maleNames;
    private List<String> femaleNames;
    private List<String> lastNames;
    private Random random = new Random(1);

    public PersonGenerator() {
        maleNames = Collections.unmodifiableList(Arrays.asList(
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
        femaleNames = Collections.unmodifiableList(Arrays.asList(
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
        lastNames = Collections.unmodifiableList(Arrays.asList(
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
    }

    public Person getNewPerson() {
        String firstName = "UNDEFINED";
        String lastName = lastNames.get(random.nextInt(lastNames.size()));
        Person.Gender gender = Person.Gender.values()[random.nextInt(Person.Gender.values().length)];
        int age = random.nextInt(40) + 21;
        switch (gender) {
            case MALE:
                firstName = maleNames.get(random.nextInt(maleNames.size()));
                break;
            case FEMALE:
                firstName = femaleNames.get(random.nextInt(femaleNames.size()));
                lastName += 'a';
                break;
            case UNDEFINED:
                firstName = femaleNames.get(random.nextInt(femaleNames.size()));
                break;
        }
        return new Person(firstName, lastName, gender, age);
    }
}
