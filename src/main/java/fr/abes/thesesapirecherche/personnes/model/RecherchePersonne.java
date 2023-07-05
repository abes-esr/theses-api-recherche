package fr.abes.thesesapirecherche.personnes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Représente une personne simplifiée pour la recherche
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecherchePersonne {

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

    @Getter
    @Setter
    List<String> roles;

    @Getter
    @Setter
    List<String> etablissements;

    @Getter
    @Setter
    List<String> disciplines;

    @Getter
    @Setter
    List<String> theses_id;

}
