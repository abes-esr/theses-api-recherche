package fr.abes.thesesapirecherche.theses.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ThesesByOrganismeResponseDto {
    private long totalHitsetabSoutenance;
    private List<TheseLiteResponseDto> etabSoutenance;
    private long totalHitsetabSoutenanceEnCours;
    private List<TheseLiteResponseDto> etabSoutenanceEnCours;
    private long totalHitspartenaireRecherche;
    private List<TheseLiteResponseDto> partenaireRecherche;
    private long totalHitspartenaireRechercheEnCours;
    private List<TheseLiteResponseDto> partenaireRechercheEnCours;
    private long totalHitsetabCotutelle;
    private List<TheseLiteResponseDto> etabCotutelle;
    private long totalHitsetabCotutelleEnCours;
    private List<TheseLiteResponseDto> etabCotutelleEnCours;
    private long totalHitsecoleDoctorale;
    private List<TheseLiteResponseDto> ecoleDoctorale;
    private long totalHitsecoleDoctoraleEnCours;
    private List<TheseLiteResponseDto> ecoleDoctoraleEnCours;
}
