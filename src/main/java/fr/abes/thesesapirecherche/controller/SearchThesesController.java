package fr.abes.thesesapirecherche.controller;

import fr.abes.thesesapirecherche.builder.SearchQueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
public class SearchThesesController {

    @Autowired
    SearchQueryBuilder searchQueryBuilder;

    @GetMapping(value = "/recherche/")
    public String rechercheSurLeTitre(@RequestParam final String q, @RequestParam final int page, @RequestParam final int nombre) throws Exception {
        log.info("debut de rechercheSurLeTitre...");
        try {
            return searchQueryBuilder.rechercheSurLeTitre(q, page, nombre);

        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }

    @GetMapping(value = "/recherche/titreEtResume/")
    public String rechercheSurLeTitreEtResume(@RequestParam final String q, @RequestParam final String rameau) throws Exception {
        log.info("debut de rechercheSurLeTitreEtResume...");
        try {
            return searchQueryBuilder.rechercheSurLeTitreEtResume(q, rameau);

        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }
}
