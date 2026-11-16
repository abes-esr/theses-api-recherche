package fr.abes.thesesapirecherche.theses.service;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.theses.dto.*;
import fr.abes.thesesapirecherche.theses.service.enums.OrgType;
import fr.abes.thesesapirecherche.theses.service.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.theses.converters.BatchTheseMapper;
import fr.abes.thesesapirecherche.theses.converters.ClientTheseMapper;
import fr.abes.thesesapirecherche.theses.converters.TheseLiteMapper;
import fr.abes.thesesapirecherche.theses.converters.TheseMapper;
import fr.abes.thesesapirecherche.theses.dto.batch.BatchResponseTheseDto;
import fr.abes.thesesapirecherche.theses.dto.batch.BatchTheseResponseDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientResponseTheseLiteDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

import static fr.abes.thesesapirecherche.theses.service.CsvWriter.ClientTheseResponseDtoCsvWriter;

@Service
@RequiredArgsConstructor
public class ThesesService {
    private final SearchQueryBuilder searchThesesQuery;

    private final  TheseMapper theseMapper;

    private final  TheseLiteMapper theseLiteMapper;

    private final  ClientTheseMapper clientTheseMapper;

    private final  BatchTheseMapper batchTheseMapper;

    /**
     * Retourne un ensemble de thèses selon les paramètres de la requête
     * 
     * @param chaine chaine de caractères à rechercher
     * @param debut numero de la page à rechercher
     * @param nombre nombre de résultats à retourner
     * @param tri tri à appliquer sur la recherche ES
     * @param filtres filtres à appliquer à la requête ES
     * @return un objet réponse de la recherche au format {@link SearchResponseTheseLiteDto} (retourne plus d'informations que la méthode {@link getThesesClient})
     * @throws Exception si une erreur est survenue
     */
    public SearchResponseTheseLiteDto getTheses(String chaine, Integer debut, Integer nombre, String tri, String filtres) throws Exception {
        SearchResponse<These> response = searchThesesQuery.getThesesSearchResponse(chaine, debut, nombre, tri, filtres);
        
        List<TheseLiteDto> liste = response.hits().hits()
                                                    .stream()
                                                    .map(theseLiteMapper::theseLiteToDto)
                                                    .toList();

        SearchResponseTheseLiteDto res = new SearchResponseTheseLiteDto();

        res.setTheses(liste);
        res.setTook(response.took());
        res.setTotalHits(response.hits().total().value());

        return res;
    }


    /**
     * Retourne un ensemble de thèses selon les paramètres de la requête. 
     * 
     * @param chaine chaine de caractères à rechercher
     * @param debut numero de la page à rechercher
     * @param nombre nombre de résultats à retourner
     * @param tri tri à appliquer sur la recherche ES
     * @param filtres filtres à appliquer à la requête ES
     * @return un objet réponse de la recherche au format {@link ClientResponseTheseLiteDto} (retourne plus d'informations que la méthode {@link getTheses})
     * @throws Exception si une erreur est survenue
     */
    public ClientResponseTheseLiteDto getThesesClient(String chaine, Integer debut, Integer nombre, String tri, String filtres) throws Exception {
        SearchResponse<These> response = searchThesesQuery.getThesesSearchResponse(chaine, debut, nombre, tri, filtres);
        
        ClientResponseTheseLiteDto res = new ClientResponseTheseLiteDto();
        List<ClientTheseLiteResponseDto> liste = new ArrayList<>();

        for(Hit<These> theseHit : response.hits().hits()) {
            liste.add(clientTheseMapper.theseToClientLiteDto(theseHit));
        }

        res.setTheses(liste);
        res.setTook(response.took());
        res.setTotalHits(response.hits().total().value());
        return res;
    }


