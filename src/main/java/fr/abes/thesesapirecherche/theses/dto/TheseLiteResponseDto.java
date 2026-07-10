package fr.abes.thesesapirecherche.theses.dto;

import fr.abes.thesesapirecherche.theses.model.Sujet;
import fr.abes.thesesapirecherche.theses.model.SujetsRameau;
import fr.abes.thesesapirecherche.theses.model.SujetsToMap;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;

@SuperBuilder
@Getter
@Setter
@JsonView({JsonViews.Lite.class})
public class TheseLiteResponseDto {

    String id;
    String numSujetSansS;
    String titrePrincipal;
    String titreEN;
    String etabSoutenanceN;
    String etabSoutenancePpn;
    String dateSoutenance;
    String datePremiereInscriptionDoctorat;
    String dateCines;
    List<ThesePersoneResponseDto> auteurs;
    List<ThesePersoneResponseDto> directeurs;
    List<ThesePersoneResponseDto> rapporteurs;
    List<ThesePersoneResponseDto> examinateurs;
    ThesePersoneResponseDto president;
    String nnt;
    String doi;
    String discipline;
    String status;
    String cas;
    List<OrganismeResponseDto> ecolesDoctorale;
    List<OrganismeResponseDto> partenairesDeRecherche;
    List<Sujet> sujets;
    List<SujetsRameau> sujetsRameau;
    List<String> oaiSetNames;

}
