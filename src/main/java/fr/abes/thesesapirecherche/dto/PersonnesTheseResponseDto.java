package fr.abes.thesesapirecherche.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API ppour un ensemble de thèses en lien avec une personne
 */
@Builder
public class PersonnesTheseResponseDto {
    @Getter
    @Setter
    @JsonProperty("nnt")
    String nnt;

    @Getter
    @Setter
    @JsonProperty("role")
    String role;
}
