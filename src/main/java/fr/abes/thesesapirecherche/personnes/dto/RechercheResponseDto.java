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
@Getter
@Setter
public class RechercheResponseDto {
    // TODO: est-ce que je fait un breaking change ou pas ? totalHits -> total_hits
    @JsonProperty("totalHits")
    long totalHits;
    long took;
    List<PersonneLiteResponseDto> personnes = new ArrayList<>();
}
