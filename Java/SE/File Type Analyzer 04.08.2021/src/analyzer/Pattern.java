package analyzer;

import java.util.Comparator;

public class Pattern {
    private final int priority;
    private final String value;
    private final String name;

    Pattern(int priority, String value, String name) {
        this.priority = priority;
        this.value = value;
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}

class SortByPriority implements Comparator<Pattern> {

    @Override
    public int compare(Pattern o1, Pattern o2) {
        return o1.getPriority() - o2.getPriority();
    }
}
