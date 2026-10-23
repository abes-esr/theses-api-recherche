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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO web retournée par l'API pour une thèse en lien avec une personne
 */
@SuperBuilder
@Getter
@Setter
public class ClientTheseResponseDto {
    @JsonProperty("id")
    String id;

    @JsonProperty("nnt")
    String nnt;


    @JsonProperty("role")
    String role;


    @JsonProperty("titre")
    String titre;


    @JsonProperty("titres")
    Map<String, String> titres;


    @JsonProperty("sujets_rameau")
    List<SujetRameauResponseDto> sujets_rameau;


    @JsonProperty("sujets")
    Map<String, List<String>> sujets;


    @JsonProperty("discipline")
    String discipline;


    @JsonProperty("date_soutenance")
    String date_soutenance;


    @JsonProperty("date_inscription")
    String date_inscription;


    @JsonProperty("etablissement_soutenance")
    EtablissementResponseDto etablissement_soutenance;


    @JsonProperty("etablissements_cotutelle")
    List<EtablissementResponseDto> etablissements_cotutelle;


    @JsonProperty("status")
    String status;


    @JsonProperty("source")
    String source;


    @JsonProperty("oaiSetNames")
    List<String> oaiSetNames;


    @JsonProperty("auteurs")
    List<ThesePersonneLiteResponseDto> auteurs;


    @JsonProperty("directeurs")
    List<ThesePersonneLiteResponseDto> directeurs;

    

    //Les champs suivants ne sont pas indexés et sont récupérés sur l'index theses avec une seconde requête
    
    @JsonProperty("langues")
    List<String> langues;
    
    
    @JsonProperty("accessible")
    String accessible;
    
    
    @JsonProperty("codeEtab")
    String codeEtab;
    
    
    @JsonProperty("dateCines")
    String dateCines;
    
    
    @JsonProperty("numSujetSansS")
    String numSujetSansS;
    
    
    @JsonProperty("doi")
    String doi;
    
    
    @JsonProperty("cas")
    String cas;



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
