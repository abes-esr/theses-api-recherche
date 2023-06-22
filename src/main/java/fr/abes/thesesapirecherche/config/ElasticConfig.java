package fr.abes.thesesapirecherche.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("es")
@Getter
@Setter
public class ElasticConfig {

    private String hostname;
    private int port;
    private String scheme;
    private String userName;
    private String password;
    private String protocol;

}
