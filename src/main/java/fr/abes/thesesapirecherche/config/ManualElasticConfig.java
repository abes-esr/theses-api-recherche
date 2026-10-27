package fr.abes.thesesapirecherche.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ManualElasticConfig {

    private final ElasticConfig elasticConfig;

    @Bean
    public ElasticsearchClient elasticsearchClient() throws Exception {

        ElasticClient.chargeClient(
                elasticConfig.getHostname(),
                elasticConfig.getPort(),
                elasticConfig.getProtocol(),
                elasticConfig.getUserName(),
                elasticConfig.getPassword());

        return new ElasticsearchClient(ElasticClient.getElasticsearchClient()._transport());
    }
}
