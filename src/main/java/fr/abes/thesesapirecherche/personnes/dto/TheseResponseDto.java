package fr.abes.thesesapirecherche.personnes.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
    Map<String, String> titres = new HashMap<String, String>();
    @JsonProperty("sujets_rameau")
    List<SujetRameauResponseDto> sujetsRameau = new ArrayList<>();
    Map<String, List<String>> sujets = new HashMap<>();
    String discipline;
    Map<String, String> resumes = new HashMap<>();
    @JsonProperty("date_soutenance")
    String dateSoutenance;
    @JsonProperty("date_inscription")
    String dateInscription;
    @JsonProperty("etablissement_soutenance")
    EtablissementResponseDto etablissementSoutenance;
    @JsonProperty("etablissements_cotutelle")
    List<EtablissementResponseDto> etablissementsCotutelle = new ArrayList<>();
    String status;
    String source;
    List<ThesePersonneLiteResponseDto> auteurs = new ArrayList<>();
    List<ThesePersonneLiteResponseDto> directeurs = new ArrayList<>();

    // TODO: extraire ça dans un service
    @JsonIgnore
    public String getDate_soutenanceTri() {
        if (dateSoutenance != null) {
            return dateSoutenance;
        } else if (dateInscription != null) {
            // TODO: chiffre magique à enlever
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
