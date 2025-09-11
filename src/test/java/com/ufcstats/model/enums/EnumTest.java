package com.ufcstats.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для enum классов
 */
class EnumTest {

    @Test
    void fightMode_ShouldHaveCorrectDisplayNames() {
        assertEquals("Стойка", FightMode.STANCE.getDisplayName());
        assertEquals("ММА", FightMode.MMA.getDisplayName());
    }

    @Test
    void fightResult_ShouldHaveCorrectDisplayNames() {
        assertEquals("Победа", FightResult.WIN.getDisplayName());
        assertEquals("Поражение", FightResult.LOSS.getDisplayName());
        assertEquals("Ничья", FightResult.DRAW.getDisplayName());
    }

    @Test
    void fightMethod_ShouldHaveCorrectDisplayNames() {
        assertEquals("Решение", FightMethod.DECISION.getDisplayName());
        assertEquals("Сабмишен", FightMethod.SUBMISSION.getDisplayName());
        assertEquals("Нокаут", FightMethod.KNOCKOUT.getDisplayName());
        assertEquals("Досрочный выход", FightMethod.EARLY_EXIT.getDisplayName());
    }

    @Test
    void weightClass_ShouldHaveCorrectDisplayNames() {
        assertEquals("Минимальный", WeightClass.MINIMUM.getDisplayName());
        assertEquals("Наилегчайший", WeightClass.FLYWEIGHT.getDisplayName());
        assertEquals("Легчайший", WeightClass.BANTAMWEIGHT.getDisplayName());
        assertEquals("Полулегкий", WeightClass.FEATHERWEIGHT.getDisplayName());
        assertEquals("Легкий", WeightClass.LIGHTWEIGHT.getDisplayName());
        assertEquals("Полусредний", WeightClass.WELTERWEIGHT.getDisplayName());
        assertEquals("Средний", WeightClass.MIDDLEWEIGHT.getDisplayName());
        assertEquals("Полутяжелый", WeightClass.LIGHT_HEAVYWEIGHT.getDisplayName());
        assertEquals("Тяжелый", WeightClass.HEAVYWEIGHT.getDisplayName());
    }

    @Test
    void fightMode_ShouldHaveCorrectValues() {
        FightMode[] modes = FightMode.values();
        assertEquals(2, modes.length);
        assertTrue(java.util.Arrays.asList(modes).contains(FightMode.STANCE));
        assertTrue(java.util.Arrays.asList(modes).contains(FightMode.MMA));
    }

    @Test
    void fightResult_ShouldHaveCorrectValues() {
        FightResult[] results = FightResult.values();
        assertEquals(3, results.length);
        assertTrue(java.util.Arrays.asList(results).contains(FightResult.WIN));
        assertTrue(java.util.Arrays.asList(results).contains(FightResult.LOSS));
        assertTrue(java.util.Arrays.asList(results).contains(FightResult.DRAW));
    }

    @Test
    void fightMethod_ShouldHaveCorrectValues() {
        FightMethod[] methods = FightMethod.values();
        assertEquals(4, methods.length);
        assertTrue(java.util.Arrays.asList(methods).contains(FightMethod.DECISION));
        assertTrue(java.util.Arrays.asList(methods).contains(FightMethod.SUBMISSION));
        assertTrue(java.util.Arrays.asList(methods).contains(FightMethod.KNOCKOUT));
        assertTrue(java.util.Arrays.asList(methods).contains(FightMethod.EARLY_EXIT));
    }

    @Test
    void weightClass_ShouldHaveCorrectValues() {
        WeightClass[] weightClasses = WeightClass.values();
        assertEquals(9, weightClasses.length);
        assertTrue(java.util.Arrays.asList(weightClasses).contains(WeightClass.MINIMUM));
        assertTrue(java.util.Arrays.asList(weightClasses).contains(WeightClass.FLYWEIGHT));
        assertTrue(java.util.Arrays.asList(weightClasses).contains(WeightClass.BANTAMWEIGHT));
        assertTrue(java.util.Arrays.asList(weightClasses).contains(WeightClass.FEATHERWEIGHT));
        assertTrue(java.util.Arrays.asList(weightClasses).contains(WeightClass.LIGHTWEIGHT));
        assertTrue(java.util.Arrays.asList(weightClasses).contains(WeightClass.WELTERWEIGHT));
        assertTrue(java.util.Arrays.asList(weightClasses).contains(WeightClass.MIDDLEWEIGHT));
        assertTrue(java.util.Arrays.asList(weightClasses).contains(WeightClass.LIGHT_HEAVYWEIGHT));
        assertTrue(java.util.Arrays.asList(weightClasses).contains(WeightClass.HEAVYWEIGHT));
    }

