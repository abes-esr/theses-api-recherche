package fr.abes.thesesapirecherche.controller;

import fr.abes.thesesapirecherche.builder.SearchQueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/theses/v1")
public class SearchThesesController {

    @Autowired
    SearchQueryBuilder searchQueryBuilder;

    @GetMapping(value = "/searchTitle/{recherche}")
    public String searchTitle(@PathVariable final String recherche) throws Exception {
        log.info("debut de searchTitle...");
        try {
            return searchQueryBuilder.getThesesTitle(recherche);

        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }
}
