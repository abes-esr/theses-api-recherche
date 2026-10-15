package fr.abes.thesesapirecherche.theses.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SujetRefDto {
    private String langue;
    private String libelle;
}
