package fr.abes.thesesapirecherche.theses.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.exception.DocumentNotFoundException;
import fr.abes.thesesapirecherche.theses.dto.*;
import fr.abes.thesesapirecherche.theses.repository.Status;
import fr.abes.thesesapirecherche.theses.repository.TheseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.converters.BatchTheseMapper;
import fr.abes.thesesapirecherche.theses.converters.ClientTheseMapper;
import fr.abes.thesesapirecherche.theses.converters.TheseLiteMapper;
import fr.abes.thesesapirecherche.theses.converters.TheseMapper;
import fr.abes.thesesapirecherche.theses.dto.batch.BatchResponseTheseDto;
import fr.abes.thesesapirecherche.theses.dto.batch.BatchTheseResponseDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientResponseTheseLiteDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientThesesByOrganismeResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

@Service
@AllArgsConstructor
public class ThesesService {

    private final TheseMapper theseMapper;

    private final TheseLiteMapper theseLiteMapper;

    private final ClientTheseMapper clientTheseMapper;

    private final BatchTheseMapper batchTheseMapper;

    private final TheseRepository theseRepository;

    /**
     * Recherche les thèses selon les paramètres de la requête en incluant les facets dans la requête
     *
     * @param chaine chaine de caractères à rechercher
     * @param debut numero de la page à rechercher
     * @param nombre nombre de résultats à retourner
     * @param tri tri à appliquer sur la recherche ES
     * @param filtres filtres à appliquer à la requête ES
     * @return un objet réponse de la recherche au format DTO (contient plus d'informations que la méthode {@link rechercheTheseDetail}).
     */
    public ResponseTheseLiteDto rechercheAvecFacets(String chaine, Integer debut, Integer nombre, String tri, String filtres) {
        SearchResponse<These> response = theseRepository.rechercherAvecFacets(chaine, debut, nombre, tri, filtres);

        List<TheseLiteResponseDto> liste = response.hits().hits()
                .stream()
                .map(t-> theseLiteMapper.theseLiteToDto(Objects.requireNonNull(t.source())))
                .toList();

        ResponseTheseLiteDto res = new ResponseTheseLiteDto();

        res.setTheses(liste);
        res.setTook(response.took());
        res.setTotalHits(Objects.requireNonNull(response.hits().total()).value());

        return res;
    }


    /**
     * Recherche les thèses selon les paramètres de la requête.
     * 
     * @param chaine chaine de caractères à rechercher
     * @param debut numero de la page à rechercher
     * @param nombre nombre de résultats à retourner
     * @param tri tri à appliquer sur la recherche ES
     * @param filtres filtres à appliquer à la requête ES
     * @return un objet réponse de la recherche au format DTO (contient plus d'informations que la méthode {@link rechercheAvecFacets}).
     */
    public ClientResponseTheseLiteDto rechercheTheseDetail(String chaine, int debut, int nombre, String tri, String filtres) {
        SearchResponse<These> response = theseRepository.rechercherAvecFacets(chaine, debut, nombre, tri, filtres);

        ClientResponseTheseLiteDto res = new ClientResponseTheseLiteDto();
        List<ClientTheseLiteResponseDto> listeThese = response.hits().hits().stream()
                .map(uneThese -> clientTheseMapper.theseToClientLiteDto(Objects.requireNonNull(uneThese.source())))
                .toList();

        res.setTheses(listeThese);
        res.setTook(response.took());
        assert response.hits().total() != null;
        res.setTotalHits(response.hits().total().value());
        return res;
    }


    /**
     * Recherche les thèses selon les paramètres de la requête et renvoie une réponse complète, qui contient une grande quantité d'information
     * 
     * @param chaine chaine de caractères à rechercher
     * @param debut numero de la page à rechercher
     * @param nombre nombre de résultats à retourner
     * @param tri tri à appliquer sur la recherche ES
     * @param filtres filtres à appliquer à la requête ES
     * @return un objet réponse de la recherche au format DTO (retourne plus d'informations que la méthode {@link rechercheAvecFacets})
     */
    public BatchResponseTheseDto getThesesBatch(String chaine, Integer debut, Integer nombre, String tri, String filtres) {
        SearchResponse<These> response = theseRepository.rechercherAvecFacets(chaine, debut, nombre, tri, filtres);
        
        BatchResponseTheseDto res = new BatchResponseTheseDto();

        List<BatchTheseResponseDto> liste = response.hits().hits().stream()
                .map(Hit::source)
                .map(batchTheseMapper::theseToBatchDtoSingle)
                .toList();

        res.setTheses(liste);
        res.setTook(response.took());
        assert response.hits().total() != null;
        res.setTotalHits(response.hits().total().value());
        return res;
    }

