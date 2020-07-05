package io.yharsh.coffeemachine;

import io.yharsh.coffeemachine.component.BeverageMaker;
import io.yharsh.coffeemachine.component.IngredientsStore;
import io.yharsh.coffeemachine.component.RecipeStore;
import io.yharsh.coffeemachine.domain.RecipeIngredient;
import io.yharsh.coffeemachine.domain.exception.InitializationException;
import io.yharsh.coffeemachine.domain.input.InitializationInput;
import io.yharsh.coffeemachine.domain.input.commands.AddIngredients;
import io.yharsh.coffeemachine.domain.input.commands.AddRecipe;
import io.yharsh.coffeemachine.domain.input.commands.Command;
import io.yharsh.coffeemachine.domain.input.commands.PrepareBeverage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class MachineImpl implements IMachine {
    private static final Logger log = LoggerFactory.getLogger(MachineImpl.class);
    private ExecutorService executor;
    private BeverageMaker beverageMaker;
    private IngredientsStore ingredientsStore;
    private RecipeStore recipeStore;
    private boolean initialized;

    public MachineImpl(BeverageMaker beverageMaker, IngredientsStore ingredientsStore, RecipeStore recipeStore,
                       InitializationInput initializationInput, ExecutorService executor) throws InitializationException {
        this.beverageMaker = beverageMaker;
        this.ingredientsStore = ingredientsStore;
        this.recipeStore = recipeStore;
        this.executor = executor;
        initialize(initializationInput);
    }

    private void initialize(InitializationInput initializationInput) throws InitializationException {
        if (initialized) {
            throw new InitializationException("MachineImpl is already initialized");
        }
        initializationInput.getMachine().getTotal_items_quantity().forEach((name, quantity) -> ingredientsStore.add(name, quantity));
        initializationInput.getMachine().getBeverages().forEach((name, ingToQntMap) ->
                recipeStore.addRecipe(name, ingToQntMap.keySet()
                        .stream()
                        .map(ing -> new RecipeIngredient(ing, ingToQntMap.get(ing)))
                        .collect(Collectors.toList()))
        );
        log.info("Machine is initialized");
        initialized = true;
    }

    @Override
    public void execute(List<Command> commands) {
        commands.forEach(command -> {
            switch (command.getOperation()) {
                case ADD_INGREDIENTS:
                    AddIngredients addIngredients = (AddIngredients) command;
                    addIngredients.getTotal_items_quantity().forEach((name, quant) -> ingredientsStore.add(name, quant));
                    break;
                case ADD_RECIPE:
                    AddRecipe recipe = (AddRecipe) command;
                    recipeStore.addRecipe(recipe.getName(), recipe.getIngredients().keySet()
                            .stream()
                            .map(name -> new RecipeIngredient(name, recipe.getIngredients().get(name))).collect(Collectors.toList()));
                    break;
                case PREPARE_BEVERAGES:
                    PrepareBeverage prepareBeverage = (PrepareBeverage) command;
                    prepareBeverage.getNames().forEach(name -> executor.submit(() -> beverageMaker.prepare(name)));
                    break;
            }
        });
    }

    @Override
    public void shutdown() {
        try {
            log.info("Shutting down");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Task interrupted", e);
        } finally {
            if (!executor.isTerminated()) {
                log.error("Cancel all unfinished tasks");
            }
            executor.shutdownNow();
            log.info("Shutdown finished");
        }
    }
}
