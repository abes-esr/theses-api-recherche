package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
@Builder
@Getter
@Setter
public class TheseResponseDto {

    @JsonProperty("id")
    String id;


    @JsonProperty("role")
    String role;


    @JsonProperty("titre")
    String titre;


    @JsonProperty("titres")
    Map<String, String> titres = new HashMap<String, String>();


    @JsonProperty("sujets_rameau")
    List<SujetRameauResponseDto> sujets_rameau = new ArrayList<>();


    @JsonProperty("sujets")
    Map<String, List<String>> sujets = new HashMap<>();


    @JsonProperty("discipline")
    String discipline;


    @JsonProperty("resumes")
    Map<String, String> resumes = new HashMap<>();


    @JsonProperty("date_soutenance")
    String date_soutenance;


    @JsonProperty("date_inscription")
    String date_inscription;


    @JsonProperty("etablissement_soutenance")
    EtablissementResponseDto etablissement_soutenance;


    @JsonProperty("etablissements_cotutelle")
    List<EtablissementResponseDto> etablissements_cotutelle = new ArrayList<>();


    @JsonProperty("status")
    String status;


    @JsonProperty("source")
    String source;


    @JsonProperty("auteurs")
    List<ThesePersonneLiteResponseDto> auteurs = new ArrayList<>();


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
