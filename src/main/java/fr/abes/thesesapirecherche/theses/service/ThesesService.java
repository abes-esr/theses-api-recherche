package fr.abes.thesesapirecherche.theses.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    
    // récupère un ensemble de thèses (pour le front)
    public ResponseTheseLiteDto getTheses(String chaine, Integer debut, Integer nombre, String tri, String filtres) throws Exception {
        SearchResponse<These> response = searchThesesQuery.getThesesSearchResponse(chaine, debut, nombre, tri, filtres);
        
        ResponseTheseLiteDto res = new ResponseTheseLiteDto();
        List<TheseLiteResponseDto> liste = new ArrayList<>();

        for(Hit<These> theseHit : response.hits().hits()) {
            liste.add(theseLiteMapper.theseLiteToDto(theseHit));
        }

        res.setTheses(liste);
        res.setTook(response.took());
        res.setTotalHits(response.hits().total().value());
        return res;
    }


    // récupère un ensemble de thèses (pour le client)
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


    // récupère un ensemble de thèses (pour le batch)
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




    // récupère une thèse (pour le front)
    public TheseResponseDto getThese(String id) throws Exception{
        return theseMapper.theseToDto(searchThesesQuery.getTheseById(id));
    }

    // récupère une thèse (pour le front)
    public ClientTheseResponseDto getTheseClient(String id) throws Exception{
        return clientTheseMapper.theseToClientDto(searchThesesQuery.getTheseById(id));
    }











    //TODO les 2 fonctions se ressemblent énormément, refactoring à faire

    //récupère les thèses selon un organisme (pour le front)
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


    
    //récupère les thèses selon un organisme (pour les clients)
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