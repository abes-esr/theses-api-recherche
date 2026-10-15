package fr.abes.thesesapirecherche.theses.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Représente une personne dans une thèse
 */
@Getter
@Setter
@Builder
public class PersonneRefDto {
    private String ppn;
    private String nom;
    private String prenom;
}
