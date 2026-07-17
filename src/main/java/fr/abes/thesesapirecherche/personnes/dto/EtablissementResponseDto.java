package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour un établissement lié à une thèse
 */
@Builder
@Getter
@Setter
@JsonView({JsonViews.Normal.class, JsonViews.Full.class})
public class EtablissementResponseDto {


    @JsonProperty("ppn")
    String ppn;


    @JsonProperty("nom")
    String nom;


    @JsonProperty("type")
    String type;

}