    @Test
    void fightMode_ShouldBeSerializable() {
        // Проверяем, что enum можно преобразовать в строку и обратно
        assertEquals("STANCE", FightMode.STANCE.name());
        assertEquals("MMA", FightMode.MMA.name());
        
        assertEquals(FightMode.STANCE, FightMode.valueOf("STANCE"));
        assertEquals(FightMode.MMA, FightMode.valueOf("MMA"));
    }

    @Test
    void fightResult_ShouldBeSerializable() {
        assertEquals("WIN", FightResult.WIN.name());
        assertEquals("LOSS", FightResult.LOSS.name());
        assertEquals("DRAW", FightResult.DRAW.name());
        
        assertEquals(FightResult.WIN, FightResult.valueOf("WIN"));
        assertEquals(FightResult.LOSS, FightResult.valueOf("LOSS"));
        assertEquals(FightResult.DRAW, FightResult.valueOf("DRAW"));
    }

    @Test
    void fightMethod_ShouldBeSerializable() {
        assertEquals("DECISION", FightMethod.DECISION.name());
        assertEquals("SUBMISSION", FightMethod.SUBMISSION.name());
        assertEquals("KNOCKOUT", FightMethod.KNOCKOUT.name());
        assertEquals("EARLY_EXIT", FightMethod.EARLY_EXIT.name());
        
        assertEquals(FightMethod.DECISION, FightMethod.valueOf("DECISION"));
        assertEquals(FightMethod.SUBMISSION, FightMethod.valueOf("SUBMISSION"));
        assertEquals(FightMethod.KNOCKOUT, FightMethod.valueOf("KNOCKOUT"));
        assertEquals(FightMethod.EARLY_EXIT, FightMethod.valueOf("EARLY_EXIT"));
    }

    @Test
    void weightClass_ShouldBeSerializable() {
        assertEquals("MINIMUM", WeightClass.MINIMUM.name());
        assertEquals("FLYWEIGHT", WeightClass.FLYWEIGHT.name());
        assertEquals("BANTAMWEIGHT", WeightClass.BANTAMWEIGHT.name());
        assertEquals("FEATHERWEIGHT", WeightClass.FEATHERWEIGHT.name());
        assertEquals("LIGHTWEIGHT", WeightClass.LIGHTWEIGHT.name());
        assertEquals("WELTERWEIGHT", WeightClass.WELTERWEIGHT.name());
        assertEquals("MIDDLEWEIGHT", WeightClass.MIDDLEWEIGHT.name());
        assertEquals("LIGHT_HEAVYWEIGHT", WeightClass.LIGHT_HEAVYWEIGHT.name());
        assertEquals("HEAVYWEIGHT", WeightClass.HEAVYWEIGHT.name());
        
        assertEquals(WeightClass.MINIMUM, WeightClass.valueOf("MINIMUM"));
        assertEquals(WeightClass.FLYWEIGHT, WeightClass.valueOf("FLYWEIGHT"));
        assertEquals(WeightClass.BANTAMWEIGHT, WeightClass.valueOf("BANTAMWEIGHT"));
        assertEquals(WeightClass.FEATHERWEIGHT, WeightClass.valueOf("FEATHERWEIGHT"));
        assertEquals(WeightClass.LIGHTWEIGHT, WeightClass.valueOf("LIGHTWEIGHT"));
        assertEquals(WeightClass.WELTERWEIGHT, WeightClass.valueOf("WELTERWEIGHT"));
        assertEquals(WeightClass.MIDDLEWEIGHT, WeightClass.valueOf("MIDDLEWEIGHT"));
        assertEquals(WeightClass.LIGHT_HEAVYWEIGHT, WeightClass.valueOf("LIGHT_HEAVYWEIGHT"));
        assertEquals(WeightClass.HEAVYWEIGHT, WeightClass.valueOf("HEAVYWEIGHT"));
    }
}
