package fr.abes.thesesapirecherche.theses.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThesesByOrganismeDto {
    // TODO : breaking change, mettre le e de établissement en majuscule
    private long totalHitsetabSoutenance;
    private List<TheseLiteDto> etabSoutenance;
    private long totalHitsetabSoutenanceEnCours;
    private List<TheseLiteDto> etabSoutenanceEnCours;
    private long totalHitspartenaireRecherche;
    private List<TheseLiteDto> partenaireRecherche;
    private long totalHitspartenaireRechercheEnCours;
    private List<TheseLiteDto> partenaireRechercheEnCours;
    private long totalHitsetabCotutelle;
    private List<TheseLiteDto> etabCotutelle;
    private long totalHitsetabCotutelleEnCours;
    private List<TheseLiteDto> etabCotutelleEnCours;
    private long totalHitsecoleDoctorale;
    private List<TheseLiteDto> ecoleDoctorale;
    private long totalHitsecoleDoctoraleEnCours;
    private List<TheseLiteDto> ecoleDoctoraleEnCours;
}
