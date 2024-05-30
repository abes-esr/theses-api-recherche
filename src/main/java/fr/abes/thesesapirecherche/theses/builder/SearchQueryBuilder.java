package fr.abes.thesesapirecherche.theses.builder;

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
import fr.abes.thesesapirecherche.commons.builder.FacetQueryBuilder;
import fr.abes.thesesapirecherche.config.ElasticClient;
import fr.abes.thesesapirecherche.config.FacetProps;
import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.theses.converters.TheseLiteMapper;
import fr.abes.thesesapirecherche.theses.converters.TheseMapper;
import fr.abes.thesesapirecherche.theses.dto.*;
import fr.abes.thesesapirecherche.theses.model.Organisme;
import fr.abes.thesesapirecherche.theses.model.These;
import jdk.jshell.spi.ExecutionControlProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fr.abes.thesesapirecherche.commons.builder.FacetQueryBuilder.addFilters;
import static fr.abes.thesesapirecherche.commons.builder.FacetQueryBuilder.buildFilter;

@Slf4j
@Component
@EnableConfigurationProperties(value = FacetProps.class)
public class SearchQueryBuilder {

    private final TheseMapper theseMapper = new TheseMapper();
    private final TheseLiteMapper theseLiteMapper = new TheseLiteMapper();

    @Value("${es.theses.indexname}")
    private String esIndexName;

    @Value("${maxfacetsvalues}")
    private int maxFacetsValues;

    @Autowired
    private FacetProps facetProps;

    private Query buildQuery(String chaine) {
        chaine = replaceAndOutsideQuotes(chaine);
        chaine = replaceSpacesOutsideQuotes(chaine);

        QueryStringQuery.Builder builderQuery = new QueryStringQuery.Builder();
        builderQuery.query(chaine);
        builderQuery.defaultOperator(Operator.And);
        builderQuery.analyzeWildcard(true);
        builderQuery.allowLeadingWildcard(true);
        builderQuery.fields("resumes.*^30",
                "titres.*^30",
                "nnt^15",
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
            } else if (c == ' ' && !insideQuotes && !insideBrackets) {
                result.append(" AND ");
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }
    public static String replaceAndOutsideQuotes(String input) {
        StringBuilder result = new StringBuilder();
        boolean insideQuotes = false;
        int i = 0;

        while (i < input.length()) {
            char currentChar = input.charAt(i);

            if (currentChar == '\"') {
                insideQuotes = !insideQuotes;
                result.append(currentChar);
                i++;
            } else if (!insideQuotes && i + 2 < input.length() && input.startsWith(" AND ", i)) {
                result.append(" ");
                i += 5;  // Skip past the "AND"
            } else {
                result.append(currentChar);
                i++;
            }
        }

        return result.toString();
    }

    public ResponseTheseLiteDto simple(String chaine, Integer debut, Integer nombre, String tri, String filtres) throws Exception {
        SearchResponse<These> response = ElasticClient.getElasticsearchClient().search(
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
        res.setTook(response.took());
        res.setTotalHits(response.hits().total().value());
        return res;
    }

    public ResponseTheseCSVDto simpleToFullThese(String chaine, Integer debut, Integer nombre, String tri, String filtres) throws Exception {
        SearchResponse<These> response = ElasticClient.getElasticsearchClient().search(
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

        ResponseTheseCSVDto res = new ResponseTheseCSVDto();
        List<TheseResponseDto> liste = new ArrayList<>();

        for (Hit<These> theseHit : response.hits().hits()) {
            liste.add(theseMapper.theseToDto(theseHit.source()));
        }

        res.setTheses(liste);
        return res;
    }

    public String getOrganismeName(String ppn) throws Exception {
        QueryStringQuery.Builder builderQuery = new QueryStringQuery.Builder();
        builderQuery.query('"' + ppn + '"');
        builderQuery.defaultOperator(Operator.And);
        builderQuery.fields("etabSoutenancePpn", "etabsCotutellePpn", "partenairesRecherchePpn", "ecolesDoctoralesPpn");
        builderQuery.quoteFieldSuffix(".exact");

        SearchResponse<These> response = ElasticClient.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .bool(t -> t
                                        .must(builderQuery.build()._toQuery())
                                ))
                        .trackTotalHits(t -> t.enabled(Boolean.TRUE)),
                These.class
        );

        if ((int) response.hits().total().value() > 0)
            return getEtabNameFromSourceAndPPN(response.hits().hits().get(0).source(), ppn);
        else return "";
        //Si on retourne "" alors ce n'est pas un organisme mais une personne
    }

    // TODO : Simplifier cette fonction / éviter duplication
    public ThesesByOrganismeResponseDto searchByOrganisme(String ppn) throws Exception {
        ThesesByOrganismeResponseDto thesesByOrganismeResponse = new ThesesByOrganismeResponseDto();

        // Theses soutenues/en cours dans cet étab

        SearchResponse<These> responseEtabSoutenanceEnEcours = ElasticClient.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .bool(t -> t
                                        .must(n -> n.match(m -> m.query(ppn).field("etabSoutenancePpn")))
                                        .filter(f -> f.term(x -> x.field("status").value("enCours")))
                                ))
                        .size(100)
                        .trackTotalHits(t -> t.enabled(Boolean.TRUE)),
                These.class
        );

