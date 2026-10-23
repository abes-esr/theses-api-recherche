package fr.abes.thesesapirecherche.personnes.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour un ensemble de personnes
 */
@Builder
@Getter
@Setter
public class PersonneLiteResponseDto {
    String id;
    String nom;
    String prenom;
    @JsonProperty("has_idref")
    Boolean hasIdref;
    Map<String, Integer> roles;
    List<String> theses;
    List<String> disciplines;
    List<String> etablissements;
}
