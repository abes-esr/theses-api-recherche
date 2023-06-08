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
public class ThesePersonne {

    @Getter
    @Setter
    String nnt;

    @Getter
    @Setter
    String role;

    @Getter
    @Setter
    String titre;

    @Getter
    @Setter
    Map<String, String> titres = new HashMap<String, String>();

    @Getter
    @Setter
    List<SujetsRameau> sujets_rameau = new ArrayList<>();

    @Getter
    @Setter
    Map<String, List<String>> sujets = new HashMap<>();

    @Getter
    @Setter
    String discipline;

    @Getter
    @Setter
    Map<String, String> resumes = new HashMap<>();

    @Getter
    @Setter
    String date_soutenance;

    @Getter
    @Setter
    Etablissement etablissement_soutenance = new Etablissement();

    @Getter
    @Setter
    List<Etablissement> etablissements_cotutelle = new ArrayList<>();

    @Getter
    @Setter
    String status;

    @Getter
    @Setter
    String source;

    @Getter
    @Setter
    List<PersonneLite> auteurs = new ArrayList<>();

    @Getter
    @Setter
    List<PersonneLite> directeurs = new ArrayList<>();

}