    /**
     * Retourne un ensemble de thèses selon les paramètres de la requête. 
     * 
     * @param chaine chaine de caractères à rechercher
     * @param debut numero de la page à rechercher
     * @param nombre nombre de résultats à retourner
     * @param tri tri à appliquer sur la recherche ES
     * @param filtres filtres à appliquer à la requête ES
     * @return un objet réponse de la recerche au format DTO (retourne plus d'informations que la méthode getThesesClient)
     * @throws Exception si une erreur est survenue
     */
    public BatchResponseTheseDto getThesesBatch(String chaine, Integer debut, Integer nombre, String tri, String filtres) throws Exception {
        SearchResponse<These> response = searchThesesQuery.getThesesSearchResponse(chaine, debut, nombre, tri, filtres);
        
        BatchResponseTheseDto res = new BatchResponseTheseDto();
        List<BatchTheseResponseDto> liste = new ArrayList<>();

        for(Hit<These> theseHit : response.hits().hits()) {
            liste.add(batchTheseMapper.theseToBatchDto(theseHit));
        }

        res.setTheses(liste);
        res.setTook(response.took());
        res.setTotalHits(response.hits().total().value());
        return res;
    }




    /**
     * Retourne une thèse dans ElasticSearch selon son identifiant au format DTO (utilisée pour le front)
     * 
     * @param id identifiant interne de Elastic Search
     * @return une thèse au format DTO web
     * @throws Exception
     */
    public TheseDto getThese(String id) throws Exception{
        return theseMapper.theseToDto(searchThesesQuery.getTheseById(id));
    }

    /**
     * Retourne une thèse dans ElasticSearch selon son identifiant au format DTO (utilisée pour les utilisateurs qui consomment l'api directement)
     * 
     * @param id
     * @return une thèse au format DTO web (plus d'informations qu'avec la méthode getThese)
     * @throws Exception
     */
    public ClientTheseResponseDto getTheseClient(String id) throws Exception{
        return clientTheseMapper.theseToClientDto(searchThesesQuery.getTheseById(id));
    }

    private record OrgSearchResult<T>(
            List<T> soutenues,
            List<T> enCours,
            long totalSoutenues,
            long totalEnCours) {
    }

    /**
     * Recherche les thèses liées d'une organisation à l'aide de l'identifiant
     * ppn (PICA Production Number) de l'organisation et le rôle de celle-ci
     *
     * @param ppn l'id de l'organisation
     * @param orgType le rôle de l'organisation par rapport aux thèses (ex : partenaire de recherche)
     * @param mapper fonction de conversion entre un type Hit<These> et un type quelconque
     * @return un record encapsulant une liste (limitée à 100 chacune) des thèses soutenues, en cours
     * ainsi que la quantité de résultat total trouvé par le moteur de recherche.
     * @param <T> un type quelconque
     */
    private <T> OrgSearchResult<T> loadThesesFor(String ppn, OrgType orgType, Function<Hit<These>,T> mapper) {
        BiFunction<String, String, SearchResponse<These>> finder = finderFor(orgType);
        SearchResponse<These> soutenues = finder.apply(ppn, Status.SOUTENUE.val);
        SearchResponse<These> enCours   = finder.apply(ppn, Status.EN_COURS.val);

        //noinspection DataFlowIssue
        return new OrgSearchResult<>(
                soutenues.hits().hits().stream().map(mapper).toList(),
                enCours.hits().hits().stream().map(mapper).toList(),
                soutenues.hits().total().value(),
                enCours.hits().total().value()
        );
    }

    /**
     * Décide de la fonction adéquate a utilisé pour effectuer une recherche selon le type de l'organisme.
     *
     * @param orgType le type de l'organisme
     * @return la fonction a appelé.
     */
    private BiFunction<String, String, SearchResponse<These>> finderFor(OrgType orgType) {
        return switch (orgType) {
            case ETABLISSEMENT_SOUTENANCE -> searchThesesQuery::searchEtabSoutenanceByPpnAndStatus;
            case PARTENAIRE_RECHERCHE     -> searchThesesQuery::searchPartenaireRechercheByPpnAndStatus;
            case ETABLISSEMENT_COTUTELLE  -> searchThesesQuery::searchEtabCotutelleByPpnAndStatus;
            case ECOLE_DOCTORALE          -> searchThesesQuery::searchEcolesDoctoralesByPpnAndStatus;
        };
    }

