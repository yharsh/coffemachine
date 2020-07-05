package io.yharsh.coffeemachine.component;

import io.yharsh.coffeemachine.domain.BeverageRecipe;
import io.yharsh.coffeemachine.domain.RecipeIngredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RecipeStore {
    private static final Logger log = LoggerFactory.getLogger(RecipeStore.class);
    private Map<String, BeverageRecipe> nameToRecipeMap = new ConcurrentHashMap<>();

    public void addRecipe(String name, List<RecipeIngredient> ingredients) {
        log.info("Adding/updating recipe {}", name);
        nameToRecipeMap.put(name, new BeverageRecipe(name, ingredients));
    }

    public BeverageRecipe getRecipe(String name) {
        return nameToRecipeMap.get(name);
    }

}
