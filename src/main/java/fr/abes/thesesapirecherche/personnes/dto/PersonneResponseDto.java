package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * DTO web retournée par l'API pour une personne spécifique
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
    @JsonProperty("roles")
    Map<String,Integer> roles;

    @Getter
    @Setter
    @JsonProperty("theses")
    Map<String,List<TheseResponseDto>> theses;
}
