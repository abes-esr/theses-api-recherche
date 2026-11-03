package fr.abes.thesesapirecherche.theses.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.*;
import fr.abes.thesesapirecherche.commons.builder.FacetQueryBuilder;
import fr.abes.thesesapirecherche.config.ElasticClient;
import fr.abes.thesesapirecherche.config.FacetProps;
import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.theses.model.These;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;

import java.io.IOException;
import java.util.List;

import static fr.abes.thesesapirecherche.commons.builder.FacetQueryBuilder.addFilters;
import static fr.abes.thesesapirecherche.theses.repository.QueryStringNormalizer.normalize;

@RequiredArgsConstructor
public class TheseRepositoryCustomImpl implements TheseRepositoryCustom {

    private final FacetProps facetProps;
    @Value("${es.theses.indexname}")
    private String esIndexName;
    private final ElasticsearchClient myElasticsearchClient;
    @Value("${maxfacetsvalues}")
    private int maxFacetsValues;

    private static final List<String> CHAMPS_RECHERCHABLE = List.of(
            "resumes.*^30",
            "titres.*^30",
            "nnt^15",
            "doi^15",
            "numSujet^15",
            "numSujetSansS^15",
            "etabSoutenancePpn^15",
            "etabsCotutellePpn^15",
            "partenairesRecherchePpn^15",
            "ecolesDoctoralesPpn^15",
            "nnt^15",
            "discipline^15",
            "sujetsRameauPpn^15",
            "sujetsRameauLibelle^15",
            "sujetsLibelle^15",
            "auteursNP^12",
            "auteursPN^12",
            "auteursPpn^12",
            "directeursNP^2",
            "directeursPN^2",
            "directeursPpn^2",
            "ecolesDoctoralesN^5",
            "etabSoutenanceN^5",
            "oaiSets^5",
            "etabsCotutelleN^1",
            "membresJuryNP^1",
            "membresJuryPN^1",
            "membresJuryPpn^1",
            "partenairesRechercheN^1",
            "presidentJuryNP^1",
            "presidentJuryPN^1",
            "presidentJuryPpn^1",
            "rapporteursNP^1",
            "rapporteursPN^1",
            "rapporteursPpn^1");


    @Override
    public SearchResponse<These> rechercher(String chaine, Integer debut, Integer nombre,
                                            String tri, String filtres) {
        try {
            return myElasticsearchClient.search( q -> q
                    .index(esIndexName)
                    .query(buildQuery(chaine))
                    .from(debut)
                    .size(nombre)
                    .sort(SortEnum.toSortOptions(tri))
                    .trackTotalHits(t -> t.enabled(Boolean.TRUE)), These.class);
        } catch (IOException e) {
            throw new DataAccessResourceFailureException(
                    "Elasticsearch search failed on index="+esIndexName+" query="+chaine+" debut="+debut+" nombre="+nombre+" tri="+tri, e);
        }
    }

    @Override
    public SearchResponse<These> rechercherAvecFacets(String chaine, Integer debut, Integer nombre,
                                            String tri, String filtres) {
        try {
            return myElasticsearchClient.search( q -> q
                    .index(esIndexName)
                    .query(buildQueryWithFacets(chaine, filtres))
                    .from(debut)
                    .size(nombre)
                    .sort(SortEnum.toSortOptions(tri))
                    .trackTotalHits(t -> t.enabled(Boolean.TRUE)), These.class);
        } catch (IOException e) {
            throw new DataAccessResourceFailureException(
                    "Elasticsearch search failed on index="+esIndexName+" query="+chaine+" debut="+debut+" nombre="+nombre+" tri="+tri, e);
        }
    }

    @Override
    public SearchResponse<Void> getSuggestions(String q) throws ElasticsearchException, IOException {
        FieldSuggester fieldSuggester = FieldSuggester.of(fs -> fs
                .completion(cs -> cs.skipDuplicates(true)
                        .size(10)
                        .fuzzy(SuggestFuzziness.of(sf -> sf.
                                fuzziness("0") // TODO p-t enlever 0 pour mettre 1
                                .transpositions(true)
                                .minLength(2)
                                .prefixLength(3)))
                        .field("suggestion")));

        Suggester suggester = Suggester.of(s -> s
                .suggesters("suggestion", fieldSuggester)
                .text(q)
        );

        return ElasticClient
                .getElasticsearchClient()
                .search(s -> s
                                .index(esIndexName)
                                .suggest(suggester)
                , Void.class);
    }

