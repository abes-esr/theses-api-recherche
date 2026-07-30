package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour une suggestion de personne
 */
@Builder
@Getter
@Setter
public class SuggestionPersonneResponseDto {


    @JsonProperty("suggestion")
    String text;

    /*
    Identifiant du document Elastic Search
     */

    @JsonProperty("id")
    String id;

}
