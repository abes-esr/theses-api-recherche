package fr.abes.thesesapirecherche.personnes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.abes.thesesapirecherche.exception.ApiException;
import fr.abes.thesesapirecherche.personnes.dto.client.ClientPersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.service.PersonnesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/client/")
public class ClientPersonneController {

    @Autowired
    PersonnesService personnesService;


     /**
     * Recherche une personne à partir de son identifiant
     *
     * @param id Identifiant de la personne
     * @return Retourne la personne
     * @throws ApiException si la personne n'est pas trouvée
     */
    @GetMapping(value = "/personnes/personne/{id}")
    @Operation(
            summary = "Rechercher une personne par son identifiant",
            description = "Retourne la personne correspondante à la recherche")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")

    public ClientPersonneResponseDto rechercherParIdentifiant(@PathVariable final String id) throws ApiException {
        log.debug("Rechercher une personne par son identifiant...");
        try {
            return personnesService.getPersonneForClient(id);

        } catch (Exception e) {
            log.error(e.toString());
            throw new ApiException(e.getLocalizedMessage());
        }
    }
}
