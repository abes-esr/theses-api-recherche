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
                    elasticConfig.getHostname2(),
                    elasticConfig.getHostname3(),
                    elasticConfig.getHostname4(),
                    elasticConfig.getPort(),
                    elasticConfig.getScheme(),
                    elasticConfig.getUserName(),
                    elasticConfig.getPassword(),
                    elasticConfig.getProtocol());
        } catch (Exception e) {
            log.error("pb lors du chargement du client ES : " + e.toString());
            throw new RuntimeException(e);
        }
    }
}