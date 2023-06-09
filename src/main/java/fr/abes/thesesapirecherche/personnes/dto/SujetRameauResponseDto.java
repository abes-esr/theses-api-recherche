package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour un sujet rameau lié à une thèse
 */
@Builder
public class SujetRameauResponseDto {

    @Getter
    @Setter
    @JsonProperty("ppn")
    String ppn;

    @Getter
    @Setter
    @JsonProperty("libelle")
    String libelle;
}
