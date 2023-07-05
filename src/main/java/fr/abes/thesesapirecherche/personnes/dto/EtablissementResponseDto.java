package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour un établissement lié à une thèse
 */
@Builder
public class EtablissementResponseDto {

    @Getter
    @Setter
    @JsonProperty("nnt")
    String ppn;

    @Getter
    @Setter
    @JsonProperty("nom")
    String nom;

    @Getter
    @Setter
    @JsonProperty("type")
    String type;

}
