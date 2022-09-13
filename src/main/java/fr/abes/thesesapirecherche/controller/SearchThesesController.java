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
    public String rechercheSurLeTitre(@RequestParam final String q) throws Exception {
        log.info("debut de rechercheSurLeTitre...");
        try {
            return searchQueryBuilder.rechercheSurLeTitre(q);

        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }
}
