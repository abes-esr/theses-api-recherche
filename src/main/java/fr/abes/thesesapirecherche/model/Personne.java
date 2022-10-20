package fr.abes.thesesapirecherche.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Représente une personne
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Personne {

    @Getter
    @Setter
    String nom;

    @Getter
    @Setter
    String prenom;

    @Getter
    @Setter
    @JsonProperty("has_idref")
    Boolean hasIdref = false;

    @Getter
    @Setter
    List<ThesePersonne> theses;

}
