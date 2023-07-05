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
    private String hostname2;
    private String hostname3;
    private String hostname4;
    private String port;
    private String protocol;
    private String userName;
    private String password;

}
