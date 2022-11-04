package fr.abes.thesesapirecherche.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO web retournée par l'API ppour un ensemble de personnes
 */
@Builder
public class PersonneResponseDto {

    @Getter
    @Setter
    @JsonProperty("id")
    String id;

    @Getter
    @Setter
    @JsonProperty("nom")
    String nom;

    @Getter
    @Setter
    @JsonProperty("prenom")
    String prenom;

    @Getter
    @Setter
    @JsonProperty("has_idref")
    Boolean hasIdref;

    @Getter
    @Setter
    @JsonProperty("theses")
    List<PersonnesTheseResponseDto> theses;
}