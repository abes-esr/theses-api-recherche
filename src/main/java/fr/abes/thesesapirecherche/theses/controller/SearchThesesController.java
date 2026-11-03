package fr.abes.thesesapirecherche.theses.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import fr.abes.thesesapirecherche.theses.repository.Status;
import fr.abes.thesesapirecherche.theses.repository.TheseRepository;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

import fr.abes.thesesapirecherche.dto.Facet;
import fr.abes.thesesapirecherche.theses.dto.ResponseTheseLiteDto;
import fr.abes.thesesapirecherche.theses.dto.ThesesByOrganismeResponseDto;
import fr.abes.thesesapirecherche.theses.rss.RssFeedView;
import fr.abes.thesesapirecherche.theses.service.ThesesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/theses/")
@Validated
@AllArgsConstructor
public class SearchThesesController {

    private RssFeedView view;
    private ThesesService thesesService;
    private TheseRepository theseRepository;

    // retourne un ensemble de thèses qui matchent avec les filtres (pour le front)
    @GetMapping(value = "/recherche/")
    @Operation(
            summary = "Recherche les thèses grâce à un moteur de recherche",
            description = """
                    Recherche les thèses sur Elasticsearch à l'aide de mot clé exacte sur de nombreux champs tel que : le résumé, le titre et les sujets, etc ...\n
                    La requête accepte l'utilisation d'opérateur logique écrit en toute lettre majuscule AND mais pas + ou le pipe |.
                    Les espaces sont considérés comme des AND par défaut.
                    Si q="quick AND brown", dans les résultats de la recherche, il y aura exactement avec la même orthographe les mots quick et brown dans un des champs d'une thèse.
                    La documentation Elasticsearch complète peut être consulté aux adresses suivantes :
                    - https://www.elastic.co/guide/en/elasticsearch/reference/5.2/query-dsl-query-string-query.html
                    - https://github.com/elastic/elasticsearch/blob/master/docs/reference/query-dsl/query-string-syntax.asciidoc
                    """
    )
    public ResponseTheseLiteDto rechercheThese(
            @RequestParam @Parameter(name = "q", description = "chaine à rechercher", example = "technologie") final String q,
            @RequestParam @Min(0) @Parameter(name = "debut", description = "indice de la première thèse du lot", example = "10") int debut,
            @RequestParam(defaultValue = "10") @Min(0) @Max(100000) @Parameter(name = "nombre", description = "nombre de thèse du lot", example = "10") int nombre,
            @RequestParam(defaultValue = "") @Parameter(name = "tri", description = "Type de tri", example = "dateAsc, dateDesc, auteursAsc, auteursDesc, disciplineAsc, discplineDesc") String tri,
            @RequestParam(defaultValue = "") @Parameter(name = "filtres", description = "filtres", example = "[discipline=\"arts (histoire, theorie, pratique)\"&discipline=\"etudes germaniques\"&discipline=\"architecture\"&langues=\"fr\"]") String filtres
    ) {
        return thesesService.recherche(q, debut, nombre, tri, filtres);
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
            @RequestParam(defaultValue = "") @Parameter(name = "tri", description = "Type de tri", example = "dateAsc, dateDesc, auteursAsc, auteursDesc, disciplineAsc, discplineDesc") String tri,
            @RequestParam(defaultValue = "") @Parameter(name = "filtres", description = "filtres", example = "[discipline=\"arts (histoire, theorie, pratique)\"&discipline=\"etudes germaniques\"&discipline=\"architecture\"&langues=\"fr\"]") String filtres
    ) {
        try {
            String csvContent = "\uFEFF" + thesesService.rechercheToCsv(q, 0, 10000, tri, filtres);

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

    @GetMapping(value = "/getorganismename/{ppn}", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Obtenir le nom d'un organisme",
            description = "Retourne le nom d'un organisme. Si ce nom est vide, alors ce PPN ne correspond pas à un organisme, mais à une personne.")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public String isOrganisme(
            @PathVariable @Parameter(name = "ppn", description = "PPN de l'établissement/organisme", example = "241345251") @NonNull final String ppn
    ) {
        Objects.requireNonNull(ppn, "ppn must not be null");
        return thesesService.getOrganismeName(ppn);
    }

    //retourne un ensemble de thèses par organisme (pour le front)
    @GetMapping(value = "/organisme/{ppn}")
    @Operation(
            summary = "Rechercher toutes les thèses liées à un établissement/organisme",
            description = "Retourne une liste de thèses correspondant liées à un établissement, groupées par rôle de l'établissement")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public ThesesByOrganismeResponseDto rechercheParOrganisme(
            @PathVariable @Parameter(name = "ppn", description = "PPN de l'établissement/organisme", example = "241345251") final String ppn
    ) {
        return thesesService.getTheseSummariesByOrganisme(ppn);
    }



    @GetMapping(value = "/completion/")
    @Operation(
            summary = "Proposer l'autocomplétion basée sur les mots clés libres, les mots clés rameau et la discipline",
            description = "Retourne 10 propositions")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public List<String> completion(
            @RequestParam @Parameter(name = "q", description = "début de la chaine à rechercher", example = "indus") final String q) throws IOException, ElasticsearchException {
        log.info("debut de completion...");
        return thesesService.completion(q);
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
        return theseRepository.facets(q, filtres.orElse(""));
    }

    @GetMapping(value = "/statsTheses")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    @Operation(
            summary = "Retourne le nombre de theses soutenues")
    public long statsTheses() throws Exception {
        return thesesService.getStatsTheses(Status.SOUTENUE);
    }

    @GetMapping(value = "/statsSujets")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    @Operation(
            summary = "Retourne le nombre de theses en préparation")
    public long statsSujets() throws Exception {
        return thesesService.getStatsTheses(Status.EN_COURS);
    }

    @GetMapping(value = "/rss")
    public View rss() {
        return view;
    }
}