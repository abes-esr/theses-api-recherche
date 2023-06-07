package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * DTO web retournée par l'API pour une thèse en lien avec une personne
 */
@Builder
public class TheseResponseDto {
    @Getter
    @Setter
    @JsonProperty("nnt")
    String nnt;

    @Getter
    @Setter
    @JsonProperty("role")
    String role;

    @Getter
    @Setter
    @JsonProperty("titre")
    String titre;

    @Getter
    @Setter
    @JsonProperty("titres")
    Map<String, String> titres = new HashMap<String, String>();

    @Getter
    @Setter
    @JsonProperty("sujets_rameau")
    List<SujetRameauResponseDto> sujets_rameau = new ArrayList<>();

    @Getter
    @Setter
    @JsonProperty("sujets")
    Map<String, List<String>> sujets = new HashMap<>();

    @Getter
    @Setter
    @JsonProperty("discipline")
    String discipline;

    @Getter
    @Setter
    @JsonProperty("resumes")
    Map<String, String> resumes = new HashMap<>();

    @Getter
    @Setter
    @JsonProperty("date_soutenance")
    String date_soutenance;

    @Getter
    @Setter
    @JsonProperty("etablissement_soutenance")
    EtablissementResponseDto etablissement_soutenance;

    @Getter
    @Setter
    @JsonProperty("etablissements_cotutelle")
    List<EtablissementResponseDto> etablissements_cotutelle = new ArrayList<>();

    @Getter
    @Setter
    @JsonProperty("status")
    String status;

    @Getter
    @Setter
    @JsonProperty("source")
    String source;

    @Getter
    @Setter
    @JsonProperty("auteurs")
    List<ThesePersonneLiteResponseDto> auteurs = new ArrayList<>();

    @Getter
    @Setter
    @JsonProperty("directeurs")
    List<ThesePersonneLiteResponseDto> directeurs = new ArrayList<>();

    public String getDate_soutenanceTri() {
        if (date_soutenance != null) {
            return date_soutenance;
        } else {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return dateFormat.format(LocalDateTime.now());
        }
    }
}
