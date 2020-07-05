package io.yharsh.coffeemachine.component;

import io.yharsh.coffeemachine.domain.BeverageRecipe;
import io.yharsh.coffeemachine.domain.RecipeIngredient;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecipeStoreTest {
    @Test
    public void testAddANewRecipe() {
        RecipeStore recipeStore = new RecipeStore();
        List<RecipeIngredient> recipeIngredients = Collections.singletonList(new RecipeIngredient("hot_water", 10));
        recipeStore.addRecipe("hot_coffee", recipeIngredients);
        BeverageRecipe recipe = recipeStore.getRecipe("hot_coffee");
        assertEquals(recipe.getName(), "hot_coffee");
        assertEquals(recipe.getIngredients(), recipeIngredients);
    }
}
