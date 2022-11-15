package springservice.configuration;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger2Config {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("gpncup")
                .packagesToScan("springservice.controllers")
                .build();
    }
}