package fr.abes.thesesapirecherche.theses.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.theses.dto.batch.BatchResponseTheseDto;
import fr.abes.thesesapirecherche.theses.service.ThesesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/batch/")
public class BatchTheseController {


    final SearchQueryBuilder searchQueryBuilder;
    

    @Autowired 
    private ThesesService thesesService;

    public BatchTheseController(SearchQueryBuilder searchQueryBuilder) {
        this.searchQueryBuilder = searchQueryBuilder;
    }

    

    // retourne un ensemble de thèses qui matchent avec les filtres (pour les clients)
    @GetMapping(value = "theses/recherche/")
    @Operation(
            summary = "Rechercher une thèse via le titre",
            description = "Retourne une liste de thèses correspondant à la recherche")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public BatchResponseTheseDto rechercheThesesBatch(
            @RequestParam @Parameter(name = "q", description = "chaine à rechercher", example = "technologie") final String q,
            @RequestParam @Parameter(name = "debut", description = "indice de la première thèse du lot", example = "10") Optional<Integer> debut,
            @RequestParam @Parameter(name = "nombre", description = "nombre de thèse du lot", example = "10") Optional<Integer> nombre,
            @RequestParam @Parameter(name = "tri", description = "Type de tri", example = "dateAsc, dateDesc, auteursAsc, auteursDesc, disciplineAsc, discplineDesc") Optional<String> tri,
            @RequestParam @Parameter(name = "filtres", description = "filtres", example = "[discipline=\"arts (histoire, theorie, pratique)\"&discipline=\"etudes germaniques\"&discipline=\"architecture\"&langues=\"fr\"]") Optional<String> filtres
    ) throws Exception {
        try {
            if(nombre.orElse(10) > 100000) nombre = Optional.of(100000);
            return thesesService.getThesesBatch(q, debut.orElse(0), nombre.orElse(10), tri.orElse(""), filtres.orElse(""));
        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }


}
