package fr.abes.thesesapirecherche.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitClientES {

    private final ElasticConfig elasticConfig;

    public InitClientES(ElasticConfig elasticConfig) {
        this.elasticConfig = elasticConfig;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {

        log.info("Load elastic client");
        try {
            ElasticClient.chargeClient(
                    elasticConfig.getHostname(),
                    elasticConfig.getPort(),
                    elasticConfig.getProtocol(),
                    elasticConfig.getUserName(),
                    elasticConfig.getPassword());
        } catch (Exception e) {
            log.error("pb lors du chargement du client ES : " + e.toString());
            throw new RuntimeException(e);
        }
    }
}