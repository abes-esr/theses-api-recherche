package fr.abes.thesesapirecherche.theses.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import fr.abes.thesesapirecherche.exception.DocumentNotFoundException;
import fr.abes.thesesapirecherche.theses.repository.TheseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.theses.converters.BatchTheseMapper;
import fr.abes.thesesapirecherche.theses.converters.ClientTheseMapper;
import fr.abes.thesesapirecherche.theses.converters.TheseLiteMapper;
import fr.abes.thesesapirecherche.theses.converters.TheseMapper;
import fr.abes.thesesapirecherche.theses.dto.ResponseTheseLiteDto;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.theses.dto.ThesesByOrganismeResponseDto;
import fr.abes.thesesapirecherche.theses.dto.batch.BatchResponseTheseDto;
import fr.abes.thesesapirecherche.theses.dto.batch.BatchTheseResponseDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientResponseTheseLiteDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientThesesByOrganismeResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

@Service
public class ThesesService {
    @Autowired
    private SearchQueryBuilder searchThesesQuery;

    @Autowired
    @Qualifier("TheseTheseMapper")
    private TheseMapper theseMapper;

    @Autowired
    private TheseLiteMapper theseLiteMapper;

    @Autowired
    @Qualifier("TheseClientTheseMapper")
    private ClientTheseMapper clientTheseMapper;

    @Autowired
    private BatchTheseMapper batchTheseMapper;

    @Autowired
    private TheseRepository theseRepository;

    private static final String STATUS_EN_COURS = "enCours";
    private static final String STATUS_SOUTENUE = "soutenue";

    /**
     * Retourne un ensemble de thèses selon les paramètres de la requête
     * 
     * @param chaine chaine de caractères à rechercher
     * @param debut numero de la page à rechercher
     * @param nombre nombre de résultats à retourner
     * @param tri tri à appliquer sur la recherche ES
     * @param filtres filtres à appliquer à la requête ES
     * @return un objet réponse de la recerche au format DTO
     * @throws Exception si une erreur est survenue
     */
    public ResponseTheseLiteDto getTheses(String chaine, Integer debut, Integer nombre, String tri, String filtres) throws Exception {
        SearchResponse<These> response = searchThesesQuery.getThesesSearchResponse(chaine, debut, nombre, tri, filtres);
        
        List<TheseLiteResponseDto> liste = response.hits().hits()
                                                    .stream()
                                                    .map(t-> theseLiteMapper.theseLiteToDto(t))
                                                    .toList();

        ResponseTheseLiteDto res = new ResponseTheseLiteDto();

        res.setTheses(liste);
        res.setTook(response.took());
        res.setTotalHits(response.hits().total().value());

        return res;
    }

