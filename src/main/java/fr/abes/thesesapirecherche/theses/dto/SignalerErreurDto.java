package fr.abes.thesesapirecherche.theses.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignalerErreurDto {
    String token;
    String url;
    String nom;
    String prenom;
    String mail;
    String domaine;
    String objet;
    String question;
    String appSource;
    String etabPpn;
}
