package fr.abes.thesesapirecherche.personnes.controller;

import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.exception.ApiException;
import fr.abes.thesesapirecherche.personnes.builder.SearchPersonneQueryBuilder;
import fr.abes.thesesapirecherche.personnes.dto.PersonneLiteResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.PersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.SuggestionPersonneResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * Cette classe ne sert que pour tester les tests d'intégrations avec les jeux de données dans ElasticSearch.
 * Les routes sont exactement les mêmes que pour les personnes mais il y a un argument supplémentaire
 * qui permet de choisir l'index ElasticSearch à requêter.
 * Les tests d'intégrations sont exécutés sur des jeux de données spécifiques qui donne lieu à des index spécifiques dans ElasticSearch.
 * Par exemple, l'index ElasticSearch 'per_recherche_simple_rousseau' contient un échantillon de personnes qui permet de tester les requêtes de recherche simple.
 * <p>
 * !! Cette classe est donc à retirer dans la version en production !!
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/tests/personnes/")
@Deprecated
public class PersonneTestController {

    @Autowired
    SearchPersonneQueryBuilder searchQueryBuilder;

    /**
     * Rechercher une personne avec un mot en choissant l'index ES à requêter
     *
     * @param q       Chaîne de caractère à rechercher
     * @param index   Nom de l'index ES à requêter
     * @param filtres Filtres des résultats
     * @return Une liste de personnes correspondant à la recherche
     * @throws ApiException
     */
    @GetMapping(value = "/recherche")
    public List<PersonneLiteResponseDto> recherche(
            @RequestParam final String q,
            @RequestParam final String index,
            @RequestParam Optional<String> filtres
    ) throws Exception {
        String decodedQuery = URLDecoder.decode(q.replaceAll("\\+", "%2b"), StandardCharsets.UTF_8.toString());
        String decodedFilters = URLDecoder.decode(filtres.orElse(""), StandardCharsets.UTF_8.toString());
        log.debug("Rechercher une personne... : " + decodedQuery);
        return searchQueryBuilder.rechercher(decodedQuery, index, decodedFilters);
    }

    /**
     * Proposer l'autocompletion basée sur les noms et prénoms
     *
     * @param q     Chaîne de caractère à compléter
     * @param index Nom de l'index ES à requêter
     * @return Retourne 10 propositions avec une priorité sur les personnes avec un identifiant Idref
     * @throws Exception
     */
    @GetMapping(value = "/completion")
    public List<SuggestionPersonneResponseDto> completion(
            @RequestParam final String q,
            @RequestParam final String index
    ) throws Exception {
        String decodedQuery = URLDecoder.decode(q, StandardCharsets.UTF_8.toString());
        log.info("debut de completion...");
        return searchQueryBuilder.completion(decodedQuery, index);
    }

    /**
     * Retourne une liste de facettes avec le nombre d'occurence pour chaque facette
     *
     * @param q     Chaîne de caractère à rechercher
     * @param index Nom de l'index ES à requêter
     * @return Retourne une liste de facettes en fonction du critère de recherche
     * @throws Exception
     */
    @GetMapping(value = "/facets")
    public List<Facet> facets(@RequestParam final String q,
                              @RequestParam final String index) throws Exception {
        return searchQueryBuilder.facets(q, index);
    }

    /**
     * Recherche une personne à partir de son identifiant
     *
     * @param id    Identifiant de la personne
     * @param index Nom de l'index ES à requêter
     * @return Retourne la personne
     * @throws ApiException si la personne n'est pas trouvée
     */
    @GetMapping(value = "/personne/{id}")
    public PersonneResponseDto rechercherParIdentifiant(@PathVariable final String id,
                                                        @RequestParam final String index) throws ApiException {
        log.debug("Rechercher une personne par son identifiant...");
        try {
            return searchQueryBuilder.rechercherParIdentifiant(id, index);

        } catch (Exception e) {
            log.error(e.toString());
            throw new ApiException(e.getLocalizedMessage());
        }
    }

}
