package fr.abes.thesesapirecherche.controller;

import fr.abes.thesesapirecherche.builder.SearchPersonneQueryBuilder;
import fr.abes.thesesapirecherche.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.dto.PersonnesResponseDto;
import fr.abes.thesesapirecherche.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/personne/")
public class PersonneController {

    @Autowired
    SearchPersonneQueryBuilder searchQueryBuilder;

    /**
     * Recherche une personne avec un mot
     * @param q Chaîne de caractère à rechercher
     * @return Une liste des personnes correspondant à la recherche
     * @throws ApiException
     */
    @GetMapping(value = "/rechercher")
    public List<PersonnesResponseDto> recherche(@RequestParam final String q) throws ApiException {
        log.info("Rechercher une personne...");
        try {
            return searchQueryBuilder.rechercher(q);

        } catch (Exception e) {
            log.error(e.toString());
            throw new ApiException(e.getLocalizedMessage());
        }
    }

}
