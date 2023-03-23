package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour une personne dans une thèse (auteurs, directeurs)
 */
@Builder
public class ThesePersonneLiteResponseDto {

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
}
