package fr.abes.thesesapirecherche.personnes.controller;

import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.exception.ApiException;
import fr.abes.thesesapirecherche.personnes.builder.SearchPersonneQueryBuilder;
import fr.abes.thesesapirecherche.personnes.dto.PersonneLiteResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.PersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.RechercheResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.SuggestionPersonneResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/personnes/")
public class PersonneController {

    @Autowired
    SearchPersonneQueryBuilder searchQueryBuilder;

    @Value("${es.personnes.indexname}")
    private String esIndexName;

    /**
     * Rechercher une personne avec un mot
     *
     * @param q       Chaîne de caractère à rechercher
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
            @RequestParam @ApiParam(name = "filtres", value = "filtres", example = "[role=\"auteurs\"&role=\"raporteurs\"]") Optional<String> filtres,
            @RequestParam @ApiParam(name = "debut", value = "indice de la première personne du lot", example = "10") Optional<Integer> debut,
            @RequestParam @ApiParam(name = "nombre", value = "nombre de personne dans le lot", example = "10") Optional<Integer> nombre
    ) throws Exception {
        String decodedQuery = URLDecoder.decode(q.replaceAll("\\+", "%2b"), StandardCharsets.UTF_8.toString());
        String decodedFilters = URLDecoder.decode(filtres.orElse(""), StandardCharsets.UTF_8.toString());
        log.debug("Rechercher une personne... : " + decodedQuery);
        return searchQueryBuilder.rechercher(decodedQuery, esIndexName, decodedFilters,debut.orElse(0), nombre.orElse(10));
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
        return searchQueryBuilder.completion(decodedQuery, esIndexName);
    }

    /**
     * Retourne une liste de facettes avec le nombre d'occurence pour chaque facette
     *
     * @param q Chaîne de caractère à rechercher
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
    public List<Facet> facets(@RequestParam @ApiParam(name = "q", value = "début de la chaine à rechercher", example = "rousseau") final String q) throws Exception {
        return searchQueryBuilder.facets(q, "personnes");
    }

    /**
     * Recherche une personne à partir de son identifiant
     *
     * @param id Identifiant de la personne
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
    public PersonneResponseDto rechercherParIdentifiant(@PathVariable final String id) throws ApiException {
        log.debug("Rechercher une personne par son identifiant...");
        try {
            return searchQueryBuilder.rechercherParIdentifiant(id,esIndexName);

        } catch (Exception e) {
            log.error(e.toString());
            throw new ApiException(e.getLocalizedMessage());
        }
    }

}
