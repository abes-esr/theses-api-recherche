package fr.abes.thesesapirecherche.theses.dto;


import lombok.Getter;
import lombok.Setter;

/**
 * Représente une personne dans une thèse
 */
@Getter
@Setter
public class PersonneRefDto {
    private String ppn;
    private String nom;
    private String prenom;
}
