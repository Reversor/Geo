package util;

import entity.Person;

import java.util.Random;

public class PersonGenerator {
    private static final String[] MALE_NAMES = new String[]{
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
    };
    private static final String[] FEMALE_NAMES = new String[]{
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
    };
    private static final String[] LAST_NAMES = new String[]{
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
    };

    private Random random;
    private int seed;

    public PersonGenerator() {
        reset();
    }

    public void reset() {
        this.seed = 1;
        random = new Random(seed);
    }

    public void setSeed(int seed) {
        this.seed = seed;
        random.setSeed(this.seed);
    }

    public Person getNewPerson() {
        String firstName = "UNDEFINED";
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        Person.Gender gender = Person.Gender.values()[random.nextInt(Person.Gender.values().length)];
        int age = random.nextInt(40) + 21;
        switch (gender) {
            case MALE:
                firstName = MALE_NAMES[random.nextInt(MALE_NAMES.length)];
                break;
            case FEMALE:
                firstName = FEMALE_NAMES[random.nextInt(FEMALE_NAMES.length)];
                lastName += 'a';
                break;
            case UNDEFINED:
                firstName = FEMALE_NAMES[random.nextInt(FEMALE_NAMES.length)];
                break;
        }
        return new Person(firstName, lastName, gender, age);
    }
}
