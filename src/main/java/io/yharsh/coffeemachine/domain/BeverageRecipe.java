package io.yharsh.coffeemachine.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable class to hold a beverage recipe
 */
public class BeverageRecipe {
    private String name;
    private List<RecipeIngredient> ingredients;

    public BeverageRecipe(String name, List<RecipeIngredient> ingredients) {
        this.ingredients = Collections.unmodifiableList(ingredients);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeverageRecipe recipe = (BeverageRecipe) o;
        return Objects.equals(name, recipe.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
