package util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PersonGenerator {
    private List<String> maleName;
    private List<String> femaleName;


    public PersonGenerator() {
        maleName = Collections.unmodifiableList(Arrays.asList(
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
        femaleName = Collections.unmodifiableList(Arrays.asList(
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

    }

    public List<String> getMaleName() {
        return maleName;
    }
}
