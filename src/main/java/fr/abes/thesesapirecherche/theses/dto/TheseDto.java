package fr.abes.thesesapirecherche.theses.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class TheseDto {

    String titrePrincipal;
    String nnt;
    String doi;
    String numSujet;
    String dateSoutenance;
    String datePremiereInscriptionDoctorat;
    String discipline;
    Map<String, String> titres;
    Map<String, String> resumes;
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
    String cas;
    String accessible;
    List<OrganismeRefDto> ecolesDoctorales;
    PersonneRefDto presidentJury;
    String source;
    String status;
    Boolean isSoutenue;

}