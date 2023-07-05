package fr.abes.thesesapirecherche.personnes.model;

import lombok.*;

/**
 * Représente un établissement
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Etablissement {

    @Getter
    @Setter
    private String ppn;

    @Getter
    @Setter
    private String nom;

    @Getter
    @Setter
    private String type;

}
