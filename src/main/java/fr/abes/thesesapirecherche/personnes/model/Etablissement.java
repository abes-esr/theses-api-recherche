package fr.abes.thesesapirecherche.personnes.model;

import lombok.*;

/**
 * Représente un établissement
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Etablissement {

    private String ppn;
    private String nom;
    private String type;

}
