package fr.abes.thesesapirecherche.theses.dto.client;

import fr.abes.thesesapirecherche.theses.dto.OrganismeRefDto;
import fr.abes.thesesapirecherche.theses.dto.PersonneRefDto;
import fr.abes.thesesapirecherche.theses.dto.SujetDto;
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
    OrganismeRefDto etabSoutenance;
    String codeEtab;
    List<OrganismeRefDto> etabCotutelle;
    List<OrganismeRefDto> partenairesRecherche;
    Map<String, List<SujetDto>> mapSujets;
    List<PersonneRefDto> membresJury;
    List<PersonneRefDto> rapporteurs;
    List<PersonneRefDto> auteurs;
    List<PersonneRefDto> directeurs;
    List<String> langues;
    List<String> oaiSetNames;
    String cas;
    String accessible;
    List<OrganismeRefDto> ecolesDoctorales;
    PersonneRefDto presidentJury;
    String source;
    String status;
    Boolean isSoutenue;


    

}