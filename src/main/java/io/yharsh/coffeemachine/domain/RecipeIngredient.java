package io.yharsh.coffeemachine.domain;

/**
 * Immutable class to hold the individual gradients of a beverage recipe
 */
public final class RecipeIngredient {
    private String name;
    private int quantity;

    public RecipeIngredient(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
