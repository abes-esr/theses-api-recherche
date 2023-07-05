package fr.abes.thesesapirecherche.personnes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Représente une personne simplifiée dans les informations d'une thèse (auteurs, directeurs,...)
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonneLite {

    @Getter
    @Setter
    String ppn;

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

}
