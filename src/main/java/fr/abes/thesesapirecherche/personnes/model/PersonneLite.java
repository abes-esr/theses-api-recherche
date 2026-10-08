package fr.abes.thesesapirecherche.personnes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Représente une personne simplifiée dans les informations d'une thèse (auteurs, directeurs,...)
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonneLite {


    String ppn;
    String nom;
    String prenom;

    @JsonProperty("has_idref")
    Boolean hasIdref = false;

}
