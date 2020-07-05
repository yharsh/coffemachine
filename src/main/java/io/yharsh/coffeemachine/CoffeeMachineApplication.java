package io.yharsh.coffeemachine;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
public class CoffeeMachineApplication implements ApplicationRunner {
    private MachineImpl machine;
    private InputParser inputParser;

    public CoffeeMachineApplication(MachineImpl machine, InputParser inputParser) {
        this.machine = machine;
        this.inputParser = inputParser;
    }

    public static void main(String[] args) {
        SpringApplication.run(CoffeeMachineApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        machine.execute(inputParser.getCommands(args));
        machine.shutdown();
    }
}
