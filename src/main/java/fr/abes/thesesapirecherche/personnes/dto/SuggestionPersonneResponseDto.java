package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour une suggestion de personne
 */
@Builder
public class SuggestionPersonneResponseDto {

    @Getter
    @Setter
    @JsonProperty("suggestion")
    String text;

    /*
    Identifiant du document Elastic Search
     */
    @Getter
    @Setter
    @JsonProperty("id")
    String id;

}
