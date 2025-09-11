package com.ufcstats.model.enums;

/**
 * Весовые категории в UFC 5
 */
public enum WeightClass {
    MINIMUM("Минимальный"),
    FLYWEIGHT("Наилегчайший"),
    BANTAMWEIGHT("Легчайший"),
    FEATHERWEIGHT("Полулегкий"),
    LIGHTWEIGHT("Легкий"),
    WELTERWEIGHT("Полусредний"),
    MIDDLEWEIGHT("Средний"),
    LIGHT_HEAVYWEIGHT("Полутяжелый"),
    HEAVYWEIGHT("Тяжелый");

    private final String displayName;

    WeightClass(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
