package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour un sujet rameau lié à une thèse
 */
@Builder
@Getter
@Setter
@JsonView({JsonViews.Normal.class, JsonViews.Full.class})
public class SujetRameauResponseDto {


    @JsonProperty("ppn")
    String ppn;


    @JsonProperty("libelle")
    String libelle;
}
