package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class PersonGenerator {
    private List<String> maleName;
    private List<String> femaleName;


    public PersonGenerator() {
        maleName = Collections.unmodifiableList(
                Stream.of(
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
                ).collect(ArrayList::new, ArrayList::add, ArrayList::addAll)
        );
        femaleName = Collections.unmodifiableList(
                Stream.of(
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
                ).collect(ArrayList::new, ArrayList::add, ArrayList::addAll)
        );

    }

    public List<String> getMaleName() {
        return maleName;
    }
}
