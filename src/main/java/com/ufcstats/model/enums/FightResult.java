package com.ufcstats.model.enums;

/**
 * Результат боя
 */
public enum FightResult {
    WIN("Победа"),
    LOSS("Поражение"),
    DRAW("Ничья");

    private final String displayName;

    FightResult(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
