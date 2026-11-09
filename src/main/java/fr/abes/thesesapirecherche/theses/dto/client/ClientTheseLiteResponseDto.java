package fr.abes.thesesapirecherche.theses.dto.client;

import java.util.List;
import java.util.Map;

import fr.abes.thesesapirecherche.theses.dto.OrganismeRefDto;
import fr.abes.thesesapirecherche.theses.dto.SujetsToMapDto;
import fr.abes.thesesapirecherche.theses.dto.PersonneRefDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ClientTheseLiteResponseDto {
    String id;
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

    // pas fournit car considéré comme trop coûteux
    // Map<String, String> resumes;
    
    OrganismeRefDto etabSoutenance;
    String codeEtab;
    List<OrganismeRefDto> etabCotutelle;
    List<OrganismeRefDto> partenairesRecherche;
    Map<String, List<SujetsToMapDto>> mapSujets;
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