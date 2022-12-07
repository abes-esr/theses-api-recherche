package fr.abes.thesesapirecherche.personnes.model;

import lombok.*;

/**
 * Représente une thèse pour une personne
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThesePersonne {

    @Getter @Setter
    String nnt;

    @Getter @Setter
    String role;

}
