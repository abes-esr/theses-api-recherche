package fr.abes.thesesapirecherche.theses.dto;

import fr.abes.thesesapirecherche.theses.model.PersonneThese;
import fr.abes.thesesapirecherche.theses.model.Sujet;
import fr.abes.thesesapirecherche.theses.model.SujetsRameau;
import fr.abes.thesesapirecherche.theses.model.SujetsToMap;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

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
    List<PersonneThese> auteurs;
    List<PersonneThese> directeurs;
    List<PersonneThese> rapporteurs;
    List<PersonneThese> examinateurs;
    PersonneThese president;
    String nnt;
    String discipline;
    String status;
    List<OrganismeResponseDto> ecolesDoctorale;
    List<OrganismeResponseDto> partenairesDeRecherche;
    List<Sujet> sujets;
    List<SujetsRameau> sujetsRameau;
}
