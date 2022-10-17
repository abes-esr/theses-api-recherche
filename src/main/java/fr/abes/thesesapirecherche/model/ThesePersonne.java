package fr.abes.thesesapirecherche.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente une thèse pour une personne
 */
@AllArgsConstructor
@NoArgsConstructor
public class ThesePersonne {

    @Getter @Setter
    String nnt;

    @Getter @Setter
    String role;

}
