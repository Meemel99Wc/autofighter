package autofighter.ui.util;

import lombok.Getter;

@Getter
public class CombatTarget {

    final String name;

    final int level;

    public CombatTarget(String name, int level) {
        this.name = name;
        this.level = level;
    }

    @Override
    public String toString() {
        return "Target [name=" + name + ", level=" + level + "]";
    }
}
