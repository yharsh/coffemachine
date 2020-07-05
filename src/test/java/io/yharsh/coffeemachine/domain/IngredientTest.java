package io.yharsh.coffeemachine.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class IngredientTest {
    @Test
    public void addIngredientShouldUpdateTheCorrectValue() {
        Ingredient ingredient = new Ingredient("abc", 10);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        IntStream.range(1, 1001).forEach(q ->
                executorService.submit(() ->
                        ingredient.addQuantity(q)
                ));
        try {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        } finally {
            executorService.shutdownNow();
        }
        Assertions.assertEquals(500510, ingredient.getQuantity());
    }

    @Test
    public void consumeIngredientShouldUpdateTheCorrectValue() {
        Ingredient ingredient = new Ingredient("abc", 500510);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        IntStream.range(1, 1001).forEach(q ->
                executorService.submit(() ->
                        ingredient.consumeQuantity(q)
                ));
        try {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        } finally {
            executorService.shutdownNow();
        }
        Assertions.assertEquals(10, ingredient.getQuantity());
    }
}
