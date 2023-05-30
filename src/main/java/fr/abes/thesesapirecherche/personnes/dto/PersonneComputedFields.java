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
     * Calcule les 3 disciplines les plus fréquentes parmi un lot de thèses
     * @param items Liste des thèses
     * @return Une liste de 3 éléments ordonnés selon le nombre d'occurrences
     */
    public static List<String> calculerDisciplines(List<ThesePersonne> items) {
        List<String> results = new ArrayList<>();

        if (items != null) {
            for (ThesePersonne item : items) {
                if (item.getDiscipline() != null) {
                    results.add(item.getDiscipline());
                }
            }
            return sortArrayElementsByFrequency(results).stream().limit(3).collect(Collectors.toList());
        }
        return results;
    }

    /**
     * Calcule les 3 établissements de soutenances les plus fréquents parmi un lot de thèses
     * @param items Liste des thèses
     * @return Une liste de 3 éléments ordonnés selon le nombre d'occurrences
     */
    public static List<String> calculerEtablissements(List<ThesePersonne> items) {
        List<String> results = new ArrayList<>();

        if (items != null) {
            for (ThesePersonne item : items) {
                if (item.getEtablissement_soutenance() != null) {
                    results.add(item.getEtablissement_soutenance().getNom());
                }
            }
            return sortArrayElementsByFrequency(results).stream().limit(3).collect(Collectors.toList());
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
