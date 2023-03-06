package fr.abes.thesesapirecherche.theses.builder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.SuggestFuzziness;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import fr.abes.thesesapirecherche.config.FacetProps;
import fr.abes.thesesapirecherche.theses.converters.TheseLiteMapper;
import fr.abes.thesesapirecherche.theses.converters.TheseMapper;
import fr.abes.thesesapirecherche.theses.dto.*;
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
import java.util.*;

@Slf4j
@Component
@EnableConfigurationProperties(value = FacetProps.class)
public class SearchQueryBuilder {

    private final TheseMapper theseMapper = new TheseMapper();
    private final TheseLiteMapper theseLiteMapper = new TheseLiteMapper();
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
    @Value("${es.theses.indexname}")
    private String esIndexName;
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

                RestClient client = RestClient.builder(new HttpHost(esHostname, Integer.parseInt(esPort), esScheme)).setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
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
        builderQuery.fields("resumes.*^30", "titres.*^30", "nnt^15", "discipline^15", "sujetsRameau^15", "sujets^15", "auteursNP^12", "directeursNP^2", "ecolesDoctoralesN^5", "etabSoutenanceN^5", "oaiSets^5", "etabsCotutelleN^1", "membresJuryNP^1", "partenairesRechercheN^1", "presidentJuryNP^1", "rapporteurs^1");
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
                                        .filter(addFilters(filtres))
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

    public List<Facet> facets(String chaine) throws Exception {
        Map<String, Aggregation> map = new HashMap<>();


        //Aggregation des facets
        facetProps.getMain().forEach((i) -> {
            Aggregation agg = new Aggregation.Builder()
                    .terms(new TermsAggregation.Builder().field(i.getChamp()).build())
                    .build();
            map.put(i.getChamp(), agg);
        });

        // Aggregation des sous-facets
        facetProps.getSubs().forEach((i) -> {
            Aggregation agg = new Aggregation.Builder()
                    .terms(new TermsAggregation.Builder().field(i.getChamp()).build())
                    .build();
            map.put(i.getChamp(), agg);
        });

        //Requête ES
        SearchResponse<Void> response = this.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .bool(t -> t
                                        .must(buildQuery(chaine))
                                ))
                        .size(0).aggregations(map),
                Void.class
        );

        List<Facet> facets = new ArrayList<>();

        // Récupération des facets
        facetProps.getMain().forEach((i) -> {
            Facet f = new Facet();
            List<Checkbox> checkboxes = new ArrayList<>();
            response.aggregations().get(i.getChamp()).sterms().buckets().array().forEach(a -> {
                        Checkbox c = new Checkbox(a.key(), a.docCount(), null, null);
                        //Récupération des sous-facets
                        facetProps.getSubs().forEach(s -> {
                            List<Checkbox> subcheckboxes = new ArrayList<>();
                            if (s.getParentName().equals(c.getName())) {

                                //Cas particulier pour facet "accessible"
                                if (s.getChamp().equals("accessible")) {
                                    response.aggregations().get(s.getChamp()).sterms().buckets().array().forEach(b -> {
                                        if (b.key().equals("oui")) {
                                            Checkbox accessibleCheckbox = new Checkbox(s.getLibelle(), b.docCount(), s.getParentName(), null);
                                            subcheckboxes.add(accessibleCheckbox);
                                        }
                                    });
                                    //Fin cas particulier
                                } else {
                                    response.aggregations().get(s.getChamp()).sterms().buckets().array().forEach(b -> subcheckboxes.add(new Checkbox(b.key(), b.docCount(), s.getParentName(), null)));
                                }
                            }
                            if (!subcheckboxes.isEmpty())
                                c.setCheckboxes(subcheckboxes);
                        });
                        checkboxes.add(c);
                    }

            );

            f.setName(i.getLibelle());
            f.setCheckboxes(checkboxes);
            f.setSearchBar(i.isSearchBar());
            facets.add(f);
        });
        return facets;
    }

    public Checkbox addSubs(Checkbox c, String chaine) throws Exception {
        Map<String, Aggregation> map = new HashMap<>();


        return c;

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
                case "dateAsc" -> new SortOptions.Builder().field(f -> f.field("dateSoutenance").order(SortOrder.Asc)).build();
                case "dateDesc" -> new SortOptions.Builder().field(f -> f.field("dateSoutenance").order(SortOrder.Desc)).build();
                case "auteursAsc" -> new SortOptions.Builder().field(f -> f.field("auteursNP.exact").order(SortOrder.Asc)).build();
                case "auteursDesc" -> new SortOptions.Builder().field(f -> f.field("auteursNP.exact").order(SortOrder.Desc)).build();
                case "disciplineAsc" -> new SortOptions.Builder().field(f -> f.field("discipline.exact").order(SortOrder.Asc)).build();
                case "disciplineDesc" -> new SortOptions.Builder().field(f -> f.field("discipline.exact").order(SortOrder.Desc)).build();
                default -> null;
            };
            if (sort != null)
                list.add(sort);
        }
        return list;
    }

    private List<Query> addFilters(String f) {
        //Nettoyage et mise en forme de la map des filtres
        //Suppression des [] et découpage de la string des filtres vers une map
        String[] filtres = f.replace("[", "").replace("\"]", "").split("\"\\&");

        //Création d'une map : NomFiltre (key) / liste valeurs (values)
        Map<String, List<String>> mapFiltres = new HashMap<>();
        for (String s : filtres) {
            String[] filtreSplit = s.split("=\"");
            if (filtreSplit.length > 1) {
                String key = filtreSplit[0];
                String value = filtreSplit[1];

                //Gestion des sous-facets
                for (FacetProps.SubFacet facet : facetProps.getSubs()) {
                    if (value.equals(facet.getLibelle().toLowerCase())) {
                        key = facet.getLibelle().toLowerCase();
                    }
                }
                // Gestion des facets principales
                List<String> a = new ArrayList<>();
                if (mapFiltres.containsKey(key)) {
                    a = mapFiltres.get(key);
                }
                a.add(value);
                mapFiltres.put(key, a);
            }
        }

        List<Query> listeFiltres = new ArrayList<>();

        //Pour chaque filtre présent dans la map, on crée une TermsQuery que l'on ajoute à la liste
        mapFiltres.forEach((k, v) -> {
            for (FacetProps.MainFacet facet : facetProps.getMain()) {
                if (facet.getLibelle().toLowerCase().equals(k)) {
                    listeFiltres.add(buildFilter(facet.getChamp(), v));
                }
            }
            for (FacetProps.SubFacet facet : facetProps.getSubs()) {
                if (facet.getLibelle().toLowerCase().equals(k)) {
                    //Cas particulier Accessible en ligne
                    if (facet.getChamp().equals("accessible"))
                        listeFiltres.add(buildFilter(facet.getChamp(), List.of("oui")));
                    else
                        listeFiltres.add(buildFilter(facet.getChamp(), v));
                }
            }
        });
        return listeFiltres;
    }

    private Query buildFilter(String field, List<String> values) {
        //Termsquery : Field / Liste de values
        TermsQueryField termsQueryField = new TermsQueryField.Builder()
                .value(values.stream().map(FieldValue::of).toList())
                .build();

        TermsQuery termsQuery = QueryBuilders.terms().field(field).terms(termsQueryField).build();

        return new Query.Builder().terms(termsQuery).build();
    }
}
