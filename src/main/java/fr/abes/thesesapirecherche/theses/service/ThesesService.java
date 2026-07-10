package fr.abes.thesesapirecherche.theses.service;
import fr.abes.thesesapirecherche.personnes.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.theses.converters.TheseEnhancedMapper;
import fr.abes.thesesapirecherche.theses.dto.OrganismeResponseDto;
import fr.abes.thesesapirecherche.theses.dto.ResponseTheseEnhancedDto;
import fr.abes.thesesapirecherche.theses.dto.ResponseTheseLiteDto;
import fr.abes.thesesapirecherche.theses.dto.TheseEnhancedResponseDto;
import fr.abes.thesesapirecherche.theses.dto.ThesesByOrganismeResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@Service
public class ThesesService {

    @Autowired
    private final SearchQueryBuilder searchThesesQuery;

    @Autowired
    private final TheseEnhancedMapper theseEnhancedMapper;

    public ThesesService(SearchQueryBuilder searchThesesQuery, TheseEnhancedMapper theseEnhancedMapper) {
        this.searchThesesQuery = searchThesesQuery;
        this.theseEnhancedMapper = theseEnhancedMapper;
    }

    public ResponseTheseEnhancedDto searchTheses(String chaine, Integer debut, Integer nombre, String tri, String filtres, Boolean viewFull) throws Exception {
        SearchResponse<These> response = searchThesesQuery.getThesesSearchResponse(chaine, debut, nombre, tri, filtres);
        
        ResponseTheseEnhancedDto res = new ResponseTheseEnhancedDto();
        List<TheseEnhancedResponseDto> liste = new ArrayList<>();

        for(Hit<These> theseHit : response.hits().hits()) {

            // si viewFull == true, il faut intégrer TOUTES les infos de la thèse, pas seulement
            if (viewFull){
                liste.add(theseEnhancedMapper.theseToEnhancedDto(theseHit));
            }

            // sinon, on intègre seulement les infos du Lite (pour des représentations + légères)
            else{
                liste.add(theseEnhancedMapper.theseToLitedDto(theseHit));
            }
        }

        
        res.setTheses(liste);
        res.setTook(response.took());
        res.setTotalHits(response.hits().total().value());
        return res;
    }



