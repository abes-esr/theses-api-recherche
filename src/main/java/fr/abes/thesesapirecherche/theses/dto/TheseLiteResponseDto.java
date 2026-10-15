package fr.abes.thesesapirecherche.theses.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class TheseLiteResponseDto {
    String id;
    String titrePrincipal;
    String titreEN;
    String etabSoutenanceN;
    String etabSoutenancePpn;
    String dateSoutenance;
    String datePremiereInscriptionDoctorat;
    List<PersonneRefDto> auteurs = new ArrayList<>();
    List<PersonneRefDto> directeurs = new ArrayList<>();
    List<PersonneRefDto> rapporteurs = new ArrayList<>();
    List<PersonneRefDto> examinateurs = new ArrayList<>();
    PersonneRefDto president;
    String nnt;
    String doi;
    String discipline;
    String status;
    List<OrganismeResponseDto> ecolesDoctorale = new ArrayList<>();
    List<OrganismeResponseDto> partenairesDeRecherche = new ArrayList<>();
    List<SujetRefDto> sujets = new ArrayList<>();
    List<RameauRefDto> sujetsRameau = new ArrayList<>();
}
