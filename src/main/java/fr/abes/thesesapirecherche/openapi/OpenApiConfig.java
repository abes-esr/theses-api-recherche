package fr.abes.thesesapirecherche.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        return new OpenAPI().servers(Arrays.asList(new Server().url("https://theses.fr")))
                .info(new Info().title("API recherche de theses.fr")
                        .description("Cette API permet de lancer une recherche dans les données de theses.fr décrivant les thèses et les personnes liées aux thèses. Les données sont récupérables au format JSON.")
                        .version("1.0"));
    }

}
