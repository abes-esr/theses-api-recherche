package fr.abes.thesesapirecherche.theses.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.abes.thesesapirecherche.commons.services.Mail;
import fr.abes.thesesapirecherche.exception.RecaptchaInvalidException;
import fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.theses.data.DbRequests;
import fr.abes.thesesapirecherche.theses.dto.CaptchaResponseDto;
import fr.abes.thesesapirecherche.theses.dto.SignalerErreurDto;
import fr.abes.thesesapirecherche.theses.dto.TheseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.elasticsearch.client.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/theses")
public class TheseController {
    final SearchQueryBuilder searchQueryBuilder;
    @Value("${google.recaptcha.key.site}")
    private String recaptchaSite;
    @Value("${google.recaptcha.key.secret}")
    private String recaptchaSecret;
    @Value("${google.recaptcha.key.threshold}")
    private float threshold;

    @Value("${theses.mail}")
    private String mailTheses;
    @Value("${mail.ws}")
    private String wsMailURL;

    @Autowired
    private DbRequests dbRequests;

    @Autowired
    private Environment env;

    public TheseController(SearchQueryBuilder searchQueryBuilder) {
        this.searchQueryBuilder = searchQueryBuilder;
    }

    @GetMapping(value = "/these/{id}")
    @Operation(
            summary = "Renvoyer une thèse à partir de son nnt",
            description = "Retourne la thèse correspondante au nnt")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public TheseResponseDto getThese(@PathVariable final String id) throws Exception {
        log.info("debut de getThese...");
        try {
            return searchQueryBuilder.rechercheSurId(id);

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
        /**
         * GESTION DU CAPTCHA GOOGLE
         */
        //requête à l'API google
        URI verifyUri = URI.create(String.format(
                "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s",
                recaptchaSecret, json.getToken()));

        //appel http via post
        RestTemplate restTemplate = new RestTemplate();
        CaptchaResponseDto captchaResponseDto = restTemplate.getForObject(verifyUri, CaptchaResponseDto.class);

        if (!captchaResponseDto.isSuccess() || captchaResponseDto.getScore() < threshold) {
            throw new RecaptchaInvalidException("reCaptcha non valide");
        }

        /**
         * SI CAPTCHA OK, ON PASSE A LA SUITE
         * ENVOI DU MAIL A LA VRAIE ADRESSE EN PROD ET TEST
         */

        List<String> to = getMailAdressesFromMovies(json, restTemplate);

        return Mail.sendMail(wsMailURL, to, mailTheses, json.getDomaine(), json.getUrl(), json.getNom(), json.getPrenom(), json.getMail(), json.getObjet(), json.getQuestion(), json.getAppSource());
    }

    private List<String> getMailAdressesFromMovies(SignalerErreurDto json, RestTemplate restTemplate) {
        URI uri = URI.create("https://movies.abes.fr/api-git/abes-esr/movies-api/subdir/v1//TH_assistance_deportee.json?ppnEtab=" + json.getEtabPpn());
        Map<String, Object> response = restTemplate.getForObject(uri, Map.class);
        if (response == null) {
            log.error("Movies a mal répondu ! ");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne est survenue");
        }

        // Extraction du tableau de "ppnEtabCible"
        List<Map<String, Map<String, String>>> bindings = (List<Map<String, Map<String, String>>>) ((Map<String, Object>) response.get("results")).get("bindings");

        if (Arrays.asList(env.getActiveProfiles()).contains("prod") || Arrays.asList(env.getActiveProfiles()).contains("test") || Arrays.asList(env.getActiveProfiles()).contains("localhost")) {
            try {



            return (List) bindings.stream()
                    .flatMap(binding -> { if (binding.get("ppnEtabCible") != null) {
                        return dbRequests.getMailAddress(binding.get("ppnEtabCible").get("value"), json.getAppSource()).stream();
                    } else {
                        return dbRequests.getMailAddress(json.getEtabPpn(), json.getAppSource()).stream();
                    }
                        })
                    .collect(Collectors.toList());
            } catch (Exception e) {
                log.error(e.toString());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Une erreur interne est survenue");
            }
        } else {
            return new ArrayList<>() {{
                add(mailTheses);
            }};
        }
    }
}
