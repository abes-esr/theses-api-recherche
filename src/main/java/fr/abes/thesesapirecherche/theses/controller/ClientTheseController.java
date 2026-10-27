package fr.abes.thesesapirecherche.theses.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.theses.dto.client.ClientResponseTheseLiteDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientThesesByOrganismeResponseDto;
import fr.abes.thesesapirecherche.theses.service.ThesesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/client/theses")
public class ClientTheseController {


    final SearchQueryBuilder searchQueryBuilder;
    

    @Autowired 
    private ThesesService thesesService;

    public ClientTheseController(SearchQueryBuilder searchQueryBuilder) {
        this.searchQueryBuilder = searchQueryBuilder;
    }

    

    // endpoint pour les clients (ceux qui utilisent l'API directement)
    @GetMapping(value = "/these/{id}")
    @Operation(
            summary = "Renvoyer une thèse à partir de son nnt",
            description = "Retourne la thèse correspondante au nnt")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public ClientTheseResponseDto getTheseClient(@PathVariable final String id) throws Exception {
        log.info("debut de getThese...");
        try {
            return thesesService.getTheseClient(id);

        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }


    // retourne un ensemble de thèses qui matchent avec les filtres (pour les clients)
    @GetMapping(value = "/recherche/")
    @Operation(
            summary = "Rechercher une thèse via le titre",
            description = "Retourne une liste de thèses correspondant à la recherche")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public ClientResponseTheseLiteDto rechercheThesesClient(
            @RequestParam @Parameter(name = "q", description = "chaine à rechercher", example = "technologie") final String q,
            @RequestParam @Parameter(name = "debut", description = "indice de la première thèse du lot", example = "10") Optional<Integer> debut,
            @RequestParam @Parameter(name = "nombre", description = "nombre de thèse du lot", example = "10") Optional<Integer> nombre,
            @RequestParam @Parameter(name = "tri", description = "Type de tri", example = "dateAsc, dateDesc, auteursAsc, auteursDesc, disciplineAsc, discplineDesc") Optional<String> tri,
            @RequestParam @Parameter(name = "filtres", description = "filtres", example = "[discipline=\"arts (histoire, theorie, pratique)\"&discipline=\"etudes germaniques\"&discipline=\"architecture\"&langues=\"fr\"]") Optional<String> filtres
    ) throws Exception {
        try {
            if(nombre.orElse(10) > 100000) nombre = Optional.of(100000);
            return thesesService.getThesesClient(q, debut.orElse(0), nombre.orElse(10), tri.orElse(""), filtres.orElse(""));
        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }


    //retourne un ensemble de thèses par organisme (pour les clients)
    @GetMapping(value = "/organisme/{ppn}")
    @Operation(
            summary = "Rechercher toutes les thèses liées à un établissement/organisme",
            description = "Retourne une liste de thèses correspondant liées à un établissement, groupées par rôle de l'établissement")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public ClientThesesByOrganismeResponseDto rechercheParOrganismeClient(
            @PathVariable @Parameter(name = "ppn", description = "PPN de l'établissement/organisme", example = "241345251") final String ppn
    ) throws Exception {
        try {
            return thesesService.getThesesByOrganismeClient(ppn);
        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }


}
