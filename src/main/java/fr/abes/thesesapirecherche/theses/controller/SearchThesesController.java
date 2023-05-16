package fr.abes.thesesapirecherche.theses.controller;

import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.theses.dto.ResponseTheseLiteDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/theses/")
public class SearchThesesController {

    @Autowired
    SearchQueryBuilder searchQueryBuilder;

    @GetMapping(value = "/recherche/")
    @ApiOperation(
            value = "Rechercher une thèse via le titre",
            notes = "Retourne une liste de thèses correspondant à la recherche")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    public ResponseTheseLiteDto simple(
            @RequestParam @ApiParam(name = "q", value = "chaine à rechercher", example = "technologie") final String q,
            @RequestParam @ApiParam(name = "debut", value = "indice de la première thèse du lot", example = "10") Optional<Integer> debut,
            @RequestParam @ApiParam(name = "nombre", value = "nombre de thèse du lot", example = "10") Optional<Integer> nombre,
            @RequestParam @ApiParam(name = "tri", value = "Type de tri", example = "dateAsc, dateDesc, auteursAsc, auteursDesc, disciplineAsc, discplineDesc") Optional<String> tri,
            @RequestParam @ApiParam(name = "filtres", value = "filtres", example = "[discipline=\"arts (histoire, theorie, pratique)\"&discipline=\"etudes germaniques\"&discipline=\"architecture\"&langues=\"fr\"]") Optional<String> filtres
    ) throws Exception {
        log.info("debut de rechercheSurLeTitre...");
        try {
            return searchQueryBuilder.simple(q, debut.orElse(0), nombre.orElse(10), tri.orElse(""), filtres.orElse(""));
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

    @GetMapping(value = "/facets/")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    @ApiOperation(
            value = "Retourne une liste de facets/filtres pour une recherche simple")
    public List<Facet> facets(@RequestParam final String q,
                              @RequestParam @ApiParam(name = "filtres", value = "filtres", example = "[discipline=\"arts (histoire, theorie, pratique)\"&discipline=\"etudes germaniques\"&discipline=\"architecture\"&langues=\"fr\"]") Optional<String> filtres
    ) throws Exception {
        return searchQueryBuilder.facets(q, filtres.orElse(""));
    }

    @GetMapping(value = "/statsTheses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    @ApiOperation(
            value = "Retourne le nombre de theses soutenues")
    public long statsTheses() throws Exception {
        return searchQueryBuilder.getStatsTheses("soutenue");
    }

    @GetMapping(value = "/statsSujets")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    @ApiOperation(
            value = "Retourne le nombre de theses en préparation")
    public long statsSujets() throws Exception {
        return searchQueryBuilder.getStatsTheses("enCours");
    }
}
