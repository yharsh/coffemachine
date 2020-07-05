package io.yharsh.coffeemachine.component;

import io.yharsh.coffeemachine.domain.Ingredient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class IngredientsStore {
    private Map<String, Ingredient> nameToIngredient = new HashMap<>();

    public void add(String name, int quantity) {
        Ingredient currentIngredient = nameToIngredient.get(name);
        if (currentIngredient != null) {
            currentIngredient.addQuantity(quantity);
        } else {
            currentIngredient = new Ingredient(name, quantity);
            nameToIngredient.put(name, currentIngredient);
        }
    }

    public Ingredient get(String name) {
        return nameToIngredient.get(name);
    }
}
