package fr.abes.thesesapirecherche.theses.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ThesesByOrganismeResponseDto {
    // TODO: Mettre le e en majuscule mais ça va casser des trucs
    private long totalHitsetabSoutenance;
    private List<TheseLiteResponseDto> etabSoutenance = new ArrayList<>();
    private long totalHitsetabSoutenanceEnCours;
    private List<TheseLiteResponseDto> etabSoutenanceEnCours = new ArrayList<>();
    private long totalHitspartenaireRecherche;
    private List<TheseLiteResponseDto> partenaireRecherche = new ArrayList<>();
    private long totalHitspartenaireRechercheEnCours;
    private List<TheseLiteResponseDto> partenaireRechercheEnCours = new ArrayList<>();
    private long totalHitsetabCotutelle;
    private List<TheseLiteResponseDto> etabCotutelle = new ArrayList<>();
    private long totalHitsetabCotutelleEnCours;
    private List<TheseLiteResponseDto> etabCotutelleEnCours = new ArrayList<>();
    private long totalHitsecoleDoctorale;
    private List<TheseLiteResponseDto> ecoleDoctorale = new ArrayList<>();
    private long totalHitsecoleDoctoraleEnCours;
    private List<TheseLiteResponseDto> ecoleDoctoraleEnCours = new ArrayList<>();
}
