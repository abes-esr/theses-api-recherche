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
@Getter
@Setter
public class RecherchePersonne {
    String ppn;
    String nom;
    String prenom;
    @JsonProperty("has_idref")
    Boolean hasIdref = false;
    List<String> roles;
    List<String> etablissements;
    List<String> disciplines;
    List<String> theses_id;

}
