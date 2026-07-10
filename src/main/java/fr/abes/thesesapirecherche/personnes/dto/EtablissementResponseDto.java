package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour un établissement lié à une thèse
 */
@Builder
@Getter
@Setter
public class EtablissementResponseDto {


    @JsonProperty("ppn")
    String ppn;


    @JsonProperty("nom")
    String nom;


    @JsonProperty("type")
    String type;

}
