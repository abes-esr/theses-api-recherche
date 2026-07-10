package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.abes.thesesapirecherche.personnes.model.Etablissement;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO web retournée par l'API ppour un ensemble de thèses en lien avec une personne
 */
@Builder
@Getter
@Setter
public class TheseLiteResponseDto {

    @JsonProperty("id")
    String id;


    @JsonProperty("role")
    String role;


    @JsonProperty("discipline")
    String discipline;


    @JsonProperty("etablissement_soutenance")
    Etablissement etablissement_soutenance;


    @JsonProperty("etabliseements_cotutelle")
    List<Etablissement> etablissements_cotutelle;

}
