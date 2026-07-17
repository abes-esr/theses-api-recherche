package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * DTO web retournée par l'API pour un ensemble de personnes
 */
@Builder
@Getter
@Setter
@JsonView({JsonViews.Normal.class, JsonViews.Full.class})
public class PersonneLiteResponseDto {


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
    List<String> theses;


    @JsonProperty("disciplines")
    List<String> disciplines;


    @JsonProperty("etablissements")
    List<String> etablissements;
}
