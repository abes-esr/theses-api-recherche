package fr.abes.thesesapirecherche.theses.controller;

import fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.theses.dto.TheseResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/theses")
public class TheseController {

    final
    SearchQueryBuilder searchQueryBuilder;

    public TheseController(SearchQueryBuilder searchQueryBuilder) {
        this.searchQueryBuilder = searchQueryBuilder;
    }

    @GetMapping(value = "/these/{id}")
    @ApiOperation(
            value = "Renvoyer une thèse à partir de son nnt",
            notes = "Retourne la thèse correspondante au nnt")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès"),
            @ApiResponse(code = 400, message = "Mauvaise requête"),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
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
