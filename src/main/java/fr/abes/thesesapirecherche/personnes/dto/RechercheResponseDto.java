package fr.abes.thesesapirecherche.personnes.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API de recherche de personnes
 */
@Builder
@Getter
@Setter
public class RechercheResponseDto {


    @JsonProperty("totalHits")
    long totalHits;


    @JsonProperty("took")
    long took;


    @JsonProperty("personnes")
    List<PersonneLiteResponseDto> personnes = new ArrayList<>();
}
