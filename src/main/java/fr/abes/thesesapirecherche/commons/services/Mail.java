package fr.abes.thesesapirecherche.commons.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Mail {

    public static String sendMail(String wsMailURL, List to, String mailTheses, String domaine, String url, String nom, String prenom, String mail, String objet, String question, String appSource) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        MailJSON mailJSON;
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Si on a trouvé un destinataire en BDD
        if (!to.isEmpty()) {
            String subject = "[thesesFr] -- " + objet + " -- Message de " + nom + " " + prenom;
            mailJSON = new MailJSON(to, new ArrayList<>() {{
                add(mail);
            }}, new ArrayList<>() {{
                add(mailTheses);
            }}, subject, MailTemplates.mailSignalerErreur(domaine, url, nom, prenom, mail, objet, question, appSource));
        }
        // Sinon on envoi un mail d'erreur à l'adresse theses en plus du mail de base
        else {
            //mail de base
            String subject = "[thesesFr] -- " + objet + " -- Message de " + nom + " " + prenom;
            mailJSON = new MailJSON(new ArrayList<>() {{
                add(mailTheses);
            }}, new ArrayList<>() {{
                add(mail);
            }}, new ArrayList<>() {
            }, subject, MailTemplates.mailSignalerErreur(domaine, url, nom, prenom, mail, objet, question, appSource));

            mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(mailJSON);

            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            HttpEntity<String> entity = new HttpEntity<String>(json, headers);
            restTemplate.postForObject(wsMailURL, entity, String.class);

            //mail d'erreur
            String subjectErr = "[thesesFr] -- Erreur d’adressage de l’assistance déportée theses.fr";
            mailJSON = new MailJSON(new ArrayList<>() {{
                add(mailTheses);
            }}, new ArrayList<>() {
            }, new ArrayList<>() {
            }, subjectErr, MailTemplates.mailSignalerErreurNoRecipient(domaine, url, nom, prenom, mail, objet, question, appSource));
        }

        mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(mailJSON);

        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        HttpEntity<String> entity = new HttpEntity<String>(json, headers);
        return restTemplate.postForObject(wsMailURL, entity, String.class);
    }


    public record MailJSON(List to, List cc, List cci, String subject, String text) {
    }
}
