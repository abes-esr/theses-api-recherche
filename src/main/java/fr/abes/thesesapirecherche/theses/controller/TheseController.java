package fr.abes.thesesapirecherche.theses.controller;

import java.io.IOException;
import java.util.List;

import fr.abes.thesesapirecherche.theses.service.MoviesService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.abes.thesesapirecherche.commons.services.Mail;
import fr.abes.thesesapirecherche.exception.RecaptchaInvalidException;
import fr.abes.thesesapirecherche.theses.data.DbRequests;
import fr.abes.thesesapirecherche.theses.dto.SignalerErreurDto;
import fr.abes.thesesapirecherche.theses.dto.TheseDto;
import fr.abes.thesesapirecherche.theses.service.ThesesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/theses")
@RequiredArgsConstructor
public class TheseController {

    @Value("${theses.mail}")
    private String mailTheses;
    @Value("${mail.ws}")
    private String wsMailURL;

    private final DbRequests dbRequests;
    private final ThesesService thesesService;
    private final MoviesService moviesService;

    // endpoint pour le front
    @GetMapping(value = "/these/{id}")
    @Operation(
            summary = "Renvoyer une thèse à partir de son nnt",
            description = "Retourne la thèse correspondante au nnt")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public TheseDto getThese(@PathVariable final String id) throws Exception {
        log.info("debut de getThese...");
        try {
            return thesesService.getThese(id);

        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }


    @GetMapping(value = "/checkNNT/{numSujet}")
    @Operation(
            summary = "Renvoyer le NNT d'une thèse à partir de son numSujet",
            description = "Si le numSujet (idStep) correspond également à une thèse soutenue et validée dans STAR, renvoi le NNT correspondant")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public String getNntIfExist(@PathVariable final String numSujet) throws Exception {
        try {
            return dbRequests.checkIfNNT(numSujet);
        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }

    @PostMapping(value = "/these/signaler/")
    @Operation(summary = "Signaler une erreur sur une page de these", description = "Envoie un mail à l'établissement concerné, avec les informations saisies dans le formulaire")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public String signalerErreur(@RequestBody SignalerErreurDto json) throws RecaptchaInvalidException, IOException {
        if (json.getEtabPpn() == null) {
            throw new IllegalArgumentException("""
                    Field "etabPpn" cannot be null
                    """);
        }

        List<String> to = moviesService.getMailAdressesFromMovies(json);

        log.info("Destinataires : " + to);
        return Mail.sendMail(wsMailURL, to, mailTheses, json.getDomaine(), json.getUrl(), json.getNom(), json.getPrenom(), json.getMail(), json.getObjet(), json.getQuestion(), json.getAppSource());
    }
}
