package fr.abes.thesesapirecherche.personnes.dto;

import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;

import java.util.*;
import java.util.stream.Collectors;

public class PersonneComputedFields {

    /**
     * Calcule le nombre de fois que les rôles ont été tenus
     * @param items Liste des thèses
     * @return Une map avec les rôles comme clé et le nombre d'occurrences en valeur
     */
    public static Map<String, Integer> calculerStatistiquesRoles(List<ThesePersonne> items) {
        Map<String, Integer> elementCountMap = new LinkedHashMap<>();

        if (items != null) {
            for (ThesePersonne item : items) {
                if (elementCountMap.containsKey(item.getRole())) {
                    elementCountMap.put(item.getRole(), elementCountMap.get(item.getRole()) + 1);
                } else {
                    elementCountMap.put(item.getRole(), 1);
                }
            }
        }
        return elementCountMap;
    }

    /**
     * Calcule le nombre de fois que les rôles ont été tenus
     * @param items Liste des roles
     * @return Une map avec les rôles comme clé et le nombre d'occurrences en valeur
     */
    public static Map<String, Integer> calculerStatistiquesRechercheRoles(List<String> items) {
        Map<String, Integer> elementCountMap = new LinkedHashMap<>();

        if (items != null) {
            for (String role : items) {
                if (elementCountMap.containsKey(role)) {
                    elementCountMap.put(role, elementCountMap.get(role) + 1);
                } else {
                    elementCountMap.put(role, 1);
                }
            }
        }
        return elementCountMap;
    }

    /**
     * Calcule les 3 disciplines les plus fréquentes parmi un lot de thèses
     * @param items Liste des thèses
     * @return Une liste de 3 éléments ordonnés selon le nombre d'occurrences
     */
    public static List<String> calculerDisciplines(List<String> items) {
        List<String> results = new ArrayList<>();

        if (items != null) {
            return sortArrayElementsByFrequency(items).stream().limit(3).collect(Collectors.toList());
        }
        return results;
    }

    /**
     * Calcule les 3 établissements de soutenances les plus fréquents parmi un lot de thèses
     * @param items Liste des thèses
     * @return Une liste de 3 éléments ordonnés selon le nombre d'occurrences
     */
    public static List<String> calculerEtablissements(List<String> items) {
        List<String> results = new ArrayList<>();

        if (items != null) {
            return sortArrayElementsByFrequency(items).stream().limit(3).collect(Collectors.toList());
        }
        return results;
    }

    /**
     * Calcule les mots-clés selon leurs langues et leurs nombre d'occurence parmi un lot de thèses
     * On réalise un premier dédoublonnage des mots-clés au sein d’une même thèse
     * @param items Liste des thèses
     * @return Une map avec les codes langues en clé et la liste des mots-clés en valeur
     */
    public static Map<String,List<String>> calculerMotsCles(List<ThesePersonne> items) {
        Map<String,List<String>> results = new HashMap<>();

        if (items != null) {
            for (ThesePersonne item : items) {

                // On enlève les doublons entre les sujets libres et les sujets Rameau
                Map<String,LinkedHashSet<String>> set = new HashMap<>();

                // Sujets libres
                if (item.getSujets() != null) {
                    for (String lang: item.getSujets().keySet()) {
                        if (set.containsKey(lang)) {
                            set.get(lang).addAll(item.getSujets().get(lang));
                        } else {
                            set.put(lang, new LinkedHashSet<>());
                            set.get(lang).addAll(item.getSujets().get(lang));
                        }
                    }
                }

                // Sujets Rameau
                if (item.getSujets_rameau() != null) {
                    if (!set.containsKey("fr")) {
                        set.put("fr", new LinkedHashSet<>());
                    }

                    set.get("fr").addAll(item.getSujets_rameau().stream()
                            .map(e->e.getLibelle())
                            .collect(Collectors.toList()));
                }

                for (String lang: set.keySet()) {
                    if (!results.containsKey(lang)) {
                        results.put(lang, new ArrayList<>());
                    }
                    results.get(lang).addAll(set.get(lang));
                }
            }

            // On ordonne chaque liste de mots-clés
            for (String lang: results.keySet()) {
                results.put(lang, sortArrayElementsByFrequency(results.get(lang)));
            }
        }
        return results;
    }

    /**
     * Ordonne une liste de chaîne de caractère selon le nombre d'occurrences
     * Les doublons comptabilités puis supprimés de la liste
     * @param inputArray Liste de chaînes de caractères
     * @return Liste de chaînes de caractère ordonnée selon le nombre d'occurrences
     */
    private static List<String> sortArrayElementsByFrequency(List<String> inputArray) {
        Map<String, Integer> elementCountMap = new LinkedHashMap<>();

        // On compte le nombre d'occurrences
        for (int i = 0; i < inputArray.size(); i++) {
            if (elementCountMap.containsKey(inputArray.get(i))) {
                elementCountMap.put(inputArray.get(i), elementCountMap.get(inputArray.get(i)) + 1);
            } else {
                elementCountMap.put(inputArray.get(i), 1);
            }
        }

        // On tri sur le nombre d'occurrences
        ArrayList<String> sortedElements = new ArrayList<>();
        elementCountMap.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .forEach(entry -> {
                    for (int i = 1; i <= entry.getValue(); i++)
                        sortedElements.add(entry.getKey());
                });

        // On enlève les doublons tout en conservant l'ordre
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.addAll(sortedElements);
        sortedElements.clear();
        sortedElements.addAll(set);

        return sortedElements;
    }
}