        SearchResponse<These> responseEtabSoutenanceSoutenue = ElasticClient.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .bool(t -> t
                                        .must(n -> n.match(m -> m.query(ppn).field("etabSoutenancePpn")))
                                        .filter(f -> f.term(x -> x.field("status").value("soutenue")))
                                ))
                        .size(100)
                        .trackTotalHits(t -> t.enabled(Boolean.TRUE)),
                These.class
        );

        Iterator<Hit<These>> iterator = responseEtabSoutenanceEnEcours.hits().hits().iterator();
        Iterator<Hit<These>> iteratorSoutenue = responseEtabSoutenanceSoutenue.hits().hits().iterator();
        List<TheseLiteResponseDto> listeEtabSoutenance = new ArrayList<>();
        List<TheseLiteResponseDto> listeEtabSoutenanceEnCours = new ArrayList<>();
        while (iterator.hasNext()) {
            Hit<These> theseHit = iterator.next();
            listeEtabSoutenanceEnCours.add(theseLiteMapper.theseLiteToDto(theseHit));
        }
        while (iteratorSoutenue.hasNext()) {
            Hit<These> theseHit = iteratorSoutenue.next();
            listeEtabSoutenance.add(theseLiteMapper.theseLiteToDto(theseHit));
        }
        thesesByOrganismeResponse.setEtabSoutenance(listeEtabSoutenance);
        thesesByOrganismeResponse.setEtabSoutenanceEnCours(listeEtabSoutenanceEnCours);
        thesesByOrganismeResponse.setTotalHitsetabSoutenanceEnCours(responseEtabSoutenanceEnEcours.hits().total().value());
        thesesByOrganismeResponse.setTotalHitsetabSoutenance(responseEtabSoutenanceSoutenue.hits().total().value());


        // Theses en cotutelle dans cet étab
        SearchResponse<These> responseEtabCotutelle = ElasticClient.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .bool(t -> t
                                        .must(n -> n.match(m -> m.query(ppn).field("etabsCotutellePpn")))
                                        .filter(f -> f.term(x -> x.field("status").value("soutenue")))
                                ))
                        .size(100)
                        .trackTotalHits(t -> t.enabled(Boolean.TRUE)),
                These.class
        );

        SearchResponse<These> responseEtabCotutelleEnCours = ElasticClient.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .bool(t -> t
                                        .must(n -> n.match(m -> m.query(ppn).field("etabsCotutellePpn")))
                                        .filter(f -> f.term(x -> x.field("status").value("enCours")))
                                ))
                        .size(100)
                        .trackTotalHits(t -> t.enabled(Boolean.TRUE)),
                These.class
        );

        List<TheseLiteResponseDto> listeEtabCotutelle = new ArrayList<>();
        List<TheseLiteResponseDto> listeEtabCotutelleEnCours = new ArrayList<>();
        iteratorSoutenue = responseEtabCotutelle.hits().hits().iterator();
        iterator = responseEtabCotutelleEnCours.hits().hits().iterator();
        while (iteratorSoutenue.hasNext()) {
            Hit<These> theseHit = iteratorSoutenue.next(); listeEtabCotutelle.add(theseLiteMapper.theseLiteToDto(theseHit));
        }
        while (iterator.hasNext()) {
            Hit<These> theseHit = iterator.next(); listeEtabCotutelleEnCours.add(theseLiteMapper.theseLiteToDto(theseHit));
        }
        thesesByOrganismeResponse.setEtabCotutelle(listeEtabCotutelle);
        thesesByOrganismeResponse.setEtabCotutelleEnCours(listeEtabCotutelleEnCours);
        thesesByOrganismeResponse.setTotalHitsetabCotutelleEnCours(responseEtabCotutelleEnCours.hits().total().value());
        thesesByOrganismeResponse.setTotalHitsetabCotutelle(responseEtabCotutelle.hits().total().value());


        // Theses en partenariat
        SearchResponse<These> responsePartenaire = ElasticClient.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .bool(t -> t
                                        .must(n -> n.match(m -> m.query(ppn).field("partenairesRecherchePpn")))
                                        .filter(f -> f.term(x -> x.field("status").value("soutenue")))
                                ))
                        .size(100)
                        .trackTotalHits(t -> t.enabled(Boolean.TRUE)),
                These.class
        );

        SearchResponse<These> responsePartenaireEnCours = ElasticClient.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .bool(t -> t
                                        .must(n -> n.match(m -> m.query(ppn).field("partenairesRecherchePpn")))
                                        .filter(f -> f.term(x -> x.field("status").value("enCours")))
                                ))
                        .size(100)
                        .trackTotalHits(t -> t.enabled(Boolean.TRUE)),
                These.class
        );

        List<TheseLiteResponseDto> listePartenaire = new ArrayList<>();
        List<TheseLiteResponseDto> listePartenaireEnCours = new ArrayList<>();
        iterator = responsePartenaireEnCours.hits().hits().iterator();
        iteratorSoutenue = responsePartenaire.hits().hits().iterator();
        while (iterator.hasNext()) {
            Hit<These> theseHit = iterator.next();listePartenaireEnCours.add(theseLiteMapper.theseLiteToDto(theseHit));
        }
        while (iteratorSoutenue.hasNext()) {
            Hit<These> theseHit = iteratorSoutenue.next();listePartenaire.add(theseLiteMapper.theseLiteToDto(theseHit));
        }
        thesesByOrganismeResponse.setPartenaireRecherche(listePartenaire);
        thesesByOrganismeResponse.setPartenaireRechercheEnCours(listePartenaireEnCours);
        thesesByOrganismeResponse.setTotalHitspartenaireRechercheEnCours(responsePartenaireEnCours.hits().total().value());
        thesesByOrganismeResponse.setTotalHitspartenaireRecherche(responsePartenaire.hits().total().value());

        // Ecoles doctorale
        SearchResponse<These> responseEcole = ElasticClient.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .bool(t -> t
                                        .must(n -> n.match(m -> m.query(ppn).field("ecolesDoctoralesPpn")))
                                        .filter(f -> f.term(x -> x.field("status").value("soutenue")))
                                ))
                        .size(100)
                        .trackTotalHits(t -> t.enabled(Boolean.TRUE)),
                These.class
        );

        SearchResponse<These> responseEcoleEnCours = ElasticClient.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .bool(t -> t
                                        .must(n -> n.match(m -> m.query(ppn).field("ecolesDoctoralesPpn")))
                                        .filter(f -> f.term(x -> x.field("status").value("enCours")))
                                ))
                        .size(100)
                        .trackTotalHits(t -> t.enabled(Boolean.TRUE)),
                These.class
        );

        List<TheseLiteResponseDto> listeEcoleDoctorale = new ArrayList<>();
        List<TheseLiteResponseDto> listeEcoleDoctoraleEnCours = new ArrayList<>();
        iterator = responseEcoleEnCours.hits().hits().iterator();
        iteratorSoutenue = responseEcole.hits().hits().iterator();
        while (iterator.hasNext()) {
            Hit<These> theseHit = iterator.next();
            listeEcoleDoctoraleEnCours.add(theseLiteMapper.theseLiteToDto(theseHit));
        }
        while (iteratorSoutenue.hasNext()) {
            Hit<These> theseHit = iteratorSoutenue.next();
            listeEcoleDoctorale.add(theseLiteMapper.theseLiteToDto(theseHit));
        }
        thesesByOrganismeResponse.setEcoleDoctorale(listeEcoleDoctorale);
        thesesByOrganismeResponse.setEcoleDoctoraleEnCours(listeEcoleDoctoraleEnCours);
        thesesByOrganismeResponse.setTotalHitsecoleDoctoraleEnCours(responseEcoleEnCours.hits().total().value());
        thesesByOrganismeResponse.setTotalHitsecoleDoctorale(responseEcole.hits().total().value());

        return thesesByOrganismeResponse;
    }

    public long getStatsTheses(String statusFilter) throws Exception {
        List<String> list = List.of(statusFilter);
        CountResponse countResponse = ElasticClient.getElasticsearchClient().count(
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
        return FacetQueryBuilder.facets(ElasticClient.getElasticsearchClient(), buildQuery(chaine), esIndexName, facetProps.getMainTheses(), facetProps.getSubsTheses(), maxFacetsValues, filtres);
    }

    public TheseResponseDto rechercheSurId(String nnt) throws Exception {
        SearchResponse<These> response = ElasticClient.getElasticsearchClient().search(s -> s
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

        SearchResponse<Void> response = ElasticClient.getElasticsearchClient().search(s -> s
                        .index(esIndexName)
                        .suggest(suggester)
                , Void.class);

        List<String> listeSuggestions = new ArrayList<String>();

        response.suggest().entrySet().forEach(a -> a.getValue().forEach(s -> s.completion().options().forEach(b -> listeSuggestions.add(b.text()))));

        return listeSuggestions;
    }

    private String getEtabNameFromSourceAndPPN(These source, String ppn) {
        //Si on est l'établissement de soutenance, on récupère le nom directement
        if (source.getEtabSoutenancePpn() != null && source.getEtabSoutenancePpn().equals(ppn)) return source.getEtabSoutenanceN();

        //sinon on cherche le PPN dans les etabs de cotutelle, etc.
        for (Organisme e : source.getEtabsCotutelle()) {
            if (e.getPpn() != null && e.getPpn().equals(ppn)) return e.getNom();
        }

        for (Organisme e : source.getPartenairesRecherche()) {
            if (e.getPpn() != null && e.getPpn().equals(ppn)) return e.getNom();
        }

        for (Organisme e : source.getEcolesDoctorales()) {
            if (e.getPpn() != null  && e.getPpn().equals(ppn)) return e.getNom();
        }

        return "";
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
