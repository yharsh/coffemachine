package io.yharsh.coffeemachine;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yharsh.coffeemachine.component.BeverageMaker;
import io.yharsh.coffeemachine.component.IngredientsStore;
import io.yharsh.coffeemachine.component.RecipeStore;
import io.yharsh.coffeemachine.domain.exception.InitializationException;
import io.yharsh.coffeemachine.domain.input.InitializationInput;
import io.yharsh.coffeemachine.domain.input.commands.AddIngredients;
import io.yharsh.coffeemachine.domain.input.commands.AddRecipe;
import io.yharsh.coffeemachine.domain.input.commands.PrepareBeverage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MachineImplTest {
    @Test
    public void addIngredientShouldAddIngredientToStore() throws IOException, InitializationException, InterruptedException {
        IngredientsStore ingredientsStore = Mockito.spy(new IngredientsStore());
        RecipeStore recipeStore = Mockito.spy(new RecipeStore());
        BeverageMaker beverageMaker = Mockito.mock(BeverageMaker.class);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        File initFile = new File(ClassLoader.getSystemClassLoader().getResource("init.json").getFile());
        InitializationInput initInput = new ObjectMapper().readValue(initFile, InitializationInput.class);
        IMachine machine = new MachineImpl(beverageMaker, ingredientsStore, recipeStore, initInput, executor);
        int previousValue = ingredientsStore.get("hot_water").getQuantity();
        AddIngredients command = new AddIngredients();
        command.setTotal_items_quantity(Collections.singletonMap("hot_water", 5));
        machine.execute(Collections.singletonList(command));
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        executor.shutdownNow();
        Assertions.assertEquals(previousValue + 5, ingredientsStore.get("hot_water").getQuantity());
    }

    @Test
    public void addRecipeShouldAddRecipeToStore() throws IOException, InitializationException, InterruptedException {
        IngredientsStore ingredientsStore = Mockito.spy(new IngredientsStore());
        RecipeStore recipeStore = Mockito.spy(new RecipeStore());
        BeverageMaker beverageMaker = Mockito.mock(BeverageMaker.class);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        File initFile = new File(ClassLoader.getSystemClassLoader().getResource("init.json").getFile());
        InitializationInput initInput = new ObjectMapper().readValue(initFile, InitializationInput.class);
        IMachine machine = new MachineImpl(beverageMaker, ingredientsStore, recipeStore, initInput, executor);
        AddRecipe command = new AddRecipe();
        command.setIngredients(Collections.singletonMap("hot_water", 5));
        command.setName("harsh_special_chai");
        machine.execute(Collections.singletonList(command));
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        executor.shutdownNow();
        Assertions.assertNotNull(recipeStore.getRecipe("harsh_special_chai"));
    }

    @Test
    public void prepareBeverageShouldPrepareBeverageAndConsumeSomeIngredient() throws IOException, InitializationException, InterruptedException {
        IngredientsStore ingredientsStore = Mockito.spy(new IngredientsStore());
        RecipeStore recipeStore = Mockito.spy(new RecipeStore());
        BeverageMaker beverageMaker = Mockito.spy(new BeverageMaker(3, recipeStore, ingredientsStore));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        File initFile = new File(ClassLoader.getSystemClassLoader().getResource("init.json").getFile());
        InitializationInput initInput = new ObjectMapper().readValue(initFile, InitializationInput.class);
        IMachine machine = new MachineImpl(beverageMaker, ingredientsStore, recipeStore, initInput, executor);
        AddRecipe addRecipeCommand = new AddRecipe();
        addRecipeCommand.setIngredients(Collections.singletonMap("ether", 5));
        addRecipeCommand.setName("harsh_special_chai");
        AddIngredients addIngredientsCommand = new AddIngredients();
        addIngredientsCommand.setTotal_items_quantity(Collections.singletonMap("ether", 5));
        PrepareBeverage prepareBeverageCommand = new PrepareBeverage();
        prepareBeverageCommand.setNames(Collections.singletonList("harsh_special_chai"));
        machine.execute(Arrays.asList(addRecipeCommand, addIngredientsCommand, prepareBeverageCommand));
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        executor.shutdownNow();
        Assertions.assertEquals(0, ingredientsStore.get("ether").getQuantity());
    }
}
