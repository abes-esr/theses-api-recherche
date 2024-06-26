package fr.abes.thesesapirecherche.personnes.controller;

import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.exception.ApiException;
import fr.abes.thesesapirecherche.personnes.builder.SearchPersonneQueryBuilder;
import fr.abes.thesesapirecherche.personnes.dto.PersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.RechercheResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.SuggestionResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Value("${es.personnes.recherche.indexname}")
    private String esRechercheIndexName;

    /**
     * Rechercher une personne avec un mot
     *
     * @param q       Chaîne de caractère à rechercher
     * @param filtres Filtres des résultats
     * @param debut   Numéro de la page courante
     * @param nombre  Nombre de résultats à retourner
     * @return Une réponse de recherche
     * @throws ApiException
     */
    @GetMapping(value = "/recherche/")
    @Operation(
            summary = "Rechercher une personne avec un mot",
            description = "Retourne une liste de personnes correspondant à la recherche")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")

    public RechercheResponseDto recherche(
            @RequestParam @Parameter(name = "q", description = "début de la chaine à rechercher", example = "rousseau") final String q,
            @RequestParam @Parameter(name = "filtres", description = "filtres", example = "[role=\"auteurs\"&role=\"rapporteurs\"]") Optional<String> filtres,
            @RequestParam @Parameter(name = "debut", description = "indice de la première personne du lot", example = "10") Optional<Integer> debut,
            @RequestParam @Parameter(name = "nombre", description = "nombre de personne dans le lot", example = "10") Optional<Integer> nombre,
            @RequestParam @Parameter(name = "tri", description = "Type de tri", example = "pertinence, PersonneDesc, PersonnesAsc") Optional<String> tri
    ) throws Exception {
        if(nombre.orElse(10) > 100000) nombre = Optional.of(100000);
        String decodedQuery = URLDecoder.decode(q.replaceAll("\\+", "%2b"), StandardCharsets.UTF_8.toString());
        String decodedFilters = URLDecoder.decode(filtres.orElse(""), StandardCharsets.UTF_8.toString());
        log.debug("Rechercher une personne... : " + decodedQuery);
        return searchQueryBuilder.rechercher(decodedQuery, esRechercheIndexName, decodedFilters, debut.orElse(0), nombre.orElse(10), tri.orElse(""));
    }

    /**
     * Proposer l'autocompletion basée sur les noms et prénoms
     *
     * @param q Chaîne de caractère à compléter
     * @return Retourne 10 propositions avec une priorité sur les personnes avec un identifiant Idref
     * @throws Exception
     */
    @GetMapping(value = "/completion")
    @Operation(
            summary = "Proposer l'autocompletion basée sur les noms et prénoms",
            description = "Retourne 10 propositions avec une priorité sur les personnes avec un identifiant Idref")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public SuggestionResponseDto completion(
            @RequestParam @Parameter(name = "q", description = "début de la chaine à rechercher", example = "indus") final String q) throws Exception {
        String decodedQuery = URLDecoder.decode(q, StandardCharsets.UTF_8.toString());
        log.info("debut de completion...");
        return searchQueryBuilder.completion(decodedQuery, esRechercheIndexName);
    }

    /**
     * Retourne une liste de facettes avec le nombre d'occurence pour chaque facette
     *
     * @param q Chaîne de caractère à rechercher
     * @return Retourne une liste de facettes en fonction du critère de recherche
     * @throws Exception
     */
    @GetMapping(value = "/facets")
    @Operation(
            summary = "Retourne une liste de facettes avec le nombre d'ocurence pour chaque facette",
            description = "Retourne une liste de facettes en fonction du critère de recherche")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public List<Facet> facets(@RequestParam @Parameter(name = "q", description = "début de la chaine à rechercher", example = "rousseau") final String q,
                              @RequestParam @Parameter(name = "filtres", description = "filtres", example = "[role=\"auteurs\"&role=\"rapporteurs\"]") Optional<String> filtres) throws Exception {
        return searchQueryBuilder.facets(q, esRechercheIndexName, filtres.orElse(""));
    }

    /**
     * Recherche une personne à partir de son identifiant
     *
     * @param id Identifiant de la personne
     * @return Retourne la personne
     * @throws ApiException si la personne n'est pas trouvée
     */
    @GetMapping(value = "/personne/{id}")
    @Operation(
            summary = "Rechercher une personne par son identifiant",
            description = "Retourne la personne correspondante à la recherche")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")

    public PersonneResponseDto rechercherParIdentifiant(@PathVariable final String id) throws ApiException {
        log.debug("Rechercher une personne par son identifiant...");
        try {
            return searchQueryBuilder.rechercherParIdentifiant(id, esIndexName);

        } catch (Exception e) {
            log.error(e.toString());
            throw new ApiException(e.getLocalizedMessage());
        }
    }

    /**
     * Retourne le nombre total de personnes
     */
    @GetMapping(value = "/stats")
    @Operation(summary = "Statistiques sur les personnes",
            description = "Retourne le nombre total de personnes")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")

    public long getStatsPersonnes() throws Exception {
        return searchQueryBuilder.getStatsPersonnes(esRechercheIndexName);
    }

}
