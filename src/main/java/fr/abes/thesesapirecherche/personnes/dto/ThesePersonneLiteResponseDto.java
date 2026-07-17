package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour une personne dans une thèse (auteurs, directeurs)
 */
@Builder
@Getter
@Setter
@JsonView({JsonViews.Normal.class, JsonViews.Full.class})
public class ThesePersonneLiteResponseDto {


    @JsonProperty("id")
    String id;


    @JsonProperty("nom")
    String nom;


    @JsonProperty("prenom")
    String prenom;


    @JsonProperty("has_idref")
    Boolean hasIdref;
}
