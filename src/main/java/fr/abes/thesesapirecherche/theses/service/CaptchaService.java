package fr.abes.thesesapirecherche.theses.service;

import fr.abes.thesesapirecherche.theses.dto.CaptchaResponseDto;
import fr.abes.thesesapirecherche.theses.dto.SignalerErreurDto;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class CaptchaService {
    @Value("${google.recaptcha.key.secret}")
    private String recaptchaSecret;

    public Pair<CaptchaResponseDto, RestTemplate> getCaptcha(SignalerErreurDto json) {
        //requête à l'API google
        URI verifyUri = URI.create(String.format(
                "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s",
                recaptchaSecret, json.getToken()));

        //appel http via post
        RestTemplate restTemplate = new RestTemplate();
        return Pair.of(restTemplate.getForObject(verifyUri, CaptchaResponseDto.class), restTemplate);
    }
}
