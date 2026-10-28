package fr.abes.thesesapirecherche.theses.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import fr.abes.thesesapirecherche.config.ElasticClient;
import fr.abes.thesesapirecherche.config.FacetProps;
import fr.abes.thesesapirecherche.theses.model.These;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder.cleanDoi;
import static fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder.replaceAndOutsideQuotes;
import static fr.abes.thesesapirecherche.theses.repository.QueryStringNormalizer.normalize;

public class TheseRepositoryCustomImpl implements TheseRepositoryCustom {

    private final ElasticsearchOperations operations;
    private final FacetProps facetProps;
    private final String esIndexName = "theses3";
    private final ElasticsearchClient myElasticsearchClient;

    private final List<String> CHAMPS_RECHERCHABLE = List.of(
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

    public TheseRepositoryCustomImpl(ElasticsearchOperations operations,
                                     FacetProps facetProps) {
        this.operations = operations;
        this.facetProps = facetProps;
        myElasticsearchClient = ElasticClient.getElasticsearchClient();
    }

    @Override
    public SearchResponse<These> rechercher(String chaine, Integer debut, Integer nombre,
                                            String tri, String filtres) {
        try {
            return myElasticsearchClient.search( q -> q
                    .index(esIndexName)
                    .query(buildQuery(chaine))
                    .from(debut)
                    .size(nombre)
                    .sort(SortEnum.fromCode(tri).getSortOption())
                    .trackTotalHits(t -> t.enabled(Boolean.TRUE)), These.class);
        } catch (IOException e) {
            throw new DataAccessResourceFailureException(
                    "Elasticsearch search failed on index="+esIndexName+" query="+chaine+" debut="+debut+" nombre="+nombre+" tri="+tri, e);
        }
    }

    private Query buildQuery(String chaine) {
        if (chaine.isBlank()) {
            return Query.of(q -> q.matchAll(m -> m));
        }

        String queryString = normalize(chaine);

        return Query.of(q -> q.queryString(qs -> qs.
                query(queryString)
                .defaultOperator(Operator.And)
                .analyzeWildcard(true)
                .allowLeadingWildcard(true)
                .fields(CHAMPS_RECHERCHABLE)
                .quoteFieldSuffix(".exact")));
    }

    public static String replaceSpacesOutsideQuotes(String input) {
        StringBuilder result = new StringBuilder();
        boolean insideQuotes = false;
        boolean insideBrackets = false;


        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '"') {
                insideQuotes = !insideQuotes;
                result.append(c);
            } else if (c == '[') {
                insideBrackets = true;
                result.append(c);
            } else if (c == ']') {
                insideBrackets = false;
                result.append(c);
            }
            else if (c == ' ') {
                if (i + 2 < input.length() && input.charAt(i + 1) == 'O'&& input.charAt(i + 2) == 'R') {

                    result.append(" OR ");
                    i+= 3;
                } else if (i + 3 < input.length() && input.charAt(i + 1) == 'N' && input.charAt(i + 2) == 'O' && input.charAt(i + 3) == 'T') {

                    result.append(" NOT ");
                    i+= 4;
                } else if (!insideQuotes && !insideBrackets) {
                    result.append(" AND ");
                } else {
                    result.append(c);
                }
            } else {
                result.append(c);

            }

        }

        return result.toString();
    }
}
