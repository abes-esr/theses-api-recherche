package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour une personne dans une thèse (auteurs, directeurs)
 */
@Builder
@Getter
@Setter
public class ThesePersonneLiteResponseDto {
    String id;
    String nom;
    String prenom;
    @JsonProperty("has_idref")
    Boolean hasIdref;
}
