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
import fr.abes.thesesapirecherche.theses.dto.ResponseTheseLiteDto;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.theses.dto.ThesesByOrganismeResponseDto;
import fr.abes.thesesapirecherche.theses.model.Organisme;
import fr.abes.thesesapirecherche.theses.model.These;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

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

    @Value("${es.theses.indexname}")
    private String esIndexName;

    @Value("${maxfacetsvalues}")
    private int maxFacetsValues;

    @Autowired
    private FacetProps facetProps;

    private Query buildQuery(String chaine) {
        QueryStringQuery.Builder builderQuery = new QueryStringQuery.Builder();
        builderQuery.query(chaine);
        builderQuery.defaultOperator(Operator.And);
        builderQuery.fields("resumes.*^30", "titres.*^30", "nnt^15", "discipline^15", "sujetsRameauPpn^15", "sujetsRameauLibelle^15", "sujets^15", "auteursNP^12", "directeursNP^2", "ecolesDoctoralesN^5", "etabSoutenanceN^5", "oaiSets^5", "etabsCotutelleN^1", "membresJuryNP^1", "partenairesRechercheN^1", "presidentJuryNP^1", "rapporteurs^1");
        builderQuery.quoteFieldSuffix(".exact");

        return builderQuery.build()._toQuery();
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

    public ThesesByOrganismeResponseDto searchByOrganisme(String ppn) throws Exception {
        ThesesByOrganismeResponseDto thesesByOrganismeResponse = new ThesesByOrganismeResponseDto();

        SearchResponse<These> responseEtabSoutenance = ElasticClient.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .match(t -> t
                                        .query(ppn)
                                        .field("etabSoutenancePpn"))),
                These.class
        );

        Iterator<Hit<These>> iterator = responseEtabSoutenance.hits().hits().iterator();
        List<TheseLiteResponseDto> listeEtabSoutenance = new ArrayList<>();
        List<TheseLiteResponseDto> listeEtabSoutenanceEnCours = new ArrayList<>();
        while (iterator.hasNext()) {
            Hit<These> theseHit = iterator.next();
            TheseLiteResponseDto theseListeDto = theseLiteMapper.theseLiteToDto(theseHit);
            if (theseListeDto.getStatus().equals("enCours"))
                listeEtabSoutenanceEnCours.add(theseLiteMapper.theseLiteToDto(theseHit));
            else listeEtabSoutenance.add(theseLiteMapper.theseLiteToDto(theseHit));
        }
        thesesByOrganismeResponse.setEtabSoutenance(listeEtabSoutenance);
        thesesByOrganismeResponse.setEtabSoutenanceEnCours(listeEtabSoutenanceEnCours);

        SearchResponse<These> responseEtabCotutelle = ElasticClient.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .match(t -> t
                                        .query(ppn)
                                        .field("etabsCotutellePpn"))),
                These.class
        );
        List<TheseLiteResponseDto> listeEtabCotutelle = new ArrayList<>();
        List<TheseLiteResponseDto> listeEtabCotutelleEnCours = new ArrayList<>();
        iterator = responseEtabCotutelle.hits().hits().iterator();
        while (iterator.hasNext()) {
            Hit<These> theseHit = iterator.next();
            TheseLiteResponseDto theseListeDto = theseLiteMapper.theseLiteToDto(theseHit);
            if (theseListeDto.getStatus().equals("enCours"))
                listeEtabCotutelleEnCours.add(theseLiteMapper.theseLiteToDto(theseHit));
            else listeEtabCotutelle.add(theseLiteMapper.theseLiteToDto(theseHit));
        }
        thesesByOrganismeResponse.setEtabCotutelle(listeEtabCotutelle);
        thesesByOrganismeResponse.setEtabCotutelleEnCours(listeEtabCotutelleEnCours);

        SearchResponse<These> responsePartenaire = ElasticClient.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .match(t -> t
                                        .query(ppn)
                                        .field("partenairesRecherchePpn"))),
                These.class
        );
        List<TheseLiteResponseDto> listePartenaire = new ArrayList<>();
        List<TheseLiteResponseDto> listePartenaireEnCours = new ArrayList<>();
        iterator = responsePartenaire.hits().hits().iterator();
        while (iterator.hasNext()) {
            Hit<These> theseHit = iterator.next();
            TheseLiteResponseDto theseListeDto = theseLiteMapper.theseLiteToDto(theseHit);
            if (theseListeDto.getStatus().equals("enCours"))
                listePartenaireEnCours.add(theseLiteMapper.theseLiteToDto(theseHit));
            else listePartenaire.add(theseLiteMapper.theseLiteToDto(theseHit));
        }
        thesesByOrganismeResponse.setPartenaireRecherche(listePartenaire);
        thesesByOrganismeResponse.setPartenaireRechercheEnCours(listePartenaireEnCours);

        SearchResponse<These> responseEcole = ElasticClient.getElasticsearchClient().search(
                s -> s
                        .index(esIndexName)
                        .query(q -> q
                                .match(t -> t
                                        .query(ppn)
                                        .field("ecolesDoctoralesPpn"))),
                These.class
        );
        List<TheseLiteResponseDto> listeEcoleDoctorale = new ArrayList<>();
        List<TheseLiteResponseDto> listeEcoleDoctoraleEnCours = new ArrayList<>();
        iterator = responseEcole.hits().hits().iterator();
        while (iterator.hasNext()) {
            Hit<These> theseHit = iterator.next();
            TheseLiteResponseDto theseListeDto = theseLiteMapper.theseLiteToDto(theseHit);
            if (theseListeDto.getStatus().equals("enCours"))
                listeEcoleDoctoraleEnCours.add(theseLiteMapper.theseLiteToDto(theseHit));
            else listeEcoleDoctorale.add(theseLiteMapper.theseLiteToDto(theseHit));
        }
        thesesByOrganismeResponse.setEcoleDoctorale(listeEcoleDoctorale);
        thesesByOrganismeResponse.setEcoleDoctoraleEnCours(listeEcoleDoctoraleEnCours);

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
        if (source.getEtabSoutenancePpn().equals(ppn)) return source.getEtabSoutenanceN();

        //sinon on cherche le PPN dans les etabs de cotutelle, etc.
        for (Organisme e : source.getEtabsCotutelle()) {
            if (e.getPpn().equals(ppn)) return e.getNom();
        }

        for (Organisme e : source.getPartenairesRecherche()) {
            if (e.getPpn().equals(ppn)) return e.getNom();
        }

        for (Organisme e : source.getEcolesDoctorales()) {
            if (e.getPpn().equals(ppn)) return e.getNom();
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
