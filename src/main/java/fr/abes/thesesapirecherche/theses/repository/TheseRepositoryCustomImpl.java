package fr.abes.thesesapirecherche.theses.repository;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import fr.abes.thesesapirecherche.config.ElasticClient;
import fr.abes.thesesapirecherche.config.FacetProps;
import fr.abes.thesesapirecherche.theses.model.These;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder.cleanDoi;
import static fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder.replaceAndOutsideQuotes;

public class TheseRepositoryCustomImpl implements TheseRepositoryCustom {

    private final ElasticsearchOperations operations;
    private final FacetProps facetProps;
    private final String esIndexName = "theses3";

    public TheseRepositoryCustomImpl(ElasticsearchOperations operations,
                                     FacetProps facetProps) {
        this.operations = operations;
        this.facetProps = facetProps;
    }

    @Override
    public SearchResponse<These> rechercher(String chaine, Integer debut, Integer nombre,
                                            String tri, String filtres) {
//        ElasticClient.getElasticsearchClient().search();

//        NativeQueryBuilder queryBuilder = NativeQuery.builder()
//                .withQuery(q -> q.bool(b -> b
//                        .must(buildQuery(chaine))
//                        .filter(FacetQueryBuilder.addFilters(filtres, facetProps.getMainTheses(), facetProps.getSubsTheses()))))
//                .withPageable(new OffsetPageRequest(debut, nombre))   // from + size
//                .withTrackTotalHits(true);
        try {
            return ElasticClient.getElasticsearchClient().search( q -> q
                    .index(esIndexName)
                    .query(buildQuery(chaine))
                    .from(debut)
                    .size(nombre)
                    .sort(addTri(tri))
                    .trackTotalHits(t -> t.enabled(Boolean.TRUE)), These.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        List<SortOptions> monTri = addTri(tri);
//        if (!monTri.isEmpty()) {
//            queryBuilder.withSort(monTri);
//        }
//
//        return operations.search(queryBuilder.build(), These.class, IndexCoordinates.of(esIndexName));
    }

    private Query buildQuery(String chaine) {
        chaine = chaine.trim();
        chaine = replaceAndOutsideQuotes(chaine);
        chaine = replaceSpacesOutsideQuotes(chaine);
        chaine = cleanDoi(chaine);

        QueryStringQuery.Builder builderQuery = new QueryStringQuery.Builder();
        builderQuery.query(chaine);
        builderQuery.defaultOperator(Operator.And);
        builderQuery.analyzeWildcard(true);
        builderQuery.allowLeadingWildcard(true);
        builderQuery.fields("resumes.*^30",
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
                "rapporteursPpn^1"
        );
        builderQuery.quoteFieldSuffix(".exact");

        return builderQuery.build()._toQuery();
    }

    private String replaceSpacesOutsideQuotes(String input) {
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