    //récupère
    public ThesesByOrganismeResponseDto searchThesesByOrganisme(String ppn, Boolean viewFull) throws IOException{
        ThesesByOrganismeResponseDto thesesByOrganismeResponse = new ThesesByOrganismeResponseDto();

        // on interroge ES sur tous les index
        SearchResponse<These> responseEtabSoutenanceEnEcours = searchThesesQuery.searchByPpn("etabSoutenancePpn", ppn, "enCours");
        SearchResponse<These> responseEtabSoutenanceSoutenue = searchThesesQuery.searchByPpn("etabSoutenancePpn", ppn, "soutenue");
        SearchResponse<These> responseEtabCotutelleEnCours = searchThesesQuery.searchByPpn("etabsCotutellePpn", ppn, "enCours");
        SearchResponse<These> responseEtabCotutelleSoutenue = searchThesesQuery.searchByPpn("etabsCotutellePpn", ppn, "soutenue");
        SearchResponse<These> responsePartenaireEnCours = searchThesesQuery.searchByPpn("partenairesRecherchePpn", ppn, "enCours");
        SearchResponse<These> responsePartenaireSoutenue = searchThesesQuery.searchByPpn("partenairesRecherchePpn", ppn, "soutenue");
        SearchResponse<These> responseEcoleEnCours = searchThesesQuery.searchByPpn("ecolesDoctoralesPpn", ppn, "enCours");
        SearchResponse<These> responseEcoleSoutenue = searchThesesQuery.searchByPpn("ecolesDoctoralesPpn", ppn, "soutenue");
        

        List<TheseEnhancedResponseDto> listeEtabSoutenance = new ArrayList<>();
        List<TheseEnhancedResponseDto> listeEtabSoutenanceEnCours = new ArrayList<>();
        List<TheseEnhancedResponseDto> listeEtabCotutelle = new ArrayList<>();
        List<TheseEnhancedResponseDto> listeEtabCotutelleEnCours = new ArrayList<>();
        List<TheseEnhancedResponseDto> listePartenaire = new ArrayList<>();
        List<TheseEnhancedResponseDto> listePartenaireEnCours = new ArrayList<>();
        List<TheseEnhancedResponseDto> listeEcoleDoctorale = new ArrayList<>();
        List<TheseEnhancedResponseDto> listeEcoleDoctoraleEnCours = new ArrayList<>();


        // si viewFull == true, on mappe avec le mapper Enhanced
        if(viewFull){
            for(Hit<These> theseHit : responseEtabSoutenanceEnEcours.hits().hits()) listeEtabSoutenance.add(theseEnhancedMapper.theseToEnhancedDto(theseHit));
            for(Hit<These> theseHit : responseEtabSoutenanceSoutenue.hits().hits()) listeEtabSoutenanceEnCours.add(theseEnhancedMapper.theseToEnhancedDto(theseHit));
            for(Hit<These> theseHit : responseEtabCotutelleEnCours.hits().hits()) listeEtabCotutelle.add(theseEnhancedMapper.theseToEnhancedDto(theseHit));
            for(Hit<These> theseHit : responseEtabCotutelleSoutenue.hits().hits()) listeEtabCotutelleEnCours.add(theseEnhancedMapper.theseToEnhancedDto(theseHit));
            for(Hit<These> theseHit : responsePartenaireEnCours.hits().hits()) listePartenaire.add(theseEnhancedMapper.theseToEnhancedDto(theseHit));
            for(Hit<These> theseHit : responsePartenaireSoutenue.hits().hits()) listePartenaireEnCours.add(theseEnhancedMapper.theseToEnhancedDto(theseHit));
            for(Hit<These> theseHit : responseEcoleEnCours.hits().hits()) listeEcoleDoctorale.add(theseEnhancedMapper.theseToEnhancedDto(theseHit));
            for(Hit<These> theseHit : responseEcoleSoutenue.hits().hits()) listeEcoleDoctoraleEnCours.add(theseEnhancedMapper.theseToEnhancedDto(theseHit));
        }

        // sinon on mappe avec le Lite (et les champs détaillés n'apparaitront pas)
        else{
            for(Hit<These> theseHit : responseEtabSoutenanceEnEcours.hits().hits()) listeEtabSoutenance.add(theseEnhancedMapper.theseToLitedDto(theseHit));
            for(Hit<These> theseHit : responseEtabSoutenanceSoutenue.hits().hits()) listeEtabSoutenanceEnCours.add(theseEnhancedMapper.theseToLitedDto(theseHit));
            for(Hit<These> theseHit : responseEtabCotutelleEnCours.hits().hits()) listeEtabCotutelle.add(theseEnhancedMapper.theseToLitedDto(theseHit));
            for(Hit<These> theseHit : responseEtabCotutelleSoutenue.hits().hits()) listeEtabCotutelleEnCours.add(theseEnhancedMapper.theseToLitedDto(theseHit));
            for(Hit<These> theseHit : responsePartenaireEnCours.hits().hits()) listePartenaire.add(theseEnhancedMapper.theseToLitedDto(theseHit));
            for(Hit<These> theseHit : responsePartenaireSoutenue.hits().hits()) listePartenaireEnCours.add(theseEnhancedMapper.theseToLitedDto(theseHit));
            for(Hit<These> theseHit : responseEcoleEnCours.hits().hits()) listeEcoleDoctorale.add(theseEnhancedMapper.theseToLitedDto(theseHit));
            for(Hit<These> theseHit : responseEcoleSoutenue.hits().hits()) listeEcoleDoctoraleEnCours.add(theseEnhancedMapper.theseToLitedDto(theseHit));

        }

        
        // on remplit le dto
        thesesByOrganismeResponse.setEtabSoutenance(listeEtabSoutenance);
        thesesByOrganismeResponse.setEtabSoutenanceEnCours(listeEtabSoutenanceEnCours);
        thesesByOrganismeResponse.setTotalHitsetabSoutenanceEnCours(responseEtabSoutenanceEnEcours.hits().total().value());
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