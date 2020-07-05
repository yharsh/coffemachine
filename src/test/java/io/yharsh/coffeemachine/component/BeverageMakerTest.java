package io.yharsh.coffeemachine.component;

import io.yharsh.coffeemachine.domain.BeverageRecipe;
import io.yharsh.coffeemachine.domain.Ingredient;
import io.yharsh.coffeemachine.domain.RecipeIngredient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BeverageMakerTest {
    RecipeStore recipeStore = Mockito.mock(RecipeStore.class);
    IngredientsStore ingredientsStore = Mockito.mock(IngredientsStore.class);

    @Test
    public void whenRecipeIsNotPresentInTheRecipeStoreThenPrepareShouldReturnFalse() {

        BeverageRecipe recipe = null;
        when(recipeStore.getRecipe("unknown")).thenReturn(recipe);
        boolean prepared = new BeverageMaker(1, recipeStore, ingredientsStore).prepare("unknown");
        assertFalse(prepared);
    }

    @Test
    public void whenAllOutletsAreOccupiedThenPrepareShouldReturnFalse() {
        BeverageMaker beverageMaker = new BeverageMaker(0, recipeStore, ingredientsStore);
        boolean prepared = beverageMaker.prepare("anyItem");
        assertFalse(prepared);
    }

    @Test
    public void whenAllIngredientsForABeverageAreNotPresentThenPrepareShouldReturnFalse() {
        BeverageRecipe recipe = new BeverageRecipe("hot_tea",
                Arrays.asList(new RecipeIngredient("hot_water", 10), new RecipeIngredient("tea_leaves", 5)));
        when(recipeStore.getRecipe("hot_tea")).thenReturn(recipe);
        Ingredient hotWater = new Ingredient("hot_water", 0);
        Ingredient tea_leaves = new Ingredient("tea_leaves", 5);
        when(ingredientsStore.get("hot_water")).thenReturn(hotWater);
        when(ingredientsStore.get("tea_leaves")).thenReturn(tea_leaves);

        boolean prepared = new BeverageMaker(1, recipeStore, ingredientsStore).prepare("hot_tea");
        assertFalse(prepared);
        assertEquals(0, hotWater.getQuantity());
        assertEquals(5, tea_leaves.getQuantity());
    }

    @Test
    public void whenAllIngredientsForABeverageAreSeemsToPresentButLaterNotFoundThenPrepareShouldReturnFalse() {
        BeverageRecipe recipe = new BeverageRecipe("hot_tea",
                Arrays.asList(new RecipeIngredient("hot_water", 10), new RecipeIngredient("tea_leaves", 5)));
        when(recipeStore.getRecipe("hot_tea")).thenReturn(recipe);
        Ingredient hotWater = new Ingredient("hot_water", 10);
        Ingredient tea_leaves = new Ingredient("tea_leaves", 5);
        when(ingredientsStore.get("hot_water")).thenReturn(hotWater).thenAnswer((t) -> {
            hotWater.consumeQuantity(3);
            return hotWater;
        });
        when(ingredientsStore.get("tea_leaves")).thenReturn(tea_leaves);

        boolean prepared = new BeverageMaker(1, recipeStore, ingredientsStore).prepare("hot_tea");
        assertFalse(prepared);
        assertEquals(7, hotWater.getQuantity());
        assertEquals(5, tea_leaves.getQuantity());
    }

    @Test
    public void whenAllIngredientsArePresentAndOutletIsAvailableThenPrepareShouldReturnTrue() {
        BeverageRecipe recipe = new BeverageRecipe("hot_tea",
                Arrays.asList(new RecipeIngredient("hot_water", 10), new RecipeIngredient("tea_leaves", 5)));
        when(recipeStore.getRecipe("hot_tea")).thenReturn(recipe);
        Ingredient hotWater = new Ingredient("hot_water", 11);
        Ingredient tea_leaves = new Ingredient("tea_leaves", 14);
        when(ingredientsStore.get("hot_water")).thenReturn(hotWater);
        when(ingredientsStore.get("tea_leaves")).thenReturn(tea_leaves);

        boolean prepared = new BeverageMaker(1, recipeStore, ingredientsStore).prepare("hot_tea");
        assertTrue(prepared);
        assertEquals(1, hotWater.getQuantity());
        assertEquals(9, tea_leaves.getQuantity());
    }
}
