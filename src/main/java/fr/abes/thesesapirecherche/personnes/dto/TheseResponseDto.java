package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO web retournée par l'API pour une thèse en lien avec une personne
 */
@SuperBuilder
@Getter
@Setter
public class TheseResponseDto {
    @JsonView({JsonViews.Normal.class})
    @JsonProperty("id")
    String id;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("role")
    String role;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("titre")
    String titre;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("titres")
    Map<String, String> titres;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("sujets_rameau")
    List<SujetRameauResponseDto> sujets_rameau;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("sujets")
    Map<String, List<String>> sujets;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("discipline")
    String discipline;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("resumes")
    Map<String, String> resumes;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("date_soutenance")
    String date_soutenance;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("date_inscription")
    String date_inscription;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("etablissement_soutenance")
    EtablissementResponseDto etablissement_soutenance;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("etablissements_cotutelle")
    List<EtablissementResponseDto> etablissements_cotutelle;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("status")
    String status;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("source")
    String source;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("auteurs")
    List<ThesePersonneLiteResponseDto> auteurs;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("directeurs")
    List<ThesePersonneLiteResponseDto> directeurs;


    @JsonView({JsonViews.Normal.class})
    @JsonProperty("oaiSetNames")
    List<String> oaiSetNames;


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