    /**
     * Retourne les thèses associées à un organisme, à destination du front.
     * <p>
     * Les thèses sont regroupées selon le rôle de l'organisme : établissement de
     * soutenance, partenaire de recherche, école doctorale ou cotutelle. La réponse
     * inclut également le nombre total de thèses soutenues et le nombre de thèses
     * actuellement en cours.
     *
     * @param ppn identifiant PPN ou Pica Production Number de l'organisme
     * @return un {@link ThesesByOrganismeDto} contenant les thèses et les totaux groupés par rôle et status
     */
    public ThesesByOrganismeDto<TheseLiteDto> getThesesSummariesByOrganisme(String ppn) {
        return getThesesGenericByOrganisme(ppn, theseLiteMapper::theseLiteToDto);
    }

    private <T> ThesesByOrganismeDto<T> getThesesGenericByOrganisme(String ppn, Function<Hit<These>, T> mapper) {
        ThesesByOrganismeDto<T> resDto = new ThesesByOrganismeDto<>();

        OrgSearchResult<T> soutenance = loadThesesFor(ppn, OrgType.ETABLISSEMENT_SOUTENANCE, mapper);
        resDto.setEtabSoutenance(soutenance.soutenues())
                .setEtabSoutenanceEnCours(soutenance.enCours())
                .setTotalHitsetabSoutenance(soutenance.totalSoutenues())
                .setTotalHitsetabSoutenanceEnCours(soutenance.totalEnCours());

        OrgSearchResult<T> cotutelle = loadThesesFor(ppn, OrgType.ETABLISSEMENT_COTUTELLE, mapper);
        resDto.setEtabCotutelle(cotutelle.soutenues())
                .setEtabCotutelleEnCours(cotutelle.enCours())
                .setTotalHitsetabCotutelle(cotutelle.totalSoutenues())
                .setTotalHitsetabCotutelleEnCours(cotutelle.totalEnCours());

        OrgSearchResult<T> partenaire = loadThesesFor(ppn, OrgType.PARTENAIRE_RECHERCHE, mapper);
        resDto.setPartenaireRecherche(partenaire.soutenues())
                .setPartenaireRechercheEnCours(partenaire.enCours())
                .setTotalHitspartenaireRecherche(partenaire.totalSoutenues())
                .setTotalHitspartenaireRechercheEnCours(partenaire.totalEnCours());

        OrgSearchResult<T> ecoleDoctorale = loadThesesFor(ppn, OrgType.ECOLE_DOCTORALE, mapper);
        resDto.setEcoleDoctorale(ecoleDoctorale.soutenues())
                .setEcoleDoctoraleEnCours(ecoleDoctorale.enCours())
                .setTotalHitsecoleDoctorale(ecoleDoctorale.totalSoutenues())
                .setTotalHitsecoleDoctoraleEnCours(ecoleDoctorale.totalEnCours());

        return resDto;

    }

    /**
     * Retourne les thèses associées à un organisme, à destination d'un client quelconque. La réponse est donc plus fournis.
     * <p>
     * Les thèses sont regroupées selon le rôle de l'organisme : établissement de
     * soutenance, partenaire de recherche, école doctorale ou cotutelle. La réponse
     * inclut également le nombre total de thèses soutenues et le nombre de thèses
     * actuellement en cours.
     *
     * @param ppn identifiant PPN de l'organisme
     * @return un {@link ThesesByOrganismeDto} contenant les thèses au format {@link ClientTheseLiteResponseDto} et les totaux groupés par rôle et status (thèses déjà soutenues et thèses en cours)
     */
    public ThesesByOrganismeDto<ClientTheseLiteResponseDto> getThesesDetailsByOrganisme(String ppn) {
        return getThesesGenericByOrganisme(ppn, clientTheseMapper::theseToClientLiteDto);
    }

    public long getStatsTheses(String status) throws Exception {
        return searchThesesQuery.getStatsTheses(status);
    }

    public List<Facet> facets(String query, String filtres) throws Exception {
        return searchThesesQuery.facets(query, filtres);
    }

    public List<String> completion(String q) throws Exception {
        return searchThesesQuery.completion(q);
    }

    public String getOrganismeName(String ppn) throws Exception {
        return searchThesesQuery.getOrganismeName(ppn);
    }

    public String getSimpleToFullTheseCsvStringFormat(String query, int debut, int nombre, String tri, String filtres) throws Exception {
        List<ClientTheseResponseDto> res = searchThesesQuery.simpleToFullThese(query, debut, nombre, tri, filtres);

        return ClientTheseResponseDtoCsvWriter.writeToCsvFrom(res);
    }
}