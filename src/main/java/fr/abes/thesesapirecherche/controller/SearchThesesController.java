package fr.abes.thesesapirecherche.controller;

import fr.abes.thesesapirecherche.builder.SearchQueryBuilder;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/recherche/")
public class SearchThesesController {

    @Autowired
    SearchQueryBuilder searchQueryBuilder;

    @GetMapping(value = "/simple/")
    @ApiOperation(
            value = "Rechercher une thèse via le titre",
            notes = "Retourne une liste de thèses correspondant à la recherche")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    public String simple(
            @RequestParam @ApiParam(name="q", value = "chaine à rechercher", example = "technologie") final String q,
            @RequestParam @ApiParam(name="debut", value = "indice de la première thèse du lot", example = "10") Optional<Integer> debut,
            @RequestParam @ApiParam(name = "nombre", value = "nombre de thèse du lot", example = "10") Optional<Integer> nombre) throws Exception {
        log.info("debut de rechercheSurLeTitre...");
        try {
            if (debut.isPresent() && nombre.isPresent())
                return searchQueryBuilder.simple(q, debut.get(), nombre.get());
            else
                return searchQueryBuilder.simple(q, 0, 10);

        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }

    @GetMapping(value = "/completion/")
    @ApiOperation(
            value = "Proposer l'automcompletion basée sur les mots clés libres, les mots clés rameau et la discipline",
            notes = "Retourne 10 propositions")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    public List<String> completion(
            @RequestParam @ApiParam(name = "q", value = "début de la chaine à rechercher", example = "indus") final String q) throws Exception {
        log.info("debut de completion...");
        return searchQueryBuilder.completion(q);
    }
}
