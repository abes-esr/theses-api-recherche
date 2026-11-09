package fr.abes.thesesapirecherche.theses.dto.client;

import fr.abes.thesesapirecherche.theses.dto.OrganismeResponseDto;
import fr.abes.thesesapirecherche.theses.dto.ThesePersonneResponseDto;
import fr.abes.thesesapirecherche.theses.model.SujetsToMap;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class ClientTheseResponseDto {

    String titrePrincipal;
    String nnt;
    String doi;
    String numSujet;
    String numSujetSansS;
    String dateSoutenance;
    String datePremiereInscriptionDoctorat;
    String dateCines;
    String discipline;
    Map<String, String> titres;
    Map<String, String> resumes;
    OrganismeResponseDto etabSoutenance;
    String codeEtab;
    List<OrganismeResponseDto> etabCotutelle;
    List<OrganismeResponseDto> partenairesRecherche;
    Map<String, List<SujetsToMap>> mapSujets;
    List<ThesePersonneResponseDto> membresJury;
    List<ThesePersonneResponseDto> rapporteurs;
    List<ThesePersonneResponseDto> auteurs;
    List<ThesePersonneResponseDto> directeurs;
    List<String> langues;
    List<String> oaiSetNames;
    String cas;
    String accessible;
    List<OrganismeResponseDto> ecolesDoctorales;
    ThesePersonneResponseDto presidentJury;
    String source;
    String status;
    Boolean isSoutenue;


    

}