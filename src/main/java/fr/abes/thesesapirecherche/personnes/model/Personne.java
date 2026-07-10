package fr.abes.thesesapirecherche.personnes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Représente une personne
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Personne {

    String nom;
    String prenom;

    @JsonProperty("has_idref")
    Boolean hasIdref = false;

    List<ThesePersonne> theses;
    List<String> roles;

}