    /**
     * Retourne une thèse dans ElasticSearch selon son identifiant au format DTO (utilisée pour le front)
     *
     * @param theseId identifiant interne de Elasticsearch
     * @return une thèse au format DTO web (moins d'informations qu'avec la méthode {@link getTheseClientToClient})
     * @throws DocumentNotFoundException si aucune thèse ne correspond
     */
    public TheseResponseDto getThese(String theseId) {
        Optional<These> optThese = theseRepository.findById(theseId);
        return theseMapper.theseToDto(optThese.orElseThrow(() -> new DocumentNotFoundException("La these "+ theseId + " n'a pas été trouvé")));
    }

    /**
     * Retourne une thèse dans ElasticSearch selon son identifiant au format DTO (utilisée pour les utilisateurs qui consomment l'api directement)
     * 
     * @param theseId identifiant interne de Elasticsearch
     * @return une thèse au format DTO web (plus d'informations qu'avec la méthode {@link getThese})
     * @throws DocumentNotFoundException si aucune thèse ne correspond
     */
    public ClientTheseResponseDto getTheseClientToClient(String theseId) {
        Optional<These> optThese = theseRepository.findById(theseId);
        return clientTheseMapper.theseToClientDto(optThese.orElseThrow(() -> new DocumentNotFoundException("La these "+ theseId + " n'a pas été trouvé")));
    }

    /**
     * Trouve le nom de l'organisme ayant l'identifiant ppn.
     * @param ppn PICA production number
     * @return nom de l'organisme si c'est un identifiant d'organisme, chaine vide sinon
     */
    public String getOrganismeName(String ppn) {
        return theseRepository.rechercherOrganisme(ppn);
    }

    /**
     * Recherche les thèses selon les paramètres de la requête.
     *
     * @param chaine chaine de caractères à rechercher
     * @param debut numero de la page à rechercher
     * @param nombre nombre de résultats à retourner
     * @param tri tri à appliquer sur la recherche ES
     * @param filtres filtres à appliquer à la requête ES
     * @return un String au format CSV séparés par des points virgules ; - voir {@link CsvWriter} pour le format précis.
     */
    public String rechercheToCsv(String chaine, Integer debut, Integer nombre, String tri, String filtres) {
        SearchResponse<These> response = theseRepository.rechercherAvecFacets(chaine, debut, nombre, tri, filtres);

        List<ClientTheseResponseDto> liste = response.hits().hits()
                .stream()
                .map(t-> clientTheseMapper.theseToClientDto(Objects.requireNonNull(t.source())))
                .toList();

        return CsvWriter.ClientTheseResponseDtoCsvWriter.write(liste);
    }

    /**
     * Retourne les thèses associées à un organisme, à destination du front.
     * <p>
     * Les thèses sont regroupées selon le rôle de l'organisme : établissement de
     * soutenance, partenaire de recherche, école doctorale ou cotutelle. La réponse
     * inclut également le nombre total de thèses soutenues et le nombre de thèses
     * actuellement en cours.
     *
     * @param ppn identifiant PPN de l'organisme
     * @return un {@link ThesesByOrganismeResponseDto} contenant les thèses par rôle et les compteurs de soutenues et en cours
     */
    public ThesesByOrganismeResponseDto getTheseSummariesByOrganisme(String ppn) {
        ThesesByOrganismeResponseDto resDto = new ThesesByOrganismeResponseDto();

        OrgSearchResult<TheseLiteResponseDto> soutenance = load(
                ppn,
                theseRepository::findByEtabSoutenancePpnAndStatus,
                theseLiteMapper::theseLiteToDtoNoHitList);
        resDto.setEtabSoutenance(soutenance.soutenues())
                .setEtabSoutenanceEnCours(soutenance.enCours())
                .setTotalHitsetabSoutenance(soutenance.totalSoutenues())
                .setTotalHitsetabSoutenanceEnCours(soutenance.totalEnCours());

        OrgSearchResult<TheseLiteResponseDto> cotutelle = load( ppn,
                theseRepository::findByEtabsCotutellePpnAndStatus,
                theseLiteMapper::theseLiteToDtoNoHitList);
        resDto.setEtabCotutelle(cotutelle.soutenues())
                .setEtabCotutelleEnCours(cotutelle.enCours())
                .setTotalHitsetabCotutelle(cotutelle.totalSoutenues())
                .setTotalHitsetabCotutelleEnCours(cotutelle.totalEnCours());

        OrgSearchResult<TheseLiteResponseDto> partenaire = load(ppn,
                theseRepository::findByPartenairesRecherchePpnAndStatus,
                theseLiteMapper::theseLiteToDtoNoHitList);
        resDto.setPartenaireRecherche(partenaire.soutenues())
                .setPartenaireRechercheEnCours(partenaire.enCours())
                .setTotalHitspartenaireRecherche(partenaire.totalSoutenues())
                .setTotalHitspartenaireRechercheEnCours(partenaire.totalEnCours());

        OrgSearchResult<TheseLiteResponseDto> ecoleDoctorale = load(
                ppn,
                theseRepository::findByEcolesDoctoralesPpnAndStatus,
                theseLiteMapper::theseLiteToDtoNoHitList);
        resDto.setEcoleDoctorale(ecoleDoctorale.soutenues())
                .setEcoleDoctoraleEnCours(ecoleDoctorale.enCours())
                .setTotalHitsecoleDoctorale(ecoleDoctorale.totalSoutenues())
                .setTotalHitsecoleDoctoraleEnCours(ecoleDoctorale.totalEnCours());


        return resDto;
    }

