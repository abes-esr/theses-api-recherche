package fr.abes.thesesapirecherche.theses.builder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.SuggestFuzziness;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import fr.abes.thesesapirecherche.commons.builder.FacetQueryBuilder;
import fr.abes.thesesapirecherche.config.FacetProps;
import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.theses.converters.TheseLiteMapper;
import fr.abes.thesesapirecherche.theses.converters.TheseMapper;
import fr.abes.thesesapirecherche.theses.dto.ResponseTheseLiteDto;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static fr.abes.thesesapirecherche.commons.builder.FacetQueryBuilder.addFilters;
import static fr.abes.thesesapirecherche.commons.builder.FacetQueryBuilder.buildFilter;

@Slf4j
@Component
@EnableConfigurationProperties(value = FacetProps.class)
public class SearchQueryBuilder {

    private final TheseMapper theseMapper = new TheseMapper();
    private final TheseLiteMapper theseLiteMapper = new TheseLiteMapper();
    @Value("${es.hostname}")
    private String esHostname;
    @Value("${es.hostname2}")
    private String esHostname2;
    @Value("${es.hostname3}")
    private String esHostname3;
    @Value("${es.hostname4}")
    private String esHostname4;
    @Value("${es.port}")
    private String esPort;
    @Value("${es.protocol}")
    private String esScheme;
    @Value("${es.username}")
    private String esUserName;
    @Value("${es.password}")
    private String esPassword;
    private ElasticsearchClient client;
    @Value("${es.theses.indexname}")
    private String esIndexName;

    @Value("${maxfacetsvalues}")
    private int maxFacetsValues;

    @Autowired
    private FacetProps facetProps;

    private ElasticsearchClient getElasticsearchClient() throws Exception {
        if (this.client == null) {
            try {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esUserName, esPassword));

                final SSLContext sslContext = SSLContexts.custom()
                        .loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
                        .build();

                RestClient client = RestClient.builder(new HttpHost(esHostname, Integer.parseInt(esPort), esScheme), new HttpHost(esHostname2, Integer.parseInt(esPort), esScheme), new HttpHost(esHostname3, Integer.parseInt(esPort), esScheme), new HttpHost(esHostname4, Integer.parseInt(esPort), esScheme)).setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider)
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                ).build();

                ElasticsearchTransport transport = new RestClientTransport(
                        client, new JacksonJsonpMapper());

                return new ElasticsearchClient(transport);
            } catch (Exception e) {
                log.error(e.toString());
                throw e;
            }
        }
        return this.client;
    }

    private Query buildQuery(String chaine) {
        QueryStringQuery.Builder builderQuery = new QueryStringQuery.Builder();
        builderQuery.query(chaine);
        builderQuery.defaultOperator(Operator.And);
        builderQuery.fields("resumes.*^30", "titres.*^30", "nnt^15", "discipline^15", "sujetsRameauPpn^15", "sujetsRameauLibelle^15", "sujets^15", "auteursNP^12", "directeursNP^2", "ecolesDoctoralesN^5", "etabSoutenanceN^5", "oaiSets^5", "etabsCotutelleN^1", "membresJuryNP^1", "partenairesRechercheN^1", "presidentJuryNP^1", "rapporteurs^1");
        builderQuery.quoteFieldSuffix(".exact");

        return builderQuery.build()._toQuery();
    }

    public ResponseTheseLiteDto simple(String chaine, Integer debut, Integer nombre, String tri, String filtres) throws Exception {
        SearchResponse<These> response = this.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .bool(t -> t
                                        .must(buildQuery(chaine))
                                        .filter(addFilters(filtres, facetProps.getMainTheses(), facetProps.getSubsTheses()))
                                ))
                        .from(debut)
                        .size(nombre)
                        .sort(addTri(tri))
                        .trackTotalHits(t -> t.enabled(Boolean.TRUE)),
                These.class
        );
        
        ResponseTheseLiteDto res = new ResponseTheseLiteDto();
        List<TheseLiteResponseDto> liste = new ArrayList<>();
        Iterator<Hit<These>> iterator = response.hits().hits().iterator();
        while (iterator.hasNext()) {
            Hit<These> theseHit = iterator.next();
            liste.add(theseLiteMapper.theseLiteToDto(theseHit));
        }
        res.setTheses(liste);
        res.setTotalHits(response.hits().total().value());
        return res;
    }

    public long getStatsTheses(String statusFilter) throws Exception {
        List<String> list = List.of(statusFilter);
        CountResponse countResponse = this.getElasticsearchClient().count(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .bool(t -> t
                                        .filter(buildFilter("status", list))
                                ))
        );

        return countResponse.count();
    }

    public List<Facet> facets(String chaine, String filtres) throws Exception {
        return FacetQueryBuilder.facets(this.getElasticsearchClient(), buildQuery(chaine), esIndexName, facetProps.getMainTheses(), facetProps.getSubsTheses(), maxFacetsValues, filtres);
    }

    public TheseResponseDto rechercheSurId(String nnt) throws Exception {
        SearchResponse<These> response = this.getElasticsearchClient().search(s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .match(t -> t
                                        .query(nnt)
                                        .field("_id"))),
                These.class
        );

        Optional<These> a = response.hits().hits().stream().map(Hit::source).findFirst();

        return a.map(theseMapper::theseToDto).orElse(null);
    }

    public List<String> completion(String q) throws Exception {
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

        List<String> listeSuggestions = new ArrayList<String>();

        response.suggest().entrySet().forEach(a -> a.getValue().forEach(s -> s.completion().options().forEach(b -> listeSuggestions.add(b.text()))));

        return listeSuggestions;
    }


    //TODO : Sortir les fields dans un fichier properties
    private List<SortOptions> addTri(String tri) {
        List<SortOptions> list = new ArrayList<>();
        if (!tri.equals("")) {
            SortOptions sort = switch (tri) {
                case "dateAsc" ->
                        new SortOptions.Builder().field(f -> f.field("dateFiltre").order(SortOrder.Asc)).build();
                case "dateDesc" ->
                        new SortOptions.Builder().field(f -> f.field("dateFiltre").order(SortOrder.Desc)).build();
                case "auteursAsc" ->
                        new SortOptions.Builder().field(f -> f.field("auteursNP.exact").order(SortOrder.Asc)).build();
                case "auteursDesc" ->
                        new SortOptions.Builder().field(f -> f.field("auteursNP.exact").order(SortOrder.Desc)).build();
                case "disciplineAsc" ->
                        new SortOptions.Builder().field(f -> f.field("discipline.exact").order(SortOrder.Asc)).build();
                case "disciplineDesc" ->
                        new SortOptions.Builder().field(f -> f.field("discipline.exact").order(SortOrder.Desc)).build();
                default -> null;
            };
            if (sort != null)
                list.add(sort);
        }
        return list;
    }
}
