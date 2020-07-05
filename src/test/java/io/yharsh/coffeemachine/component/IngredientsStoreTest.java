package io.yharsh.coffeemachine.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IngredientsStoreTest {
    @Test
    public void whenAnIngredientIsFirsTimeAddedToStoreAddShouldReturnCorrectQuantity() {
        IngredientsStore store = new IngredientsStore();
        store.add("water", 10);
        Assertions.assertEquals(10, store.get("water").getQuantity());
    }

    @Test
    public void whenAlreadyPresentIngredientIsAddedToStoreAddShouldReturnCorrectQuantity() {
        IngredientsStore store = new IngredientsStore();
        store.add("water", 10);
        store.add("water", 11);
        Assertions.assertEquals(21, store.get("water").getQuantity());
    }
}
