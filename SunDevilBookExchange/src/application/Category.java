package application;

import java.util.Arrays;
import java.util.List;

public enum Category {
    NaturalScience("Natural Science Books"),
    Computer("Computer Books"),
    Math("Math Books"),
    EnglishLanguage("English Language Books"),
    ScienceFiction("Science Fiction Books"),
    Fantasy("Fantasy Books");

    private String value;

    private Category(String value) {
        this.value = value;
    }

    public static String[] getAllValuesAsStrings() {
        return Arrays.stream(Category.values())
                .map(Category::getValue) // Map each Category to its associated string value
                .toArray(String[]::new); // Collect the results into an array
    }

    // Getter for the value field
    public String getValue() {
        return this.value;
    }
}
