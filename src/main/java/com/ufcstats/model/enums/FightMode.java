package com.ufcstats.model.enums;

/**
 * Режим боя в UFC 5
 */
public enum FightMode {
    STANCE("Стойка"),
    MMA("ММА");

    private final String displayName;

    FightMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
