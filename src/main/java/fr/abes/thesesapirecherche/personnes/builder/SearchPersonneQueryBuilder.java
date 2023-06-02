package fr.abes.thesesapirecherche.personnes.builder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.*;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import fr.abes.thesesapirecherche.commons.builder.FacetQueryBuilder;
import fr.abes.thesesapirecherche.config.FacetProps;
import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.personnes.converters.PersonneMapper;
import fr.abes.thesesapirecherche.personnes.dto.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;

import static fr.abes.thesesapirecherche.commons.builder.FacetQueryBuilder.addFilters;

@Slf4j
@Component
public class SearchPersonneQueryBuilder {

    PersonneMapper personneMapper = new PersonneMapper();
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
    @Autowired
    private FacetProps facetProps;

    @Value("${maxfacetsvalues}")
    private int maxFacetsValues;

    private ElasticsearchClient getElasticsearchClient() throws Exception {
        if (this.client == null) {
            try {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esUserName, esPassword));

                final SSLContext sslContext = SSLContexts.custom()
                        .loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
                        .build();

                RestClient client = RestClient.builder(new HttpHost(esHostname, Integer.parseInt(esPort), esHttpProtocol)).setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider)
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                ).build();

                ElasticsearchTransport transport = new RestClientTransport(
                        client, new JacksonJsonpMapper());

                this.client = new ElasticsearchClient(transport);
            } catch (Exception e) {
                log.error(e.toString());
                throw e;
            }
        }
        return this.client;
    }

    public Query buildQuery(String chaine) {

        // Recherche par nom et prénom
        QueryStringQuery.Builder nomPrenomBuilderQuery = new QueryStringQuery.Builder();
        nomPrenomBuilderQuery.query(chaine);
        nomPrenomBuilderQuery.defaultOperator(Operator.And);
        nomPrenomBuilderQuery.fields(List.of("nom", "prenom", "nom_complet", "nom_complet.exact"));
        nomPrenomBuilderQuery.quoteFieldSuffix(".exact");
        Query nomPrenomQueryString = nomPrenomBuilderQuery.build()._toQuery();

        // Recherche par thématique
        QueryStringQuery.Builder thematiqueBuilderQuery = new QueryStringQuery.Builder();
        thematiqueBuilderQuery.query(chaine);
        thematiqueBuilderQuery.defaultOperator(Operator.And);
        thematiqueBuilderQuery.fields(List.of("theses.sujets.*", "theses.sujets_rameau", "theses.resumes.*", "theses.discipline"));
        thematiqueBuilderQuery.quoteFieldSuffix(".exact");
        Query nestedThematiqueQuery = new NestedQuery.Builder().query(thematiqueBuilderQuery.build()._toQuery()).path("theses").build()._toQuery();

        // Recherche par nom et prénom ou par thématique
        Query thematiqueQueryString = QueryBuilders.bool().should(nomPrenomQueryString, nestedThematiqueQuery).build()._toQuery();

        // Boost IdRef
        TermQuery idrefQuery = QueryBuilders.term().field("has_idref").value(true).build();
        FunctionScore functionScoreIdref = new FunctionScore.Builder().filter(idrefQuery._toQuery()).weight(150.0).build();

        // Boost Role Directeur de thèse
        TermQuery roleDirecteurQuery = QueryBuilders.term().field("roles").value("directeur de thèse").build();
        FunctionScore functionScoreRoleDirecteur = new FunctionScore.Builder().filter(roleDirecteurQuery._toQuery()).weight(100.0).build();

        // Boost Role Rapporteur
        TermQuery roleRapporteurQuery = QueryBuilders.term().field("roles").value("rapporteur").build();
        FunctionScore functionScoreRoleRapporteur = new FunctionScore.Builder().filter(roleRapporteurQuery._toQuery()).weight(100.0).build();

        FunctionScoreQuery functionScoreQuery = new FunctionScoreQuery.Builder()
                .query(thematiqueQueryString)
                .functions(List.of(functionScoreIdref, functionScoreRoleDirecteur, functionScoreRoleRapporteur))
                .boostMode(FunctionBoostMode.Multiply)
                .scoreMode(FunctionScoreMode.Sum)
                .build();

        return new Query.Builder().functionScore(functionScoreQuery).build();
    }

    /**
     * Fonction pour trier les résultats.
     *
     * @return Liste d'options de tri Elastic Search
     */
    private List<SortOptions> buildSort(String tri ) {
        List<SortOptions> list = new ArrayList<>();

        if (tri.equals("PersonnesAsc")) {
            list.add(new SortOptions.Builder().field(f -> f.field("nom.sort").order(SortOrder.Asc)).build());
            list.add(new SortOptions.Builder().field(f -> f.field("prenom.sort").order(SortOrder.Asc)).build());
            list.add(new SortOptions.Builder().field(f -> f.field("_score").order(SortOrder.Desc)).build());

        } else if (tri.equals("PersonnesDesc")) {
            list.add(new SortOptions.Builder().field(f -> f.field("nom.sort").order(SortOrder.Desc)).build());
            list.add(new SortOptions.Builder().field(f -> f.field("prenom.sort").order(SortOrder.Desc)).build());
            list.add(new SortOptions.Builder().field(f -> f.field("_score").order(SortOrder.Desc)).build());
        } else {
            // Pertinence
            list.add(new SortOptions.Builder().field(f -> f.field("_score").order(SortOrder.Desc)).build());
            list.add(new SortOptions.Builder().field(f -> f.field("nom.sort").order(SortOrder.Asc)).build());
            list.add(new SortOptions.Builder().field(f -> f.field("prenom.sort").order(SortOrder.Asc)).build());
        }

        return list;
    }

    /**
     * Rechercher dans ElasticSearch une personne avec son nom et prénom.
     *
     * @param chaine  Chaîne de caractère à rechercher
     * @param index   Nom de l'index ES à requêter
     * @param filtres Filtres à appliquer à la requête ES
     * @param debut   Numéro de la page courante
     * @param nombre  Nombre de résultats à retourner
     * @param tri     Tri à appliquer sur la requête ES
     * @return Un objet réponse de la recherche au format Dto web
     * @throws Exception si une erreur est survenue
     */
    public RechercheResponseDto rechercher(String chaine, String index, String filtres, Integer debut, Integer nombre, String tri) throws Exception {

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(index)
                .source(SourceConfig.of(s -> s.filter(f -> f.includes(List.of("nom", "prenom", "has_idref", "theses")))))
                .query(q -> q
                        .bool(t -> t
                                .must(buildQuery(chaine))
                                .filter(addFilters(filtres, facetProps.getMainPersonnes(), facetProps.getSubsPersonnes()))
                        ))
                .from(debut)
                .size(nombre)
                .sort(buildSort(tri))
                .trackTotalHits(t -> t.enabled(Boolean.TRUE))
                .build();

        SearchResponse<Personne> response = this.getElasticsearchClient().search(searchRequest, Personne.class);

        return RechercheResponseDto.builder().personnes(personneMapper.personnesListToDto(response.hits().hits())).totalHits(response.hits().total().value()).build();
    }

    public SuggestionResponseDto completion(String q, String index) throws Exception {
        return SuggestionResponseDto.builder().personnes(completionPersonne(q, index)).thematiques(completionThematique(q, index)).build();
    }

    /**
     * Retourne 10 suggestion de personnes à partir de son nom ou prénom
     * Les personnes avec un identifiant Idref sont priorisées
     * Les personnes avec un rôle de directeur de thèse ou de rapporteur sont priorisées
     *
     * @param q     Chaîne de caractère à compléter
     * @param index Nom de l'index ES à requêter
     * @return Une liste de suggestions de personnes au format Dto web
     * @throws Exception
     */
    public List<SuggestionPersonneResponseDto> completionPersonne(String q, String index) throws Exception {

        // Définition du contexte pour booster les personnes avec Idref
        Context catAvecIdref = new Context.Builder().category("true").build();
        CompletionContext contextAvecIdref = new CompletionContext.Builder().context(catAvecIdref).boost(150.0).build();

        // Définition du contexte pour booster les personnes avec le rôle
        Context catDirecteur = new Context.Builder().category("directeur de thèse").build();
        Context catRapporteur = new Context.Builder().category("rapporteur").build();
        CompletionContext contextDirecteur = new CompletionContext.Builder().context(catDirecteur).boost(100.0).build();
        CompletionContext contextRapporteur = new CompletionContext.Builder().context(catRapporteur).boost(100.0).build();

        FieldSuggester fieldSuggester = FieldSuggester.of(fs -> fs
                .completion(cs ->
                        cs.skipDuplicates(true)
                                .size(10)
                                .fuzzy(SuggestFuzziness.of(sf -> sf.fuzziness("0").transpositions(true).minLength(2).prefixLength(3)))
                                .field("completion_nom")
                                .contexts("has_idref", List.of(contextAvecIdref))
                                .contexts("roles", List.of(contextDirecteur, contextRapporteur))
                )
        );

        Suggester suggester = Suggester.of(s -> s
                .suggesters("personne-suggestion", fieldSuggester)
                .text(q)
        );

        SearchResponse<Void> response = this.getElasticsearchClient().search(s -> s
                        .index(index)
                        .suggest(suggester)
                , Void.class);

        return personneMapper.suggestionListPersonneToDto(response.suggest());

    }

    /**
     * Retourne 10 suggestion de thématiques à partir d'un mot
     *
     * @param q     Chaîne de caractère à compléter
     * @param index Nom de l'index ES à requêter
     * @return Une liste de suggestions de thématiques au format Dto web
     * @throws Exception
     */
    public List<SuggestionPersonneResponseDto> completionThematique(String q, String index) throws Exception {

        FieldSuggester fieldSuggester = FieldSuggester.of(fs -> fs
                .completion(cs ->
                        cs.skipDuplicates(true)
                                .size(10)
                                .fuzzy(SuggestFuzziness.of(sf -> sf.fuzziness("0").transpositions(true).minLength(2).prefixLength(3)))
                                .field("completion_thematique")
                )
        );

        Suggester suggester = Suggester.of(s -> s
                .suggesters("thematique-suggestion", fieldSuggester)
                .text(q)
        );

        SearchResponse<Void> response = this.getElasticsearchClient().search(s -> s
                        .index(index)
                        .suggest(suggester)
                , Void.class);

        return personneMapper.suggestionListPersonneToDto(response.suggest());
    }

    /**
     * Retourne une liste de facettes en fonction du critère de recherche
     *
     * @param chaine Chaîne de caractère à rechercher
     * @param index  Nom de l'index ES à requêter
     * @return Retourne la liste des facettes au format Dto
     * @throws Exception
     */
    public List<Facet> facets(String chaine, String index) throws Exception {
        return FacetQueryBuilder.facets(this.getElasticsearchClient(), buildQuery(chaine), index, facetProps.getMainPersonnes(), facetProps.getSubsPersonnes(), maxFacetsValues, "");
    }

    /**
     * Rechercher dans ElasticSearch une personne avec son identifiant.
     *
     * @param id    Chaîne de caractère de l'identifiant de la personne
     * @param index Nom de l'index ES à requêter
     * @return Une personne au format Dto web
     * @throws Exception si aucune personne n'a été trouvé ou si une autre erreur est survenue
     */
    public PersonneResponseDto rechercherParIdentifiant(String id, String index) throws Exception {

        TermQuery termQuery = QueryBuilders.term().field("_id").value(id).build();
        Query query = new Query.Builder().term(termQuery).build();

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(index)
                .source(SourceConfig.of(s -> s.filter(f -> f.includes(List.of("nom", "prenom", "has_idref", "theses")))))
                .query(query)
                .build();

        SearchResponse<Personne> response = this.getElasticsearchClient().search(searchRequest, Personne.class);

        if (response.hits().hits().size() != 1) {
            throw new Exception("Person not found");
        }

        return personneMapper.personneToDto(response.hits().hits().get(0));
    }

    //Retourne le nb total de personnes dans l'index
    public long getStatsPersonnes(String index) throws Exception {
        CountResponse countResponse = this.getElasticsearchClient().count(s -> s.index(index));
        return countResponse.count();
    }
}
