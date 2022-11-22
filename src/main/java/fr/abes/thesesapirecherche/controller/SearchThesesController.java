package fr.abes.thesesapirecherche.controller;

import fr.abes.thesesapirecherche.builder.SearchQueryBuilder;
import io.swagger.annotations.ApiOperation;
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

    @GetMapping(value = "/titre/")
    @ApiOperation(
            value = "Rechercher une thèse via le titre",
            notes = "Retourne une liste de thèses correspondant à la recherche")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    public String rechercheSurLeTitre(@RequestParam final String q,
                                      @RequestParam Optional<Integer> page,
                                      @RequestParam Optional<Integer> nombre) throws Exception {
        log.info("debut de rechercheSurLeTitre...");
        try {
            if (page.isPresent() && nombre.isPresent())
                return searchQueryBuilder.rechercheSurLeTitre(q, page.get(), nombre.get());
            else
                return searchQueryBuilder.rechercheSurLeTitre(q, 0, 10);

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
    public List<String> completion(@RequestParam final String q) throws Exception {
        log.info("debut de completion...");
        return searchQueryBuilder.completion(q);
    }
}
