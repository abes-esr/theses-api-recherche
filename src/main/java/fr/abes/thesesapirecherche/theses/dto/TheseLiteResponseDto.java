package fr.abes.thesesapirecherche.theses.dto;

import java.util.List;

import fr.abes.thesesapirecherche.theses.model.Sujet;
import fr.abes.thesesapirecherche.theses.model.SujetsRameau;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
    List<PersonneRefDto> auteurs;
    List<PersonneRefDto> directeurs;
    List<PersonneRefDto> rapporteurs;
    List<PersonneRefDto> examinateurs;
    PersonneRefDto president;
    String nnt;
    String doi;
    String discipline;
    String status;
    List<OrganismeResponseDto> ecolesDoctorale;
    List<OrganismeResponseDto> partenairesDeRecherche;
    List<Sujet> sujets;
    List<SujetsRameau> sujetsRameau;
}