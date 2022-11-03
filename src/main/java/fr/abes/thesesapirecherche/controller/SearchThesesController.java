package fr.abes.thesesapirecherche.controller;

import fr.abes.thesesapirecherche.builder.SearchQueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/recherche/")
public class SearchThesesController {

    @Autowired
    SearchQueryBuilder searchQueryBuilder;

    @GetMapping(value = "/titre/")
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
}
