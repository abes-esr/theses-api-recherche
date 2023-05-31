package fr.abes.thesesapirecherche.personnes.controller;

import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.exception.ApiException;
import fr.abes.thesesapirecherche.personnes.builder.SearchPersonneQueryBuilder;
import fr.abes.thesesapirecherche.personnes.dto.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
     * @param debut Numéro de la page courante
     * @param nombre Nombre de résultats à retourner
     * @return Une réponse de recherche
     * @throws ApiException
     */
    @GetMapping(value = "/recherche")
    @ApiOperation(
            value = "Rechercher une personne avec un mot",
            notes = "Retourne une liste de personnes correspondant à la recherche")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    public RechercheResponseDto recherche(
            @RequestParam @ApiParam(name = "q", value = "début de la chaine à rechercher", example = "rousseau") final String q,
            @RequestParam @ApiParam(name = "index", value = "nom de l'index à réquêter", example = "personnes") final String index,
            @RequestParam @ApiParam(name = "filtres", value = "filtres", example = "[role=\"auteurs\"&role=\"rapporteurs\"]") Optional<String> filtres,
            @RequestParam @ApiParam(name = "debut", value = "indice de la première personne du lot", example = "10") Optional<Integer> debut,
            @RequestParam @ApiParam(name = "nombre", value = "nombre de personne dans le lot", example = "10") Optional<Integer> nombre,
            @RequestParam @ApiParam(name = "tri", value = "Type de tri", example = "pertinence, PersonneDesc, PersonnesAsc") Optional<String> tri
    ) throws Exception {
        String decodedQuery = URLDecoder.decode(q.replaceAll("\\+", "%2b"), StandardCharsets.UTF_8.toString());
        String decodedFilters = URLDecoder.decode(filtres.orElse(""), StandardCharsets.UTF_8.toString());
        log.debug("Rechercher une personne... : " + decodedQuery);
        return searchQueryBuilder.rechercher(decodedQuery, index, decodedFilters, debut.orElse(0), nombre.orElse(10),tri.orElse(""));
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
    @ApiOperation(
            value = "Proposer l'autocompletion basée sur les noms et prénoms",
            notes = "Retourne 10 propositions avec une priorité sur les personnes avec un identifiant Idref")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    public SuggestionResponseDto completion(
            @RequestParam @ApiParam(name = "q", value = "début de la chaine à rechercher", example = "indus") final String q,
            @RequestParam @ApiParam(name = "index", value = "nom de l'index à réquêter", example = "personnes") final String index
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
    @ApiOperation(
            value = "Retourne une liste de facettes avec le nombre d'ocurence pour chaque facette",
            notes = "Retourne une liste de facettes en fonction du critère de recherche")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    public List<Facet> facets(@RequestParam @ApiParam(name = "q", value = "début de la chaine à rechercher", example = "rousseau") final String q,
                              @RequestParam @ApiParam(name = "index", value = "nom de l'index à réquêter", example = "personnes") final String index) throws Exception {
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
    @ApiOperation(
            value = "Rechercher une personne par son identifiant",
            notes = "Retourne la personne correspondante à la recherche")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    public PersonneResponseDto rechercherParIdentifiant(@PathVariable final String id,
                                                        @RequestParam @ApiParam(name = "index", value = "nom de l'index à réquêter", example = "personnes") final String index) throws ApiException {
        log.debug("Rechercher une personne par son identifiant...");
        try {
            return searchQueryBuilder.rechercherParIdentifiant(id, index);

        } catch (Exception e) {
            log.error(e.toString());
            throw new ApiException(e.getLocalizedMessage());
        }
    }

}
