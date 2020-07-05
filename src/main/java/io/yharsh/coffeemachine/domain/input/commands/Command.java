package io.yharsh.coffeemachine.domain.input.commands;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.yharsh.coffeemachine.domain.input.Operation;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "operation")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddIngredients.class, name = "ADD_INGREDIENTS"),
        @JsonSubTypes.Type(value = AddRecipe.class, name = "ADD_RECIPE"),
        @JsonSubTypes.Type(value = PrepareBeverage.class, name = "PREPARE_BEVERAGES")
})
public abstract class Command {
    private Operation operation;

    public Command(Operation operation) {
        this.operation = operation;
    }

    public Operation getOperation() {
        return operation;
    }
}
