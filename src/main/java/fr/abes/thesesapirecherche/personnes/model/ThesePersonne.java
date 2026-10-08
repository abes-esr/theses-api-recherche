package fr.abes.thesesapirecherche.personnes.model;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Représente une thèse pour une personne
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThesePersonne {
    String id;
    String doi;
    String nnt;
    String numSujetSansS;
    String role;
    String titre;
    Map<String, String> titres = new HashMap<String, String>();
    List<SujetsRameau> sujets_rameau = new ArrayList<>();
    Map<String, List<String>> sujets = new HashMap<>();
    String discipline;
    Map<String, String> resumes = new HashMap<>();
    String date_soutenance;
    String date_inscription;
    String dateCines;
    String codeEtab;
    Etablissement etablissement_soutenance = new Etablissement();
    List<Etablissement> etablissements_cotutelle = new ArrayList<>();
    String status;
    String accessible;
    String source;
    String cas;
    List<PersonneLite> auteurs = new ArrayList<>();
    List<PersonneLite> directeurs = new ArrayList<>();
    List<String> langues;
    List<String> oaiSetNames = new ArrayList<>();
   
    

}
