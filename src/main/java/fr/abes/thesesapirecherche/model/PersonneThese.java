package fr.abes.thesesapirecherche.model;


import lombok.Getter;
import lombok.Setter;

/**
 * Représente une personne dans une thèse
 */
@Getter
@Setter
public class PersonneThese {
    private String ppn;
    private String nom;
    private String prenom;
}
