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
public class TheseLiteResponseDto {


    @JsonView({JsonViews.Lite.class})
    String id;
    

    @JsonView({JsonViews.Lite.class})
    String numSujetSansS;
    

    @JsonView({JsonViews.Lite.class})
    String titrePrincipal;
    

    @JsonView({JsonViews.Lite.class})
    String titreEN;
    

    @JsonView({JsonViews.Lite.class})
    String etabSoutenanceN;
    

    @JsonView({JsonViews.Lite.class})
    String etabSoutenancePpn;
    

    @JsonView({JsonViews.Lite.class})
    String dateSoutenance;
    

    @JsonView({JsonViews.Lite.class})
    String datePremiereInscriptionDoctorat;
    

    @JsonView({JsonViews.Lite.class})
    String dateCines;
    

    @JsonView({JsonViews.Lite.class})
    List<ThesePersoneResponseDto> auteurs;
    

    @JsonView({JsonViews.Lite.class})
    List<ThesePersoneResponseDto> directeurs;
    

    @JsonView({JsonViews.Lite.class})
    List<ThesePersoneResponseDto> rapporteurs;
    

    @JsonView({JsonViews.Lite.class})
    List<ThesePersoneResponseDto> examinateurs;
    

    @JsonView({JsonViews.Lite.class})
    ThesePersoneResponseDto president;
    

    @JsonView({JsonViews.Lite.class})
    String nnt;
    

    @JsonView({JsonViews.Lite.class})
    String doi;
    

    @JsonView({JsonViews.Lite.class})
    String discipline;
    

    @JsonView({JsonViews.Lite.class})
    String status;
    

    @JsonView({JsonViews.Lite.class})
    String cas;
    

    @JsonView({JsonViews.Lite.class})
    List<OrganismeResponseDto> ecolesDoctorale;
    

    @JsonView({JsonViews.Lite.class})
    List<OrganismeResponseDto> partenairesDeRecherche;
    

    @JsonView({JsonViews.Lite.class})
    List<Sujet> sujets;
    

    @JsonView({JsonViews.Lite.class})
    List<SujetsRameau> sujetsRameau;
    

    @JsonView({JsonViews.Lite.class})
    List<String> oaiSetNames;

}
