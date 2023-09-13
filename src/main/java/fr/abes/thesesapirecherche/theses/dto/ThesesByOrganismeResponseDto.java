package fr.abes.thesesapirecherche.theses.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ThesesByOrganismeResponseDto {
    private List<TheseLiteResponseDto> etabSoutenance;
    private List<TheseLiteResponseDto> etabSoutenanceEnCours;
    private List<TheseLiteResponseDto> partenaireRecherche;
    private List<TheseLiteResponseDto> partenaireRechercheEnCours;
    private List<TheseLiteResponseDto> etabCotutelle;
    private List<TheseLiteResponseDto> etabCotutelleEnCours;
    private List<TheseLiteResponseDto> ecoleDoctorale;
    private List<TheseLiteResponseDto> ecoleDoctoraleEnCours;
}
