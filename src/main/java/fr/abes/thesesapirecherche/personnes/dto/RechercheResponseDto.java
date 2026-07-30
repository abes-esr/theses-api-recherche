package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO web retournée par l'API de recherche de personnes
 */
@Builder
@Getter
@Setter
@JsonView({JsonViews.Normal.class, JsonViews.Full.class})
public class RechercheResponseDto {


    @JsonProperty("totalHits")
    long totalHits;


    @JsonProperty("took")
    long took;


    @JsonProperty("personnes")
    List<PersonneLiteResponseDto> personnes = new ArrayList<>();
}
