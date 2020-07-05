package io.yharsh.coffeemachine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yharsh.coffeemachine.InputParser;
import io.yharsh.coffeemachine.component.BeverageMaker;
import io.yharsh.coffeemachine.component.IngredientsStore;
import io.yharsh.coffeemachine.component.RecipeStore;
import io.yharsh.coffeemachine.domain.input.InitializationInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {
    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public InitializationInput initializationInput(ApplicationArguments applicationArguments, InputParser inputParser) throws IOException {
        return inputParser.getInitializationInput(applicationArguments);
    }

    @Bean
    public BeverageMaker beverageMaker(InitializationInput initializationInput, IngredientsStore ingredientsStore,
                                       RecipeStore recipeStore) {
        return new BeverageMaker(initializationInput.getMachine().getOutlets().getCount_n(), recipeStore, ingredientsStore);
    }

    @Bean
    public ExecutorService executor(InitializationInput initializationInput) {
        //One is added to simulate that parallel outlet if all occupied then current request should be rejected
        return Executors.newFixedThreadPool(initializationInput.getMachine().getOutlets().getCount_n() + 1);
    }
}
