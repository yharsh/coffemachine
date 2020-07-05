package io.yharsh.coffeemachine;

import io.yharsh.coffeemachine.domain.input.commands.Command;

import java.util.List;

public interface IMachine {
    /**
     * Should execute commands with appropriate operations
     * @param commands to execute
     */
    void execute(List<Command> commands);

    /**
     * To do a proper shutdown of machine
     */
    void shutdown();
}