    @Override
    public List<Facet> facets(String chaine, String filtres) throws Exception {
        return FacetQueryBuilder.facets(
                ElasticClient.getElasticsearchClient(),
                buildQuery(chaine),
                esIndexName,
                facetProps.getMainTheses(),
                facetProps.getSubsTheses(),
                maxFacetsValues,
                filtres);
    }

    @Override
    public long getStatsByStatus(Status status) throws IOException, ElasticsearchException {
        return ElasticClient.getElasticsearchClient().count(c -> c
                .index(esIndexName)
                .query(q -> q.term(t -> t
                        .field("status")
                        .value(status.val)))
        ).count();
    }

    @Override
    public String rechercherOrganisme(String ppn) {
        List<String> fieldsToSearchIn = List.of(
                "etabSoutenancePpn",
                "etabsCotutellePpn",
                "partenairesRecherchePpn",
                "ecolesDoctoralesPpn"
        );

        List<String> fieldsIncludedInResponse = List.of(
                "etabSoutenance",
                "etabsCotutelle",
                "partenairesRecherche",
                "ecolesDoctorales"

        );

        Query q = buildQueryForOrganisme(ppn, fieldsToSearchIn);

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(esIndexName)
                .query(q)
                .size(1)
                .source(SourceConfig.of(sc -> sc
                        .filter(f -> f
                                .includes(fieldsIncludedInResponse)) // Récupère cette liste de champ uniquement dans la réponse
                        )
                ));

        SearchResponse<GroupeOrganisme> response;
        try {
            response = myElasticsearchClient.search(searchRequest, GroupeOrganisme.class);

        } catch (IOException e) {
            throw new DataAccessResourceFailureException(
                    "Elasticsearch search failed on index="+esIndexName+" searching for organisme ppn="+ppn, e);
        }


        return response.hits().hits().stream().findFirst()
                    .map(Hit::source)
                    .map(unGroupeDOrganisme -> unGroupeDOrganisme.firstMatch(ppn))
                .orElse("");
    }

    /**
     * Crée une requête ElasticSearch afin de faire correspondre exactement les champs donnés à la valeur ppn
     * @param ppn la valeur qu'on recherche
     * @param fields les champs dans lesquels nous allons lancer la recherche
     * @return une requête type Query d'Elasticsearch
     */
    private Query buildQueryForOrganisme(String ppn, List<String> fields) {
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();
        for (String field : fields) {
            boolBuilder.should(
                    MatchPhraseQuery.of(mp -> mp
                            .field(field)
                            .query(ppn)
                            .slop(0)
                    )._toQuery()
            );
        }

        return boolBuilder.build()._toQuery();
    }

    private Query buildQuery(String chaine) {
        String queryString = normalize(chaine);
        Query query = Query.of(q -> q.queryString(qs -> qs.query(queryString)
                .defaultOperator(Operator.And)
                .analyzeWildcard(true)
                .allowLeadingWildcard(true)
                .fields(CHAMPS_RECHERCHABLE)
                .quoteFieldSuffix(".exact")));

        return query;
    }

    private Query buildQueryWithFacets(String chaine, String filtres) {
        List<Query> filtresList = addFilters(filtres, facetProps.getMainTheses(), facetProps.getSubsTheses());

        return Query.of(q -> q.bool(b -> {
            String queryString = normalize(chaine);
            b.must(m -> m.queryString(qs -> qs
                    .query(queryString)
                    .defaultOperator(Operator.And)
                    .analyzeWildcard(true)
                    .allowLeadingWildcard(true)
                    .fields(CHAMPS_RECHERCHABLE)
                    .quoteFieldSuffix(".exact")));


            b.filter(filtresList);
            return b;
        }));
    }
}
