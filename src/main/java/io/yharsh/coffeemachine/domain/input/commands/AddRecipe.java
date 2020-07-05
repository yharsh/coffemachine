package io.yharsh.coffeemachine.domain.input.commands;

import io.yharsh.coffeemachine.domain.input.Operation;

import java.util.Map;

public class AddRecipe extends Command {
    private String name;
    private Map<String, Integer> ingredients;

    public AddRecipe() {
        super(Operation.ADD_RECIPE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Integer> ingredients) {
        this.ingredients = ingredients;
    }
}
