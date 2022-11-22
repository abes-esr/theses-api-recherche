package fr.abes.thesesapirecherche.personnes.builder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import fr.abes.thesesapirecherche.personnes.converters.PersonneMapper;
import fr.abes.thesesapirecherche.personnes.dto.PersonneLiteResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.PersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.model.Personne;
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
import java.util.List;

@Slf4j
@Component
public class SearchPersonneQueryBuilder {

    @Value("${es.hostname}")
    private String esHostname;
    @Value("${es.port}")
    private String esPort;

    @Value("${es.protocol}")
    private String esHttpProtocol;

    @Value("${es.username}")
    private String esUserName;

    @Value("${es.password}")
    private String esPassword;

    private ElasticsearchClient client;

    PersonneMapper personneMapper = new PersonneMapper();

    private ElasticsearchClient getElasticsearchClient() throws Exception {
        if (this.client == null) {
            try {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esUserName, esPassword));

                final SSLContext sslContext = SSLContexts.custom()
                        .loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
                        .build();

                RestClient client = RestClient.builder(new HttpHost(esHostname,  Integer.parseInt(esPort), esHttpProtocol)).setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider)
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                ).build();

                ElasticsearchTransport transport = new RestClientTransport(
                        client, new JacksonJsonpMapper());

                this.client = new ElasticsearchClient(transport);
            }
            catch (Exception e) {
                log.error(e.toString());
                throw e;
            }
        }
        return this.client;
    }

    /**
     * Rechercher dans ElasticSearch une personne avec son nom et prénom.
     *
     * @param chaine Chaîne de caractère à rechercher
     * @return Une liste de personnes au format Dto web
     * @throws Exception si une erreur est survenue
     */
    public List<PersonneLiteResponseDto> rechercher(String chaine) throws Exception {

        // Liste des champs utilisés pour la recherche
        FuzzyQuery nomQuery = QueryBuilders.fuzzy().field("nom").value(chaine).build();
        FuzzyQuery prenomQuery = QueryBuilders.fuzzy().field("prenom").value(chaine).build();

        // La valeur doit être trouvé dans l'un ou l'autres des champs
        BoolQuery boolQuery = new BoolQuery.Builder().should(nomQuery._toQuery(),prenomQuery._toQuery()).build();

        // Boost IdRef
        TermQuery termQuery = QueryBuilders.term().field("has_idref").value(true).build();
        FunctionScore functionScore = new FunctionScore.Builder().filter(termQuery._toQuery()).weight(360.0).build();
        FunctionScoreQuery functionScoreQuery = new FunctionScoreQuery.Builder()
                .query(boolQuery._toQuery())
                .functions(List.of(functionScore))
                .boostMode(FunctionBoostMode.Multiply)
                .scoreMode(FunctionScoreMode.Sum)
                .build();

        Query query = new Query.Builder().functionScore(functionScoreQuery).build();

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("personnes")
                .query(query)
                .build();

        SearchResponse<Personne> response = this.getElasticsearchClient().search(searchRequest,Personne.class);

        return personneMapper.personnesListToDto(response.hits().hits());
    }

    /**
     * Rechercher dans ElasticSearch une personne avec son identifiant.
     *
     * @param id Chaîne de caractère de l'identifiant de la personne
     * @return Une personne au format Dto web
     * @throws Exception si aucune personne n'a été trouvé ou si une autre erreur est survenue
     */
    public PersonneResponseDto rechercherParIdentifiant(String id) throws Exception {

        TermQuery termQuery = QueryBuilders.term().field("_id").value(id).build();
        Query query = new Query.Builder().term(termQuery).build();

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("personnes")
                .query(query)
                .build();

        SearchResponse<Personne> response = this.getElasticsearchClient().search(searchRequest,Personne.class);

        if (response.hits().hits().size() != 1) {
            throw new Exception("Person not found");
        }

        return personneMapper.personneToDto(response.hits().hits().get(0));
    }


}
