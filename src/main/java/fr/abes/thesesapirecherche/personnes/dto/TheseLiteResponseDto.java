package fr.abes.thesesapirecherche.personnes.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO web retournée par l'API ppour un ensemble de thèses en lien avec une personne
 */
@Builder
@Getter
@Setter
public class TheseLiteResponseDto {
    String id;
    String role;
    String discipline;
    @JsonProperty("etablissement_soutenance")
    // TODO: utiliser un DTO à la place du model
    EtablissementRefDto etablissementSoutenance;
    // TODO: vérifier quelle requêtes utilise ce dto et vérifier dans le front si ils ont garder la faut de frappe ou pas
    @JsonProperty("etabliseements_cotutelle")
    List<EtablissementRefDto> etablissementsCotutelle = new ArrayList<>();
}
