package io.yharsh.coffeemachine.domain.input.commands;

import io.yharsh.coffeemachine.domain.input.Operation;

import java.util.Map;

public class AddIngredients extends Command {
    private Map<String, Integer> total_items_quantity;

    public AddIngredients() {
        super(Operation.ADD_INGREDIENTS);
    }

    public Map<String, Integer> getTotal_items_quantity() {
        return total_items_quantity;
    }

    public void setTotal_items_quantity(Map<String, Integer> total_items_quantity) {
        this.total_items_quantity = total_items_quantity;
    }
}
