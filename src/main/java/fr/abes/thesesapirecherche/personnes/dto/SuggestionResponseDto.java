package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO web retournée par l'API pour une suggestion du module personne
 */
@Builder
public class SuggestionResponseDto {

    @Getter
    @Setter
    @JsonProperty("personnes")
    List<SuggestionPersonneResponseDto> personnes;

    @Getter
    @Setter
    @JsonProperty("thematiques")
    List<SuggestionPersonneResponseDto> thematiques;



}
