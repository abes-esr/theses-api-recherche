package fr.abes.thesesapirecherche.theses.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ThesesByOrganismeResponseDto {
    private List<TheseLiteResponseDto> etabSoutenance;
    private List<TheseLiteResponseDto> partenaireRecherche;
    private List<TheseLiteResponseDto> etabCotutelle;
}