    public ResponseTheseLiteDto recherche(String chaine, Integer debut, Integer nombre, String tri, String filtres) throws Exception {
        SearchResponse<These> response = theseRepository.rechercher(chaine, debut, nombre, tri, filtres);

        List<TheseLiteResponseDto> liste = response.hits().hits()
                .stream()
                .map(t-> theseLiteMapper.theseLiteToDto(t))
                .toList();

        ResponseTheseLiteDto res = new ResponseTheseLiteDto();

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
     * @return un objet réponse de la recerche au format DTO (retourne plus d'informations que la méthode getTheses)
     * @throws Exception si une erreur est survenue
     */
    public ClientResponseTheseLiteDto getThesesClient(String chaine, Integer debut, Integer nombre, String tri, String filtres) throws Exception {
        // TODO FIXME passer par repository
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

    public ClientResponseTheseLiteDto rechercheTheseDetail(String chaine, int debut, int nombre, String tri, String filtres) throws Exception {
        // TODO FIXME passer par repository
        SearchResponse<These> response = theseRepository.rechercher(chaine, debut, nombre, tri, filtres);

        ClientResponseTheseLiteDto res = new ClientResponseTheseLiteDto();
        List<ClientTheseLiteResponseDto> listeThese = response.hits().hits().stream()
                .map(uneThese -> clientTheseMapper.theseToClientLiteDto(uneThese))
                .toList();

        res.setTheses(listeThese);
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
    public TheseResponseDto getThese(String id) throws Exception { // TODO supprimer cette fonction et la remplacer par celle en dessous getTheseNewAndImproved()
        return theseMapper.theseToDto(searchThesesQuery.getTheseById(id));
    }

    /**
     * Retourne une thèse dans ElasticSearch selon son identifiant au format DTO (utilisée pour le front)
     *
     * @param theseId identifiant interne de Elastic Search
     * @return une thèse au format DTO web
     * @throws DocumentNotFoundException si aucune thèse ne correspond
     */
    public TheseResponseDto getTheseNewAndImproved(String theseId) {
        Optional<These> optThese = theseRepository.findById(theseId);
        return theseMapper.theseToDtoCustomTotoToto(optThese.orElseThrow(() -> new DocumentNotFoundException("La these "+ theseId + " n'a pas été trouvé")));
    }

    /**
     * Retourne une thèse dans ElasticSearch selon son identifiant au format DTO (utilisée pour les utilisateurs qui consomment l'api directement)
     * 
     * @param id
     * @return une thèse au format DTO web (plus d'informations qu'avec la méthode getThese)
     * @throws Exception
     */
    public ClientTheseResponseDto getTheseClient(String id) throws Exception {
        return clientTheseMapper.theseToClientDto(searchThesesQuery.getTheseById(id));
    }

    public ClientTheseResponseDto getTheseClientToto(String theseId) throws Exception{
        Optional<These> optThese = theseRepository.findById(theseId);
        return clientTheseMapper.theseToClientDto(optThese.orElseThrow(() -> new DocumentNotFoundException("La these "+ theseId + " n'a pas été trouvé")));
    }











    //TODO les 2 fonctions se ressemblent énormément, refactoring à faire

    /** Retourne toutes les thèses associées à un organisme (utilisé pour le front)
     * 
     * @param ppn l'identifiant ppn de l'organisme
     * @return les listes de thèses soutenues et en cours
     * @throws IOException
     */
    public ThesesByOrganismeResponseDto getThesesByOrganisme(String ppn) throws IOException{
        ThesesByOrganismeResponseDto thesesByOrganismeResponse = new ThesesByOrganismeResponseDto();

        // on interroge ES sur tous les index
        SearchResponse<These> responseEtabSoutenanceEnCours = searchThesesQuery.searchByPpn("etabSoutenancePpn", ppn, "enCours");
        SearchResponse<These> responseEtabSoutenanceSoutenue = searchThesesQuery.searchByPpn("etabSoutenancePpn", ppn, "soutenue");
        SearchResponse<These> responseEtabCotutelleEnCours = searchThesesQuery.searchByPpn("etabsCotutellePpn", ppn, "enCours");
        SearchResponse<These> responseEtabCotutelleSoutenue = searchThesesQuery.searchByPpn("etabsCotutellePpn", ppn, "soutenue");
        SearchResponse<These> responsePartenaireEnCours = searchThesesQuery.searchByPpn("partenairesRecherchePpn", ppn, "enCours");
        SearchResponse<These> responsePartenaireSoutenue = searchThesesQuery.searchByPpn("partenairesRecherchePpn", ppn, "soutenue");
        SearchResponse<These> responseEcoleEnCours = searchThesesQuery.searchByPpn("ecolesDoctoralesPpn", ppn, "enCours");
        SearchResponse<These> responseEcoleSoutenue = searchThesesQuery.searchByPpn("ecolesDoctoralesPpn", ppn, "soutenue");
        

        List<TheseLiteResponseDto> listeEtabSoutenance = new ArrayList<>();
        List<TheseLiteResponseDto> listeEtabSoutenanceEnCours = new ArrayList<>();
        List<TheseLiteResponseDto> listeEtabCotutelle = new ArrayList<>();
        List<TheseLiteResponseDto> listeEtabCotutelleEnCours = new ArrayList<>();
        List<TheseLiteResponseDto> listePartenaire = new ArrayList<>();
        List<TheseLiteResponseDto> listePartenaireEnCours = new ArrayList<>();
        List<TheseLiteResponseDto> listeEcoleDoctorale = new ArrayList<>();
        List<TheseLiteResponseDto> listeEcoleDoctoraleEnCours = new ArrayList<>();


        for(Hit<These> theseHit : responseEtabSoutenanceSoutenue.hits().hits()) listeEtabSoutenance.add(theseLiteMapper.theseLiteToDto(theseHit));
        for(Hit<These> theseHit : responseEtabSoutenanceEnCours.hits().hits()) listeEtabSoutenanceEnCours.add(theseLiteMapper.theseLiteToDto(theseHit));
        for(Hit<These> theseHit : responseEtabCotutelleSoutenue.hits().hits()) listeEtabCotutelle.add(theseLiteMapper.theseLiteToDto(theseHit));
        for(Hit<These> theseHit : responseEtabCotutelleEnCours.hits().hits()) listeEtabCotutelleEnCours.add(theseLiteMapper.theseLiteToDto(theseHit));
        for(Hit<These> theseHit : responsePartenaireSoutenue.hits().hits()) listePartenaire.add(theseLiteMapper.theseLiteToDto(theseHit));
        for(Hit<These> theseHit : responsePartenaireEnCours.hits().hits()) listePartenaireEnCours.add(theseLiteMapper.theseLiteToDto(theseHit));
        for(Hit<These> theseHit : responseEcoleSoutenue.hits().hits()) listeEcoleDoctorale.add(theseLiteMapper.theseLiteToDto(theseHit));
        for(Hit<These> theseHit : responseEcoleEnCours.hits().hits()) listeEcoleDoctoraleEnCours.add(theseLiteMapper.theseLiteToDto(theseHit));

        
        // on remplit le dto
        thesesByOrganismeResponse.setEtabSoutenance(listeEtabSoutenance);
        thesesByOrganismeResponse.setEtabSoutenanceEnCours(listeEtabSoutenanceEnCours);
        thesesByOrganismeResponse.setTotalHitsetabSoutenanceEnCours(responseEtabSoutenanceEnCours.hits().total().value());
        thesesByOrganismeResponse.setTotalHitsetabSoutenance(responseEtabSoutenanceSoutenue.hits().total().value());

        thesesByOrganismeResponse.setEtabCotutelle(listeEtabCotutelle);
        thesesByOrganismeResponse.setEtabCotutelleEnCours(listeEtabCotutelleEnCours);
        thesesByOrganismeResponse.setTotalHitsetabCotutelleEnCours(responseEtabCotutelleEnCours.hits().total().value());
        thesesByOrganismeResponse.setTotalHitsetabCotutelle(responseEtabCotutelleSoutenue.hits().total().value());

        thesesByOrganismeResponse.setPartenaireRecherche(listePartenaire);
        thesesByOrganismeResponse.setPartenaireRechercheEnCours(listePartenaireEnCours);
        thesesByOrganismeResponse.setTotalHitspartenaireRechercheEnCours(responsePartenaireEnCours.hits().total().value());
        thesesByOrganismeResponse.setTotalHitspartenaireRecherche(responsePartenaireSoutenue.hits().total().value());

        thesesByOrganismeResponse.setEcoleDoctorale(listeEcoleDoctorale);
        thesesByOrganismeResponse.setEcoleDoctoraleEnCours(listeEcoleDoctoraleEnCours);
        thesesByOrganismeResponse.setTotalHitsecoleDoctoraleEnCours(responseEcoleEnCours.hits().total().value());
        thesesByOrganismeResponse.setTotalHitsecoleDoctorale(responseEcoleSoutenue.hits().total().value());


        return thesesByOrganismeResponse;
    }

    private record OrgSearchResult<T>(
            List<T> soutenues,
            List<T> enCours,
            int totalSoutenues,
            int totalEnCours) {
    }

    private <T> OrgSearchResult<T> load(String ppn,BiFunction<String, String, List<These>> finder, Function<List<These>,List<T>> mapper) {
        List<These> soutenues = finder.apply(ppn, STATUS_SOUTENUE);
        List<These> enCours   = finder.apply(ppn, STATUS_EN_COURS);
        return new OrgSearchResult<>(
                mapper.apply(soutenues),
                mapper.apply(enCours),
                soutenues.size(),
                enCours.size());
    }


    public ClientThesesByOrganismeResponseDto getThesesByOrganismeClientToto(String ppn) {
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

    /** Retourne toutes les thèses associées à un organisme (utilisée pour les utilisateurs qui consomment l'api directement)
     * 
     * @param ppn l'identifiant ppn de l'organisme
     * @return les listes de thèses soutenues et en cours
     * @throws IOException
     */
    public ClientThesesByOrganismeResponseDto getThesesByOrganismeClient(String ppn) throws IOException{
        ClientThesesByOrganismeResponseDto thesesByOrganismeResponse = new ClientThesesByOrganismeResponseDto();

        // on interroge ES sur tous les index
        SearchResponse<These> responseEtabSoutenanceEnCours = searchThesesQuery.searchByPpn("etabSoutenancePpn", ppn, "enCours");
        SearchResponse<These> responseEtabSoutenanceSoutenue = searchThesesQuery.searchByPpn("etabSoutenancePpn", ppn, "soutenue");
        SearchResponse<These> responseEtabCotutelleEnCours = searchThesesQuery.searchByPpn("etabsCotutellePpn", ppn, "enCours");
        SearchResponse<These> responseEtabCotutelleSoutenue = searchThesesQuery.searchByPpn("etabsCotutellePpn", ppn, "soutenue");
        SearchResponse<These> responsePartenaireEnCours = searchThesesQuery.searchByPpn("partenairesRecherchePpn", ppn, "enCours");
        SearchResponse<These> responsePartenaireSoutenue = searchThesesQuery.searchByPpn("partenairesRecherchePpn", ppn, "soutenue");
        SearchResponse<These> responseEcoleEnCours = searchThesesQuery.searchByPpn("ecolesDoctoralesPpn", ppn, "enCours");
        SearchResponse<These> responseEcoleSoutenue = searchThesesQuery.searchByPpn("ecolesDoctoralesPpn", ppn, "soutenue");
        

        List<ClientTheseLiteResponseDto> listeEtabSoutenance = new ArrayList<>();
        List<ClientTheseLiteResponseDto> listeEtabSoutenanceEnCours = new ArrayList<>();
        List<ClientTheseLiteResponseDto> listeEtabCotutelle = new ArrayList<>();
        List<ClientTheseLiteResponseDto> listeEtabCotutelleEnCours = new ArrayList<>();
        List<ClientTheseLiteResponseDto> listePartenaire = new ArrayList<>();
        List<ClientTheseLiteResponseDto> listePartenaireEnCours = new ArrayList<>();
        List<ClientTheseLiteResponseDto> listeEcoleDoctorale = new ArrayList<>();
        List<ClientTheseLiteResponseDto> listeEcoleDoctoraleEnCours = new ArrayList<>();


        for(Hit<These> theseHit : responseEtabSoutenanceSoutenue.hits().hits()) listeEtabSoutenance.add(clientTheseMapper.theseToClientLiteDto(theseHit));
        for(Hit<These> theseHit : responseEtabSoutenanceEnCours.hits().hits()) listeEtabSoutenanceEnCours.add(clientTheseMapper.theseToClientLiteDto(theseHit));
        for(Hit<These> theseHit : responseEtabCotutelleSoutenue.hits().hits()) listeEtabCotutelle.add(clientTheseMapper.theseToClientLiteDto(theseHit));
        for(Hit<These> theseHit : responseEtabCotutelleEnCours.hits().hits()) listeEtabCotutelleEnCours.add(clientTheseMapper.theseToClientLiteDto(theseHit));
        for(Hit<These> theseHit : responsePartenaireSoutenue.hits().hits()) listePartenaire.add(clientTheseMapper.theseToClientLiteDto(theseHit));
        for(Hit<These> theseHit : responsePartenaireEnCours.hits().hits()) listePartenaireEnCours.add(clientTheseMapper.theseToClientLiteDto(theseHit));
        for(Hit<These> theseHit : responseEcoleSoutenue.hits().hits()) listeEcoleDoctorale.add(clientTheseMapper.theseToClientLiteDto(theseHit));
        for(Hit<These> theseHit : responseEcoleEnCours.hits().hits()) listeEcoleDoctoraleEnCours.add(clientTheseMapper.theseToClientLiteDto(theseHit));
        
        // on remplit le dto
        thesesByOrganismeResponse.setEtabSoutenance(listeEtabSoutenance);
        thesesByOrganismeResponse.setEtabSoutenanceEnCours(listeEtabSoutenanceEnCours);
        thesesByOrganismeResponse.setTotalHitsetabSoutenanceEnCours(responseEtabSoutenanceEnCours.hits().total().value());
        thesesByOrganismeResponse.setTotalHitsetabSoutenance(responseEtabSoutenanceSoutenue.hits().total().value());

        thesesByOrganismeResponse.setEtabCotutelle(listeEtabCotutelle);
        thesesByOrganismeResponse.setEtabCotutelleEnCours(listeEtabCotutelleEnCours);
        thesesByOrganismeResponse.setTotalHitsetabCotutelleEnCours(responseEtabCotutelleEnCours.hits().total().value());
        thesesByOrganismeResponse.setTotalHitsetabCotutelle(responseEtabCotutelleSoutenue.hits().total().value());

        thesesByOrganismeResponse.setPartenaireRecherche(listePartenaire);
        thesesByOrganismeResponse.setPartenaireRechercheEnCours(listePartenaireEnCours);
        thesesByOrganismeResponse.setTotalHitspartenaireRechercheEnCours(responsePartenaireEnCours.hits().total().value());
        thesesByOrganismeResponse.setTotalHitspartenaireRecherche(responsePartenaireSoutenue.hits().total().value());

        thesesByOrganismeResponse.setEcoleDoctorale(listeEcoleDoctorale);
        thesesByOrganismeResponse.setEcoleDoctoraleEnCours(listeEcoleDoctoraleEnCours);
        thesesByOrganismeResponse.setTotalHitsecoleDoctoraleEnCours(responseEcoleEnCours.hits().total().value());
        thesesByOrganismeResponse.setTotalHitsecoleDoctorale(responseEcoleSoutenue.hits().total().value());

        return thesesByOrganismeResponse;
    }
}