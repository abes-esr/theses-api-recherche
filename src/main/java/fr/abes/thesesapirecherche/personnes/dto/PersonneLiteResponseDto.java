package fr.abes.thesesapirecherche.personnes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.abes.thesesapirecherche.theses.dto.PersonnesTheseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    @JsonProperty("theses")
    List<PersonnesTheseResponseDto> theses;
}
