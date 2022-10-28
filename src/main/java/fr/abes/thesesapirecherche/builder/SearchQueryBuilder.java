package fr.abes.thesesapirecherche.builder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.jackson.JacksonJsonpGenerator;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.core.JsonFactory;
import fr.abes.thesesapirecherche.model.These;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.StringWriter;

@Slf4j
@Component
public class SearchQueryBuilder {

    @Value("${es.hostname}")
    private String esHostname;
    @Value("${es.port}")
    private String esPort;
    @Value("${es.protocol}")
    private String esScheme;

    @Value("${es.username}")
    private String esUserName;

    @Value("${es.password}")
    private String esPassword;

    private ElasticsearchClient client;
    private ElasticsearchClient getElasticsearchClient() throws Exception {
        if (this.client == null) {
            try {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esUserName, esPassword));

                final SSLContext sslContext = SSLContexts.custom()
                        .loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
                        .build();

                RestClient client = RestClient.builder(new HttpHost(esHostname,  Integer.parseInt(esPort), esScheme)).setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider)
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                ).build();

                ElasticsearchTransport transport = new RestClientTransport(
                        client, new JacksonJsonpMapper());

                return new ElasticsearchClient(transport);
            }
            catch (Exception e) {
                log.error(e.toString());
                throw e;
            }
        }
        return this.client;
    }

    public String rechercheSurLeTitre (String chaine, int page, int nombre) throws Exception {

        SearchResponse<These> response = this.getElasticsearchClient().search(s -> s
                        .index("theses-sample")
                        .query(q->q
                                .match(t->t
                                        .query(chaine)
                                        .field("titre")))
                        .from(page)
                        .size(nombre),
                These.class
        );

        /* la réponse est récupérable :

        1) via un tableau d'objets java :

        List<Hit<These>> theses = response.hits().hits();
        for (Hit<These> hit : theses) {
            These these = hit.source();
            reponse += these.getTitre(); etc.
        }

        2) directement en json :
         */
        final StringWriter writer = new StringWriter();
        try (final JacksonJsonpGenerator generator = new JacksonJsonpGenerator(new JsonFactory().createGenerator(writer))) {
            response.serialize(generator, new JacksonJsonpMapper());
        }
        return writer.toString();
    }

    public String rechercheSurLeTitreEtResume (String chaine, String termeRameau) throws Exception {

        QueryStringQuery.Builder builderQuery = new QueryStringQuery.Builder();
        builderQuery.query(chaine);
        builderQuery.fields("titre^5","abstractFR");
        builderQuery.quoteFieldSuffix(".exact");
        Query query = builderQuery.build()._toQuery();

        TermQuery.Builder builderTerm = new TermQuery.Builder();
        builderTerm.field("rameau");
        builderTerm.value(termeRameau);
        TermQuery termQuery = builderTerm.build();

        SearchResponse<These> response = this.getElasticsearchClient().search(
                s -> s
                        .index("theses-sample")
                        .query(q->q
                                .bool(t-> t
                                        .must(query)
                                        .filter(a->a
                                                .term(termQuery))
                                )),
                These.class
        );
        final StringWriter writer = new StringWriter();
        try (final JacksonJsonpGenerator generator = new JacksonJsonpGenerator(new JsonFactory().createGenerator(writer))) {
            response.serialize(generator, new JacksonJsonpMapper());
        }
        return writer.toString();
    }
}
