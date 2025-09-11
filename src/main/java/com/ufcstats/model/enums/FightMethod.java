package com.ufcstats.model.enums;

/**
 * Метод завершения боя
 */
public enum FightMethod {
    DECISION("Решение"),
    SUBMISSION("Сабмишен"),
    KNOCKOUT("Нокаут"),
    EARLY_EXIT("Досрочный выход");

    private final String displayName;

    FightMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