    private record OrgSearchResult<T>(
            List<T> soutenues,
            List<T> enCours,
            int totalSoutenues,
            int totalEnCours) {
    }

    private <T> OrgSearchResult<T> load(String ppn,BiFunction<String, String, List<These>> finder, Function<List<These>,List<T>> mapper) {
        List<These> soutenues = finder.apply(ppn, Status.SOUTENUE.val);
        List<These> enCours   = finder.apply(ppn, Status.EN_COURS.val);
        return new OrgSearchResult<>(
                mapper.apply(soutenues),
                mapper.apply(enCours),
                soutenues.size(),
                enCours.size());
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
     * @return un {@link ThesesByOrganismeResponseDto} contenant les thèses par rôle et les compteurs de soutenues et en cours
     */
    public ClientThesesByOrganismeResponseDto getTheseDetailsByOrganisme(String ppn) {
        ClientThesesByOrganismeResponseDto resDto = new ClientThesesByOrganismeResponseDto();


        OrgSearchResult<ClientTheseLiteResponseDto> soutenance = load(
                ppn,
                theseRepository::findByEtabSoutenancePpnAndStatus,
                clientTheseMapper::theseToClientLiteDto);
        resDto.setEtabSoutenance(soutenance.soutenues())
                .setEtabSoutenanceEnCours(soutenance.enCours())
                .setTotalHitsetabSoutenance(soutenance.totalSoutenues())
                .setTotalHitsetabSoutenanceEnCours(soutenance.totalEnCours());

        OrgSearchResult<ClientTheseLiteResponseDto> cotutelle = load( ppn,
                theseRepository::findByEtabsCotutellePpnAndStatus,
                clientTheseMapper::theseToClientLiteDto);
        resDto.setEtabCotutelle(cotutelle.soutenues())
                .setEtabCotutelleEnCours(cotutelle.enCours())
                .setTotalHitsetabCotutelle(cotutelle.totalSoutenues())
                .setTotalHitsetabCotutelleEnCours(cotutelle.totalEnCours());

        OrgSearchResult<ClientTheseLiteResponseDto> partenaire = load(ppn,
                theseRepository::findByPartenairesRecherchePpnAndStatus,
                clientTheseMapper::theseToClientLiteDto);
        resDto.setPartenaireRecherche(partenaire.soutenues())
                .setPartenaireRechercheEnCours(partenaire.enCours())
                .setTotalHitspartenaireRecherche(partenaire.totalSoutenues())
                .setTotalHitspartenaireRechercheEnCours(partenaire.totalEnCours());

        OrgSearchResult<ClientTheseLiteResponseDto> ecoleDoctorale = load(
                ppn,
                theseRepository::findByEcolesDoctoralesPpnAndStatus,
                clientTheseMapper::theseToClientLiteDto);
        resDto.setEcoleDoctorale(ecoleDoctorale.soutenues())
                .setEcoleDoctoraleEnCours(ecoleDoctorale.enCours())
                .setTotalHitsecoleDoctorale(ecoleDoctorale.totalSoutenues())
                .setTotalHitsecoleDoctoraleEnCours(ecoleDoctorale.totalEnCours());


        return resDto;
    }

    public List<String> completion(String q) throws IOException, ElasticsearchException {
        SearchResponse<Void> response = theseRepository.getSuggestions(q);

        List<String> listeSuggestions = new ArrayList<>();
        response
                .suggest()
                .forEach((key, value) -> value.forEach(s -> s
                        .completion()
                        .options()
                        .forEach(b -> listeSuggestions.add(b.text()))));

        return listeSuggestions;
    }

    public List<Facet> facets(String chaine, String filtres) throws Exception {
        return theseRepository.facets(chaine, filtres);
    }

    public long getStatsTheses(Status status) throws IOException, ElasticsearchException {
        return theseRepository.getStatsByStatus(status);
    }
}