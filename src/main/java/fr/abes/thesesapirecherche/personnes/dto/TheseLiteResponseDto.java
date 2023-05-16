package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.abes.thesesapirecherche.personnes.model.Etablissement;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO web retournée par l'API ppour un ensemble de thèses en lien avec une personne
 */
@Builder
public class TheseLiteResponseDto {
    @Getter
    @Setter
    @JsonProperty("nnt")
    String nnt;

    @Getter
    @Setter
    @JsonProperty("role")
    String role;

    @Getter
    @Setter
    @JsonProperty("discipline")
    String discipline;

    @Getter
    @Setter
    @JsonProperty("etablissement_soutenance")
    Etablissement etablissement_soutenance;

    @Getter
    @Setter
    @JsonProperty("etabliseements_cotutelle")
    List<Etablissement> etablissements_cotutelle;

}
