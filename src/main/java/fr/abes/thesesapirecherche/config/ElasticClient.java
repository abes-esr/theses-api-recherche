package fr.abes.thesesapirecherche.config;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;

import javax.net.ssl.SSLContext;

@Slf4j
public class ElasticClient {

    private static ElasticsearchClient client;

    private ElasticClient() {
    }

    public static ElasticsearchClient getElasticsearchClient() {
        return client;
    }

    public static void chargeClient(String hostname,
                                    String port,
                                    String protocol,
                                    String userName,
                                    String password) throws Exception {
        try {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));

            final SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
                    .build();

            RestClient cc = RestClient.builder(
                            new HttpHost(hostname, Integer.parseInt(port), protocol)
                    )
                    .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                            .setDefaultCredentialsProvider(credentialsProvider)
                            .setSSLContext(sslContext)
                            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    ).build();

            ElasticsearchTransport transport = new RestClientTransport(
                    cc, new JacksonJsonpMapper());

            client = new ElasticsearchClient(transport);

        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }
}

