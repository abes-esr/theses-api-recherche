package fr.abes.thesesapirecherche.theses.service;

import fr.abes.thesesapirecherche.exception.RecaptchaInvalidException;
import fr.abes.thesesapirecherche.theses.data.DbRequests;
import fr.abes.thesesapirecherche.theses.dto.CaptchaResponseDto;
import fr.abes.thesesapirecherche.theses.dto.SignalerErreurDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class MoviesService {
    private final Environment env;
    private final DbRequests dbRequests;
    private final CaptchaService captchaService;
    @Value("${theses.mail}")
    private String mailTheses;
    @Value("${google.recaptcha.key.threshold}")
    private float threshold;

    // 9 characters: (8 chiffres) et (un x en ignorant la casse ou un chiffre)
    private static final Pattern VALID_PPN = Pattern.compile("^[0-9]{8}[xX0-9]$");

    public List<String> getMailAdressesFromMovies(SignalerErreurDto json) throws RecaptchaInvalidException {
        if (!VALID_PPN.matcher(json.getEtabPpn()).matches()) {
            throw new IllegalArgumentException(String.format("Invalid query value (%s). Must 9 digits long. Last digit can be an X", json.getEtabPpn()));
        }

        /**
         * GESTION DU CAPTCHA GOOGLE
         */
        //requête à l'API google
        Pair<CaptchaResponseDto, RestTemplate> returnValues = captchaService.getCaptcha(json);
        CaptchaResponseDto captchaResponseDto = returnValues.getLeft();
        RestTemplate restTemplate = returnValues.getRight();

        if (!captchaResponseDto.isSuccess() || captchaResponseDto.getScore() < threshold) {
            throw new RecaptchaInvalidException("reCaptcha non valide");
        }

        /**
         * SI CAPTCHA OK, ON PASSE A LA SUITE
         * ENVOI DU MAIL A LA VRAIE ADRESSE EN PROD ET TEST
         */

        if (Arrays.asList(env.getActiveProfiles()).contains("prod") || Arrays.asList(env.getActiveProfiles()).contains("test") || Arrays.asList(env.getActiveProfiles()).contains("localhost")) {

            URI uri = UriComponentsBuilder.fromUriString(env.getProperty("movies.assistance_deportee.url"))
                    .replaceQuery(null) // raz des query parameter
                    .queryParam("ppnEtab", "{preparedValue}")
                    .encode()
                    .buildAndExpand(json.getEtabPpn())
                    .toUri();

            Map<String, Object> response = restTemplate.getForObject(uri, Map.class);
            if (response == null) {
                log.error("Movies a mal répondu ! ");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne est survenue");
            }

            List<Map<String, Map<String, String>>> bindings = (List<Map<String, Map<String, String>>>) ((Map<String, Object>) response.get("results")).get("bindings");

            // Extraction du tableau de "ppnEtabCible"
            try {
                return bindings.stream()
                        .flatMap(binding -> {
                            if (binding.get("ppnEtabCible") != null) {
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
