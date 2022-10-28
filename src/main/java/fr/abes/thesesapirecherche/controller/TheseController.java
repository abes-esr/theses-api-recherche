package fr.abes.thesesapirecherche.controller;

import fr.abes.thesesapirecherche.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.model.These;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
public class TheseController {

    final
    SearchQueryBuilder searchQueryBuilder;

    public TheseController(SearchQueryBuilder searchQueryBuilder) {
        this.searchQueryBuilder = searchQueryBuilder;
    }

    @GetMapping(value = "/these/{id}")
    public TheseResponseDto getThese(@PathVariable final String id) throws Exception {
        log.info("debut de getThese...");
        try {
            return searchQueryBuilder.rechercheSurId(id);

        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }

}
