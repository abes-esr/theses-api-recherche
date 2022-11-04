package fr.abes.thesesapirecherche.controller;

import fr.abes.thesesapirecherche.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.model.These;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @GetMapping(value = "/recherche/these/{id}")
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
