package application;

import java.util.Arrays;

public enum Condition {
	New("New"),
    UsedLikeNew("Used Like New"),
    ModeratelyUsed("Moderately Used"),
    HeavilyUsed("Heavily Used");

    private String value;

    private Condition(String value) {
        this.value = value;
    }

    public static String[] getAllValuesAsStrings() {
        return Arrays.stream(Condition.values())
                .map(Condition::getValue)
                .toArray(String[]::new);
    }

    // Getter for the value field
    public String getValue() {
        return this.value;
    }
}
