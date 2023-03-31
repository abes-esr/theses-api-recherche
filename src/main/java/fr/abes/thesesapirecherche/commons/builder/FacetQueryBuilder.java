package fr.abes.thesesapirecherche.commons.builder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import fr.abes.thesesapirecherche.config.FacetProps;
import fr.abes.thesesapirecherche.dto.Checkbox;
import fr.abes.thesesapirecherche.dto.Facet;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableConfigurationProperties(value = FacetProps.class)
public class FacetQueryBuilder {

    public static List<Facet> facets(ElasticsearchClient client, Query query, String index, List<FacetProps.MainFacet> mainFacets, List<FacetProps.SubFacet> subsFacets, int maxFacetValues) throws Exception {
        Map<String, Aggregation> map = new HashMap<>();

        if (subsFacets == null) {
            subsFacets = new ArrayList<>();
        }

        // Tri par ordre alphabétique sur les clés
        List<Map<String, SortOrder>> order = new ArrayList<>();
        Map<String, SortOrder> orderMap = new HashMap<>();
        orderMap.put("_key", SortOrder.Asc);
        order.add(orderMap);

        //Aggregation des facets
        mainFacets.forEach((i) -> {
            Aggregation agg;
            // Pour les disciplines, on ne remonte que les valeurs ayant + de 5 theses
            if (i.getChamp().equals("discipline.exact")) {
                agg = new Aggregation.Builder()
                        .terms(new TermsAggregation.Builder().field(i.getChamp()).order(order).size(maxFacetValues).minDocCount(5).build())
                        .build();
            } else {
                agg = new Aggregation.Builder()
                        .terms(new TermsAggregation.Builder().field(i.getChamp()).order(order).size(maxFacetValues).build())
                        .build();
            }
            map.put(i.getChamp(), agg);
        });

        // Aggregation des sous-facets
        subsFacets.forEach((i) -> {
            Aggregation agg = new Aggregation.Builder()
                    .terms(new TermsAggregation.Builder().field(i.getChamp()).order(order).size(maxFacetValues).build())
                    .build();
            map.put(i.getChamp(), agg);
        });

        //Requête ES
        SearchResponse<Void> response = client.search(
                s -> s
                        .index(index)
                        .query(q -> q
                                .bool(t -> t
                                        .must(query)
                                ))
                        .size(0).aggregations(map),
                Void.class
        );

        List<Facet> facets = new ArrayList<>();

        // Récupération des facets
        List<FacetProps.SubFacet> finalSubsFacets = subsFacets;
        mainFacets.forEach((i) -> {
            Facet f = new Facet();
            List<Checkbox> checkboxes = new ArrayList<>();
            response.aggregations().get(i.getChamp()).sterms().buckets().array().forEach(a -> {
                        Checkbox c = new Checkbox(a.key(), a.docCount(), null, null);
                        //Récupération des sous-facets
                        finalSubsFacets.forEach(s -> {
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

    public static List<Query> addFilters(String f, List<FacetProps.MainFacet> mainFacets, List<FacetProps.SubFacet> subsFacets) {
        //Nettoyage et mise en forme de la map des filtres
        //Suppression des [] et découpage de la string des filtres vers une map
        String[] filtres = f.replace("[", "").replace("\"]", "").split("\"\\&");

        //Création d'une map : NomFiltre (key) / liste valeurs (values)
        Map<String, List<String>> mapFiltres = new HashMap<>();
        String dateDebut = "";
        String dateFin = "";

        for (String s : filtres) {
            String[] filtreSplit = s.split("=\"");
            if (filtreSplit.length > 1) {
                String key = filtreSplit[0];
                String value = filtreSplit[1];

                //Gestion des sous-facets
                if (subsFacets != null) {
                    for (FacetProps.SubFacet facet : subsFacets) {
                        if (value.equals(facet.getLibelle().toLowerCase())) {
                            key = facet.getLibelle().toLowerCase();
                        }
                    }
                }

                // Gestion des facets principales
                List<String> a = new ArrayList<>();
                if (mapFiltres.containsKey(key)) {
                    a = mapFiltres.get(key);
                }
                a.add(value);
                mapFiltres.put(key, a);

                //Gestion des dates
                if (key.equals("datedebut")) {
                    dateDebut = value;
                } else if (key.equals("datefin")) {
                    dateFin = value;
                }
            }
        }

        List<Query> listeFiltres = new ArrayList<>();

        //Pour chaque filtre présent dans la map, on crée une TermsQuery que l'on ajoute à la liste
        mapFiltres.forEach((k, v) -> {
            for (FacetProps.MainFacet facet : mainFacets) {
                if (facet.getLibelle().toLowerCase().equals(k)) {
                    listeFiltres.add(buildFilter(facet.getChamp(), v));
                }
            }

            if (subsFacets != null) {
                for (FacetProps.SubFacet facet : subsFacets) {
                    if (facet.getLibelle().toLowerCase().equals(k)) {
                        //Cas particulier Accessible en ligne
                        if (facet.getChamp().equals("accessible"))
                            listeFiltres.add(buildFilter(facet.getChamp(), List.of("oui")));
                        else
                            listeFiltres.add(buildFilter(facet.getChamp(), v));
                    }
                }
            }
        });

        if (!dateDebut.isEmpty() || !dateFin.isEmpty()) {
            listeFiltres.add(dateFilter(dateDebut, dateFin, f));
        }

        return listeFiltres;
    }

    private static Query buildFilter(String field, List<String> values) {
        //Termsquery : Field / Liste de values
        TermsQueryField termsQueryField = new TermsQueryField.Builder()
                .value(values.stream().map(FieldValue::of).toList())
                .build();

        TermsQuery termsQuery = QueryBuilders.terms().field(field).terms(termsQueryField).build();

        return new Query.Builder().terms(termsQuery).build();
    }

    private static Query dateFilter(String dateMin, String dateMax, String filtres) {
        String field;

        //TODO : Corriger ce champ une fois l'indexation faite
        if (filtres.contains("enCours")) {
            field = "datePremiereInscriptionDoctorat";
        } else {
            field = "dateSoutenance";
        }

        Query dateRangeQuery = RangeQuery.of(r -> r
                .field(field)
                .gte(JsonData.of(dateMin.equals("") ? "1000" : dateMin))
                .lte(JsonData.of(dateMax.equals("") ? "2999" : dateMax))
                .format("yyyy")
        )._toQuery();

        return dateRangeQuery;
    }
}
