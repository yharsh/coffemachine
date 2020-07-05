package io.yharsh.coffeemachine.component;

import io.yharsh.coffeemachine.domain.BeverageRecipe;
import io.yharsh.coffeemachine.domain.Ingredient;
import io.yharsh.coffeemachine.domain.RecipeIngredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BeverageMaker {
    private static final Logger log = LoggerFactory.getLogger(BeverageMaker.class);
    private AtomicInteger itemsBeingPrepared = new AtomicInteger(0);
    private int outlets;
    private RecipeStore recipeStore;
    private IngredientsStore ingredientsStore;

    public BeverageMaker(int outlets, RecipeStore recipeStore, IngredientsStore ingredientsStore) {
        this.outlets = outlets;
        this.recipeStore = recipeStore;
        this.ingredientsStore = ingredientsStore;
    }

    public boolean prepare(String itemName) {
        //check if current beverage recipe is present or not
        if (recipeStore.getRecipe(itemName) == null) {
            log.warn("Beverage {} is not supported", itemName);
            return false;
        }
        //Do an early reject
        if (itemsBeingPrepared.get() >= outlets) {
            log.info("All outlets are occupied try after sometime");
            return false;
        } else {
            try {
                itemsBeingPrepared.incrementAndGet();
                //do a quick check on the ingredients availability without holding a lock in order to fail early
                BeverageRecipe recipe = recipeStore.getRecipe(itemName);
                for (RecipeIngredient ingredient : recipe.getIngredients()) {
                    Ingredient storeIngredient = ingredientsStore.get(ingredient.getName());
                    if (storeIngredient == null || !storeIngredient.isAvailable(ingredient.getQuantity())) {
                        log.warn("Ingredient {} is not available", ingredient.getName());
                        return false;
                    }
                }
                List<RecipeIngredient> ingredients = new ArrayList<>(recipe.getIngredients().size());
                //Try to acquire all ingredients
                for (RecipeIngredient ingredient : recipe.getIngredients()) {
                    Ingredient storeIngredient = ingredientsStore.get(ingredient.getName());
                    if (storeIngredient != null && storeIngredient.consumeQuantity(ingredient.getQuantity())) {
                        ingredients.add(ingredient);
                    } else {
                        //unable to acquire all ingredients hence releasing unconsumed one
                        ingredients.forEach(ig -> ingredientsStore.get(ig.getName()).addQuantity(ig.getQuantity()));
                        log.warn("Ingredient {} is not available", ingredient.getName());
                        return false;
                    }
                }
                log.info("Prepared {}", itemName);
                return true;
            } finally {
                itemsBeingPrepared.decrementAndGet();
            }
        }
    }
}
