package fr.abes.thesesapirecherche.personnes.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour une thèse en lien avec une personne
 */
@Builder
@Getter
@Setter
public class TheseResponseDto {
    String id;
    String role;
    String titre;
    Map<String, String> titres;
    @JsonProperty("sujets_rameau")
    List<SujetRameauResponseDto> sujetsRameau;
    Map<String, List<String>> sujets;
    String discipline;
    Map<String, String> resumes;
    @JsonProperty("date_soutenance")
    String dateSoutenance;
    @JsonProperty("date_inscription")
    String dateInscription;
    @JsonProperty("etablissement_soutenance")
    EtablissementResponseDto etablissementSoutenance;
    @JsonProperty("etablissements_cotutelle")
    List<EtablissementResponseDto> etablissementsCotutelle;
    String status;
    String source;
    List<ThesePersonneLiteResponseDto> auteurs;
    List<ThesePersonneLiteResponseDto> directeurs;

    @JsonIgnore
    public String getDate_soutenanceTri() {
        if (dateSoutenance != null) {
            return dateSoutenance;
        } else if (dateInscription != null) {
            // On ajoute 20 ans à la date pour mettre les thèses en préparation
            // avant les thèses soutenues
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateInscription);
            return date.plusYears(20).format(dateFormat);
        } else {
            // Au cas où, on met la date du jour
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return dateFormat.format(LocalDateTime.now());
        }
    }
}
