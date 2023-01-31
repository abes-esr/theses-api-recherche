package fr.abes.thesesapirecherche.theses.controller;

import fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.theses.dto.ResponseTheseLiteDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    public ResponseTheseLiteDto simple(
            @RequestParam @ApiParam(name = "q", value = "chaine à rechercher", example = "technologie") final String q,
            @RequestParam @ApiParam(name = "debut", value = "indice de la première thèse du lot", example = "10") Optional<Integer> debut,
            @RequestParam @ApiParam(name = "nombre", value = "nombre de thèse du lot", example = "10") Optional<Integer> nombre,
        @RequestParam @ApiParam(name = "tri", value = "Type de tri", example = "dateAsc, dateDesc, auteursAsc, auteursDesc, disciplineAsc, discplineDesc") Optional<String> tri,
            @RequestParam @ApiParam(name = "filtres", value = "filtres", example = "soutenue, accessible") Optional<String> filtres
            ) throws Exception {
        log.info("debut de rechercheSurLeTitre...");
        try {
            String chaine = remplaceEtOuSauf(q);
            return searchQueryBuilder.simple(chaine, debut.orElse(0), nombre.orElse(10), tri.orElse(""), filtres.orElse(""));
        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }

    public String remplaceEtOuSauf(String q) {
        q = remplacerMotsCles(q, q,"ET", "AND", 0);
        q = remplacerMotsCles(q, q,"OU", "OR", 0);
        q = remplacerMotsCles(q, q,"SAUF", "NOT", 0);

        return q;
    }

    /**
     * Remplace les motCle par les nouveauMotCle dans q, s'ils ne sont pas entre guillemets
     */
    private static String remplacerMotsCles(String q, String ref, String motCle, String nouveauMotCle, int limit) {
        limit++;
        if (limit>1000) {
            return q;
        }
        int index = q.indexOf(" " + motCle + " ");
        if (index >= 0) {
            int indexRef = index + (ref.length() - q.length());
            long nombreGuillemets = ref.substring(0, indexRef).chars().filter(s -> s == '\"').count();
            if (nombreGuillemets % 2 == 0) {
                q = q.substring(0, index) + " " + nouveauMotCle + " " + q.substring(index + motCle.length() + 2);
                return q.substring(0, index + 2) +
                        remplacerMotsCles(q.substring(index + 2), ref, motCle, nouveauMotCle, limit);
            } else {
                int indexProchainGuillemets = q.substring(index).indexOf("\"") + index + 1;
                return q.substring(0, indexProchainGuillemets) +
                 remplacerMotsCles(q.substring(indexProchainGuillemets), ref, motCle, nouveauMotCle, limit);
            }
        }
        return q;
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
    public Map<String, Long> facets(@RequestParam final String q) throws Exception {
        return searchQueryBuilder.facets(q);
     }
}
