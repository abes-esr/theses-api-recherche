package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * DTO web retournée par l'API pour un ensemble de personnes
 */
@Builder
public class PersonneLiteResponseDto {

    @Getter
    @Setter
    @JsonProperty("id")
    String id;

    @Getter
    @Setter
    @JsonProperty("nom")
    String nom;

    @Getter
    @Setter
    @JsonProperty("prenom")
    String prenom;

    @Getter
    @Setter
    @JsonProperty("has_idref")
    Boolean hasIdref;

    @Getter
    @Setter
    @JsonProperty("roles")
    Map<String,Integer> roles;

    @Getter
    @Setter
    @JsonProperty("these")
    TheseLiteResponseDto these;

    @Getter
    @Setter
    @JsonProperty("theses")
    @JsonInclude(value = JsonInclude.Include.CUSTOM,
            valueFilter = ThesesFilter.class)
    List<TheseLiteResponseDto> theses;

    @Getter
    @Setter
    @JsonProperty("disciplines")
    List<String> disciplines;

    @Getter
    @Setter
    @JsonProperty("etablissements")
    List<String> etablissements;
}
