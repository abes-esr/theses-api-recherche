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
    List<String> sujets_rameau = new ArrayList<>();

    @Getter
    @Setter
    Map<String, String> sujets = new HashMap<String, String>();

    @Getter
    @Setter
    String discipline;

    @Getter
    @Setter
    Map<String, String> resumes = new HashMap<String, String>();

}
