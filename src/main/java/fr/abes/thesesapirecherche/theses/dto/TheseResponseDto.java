package fr.abes.thesesapirecherche.theses.dto;

import fr.abes.thesesapirecherche.theses.model.SujetsToMap;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class TheseResponseDto {

    String titrePrincipal;
    String nnt;
    String dateSoutenance;
    String datePremiereInscriptionDoctorat;
    String discipline;
    Map<String, String> titres;
    Map<String, String> resumes;
    OrganismeResponseDto etabSoutenance;
    List<OrganismeResponseDto> etabCotutelle;
    List<OrganismeResponseDto> partenairesRecherche;
    Map<String, List<SujetsToMap>> mapSujets;
    List<ThesePersoneResponseDto> membresJury;
    List<ThesePersoneResponseDto> rapporteurs;
    List<ThesePersoneResponseDto> auteurs;
    List<ThesePersoneResponseDto> directeurs;
    String cas;
    List<OrganismeResponseDto> ecolesDoctorales;
    ThesePersoneResponseDto presidentJury;
    String source;
    String status;

}
