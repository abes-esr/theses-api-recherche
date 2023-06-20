package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    @JsonProperty("id")
    String id;

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
    @JsonProperty("date_inscription")
    String date_inscription;

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

    @JsonIgnore
    public String getDate_soutenanceTri() {
        if (date_soutenance != null) {
            return date_soutenance;
        } else if (date_inscription != null) {
            // On ajoute 20 ans à la date pour mettre les thèses en préparation
            // avant les thèses soutenues
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(date_inscription);
            return date.plusYears(20).format(dateFormat);
        } else {
            // Au cas où, on met la date du jour
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return dateFormat.format(LocalDateTime.now());
        }
    }
}
