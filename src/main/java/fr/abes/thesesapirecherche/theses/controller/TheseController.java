package fr.abes.thesesapirecherche.theses.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.abes.thesesapirecherche.commons.services.Mail;
import fr.abes.thesesapirecherche.exception.RecaptchaInvalidException;
import fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.theses.dto.CaptchaResponseDto;
import fr.abes.thesesapirecherche.theses.dto.SignalerErreurDto;
import fr.abes.thesesapirecherche.theses.dto.TheseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

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
    private JdbcTemplate jdbcTemplate;

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

    @PostMapping(value = "/these/signaler/")
    @Operation(summary = "Signaler une erreur sur une page de these", description = "Envoie un mail à l'établissement concerné, avec les informations saisies dans le formulaire")
    @ApiResponse(responseCode = "200", description = "Opération terminée avec succès")
    @ApiResponse(responseCode = "400", description = "Mauvaise requête")
    @ApiResponse(responseCode = "503", description = "Service indisponible")
    public String signalerErreur(@RequestBody SignalerErreurDto json) throws RecaptchaInvalidException, JsonProcessingException {

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
            throw new RecaptchaInvalidException("reCapatcha non valide");
        }

        /**
         * SI CAPTCHA OK, ON PASSE A LA SUITE
         * ENVOI DU MAIL A LA VRAIE ADRESSE EN PROD ET TEST
         */
        List to;

        if (Arrays.asList(env.getActiveProfiles()).contains("prod") || Arrays.asList(env.getActiveProfiles()).contains("test") || Arrays.asList(env.getActiveProfiles()).contains("localhost")) {
            to = getMailAddress(json.getEtabPpn(), json.getAppSource());
        } else {
            to = new ArrayList<>() {{
                add(mailTheses);
            }};
        }
        return Mail.sendMail(wsMailURL, to, mailTheses, json.getDomaine(), json.getUrl(), json.getNom(), json.getPrenom(), json.getMail(), json.getObjet(), json.getQuestion(), json.getAppSource());
    }

    private List getMailAddress(String ppnEtab, String source) {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        TimeZone.setDefault(timeZone);
        List<Map<String, Object>> res = jdbcTemplate.queryForList("SELECT EMAIL FROM COMPTE WHERE PPN = ? AND LOWER(SOURCE) = ?", ppnEtab, source.toLowerCase());
        List<String> to = new ArrayList<>();
        for (Map<String, Object> m : res
        ) {
            to.add(m.get("EMAIL").toString());
        }
        return to;
    }
}
