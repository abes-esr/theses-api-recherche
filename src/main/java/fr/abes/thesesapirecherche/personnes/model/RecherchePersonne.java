package fr.abes.thesesapirecherche.personnes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
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
    Boolean hasIdref; // default to false
    List<String> roles = new ArrayList<>();
    List<String> etablissements = new ArrayList<>();
    List<String> disciplines = new ArrayList<>();
    @JsonProperty("theses_id")
    List<String> thesesId = new ArrayList<>();
}
