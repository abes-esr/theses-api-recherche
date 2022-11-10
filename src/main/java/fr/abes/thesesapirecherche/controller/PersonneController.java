package fr.abes.thesesapirecherche.controller;

import fr.abes.thesesapirecherche.builder.SearchPersonneQueryBuilder;
import fr.abes.thesesapirecherche.dto.PersonneResponseDto;
import fr.abes.thesesapirecherche.dto.PersonnesResponseDto;
import fr.abes.thesesapirecherche.exception.ApiException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/personne/")
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
    @GetMapping(value = "/rechercher")
    @ApiOperation(
            value = "Rechercher une personne avec un mot",
            notes = "Retourne une liste de personnes correspondant à la recherche")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    public List<PersonnesResponseDto> recherche(@RequestParam final String q) throws ApiException {
        log.debug("Rechercher une personne...");
        try {
            return searchQueryBuilder.rechercher(q);

        } catch (Exception e) {
            log.error(e.toString());
            throw new ApiException(e.getLocalizedMessage());
        }
    }

    @GetMapping(value = "/rechercher/{id}")
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

}
