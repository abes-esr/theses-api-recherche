package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO web retournée par l'API de recherche de personnes
 */
@Builder
public class RechercheResponseDto {

    @Getter
    @Setter
    @JsonProperty("totalHits")
    long totalHits;

    @Getter
    @Setter
    @JsonProperty("took")
    long took;

    @Getter
    @Setter
    @JsonProperty("personnes")
    List<PersonneLiteResponseDto> personnes = new ArrayList<>();
}
