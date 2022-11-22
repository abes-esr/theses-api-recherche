package fr.abes.thesesapirecherche.builder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.*;
import co.elastic.clients.json.jackson.JacksonJsonpGenerator;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.core.JsonFactory;
import fr.abes.thesesapirecherche.converters.TheseMapper;
import fr.abes.thesesapirecherche.dto.TheseResponseDto;
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
import java.util.*;

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

    private String esIndexName = "theses-sample-2";

    private final TheseMapper theseMapper = new TheseMapper();
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

    public String rechercheSurLeTitre (String chaine, Integer page, Integer nombre) throws Exception {

        QueryStringQuery.Builder builderQuery = new QueryStringQuery.Builder();
        builderQuery.query(chaine);
        builderQuery.defaultOperator(Operator.And);
        builderQuery.fields("titres.*");
        builderQuery.quoteFieldSuffix(".exact");
        Query query = builderQuery.build()._toQuery();

        SearchResponse<These> response = this.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q->q
                                .bool(t-> t
                                        .must(query)
                                ))
                        .from(page)
                        .size(nombre),
                These.class
        );

        final StringWriter writer = new StringWriter();
        try (final JacksonJsonpGenerator generator = new JacksonJsonpGenerator(new JsonFactory().createGenerator(writer))) {
            response.serialize(generator, new JacksonJsonpMapper());
        }
        return writer.toString();
    }

    public TheseResponseDto rechercheSurId(String nnt) throws Exception {
        SearchResponse<These> response = this.getElasticsearchClient().search(s -> s
                        .index(esIndexName)
                        .query(q->q
                                .match(t->t
                                        .query(nnt)
                                        .field("nnt"))),
                These.class
        );

        Optional<These> a = response.hits().hits().stream().map(Hit::source).findFirst();

        return a.map(theseMapper::theseToDto).orElse(null);
    }

    public List<String> completion (String q) throws Exception {

        FieldSuggester fieldSuggester = FieldSuggester.of(fs -> fs
                .completion(cs -> cs.skipDuplicates(true)
                                .size(10)
                                .fuzzy(SuggestFuzziness.of(sf -> sf.fuzziness("0").transpositions(true).minLength(2).prefixLength(3)))
                                .field("suggestion")));

        Suggester suggester = Suggester.of(s -> s
                .suggesters("suggestion", fieldSuggester)
                .text(q)
        );

        SearchResponse<Void> response = this.getElasticsearchClient().search(s -> s
                .index(esIndexName)
                .suggest(suggester)
                , Void.class);

        List<String> listeSuggestions  = new ArrayList<String>();

        response.suggest().entrySet().forEach(a->a.getValue().forEach(s->s.completion().options().forEach(b->listeSuggestions.add(b.text()))));


        return listeSuggestions;
    }
}
