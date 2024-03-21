package fr.abes.thesesapirecherche.theses.controller;

import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.theses.dto.ResponseTheseLiteDto;
import fr.abes.thesesapirecherche.theses.dto.ThesesByOrganismeResponseDto;
import fr.abes.thesesapirecherche.theses.rss.RssFeedView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/theses/")
public class SearchThesesController {

    @Autowired
    SearchQueryBuilder searchQueryBuilder;

    @Autowired
    private RssFeedView view;

    @GetMapping(value = "/recherche/")
    @Operation(
            summary = "Rechercher une thèse via le titre",
            description = "Retourne une liste de thèses correspondant à la recherche")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public ResponseTheseLiteDto simple(
            @RequestParam @Parameter(name = "q", description = "chaine à rechercher", example = "technologie") final String q,
            @RequestParam @Parameter(name = "debut", description = "indice de la première thèse du lot", example = "10") Optional<Integer> debut,
            @RequestParam @Parameter(name = "nombre", description = "nombre de thèse du lot", example = "10") Optional<Integer> nombre,
            @RequestParam @Parameter(name = "tri", description = "Type de tri", example = "dateAsc, dateDesc, auteursAsc, auteursDesc, disciplineAsc, discplineDesc") Optional<String> tri,
            @RequestParam @Parameter(name = "filtres", description = "filtres", example = "[discipline=\"arts (histoire, theorie, pratique)\"&discipline=\"etudes germaniques\"&discipline=\"architecture\"&langues=\"fr\"]") Optional<String> filtres
    ) throws Exception {
        log.info("debut de rechercheSurLeTitre...");
        try {
            return searchQueryBuilder.simple(q, debut.orElse(0), nombre.orElse(10), tri.orElse(""), filtres.orElse(""));
        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }

    @GetMapping(value = "/rechercheCSV")
    @Operation(
            summary = "Rechercher une thèse via le titre et exporter le résultat dans un fichier CSV",
            description = "Retourne une liste de thèses correspondant à la recherche au format CSV")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public ResponseEntity<ByteArrayResource> simpleToCSV(
            @RequestParam @Parameter(name = "q", description = "chaine à rechercher", example = "technologie") final String q,
            @RequestParam @Parameter(name = "tri", description = "Type de tri", example = "dateAsc, dateDesc, auteursAsc, auteursDesc, disciplineAsc, discplineDesc") Optional<String> tri,
            @RequestParam @Parameter(name = "filtres", description = "filtres", example = "[discipline=\"arts (histoire, theorie, pratique)\"&discipline=\"etudes germaniques\"&discipline=\"architecture\"&langues=\"fr\"]") Optional<String> filtres
    ) throws Exception {
        try {
            String csvContent = "\uFEFF" + searchQueryBuilder.simpleToFullThese(q, 0, 10000, tri.orElse(""), filtres.orElse("")).toCSV();

            byte[] csvBytes = csvContent.getBytes(StandardCharsets.UTF_8);
            ByteArrayResource resource = new ByteArrayResource(csvBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export theses.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(resource);


        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }

    @GetMapping(value = "/getorganismename/{ppn}")
    @Operation(
            summary = "Obtenir le nom d'un organisme",
            description = "Retourne le nom d'un organisme. Si ce nom est vide, alors ce PPN ne correspond pas à un organisme, mais à une personne.")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public String isOrganisme(
            @PathVariable @Parameter(name = "ppn", description = "PPN de l'établissement/organisme", example = "241345251") final String ppn
    ) throws Exception {
        try {
            return searchQueryBuilder.getOrganismeName(ppn);
        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }

    @GetMapping(value = "/organisme/{ppn}")
    @Operation(
            summary = "Rechercher toutes les thèses liées à un établissement/organisme",
            description = "Retourne une liste de thèses correspondant liées à un établissement, groupées par rôle de l'établissement")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public ThesesByOrganismeResponseDto rechercheParOrganisme(
            @PathVariable @Parameter(name = "ppn", description = "PPN de l'établissement/organisme", example = "241345251") final String ppn
    ) throws Exception {
        try {
            return searchQueryBuilder.searchByOrganisme(ppn);
        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }

    @GetMapping(value = "/completion/")
    @Operation(
            summary = "Proposer l'automcompletion basée sur les mots clés libres, les mots clés rameau et la discipline",
            description = "Retourne 10 propositions")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public List<String> completion(
            @RequestParam @Parameter(name = "q", description = "début de la chaine à rechercher", example = "indus") final String q) throws Exception {
        log.info("debut de completion...");
        return searchQueryBuilder.completion(q);
    }

    @GetMapping(value = "/facets/")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    @Operation(
            summary = "Retourne une liste de facets/filtres pour une recherche simple")
    public List<Facet> facets(@RequestParam final String q,
                              @RequestParam @Parameter(name = "filtres", description = "filtres", example = "[discipline=\"arts (histoire, theorie, pratique)\"&discipline=\"etudes germaniques\"&discipline=\"architecture\"&langues=\"fr\"]") Optional<String> filtres
    ) throws Exception {
        return searchQueryBuilder.facets(q, filtres.orElse(""));
    }

    @GetMapping(value = "/statsTheses")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    @Operation(
            summary = "Retourne le nombre de theses soutenues")
    public long statsTheses() throws Exception {
        return searchQueryBuilder.getStatsTheses("soutenue");
    }

    @GetMapping(value = "/statsSujets")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    @Operation(
            summary = "Retourne le nombre de theses en préparation")
    public long statsSujets() throws Exception {
        return searchQueryBuilder.getStatsTheses("enCours");
    }

    @GetMapping(value = "/rss")
    public View rss() {
        return view;
    }
}
