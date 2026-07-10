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
    Map<String, String> titres = new HashMap<String, String>();
    List<SujetsRameau> sujets_rameau = new ArrayList<>();
    Map<String, List<String>> sujets = new HashMap<>();
    String discipline;
    Map<String, String> resumes = new HashMap<>();
    String date_soutenance;
    String date_inscription;
    Etablissement etablissement_soutenance = new Etablissement();
    List<Etablissement> etablissements_cotutelle = new ArrayList<>();
    String status;
    String source;
    List<PersonneLite> auteurs = new ArrayList<>();
    List<PersonneLite> directeurs = new ArrayList<>();
    List<String> oaiSetNames = new ArrayList<>();

}
