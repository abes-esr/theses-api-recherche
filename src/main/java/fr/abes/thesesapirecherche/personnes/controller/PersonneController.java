package fr.abes.thesesapirecherche.personnes.controller;

import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.exception.ApiException;
import fr.abes.thesesapirecherche.personnes.builder.SearchPersonneQueryBuilder;
import fr.abes.thesesapirecherche.personnes.dto.PersonneLiteResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.PersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.SuggestionPersonneResponseDto;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/personnes/")
public class PersonneController {

    @Autowired
    SearchPersonneQueryBuilder searchQueryBuilder;

    /**
     * Rechercher une personne avec un mot
     *
     * @param q Chaîne de caractère à rechercher
     * @return Une liste de personnes correspondant à la recherche
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
    public List<PersonneLiteResponseDto> recherche(@RequestParam final String q) throws Exception {
        String decodedQuery = URLDecoder.decode(q.replaceAll("\\+", "%2b"), StandardCharsets.UTF_8.toString());
        log.debug("Rechercher une personne... : " + decodedQuery);
        return searchQueryBuilder.rechercher(decodedQuery);
    }

    /**
     * Proposer l'autocompletion basée sur les noms et prénoms
     *
     * @param q Chaîne de caractère à compléter
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
    public List<SuggestionPersonneResponseDto> completion(
            @RequestParam @ApiParam(name = "q", value = "début de la chaine à rechercher", example = "indus") final String q) throws Exception {
        String decodedQuery = URLDecoder.decode(q, StandardCharsets.UTF_8.toString());
        log.info("debut de completion...");
        return searchQueryBuilder.completion(decodedQuery);
    }

    @GetMapping(value = "/personne/{id}")
    @ApiOperation(
            value = "Rechercher une personne par son identifiant",
            notes = "Retourne la personne correspondante à la recherche")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    public PersonneResponseDto rechercherParIdentifiant(@PathVariable final String id) throws ApiException {
        log.debug("Rechercher une personne par son identifiant...");
        try {
            return searchQueryBuilder.rechercherParIdentifiant(id);

        } catch (Exception e) {
            log.error(e.toString());
            throw new ApiException(e.getLocalizedMessage());
        }
    }

    @GetMapping(value = "/facets/")
    public List<Facet> facets(@RequestParam final String q) throws Exception {
        return searchQueryBuilder.facets(q);
    }

}
