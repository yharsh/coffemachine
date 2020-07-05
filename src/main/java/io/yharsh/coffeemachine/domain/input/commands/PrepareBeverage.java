package io.yharsh.coffeemachine.domain.input.commands;

import io.yharsh.coffeemachine.domain.input.Operation;

import java.util.List;

public class PrepareBeverage extends Command {
    private List<String> names;

    public PrepareBeverage() {
        super(Operation.PREPARE_BEVERAGES);
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
