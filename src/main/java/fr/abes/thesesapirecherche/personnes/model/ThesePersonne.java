package fr.abes.thesesapirecherche.personnes.model;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente une thèse pour une personne
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThesePersonne {
    String id;
    String role;
    String titre;
    Map<String, String> titres = new HashMap<>();
    List<SujetsRameau> sujetsRameau = new ArrayList<>();
    Map<String, List<String>> sujets = new HashMap<>();
    String discipline;
    Map<String, String> resumes = new HashMap<>();
    String dateSoutenance;
    String dateInscription;
    Etablissement etablissementSoutenance;
    List<Etablissement> etablissementsCotutelle = new ArrayList<>();
    String status;
    String source;
    List<PersonneLite> auteurs = new ArrayList<>();
    List<PersonneLite> directeurs = new ArrayList<>();
    List<String> oaiSetNames = new ArrayList<>();
}
