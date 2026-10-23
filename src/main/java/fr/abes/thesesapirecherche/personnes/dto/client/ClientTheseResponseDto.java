package fr.abes.thesesapirecherche.personnes.dto.client;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.abes.thesesapirecherche.personnes.dto.EtablissementResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.SujetRameauResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.ThesePersonneLiteResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour une thèse en lien avec une personne
 */
@Builder
@Getter
@Setter
public class ClientTheseResponseDto {
    String id;
    String nnt;
    String role;
    String titre;
    Map<String, String> titres;
    @JsonProperty("sujets_rameau")
    List<SujetRameauResponseDto> sujetsRameau;
    Map<String, List<String>> sujets;
    String discipline;
    String dateSoutenance;
    String dateInscription;
    @JsonProperty("etablissement_soutenance")
    EtablissementResponseDto etablissementSoutenance;
    @JsonProperty("etablissements_cotutelle")
    List<EtablissementResponseDto> etablissementsCotutelle;
    String status;
    String source;
    List<String> oaiSetNames;
    List<ThesePersonneLiteResponseDto> auteurs;
    List<ThesePersonneLiteResponseDto> directeurs;

    
    // TODO
    //Les champs suivants ne sont pas indexés et sont récupérés sur l'index theses avec une seconde requête

    List<String> langues;
    String accessible;
    @JsonProperty("codeEtab")
    String codeEtab;
    @JsonProperty("dateCines")
    String dateCines;
    @JsonProperty("numSujetSansS")
    String numSujetSansS;
    String doi;
    String cas;

    // TODO : extract cette fonction vers une librairie ou un service
    @JsonIgnore
    public String getDate_soutenanceTri() {
        if (dateSoutenance != null) {
            return dateSoutenance;
        } else if (dateInscription != null) {
            // TODO investiguer ce chiffre magique
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
