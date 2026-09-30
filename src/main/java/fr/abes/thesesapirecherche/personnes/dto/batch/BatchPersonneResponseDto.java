package fr.abes.thesesapirecherche.personnes.dto.batch;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour une personne spécifique
 */
@Builder
@Getter
@Setter
public class BatchPersonneResponseDto {


    @JsonProperty("id")
    String id;


    @JsonProperty("nom")
    String nom;


    @JsonProperty("prenom")
    String prenom;


    @JsonProperty("has_idref")
    Boolean hasIdref;


    @JsonProperty("roles")
    Map<String, Integer> roles;


    @JsonProperty("theses")
    Map<String, List<BatchTheseResponseDto>> theses;


    @JsonProperty("mots_cles")
    Map<String, List<String>> motsCles;
}
